package es.caib.gusite.microback.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.gusite.utilities.statusbar.StatusBar;
import es.caib.gusite.utilities.statusbar.StatusBarFactory;

/**
 * Action que visualiza la barra de estado en el testeo del proceso W3C <P>
 * 
 * 	Definición Struts:<BR>
 *  action path="/procesow3ctesteandopintastatus"<BR> 
 *  unknown="false" <BR>
 *  forward name="detallestatusbar" path="/procesow3ctesteandopintastatus.jsp" 
 *  
 *  @author - Indra
 */
public class ProcesoW3CTesteandoValorStatusBar extends BaseAction {

	protected static Log log = LogFactory.getLog(ProcesoW3CTesteandoValorStatusBar.class);
	
	public ActionForward doExecute(ActionMapping mapping, ActionForm form, 
    		HttpServletRequest request, HttpServletResponse response) throws Exception {
		String idstatusbar = "" + request.getParameter("idstatusbar");
		if (!idstatusbar.equals("null")) {
			StatusBar barra = StatusBarFactory.obtenerStatusBar(idstatusbar);
			if ((barra!=null) && (barra.getActualitem()!=null) ) request.setAttribute("MVS_valorbarraestado", barra.getPorcentaje());
			else request.setAttribute("MVS_valorbarraestado", "0");
			request.setAttribute("MVS_idbarraestado", idstatusbar);
		}
		
		return mapping.findForward("detallestatusbar");
	}
	
}
