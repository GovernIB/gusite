package es.caib.gusite.microfront.contenido.actions;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.RedirectingActionForward;

import es.caib.gusite.microfront.base.bean.ErrorMicrosite;
import es.caib.gusite.microfront.BaseAction;
import es.caib.gusite.microfront.contenido.util.Bdcontenido;
import es.caib.gusite.microfront.exception.*;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.TraduccionContenido;
import es.caib.gusite.micromodel.TraduccionMicrosite;


/**
 *  Action que consulta un contenido <P>
 *  Definición Struts:<BR>
 *  action path="/contenido" <BR> 
 *  unknown="false" <BR>
 *  forward name="contenidov4" path=/v4/contenido/contenido.jsp <BR>
 *  forward name="contenidov1" path=/v1/contenido/contenido.jsp
 *  
 *  @author - Indra
 */
public class ContenidoAction extends BaseAction {

	
	protected static Log log = LogFactory.getLog(ContenidoAction.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
			String forwardlocal="contenido";
		  	Microsite microsite = new Microsite();

	        try { 	
		  		Bdcontenido bdcontenido = new Bdcontenido(request);
		    	
				// Dejamos mostrar en previsualizacion los contenidos caducados o no visibles
				// de las 4 condiciones permitimos caducados y no visibles y no permitimos nosite y noinfo 
				boolean previsualizar = request.getParameter("previsual")!=null;
				boolean nosite = bdcontenido.getBeanerror().getMensaje().equals("El contenido solicitado no pertenece al site.");
				boolean noinfo = bdcontenido.getBeanerror().getMensaje().equals("El contenido solicitado no contiene información.");
				
			    if (!bdcontenido.isError() || (previsualizar && !nosite && !noinfo) ) {
			    	
				    //url del site
				    if ((bdcontenido.getUrlredireccionada()!=null) && (!bdcontenido.getUrlredireccionada().equals("null"))) {
				    	ActionForward elforward = new RedirectingActionForward();
				    	String sMenuCont = bdcontenido.getContenidolocal().getId().toString();

			    		if ((sMenuCont != null) && (!sMenuCont.equals("")) &&
			    						( bdcontenido.getUrlredireccionada().indexOf("idsite=") >=0 
			    								|| bdcontenido.getUrlredireccionada().indexOf("mkey=") >=0) ){
			    			elforward.setPath(bdcontenido.getUrlredireccionada()+"&mcont="+ sMenuCont);
			    		}else {
			    			elforward.setPath(bdcontenido.getUrlredireccionada());
			    		}
		 	        	return elforward;
				    }
			    	
			    	/*** SIEMPRE FIJAS para version 4**/
			    	//(redi)añadido para controlar que si viene de que es un único hijo, no ponerle el titulo de la mollapa
				    String redi = "" + request.getParameter("redi");
			    	String titol=((TraduccionContenido)bdcontenido.getContenidolocal().getTraduccion(bdcontenido.getIdioma())).getTitulo();
			    	String submolla = ((bdcontenido.getTitulomollapa().length()>0) && (!redi.equals("yes")))?bdcontenido.getTitulomollapa():"";
			    	request.setAttribute("MVS2_mollapan", mollapan(request, bdcontenido.getIdioma(),titol,submolla));
			    	menucaib(request, bdcontenido.getIdioma());
			    	/*** FIN SIEMPRE **/
	
			    	if ((submolla.length()>0) && (!redi.equals("yes"))) request.setAttribute("MVS_titulomollapa", submolla);
			    	
		    		request.setAttribute("MVS_contenido", bdcontenido.getContenidolocal());
		    	 
		    		request.setAttribute("MVS_tipobeta",""+request.getParameter("tipo"));
				    forwardlocal="contenido";
				    
					microsite = (Microsite)request.getSession().getAttribute("MVS_microsite");
					
				    //Pegote de la estructura a seguir. Campo restringido, N=azules, S=azules, 2=blancos.
				    //OJO: es la estructura y no la hoja de estilos.
				    if ( (microsite.getRestringido().equals("N")) || (microsite.getRestringido().equals("S")) )  
				    	return mapping.findForward(forwardlocal+"v1");
			    	else
				    	return mapping.findForward(forwardlocal+"v4");
				    
			    } else {
			    	return mapping.findForward(getForwardError (request, microsite, ErrorMicrosite.ERROR_AMBIT_PAGINA, response));
		  		
			    }

	        } catch (ExceptionFrontPagina efp) {	        	
	        	log.error(efp.getMessage());
	        	return mapping.findForward(getForwardError (request, microsite, ErrorMicrosite.ERROR_AMBIT_PAGINA, response));

	        } catch (ExceptionFrontMicro efm) {		    	
		    	log.error(efm.getMessage());
		    	return mapping.findForward(getForwardError (request, microsite, ErrorMicrosite.ERROR_AMBIT_MICRO, response));
		    	
		    }  catch (Exception e) {	        	
	        	log.error(e.getMessage());
	        	return mapping.findForward(getForwardError (request, microsite, ErrorMicrosite.ERROR_AMBIT_PAGINA, response));
	        }  		  	
	  }
	
	/**
	 * Método privado para guardar el recorrido que ha realizado el usuario por el microsite.
	 * @param request  
	 * @param idi   idioma
	 * @return string recorrido en el microsite
	 */
	private String mollapan(HttpServletRequest request, String idi, String titulo, String submolla) {
		StringBuffer stbuf = new StringBuffer("");
		ResourceBundle rb =	ResourceBundle.getBundle("ApplicationResources_front", new Locale(idi.toUpperCase(), idi.toUpperCase()));
		String mvs_ua = "" + request.getSession().getAttribute("MVS_ua");
		
		stbuf.append("<li><a href=\"http://www.caib.es\">" + rb.getString("general.inicio") + "</a></li>");
		if (!(mvs_ua.equals("null")) && (mvs_ua.length()>0))
			stbuf.append("<li>&gt; " + mvs_ua + "</li>");
		//	añado el titulo del microsite
		Microsite microsite = (Microsite)request.getSession().getAttribute("MVS_microsite");
		String titulo_mic = ((TraduccionMicrosite)microsite.getTraduccion(idi)!=null)?((TraduccionMicrosite)microsite.getTraduccion(idi)).getTitulo():"&nbsp;";
		stbuf.append("<li>&gt; <a href=\"home.do?mkey="+microsite.getClaveunica()+"&lang=" + idi + "\">" + titulo_mic+ " </a></li>");
		if (!(submolla.equals("null")) && (submolla.length()>0))
			stbuf.append("<li>&gt; " + submolla + "</li>");
		stbuf.append("<li>&gt; " + titulo + "</li>");

		return stbuf.toString();
	}
	
}
