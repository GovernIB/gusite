package es.caib.gusite.microback.utils.job;

import java.util.List;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerUtils;
import org.quartz.impl.StdSchedulerFactory;

import es.caib.gusite.micromodel.SolrPendienteJob;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.SolrPendienteDelegate;

public class IndexacionJobUtil {
	
	/**
     *  Crea el job.
     * @param tipoIndexacion
     * @throws SchedulerException 
     *  
   	 * @ejb.interface-method
   	 * @ejb.permission unchecked="true"
   	 * @ejb.transaction type="RequiresNew"
     */
    public static void crearJob(final String tipoIndexacion, final String idUAdministrativa, final Long idMicrosite) throws Exception  {
    	
    	//Se ha simplificado, se verán los últimos jobs ejecutados y, si alguno de ellos está sin fecha fin
    	//  se da por hecho que se está ejecutando.
    	SolrPendienteDelegate solrPendienteDel = DelegateUtil.getSolrPendienteDelegate();
    	List<SolrPendienteJob> jobs = solrPendienteDel.getListJobs(10, tipoIndexacion, null);
    	for(SolrPendienteJob job : jobs) {
    		if (job.getFechaFin() == null) {
    			throw new Exception("Se está ejecutando un job, intentelo más tarde");
    		}
    	}
    	
    	Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler(); 
    	scheduler.start(); 
    	JobDetail jobDetail = new JobDetail("IndexacionJob", Scheduler.DEFAULT_GROUP, IndexacionJob.class);
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
	

}
