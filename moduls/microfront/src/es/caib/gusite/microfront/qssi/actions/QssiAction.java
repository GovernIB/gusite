package es.caib.gusite.microfront.qssi.actions;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.gusite.microfront.base.bean.ErrorMicrosite;
import es.caib.gusite.microfront.BaseAction;
import es.caib.gusite.microfront.exception.ExceptionFrontMicro;
import es.caib.gusite.microfront.exception.ExceptionFrontPagina;
import es.caib.gusite.microfront.qssi.util.BdQssi;
import es.caib.gusite.micromodel.Frqssi;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.TraduccionFrqssi;
import es.caib.gusite.micromodel.TraduccionMicrosite;

/**
 *  Action que consulta un QSSI <P>
 *  Definición Struts:<BR>
 *  action path="/qssi" <BR> 
 *  unknown="false" <BR>
 *  forward name="contenidov4" path="/v4/qssi/qssi.jsp" <BR>
 *  forward name="contenidov1" path="/v1/qssi/qssi.jsp"
 *  
 *  @author - Indra
 */
public class QssiAction extends BaseAction   {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		  	String forwardlocal="contenido";

		  	try {

			  	BdQssi bdqssi = new BdQssi(request);
		  		
			  	if (!bdqssi.isError()) {
	
			    	request.setAttribute("MVS_qssi", bdqssi.getqssi());
			    	forwardlocal="contenido";
			    
			    	/*** SIEMPRE FIJAS para version 4**/
			    	String titol=((TraduccionFrqssi)bdqssi.getqssi().getTraduccion(bdqssi.getIdioma())).getNombre();
			    	request.setAttribute("MVS2_mollapan", mollapan(request, bdqssi.getIdioma(),titol));
			    	menucaib(request, bdqssi.getIdioma());
			    	/*** FIN SIEMPRE **/

			    	/* agarcia: esto ha funcionado alguna vez? No veo este bundle en el classpath del front.
			    	 ResourceBundle rb =	ResourceBundle.getBundle("sac-microback-messages");
					 String Urlqssi=(String)rb.getObject("frqssi.url");
			    	 */
			    	 String Urlqssi= System.getProperty("es.caib.gusite.frqssi.url");
			    	
					 Frqssi qssi = bdqssi.getqssi();
					 String laurl;
					 if (qssi.getCentro()!= null && qssi.getTipoescrito()!= null){
						  laurl = Urlqssi+"&centre="+qssi.getCentro()+"&tipus_escrit="+qssi.getTipoescrito()+"&asunto="+((TraduccionFrqssi)qssi.getTraduce()).getNombre()+"?idioma="+bdqssi.getIdioma();
					 } else{ 
						 if (qssi.getCentro()!= null) {
						 laurl = Urlqssi+"&centre="+qssi.getCentro()+"&asunto="+((TraduccionFrqssi)qssi.getTraduce()).getNombre()+"?idioma="+bdqssi.getIdioma();
						 } else  laurl = Urlqssi+"&asunto="+((TraduccionFrqssi)qssi.getTraduce()).getNombre()+"?idioma="+bdqssi.getIdioma();
					 }
		 			 request.setAttribute("laurl",laurl);
		 			 
				    //Pegote de la estructura a seguir. Campo restringido, N=azules, S=azules, 2=blancos.
				    //OJO: es la estructura y no la hoja de estilos.
			    	Microsite microsite = (Microsite)request.getSession().getAttribute("MVS_microsite");
				    if ( (microsite.getRestringido().equals("N")) || (microsite.getRestringido().equals("S")) )  
				    	return mapping.findForward(forwardlocal+"v1");
			    	else
				    	return mapping.findForward(forwardlocal+"v4");
	    
			    } else {
			    	return mapping.findForward(getForwardError (request, ErrorMicrosite.ERROR_AMBIT_PAGINA, response));
				}
				    
	        } catch (ExceptionFrontPagina efp) {
	        	log.error(efp.getMessage());
	        	return mapping.findForward(getForwardError (request, ErrorMicrosite.ERROR_AMBIT_PAGINA, response));

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