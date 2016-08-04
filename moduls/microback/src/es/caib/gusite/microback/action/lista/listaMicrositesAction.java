package es.caib.gusite.microback.action.lista;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.RedirectingActionForward;

import es.caib.gusite.microback.action.BaseAction;
import es.caib.gusite.microback.actionform.TraDynaActionForm;
import es.caib.gusite.microback.actionform.listaActionForm;

/**
 * Action que valida y trata el listado de microsites <BR>
 * <P>
 * 	Definición Struts:<BR>
 *  action path="/micrositesAcc" <BR> 
 *  name="listaActionForm" <BR> 
 *  scope="request" <BR>
 *  forward name="detalleMicrosite" path="/index.jsp"<BR> 
 *  forward name="crearMicrosite" path="/home.do"
 *  
 *  @author Indra
 */
public class listaMicrositesAction extends BaseAction {

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

	protected static Log log = LogFactory.getLog(listaMicrositesAction.class);
	
    public ActionForward doExecute(ActionMapping mapping, ActionForm form, 
    		HttpServletRequest request, HttpServletResponse response) throws Exception  {

        listaActionForm f = (listaActionForm) form;
        
        //********************************************************
        //************* ERROR DE VALIDACION **********************
        //********************************************************
        if (request.getSession().getAttribute("microForm")!=null && request.getAttribute(Globals.ERROR_KEY)!=null) {
        	TraDynaActionForm fdet=(TraDynaActionForm) request.getSession().getAttribute("microForm");
        	request.setAttribute("microForm", fdet);
            return mapping.findForward("detalleMicrosite");
        }
        //********************************************************
        //********************** CREAMOS *************************
        //********************************************************
        if ( f.getAccion().equals("crear")) {
        	request.getSession().removeAttribute("microForm");
	        ActionForward elforward = new RedirectingActionForward();
	        elforward.setPath("home.do?accion=alta");
	        return elforward;
        }
        //********************************************************
        //********************* BORRAMOS *************************
        //********************************************************
        if ( f.getAccion().equals("borrar")) {
        	request.getSession().removeAttribute("microForm");
   	        ActionForward elforward = new RedirectingActionForward();
   	        elforward.setPath("home.do?accion=eliminar&idsite="+f.getSeleccionados()[0]);
   	        return elforward;
        }
        //********************************************************
        //******************** EXPORTAMOS ************************
        //********************************************************
        if ( f.getAccion().equals("exportar")) {
        	request.getSession().removeAttribute("microForm");
   	        ActionForward elforward = new RedirectingActionForward();
   	        elforward.setPath("home.do?accion=exportar&idsite="+f.getSeleccionados()[0]);
   	        return elforward;
        }
        //********************************************************
        //******************** IMPORTAMOS ************************
        //********************************************************
        if ( f.getAccion().equals("importar")) {
        	request.getSession().removeAttribute("microForm");
	        ActionForward elforward = new RedirectingActionForward();
	        elforward.setPath("home.do?accion=importar");
	        return elforward;
        }
        //********************************************************
        //********************** CONSOLA *************************
        //********************************************************
        if ( f.getAccion().equals("consola")) {
        	request.getSession().removeAttribute("microForm");
	        ActionForward elforward = new RedirectingActionForward();
	        elforward.setPath("home.do?accion=consola");
	        return elforward;
        }
        
        addMessage(request, "peticion.error");
        return mapping.findForward("info");

    }

}