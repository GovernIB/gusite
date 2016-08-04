package es.caib.gusite.microfront.encuesta.actions;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.caib.gusite.utilities.auth.CertsPrincipal;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.gusite.microfront.base.bean.ErrorMicrosite;
import es.caib.gusite.microfront.BaseAction;
import es.caib.gusite.microfront.Microfront;
import es.caib.gusite.microfront.encuesta.util.BdEncuesta;
import es.caib.gusite.microfront.exception.ExceptionFrontMicro;
import es.caib.gusite.microfront.exception.ExceptionFrontPagina;
import es.caib.gusite.microfront.home.UserRequestProcessor;
import es.caib.gusite.microfront.util.microtag.MParserEncuesta;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.TraduccionEncuesta;
import es.caib.gusite.micromodel.TraduccionMicrosite;

/**
 *  Action que consulta una encuesta <P>
 *  Definición Struts:<BR>
 *  action path="/encuesta" <BR> 
 *  unknown="false" <BR>
 *  forward name="contenidov4" path="/v4/encuesta/encuesta.jsp" <BR>
 *  forward name="realizadov4" path="/v4/encuesta/envioencuesta.jsp"    <BR>
 *  forward name="contenidov1" path="/v1/encuesta/encuesta.jsp"<BR>
 *  forward name="realizadov1" path="/v1/encuesta/envioencuesta.jsp" <BR> 
 *  
 *  @author - Indra
 */
public class EncuestaAction extends BaseAction   {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		  	String forwardlocal="contenido";
		    
		  	try {

		  		siEstaEnMantenimientoAvisar(request);
		  			
			  	BdEncuesta bdencuesta = new BdEncuesta(request);
			  //Canvi Salvador 05/10/2010
				String encuestaRellena=(String)request.getSession().getAttribute("MVS_encuestarellena" + bdencuesta.getEncuesta().getId().toString());
			  	String visita=(String)request.getSession().getAttribute("MVS_visita" + bdencuesta.getEncuesta().getId().toString());
			  	String pview=(String)request.getParameter(Microfront.PVIEW);
			  	if (encuestaRellena!=null){
			  		if (visita!=null && visita.equalsIgnoreCase("1VOT"))
			  			visita="NVOT";
			  		else if (visita!=null && visita.equalsIgnoreCase("NVOT"))
			  			visita="NVOT";
			  		else if (pview==null)
			  			visita="1VOT";
			  		request.getSession().setAttribute("MVS_visita" + bdencuesta.getEncuesta().getId().toString(), visita);
			  		bdencuesta.getEncuesta().setVotoDuplicado(visita);
			  	}
			  	
			    if (!bdencuesta.isError()) {
	
			    	request.setAttribute("MVS_encuesta", bdencuesta.getEncuesta());
			    	if (bdencuesta.isEnsesion()) {
			    		forwardlocal="realizado";
			    	}
			    		else forwardlocal="contenido";
			    			    	
			    	if (bdencuesta.getEncuesta().getIdentificacion().equals("S")){
		    			try {
							CertsPrincipal.getCurrent();
						} catch (Exception e) {
							log.error("Error en la identificación del usuario en la encuesta: " + bdencuesta.getEncuesta().getId() + " ---> " + e);
							 String url = request.getRequestURL().toString();
							 String queryString = request.getQueryString();
							 request.getSession().setAttribute("redirect", url + "?" + queryString );
							 response.sendRedirect(request.getContextPath() + UserRequestProcessor._URLINTRANETLOGIN + "?idsite=" + bdencuesta.getIdsite() + "&lang=" + bdencuesta.getIdioma());
							 return super.execute(mapping, form, request, response);
						}
			    	}
			    	
					MParserEncuesta parser = new MParserEncuesta(bdencuesta.getMicrosite().getRestringido());
			    	StringBuffer sb = parser.getHtmlEncuesta(
			    			request,
							bdencuesta.getIdsite(),
							bdencuesta.getEncuesta().getId().toString(),
							bdencuesta.getIdioma()
						);
			    	request.setAttribute("MVS_htmlEncuestaParsed",sb);
				    	
			    	/*** SIEMPRE FIJAS para version 4**/
			    	String titol=((TraduccionEncuesta)bdencuesta.getEncuesta().getTraduccion(bdencuesta.getIdioma())).getTitulo();
			    	request.setAttribute("MVS2_mollapan", mollapan(request, bdencuesta.getIdioma(),titol));
			    	menucaib(request, bdencuesta.getIdioma());
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


	protected void siEstaEnMantenimientoAvisar(HttpServletRequest request) {
		if(estaEnMantenimiento() ) {
			request.setAttribute("MVS_manteniment", new Object());
		}
	}


	protected boolean estaEnMantenimiento() {
		String mantenimiento = System.getProperty("es.caib.gusite.mantenimiento");
		return null!=mantenimiento && "si".equalsIgnoreCase(mantenimiento);
	}
	

	/**
	 * Método privado para guardar el recorrido que ha realizado el usuario por el microsite.
	 * @param request  
	 * @param idi   idioma
	 * @return string recorrido en el microsite
	 */
	private String mollapan(HttpServletRequest request, String idi, String titulo) {
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
		stbuf.append("<li>&gt; " + titulo + "</li>");

		return stbuf.toString();
	}
	
}
