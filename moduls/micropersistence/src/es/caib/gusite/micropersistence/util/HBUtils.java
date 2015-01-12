package es.caib.gusite.micropersistence.util;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.HibernateException;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.proxy.HibernateProxyHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.gusite.micromodel.ValueObject;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.io.Serializable;

/**
 * Utils para Beans obtenidos a partir de Hibernate.
 * @author INDRA 
 */
public final class HBUtils {

    private static final Log log = LogFactory.getLog(HBUtils.class);

    /**
     * Procesa un bean eliminando las referencias a classes Hibernate.
     * @param bean Objeto a procesar.
     */
    public static void processHibernatedBean(final Object bean, final Session session) {
        processHibernatedBean(bean, new HashSet<Object>(), session);
    }

    private static void processHibernatedBean(final Object bean, final Set<Object> processed, final Session session) {
        if (processed.contains(bean)) {
            return;
        } else {
            try {
                session.evict(bean);
            } catch (HibernateException e) {
                log.warn("Error a session.evict",e);
            }
            processed.add(bean);
        }
        final BeanInfo beanInfo = getBeanInfo(bean.getClass());
        if (beanInfo == null) return;
        final PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (int i = 0; i < propertyDescriptors.length; i++) {
            final PropertyDescriptor propertyDescriptor = propertyDescriptors[i];
            final Class<?> type = propertyDescriptor.getPropertyType();
            Object value;
            try {
                value = propertyDescriptor.getReadMethod().invoke(bean, new Object[0]);
            } catch (Exception e) {
                log.warn("Error leyendo propiedad", e);
                value = null;
            }
            if (value != null) {
                final Class<?> clazz = value.getClass();
                if ((Collection.class.isAssignableFrom(type) || Map.class.isAssignableFrom(type)) && clazz.getName().startsWith("org.hibernate")) {

                    final Object newValue;
                    final boolean inicialitzat = Hibernate.isInitialized(value);
                    switch (getCollectionType(clazz)) {
                        case TYPE_LIST:
                            newValue = inicialitzat ? new ArrayList((Collection<?>) value) : Collections.EMPTY_LIST;
                            break;
                        case TYPE_SET:
                            newValue = inicialitzat ? new HashSet((Collection) value) : Collections.EMPTY_SET;
                            break;
                        case TYPE_MAP:
                            newValue = inicialitzat ? new HashMap((Map) value) : Collections.EMPTY_MAP;
                            break;
                        default:
                            //log.info("Tipus desconegut? " + clazz.getName());
                            newValue = null;
                            break;
                    }
                    try {
                        propertyDescriptor.getWriteMethod().invoke(bean, new Object[]{newValue});
                    } catch (Exception e) {
                        log.warn("Error fijando propiedad", e);
                    }
                    if (inicialitzat) {
                        Collection<? extends Object> values = Collection.class.isAssignableFrom(type) ? (Collection<?>) newValue : ((Map<?, ?>) newValue).values();
                        for (Iterator<?> iterator = values.iterator(); iterator.hasNext();) {
                            final Object o = iterator.next();
                            if (o != null) {
                                if (ValueObject.class.isAssignableFrom(o.getClass())) {
                                    processHibernatedBean(o, processed, session);
                                }
                            }
                        }
                    }
                } else if (ValueObject.class.isAssignableFrom(type)) {
                    if (Hibernate.isInitialized(value)) {
                        processHibernatedBean(value, processed, session);
                    } else {
                        Object newValue = null;
                        try {
                            Class<?> newClazz = HibernateProxyHelper.getClassWithoutInitializingProxy(value);
                            newValue = newClazz.newInstance();
                            ClassMetadata clazzMetaData = session.getSessionFactory().getClassMetadata(newClazz);
                            Serializable id = clazzMetaData.getIdentifier(value, ((SessionImplementor)session).getEntityMode()); 
                            clazzMetaData.setIdentifier(newValue, id, ((SessionImplementor)session).getEntityMode());
                        } catch (Exception e) {
                            log.warn("Error intentant recrear lazy", e);
                        }
                        try {
                            propertyDescriptor.getWriteMethod().invoke(bean, new Object[] {newValue});
                        } catch (Exception e) {
                            log.warn("Error fijando propiedad", e);
                        }
                    }
                }
            }
        }
    }

    private static final int TYPE_LIST = 1;
    private static final int TYPE_SET = 2;
    private static final int TYPE_MAP = 3;
    private static final int TYPE_UNKNOWN = 4;

    private static int getCollectionType(final Class<?> clazz) {
        if (List.class.isAssignableFrom(clazz)) {
            return TYPE_LIST;
        } else if (Set.class.isAssignableFrom(clazz)) {
            return TYPE_SET;
        } else if (Map.class.isAssignableFrom(clazz)) {
            return TYPE_MAP;
        }
        return TYPE_UNKNOWN;
    }

    private static final Map<Class<?>, BeanInfo> beanInfoMap = new HashMap<Class<?>, BeanInfo>();

    private static BeanInfo getBeanInfo(final Class<?> clazz) {
        if (!beanInfoMap.containsKey(clazz)) {
            try {
                final BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
                beanInfoMap.put(clazz, beanInfo);
                return beanInfo;
            } catch (IntrospectionException e) {
                log.warn("Error en getBeanInfo()", e);
                return null;
            }
        } else {
            return (BeanInfo) beanInfoMap.get(clazz);
        }
    }
}
