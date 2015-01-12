package es.caib.gusite.front.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import es.caib.gusite.front.general.BaseCriteria;
import es.caib.gusite.front.general.Microfront;
import es.caib.gusite.front.noticia.NoticiaCriteria;
import es.caib.gusite.micromodel.Agenda;
import es.caib.gusite.micromodel.Archivo;
import es.caib.gusite.micromodel.Contacto;
import es.caib.gusite.micromodel.Contenido;
import es.caib.gusite.micromodel.Encuesta;
import es.caib.gusite.micromodel.Frqssi;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.Noticia;
import es.caib.gusite.micromodel.Tipo;
import es.caib.rolsac.api.v2.unitatAdministrativa.UnitatAdministrativaDTO;

@Service
public class FrontUrlFactory {

	protected static final String LANG_EN = "en";
	protected static final String LANG_CA = "ca";
	protected static final String LANG_ES = "es";

	public String traduccionEnlacePie(String context, String url, String idioma, String palabraBuscada) {
		
		String urlTraducida = "";

		String urlSinSites[] = url.split(context);

		String urlEditada = "";

		urlEditada = urlEditada + urlSinSites[1];

		String urlSeparada[] = urlEditada.split("/");
		
		Boolean idiomaFinal = true;
		Boolean sinBarraFinal=false;
		Boolean idiomaContact=false;

		for (int i = 1; i < urlSeparada.length; i++) {
			if ((urlSeparada[i].contentEquals("ca"))
					|| (urlSeparada[i].contentEquals("es"))
					|| (urlSeparada[i].contentEquals("en"))
					|| (urlSeparada[i].contentEquals("de"))
					|| (urlSeparada[i].contentEquals("fr"))) {

				idiomaFinal = false;
				idiomaContact=true;
		
				urlTraducida = urlTraducida + "/" + idioma;

			} else if((urlSeparada[i].contentEquals("contacte"))||(urlSeparada[i].contentEquals("contacto"))||(urlSeparada[i].contentEquals("contact"))){
					
				if(idiomaContact==true){
					if(idioma.contentEquals("es")){
						urlTraducida = urlTraducida + "/" +"contact";	
					}
					if(idioma.contentEquals("ca")){
						urlTraducida = urlTraducida + "/" +"contact";	
					}
					if(idioma.contentEquals("en")){
						urlTraducida = urlTraducida + "/" +"contact";	
					}
				}
				if(idiomaContact==false){
					if(idioma.contentEquals("es")){
						urlTraducida = urlTraducida + "/" +"contacto";	
					}
					if(idioma.contentEquals("ca")){
						urlTraducida = urlTraducida + "/" +"contacte";	
					}
					if(idioma.contentEquals("en")){
						urlTraducida = urlTraducida + "/" +"contact";	
					}
				}
					idiomaFinal = false;
				}
			else if((urlSeparada[i].contentEquals("cercar"))||(urlSeparada[i].contentEquals("buscar"))||(urlSeparada[i].contentEquals("search"))){
				
				if(idioma.equals("es")){
					urlTraducida = urlTraducida + "/" +"buscar"+"/?cerca="+palabraBuscada+"&lang="+idioma;	
				}
				if(idioma.equals("ca")){
					urlTraducida = urlTraducida + "/" +"cercar"+"/?cerca="+palabraBuscada+"&lang="+idioma;	
				}
				if(idioma.equals("en")){
					urlTraducida = urlTraducida + "/" +"search"+"/?cerca="+palabraBuscada+"&lang="+idioma;	
				}
				
				idiomaFinal = true;
				sinBarraFinal=true;
			}
				else{
				urlTraducida = urlTraducida + "/" + urlSeparada[i];
				}	
		}
		
		if (urlTraducida.startsWith("//")) {
			String urlSinDobleBarra[] = urlTraducida.split("//");
			urlTraducida = "/" + urlSinDobleBarra[1];
		}

		if (idiomaFinal == false) {

			urlTraducida = urlTraducida + "/";
		}
		else if((idiomaFinal==true)&&(sinBarraFinal==true)){
			urlTraducida = urlTraducida + "";
		}

		else {
			urlTraducida = urlTraducida + "/" + idioma + "/";
		}

		return urlTraducida;
	}

	public String encuesta(Microsite microsite, Idioma lang, Encuesta encuesta) {

		return microsite(microsite, lang) + "encuesta/" + encuesta.getId() + "/";
	}
	
	public String envioencuesta(Microsite microsite, Idioma lang, Encuesta encuesta) {
		
		return microsite(microsite, lang) + "envioencuesta/" + encuesta.getId() + "/";
	}
	
	public String resultadosencuesta(Microsite microsite, Idioma lang, Encuesta encuesta) {
		
		return microsite(microsite, lang) + "resultados/" + encuesta.getId() + "/";
	}

	public String menuPreview(Microsite microsite, Idioma lang) {

		return microsite(microsite, lang) + "menupreview/";
	}

	public String errorGenerico(Microsite microsite, Idioma lang) {

		return microsite(microsite, lang) + "errorgenerico/";
	}

	public String msgGenerico(Microsite microsite, Idioma lang) {

		return microsite(microsite, lang) + "msggenerico/";
	}

	public String tawItemContenido(Microsite microsite, Idioma lang, Contenido contenido) {
	
		return microsite(microsite, lang) + "tawitem/contenido/"+contenido.getId();
	}
	
	public String tawItemAgenda(Microsite microsite, Idioma lang, Agenda agenda) {
		
		return microsite(microsite, lang) + "tawitem/agenda/"+agenda.getId();
	}
	
	public String tawItemNoticia(Microsite microsite, Idioma lang, Noticia noticia) {
		
		return microsite(microsite, lang) + "tawitem/noticia/"+noticia.getId();
	}

	public String intranetLogin(Microsite microsite, Idioma lang) {

		return microsite(microsite, lang) + "intranetlogin/";
	}

	public String intranetHome(Microsite microsite, Idioma lang) {

		return microsite(microsite, lang) + "intranet/";
	}

	public String cercar(Microsite microsite, Idioma lang) {
		
		if (lang.getLang().toLowerCase().equals(LANG_CA)) {
			return microsite(microsite) + "cercar/";
		} else if (lang.getLang().toLowerCase().equals(LANG_ES)) {
			return microsite(microsite) + "buscar/";
		} else if (lang.getLang().toLowerCase().equals(LANG_EN)) {
			return microsite(microsite) + "search/";
		} else {
			return microsite(microsite, lang) + "search/";
		}
	}

	public String contacto(Microsite microsite, Idioma lang, Contacto contacto) {

		return listarContactos(microsite, lang) + contacto.getId() + "/";
	}

	public String listarContactos(Microsite microsite, Idioma lang) {

		if (lang.getLang().equals(LANG_CA)) {
			return microsite(microsite) + "contacte/";
		} else if (lang.getLang().equals(LANG_ES)) {
			return microsite(microsite) + "contacto/";
		} else if (lang.getLang().equals(LANG_EN)) {
			return microsite(microsite) + "contact/";
		} else {
			return microsite(microsite, lang) + "contact/";
		}
	}

	public String listarAgenda(Microsite microsite, Idioma lang) {

		return microsite(microsite, lang) + "agenda/";
	}

	public String listarAgendaFechaSinPagina(Microsite microsite, Idioma lang,
			Date fecha, String mcont, String pcampa) {

		StringBuilder ret = new StringBuilder();
		ret.append(listarAgendaFecha(microsite, lang, fecha));
		Map<String, String> params = new Hashtable<String, String>();
		params.put(Microfront.MCONT, mcont);
		params.put(Microfront.PCAMPA, pcampa);
		return this.addParams(ret.toString(), params);
	}

	public String listarAgendaSinPagina(Microsite microsite, Idioma lang,
			String mcont, String pcampa) {

		StringBuilder ret = new StringBuilder();
		ret.append(listarAgenda(microsite, lang));
		Map<String, String> params = new Hashtable<String, String>();
		params.put(Microfront.MCONT, mcont);
		params.put(Microfront.PCAMPA, pcampa);
		return this.addParams(ret.toString(), params);
	}

	private static SimpleDateFormat agendaFechaFormat = new SimpleDateFormat(
			"yyyyMMdd");

	public String listarAgendaFecha(Microsite microsite, Idioma lang, Date fecha) {

		return listarAgenda(microsite, lang) + agendaFechaFormat.format(fecha);
	}

	public String listarAgendaFechaFormateada(Microsite microsite, Idioma lang,
			String fechaEvento) {

		return listarAgenda(microsite, lang) + fechaEvento;
	}

	public String archivopubNombre(Microsite microsite, Archivo archivo) {

		return archivopubByNombre(microsite, archivo.getNombre());
	}

	public String archivopubByNombre(Microsite microsite, String nombre) {

		return microsite(microsite) + "f/" + nombre;
	}

	public String archivopub(Microsite microsite, Archivo archivo) {

		return archivopubById(microsite, archivo.getId());
	}

	public String archivopubById(Microsite microsite, Long id) {

		return microsite(microsite) + "f/" + id;
	}

	public String contenido(Microsite microsite, Idioma lang,
			Contenido contenido) {

		return microsite(microsite, lang) + "c/" + contenido.getId() + "/";
	}
	
	public String contenido(Microsite microsite, Idioma lang, Contenido contenido, String pcampa) {

		String ret = contenido(microsite, lang, contenido);
		if (!StringUtils.isEmpty(pcampa)) {
			ret = ret + "?" + Microfront.PCAMPA  + "=" + pcampa;
		}
		return ret;
	}

	public String listarElementos(Microsite microsite, Idioma lang, Tipo tipo) {

		return microsite(microsite, lang) + "l/" + tipo.getId() + "/";
	}
	
	public String listarElementos(Microsite microsite, Idioma lang, String tipo) {

		return microsite(microsite, lang) + "l/" + tipo + "/";
	}
	
	public String qssi(Microsite microsite, Idioma lang, Long qssi) {

		return microsite(microsite, lang) + "qssi/" + qssi + "/";
	}

	/**
	 * @deprecated usar listarElementos
	 */
	public String listarNoticias(Microsite microsite, Idioma lang, Tipo tipo) {
	
		return listarElementos(microsite, lang, tipo);
	}

	public String listarAnual(Microsite microsite, Idioma lang, Tipo tipo, String anyo) {

		String any = anyo;

		if (any == "-1") {
			any = "";
		} else if(anyo=="Tots"){
			any="";
		} 
		if (StringUtils.isEmpty(any)) {
			return listarElementos(microsite, lang, tipo);
		} else {
			return listarElementos(microsite, lang, tipo) + any + "/";
		}
	}
	
	public String listarAnual(Microsite microsite, Idioma lang, String tipo, String anyo) {

		String any = anyo;

		if (any == "-1") {
			any = "";
		} else if(anyo=="Tots"){
			any="";
		} 
		if (StringUtils.isEmpty(any)) {
			return listarElementos(microsite, lang, tipo);
		} else {
			return listarElementos(microsite, lang, tipo) + any + "/";
		}
	}

	public String listarFaqs(Microsite microsite, Idioma lang) {

		return microsite(microsite, lang) + "faq/";
	}

	public String accessibilitat(Microsite microsite, Idioma lang) {

		return microsite(microsite, lang) + "accessibility/";
	}

	public String mapa(Microsite microsite, Idioma lang) {

		return microsite(microsite, lang) + "mapa/";
	}

	public String noticia(Microsite microsite, Idioma lang, Noticia noticia) {

		return microsite(microsite, lang) + "n/" + noticia.getId() + "/";
	}

	public String noticiaDescarga(Microsite microsite, Idioma lang, Noticia noticia) {

		return microsite(microsite, lang) + "d/" + noticia.getId() + "/";
	}
	
	public String noticiaDescarga(Microsite microsite, Idioma lang, String noticia) {

		return microsite(microsite, lang) + "d/" + noticia + "/";
	}

	public String microsite(Microsite microsite) {
		
		return "/" + microsite.getClaveunica() + "/";
	}

	public String microsite(Microsite microsite, Idioma lang) {
		
		return "/" + microsite.getClaveunica() + "/" + lang.getLang().toLowerCase() + "/";
	}

	public String home(Microsite microsite, Idioma lang) {

		return microsite(microsite, lang);
	}

	/**
	 * Enlace a una unidad administrativa
	 * 
	 * @param ua
	 * @param lang
	 * @return
	 */
	public String ua(UnitatAdministrativaDTO ua, Idioma lang) {
		String uaUrl = "/govern/organigrama/area.do?lang=" + lang.getLang()
				+ "&coduo=" + ua.getId();
		uaUrl = System.getProperty("es.caib.gusite.portal.url") + uaUrl;
		return uaUrl;
	}
	
	public String listarContactosSinPagina(Microsite microsite, Idioma lang, BaseCriteria criteria) {
		StringBuilder ret = new StringBuilder();
		ret.append(listarContactos(microsite, lang));

		Map<String, String> params = new Hashtable<String, String>();
		if (!StringUtils.isEmpty(criteria.getFiltro()))
			params.put("filtro", criteria.getFiltro());
		if (!StringUtils.isEmpty(criteria.getOrdenacion()))
			params.put("ordenacion", criteria.getOrdenacion());

		return this.addParams(ret.toString(), params);
	}
	
	public String listarNoticiasSinPagina(Microsite microsite, Idioma lang,
			Tipo tipo, NoticiaCriteria criteria, String mcont, String pcampa) {

		StringBuilder ret = new StringBuilder();
		ret.append(listarNoticias(microsite, lang, tipo));

		Map<String, String> params = new Hashtable<String, String>();
		params.put(Microfront.MCONT, mcont);
		params.put(Microfront.PCAMPA, pcampa);
		if (!StringUtils.isEmpty(criteria.getFiltro()))
			params.put("filtro", criteria.getFiltro());
		if (!StringUtils.isEmpty(criteria.getOrdenacion()))
			params.put("ordenacion", criteria.getOrdenacion());
		if (criteria.getAnyo() != 0)
			params.put("anyo", String.valueOf(criteria.getAnyo()));

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
}
