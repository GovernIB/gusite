package es.caib.gusite.microback.utils;

import java.util.ArrayList;
import java.util.List;

import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.utilities.rolsacAPI.APIUtil;
import es.caib.rolsac.api.v2.exception.QueryServiceException;
import es.caib.rolsac.api.v2.rolsac.RolsacQueryService;
import es.caib.rolsac.api.v2.unitatAdministrativa.UnitatAdministrativaCriteria;
import es.caib.rolsac.api.v2.unitatAdministrativa.UnitatAdministrativaOrdenacio;
import es.caib.rolsac.api.v2.unitatAdministrativa.UnitatAdministrativaQueryServiceAdapter;

/**
 * 
 * @author amartin
 *
 */
public class General {
	
	public static ArrayList<UnitatAdministrativaQueryServiceAdapter> obtenerListaUAs() {
    	
    	RolsacQueryService rqs = APIUtil.getRolsacQueryService();
       	List<UnitatAdministrativaQueryServiceAdapter> listaUAs = null;
       	
       	try {
       		       		
       		UnitatAdministrativaCriteria uaCriteria = new UnitatAdministrativaCriteria();
           	uaCriteria.setIdioma(Idioma.getIdiomaPorDefecto());
           	uaCriteria.setOrdenar( new UnitatAdministrativaOrdenacio[]{UnitatAdministrativaOrdenacio.orden_asc} );
       		listaUAs = rqs.llistarUnitatsAdministratives(uaCriteria);
						
		} catch (QueryServiceException e) {
			
			e.printStackTrace();
			
		}
       	
       	return new ArrayList<UnitatAdministrativaQueryServiceAdapter>(listaUAs);
       	    	
    }

}
