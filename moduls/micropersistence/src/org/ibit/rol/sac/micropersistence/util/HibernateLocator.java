package org.ibit.rol.sac.micropersistence.util;

import java.net.URL;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Interceptor;
import net.sf.hibernate.SessionFactory;
import net.sf.hibernate.cfg.Configuration;
import net.sf.hibernate.cfg.Environment;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ibit.rol.sac.micropersistence.search.IndexerInterceptor;

/**
 * Clase HibernateLocator
 * @author INDRA 
 */
public class HibernateLocator {

    protected static Log log = LogFactory.getLog(HibernateLocator.class);

    private static SessionFactory sf;

    private HibernateLocator() {
        sf = initSessionFactory();
    }

    protected void finalize() throws Throwable {
        super.finalize();
        sf.close();
    }

    // Singleton
    private static HibernateLocator instance;

    public static HibernateLocator getInstance() {
        if (instance == null) {
            synchronized(HibernateLocator.class) {
                if (instance == null) {
                    instance = new HibernateLocator();
                }
            }
        }
        return instance;
    }

    public static SessionFactory getSessionFactory() {
        return getInstance().sf;
    }
    
    public static void resetSessionFactory() {
        sf = initSessionFactory();
    }
   
    public static SessionFactory initSessionFactory() {
        try {
            System.setProperty(Environment.USE_STREAMS_FOR_BINARY, "true");
        } catch (SecurityException e) {
            log.warn("No s'ha pogut fixar la propietat " + Environment.USE_STREAMS_FOR_BINARY);
        }
        try {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            URL url = cl.getResource("hibernate-microsites.cfg.xml");
            Interceptor interceptor =
                new ChainedInterceptor(
                        new Interceptor[] {
                                new IndexerInterceptor() } );
            Configuration cfg = new Configuration().configure(url).setInterceptor(interceptor);            
            
            return cfg.buildSessionFactory();
        } catch (HibernateException e) {
        	log.error("error de hibernate " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
    
   
}
