package es.caib.gusite.front.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import es.caib.gusite.front.general.BaseCriteria;
import es.caib.gusite.front.general.Microfront;
import es.caib.gusite.front.noticia.NoticiaCriteria;
import es.caib.gusite.micromodel.Agenda;
import es.caib.gusite.micromodel.Archivo;
import es.caib.gusite.micromodel.Contacto;
import es.caib.gusite.micromodel.Contenido;
import es.caib.gusite.micromodel.Encuesta;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.Noticia;
import es.caib.gusite.micromodel.Tipo;
import es.caib.gusite.micromodel.TraduccionContenido;
import es.caib.gusite.micromodel.TraduccionEncuesta;
import es.caib.gusite.micromodel.TraduccionNoticia;
import es.caib.gusite.micromodel.TraduccionTipo;
import es.caib.gusite.plugins.organigrama.UnidadData;

@Service
public class FrontUrlFactory {

	protected static final String LANG_EN = "en";
	protected static final String LANG_CA = "ca";
	protected static final String LANG_ES = "es";

	protected static final String CONTEXT_KEY = "es.caib.gusite.context.front";

	public String traduccionEnlacePie(String context, String url, String idioma, String palabraBuscada) {

		String urlTraducida = "";

		String urlSinSites[] = url.split(context);

		String urlEditada = "";

		urlEditada = urlEditada + urlSinSites[1];

		String urlSeparada[] = urlEditada.split("/");

		Boolean idiomaFinal = true;
		Boolean sinBarraFinal = false;
		Boolean idiomaContact = false;

		for (int i = 1; i < urlSeparada.length; i++) {
			if ((urlSeparada[i].contentEquals("ca")) || (urlSeparada[i].contentEquals("es")) || (urlSeparada[i].contentEquals("en"))
					|| (urlSeparada[i].contentEquals("de")) || (urlSeparada[i].contentEquals("fr"))) {

				idiomaFinal = false;
				idiomaContact = true;

				urlTraducida = urlTraducida + "/" + idioma;

			} else if ((urlSeparada[i].contentEquals("contacte")) || (urlSeparada[i].contentEquals("contacto"))
					|| (urlSeparada[i].contentEquals("contact"))) {

				if (idiomaContact == true) {
					if (idioma.contentEquals("es")) {
						urlTraducida = urlTraducida + "/" + "contact";
					}
					if (idioma.contentEquals("ca")) {
						urlTraducida = urlTraducida + "/" + "contact";
					}
					if (idioma.contentEquals("en")) {
						urlTraducida = urlTraducida + "/" + "contact";
					}
				}
				if (idiomaContact == false) {
					if (idioma.contentEquals("es")) {
						urlTraducida = urlTraducida + "/" + "contacto";
					}
					if (idioma.contentEquals("ca")) {
						urlTraducida = urlTraducida + "/" + "contacte";
					}
					if (idioma.contentEquals("en")) {
						urlTraducida = urlTraducida + "/" + "contact";
					}
				}
				idiomaFinal = false;
			} else if ((urlSeparada[i].contentEquals("cercar")) || (urlSeparada[i].contentEquals("buscar"))
					|| (urlSeparada[i].contentEquals("search"))) {

				if (idioma.equals("es")) {
					urlTraducida = urlTraducida + "/" + "buscar" + "/?cerca=" + palabraBuscada + "&lang=" + idioma;
				}
				if (idioma.equals("ca")) {
					urlTraducida = urlTraducida + "/" + "cercar" + "/?cerca=" + palabraBuscada + "&lang=" + idioma;
				}
				if (idioma.equals("en")) {
					urlTraducida = urlTraducida + "/" + "search" + "/?cerca=" + palabraBuscada + "&lang=" + idioma;
				}

				idiomaFinal = true;
				sinBarraFinal = true;
			} else {
				urlTraducida = urlTraducida + "/" + urlSeparada[i];
			}
		}

		if (urlTraducida.startsWith("//")) {
			String urlSinDobleBarra[] = urlTraducida.split("//");
			urlTraducida = "/" + urlSinDobleBarra[1];
		}

		if (idiomaFinal == false) {

			urlTraducida = urlTraducida + "/";
		} else if ((idiomaFinal == true) && (sinBarraFinal == true)) {
			urlTraducida = urlTraducida + "";
		}

		else {
			urlTraducida = urlTraducida + "/" + idioma + "/";
		}

		return urlTraducida;
	}

	public String encuesta(Microsite microsite, Idioma lang, Encuesta encuesta) {

		return this.microsite(microsite, lang) + "encuesta/" + ((TraduccionEncuesta) encuesta.getTraduccion(lang.getLang().toLowerCase())).getUri()
				+ "/";
	}

	public String envioencuesta(Microsite microsite, Idioma lang, Encuesta encuesta) {

		return this.microsite(microsite, lang) + "envioencuesta/"
				+ ((TraduccionEncuesta) encuesta.getTraduccion(lang.getLang().toLowerCase())).getUri() + "/";
	}

	public String resultadosencuesta(Microsite microsite, Idioma lang, Encuesta encuesta) {

		return this.microsite(microsite, lang) + "resultados/" + ((TraduccionEncuesta) encuesta.getTraduccion(lang.getLang().toLowerCase())).getUri()
				+ "/";
	}

	public String menuPreview(Microsite microsite, Idioma lang) {

		return this.microsite(microsite, lang) + "menupreview/";
	}

	public String errorGenerico(Microsite microsite, Idioma lang) {

		return this.microsite(microsite, lang) + "errorgenerico/";
	}

	public String msgGenerico(Microsite microsite, Idioma lang) {

		return this.microsite(microsite, lang) + "msggenerico/";
	}

	public String tawItemContenido(Microsite microsite, Idioma lang, Contenido contenido) {

		return this.microsite(microsite, lang) + "tawitem/contenido/" + contenido.getId();
	}

	public String tawItemAgenda(Microsite microsite, Idioma lang, Agenda agenda) {

		return this.microsite(microsite, lang) + "tawitem/agenda/" + agenda.getId();
	}

	public String tawItemNoticia(Microsite microsite, Idioma lang, Noticia noticia) {

		return this.microsite(microsite, lang) + "tawitem/noticia/" + noticia.getId();
	}

	public String intranetLogin(String contextPath) {

		return contextPath.concat("/intranetLogin");
	}

	public String intranetHome(Microsite microsite, Idioma lang) {

		return this.microsite(microsite, lang) + "intranet/";
	}

	public String cercar(Microsite microsite, Idioma lang) {

		if (lang.getLang().toLowerCase().equals(LANG_CA)) {
			return this.microsite(microsite) + "cercar/";
		} else if (lang.getLang().toLowerCase().equals(LANG_ES)) {
			return this.microsite(microsite) + "buscar/";
		} else if (lang.getLang().toLowerCase().equals(LANG_EN)) {
			return this.microsite(microsite) + "search/";
		} else {
			return this.microsite(microsite, lang) + "search/";
		}
	}

	public String contacto(Microsite microsite, Idioma lang, Contacto contacto) {

		return this.listarContactos(microsite, lang) + contacto.getId() + "/";
	}

	public String listarContactos(Microsite microsite, Idioma lang) {

		if (lang.getLang().equals(LANG_CA)) {
			return this.microsite(microsite) + "contacte/";
		} else if (lang.getLang().equals(LANG_ES)) {
			return this.microsite(microsite) + "contacto/";
		} else if (lang.getLang().equals(LANG_EN)) {
			return this.microsite(microsite) + "contact/";
		} else {
			return this.microsite(microsite, lang) + "contact/";
		}
	}

	public String listarAgenda(Microsite microsite, Idioma lang) {

		return this.microsite(microsite, lang) + "agenda/";
	}

	public String listarAgendaFechaSinPagina(Microsite microsite, Idioma lang, Date fecha, String mcont, String pcampa) {

		StringBuilder ret = new StringBuilder();
		ret.append(this.listarAgendaFecha(microsite, lang, fecha));
		Map<String, String> params = new Hashtable<String, String>();
		params.put(Microfront.MCONT, mcont);
		params.put(Microfront.PCAMPA, pcampa);
		return this.addParams(ret.toString(), params);
	}

	public String listarAgendaSinPagina(Microsite microsite, Idioma lang, String mcont, String pcampa) {

		StringBuilder ret = new StringBuilder();
		ret.append(this.listarAgenda(microsite, lang));
		Map<String, String> params = new Hashtable<String, String>();
		params.put(Microfront.MCONT, mcont);
		params.put(Microfront.PCAMPA, pcampa);
		return this.addParams(ret.toString(), params);
	}

	private static SimpleDateFormat agendaFechaFormat = new SimpleDateFormat("yyyyMMdd");

	public String listarAgendaFecha(Microsite microsite, Idioma lang, Date fecha) {

		return this.listarAgenda(microsite, lang) + agendaFechaFormat.format(fecha);
	}

	public String listarAgendaFechaFormateada(Microsite microsite, Idioma lang, String fechaEvento) {

		return this.listarAgenda(microsite, lang) + fechaEvento;
	}

	public String archivopubNombre(Microsite microsite, Archivo archivo) {

		return this.archivopubByNombre(microsite, archivo.getNombre());
	}

	public String archivopubByNombre(Microsite microsite, String nombre) {

		return this.microsite(microsite) + "f/?name=" + nombre;
	}

	public String archivopub(Microsite microsite, Archivo archivo) {

		return this.archivopubById(microsite, archivo.getId());
	}

	public String archivopubById(Microsite microsite, Long id) {

		return this.microsite(microsite) + "f/" + id;
	}

	public String contenido(Microsite microsite, Idioma lang, Contenido contenido) {

		return this.microsite(microsite, lang) + "c/" + ((TraduccionContenido) contenido.getTraduccion(lang.getLang().toLowerCase())).getUri() + "/";
	}

	public String contenido(Microsite microsite, Idioma lang, Contenido contenido, String pcampa) {

		String ret = this.contenido(microsite, lang, contenido);
		if (!StringUtils.isEmpty(pcampa)) {
			ret = ret + "?" + Microfront.PCAMPA + "=" + pcampa;
		}
		return ret;
	}

	public String listarElementos(Microsite microsite, Idioma lang, Tipo tipo) {

		return this.microsite(microsite, lang) + "l/" + ((TraduccionTipo) tipo.getTraduccion(lang.getLang().toLowerCase())).getUri() + "/";
	}

	public String qssi(Microsite microsite, Idioma lang, Long qssi) {

		return this.microsite(microsite, lang) + "qssi/" + qssi + "/";
	}

	/**
	 * @deprecated usar listarElementos
	 */
	public String listarNoticias(Microsite microsite, Idioma lang, Tipo tipo) {

		return this.listarElementos(microsite, lang, tipo);
	}

	public String listarAnual(Microsite microsite, Idioma lang, Tipo tipo, String anyo) {

		String any = anyo;

		if (any == "-1") {
			any = "";
		} else if (anyo == "Tots") {
			any = "";
		}
		if (StringUtils.isEmpty(any)) {
			return this.listarElementos(microsite, lang, tipo);
		} else {
			return this.listarElementos(microsite, lang, tipo) + any + "/";
		}
	}

	/*
	 * public String listarAnual(Microsite microsite, Idioma lang, Tipo tipo,
	 * String anyo) {
	 * 
	 * String any = anyo;
	 * 
	 * if (any == "-1") { any = ""; } else if(anyo=="Tots"){ any=""; } if
	 * (StringUtils.isEmpty(any)) { return listarElementos(microsite, lang,
	 * tipo); } else { return listarElementos(microsite, lang, tipo) + any +
	 * "/"; } }
	 */

	public String listarFaqs(Microsite microsite, Idioma lang) {

		return this.microsite(microsite, lang) + "faq/";
	}

	public String accessibilitat(Microsite microsite, Idioma lang) {

		return this.microsite(microsite, lang) + "accessibility/";
	}

	public String mapa(Microsite microsite, Idioma lang) {

		return this.microsite(microsite, lang) + "mapa/";
	}

	public String noticia(Microsite microsite, Idioma lang, Noticia noticia) {

		return this.noticia(microsite, lang, noticia, null);
	}

	public String noticia(Microsite microsite, Idioma lang, Noticia noticia, String mcont) {

		if (!StringUtils.isEmpty(mcont)) {
			return this.microsite(microsite, lang) + "n/" + ((TraduccionNoticia) noticia.getTraduccion(lang.getLang().toLowerCase())).getUri() + "/" + "?" + Microfront.MCONT + "=" + mcont;
		} else {
			return this.microsite(microsite, lang) + "n/" + ((TraduccionNoticia) noticia.getTraduccion(lang.getLang().toLowerCase())).getUri() + "/";
		}
	}


	public String noticiaDescarga(Microsite microsite, Idioma lang, Noticia noticia) {

		return this.microsite(microsite, lang) + "d/" + ((TraduccionNoticia) noticia.getTraduccion(lang.getLang().toLowerCase())).getUri() + "/";
	}

	public String noticiaDescarga(Microsite microsite, Idioma lang, String noticia) {

		return this.microsite(microsite, lang) + "d/" + noticia + "/";
	}

	public String microsite(Microsite microsite) {

		return "/" + microsite.getUri() + "/";
	}

	public String microsite(Microsite microsite, Idioma lang) {

		return "/" + microsite.getUri() + "/" + lang.getLang().toLowerCase() + "/";
	}

	public String home(Microsite microsite, Idioma lang) {

		return this.microsite(microsite, lang);
	}

	/**
	 * Enlace a una unidad administrativa
	 * 
	 * @param ua
	 * @param lang
	 * @return
	 */
	public String ua(UnidadData ua, Idioma lang) {
		return ua.getUrl();
	}

	public String listarContactosSinPagina(Microsite microsite, Idioma lang, BaseCriteria criteria) {
		StringBuilder ret = new StringBuilder();
		ret.append(this.listarContactos(microsite, lang));

		Map<String, String> params = new Hashtable<String, String>();
		if (!StringUtils.isEmpty(criteria.getFiltro())) {
			params.put("filtro", criteria.getFiltro());
		}
		if (!StringUtils.isEmpty(criteria.getOrdenacion())) {
			params.put("ordenacion", criteria.getOrdenacion());
		}

		return this.addParams(ret.toString(), params);
	}

	public String listarNoticiasSinPagina(Microsite microsite, Idioma lang, Tipo tipo, NoticiaCriteria criteria, String mcont, String pcampa) {

		StringBuilder ret = new StringBuilder();
		ret.append(this.listarNoticias(microsite, lang, tipo));

		Map<String, String> params = new Hashtable<String, String>();
		params.put(Microfront.MCONT, mcont);
		params.put(Microfront.PCAMPA, pcampa);
		if (!StringUtils.isEmpty(criteria.getFiltro())) {
			params.put("filtro", criteria.getFiltro());
		}
		if (!StringUtils.isEmpty(criteria.getOrdenacion())) {
			params.put("ordenacion", criteria.getOrdenacion());
		}
		if (criteria.getAnyo() != 0) {
			params.put("anyo", String.valueOf(criteria.getAnyo()));
		}

		return this.addParams(ret.toString(), params);
	}

	private String addParams(String uri, Map<String, String> params) {
		StringBuilder ret = new StringBuilder();

		boolean first = true;
		for (String param : params.keySet()) {
			if (!StringUtils.isEmpty(params.get(param))) {
				if (!first) {
					ret.append("&amp;");
					first = false;
				}
				ret.append(param).append("=").append(params.get(param));
			}
		}
		String paramString = ret.toString();
		if (!StringUtils.isEmpty(paramString)) {
			return uri + "?" + paramString;
		} else {
			return uri;
		}
	}

	/**
	 * Convierte una url interna (relativa) legacy a una válida para front.
	 * Antes comprueba que se trate de una uri interna TODO: hacer que confierta
	 * a la URL final de front, para evitar una redirección
	 * 
	 * @param urlredireccionada
	 * @param lang
	 * @return
	 */
	public String legacyToFrontUri(String legacyUri, Idioma lang) {

		if (this.isLocalLegacyUri(legacyUri)) {
			UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(legacyUri);
			uri.replaceQueryParam(Microfront.PLANG, lang.getLang());
			return "/" + uri.build().toUriString();
		} else {
			return legacyUri;
		}

	}

	/**
	 * Comprueba que se trate de una url relativa interna
	 * 
	 * @param legacyUri
	 * @return
	 */
	public boolean isLocalLegacyUri(String legacyUri) {
		// TODO: es mejorable, pero debería funcionar comprobar que ni comienza
		// por / ni contiene marca de protocolo, y lleva parámetro de site
		return !legacyUri.startsWith("/") && !legacyUri.contains("://") && (legacyUri.indexOf("idsite=") >= 0 || legacyUri.indexOf("mkey=") >= 0);

	}
}
