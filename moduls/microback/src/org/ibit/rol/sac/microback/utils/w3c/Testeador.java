package org.ibit.rol.sac.microback.utils.w3c;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ibit.rol.sac.extractor.taw.TawHtmlParser;
import org.ibit.rol.sac.extractor.taw.TawResultBean;
import org.ibit.rol.sac.extractor.tidy.TidyHtmlParser;
import org.ibit.rol.sac.extractor.tidy.TidyResultBean;

/**
 * Clase que valida el formato HTML
 * 
 * @author Indra
 *
 */
public class Testeador {

	protected static Log log = LogFactory.getLog(Testeador.class);
	
	public static final String HTML_TAG_DOCTYPE = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">";
	public static final String HTML_TAG_XHTML = "<html xmlns=\"http://www.w3.org/1999/xhtml\">";
	
	/**
	 * M�todo que testea con el Tidy un pegote de html.<br/>
	 * @param input String 
	 * @return TidyResultBean
	 */
	public static TidyResultBean testeoPegoteHTML(String input) {
		try {
			//simulamos que es una pagina html bien construida
			String inputck = HTML_TAG_DOCTYPE + HTML_TAG_XHTML +
			"<head><title></title></head><body>" + 
			input +
			"</body></html>";
			TidyHtmlParser htmlparser = new TidyHtmlParser();

			return htmlparser.parse(inputck);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}
	
	
	/**
	 * M�todo que testea con el Tidy un html completo.<br/>
	 * @param input String 
	 * @return TidyResultBean
	 */
	public static TidyResultBean testeoHTML(String input) {
		try {
			TidyHtmlParser htmlparser = new TidyHtmlParser();
			return htmlparser.parse(input);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}
	
	/**
	 * M�todo que analiza con el Taw una p�gina.<br/>
	 * @param url String con la url a analizar.
	 * @return TawResultBean
	 */
	public static TawResultBean testeoTaw(String url) {
		try {
			TawHtmlParser tawhtmlparser = new TawHtmlParser();
			return tawhtmlparser.parse(url);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}
	
	
}
