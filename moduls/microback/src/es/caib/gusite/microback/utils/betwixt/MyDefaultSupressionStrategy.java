package es.caib.gusite.microback.utils.betwixt;

import org.apache.commons.betwixt.strategy.PropertySuppressionStrategy;

/**
 * Extensión del PropertySupressionStrategy por defecto para permitir el 
 * atributo class en las colecciones de componentes
 * 
 * @author Indra
 */
public class MyDefaultSupressionStrategy extends PropertySuppressionStrategy {

    public boolean suppressProperty(Class clazz, Class type, String name) 
    {
    	/*
    	if (Componente.class.isAssignableFrom(clazz)) {
            return false;
        } else {
        */
            return PropertySuppressionStrategy.DEFAULT.suppressProperty(clazz, type, name);
        /*            
        }
        */
    }
}
