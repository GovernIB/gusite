package  es.caib.gusite.microintegracion.traductor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.gusite.micromodel.Traduccion;
import es.caib.gusite.micromodel.TraduccionActividadagenda;
import es.caib.gusite.micromodel.TraduccionAgenda;
import es.caib.gusite.micromodel.TraduccionComponente;
import es.caib.gusite.micromodel.TraduccionContenido;
import es.caib.gusite.micromodel.TraduccionLineadatocontacto;
import es.caib.gusite.micromodel.TraduccionMenu;
import es.caib.gusite.micromodel.TraduccionMicrosite;
import es.caib.gusite.micromodel.TraduccionNoticia;
import es.caib.gusite.micromodel.TraduccionTipo;

/**
 * Clase que traduce las propiedades de los beans del módulo Microsites
 * @author Indra
 */
public class TraductorMicrosites extends Traductor implements Traduccion {

	private static final long serialVersionUID = -3678888573936281923L;

	protected static Log log = LogFactory.getLog(TraductorMicrosites.class);

	/* agarcia: parece que esto no se usa
	private List<String> _listLangMicro, _listLangTraductorMicro;
	private Hashtable<String,String> _hshIdiomesMicro = new Hashtable<String,String>();
	 */

	private static final String MODE_TXT = "TXT", 
								MODE_HTML = "HTML",
								TAG_INI_HTML =  "<HTML><BODY>", 
								TAG_FI_HTML = "</BODY></HTML>";
	
	private static final String CONFIGURACION_GENERAL = "GENERAL";

	/**
	 * Constructor por defecto de la clase. 
	 */
	public TraductorMicrosites() throws TraductorException {
	}	
	
	/**
	 * Método que traduce las propiedades de un bean TraduccionMicrosite origen
	 * y las guarda en un bean TraduccionMicrosite destino
	 * 
	 * @param microOrigen	bean de traducción de microsite origen
	 * @param microDesti	bean de traducción de microsite destino
	 * @param configuracion		String que indica al método que traduzca los campos de configuración general o los de Cabecera/Pie
	 * @return	boolean			devuelve verdadero si la traducción finaliza correctamente. Si no devuelve falso.
	 */
	public boolean traducir(TraduccionMicrosite microOrigen, TraduccionMicrosite microDesti, String configuracion) throws TraductorException {

	    	if (configuracion.equals(CONFIGURACION_GENERAL)) {
				if(null!=microOrigen.getTitulo())		microDesti.setTitulo(traducir(microOrigen.getTitulo(),MODE_TXT));
		    	if(null!=microOrigen.getTitulocampanya())		microDesti.setTitulocampanya(traducir(microOrigen.getTitulocampanya(),MODE_TXT));
		    	if(null!=microOrigen.getSubtitulocampanya())	microDesti.setSubtitulocampanya(traducir(microOrigen.getSubtitulocampanya(),MODE_TXT));	    	
	    	
	    	} else {
	    	   	//Traducción de los campos opcionales de la cabecera
	    		if(null!=microOrigen.getTxtop1())		microDesti.setTxtop1(traducir(microOrigen.getTxtop1(),MODE_TXT));
		    	if(null!=microOrigen.getTxtop2())		microDesti.setTxtop2(traducir(microOrigen.getTxtop2(),MODE_TXT));
		    	
		    	//Traducción de los campos opcionales del pie
		    	if(null!=microOrigen.getTxtop3())		microDesti.setTxtop3(traducir(microOrigen.getTxtop3(),MODE_TXT));
		    	if(null!=microOrigen.getTxtop4())		microDesti.setTxtop4(traducir(microOrigen.getTxtop4(),MODE_TXT));
		    	if(null!=microOrigen.getTxtop5())		microDesti.setTxtop5(traducir(microOrigen.getTxtop5(),MODE_TXT));
		    	if(null!=microOrigen.getTxtop6())		microDesti.setTxtop6(traducir(microOrigen.getTxtop6(),MODE_TXT));
		    	if(null!=microOrigen.getTxtop7())		microDesti.setTxtop7(traducir(microOrigen.getTxtop7(),MODE_TXT));
		    		
		    	//Traducción de los campos Personalizados de Cabecera y Pie
		    	if(null!=microOrigen.getCabecerapersonal())		microDesti.setCabecerapersonal(traducir(microOrigen.getCabecerapersonal(),MODE_HTML));
		    	if(null!=microOrigen.getPiepersonal())			microDesti.setPiepersonal(traducir(microOrigen.getPiepersonal(),MODE_HTML));
	    	
	    	   	//Las Urls no se traducen pero se guardan en los nuevos campos
	    		if(null!=microOrigen.getUrlop1())		microDesti.setUrlop1(microOrigen.getUrlop1());
	    		if(null!=microOrigen.getUrlop2())		microDesti.setUrlop2(microOrigen.getUrlop2());
	    		
	    		if(null!=microOrigen.getUrlop3())		microDesti.setUrlop3(microOrigen.getUrlop3());
	    		if(null!=microOrigen.getUrlop4())		microDesti.setUrlop4(microOrigen.getUrlop4());
	    		if(null!=microOrigen.getUrlop5())		microDesti.setUrlop5(microOrigen.getUrlop5());
	    		if(null!=microOrigen.getUrlop6())		microDesti.setUrlop6(microOrigen.getUrlop6());
	    		if(null!=microOrigen.getUrlop7())		microDesti.setUrlop7(microOrigen.getUrlop7());

	    	}
	    	
    	return true;
	}

	
	/**
	 * Método que traduce las propiedades de un bean TraduccionMenu origen
	 * y las guarda en un bean TraduccionMenu destino
	 * 
	 * @param menuOrigen	bean de traducción de menú origen
	 * @param menuDesti		bean de traducción de menú destino
	 * @return	boolean		devuelve verdadero si la traducción finaliza correctamente. Si no devuelve falso.
	 */
	public boolean traducir(TraduccionMenu menuOrigen, TraduccionMenu menuDesti) throws TraductorException {
    	if(null!=menuOrigen.getNombre())		menuDesti.setNombre(traducir(menuOrigen.getNombre(),MODE_TXT));
    	return true;
	}	
	
	/**
	 * Método que traduce las propiedades de un bean TraduccionLineadatocontacto origen
	 * y las guarda en un bean TraduccionLineadatocontacto destino
	 * 
	 * @param lineadatocontactoOrigen	bean de traducción de línea de contacto origen
	 * @param lineadatocontactoDesti	bean de traducción de línea de contacto destino
	 * @param boolSelector 				boolean que indica si hay valores de campo selector
	 * @param limiteSelector		    limite de campos del selector
	 * @param numLangs					número de lenguajes a traducir
	 * @param indexIdioma				indice de Idioma dentro de la tabla selector
	 * @param textoSelector				tabla de Strings con valores de selector
	 * @return	boolean					devuelve verdadero si la traducción finaliza correctamente. Si no devuelve falso.
	 */
	public boolean traducir(TraduccionLineadatocontacto lineadatocontactoOrigen, TraduccionLineadatocontacto lineadatocontactoDesti, boolean boolSelector, int limiteSelector, int numLangs, int indexIdioma, String [] textoSelector) throws TraductorException {
			
		if(null!=lineadatocontactoOrigen.getTexto())		lineadatocontactoDesti.setTexto(traducir(lineadatocontactoOrigen.getTexto(),MODE_TXT));
		
		if (boolSelector == true && (null!=textoSelector) ){
    		int indexIdiomaDefault = 0;
			while(indexIdiomaDefault < (limiteSelector *numLangs)){
				if (!textoSelector[indexIdiomaDefault].equals("")){
					textoSelector[indexIdiomaDefault+indexIdioma]=(traducir(textoSelector[indexIdiomaDefault],MODE_TXT));
				}
				indexIdiomaDefault = indexIdiomaDefault + numLangs;
			} 
		}
    	return true;
	}
	
	/**
	 * Método que traduce las propiedades de un bean TraduccionActividad origen
	 * y las guarda en un bean TraduccionActividad destino
	 * 
	 * @param actividadAgendaOrigen		bean de traducción de actividad origen
	 * @param actividadAgendaDesti		bean de traducción de actividad destino
	 * @return	boolean					devuelve verdadero si la traducción finaliza correctamente. Si no devuelve falso.
	 */
	public boolean traducir(TraduccionActividadagenda actividadAgendaOrigen, TraduccionActividadagenda actividadAgendaDesti) throws TraductorException {

    	if(null!=actividadAgendaOrigen.getNombre())		actividadAgendaDesti.setNombre(traducir(actividadAgendaOrigen.getNombre(),MODE_TXT));
    	return true;
	}
	
	/**
	 * Método que traduce las propiedades de un bean TraduccionAgenda origen
	 * y las guarda en un bean TraduccionContenido destino
	 * 
	 * @param agendaOrigen		bean de traducción de agenda origen
	 * @param agendaDesti		bean de traducción de agenda destino
	 * @return	boolean			devuelve verdadero si la traducción finaliza correctamente. Si no devuelve falso.
	 */
	public boolean traducir(TraduccionAgenda agendaOrigen, TraduccionAgenda agendaDesti) throws TraductorException {

	 	if(null != agendaOrigen.getTitulo())		agendaDesti.setTitulo(traducir(agendaOrigen.getTitulo(),MODE_TXT));
	 	if(null != agendaOrigen.getDescripcion())	agendaDesti.setDescripcion(traducir(agendaOrigen.getDescripcion(),MODE_HTML));
	 	if(null != agendaOrigen.getUrlnom())		agendaDesti.setUrlnom(traducir(agendaOrigen.getUrlnom(),MODE_TXT));
	 	if(null!=agendaOrigen.getUrl())				agendaDesti.setUrl(traducir(agendaOrigen.getUrl(),MODE_TXT));
	
    	return true;
	}
	

	/**
	 * Método que traduce las propiedades de un bean TraduccionTipo origen
	 * y las guarda en un bean TraduccionTipo destino
	 * 
	 * @param tipoOrigen	bean de traducción de Tipo origen
	 * @param tipoDesti		bean de traducción de Tipo destino
	 * @return	boolean		devuelve verdadero si la traducción finaliza correctamente. Si no devuelve falso.
	 */
	public boolean traducir(TraduccionTipo tipoOrigen, TraduccionTipo tipoDesti) throws TraductorException {

    	if(null!=tipoOrigen.getNombre())		tipoDesti.setNombre(traducir(tipoOrigen.getNombre(),MODE_TXT));
    	return true;
	}	
	
	
	/**
	 * Método que traduce las propiedades de un bean TraduccionNoticia origen
	 * y las guarda en un bean TraduccionNoticia destino
	 * 
	 * @param noticiaOrigen		bean de traducción de Noticia origen
	 * @param noticiaDesti		bean de traducción de Noticia destino
	 * @return	boolean			devuelve verdadero si la traducción finaliza correctamente. Si no devuelve falso.
	 * @throws TraductorException
	 */
	public boolean traducir(TraduccionNoticia noticiaOrigen, TraduccionNoticia noticiaDesti) throws TraductorException {
    	if(null!=noticiaOrigen.getTitulo())		noticiaDesti.setTitulo(traducir(noticiaOrigen.getTitulo(),MODE_TXT));
    	if(null!=noticiaOrigen.getSubtitulo())	noticiaDesti.setSubtitulo(traducir(noticiaOrigen.getSubtitulo(),MODE_TXT));
    	if(null!=noticiaOrigen.getFuente())		noticiaDesti.setFuente(traducir(noticiaOrigen.getFuente(),MODE_TXT));
    	if(null!=noticiaOrigen.getLaurl())		noticiaDesti.setLaurl(traducir(noticiaOrigen.getLaurl(),MODE_TXT));
    	if(null!=noticiaOrigen.getUrlnom())		noticiaDesti.setUrlnom(traducir(noticiaOrigen.getUrlnom(),MODE_TXT));
    	if(null!=noticiaOrigen.getTexto())		noticiaDesti.setTexto(traducir(noticiaOrigen.getTexto(),MODE_HTML));
    	return true;
	}	
	
	
	/**
	 * Método que traduce las propiedades de un bean TraduccionComponente origen
	 * y las guarda en un bean TraduccionComponente destino
	 * 
	 * @param componenteOrigen	bean de traducción de Componente origen
	 * @param componenteDesti	bean de traducción de Componente destino
	 * @return	boolean			devuelve verdadero si la traducción finaliza correctamente. Si no devuelve falso.
	 * @throws TraductorException
	 */
	public boolean traducir(TraduccionComponente componenteOrigen, TraduccionComponente componenteDesti) throws TraductorException {
    	if(null!=componenteOrigen.getTitulo())		componenteDesti.setTitulo(traducir(componenteOrigen.getTitulo(),MODE_TXT));
    	return true;
	}	
	
	
	/**
	 * Método que traduce las propiedades de un bean TraduccionContenido origen
	 * y las guarda en un bean TraduccionContenido destino
	 * 
	 * @param contenidoOrigen	bean de traducción de contenido origen
	 * @param contenidoDesti	bean de traducción de contenido destino
	 * @return	boolean			devuelve verdadero si la traducción finaliza correctamente. Si no devuelve falso.
	 * @throws TraductorException 
	 * @throws TraductorException
	 */
	public boolean traducir(TraduccionContenido contenidoOrigen, TraduccionContenido contenidoDesti) throws TraductorException {

    	if(null!=contenidoOrigen.getTitulo())		contenidoDesti.setTitulo(traducir(contenidoOrigen.getTitulo(),MODE_TXT));
    	if(null!=contenidoOrigen.getUrl())			contenidoDesti.setUrl(traducir(contenidoOrigen.getUrl(),MODE_TXT));
		if(null!=contenidoOrigen.getTexto())		contenidoDesti.setTexto(traducir(contenidoOrigen.getTexto(),MODE_HTML));
    	if(null!=contenidoOrigen.getTxbeta())		contenidoDesti.setTxbeta(traducir(contenidoOrigen.getTxbeta(),MODE_HTML));
			
    	return true;
	}
	

	

	/**
	 * Método que parametriza el traductor según el tipo de texto y envía el texto al método translate
	 * 
	 * @param textTraduccio		texto a traducir
	 * @param modeTraduccio		modo de traducción (HTML, TXT)
	 * @return String			texto traducido
	 * @throws TraductorException		lanza una nueva excepción proceso de traducción no ha funcionado
	 */
	private String traducir(String textTraduccio, String modeTraduccio) throws TraductorException {
  
		if (modeTraduccio == MODE_HTML) {
			textTraduccio = TAG_INI_HTML + textTraduccio + TAG_FI_HTML;
			_colorMarkups = ACTIVE;
			_markUnknowns = ACTIVE;
			_markAlternatives = ACTIVE;
			
		} else {
				_colorMarkups = INACTIVE;
				_markUnknowns = INACTIVE;
				_markAlternatives = INACTIVE;
		}

		return translate(textTraduccio);
		
	}
	
}
