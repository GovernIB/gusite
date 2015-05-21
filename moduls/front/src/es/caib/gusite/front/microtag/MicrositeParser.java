package es.caib.gusite.front.microtag;

import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.caib.gusite.front.general.ExceptionFrontPagina;
import es.caib.gusite.front.general.Microfront;
import es.caib.gusite.front.general.bean.Tridato;
import es.caib.gusite.front.noticia.NoticiaCriteria;
import es.caib.gusite.front.noticia.ResultadoNoticias;
import es.caib.gusite.front.service.EncuestasDataService;
import es.caib.gusite.front.service.FrontDataService;
import es.caib.gusite.front.service.FrontUrlFactory;
import es.caib.gusite.front.service.NoticiasDataService;
import es.caib.gusite.front.service.TemplateNameFactory;
import es.caib.gusite.front.thymeleaf.GusiteTemplateProcessor;
import es.caib.gusite.front.util.Fechas;
import es.caib.gusite.front.view.EncuestaView;
import es.caib.gusite.front.view.LayoutView;
import es.caib.gusite.micromodel.Componente;
import es.caib.gusite.micromodel.Encuesta;
import es.caib.gusite.micromodel.Frqssi;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.Noticia;
import es.caib.gusite.micromodel.TraduccionComponente;
import es.caib.gusite.micromodel.TraduccionFrqssi;
import es.caib.gusite.micromodel.TraduccionNoticia;
import es.caib.gusite.micropersistence.delegate.ComponenteDelegate;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.TipoDelegate;
import es.caib.gusite.utilities.auth.ClientPrincipal;

/**
 * Parseador de tags de microsite, que contiene métodos que parsean los tags
 * especiales embebidos en contenidos. Los tags pueden ser de cualquier version.
 * 
 */
@Component
public class MicrositeParser {

	@Autowired
	private FrontDataService dataService;

	@Autowired
	protected NoticiasDataService noticiasDataService;

	@Autowired
	protected EncuestasDataService encuestasDataService;

	@Autowired
	private TemplateNameFactory templateNameFactory;

	@Autowired
	private GusiteTemplateProcessor templateProcessor;

	@Autowired
	protected FrontUrlFactory urlFactory;

	protected static Log log = LogFactory.getLog(MicrositeParser.class);

	private class MicrositeParserAnalysis {
		private Hashtable<String, String> hshTags = new Hashtable<String, String>();
		private Hashtable<String, Tridato> hshTagsStatus = new Hashtable<String, Tridato>();
	}

	/**
	 * Método que analiza el código html. Meterá en hshTagsStatus un listado con
	 * todos los tags a parsear encontrados en el html.
	 * 
	 * @param idioma
	 */
	private MicrositeParserAnalysis doAnalyze(String htmlOld, String idioma) {
		MicrositeParserAnalysis result = new MicrositeParserAnalysis();
		if (htmlOld != null) {

			StringBuffer stbuf = new StringBuffer(htmlOld);

			/* ******** tags version 1.0 ******** */
			int pos = 0;

			// buscar tag de noticias. v1.0
			pos = stbuf.indexOf(Microfront.TAG_NOTICIAS);
			while (pos > -1) {
				StringBuffer stbuf_tmp = new StringBuffer(stbuf.substring(0, pos));
				int pos_ini_tag = stbuf_tmp.lastIndexOf("<");
				int pos_fin_tag = stbuf.indexOf(">", pos);
				String tag = stbuf.substring(pos_ini_tag, pos_fin_tag + 1);
				result.hshTagsStatus.put(tag, new Tridato(Microfront.TAG_NOTICIAS, "V1.0", null));
				pos = stbuf.indexOf(Microfront.TAG_NOTICIAS, pos + tag.length());
			}

			// buscar tag de agenda. v1.0
			pos = stbuf.indexOf(Microfront.TAG_AGENDA);
			while (pos > -1) {
				StringBuffer stbuf_tmp = new StringBuffer(stbuf.substring(0, pos));
				int pos_ini_tag = stbuf_tmp.lastIndexOf("<");
				int pos_fin_tag = stbuf.indexOf(">", pos);
				String tag = stbuf.substring(pos_ini_tag, pos_fin_tag + 1);
				result.hshTagsStatus.put(tag, new Tridato(Microfront.TAG_AGENDA, "V1.0", null));
				pos = stbuf.indexOf(Microfront.TAG_AGENDA, pos + tag.length());
			}

			// buscar tag de banner. v1.0
			pos = stbuf.indexOf(Microfront.TAG_BANNER);
			while (pos > -1) {
				StringBuffer stbuf_tmp = new StringBuffer(stbuf.substring(0, pos));
				int pos_ini_tag = stbuf_tmp.lastIndexOf("<");
				int pos_fin_tag = stbuf.indexOf(">", pos);
				String tag = stbuf.substring(pos_ini_tag, pos_fin_tag + 1);
				result.hshTagsStatus.put(tag, new Tridato(Microfront.TAG_BANNER, "V1.0", null));
				pos = stbuf.indexOf(Microfront.TAG_BANNER, pos + tag.length());
			}

			/* ******** tags version 1.3 ******** */

			// buscar tag de noticias. v1.3
			pos = stbuf.indexOf(Microfront.TAG_GENERICO_DUMMY + Microfront.RNOTICIA);
			while (pos > -1) {
				StringBuffer stbuf_tmp = new StringBuffer(stbuf.substring(0, pos));
				int pos_ini_tag = stbuf_tmp.lastIndexOf("<");
				int pos_fin_tag = stbuf.indexOf("</div>", pos);
				String tag = stbuf.substring(pos_ini_tag, pos_fin_tag + 6);
				String tiponoticia = this.averiguaTipo(tag);
				if (!tiponoticia.equals("-1")) {
					result.hshTagsStatus.put(tag, new Tridato(Microfront.TAG_GENERICO_DUMMY + Microfront.RNOTICIA,
							"V1.3", tiponoticia));
				}
				pos = stbuf.indexOf(Microfront.TAG_GENERICO_DUMMY + Microfront.RNOTICIA, pos + tag.length());
			}

			// buscar tag de agenda. v1.3
			pos = stbuf.indexOf(Microfront.TAG_GENERICO_DUMMY + Microfront.RAGENDA);
			while (pos > -1) {
				StringBuffer stbuf_tmp = new StringBuffer(stbuf.substring(0, pos));
				int pos_ini_tag = stbuf_tmp.lastIndexOf("<");
				int pos_fin_tag = stbuf.indexOf("</div>", pos);
				String tag = stbuf.substring(pos_ini_tag, pos_fin_tag + 6);
				result.hshTagsStatus.put(tag, new Tridato(Microfront.TAG_GENERICO_DUMMY + Microfront.RAGENDA, "V1.3",
						null));
				pos = stbuf.indexOf(Microfront.TAG_GENERICO_DUMMY + Microfront.RAGENDA, pos + tag.length());
			}

			// buscar tag de banner. v1.3
			pos = stbuf.indexOf(Microfront.TAG_GENERICO_DUMMY + Microfront.RBANNER);
			while (pos > -1) {
				StringBuffer stbuf_tmp = new StringBuffer(stbuf.substring(0, pos));
				int pos_ini_tag = stbuf_tmp.lastIndexOf("<");
				int pos_fin_tag = stbuf.indexOf("</div>", pos);
				String tag = stbuf.substring(pos_ini_tag, pos_fin_tag + 6);
				result.hshTagsStatus.put(tag, new Tridato(Microfront.TAG_GENERICO_DUMMY + Microfront.RBANNER, "V1.3",
						null));
				pos = stbuf.indexOf(Microfront.TAG_GENERICO_DUMMY + Microfront.RBANNER, pos + tag.length());
			}

			/* ******** tags version 1.4 ******** */
			/*
			 * Los tags de agenda y banner no han sufrido variacion desde la
			 * version anterior.
			 */

			// buscar tag de noticias. v1.4
			pos = stbuf.indexOf(Microfront.TAG_GENERICO_DUMMY + Microfront.RNOTICIA);
			while (pos > -1) {
				StringBuffer stbuf_tmp = new StringBuffer(stbuf.substring(0, pos));
				int pos_ini_tag = stbuf_tmp.lastIndexOf("<");
				int pos_fin_tag = stbuf.indexOf("</div>", pos);
				String tag = stbuf.substring(pos_ini_tag, pos_fin_tag + 6);
				String idcomponente = this.averiguaComponente(tag);
				if (!idcomponente.equals("-1")) {
					result.hshTagsStatus.put(tag, new Tridato(Microfront.TAG_GENERICO_DUMMY + Microfront.RNOTICIA,
							"V1.4", idcomponente));
				}
				pos = stbuf.indexOf(Microfront.TAG_GENERICO_DUMMY + Microfront.RNOTICIA, pos + tag.length());
			}

			// buscar tag de qssi
			pos = stbuf.indexOf(Microfront.TAG_GENERICO_DUMMY + Microfront.RQSSI);
			while (pos > -1) {
				StringBuffer stbuf_tmp = new StringBuffer(stbuf.substring(0, pos));
				int pos_ini_tag = stbuf_tmp.lastIndexOf("<");
				int pos_fin_tag = stbuf.indexOf("</div>", pos);
				String tag = stbuf.substring(pos_ini_tag, pos_fin_tag + 6);
				String idcomponente = this.averiguaComponente(tag);
				if (!idcomponente.equals("-1")) {
					result.hshTagsStatus.put(tag, new Tridato(Microfront.TAG_GENERICO_DUMMY + Microfront.RQSSI, "V1.4",
							idcomponente));
				}
				pos = stbuf.indexOf(Microfront.TAG_GENERICO_DUMMY + Microfront.RQSSI, pos + tag.length());
			}

			/* ******** tags version 2.0 ******** */
			/*
			 * Los tags de agenda y banner no han sufrido variacion desde la
			 * version anterior.
			 */

			// buscar tag de encuestas. v2.0
			pos = stbuf.indexOf(Microfront.TAG_GENERICO_DUMMY + Microfront.RENCUESTA);
			while (pos > -1) {
				StringBuffer stbuf_tmp = new StringBuffer(stbuf.substring(0, pos));
				int pos_ini_tag = stbuf_tmp.lastIndexOf("<");
				int pos_fin_tag = stbuf.indexOf("</div>", pos);
				String tag = stbuf.substring(pos_ini_tag, pos_fin_tag + 6);
				String idcomponente = this.averiguaComponente(tag);
				if (!idcomponente.equals("-1")) {
					result.hshTagsStatus.put(tag, new Tridato(Microfront.TAG_GENERICO_DUMMY + Microfront.RENCUESTA,
							"V2.0", idcomponente));
				}
				pos = stbuf.indexOf(Microfront.TAG_GENERICO_DUMMY + Microfront.RENCUESTA, pos + tag.length());
			}

		}
		return result;
	}

	/**
	 * Método que parsea el código html. Reemplaza los tags de los microsites
	 * por los "trozos" de html correspondientes. Además meterá en hshTagsStatus
	 * un listado con todos los tags a parsear encontrados y su estado. Además
	 * meterá en hshTags un listado con todos los tags a parsear encontrados y
	 * su correspondiete "trozo" de html.
	 * 
	 */
	public String doParser(Microsite site, String htmlOld, String idioma, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			if (htmlOld == null) {
				return null;
			}
			MicrositeParserAnalysis analysis = this.doAnalyze(htmlOld, idioma);
			this.doCalculaTags(site, idioma, analysis, request, response);

			StringBuffer htmlParsed = new StringBuffer(htmlOld);

			if (htmlOld != null) {
				Enumeration<String> enumera = analysis.hshTags.keys();
				while (enumera.hasMoreElements()) {
					String oldstringtmp = enumera.nextElement();
					String newstringtmp = analysis.hshTags.get(oldstringtmp);
					log.debug("Parseado " + (analysis.hshTagsStatus.get(oldstringtmp)).getKey() + " "
							+ (analysis.hshTagsStatus.get(oldstringtmp)).getValue1());

					int pos = htmlParsed.indexOf(oldstringtmp);
					while (pos > -1) {
						htmlParsed.replace(pos, pos + oldstringtmp.length(), newstringtmp);
						pos = htmlParsed.toString().indexOf(oldstringtmp, pos + newstringtmp.length());
					}
				}
			}
			return htmlParsed.toString();

		} catch (Exception e) {
			log.error("Se ha producido un error parseando html. ", e);
			return "";
		}

	}

	/**
	 * Método que parsea el código html.<br/>
	 * Reemplaza los tags de los microsites por comentarios Además meterá en
	 * hshTagsStatus un listado con todos los tags a parsear encontrados y su
	 * estado. Además meterá en hshTags un listado con todos los tags a parsear
	 * encontrados y su correspondiete "trozo" de html.
	 * 
	 * @return
	 * 
	 */
	public String doParser2Comentario(Microsite site, String htmlOld, String idioma) {
		try {
			MicrositeParserAnalysis analysis = this.doAnalyze(htmlOld, idioma);
			this.doCalculaTagsComentario(site, idioma, analysis);

			StringBuffer htmlParsed = new StringBuffer(htmlOld);
			if (htmlOld != null) {
				Enumeration<String> enumera = analysis.hshTags.keys();
				while (enumera.hasMoreElements()) {
					String oldstringtmp = enumera.nextElement();
					String newstringtmp = analysis.hshTags.get(oldstringtmp);
					log.debug("Parseado " + (analysis.hshTagsStatus.get(oldstringtmp)).getKey() + " "
							+ (analysis.hshTagsStatus.get(oldstringtmp)).getValue1());

					int pos = htmlParsed.indexOf(oldstringtmp);
					while (pos > -1) {
						htmlParsed.replace(pos, pos + oldstringtmp.length(), newstringtmp);
						pos = htmlParsed.toString().indexOf(oldstringtmp, pos + newstringtmp.length());
					}
				}
			}
			return htmlParsed.toString();

		} catch (Exception e) {
			log.error("Se ha producido un error parseando html. " + e);
			return "";
		}

	}

	/**
	 * Reemplaza el tag por un comentario.
	 * 
	 * @param analysis
	 * @param idioma
	 * @param site
	 */
	private void doCalculaTagsComentario(Microsite site, String idioma, MicrositeParserAnalysis analysis) {

		Enumeration<String> enumera = analysis.hshTagsStatus.keys();
		while (enumera.hasMoreElements()) {
			String key = enumera.nextElement();
			Tridato tridato = analysis.hshTagsStatus.get(key);

			if (tridato.getKey().equals(Microfront.TAG_NOTICIAS)) {
				analysis.hshTags.put(key,
						"<!-- " + Microfront.TAG_NOTICIAS + site.getId() + idioma + site.getNumeronoticias() + " -->");
			}
			if (tridato.getKey().equals(Microfront.TAG_AGENDA)) {
				analysis.hshTags.put(key,
						"<!-- " + Microfront.TAG_AGENDA + site.getId() + idioma + site.getNumeronoticias() + " -->");
			}
			if (tridato.getKey().equals(Microfront.TAG_BANNER)) {
				analysis.hshTags.put(key, "<!-- " + Microfront.TAG_BANNER + site.getId() + idioma + 1 + " -->");
			}
			if (tridato.getKey().equals(Microfront.TAG_GENERICO_DUMMY + Microfront.RAGENDA)) {
				analysis.hshTags.put(key, "<!-- " + Microfront.TAG_GENERICO_DUMMY + Microfront.RAGENDA + site.getId()
						+ idioma + site.getNumeronoticias() + " -->");
			}
			if (tridato.getKey().equals(Microfront.TAG_GENERICO_DUMMY + Microfront.RQSSI)) {
				analysis.hshTags.put(key, "<!-- " + Microfront.TAG_GENERICO_DUMMY + Microfront.RQSSI + site.getId()
						+ tridato.getValue2() + idioma + " -->");
			}
			if (tridato.getKey().equals(Microfront.TAG_GENERICO_DUMMY + Microfront.RBANNER)) {
				analysis.hshTags.put(key, "<!-- " + Microfront.TAG_GENERICO_DUMMY + Microfront.RBANNER + site.getId()
						+ idioma + 1 + " -->");
			}
			if (tridato.getKey().equals(Microfront.TAG_GENERICO_DUMMY + Microfront.RNOTICIA)) {
				if (tridato.getValue1().equals("V1.3")) {
					analysis.hshTags.put(
							key,
							"<!-- " + Microfront.TAG_GENERICO_DUMMY + Microfront.RNOTICIA + site.getId()
									+ tridato.getValue2() + idioma + site.getNumeronoticias() + " -->");
				}
				if (tridato.getValue1().equals("V1.4")) {
					analysis.hshTags.put(
							key,
							"<!-- " + Microfront.TAG_GENERICO_DUMMY + Microfront.RNOTICIA + site.getId()
									+ tridato.getValue2() + idioma + " -->");
				}
			}
			if (tridato.getKey().equals(Microfront.TAG_GENERICO_DUMMY + Microfront.RENCUESTA)) {
				if (tridato.getValue1().equals("V2.0")) {
					analysis.hshTags.put(
							key,
							"<!-- " + Microfront.TAG_GENERICO_DUMMY + Microfront.RENCUESTA + site.getId()
									+ tridato.getValue2() + idioma + " -->");
				}
			}

		}

	}

	/**
	 * Método privado para calcular Tags
	 * 
	 * @param site
	 * @param idioma
	 * @param analysis
	 * @throws DelegateException
	 * @throws ExceptionFrontPagina
	 * @throws NumberFormatException
	 */
	private void doCalculaTags(Microsite site, String idioma, MicrositeParserAnalysis analysis,
			HttpServletRequest request, HttpServletResponse response) throws DelegateException, NumberFormatException,
			ExceptionFrontPagina {

		Enumeration<String> enumera = analysis.hshTagsStatus.keys();
		while (enumera.hasMoreElements()) {
			String key = enumera.nextElement();
			Tridato tridato = analysis.hshTagsStatus.get(key);

			if (tridato.getKey().equals(Microfront.TAG_NOTICIAS)) {
				analysis.hshTags.put(key, this.getHtmlNoticias(site, idioma, request, response));
			} else if (tridato.getKey().equals(Microfront.TAG_AGENDA)
					|| tridato.getKey().equals(Microfront.TAG_GENERICO_DUMMY + Microfront.RAGENDA)) {
				analysis.hshTags.put(key, this.getHtmlAgendaCalendario(site, idioma, request, response));
			} else if (tridato.getKey().equals(Microfront.TAG_GENERICO_DUMMY + Microfront.RQSSI)) {
				analysis.hshTags.put(key, this.getHtmlQssi(site, tridato.getValue2(), idioma));
			} else if (tridato.getKey().equals(Microfront.TAG_GENERICO_DUMMY + Microfront.RNOTICIA)) {
				if (tridato.getValue1().equals("V1.3")) {
					analysis.hshTags
							.put(key, this.getHtmlElementos(site, Long.parseLong(tridato.getValue2()), idioma, request,
									response));
				}
				if (tridato.getValue1().equals("V1.4")) {
					analysis.hshTags.put(key, this.getHtmlElementosComponente(site,
							Long.parseLong(tridato.getValue2()), idioma, request, response));
				}
			}
			if (tridato.getKey().equals(Microfront.TAG_GENERICO_DUMMY + Microfront.RENCUESTA)) {
				if (tridato.getValue1().equals("V2.0")) {
					analysis.hshTags.put(key,
							this.getHtmlEncuesta(site, Long.parseLong(tridato.getValue2()), idioma, request, response));
				}
			}

		}

	}

	/**
	 * Método que devuelve un string preparado para insertar en un html. Ese
	 * string contiene una encuesta.
	 * 
	 * @param idmicrosite
	 * @param idencuesta
	 * @param idioma
	 * @param response
	 * @param request2
	 * @return StringBuffer con el pegote de html
	 * @throws ExceptionFrontPagina
	 */
	public String getHtmlEncuesta(Microsite site, Long idEncuesta, String idioma, HttpServletRequest request,
			HttpServletResponse response) throws ExceptionFrontPagina {

		Encuesta encuesta = this.encuestasDataService.getEncuesta(site, idioma, idEncuesta);
		ResourceBundle rb = ResourceBundle.getBundle("ApplicationResources_front", new Locale(idioma.toUpperCase(),
				idioma.toUpperCase()));

		EncuestaView view = new EncuestaView();

		view.setMicrosite(site);
		view.setIdioma(idioma);
		view.setEncuesta(encuesta);
		view.setFechasVigente(Fechas.vigente(encuesta.getFpublicacion(), encuesta.getFcaducidad()));

		// TODO: esto tiene que ver con el procesamiento que se hacía en
		// microfront en UserRequestProcessor
		// u91856 29/06/2011 Respostes que venen donades fixes
		Map param = (request == null) ? null : (HashMap) request.getSession().getAttribute(Microfront.ENCPARAM);
		if (param == null) {
			param = new HashMap();
		} else {
			request.getSession().removeAttribute(Microfront.ENCPARAM);
		}
		// Comprobar si funciona correctamente
		view.setRespostesFixades(param);

		// Identificacion del usuario que contesta segun configuracion de la
		// encuesta
		if (encuesta.getIdentificacion().equals("S")) {
			try {
				ClientPrincipal principal = null;
				principal = ClientPrincipal.getCurrent();
				// principal = (CertsPrincipal) request.getUserPrincipal();
				String identificacio = rb.getString("encuesta.identificacion").replaceAll("\\{1\\}",
						principal.getFullName());
				view.setIdentificacion(identificacio);
			} catch (NamingException e) {
				log.error("Error en la identificación del usuario en la encuesta: " + idEncuesta + " ---> " + e);
				throw new ExceptionFrontPagina("Error en la identificación del usuario");
			}
		}

		return this.templateProcessor.process(LayoutView.modelForView(view),
				this.templateNameFactory.componenteEncuesta(site), idioma, request, response);

	}

	/**
	 * Método que devuelve un string preparado para insertar en un html. Ese
	 * string contiene el listado de los últimos elementos según el 'id' del
	 * componente. Este método es el que se usa para los tags de la version 1.4
	 * 
	 * @param site
	 * @param id
	 * @param idioma
	 * @param response
	 * @param request
	 * @return String todo el 'html' necesario para montar el listado
	 * @throws DelegateException
	 */
	public String getHtmlElementosComponente(Microsite site, long id, String idioma, HttpServletRequest request,
			HttpServletResponse response) throws DelegateException {

		ComponenteDelegate compodel = DelegateUtil.getComponentesDelegate();
		Componente componente = compodel.obtenerComponente(id);

		if (componente.getTipo().getTipoelemento().equals(Microfront.ELEM_NOTICIA)) {
			// si es de clase noticia.
			return this.getHtmlComponenteTNoticia(site, componente, idioma, request, response);
		} else if (componente.getTipo().getTipoelemento().equals(Microfront.ELEM_LINK)) {
			// si es de clase enlace.
			return this.getHtmlComponenteTEnlace(site, componente, idioma, request, response);
		} else if (componente.getTipo().getTipoelemento().equals(Microfront.ELEM_DOCUMENTO)) {
			// si es de clase documento
			return this.getHtmlComponenteTDocumento(site, componente, idioma, request, response);
		} else if (componente.getTipo().getTipoelemento().equals(Microfront.ELEM_CONEXIO_EXTERNA)) {
			// si es de clase conexión externa
			return this.getHtmlComponenteTExterno(site, componente, idioma, request, response);
		}

		// TODO: error?
		return "";
	}

	/**
	 * Método privado que devuelve un string para insertar en un HTML un
	 * documento externo
	 * 
	 * @param idmicrosite
	 * @param componente
	 * @param idioma
	 * @param request
	 * @param response
	 * @return StringBuffer Código HTML
	 * @throws DelegateException
	 */
	private String getHtmlComponenteTExterno(Microsite site, Componente componente, String idioma,
			HttpServletRequest request, HttpServletResponse response) throws DelegateException {

		String nombreformulario = "formnoticiasexternocomp" + componente.getId().longValue();
		String nombre_elemento = ((TraduccionComponente) componente.getTraduccion(idioma)) != null ? ((TraduccionComponente) componente
				.getTraduccion(idioma)).getTitulo() : "[ empty ]";

		TipoDelegate tD = DelegateUtil.getTipoDelegate();

		// preparamos el map a enviar
		Map<String, String> filtrado = new HashMap<String, String>();
		filtrado.put("lang", idioma);
		filtrado.put("nameform", nombreformulario);
		filtrado.put("idcomponente", "" + componente.getId().longValue());

		String resultadoHTML = tD.obtenerPegoteHTMLExterno(componente.getTipo().getId(), filtrado);

		Map<String, Object> model = new HashMap<String, Object>();
		model.put("MVS_nombreformulario", nombreformulario);
		model.put("MVS_nombreelemento", nombre_elemento);
		model.put("MVS_idcomponente", "element" + componente.getId());
		model.put("MVS_mchtml", resultadoHTML);
		model.put("MVS_microsite", site);
		model.put("MVS_idioma", new Idioma(idioma));

		return this.templateProcessor.process(model, this.templateNameFactory.componenteExterno(site), idioma, request,
				response);

	}

	/**
	 * Método privado que devuelve un string para insertar en un HTML una
	 * Noticia
	 * 
	 * @param idmicrosite
	 * @param componente
	 * @param idioma
	 * @return StringBuffer Código Html
	 * @throws DelegateException
	 */
	private String getHtmlComponenteTNoticia(Microsite site, Componente componente, String idioma,
			HttpServletRequest request, HttpServletResponse response) throws DelegateException {
		Map<String, Object> model = new HashMap<String, Object>();
		// -TODO: TEST plantilla thymeleaf
		Collection<Noticia> colnoticias = this.getListaNoticias(site, componente, idioma);
		String nombre_elemento = ((TraduccionComponente) componente.getTraduccion(idioma)) != null ? ((TraduccionComponente) componente
				.getTraduccion(idioma)).getTitulo() : "[ empty ]";
		List<Noticia> listanoticias;
		if (colnoticias instanceof List) {
			listanoticias = (List<Noticia>) colnoticias;
		} else {
			listanoticias = new ArrayList<Noticia>(colnoticias);
		}
		model.put("nombre_elemento", nombre_elemento);
		model.put("lista_noticias", listanoticias);
		model.put("MVS_componente", componente);
		model.put("MVS_microsite", site);
		model.put("MVS_idioma", new Idioma(idioma));
		return this.templateProcessor.process(model, this.templateNameFactory.moduloListaNoticias(site), idioma,
				request, response);
	}

	private Collection<Noticia> getListaNoticias(Microsite site, Componente componente, String idioma)
			throws DelegateException {
		NoticiaCriteria criteria = new NoticiaCriteria();
		componente.getTipo().setIdi(idioma);
		criteria.setTipo(componente.getTipo());
		criteria.setOrdenacion(componente.getOrdenacion().toString());
		criteria.setPagina(1);
		criteria.setTamPagina(componente.getNumelementos().intValue());

		ResultadoNoticias<Noticia> noticias = this.noticiasDataService.listarNoticias(site, idioma, criteria);
		return noticias.getResultados();
	}

	/**
	 * Método privado que devuelve un string para insertar en un HTML un Enlace
	 * 
	 * @param idmicrosite
	 * @param componente
	 * @param idioma
	 * @return StringBuffer Código HTML
	 * @throws DelegateException
	 */
	private String getHtmlComponenteTEnlace(Microsite site, Componente componente, String idioma,
			HttpServletRequest request, HttpServletResponse response) throws DelegateException {

		Map<String, Object> model = new HashMap<String, Object>();
		// -TODO: TEST plantilla thymeleaf
		Collection<Noticia> colnoticias = this.getListaNoticias(site, componente, idioma);
		String nombre_elemento = ((TraduccionComponente) componente.getTraduccion(idioma)) != null ? ((TraduccionComponente) componente
				.getTraduccion(idioma)).getTitulo() : "[ empty ]";
		List<Noticia> listanoticias;
		if (colnoticias instanceof List) {
			listanoticias = (List<Noticia>) colnoticias;
		} else {
			listanoticias = new ArrayList<Noticia>(colnoticias);
		}
		model.put("nombre_elemento", nombre_elemento);
		model.put("lista_noticias", listanoticias);
		model.put("MVS_componente", componente);
		model.put("MVS_microsite", site);
		model.put("MVS_idioma", new Idioma(idioma));
		String toReturn = this.templateProcessor.process(model, this.templateNameFactory.moduloListaEnlaces(site), idioma,
				request, response);
		return toReturn;
	}

	/**
	 * Método privado que devuelve un string para insertar en un HTML un
	 * documento
	 * 
	 * @param idmicrosite
	 * @param componente
	 * @param idioma
	 * @return StringBuffer Código HTML
	 * @throws DelegateException
	 */
	private String getHtmlComponenteTDocumento(Microsite site, Componente componente, String idioma,
			HttpServletRequest request, HttpServletResponse response) throws DelegateException {
		StringBuffer retorno = new StringBuffer();
		Map<String, Object> model = new HashMap<String, Object>();
		Collection<Noticia> colnoticias = this.getListaNoticias(site, componente, idioma);
		String nombre_elemento = ((TraduccionComponente) componente.getTraduccion(idioma)) != null ? ((TraduccionComponente) componente
				.getTraduccion(idioma)).getTitulo() : "[ empty ]";
		// ////////// -TODO: TEST plantilla thymeleaf
		ResourceBundle rb = ResourceBundle.getBundle("ApplicationResources_front", new Locale(idioma.toUpperCase(),
				idioma.toUpperCase()));
		List<Noticia> listanoticias;
		if (colnoticias instanceof List) {
			listanoticias = (List<Noticia>) colnoticias;
		} else {
			listanoticias = new ArrayList<Noticia>(colnoticias);
		}
		model.put("nombre_elemento", nombre_elemento);
		model.put("lista_noticias", listanoticias);
		model.put("MVS_componente", componente);
		model.put("MVS_microsite", site);
		model.put("MVS_idioma", new Idioma(idioma));
		String toReturn = this.templateProcessor.process(model, this.templateNameFactory.moduloListaDocumentos(site), idioma,
				request, response);
		return toReturn;
	}

	private String getHtmlQssi(Microsite site, String ideqssi, String idioma) throws ExceptionFrontPagina {

		Long id = new Long(Long.parseLong(ideqssi));
		ResourceBundle.getBundle("ApplicationResources_front", new Locale(idioma.toUpperCase(), idioma.toUpperCase()));
		Frqssi qssi = this.dataService.getFormularioQssi(site, idioma, id);
		String laurl = this.urlFactory.qssiFinalUrl(site, idioma, qssi);
		return "<a href=\"" + laurl + "\">" + ((TraduccionFrqssi) qssi.getTraduce()).getNombre() + "</a>";

	}

	/* ******************** auxiliares ******************* */

	private String averiguaTipo(String cadena) {
		MParserHTML parsehtml = new MParserHTML();
		return parsehtml.getAtributoTag(cadena, "propertyid");
	}

	private String averiguaComponente(String cadena) {
		MParserHTML parsehtml = new MParserHTML();
		return parsehtml.getAtributoTag(cadena, "componenteid");
	}

	// Agenda
	/**
	 * Método que devuelve un string preparado para insertar en un html. Ese
	 * string contiene el calendario de la agenda. Se le pasa el numero de meses
	 * que se quiere mostrar.
	 * 
	 * @param site
	 * @param idioma
	 * @return StringBuffer con el codigo HTML del calendario de la agenda
	 * @throws DelegateException
	 */
	private String getHtmlAgendaCalendario(Microsite site, String idioma, HttpServletRequest request,
			HttpServletResponse response) throws DelegateException {

		Map<String, Object> model = new HashMap<String, Object>();

		model.put("MVS_datos_agenda_calendario", this.dataService.getDatosCalendario(site, idioma));
		model.put("MVS_microsite", site);
		model.put("MVS_idioma", new Idioma(idioma));

		return this.templateProcessor.process(model, this.templateNameFactory.moduloAgenda(site), idioma, request,
				response);
	}

	/**
	 * Método que devuelve un string preparado para insertar en un html. Ese
	 * string contiene el listado de los últimos elementos de clase 'noticias'.
	 * 
	 * @param site
	 * @param idioma
	 * @return StringBuffer que contiene todo el 'html' necesario para montar
	 *         noticias
	 * @throws DelegateException
	 */
	public String getHtmlNoticias(Microsite site, String idioma, HttpServletRequest request,
			HttpServletResponse response) throws DelegateException {

		Map<String, Object> model = new HashMap<String, Object>();

		model.put("MVS_home_datos_noticias_listado", this.dataService.getNoticiasHome(site, idioma));
		model.put("MVS_microsite", site);
		model.put("MVS_idioma", new Idioma(idioma));

		return this.templateProcessor.process(model, this.templateNameFactory.moduloNoticiasHome(site), idioma,
				request, response);

	}

	/**
	 * Método que devuelve un string preparado para insertar en un html. Ese
	 * string contiene el listado de los últimos elementos según el 'tipo' que
	 * se pase. Este método es el que se usa para los tags de la version 1.3
	 * 
	 * @param idmicrosite
	 * @param txttipo
	 *            tipo de elemento
	 * @param idioma
	 * @return String que contiene todo el 'html' necesario para montar el
	 *         listado
	 * @throws DelegateException
	 * @throws ExceptionFrontPagina
	 */
	public String getHtmlElementos(Microsite site, Long tipo, String idioma, HttpServletRequest request,
			HttpServletResponse response) throws ExceptionFrontPagina, DelegateException {

		Map<String, Object> model = new HashMap<String, Object>();
		model.put("MVS_tipo", this.noticiasDataService.loadTipo(tipo, idioma));
		model.put("MVS_listado_elementos",
				this.noticiasDataService.listarNoticiasTipo(site, idioma, tipo, site.getNumeronoticias()));
		model.put("MVS_microsite", site);
		model.put("MVS_idioma", new Idioma(idioma));

		return this.templateProcessor.process(model, this.templateNameFactory.componenteElementos(site), idioma,
				request, response);

	}

}
