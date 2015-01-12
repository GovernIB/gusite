package org.ibit.rol.sac.micropersistence.ejb;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import net.sf.hibernate.*;
import net.sf.hibernate.expression.Expression;

import org.ibit.lucene.indra.model.Catalogo;
import org.ibit.lucene.indra.model.ModelFilterObject;
import org.ibit.lucene.indra.model.TraModelFilterObject;
import org.ibit.rol.sac.micromodel.Idioma;
import org.ibit.rol.sac.micromodel.Microsite;
import org.ibit.rol.sac.micromodel.MicrositeCompleto;
import org.ibit.rol.sac.micromodel.TraduccionMicrosite;
import org.ibit.rol.sac.micromodel.UsuarioPropietarioMicrosite;
import org.ibit.rol.sac.micromodel.UsuarioPropietarioRespuesta;
import org.ibit.rol.sac.micropersistence.delegate.DelegateException;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;
import org.ibit.rol.sac.micropersistence.delegate.UsuarioDelegate;
import org.ibit.rol.sac.model.Ficha;
import org.ibit.rol.sac.model.Materia;
import org.ibit.rol.sac.model.TraduccionFicha;
import org.ibit.rol.sac.model.TraduccionMateria;
import org.ibit.rol.sac.model.TraduccionUA;
import org.ibit.rol.sac.model.UnidadAdministrativa;
import org.ibit.rol.sac.micromodel.Usuario;

/**
 * SessionBean para consultar Microsite.
 *
 * @ejb.bean
 *  name="sac/micropersistence/MicrositeFacade"
 *  jndi-name="org.ibit.rol.sac.micropersistence.MicrositeFacade"
 *  type="Stateless"
 *  view-type="remote"
 *  transaction-type="Container"
 *
 * @ejb.transaction type="Required"
 */

public abstract class MicrositeFacadeEJB extends HibernateEJB{

	private static final long serialVersionUID = -2076446869522196666L;

	/**
     * @ejb.create-method
     * @ejb.permission unchecked="true"
     */
    public void ejbCreate() throws CreateException {
        super.ejbCreate();
        //System.out.println("Creando MicrositeFacadeEJB " +this.getClass().hashCode());
    }

    /**
     * Inicializo los parámetros de la consulta de Microsite....
     * No está bien hecho debería ser Statefull
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
    public void init() {
    	super.tampagina=10;
    	super.pagina=0;
    	super.select="";
    	super.from=" from Microsite site join site.traducciones trad ";
    	super.where=" where index(trad)='"+Idioma.DEFAULT+"'";
    	super.whereini=" ";
    	super.orderby="";

    	super.camposfiltro= new String[] {"trad.titulo"};
    	super.cursor=0;
    	super.nreg=0;
    	super.npags=0;	
    }        
    
    /**
     * Crea o actualiza un Microsite
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
     */
    public Long grabarMicrosite(Microsite site) {
        Session session = getSession();
        boolean nuevo = false;
        try {
            Transaction tx = session.beginTransaction();
            if (site.getId()==null) nuevo=true;
            if (nuevo) site.setClaveunica(obtenerClaveUnica(site));
            session.saveOrUpdate(site);
            session.flush();
            if (nuevo) {
            	//Ahora se asocian todos los usuarios admin
            	UsuarioDelegate uDel = DelegateUtil.getUsuarioDelegate();
            	List<?> listau =  uDel.listarUsuariosPerfil("sacadmin");
            	Iterator<?> iter = listau.iterator();
            	while (iter.hasNext()) {
            		Usuario user = (Usuario)iter.next(); 
            		UsuarioPropietarioMicrosite upm = new UsuarioPropietarioMicrosite(site.getId(),user.getId());
                	session.save(upm);
            	}
            }
            
            session.flush();
            tx.commit();
            return site.getId();
        } catch (HibernateException he) {
            throw new EJBException(he);
        } catch (DelegateException e) {
        	throw new EJBException(e);
		} finally {
            close(session);
        }
    }

    
    
    /**
     * Crea o actualiza UsuarioPropietarioMicrosite
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
     */
    public Long grabarUsuarioPropietarioMicrosite(UsuarioPropietarioMicrosite upm) {
        Session session = getSession();
        try {
            session.save(upm);
            session.flush();
            return upm.getIdmicrosite();
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }
    
    
    /**
     * borra un UsuarioPropietarioMicrosite
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
     */
    public void borrarUsuarioPropietarioMicrosite(UsuarioPropietarioMicrosite upm) {
        Session session = getSession();
        try {
        		Criteria criteri = session.createCriteria(UsuarioPropietarioMicrosite.class);
        		criteri.add(Expression.eq("idmicrosite", upm.getIdmicrosite()));
        		criteri.add(Expression.eq("idusuario", upm.getIdusuario()));
        		UsuarioPropietarioMicrosite upm2 = (UsuarioPropietarioMicrosite)criteri.uniqueResult();
        	
            	session.delete(upm2);
            	session.flush();
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }
    

    
   
    
    /**
     * Obtiene un Microsite
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public Microsite obtenerMicrosite(Long id) {
        Session session = getSession();
        try {
        	
        	Microsite site = (Microsite) session.load(Microsite.class, id);
        	//session.refresh(site);
            return site;
        } catch (ObjectNotFoundException oNe) {
        	log.error(oNe.getMessage());
        	return new Microsite();            
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }

    /**
     * Obtiene un Microsite a partir de su clave de identificación.
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public Microsite obtenerMicrositebyKey(String key) {
        Session session = getSession();
        try {
        	
        	Criteria criteri = session.createCriteria(Microsite.class);
            criteri.add(Expression.eq("claveunica", key));
            Microsite site = (Microsite)criteri.uniqueResult();
        	
            return site;
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }

        
    
    /**
     * Obtiene un Microsite para la exportación
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public MicrositeCompleto obtenerMicrositeCompleto(Long id) {
        Session session = getSession();
        try {
        	
        	MicrositeCompleto site = (MicrositeCompleto) session.load(MicrositeCompleto.class, id);
        	//session.refresh(site);
            return site;
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }

    
    /**
     * Obtiene un Microsite para la exportación
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public MicrositeCompleto obtenerMicrositeCompletobyKey(String key) {
        Session session = getSession();
        try {
        	
        	Criteria criteri = session.createCriteria(MicrositeCompleto.class);
            criteri.add(Expression.eq("claveunica", key));
            MicrositeCompleto site = (MicrositeCompleto)criteri.uniqueResult();
            
            return site;
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }
    
    /**
     * Crea un Microsite Completo, durante importación
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin}"
     */
    public Long grabarMicrositeCompleto(MicrositeCompleto site) {
        Session session = getSession();
        boolean nuevo = false;
        try {
            Transaction tx = session.beginTransaction();
            if (site.getId()==null) nuevo=true;
            if (nuevo) site.setClaveunica(obtenerClaveUnica(site));
            session.saveOrUpdate(site);
            if (nuevo) {
    	     	UsuarioDelegate usudel=DelegateUtil.getUsuarioDelegate();
    	     	Usuario usu = usudel.obtenerUsuariobyUsername(super.getUsuarioEJB());
            	UsuarioPropietarioMicrosite upm = new UsuarioPropietarioMicrosite(site.getId(),usu.getId());
            	session.save(upm);
            }
            session.flush();
            tx.commit();
            return site.getId();
        } catch (HibernateException he) {
            throw new EJBException(he);
        } catch (DelegateException e) {
        	throw new EJBException(e);
		} finally {
            close(session);
        }
    }

    /**
     * borra un Microsite Completo
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin}"
     */
    public void borrarMicrositeCompleto(Long id) {
        Session session = getSession();

        try {
        	Transaction tx = session.beginTransaction();
        	
        	//Primero: borrar los usuarios asociados
        	Criteria criteri = session.createCriteria(UsuarioPropietarioMicrosite.class);
            criteri.add(Expression.eq("idmicrosite", id));
            
            Iterator<?> iter = criteri.list().iterator();
            while (iter.hasNext()) {
            	UsuarioPropietarioMicrosite upm = (UsuarioPropietarioMicrosite)iter.next();
            	session.delete(upm);
            }
        
            //Segundo: En el caso que el microsite tenga encuestas, borraremos  la relación de usuarios con las respuestas de la encuesta
       		List<?> listIdRespu = idRespDeEncDelMicrosite(id);				
            if (!listIdRespu.isEmpty()){
            	Criteria criteriUsuPropiResp = session.createCriteria(UsuarioPropietarioRespuesta.class);
	            criteriUsuPropiResp.add(Expression.in("idrespuesta",listIdRespu));
	            Iterator<?> iterUsuPropiResp = criteriUsuPropiResp.list().iterator();
	            while (iterUsuPropiResp.hasNext()) {
	            	UsuarioPropietarioRespuesta upm = (UsuarioPropietarioRespuesta)iterUsuPropiResp.next();
		            session.delete(upm);
		        }
	        }
            
        	//Tercero: borrar el microsite completo
	        MicrositeCompleto site = (MicrositeCompleto) session.load(MicrositeCompleto.class, id);
            session.delete(site);
            
            indexBorraMicrosite(id);
            
            session.flush();
            tx.commit();
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }
    /**
     * Lista todos los Ids de las respuestas de las encuestas de un microsite.
     */
    private List<?> idRespDeEncDelMicrosite(Long idMicrosite) {
        Session session = getSession();
        try {
        	
        	String hql ="SELECT RESP.Id FROM  Encuesta ENC,  Pregunta PRE, Respuesta RESP WHERE" +
        			" RESP.idpregunta = PRE.Id AND PRE.idencuesta = ENC.Id AND ENC.idmicrosite =" + idMicrosite.toString();
        	   
        	Query query = session.createQuery(hql);
        	List<?> queryList = query.list();
        	return queryList;
        	
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }
    
    
    /**
     * Reemplaza un microsite basandose en la clave unica del microsite.
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin}"
     */
    public Long reemplazarMicrositeCompleto(MicrositeCompleto site) {
        Session session = getSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            
            MicrositeCompleto oldsite = (MicrositeCompleto)obtenerMicrositeCompletobyKey(site.getClaveunica());
            ArrayList<Long> listausuariosold = new ArrayList<Long>();
            
            if (oldsite!=null) {
            
	            //Primero: recoger todos los usuarios asociados al antiguo microsite y además borrar upm
	        	Criteria criteri = session.createCriteria(UsuarioPropietarioMicrosite.class);
	            criteri.add(Expression.eq("idmicrosite", oldsite.getId()));
	            Iterator<?> iter = criteri.list().iterator();
	            while (iter.hasNext()) {
	            	UsuarioPropietarioMicrosite upm = (UsuarioPropietarioMicrosite)iter.next();
	            	listausuariosold.add(upm.getIdusuario());
	            	session.delete(upm);
	            }
	            
	            //Segundo: En el caso que el microsite tenga encuestas, borraremos  la relación de usuarios con las respuestas de la encuesta
	            List<?> listIdRespu = idRespDeEncDelMicrosite(oldsite.getId());				
	            if (!listIdRespu.isEmpty()){
	            	Criteria criteriUsuPropiResp = session.createCriteria(UsuarioPropietarioRespuesta.class);
		            criteriUsuPropiResp.add(Expression.in("idrespuesta",listIdRespu));
		
		            Iterator<?> iterUsuPropiResp = criteriUsuPropiResp.list().iterator();
		            while (iterUsuPropiResp.hasNext()) {
		            	UsuarioPropietarioRespuesta upm = (UsuarioPropietarioRespuesta)iterUsuPropiResp.next();
			            session.delete(upm);
			        }
		        }
	            
	            //Tercero: borrar, si procede, el microsite antiguo
	            session.delete(oldsite);
	            session.flush();
            }
            //Tercero: grabar el nuevo microsite
            session.saveOrUpdate(site);
            session.flush();
            
            //Cuarto: volcar los usuarios del antiguo microsite en el nuevo
            Iterator<?> iter2 = listausuariosold.iterator();
            while (iter2.hasNext()) {
            	Long idusuario= (Long)iter2.next();
            	UsuarioPropietarioMicrosite upm = new UsuarioPropietarioMicrosite(site.getId(),idusuario);
            	session.save(upm);
            }
            session.flush();
            
            tx.commit();
            return site.getId();
        } catch (HibernateException he) {
        		try {
        			if (tx!=null) tx.rollback();
				} catch (HibernateException e) {
		            throw new EJBException(e);
		        }
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }
    
    
    /**
     * Lista todos los Microsites
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
    public List<?> listarMicrosites() {
        Session session = getSession();
        try {
        	//parametrosCons(); // Establecemos los parámetros de la paginación
           	
        	//Query query = session.createQuery(select+from+where+orderby);
        	Query query = session.createQuery(" FROM Microsite micro ");
        	List<?> microlista = query.list();
        	return microlista;
            //query.setFirstResult(cursor-1);
            //query.setMaxResults(tampagina);
        	//return query.list();
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }
    
    
    /**
     * Listado ligero de todos los Microsites. Solo rellena el idioma por defecto
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
    public List<?> listarMicrositesThin() {
        Session session = getSession();
        try {
        	
        	String sql="select mic.id, mic.unidadAdministrativa, trad.titulo from Microsite mic join mic.traducciones trad where index(trad)='"+Idioma.DEFAULT+"' order by trad.titulo";
        	
        	Query query = session.createQuery(sql);
            
            ArrayList<Microsite> lista = new ArrayList<Microsite>();
            Iterator<?> res=query.iterate();
            int i=0;
            while ( res.hasNext() ) {
                Object[] fila = (Object[]) res.next();
                Microsite mic= new Microsite();
                mic.setId((Long)fila[0]);
                Integer idua = (Integer)fila[1];
                mic.setUnidadAdministrativa(idua.intValue());
                TraduccionMicrosite trad= new TraduccionMicrosite ();
                trad.setTitulo((String)fila[2]);
                mic.setTraduccion(Idioma.DEFAULT,trad);
                lista.add(i,mic);
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
     * Lista todos los Microsites a los que el usuario puede acceder
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
    public List<?> listarMicrositesFiltro(Usuario usu, Map<?, ?> param) {
        Session session = getSession();
        try {

        	Criteria criteri = session.createCriteria(Microsite.class);
        	populateCriteria(criteri, param);
        	List<?> microlista = criteri.list();

    		ArrayList<?> microlistaOrd = new ArrayList<Object>(microlista);

    		Comparator<Object> comp = new MicrosComparator();
    				  
    	  	Collections.sort(microlistaOrd, comp);     	
        	
        	return microlistaOrd;
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }

	  private static class MicrosComparator implements Comparator<Object> {
		    public int compare(Object element1, Object element2) {
		    	
		    	String nom1 = (((TraduccionMicrosite)((Microsite)element1).getTraduccion("ca")).getTitulo() !=null )?((TraduccionMicrosite)((Microsite)element1).getTraduccion("ca")).getTitulo():"";
		    	String nom2 = (((TraduccionMicrosite)((Microsite)element2).getTraduccion("ca")).getTitulo() !=null )?((TraduccionMicrosite)((Microsite)element2).getTraduccion("ca")).getTitulo():"";
	    	
		    	return nom1.toLowerCase().compareTo(nom2.toLowerCase());
		    }
	  } 

    /**
     * borra un Microsite
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin}"
     */
    public void borrarMicrosite(Long id) {
        Session session = getSession();
        try {
        	Transaction tx = session.beginTransaction();
        	Microsite site = (Microsite) session.load(Microsite.class, id);
            session.delete(site);
            
            indexBorraMicrosite(id);
            
            session.flush();
            tx.commit();
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }

    /**
     * Anyade un id de contenido al listado de los ultimos modificados en el microsite
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
     */
    public void grabarUltimoIdcontenido(Microsite site, Long idcontenido) {
        Session session = getSession();
        try {
        	
        	site.setServiciosSeleccionados( manejaListadoUltimosIds(site.getServiciosSeleccionados(), "" +idcontenido.longValue()) );
        	
            Transaction tx = session.beginTransaction();
            session.saveOrUpdate(site);
            session.flush();
            tx.commit();
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    	
    }    
    
    
    /**
     * 
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
     */
    public List<?> listarMicrositesbyUser(Usuario usuario) {
        Session session = getSession();
        try {

            String hql="select mic.id, mic.unidadAdministrativa, trad.titulo ";
            hql += " from UsuarioPropietarioMicrosite upm, Microsite mic join mic.traducciones trad ";
            hql += " where upm.idusuario="+ usuario.getId().longValue() + " and  mic.id=upm.idmicrosite and index(trad)='"+Idioma.DEFAULT+"' order by trad.titulo";
            
        	Query query = session.createQuery(hql);
            
            ArrayList<Microsite> lista=new ArrayList<Microsite>();
            Iterator<?> res=query.iterate();
            int i=0;
            while ( res.hasNext() ) {
                Object[] fila = (Object[]) res.next();
                Microsite mic= new Microsite();
                mic.setId((Long)fila[0]);
                Integer idua = (Integer)fila[1];
                mic.setUnidadAdministrativa(idua.intValue());
                TraduccionMicrosite trad= new TraduccionMicrosite ();
                trad.setTitulo((String)fila[2]);
                mic.setTraduccion(Idioma.DEFAULT,trad);
                lista.add(i,mic);
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
     * 
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
     */
    public List<?> llistarMicrodeluser(String id) {
        Session session = getSession();
        try {

            String hql="select mic.id, mic.unidadAdministrativa, trad.titulo ";
            hql += " from UsuarioPropietarioMicrosite upm, Microsite mic join mic.traducciones trad ";
            hql += " where upm.idusuario="+ id + " and  mic.id=upm.idmicrosite and index(trad)='"+Idioma.DEFAULT+"' order by trad.titulo";
            
        	Query query = session.createQuery(hql);
            
            ArrayList<Microsite> lista = new ArrayList<Microsite>();
            Iterator<?> res=query.iterate();
            int i=0;
            while ( res.hasNext() ) {
                Object[] fila = (Object[]) res.next();
                Microsite mic= new Microsite();
                mic.setId((Long)fila[0]);
                Integer idua = (Integer)fila[1];
                mic.setUnidadAdministrativa(idua.intValue());
                TraduccionMicrosite trad= new TraduccionMicrosite ();
                trad.setTitulo((String)fila[2]);
                mic.setTraduccion(Idioma.DEFAULT,trad);
                lista.add(i,mic);
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
     * 
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */    
    public List<?> listarUsernamesbyMicrosite(Long idmicrosite) {
        Session session = getSession();
        try {

            String hql="select usuario ";
            hql += " from UsuarioPropietarioMicrosite upm, Usuario usuario ";
            hql += " where upm.idmicrosite="+ idmicrosite.longValue() + " and  usuario.id=upm.idusuario order by usuario.username";
            
        	Query query = session.createQuery(hql);
            
        	return query.list();
           	
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }
    
    
    private void populateCriteria(Criteria criteri, Map<?, ?> parametros) {
        parametros.remove("id");
        for (Iterator<?> iterator = parametros.keySet().iterator(); iterator.hasNext();) {
            String key = (String) iterator.next();
            Object value = parametros.get(key);
            if (value != null) {
                if (value instanceof String) {
                    String sValue = (String) value;
                    if (sValue.length() > 0) {
                        criteri.add(Expression.ilike(key, value));
                    }
                } else {
                    criteri.add(Expression.eq(key, value));
                }
            }
        }
    }

    
    /**
     * Metodo privado que se le pasa un string con ids separados por ;, y le añade en la primera posicion el nuevo id 
     * @param oldlistado
     * @param newIdcontenido
     * @return
     */
    private String manejaListadoUltimosIds(String oldlistado, String newIdcontenido) {
    	 	
    	int _numMax=5;
    	String _nulo="-1";
    	Hashtable<String, String> hshlistaids = new Hashtable<String, String>();
    	
    	//inicializo 5 entradas a nulo
    	for (int i=0;i<_numMax;i++) {
    		hshlistaids.put("" + i, _nulo);
    	}
    	
    	String servs=oldlistado;
    	
    	if (servs!=null) {
    		StringTokenizer st=new StringTokenizer(servs,";");
    		int n=st.countTokens();

    		//recoger el hash
    		for (int i=0;i<n;i++) {
    			hshlistaids.put("" + i,st.nextToken());
    		}
    		
    		//comprobar que no se repita.
    		int repe=-1; //obtendrá la posicion repetida
    		for (int i=0;i<n;i++) {
    			if ( hshlistaids.get("" +i).equals(newIdcontenido) )
    					repe=i;
    		}
    		if (repe!=-1) {
    			//es repetido
    			hshlistaids.put("" + repe, _nulo);
    			for (int i=repe;i<_numMax-1;i++) {
    				hshlistaids.put("" + i, hshlistaids.get("" + (i+1)) );
        		}
    			
    		}
    		
    		//ahora, desplazar el hash
    		for (int i=_numMax-1;i>=0;i--) {
    			hshlistaids.put("" + (i+1), hshlistaids.get("" + i) );
    		}
    		
    	}
    	
    	//anyadir el id que se ha pasado
    	hshlistaids.put("0", newIdcontenido );
    	
    	
    	//volcar el hash en el string final
    	servs="";
    	for (int i=0;i<_numMax;i++)
    		if (!hshlistaids.get("" +i).equals(_nulo)) servs+=hshlistaids.get("" +i)+";";
    	if (servs.length()>0)
    		servs=servs.substring(0,servs.length()-1);
    	
    	
    	return servs;
    }

    

    
	/**
	 * Metodo que obtiene un bean con el filtro para la indexacion
	 * 
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
	 */
	public ModelFilterObject obtenerFilterObject(Long idsite) {
		
		ModelFilterObject filter = new ModelFilterObject();
		
		Session session = getSession();

		try {
			
			Microsite site = (Microsite) session.load(Microsite.class, idsite);
			
    		filter.setMicrosite_id(idsite);
    		filter.setBuscador(site.getBuscador());
    		filter.setRestringido(site.getRestringido());
    		
    		List<?> langs = DelegateUtil.getIdiomaDelegate().listarLenguajes();
        	List<?> listapadres= org.ibit.rol.sac.persistence.delegate.DelegateUtil.getUADelegate().listarPadresUnidadAdministrativa(new Long(site.getUnidadAdministrativa()));
        	List<?> listaFichas = org.ibit.rol.sac.persistence.delegate.DelegateUtil.getFichaDelegate().getFichasMicrosite(""+idsite);
        	
			for (int i = 0; i < langs.size(); i++) {
				String idioma = (String) langs.get(i);
				TraModelFilterObject trafilter = new TraModelFilterObject();

				// Titulo Microsite

				if (site.getTraduccion(idioma)!=null) trafilter.setMaintitle( ((TraduccionMicrosite)site.getTraduccion(idioma)).getTitulo() );
				else if (site.getTraduccion(Idioma.DEFAULT)!=null) trafilter.setMaintitle( ((TraduccionMicrosite)site.getTraduccion(Idioma.DEFAULT)).getTitulo() );
					else trafilter.setMaintitle("");
	        	
	    		// Unidades Administrativas
				
	    		String txids=Catalogo.KEY_SEPARADOR;
	    		String txtexto=" "; //espacio en blanco, que es para tokenizar
	    		UnidadAdministrativa ua=null;
	    		for (int x = 0; x < listapadres.size(); x++) {
	    			ua=(UnidadAdministrativa)listapadres.get(x);
	    			txids+=ua.getId()+Catalogo.KEY_SEPARADOR;
	    			if ((TraduccionUA)ua.getTraduccion(idioma)!=null)
	    				txtexto+=((TraduccionUA)ua.getTraduccion(idioma)).getNombre()+" "; //espacio en blanco, que es para tokenizar
	    		}	
	    		filter.setUo_id( (txids.length()==1) ? null: txids);
	    		trafilter.setUo_text( (txtexto.length()==1) ? null: txtexto);
	    		
	    		// Materias. se identifican por las que hay en las fichas a través de su url  
	    		
	    		txids=Catalogo.KEY_SEPARADOR;
	    		txtexto=" "; //espacio en blanco, que es para tokenizar
	    		Ficha fic=null;
	    		for (int x = 0; x < listaFichas.size(); x++) {
	    			fic=(Ficha)listaFichas.get(x);
	    			
	    			// La ficha sirve si el siguiente caracter de la url (si hay) de la forma idsite=xx no debe ser un número
	    			// Ejemplo: si idsite=25 , no debe permitir idsite=2521
	    			TraduccionFicha trafic=(TraduccionFicha) fic.getTraduccion(idioma);
	    			boolean ficha_valida=true;
	    			if (trafic!=null) {
	    				String url=trafic.getUrl();
	    				if (url!=null) {
		    				int poscar=url.indexOf("idsite="+idsite)+ ("idsite="+idsite).length();
		    				if (poscar<url.length())
		    					if (url.charAt(poscar)>='0' && url.charAt(poscar)<='9') 
		    						ficha_valida=false;
		    				if (url.indexOf("idsite="+idsite)<0) 
		    					ficha_valida=false;
	    				} else {
	    					ficha_valida=false;
	    				}
	    			}
	    			else {
	    				ficha_valida=false;
	    			}
	    			if (ficha_valida) {
	    				Iterator<?> itmat=fic.getMaterias().iterator();
	    				while(itmat.hasNext()) {
	    					Materia mat=(Materia)itmat.next();
	    					txids+=mat.getId()+Catalogo.KEY_SEPARADOR;
	    					if (mat.getTraduccion(idioma)!=null) {
	    		        			txtexto+=((TraduccionMateria)mat.getTraduccion(idioma)).getNombre() + " ";
	    		        			txtexto+=((TraduccionMateria)mat.getTraduccion(idioma)).getDescripcion() + " ";
	    		        			txtexto+=((TraduccionMateria)mat.getTraduccion(idioma)).getPalabrasclave() + " ";
	    					}
	    				}
	    			}
	    		}	
	    		filter.setMateria_id( (txids.length()==1) ? null: txids);
	    		trafilter.setMateria_text( (txtexto.length()==1) ? null: txtexto);
	    		
	    		filter.addTraduccion(idioma, trafilter);
 
			}
        	
        } catch (HibernateException he) {
            throw new EJBException(he);
        } catch (org.ibit.rol.sac.persistence.delegate.DelegateException e) {
            throw new EJBException(e);
		} catch (DelegateException e) {
            throw new EJBException(e);
		} finally {
            close(session);
        }
		

		return filter;
	}    
    
    private void indexBorraMicrosite(Long idsite) {
		try {
			DelegateUtil.getIndexerDelegate().desindexarMicrosite(idsite);
		}
		catch (DelegateException ex) {
			throw new EJBException(ex);
		}    	
    }	
    
    /**
     * Metodo que genera una clave única.
     * @param site
     * @return
     */
    private String obtenerClaveUnica(Object site) {
    	String retorno="";
    	
    	SimpleDateFormat fmt = new SimpleDateFormat ("yyMMddHHmmss");											
    	retorno += "M" + fmt.format (new Date());
    	retorno += site.hashCode();
    	
    	return retorno;
    }
	
}