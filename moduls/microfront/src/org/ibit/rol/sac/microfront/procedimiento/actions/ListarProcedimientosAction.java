package org.ibit.rol.sac.microfront.procedimiento.actions;

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
import org.ibit.rol.sac.microfront.procedimiento.util.Bdlistaprocedimientos;
import org.ibit.rol.sac.micromodel.Microsite;
import org.ibit.rol.sac.micromodel.TraduccionMicrosite;

/**
 *  Action para listar procedimientos <P>
 *  Definición Struts:<BR>
 *  action path="/procedimientos" <BR> 
 *  unknown="false" <BR>
 *  forward name="listarprocedimientosv4" path="/v4/procedimiento/listarprocedimientos.jsp" <BR>
 *  forward name="listarprocedimientosv1" path="/v1/procedimiento/listarprocedimientos.jsp"
 *  
 *  @author - Indra
 */
public class ListarProcedimientosAction  extends BaseAction {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	  	String forwardlocal="listarprocedimientos";
	  
	    try {
	  	
		  	Bdlistaprocedimientos bdlistaprocedimientos = new Bdlistaprocedimientos(request, form);
		    if (!bdlistaprocedimientos.isError()) {
			    request.setAttribute("MVS_listado", bdlistaprocedimientos.getListaprocedimientosfinal());
			    request.setAttribute("MVS_idioma_x", bdlistaprocedimientos.getIdioma().toLowerCase());
		    
		    	/*** SIEMPRE FIJAS para version 4**/
		    	request.setAttribute("MVS2_mollapan", mollapan(request, bdlistaprocedimientos.getIdioma()));
		    	menucaib(request, bdlistaprocedimientos.getIdioma());
		    	/*** FIN SIEMPRE **/
			    
			    //Pegote de la estructura a seguir. Campo restringido, N=azules, S=azules, 2=blancos.
			    //OJO: es la estructura y no la hoja de estilos.
			    Microsite microsite = new Microsite();
			    microsite = (Microsite)request.getSession().getAttribute("MVS_microsite");
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
		
		stbuf.append("<li><a href=\"http://www.caib.es\">" + rb.getString("general.inicio") + "</a></li>");
		if (!(mvs_ua.equals("null")) && (mvs_ua.length()>0))
			stbuf.append("<li>&gt; " + mvs_ua + "</li>");
		//añado el titulo del microsite
		Microsite microsite = (Microsite)request.getSession().getAttribute("MVS_microsite");
		String titulo_mic = ((TraduccionMicrosite)microsite.getTraduccion(idi)!=null)?((TraduccionMicrosite)microsite.getTraduccion(idi)).getTitulo():"&nbsp;";
		stbuf.append("<li>&gt; <a href=\"home.do?mkey="+microsite.getClaveunica()+"&lang=" + idi + "\">" + titulo_mic+ " </a></li>");
		stbuf.append("<li>&gt; " + rb.getString("listarprocs.procedimientos") + "</li>");
		
		return stbuf.toString();
	}	
	
}
