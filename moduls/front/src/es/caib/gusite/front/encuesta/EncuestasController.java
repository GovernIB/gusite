package es.caib.gusite.front.encuesta;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import es.caib.gusite.front.general.BaseController;
import es.caib.gusite.front.general.ExceptionFrontMicro;
import es.caib.gusite.front.general.Front;
import es.caib.gusite.front.general.Microfront;
import es.caib.gusite.front.general.FrontController.SiteId;
import es.caib.gusite.front.general.bean.ErrorMicrosite;
import es.caib.gusite.front.general.bean.PathItem;
import es.caib.gusite.front.microtag.MParserEncuesta;
import es.caib.gusite.front.service.EncuestasDataService;
import es.caib.gusite.front.util.Fechas;
import es.caib.gusite.front.estadistica.util.StatManager;
import es.caib.gusite.micromodel.Encuesta;
import es.caib.gusite.micromodel.Estadistica;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.Pregunta;
import es.caib.gusite.micromodel.Respuesta;
import es.caib.gusite.micromodel.RespuestaDato;
import es.caib.gusite.micromodel.TraduccionEncuesta;
import es.caib.gusite.micromodel.UsuarioEncuesta;
import es.caib.gusite.micromodel.UsuarioPropietarioRespuesta;
import es.caib.gusite.micromodel.UsuarioPropietarioRespuestaPK;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.EncuestaDelegate;
import es.caib.gusite.micropersistence.delegate.RespuestaDatoDelegate;
import es.caib.gusite.micropersistence.delegate.UsuarioEncuestaDelegate;
import es.caib.loginModule.client.SeyconPrincipal;
import es.caib.gusite.front.encuesta.ParserHtmlJavascript;


@Controller
public class EncuestasController extends BaseController {

	private static Log log = LogFactory.getLog(EncuestasController.class);
	
	
	@Autowired
	protected EncuestasDataService encuestasDataService;

	/**
	 * TODO: mkey debería ser el uri del site TODO: tipo debería ser el
	 * nemotecnico del tipo
	 * 
	 * @param lang
	 * @param mkey
	 * @param model
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET,value="{mkey}/{lang}/encuesta/{encuesta}/")
	public String encuesta(
			@PathVariable("mkey") SiteId siteId, 
			@PathVariable("lang") Idioma lang,
			@PathVariable("encuesta") long idEncuesta,
			Model model,
			@RequestParam(value=Microfront.MCONT, required = false, defaultValue="") String mcont,
			@RequestParam(value=Microfront.PCAMPA, required = false, defaultValue="") String pcampa,
			HttpServletRequest req) {
		
		
		 boolean error = false;
		 boolean ensesion = false;

		Microsite microsite = null;

		try {

			if (estaEnMantenimiento()) {
				model.addAttribute("MVS_manteniment", new Object());

			}

			microsite = super.loadMicrosite(siteId.mkey, lang, model, pcampa);

			// Sustitución función procesaSesion(); de BdEncuesta
			try {

				String vsesion = ""
						+ req.getSession().getAttribute(
								"MVS_encuestarellena" + idEncuesta);
				if (vsesion.equals("yes"))
					ensesion = true;
			} catch (Exception e) {
				log.warn("Error averiguando si el usario ha rellenado la encuesta en sesion");
				ensesion = false;
			}
			// hasta aqui funcion procesSesion(); de BdEncuesta

			Encuesta encuesta = this.encuestasDataService.getEncuestas(
					microsite, lang, idEncuesta);

			// //Canvi Salvador 05/10/2010
			String encuestaRellena = (String) req.getSession().getAttribute(
					"MVS_encuestarellena" + encuesta.getId().toString());
			String visita = (String) req.getSession().getAttribute(
					"MVS_visita" + encuesta.getId().toString());
			String pview = (String) req.getParameter(Microfront.PVIEW);
			if (encuestaRellena != null) {
				if (visita != null && visita.equalsIgnoreCase("1VOT"))
					visita = "NVOT";
				else if (visita != null && visita.equalsIgnoreCase("NVOT"))
					visita = "NVOT";
				else if (pview == null)
					visita = "1VOT";

				req.getSession().setAttribute(
						"MVS_visita" + encuesta.getId().toString(), visita);
				encuesta.setVotoDuplicado(visita);
			}

			// comprobacion de microsite
			if (!(encuesta.getIdmicrosite().longValue() == microsite.getId()
					.longValue())) {
				log.error("El elemento solicitado no pertenece al site");
				error=true;
				return getForwardError(microsite, lang, model,
						ErrorMicrosite.ERROR_AMBIT_MICRO);
			}
			// comprobacion de visibilidad
			if (!encuesta.getVisible().equals("S")) {
				log.error("El elemento solicitado no está visible");
				error=true;
				return getForwardError(microsite, lang, model,
						ErrorMicrosite.ERROR_AMBIT_PAGINA);
			}

			// o bien comprobacion de que esté vigente
			if (!Fechas.vigente(encuesta.getFpublicacion(),
					encuesta.getFcaducidad())) {
				// no pasamos error, simulamos que está rellena la encuesta
				ensesion = true;
			}
			model.addAttribute("MVS_Fechas_vigente", Fechas.vigente(encuesta.getFpublicacion(),
					encuesta.getFcaducidad()));

			// traduce encuesta
			encuesta.setIdi(lang.getLang());
			Iterator<?> iter = encuesta.getPreguntas().iterator();
			while (iter.hasNext()) {

				Pregunta pregunta = (Pregunta) iter.next();
				pregunta.setIdi(lang.getLang());

				Iterator<?> iter2 = pregunta.getRespuestas().iterator();
				while (iter2.hasNext()) {

					Respuesta respuesta = (Respuesta) iter2.next();
					respuesta.setIdi(lang.getLang());
				}

			}

			// si llegamos aqui.... todo ok, así que grabamos la estadistica
			if (error==false) {
			if ("no".equals("" + req.getSession().getAttribute("MVS_stat"))) {
				log.info("Skip Estadistica, preview conten");
			}
			}

			model.addAttribute("MVS_encuesta", encuesta);
			
			if (ensesion==true) {
				return this.enviarEncuesta(siteId, lang, idEncuesta, model, mcont, pcampa, req);
	    		
	    	}

			if (encuesta.getIdentificacion().equals("S")) {
				try {
					SeyconPrincipal principal = null;
					principal = SeyconPrincipal.getCurrent();
				} catch (Exception e) {
					log.error("Error en la identificación del usuario en la encuesta: "
							+ encuesta.getId() + " ---> " + e);

					return getForwardError(microsite, lang, model,
							ErrorMicrosite.ERROR_AMBIT_ACCES);
				}
			}
			
			
			//JAVASCRIPT ENCUESTAS INTRODUCIDO DESDE JAVA EN UNA VARIABLE CON LA NUEVA CLASE (SOLO JAVASCRIPT, PERO HAY QUE ADAPTARLO)
//			ParserHtmlJavascript parsehtml=new ParserHtmlJavascript(microsite.getRestringido());
//			StringBuffer sb1 = parsehtml.getHtmlEncuesta(
//	    			req,
//	    			encuesta.getIdmicrosite(),
//	    			encuesta.getId().toString(),
//	    			encuesta.getIdi()
//				);
//	    	req.setAttribute("MVS_htmlEncuestaParsed",sb1);
			
	    	//HTML Y JAVASCRIPT ENCUESTAS INTRODUCIDO DESDE JAVA EN UNA VARIABLE CON LA CLASE ANTIGUA
//			MParserEncuesta parser = new MParserEncuesta(microsite.getRestringido());
//	    	StringBuffer sb = parser.getHtmlEncuesta(
//	    			req,
//	    			encuesta.getIdmicrosite(),
//	    			encuesta.getId().toString(),
//	    			encuesta.getIdi()
//				);
//	    	req.setAttribute("MVS_htmlEncuestaParsed",sb);
	    	
	    	
			
			ResourceBundle rb =	ResourceBundle.getBundle("ApplicationResources_front", new Locale(lang.getLang().toUpperCase(), lang.getLang().toUpperCase()));
			
						
				// u91856 29/06/2011 Respostes que venen donades fixes
				Map param = (req==null)?null:(HashMap)req.getSession().getAttribute(Microfront.ENCPARAM);
				if (param == null){
					param = new HashMap();
				}
				else{ 
					req.getSession().removeAttribute(Microfront.ENCPARAM);	
				}
				
				//Comprobar si funciona correctamente
				model.addAttribute("MVS_mapRespostesFixades", param);
				
				//u91856 02/03/2012 Salvador Antich: Identificacion del usuario que contesta segun configuracion de la encuesta
				if (encuesta.getIdentificacion().equals("S")){
	    			try {
						SeyconPrincipal principal = null;
						principal = SeyconPrincipal.getCurrent();
						//principal = (SeyconPrincipal) request.getUserPrincipal();
						String identificacio = rb.getString("encuesta.identificacion").replaceAll("\\{1\\}",principal.getFullName());
						model.addAttribute("MVS_identificacion", identificacio);
					} catch (Exception e) {
						log.error("Error en la identificación del usuario en la encuesta: " + idEncuesta + " ---> " + e);
						return getForwardError(microsite, lang, model,
								ErrorMicrosite.ERROR_AMBIT_ACCES);
//						throw new Exception("Error en la identificación del usuario");
					}
	    		}else{
	    			//retorno.append(rb.getString("encuesta.anonima"));
	    		}
			

			cargarMollapan(microsite, model, lang, encuesta);

			return this.templateNameFactory.encuesta(microsite);

		} catch (DelegateException e) {
			log.error(e.getMessage());
			return getForwardError(microsite, lang, model,
					ErrorMicrosite.ERROR_AMBIT_PAGINA);
		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return getForwardError(microsite, lang, model,
					ErrorMicrosite.ERROR_AMBIT_MICRO);
		}
	}
	
	/**
	 * TODO: mkey debería ser el uri del site
	 * TODO: tipo debería ser el nemotecnico del tipo
	 * @param lang
	 * @param mkey
	 * @param model
	 * @return
	 */

	@RequestMapping(method=RequestMethod.POST,value="{mkey}/{lang}/envioencuesta/{encuesta}/")
	public String enviarEncuesta (
					@PathVariable("mkey") SiteId siteId, 
					@PathVariable("lang") Idioma lang,
					@PathVariable("encuesta") long idEncuesta,
					Model model,
					@RequestParam(value=Microfront.MCONT, required = false, defaultValue="") String mcont,
					@RequestParam(value=Microfront.PCAMPA, required = false, defaultValue="") String pcampa,					
					HttpServletRequest req) {
		
		boolean error = false;
		boolean entera = true;
		boolean ensesion = false;
		Microsite microsite = null;
	  	try {
	  		
	  		//sustitucion procesaSesion(); de BdEnvioEncuesta.java
	  		String idencuesta = "" + req.getParameter(Microfront.PCONT);
			String solover = "" + req.getParameter(Microfront.PVIEW);
			String vsesion = "" + req.getSession().getAttribute("MVS_encuestarellena" + idencuesta);
			if (vsesion.equals("yes")) ensesion=true;
			if (solover.equals("yes")) ensesion=true;
			//hasta aqui
			
		  	microsite =  super.loadMicrosite(siteId.mkey, lang, model, pcampa);
			Encuesta encuesta = this.encuestasDataService.getEncuestas(microsite, lang, idEncuesta);
			
			//Canvi Salvador 05/10/2010
			String encuestaRellena=(String)req.getSession().getAttribute("MVS_encuestarellena" + encuesta.getId().toString());
		  	String visita=(String)req.getSession().getAttribute("MVS_visita" + encuesta.getId().toString());
		  	String pview=(String)req.getParameter(Microfront.PVIEW);
		  	if (encuestaRellena!=null && (pview==null)){
		  		if (visita!=null && visita.equalsIgnoreCase("1VOT"))
		  			visita="NVOT";
		  		else if (visita!=null && visita.equalsIgnoreCase("NVOT"))
		  			visita="NVOT";
		  		else 
		  			visita="1VOT";
		  		req.getSession().setAttribute("MVS_visita" + encuesta.getId().toString(), visita);
		  		encuesta.setVotoDuplicado(visita);
		  	}
			
			if (!ensesion) {
			//sustitucion procesaformulario(); de BdEnvioencuesta.java
				if (es.caib.gusite.utilities.date.Fechas.vigente(encuesta.getFpublicacion(), encuesta.getFcaducidad())) {
					
					String cuerpomensaje="\n";
					EncuestaDelegate encuestadel = DelegateUtil.getEncuestaDelegate();
					RespuestaDatoDelegate resdatdel = DelegateUtil.getRespuestaDatoDelegate();
					
					Enumeration<?> paramNames = req.getParameterNames();
			
					//idioma
					String parametrofijo = "" + req.getParameter(Microfront.PLANG); 
					if (parametrofijo.equals(Microfront.PLANG)) 
				    	  cuerpomensaje+="Idioma = " + req.getParameter(parametrofijo) + "\n";			   
					
					//identificador de si es encuesta entera o componente 
					parametrofijo = "" + req.getParameter(Microfront.PENCCOMP);
					if (parametrofijo.equals("no")) entera=true;
						else entera=false;											

					UsuarioEncuesta usuario = new UsuarioEncuesta();
				    if (!"S".equals(encuesta.getIdentificacion())) 
				    	usuario.setNombre("Anonimo");
				    else{
				    	SeyconPrincipal principal = null;
			    		try{
			    			principal = SeyconPrincipal.getCurrent();
			    			usuario.setNombre(principal.getFullName());			    
					    	usuario.setDni(principal.getNif());
			    		}catch(Exception e){
			    			log.error("Error BdEnvioEncuesta (obteniendo identicacion Seycon): " + e);
			    		}			    	
				    }
				    
				    UsuarioPropietarioRespuesta upm = new UsuarioPropietarioRespuesta ();
				    UsuarioEncuestaDelegate encu = DelegateUtil.getUsuarioEncuestaDelegate();
				    
				    upm.getId().setIdusuario(encu.grabarUsuarioEncuesta(usuario));
				    			      
				    while (paramNames.hasMoreElements())  {
					      // Campos fijos que vienen del request: lang,idsite,cont,btnanar,enccomp. Evidentemente, no hay que tratarlos
				    	  String paramName = (String)paramNames.nextElement();
					      if (!paramName.equals(Microfront.PLANG) && !paramName.equals(Microfront.PIDSITE)
					    		  && !paramName.equals(Microfront.PCONT) && !paramName.equals(Microfront.PBTNANAR) 
					    		  && !paramName.equals(Microfront.PENCCOMP)) {
					    	  
					    	  String idpregunta = paramName.substring(1);
					    	  if (paramName.charAt(0)=='T') {
							    		  //es de tipo texto libre
								  	cuerpomensaje+= paramName + " = ";
								  	String[] paramValues = req.getParameterValues(paramName);
								  	String paramValue = paramValues[0];
								  	cuerpomensaje+= paramValue + "\n";
								  	
								  	//hay que hacer un split para separar respuesta y pregunta
								  	StringTokenizer st=new StringTokenizer(paramName,"_");
								    int n=st.countTokens();
								    String[] str= new String[n];
								    str[0]=st.nextToken();//aqui viene la respuesta, con la T delante
								    str[1]=st.nextToken();//aqui viene la pregunta
									String respuesta = str[0].substring(1);
									String pregunta = str[1];
									RespuestaDato resdat = new RespuestaDato();
									resdat.setDato(paramValue);
									resdat.setIdencuesta(encuesta.getId());
									resdat.setIdpregunta(new Long(pregunta));
									resdat.setIdrespueta(new Long(respuesta));
									resdat.setIdusuari(upm.getId().getIdusuario());
					    			if ((paramValue!=null) && (!paramValue.equals("null")))
					    				resdatdel.grabarRespuestaDato(resdat);
					    		  	
					    	  } else {
					    		  	cuerpomensaje+= paramName + " = ";
								    String[] paramValues = req.getParameterValues(paramName);
			
								    if (paramValues.length == 1) {
								        String paramValue = paramValues[0];
								
								        if (paramValue.length() == 0) {
								        	cuerpomensaje+="[sin valor]"+ "\n";
								        } else {
								        	cuerpomensaje+= paramValue + "\n";
									        encuestadel.sumarPregunta(new Long(idpregunta));
									        encuestadel.sumarRespuesta(new Long(paramValue));
										    upm.getId().setIdrespuesta(new Long(paramValue));
										    encuestadel.grabarUsuarioPropietarioRespuesta(upm);
									    }
							      } else  {
							    	  	encuestadel.sumarPregunta(new Long(idpregunta));
								        for (int i=0; i < paramValues.length; i++) {
								        	cuerpomensaje+= paramValues[i] + ", ";
								        	encuestadel.sumarRespuesta(new Long(paramValues[i]));
								        	upm.getId().setIdrespuesta(new Long(paramValues[i]));
										    encuestadel.grabarUsuarioPropietarioRespuesta(upm);
								        }
								        cuerpomensaje+="\n";
							      }
		      
					    	  }
					      }
				    }
				    //metemos el valor del idencuesta para no volver a rellenarla
				    req.getSession().setAttribute("MVS_encuestarellena" + encuesta.getId(), "yes");
				    log.debug("[aviso] Encuesta: " + cuerpomensaje );    
			}
			//hasta aqui procesaformulario();	
				
			//sustitucion recogerencuesta(); de BdEnvioencuesta.java	
				//comprobacion de microsite
				if (encuesta.getIdmicrosite().longValue()!=microsite.getId().longValue()) {
						log.error("El elemento solicitado no pertenece al site");
						error=true;
						return getForwardError (microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO);
				}
				//comprobacion de visibilidad
				if (!encuesta.getVisible().equals("S")) {
					log.error("El elemento solicitado no está visible");
					error=true;
					return getForwardError (microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_PAGINA);
				}	
				
				//traduce encuesta
				encuesta.setIdi(lang.getLang());
				Iterator<?> iter = encuesta.getPreguntas().iterator();
			    while (iter.hasNext()) {
			    	
			    	Pregunta pregunta = (Pregunta)iter.next();
			    	pregunta.setIdi(lang.getLang());
			    	
					Iterator<?> iter2 = pregunta.getRespuestas().iterator();
				    while (iter2.hasNext()) {
				    	
				    	Respuesta respuesta = (Respuesta)iter2.next();
				    	respuesta.setIdi(lang.getLang());
				    }
			    	
			    }
			    //hasta aqui traduce encuesta
	
			    
			 // si llegamos aqui.... todo ok, así que grabamos la estadistica
			    if (error==false) {
				if ("no".equals("" + req.getSession().getAttribute("MVS_stat"))) {
					log.info("Skip Estadistica, preview conten");
				}
			    }
				//hasta aqui recogerencuesta();
								
			} else {
				
				//sustitucion recogerencuesta(); de BdEnvioencuesta.java	
				//comprobacion de microsite
				if (encuesta.getIdmicrosite().longValue()!=microsite.getId().longValue()) {
						log.error("El elemento solicitado no pertenece al site");
						error=true;
						return getForwardError (microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO);
				}
				//comprobacion de visibilidad
				if (!encuesta.getVisible().equals("S")) {
					log.error("El elemento solicitado no está visible");
					error=true;
					return getForwardError (microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_PAGINA);
				}	
				
				//traduce encuesta
				encuesta.setIdi(lang.getLang());
				Iterator<?> iter = encuesta.getPreguntas().iterator();
			    while (iter.hasNext()) {
			    	
			    	Pregunta pregunta = (Pregunta)iter.next();
			    	pregunta.setIdi(lang.getLang());
			    	
					Iterator<?> iter2 = pregunta.getRespuestas().iterator();
				    while (iter2.hasNext()) {
				    	
				    	Respuesta respuesta = (Respuesta)iter2.next();
				    	respuesta.setIdi(lang.getLang());
				    }
			    	
			    }
			    //hasta aqui traduce encuesta
	
			    
			 // si llegamos aqui.... todo ok, así que grabamos la estadistica
			    if (error==false) {
				if ("no".equals("" + req.getSession().getAttribute("MVS_stat"))) {
					log.info("Skip Estadistica, preview conten");
				}
			    }
				//hasta aqui recogerencuesta();
				
			}

			req.setAttribute("MVS_encuesta_entera",((entera==true)?"entera":"componente"));
			model.addAttribute("MVS_encuesta",encuesta);

		    cargarMollapan(microsite, model, lang, encuesta);
		

		    return this.templateNameFactory.envioEncuesta(microsite);

	
        } catch (DelegateException e) {
        	log.error(e.getMessage());
        	return getForwardError (microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_PAGINA);
        } catch (ExceptionFrontMicro e) {
        	log.error(e.getMessage());
        	return getForwardError (microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO);
		}      

	}

	protected boolean estaEnMantenimiento() {
		String mantenimiento = System
				.getProperty("es.caib.gusite.mantenimiento");
		return null != mantenimiento && "si".equalsIgnoreCase(mantenimiento);
	}

	
	/**
	 * TODO: mkey debería ser el uri del site
	 * TODO: tipo debería ser el nemotecnico del tipo
	 * @param lang
	 * @param mkey
	 * @param model
	 * @return
	 */
	//@RequestMapping(method = {RequestMethod.POST, RequestMethod.GET},value="{mkey}/{lang}/envioencuesta/{encuesta}/")
	@RequestMapping(method=RequestMethod.GET,value="{mkey}/{lang}/resultados/{encuesta}/")
	public String resultadoEncuestas (
					@PathVariable("mkey") SiteId siteId, 
					@PathVariable("lang") Idioma lang,
					@PathVariable("encuesta") long idEncuesta,
					Model model,
					@RequestParam(value=Microfront.MCONT, required = false, defaultValue="") String mcont,
					@RequestParam(value=Microfront.PCAMPA, required = false, defaultValue="") String pcampa,					
					HttpServletRequest req) {
		
		
		boolean error = false;
		boolean entera = true;
		boolean ensesion = true;
		Microsite microsite = null;
	  	try {
	  		
	  		//sustitucion procesaSesion(); de BdEnvioEncuesta.java
	  		String idencuesta = "" + req.getParameter(Microfront.PCONT);
			String solover = "" + req.getParameter(Microfront.PVIEW);
			String vsesion = "" + req.getSession().getAttribute("MVS_encuestarellena" + idencuesta);
			if (vsesion.equals("yes")) ensesion=true;
			if (solover.equals("yes")) ensesion=true;
			//hasta aqui
			
		  	microsite =  super.loadMicrosite(siteId.mkey, lang, model, pcampa);
			Encuesta encuesta = this.encuestasDataService.getEncuestas(microsite, lang, idEncuesta);
			
			//Canvi Salvador 05/10/2010
			String encuestaRellena=(String)req.getSession().getAttribute("MVS_encuestarellena" + encuesta.getId().toString());
		  	String visita=(String)req.getSession().getAttribute("MVS_visita" + encuesta.getId().toString());
		  	String pview=(String)req.getParameter(Microfront.PVIEW);
		  	if (encuestaRellena!=null && (pview==null)){
		  		if (visita!=null && visita.equalsIgnoreCase("1VOT"))
		  			visita="NVOT";
		  		else if (visita!=null && visita.equalsIgnoreCase("NVOT"))
		  			visita="NVOT";
		  		else 
		  			visita="1VOT";
		  		req.getSession().setAttribute("MVS_visita" + encuesta.getId().toString(), visita);
		  		encuesta.setVotoDuplicado(visita);
		  	}
			
			if (!ensesion) {
			//sustitucion procesaformulario(); de BdEnvioencuesta.java
				if (es.caib.gusite.utilities.date.Fechas.vigente(encuesta.getFpublicacion(), encuesta.getFcaducidad())) {
					
					String cuerpomensaje="\n";
					EncuestaDelegate encuestadel = DelegateUtil.getEncuestaDelegate();
					RespuestaDatoDelegate resdatdel = DelegateUtil.getRespuestaDatoDelegate();
					
					Enumeration<?> paramNames = req.getParameterNames();
			
					//idioma
					String parametrofijo = "" + req.getParameter(Microfront.PLANG); 
					if (parametrofijo.equals(Microfront.PLANG)) 
				    	  cuerpomensaje+="Idioma = " + req.getParameter(parametrofijo) + "\n";			   
					
					//identificador de si es encuesta entera o componente 
					parametrofijo = "" + req.getParameter(Microfront.PENCCOMP);
					if (parametrofijo.equals("no")) entera=true;
						else entera=false;											

					UsuarioEncuesta usuario = new UsuarioEncuesta();
				    if (!"S".equals(encuesta.getIdentificacion())) 
				    	usuario.setNombre("Anonimo");
				    else{
				    	SeyconPrincipal principal = null;
			    		try{
			    			principal = SeyconPrincipal.getCurrent();
			    			usuario.setNombre(principal.getFullName());			    
					    	usuario.setDni(principal.getNif());
			    		}catch(Exception e){
			    			log.error("Error BdEnvioEncuesta (obteniendo identicacion Seycon): " + e);
			    		}			    	
				    }
				    
				    UsuarioPropietarioRespuesta upm = new UsuarioPropietarioRespuesta ();
				    UsuarioEncuestaDelegate encu = DelegateUtil.getUsuarioEncuestaDelegate();
				    
				    upm.getId().setIdusuario(encu.grabarUsuarioEncuesta(usuario));
				    			      
				    while (paramNames.hasMoreElements())  {
					      // Campos fijos que vienen del request: lang,idsite,cont,btnanar,enccomp. Evidentemente, no hay que tratarlos
				    	  String paramName = (String)paramNames.nextElement();
					      if (!paramName.equals(Microfront.PLANG) && !paramName.equals(Microfront.PIDSITE)
					    		  && !paramName.equals(Microfront.PCONT) && !paramName.equals(Microfront.PBTNANAR) 
					    		  && !paramName.equals(Microfront.PENCCOMP)) {
					    	  
					    	  String idpregunta = paramName.substring(1);
					    	  if (paramName.charAt(0)=='T') {
							    		  //es de tipo texto libre
								  	cuerpomensaje+= paramName + " = ";
								  	String[] paramValues = req.getParameterValues(paramName);
								  	String paramValue = paramValues[0];
								  	cuerpomensaje+= paramValue + "\n";
								  	
								  	//hay que hacer un split para separar respuesta y pregunta
								  	StringTokenizer st=new StringTokenizer(paramName,"_");
								    int n=st.countTokens();
								    String[] str= new String[n];
								    str[0]=st.nextToken();//aqui viene la respuesta, con la T delante
								    str[1]=st.nextToken();//aqui viene la pregunta
									String respuesta = str[0].substring(1);
									String pregunta = str[1];
									RespuestaDato resdat = new RespuestaDato();
									resdat.setDato(paramValue);
									resdat.setIdencuesta(encuesta.getId());
									resdat.setIdpregunta(new Long(pregunta));
									resdat.setIdrespueta(new Long(respuesta));
									resdat.setIdusuari(upm.getId().getIdusuario());
					    			if ((paramValue!=null) && (!paramValue.equals("null")))
					    				resdatdel.grabarRespuestaDato(resdat);
					    		  	
					    	  } else {
					    		  	cuerpomensaje+= paramName + " = ";
								    String[] paramValues = req.getParameterValues(paramName);
			
								    if (paramValues.length == 1) {
								        String paramValue = paramValues[0];
								
								        if (paramValue.length() == 0) {
								        	cuerpomensaje+="[sin valor]"+ "\n";
								        } else {
								        	cuerpomensaje+= paramValue + "\n";
									        encuestadel.sumarPregunta(new Long(idpregunta));
									        encuestadel.sumarRespuesta(new Long(paramValue));
										    upm.getId().setIdrespuesta(new Long(paramValue));
										    encuestadel.grabarUsuarioPropietarioRespuesta(upm);
									    }
							      } else  {
							    	  	encuestadel.sumarPregunta(new Long(idpregunta));
								        for (int i=0; i < paramValues.length; i++) {
								        	cuerpomensaje+= paramValues[i] + ", ";
								        	encuestadel.sumarRespuesta(new Long(paramValues[i]));
								        	upm.getId().setIdrespuesta(new Long(paramValues[i]));
										    encuestadel.grabarUsuarioPropietarioRespuesta(upm);
								        }
								        cuerpomensaje+="\n";
							      }
		      
					    	  }
					      }
				    }
				    //metemos el valor del idencuesta para no volver a rellenarla
				    req.getSession().setAttribute("MVS_encuestarellena" + encuesta.getId(), "yes");
				    log.debug("[aviso] Encuesta: " + cuerpomensaje );    
			}
			//hasta aqui procesaformulario();	
				
			//sustitucion recogerencuesta(); de BdEnvioencuesta.java	
				//comprobacion de microsite
				if (encuesta.getIdmicrosite().longValue()!=microsite.getId().longValue()) {
						log.error("El elemento solicitado no pertenece al site");
						error=true;
						return getForwardError (microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO);
				}
				//comprobacion de visibilidad
				if (!encuesta.getVisible().equals("S")) {
					log.error("El elemento solicitado no está visible");
					error=true;
					return getForwardError (microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_PAGINA);
				}	
				
				//traduce encuesta
				encuesta.setIdi(lang.getLang());
				Iterator<?> iter = encuesta.getPreguntas().iterator();
			    while (iter.hasNext()) {
			    	
			    	Pregunta pregunta = (Pregunta)iter.next();
			    	pregunta.setIdi(lang.getLang());
			    	
					Iterator<?> iter2 = pregunta.getRespuestas().iterator();
				    while (iter2.hasNext()) {
				    	
				    	Respuesta respuesta = (Respuesta)iter2.next();
				    	respuesta.setIdi(lang.getLang());
				    }
			    	
			    }
			    //hasta aqui traduce encuesta
	
			    
			 // si llegamos aqui.... todo ok, así que grabamos la estadistica
			    if (error==false) {
				if ("no".equals("" + req.getSession().getAttribute("MVS_stat"))) {
					log.info("Skip Estadistica, preview conten");
				}
			    }
				//hasta aqui recogerencuesta();
								
			} else {
				
				//sustitucion recogerencuesta(); de BdEnvioencuesta.java	
				//comprobacion de microsite
				if (encuesta.getIdmicrosite().longValue()!=microsite.getId().longValue()) {
						log.error("El elemento solicitado no pertenece al site");
						error=true;
						return getForwardError (microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO);
				}
				//comprobacion de visibilidad
				if (!encuesta.getVisible().equals("S")) {
					log.error("El elemento solicitado no está visible");
					error=true;
					return getForwardError (microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_PAGINA);
				}	
				
				//traduce encuesta
				encuesta.setIdi(lang.getLang());
				Iterator<?> iter = encuesta.getPreguntas().iterator();
			    while (iter.hasNext()) {
			    	
			    	Pregunta pregunta = (Pregunta)iter.next();
			    	pregunta.setIdi(lang.getLang());
			    	
					Iterator<?> iter2 = pregunta.getRespuestas().iterator();
				    while (iter2.hasNext()) {
				    	
				    	Respuesta respuesta = (Respuesta)iter2.next();
				    	respuesta.setIdi(lang.getLang());
				    }
			    	
			    }
			    //hasta aqui traduce encuesta
	
			    
			 // si llegamos aqui.... todo ok, así que grabamos la estadistica
			    if (error==false) {
				if ("no".equals("" + req.getSession().getAttribute("MVS_stat"))) {
					log.info("Skip Estadistica, preview conten");
				}
			    }
				//hasta aqui recogerencuesta();
			   
			}

			req.setAttribute("MVS_encuesta_entera",((entera==true)?"entera":"componente"));
			model.addAttribute("MVS_encuesta",encuesta);

		    cargarMollapan(microsite, model, lang, encuesta);
		
		    

		    return this.templateNameFactory.envioEncuesta(microsite);
		 
	
        } catch (DelegateException e) {
        	log.error(e.getMessage());
        	return getForwardError (microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_PAGINA);
        } catch (ExceptionFrontMicro e) {
        	log.error(e.getMessage());
        	return getForwardError (microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO);
		}      

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

	private void cargarMollapan(Microsite microsite, Model model, Idioma lang,
			Encuesta encuesta) {

		List<PathItem> path = super.getBasePath(microsite, model, lang);

		String titulo = ((TraduccionEncuesta)encuesta.getTraduccion(encuesta.getIdi())).getTitulo();

		path.add(new PathItem(titulo, this.urlFactory.encuesta(microsite, lang,
				encuesta)));

		// Datos para la plantilla
		model.addAttribute("MVS2_pathdata", path);

	}

	@Override
	public String setServicio() {
		// TODO Auto-generated method stub
		return Microfront.RENCUESTA;
	}

}
