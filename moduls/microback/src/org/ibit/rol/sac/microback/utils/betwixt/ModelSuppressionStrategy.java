package org.ibit.rol.sac.microback.utils.betwixt;

import org.apache.commons.betwixt.strategy.PropertySuppressionStrategy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.ibit.rol.sac.micromodel.*;

/**
 * Controla las propiedades que se eliminaran del modelo
 * 
 *@author Indra
 */
public class ModelSuppressionStrategy extends PropertySuppressionStrategy {

    private Map<Class<?>, Set<String>> propiedadesInvalidas = new HashMap<Class<?>, Set<String>>();

    protected void addSupressionList(Class<?> clazz, String[] properties) {
    	
    	Set set = new HashSet<String>(Arrays.asList(properties));
        propiedadesInvalidas.put(clazz, set);
    }

    public ModelSuppressionStrategy() {
    	
    	//addSupressionList( MicrositeCompleto.class, new String[] {"id"} );
		addSupressionList( Banner.class, new String[] {"idmicrosite"} );
    	addSupressionList( Contacto.class, new String[] {"idmicrosite"} );
    	addSupressionList( Lineadatocontacto.class, new String[] {"id", "idcontacto"} );
    	addSupressionList( MProcedimiento.class, new String[] {"id", "idmicrosite"} );
    	addSupressionList( Archivo.class, new String[] {"idmicrosite"} );
    	
    	addSupressionList( Actividadagenda.class, new String[] {"idmicrosite"} );
    	addSupressionList( Agenda.class, new String[] {"id","idmicrosite"} );
    	
    	addSupressionList( Temafaq.class, new String[] {"idmicrosite"} );
    	addSupressionList( Faq.class, new String[] {"id", "idmicrosite"} );
    	
    	addSupressionList( Tipo.class, new String[] {"idmicrosite"} );
    	addSupressionList( Noticia.class, new String[] {"idmicrosite"} );

    	addSupressionList( Menu.class, new String[] {"idmicrosite"} );
    	addSupressionList( Contenido.class, new String[] {"idmenu"} );
    	
    	addSupressionList( Componente.class, new String[] {"idmicrosite"} );

    	addSupressionList( Encuesta.class, new String[] {"idmicrosite"} );
    	addSupressionList( Pregunta.class, new String[] {"id", "idencuesta", "nrespuestas"} );
    	addSupressionList( Respuesta.class, new String[] {"id", "idpregunta", "nrespuestas"} );
    	
    }

    public boolean suppressProperty(Class clazz, Class tipus, String nom) {
        // si es un traducible eliminam directament una serie de propietats.
    	//System.out.println( clazz.getName() + ": Propiedad [" + nom + "]" );

    	if (org.ibit.rol.sac.micromodel.Traducible.class.isAssignableFrom(clazz)) {
            //if (nom.equals("traduccion") || nom.equals("traduce") || nom.equals("traduccionMap")) {
    		if (nom.equals("traduccion") || nom.equals("traduce")) {
    			return true;
            }
        }

        // si nó, miram les llistes.
        if (propiedadesInvalidas.containsKey(clazz)) {
            if (((Set) propiedadesInvalidas.get(clazz)).contains(nom)) {
                return true;
            }
        }

        return false;
    }
}
