package org.ibit.rol.sac.microfront.estadistica.util;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ibit.rol.sac.micromodel.Estadistica;

/**
 * Clase que inicializa el traductor y lo guarda como atributo de
 * contexto a partir de una propiedad de sistema 
 * @author Indra
 *
 */
public class BufferStatInitializer implements ServletContextListener {

	protected static Log log = LogFactory.getLog(BufferStatInitializer.class);

	/**
	 * M�todo que inicializa y guarda el un atributo en contexto para utilizarlo 
	 * como buffer de estad�sticas
	 * Este buffer se utiliza debido a incidencia a la hora de guardar estad�sticas 
	 * en horario de mantenimiento
	 * 
	 * @param event	evento de contexto
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent event) {

		try {
			List<Estadistica> bufferEstadisticas = new ArrayList<Estadistica>();
			event.getServletContext().setAttribute("bufferStats", bufferEstadisticas);

		} catch (Exception e) {
			log.warn("El buffer d' estad�stiques no s'ha carregat correctament");
		}

	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent event) {
		// no es necesario implementar c�digo
	}	

}
