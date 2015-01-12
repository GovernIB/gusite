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
 * Clase InvalidRolAction. Se ejecuta cuando se realiza un acceso invalido.
 * @author Indra
 *
 */
public class InvalidRolAction extends BaseAction {

	public static final String INVALID_SESSION_ROL = "rolInvalid";
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, Exception {
		  
		request.getSession().setAttribute(INVALID_SESSION_ROL, true);
		 return mapping.findForward(getForwardError(request, ErrorMicrosite.ERROR_AMBIT_ACCES));
		 
	}
	
}
