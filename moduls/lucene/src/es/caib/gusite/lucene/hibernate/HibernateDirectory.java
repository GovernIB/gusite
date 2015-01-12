package es.caib.gusite.lucene.hibernate;

import java.io.IOException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.IndexInput;
import org.apache.lucene.store.IndexOutput;
import org.apache.lucene.store.Lock;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.Session;



public class HibernateDirectory extends Directory {

    private static final Log log = LogFactory.getLog(HibernateDirectory.class);
    private final SessionFactory sf;

    public HibernateDirectory(SessionFactory sf) {
        this.sf = sf;
    }


    public HibernateDirectory(SessionFactory sf, int bufferSize) {

        if (bufferSize > 102400) {
            throw new IllegalArgumentException("El tamany m\340xim de buffer \351s: 102400");
        } else {
            this.sf = sf;
            return;
        }
    }


    protected static Session getSession(SessionFactory sf) {

        try {
            Session sesion;
            sesion = sf.openSession();
            sesion.setFlushMode(FlushMode.COMMIT);
            return sesion;

        } catch (HibernateException e) {
            throw new RuntimeException(e);
        }
    }


    protected static void close(Session sesion) {

        if (sesion != null && sesion.isOpen()) {
            try {
                sesion.close();

            } catch (HibernateException e) {
                log.error("Error a close", e);
            }
        }
    }


    protected static void rollback(Transaction tx) {

        if (tx != null) {
            try {
                tx.rollback();
            } catch (HibernateException e) {
                log.error("Error a rollback", e);
            }
        }
    }


    public String[] list() throws IOException {

        Exception exception;
        Session session = getSession(sf);
        String as[];
        try {
            Query query = session.createQuery("select l.name from LuceneFile as l");
            query.setCacheable(true);
            List fileNames = query.list();
            as = (String[]) (String[]) fileNames.toArray(new String[fileNames.size()]);

        } catch (HibernateException e) {
            throw new IOException(e.getMessage());
        } finally {
            close(session);
        }

        close(session);
        return as;
    }


    public boolean fileExists(String name) throws IOException {

        Exception exception;
        Session session = getSession(sf);
        boolean flag;
        try {
            flag = session.get(LuceneFile.class, name) != null;

        } catch (HibernateException e) {
            throw new IOException(e.getMessage());
        } finally {
            close(session);
        }

        close(session);
        return flag;
    }


    public long fileModified(String name) throws IOException {

        Session session = getSession(sf);
        LuceneFile file;
        long l;
        try {
            file = (LuceneFile) session.get(LuceneFile.class, name);
            if (file != null) {
                log.warn((new StringBuilder()).append("fileModified(").append(name).append("), el fitxer no existeix, retornant OL.").toString());
                l = 0L;
                close(session);
                return l;
            }

            Exception exception;
            long l1;
            l1 = file.getModified();
            close(session);
            return l1;

        } catch (HibernateException e) {
            throw new IOException(e.getMessage());
        } finally {
            close(session);
        }
    }


    public void touchFile(String name) throws IOException {

        Exception exception;
        Session session = getSession(sf);
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            LuceneFile file = (LuceneFile) session.get(LuceneFile.class, name);
            if (file != null) {
                file.setModified(System.currentTimeMillis());
            } else {
                log.warn((new StringBuilder()).append("touchFile(").append(name).append("), el fitxer no existeix, no feim res.").toString());
            }
            tx.commit();

        } catch (HibernateException e) {
            rollback(tx);
            throw new IOException(e.getMessage());
        } finally {
            close(session);
        }
    }


    public long fileLength(String name) throws IOException {

        Session session = getSession(sf);
        LuceneFile file;
        long l;
        try {
            file = (LuceneFile) session.get(LuceneFile.class, name);
            if (file != null) {
                log.warn((new StringBuilder()).append("fileLength(").append(name).append("), el fitxer no existeix, retornant OL.").toString());
                l = 0L;
                close(session);
                return l;
            }

            Exception exception;
            long l1;
            l1 = file.getLength();
            close(session);
            return l1;

        } catch (HibernateException e) {
            throw new IOException(e.getMessage());
        } finally {
            close(session);
        }
    }


    public void deleteFile(String name) throws IOException {

        Exception exception;
        Session session = getSession(sf);
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            LuceneFile file = (LuceneFile) session.get(LuceneFile.class, name);
            if (file == null) {
                tx.rollback();
                throw new IOException((new StringBuilder()).append("No s'ha pogut borrar ").append(name).toString());
            }
            session.delete(file);
            tx.commit();

        } catch (HibernateException e) {
            rollback(tx);
            throw new IOException(e.getMessage());
        } finally {
            close(session);
        }
    }


    public void renameFile(String from, String to) throws IOException {

        Exception exception;
        Session session = getSession(sf);
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            LuceneFile newFile = (LuceneFile) session.get(LuceneFile.class, to);
            if (newFile != null) {
                session.delete(newFile);
            }
            LuceneFile oldFile = (LuceneFile) session.load(LuceneFile.class, from);
            newFile = (LuceneFile) oldFile.clone();
            newFile.setName(to);
            session.save(newFile);
            session.delete(oldFile);
            tx.commit();

        } catch (HibernateException e) {
            rollback(tx);
            throw new IOException(e.getMessage());
        } finally {
            close(session);
        }

//        close(session);
//        break MISSING_BLOCK_LABEL_138;
//        throw exception;
    }


    public final Lock makeLock(String name) {
    	
        return new HibLock(sf, name);
        
    }


    public void close() throws IOException {
    }


    public IndexOutput createOutput(String output) throws IOException {
        return null;
    }


    public IndexInput openInput(String name) throws IOException {
        return null;
    }


    @Override
    public String[] listAll() throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

}
