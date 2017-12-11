package es.caib.gusite.microback.utils.job;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;

/**
 * Servlet encargado de arrancar el check de indexador.
 * Se arrancará cada hora.
 * 
 * @author Indra
 * 
 */
public class IndexadorCheckSchedulerServlet implements Servlet {

	private Log log = LogFactory.getLog(IndexadorSchedulerServlet.class);

	public void init(ServletConfig conf) throws ServletException {

		try {
			final SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();
			final Scheduler sched = schedFact.getScheduler();
			sched.start();
			final JobDetail jobDetail = new JobDetail("CheckIndexador", "Generadores", CheckerIndexacion.class);
			final CronTrigger trigger = new CronTrigger("CheckIndexador", "Generadores");
			//Para ejecutar cada 2 horas
			//trigger.setCronExpression("* * */2 * * ? "); 
			trigger.setCronExpression("0 0 * * * ? ");
			sched.scheduleJob(jobDetail, trigger);
			log.info("Job Checker Indexacion programado...");
		} catch (Exception ex) {
			log.error(
					"Error al programar Job GeneraIndexador: "
							+ ex.getMessage(), ex);
		}


	}

	public ServletConfig getServletConfig() {
		return null;
	}

	public void service(ServletRequest arg0, ServletResponse arg1)
			throws ServletException, IOException {
	}

	public String getServletInfo() {
		return null;
	}

	public void destroy() {
	}
}
