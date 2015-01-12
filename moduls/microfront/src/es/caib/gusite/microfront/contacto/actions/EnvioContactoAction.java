package es.caib.gusite.microfront.contacto.actions;

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
import es.caib.gusite.microfront.contacto.util.Bdenviocontacto;
import es.caib.gusite.microfront.exception.ExceptionFrontMicro;
import es.caib.gusite.microfront.exception.ExceptionFrontPagina;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.TraduccionMicrosite;

/**
 *  Action Envio contacto <P>
 *  Definición Struts:<BR>
 *  action path="/enviocontacto" <BR> 
 *  unknown="false" <BR>
 *  forward name="contenidov4" path="/v4/contacto/enviocontacto.jsp"
 *  forward name="contenidov1" path="/v1/contacto/enviocontacto.jsp"
 *  
 *  @author - Indra
 */
public class EnvioContactoAction extends BaseAction {
	

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, Exception {
	  	
		String forwardlocal="contenido";
	  	try {
					  			
		  	Bdenviocontacto bdenviocontacto = new Bdenviocontacto(request);
			request.setAttribute("idsite", bdenviocontacto.getIdsite());
			
		    if (!bdenviocontacto.isError()) {
			    forwardlocal="contenido";
		
				/*** SIEMPRE FIJAS para version 4**/
				request.setAttribute("MVS2_mollapan", mollapan(request, bdenviocontacto.getIdioma()));
				menucaib(request, bdenviocontacto.getIdioma());
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
		stbuf.append("<li>&gt; <a href=\"contactos.do?idsite=" + microsite.getId().longValue() + "&lang=" + idi + "\">" + rb.getString("contacto.frmcontacto") + "</a></li>");		
		stbuf.append("<li>&gt; " + rb.getString("contacto.formulario") + "</li>");

		return stbuf.toString();
	}

}
