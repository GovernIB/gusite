package  es.caib.gusite.microintegracion.traductor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.IdiomaDelegate;

/**
 * Clase que traduce las propiedades de los beans del módulo Rolsac
 * @author Indra
 *
 */
public class Traductor extends AutomaticTranslationService {

	private static final long serialVersionUID = 4007299757118205848L;
	protected static Log log = LogFactory.getLog(Traductor.class);
	private Hashtable<String,String> _hshIdiomes = new Hashtable<String,String>();
	
	private static final String MODE_TXT = "TXT",
								MODE_HTML = "HTML",
								TAG_INI_HTML =  "<HTML><BODY>",
								TAG_FI_HTML = "</BODY></HTML>";
	

	private List<String> _listLang, _listLangTraductor;
	private boolean initialized = false;
	private void inicializaTraductor () throws TraductorException {
		if (initialized) return;
		
		try {
	    	IdiomaDelegate idiomaDelegate = DelegateUtil.getIdiomaDelegate();
			List<Idioma> listaIdiomas;
			listaIdiomas = idiomaDelegate.listarIdiomas();
		
			Map<Integer, Idioma> mapIdiomas = new HashMap<Integer, Idioma>();
			
			for (Idioma idioma : listaIdiomas)
				mapIdiomas.put(idioma.getOrden(), idioma);
			
			List<String> idiomasTraductor = new ArrayList<String>();
			List<String> idiomas = new ArrayList<String>();
			
			
			//Se ordenan por defecto los idiomas según el campo orden
			for (int i = 1; i <= mapIdiomas.size(); i++ ) {
				idiomas.add(mapIdiomas.get(i).getLang());
				idiomasTraductor.add(mapIdiomas.get(i).getLangTraductor());
			}
			
			_listLang = idiomas;
			_listLangTraductor = idiomasTraductor;
			
	    	Iterator<String> itLang =  _listLang.iterator();
	    	Iterator<String> itLangTraductor = _listLangTraductor.iterator();
	    	while (itLang.hasNext()) {
	    		_hshIdiomes.put(itLang.next(), itLangTraductor.next());
	    	}
			
			initialized = true;
			
		} catch (DelegateException e) {
			log.error(e);
			throw new TraductorException(e.getMessage(), e);
		}
		
	}
	
	/**
	 * Constructor por defecto de la clase. 
	 * 
	 * Carga los códigos de Idioma de la capa de negocio para la traducción
	 * e inicia una Hashtable para saber el origen-destino de la traducción
	 * @throws TraductorException 
	 * 
	 */
	public Traductor() throws TraductorException {}
	
	/**
	 * Método que devuelve la url del servidor de traducción Lucy
	 * 
	 * @return _TranslationServerUrl url del servidor de traducción Lucy	
	 */
    public String getTranslationServerUrl() {
        return _translationServerUrl;
    }
    
    /**
     * Método que guarda la url del servidor de traducción Lucy
     * 
     * @param translationServer	nombreServidor:puerto donde está el servidor de traducción Lucy
     */
    public void setTranslationServerUrl(String translationServer) {
    	_translationServerUrl = "http://" + translationServer + "/jaxws-AutomaticTranslationService/AutoTranslate";
    }
    
    /**
     * Método que guarda la dirección de la traducción
     * 
     * @param idiomaOrigen	id del idioma de origen (ca, es, en, de, fr)
     * @param idiomaDesti	id del idioma de destino (ca, es, en, de, fr)
     */
    public void setDirTraduccio(String idiomaOrigen, String idiomaDesti) {
    	_translationDirection = _hshIdiomes.get(idiomaOrigen) + "-" + _hshIdiomes.get(idiomaDesti);
    }
    
    /**
     * Método que devuelve el listado de lenguajes de negocio
     * 
     * @return	_listLang	listado de lenguajes de negocio (ca, es, en, de, fr)
     * @throws TraductorException 
     */
	public List<String> getListLang() throws TraductorException {
		inicializaTraductor();		
		return _listLang;
	}
    

	/**
	 * Método que asigna las propiedades de objeto a null para su posterior eliminación.
	 */
	public void dispose() {
		_listLang = null;
    	_listLangTraductor = null;
		_hshIdiomes = null;
		initialized = false;
	}

}
