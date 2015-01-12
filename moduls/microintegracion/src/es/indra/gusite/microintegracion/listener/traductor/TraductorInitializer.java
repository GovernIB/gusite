/**
 * 
 */
package es.indra.gusite.microintegracion.listener.traductor;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ibit.rol.sac.micromodel.Microsite;

import es.indra.gusite.microintegracion.traductor.TraductorMicrosites;

/**
 * Clase que inicializa el traductor y lo guarda como atributo de
 * contexto a partir de una propiedad de sistema 
 * @author Indra
 *
 */
public class TraductorInitializer implements ServletContextListener {

	protected static Log log = LogFactory.getLog(TraductorInitializer.class);

	/**
	 * M�todo que inicializa y guarda el traductor en contexto dependiendo de la
	 * propiedad de sistema "es.caib.rolsac.integracion.traductor" (valores S � N).
	 * En el caso de que no exista la propiedad de sistema el traductor no se inicializa
	 * 
	 * @param event	evento de contexto
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent event) {

		if(!traductorHabilitat()) return;
		
		try {
			
			establecerTraductorEnAmbitoAplicacion(event);

		} catch (Exception e) {
			log.info("No s'ha trobat par�metre d' inicialitzaci� de traducci� autom�tica");
			log.info("Carregant Rolsac sense traducci� autom�tica");
		}

	}
	

	private void establecerTraductorEnAmbitoAplicacion(ServletContextEvent event) throws Exception {
			TraductorMicrosites traductor = crearTraductor();

			event.getServletContext().setAttribute("traductor", traductor);
			log.info("Carregant Microsites amb traducci� autom�tica");
			log.info("URL de servidor de traducci�: " + traductor.getTranslationServerUrl());
	}


	private TraductorMicrosites crearTraductor() throws Exception {
		TraductorMicrosites traductor = new TraductorMicrosites();
		
		String urlTraductor=obtenerServidorDeTraduccion();
		
		if ( urlTraductor != null) 
			traductor.setTranslationServerUrl(urlTraductor);
		return traductor;
	}

	
	
	private boolean traductorHabilitat() {
		return Microsite.traductorHabilitat();
	}
	
	

	private String obtenerServidorDeTraduccion() {
		return System.getProperty("es.caib.rolsac.integracion.traductor.servidor");
	}
	
	
	

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent event) {
		// no es necesario implementar c�digo
	}	

}
