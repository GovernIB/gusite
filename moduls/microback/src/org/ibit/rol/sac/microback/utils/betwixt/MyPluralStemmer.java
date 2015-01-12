package org.ibit.rol.sac.microback.utils.betwixt;

import org.apache.commons.betwixt.ElementDescriptor;
import org.apache.commons.betwixt.strategy.DefaultPluralStemmer;

import java.util.Map;

/**
 * Implementación por defecto añadiendo el mapping valorPosible -> valoresPosibles.
 * 
 * @author Indra
 */
public class MyPluralStemmer extends DefaultPluralStemmer {

    public ElementDescriptor findPluralDescriptor(String propertyName, Map map) {
        ElementDescriptor answer = null;
        
        //System.out.println( "PLURAL STEMMER " + propertyName +  ":" + map );
        
        if ("valorPosible".equals(propertyName)) {
            answer = (ElementDescriptor) map.get("valoresPosibles");
        }

        if (answer != null) {
            return answer;
        }

        return super.findPluralDescriptor(propertyName, map);
    }
}
