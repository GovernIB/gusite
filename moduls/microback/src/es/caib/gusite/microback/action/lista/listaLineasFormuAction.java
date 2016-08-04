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
import es.caib.gusite.microback.actionform.formulario.lineaFormularioForm;

/**
 * Action que valida y trata el listado de lineas de un formulario de un microsite <BR>
 * <P>
 * 	Definición Struts:<BR>
 *  action path="/lineaFormuAcc" <BR> 
 *  name="listaActionForm" <BR> 
 *  scope="request" <BR>
 *  forward name="detalleLineaFormu" path="/detalleLineaFormulario.jsp"
 *  
 *  @author Indra
 */
public class listaLineasFormuAction extends BaseAction {

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

	protected static Log log = LogFactory.getLog(listaLineasFormuAction.class);
	
    public ActionForward doExecute(ActionMapping mapping, ActionForm form, 
    		HttpServletRequest request, HttpServletResponse response) throws Exception  {

        //********************************************************
        //************* ERROR DE VALIDACION **********************
        //********************************************************
        if (request.getSession().getAttribute("lineaFormularioForm")!=null && request.getAttribute(Globals.ERROR_KEY)!=null) {
        	lineaFormularioForm fdet=(lineaFormularioForm) request.getSession().getAttribute("lineaFormularioForm");
        	request.setAttribute("lineaFormularioForm", fdet);
        	request.setAttribute("validacion", "si");
        	return mapping.findForward("detalleLineaFormu");
        }
        
        addMessageError(request, "peticion.error");
        return mapping.findForward("info");

    }

}