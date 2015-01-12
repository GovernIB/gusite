package es.caib.gusite.microback.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Action que redirige al indexador<P>
 * 
 * 	Definición Struts:<BR>
 *  action path="/indexador"<BR> 
 *  unknown="false" <BR>
 *  forward name="indexador" path="/indexador.jsp"
 *  
 *  @author - Indra
 */
public class IndexadorAction extends BaseAction {

	
	protected static Log log = LogFactory.getLog(IndexadorAction.class);
	
	
	public ActionForward doExecute(ActionMapping mapping, ActionForm form, 
    		HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		String elforward="indexador";
        return mapping.findForward(elforward);
	
	}	
	
}
