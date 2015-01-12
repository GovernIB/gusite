package org.ibit.rol.sac.microback.plugins;

import org.apache.commons.beanutils.Converter;
import net.sf.hibernate.collection.PersistentCollection;
import net.sf.hibernate.Hibernate;

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
