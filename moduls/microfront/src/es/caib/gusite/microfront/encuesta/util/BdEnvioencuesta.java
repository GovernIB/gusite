package es.caib.gusite.microfront.encuesta.util;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import es.caib.gusite.utilities.auth.CertsPrincipal;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.gusite.microfront.base.bean.ErrorMicrosite;
import es.caib.gusite.microfront.Microfront;
import es.caib.gusite.microfront.base.Bdbase;
import es.caib.gusite.microfront.estadistica.util.StatManager;
import es.caib.gusite.micromodel.Encuesta;
import es.caib.gusite.micromodel.Estadistica;
import es.caib.gusite.micromodel.Pregunta;
import es.caib.gusite.micromodel.Respuesta;
import es.caib.gusite.micromodel.RespuestaDato;
import es.caib.gusite.micromodel.UsuarioEncuesta;
import es.caib.gusite.micromodel.UsuarioPropietarioRespuesta;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.EncuestaDelegate;
import es.caib.gusite.micropersistence.delegate.RespuestaDatoDelegate;
import es.caib.gusite.micropersistence.delegate.UsuarioEncuestaDelegate;

public class BdEnvioencuesta  extends Bdbase {

	protected static Log log = LogFactory.getLog(BdEnvioencuesta.class);
	
	private HttpServletRequest req;
	private boolean error = false;
	private boolean entera = true;
	//private Long idencuesta = new Long(0);
	private Encuesta encuesta = new Encuesta();
	private boolean ensesion = false;
	
	public void finalize() {
		this.dispose();
	}
	
	/**
	 * Metodo público que borra las variables.
	 */
	public void dispose() {
		//idencuesta = null;
		encuesta = null;
		req = null;
		super.dispose();
	}
	
	/**
	 *  Constructor de la clase, recoge el formulario de encuesta, 
	 *  graba la información de la encuesta y la recoge (con los resultados) para enviarla al usuario.
	 * @param request
	 * @throws Exception
	 */
	public BdEnvioencuesta(HttpServletRequest request) throws Exception {
		super(request);
		req = request;
		try {
			
			procesaSesion();
			if (!ensesion) {
				procesaformulario();
				recogerencuesta();				
			} else { 
				recogerencuesta();
			}
			
		} catch (Exception e) {
			log.error(e.getMessage());
			error=true;
			beanerror.setAviso("Error");
			beanerror.setMensaje("Se ha producido un error desconocido en el envio del formulario de encuesta.");
		}
	}	
	
	/**
	 *  Método privado que procesa la sesión. Averigua si el usuario ha rellenado la encuesta en sesión.
	 */
	private void procesaSesion() {
		try {
			String idencuesta = "" + req.getParameter(Microfront.PCONT);
			String solover = "" + req.getParameter(Microfront.PVIEW);
			String vsesion = "" + req.getSession().getAttribute("MVS_encuestarellena" + idencuesta);
			if (vsesion.equals("yes")) ensesion=true;
			if (solover.equals("yes")) ensesion=true;

			EncuestaDelegate encuestadel = DelegateUtil.getEncuestaDelegate();
			Long idcont = new Long(Long.parseLong(idencuesta));
			encuesta = encuestadel.obtenerEncuesta(idcont);
			
		} catch (Exception e) {
			log.warn("Error averiguando si el usario ha rellenado la encuesta en sesion.");
			ensesion=false;
		}
	}
	
	/**
	 * Recoge una encuesta a partir del id de la encuesta
	 * 
	 */
	private void recogerencuesta(){
		try {			
			//comprobacion de microsite
			if (!(encuesta.getIdmicrosite().longValue()==idsite.longValue())) {
					beanerror.setAviso("Aviso");
					beanerror.setMensaje("El elemento solicitado no pertenece al site");
					error=true;
			}
			//comprobacion de visibilidad
			if (!encuesta.getVisible().equals("S")) {
					beanerror.setAviso("Aviso");
					beanerror.setMensaje("El elemento solicitado no pertenece al site");
					error=true;
			}			

			traduceencuesta();
			
			//si llegamos aqui.... todo ok, así que grabamos la estadistica
			if (!error) {
				if ("no".equals(""+req.getSession().getAttribute("MVS_stat"))) log.info("Skip Estadistica, preview conten"); 
				 		else req.getSession().getServletContext().setAttribute("bufferStats", 
								StatManager.grabarestadistica(encuesta, super.publico, 
										(List<Estadistica>) req.getSession().getServletContext().getAttribute("bufferStats")));
			}
		} catch (Exception e) {
			log.error(e.getMessage());
            beanerror = new ErrorMicrosite("Error", "Se ha producido un error desconocido al recuperar la pagina solicitada.");
			error=true;
		}
		
	}	
	
	private void traduceencuesta() {
		encuesta.setIdi(idioma);
		Iterator<?> iter = encuesta.getPreguntas().iterator();
	    while (iter.hasNext()) {
	    	
	    	Pregunta pregunta = (Pregunta)iter.next();
	    	pregunta.setIdi(idioma);
	    	
			Iterator<?> iter2 = pregunta.getRespuestas().iterator();
		    while (iter2.hasNext()) {
		    	
		    	Respuesta respuesta = (Respuesta)iter2.next();
		    	respuesta.setIdi(idioma);
		    }
	    	
	    }
	}		
	
	/**
	 * Método privado. 	
	 * El formulario no lo tenemos en ninguna clase 'form', así que
	 * recorreremos todos los parametros que se han enviado por el
	 * request.
	 * De esta forma podremos procesar el formulario. Y grabar la encuesta.
	 * @throws Exception
	 */
	private void procesaformulario() throws Exception {
				
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
			    	CertsPrincipal principal = null;
		    		try{
		    			principal = CertsPrincipal.getCurrent();
						if (principal != null) {
							usuario.setNombre(principal.getFullName());
							usuario.setDni(principal.getNif());
						} else {
							usuario.setNombre(req.getUserPrincipal().getName());
							//usuario.setDni(principal.getNif());
						}
		    		}catch(Exception e){
		    			log.error("Error BdEnvioEncuesta (obteniendo identicacion): " + e);
		    			throw new Exception("Error BdEnvioEncuesta (obteniendo identicacion)");
		    		}			    	
			    }
			    
			    UsuarioPropietarioRespuesta upm = new UsuarioPropietarioRespuesta ();
			    UsuarioEncuestaDelegate encu = DelegateUtil.getUsuarioEncuestaDelegate();
			    
			    upm.getId().setIdusuario(encu.grabarUsuarioEncuesta(usuario));
			    	
			    String idPreguntaAux = "";
			    String anterior = "";
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
								idpregunta = str[1];
								RespuestaDato resdat = new RespuestaDato();
								resdat.setDato(paramValue);
								resdat.setIdencuesta(encuesta.getId());
								resdat.setIdpregunta(new Long(idpregunta));
								resdat.setIdrespueta(new Long(respuesta));
								resdat.setIdusuari(upm.getId().getIdusuario());
				    			if ((!paramValue.isEmpty()) && (paramValue!=null) && (!paramValue.equals("null"))){				    				
				    				resdatdel.grabarRespuestaDato(resdat);
				    				
				    				if(StringUtils.isEmpty(anterior) || anterior.charAt(0) != 'R'){		//El anterior no es radio(mono) asociado a textarea de usuario										
				    					idPreguntaAux = str[1];
				    					encuestadel.sumarRespuesta(new Long(respuesta));
									}
				    			}
				    	  } else {
				    		  	cuerpomensaje+= paramName + " = ";
							    String[] paramValues = req.getParameterValues(paramName);
		
							    if (paramValues.length == 1) {
							        String paramValue = paramValues[0];
							
							        if (paramValue.length() == 0) {
							        	cuerpomensaje+="[sin valor]"+ "\n";
							        } else {
							        	cuerpomensaje+= paramValue + "\n";
								       // encuestadel.sumarPregunta(new Long(idpregunta));
								        if(idPreguntaAux.compareTo(idpregunta) != 0){										
											encuestadel.sumarPregunta(new Long(idpregunta));
											idPreguntaAux = idpregunta;
										}
								        encuestadel.sumarRespuesta(new Long(paramValue));
									    upm.getId().setIdrespuesta(new Long(paramValue));
									    encuestadel.grabarUsuarioPropietarioRespuesta(upm);
								    }
						      } else  {
						    	  	//encuestadel.sumarPregunta(new Long(idpregunta));
						    	  	if(idPreguntaAux.compareTo(idpregunta) != 0){										
										encuestadel.sumarPregunta(new Long(idpregunta));
										idPreguntaAux = idpregunta;
									}
							        for (int i=0; i < paramValues.length; i++) {
							        	cuerpomensaje+= paramValues[i] + ", ";
							        	encuestadel.sumarRespuesta(new Long(paramValues[i]));
							        	upm.getId().setIdrespuesta(new Long(paramValues[i]));
									    encuestadel.grabarUsuarioPropietarioRespuesta(upm);
							        }
							        cuerpomensaje+="\n";
						      }
	      
				    	  }
				    	  anterior = paramName;
				      }
			    }
			    Encuesta encuestaAux = encuestadel.obtenerEncuesta(encuesta.getId());
    			encuesta.setPreguntas(encuestaAux.getPreguntas());
			    //metemos el valor del idencuesta para no volver a rellenarla
			    req.getSession().setAttribute("MVS_encuestarellena" + encuesta.getId(), "yes");
			    log.debug("[aviso] Encuesta: " + cuerpomensaje );    
		}
	}	
	
	public boolean isError() {
		return error;
	}
	
	/**
	 * Implementacion del método abstracto.
	 * Se le indica que estamos en el servicio de contacto.
	 */
	public String setServicio() {
		return Microfront.RENCUESTA;
	}

	public boolean isEntera() {
		return entera;
	}

	public Long getIdencuesta() {
		return encuesta.getId();
	}

	public Encuesta getEncuesta() {
		return encuesta;
	}	
	
}
