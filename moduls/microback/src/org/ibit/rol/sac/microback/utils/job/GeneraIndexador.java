package org.ibit.rol.sac.microback.utils.job;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ibit.rol.sac.micromodel.Tipo;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;
import org.ibit.rol.sac.micropersistence.delegate.IndexerDelegate;
import org.ibit.rol.sac.micropersistence.delegate.TipoDelegate;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;


/**
 * Job que realiza el proceso diario de indexar el buscador
 * 
 *@author Indra
 *
 */
public class GeneraIndexador implements Job {

	
	private Log log = LogFactory.getLog( GeneraIndexador.class  );
	
	public void execute(JobExecutionContext context) throws JobExecutionException
	{
			
		try{
			String value = System.getProperty("es.caib.webcaib.principal");
			if ((value != null) && value.equals("S")) {
			
				log.info("Inicio CRON indexacion microsites");
				
				//Solo se indexaran los microsites que tienen listas con buscador
				Hashtable<Long, Long> microsites = new Hashtable<Long, Long>();
				TipoDelegate tipodel=DelegateUtil.getTipoDelegate();
				tipodel.init();tipodel.setTampagina(10000);tipodel.setPagina(1);
				Iterator<?> iter = tipodel.listarTipos().iterator();
				while (iter.hasNext()) {
					Tipo tipo = (Tipo)iter.next();
					if (tipo.getBuscador().equals("S"))
					microsites.put(tipo.getIdmicrosite(),tipo.getIdmicrosite());
				}
				
				//una vez tenemos la lista de microsites a indexar... al ataqueerrrr
				IndexerDelegate ind=DelegateUtil.getIndexerDelegate();
				Enumeration<Long> enumkeys = microsites.keys();
				while (enumkeys.hasMoreElements()) {
					Long idsite = (Long)enumkeys.nextElement();
					ind.reindexarMicrosite(idsite);
				}
	
		        log.info("Fin CRON indexacion microsites");
			} else {
				log.info("El jboss en donde está la aplicación no es el principal.");
			}
		}catch (Exception ex){
			log.error("Error al realizar Job GeneraIndexador: " + ex.getMessage(),ex);			
		}		
		
	}
}
