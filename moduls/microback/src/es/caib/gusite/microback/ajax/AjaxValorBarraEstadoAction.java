package es.caib.gusite.microback.ajax;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.gusite.microback.action.BaseAction;
import es.caib.gusite.utilities.statusbar.StatusBar;
import es.caib.gusite.utilities.statusbar.StatusBarFactory;

/**
 * Action que simplemente se encarga de devolver el valor de un statusbar.<br>
 * 
 * @author Indra
 *
 */
public class AjaxValorBarraEstadoAction extends BaseAction {

	protected static Log log = LogFactory.getLog(AjaxValorBarraEstadoAction.class);
	
	public ActionForward doExecute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception {
		
		/*
		String idstatusbar = "" + request.getParameter("idstatusbar");
		if (!idstatusbar.equals("null")) {
			StatusBar barra = StatusBarFactory.obtenerStatusBar(idstatusbar);
			if ((barra!=null) && (barra.getActualitem()!=null) ) request.setAttribute(Microback.SVS_AJAX_RESPUETA, "" + barra.getPorcentaje());
			else request.setAttribute(Microback.SVS_AJAX_RESPUETA, "error");
		} else {
			request.setAttribute(Microback.SVS_AJAX_RESPUETA, "error");
		}
		
		return mapping.findForward("respuesta");
		*/
		
		/*
		 * <?xml version="1.0" encoding="utf-8"?>
<datos>
<dato>100</dato>
</datos>
		 */
		
		String txtrespuesta="<?xml version=\"1.0\" encoding=\"utf-8\"?>";
		
		String idstatusbar = "" + request.getParameter("idstatusbar");
		if (!idstatusbar.equals("null")) {
			StatusBar barra = StatusBarFactory.obtenerStatusBar(idstatusbar);
			if ((barra!=null) && (barra.getActualitem()!=null) ) txtrespuesta += "<datos><dato>" + barra.getPorcentaje() + "</dato></datos>";
		} else {
			txtrespuesta+="<datos><dato>error</dato></datos>";
		}
		response.reset();
		response.setContentType("text/xml;charset=utf-8");
        response.setHeader("Content-Disposition", "inline; filename=ajaxBarraEstado");
		response.setContentLength(txtrespuesta.getBytes().length);
		response.getOutputStream().write(txtrespuesta.getBytes());
		return null;
	}
		
		
}
