package org.ibit.rol.sac.micropersistence.ejb;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import net.sf.hibernate.*;

import org.ibit.lucene.indra.model.Catalogo;
import org.ibit.lucene.indra.model.ModelFilterObject;
import org.ibit.rol.sac.micromodel.Archivo;
import org.ibit.rol.sac.micromodel.Contenido;
import org.ibit.rol.sac.micromodel.Idioma;
import org.ibit.rol.sac.micromodel.IndexObject;
import org.ibit.rol.sac.micromodel.Menu;
import org.ibit.rol.sac.micromodel.TraduccionMenu;
import org.ibit.rol.sac.micromodel.TraduccionContenido;
import org.ibit.rol.sac.micropersistence.delegate.DelegateException;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;
import org.ibit.rol.sac.micropersistence.delegate.MenuDelegate;

import au.id.jericho.lib.html.Element;
import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.Tag;

/**
 * SessionBean para consultar Contenido.
 *
 * @ejb.bean
 *  name="sac/micropersistence/ContenidoFacade"
 *  jndi-name="org.ibit.rol.sac.micropersistence.ContenidoFacade"
 *  type="Stateless"
 *  view-type="remote"
 *  transaction-type="Container"
 *
 * @ejb.transaction type="Required"
 * 
 * @author Indra
 */
public abstract class ContenidoFacadeEJB extends HibernateEJB
{

	private static final long serialVersionUID = 1665804059893814046L;

	/**
     * @ejb.create-method
     * @ejb.permission unchecked="true"
     */
    public void ejbCreate() throws CreateException {
        super.ejbCreate();
        try {
        	super.langs= DelegateUtil.getIdiomaDelegate().listarLenguajes();
        }
        catch(Exception ex) {
        	throw new EJBException(ex);
        }
    }

    /**
     * Inicializo los parámetros de la consulta de Contenido.... 
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
    public void init(String menu) {
    	super.tampagina=10;
    	super.pagina=0;
    	super.select="";
    	super.from=" from Contenido contenido join contenido.traducciones trad ";
    	super.where=" where index(trad)='"+Idioma.DEFAULT+"' and contenido.idmenu="+menu;
    	super.whereini=" ";
    	super.orderby=" order by contenido.orden";

    	super.camposfiltro= new String[] {"contenido.fpublicacion","contenido.fcaducidad","trad.titulo","trad.texto","trad.txbeta","trad.url"};
    	super.cursor=0;
    	super.nreg=0;
    	super.npags=0;	
    }          
    
    /**
     * Inicializo los parámetros de la consulta de Contenido.... 
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
    public void init() {
    	super.tampagina=10;
    	super.pagina=0;
    	super.select="";
    	super.from=" from Contenido contenido join contenido.traducciones trad ";
    	super.where=" where index(trad)='"+Idioma.DEFAULT+"'";
    	super.whereini=" ";
    	super.orderby="";

    	super.camposfiltro= new String[] {"contenido.fpublicacion","contenido.fcaducidad","trad.titulo","trad.url"};
    	super.cursor=0;
    	super.nreg=0;
    	super.npags=0;	
    } 
    
    /**
     * Crea o actualiza un Contenido
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
     */
    public Long grabarContenido(Contenido contenido) {
        Session session = getSession();
        boolean nuevo=false;
        try {
            Transaction tx = session.beginTransaction();
            if (contenido.getId()==null) nuevo=true;
            session.saveOrUpdate(contenido);
            session.flush();
            tx.commit();
            
            return contenido.getId();
            
        } catch (HibernateException he) {
        	if (!nuevo) indexBorraContenido(contenido.getId());
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }

    /**
     * Obtiene un Contenido
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public Contenido obtenerContenido(Long id) {
        Session session = getSession();
        try {
        	Contenido contenido = (Contenido) session.load(Contenido.class, id);
            return contenido;
        } catch (ObjectNotFoundException oNe) {
        	log.error(oNe.getMessage());
        	return new Contenido();
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }

    /**
     * Comprueba si existe un contenido
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public boolean existeContenido(Long id) {
        Session session = getSession();
        try {
        	List<?> lista= session.find("from Contenido conte where conte.id="+id.toString());
            return (lista.size()>0);
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }
    
    /**
     * Lista todos los Contenidos
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
    public List<?> listarContenidos() {
        Session session = getSession();
        try {
        	parametrosCons(); // Establecemos los parámetros de la paginación
           	
        	Query query = session.createQuery(select+from+where+orderby);
            query.setFirstResult(cursor-1);
            query.setMaxResults(tampagina);
        	return query.list();
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }

    /**
     * Lista todos los contenidos poniendole un idioma por defecto
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
    public ArrayList<Contenido> listarContenidos(String idioma) {
        Session session = getSession();
        try {
        	parametrosCons(); // Establecemos los parámetros de la paginación
           	
        	Query query = session.createQuery(select+from+where+orderby);
            query.setFirstResult(cursor-1);
            query.setMaxResults(tampagina);
            return crearlistadostateful(query.list(),idioma);
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }     

    /**
     * borra un Contenido
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
     */
    public void borrarContenido(Long id) throws DelegateException {
        Session session = getSession();
        try {
        	Transaction tx = session.beginTransaction();
        	Contenido contenido = (Contenido) session.load(Contenido.class, id);
            session.delete(contenido);
            
            eliminarDocumentos(null, id.toString());
            //indexBorraContenido(contenido.getId());
            
            session.flush();
            tx.commit();
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }

    /**
     * metodo privado que devuelve un arraylist 'nuevo'.
     * Vuelca el contenido del listado e inicializa el idioma del bean.
     * @param listado
     * @param idioma
     * @return
     */
    private ArrayList<Contenido> crearlistadostateful(List<?> listado, String idioma) {
		ArrayList<Contenido> lista = new ArrayList<Contenido>();
    	Iterator<?> iter = listado.iterator();
	    Contenido contenido;
         while (iter.hasNext()) {
        	 contenido = new Contenido();
        	 contenido = (Contenido)iter.next();
        	 contenido.setIdi(idioma);
        	 lista.add(contenido);
         }
         return lista;
    }    

    /**
     * Elimina todos los documentos de la pagina
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
     */
    public void eliminarDocumentos(String micro, String pagina) throws DelegateException {
        Session session = getSession();
        try {
        	List<Archivo> lista = listarDocumentos(micro, pagina);
        	Iterator<Archivo> iter = lista.iterator();
        	Archivo archi;
        	while (iter.hasNext()) {
        		archi = (Archivo)iter.next();
            	session.delete(archi);
            }
        	session.flush();
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }
    
    /**
     * Devuelve el titulo del listado de contenidos, la miga de pan de donde se encuentra.
     * Podrá ser:
     *  Menu padre > Menu hijo > Contenido  o bien
     *  Menu padre > Contenido
     *  según exista idmenu y/o idcontenido
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
     */
    public String migapan(String idmenu, Long idcontenido) throws DelegateException {
    
    	String ret="";
    	String contnombre="";
    	if (idmenu.equals("")) return ret;
    	else 
    	{
    		
    		if (idcontenido!=null) {
    			Contenido cont=obtenerContenido(idcontenido);
    			if (((TraduccionContenido)cont.getTraduccion(Idioma.DEFAULT)).getTitulo()!=null)
    				contnombre=((TraduccionContenido)cont.getTraduccion(Idioma.DEFAULT)).getTitulo();
    		}
    		
   			MenuDelegate bdMenu = DelegateUtil.getMenuDelegate();
   			Menu menu1=bdMenu.obtenerMenu(new Long(idmenu));
   			Long idmenupadre=menu1.getPadre();
   			String padre1="";

			if (((TraduccionMenu)menu1.getTraduccion(Idioma.DEFAULT)).getNombre()!=null)
				padre1=((TraduccionMenu)menu1.getTraduccion(Idioma.DEFAULT)).getNombre();
   			
   			if (idmenupadre.intValue()==0) { // Era el padre
   					ret= padre1 +" > "+contnombre;
   			}
   			
   			if (idmenupadre.intValue()!=0) { // Era el hijo
       			Menu menu2=bdMenu.obtenerMenu(idmenupadre);
       			if (((TraduccionMenu)menu2.getTraduccion(Idioma.DEFAULT)).getNombre()!=null)
       				ret=((TraduccionMenu)menu2.getTraduccion(Idioma.DEFAULT)).getNombre()+" > "+ padre1 +" > "+contnombre;
       			else
       				ret= " > "+ padre1 +" > "+contnombre;
   			}

    	}
    	return ret;

    }    

    /**
     * Lista todos los contenidos de un microsite
     *  
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
    public List<?> listarAllContenidos(String micro) throws DelegateException {
    	 Session session = getSession();
         try {
        	 String sqlin ="";
        	 
        	 String sql="select menu.id from Menu menu where menu.idmicrosite=" + micro ;
        	 Query query = session.createQuery(sql);
        	 Iterator<?> iter = query.list().iterator();
        	 while (iter.hasNext()) {
        		 Long idmicrosite = (Long)iter.next();
        		 sqlin += idmicrosite.toString() + ", ";
        	 }
        	 if (sqlin.length()>0) sqlin = sqlin.substring(0, sqlin.length()-2);
        	 
        	 sql="select contenido from Contenido contenido ";
        	 if (sqlin.length()>0) sql += " where contenido.idmenu in (" + sqlin + ") ";
        	 else sql += " where contenido.idmenu=-1";
        		 
        	 query = session.createQuery(sql);
        	 return query.list();
         } catch (HibernateException he) {
             throw new EJBException(he);
         } finally {
             close(session);
         }
    }
    
    /**
     * Lista todos los documentos del microsite si pagina es null, o bien los
     * del microsite que pertenezcan a una página de contenido
     *  
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
	public List<Archivo> listarDocumentos(String micro, String pagina) {
        Session session = getSession();
        try {
        	String sql="select ar.id, ar.nombre, ar.mime, ar.peso from Archivo ar ";
        	
        	if (pagina==null)
        		sql+=" where ar.idmicrosite=" + micro + " and ar.pagina is null";
        	else	
        		if (micro==null)
        			sql+=" where ar.pagina="+pagina;
        		else
        			sql+=" where ar.idmicrosite="+micro+" and ar.pagina="+pagina;
        	
        	sql+=" order by ar.nombre";
        	
        	Query query = session.createQuery(sql);
        			
            ArrayList<Archivo> lista=new ArrayList<Archivo>();
            Iterator<?> res=query.iterate();
            int i=0;
            while ( res.hasNext() ) {
                Object[] fila = (Object[]) res.next();
                Archivo archi= new Archivo();
                archi.setId((Long)fila[0]);
                archi.setNombre((String)fila[1]);
                archi.setMime(((String)fila[2]).toUpperCase());
                archi.setPeso((((Long)fila[3]).longValue()/1024)+1);
                lista.add(i, archi);
                i++;
            }

            return lista;
        	
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }

    /**
     * Comprueba que el contenido pertenece al microsite
     * @throws DelegateException 
     *  
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
	public boolean checkSite(Long id, Long idsite) throws DelegateException  {
		
		//	************COMPROBACION DE MICROSITE*************
    	Contenido con=obtenerContenido(id);
    	MenuDelegate bdMenu= DelegateUtil.getMenuDelegate();
    	Long idmicro=bdMenu.obtenerMenu(con.getIdmenu()).getIdmicrosite();
	
    	return idmicro.longValue()!=idsite.longValue(); 
		
	}
	
	
/***************************************************************************************/
/*******************             INDEXACION         ************************************/
/***************************************************************************************/
	
    /**
     * Añade el contenido al indice en todos los idiomas
     * 
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
	public void indexInsertaContenido(Contenido con, ModelFilterObject filter)  {
		
		IndexObject io= new IndexObject();
		try {
			
			
			if (filter==null) {
				Long idmicro=DelegateUtil.getMenuDelegate().obtenerMenu(con.getIdmenu()).getIdmicrosite();
				filter=DelegateUtil.getMicrositeDelegate().obtenerFilterObject(idmicro);
			}
			
			if (filter!=null) {
				if (filter.getBuscador().equals("N")) return;
			}
			
			for (int i = 0; i < langs.size(); i++) {
				String idi = (String) langs.get(i);

				
				io = new IndexObject();
				io.setId(Catalogo.SRVC_MICRO_CONTENIDOS + "." + con.getId());
				io.setClasificacion(Catalogo.SRVC_MICRO_CONTENIDOS);
				
				
				io.setMicro(filter.getMicrosite_id());
				io.setRestringido(filter.getRestringido());
				io.setUo(filter.getUo_id());
				io.setMateria(filter.getMateria_id());
				io.setSeccion(filter.getSeccion_id());
				io.setFamilia(filter.getFamilia_id());

				
				io.setTitulo("");
				io.setUrl("/sacmicrofront/contenido.do?lang="+idi+"&idsite="+filter.getMicrosite_id().toString()+"&cont="+con.getId());
				io.setCaducidad("");
				io.setPublicacion("");
				io.setDescripcion("");
				io.setTituloserviciomain(filter.getTraduccion(idi).getMaintitle());
				
				if (con.getFcaducidad()!=null)		io.setCaducidad(new java.text.SimpleDateFormat("yyyyMMdd").format(con.getFcaducidad()));
				if (con.getFpublicacion()!=null)	io.setPublicacion(new java.text.SimpleDateFormat("yyyyMMdd").format(con.getFpublicacion()));
				
				TraduccionContenido trad=((TraduccionContenido)con.getTraduccion(idi));
				if (trad!=null) {

					io.addTextLine(trad.getTitulo());
					
					//Si hay URL no indexamos el contenido
					if ( (trad.getUrl()!=null) && (trad.getUrl().length()!=0) ) {
						io.addDescripcionLine(trad.getUrl());
					} else {
						//si el texto no es nulo, palante
						if (trad.getTexto()!=null) {
							// Añadimos los archivos que pudiera haber referenciados en el contenido
//							io = extraeArchivosHTML (trad.getTexto(), io);
							// Transformamos el texto del contenido en un bean Archivo de tipo HTML
		            		Archivo archi= new Archivo();
		            		archi.setMime("text/html");
		            		archi.setPeso(trad.getTexto().length());
		            		archi.setDatos(trad.getTexto().getBytes());
		            		io.addArchivo(archi);
		            		io.addDescripcion(archi);
						}
						
	            	}
					
					io.addTextopcionalLine(filter.getTraduccion(idi).getMateria_text());
					io.addTextopcionalLine(filter.getTraduccion(idi).getSeccion_text());
					io.addTextopcionalLine(filter.getTraduccion(idi).getUo_text());
					
					if (trad.getTitulo()!=null) io.setTitulo(trad.getTitulo());
				}
				
				if (io.getText().length()>0)
					DelegateUtil.getIndexerDelegate().insertaObjeto(io, idi);
			}
		}
		catch (DelegateException ex) {
			log.warn("[indexInsertaContenido:" + con.getId() + "] No se ha podido indexar contenido. " + ex.getMessage());
			//throw new EJBException(ex);
		}
		catch (Exception e) {
			log.warn("[indexInsertaContenido:" + con.getId() + "] No se ha podido indexar elemento. " + e.getMessage());
		}
        
	}
	
	 /**
     * Elimina el contenido en el indice en todos los idiomas
     *  
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
	public void indexBorraContenido(Long id)  {
		
		try {
			for (int i = 0; i < langs.size(); i++) {
				DelegateUtil.getIndexerDelegate().borrarObjeto(Catalogo.SRVC_MICRO_CONTENIDOS + "." + id, ""+langs.get(i));
			}
		}
		catch (DelegateException ex) {
			throw new EJBException(ex);
		}
        
	}
	
	
	/**
	 * Extrae los archivos referenciados que pudiera haber en el 
	 * HTML del contenido
	 */
	@SuppressWarnings("unused")
	private IndexObject extraeArchivosHTML (String html, IndexObject io) {
		
		try {
			
			Source fuente = new Source(html);
			fuente.fullSequentialParse();

			List<?> elementList=fuente.findAllElements(Tag.A);
			for (Iterator<?> i=elementList.iterator(); i.hasNext();) {
				Element link=(Element)i.next();
				String laurl=link.getAttributeValue("href");
				if (laurl!=null && laurl.startsWith("archivopub.do?ctrl=CNTSP")) {
					int ini=laurl.lastIndexOf("&id=");
					if (ini!=-1) {
						String id=laurl.substring(ini+4);
						Archivo archi=DelegateUtil.getArchivoDelegate().obtenerArchivo(new Long(id));
						if (archi.getPeso()>0 &&  archi.getDatos()!=null) io.addArchivo(archi);
					}
				}
			
			}
		}
		catch (Exception ex) {
			io.addTextLine("");
			log.warn("[Error extrayendo html de " + io.getMicro() + "." + io.getTitulo() + "] " + ex.getMessage());
		}
		
		return io;
	}


	
	
	
}