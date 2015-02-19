package es.caib.gusite.front.microtag;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.caib.gusite.front.general.ExceptionFrontPagina;
import es.caib.gusite.front.general.Microfront;
import es.caib.gusite.front.general.bean.Tridato;
import es.caib.gusite.front.service.FrontDataService;
import es.caib.gusite.front.service.NoticiasDataService;
import es.caib.gusite.front.service.TemplateNameFactory;
import es.caib.gusite.front.thymeleaf.GusiteFlow;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micropersistence.delegate.DelegateException;

/**
 * Clase MicrositeParser. Parseador de tags de microsite. Esta clase contiene
 * métodos que parsean los tags especiales de los microsites. Los tags pueden
 * ser de cualquier version.
 * 
 */
@Component
public class MicrositeParser {

	@Autowired
	private FrontDataService dataService;

	@Autowired
	protected NoticiasDataService noticiasDataService;

	@Autowired
	private TemplateNameFactory templateNameFactory;

	@Autowired
	private GusiteFlow gusiteFlow;

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
				StringBuffer stbuf_tmp = new StringBuffer(stbuf.substring(0,
						pos));
				int pos_ini_tag = stbuf_tmp.lastIndexOf("<");
				int pos_fin_tag = stbuf.indexOf(">", pos);
				String tag = stbuf.substring(pos_ini_tag, pos_fin_tag + 1);
				result.hshTagsStatus.put(tag, new Tridato(
						Microfront.TAG_NOTICIAS, "V1.0", null));
				pos = stbuf
						.indexOf(Microfront.TAG_NOTICIAS, pos + tag.length());
			}

			// buscar tag de agenda. v1.0
			pos = stbuf.indexOf(Microfront.TAG_AGENDA);
			while (pos > -1) {
				StringBuffer stbuf_tmp = new StringBuffer(stbuf.substring(0,
						pos));
				int pos_ini_tag = stbuf_tmp.lastIndexOf("<");
				int pos_fin_tag = stbuf.indexOf(">", pos);
				String tag = stbuf.substring(pos_ini_tag, pos_fin_tag + 1);
				result.hshTagsStatus.put(tag, new Tridato(
						Microfront.TAG_AGENDA, "V1.0", null));
				pos = stbuf.indexOf(Microfront.TAG_AGENDA, pos + tag.length());
			}

			// buscar tag de banner. v1.0
			pos = stbuf.indexOf(Microfront.TAG_BANNER);
			while (pos > -1) {
				StringBuffer stbuf_tmp = new StringBuffer(stbuf.substring(0,
						pos));
				int pos_ini_tag = stbuf_tmp.lastIndexOf("<");
				int pos_fin_tag = stbuf.indexOf(">", pos);
				String tag = stbuf.substring(pos_ini_tag, pos_fin_tag + 1);
				result.hshTagsStatus.put(tag, new Tridato(
						Microfront.TAG_BANNER, "V1.0", null));
				pos = stbuf.indexOf(Microfront.TAG_BANNER, pos + tag.length());
			}

			/* ******** tags version 1.3 ******** */

			// buscar tag de noticias. v1.3
			pos = stbuf.indexOf(Microfront.TAG_GENERICO_DUMMY
					+ Microfront.RNOTICIA);
			while (pos > -1) {
				StringBuffer stbuf_tmp = new StringBuffer(stbuf.substring(0,
						pos));
				int pos_ini_tag = stbuf_tmp.lastIndexOf("<");
				int pos_fin_tag = stbuf.indexOf("</div>", pos);
				String tag = stbuf.substring(pos_ini_tag, pos_fin_tag + 6);
				String tiponoticia = this.averiguaTipo(tag);
				if (!tiponoticia.equals("-1")) {
					result.hshTagsStatus
							.put(tag, new Tridato(Microfront.TAG_GENERICO_DUMMY
									+ Microfront.RNOTICIA, "V1.3", tiponoticia));
				}
				pos = stbuf.indexOf(Microfront.TAG_GENERICO_DUMMY
						+ Microfront.RNOTICIA, pos + tag.length());
			}

			// buscar tag de agenda. v1.3
			pos = stbuf.indexOf(Microfront.TAG_GENERICO_DUMMY
					+ Microfront.RAGENDA);
			while (pos > -1) {
				StringBuffer stbuf_tmp = new StringBuffer(stbuf.substring(0,
						pos));
				int pos_ini_tag = stbuf_tmp.lastIndexOf("<");
				int pos_fin_tag = stbuf.indexOf("</div>", pos);
				String tag = stbuf.substring(pos_ini_tag, pos_fin_tag + 6);
				result.hshTagsStatus.put(tag, new Tridato(
						Microfront.TAG_GENERICO_DUMMY + Microfront.RAGENDA,
						"V1.3", null));
				pos = stbuf.indexOf(Microfront.TAG_GENERICO_DUMMY
						+ Microfront.RAGENDA, pos + tag.length());
			}

			// buscar tag de banner. v1.3
			pos = stbuf.indexOf(Microfront.TAG_GENERICO_DUMMY
					+ Microfront.RBANNER);
			while (pos > -1) {
				StringBuffer stbuf_tmp = new StringBuffer(stbuf.substring(0,
						pos));
				int pos_ini_tag = stbuf_tmp.lastIndexOf("<");
				int pos_fin_tag = stbuf.indexOf("</div>", pos);
				String tag = stbuf.substring(pos_ini_tag, pos_fin_tag + 6);
				result.hshTagsStatus.put(tag, new Tridato(
						Microfront.TAG_GENERICO_DUMMY + Microfront.RBANNER,
						"V1.3", null));
				pos = stbuf.indexOf(Microfront.TAG_GENERICO_DUMMY
						+ Microfront.RBANNER, pos + tag.length());
			}

			/* ******** tags version 1.4 ******** */
			/*
			 * Los tags de agenda y banner no han sufrido variacion desde la
			 * version anterior.
			 */

			// buscar tag de noticias. v1.4
			pos = stbuf.indexOf(Microfront.TAG_GENERICO_DUMMY
					+ Microfront.RNOTICIA);
			while (pos > -1) {
				StringBuffer stbuf_tmp = new StringBuffer(stbuf.substring(0,
						pos));
				int pos_ini_tag = stbuf_tmp.lastIndexOf("<");
				int pos_fin_tag = stbuf.indexOf("</div>", pos);
				String tag = stbuf.substring(pos_ini_tag, pos_fin_tag + 6);
				String idcomponente = this.averiguaComponente(tag);
				if (!idcomponente.equals("-1")) {
					result.hshTagsStatus
							.put(tag,
									new Tridato(Microfront.TAG_GENERICO_DUMMY
											+ Microfront.RNOTICIA, "V1.4",
											idcomponente));
				}
				pos = stbuf.indexOf(Microfront.TAG_GENERICO_DUMMY
						+ Microfront.RNOTICIA, pos + tag.length());
			}

			// buscar tag de qssi
			pos = stbuf.indexOf(Microfront.TAG_GENERICO_DUMMY
					+ Microfront.RQSSI);
			while (pos > -1) {
				StringBuffer stbuf_tmp = new StringBuffer(stbuf.substring(0,
						pos));
				int pos_ini_tag = stbuf_tmp.lastIndexOf("<");
				int pos_fin_tag = stbuf.indexOf("</div>", pos);
				String tag = stbuf.substring(pos_ini_tag, pos_fin_tag + 6);
				String idcomponente = this.averiguaComponente(tag);
				if (!idcomponente.equals("-1")) {
					result.hshTagsStatus.put(tag, new Tridato(
							Microfront.TAG_GENERICO_DUMMY + Microfront.RQSSI,
							"V1.4", idcomponente));
				}
				pos = stbuf.indexOf(Microfront.TAG_GENERICO_DUMMY
						+ Microfront.RQSSI, pos + tag.length());
			}

			/* ******** tags version 2.0 ******** */
			/*
			 * Los tags de agenda y banner no han sufrido variacion desde la
			 * version anterior.
			 */

			// buscar tag de encuestas. v2.0
			pos = stbuf.indexOf(Microfront.TAG_GENERICO_DUMMY
					+ Microfront.RENCUESTA);
			while (pos > -1) {
				StringBuffer stbuf_tmp = new StringBuffer(stbuf.substring(0,
						pos));
				int pos_ini_tag = stbuf_tmp.lastIndexOf("<");
				int pos_fin_tag = stbuf.indexOf("</div>", pos);
				String tag = stbuf.substring(pos_ini_tag, pos_fin_tag + 6);
				String idcomponente = this.averiguaComponente(tag);
				if (!idcomponente.equals("-1")) {
					result.hshTagsStatus.put(tag, new Tridato(
							Microfront.TAG_GENERICO_DUMMY
									+ Microfront.RENCUESTA, "V2.0",
							idcomponente));
				}
				pos = stbuf.indexOf(Microfront.TAG_GENERICO_DUMMY
						+ Microfront.RENCUESTA, pos + tag.length());
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
	public String doParser(Microsite site, String htmlOld, String idioma,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			MicrositeParserAnalysis analysis = this.doAnalyze(htmlOld, idioma);
			this.doCalculaTags(site, idioma, analysis, request, response);

			StringBuffer htmlParsed = new StringBuffer(htmlOld);

			if (htmlOld != null) {
				Enumeration<String> enumera = analysis.hshTags.keys();
				while (enumera.hasMoreElements()) {
					String oldstringtmp = enumera.nextElement();
					String newstringtmp = analysis.hshTags.get(oldstringtmp);
					log.debug("Parseado "
							+ (analysis.hshTagsStatus.get(oldstringtmp))
									.getKey()
							+ " "
							+ (analysis.hshTagsStatus.get(oldstringtmp))
									.getValue1());

					int pos = htmlParsed.indexOf(oldstringtmp);
					while (pos > -1) {
						htmlParsed.replace(pos, pos + oldstringtmp.length(),
								newstringtmp);
						pos = htmlParsed.toString().indexOf(oldstringtmp,
								pos + newstringtmp.length());
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
	 * Método que parsea el código html.<br/>
	 * Reemplaza los tags de los microsites por comentarios Además meterá en
	 * hshTagsStatus un listado con todos los tags a parsear encontrados y su
	 * estado. Además meterá en hshTags un listado con todos los tags a parsear
	 * encontrados y su correspondiete "trozo" de html.
	 * 
	 * @return
	 * 
	 */
	public String doParser2Comentario(Microsite site, String htmlOld,
			String idioma) {
		try {
			MicrositeParserAnalysis analysis = this.doAnalyze(htmlOld, idioma);
			this.doCalculaTagsComentario(site, idioma, analysis);

			StringBuffer htmlParsed = new StringBuffer(htmlOld);
			if (htmlOld != null) {
				Enumeration<String> enumera = analysis.hshTags.keys();
				while (enumera.hasMoreElements()) {
					String oldstringtmp = enumera.nextElement();
					String newstringtmp = analysis.hshTags.get(oldstringtmp);
					log.debug("Parseado "
							+ (analysis.hshTagsStatus.get(oldstringtmp))
									.getKey()
							+ " "
							+ (analysis.hshTagsStatus.get(oldstringtmp))
									.getValue1());

					int pos = htmlParsed.indexOf(oldstringtmp);
					while (pos > -1) {
						htmlParsed.replace(pos, pos + oldstringtmp.length(),
								newstringtmp);
						pos = htmlParsed.toString().indexOf(oldstringtmp,
								pos + newstringtmp.length());
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
	private void doCalculaTagsComentario(Microsite site, String idioma,
			MicrositeParserAnalysis analysis) {

		Enumeration<String> enumera = analysis.hshTagsStatus.keys();
		while (enumera.hasMoreElements()) {
			String key = enumera.nextElement();
			Tridato tridato = analysis.hshTagsStatus.get(key);

			if (tridato.getKey().equals(Microfront.TAG_NOTICIAS)) {
				analysis.hshTags.put(key, "<!-- " + Microfront.TAG_NOTICIAS
						+ site.getId() + idioma + site.getNumeronoticias()
						+ " -->");
			}
			if (tridato.getKey().equals(Microfront.TAG_AGENDA)) {
				analysis.hshTags.put(key, "<!-- " + Microfront.TAG_AGENDA
						+ site.getId() + idioma + site.getNumeronoticias()
						+ " -->");
			}
			if (tridato.getKey().equals(Microfront.TAG_BANNER)) {
				analysis.hshTags.put(key, "<!-- " + Microfront.TAG_BANNER
						+ site.getId() + idioma + 1 + " -->");
			}
			if (tridato.getKey().equals(
					Microfront.TAG_GENERICO_DUMMY + Microfront.RAGENDA)) {
				analysis.hshTags.put(key, "<!-- "
						+ Microfront.TAG_GENERICO_DUMMY + Microfront.RAGENDA
						+ site.getId() + idioma + site.getNumeronoticias()
						+ " -->");
			}
			if (tridato.getKey().equals(
					Microfront.TAG_GENERICO_DUMMY + Microfront.RQSSI)) {
				analysis.hshTags.put(key, "<!-- "
						+ Microfront.TAG_GENERICO_DUMMY + Microfront.RQSSI
						+ site.getId() + tridato.getValue2() + idioma + " -->");
			}
			if (tridato.getKey().equals(
					Microfront.TAG_GENERICO_DUMMY + Microfront.RBANNER)) {
				analysis.hshTags.put(key, "<!-- "
						+ Microfront.TAG_GENERICO_DUMMY + Microfront.RBANNER
						+ site.getId() + idioma + 1 + " -->");
			}
			if (tridato.getKey().equals(
					Microfront.TAG_GENERICO_DUMMY + Microfront.RNOTICIA)) {
				if (tridato.getValue1().equals("V1.3")) {
					analysis.hshTags.put(
							key,
							"<!-- " + Microfront.TAG_GENERICO_DUMMY
									+ Microfront.RNOTICIA + site.getId()
									+ tridato.getValue2() + idioma
									+ site.getNumeronoticias() + " -->");
				}
				if (tridato.getValue1().equals("V1.4")) {
					analysis.hshTags.put(key,
							"<!-- " + Microfront.TAG_GENERICO_DUMMY
									+ Microfront.RNOTICIA + site.getId()
									+ tridato.getValue2() + idioma + " -->");
				}
			}
			if (tridato.getKey().equals(
					Microfront.TAG_GENERICO_DUMMY + Microfront.RENCUESTA)) {
				if (tridato.getValue1().equals("V2.0")) {
					analysis.hshTags.put(key,
							"<!-- " + Microfront.TAG_GENERICO_DUMMY
									+ Microfront.RENCUESTA + site.getId()
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
	private void doCalculaTags(Microsite site, String idioma,
			MicrositeParserAnalysis analysis, HttpServletRequest request,
			HttpServletResponse response) throws DelegateException,
			NumberFormatException, ExceptionFrontPagina {

		MParserComponente parsecomponente = new MParserComponente();
		MParserEncuesta parseencuesta = new MParserEncuesta();
		MParserQssi parserqssi = new MParserQssi();
		Enumeration<String> enumera = analysis.hshTagsStatus.keys();
		while (enumera.hasMoreElements()) {
			String key = enumera.nextElement();
			Tridato tridato = analysis.hshTagsStatus.get(key);

			if (tridato.getKey().equals(Microfront.TAG_NOTICIAS)) {
				analysis.hshTags.put(key,
						this.getHtmlNoticias(site, idioma, request, response));
			} else if (tridato.getKey().equals(Microfront.TAG_AGENDA)
					|| tridato.getKey().equals(
							Microfront.TAG_GENERICO_DUMMY + Microfront.RAGENDA)) {
				analysis.hshTags.put(key, this.getHtmlAgendaCalendario(site,
						idioma, request, response));
			} else if (tridato.getKey().equals(
					Microfront.TAG_GENERICO_DUMMY + Microfront.RQSSI)) {
				// TODO cnavarro: falta cambiar parserqssi thymeleaf
				analysis.hshTags.put(
						key,
						parserqssi.getHtmlQssi(site.getId(),
								tridato.getValue2(), idioma).toString());
			} else if (tridato.getKey().equals(
					Microfront.TAG_GENERICO_DUMMY + Microfront.RNOTICIA)) {
				if (tridato.getValue1().equals("V1.3")) {
					analysis.hshTags.put(key, this.getHtmlElementos(site,
							Long.parseLong(tridato.getValue2()), idioma,
							request, response));
				}
				if (tridato.getValue1().equals("V1.4")) {
					/*
					 * //TODO cnavarro: falta cambiar parsecomponente thymeleaf
					 * ComponenteDelegate compodel = DelegateUtil.getComponentesDelegate(); 
					 * Componente componente = compodel.obtenerComponente( new Long(Long.parseLong(tridato.getValue2())) );
					 * analysis.hshTags.put(key, this.getHtmlElementos(site, componente.getTipo().getId(), idioma, request, response));
					 */
					analysis.hshTags.put(
							key,
							parsecomponente.getHtmlElementosComponente(
									site.getId(), tridato.getValue2(), idioma)
									.toString());
				}
			}
			if (tridato.getKey().equals(
					Microfront.TAG_GENERICO_DUMMY + Microfront.RENCUESTA)) {
				if (tridato.getValue1().equals("V2.0")) {
					// TODO cnavarro: falta cambiar parseencuesta
					analysis.hshTags.put(
							key,
							parseencuesta.getHtmlEncuesta(null, site.getId(),
									tridato.getValue2(), idioma).toString());
				}
			}

		}

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
	private String getHtmlAgendaCalendario(Microsite site, String idioma,
			HttpServletRequest request, HttpServletResponse response)
			throws DelegateException {

		Map<String, Object> model = new HashMap<String, Object>();

		model.put("MVS_datos_agenda_calendario",
				this.dataService.getDatosCalendario(site, idioma));
		model.put("MVS_microsite", site);
		model.put("MVS_idioma", new Idioma(idioma));

		return this.gusiteFlow.process(model,
				this.templateNameFactory.moduloAgenda(site), idioma, request,
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
	public String getHtmlNoticias(Microsite site, String idioma,
			HttpServletRequest request, HttpServletResponse response)
			throws DelegateException {

		Map<String, Object> model = new HashMap<String, Object>();

		model.put("MVS_home_datos_noticias_listado",
				this.dataService.getNoticiasHome(site, idioma));
		model.put("MVS_microsite", site);
		model.put("MVS_idioma", new Idioma(idioma));

		return this.gusiteFlow.process(model,
				this.templateNameFactory.moduloNoticiasHome(site), idioma,
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
	public String getHtmlElementos(Microsite site, Long tipo, String idioma,
			HttpServletRequest request, HttpServletResponse response)
			throws ExceptionFrontPagina, DelegateException {

		Map<String, Object> model = new HashMap<String, Object>();
		model.put("MVS_tipo", this.noticiasDataService.loadTipo(tipo, idioma));
		model.put("MVS_listado_elementos", this.noticiasDataService
				.listarNoticiasTipo(site, idioma, tipo,
						site.getNumeronoticias()));
		model.put("MVS_microsite", site);
		model.put("MVS_idioma", new Idioma(idioma));

		return this.gusiteFlow.process(model,
				this.templateNameFactory.componenteElementos(site), idioma,
				request, response);

	}

}
