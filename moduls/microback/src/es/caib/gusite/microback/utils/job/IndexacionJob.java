package es.caib.gusite.microback.utils.job;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;

import es.caib.gusite.micromodel.SolrPendienteJob;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.SolrPendienteDelegate;
import es.caib.gusite.micropersistence.delegate.SolrPendienteProcesoDelegate;


/***
 * la indexación. 
 * @author slromero
 *
 */
public class IndexacionJob implements Job  {

	/** El log. **/
	protected static Log log = LogFactory.getLog(IndexacionJob.class);

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
    	SolrPendienteProcesoDelegate solrDelegate = DelegateUtil.getSolrPendienteProcesoDelegate();
    	
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
	    	switch(tipoIndexacion) {
	    		case "IDX_TODO":
	    			solrDelegate.indexarTodo(solrPendienteJob);
	    			break;
	    		case "IDX_UA":
	    			solrDelegate.indexarMicrositeByUA(idElemento.toString(),solrPendienteJob);
	    			break;
	    		case "IDX_PDT":
	    			solrDelegate.indexarPendientes(solrPendienteJob);
	    			break;
	    		case "IDX_MIC":
	    			solrDelegate.indexarMicrosite(idElemento,solrPendienteJob, null);
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
    	
    }
    
   
}

