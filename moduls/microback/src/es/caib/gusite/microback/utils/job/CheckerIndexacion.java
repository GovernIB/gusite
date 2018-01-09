package es.caib.gusite.microback.utils.job;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.impl.StdSchedulerFactory;

import es.caib.gusite.micromodel.SolrPendienteJob;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.MicrositeDelegate;
import es.caib.gusite.micropersistence.delegate.SolrPendienteDelegate;
import es.caib.gusite.micropersistence.delegate.SolrPendienteProcesoDelegate;
import es.caib.gusite.micropersistence.util.IndexacionUtil;

/**
 * Job que checkea que haya alguna indexacion pendiente. 
 * 
 * @author Indra
 *
 */
public class CheckerIndexacion implements Job {

	private Log log = LogFactory.getLog(GeneraIndexador.class);

	public void execute(JobExecutionContext context)
			throws JobExecutionException {

		try {
			//SolrPendienteProcesoDelegate solrDelegate = DelegateUtil.getSolrPendienteProcesoDelegate();
			MicrositeDelegate micrositeDelegate = DelegateUtil.getMicrositeDelegate();
			//Si no están todos indexados, entonces checkear.
			if (!micrositeDelegate.isTodosIndexados()) {
				
				boolean existeJobAbierto = false;
				
				
				//Comprobacion 1. Hay una tarea de quartz de los que se está ejecutando. 
		    	List<JobExecutionContext> currentlyExecuting = StdSchedulerFactory.getDefaultScheduler().getCurrentlyExecutingJobs();
		    	//verifying if job is running       
		    	for (JobExecutionContext jobExecutionContext : currentlyExecuting) {
		    		if(jobExecutionContext.getJobDetail().getKey().getName().startsWith("IndexacionJob" + IndexacionUtil.TIPO_TODO_SIN_INDEXAR)){
		    	    	existeJobAbierto = true;
		    	    }
		    	}

		    	//Comprobacion 2. Busco en el job de DEFAULT a ver si veo algo (a lo mejor con el primer check sobrra).
		    	if (!existeJobAbierto) {
	    			String[] tareas = StdSchedulerFactory.getDefaultScheduler().getJobNames("DEFAULT");
		    		for (String tarea : tareas) {
		    			if (tarea.startsWith("IndexacionJob" + IndexacionUtil.TIPO_TODO_SIN_INDEXAR)) {
		    				existeJobAbierto  = true;
		    			}
		    		}
	    		
		    	}
				
				if (!existeJobAbierto) {
					//Cerramos por bbdd las tareas no finiquitadas.
					DelegateUtil.getSolrPendienteDelegate().cerrarJobsPorFechaFin();
					
					//Directamente le ponemos el tiempo 1 minuto para que se indexe.
					IndexacionJobUtil.crearJobTiempo(1);
				}
				
				
			}
			
			
		} catch (Exception ex) {
			log.error("Error CheckerIndexacion comprobando si todos indexados o si hay tarea arrancada.", ex);
		}
		
	}

}
