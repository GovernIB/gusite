package  es.indra.gusite.microintegracion.traductor;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import es.indra.rol.sac.integracion.traductor.Traductor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ibit.rol.sac.micromodel.TraduccionActividadagenda;
import org.ibit.rol.sac.micromodel.TraduccionAgenda;
import org.ibit.rol.sac.micromodel.TraduccionComponente;
import org.ibit.rol.sac.micromodel.TraduccionContenido;
import org.ibit.rol.sac.micromodel.Traduccion;
import org.ibit.rol.sac.micromodel.TraduccionLineadatocontacto;
import org.ibit.rol.sac.micromodel.TraduccionMenu;
import org.ibit.rol.sac.micromodel.TraduccionMicrosite;
import org.ibit.rol.sac.micromodel.TraduccionNoticia;
import org.ibit.rol.sac.micromodel.TraduccionTipo;
import org.ibit.rol.sac.micropersistence.delegate.DelegateException;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;
import org.ibit.rol.sac.micropersistence.delegate.IdiomaDelegate;


/**
 * Clase que traduce las propiedades de los beans del m�dulo Microsites
 * @author Indra
 */
public class TraductorMicrosites extends Traductor implements Traduccion {

	private static final long serialVersionUID = -3678888573936281923L;

	protected static Log log = LogFactory.getLog(TraductorMicrosites.class);
	
	private List<String> _listLangMicro, _listLangTraductorMicro;
	
	private Hashtable<String,String> _hshIdiomesMicro = new Hashtable<String,String>();	

	private static final String MODE_TXT = "TXT", 
								MODE_HTML = "HTML",
								TAG_INI_HTML =  "<HTML><BODY>", 
								TAG_FI_HTML = "</BODY></HTML>";
	
	private static final String CONFIGURACION_GENERAL = "GENERAL";

	/**
	 * Constructor por defecto de la clase. 
	 * 
	 * Carga los c�digos de Idioma de la capa de negocio para la traducci�n
	 * e inicia una Hashtable para saber el origen-destino de la traducci�n
	 *  
	 */
	public TraductorMicrosites() throws Exception {
		
    	IdiomaDelegate idiomaDelegate = DelegateUtil.getIdiomaDelegate();
    	
    	_listLangMicro = (List<String>) idiomaDelegate.listarLenguajes();
    	_listLangTraductorMicro = idiomaDelegate.listarLenguajesTraductor();
 
		iniHshIdiomes();

	}	
	
    /**
     * M�todo que inicia la Hastable de relaci�n entre lengujes de negocio
     * e ids de traductor. Esta Hashtable se utiliza para guardar la propiedad
     * _translationDirection
     * 
     * @throws DelegateException
     */
    private void iniHshIdiomes() throws DelegateException {
    	
    	Iterator<String> itLang =  _listLangMicro.iterator();
    	Iterator<String> itLangTraductor = _listLangTraductorMicro.iterator();

        while (itLang.hasNext()){

        	_hshIdiomesMicro.put(itLang.next(), itLangTraductor.next());

        }
    }	
	
	/**
	 * M�todo que traduce las propiedades de un bean TraduccionMicrosite origen
	 * y las guarda en un bean TraduccionMicrosite destino
	 * 
	 * @param microOrigen	bean de traducci�n de microsite origen
	 * @param microDesti	bean de traducci�n de microsite destino
	 * @param configuracion		String que indica al m�todo que traduzca los campos de configuraci�n general o los de Cabecera/Pie
	 * @return	boolean			devuelve verdadero si la traducci�n finaliza correctamente. Si no devuelve falso.
	 * @throws Exception
	 */
	public boolean traducir(TraduccionMicrosite microOrigen, TraduccionMicrosite microDesti, String configuracion) throws Exception {
		try {

	    	if (configuracion.equals(CONFIGURACION_GENERAL)) {
				if(null!=microOrigen.getTitulo())		microDesti.setTitulo(traducir(microOrigen.getTitulo(),MODE_TXT));
		    	if(null!=microOrigen.getTitulocampanya())		microDesti.setTitulocampanya(traducir(microOrigen.getTitulocampanya(),MODE_TXT));
		    	if(null!=microOrigen.getSubtitulocampanya())	microDesti.setSubtitulocampanya(traducir(microOrigen.getSubtitulocampanya(),MODE_TXT));	    	
	    	
	    	} else {
	    	   	//Traducci�n de los campos opcionales de la cabecera
	    		if(null!=microOrigen.getTxtop1())		microDesti.setTxtop1(traducir(microOrigen.getTxtop1(),MODE_TXT));
		    	if(null!=microOrigen.getTxtop2())		microDesti.setTxtop2(traducir(microOrigen.getTxtop2(),MODE_TXT));
		    	
		    	//Traducci�n de los campos opcionales del pie
		    	if(null!=microOrigen.getTxtop3())		microDesti.setTxtop3(traducir(microOrigen.getTxtop3(),MODE_TXT));
		    	if(null!=microOrigen.getTxtop4())		microDesti.setTxtop4(traducir(microOrigen.getTxtop4(),MODE_TXT));
		    	if(null!=microOrigen.getTxtop5())		microDesti.setTxtop5(traducir(microOrigen.getTxtop5(),MODE_TXT));
		    	if(null!=microOrigen.getTxtop6())		microDesti.setTxtop6(traducir(microOrigen.getTxtop6(),MODE_TXT));
		    	if(null!=microOrigen.getTxtop7())		microDesti.setTxtop7(traducir(microOrigen.getTxtop7(),MODE_TXT));
		    		
		    	//Traducci�n de los campos Personalizados de Cabecera y Pie
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
	    	
		} catch (Exception e) {
			
			e.printStackTrace();
			log.error(e.getMessage());
			return false;
		}
    	return true;
	}

	
	/**
	 * M�todo que traduce las propiedades de un bean TraduccionMenu origen
	 * y las guarda en un bean TraduccionMenu destino
	 * 
	 * @param menuOrigen	bean de traducci�n de men� origen
	 * @param menuDesti		bean de traducci�n de men� destino
	 * @return	boolean		devuelve verdadero si la traducci�n finaliza correctamente. Si no devuelve falso.
	 * @throws Exception
	 */
	public boolean traducir(TraduccionMenu menuOrigen, TraduccionMenu menuDesti) throws Exception {
		try {

	    	if(null!=menuOrigen.getNombre())		menuDesti.setNombre(traducir(menuOrigen.getNombre(),MODE_TXT));
			
		} catch (Exception e) {
			
			e.printStackTrace();
			log.error(e.getMessage());
			return false;
		}
    	return true;
	}	
	
	/**
	 * M�todo que traduce las propiedades de un bean TraduccionLineadatocontacto origen
	 * y las guarda en un bean TraduccionLineadatocontacto destino
	 * 
	 * @param lineadatocontactoOrigen	bean de traducci�n de l�nea de contacto origen
	 * @param lineadatocontactoDesti	bean de traducci�n de l�nea de contacto destino
	 * @param boolSelector 				boolean que indica si hay valores de campo selector
	 * @param limiteSelector		    limite de campos del selector
	 * @param numLangs					n�mero de lenguajes a traducir
	 * @param indexIdioma				indice de Idioma dentro de la tabla selector
	 * @param textoSelector				tabla de Strings con valores de selector
	 * @return	boolean					devuelve verdadero si la traducci�n finaliza correctamente. Si no devuelve falso.
	 * @throws Exception
	 */
	public boolean traducir(TraduccionLineadatocontacto lineadatocontactoOrigen, TraduccionLineadatocontacto lineadatocontactoDesti, boolean boolSelector, int limiteSelector, int numLangs, int indexIdioma, String [] textoSelector) throws Exception {
		try {
			
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
		} catch (Exception e) {
			
			e.printStackTrace();
			log.error(e.getMessage());
			return false;
		}
    	return true;
	}
	
	/**
	 * M�todo que traduce las propiedades de un bean TraduccionActividad origen
	 * y las guarda en un bean TraduccionActividad destino
	 * 
	 * @param actividadAgendaOrigen		bean de traducci�n de actividad origen
	 * @param actividadAgendaDesti		bean de traducci�n de actividad destino
	 * @return	boolean					devuelve verdadero si la traducci�n finaliza correctamente. Si no devuelve falso.
	 * @throws Exception
	 */
	public boolean traducir(TraduccionActividadagenda actividadAgendaOrigen, TraduccionActividadagenda actividadAgendaDesti) throws Exception {
		try {

	    	if(null!=actividadAgendaOrigen.getNombre())		actividadAgendaDesti.setNombre(traducir(actividadAgendaOrigen.getNombre(),MODE_TXT));
			
		} catch (Exception e) {
			
			e.printStackTrace();
			log.error(e.getMessage());
			return false;
		}
    	return true;
	}
	
	/**
	 * M�todo que traduce las propiedades de un bean TraduccionAgenda origen
	 * y las guarda en un bean TraduccionContenido destino
	 * 
	 * @param agendaOrigen		bean de traducci�n de agenda origen
	 * @param agendaDesti		bean de traducci�n de agenda destino
	 * @return	boolean			devuelve verdadero si la traducci�n finaliza correctamente. Si no devuelve falso.
	 * @throws Exception
	 */
	public boolean traducir(TraduccionAgenda agendaOrigen, TraduccionAgenda agendaDesti) throws Exception {
		try {

		 	if(null != agendaOrigen.getTitulo())		agendaDesti.setTitulo(traducir(agendaOrigen.getTitulo(),MODE_TXT));
		 	if(null != agendaOrigen.getDescripcion())	agendaDesti.setDescripcion(traducir(agendaOrigen.getDescripcion(),MODE_HTML));
		 	if(null != agendaOrigen.getUrlnom())		agendaDesti.setUrlnom(traducir(agendaOrigen.getUrlnom(),MODE_TXT));
		 	if(null!=agendaOrigen.getUrl())				agendaDesti.setUrl(traducir(agendaOrigen.getUrl(),MODE_TXT));
	
		} catch (Exception e) {
			
			e.printStackTrace();
			log.error(e.getMessage());
			return false;
		}
    	return true;
	}
	

	/**
	 * M�todo que traduce las propiedades de un bean TraduccionTipo origen
	 * y las guarda en un bean TraduccionTipo destino
	 * 
	 * @param tipoOrigen	bean de traducci�n de Tipo origen
	 * @param tipoDesti		bean de traducci�n de Tipo destino
	 * @return	boolean		devuelve verdadero si la traducci�n finaliza correctamente. Si no devuelve falso.
	 * @throws Exception
	 */
	public boolean traducir(TraduccionTipo tipoOrigen, TraduccionTipo tipoDesti) throws Exception {
		try {

	    	if(null!=tipoOrigen.getNombre())		tipoDesti.setNombre(traducir(tipoOrigen.getNombre(),MODE_TXT));
			
		} catch (Exception e) {
			
			e.printStackTrace();
			log.error(e.getMessage());
			return false;
		}
    	return true;
	}	
	
	
	/**
	 * M�todo que traduce las propiedades de un bean TraduccionNoticia origen
	 * y las guarda en un bean TraduccionNoticia destino
	 * 
	 * @param noticiaOrigen		bean de traducci�n de Noticia origen
	 * @param noticiaDesti		bean de traducci�n de Noticia destino
	 * @return	boolean			devuelve verdadero si la traducci�n finaliza correctamente. Si no devuelve falso.
	 * @throws Exception
	 */
	public boolean traducir(TraduccionNoticia noticiaOrigen, TraduccionNoticia noticiaDesti) throws Exception {
		try {

	    	if(null!=noticiaOrigen.getTitulo())		noticiaDesti.setTitulo(traducir(noticiaOrigen.getTitulo(),MODE_TXT));
	    	if(null!=noticiaOrigen.getSubtitulo())	noticiaDesti.setSubtitulo(traducir(noticiaOrigen.getSubtitulo(),MODE_TXT));
	    	if(null!=noticiaOrigen.getFuente())		noticiaDesti.setFuente(traducir(noticiaOrigen.getFuente(),MODE_TXT));
	    	if(null!=noticiaOrigen.getLaurl())		noticiaDesti.setLaurl(traducir(noticiaOrigen.getLaurl(),MODE_TXT));
	    	if(null!=noticiaOrigen.getUrlnom())		noticiaDesti.setUrlnom(traducir(noticiaOrigen.getUrlnom(),MODE_TXT));
	    	if(null!=noticiaOrigen.getTexto())		noticiaDesti.setTexto(traducir(noticiaOrigen.getTexto(),MODE_HTML));
	    	
	    	
		} catch (Exception e) {
			
			e.printStackTrace();
			log.error(e.getMessage());
			return false;
		}
    	return true;
	}	
	
	
	/**
	 * M�todo que traduce las propiedades de un bean TraduccionComponente origen
	 * y las guarda en un bean TraduccionComponente destino
	 * 
	 * @param componenteOrigen	bean de traducci�n de Componente origen
	 * @param componenteDesti	bean de traducci�n de Componente destino
	 * @return	boolean			devuelve verdadero si la traducci�n finaliza correctamente. Si no devuelve falso.
	 * @throws Exception
	 */
	public boolean traducir(TraduccionComponente componenteOrigen, TraduccionComponente componenteDesti) throws Exception {
		try {

	    	if(null!=componenteOrigen.getTitulo())		componenteDesti.setTitulo(traducir(componenteOrigen.getTitulo(),MODE_TXT));

		} catch (Exception e) {
			
			e.printStackTrace();
			log.error(e.getMessage());
			return false;
		}
    	return true;
	}	
	
	
	/**
	 * M�todo que traduce las propiedades de un bean TraduccionContenido origen
	 * y las guarda en un bean TraduccionContenido destino
	 * 
	 * @param contenidoOrigen	bean de traducci�n de contenido origen
	 * @param contenidoDesti	bean de traducci�n de contenido destino
	 * @return	boolean			devuelve verdadero si la traducci�n finaliza correctamente. Si no devuelve falso.
	 * @throws Exception
	 */
	public boolean traducir(TraduccionContenido contenidoOrigen, TraduccionContenido contenidoDesti) throws Exception {
		try {

	    	if(null!=contenidoOrigen.getTitulo())		contenidoDesti.setTitulo(traducir(contenidoOrigen.getTitulo(),MODE_TXT));
	    	if(null!=contenidoOrigen.getUrl())			contenidoDesti.setUrl(traducir(contenidoOrigen.getUrl(),MODE_TXT));
			if(null!=contenidoOrigen.getTexto())		contenidoDesti.setTexto(traducir(contenidoOrigen.getTexto(),MODE_HTML));
	    	if(null!=contenidoOrigen.getTxbeta())		contenidoDesti.setTxbeta(traducir(contenidoOrigen.getTxbeta(),MODE_HTML));
			
		} catch (Exception e) {
			
			e.printStackTrace();
			log.error(e.getMessage());
			return false;
		}
    	return true;
	}
	

	

	/**
	 * M�todo que parametriza el traductor seg�n el tipo de texto y env�a el texto al m�todo translate
	 * 
	 * @param textTraduccio		texto a traducir
	 * @param modeTraduccio		modo de traducci�n (HTML, TXT)
	 * @return String			texto traducido
	 * @throws Exception		lanza una nueva excepci�n proceso de traducci�n no ha funcionado
	 */
	private String traducir(String textTraduccio, String modeTraduccio) throws Exception {
  
		try {

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
    	
		} catch (Exception e) {

			log.error(e.getMessage());
			throw new Exception(e); 
		}
		
	}
	
}
