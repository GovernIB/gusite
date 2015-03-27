package es.caib.gusite.microback.utils;

import java.util.ArrayList;
import java.util.Collection;

import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.plugins.PluginException;
import es.caib.gusite.plugins.PluginFactory;
import es.caib.gusite.plugins.organigrama.UnidadData;
import es.caib.gusite.plugins.organigrama.UnidadListData;

/**
 * 
 * @author at4.net
 *
 */
public class General {
	
	public static Collection<UnidadListData> obtenerListaUAs() {
    	
       	try {
	       		
       		return PluginFactory.getInstance().getOrganigramaProvider().getUnidades(Idioma.getIdiomaPorDefecto());
						
		} catch (PluginException e) {
			
			e.printStackTrace();
			
		}
       	
       	return new ArrayList<UnidadListData>();
       	    	
    }

}
