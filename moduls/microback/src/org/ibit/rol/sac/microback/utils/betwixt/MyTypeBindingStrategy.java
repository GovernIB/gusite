package org.ibit.rol.sac.microback.utils.betwixt;

import org.apache.commons.betwixt.strategy.TypeBindingStrategy;

/**
 * TypeBindingStrategy que empra la implementaci� per defecte excepte per
 * arrays de bytes que diu que �s un tipus primitiu.
 * 
 * @author Indra
 */
public class MyTypeBindingStrategy extends TypeBindingStrategy {
    public TypeBindingStrategy.BindingType bindingType(Class type) {
        if (type.equals(byte[].class)) {
            return TypeBindingStrategy.BindingType.PRIMITIVE;
        } else {
            return DEFAULT.bindingType(type);
        }
    }
}
