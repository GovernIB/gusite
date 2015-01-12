package org.ibit.rol.sac.microback.utils.job;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;
import org.ibit.rol.sac.micropersistence.delegate.IndexerDelegate;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Job que realiza el proceso diario de generar el diccionario para el "quiso decir"
 * 
 * @author Indra
 *
 */
public class GeneraDiccionarios implements Job
{
	
	private Log log = LogFactory.getLog( GeneraDiccionarios.class  );
	
	public void execute(JobExecutionContext context) throws JobExecutionException
	{
			
		try{
			String value = System.getProperty("es.caib.webcaib.principal");
			if ((value != null) && value.equals("S")) {
				// Regeneramos los diccionarios
				log.info("Inicio CRON confeccion de diccionarios");
				IndexerDelegate ind=DelegateUtil.getIndexerDelegate();
		        List<?> langs = DelegateUtil.getIdiomaDelegate().listarLenguajes();
		       	
		        for (int i = 0; i < langs.size(); i++) {
		        		ind.confeccionaDiccionario(""+langs.get(i));
		       	}
		        log.info("Fin CRON confeccion de diccionarios");
			} else {
				log.info("El jboss en donde está la aplicación no es el principal.");
			}
		}catch (Exception ex){
			log.error("Error al realizar Job GeneraDiccionarios: " + ex.getMessage(),ex);			
		}		
		
	}
	
}	
