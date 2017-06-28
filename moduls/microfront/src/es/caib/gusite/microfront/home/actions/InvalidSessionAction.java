package es.caib.gusite.microfront.home.actions;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.gusite.microfront.base.bean.ErrorMicrosite;
import es.caib.gusite.microfront.BaseAction;

/**
 * Class InvalidSessionAction. S'executa quan la sessió ha experirat
 *
 */
public class InvalidSessionAction extends BaseAction {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, Exception {
		 
		request.getSession().invalidate();
		return mapping.findForward(getForwardError(request, ErrorMicrosite.ERROR_AMBIT_SESSIO, response));
		 
	}
	
}
