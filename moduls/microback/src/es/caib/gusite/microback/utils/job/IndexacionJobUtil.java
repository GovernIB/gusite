package es.caib.gusite.microback.utils.job;

import java.util.Calendar;
import java.util.List;

import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerUtils;
import org.quartz.impl.StdSchedulerFactory;

import es.caib.gusite.micromodel.SolrPendienteJob;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.SolrPendienteDelegate;
import es.caib.gusite.micropersistence.delegate.SolrPendienteProcesoDelegate;
import es.caib.gusite.micropersistence.util.IndexacionUtil;

/**
 * Clase de apoyo para realizar tareas de indexación con quartz.
 * @author slromero
 *
 */
public class IndexacionJobUtil {
	
	/**
     *  Crea el job.
     * @param tipoIndexacion
     * @throws SchedulerException 
     *  
     */
    public static void crearJob(final String tipoIndexacion, final String idUAdministrativa, final Long idMicrosite) throws Exception  {
    	
    	//Se ha simplificado, se verán los últimos jobs ejecutados y, si alguno de ellos está sin fecha fin
    	//  se da por hecho que se está ejecutando.
    	if (existeJobAbierto(null, tipoIndexacion)) {
    			throw new Exception("Se está ejecutando un job, intentelo más tarde");
    	}
    	
    	Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler(); 
    	scheduler.start(); 
    	JobDetail jobDetail = new JobDetail("IndexacionJob"+tipoIndexacion, Scheduler.DEFAULT_GROUP, IndexacionJob.class);
    	Trigger trigger = TriggerUtils.makeImmediateTrigger(0, 0); 
    	scheduler.getContext().put("tipoindexacion", tipoIndexacion);
    	if (idUAdministrativa != null && !idUAdministrativa.isEmpty()) {
    		scheduler.getContext().put("idUAdministrativa", idUAdministrativa);
    	}
    	if (idMicrosite != null) {
    		scheduler.getContext().put("idMicrosite", idMicrosite);
    	}
    	
        trigger.setName("FireOnceNowTrigger");  
    	scheduler.scheduleJob(jobDetail, trigger);    	
    	
    }
    
    
    /**
     *  Crea el job con tiempo, no se comprueba si hay otro job.
     * @param tipoIndexacion
     * @throws SchedulerException 
     *  
     */
    public static void crearJobTiempo(final int tiempoEspera) throws Exception  {
    	
    	final Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler(); 
    	scheduler.start(); 
    	final Calendar calendar = Calendar.getInstance();
    	calendar.add(Calendar.MINUTE, tiempoEspera);
    	final String aleatorio = String.valueOf(calendar.getTimeInMillis());
    	final JobDetail jobDetail = new JobDetail("IndexacionJob" + IndexacionUtil.TIPO_TODO_SIN_INDEXAR+aleatorio, Scheduler.DEFAULT_GROUP, IndexacionJob.class);
    	final SimpleTrigger trigger = new SimpleTrigger("triggerJobSinIndexar",
                null,
                calendar.getTime(),
                null,
                0,
                1); 
    	scheduler.getContext().put("tipoindexacion", IndexacionUtil.TIPO_TODO_SIN_INDEXAR);
    	
    	
        trigger.setName("FireOnceNowTrigger"+aleatorio);  
    	scheduler.scheduleJob(jobDetail, trigger);    	
    	
    }
	

	


	/**
     * Mata los jobs el job.
     * @param tipoIndexacion
     * @throws Exception 
     */
    public static void matarJobs(final String tipoIndexacion) throws Exception  {
    		
    	//Paso 1. Parar jobs.
    	List<JobExecutionContext> currentlyExecuting = StdSchedulerFactory.getDefaultScheduler().getCurrentlyExecutingJobs();
    	//verifying if job is running       
    	for (JobExecutionContext jobExecutionContext : currentlyExecuting) {
    	    if(jobExecutionContext.getJobDetail().getKey().getName().startsWith("IndexacionJob")){
    	    	StdSchedulerFactory.getDefaultScheduler().interrupt(jobExecutionContext.getJobDetail().getKey().getName(), Scheduler.DEFAULT_GROUP);
    	    }
    	}
    	

    	//Paso 2. Marcar jobs como finalizados.
    	//Se ha simplificado, se verán los últimos jobs ejecutados y, si alguno de ellos está sin fecha fin
    	//  se da por hecho que se está ejecutando.
    	DelegateUtil.getSolrPendienteDelegate().finalizarTodo();
    	
    
    }


    /**
     * Tarea que se encarga de comprobar si hay alguna tarea abierta. 
     * @return
     * @throws SchedulerException 
     * @throws DelegateException 
     */
	public static boolean existeJobAbierto(JobExecutionContext context, String tipoIndexacion) throws SchedulerException, DelegateException {
		
		boolean existeJobAbierto = false; //StdSchedulerFactory.getDefaultScheduler().getJobGroupNames() //[Generadores, DEFAULT]
		
		//Paso 1. Buscar los jobs abiertos en el quartz. 
    	List<JobExecutionContext> currentlyExecuting = StdSchedulerFactory.getDefaultScheduler().getCurrentlyExecutingJobs();
    	//verifying if job is running       
    	for (JobExecutionContext jobExecutionContext : currentlyExecuting) {
    	    if(jobExecutionContext.getJobDetail().getKey().getName().startsWith("IndexacionJob")){
    	    	if (context == null) {
	    	    	existeJobAbierto = true;
	    	    	break;
    	    	} else if (!context.getJobDetail().getKey().getName().equals(jobExecutionContext.getJobDetail().getKey().getName())) {
    	    		existeJobAbierto = true;
	    	    	break;
    	    	}
    	    }
    	}
    	

    	//Paso 2. Marcar jobs que no estén finalizados.
    	if (!existeJobAbierto) {
	    	List<SolrPendienteJob> tareas = DelegateUtil.getSolrPendienteDelegate().getListJobs(15);
	    	for(SolrPendienteJob job : tareas) {
	    		if (job != null && job.getFechaFin() == null) {
	    			existeJobAbierto = true;
	    			break;
	    		}
	    	}
    	}
    	
    	//Paso 3. Si no somos del tipo todo sin indexar, comprobamos si hay alguna tarea pendiente
    	if (!existeJobAbierto) {
    		//if (!tipoIndexacion.equals(IndexacionUtil.TIPO_TODO_SIN_INDEXAR)) {
	    		String[] tareas = StdSchedulerFactory.getDefaultScheduler().getJobNames("DEFAULT");
	    		for (String tarea : tareas) {
	    			if (!tarea.contains(tipoIndexacion)) { //Comprobamos que no sea del mismo tipo
	    				existeJobAbierto = true;
	    			}
	    		}
    		//}
    	}
    	return existeJobAbierto;
	}
	

}
