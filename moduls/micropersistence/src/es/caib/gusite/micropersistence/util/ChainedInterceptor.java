package es.caib.gusite.micropersistence.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.CallbackException;
import org.hibernate.EntityMode;
import org.hibernate.Interceptor;
import org.hibernate.Transaction;
import org.hibernate.type.Type;

/**
 * Implementation of the Hibernate <code>Interceptor</code> interface that
 * allows the chaining of several different instances of the same interface.
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
	 * @param interceptors
	 *            array of interceptors
	 */
	public ChainedInterceptor(Interceptor[] interceptors) {
		super();
		this.interceptors = interceptors;
	}

	/**
	 * @see net.sf.hibernate.Interceptor#onLoad(Object, java.io.Serializable,
	 *      Object[], String[], net.sf.hibernate.type.Type[])
	 */
	@Override
	public boolean onLoad(Object entity, Serializable id, Object[] state,
			String[] propertyNames, Type[] types) throws CallbackException {
		boolean result = false;
		for (Interceptor interceptor : this.interceptors) {
			if (interceptor.onLoad(entity, id, state, propertyNames, types)) {
				/*
				 * Returns true if one interceptor in the chain has modified the
				 * object state
				 */
				result = true;
			}
		}
		return result;
	}

	/**
	 * @see net.sf.hibernate.Interceptor#onFlushDirty(Object,
	 *      java.io.Serializable, Object[], Object[], String[],
	 *      net.sf.hibernate.type.Type[])
	 */
	@Override
	public boolean onFlushDirty(Object entity, Serializable id,
			Object[] currentState, Object[] previousState,
			String[] propertyNames, Type[] types) throws CallbackException {
		boolean result = false;
		for (Interceptor interceptor : this.interceptors) {
			if (interceptor.onFlushDirty(entity, id, currentState,
					previousState, propertyNames, types)) {
				/*
				 * Returns true if one interceptor in the chain has modified the
				 * object current state
				 */
				result = true;
			}
		}
		return result;
	}

	/**
	 * @see net.sf.hibernate.Interceptor#onSave(Object, java.io.Serializable,
	 *      Object[], String[], net.sf.hibernate.type.Type[])
	 */
	@Override
	public boolean onSave(Object entity, Serializable id, Object[] state,
			String[] propertyNames, Type[] types) throws CallbackException {
		boolean result = false;
		for (Interceptor interceptor : this.interceptors) {
			if (interceptor.onSave(entity, id, state, propertyNames, types)) {
				/*
				 * Returns true if one interceptor in the chain has modified the
				 * object state
				 */
				result = true;
			}
		}
		return result;
	}

	/**
	 * @see net.sf.hibernate.Interceptor#onDelete(Object, java.io.Serializable,
	 *      Object[], String[], net.sf.hibernate.type.Type[])
	 */
	@Override
	public void onDelete(Object entity, Serializable id, Object[] state,
			String[] propertyNames, Type[] types) throws CallbackException {
		for (Interceptor interceptor : this.interceptors) {
			interceptor.onDelete(entity, id, state, propertyNames, types);
		}
	}

	/**
	 * @see Interceptor#postFlush(Iterator)
	 */
	@Override
	public void postFlush(Iterator entities) throws CallbackException {
		List<?> entityList = this.createList(entities);
		for (Interceptor interceptor : this.interceptors) {
			interceptor.postFlush(entityList.iterator());
		}
	}

	/**
	 * @see Interceptor#preFlush(Iterator)
	 */
	@Override
	public void preFlush(Iterator entities) throws CallbackException {
		List<?> entityList = this.createList(entities);
		for (Interceptor interceptor : this.interceptors) {
			interceptor.preFlush(entityList.iterator());
		}
	}

	/**
	 * Creates and returns a new <code>List</code> containing all the elements
	 * returned from the <code>Iterator</code>.
	 * 
	 * @param iterator
	 *            The iterator.
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
		for (Interceptor interceptor : this.interceptors) {
			result = interceptor.isTransient(entity);
			if (result != null) {
				// If any interceptor has returned either true or false, stop
				// the chain
				break;
			}
		}
		return result;
	}

	/**
	 * @see net.sf.hibernate.Interceptor#findDirty(Object, java.io.Serializable,
	 *      Object[], Object[], String[], net.sf.hibernate.type.Type[])
	 */
	@Override
	public int[] findDirty(Object entity, Serializable id,
			Object[] currentState, Object[] previousState,
			String[] propertyNames, Type[] types) {
		int[] result = null;
		for (Interceptor interceptor : this.interceptors) {
			result = interceptor.findDirty(entity, id, currentState,
					previousState, propertyNames, types);
			if (result != null) {
				/*
				 * If any interceptor has returned something not null, stop the
				 * chain
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
		return this.interceptors;
	}

	/**
	 * Sets the instances of the <code>Interceptor</code> interface that are
	 * chained within this interceptor.
	 * 
	 * @param interceptors
	 */
	public void setInterceptors(Interceptor[] interceptors) {
		this.interceptors = interceptors;
	}

	@Override
	public void afterTransactionBegin(Transaction arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterTransactionCompletion(Transaction arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeTransactionCompletion(Transaction arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getEntity(String arg0, Serializable arg1)
			throws CallbackException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getEntityName(Object arg0) throws CallbackException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object instantiate(String arg0, EntityMode arg1, Serializable arg2)
			throws CallbackException {

		Object result = null;

		for (Interceptor interceptor : this.interceptors) {
			result = interceptor.instantiate(arg0, arg1, arg2);
			if (result != null) {
				break;
			}
		}

		return result;

	}

	@Override
	public Boolean isTransient(Object arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCollectionRecreate(Object arg0, Serializable arg1)
			throws CallbackException {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCollectionRemove(Object arg0, Serializable arg1)
			throws CallbackException {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCollectionUpdate(Object arg0, Serializable arg1)
			throws CallbackException {
		// TODO Auto-generated method stub

	}

	@Override
	public String onPrepareStatement(String arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

}
