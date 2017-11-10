package es.caib.gusite.micropersistence.ejb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import es.caib.gusite.micromodel.SolrPendiente;
import es.caib.gusite.micromodel.SolrPendienteJob;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.util.IndexacionUtil;
import es.caib.gusite.plugins.PluginException;
import es.caib.gusite.plugins.PluginFactory;
import es.caib.gusite.plugins.organigrama.OrganigramaProvider;
import es.caib.gusite.plugins.organigrama.UnidadListData;
import es.caib.solr.api.model.types.EnumCategoria;



/**
 * SessionBean para manipular datos de las respuestas.
 * 
 * @ejb.bean name="sac/micropersistence/SolrPendienteFacade"
 *           jndi-name="es.caib.gusite.micropersistence.SolrPendienteFacade"
 *           type="Stateless" view-type="remote" transaction-type="Container"
 * 
 * @ejb.transaction type="RequiresNew"
 */
public abstract class SolrPendienteFacadeEJB extends HibernateEJB {

	private static final long serialVersionUID = 6661917618076931960L;
	
	/**
	 * @ejb.create-method
	 * @ejb.permission unchecked="true"
	 */
	@Override
	public void ejbCreate() throws CreateException {
		super.ejbCreate();
	}

	 /**
     * Lista todos los SolrPendientes
     * 
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     * @ejb.transaction type="RequiresNew"
     *
     * @return Devuelve un listado de todos los SolrPendientes.
     */
    public List<SolrPendiente> getPendientes() {
    	final Session session = getSession();
        try {
            final Criteria criteri = session.createCriteria(SolrPendiente.class);
            criteri.add(Restrictions.eq("resultado", 0));             
            criteri.addOrder(Order.asc("id"));            
            return  ( List<SolrPendiente>) criteri.list();
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }
    
    
	 /**
     * Lista todos los SolrPendientes ordenados
     * 
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     * @ejb.transaction type="RequiresNew"
     *
     * @return Devuelve un listado de todos los SolrPendientes, ordenados por fecha creación.
     */
    public List<SolrPendiente> getPendientesOrdenFC() {
    	final Session session = getSession();
        try {
            final Criteria criteri = session.createCriteria(SolrPendiente.class);
            criteri.add(Restrictions.eq("resultado", 0));             
            criteri.addOrder(Order.desc("fechaCreacion"));
            return  ( List<SolrPendiente>) criteri.list();
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }
    
    
    
    
    /**
     * Lista todos los SolrPendientes
     * 
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     * @ejb.transaction type="RequiresNew"
     *
     * @return Devuelve un listado de todos los SolrPendientes.
     */
    public List<SolrPendiente> getPendientes(Long idElemento, Long idArchivo, String tipo, Long accion, Integer resultado) {
    	final Session session = getSession();
        try {
            final Criteria criteri = session.createCriteria(SolrPendiente.class);
            if (idElemento != null) { 
            	criteri.add(Restrictions.eq("idElem", idElemento));
            }
                        
            if (tipo != null) {
            	criteri.add(Restrictions.eq("tipo", tipo));
            }
            
            /** Solo se añade el id Archivo para los de tipo microsite. **/
            if (idArchivo != null && tipo.equals(EnumCategoria.GUSITE_MICROSITE.toString())) {
            	criteri.add(Restrictions.eq("idArchivo", idArchivo));
            }
            
            if (accion != null) {
            	criteri.add(Restrictions.eq("accion", accion.intValue()));
            }
            
            if (resultado != null) {
            	criteri.add(Restrictions.eq("resultado", resultado));
            }
            return  ( List<SolrPendiente>) criteri.list();
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }
    
    
    /**
     * Crear un solrpendiente
     * 
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     * @ejb.transaction type="RequiresNew"
     * 
     * @return Booleano indicando si se ha obtenido el registro .
     * @throws DelegateException 
   	 */
    public SolrPendiente grabarSolrPendiente(String tipo, Long idElemento, Long idArchivo, Long accion)  
 	throws DelegateException{
    	    	
    	 Session session = getSession();
         try
        	{
        	 	//Paso 1. Si ya hay pendiente una acción igual, no se guarda
        	    List<SolrPendiente> pendientes = getPendientes(idElemento, idArchivo, tipo, accion, 0);
        	    if (pendientes.size() > 0) {
        	    	return null;
        	    }
        	 
        	 	SolrPendiente solrpendiente = new SolrPendiente();
        	 	solrpendiente.setTipo(tipo);
    			solrpendiente.setIdElem(idElemento);
    			solrpendiente.setIdArchivo(idArchivo);
    			solrpendiente.setAccion((int)(long)accion);
    			solrpendiente.setFechaCreacion(new Date());
    			solrpendiente.setResultado(0);
    			
    			// Paso 2. Si es un desindexar, se borra todas las reindexaciones pendientes.
    			 if (accion == IndexacionUtil.DESINDEXAR) {
	    			
    				pendientes = getPendientes(idElemento, idArchivo, tipo, IndexacionUtil.REINDEXAR, 0);
    				for (SolrPendiente pendiente : pendientes) {
    					session.delete(pendiente);
    				}
    			}
    			
    			 //Paso 3. Guardamos.
    			session.save(solrpendiente);
				session.flush();
				
    			return solrpendiente;
    		           		     
            } catch (HibernateException he) {
                throw new EJBException(he);
            } finally {
                close(session);
            }
    }
    
    /**
     * Obtener Unidades Administrativas
     * 
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     * @ejb.transaction type="RequiresNew"
     * 
     * @return lista de Unidades Administrativas . 
   	 */
    public Collection<UnidadListData> getUnidadesAdministrativas(String lang){
    	OrganigramaProvider op = PluginFactory.getInstance().getOrganigramaProvider();
    	Collection<UnidadListData> list = null;
    	try{
    		list = op.getUnidadesPrincipales(lang);	
    	} catch (PluginException e1) {    
    		log.error("No se han podido obtener la Unidades Administrativas");
    		return null;
		}
    	
    	return list;
    	
    }
    
    /**
     * Lista todos los SolrPendientesJob según cuantos.
     * 
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     * @ejb.transaction type="RequiresNew"
     *
     * @return Devuelve un listado de todos los SolrPendientes.
     */
    public List<SolrPendienteJob> getListJobs(final int cuantos, final String tipoIndexacion, final String idElemento) {

        final Session session = getSession();
        try {
        	final StringBuffer sql = new StringBuffer();
        	sql.append(" from SolrPendienteJob solrpendientejob where 1 = 1 ");
        	if (tipoIndexacion != null && !tipoIndexacion.isEmpty()) {
        		sql.append(" and solrpendientejob.tipo = '"+tipoIndexacion+"' ");
        	}
        	if (idElemento != null && !idElemento.isEmpty()) {
        		sql.append(" and solrpendientejob.idElem = "+idElemento+" ");
        	}
        	sql.append(" order by solrpendientejob.id desc ");
        	final Query query = session.createQuery(sql.toString());
        	query.setMaxResults(cuantos);
        	
        	return query.list();
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }
    
    
    /**
     * Crear una solr pendiente job. 
     * 
   	 * @ejb.interface-method
   	 * @ejb.permission unchecked="true"
   	 * @ejb.transaction type="RequiresNew"
   	 * 
   	 * @return Booleano indicando si se indexan todos los procesos pendientes .     *  
   	 */
	public SolrPendienteJob crearSorlPendienteJob(String tipo, Long idElemento) {
		try
    	{
			final Session session = getSession();
			final SolrPendienteJob solrpendienteJob = new SolrPendienteJob();
	    	solrpendienteJob.setFechaIni(new Date());
	    	solrpendienteJob.setTipo(tipo);
	    	solrpendienteJob.setIdElem(idElemento);
	    	session.save(solrpendienteJob); 
			session.flush();
			session.close();
			return solrpendienteJob;
    	 } catch(Exception exception) {
 			throw new EJBException(exception);
 		}
	}
	
	
	
	/**
	 * Actualizar el pendiente job.
	 * 
   	 * @ejb.interface-method
   	 * @ejb.permission unchecked="true"
   	 * @ejb.transaction type="RequiresNew"
   	 *   
   	 */
    public void actualizarSorlPendienteJob(SolrPendienteJob solrpendienteJob)  {
    	try
    	{
    		Session session = getSession();
    		session.update(solrpendienteJob); 
			session.flush();
			session.close();
	    } catch(Exception exception) {
			throw new EJBException(exception);
		}
    }
	
	
	/**
	 * Cerrando el pendiente job.
	 * 
   	 * @ejb.interface-method
   	 * @ejb.permission unchecked="true"
   	 * @ejb.transaction type="RequiresNew"
   	 *   
   	 */
    public void cerrarSorlPendienteJob(SolrPendienteJob solrpendienteJob)  {
    	try
    	{
    		Session session = getSession();
    		
    		
	    	solrpendienteJob.setFechaFin(new Date());
	    	session.update(solrpendienteJob); 
			session.flush();
			session.close();
	    } catch(Exception exception) {
			throw new EJBException(exception);
		}
    }

    /**
     * Lista todos los SolrPendientesJob según cuantos.
     * 
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     * @ejb.transaction type="RequiresNew"
     *
     * @return Devuelve un listado de todos los SolrPendientes.
     */
    public List<SolrPendienteJob> getListJobs(final int cuantos) {

        Session session = getSession();
        try {
        	Query query = session.createQuery("select solrpendientejob.id, solrpendientejob.fechaIni, solrpendientejob.fechaFin, solrpendientejob.tipo, solrpendientejob.idElem  from SolrPendienteJob solrpendientejob  order by solrpendientejob.id desc");
        	query.setMaxResults(cuantos);
        	List<SolrPendienteJob> jobs= new ArrayList<SolrPendienteJob>();
        	List<Object[]> lvalores = query.list();
        	for(int i = 0 ; i < lvalores.size() ; i++) {
        		Object[] valores = lvalores.get(i);
        		SolrPendienteJob job = new SolrPendienteJob();
        		if (valores[0] != null) { job.setId(Long.valueOf(valores[0].toString())); }
        		if (valores[1] != null) { job.setFechaIni((Date) valores[1]); }
        		if (valores[2] != null) { job.setFechaFin((Date) valores[2]); }
        		if (valores[3] != null) { job.setTipo(valores[3].toString()); }
        		if (valores[4] != null) { job.setIdElem(Long.valueOf(valores[4].toString())); }
        		jobs.add(job);
        	}
        	return jobs;
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }
    
    /**
     * Devuelve un solrPendienteJob a partir de su id.
     * 
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     * @ejb.transaction type="RequiresNew"
     *
     * @return Devuelve un listado de todos los SolrPendientes.
     */
    public SolrPendienteJob obtenerSolrPendienteJob(Long id) {
    	Session session = this.getSession();
		try {
			SolrPendienteJob solrPendienteJob = (SolrPendienteJob) session.get(SolrPendienteJob.class, id);
			return solrPendienteJob;

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
    }
    

    
    
    /**
     * Método que se encarga de realizar las acciones segun si ha sido correcto o no.
     * @param solrpendiente
     * 
     * @throws HibernateException
     * @ejb.interface-method
   	 * @ejb.permission unchecked="true"
	 * @ejb.transaction type="RequiresNew" 
   	 */
    public void actualizarSolrPendiente(final SolrPendiente solrpendiente)  {
    	Session session = null;
    	try {
	    	 	session = getSession();
	    	 	//Para evitar problemas por tamaño demasiado grande.
	    	 	if (solrpendiente.getMensajeError() != null && solrpendiente.getMensajeError().length() > 300) {
	    	 		solrpendiente.setMensajeError(solrpendiente.getMensajeError().substring(0, 299));
	    	 	}
	    	 	session.update(solrpendiente);
	    	 	session.flush();
    	} catch(Exception e) {
    		log.error("Error resolver pendiente", e);
    	} finally {
    		session.close(); 
    	}
	}


    /**
	 * Marca todas las tareas como finalizadas. 
	 * 
   	 * @ejb.interface-method
     * @ejb.permission unchecked="true"     
     * @return 
     * @throws DelegateException 
   	 */
     public void finalizarTodo() throws DelegateException {
    	 Session session = null;
    	 try {
			 session = getSession();
			 //session.createQuery("update SolrPendienteJob set fechaFin = sysdate where fechaFin is null").uniqueResult();
			 List<SolrPendienteJob> jobs = session.createQuery("Select solrjob from SolrPendienteJob solrjob where solrjob.fechaFin is null").list();
			 for(SolrPendienteJob job : jobs) {
				 job.setFechaFin(new Date());
				 session.update(job);
			 }
			 session.flush();
		 }  catch (Exception e) {
				throw new DelegateException(e);
		 }
    }
    
     /**
 	 * Borra los jobs antiguos.
 	 * 
     * @ejb.interface-method
     * @ejb.permission unchecked="true"     
     * @throws DelegateException 
     */
     public void limpiezaJobs(final Long minimoId) throws DelegateException {
     	 Session session = null;
     	 try {
 			 session = getSession();
 			 List<SolrPendienteJob> jobs = session.createQuery("Select solrjob from SolrPendienteJob solrjob where solrjob.id < " + minimoId).list();
 			 for(SolrPendienteJob job : jobs) {
 				session.delete(job);
 			 }
 			 session.flush();
 		 }  catch (Exception e) {
 				throw new DelegateException(e);
 		 }
     }
     
    
     
     
     
}