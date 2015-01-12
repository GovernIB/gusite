package es.caib.gusite.microback.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Action que visualiza los servicios del microsite <P>
 * 
 * 	Definición Struts:<BR>
 *  action path="/listarServicios"<BR> 
 *  unknown="false" <BR>
 *  forward name="listarservicios" path="/listarservicios.jsp" 
 *  
 *  @author - Indra
 */
public class ServiciosAction  extends Action{

	
	protected static Log log = LogFactory.getLog(ServiciosAction.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String elforward="listarservicios";
		
        return mapping.findForward(elforward);
	}
	
}
