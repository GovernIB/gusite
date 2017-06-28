package es.caib.gusite.front.general;

import java.beans.PropertyEditorSupport;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.ModelAndView;

import es.caib.gusite.front.general.bean.ErrorMicrosite;
import es.caib.gusite.front.service.FrontDataService;
import es.caib.gusite.front.service.FrontUrlFactory;
import es.caib.gusite.front.service.TemplateNameFactory;
import es.caib.gusite.front.view.ErrorGenericoView;
import es.caib.gusite.front.view.PageView;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.Microsite;

/**
 * Front controller.
 * @author slromero
 *
 */
public class FrontController {

	/**
	 * TODO: mover a un WebBindingInitializer
	 * (http://docs.spring.io/spring/docs/
	 * 3.0.x/reference/mvc.html#mvc-ann-webdatabinder)
	 * 
	 * @param binder
	 */
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Idioma.class, new PropertyEditorSupport() {
			@Override
			public String getAsText() {
				return ((Idioma) this.getValue()).getLang();
			}

			@Override
			public void setAsText(String text) throws IllegalArgumentException {
				if (text == null || text.length() != 2 || !text.matches("[a-zA-Z][a-zA-Z]")) {
					throw new IllegalArgumentException("No es un identificador de idioma");
				}
				this.setValue(new Idioma(text.toLowerCase()));
			}
		});
		binder.registerCustomEditor(SiteId.class, new PropertyEditorSupport() {
			@Override
			public String getAsText() {
				return ((SiteId) this.getValue()).uri;
			}

			@Override
			public void setAsText(String text) throws IllegalArgumentException {
				if (text.startsWith("resources")) {
					throw new IllegalArgumentException("No es un identificador de sitio web");
				}
				this.setValue(new SiteId(text));
			}
		});
		binder.registerCustomEditor(UriContenido.class, new PropertyEditorSupport() {
			@Override
			public String getAsText() {
				return ((UriContenido) this.getValue()).nemotecnic;
			}

			@Override
			public void setAsText(String text) throws IllegalArgumentException {
				if (text.length() < 3) {
					throw new IllegalArgumentException("No es un nemotècnico válido");
				}
				this.setValue(new UriContenido(text));
			}
		});
	}

	public class SiteId {

		public SiteId(String text) {
			this.uri = text;
		}

		public String uri = null;

	}

	public class UriContenido {

		public UriContenido(String text) {
			this.nemotecnic = text;
		}

		public String nemotecnic = null;

	}

	private static Log log = LogFactory.getLog(FrontController.class);
	public static final Idioma DEFAULT_IDIOMA = new Idioma(Front.DEFAULT_IDIOMA);
	protected static final String LANG_EN = "en";
	protected static final String LANG_CA = "ca";
	protected static final String LANG_ES = "es";

	@Autowired
	protected FrontUrlFactory urlFactory;

	@Autowired
	protected FrontDataService dataService;

	@Autowired
	protected TemplateNameFactory templateNameFactory;

	@Autowired
	private MessageSource messageSource;

	/**
	 * Obtiene una cadena de mensaje
	 * 
	 * @param uri
	 * @param lang
	 * @return
	 */
	protected String getMessage(String uri, Idioma lang) {

		return this.getMessage(uri, lang.getLang());
	}

	/**
	 * Obtiene una cadena de mensaje
	 * 
	 * @param uri
	 * @param lang
	 * @return
	 */
	protected String getMessage(String uri, String lang) {

		return this.messageSource.getMessage(uri, null, new Locale(lang.toUpperCase(), lang.toUpperCase()));
	}

	/**
	 * Metodo protegido que devuelve String de error de contenido de un site.
	 * 
	 * @param HttpServletRequest
	 *            request, Microsite microsite, ErrorMicrosite errorMicrosite
	 * @exception Exception
	 */
	protected String getForwardError(Microsite microsite, Idioma lang, Model model, String ambitError, HttpServletResponse response) {

		ErrorMicrosite errorMicrosite;

		if (ambitError.equals(ErrorMicrosite.ERROR_AMBIT_MICRO)) {
			if (microsite == null) {
				response.setStatus(ErrorMicrosite.ESTADO_NOT_FOUNT_INT);
				errorMicrosite = new ErrorMicrosite(ErrorMicrosite.ERROR_MICRO_TIT, ErrorMicrosite.ERROR_MICRO_MSG,"","",ErrorMicrosite.ESTADO_NOT_FOUNT);
			} else {
				response.setStatus(ErrorMicrosite.ESTADO_SERVER_INT);
				errorMicrosite = new ErrorMicrosite(ErrorMicrosite.ERROR_SERVER_TIT, ErrorMicrosite.ERROR_SERVER_MSG + microsite.getId(),"","",ErrorMicrosite.ESTADO_SERVER);
			}
			
		} else if (ErrorMicrosite.ERROR_AMBIT_PAGINA.equals(ambitError)) {
			response.setStatus(ErrorMicrosite.ESTADO_NOT_FOUNT_INT);
			errorMicrosite = new ErrorMicrosite(ErrorMicrosite.ERROR_PAGINA_TIT, ErrorMicrosite.ERROR_PAGINA_MSG,"","",ErrorMicrosite.ESTADO_NOT_FOUNT);
			
		} else if (ErrorMicrosite.ERROR_AMBIT_DOCUMENT.equals(ambitError) ) {
			response.setStatus(ErrorMicrosite.ESTADO_NOT_FOUNT_INT);
			errorMicrosite = new ErrorMicrosite(ErrorMicrosite.ERROR_DOCU_TIT, ErrorMicrosite.ERROR_DOCU_MSG,"","",ErrorMicrosite.ESTADO_NOT_FOUNT);
			
		} else if (ErrorMicrosite.ERROR_AMBIT_ACCES.equals(ambitError)) {
			response.setStatus(ErrorMicrosite.ESTADO_FORBIDDEN_INT);
			errorMicrosite = new ErrorMicrosite(ErrorMicrosite.ERROR_ACCES_TIT, ErrorMicrosite.ERROR_ACCES_MSG,"","",ErrorMicrosite.ESTADO_FORBIDDEN);
			
		} else if (ErrorMicrosite.ERROR_AMBIT_SESSIO.equals(ambitError)) {
			response.setStatus(ErrorMicrosite.ESTADO_SESSION_INT);
			errorMicrosite = new ErrorMicrosite(ErrorMicrosite.ERROR_SESSIO_TIT, ErrorMicrosite.ERROR_SESSIO_MSG,"","",ErrorMicrosite.ESTADO_SESSION);
			
		} else if (ErrorMicrosite.ERROR_AMBIT_SERVER.equals(ambitError)) {
			response.setStatus(ErrorMicrosite.ESTADO_SERVER_INT);
			errorMicrosite = new ErrorMicrosite(ErrorMicrosite.ERROR_SOLR_TIT, ErrorMicrosite.ERROR_SOLR_MSG,"","",ErrorMicrosite.ESTADO_SERVER);
			
		} else if (ErrorMicrosite.ERROR_AMBIT_SOLR.equals(ambitError)) {
			response.setStatus(ErrorMicrosite.ESTADO_SERVER_INT);
			errorMicrosite = new ErrorMicrosite(ErrorMicrosite.ERROR_SOLR_TIT, ErrorMicrosite.ERROR_SOLR_MSG,"","",ErrorMicrosite.ESTADO_SERVER);
			
		} else {
			response.setStatus(ErrorMicrosite.ESTADO_SERVER_INT);
			errorMicrosite = new ErrorMicrosite(ErrorMicrosite.ERROR_SERVER_TIT, ErrorMicrosite.ERROR_SERVER_MSG,"","",ErrorMicrosite.ESTADO_SERVER);
			
		}
		model.addAttribute("MVS_errparam", errorMicrosite);

		if (microsite != null) {
			microsite.setTipocabecera("1");
		}
		model.addAttribute("MVS_errestado", errorMicrosite.getEstado());
		model.addAttribute("MVS_microsite", microsite);
		model.addAttribute("MVS_idioma", lang.getLang());

		return this.templateNameFactory.errorGenerico(microsite);

	}

}
