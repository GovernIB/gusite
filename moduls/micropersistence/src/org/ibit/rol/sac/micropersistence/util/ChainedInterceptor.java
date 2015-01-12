package org.ibit.rol.sac.micropersistence.util;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import net.sf.hibernate.Interceptor;
import net.sf.hibernate.CallbackException;
import net.sf.hibernate.type.Type;

/**
 * Implementation of the Hibernate <code>Interceptor</code>
 * interface that allows the chaining of several different
 * instances of the same interface.
 *
 * @author Indra
 * @see Interceptor
 */
public class ChainedInterceptor implements Interceptor {

    // Interceptors to be chained
    private Interceptor[] interceptors;

    /**
     * Constructor
     */
    public ChainedInterceptor() {
        super();
    }

    /**
     * Constructor
     *
     * @param interceptors array of interceptors
     */
    public ChainedInterceptor(Interceptor[] interceptors) {
        super();
        this.interceptors = interceptors;
    }

    /**
     * @see net.sf.hibernate.Interceptor#onLoad(Object,
     *      java.io.Serializable, Object[], String[],
     *      net.sf.hibernate.type.Type[])
     */
    public boolean onLoad(
            Object entity,
            Serializable id,
            Object[] state,
            String[] propertyNames,
            Type[] types) throws CallbackException {
        boolean result = false;
        for (int i = 0; i < interceptors.length; i++) {
            if (interceptors[i]
                    .onLoad(entity, id, state, propertyNames, types)) {
                /* Returns true if one interceptor in the chain has
                 * modified the object state
                 */
                result = true;
            }
        }
        return result;
    }

    /**
     * @see net.sf.hibernate.Interceptor#onFlushDirty(Object,
     *      java.io.Serializable, Object[], Object[],
     *      String[], net.sf.hibernate.type.Type[])
     */
    public boolean onFlushDirty(
            Object entity,
            Serializable id,
            Object[] currentState,
            Object[] previousState,
            String[] propertyNames,
            Type[] types) throws CallbackException {
        boolean result = false;
        for (int i = 0; i < interceptors.length; i++) {
            if (interceptors[i]
                    .onFlushDirty(
                            entity,
                            id,
                            currentState,
                            previousState,
                            propertyNames,
                            types)) {
                /* Returns true if one interceptor in the chain has modified
                 * the object current state
                 */
                result = true;
            }
        }
        return result;
    }

    /**
     * @see net.sf.hibernate.Interceptor#onSave(Object,
     *      java.io.Serializable, Object[], String[],
     *      net.sf.hibernate.type.Type[])
     */
    public boolean onSave(
            Object entity,
            Serializable id,
            Object[] state,
            String[] propertyNames,
            Type[] types) throws CallbackException {
        boolean result = false;
        for (int i = 0; i < interceptors.length; i++) {
            if (interceptors[i]
                    .onSave(entity, id, state, propertyNames, types)) {
                /* Returns true if one interceptor in the chain has
                 * modified the object state
                 */
                result = true;
            }
        }
        return result;
    }

    /**
     * @see net.sf.hibernate.Interceptor#onDelete(Object,
     *      java.io.Serializable, Object[], String[],
     *      net.sf.hibernate.type.Type[])
     */
    public void onDelete(
            Object entity,
            Serializable id,
            Object[] state,
            String[] propertyNames,
            Type[] types) throws CallbackException {
        for (int i = 0; i < interceptors.length; i++) {
            interceptors[i].onDelete(entity, id, state, propertyNames, types);
        }
    }

    /**
     * @see Interceptor#postFlush(Iterator)
     */
    public void postFlush(Iterator entities) throws CallbackException {
        List<?> entityList = createList(entities);
        for (int i = 0; i < interceptors.length; i++) {
            interceptors[i].postFlush(entityList.iterator());
        }
    }

    /**
     * @see Interceptor#preFlush(Iterator)
     */
    public void preFlush(Iterator entities) throws CallbackException {
        List<?> entityList = createList(entities);
        for (int i = 0; i < interceptors.length; i++) {
            interceptors[i].preFlush(entityList.iterator());
        }
    }

    /**
     * Creates and returns a new <code>List</code> containing all the
     * elements returned from the <code>Iterator</code>.
     *
     * @param iterator The iterator.
     * @return A <code>List</code> of the iterator's elements.
     */
    private List createList(Iterator iterator) {
        List list = new ArrayList<Object>();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        return list;
    }


    /**
     * @see net.sf.hibernate.Interceptor#isUnsaved(java.lang.Object)
     */
    public Boolean isUnsaved(Object entity) {
        Boolean result = null;
        for (int i = 0; i < interceptors.length; i++) {
            result = interceptors[i].isUnsaved(entity);
            if (result != null) {
                // If any interceptor has returned either true or false, stop the chain
                break;
            }
        }
        return result;
    }

    /**
     * @see net.sf.hibernate.Interceptor#findDirty(Object,
     *      java.io.Serializable, Object[], Object[],
     *      String[], net.sf.hibernate.type.Type[])
     */
    public int[] findDirty(
            Object entity,
            Serializable id,
            Object[] currentState,
            Object[] previousState,
            String[] propertyNames,
            Type[] types) {
        int[] result = null;
        for (int i = 0; i < interceptors.length; i++) {
            result =
                    interceptors[i].findDirty(
                            entity,
                            id,
                            currentState,
                            previousState,
                            propertyNames,
                            types);
            if (result != null) {
                /* If any interceptor has returned something not null,
                 * stop the chain
                 */
                break;
            }
        }
        return result;
    }

    /**
     * @see net.sf.hibernate.Interceptor#instantiate(Class,
     *      java.io.Serializable)
     */
    public Object instantiate(Class clazz, Serializable id) throws CallbackException {
        Object result = null;
        for (int i = 0; i < interceptors.length; i++) {
            result = interceptors[i].instantiate(clazz, id);
            if (result != null) {
                /* If any interceptor has returned something not null,
                 * stop the chain
                 */
                break;
            }
        }
        return result;
    }

    /**
     * Returns an array containing the instances of the <code>Interceptor</code>
     * interface that are chained within this interceptor.
     *
     * @return An array of interceptor
     */
    public Interceptor[] getInterceptors() {
        return interceptors;
    }

    /**
     * Sets  the instances of the <code>Interceptor</code> interface
     * that are chained within this interceptor.
     *
     * @param interceptors
     */
    public void setInterceptors(Interceptor[] interceptors) {
        this.interceptors = interceptors;
    }

}
