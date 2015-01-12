package org.ibit.rol.sac.microback.utils.job;

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
 * Job indexador de microsites
 * 
 * @author Indra
 * 
 */
public class IndexadorSchedulerServlet implements Servlet {

	private Log log = LogFactory.getLog(IndexadorSchedulerServlet.class);

	public void init(ServletConfig conf) throws ServletException {

		try {
			SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();
			Scheduler sched = schedFact.getScheduler();
			sched.start();
			JobDetail jobDetail = new JobDetail("Indexador", "Generadores",
					GeneraIndexador.class);
			CronTrigger trigger = new CronTrigger("Indexador", "Generadores");
			trigger.setCronExpression("0 0 5 ? * MON-FRI"); // de Lunes a
															// Viernes a las 6
															// a.m.
			sched.scheduleJob(jobDetail, trigger);
			log.info("Job Indexador programado...");
		} catch (Exception ex) {
			log.error(
					"Error al programar Job GeneraIndexador: "
							+ ex.getMessage(), ex);
		}

		/*
		 * 
		 * Seconds 0-59 , - * / Minutes 0-59 , - * / Hours 0-23 , - * /
		 * Day-of-month 1-31 , - * ? / L W C Month 1-12 or JAN-DEC , - * /
		 * Day-of-Week 1-7 or SUN-SAT , - * ? / L C # Year (Optional) empty,
		 * 1970-2099 , - * / "0 0 12 * * ?" Fire at 12pm (noon) every day
		 * "0 15 10 ? * *" Fire at 10:15am every day "0 15 10 * * ?" Fire at
		 * 10:15am every day "0 15 10 * * ? *" Fire at 10:15am every day
		 * "0 15 10 * * ? 2005" Fire at 10:15am every day during the year 2005
		 * "0 * 14 * * ?" Fire every minute starting at 2pm and ending at
		 * 2:59pm, every day "0 0/5 14 * * ?" Fire every 5 minutes starting at
		 * 2pm and ending at 2:55pm, every day "0 0/5 14,18 * * ?" Fire every 5
		 * minutes starting at 2pm and ending at 2:55pm, AND fire every 5
		 * minutes starting at 6pm and ending at 6:55pm, every day
		 * "0 0-5 14 * * ?" Fire every minute starting at 2pm and ending at
		 * 2:05pm, every day "0 10,44 14 ? 3 WED" Fire at 2:10pm and at 2:44pm
		 * every Wednesday in the month of March. "0 15 10 ? * MON-FRI" Fire at
		 * 10:15am every Monday, Tuesday, Wednesday, Thursday and Friday
		 * "0 15 10 15 * ?" Fire at 10:15am on the 15th day of every month
		 * "0 15 10 L * ?" Fire at 10:15am on the last day of every month
		 * "0 15 10 ? * 6L" Fire at 10:15am on the last Friday of every month
		 * "0 15 10 ? * 6L" Fire at 10:15am on the last Friday of every month
		 * "0 15 10 ? * 6L 2002-2005" Fire at 10:15am on every last friday of
		 * every month during the years 2002, 2003, 2004 and 2005
		 * "0 15 10 ? * 6#3" Fire at 10:15am on the third Friday of every month
		 */

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
