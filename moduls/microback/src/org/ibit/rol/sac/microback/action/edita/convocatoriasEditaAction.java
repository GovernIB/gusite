package org.ibit.rol.sac.microback.action.edita;


import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.ibit.rol.sac.microback.action.BaseAction;
import org.ibit.rol.sac.microback.actionform.formulario.convocatoriaForm;
import org.ibit.rol.sac.microback.base.Base;
import org.ibit.rol.sac.microback.utils.CorreoService;
import org.ibit.rol.sac.micromodel.Convocatoria;
import org.ibit.rol.sac.micromodel.Correo;
import org.ibit.rol.sac.micromodel.DistribucionConvocatoria;
import org.ibit.rol.sac.micromodel.Encuesta;
import org.ibit.rol.sac.micromodel.Idioma;
import org.ibit.rol.sac.micromodel.ListaDistribucion;
import org.ibit.rol.sac.micromodel.Microsite;
import org.ibit.rol.sac.micromodel.Pregunta;
import org.ibit.rol.sac.micromodel.Respuesta;
import org.ibit.rol.sac.micromodel.TraduccionEncuesta;
import org.ibit.rol.sac.micromodel.TraduccionPregunta;
import org.ibit.rol.sac.micromodel.TraduccionRespuesta;
import org.ibit.rol.sac.micropersistence.delegate.ConvocatoriaDelegate;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;
import org.ibit.rol.sac.micropersistence.delegate.EncuestaDelegate;
import org.ibit.rol.sac.micropersistence.delegate.LDistribucionDelegate;

/**
 * Action que edita los convocatorias de un microsite <BR>
 * <P>
 * 	Definición Struts:<BR>
 *  action path="/convocatoriaEdita" <BR> 
 *  name="convocatoriaForm" <BR> 
 *  input="/convocatoriasAcc.do"  <BR>
 *	scope="session" <BR>
 *  unknown="false" <BR>
 *  forward name="detalle" path="/detalleConvocatoria.jsp" <BR>
 *  forward name="info" path="/infoConvocatoria.jsp"
 *  
 *  @author Salvador Antich
 */
public class convocatoriasEditaAction extends BaseAction 
{

	protected static Log log = LogFactory.getLog(convocatoriasEditaAction.class);
	convocatoriaForm convForm;
	ConvocatoriaDelegate convDelegate;

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		convForm = (convocatoriaForm) form;
		convDelegate = DelegateUtil.getConvocatoriaDelegate(); 
		
        //********************************************************
        //*********** EDICION / CREACION DE LA ENCUESTA **********
        //********************************************************

    	if ((String)request.getParameter("accion") != null) {

			//Volcamos los datos del formulario al Bean de convocatoria			    		
			if (request.getParameter("accion").equals("CrearConv")){
				request.setAttribute("destinatarios", new ArrayList());
				convForm.resetForm();
				return mapping.findForward("detalle");
				
			}else if (request.getParameter("accion").equals("GuardarConv")) {
				Convocatoria conv;
				try{
					conv = guardar(form, request);
				}catch(Exception e){
					addMessage(request, "peticion.error");
		            return mapping.findForward("info");
				}	         		
		       	if (convForm.getId() != null) 
		       		addMessage(request, "mensa.modifconvocatoria");
		       	else
		       		addMessage(request, "mensa.nuevaconvocatoria");
	       		addMessage(request, "mensa.editarconvocatoria", "" + conv.getId());
	           	addMessage(request, "mensa.listaconvocatorias");
		       	return mapping.findForward("info");
		       	
			} else if(request.getParameter("accion").equals("BorrarConv")) {
	    		convDelegate.borrarConvocatoria(convForm.getId());	
	       		addMessage(request, "mensa.listaconvborradas", "" + convForm.getId());
	           	addMessage(request, "mensa.listaconvocatorias");
	           	
	           	return mapping.findForward("info");	  
	           	
	        } else if(request.getParameter("accion").equals("enviarMailing")) {
	        	convocatoriaForm convForm = null;
	        	Convocatoria conv = null;
	        	Integer correus_enviats = new Integer(0);
	        	try{
		        	// Enviar Mailing	        		
	        		convForm = (convocatoriaForm) form;
		        	ConvocatoriaDelegate convDelegate = DelegateUtil.getConvocatoriaDelegate();
		        	LDistribucionDelegate distribDelegate = DelegateUtil.getLlistaDistribucionDelegate();
		        	
		        	// Actualitzam la data del darrer envio
		        	conv = convDelegate.obtenerConvocatoria(new Long(request.getParameter("id")));
		        	conv.setUltimoEnvio(new Date(System.currentTimeMillis()));
		        	convDelegate.grabarConvocatoria(conv);
		        	
		        	// Calculam els destinataris que no han contestat (de les llistes de distribucio + altres destinataris)
		        	Integer numIntentos = (conv.getEnvioSiError())? 0 : new Integer(System.getProperty("es.caib.gusite.intentosMailing"));
		        	HashSet hs = new HashSet(); // Perque no hi hagi repetits
		        	hs.addAll(convDelegate.listarCorreosEnvio(convForm.getId(), numIntentos));
		        	if (conv.getOtrosDestinatarios() != null){
			        	for(String correo:conv.getOtrosDestinatarios().split(",")){
			        		Iterator it = convDelegate.listarCorreosEnvio(convForm.getId(), numIntentos, correo).iterator();			        		
			        		if (it.hasNext()) hs.add(it.next()); 
			        	}
		        	}
		        	
		        	// Enviam els correus
		        	CorreoService mailing = new CorreoService();
		        	mailing.clearInforme();
			        for(Object o:hs){			        	
			        	Correo correo = distribDelegate.consultaCorreo((String)o);
			        	String nomUsuari = (correo.getNombre()!=null? correo.getNombre():"");
			        	String cognomUsuari = (correo.getApellidos()!=null? correo.getApellidos():"");
			        	StringBuffer sb = new StringBuffer().append(conv.getTextoMsg()
			        			.replace("{nom}", nomUsuari)
			        			.replace("{cognom}", cognomUsuari)
			        			.replace("{correu}", (String)o));
			        	mailing.initCorreo(correo.getCorreo(), conv.getAsuntoMsg(), true, sb);
			        	mailing.enviarCorreo();
			        }
			        
		        	// Actualitzam els correus amb els errors			        
		        	for(String s:mailing.getInforme().keySet()){
		        		Correo mail = distribDelegate.consultaCorreo(s);
		        		// Per cada correu que no s'hagi enviat bé, actualitzar el correu amb l'error
		        		if("ok".equals(mailing.getInforme().get(s))){
		        			mail.setIntentoEnvio(0);
		        			correus_enviats++;
		        		}else{
		        			mail.setIntentoEnvio(mail.getIntentoEnvio()!=null?mail.getIntentoEnvio() + 1:1);
		        			String error = mailing.getInforme().get(s);
		        			mail.setTraceError(error.substring(0, Math.min(254,error.length())));		        			
		        		}
		        		mail.setUltimoEnvio(new Timestamp(System.currentTimeMillis()));
	        			distribDelegate.actualizaCorreo(mail);
		        	}

	        	}catch(Exception e){
	        		addMessage(request, "peticion.error");
		            return mapping.findForward("info");
	        	}
	        	addMessage(request, "mensa.mailing", "" + conv.getNombre());
	        	addMessage(request, "mensa.correus.enviats",correus_enviats);
	           	addMessage(request, "mensa.listaconvocatorias");
	           	
	           	return mapping.findForward("info");
	           	
	        }else if (request.getParameter("accion").equals("detalleEncuestas")) {	//Cridat des de AJAX
	        	
				//Devolvemos el desplegable con las convocatorias
				boolean primer = true;
				StringBuffer sb = new StringBuffer();
				sb.append("[");
				if (request.getParameter("id")!=null && !"".equals(request.getParameter("id"))) {
					Long idMicro = new Long(request.getParameter("id"));
				
					EncuestaDelegate encDel = DelegateUtil.getEncuestaDelegate();
					encDel.init(idMicro);					
					for(Object enc:encDel.listarEncuestas()){
						Encuesta encuesta = encDel.obtenerEncuesta(((Encuesta)enc).getId());
						if(encuestaElegible(encuesta)){
							if (!primer) sb.append(",");
							primer = false;
							String txtEnc = ((TraduccionEncuesta)((Encuesta)enc).getTraduccionMap().get(Idioma.DEFAULT)).getTitulo();
							String codEnc = ((Encuesta)enc).getId().toString();
							sb.append("{\"codi\":\"" + codEnc + "\",\"texte\":\"" + txtEnc + "\"}");							
						}
					}
				}
				sb.append("]");
									
				response.setContentLength(sb.length());
	            response.getWriter().write(sb.toString());
	            response.setContentType("plain/text");
	            return null;
	            
			} else if (request.getParameter("accion").equals("detalleResCorreo")) { //Cridat des de AJAX
				//Devolvemos el desplegable con las preguntas de correo
				boolean primer = true;

				StringBuffer sb = new StringBuffer();
				sb.append("[");
				if (request.getParameter("id")!=null && !"".equals(request.getParameter("id"))){ 
					Long idConvocatoria = new Long(request.getParameter("id"));
					EncuestaDelegate encDel = DelegateUtil.getEncuestaDelegate();	
					for(Object pre:encDel.listarPreguntas(idConvocatoria)){
						Pregunta pregunta = encDel.obtenerPregunta(((Pregunta)pre).getId()); 
						for(Object res:DelegateUtil.getEncuestaDelegate().listarRespuestas(((Pregunta)pre).getId())){
							Respuesta respuesta = encDel.obtenerRespuesta(((Respuesta)res).getId());
							if ("N".equals(pregunta.getMultiresp()) && "I".equals(respuesta.getTipo())){ // Respuesta simple libre
								if (!primer) sb.append(",");
								primer = false;						
								String txtResp = ((TraduccionRespuesta)respuesta.getTraduccionMap().get(Idioma.DEFAULT)).getTitulo();
								String codResp = respuesta.getId().toString();
								sb.append("{\"codi\":\"" + codResp + "\",\"texte\":\"" + txtResp + "\"}");
							}
						}
					}
				}
				sb.append("]");
									
				response.setContentLength(sb.length());
	            response.getWriter().write(sb.toString());
	            response.setContentType("plain/text");
	            return null;
				
			} else if (request.getParameter("accion").equals("detallePreConfirmacion")) { //Cridat des de AJAX
				//Devolvemos el desplegable con las preguntas de confirmacion
				boolean primer = true;
				Pregunta pre;
				StringBuffer sb = new StringBuffer();
				sb.append("[");
				if (request.getParameter("id")!=null && !"".equals(request.getParameter("id"))){ 
					Long idEncuesta = new Long(request.getParameter("id"));
					
					for(Object o:DelegateUtil.getEncuestaDelegate().listarPreguntas(idEncuesta)){
						pre = (Pregunta)o;
						if (preguntaElegible(pre,"N","N")){ // Respuesta simple tipada
							if (!primer) sb.append(",");
							primer = false;
							sb.append("{\"codi\":\"" + pre.getId().toString() + "\",\"texte\":\"" + ((TraduccionPregunta)pre.getTraduccionMap().get(Idioma.DEFAULT)).getTitulo() + "\"}");
						}
					}
				}
				sb.append("]");
									
				response.setContentLength(sb.length());
	            response.getWriter().write(sb.toString());
	            response.setContentType("plain/text");
	            return null;
	            
			} else if (request.getParameter("accion").equals("detalleResConfirmacion")) { //Cridat des de AJAX
				//Devolvemos el desplegable con las respuestas de confirmacion
				boolean primer = true; 
				Respuesta res;
				StringBuffer sb = new StringBuffer();
				sb.append("[");
				if (request.getParameter("id")!=null && !"".equals(request.getParameter("id"))){
					Long idPregunta = new Long(request.getParameter("id"));
				
					for(Object o:DelegateUtil.getEncuestaDelegate().listarRespuestas(idPregunta)){
						res = (Respuesta)o;
						if (res.getTipo().equals("N")){
							if (!primer) sb.append(",");
							primer = false;						
							sb.append("{\"codi\":\"" + res.getId().toString() + "\",\"texte\":\"" + ((TraduccionRespuesta)res.getTraduccionMap().get(Idioma.DEFAULT)).getTitulo() + "\"}");
						}
					}
				}
				sb.append("]");
									
				response.setContentLength(sb.length());
	            response.getWriter().write(sb.toString());
	            response.setContentType("plain/text");
	            return null;
	            
			}
		} else if (request.getParameter("id")!=null) { 
        	try{
        		recuperar(form, request);	                
        	}catch(Exception e){
        		addMessage(request, "peticion.error");
                return mapping.findForward("info");
        	}
            return mapping.findForward("detalle");
            
        } 

		//request.setAttribute("listaDocs", convocatoriaDelegate.listarDocumentos(micrositeBean.getId().toString(), ""+convocatoria.getId()));
		//request.getSession().setAttribute("migapan", convocatoriaDelegate.migapan(""+convocatoria.getIdmenu(),convocatoria.getId()) );
		//vrs: anyadido para saber migapan de la url
		//request.setAttribute("MVS_HS_URL_migapan",hashMigaPan(convocatoria));
		
		//Refresco de parámetro MVS de menú
		Base.menuRefresh(request);

		return mapping.findForward("detalle");    
	}

	private boolean encuestaElegible(Encuesta encuesta){		
		boolean valPreCorreo = false;
		boolean valPreConfirmacion = false;
		int numRespConfirmacion;
		
		for(Iterator itPre = encuesta.getPreguntas().iterator();itPre.hasNext();){
			Pregunta pre = (Pregunta)itPre.next();
			if(pre.getMultiresp().equals("N")){
				for(Iterator itRes = pre.getRespuestas().iterator();itRes.hasNext() && !valPreCorreo;){
					Respuesta res = (Respuesta)itRes.next();
					if(res.getTipo().equals("I")){
						valPreCorreo = true;
					}
				}
				numRespConfirmacion = 0;
				for(Iterator itRes = pre.getRespuestas().iterator();itRes.hasNext() && !valPreConfirmacion;){
					Respuesta res = (Respuesta)itRes.next();
					if(res.getTipo().equals("N"))
						numRespConfirmacion++;
				}
				if (numRespConfirmacion > 1) valPreConfirmacion = true;	
			}
		}
		return valPreCorreo && valPreConfirmacion;		
	}
	
	private boolean preguntaElegible(Pregunta pre, String tipoPregunta, String tipoRespuesta){  // Tipo: N Fija, I: Libre
		boolean valPregunta = false;
		if (pre.getMultiresp().equals(tipoPregunta)) {
			for (Iterator itRes = pre.getRespuestas().iterator(); itRes.hasNext() && !valPregunta;) {
				Respuesta res = (Respuesta) itRes.next();
				if (res.getTipo().equals(tipoRespuesta)) {
					valPregunta = true;
				}
			}
		}
		return valPregunta; 
	}
	
	private Convocatoria guardar(ActionForm form, HttpServletRequest request) throws Exception{
		convForm = (convocatoriaForm) form;
		Long idMicrosite = ((Microsite)request.getSession().getAttribute("MVS_microsite")).getId().longValue();
		Convocatoria conv = null;
		
    	if (convForm.getId() == null) {  
        	conv = new Convocatoria(); // Es Alta
        } else {  // Es modificacion
        	conv = convDelegate.obtenerConvocatoria(convForm.getId());
        	//************COMPROBACION DE IDES*************
        	if (!conv.getMicrosite().getId().equals(idMicrosite))
        	{
        		throw new Exception();
        	}
        }
    	LDistribucionDelegate lDistribDel = DelegateUtil.getLlistaDistribucionDelegate();
    	EncuestaDelegate encuestaDel = DelegateUtil.getEncuestaDelegate();
    	encuestaDel.init(idMicrosite);
    	Microsite microsite = DelegateUtil.getMicrositeDelegate().obtenerMicrosite(idMicrosite);
    	conv.setMicrosite(microsite);
    	conv.setNombre(convForm.getNombre());
    	conv.setDescripcion(convForm.getDescripcion());
        conv.setEnvioSiError((convForm.getEnvioError() != null)? convForm.getEnvioError().equals("S"):false);		        
    	conv.setEncuesta(encuestaDel.obtenerEncuesta(convForm.getEncuesta()));
        conv.setRespuestaCorreo(encuestaDel.obtenerRespuesta(convForm.getResCorreo()));
        conv.setPreguntaConfirmacion(encuestaDel.obtenerPregunta(convForm.getPreConfirmacion()));
        conv.setRespuestaConfirmacion(encuestaDel.obtenerRespuesta(convForm.getResConfirmacion()));
        //conv.setDestinatarios(destinatarios);
        String otrosDestinatarios = convForm.getOtrosDestinatarios();
        otrosDestinatarios = otrosDestinatarios.replace("\r", "").replace("\t","").replace("\n", "").replace(" ", "");;
        conv.setOtrosDestinatarios(otrosDestinatarios);
        for(String s:otrosDestinatarios.split(",")){
    		if (s.matches("^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$")){
    			Correo correo = new Correo();
    			correo.setCorreo(s);
        		if (lDistribDel.consultaCorreo(correo.getCorreo())==null) 
        			lDistribDel.anadeCorreo(correo);
    		}
        }
        conv.setAsuntoMsg(convForm.getTxtAsunto());
        conv.setTextoMsg(convForm.getTxtMensaje());
        //conv.setUltimoEnvio(ultimoEnvio);
        
       	convDelegate.grabarConvocatoria(conv);
        return conv;
	}

	private void recuperar(ActionForm form, HttpServletRequest request)throws Exception{
		convocatoriaForm convForm = (convocatoriaForm) form;
		ListaDistribucion distrib = null;
		if (request.getParameter("id")!= null)
		{
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");               
	    	
			LDistribucionDelegate distrDelegate = DelegateUtil.getLlistaDistribucionDelegate();
	    	ConvocatoriaDelegate convDelegate = DelegateUtil.getConvocatoriaDelegate();
	    	Convocatoria conv = convDelegate.obtenerConvocatoria(new Long (request.getParameter("id")));
	    	//request.setAttribute("titconvocatoria",((TraduccionConvocatoria)enc.getTraduccion()).getTitulo());
	        
	        //************COMPROBACION DE IDES*************
	    	if (conv.getMicrosite().getId()!=((Microsite)request.getSession().getAttribute("MVS_microsite")).getId().longValue())
	    	{
	    		throw new Exception();
	    	}
	    	//*********************************************
	    	convForm.setId(conv.getId());
	    	convForm.setNombre(conv.getNombre());
	    	convForm.setDescripcion(conv.getDescripcion());
	        convForm.setEnvioError((conv.getEnvioSiError())?"S":"N");		        
	    	convForm.setEncuesta(conv.getEncuesta().getId());		        		        
	        convForm.setResCorreo(conv.getRespuestaCorreo().getId());
	        convForm.setPreConfirmacion(conv.getPreguntaConfirmacion().getId());
	        convForm.setResConfirmacion(conv.getRespuestaConfirmacion().getId());
	        Set destinatarios = new HashSet<ListaDistribucion>();      
	        for(Iterator it = conv.getDestinatarios().iterator();it.hasNext();){
	        	DistribucionConvocatoria distribucion = ((DistribucionConvocatoria)it.next());
	        	distrib = distrDelegate.obtenerListaDistribucion(distribucion.getIdDistribucion());	        	
	        	destinatarios.add(distrib);
	        }
	        convForm.setDestinatarios(destinatarios);
	        convForm.setOtrosDestinatarios(conv.getOtrosDestinatarios());
	        convForm.setTxtAsunto(conv.getAsuntoMsg());
	        if (conv.getTextoMsg() != null) convForm.setTxtMensaje(conv.getTextoMsg());
	        
	        convForm.setUltimoEnvio((conv.getUltimoEnvio() != null)? sdf.format(conv.getUltimoEnvio()):"");
		}
        request.setAttribute("convocatoriaForm", convForm);
	}

}

