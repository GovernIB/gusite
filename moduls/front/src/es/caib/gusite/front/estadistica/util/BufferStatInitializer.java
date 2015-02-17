package es.caib.gusite.front.estadistica.util;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.gusite.micromodel.Estadistica;

/**
 * Clase que inicializa el traductor y lo guarda como atributo de contexto a
 * partir de una propiedad de sistema
 * 
 * @author Indra
 * 
 */
public class BufferStatInitializer implements ServletContextListener {

	protected static Log log = LogFactory.getLog(BufferStatInitializer.class);

	/**
	 * Método que inicializa y guarda el un atributo en contexto para utilizarlo
	 * como buffer de estadísticas Este buffer se utiliza debido a incidencia a
	 * la hora de guardar estadísticas en horario de mantenimiento
	 * 
	 * @param event
	 *            evento de contexto
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent event) {

		try {
			List<Estadistica> bufferEstadisticas = new ArrayList<Estadistica>();
			event.getServletContext().setAttribute("bufferStats",
					bufferEstadisticas);

		} catch (Exception e) {
			log.warn("El buffer d' estadístiques no s'ha carregat correctament");
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.
	 * ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		// no es necesario implementar código
	}

}
