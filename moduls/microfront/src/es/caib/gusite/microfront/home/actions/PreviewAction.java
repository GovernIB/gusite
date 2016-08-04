package es.caib.gusite.microfront.home.actions;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;

import es.caib.gusite.microfront.base.bean.ErrorMicrosite;
import es.caib.gusite.microfront.home.util.Bdhome;
import es.caib.gusite.microfront.BaseAction;
import es.caib.gusite.microfront.exception.ExceptionFrontMicro;
import es.caib.gusite.microfront.exception.ExceptionFrontPagina;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.TraduccionMicrosite;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 *  Action de previsualización <P>
 *  Definición Struts:<BR>
 *  action path="/menupreview" <BR> 
 *  unknown="false" <BR>
 *  forward name="previewv4" path="/v4/home/menupreview.jsp" <BR>
 *  forward name="previewv1" path="/v1/home/menupreview.jsp"
 *  
 *  @author - Indra
 */
public class PreviewAction extends BaseAction {

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	  	String forwardlocal="preview";	  
	    Microsite microsite = new Microsite();
		
	  	try {
		    microsite = (Microsite)request.getSession().getAttribute("MVS_microsite");
		
		    //averiguar información de la home
		    Bdhome bdhome = new Bdhome(request);
		    
			/*** SIEMPRE FIJAS para version 4**/
			request.setAttribute("MVS2_mollapan", mollapan(request, bdhome.getIdioma()));
			menucaib(request, bdhome.getIdioma());
			/*** FIN SIEMPRE **/
		
		    //Pegote de la estructura a seguir. Campo restringido, N=azules, S=azules, 2=blancos.
		    //OJO: es la estructura y no la hoja de estilos.
		    if ( (microsite.getRestringido().equals("N")) || (microsite.getRestringido().equals("S")) )  
		    	return mapping.findForward(forwardlocal+"v1");
			else
		    	return mapping.findForward(forwardlocal+"v4");

        } catch (ExceptionFrontPagina efp) {
        	log.error(efp.getMessage());
        	return mapping.findForward(getForwardError (request, microsite, ErrorMicrosite.ERROR_AMBIT_PAGINA));

        } catch (ExceptionFrontMicro efm) {
	    	log.error(efm.getMessage());
	    	return mapping.findForward(getForwardError (request, microsite, ErrorMicrosite.ERROR_AMBIT_MICRO));
	    	
	    }  catch (Exception e) { 	
        	log.error(e.getMessage());
        	return mapping.findForward(getForwardError (request, microsite, ErrorMicrosite.ERROR_AMBIT_PAGINA));
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
		
		stbuf.append("<li><a href=\"http://www.caib.es\">" + rb.getString("general.inicio") + "</a></li>");
		if (!(mvs_ua.equals("null")) && (mvs_ua.length()>0))
			stbuf.append("<li>&gt; " + mvs_ua + "</li>");
		//añado el titulo del microsite
		Microsite microsite = (Microsite)request.getSession().getAttribute("MVS_microsite");
		String titulo_mic = ((TraduccionMicrosite)microsite.getTraduccion(idi)!=null)?((TraduccionMicrosite)microsite.getTraduccion(idi)).getTitulo():"&nbsp;";
		stbuf.append("<li>&gt; <a href=\"home.do?mkey="+microsite.getClaveunica()+"&lang=" + idi + "\">" + titulo_mic+ " </a></li>");
		return stbuf.toString();
	}	
}