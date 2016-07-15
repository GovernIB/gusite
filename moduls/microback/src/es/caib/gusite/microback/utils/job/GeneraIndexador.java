package es.caib.gusite.microback.utils.job;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.SolrPendienteDelegate;

/**
 * Job que realiza el proceso diario de indexar el buscador
 * 
 *@author Indra
 *
 */
public class GeneraIndexador implements Job {

	private Log log = LogFactory.getLog(GeneraIndexador.class);

	public void execute(JobExecutionContext context) throws JobExecutionException {

		try {
			
			log.error("Job noctura que se ejecuta para la Indexacion pendiente.");	
			SolrPendienteDelegate solrDelegate = DelegateUtil.getSolrPendienteDelegate();
	    	try { 
	    		solrDelegate.indexarPendientes();
	    	} catch (DelegateException e) { log.error("Error indexando pendientes", e); }
	    	log.error("jobIndexacionNocturna..");
			
		} catch (Exception ex) {
			
			log.error("Error al realizar Job GeneraIndexador: " + ex.getMessage(), ex);
			
		}

	}
	
}
