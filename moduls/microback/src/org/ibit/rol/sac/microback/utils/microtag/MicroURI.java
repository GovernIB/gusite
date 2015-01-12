package org.ibit.rol.sac.microback.utils.microtag;

import org.ibit.rol.sac.microback.Microback;

/**
 * Clase que genera los URIS de los componentes
 * 
 * @author Indra
 *
 */
public class MicroURI {

	/**
	 * Método que devuelve la uri de acceso a un documento.
	 * @param constante
	 * @param iditem
	 * @param iddoc
	 * @return String
	 */
	public static String uriImg(String constante, long iditem, long iddoc) {
		String retorno="";
		retorno="archivopub.do?ctrl=" + constante + iditem + Microback.separatordocs + iddoc + "&id=" + iddoc;
		return retorno;
	}
	
	
	/**
	 * Método que devuelve la uri de acceso a una noticia
	 * @param idsite
	 * @param iditem
	 * @param idioma
	 * @return String
	 */
	public static String uriNoticia(Long idsite, Long iditem, String idioma) {
		String retorno="";
		retorno="noticia.do?" + Microback.PIDSITE + "=" + idsite + "&" + Microback.PCONT + "=" + iditem + "&" + Microback.PLANG + "=" + idioma;
		return retorno;
	}
	
	/**
	 * Método que devuelve la uri de acceso a un evento de agenda
	 * @param idsite
	 * @param iditem
	 * @param idioma
	 * @return String
	 */
	public static String uriAgenda(Long idsite, String iditem, String idioma) {
		String retorno="";
		retorno="agenda.do?" + Microback.PIDSITE + "=" + idsite + "&" + Microback.PCONT + "=" + iditem + "&" + Microback.PLANG + "=" + idioma;
		return retorno;
	}
	
	/**
	 * Método que devuelve la uri de acceso a una pagina de contenido
	 * @param idsite
	 * @param iditem
	 * @param idioma
	 * @return String
	 */
	public static String uriContenido(Long idsite, String iditem, String idioma) {
		String retorno="";
		retorno="contenido.do?" + Microback.PIDSITE + "=" + idsite + "&" + Microback.PCONT + "=" + iditem + "&" + Microback.PLANG + "=" + idioma;
		return retorno;
	}	
	
	/**
	 * Método que devuelve la uri de acceso a un formulario de contacto
	 * @param idsite
	 * @param iditem
	 * @param idioma
	 * @return String
	 */
	public static String uriContacto(Long idsite, Long iditem, String idioma) {
		String retorno="";
		retorno="contacto.do?" + Microback.PIDSITE + "=" + idsite + "&" + Microback.PCONT + "=" + iditem + "&" + Microback.PLANG + "=" + idioma;
		return retorno;
	}
	
	/**
	 * Método que devuelve la uri de acceso a un recurso del portal.
	 * En el parametro recurso se pasa la página a visualizar
	 * @param recurso
	 * @param idsite
	 * @param idioma
	 * @return String
	 */
	public static String uriGeneral(String recurso, Long idsite, String idioma) {
		String retorno="";
		if (recurso.indexOf("?")!=-1) 
			retorno=recurso + "&" + Microback.PIDSITE + "=" + idsite + "&" + Microback.PLANG + "=" + idioma;
		else
			retorno=recurso + "?" + Microback.PIDSITE + "=" + idsite + "&" + Microback.PLANG + "=" + idioma;
		return retorno;
	}	
	
	/**
	 * Método que devuelve la uri de acceso a un recurso del portal.
	 * En el parametro recurso se pasa la página a visualizar
	 * @param recurso
	 * @param idsite
	 * @param iditem
	 * @param idioma
	 * @return String
	 */
	public static String uriGeneral(String recurso, Long idsite, String iditem, String idioma) {
		String retorno="";
		if (recurso.indexOf("?")!=-1) 
			retorno=recurso + "&" + Microback.PIDSITE + "=" + idsite + "&" + Microback.PCONT + "=" + iditem + "&" + Microback.PLANG + "=" + idioma;
		else
			retorno=recurso + "?" + Microback.PIDSITE + "=" + idsite + "&" + Microback.PCONT + "=" + iditem + "&" + Microback.PLANG + "=" + idioma;
		return retorno;
	}	
	
	
}
