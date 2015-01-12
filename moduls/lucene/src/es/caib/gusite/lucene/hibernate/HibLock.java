package es.caib.gusite.lucene.hibernate;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.store.Lock;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.Session;


final class HibLock extends Lock {

    private static final Log log = LogFactory.getLog(HibLock.class);
    private final SessionFactory sf;
    private final String name;


    public HibLock(SessionFactory sf, String name) {
        this.sf = sf;
        this.name = (new StringBuilder()).append("LOCK_").append(name).toString();
    }


    public boolean obtain() throws IOException {

        Exception exception;
        Session session = HibernateDirectory.getSession(sf);
        Transaction tx = null;
        boolean flag;
        try {
            tx = session.beginTransaction();
            boolean result = false;
            if (session.get(LuceneFile.class, name) == null) {
                LuceneFile file = new LuceneFile(name);
                session.save(file);
                log.info((new StringBuilder()).append("Obtained lock ").append(name).toString());
                result = true;
            }
            tx.commit();
            flag = result;
            HibernateDirectory.close(session);
            return flag;

        } catch (HibernateException e) {
            HibernateDirectory.rollback(tx);
            throw new IOException(e.getMessage());
        } finally {
            HibernateDirectory.close(session);
        }
//        throw exception;
    }


    public void release() {

        Session session = HibernateDirectory.getSession(sf);;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            LuceneFile file = (LuceneFile)session.get(LuceneFile.class, name);
            if (file != null) {
                session.delete(file);
                log.info((new StringBuilder()).append("Released lock ").append(name).toString());
            }
            tx.commit();

        } catch (HibernateException e) {
            HibernateDirectory.rollback(tx);
            log.error("on release ", e);
            HibernateDirectory.close(session);
//            break MISSING_BLOCK_LABEL_120;
        } catch (Exception exception) {
            HibernateDirectory.close(session);
//            throw exception;
        }
    }


    public boolean isLocked() {

        Exception exception;
        Session session = HibernateDirectory.getSession(sf);
        boolean flag;
        try {
            flag = session.get(LuceneFile.class, name) != null;
            HibernateDirectory.close(session);
            return flag;

        } catch (HibernateException e) {
            throw new RuntimeException("Error a isLocked", e);
        } finally {
            HibernateDirectory.close(session);
        }
//        throw exception;
    }

}
