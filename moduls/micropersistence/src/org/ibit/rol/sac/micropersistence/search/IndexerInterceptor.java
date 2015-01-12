package org.ibit.rol.sac.micropersistence.search;


import net.sf.hibernate.Interceptor;
import net.sf.hibernate.CallbackException;
import net.sf.hibernate.type.Type;

import java.io.Serializable;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ibit.rol.sac.micromodel.Indexable;
import org.ibit.rol.sac.micropersistence.delegate.DelegateException;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;
import org.ibit.rol.sac.micropersistence.delegate.IndexerDelegate;


/**
 * Clase IndexerInterceptor implementa Interceptor
 * @author INDRA 
 */
public class IndexerInterceptor implements Interceptor {

    protected static final Log log = LogFactory.getLog(IndexerInterceptor.class);
    private final IndexerDelegate indexdel;

    public IndexerInterceptor() {
    	indexdel = DelegateUtil.getIndexerDelegate();
    }

    public boolean onLoad(Object entity,
                          Serializable id,
                          Object[] state,
                          String[] propertyNames,
                          Type[] types) throws CallbackException {
        return false;
    }

    public boolean onFlushDirty(Object entity,
                                Serializable id,
                                Object[] currentState,
                                Object[] previousState,
                                String[] propertyNames,
                                Type[] types) throws CallbackException {
    	if (entity instanceof Indexable) {
	    	try {
				indexdel.indexarObjeto(entity);
			} catch (DelegateException e) {
				log.error("onFlushDirty: " + e.getMessage());
			}
    	}
        return false;
    }

    public boolean onSave(Object entity,
                          Serializable id,
                          Object[] state,
                          String[] propertyNames,
                          Type[] types) throws CallbackException {
    	if (entity instanceof Indexable) {
	    	try {
				indexdel.indexarObjeto(entity);
			} catch (DelegateException e) {
				log.error("onSave: " + e.getMessage());
			}
    	}
        return false;
    }

    public void onDelete(Object entity,
                         Serializable id,
                         Object[] state,
                         String[] propertyNames,
                         Type[] types) throws CallbackException {
    	if (entity instanceof Indexable) {
	    	try {
				indexdel.desindexarObjeto(entity);
			} catch (DelegateException e) {
				log.error("onDelete: " + e.getMessage());
			}
    	}
    }

    public void preFlush(Iterator entities) throws CallbackException {
    }

    public void postFlush(Iterator entities) throws CallbackException {
    }

    public Boolean isUnsaved(Object entity) {
        return null;
    }

    public int[] findDirty(Object entity,
                           Serializable id,
                           Object[] currentState,
                           Object[] previousState,
                           String[] propertyNames,
                           Type[] types) {
        return null;
    }

    public Object instantiate(Class clazz,
                              Serializable id) throws CallbackException {
        return null;
    }
}
