package es.caib.gusite.microback.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Action que redirige al exportador<P>
 * 
 * 	Definición Struts:<BR>
 *  action path="/exportador"<BR> 
 *  unknown="false" <BR>
 *  forward name="exportador" path="/exportador.jsp"
 *  
 *  @author - Indra
 */
public class ExportadorAction  extends BaseAction {

	protected static Log log = LogFactory.getLog(ExportadorAction.class);
	
	public ActionForward doExecute(ActionMapping mapping, ActionForm form, 
    		HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		String elforward="exportador";
        return mapping.findForward(elforward);
	
	}
	
}
