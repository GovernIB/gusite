package es.caib.gusite.microback.action;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.gusite.microback.action.util.Bdarchivopub;
import es.caib.gusite.micromodel.Archivo;
import es.caib.gusite.micromodel.Encuesta;
import es.caib.gusite.micromodel.Pregunta;
import es.caib.gusite.micromodel.Respuesta;
import es.caib.gusite.micromodel.RespuestaDato;
import es.caib.gusite.micromodel.TraduccionPregunta;
import es.caib.gusite.micromodel.TraduccionRespuesta;
import es.caib.gusite.micromodel.UsuarioEncuesta;
import es.caib.gusite.micropersistence.delegate.ArchivoDelegate;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.EncuestaDelegate;
import es.caib.gusite.micropersistence.delegate.UsuarioEncuestaDelegate;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Action que descarga un archivo<P>
 * 
 * 	Definición Struts:<BR>
 *  action path="/exportaEncuesta"<BR> 
 *  
 *  @author - Indra
 */
public class ExportarEncuestaAction extends Action {

    protected static Log log = LogFactory.getLog(ExportarEncuestaAction.class);

    public final ActionForward execute(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request, HttpServletResponse response)
            throws Exception {
    	StringBuffer sbfOut = obtenerArchivo(mapping, form, request);
    	Long idEncuesta = new Long(request.getParameter("idEncuesta"));
	    if (sbfOut != null) {
            response.reset();
            response.setContentType("plain/text");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + "estadistica" + idEncuesta.toString() + ".csv" + "\"");
            response.setContentLength(sbfOut.length());
            response.getWriter().write(sbfOut.toString());
        }
        return null;
    }

    public StringBuffer obtenerArchivo(ActionMapping mapping, ActionForm form,
            HttpServletRequest request) throws Exception {
    	
    	Long idEncuesta = new Long(request.getParameter("idEncuesta"));
    	EncuestaDelegate enc = DelegateUtil.getEncuestaDelegate();

    	return exportarEncuesta(idEncuesta);
    }   
    
    private  StringBuffer exportarEncuesta(Long idEnc){
		StringBuffer sbf = new StringBuffer();

		final String NEW_COL = ";";
		final String NEW_LINE = "\n";
		final String DELIMITADOR = "\"";
		final String IDIOMA = "ca";

		TraduccionPregunta traduccPreg = null;
		TraduccionRespuesta traduccResp = null;
    	EncuestaDelegate encDelegate = DelegateUtil.getEncuestaDelegate();
    	try {
			Encuesta enc = encDelegate.obtenerEncuesta(idEnc);
			// Titulos de las preguntas
			sbf.append(DELIMITADOR + " " + DELIMITADOR);
			sbf.append(NEW_COL);
			sbf.append(DELIMITADOR + " " + DELIMITADOR);
			sbf.append(NEW_COL);
			sbf.append(DELIMITADOR + " " + DELIMITADOR);
			sbf.append(NEW_COL);
			for (Iterator itPreg = enc.getPreguntas().iterator(); itPreg.hasNext();){ //Obtenemos las preguntas de una encuesta
				Pregunta preg = (Pregunta)itPreg.next();
				traduccPreg = (TraduccionPregunta)preg.getTraduccionMap().get(IDIOMA);
				if (preg.getRespuestas().size()>0)
					sbf.append(DELIMITADOR + traduccPreg.getTitulo().replace(";", " ") + DELIMITADOR);
				if("S".equals(preg.getMultiresp())){ // Mirar si es multi respuesta. Mateixa pregunta per totes les respostes							 
					for (int i = 0; i < preg.getRespuestas().size() - 1; i++){
						sbf.append(NEW_COL);					
						sbf.append(DELIMITADOR + traduccPreg.getTitulo().replace(";", " ") + DELIMITADOR);
					}				
				}
				if(itPreg.hasNext() && (preg.getRespuestas().size()>0)) 
					sbf.append(NEW_COL);
			}
			sbf.append(NEW_LINE);
			// Títulos de las respuestas
			sbf.append(DELIMITADOR + "USER ID" + DELIMITADOR);
			sbf.append(NEW_COL);
			sbf.append(DELIMITADOR + "DNI" + DELIMITADOR);
			sbf.append(NEW_COL);
			sbf.append(DELIMITADOR + "NOM" + DELIMITADOR);
			sbf.append(NEW_COL);
			for (Iterator itPreg = enc.getPreguntas().iterator(); itPreg.hasNext();){ //Obtenemos las preguntas de una encuesta
				Pregunta preg = (Pregunta)itPreg.next();			
				if("S".equals(preg.getMultiresp())){ // Mirar si es multi respuesta y valores
					for (Iterator itResp = preg.getRespuestas().iterator();itResp.hasNext();){
						Respuesta resp = (Respuesta)itResp.next();
						traduccResp = (TraduccionRespuesta)resp.getTraduccionMap().get(IDIOMA);
						sbf.append(DELIMITADOR + traduccResp.getTitulo().replace(";", " ") + DELIMITADOR);
						if (itResp.hasNext()) sbf.append(NEW_COL);
					}							
				}else{
					sbf.append(DELIMITADOR + " " + DELIMITADOR);
				}
				if(itPreg.hasNext()&& (preg.getRespuestas().size()>0)) 
					sbf.append(NEW_COL);			
			}
			// Respuestas de los encuestados
			sbf.append(NEW_LINE);
			List usuaris = encDelegate.obtenerUsuariosEncuesta(idEnc);
			
			UsuarioEncuestaDelegate usuEncDel = DelegateUtil.getUsuarioEncuestaDelegate();
			Long usuId = null;
			int i=0;
			for (Iterator itUsu = usuaris.iterator(); itUsu.hasNext();) {  // Para cada usuario
				try{
					usuId = (Long)itUsu.next();
					i++;
					UsuarioEncuesta usuari = usuEncDel.obtenerUsuarioEncuesta(usuId);
					Respuesta resp = null;
					List respUsu = encDelegate.obtenerRespuestasDeUsuario(usuari.getId()); // Respuestas de un usuario
					sbf.append(usuari.getId());
					sbf.append(NEW_COL);
					sbf.append(usuari.getDni());
					sbf.append(NEW_COL);
					sbf.append(usuari.getNombre());
					
					Hashtable<Long, RespuestaDato> respLibre =(Hashtable<Long, RespuestaDato>) encDelegate.listarRespuestasDato(enc.getId(), usuari.getId());
					for (Iterator itPreg = enc.getPreguntas().iterator(); itPreg.hasNext();) { //Obtenemos las preguntas de una encuesta
						boolean colUnica = false;
						Pregunta preg = (Pregunta) itPreg.next();				
						for (Iterator itResp = preg.getRespuestas().iterator();itResp.hasNext();) { // Respuestas de una pregunta
							resp = (Respuesta)itResp.next();
							if ("S".equals(preg.getMultiresp())) { // Una columna para cada solución
								if ("N".equals(resp.getTipo())) { // Texte tipificat											
									sbf.append(NEW_COL);
									if(respUsu.contains(resp.getId())) 
										sbf.append("1");
									else
										sbf.append("0");
								}else if ("I".equals(resp.getTipo())){ // Resposta lliure
									//RespuestaDato respDato = encDelegate.obtenerRespuestaDato(resp.getId(),usuari.getId());
									RespuestaDato respDato = respLibre.get(resp.getId());
									sbf.append(NEW_COL);
									if (respDato != null && respDato.getDato() != null){
										sbf.append(DELIMITADOR + respDato.getDato().
												replace(";", " ").
												replace("\n", " ").
												replace("\t", " ").
												replace("\r", "")+ DELIMITADOR);
									}else{
										sbf.append(" ");							
									}						
								}							
							}else {						// Una columna con la solucion
								if (!colUnica) {
									sbf.append(NEW_COL);
									colUnica = true;
								}
								if ("N".equals(resp.getTipo())) { // Texte tipificat																		
									if(respUsu.contains(resp.getId())){							
										traduccResp = (TraduccionRespuesta)resp.getTraduccionMap().get(IDIOMA);
										sbf.append(DELIMITADOR + traduccResp.getTitulo().replace(";", " ") + DELIMITADOR);								
									}								
								}else if ("I".equals(resp.getTipo())){ // Resposta lliure					
									//RespuestaDato respDato = encDelegate.obtenerRespuestaDato(resp.getId(),usuari.getId());
									RespuestaDato respDato = respLibre.get(resp.getId());
									if (respDato != null && respDato.getDato() != null){
										sbf.append(DELIMITADOR + respDato.getDato().
												replace(";", " ").
												replace("\n", " ").
												replace("\t", " ").
												replace("\r", "")+ DELIMITADOR);
									}else{
										sbf.append(" ");
									}
								}
							}
						} // End FOR respuestas				
					} // End FOR preguntas
					if(itUsu.hasNext()) sbf.append(NEW_LINE);
				} // End FOR usuarios
			catch(Exception e){
					log.error("idUsuariEncuesta: " + usuId + " " + e);
				}
			}
	}catch(Exception e){
		log.error("Error recuperant usuaris de la la enquesta: " + idEnc + " " + e);
	}
	return sbf;
    }

}
