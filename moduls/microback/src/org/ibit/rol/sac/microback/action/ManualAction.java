package org.ibit.rol.sac.microback.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Action que visualiza el manual de usuario o las píldoras informativas <P>
 * 
 * 	Definición Struts:<BR>
 *  action path="/manual"<BR> 
 *  unknown="false" <BR>
 *  forward name="mostramanual" path="/manual.jsp" <BR>
 *  forward name="listarpildoras" path="/listarpildoras.jsp" 
 *  
 *  @author - Indra
 */
public class ManualAction extends BaseAction  {

	protected static Log log = LogFactory.getLog(ManualAction.class);
	
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception {
	
	
		String elforward="mostramanual";
		
		String tipo = "" + request.getParameter("tipo");
		if (tipo.equals("pildora")) 
				elforward="listarpildoras";
			
		
        return mapping.findForward(elforward);
	
	}	
	
}
