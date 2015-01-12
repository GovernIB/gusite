package org.ibit.rol.sac.microback.utils.betwixt;

import org.apache.commons.betwixt.expression.Context;
import org.apache.commons.betwixt.strategy.DefaultObjectStringConverter;

/**
 * Delega la implementación por defecto excepto para el tipus array de bytes,
 * que utiliza un string base64 para escribir y leer
 * 
 * @author Indra
 * 
 */
public class MyObjectStringConverter extends DefaultObjectStringConverter {

    public String objectToString(Object object, Class type, String flavour, Context context) {
        if (object != null) {
            if (object instanceof byte[]) {
                return new String(Base64Coder.encode((byte[]) object));
            } else {
                // use Default implementation
                return super.objectToString(object, type, flavour, context);
            }
        }
        return "";
    }

    public Object stringToObject(String value, Class type, String flavour, Context context) {
        if (type.equals(byte[].class)) {
            return Base64Coder.decode(value.toCharArray());
        } else {
            // use default implementation
            return super.stringToObject(value, type, flavour, context);
        }
    }
}
