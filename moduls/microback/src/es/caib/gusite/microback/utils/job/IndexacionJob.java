package es.caib.gusite.microback.utils.job;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.InterruptableJob;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.quartz.UnableToInterruptJobException;

import es.caib.gusite.micromodel.SolrPendienteJob;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.SolrPendienteDelegate;
import es.caib.gusite.micropersistence.delegate.SolrPendienteProcesoDelegate;
import es.caib.gusite.utilities.job.GusiteJobUtil;
import es.caib.gusite.utilities.property.GusitePropertiesUtil;


/***
 * la indexación. 
 * @author slromero
 *
 */
public class IndexacionJob implements Job, InterruptableJob  {

	/** El log. **/
	protected static Log log = LogFactory.getLog(GusiteJobUtil.class);

	
	
	/***
	 * Método para ejcutar el job.
	 */
    public void execute(JobExecutionContext context) throws JobExecutionException
    {
    	SchedulerContext schedulerContext;
    	SolrPendienteJob solrPendienteJob = null;
    	
    	try {
			schedulerContext = context.getScheduler().getContext();
		} catch (SchedulerException e) {
			log.debug("Error extrayendo el contexto en el job.", e);
			return;
		}
    	final String tipoIndexacion = (String) schedulerContext.get("tipoindexacion");
        
    	SolrPendienteDelegate solrPendienteDelegate = DelegateUtil.getSolrPendienteDelegate();
    	SolrPendienteProcesoDelegate solrProcesoDelegate = DelegateUtil.getSolrPendienteProcesoDelegate();
    	
    	//PASO 0. EXTRAER MICROSITE SI ES UN JOB.
    	Long idElemento = null;
		if (tipoIndexacion != null && "IDX_MIC".equals(tipoIndexacion)) {
			idElemento = (Long) schedulerContext.get("idMicrosite");
    	}
		if (tipoIndexacion != null && "IDX_UA".equals(tipoIndexacion)) {
			idElemento = (Long) schedulerContext.get("idUAdministrativa");;
    	}
		
    	///PASO 1. GUARDAR EL JOB.
    	try {    		 
			 solrPendienteJob = solrPendienteDelegate.crearSorlPendienteJob(tipoIndexacion, idElemento);
		} catch (DelegateException e) {
			log.debug("Error creando el job en bbdd.", e);
			return;
		}
    	
    	
    	//PASO 2. EJECUTAR LA INDEXACIÓN.
        try { 
        	GusiteJobUtil.interrumpirTarea = false;
	    	switch(tipoIndexacion) {
	    		case "IDX_TODO":
	    			solrProcesoDelegate.indexarTodo(solrPendienteJob);
	    			break;
	    		case "IDX_UA":
	    			solrProcesoDelegate.indexarMicrositeByUA(idElemento.toString(),solrPendienteJob);
	    			break;
	    		case "IDX_PDT":
	    			solrProcesoDelegate.indexarPendientes(solrPendienteJob);
	    			break;
	    		case "IDX_MIC":
	    			solrProcesoDelegate.indexarMicrosite(idElemento,solrPendienteJob, null);
	    			break;    			
	    		default:
	    			break;
	    	}
    	} catch (Exception e) {
    		log.error("Error intentando indexar" , e);
    	}
    	
    	//Entrar solo si es no pendientes
    	//PASO 3. CERRAR JOB.
        try {
        	solrPendienteDelegate.cerrarSorlPendienteJob(solrPendienteJob);
		} catch (DelegateException e) {
			log.error("Error cerrando el job", e);
		}
        
        ///PASO 4. LIMPIAR LOS JOBS obteniendo el mínimo id.
        try {
	        final int tamanyoMaximo = GusitePropertiesUtil.getTamanyoMaximoJobs();
	        final List<SolrPendienteJob> jobs = solrPendienteDelegate.getListJobs(tamanyoMaximo);
	        Long minimoId = GusitePropertiesUtil.minimoIdMax;
	        for(final SolrPendienteJob job : jobs) {
	        	if (job.getId() < minimoId) {
	        		minimoId = job.getId();
	        	}
	        }
	        
	        solrProcesoDelegate.limpiezaJobs(minimoId);
        } catch (DelegateException e) {
			log.error("Error borrando jobs", e);
		}
        
    }
    
    /**
     * Para interrumpir una tarea.
     * @throws UnableToInterruptJobException
     */
    public void interrupt() throws UnableToInterruptJobException {
    	log.error("Intentando interrumpir a la fuerza la tarea");
    	GusiteJobUtil.interrumpirTarea = true;
    }
    
   
}

