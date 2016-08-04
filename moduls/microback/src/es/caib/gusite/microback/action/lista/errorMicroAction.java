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
import es.caib.gusite.microback.actionform.formulario.microForm;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.TiposervicioDelegate;

/**
 * Action que valida propiedades de un microsite <P>
 * 	Definición Struts:<BR>
 *  action path="/microAcc"<BR> 
 *  name="listaActionForm"<BR>
 *	scope="request" <BR>
 *  forward name="general" path="/microGeneral.jsp"
 *  
 *  @author - Indra
 */
public class errorMicroAction extends BaseAction {

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

	protected static Log log = LogFactory.getLog(errorMicroAction.class);
	
    public ActionForward doExecute(ActionMapping mapping, ActionForm form, 
    		HttpServletRequest request, HttpServletResponse response) throws Exception  {


    	/**************************************************************/
    	/********************* ERROR DE VALIDACION ********************/
    	/**************************************************************/
    	if (request.getSession().getAttribute("microForm")!=null && request.getAttribute(Globals.ERROR_KEY)!=null) {
    		microForm fdet=(microForm) request.getSession().getAttribute("microForm");
        	request.setAttribute("microForm", fdet);
            
            // UA viene de sesion
            request.setAttribute("uo","Unidad administrativa");

            // Lista Check de tipos de servicio que aparecerán
            TiposervicioDelegate bdTipo = DelegateUtil.getTiposervicioDelegate();
            request.setAttribute("tiposServicios", bdTipo.listarTipos());
            request.setAttribute("validacion", "si");
        	
            return mapping.findForward("general");
        }

        addMessage(request, "peticion.error");
        return mapping.findForward("info");

    }

}