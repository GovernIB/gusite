package es.caib.gusite.front.general;

import java.beans.PropertyEditorSupport;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import es.caib.gusite.front.general.bean.ErrorMicrosite;
import es.caib.gusite.front.service.FrontDataService;
import es.caib.gusite.front.service.FrontUrlFactory;
import es.caib.gusite.front.service.TemplateNameFactory;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.Microsite;

public class FrontController {

	/**
	 * TODO: mover a un WebBindingInitializer (http://docs.spring.io/spring/docs/3.0.x/reference/mvc.html#mvc-ann-webdatabinder)
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
	            setValue(new Idioma(text.toLowerCase()));
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
	            setValue(new SiteId(text));
	        }
	    });
	    binder.registerCustomEditor(TipoNoticiaId.class, new PropertyEditorSupport() {
	        @Override
	        public String getAsText() {
	            return ((TipoNoticiaId) this.getValue()).nemotecnic;
	        }

	        @Override
	        public void setAsText(String text) throws IllegalArgumentException {
	        	if (text.length() < 3 || StringUtils.isNumeric(text) ) { 
	        		throw new IllegalArgumentException("No es un nemotècnico válido");
	        	}
	            setValue(new TipoNoticiaId(text));
	        }
	    });
	}	
	
	
	public class SiteId {

		public SiteId(String text) {
			uri = text;
		}

		public String uri = null;
		
		
		
	}
	
	
	
	
	public class TipoNoticiaId {

		public TipoNoticiaId(String text) {
			nemotecnic = text;
		}

		public String nemotecnic = null;
		
		
	}
	
	private static Log log = LogFactory.getLog(FrontController.class);
	protected static final Idioma DEFAULT_IDIOMA = new Idioma("ca");
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
	 * @param uri
	 * @param lang
	 * @return
	 */
	protected String getMessage(String uri, Idioma lang) {

		return this.messageSource.getMessage(uri, null, new Locale(lang.getLang().toUpperCase(), lang.getLang().toUpperCase()));
	}

    /**
     * Metodo protegido que devuelve String de error de contenido de un site
     * @param HttpServletRequest request, Microsite microsite, ErrorMicrosite errorMicrosite
     * @exception Exception
     */
	protected String getForwardError(Microsite microsite, Idioma lang, Model model, String ambitError) {
	     
		   ErrorMicrosite errorMicrosite;
			
		   if (ambitError.equals(ErrorMicrosite.ERROR_AMBIT_MICRO)) {
		  	   
			  if(microsite != null) {
		  	   errorMicrosite = new ErrorMicrosite(ErrorMicrosite.ERROR_MICRO_TIT, ErrorMicrosite.ERROR_MICRO_MSG + microsite.getId());
			  } else {
				  errorMicrosite = new ErrorMicrosite(ErrorMicrosite.ERROR_MICRO_TIT, ErrorMicrosite.ERROR_MICRO_MSG_NULL);
			  }
			  model.addAttribute("MVS_errparam", errorMicrosite); 
		   }	   
		   else if (ambitError == ErrorMicrosite.ERROR_AMBIT_PAGINA) {
			   errorMicrosite = new ErrorMicrosite(ErrorMicrosite.ERROR_PAGINA_TIT, ErrorMicrosite.ERROR_PAGINA_MSG);
			   model.addAttribute("MVS_errparam", errorMicrosite); 
		   }
		   else if (ambitError == ErrorMicrosite.ERROR_AMBIT_DOCUMENT) {
			   errorMicrosite = new ErrorMicrosite(ErrorMicrosite.ERROR_DOCU_TIT, ErrorMicrosite.ERROR_DOCU_MSG);
			   model.addAttribute("MVS_errparam", errorMicrosite); 
		   }
		   else if (ambitError == ErrorMicrosite.ERROR_AMBIT_ACCES) {
			   errorMicrosite = new ErrorMicrosite(ErrorMicrosite.ERROR_ACCES_TIT, ErrorMicrosite.ERROR_ACCES_MSG);
			   model.addAttribute("MVS_errparam", errorMicrosite); 
		   }
		   else if (ambitError == ErrorMicrosite.ERROR_AMBIT_SESSIO) {
			   errorMicrosite = new ErrorMicrosite(ErrorMicrosite.ERROR_SESSIO_TIT, ErrorMicrosite.ERROR_SESSIO_MSG);
			   model.addAttribute("MVS_errparam", errorMicrosite); 
		   }	   
		   else {
			   errorMicrosite = new ErrorMicrosite(ErrorMicrosite.ERROR_PAGINA_TIT, ErrorMicrosite.ERROR_PAGINA_MSG);
			   model.addAttribute("MVS_errparam", errorMicrosite); 
		   }
		     	   
		  if(microsite != null) {
			  microsite.setTipocabecera("1");
		  }
	  	   
	  	   model.addAttribute("MVS_microsite", microsite); 
	  	   model.addAttribute("MVS_idioma", lang.getLang());
	  	   
	  	   return this.templateNameFactory.errorGenerico(microsite);
			
	}

	
	
}
