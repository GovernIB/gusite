package es.caib.gusite.microback.plugins;

import org.apache.commons.beanutils.Converter;
import org.hibernate.Hibernate;
import org.hibernate.collection.PersistentCollection;

/**
 * Converter para evitar excepciones hibernate
 * 
 * @author Indra
 */
public class HibernateDelegateConverter implements Converter {

    private Converter delegate;

    public HibernateDelegateConverter(Converter delegate) {
        this.delegate = delegate;
    }

    public Object convert(Class type, Object value) {
        if (value != null && value instanceof PersistentCollection && !Hibernate.isInitialized(value)) {
            return delegate.convert(type, "[not initialized]");
        } else {
            return delegate.convert(type, value);
        }
    }

}
