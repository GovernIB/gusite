package es.caib.gusite.micropersistence.util;

import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Interceptor;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Environment;

import es.caib.gusite.micropersistence.search.IndexerInterceptor;

/**
 * Clase HibernateLocator
 * 
 * @author INDRA
 */
public class HibernateLocator {

	protected static Log log = LogFactory.getLog(HibernateLocator.class);

	private static SessionFactory sf;

	private HibernateLocator() {
		sf = initSessionFactory();
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		sf.close();
	}

	// Singleton
	private static HibernateLocator instance;

	public static HibernateLocator getInstance() {
		if (instance == null) {
			synchronized (HibernateLocator.class) {
				if (instance == null) {
					instance = new HibernateLocator();
				}
			}
		}
		return instance;
	}

	public static SessionFactory getSessionFactory() {
		getInstance();
		return HibernateLocator.sf;
	}

	public static void resetSessionFactory() {
		sf = initSessionFactory();
	}

	public static SessionFactory initSessionFactory() {
		try {
			System.setProperty(Environment.USE_STREAMS_FOR_BINARY, "true");
		} catch (SecurityException e) {
			log.warn("No s'ha pogut fixar la propietat "
					+ Environment.USE_STREAMS_FOR_BINARY);
		}
		try {
			ClassLoader cl = Thread.currentThread().getContextClassLoader();
			URL url = cl.getResource("hibernate-microsites.cfg.xml");
			Interceptor interceptor = new ChainedInterceptor(
					new Interceptor[] { new IndexerInterceptor() });

			// Configuration cfg = new
			// Configuration().configure(url).setInterceptor(interceptor);
			AnnotationConfiguration cfg = new AnnotationConfiguration();
			cfg.configure(url).setInterceptor(interceptor);

			return cfg.buildSessionFactory();
		} catch (HibernateException e) {
			log.error("error de hibernate " + e.getMessage());
			throw new RuntimeException(e);
		}
	}

}
