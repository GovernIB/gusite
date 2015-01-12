package es.caib.gusite.front.microtag;

import java.net.URLEncoder;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.gusite.micromodel.Frqssi;
import es.caib.gusite.micromodel.TraduccionFrqssi;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.FrqssiDelegate;


/**
 * Parseo de 'encuestas'. Esta clase contiene métodos que parsean los tags especiales de los microsites.
 * Los tags son de la version 2.0
 * Devuelven trozos de código HTML pertenecientes a las encuestas.
 * 
 * @author Indra
 *
 */
public class MParserQssi extends MParserHTML {

	protected static Log log = LogFactory.getLog(MParserEncuesta.class);
	private Frqssi qssi = new Frqssi();
	public MParserQssi(String version) {
		super(version);
	}
	
	/**
	 * Método que devuelve un string preparado para insertar en un html.
	 * Ese string contiene una encuesta.
	 * @param idmicrosite
	 
	 * @param idioma
	 * @return StringBuffer con el pegote de html
	 */	
	public StringBuffer getHtmlQssi(Long idmicrosite, String ideqssi, String idioma) {
		StringBuffer retorno = new StringBuffer();
		 
		ResourceBundle rb =	ResourceBundle.getBundle("ApplicationResources_front", new Locale(idioma.toUpperCase(), idioma.toUpperCase()));
		
		try {
		    ResourceBundle rb2 =	ResourceBundle.getBundle("sac-microback-messages");
		    String Urlqssi=(String)rb2.getObject("frqssi.url");
			FrqssiDelegate qssidel = DelegateUtil.getFrqssiDelegate();
			Long idcont = new Long(Long.parseLong(ideqssi));
			Frqssi qss = qssidel.obtenerFrqssi(idcont);
		    qssi = qss;
			
			qssi.setIdi(idioma);
			 String laurl;
			 if (qssi.getCentro()!= null && qssi.getTipoescrito()!= null){
				  laurl = Urlqssi+"&centre="+qssi.getCentro()+"&tipus_escrit="+qssi.getTipoescrito()+"&asunto="+URLEncoder.encode(((TraduccionFrqssi)qssi.getTraduce()).getNombre(),"UTF-8")+"&idioma="+idioma;
			 } else{ 
				 if (qssi.getCentro()!= null) {
				 laurl = Urlqssi+"&centre="+qssi.getCentro()+"&asunto="+URLEncoder.encode(((TraduccionFrqssi)qssi.getTraduce()).getNombre(),"UTF-8")+"&idioma="+idioma;
				 } else  laurl = Urlqssi+"&asunto="+URLEncoder.encode(((TraduccionFrqssi)qssi.getTraduce()).getNombre(),"UTF-8")+"&idioma="+idioma;
			 }
			
			String enlace ="<a href=\""+laurl+"\">"+((TraduccionFrqssi)qssi.getTraduce()).getNombre()+"</a>";
			retorno.append(enlace);
			
		} catch (Exception e) {
			log.error("[getHtmlqssi]: " + e.getMessage());
			retorno= new StringBuffer("");
		}
		
		return retorno;
	}				
	
}
