package org.ibit.rol.sac.microfront.noticia.actions;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.ibit.rol.sac.microfront.BaseAction;
import org.ibit.rol.sac.microfront.base.bean.ErrorMicrosite;
import org.ibit.rol.sac.microfront.exception.ExceptionFrontMicro;
import org.ibit.rol.sac.microfront.exception.ExceptionFrontPagina;
import org.ibit.rol.sac.microfront.noticia.util.Bdnoticia;
import org.ibit.rol.sac.micromodel.Microsite;
import org.ibit.rol.sac.micromodel.Noticia;
import org.ibit.rol.sac.micromodel.TraduccionMicrosite;

/**
 *  Action para ver una noticia en el front <P>
 *  Definici�n Struts:<BR>
 *  action path="/noticia" <BR> 
 *  unknown="false" <BR>
 *  forward name="contenidov4" path="/v4/noticia/noticia.jsp"<BR>
 * 	forward name="contenidov1" path="/v1/noticia/noticia.jsp"
 *  
 *  @author - Indra
 */	
public class NoticiaAction extends BaseAction   {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String forwardlocal="contenido";
	  	
		try {
		  	Bdnoticia bdnoticia = new Bdnoticia(request);
		  	if (!bdnoticia.isError()) {
		    	 
			    request.setAttribute("MVS_noticia", bdnoticia.getNoticia());
			    if (bdnoticia.getNoticia().getImagen()!= null){
			    	if (bdnoticia.getNoticia().getImagen().getPeso()> 100*1024) request.setAttribute("MVS_anchoImg", "width=460");
			    	else  request.setAttribute("MVS_anchoImg", "");
			    }	
			    request.setAttribute("MVS_tiponoticia", bdnoticia.getDesctiponoticia());
			    String sIdMenuContenidoNotic= request.getParameter("mcont");
		        //solo quiero que a�ada el atributo en el caso de cargar noticia del listarnoticias.jsp
		        if ( (sIdMenuContenidoNotic != null) && (!sIdMenuContenidoNotic.equals(""))  ){
		    	      request.setAttribute("MVS_menu_cont_notic",  sIdMenuContenidoNotic);
		        }
		
		    	/*** SIEMPRE FIJAS para version 4**/
		    	request.setAttribute("MVS2_mollapan", mollapan(request, bdnoticia.getIdioma()));
		    	menucaib(request, bdnoticia.getIdioma());
		    	/*** FIN SIEMPRE **/
			    
			    //Pegote de la estructura a seguir. Campo restringido, N=azules, S=azules, 2=blancos.
			    //OJO: es la estructura y no la hoja de estilos.
			    Microsite microsite = new Microsite();
			    microsite = (Microsite)request.getSession().getAttribute("MVS_microsite");
			    request.setAttribute(" MVS_idsite",microsite);
			    
			    if ( (microsite.getRestringido().equals("N")) || (microsite.getRestringido().equals("S")) )  
			    	return mapping.findForward(forwardlocal+"v1");
		    	else
			    	return mapping.findForward(forwardlocal+"v4");
		    
		    } else {
		    	return mapping.findForward(getForwardError (request, ErrorMicrosite.ERROR_AMBIT_PAGINA)); 	
			}
	    
	    } catch (ExceptionFrontPagina efp) {
	    	log.error(efp.getMessage());
	    	return mapping.findForward(getForwardError (request,  ErrorMicrosite.ERROR_AMBIT_PAGINA));
	
	    } catch (ExceptionFrontMicro efm) {	    	
	    	log.error(efm.getMessage());
	    	return mapping.findForward(getForwardError (request, ErrorMicrosite.ERROR_AMBIT_MICRO));
	    	
	    }  catch (Exception e) {
	    	log.error(e.getMessage());
	    	return mapping.findForward(getForwardError (request, ErrorMicrosite.ERROR_AMBIT_PAGINA));
	    } 			    
	    
	}
	
	/**
	 * M�todo privado para guardar el recorrido que ha realizado el usuario por el microsite.
	 * @param request  
	 * @param idi   idioma
	 * @return string recorrido en el microsite
	 */	
	private String mollapan(HttpServletRequest request, String idi) {
		StringBuffer stbuf = new StringBuffer("");
		ResourceBundle rb =	ResourceBundle.getBundle("ApplicationResources_front", new Locale(idi.toUpperCase(), idi.toUpperCase()));
		String mvs_ua = "" + request.getSession().getAttribute("MVS_ua");
		Microsite microsite = (Microsite)request.getSession().getAttribute("MVS_microsite");
		String mvs_tiponoticia="" + request.getAttribute("MVS_tiponoticia");
		Noticia noticia = (Noticia)request.getAttribute("MVS_noticia");
		
		stbuf.append("<li><a href=\"http://www.caib.es\">" + rb.getString("general.inicio") + "</a></li>");
		if (!(mvs_ua.equals("null")) && (mvs_ua.length()>0))
			stbuf.append("<li>&gt; " + mvs_ua + "</li>");
		//a�ado el titulo del microsite
		String titulo_mic = ((TraduccionMicrosite)microsite.getTraduccion(idi)!=null)?((TraduccionMicrosite)microsite.getTraduccion(idi)).getTitulo():"&nbsp;";
		stbuf.append("<li>&gt; <a href=\"home.do?mkey="+microsite.getClaveunica()+"&lang=" + idi + "\">" + titulo_mic+ " </a></li>");
		stbuf.append("<li>&gt; <a href=\"noticias.do?idsite=" + microsite.getId().longValue() + "&lang=" + idi + "&tipo=" + noticia.getTipo().getId().longValue() + "\">" + mvs_tiponoticia + "</a></li>");
		stbuf.append("<li>&gt; " + rb.getString("noticia.detalle") + "</li>");

		return stbuf.toString();
	}		
		
}
