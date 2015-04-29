package es.caib.gusite.front.microtag;

import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
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
					result.hshTagsStatus.put(tag, new Tridato(Microfront.TAG_GENERICO_DUMMY + Microfront.RNOTICIA, "V1.3", tiponoticia));
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
				result.hshTagsStatus.put(tag, new Tridato(Microfront.TAG_GENERICO_DUMMY + Microfront.RAGENDA, "V1.3", null));
				pos = stbuf.indexOf(Microfront.TAG_GENERICO_DUMMY + Microfront.RAGENDA, pos + tag.length());
			}

			// buscar tag de banner. v1.3
			pos = stbuf.indexOf(Microfront.TAG_GENERICO_DUMMY + Microfront.RBANNER);
			while (pos > -1) {
				StringBuffer stbuf_tmp = new StringBuffer(stbuf.substring(0, pos));
				int pos_ini_tag = stbuf_tmp.lastIndexOf("<");
				int pos_fin_tag = stbuf.indexOf("</div>", pos);
				String tag = stbuf.substring(pos_ini_tag, pos_fin_tag + 6);
				result.hshTagsStatus.put(tag, new Tridato(Microfront.TAG_GENERICO_DUMMY + Microfront.RBANNER, "V1.3", null));
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
					result.hshTagsStatus.put(tag, new Tridato(Microfront.TAG_GENERICO_DUMMY + Microfront.RNOTICIA, "V1.4", idcomponente));
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
					result.hshTagsStatus.put(tag, new Tridato(Microfront.TAG_GENERICO_DUMMY + Microfront.RQSSI, "V1.4", idcomponente));
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
					result.hshTagsStatus.put(tag, new Tridato(Microfront.TAG_GENERICO_DUMMY + Microfront.RENCUESTA, "V2.0", idcomponente));
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
	public String doParser(Microsite site, String htmlOld, String idioma, HttpServletRequest request, HttpServletResponse response) {
		try {
			MicrositeParserAnalysis analysis = this.doAnalyze(htmlOld, idioma);
			this.doCalculaTags(site, idioma, analysis, request, response);

			StringBuffer htmlParsed = new StringBuffer(htmlOld);

			if (htmlOld != null) {
				Enumeration<String> enumera = analysis.hshTags.keys();
				while (enumera.hasMoreElements()) {
					String oldstringtmp = enumera.nextElement();
					String newstringtmp = analysis.hshTags.get(oldstringtmp);
					log.debug("Parseado " + (analysis.hshTagsStatus.get(oldstringtmp)).getKey() + " " + (analysis.hshTagsStatus.get(oldstringtmp)).getValue1());

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
				analysis.hshTags.put(key, "<!-- " + Microfront.TAG_NOTICIAS + site.getId() + idioma + site.getNumeronoticias() + " -->");
			}
			if (tridato.getKey().equals(Microfront.TAG_AGENDA)) {
				analysis.hshTags.put(key, "<!-- " + Microfront.TAG_AGENDA + site.getId() + idioma + site.getNumeronoticias() + " -->");
			}
			if (tridato.getKey().equals(Microfront.TAG_BANNER)) {
				analysis.hshTags.put(key, "<!-- " + Microfront.TAG_BANNER + site.getId() + idioma + 1 + " -->");
			}
			if (tridato.getKey().equals(Microfront.TAG_GENERICO_DUMMY + Microfront.RAGENDA)) {
				analysis.hshTags.put(key,
						"<!-- " + Microfront.TAG_GENERICO_DUMMY + Microfront.RAGENDA + site.getId() + idioma + site.getNumeronoticias() + " -->");
			}
			if (tridato.getKey().equals(Microfront.TAG_GENERICO_DUMMY + Microfront.RQSSI)) {
				analysis.hshTags.put(key, "<!-- " + Microfront.TAG_GENERICO_DUMMY + Microfront.RQSSI + site.getId() + tridato.getValue2() + idioma
						+ " -->");
			}
			if (tridato.getKey().equals(Microfront.TAG_GENERICO_DUMMY + Microfront.RBANNER)) {
				analysis.hshTags.put(key, "<!-- " + Microfront.TAG_GENERICO_DUMMY + Microfront.RBANNER + site.getId() + idioma + 1 + " -->");
			}
			if (tridato.getKey().equals(Microfront.TAG_GENERICO_DUMMY + Microfront.RNOTICIA)) {
				if (tridato.getValue1().equals("V1.3")) {
					analysis.hshTags.put(key, "<!-- " + Microfront.TAG_GENERICO_DUMMY + Microfront.RNOTICIA + site.getId() + tridato.getValue2()
							+ idioma + site.getNumeronoticias() + " -->");
				}
				if (tridato.getValue1().equals("V1.4")) {
					analysis.hshTags.put(key, "<!-- " + Microfront.TAG_GENERICO_DUMMY + Microfront.RNOTICIA + site.getId() + tridato.getValue2()
							+ idioma + " -->");
				}
			}
			if (tridato.getKey().equals(Microfront.TAG_GENERICO_DUMMY + Microfront.RENCUESTA)) {
				if (tridato.getValue1().equals("V2.0")) {
					analysis.hshTags.put(key, "<!-- " + Microfront.TAG_GENERICO_DUMMY + Microfront.RENCUESTA + site.getId() + tridato.getValue2()
							+ idioma + " -->");
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
	private void doCalculaTags(Microsite site, String idioma, MicrositeParserAnalysis analysis, HttpServletRequest request,
			HttpServletResponse response) throws DelegateException, NumberFormatException, ExceptionFrontPagina {

		Enumeration<String> enumera = analysis.hshTagsStatus.keys();
		while (enumera.hasMoreElements()) {
			String key = enumera.nextElement();
			Tridato tridato = analysis.hshTagsStatus.get(key);

			if (tridato.getKey().equals(Microfront.TAG_NOTICIAS)) {
				analysis.hshTags.put(key, this.getHtmlNoticias(site, idioma, request, response));
			} else if (tridato.getKey().equals(Microfront.TAG_AGENDA) || tridato.getKey().equals(Microfront.TAG_GENERICO_DUMMY + Microfront.RAGENDA)) {
				analysis.hshTags.put(key, this.getHtmlAgendaCalendario(site, idioma, request, response));
			} else if (tridato.getKey().equals(Microfront.TAG_GENERICO_DUMMY + Microfront.RQSSI)) {
				analysis.hshTags.put(key, this.getHtmlQssi(site, tridato.getValue2(), idioma));
			} else if (tridato.getKey().equals(Microfront.TAG_GENERICO_DUMMY + Microfront.RNOTICIA)) {
				if (tridato.getValue1().equals("V1.3")) {
					analysis.hshTags.put(key, this.getHtmlElementos(site, Long.parseLong(tridato.getValue2()), idioma, request, response));
				}
				if (tridato.getValue1().equals("V1.4")) {
					analysis.hshTags.put(key, this.getHtmlElementosComponente(site, Long.parseLong(tridato.getValue2()), idioma, request, response));
				}
			}
			if (tridato.getKey().equals(Microfront.TAG_GENERICO_DUMMY + Microfront.RENCUESTA)) {
				if (tridato.getValue1().equals("V2.0")) {
					analysis.hshTags.put(key, this.getHtmlEncuesta(site, Long.parseLong(tridato.getValue2()), idioma, request, response));
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
	public String getHtmlEncuesta(Microsite site, Long idEncuesta, String idioma, HttpServletRequest request, HttpServletResponse response) throws ExceptionFrontPagina {

		Encuesta encuesta = this.encuestasDataService.getEncuesta(site, idioma, idEncuesta);
		ResourceBundle rb = ResourceBundle.getBundle("ApplicationResources_front", new Locale(idioma.toUpperCase(), idioma.toUpperCase()));

		EncuestaView view = new EncuestaView();
		
		view.setMicrosite(site);
		view.setIdioma(idioma);
		view.setEncuesta(encuesta);
		view.setFechasVigente(Fechas.vigente(encuesta.getFpublicacion(), encuesta.getFcaducidad()));
		
		// Identificacion del usuario que contesta segun configuracion de la encuesta
		if (encuesta.getIdentificacion().equals("S")) {
			try {
				ClientPrincipal principal = null;
				principal = ClientPrincipal.getCurrent();
				// principal = (CertsPrincipal) request.getUserPrincipal();
				String identificacio = rb.getString("encuesta.identificacion").replaceAll("\\{1\\}", principal.getFullName());
				view.setIdentificacion(identificacio);
			} catch (NamingException e) {
				log.error("Error en la identificación del usuario en la encuesta: " + idEncuesta + " ---> " + e);
				throw new ExceptionFrontPagina("Error en la identificación del usuario");				
			}
		}
		
		

		return this.templateProcessor.process(LayoutView.modelForView(view), this.templateNameFactory.componenteElementos(site), idioma, request, response);

		
		/*
		// u91856 29/06/2011 Respostes que venen donades fixes
		Map param = (request == null) ? null : (HashMap) request.getSession().getAttribute(Microfront.ENCPARAM);
		if (param == null) {
			param = new HashMap();
		} else {
			request.getSession().removeAttribute(Microfront.ENCPARAM);
		}

		StringBuffer retorno = new StringBuffer();
		ResourceBundle rb = ResourceBundle.getBundle("ApplicationResources_front", new Locale(idioma.toUpperCase(), idioma.toUpperCase()));

		String obligatoriedad = "";

		// u91856 02/03/2012 Salvador Antich: Identificacion del usuario que
		// contesta segun configuracion de la encuesta
		if (encuesta.getIdentificacion().equals("S")) {
				CertsPrincipal principal = null;
				try {
					principal = CertsPrincipal.getCurrent();
				} catch (NamingException e) {
					log.error("Error en la identificación del usuario en la encuesta: " + idEncuesta + " ---> " + e);
					throw new ExceptionFrontPagina("Error en la identificación del usuario");
				}
				// principal = (CertsPrincipal) request.getUserPrincipal();
				String identificacio = rb.getString("encuesta.identificacion").replaceAll("\\{1\\}", principal.getFullName());
				retorno.append(identificacio);
		} else {
			// retorno.append(rb.getString("encuesta.anonima"));
		}
		retorno.append("<br><br>");

		if ((encuesta.getVisible().equals("S")) && (Fechas.vigente(encuesta.getFpublicacion(), encuesta.getFcaducidad()))) {

			retorno.append("<form name=\"encuesta\" action=\"envioencuesta.do\" method=\"post\" id=\"encuesta\">\n");
			retorno.append("<input type=\"hidden\" name=\"idsite\" value=\"" + site.getId() + "\">\n");
			retorno.append("<input type=\"hidden\" name=\"lang\" value=\"" + idioma + "\">\n");
			retorno.append("<input type=\"hidden\" name=\"cont\" value=\"" + idEncuesta + "\">\n");
			retorno.append("<input type=\"hidden\" name=\"enccomp\" value=\"yes\">\n");

			StringBuffer scriptValidar = new StringBuffer();
			scriptValidar.append("<script>\n function comptaChecks(pregunta){ \n");
			scriptValidar.append("	 var num=0; \n");
			scriptValidar.append("	 for (i=0; i<eval('document.encuesta.' + pregunta + '.length'); i++){ \n");
			scriptValidar.append("	 	if (eval('document.encuesta.' + pregunta + '[i].checked')==true) \n");
			scriptValidar.append("	 		num++; \n");
			scriptValidar.append("	 } \n");
			scriptValidar.append("	 return num;  \n } \n \n");

			scriptValidar.append("function marcaCheck(idResp, idChk){ \n");
			scriptValidar.append("	 if (eval('document.encuesta.T' + idResp + '_' + idChk).value.length==0) { \n");
			scriptValidar.append("	 	document.getElementById(idResp + '_' + idChk).checked = false;\n");
			scriptValidar.append("	 }else{ \n");
			scriptValidar.append("	 	document.getElementById(idResp + '_' + idChk).checked = true;\n");
			scriptValidar.append("   } \n} \n \n");

			scriptValidar.append("function getChecked(radioObj) {\n");
			scriptValidar.append("	 if(!radioObj) return false;\n");
			scriptValidar.append("	 var radioLength = radioObj.length;\n");
			scriptValidar.append("	 if(radioLength == undefined){\n");
			scriptValidar.append("   	if(radioObj.checked) return true;\n");
			scriptValidar.append("		else false; }\n");
			scriptValidar.append("	 for(var i = 0; i < radioLength; i++) {\n");
			scriptValidar.append("		if(radioObj[i].checked) return true;\n");
			scriptValidar.append("	 }\n");
			scriptValidar.append("	 return false;\n");
			scriptValidar.append("} \n \n");

			scriptValidar.append("function validaencuesta(){\n var txtError = \"\"; \n");

			Iterator iter = encuesta.getPreguntas().iterator();
			while (iter.hasNext()) { // Preguntas de una encuesta

				Pregunta pregunta = (Pregunta) iter.next();
				pregunta.setIdi(idioma);

				obligatoriedad = "";
				if ((pregunta.getVisible().equals("S")) && (pregunta.getVisiblecmp().equals("S"))) {
					Integer max = (pregunta.getMaxContestadas() != null) ? pregunta.getMaxContestadas() : new Integer(0);
					Integer min = (pregunta.getMinContestadas() != null) ? pregunta.getMinContestadas() : new Integer(0);
					if ("S".equals(pregunta.getMultiresp())) {
						if (min != 0 && max != 0) {
							// obligatoriedad = new String
							// (rb.getSting("clau").getBytes("ISO-8859-1"),"UTF-8");
							obligatoriedad = rb.getString("encuesta.respcont.minmax");
							obligatoriedad = obligatoriedad.replaceAll("\\{1\\}", min.toString()).replaceAll("\\{2\\}", max.toString());

							scriptValidar.append("if (comptaChecks(\"C" + pregunta.getId() + "\") > " + max + " || comptaChecks(\"C"
									+ pregunta.getId() + "\") < " + min + ") ");
							scriptValidar.append(" txtError = txtError + \"\\n"
									+ ((pregunta.getTraduce() != null) ? ((TraduccionPregunta) pregunta.getTraduce()).getTitulo()
											: "Error en traduccion" + "\"") + "\";\n");
						} else if (min != 0) {
							obligatoriedad = rb.getString("encuesta.respcont.min");
							obligatoriedad = obligatoriedad.replaceAll("\\{1\\}", min.toString());

							scriptValidar.append("if (comptaChecks(\"C" + pregunta.getId() + "\") < " + min + ") ");
							scriptValidar.append(" txtError = txtError + \"\\n"
									+ ((pregunta.getTraduce() != null) ? ((TraduccionPregunta) pregunta.getTraduce()).getTitulo()
											: "Error en traduccion" + "\"") + "\";\n");
						} else if (max != 0) {
							obligatoriedad = rb.getString("encuesta.respcont.max");
							obligatoriedad = obligatoriedad.replaceAll("\\{1\\}", max.toString());

							scriptValidar.append("if (comptaChecks(\"C" + pregunta.getId() + "\") > " + max + ") ");
							scriptValidar.append(" txtError = txtError + \"\\n"
									+ ((pregunta.getTraduce() != null) ? ((TraduccionPregunta) pregunta.getTraduce()).getTitulo()
											: "Error en traduccion" + "\"") + "\";\n");
						}
					} else { // Resposta simple
						if (min == 1) {
							obligatoriedad = rb.getString("encuesta.obligatoria");

							scriptValidar.append("	if (!getChecked(document.encuesta.R" + pregunta.getId() + ")) \n");
							scriptValidar.append(" txtError = txtError + \"\\n"
									+ ((pregunta.getTraduce() != null) ? ((TraduccionPregunta) pregunta.getTraduce()).getTitulo()
											: "Error en traduccion" + "\"") + "\";\n");
						}
					}
					retorno.append("<div id=\"enquestaPreguntaCom\">\n");
					retorno.append("<h3>"
							+ ((pregunta.getTraduce() != null) ? ((TraduccionPregunta) pregunta.getTraduce()).getTitulo() + " " + obligatoriedad
									: "&nbsp;") + "</h3>\n");

					if (pregunta.getImagen() != null) {
						retorno.append("<p><img src=\"archivopub.do?ctrl=MCRST" + site.getId() + "ZI" + pregunta.getImagen().getId() + "&id="
								+ pregunta.getImagen().getId() + "\" alt=\"\" /></p>\n");
					}

					retorno.append("<ul>\n");

					String scriptRadioDisabled = "";
					String scriptTxtDisabled = "";
					String checked = "";
					String disabledTxt = "";
					String disabledChk = "";
					String disabledRadio = "";

					Iterator iter2 = pregunta.getRespuestas().iterator();
					while (iter2.hasNext()) {
						checked = "";

						disabledTxt = "";
						disabledChk = "";

						Respuesta respuesta = (Respuesta) iter2.next();
						respuesta.setIdi(idioma);
						retorno.append("<li>\n");
						retorno.append("<label>\n");

						if (param.containsKey(respuesta.getId())) {
							checked = " checked='checked' ";
							disabledTxt = " readonly ";
							disabledChk = " disabled ";
							disabledRadio = " disabled ";
						}

						String compId = respuesta.getId() + "_" + pregunta.getId();

						if (pregunta.getMultiresp().equals("S")) {
							if (respuesta.getTipo().equals("I")) {
								disabledChk = " disabled ";
							}

							retorno.append("<input type=\"checkbox\" id=\"" + compId + "\" name=\"C" + pregunta.getId() + "\" value=\""
									+ respuesta.getId() + "\"" + disabledChk + checked + ">");
						} else {
							scriptRadioDisabled += "document.getElementById(\"" + compId + "\").disabled=true;\n";

							retorno.append("<input type=\"radio\"id=\"" + compId + "\"name=\"R" + pregunta.getId() + "\" value=\""
									+ respuesta.getId() + "\"" + checked + ">");
						}

						retorno.append(((respuesta.getTraduce() != null) ? ((TraduccionRespuesta) respuesta.getTraduce()).getTitulo() : "&nbsp;"));
						retorno.append("</label>\n");

						if (respuesta.getTipo().equals("I")) {
							scriptTxtDisabled += "document.encuesta.T" + respuesta.getId() + "_" + pregunta.getId() + ".readonly=true;\n";
							// retorno.append("<input type=\"text\" name=\"T"
							// + respuesta.getId() + "_" + pregunta.getId()
							// + "\"" + readonly + ">");
							String onKeyUp = "onKeyUp=\"marcaCheck('" + respuesta.getId() + "','" + pregunta.getId() + "');\"";
							retorno.append("<br><TEXTAREA COLS=\"120\" ROWS=\"2\" " + onKeyUp + " name=\"T" + respuesta.getId() + "_"
									+ pregunta.getId() + "\"" + disabledTxt + "></TEXTAREA>");
							if (param.get(respuesta.getId()) != null) {
								retorno.append("<script>document.getElementById(\"encuesta\").T" + respuesta.getId() + "_" + pregunta.getId()
										+ ".value='" + (String) param.get(respuesta.getId()) + "';</script>");
							}
						}
						retorno.append("</li>\n");
					}
					if (!"".equals(disabledRadio)) {
						retorno.append("<script>\n" + scriptRadioDisabled + "\n" + scriptTxtDisabled + "</script>\n\n");
					}

					retorno.append("</ul>\n");
					retorno.append("</div>\n");
				}
			}
			scriptValidar.append("; \n if (txtError == \"\") { \n document.encuesta.submit(); \n } else { \n alert(\""
					+ rb.getString("encuesta.condiciones") + "\" + txtError); } }\n</script>\n");

			retorno.append("<div id=\"botoneraCom\"><label><input type=\"button\" onClick = \"validaencuesta();\" name=\"btnanar\" value=\""
					+ rb.getString("encuesta.enviar") + "\" /></label></div>\n");
			retorno.append("</form>\n");
			if (encuesta.getMostrar().equalsIgnoreCase("S")) {
				retorno.append("<p style=\"text-align:center;\"><a href=\"envioencuesta.do?" + Microfront.PIDSITE + "=" + site.getId() + "&"
						+ Microfront.PCONT + "=" + idEncuesta + "&" + Microfront.PLANG + "=" + idioma + "&" + Microfront.PVIEW + "=yes\">"
						+ rb.getString("encuesta.verresultados") + "</a></p>\n");
			}

			retorno.append(scriptValidar);
		} else {
			// encuesta no visible o caducada
			retorno.append("<div id=\"enquestaPreguntaCom\">\n");
			retorno.append("<h3>" + ((encuesta.getTraduce() != null) ? ((TraduccionEncuesta) encuesta.getTraduce()).getTitulo() : "&nbsp;")
					+ "</h3>\n");
			retorno.append("<p style=\"text-align:center;\">" + rb.getString("encuesta.caducada") + "</p>\n");
			retorno.append("<p style=\"text-align:center;\"><a href=\"envioencuesta.do?" + Microfront.PIDSITE + "=" + site.getId() + "&"
					+ Microfront.PCONT + "=" + idEncuesta + "&" + Microfront.PLANG + "=" + idioma + "&" + Microfront.PVIEW + "=yes\">"
					+ rb.getString("encuesta.verresultados") + "</a></p>\n");

		}

		return retorno.toString();
		*/
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
	public String getHtmlElementosComponente(Microsite site, long id, String idioma, HttpServletRequest request, HttpServletResponse response) throws DelegateException {

		ComponenteDelegate compodel = DelegateUtil.getComponentesDelegate();
		Componente componente = compodel.obtenerComponente(id);

		if (componente.getTipo().getTipoelemento().equals(Microfront.ELEM_NOTICIA)) {
			// si es de clase noticia.
			return this.getHtmlComponenteTNoticia(site, componente, idioma);
		} else if (componente.getTipo().getTipoelemento().equals(Microfront.ELEM_LINK)) {
			// si es de clase enlace.
			return this.getHtmlComponenteTEnlace(site, componente, idioma);
		} else if (componente.getTipo().getTipoelemento().equals(Microfront.ELEM_DOCUMENTO)) {
			// si es de clase documento
			return this.getHtmlComponenteTDocumento(site, componente, idioma);
		} else if (componente.getTipo().getTipoelemento().equals(Microfront.ELEM_CONEXIO_EXTERNA)) {
			// si es de clase documento
			return  this.getHtmlComponenteTExterno(site, componente, idioma);
		}
		
		//TODO: error?
		return "";
	}
	
	
	/**
	 * Método privado que devuelve un string para insertar en un HTML un
	 * documento externo
	 * 
	 * @param idmicrosite
	 * @param componente
	 * @param idioma
	 * @return StringBuffer Código HTML
	 * @throws DelegateException 
	 */
	private String getHtmlComponenteTExterno(Microsite site, Componente componente, String idioma) throws DelegateException {

		String nombreformulario = "formnoticiasexternocomp" + componente.getId().longValue();
		String nombre_elemento = ((TraduccionComponente) componente.getTraduccion(idioma)) != null ? ((TraduccionComponente) componente.getTraduccion(idioma)).getTitulo() : "[ empty ]";

		
		TipoDelegate tD = DelegateUtil.getTipoDelegate();

		// preparamos el map a enviar
		Map<String, String> filtrado = new HashMap<String, String>();
		filtrado.put("lang", idioma);
		filtrado.put("nameform", nombreformulario);
		filtrado.put("idcomponente", "" + componente.getId().longValue());

		String resultadoHTML = tD.obtenerPegoteHTMLExterno(componente.getTipo().getId(), filtrado);

		//////////// -TODO: convertir en fragmento thymeleaf

		StringBuffer retorno = new StringBuffer();
		// al div le ponemos el id de la clase que acabamos de configurar
		retorno.append(this.configStyle3(site, componente));
		retorno.append("<div id=\"element").append(componente.getId()).append("\">");
		retorno.append("<h2>").append(nombre_elemento).append("</h2>");
		retorno.append("<form action=\"noticias.do\" name=\"").append(nombreformulario).append("\" id=\"").append(nombreformulario)
				.append("\" >");

		retorno.append("<input type=\"hidden\" name=\"lang\" value=\"").append(idioma).append("\">");
		retorno.append("<input type=\"hidden\" name=\"nameform\" value=\"").append(nombreformulario).append("\">");


		// retorno.append("<style>").append(resultado.get(Microfront.MAP_EXT_MCSTYLE)).append("</style>");
		retorno.append(resultadoHTML);
		// retorno.append("<script>").append(resultado.get(Microfront.MAP_EXT_MCJAVASCRIPT)).append("</script>");

		retorno.append("</form></div>");


		return retorno.toString();
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
	private String getHtmlComponenteTNoticia(Microsite site, Componente componente, String idioma) throws DelegateException {

		Collection<Noticia> listanoticias = this.getListaNoticias(site, componente, idioma);
		String nombre_elemento = ((TraduccionComponente) componente.getTraduccion(idioma)) != null ? ((TraduccionComponente) componente.getTraduccion(idioma)).getTitulo() : "[ empty ]";

		
		//////////// -TODO: convertir en fragmento thymeleaf

		StringBuffer retorno = new StringBuffer();
		// al div le ponemos el id de la clase que acabamos de configurar
		retorno.append(this.configStyle3(site, componente));
		retorno.append("<div id=\"element" + componente.getId() + "\">");
		retorno.append("<h2>" + nombre_elemento + "</h2>");

		
		ResourceBundle rb = ResourceBundle.getBundle("ApplicationResources_front", new Locale(idioma.toUpperCase(), idioma.toUpperCase()));

		if (listanoticias.size() != 0) {

			retorno.append("<table width=\"100%\" border=\"0\" cellPadding=\"0\" cellSpacing=\"0\" id=\"element" + componente.getId() + "\">");
			Iterator<?> iter = listanoticias.iterator();
			int cont = 0;
			// Si SoloImagen, es mostra en forma de mosaic a dues columnes
			String nCols = (componente.getSoloimagen().equals("S")) ? "" : "<tr>";
			while (iter.hasNext()) {
				java.text.SimpleDateFormat dia = new java.text.SimpleDateFormat("dd/MM/yyyy");
				Noticia noti = (Noticia) iter.next();

				retorno.append(((cont % 2) == 0) ? "<tr class=\"par\">" : nCols);
				if (componente.getSoloimagen().equals("S")) {
					// en el caso de que sólo imágenes
					retorno.append("<td style=\"width:1%\" >");
					retorno.append("<a href=\"" + MicroURI.uriNoticia(site.getId(), noti.getId(), idioma) + "\">");
					if (noti.getImagen() != null) {
						retorno.append("<img src=\""
								+ MicroURI.uriImg(Microfront.RNOTICIA, noti.getId().longValue(), noti.getImagen().getId().longValue())
								+ "\" alt=\"\" class=\"imagen\" />");
					} else {
						if (componente.getImagenbul() != null) {
							retorno.append("<img src=\""
									+ MicroURI.uriImg(Microfront.RMICROSITE, site.getId().longValue(), componente.getImagenbul().getId()
											.longValue()) + "\" alt=\"" + ((TraduccionNoticia) noti.getTraduccion(idioma)).getTitulo()
									+ "\" class=\"imagen\" />&nbsp;");
						} else {
							retorno.append("<img src=\"imgs/listados/bullet_gris.gif\" alt=\"\" /> &nbsp;");
						}
						retorno.append(((TraduccionNoticia) noti.getTraduccion(idioma)).getTitulo());
					}
					retorno.append("</a>");
					retorno.append("</td><td>&nbsp;</td> \n");

				} else {
					retorno.append("<td style=\"width:1%\" >");
					if (noti.getImagen() != null) {
						retorno.append("<img src=\""
								+ MicroURI.uriImg(Microfront.RNOTICIA, noti.getId().longValue(), noti.getImagen().getId().longValue())
								+ "\" width=\"48\" height=\"48\" alt=\"\" class=\"imagen\" width=\"266\" height=\"127\" />&nbsp;");
					} else {
						if (componente.getImagenbul() != null) {
							retorno.append("<img src=\""
									+ MicroURI.uriImg(Microfront.RMICROSITE, site.getId().longValue(), componente.getImagenbul().getId()
											.longValue()) + "\" alt=\"" + ((TraduccionNoticia) noti.getTraduccion(idioma)).getTitulo()
									+ "\" class=\"imagen\" />&nbsp;");
						} else {
							retorno.append("<img src=\"imgs/listados/bullet_gris.gif\" alt=\"\" /> &nbsp;");
						}
					}
					retorno.append(noti.getFpublicacion() != null ? dia.format(noti.getFpublicacion()) : "");
					retorno.append("&nbsp;<a href=\"" + MicroURI.uriNoticia(site.getId(), noti.getId(), idioma) + "\">");
					retorno.append(((TraduccionNoticia) noti.getTraduccion(idioma)).getTitulo() + "</a>");
					String subTitol = ((TraduccionNoticia) noti.getTraduccion(idioma)).getSubtitulo();
					retorno.append("<br/>" + (subTitol == null ? "" : subTitol) + "<br/>");

					retorno.append("</td> \n");
				}
				retorno.append(((cont % 2) == 0) ? nCols : "</tr> \n");
				cont++;
			}
			retorno.append("</table> \n");

		} else {
			retorno.append("<p>" + rb.getString("microhtml.nonoticias") + "</p>");
		}
		retorno.append("</div>");
		if (site.getId().longValue() == 0) {
			retorno.append("");
		}

		return retorno.toString();
	}

	private Collection<Noticia> getListaNoticias(Microsite site, Componente componente, String idioma) throws DelegateException {
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
	private String getHtmlComponenteTEnlace(Microsite site, Componente componente, String idioma) throws DelegateException {

		Collection<Noticia> listanoticias = this.getListaNoticias(site, componente, idioma);
		String nombre_elemento = ((TraduccionComponente) componente.getTraduccion(idioma)) != null ? ((TraduccionComponente) componente
				.getTraduccion(idioma)).getTitulo() : "[ empty ]";

		//////////// -TODO: convertir en fragmento thymeleaf

		StringBuffer retorno = new StringBuffer();
		ResourceBundle rb = ResourceBundle.getBundle("ApplicationResources_front", new Locale(idioma.toUpperCase(), idioma.toUpperCase()));
		// al div le ponemos el id de la clase que acabamos de configurar
		retorno.append(this.configStyle3(site, componente));
		retorno.append("<div id=\"element" + componente.getId() + "\">");
		retorno.append("<h2>" + nombre_elemento + "</h2>");
		if (listanoticias.size() != 0) {

			retorno.append("<table width=\"100%\" border=\"0\" cellPadding=\"0\" cellSpacing=\"0\" id=\"element" + componente.getId() + "\">");
			Iterator<?> iter = listanoticias.iterator();
			int cont = 0;
			// Si SoloImagen, es mostra en forma de mosaic a dues columnes
			String nCols = (componente.getSoloimagen().equals("S")) ? "" : "<tr>";
			while (iter.hasNext()) {
				Noticia noti = (Noticia) iter.next();
				retorno.append(((cont % 2) == 0) ? "<tr class=\"par\">" : nCols);
				if (componente.getSoloimagen().equals("S") && (noti.getImagen() != null)) {
					// en el caso de que sólo imágenes
					retorno.append("<td style=\"width:1%\" >");
					retorno.append("<a href=\"" + MicroURI.uriNoticia(site.getId(), noti.getId(), idioma) + "\">");
					if (noti.getImagen() != null) {
						retorno.append("<img src=\""
								+ MicroURI.uriImg(Microfront.RNOTICIA, noti.getId().longValue(), noti.getImagen().getId().longValue())
								+ "\" alt=\"" + ((TraduccionNoticia) noti.getTraduccion(idioma)).getTitulo() + "\" class=\"imagen\" />");
					} else {
						retorno.append(((TraduccionNoticia) noti.getTraduccion(idioma)).getTitulo());
					}
					retorno.append("</a>");
					retorno.append("</td><td>&nbsp;</td> \n");

				} else {
					retorno.append("<td style=\"width:1%\" >");
					if (componente.getImagenbul() != null) {
						retorno.append("<img src=\""
								+ MicroURI.uriImg(Microfront.RMICROSITE, site.getId().longValue(), componente.getImagenbul().getId().longValue())
								+ "\" alt=\"\" class=\"imagen\" />");
					} else {
						retorno.append("<img src=\"imgs/listados/bullet_gris.gif\" alt=\"\" />");
					}

					boolean urlExt = ((TraduccionNoticia) noti.getTraduccion(idioma)).getLaurl().startsWith("http:");
					String target = (urlExt) ? "_blank" : "_self";
					retorno.append("<a href=\"" + ((TraduccionNoticia) noti.getTraduccion(idioma)).getLaurl() + "\" target=\"" + target
							+ "\">&nbsp;" + ((TraduccionNoticia) noti.getTraduccion(idioma)).getTitulo() + "</a>");

					if (((TraduccionNoticia) noti.getTraduccion(idioma)).getSubtitulo() != null) {
						retorno.append("<br/>" + ((TraduccionNoticia) noti.getTraduccion(idioma)).getSubtitulo());
					}
					if (((TraduccionNoticia) noti.getTraduccion(idioma)).getTexto() != null) {
						retorno.append("<br/>" + ((TraduccionNoticia) noti.getTraduccion(idioma)).getTexto());
					}
					retorno.append("</td> \n");
				}
				retorno.append(((cont % 2) == 0) ? nCols : "</tr> \n");
				cont++;
			}
			retorno.append("</table> \n");

		} else {
			retorno.append("<p>" + rb.getString("microhtml.nonoticias") + "</p>");
		}
		retorno.append("</div>");

		return retorno.toString();
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
	private String getHtmlComponenteTDocumento(Microsite site, Componente componente, String idioma) throws DelegateException {
		StringBuffer retorno = new StringBuffer();

		Collection<Noticia> listanoticias = this.getListaNoticias(site, componente, idioma);
		String nombre_elemento = ((TraduccionComponente) componente.getTraduccion(idioma)) != null ? ((TraduccionComponente) componente.getTraduccion(idioma)).getTitulo() : "[ empty ]";

		//////////// -TODO: convertir en fragmento thymeleaf
		
		ResourceBundle rb = ResourceBundle.getBundle("ApplicationResources_front", new Locale(idioma.toUpperCase(), idioma.toUpperCase()));

		// al div le ponemos el id de la clase que acabamos de configurar
		retorno.append(this.configStyle3(site, componente));
		retorno.append("<div id=\"element" + componente.getId() + "\">");
		retorno.append("<h2>" + nombre_elemento + "</h2>");

		if (listanoticias.size() != 0) {

			retorno.append("<table width=\"100%\" border=\"0\" cellPadding=\"0\" cellSpacing=\"0\" id=\"element" + componente.getId() + "\">");
			Iterator<?> iter = listanoticias.iterator();
			int cont = 0;
			// Si SoloImagen, es mostra en forma de mosaic a dues columnes
			String nCols = (componente.getSoloimagen().equals("S")) ? "" : "<tr>";
			while (iter.hasNext()) {

				Noticia noti = (Noticia) iter.next();
				retorno.append(((cont % 2) == 0) ? "<tr class=\"par\">" : nCols);
				if (componente.getSoloimagen().equals("S") && (noti.getImagen() != null)) {
					// en el caso de que sólo imágenes
					retorno.append("<td style=\"width:1%\" >");
					retorno.append("<a href=\"" + MicroURI.uriNoticia(site.getId(), noti.getId(), idioma) + "\">");
					if (noti.getImagen() != null) {
						retorno.append("<img src=\""
								+ MicroURI.uriImg(Microfront.RNOTICIA, noti.getId().longValue(), noti.getImagen().getId().longValue())
								+ "\" alt=\"" + ((TraduccionNoticia) noti.getTraduccion(idioma)).getTitulo() + "\" class=\"imagen\" />");
					} else {
						retorno.append(((TraduccionNoticia) noti.getTraduccion(idioma)).getTitulo());
					}
					retorno.append("</a>");
					retorno.append("</td><td>&nbsp;</td> \n");

				} else {
					retorno.append("<td style=\"width:1%\" >");
					if (componente.getImagenbul() != null) {
						retorno.append("<img src=\""
								+ MicroURI.uriImg(Microfront.RMICROSITE, site.getId().longValue(), componente.getImagenbul().getId().longValue())
								+ "\" alt=\"\" class=\"imagen\" />");
					} else {
						retorno.append("<img src=\"imgs/listados/bullet_gris.gif\" alt=\"\" />");
					}
					retorno.append("<a href=\"elementodocumento.do?idsite=" + site.getId() + "&cont=" + noti.getId() + "&lang=" + idioma
							+ "\" target=\"_blank\">&nbsp;" + ((TraduccionNoticia) noti.getTraduccion(idioma)).getTitulo() + "</a>");
					if (((TraduccionNoticia) noti.getTraduccion(idioma)).getTexto() != null) {
						retorno.append("<br/>" + ((TraduccionNoticia) noti.getTraduccion(idioma)).getTexto());
					}
					retorno.append("</td> \n");
				}
				retorno.append(((cont % 2) == 0) ? nCols : "</tr> \n");
				cont++;
			}
			retorno.append("</table> \n");

		} else {
			retorno.append("<p>" + rb.getString("microhtml.nonoticias") + "</p>");
		}
		retorno.append("</div>");

		return retorno.toString();
	}
	
	
	private String getHtmlQssi(Microsite site, String ideqssi, String idioma) throws ExceptionFrontPagina {

		Long id = new Long(Long.parseLong(ideqssi));
		ResourceBundle.getBundle("ApplicationResources_front", new Locale(idioma.toUpperCase(), idioma.toUpperCase()));
		Frqssi qssi = this.dataService.getFormularioQssi(site, idioma, id);
		String laurl = this.urlFactory.qssiFinalUrl(site, idioma, qssi);
		return "<a href=\"" + laurl + "\">" + ((TraduccionFrqssi) qssi.getTraduce()).getNombre() + "</a>";
		
		
	}

	/**
	 * Método que crea un bloque completo del tag de html "<style>"
	 * 
	 * @param idmicrosite
	 * @param componente
	 * @return StringBuffer con todo el bloque '<style>'
	 */
	private StringBuffer configStyle3(Microsite site, Componente componente) {
		StringBuffer retorno = new StringBuffer();

		// se monta todo en un estilo, luego simplemente hay que aplicarlo
		retorno.append("<style> " + "\n");
		// retorno.append("div#element" + componente.getId() +
		// " h2 { padding:.5em 0 0 2em; border-top: #85bbe4 2px solid; background: url(imgs/titol/ico_blau.gif) #fff no-repeat left top; color: #00276c;  } \n");
		if (componente.getSoloimagen().equals("S")) {
			retorno.append("table#element" + componente.getId() + " tr { margin:0; padding:.2em; } \n");
			retorno.append("table#element" + componente.getId() + " tr td { vertical-align:top; }   \n");

		} else {
			if (componente.getImagenbul() != null) {
				retorno.append("table#element" + componente.getId() + " tr { margin:0; padding:.2em; } \n");
				retorno.append("table#element" + componente.getId() + " tr td { vertical-align:top; }  \n");
			} else {
				if (componente.getTipo().getTipoelemento().equals(Microfront.ELEM_NOTICIA)) {
					retorno.append("table#element" + componente.getId() + " tr { margin:0; padding:.2em; } \n");
					retorno.append("div#element" + componente.getId() + " tr td { vertical-align:top; }  \n");
				} else {
					retorno.append("table#element" + componente.getId() + " tr { margin:0; padding:.2em; } \n");
					retorno.append("table#element" + componente.getId() + " tr td { vertical-align:top; }  \n");
				}
			}
		}
		if (componente.getFilas().equals("S")) {
			retorno.append("table#element" + componente.getId() + " tr.par { background-color:#EFEFEF; } \n");
		}

		retorno.append("table#element" + componente.getId() + " tr td span.font { font-weight: bold; font-style: italic } \n");
		retorno.append("table#element" + componente.getId() + " tr td span.enllas { margin-top: 0.2em; display: block; padding-left: 0.2em } \n");
		retorno.append("table#element" + componente.getId() + " tr td img { margin-right:0em; } \n");
		retorno.append("table#element" + componente.getId()
				+ " tr td img.imagen { float:left; margin-right:.3em; padding-right:.2em; padding-bottom:.8em;} \n");
		retorno.append(" </style>" + "\n");

		return retorno;
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
	private String getHtmlAgendaCalendario(Microsite site, String idioma, HttpServletRequest request, HttpServletResponse response)
			throws DelegateException {

		Map<String, Object> model = new HashMap<String, Object>();

		model.put("MVS_datos_agenda_calendario", this.dataService.getDatosCalendario(site, idioma));
		model.put("MVS_microsite", site);
		model.put("MVS_idioma", new Idioma(idioma));

		return this.templateProcessor.process(model, this.templateNameFactory.moduloAgenda(site), idioma, request, response);
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
	public String getHtmlNoticias(Microsite site, String idioma, HttpServletRequest request, HttpServletResponse response) throws DelegateException {

		Map<String, Object> model = new HashMap<String, Object>();

		model.put("MVS_home_datos_noticias_listado", this.dataService.getNoticiasHome(site, idioma));
		model.put("MVS_microsite", site);
		model.put("MVS_idioma", new Idioma(idioma));

		return this.templateProcessor.process(model, this.templateNameFactory.moduloNoticiasHome(site), idioma, request, response);

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
	public String getHtmlElementos(Microsite site, Long tipo, String idioma, HttpServletRequest request, HttpServletResponse response)
			throws ExceptionFrontPagina, DelegateException {

		Map<String, Object> model = new HashMap<String, Object>();
		model.put("MVS_tipo", this.noticiasDataService.loadTipo(tipo, idioma));
		model.put("MVS_listado_elementos", this.noticiasDataService.listarNoticiasTipo(site, idioma, tipo, site.getNumeronoticias()));
		model.put("MVS_microsite", site);
		model.put("MVS_idioma", new Idioma(idioma));

		return this.templateProcessor.process(model, this.templateNameFactory.componenteElementos(site), idioma, request, response);

	}

}
