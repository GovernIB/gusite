package es.caib.gusite.microback.utils.job;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Job que realiza el proceso diario de generar el diccionario para el "quiso
 * decir"
 *
 * @author Indra
 *
 */
public class GeneraDiccionarios implements Job {

	private final Log log = LogFactory.getLog(GeneraDiccionarios.class);

	@Override
	public void execute(final JobExecutionContext context) throws JobExecutionException {

		try {

			final String value = System.getProperty("es.caib.webcaib.principal");

			if ((value != null) && value.equals("S")) {

				// Regeneramos los diccionarios
				log.info("Inicio CRON confeccion de diccionarios");

				// TODO HABRÁ QUE REENFOCAR COMO COMPORTARNOS CON LOS JOBS.

				log.info("Fin CRON confección de diccionarios");

			} else {

				log.info("El JBoss donde está la aplicación no es el principal.");

			}

		} catch (final Exception ex) {

			log.error("Error al realizar Job GeneraDiccionarios: " + ex.getMessage(), ex);

		}

	}

}
