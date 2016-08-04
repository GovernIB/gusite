package es.caib.gusite.microfront.agenda.actions;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.gusite.microfront.base.bean.ErrorMicrosite;
import es.caib.gusite.microfront.BaseAction;
import es.caib.gusite.microfront.agenda.util.Bdagenda;
import es.caib.gusite.microfront.exception.ExceptionFrontMicro;
import es.caib.gusite.microfront.exception.ExceptionFrontPagina;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.TraduccionMicrosite;

/**
 *  Action que consulta una agenda <P>
 *  Definición Struts:<BR>
 *  action path="/agenda" <BR> 
 *  unknown="false" <BR>
 *  forward name="contenidov4" path="/v4/agenda/agenda.jsp"
 *  forward name="contenidov1" path="/v1/agenda/agenda.jsp"
 *  
 *  @author - Indra
 */
public class AgendaAction extends BaseAction {

	/**
	* Metodo público que devuelve ActionForward cuando se consulta un día de la agenda
	* @param mapping 
	* @param form
	* @param request
	* @param response
	* @return ActionForward
	* @exception  IOException, ServletException, Exception
	*/
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, Exception {
				  	
		String forwardlocal="contenido";
				    
		try {
				  	
		  	Bdagenda bdagenda = new Bdagenda(request);
		    if (!bdagenda.isError()) {
		    	
			    request.setAttribute("MVS_agenda_lista", bdagenda.getListaeventos());
			    request.setAttribute("MVS_agenda_diaevento", bdagenda.getFecha());
			    request.setAttribute("MVS_seulet_sin",bdagenda.getUrl_sinpagina());
			    request.setAttribute("MVS_parametros_pagina",bdagenda.getParametros());
			    forwardlocal="contenido";
		
		    	/*** SIEMPRE FIJAS para version 4**/
		    	request.setAttribute("MVS2_mollapan", mollapan(request, bdagenda.getIdioma()));
		    	menucaib(request, bdagenda.getIdioma());
		    	/*** FIN SIEMPRE **/
							
			    //Pegote de la estructura a seguir. Campo restringido, N=azules, S=azules, 2=blancos.
			    //OJO: es la estructura y no la hoja de estilos.
		    	Microsite microsite = (Microsite)request.getSession().getAttribute("MVS_microsite");
			    if ( (microsite.getRestringido().equals("N")) || (microsite.getRestringido().equals("S")) )  
			    	return mapping.findForward(forwardlocal+"v1");
		    	else
			    	return mapping.findForward(forwardlocal+"v4");
	    
		    } else {
		    	return mapping.findForward(getForwardError (request, ErrorMicrosite.ERROR_AMBIT_PAGINA));
			}
						    
        } catch (ExceptionFrontPagina efp) {
        	log.error(efp.getMessage());
        	return mapping.findForward(getForwardError (request, ErrorMicrosite.ERROR_AMBIT_PAGINA));
        	
        } catch (ExceptionFrontMicro efm) {
	    	log.error(efm.getMessage());
	    	return mapping.findForward(getForwardError (request, ErrorMicrosite.ERROR_AMBIT_MICRO));
	    	
	    }  catch (Exception e) {
        	log.error(e.getMessage());
        	return mapping.findForward(getForwardError (request, ErrorMicrosite.ERROR_AMBIT_PAGINA));
        } 
					
	}
	  
	/**
	 * Método privado para guardar el recorrido que ha realizado el usuario por el microsite.
	 * @param request  
	 * @param idi   idioma
	 * @return string recorrido en el microsite
	 */
	private String mollapan(HttpServletRequest request, String idi) {
		StringBuffer stbuf = new StringBuffer("");
		ResourceBundle rb =	ResourceBundle.getBundle("ApplicationResources_front", new Locale(idi.toUpperCase(), idi.toUpperCase()));
		String mvs_ua = "" + request.getSession().getAttribute("MVS_ua");
		Microsite microsite = (Microsite)request.getSession().getAttribute("MVS_microsite");
		
		stbuf.append("<li><a href=\"http://www.caib.es\">" + rb.getString("general.inicio") + "</a></li>");
		if (!(mvs_ua.equals("null")) && (mvs_ua.length()>0))
			stbuf.append("<li>&gt; " + mvs_ua + "</li>");
		//añado el titulo del microsite
		String titulo_mic = ((TraduccionMicrosite)microsite.getTraduccion(idi)!=null)?((TraduccionMicrosite)microsite.getTraduccion(idi)).getTitulo():"&nbsp;";
		stbuf.append("<li>&gt; <a href=\"home.do?mkey="+microsite.getClaveunica()+"&lang=" + idi + "\">" + titulo_mic+ " </a></li>");
		stbuf.append("<li>&gt; <a href=\"agendas.do?idsite=" + microsite.getId().longValue() + "&lang=" + idi + "\">" + rb.getString("agenda.agenda") + "</a></li>");	
		stbuf.append("<li>&gt; " + rb.getString("agenda.evento") + "</li>");
	
		return stbuf.toString();
	}			  			
			  
}
