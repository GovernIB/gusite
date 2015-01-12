package org.ibit.rol.sac.microfront.util.microtag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ibit.rol.sac.microfront.Microfront;
import org.ibit.rol.sac.micromodel.Banner;
import org.ibit.rol.sac.micromodel.TraduccionBanner;


/**
 * Esta clase contiene métodos que parsea los tags especiales de los microsite.
 * Devuelven trozos de código HTML pertenecientes a los banners
 * 
 * @author Indra
 *
 */
public class MParserBanner extends MParserHTML {

	protected static Log log = LogFactory.getLog(MParserBanner.class);
	
	public MParserBanner(String version) {
		super(version);
	}
	
	/**
	 * Método que devuelve un string preparado para insertar en un html.
	 * Ese string contiene un banner.
	 * @param idmicrosite
	 * @param idioma
	 * @return StringBuffer que contiene el codigo HTML de un banner
	 */
	public StringBuffer getHtmlBanner(Long idmicrosite, String idioma, Banner banner ) {
		StringBuffer retorno = new StringBuffer();
		try {
				retorno.append("<a href=\"lanzabanner.do?idsite=" + idmicrosite.longValue() + "&idbanner=" + banner.getId() + "\" target=\"_blank\">");
				retorno.append("<img src=\"");
				retorno.append(MicroURI.uriImg(Microfront.RBANNER, banner.getId().longValue(), ((TraduccionBanner)banner.getTraduccion(idioma)).getImagen().getId().longValue())); 
				retorno.append("\" width=235 height=64 alt=\"" + ((TraduccionBanner)banner.getTraduccion(idioma)).getAlt() + "\">");
				retorno.append("</a>");
		} catch(Exception e) {
			log.error("[getHtmlBanner]: " + e.getMessage());
			retorno= new StringBuffer("");
		}
		
		return retorno;
	}
	
}
