/**
 *
 */
package es.caib.gusite.microintegracion.listener.traductor;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.gusite.microintegracion.traductor.TraductorMicrosites;
import es.caib.gusite.micromodel.Microsite;

/**
 * Clase que inicializa el traductor y lo guarda como atributo de contexto a
 * partir de una propiedad de sistema
 *
 * @author Indra
 *
 */
public class TraductorInitializer implements ServletContextListener {

	protected static Log log = LogFactory.getLog(TraductorInitializer.class);

	/**
	 * Método que inicializa y guarda el traductor en contexto dependiendo de la
	 * propiedad de sistema "es.caib.rolsac.integracion.traductor" (valores S ó N).
	 * En el caso de que no exista la propiedad de sistema el traductor no se
	 * inicializa
	 *
	 * @param event
	 *            evento de contexto
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextInitialized(final ServletContextEvent event) {

		if (!traductorHabilitat())
			return;

		try {

			establecerTraductorEnAmbitoAplicacion(event);

		} catch (final Exception e) {
			e.printStackTrace();
			log.info("No s'ha trobat parámetre d' inicialització de traducció automática");
			log.info("Carregant Rolsac sense traducció automática");
		}

	}

	private void establecerTraductorEnAmbitoAplicacion(final ServletContextEvent event) throws Exception {
		final TraductorMicrosites traductor = crearTraductor();
		event.getServletContext().setAttribute("traductor", traductor);
		log.info("Carregant Microsites amb traducció automática");
	}

	private TraductorMicrosites crearTraductor() throws Exception {
		final TraductorMicrosites traductor = new TraductorMicrosites();
		return traductor;
	}

	private boolean traductorHabilitat() {
		return Microsite.traductorHabilitat();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.
	 * ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(final ServletContextEvent event) {
		// no es necesario implementar código
	}

}
