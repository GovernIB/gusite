package es.caib.gusite.microfront.encuesta.actions;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.gusite.microfront.base.bean.ErrorMicrosite;
import es.caib.gusite.microfront.BaseAction;
import es.caib.gusite.microfront.Microfront;
import es.caib.gusite.microfront.encuesta.util.BdEnvioencuesta;
import es.caib.gusite.microfront.exception.ExceptionFrontMicro;
import es.caib.gusite.microfront.exception.ExceptionFrontPagina;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.TraduccionEncuesta;
import es.caib.gusite.micromodel.TraduccionMicrosite;

/**
 *  Action que se ejecuta cuando un usuario envia una encuesta <P>
 *  Definición Struts:<BR>
 *  action path="/envioencuesta" <BR> 
 *  unknown="false" <BR>
 *  forward name="contenidov4" path=/v4/encuesta/envioencuesta.jsp <BR>
 *  forward name="contenidov1" path=/v1/encuesta/envioencuesta.jsp
 *  
 *  @author - Indra
 */
public class EnvioEncuestaAction extends BaseAction {
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		  	String forwardlocal="contenido";
		    
		  	try {
		  	
			  	BdEnvioencuesta bdenvioencuesta = new BdEnvioencuesta(request);
			  	
	    		//Canvi Salvador 05/10/2010
				String encuestaRellena=(String)request.getSession().getAttribute("MVS_encuestarellena" + bdenvioencuesta.getEncuesta().getId().toString());
			  	String visita=(String)request.getSession().getAttribute("MVS_visita" + bdenvioencuesta.getEncuesta().getId().toString());
			  	String pview=(String)request.getParameter(Microfront.PVIEW);
			  	if (encuestaRellena!=null && (pview==null)){
			  		if (visita!=null && visita.equalsIgnoreCase("1VOT"))
			  			visita="NVOT";
			  		else if (visita!=null && visita.equalsIgnoreCase("NVOT"))
			  			visita="NVOT";
			  		else 
			  			visita="1VOT";
			  		request.getSession().setAttribute("MVS_visita" + bdenvioencuesta.getEncuesta().getId().toString(), visita);
			  		bdenvioencuesta.getEncuesta().setVotoDuplicado(visita);
			  	}
			  		// Final Canvi	
			  	if (!bdenvioencuesta.isError()) {
			    	request.setAttribute("MVS_encuesta_entera",(bdenvioencuesta.isEntera()?"entera":"componente"));
			    	request.setAttribute("MVS_encuesta",bdenvioencuesta.getEncuesta());
				    forwardlocal="contenido";
			    
			    	/*** SIEMPRE FIJAS para version 4**/
			    	String titol=((TraduccionEncuesta)bdenvioencuesta.getEncuesta().getTraduccion(bdenvioencuesta.getIdioma())).getTitulo();
			    	request.setAttribute("MVS2_mollapan", mollapan(request, bdenvioencuesta.getIdioma(),titol));
			    	menucaib(request, bdenvioencuesta.getIdioma());
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
	private String mollapan(HttpServletRequest request, String idi, String titulo) {
		StringBuffer stbuf = new StringBuffer("");
		ResourceBundle rb =	ResourceBundle.getBundle("ApplicationResources_front", new Locale(idi.toUpperCase(), idi.toUpperCase()));
		String mvs_ua = "" + request.getSession().getAttribute("MVS_ua");
		
		stbuf.append("<li><a href=\"http://www.caib.es\">" + rb.getString("general.inicio") + "</a></li>");
		if (!(mvs_ua.equals("null")) && (mvs_ua.length()>0))
			stbuf.append("<li>&gt; " + mvs_ua + "</li>");
		//		añado el titulo del microsite
		Microsite microsite = (Microsite)request.getSession().getAttribute("MVS_microsite");
		String titulo_mic = ((TraduccionMicrosite)microsite.getTraduccion(idi)!=null)?((TraduccionMicrosite)microsite.getTraduccion(idi)).getTitulo():"&nbsp;";
		stbuf.append("<li>&gt; <a href=\"home.do?mkey="+microsite.getClaveunica()+"&lang=" + idi + "\">" + titulo_mic+ " </a></li>");
		stbuf.append("<li>&gt; " + titulo + ": " + rb.getString("encuesta.resultados") + "</li>");
		
		return stbuf.toString();
	}
	
}
