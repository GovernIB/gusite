package es.caib.gusite.microback.action.lista;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.gusite.microback.action.BaseAction;
import es.caib.gusite.microback.actionform.formulario.menuForm;

/**
 * Action que valida campos de menu de un microsite <P>
 * 	Definición Struts:<BR>
 *  action path="/menuAcc"<BR> 
 *  name="listaActionForm"<BR>
 *	scope="request" <BR>
 *  forward name="detalle" path="/detalleMenu.jsp"
 *  
 *  @author - Indra
 */
public class errorMenuAction extends BaseAction {

    /**
     * This is the main action called from the Struts framework.
     * @param mapping The ActionMapping used to select this instance.
     * @param form The optional ActionForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     * @return 
     */

	protected static Log log = LogFactory.getLog(errorMenuAction.class);
	
    public ActionForward doExecute(ActionMapping mapping, ActionForm form, 
    		HttpServletRequest request, HttpServletResponse response) throws Exception  {

      	
    	/********************************************************/
        /************* ERROR DE VALIDACION **********************/
        /********************************************************/
        if (request.getSession().getAttribute("menuForm")!=null && request.getAttribute(Globals.ERROR_KEY)!=null) {
        	menuForm fdet=(menuForm) request.getSession().getAttribute("menuForm");
        	request.setAttribute("menuForm", fdet);
        	return mapping.findForward("detalle");
        }

        addMessage(request, "peticion.error");
        return mapping.findForward("info");

    }

}