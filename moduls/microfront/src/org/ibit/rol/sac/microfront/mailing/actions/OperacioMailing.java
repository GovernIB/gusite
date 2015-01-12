package org.ibit.rol.sac.microfront.mailing.actions;

import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.ibit.rol.sac.microfront.BaseAction;
import org.ibit.rol.sac.microfront.Microfront;
import org.ibit.rol.sac.microfront.base.bean.ErrorMicrosite;
import org.ibit.rol.sac.microfront.exception.ExceptionFrontMicro;
import org.ibit.rol.sac.microfront.exception.ExceptionFrontPagina;
import org.ibit.rol.sac.microfront.mailing.util.Bdsuscripcion;
import org.ibit.rol.sac.microfront.util.correo.CorreoEngineService;
import org.ibit.rol.sac.micromodel.ListaDistribucion;
import org.ibit.rol.sac.micromodel.Microsite;
import org.ibit.rol.sac.micromodel.TraduccionMicrosite;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;
import org.ibit.rol.sac.micropersistence.delegate.LDistribucionDelegate;

/**
 *  Action para suscribirse o darse de baja en una lista de distribucion <P>
 *  Definición Struts:<BR>
 *  action path="/operacioMailing" <BR> 
 *  unknown="false" <BR>
 *  forward name="contenidov4" path="/v4/usuariomsg/msggenerico.jsp"<BR>
 * 	forward name="contenidov1" path="/v1/usuariomsg/msggenerico.jsp"
 */	
public class OperacioMailing extends BaseAction   {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String forwardlocal="contenido";
	  	Bdsuscripcion bdSuscripcion = null;
	  	ResourceBundle rb = ResourceBundle.getBundle("ApplicationResources_front", request.getLocale());
		try {
			bdSuscripcion = new Bdsuscripcion(request);
			StringBuffer body = new StringBuffer();
			String assumpte = "";
			
			if ("alta".equals(request.getParameter(Microfront.PACTION))){					
				bdSuscripcion.alta();
				assumpte = rb.getString("mailing.assumpte.alta");
				body.append(rb.getString("mailing.body.alta"));
			}else if("baixa".equals(request.getParameter(Microfront.PACTION))){
				bdSuscripcion.baixa();
				assumpte = rb.getString("mailing.assumpte.baixa");
				body.append(rb.getString("mailing.body.baixa"));
			}
			Microsite microsite = bdSuscripcion.getMicrosite();
			if(microsite.getIdiomas().contains(request.getLocale().toString()))
				body.append(((TraduccionMicrosite)microsite.getTraduccion(request.getLocale().toString())).getTitulo());
			else
				body.append(((TraduccionMicrosite)microsite.getTraduce()).getTitulo());
			
			CorreoEngineService mail = new CorreoEngineService();
			mail.initCorreo(request.getParameter(Microfront.PEMAIL), assumpte, false, body);
			mail.enviarCorreo();
			
			if ( (microsite.getRestringido().equals("N")) || (microsite.getRestringido().equals("S")) )  
		    	return mapping.findForward(forwardlocal+"v1");
	    	else
		    	return mapping.findForward(forwardlocal+"v4");			
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
	
}
