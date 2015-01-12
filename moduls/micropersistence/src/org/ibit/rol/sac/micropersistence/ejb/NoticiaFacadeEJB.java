package org.ibit.rol.sac.micropersistence.ejb;

import java.util.*;

import javax.ejb.CreateException;
import javax.ejb.EJBException;

import net.sf.hibernate.*;

import org.ibit.lucene.indra.model.Catalogo;
import org.ibit.lucene.indra.model.IndexEncontrado;
import org.ibit.lucene.indra.model.IndexResultados;
import org.ibit.lucene.indra.model.ModelFilterObject;
import org.ibit.rol.sac.micromodel.Archivo;
import org.ibit.rol.sac.micromodel.Idioma;
import org.ibit.rol.sac.micromodel.IndexObject;
import org.ibit.rol.sac.micromodel.Noticia;
import org.ibit.rol.sac.micromodel.TraduccionNoticia;
import org.ibit.rol.sac.micromodel.TraduccionTipo;
import org.ibit.rol.sac.micropersistence.delegate.BuscarElementosParameter;
import org.ibit.rol.sac.micropersistence.delegate.DelegateException;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;
import org.ibit.rol.sac.micropersistence.delegate.NoticiaServiceItf;
import org.ibit.rol.sac.micropersistence.intf.DominioInterface;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import static org.apache.commons.lang.StringUtils.*;



/**
 * SessionBean para manipular noticias.
 *
 * @ejb.bean
 *  name="sac/micropersistence/NoticiaFacade"
 *  jndi-name="org.ibit.rol.sac.micropersistence.NoticiaFacade"
 *  type="Stateless"
 *  view-type="remote"
 *  transaction-type="Container"
 *
 * @ejb.transaction type="Required"
 */

public abstract class NoticiaFacadeEJB extends HibernateEJB implements DominioInterface, NoticiaServiceItf {

	private static final long serialVersionUID = -7037666449767486638L;
	protected static Log log = LogFactory.getLog(NoticiaFacadeEJB.class);
	
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
     * Inicializo los par�metros de la consulta....
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public void init(Long site) {
    	super.tampagina=10;
    	super.pagina=0;
    	//super.select="";
    	super.select="select noti.id, noti.fcaducidad, noti.fpublicacion, noti.tipo, trad.titulo, trad.subtitulo,trad.texto,noti.orden ";
    	super.from=" from Noticia noti join noti.traducciones trad ";
    	super.where=" where index(trad)='"+Idioma.DEFAULT+"' and noti.idmicrosite="+site.toString();
    	super.whereini=" ";
    	super.orderby="";

    	super.camposfiltro= new String[] {"trad.titulo","trad.subtitulo","trad.texto"};
    	super.cursor=0;
    	super.nreg=0;
    	super.npags=0;	
    }

    /**
     * Inicializo los par�metros de la consulta....
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public void init() {
    	super.tampagina=10;
    	super.pagina=0;
    	//super.select="";
    	super.select="select noti.id, noti.fcaducidad, noti.fpublicacion, noti.tipo, trad.titulo, trad.subtitulo,noti.orden ";
    	super.from=" from Noticia noti join noti.traducciones trad ";
    	super.where=" where index(trad)='"+Idioma.DEFAULT+"'";
    	super.whereini=" ";
    	super.orderby="";

    	super.camposfiltro= new String[] {"trad.titulo","trad.subtitulo","trad.texto"};
    	super.cursor=0;
    	super.nreg=0;
    	super.npags=0;	
    }    
    
    /**
     * Crea o actualiza una noticia
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
     */
    public Long grabarNoticia(Noticia noticia) {
        Session session = getSession();
        boolean nuevo=false;
        try {
            Transaction tx = session.beginTransaction();
            if (noticia.getId()==null) nuevo=true;
            session.saveOrUpdate(noticia);
            session.flush();
            tx.commit();
            
            //if (!nuevo) indexBorraNoticia(noticia.getId());
           	//indexInsertaNoticia(noticia, null);
            
            return noticia.getId();
        } catch (HibernateException he) {
        	if (!nuevo) indexBorraNoticia(noticia.getId());
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }

    /**
     * Obtiene una noticia
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public Noticia obtenerNoticia(Long id) {
        Session session = getSession();
        try {
        	Noticia noticia = (Noticia) session.load(Noticia.class, id);
        	return noticia;
        } catch (ObjectNotFoundException oNe) {
        	log.error(oNe.getMessage());
        	return new Noticia();	
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }

    
    /**
     * Clona o duplica una noticia dado un id. Devuelve el id de la nueva noticia
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
     */
    public Long clonarNoticia(Long id) {
        Session session = getSession();
        try {
        	Noticia noticia = (Noticia) session.load(Noticia.class, id);
        	        	
        	Noticia newnoticia = clonar4Hibernate(noticia);
       	        	
        	Transaction tx = session.beginTransaction();
            session.saveOrUpdateCopy(newnoticia);
            session.flush();
            tx.commit();
            
            //indexInsertaNoticia(newnoticia, null);
            return newnoticia.getId();        	
        	
        } catch (HibernateException he) {
            throw new EJBException(he);
        } catch (Exception he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }    
    
    /**
     * Obtiene una noticia
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
    public Noticia obtenerNoticiaThin(Long id, String idioma) {
        Session session = getSession();
        try {
        	//Noticia noticia = (Noticia) session.load(Noticia.class, id);
        	super.where=" where index(trad)='"+idioma+"' and noti.id=" + id;
        	super.from=" from Noticia noti join noti.traducciones trad ";
        	List<Noticia> lista = listarNoticiasThin(idioma);
        	Noticia noticia = (Noticia)lista.iterator().next();
        	return noticia;
        } catch (Exception he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }       
    
    /**
     * Lista todas las noticias
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public List<Noticia> listarNoticias() {
        Session session = getSession();
        try {
        	parametrosCons(); // Establecemos los par�metros de la paginaci�n
       	
        	Query query = session.createQuery(select+from+where+orderby);
        	
        	ScrollableResults scr = query.scroll();
        	ArrayList<Noticia> lista=new ArrayList<Noticia>();        	
        	scr.first();
        	scr.scroll(cursor-1);
        	int i = 0;
        	while (tampagina > i++) {
                Object[] fila = (Object[]) scr.get();
                Noticia not= new Noticia();
                not.setId((Long)fila[0]);
                not.setFcaducidad((java.util.Date)fila[1]);
                not.setFpublicacion((java.util.Date)fila[2]);
                not.setTipo((org.ibit.rol.sac.micromodel.Tipo)fila[3]);
                TraduccionNoticia trad= new TraduccionNoticia();
                trad.setTitulo((String)fila[4]);
                trad.setSubtitulo((String)fila[5]);
                trad.setTexto((String)fila[6]);
                not.setTraduccion(Idioma.DEFAULT,trad);
                not.setOrden((Integer)fila[7]);
                //lista.add(i,not);
                lista.add(not);
        		if (!scr.next()) break;
        	}
        	scr.close();

            return lista;
        	
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }
    
    /**
     * Lista los anyos que tienen noticias
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
	public List<String> listarAnyos() throws HibernateException {
    	List<String> anyos  = new ArrayList<String>();
        Session session = getSession();
     	try {
     		String select = "select distinct to_char(noti.fpublicacion,'YYYY')"
     					+ from
     					+ where
     					+ " order by to_char(noti.fpublicacion,'YYYY') desc";
			Query query = session.createQuery(select);
			Iterator<String> res=query.iterate();
			while ( res.hasNext() ) {
	        	String anyo = res.next();
				if(anyoValido(anyo)) {
					anyos.add(anyo);
				}
			}
		} catch (HibernateException e) {
			log.error("",e);
			throw e;
		} finally {
			close(session);
		}
        //String select
    	return anyos;
    }

	private boolean anyoValido(String anyo) {
		if(null == anyo) return false;
		if(Integer.valueOf(anyo) < 1970) return false; 
		return true;
	}
    
    /**
     * Lista todas las noticias
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public List<Noticia> listarNoticiasThin(String idioma) {
        Session session = getSession();
        try {
        long startTime = System.nanoTime();    

        	parametrosCons(); // Establecemos los par�metros de la paginaci�n
       	
        long estimatedTime = System.nanoTime() - startTime;
        log.debug("listarNoticiasThin.parametrosCons() ha tardado "+estimatedTime);

        	super.select="select noti.id, noti.idmicrosite, noti.fcaducidad, noti.fpublicacion, noti.tipo, noti.imagen, noti.visible, trad.titulo, trad.subtitulo, trad.laurl, trad.urlnom, trad.fuente, trad.texto ";
        	Query query = session.createQuery(select+from+where+orderby);
            query.setFirstResult(cursor-1);
            query.setMaxResults(tampagina);
            
            ArrayList<Noticia> lista=new ArrayList<Noticia>();
        startTime = System.nanoTime();
            
            Iterator<?> res=query.iterate();
            
        estimatedTime = System.nanoTime() - startTime;
        log.debug("listarNoticiasThin.query ha tardado "+estimatedTime);
            
            int i=0;
            while ( res.hasNext() ) {
                Object[] fila = (Object[]) res.next();
                Noticia not= new Noticia();
                not.setId((Long)fila[0]);
                not.setIdmicrosite((Long)fila[1]);
                not.setFcaducidad((java.util.Date)fila[2]);
                not.setFpublicacion((java.util.Date)fila[3]);
                not.setTipo((org.ibit.rol.sac.micromodel.Tipo)fila[4]);
                not.setImagen((org.ibit.rol.sac.micromodel.Archivo)fila[5]);
                not.setVisible((String)fila[6]);
                TraduccionNoticia trad= new TraduccionNoticia();
                trad.setTitulo((String)fila[7]);
                trad.setSubtitulo((String)fila[8]);
                trad.setLaurl((String)fila[9]);
                trad.setUrlnom((String)fila[10]);
                trad.setFuente((String)fila[11]);
                trad.setTexto((String)fila[12]);
                not.getTipo().setIdi(idioma);
                not.setIdi(idioma);
                not.setTraduccion(idioma,trad);
                lista.add(i,not);
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
     * Buscar elementos
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    @Deprecated
	public List<?> buscarElementos(Map<?, ?> parametros, Map<?, ?> traduccion, String idmicrosite, String idtipo, String idioma) throws Exception {
    	BuscarElementosParameter parameters = new BuscarElementosParameter(
    			parametros, traduccion, idmicrosite, idtipo, idioma);
    	return buscarElementos(parameters);
    }


    /**
     * Buscar elementos
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public List<?> buscarElementos(BuscarElementosParameter parameter) {
    	Session session = getSession();
        try {

            List<Object> params = new ArrayList<Object>();
            String camposQuery = populateQuery(parameter.parametros, parameter.traduccion, params);
            
//          Eliminado "left join fetch" por problemas en el cache de traducciones.
//            Query query = session.createQuery("select procedimiento from ProcedimientoLocal as procedimiento " +
  //                  ", procedimiento.traducciones as trad " + sQuery);            
            


            
            String sQuery = 
            		"select noti from Noticia as noti, noti.traducciones as trad "
            		+ "where noti.idmicrosite = " + parameter.idmicrosite
            		+ " and noti.tipo = " + parameter.idtipo
            		+ " and index(trad) = '" + parameter.idioma + "'"
            		+ " and (" + camposQuery + ")";

            if(isNotEmpty(parameter.where))
            	sQuery += " and " + parameter.where;
            
            
            /*
            String anyo ="2013";
            sQuery +="  and (to_char(noti.fpublicacion,'yyyy-MM-dd')<='" + anyo + "-12-31')";
    		sQuery +=" and (to_char(noti.fpublicacion,'yyyy-MM-dd')>='" + anyo + "-01-01')";
*/
            
            Query query = session.createQuery( sQuery ) ;
            
             for (int i = 0; i < params.size(); i++) {
                String o = (String)params.get(i);
                query.setString(i, o);
            }
             

         /*  List<?> elementos = query.list();
            nreg = elementos.size();*/
             ArrayList<Noticia> elementos=new ArrayList<Noticia>();
             Iterator<?> res=query.iterate();
             int i=0;
             while ( res.hasNext() ) {
                 Noticia not= new Noticia();
                 not = (Noticia) res.next();
                 not.setIdi(parameter.idioma);
                 elementos.add(i,not);
                 i++;
             }
             nreg = i;
            
            return elementos;
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }

    
    /**
     * Buscar elementos en lucene
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public List<Noticia> buscarElementosLuc(String micro, String idi, String idlista, String cadena, boolean sugerir) {
    	Session session = getSession();
    	List<Noticia> listaelementos = new ArrayList<Noticia>();
        try {
        	IndexResultados resultado;
			resultado = DelegateUtil.getIndexerDelegate().buscar(micro, idi, idlista, cadena, sugerir);
        	if ( (resultado.getLista()!=null) && (resultado.getLista().size()>0) ) {
				Iterator<?> iter = resultado.getLista().iterator();
	        	while (iter.hasNext())  {
	        		String id = ((IndexEncontrado)iter.next()).getId();//el id esta codificado MCR.NTCS0.3412
	        		String idtrue = id.substring(Catalogo.SRVC_MICRO_ELEMENTOS.length()+1,id.length());
	        		Noticia noti = obtenerNoticia(new Long(idtrue));
	        		listaelementos.add(noti);
	        	}
        	}

            nreg = listaelementos.size();
            
            return listaelementos;
        } catch (DelegateException e) {
        	throw new EJBException(e);
        } finally {
            close(session);
        }
    }    
    
    /**
     * borra una noticia
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
     */
    public void borrarNoticia(Long id) {
        Session session = getSession();
        try {
        	Noticia noticia = (Noticia) session.load(Noticia.class, id);
            session.delete(noticia);
            
            //indexBorraNoticia(noticia.getId());
            
            session.flush();
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }
 
    /**
     * Comprueba que el elemento pertenece al Microsite
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
     */
    public boolean checkSite(Long site, Long id) {
        Session session = getSession();
        try {
        	Query query = session.createQuery("from Noticia noti where noti.idmicrosite="+site.toString()+" and noti.id="+id.toString());
        	return query.list().isEmpty();
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }
    
    /**
     * Construye el query de b�squeda segun los par�metros
     */
    protected String populateQuery(Map<?, ?> parametros, Map<?, ?> traduccion, List<Object> params) {
        String aux = "";

        for (Iterator<?> iter1 = parametros.keySet().iterator(); iter1.hasNext();) {
            String key = (String) iter1.next();
            Object value = parametros.get(key);
            if (value != null) {
                if (value instanceof String) {
                    String sValue = (String) value;
                    if (sValue.length() > 0) {
                        if (aux.length() > 0) aux = aux + " or ";
                        if (sValue.startsWith("\"") && sValue.endsWith("\"")) {
                            sValue = sValue.substring(1, (sValue.length() - 1));
                            aux = aux + " upper( noti." + key + " ) like ? ";
                            params.add(sValue);
                        } else {
                        	aux = aux + " upper( noti." + key + " ) like ? ";
                            params.add("%"+sValue+"%");
                        }
                    }
                } else if (value instanceof Date) {
                    if (aux.length() > 0) aux = aux + " or ";
                    aux = aux + "noti." + key + " = '" + value + "'";
                } else {
                    if (aux.length() > 0) aux = aux + " or ";
                    aux = aux + "noti." + key + " = " + value;
                }
            }
        }

       
        for (Iterator<?> iter2 = traduccion.keySet().iterator(); iter2.hasNext();) {
            String key = (String) iter2.next();
            Object value = traduccion.get(key);
            if (value != null) {
                if (value instanceof String) {
                    String sValue = (String) value;
                    if (sValue.length() > 0) {
                        String term = " upper( trad." + key + " ) like ? ";
                        if (sValue.startsWith("\"") && sValue.endsWith("\"")) {
                            sValue = sValue.substring(1, (sValue.length() - 1));

                            aux = afegirTraduccioAlQuery(aux, term);
                            params.add(sValue);
                        } else {
                        	aux = afegirTraduccioAlQuery(aux, term);
                            params.add("%"+sValue+"%");
                        }
                    }
                } else {
                	String term = " trad." + key + " = ? ";
                	aux = afegirTraduccioAlQuery(aux, term);
                    params.add(value);
                }
            }
        }
        
        return aux;
    }

	private String afegirTraduccioAlQuery(String aux, String term) {
		return (StringUtils.isEmpty(aux))? term :  aux + " or "+ term;

	}    
    
    private Noticia clonar4Hibernate(Noticia noticia) throws HibernateException {
    	Noticia newnoticia = new Noticia();
    	try {
	    	newnoticia= (Noticia)BeanUtils.cloneBean(noticia);
	    	newnoticia.setId(null);
	    	if (newnoticia.getImagen()!=null) newnoticia.getImagen().setId(null);
	    	newnoticia.setTraduccionMap(noticia.getTraduccionMap());
	    	Iterator<?> iter = newnoticia.getTraduccionMap().keySet().iterator();
	    	while (iter.hasNext()) {
	    		String key = (String)iter.next();
	    		TraduccionNoticia tranot = (TraduccionNoticia)newnoticia.getTraduccionMap().get(key);
	    		if ((tranot!=null) && (tranot.getDocu()!=null)) tranot.getDocu().setId(null);
	    		if (tranot==null) {
	    			tranot = new TraduccionNoticia();
	    			tranot.setTitulo("");
	    		}
	    		newnoticia.setTraduccion(key, tranot);
	    	}
	    	
	    	
    	} catch (Exception e) {
    		throw new HibernateException("[clonar4Hibernate] " + e.getMessage());
    	}
    	return newnoticia;
    }
 
    
    /***************************************************************************************/
    /*******************             INDEXACION         ************************************/
    /***************************************************************************************/    
    
    /**
     * A�ade la noticia al indice en todos los idiomas
     * 
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
	public void indexInsertaNoticia(Noticia noti, ModelFilterObject filter)  {
		
		IndexObject io= new IndexObject();
		try {

			if (filter==null) 
				filter=DelegateUtil.getMicrositeDelegate().obtenerFilterObject(noti.getIdmicrosite());
			
			if (filter!=null) {
				if (filter.getBuscador().equals("N")) return;
			}
			
			for (int i = 0; i < langs.size(); i++) {
	            String idi = (String) langs.get(i);
	            

				io = new IndexObject();
				io.setId(Catalogo.SRVC_MICRO_ELEMENTOS + "." + noti.getId());
				io.setClasificacion(Catalogo.SRVC_MICRO_ELEMENTOS + "." + noti.getTipo().getId());				
				
				io.setMicro(filter.getMicrosite_id());
				io.setRestringido(filter.getRestringido());
				io.setUo(filter.getUo_id());
				io.setMateria(filter.getMateria_id());
				io.setSeccion(filter.getSeccion_id());
				io.setFamilia(filter.getFamilia_id());
				

	            io.setTitulo("");
	            io.setUrl("/sacmicrofront/noticia.do?lang="+idi+"&idsite="+noti.getIdmicrosite().toString()+"&cont="+noti.getId().toString());
				io.setCaducidad("");
				io.setPublicacion("");
				io.setDescripcion("");
				io.setTituloserviciomain(filter.getTraduccion(idi).getMaintitle());
	            
	            if (noti.getFcaducidad()!=null)		io.setCaducidad(new java.text.SimpleDateFormat("yyyyMMdd").format(noti.getFcaducidad()));
				if (noti.getFpublicacion()!=null)	io.setPublicacion(new java.text.SimpleDateFormat("yyyyMMdd").format(noti.getFpublicacion()));
				
	            TraduccionNoticia trad=((TraduccionNoticia)noti.getTraduccion(idi));
	            if (trad!=null) {

	            	
	            	if (trad.getTexto()!=null)  {  // simulamos un bean Archivo con el contenido
	            		Archivo archi= new Archivo();
	            		archi.setMime("text/html");
	            		archi.setPeso(trad.getTexto().length());
	            		archi.setDatos(trad.getTexto().getBytes());
	            		io.addArchivo(archi);
	            		
	            		//	En elementos la descripcion sera el principio del text 
	            		if (io.getText().length()>200) io.addDescripcionLine(io.getText().substring(0,199)+"...");
	                	else io.addDescripcionLine(io.getText());
	            	}

	            	io.addTextLine(trad.getTitulo());
	            	io.addTextLine(trad.getFuente());
	            	io.addTextLine(trad.getSubtitulo());	            	
	            	if (noti.getFpublicacion()!=null)	io.addTextLine(new java.text.SimpleDateFormat("dd/MM/yyyy").format(noti.getFpublicacion()));
	            	
					io.addTextopcionalLine(filter.getTraduccion(idi).getMateria_text());
					io.addTextopcionalLine(filter.getTraduccion(idi).getSeccion_text());
					io.addTextopcionalLine(filter.getTraduccion(idi).getUo_text());	            	
	            	
	            	if (trad.getTitulo()!=null) io.setTitulo(trad.getTitulo());
	            	if (trad.getDocu()!=null) io.addArchivo(trad.getDocu());
	           	
	            }
	        
	            TraduccionTipo trad1 = (TraduccionTipo)noti.getTipo().getTraduccion(idi);
	            if (trad1!=null) io.addTextLine(trad1.getNombre());

	            if (io.getText().length()>0)
	            	DelegateUtil.getIndexerDelegate().insertaObjeto(io, idi);
			}
		}
		catch (DelegateException ex) {
			log.warn("[indexInsertaNoticia:" + noti.getId() + "] No se ha podido indexar elemento. " + ex.getMessage());
			//throw new EJBException(ex);
		}
		catch (Exception e) {
			log.warn("[indexInsertaNoticia:" + noti.getId() + "] No se ha podido indexar elemento. " + e.getMessage());
		}
        
	}
	
	 /**
     * Elimina la noticia en el indice en todos los idiomas
     * 
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
	public void indexBorraNoticia(Long id)  {
		
		try {
			for (int i = 0; i < langs.size(); i++) {
				DelegateUtil.getIndexerDelegate().borrarObjeto(Catalogo.SRVC_MICRO_ELEMENTOS + "." + id, ""+langs.get(i));
			}
		}
		catch (DelegateException ex) {
			throw new EJBException(ex);
		}
        
	}        
    
	 /**
     * Prueba de utilizar lo de los listados externos
     * 
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
	public Map<String, String> obtenerListado(String id, Map parametros)  {
		Map<String, String> mapretorno=new HashMap<String, String>();
		String cabecera = "<h1>Cabecera de NOTICIA</h1></br><p>Esto es una prueba de cabecera:</p>";
		String cuerpo = "<p>Esto es una prueba de cuerpaaaaaaaaaaaaaaaaaaaaaaaaaaaaa</p>";
		mapretorno.put("Cabecera", cabecera);
		mapretorno.put("Cuerpo", cuerpo);
		mapretorno.put("Paginado", "true");
		mapretorno.put("Pagina", "2");
		mapretorno.put("NumeroPaginas", "6");
		mapretorno.put("ElementosPorPagina", "6");
		return mapretorno;
	}
	
}