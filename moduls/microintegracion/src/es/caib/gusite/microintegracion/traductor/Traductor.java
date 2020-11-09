package es.caib.gusite.microintegracion.traductor;

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
 *
 * @author Indra
 *
 */
public class Traductor {

	private static final long serialVersionUID = 4007299757118205848L;
	protected static Log log = LogFactory.getLog(Traductor.class);
	private Hashtable<String, String> _hshIdiomes = new Hashtable<String, String>();

	private static final String MODE_TXT = "TXT", MODE_HTML = "HTML", TAG_INI_HTML = "<HTML><BODY>",
			TAG_FI_HTML = "</BODY></HTML>";

	private List<String> _listLang, _listLangTraductor;
	private boolean initialized = false;

	private void inicializaTraductor() throws TraductorException {
		if (initialized)
			return;

		try {
			final IdiomaDelegate idiomaDelegate = DelegateUtil.getIdiomaDelegate();
			List<Idioma> listaIdiomas;
			listaIdiomas = idiomaDelegate.listarIdiomas();

			final Map<Integer, Idioma> mapIdiomas = new HashMap<Integer, Idioma>();

			for (final Idioma idioma : listaIdiomas)
				mapIdiomas.put(idioma.getOrden(), idioma);

			final List<String> idiomasTraductor = new ArrayList<String>();
			final List<String> idiomas = new ArrayList<String>();

			// Se ordenan por defecto los idiomas según el campo orden
			for (int i = 1; i <= mapIdiomas.size(); i++) {
				idiomas.add(mapIdiomas.get(i).getLang());
				idiomasTraductor.add(mapIdiomas.get(i).getLangTraductor());
			}

			_listLang = idiomas;
			_listLangTraductor = idiomasTraductor;

			final Iterator<String> itLang = _listLang.iterator();
			final Iterator<String> itLangTraductor = _listLangTraductor.iterator();
			while (itLang.hasNext()) {
				_hshIdiomes.put(itLang.next(), itLangTraductor.next());
			}

			initialized = true;

		} catch (final DelegateException e) {
			log.error(e);
			throw new TraductorException(e.getMessage(), e);
		}

	}

	/**
	 * Constructor por defecto de la clase.
	 *
	 * Carga los códigos de Idioma de la capa de negocio para la traducción e inicia
	 * una Hashtable para saber el origen-destino de la traducción
	 *
	 * @throws TraductorException
	 *
	 */
	public Traductor() throws TraductorException {
	}

	/**
	 * Método que devuelve el listado de lenguajes de negocio
	 *
	 * @return _listLang listado de lenguajes de negocio (ca, es, en, de, fr)
	 * @throws TraductorException
	 */
	public List<String> getListLang() throws TraductorException {
		inicializaTraductor();
		return _listLang;
	}

	/**
	 * Método que asigna las propiedades de objeto a null para su posterior
	 * eliminación.
	 */
	public void dispose() {
		_listLang = null;
		_listLangTraductor = null;
		_hshIdiomes = null;
		initialized = false;
	}

}
