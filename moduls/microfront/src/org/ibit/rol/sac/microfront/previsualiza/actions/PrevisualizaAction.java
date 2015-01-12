package org.ibit.rol.sac.microfront.previsualiza.actions;
  
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.ibit.rol.sac.microfront.BaseAction;
import org.ibit.rol.sac.microfront.base.bean.ErrorMicrosite;

/**
 *  Action para previsualizar <P>
 *  Definición Struts:<BR>
 *  action path="/previsualiza" <BR> 
 *  unknown="false" <BR>
 *  forward name="previsualiza" path="/v1/previsualiza/previsualiza.jsp"<BR>
 * 
 * @author Indra
 *
 */
public class PrevisualizaAction extends BaseAction    {

	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		  
		String elsite="" + request.getParameter("idsite");
		
		if (!elsite.equals("null")) { 
		
		request.getSession().setAttribute("misite",elsite);
	  	request.getSession().setAttribute("previsualiza","si");
		return mapping.findForward("previsualiza");
		
		} else {
        	return mapping.findForward(getForwardError (request, ErrorMicrosite.ERROR_AMBIT_MICRO));
		}
		
	  }
	
}
