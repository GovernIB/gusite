package es.caib.gusite.microback.utils.betwixt;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.betwixt.strategy.PropertySuppressionStrategy;

/**
 * Permite encadenar una lista de {@link PropertySuppressionStrategy},
 * si alguno de ellos da <code>true</code> la propiedad se elimina.
 * 
 * @author Indra
 */
public class ChainSuppressionStrategy extends PropertySuppressionStrategy {

    private final List<PropertySuppressionStrategy> strategies = new ArrayList<PropertySuppressionStrategy>();

    /**
     * @see #suppressProperty(java.lang.Class, java.lang.Class, java.lang.String)
     */
    public boolean suppressProperty(Class classContainingTheProperty, Class propertyType, String propertyName) {
        boolean result = false;
        for (Iterator<PropertySuppressionStrategy> it = strategies.iterator(); it.hasNext();) {
            PropertySuppressionStrategy strategy = (PropertySuppressionStrategy) it.next();
            if (strategy.suppressProperty(classContainingTheProperty, propertyType, propertyName)) {
                result = true;
                break;
            }

        }
        return result;
    }

    /**
     * Adds a strategy to the list
     * @param strategy <code>PropertySuppressionStrategy</code>, not null
     */
    public void addStrategy(PropertySuppressionStrategy strategy) {
        strategies.add(strategy);
    }
}
