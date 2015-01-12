package es.caib.gusite.microfront.job;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;


/**
 * Action GeneraMenu. Job utilizado para refrescar la cabecera de los microsites.
 *  Definición Struts:<BR>
 *  action path="/home" <BR> 
 *  unknown="true" <BR>
 *  forward name="plantilla1v4" path=/v4/home/home1.jsp <BR>
 *  forward name="plantilla1v1" path=/v1/home/home1.jsp
 * @author Indra
 *
 */
public class GeneraMenu implements Job {
	private Log log = LogFactory.getLog( GeneraMenu.class  );
	
	public void execute(JobExecutionContext context) throws JobExecutionException {
			
		try{
			
			MenuCabecera.refrescarMenu();
			
		}catch (Exception ex){
			log.error("Error al realizar el refresco automatico del menu de la cabecera del PortalCAIB en Microsites: " + ex.getMessage(),ex);			
		}		
		
	}
}
