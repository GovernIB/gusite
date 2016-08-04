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
import es.caib.gusite.microback.actionform.TraDynaActionForm;
import es.caib.gusite.microback.actionform.listaActionForm;
import es.caib.gusite.micropersistence.delegate.ActividadDelegate;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;


/**
 * Action que valida y trata el listado de actividades de agenda de un microsite <BR>
 * <P>
 * 	Definición Struts:<BR>
 *  action path="/actividadesAcc" <BR> 
 *  name="listaActionForm" <BR> 
 *  scope="request" <BR>
 *  forward name="detalleActivi" path="/detalleActividad.jsp"<BR> 
 *  forward name="listarActividades" path="/actividades.do"
 *  
 *  @author Indra
 */
public class listaActividadesAction extends BaseAction {

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

	protected static Log log = LogFactory.getLog(listaNoticiasAction.class);
	
    public ActionForward doExecute(ActionMapping mapping, ActionForm form, 
    		HttpServletRequest request, HttpServletResponse response) throws Exception  {

        listaActionForm f = (listaActionForm) form;
        
        //********************************************************
        //************* ERROR DE VALIDACION **********************
        //********************************************************
        if (request.getSession().getAttribute("actividadForm")!=null && request.getAttribute(Globals.ERROR_KEY)!=null) {
        	TraDynaActionForm fdet=(TraDynaActionForm) request.getSession().getAttribute("actividadForm");
        	request.setAttribute("actividadForm", fdet);
        	request.setAttribute("validacion", "si");
            return mapping.findForward("detalleActivi");
        }
        //********************************************************
        //********************** CREAMOS *************************
        //********************************************************
        if ( f.getAccion().equals("crear")) {
        	request.getSession().removeAttribute("actividadForm");
        	return mapping.findForward("detalleActivi");
        }
        //********************************************************
        //********************* BORRAMOS *************************
        //********************************************************
        if ( f.getAccion().equals("borrar")) {

            try{
                Long id =null;
                ActividadDelegate bdActivi = DelegateUtil.getActividadagendaDelegate();
	            String lis="";
	            for (int i=0;i<f.getSeleccionados().length;i++) {
	                id = new Long(f.getSeleccionados()[i]);
	                bdActivi.borrarActividad(id);
	                lis+=id+", ";
	            }
	            lis=lis.substring(0,lis.length()-2);
	            
	            request.getSession().setAttribute("mensajeBorrarActividad", new String(lis));
	  
	            return mapping.findForward("listarActividades");
		    	
            } catch (Exception e) {
	    		request.getSession().setAttribute("elementosNoBorrados", "si");	
		      	return mapping.findForward("listarActividades");	
		   }
        }

        addMessageError(request, "peticion.error");
        return mapping.findForward("info");

    }

}