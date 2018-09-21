package es.caib.gusite.front.contacto;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.util.StringUtils;

import es.caib.gusite.front.captcha.CaptchaUtil;
import es.caib.gusite.front.captcha.ImagenCaptcha;
import es.caib.gusite.front.general.BaseCriteria;
import es.caib.gusite.front.general.BaseViewController;
import es.caib.gusite.front.general.ExceptionFrontContactos;
import es.caib.gusite.front.general.ExceptionFrontMicro;
import es.caib.gusite.front.general.ExceptionFrontPagina;
import es.caib.gusite.front.general.Microfront;
import es.caib.gusite.front.general.bean.ErrorMicrosite;
import es.caib.gusite.front.general.bean.Pardato;
import es.caib.gusite.front.general.bean.PathItem;
import es.caib.gusite.front.general.bean.ResultadoBusqueda;
import es.caib.gusite.front.microtag.MParserHTML;
import es.caib.gusite.front.service.ContactosDataService;
import es.caib.gusite.front.service.CorreoEngineService;
import es.caib.gusite.front.view.ContactoDatosView;
import es.caib.gusite.front.view.ContactoView;
import es.caib.gusite.front.view.ListarContactosView;
import es.caib.gusite.front.view.PageView;
import es.caib.gusite.micromodel.Contacto;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.Lineadatocontacto;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.TraduccionFContacto;
import es.caib.gusite.micromodel.TraduccionLineadatocontacto;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import nl.captcha.Captcha;

/**
 * Listados de formularios de contacto y página de formulario de contacto
 * 
 * @author brujula-at4
 * 
 */
@Controller
public class ContactosController extends BaseViewController {

	private static Log log = LogFactory.getLog(ContactosController.class);

	@Autowired
	protected ContactosDataService contactosDataService;

	/**
	 * Listado de contactos del microsite. Si sólo existe un contacto, redirige
	 * a {@link #contacto}
	 * 
	 * @param uri
	 *            Uri de microsite
	 * @param lang
	 *            Idioma de la petición
	 * @return
	 */
	@RequestMapping("{uri}/{lang:[a-zA-Z][a-zA-Z]}/fcontacto")
	public ModelAndView listarcontactos(@PathVariable("uri") SiteId URI, @PathVariable("lang") Idioma lang,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa,
			@RequestParam(value = "filtro", required = false, defaultValue = "") String filtro,
			@RequestParam(value = "pagina", required = false, defaultValue = "1") int pagina,
			@RequestParam(value = "ordenacion", required = false, defaultValue = "") String ordenacion, HttpServletRequest req, HttpServletResponse response) {

		ListarContactosView view = new ListarContactosView();
		try {
			super.configureLayoutView(URI.uri, lang, view, pcampa, null);
			Microsite microsite = view.getMicrosite();
			if (microsite == null) {
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + URI);				
			}
			
			BaseCriteria criteria = new BaseCriteria(filtro, pagina, ordenacion);
			ResultadoBusqueda<Contacto> formularios = this.contactosDataService.getListadoFormularios(microsite, lang, criteria);

			if (formularios.getTotalNumRecords() == 1) {
				Contacto contacto = formularios.getResultados().iterator().next();
				String uriContacto = ((TraduccionFContacto) contacto.getTraduccion(lang.getCodigoEstandar())).getUri();
				if(StringUtils.isEmpty(uriContacto)) {
					return this.contacto(URI, lang, uriContacto, mcont, pcampa, req,null, response);
				}				
			}

			List<Pardato> listaNombreContactos = new ArrayList<Pardato>();
			for (Contacto contacto : formularios.getResultados()) {
				Pardato pardato = new Pardato();
				pardato.setKey(contacto.getTitulocontacto(lang.getLang().toLowerCase()));
				pardato.setValue(this.urlFactory.contacto(microsite, lang, contacto));
				listaNombreContactos.add(pardato);
			}

			view.setSeuletSin(this.urlFactory.listarContactosSinPagina(microsite, lang, criteria));
			view.setParametrosPagina(formularios.getParametros());
			view.setListado(listaNombreContactos);
			view.setIdContenido(mcont);
			this.cargarMollapan(view);

			return this.modelForView(this.templateNameFactory.listarContactos(microsite), view);

		} catch (DelegateException e) {
			log.error(e.getMessage());
			return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_PAGINA, response);
		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_MICRO, response,URI.uri,lang,req);
		} catch (Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		} 

	}

	/*
	 * Listado de contactos del microsite en español.
	 * 
	 * @see #listarcontactos(SiteId, Idioma, Model, String, String, String, int,
	 *      String, HttpServletRequest)
	 * @param uri
	 *            Uri de microsite
	 * @param lang
	 *            Idioma de la petición
	 * @return
	 *
	@RequestMapping("{uri}/contacto/")
	public ModelAndView listarcontactosEs(@PathVariable("uri") SiteId URI,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa,
			@RequestParam(value = "filtro", required = false, defaultValue = "") String filtro,
			@RequestParam(value = "pagina", required = false, defaultValue = "1") int pagina,
			@RequestParam(value = "ordenacion", required = false, defaultValue = "") String ordenacion, HttpServletRequest req, HttpServletResponse response) {

		return this.listarcontactos(URI, DEFAULT_IDIOMA, mcont, pcampa, filtro, pagina, ordenacion, req, response);
	}

	/**
	 * Listado de contactos del microsite en catalán.
	 * 
	 * @see #listarcontactos(SiteId, Idioma, Model, String, String, String, int,
	 *      String, HttpServletRequest)
	 * @param uri
	 *            Uri de microsite
	 * @param lang
	 *            Idioma de la petición
	 * @return
	 *
	@RequestMapping("{uri}/contacte/")
	public ModelAndView listarcontactosCa(@PathVariable("uri") SiteId URI,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa,
			@RequestParam(value = "filtro", required = false, defaultValue = "") String filtro,
			@RequestParam(value = "pagina", required = false, defaultValue = "1") int pagina,
			@RequestParam(value = "ordenacion", required = false, defaultValue = "") String ordenacion, HttpServletRequest req, HttpServletResponse response) {

		return this.listarcontactos(URI, new Idioma(LANG_CA), mcont, pcampa, filtro, pagina, ordenacion, req, response);
	}

	/**
	 * Listado de contactos del microsite en inglés.
	 * 
	 * @see #listarcontactos(SiteId, Idioma, Model, String, String, String, int,
	 *      String, HttpServletRequest)
	 * @param uri
	 *            Uri de microsite
	 * @param lang
	 *            Idioma de la petición
	 * @return
	 *
	@RequestMapping("{uri}/contact/")
	public ModelAndView listarcontactosEn(@PathVariable("uri") SiteId URI,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa,
			@RequestParam(value = "filtro", required = false, defaultValue = "") String filtro,
			@RequestParam(value = "pagina", required = false, defaultValue = "1") int pagina,
			@RequestParam(value = "ordenacion", required = false, defaultValue = "") String ordenacion, HttpServletRequest req, HttpServletResponse response) {

		return this.listarcontactos(URI, new Idioma(LANG_EN), mcont, pcampa, filtro, pagina, ordenacion, req, response);
	}*/

	/**
	 * @param lang
	 * @param uri
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "{uri}/{lang:[a-zA-Z][a-zA-Z]}/fcontacto/{uriContacto}")
	public ModelAndView contacto(@PathVariable("uri") SiteId URI, @PathVariable("lang") Idioma lang, @PathVariable("uriContacto") String uriContacto,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa, HttpServletRequest req,
			@RequestParam(value = Microfront.ERRORCAPTCHA, required = false, defaultValue = "") String errorCaptcha, HttpServletResponse response) {

		ContactoView view = new ContactoView();
		try {
			super.configureLayoutView(URI.uri, lang, view, pcampa, null);
			Microsite microsite = view.getMicrosite();
			if (microsite == null) {
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + URI);				
			}
			
			Contacto contacto = this.contactosDataService.getFormularioByUri(microsite, lang, uriContacto);
			
			// comprobacion de microsite
			if (contacto.getIdmicrosite().longValue() != microsite.getId().longValue()) {
				log.error("El elemento solicitado no pertenece al site");
				return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
			}
			// comprobacion de visibilidad
			if (!contacto.getVisible().equals("S")) {
				log.error("El elemento solicitado no está visible");
				return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_PAGINA, response);
			}
 
			view.setContacto(contacto);
			view.setContactoTitulo(contacto.getTitulocontacto(lang.getLang()));
			view.setContactoListaTags(this.montaListaTags(microsite, lang, contacto));
			view.setIdContenido(mcont);
			
			//captcha
			String key = CaptchaUtil.generarKeyCaptcha();
			req.getSession().setAttribute("captchaKey", key);
		    ImagenCaptcha imgCap = CaptchaUtil.generaCaptcha(key);
		    if (errorCaptcha != null && !errorCaptcha.isEmpty()){
		    	imgCap.setError(errorCaptcha);
		    }
		    view.setCaptcha(imgCap);
			
			this.cargarMollapan(view, contacto);

			return this.modelForView(this.templateNameFactory.contacto(microsite), view);

		} catch (DelegateException e) {
			log.error(e.getMessage());
			return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_PAGINA, response);
		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_MICRO, response,URI.uri,lang,req);
		} catch (ExceptionFrontContactos e) {
			log.error(e.getMessage()); 
			return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_PAGINA, response);
		} catch (Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		} 

	}
	
	
	
	/**
	 * @param lang
	 * @param uri
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "{uri}/{lang:[a-zA-Z][a-zA-Z]}/contact/{contacto}/")
	public ModelAndView contactoOld(@PathVariable("uri") SiteId URI, @PathVariable("lang") Idioma lang, @PathVariable("contacto") long idContacto,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa, HttpServletRequest req,
			@RequestParam(value = Microfront.ERRORCAPTCHA, required = false, defaultValue = "") String errorCaptcha, HttpServletResponse response) {

		ContactoView view = new ContactoView();
		try {
						
			super.configureLayoutView(URI.uri, lang, view, pcampa, null);
			Microsite microsite = view.getMicrosite();
			if (microsite == null) {
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + URI);				
			}
			
			Contacto contacto = this.contactosDataService.getFormulario(microsite, lang, idContacto);
			if (contacto == null) {
				throw new ExceptionFrontContactos(URI.uri, idContacto);
			}
			
			return new ModelAndView("redirect:"+ this.urlFactory.contacto( microsite, lang, contacto));

		} catch (DelegateException e) {
			log.error(e.getMessage());
			return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_PAGINA, response);
		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_MICRO, response,URI.uri,lang,req);
		} catch (ExceptionFrontContactos e) {
			log.error(e.getMessage()); 
			return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_PAGINA, response);
		} catch (Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		} 

	}
	
	
	

	/**
	 * @param lang
	 * @param uri
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "{uri}/{lang:[a-zA-Z][a-zA-Z]}/fcontacto/{uriContacto}")
	public ModelAndView enviarContacto(@PathVariable("uri") SiteId URI, @PathVariable("lang") Idioma lang, @PathVariable("uriContacto") String uriContacto,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa,
			@RequestParam(value = "docAnex", required = false) CommonsMultipartFile docAnexFileData, MultipartHttpServletRequest req,
			@RequestParam(value = Microfront.RCAPTCHA, required = false, defaultValue = "") String respCaptcha, HttpServletResponse response) {

		ContactoDatosView view = new ContactoDatosView();
		try {
			
			String captchaKey=(String) req.getSession().getAttribute("captchaKey");
			
			//No se ha rellenado le captcha
			if (respCaptcha == null || respCaptcha.isEmpty()){				
				return this.contacto(URI, lang, uriContacto, mcont, pcampa, req, this.getMessage("captcha.no.relleno", lang), response);
			}
			//Comprobamos
			if (captchaKey!=null){
				Captcha captcha = CaptchaUtil.creaCaptcha(captchaKey);
				if (!captcha.isCorrect(respCaptcha)){
					return this.contacto(URI, lang, uriContacto, mcont, pcampa, req, this.getMessage("captcha.incorrecto", lang), response);
				}
			}
			
			super.configureLayoutView(URI.uri, lang, view, pcampa, null);
			Microsite microsite = view.getMicrosite();
			if (microsite == null) {
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + URI);				
			}
			
			Contacto contacto = this.contactosDataService.getFormularioByUri(microsite, lang, uriContacto);
			
			
			view.setContacto(contacto);
			view.setContactoTitulo(contacto.getTitulocontacto(lang.getLang()));

			// comprobacion de microsite
			if (contacto.getIdmicrosite().longValue() != microsite.getId().longValue()) {
				log.error("El elemento solicitado no pertenece al site");
				return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_MICRO, response);
			}
			// comprobacion de visibilidad
			if (!contacto.getVisible().equals("S")) {
				log.error("El elemento solicitado no está visible");
				return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_PAGINA, response);
			}

			String mensaje = this.procesaFormulario(contacto, view.getLang(), req);

			if (docAnexFileData == null) {
				this.enviarFormulario(contacto, lang, mensaje, null, null);
			} else {
				this.enviarFormulario(contacto, lang, mensaje, docAnexFileData.getInputStream(), docAnexFileData.getOriginalFilename());
			}

			this.cargarMollapan(view, contacto);

			return this.modelForView(this.templateNameFactory.envioContacto(microsite), view);

		} catch (DelegateException e) {
			log.error(e.getMessage());
			return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_PAGINA, response);
		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_MICRO, response,URI.uri,lang,req);
		} catch (ExceptionFrontPagina e) {
			log.error(e.getMessage());
			return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_PAGINA, response);
		} catch (IOException e) {
			log.error(e.getMessage());
			return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_PAGINA, response);
		} catch (ExceptionFrontContactos e) {
			log.error(e.getMessage());
			return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_PAGINA, response);
		} catch (Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		} 

	}

	/**
	 * Método privado que se encarga de enviar via correo electrónico la
	 * información del formulario
	 * 
	 * @throws ExceptionFrontPagina
	 */
	private void enviarFormulario(Contacto contacto, Idioma lang, String mensaje, InputStream docAnex, String fileName) throws ExceptionFrontPagina {

		java.util.GregorianCalendar fecha = new java.util.GregorianCalendar();
		String mensaje_asunto = contacto.getTitulocontacto(lang.getLang().toLowerCase());
		StringBuffer mensaje_cuerpo = new StringBuffer("");

		mensaje_cuerpo.append(mensaje);
		mensaje_cuerpo.append(fecha.getTime().toString());

		CorreoEngineService correo = new CorreoEngineService();
		if (docAnex != null) {
			correo.setFile(docAnex, fileName);
		}
		correo.initCorreo(contacto.getEmail(), mensaje_asunto, false, mensaje_cuerpo);
		if (!correo.enviarCorreo()) {
			throw new ExceptionFrontPagina("Problema enviando correo");
		}
	}

	/**
	 * Recorreremos el formulario y lo iremos empaquetando en un string que será
	 * el cuerpo del mensaje a enviar por correo.
	 * 
	 */
	private String procesaFormulario(Contacto contacto, Idioma lang, HttpServletRequest req) {

		StringBuilder cuerpoMensaje = new StringBuilder();
		cuerpoMensaje.append("Idioma = ").append(lang.getLang()).append("\n");

		for (Lineadatocontacto linea : contacto.getLineasdatocontacto()) {

			if (linea.getTipo().equals(Contacto.RTYPE_TITULO)) {
				continue; // el tipo n
			}
			String paramName = ((linea.getObligatorio() == 1) ? Microfront.VCAMPO_REQUERIDO : "") + linea.getId().toString();
			String campovalor = ((TraduccionLineadatocontacto) linea.getTraduccion(lang.getLang())).getTexto().split("#")[0] + " = ";
			String[] paramValues = req.getParameterValues(paramName);
			if (paramValues.length == 1) {
				String paramValue = paramValues[0];
				if (paramValue.length() == 0) {
					campovalor += "[sin valor]" + "\n";
				} else {
					campovalor += paramValue + "\n";
				}
			} else {
				for (String paramValue : paramValues) {
					campovalor += paramValue + ", ";
				}
				campovalor += "\n";

			}
			cuerpoMensaje.append(campovalor);
		}
		return cuerpoMensaje.toString();
	}

	/**
	 * Prepara un arraylist que contiene el bean pardato. Prepara una hashtable
	 * que contiene el bean pardato. En el bean se mete: en el key: el texto o
	 * titulo en el value: el tag html del elemento del formulario
	 * 
	 * La clave en el hash será el id de lineadatocontacto.
	 * 
	 * @param lang
	 * @return
	 * 
	 */
	private List<Pardato> montaListaTags(Microsite microsite, Idioma lang, Contacto contacto) {

		List<Pardato> listalineas = new ArrayList<Pardato>();

		for (Lineadatocontacto ld : contacto.getLineasdatocontacto()) {
			Pardato pardato = new Pardato();
			pardato.setId(ld.getId().toString());
			MParserHTML parserhtml = new MParserHTML();
			if ((ld.getTipo().equals(Contacto.RTYPE_TEXTAREA)) || (ld.getTipo().equals(Contacto.RTYPE_TEXTO))) {
				pardato.setKey(((TraduccionLineadatocontacto) ld.getTraduccion(lang.getLang())).getTexto());
				if (ld.getLineas() == 0) {
					pardato.setValue(parserhtml.getTagText(ld.getId().toString(), ld.getTamano(), ld.getObligatorio()).toString());
				} else {
					pardato.setValue(parserhtml.getTagTextarea(ld.getId().toString(), 50, ld.getLineas(), ld.getObligatorio()).toString());
				}
			}
			if ((ld.getTipo().equals(Contacto.RTYPE_SELECTORMULTIPLE)) || (ld.getTipo().equals(Contacto.RTYPE_SELECTOR))) {
				pardato.setKey(this.getNombreinselect(((TraduccionLineadatocontacto) ld.getTraduccion(lang.getLang())).getTexto()));
				pardato.setValue(parserhtml.getTagSelect(ld.getId().toString(),
						this.getListaopciones(((TraduccionLineadatocontacto) ld.getTraduccion(lang.getLang())).getTexto()), ld.getTipo(),
						ld.getLineas(), ld.getObligatorio()).toString());

			}
			if (!ld.getTipo().equals(Contacto.RTYPE_TITULO)) {
				listalineas.add(pardato);
			}
		}
		return listalineas;
	}

	/**
	 * devuelve la primera cadena de texto de un string utilizando como
	 * separador Microfront.separatorwords
	 * 
	 * @param cadena
	 * @return
	 */

	private String getNombreinselect(String cadena) {
		String retorno = "";
		if (cadena.length() > 0) {
			String txseparador = "" + Microfront.separatorwordsform;
			String[] listastringcadenas = cadena.split(txseparador);
			if (listastringcadenas.length == 0) {
			} else {
				retorno = listastringcadenas[0];
			}

		}
		return retorno;
	}

	/**
	 * Devuelve un arraylist que contiene strings. En la lista se introducen
	 * todos menos el primero. Se utiliza como separador del string
	 * Microfront.separatorwords
	 * 
	 * @param cadena
	 * @return
	 */
	private ArrayList<String> getListaopciones(String cadena) {
		ArrayList<String> lista = new ArrayList<String>();
		if (cadena.length() > 0) {
			String txseparador = "" + Microfront.separatorwordsform;
			String[] listastringcadenas = cadena.split(txseparador);
			for (int i = 1; i < listastringcadenas.length; i++) {
				if (listastringcadenas[i].length() > 0) {
					lista.add(listastringcadenas[i]);
				}
			}
		}
		return lista;
	}

	/*
	 * @param lang
	 * @param uri
	 * @param model
	 * @return
	 **/
	@RequestMapping(method = RequestMethod.GET, value = "{uri}/contacto/{contacto}")
	public ModelAndView contactoEs(@PathVariable("uri") SiteId URI, @PathVariable("contacto") long idContacto,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa, HttpServletRequest req, HttpServletResponse response) {

		return this.contactoOld(URI, new Idioma(LANG_ES), idContacto, mcont, pcampa, req,null, response);
	}

	/**
	 * @param lang
	 * @param uri
	 * @param model
	 * @return
	 **/
	@RequestMapping(method = RequestMethod.GET, value = "{uri}/contacte/{contacto}")
	public ModelAndView contactoCa(@PathVariable("uri") SiteId URI, @PathVariable("contacto") long idContacto,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa, HttpServletRequest req, HttpServletResponse response) {

		return this.contactoOld(URI, new Idioma(LANG_CA), idContacto, mcont, pcampa, req,null, response);
	}

	/**
	 * @param lang
	 * @param uri
	 * @param model
	 * @return
	 **/
	@RequestMapping(method = RequestMethod.GET, value = "{uri}/contact/{contacto}")
	public ModelAndView contactoEn(@PathVariable("uri") SiteId URI, @PathVariable("contacto") long idContacto,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa, HttpServletRequest req, HttpServletResponse response) {

		return this.contactoOld(URI, new Idioma(LANG_EN), idContacto, mcont, pcampa, req,null, response);
	}
	

	/**
	 * Método privado para guardar el recorrido que ha realizado el usuario por
	 * el microsite.
	 * 
	 * @param microsite
	 * @param model
	 * @param lang
	 * @return
	 * @return string recorrido en el microsite
	 */
	private List<PathItem> cargarMollapan(PageView view) {

		List<PathItem> path = super.getBasePath(view);

		path.add(new PathItem(this.getMessage("listarcontactos.frmcontacto", view.getLang()), this.urlFactory.listarContactos(view.getMicrosite(),
				view.getLang())));

		// Datos para la plantilla
		view.setPathData(path);

		return path;

	}

	private void cargarMollapan(PageView view, Contacto contacto) {

		List<PathItem> path = this.cargarMollapan(view);

		/*
		 * original: path.add(new PathItem(getMessage("contacto.formulario",
		 * lang), this.urlFactory.contacto(microsite, lang, contacto)));
		 * 
		 * Mejor usar el título del formulario:
		 */
		String titulo = contacto.getTitulocontacto(view.getLang().getLang());
		if (StringUtils.isEmpty(titulo)) {
			titulo = this.getMessage("contacto.formulario", view.getLang());
		}
		path.add(new PathItem(titulo, this.urlFactory.contacto(view.getMicrosite(), view.getLang(), contacto)));

		// Datos para la plantilla
		view.setPathData(path);

	}

	@Override
	public String setServicio() {

		return Microfront.RCONTACTO;
	}

}
