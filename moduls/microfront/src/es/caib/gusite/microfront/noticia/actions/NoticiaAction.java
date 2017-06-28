package es.caib.gusite.microfront.noticia.actions;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.gusite.microfront.Microfront;
import es.caib.gusite.microfront.base.bean.ErrorMicrosite;
import es.caib.gusite.microfront.BaseAction;
import es.caib.gusite.microfront.exception.ExceptionFrontMicro;
import es.caib.gusite.microfront.exception.ExceptionFrontPagina;
import es.caib.gusite.microfront.noticia.util.Bdnoticia;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.Noticia;
import es.caib.gusite.micromodel.TraduccionMicrosite;

/**
 *  Action para ver una noticia en el front <P>
 *  Definición Struts:<BR>
 *  action path="/noticia" <BR> 
 *  unknown="false" <BR>
 *  forward name="contenidov4" path="/v4/noticia/noticia.jsp"<BR>
 *  forward name="contenidoMapav4" path="/v4/noticia/ubicacion.jsp"<BR>
 * 	forward name="contenidov1" path="/v1/noticia/noticia.jsp"
 *  
 *  @author - Indra
 */	
public class NoticiaAction extends BaseAction   {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String forwardlocal="contenido";
		boolean forzarmapa = false;
	  	
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
		        //solo quiero que añada el atributo en el caso de cargar noticia del listarnoticias.jsp
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
			    
			    if ( (microsite.getRestringido().equals("N")) || (microsite.getRestringido().equals("S")) ){  
			    	return mapping.findForward(forwardlocal+"v1");
			    }else{
		    		
			    	String fmapa = request.getParameter(Microfront.FMAPA);
			    	
		    		if (bdnoticia.getNoticia().getTipo().getTipoelemento().equals(Microfront.ELEM_MAPA)) {
		    			forwardlocal += "Mapa";
		    		}else if(  !StringUtils.isEmpty(fmapa) && fmapa.equals("true")
							&& !StringUtils.isEmpty(bdnoticia.getNoticia().getLatitud())
							&& !StringUtils.isEmpty(bdnoticia.getNoticia().getLongitud())){
		    			forwardlocal += "Mapa";
		    			forzarmapa = true;		    			
		    		}	
			    }
		    				    		
	    		request.setAttribute("MVS_forzarmapa", forzarmapa);
		    	return mapping.findForward(forwardlocal+"v4");
		    
		    } else {
		    	return mapping.findForward(getForwardError (request, ErrorMicrosite.ERROR_AMBIT_PAGINA, response)); 	
			}
	    
	    } catch (ExceptionFrontPagina efp) {
	    	log.error(efp.getMessage());
	    	return mapping.findForward(getForwardError (request,  ErrorMicrosite.ERROR_AMBIT_PAGINA, response));
	
	    } catch (ExceptionFrontMicro efm) {	    	
	    	log.error(efm.getMessage());
	    	return mapping.findForward(getForwardError (request, ErrorMicrosite.ERROR_AMBIT_MICRO, response));
	    	
	    }  catch (Exception e) {
	    	log.error(e.getMessage());
	    	return mapping.findForward(getForwardError (request, ErrorMicrosite.ERROR_AMBIT_PAGINA, response));
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
		String mvs_tiponoticia="" + request.getAttribute("MVS_tiponoticia");
		Noticia noticia = (Noticia)request.getAttribute("MVS_noticia");
		
		stbuf.append("<li><a href=\"http://www.caib.es\">" + rb.getString("general.inicio") + "</a></li>");
		if (!(mvs_ua.equals("null")) && (mvs_ua.length()>0))
			stbuf.append("<li>&gt; " + mvs_ua + "</li>");
		//añado el titulo del microsite
		String titulo_mic = ((TraduccionMicrosite)microsite.getTraduccion(idi)!=null)?((TraduccionMicrosite)microsite.getTraduccion(idi)).getTitulo():"&nbsp;";
		stbuf.append("<li>&gt; <a href=\"home.do?mkey="+microsite.getClaveunica()+"&lang=" + idi + "\">" + titulo_mic+ " </a></li>");
		stbuf.append("<li>&gt; <a href=\"noticias.do?idsite=" + microsite.getId().longValue() + "&lang=" + idi + "&tipo=" + noticia.getTipo().getId().longValue() + "\">" + mvs_tiponoticia + "</a></li>");
		stbuf.append("<li>&gt; " + rb.getString("noticia.detalle") + "</li>");

		return stbuf.toString();
	}		
		
}
