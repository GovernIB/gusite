package es.caib.gusite.microfront.job;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.gusite.microfront.Microfront;
import es.caib.gusite.utilities.rolsacAPI.APIUtil;
import es.caib.rolsac.api.v2.idioma.IdiomaCriteria;
import es.caib.rolsac.api.v2.idioma.IdiomaQueryServiceAdapter;
import es.caib.rolsac.api.v2.rolsac.RolsacQueryService;
import es.caib.rolsac.api.v2.unitatAdministrativa.UnitatAdministrativaCriteria;
import es.caib.rolsac.api.v2.unitatAdministrativa.UnitatAdministrativaQueryServiceAdapter;

/**
 * Clase MenuCabecera. Utilizado para trabajar con las cabeceras del microsite
 * @author Indra
 *
 */
public class MenuCabecera {

	/* Se guardará en cada hash una entrada por idioma. Y a su vez, el `value` del hash será un ArrayList con el listado correspondiente. */
	private static Hashtable<String, Collection<?>> uos = new Hashtable<String, Collection<?>>();
	
	private static Log log = LogFactory.getLog(MenuCabecera.class);
	
	/**
	 * Parte que se ejecuta la primera vez que se invoca a esta clase
	 */
	static {
		log.info("Refresco de menu estático de PortalCAIB en Microsites realizado.");
		refrescarMenu();
	}
	
	/**
	 * Método público para refrescar el Menu de los microsites
	 */
	public static void refrescarMenu() {

		try {
			RolsacQueryService rqs = APIUtil.getRolsacQueryService();
			List<IdiomaQueryServiceAdapter> listaIdiomas = rqs.llistarIdiomes(new IdiomaCriteria());

			// Rellenamos array de idiomas.
			List<String> idiomas = new ArrayList<String>();
			for (IdiomaQueryServiceAdapter idioma : listaIdiomas) {
				idiomas.add(idioma.getLang());
			}

			String idUOGovern = System.getProperty("es.caib.gusite.codigoUO.govern");
			if (idUOGovern == null) {
				throw new RuntimeException("No se estableció la propiedad de sistema es.caib.gusite.codigoUO.govern");
			}

			Long UO_GOVERN_IB = new Long(idUOGovern);
			UnitatAdministrativaCriteria uaCriteria = new UnitatAdministrativaCriteria();
			uaCriteria.setId(UO_GOVERN_IB.toString());

			UnitatAdministrativaQueryServiceAdapter ua = rqs.obtenirUnitatAdministrativa(uaCriteria);
			List<UnitatAdministrativaQueryServiceAdapter> listaUAs = ua.llistarFilles(new UnitatAdministrativaCriteria());

			for (String lang : idiomas) {
				Collection<UnitatAdministrativaQueryServiceAdapter> nuevasUAs = new ArrayList<UnitatAdministrativaQueryServiceAdapter>();
				Iterator<UnitatAdministrativaQueryServiceAdapter> it = listaUAs.iterator();

				// La conselleria de Portavoz se trata a parte.
				UnitatAdministrativaQueryServiceAdapter conse;
				while (it.hasNext()) {
					conse = it.next();
					nuevasUAs.add(conse);
				}

				uos.put(lang, nuevasUAs);
			}
			
		} catch (Exception e) {
			log.error("NO SE HA PODIDO CALCULAR EL MENU DE LA CABECERA DEL PORTALCAIB.");
			e.printStackTrace();
		}
	}

	/**
	 * Devuelve un listado de las consellerias
	 * @param lang
	 * @return ArrayList
	 */
	public static Collection<?> getUos(String lang) {

		Collection<?> lista = (Collection) uos.get(lang);
		if (lista == null) {
			return lista;
		}
		try {
			Collection listaDef = new  ArrayList();
			Iterator<?> conseDef = lista.iterator();
			while (conseDef.hasNext()) {
				UnitatAdministrativaQueryServiceAdapter cons = (UnitatAdministrativaQueryServiceAdapter) conseDef.next();
				if (!cons.getId().toString().equals(Microfront.UO_PORTAVOZ)) {
					listaDef.add(cons);
				}
	    	}
	    	return listaDef;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return lista;
	}
	
}
