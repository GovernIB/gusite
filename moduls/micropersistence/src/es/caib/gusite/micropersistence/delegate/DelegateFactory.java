package es.caib.gusite.micropersistence.delegate;

import java.util.HashMap;
import java.util.Map;

/**
 * Factoria de objetos delegate.
 * @author Indra
 */
class DelegateFactory {

    private static Map<Class<?>, Object> delegates = new HashMap<Class<?>, Object>();

    static synchronized Delegate getDelegate(Class<?> clazz) {
        if (!Delegate.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException(clazz + " no es subclase de " + Delegate.class);
        }

        // Utilizamos un cache para reutilizar instancias cacheables.
        if (StatelessDelegate.class.isAssignableFrom(clazz)) {
            Object delegate = delegates.get(clazz);
            if (delegate == null) {
                delegate = getEnhancedInstance(clazz);
                delegates.put(clazz, delegate);
            }
            return (Delegate) delegate;
        } else {
            return (Delegate) getEnhancedInstance(clazz);
        }
    }

    private static Object getEnhancedInstance(Class<?> clazz) {
        try {
            return clazz.newInstance();
        } catch (Throwable t) {
            return null;
        }
        
    }

}