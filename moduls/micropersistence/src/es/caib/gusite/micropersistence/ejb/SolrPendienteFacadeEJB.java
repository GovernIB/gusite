package es.caib.gusite.micropersistence.ejb;

import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ejb.CreateException;
import javax.ejb.EJBException;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import es.caib.gusite.micromodel.Agenda;
import es.caib.gusite.micromodel.Archivo;
import es.caib.gusite.micromodel.Contenido;
import es.caib.gusite.micromodel.Encuesta;
import es.caib.gusite.micromodel.Faq;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.Noticia;
import es.caib.gusite.micromodel.SolrPendiente;
import es.caib.gusite.micromodel.SolrPendienteJob;
import es.caib.gusite.micromodel.SolrPendienteResultado;
import es.caib.gusite.micromodel.Traduccion;
import es.caib.gusite.micromodel.TraduccionAgenda;
import es.caib.gusite.micromodel.TraduccionNoticia;
import es.caib.gusite.micropersistence.delegate.AgendaDelegate;
import es.caib.gusite.micropersistence.delegate.ArchivoDelegate;
import es.caib.gusite.micropersistence.delegate.ContenidoDelegate;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.EncuestaDelegate;
import es.caib.gusite.micropersistence.delegate.FaqDelegate;
import es.caib.gusite.micropersistence.delegate.MicrositeDelegate;
import es.caib.gusite.micropersistence.delegate.NoticiaDelegate;
import es.caib.gusite.micropersistence.delegate.SolrPendienteJobDelegate;
import es.caib.gusite.micropersistence.util.IndexacionUtil;
import es.caib.gusite.plugins.PluginException;
import es.caib.gusite.plugins.PluginFactory;
import es.caib.gusite.plugins.organigrama.OrganigramaProvider;
import es.caib.gusite.plugins.organigrama.UnidadListData;
import es.caib.gusite.utilities.property.GusitePropertiesUtil;
import es.caib.solr.api.SolrFactory;
import es.caib.solr.api.SolrIndexer;
import es.caib.solr.api.exception.ExcepcionSolrApi;
import es.caib.solr.api.model.types.EnumAplicacionId;
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
    public List<SolrPendienteJob> getListJobs(final int cuantos, final String tipoIndexacion) {

        Session session = getSession();
        try {
        	Query query = session.createQuery(" from SolrPendienteJob solrpendientejob where solrpendientejob.tipo = '"+tipoIndexacion+"' order by solrpendientejob.id desc");
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
	public SolrPendienteJob crearSorlPendienteJob(String tipo) {
		try
    	{
			final Session session = getSession();
			final SolrPendienteJob solrpendienteJob = new SolrPendienteJob();
	    	solrpendienteJob.setFechaIni(new Date());
	    	solrpendienteJob.setTipo(tipo);
	    	
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
        	Query query = session.createQuery(" from SolrPendienteJob solrpendientejob  order by solrpendientejob.id desc");
        	query.setMaxResults(cuantos);
        	return query.list();
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }
}
