package es.caib.gusite.microback.utils.job;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import es.caib.gusite.micromodel.SolrPendienteJob;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.SolrPendienteDelegate;
import es.caib.gusite.micropersistence.delegate.SolrPendienteProcesoDelegate;

/**
 * Job que realiza el proceso diario de indexar el buscador
 * 
 * @author Indra
 *
 */
public class GeneraIndexador implements Job {

	private Log log = LogFactory.getLog(GeneraIndexador.class);

	public void execute(JobExecutionContext context)
			throws JobExecutionException {

		log.debug("Job que se ejecuta para la Indexacion pendiente: inicio");
		try {
			SolrPendienteProcesoDelegate solrDelegate = DelegateUtil.getSolrPendienteProcesoDelegate();
			SolrPendienteDelegate solrPendienteDelegate = DelegateUtil.getSolrPendienteDelegate();
			
			SolrPendienteJob solrPendienteJob = solrPendienteDelegate.crearSorlPendienteJob("IDX_PDT", null);
			
			solrDelegate.indexarPendientes(solrPendienteJob);	
			
			solrPendienteDelegate.cerrarSorlPendienteJob(solrPendienteJob);
			
		} catch (Exception ex) {
			log.error("Error indexando pendientes", ex);
		}
		log.debug("Job que se ejecuta para la Indexacion pendiente: fin");

	}

}
