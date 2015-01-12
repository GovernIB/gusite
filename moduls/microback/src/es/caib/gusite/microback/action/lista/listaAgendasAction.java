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
import es.caib.gusite.microback.actionform.listaActionForm;
import es.caib.gusite.microback.actionform.formulario.agendaForm;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micropersistence.delegate.ActividadDelegate;
import es.caib.gusite.micropersistence.delegate.AgendaDelegate;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;

/**
 * Action que valida y trata el listado de agendas (eventos) de un microsite <BR>
 * <P>
 * 	Definición Struts:<BR>
 *  action path="/agendasAcc" <BR> 
 *  name="listaActionForm" <BR> 
 *  scope="request" <BR>
 *  forward name="detalleAgenda" path="/detalleAgenda.jsp"<BR> 
 *  forward name="listarAgendas" path="/agendas.do"
 *  
 *  @author Indra
 */
public class listaAgendasAction extends BaseAction {

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

	protected static Log log = LogFactory.getLog(listaAgendasAction.class);
	
    public ActionForward doExecute(ActionMapping mapping, ActionForm form, 
    		HttpServletRequest request, HttpServletResponse response) throws Exception  {

        listaActionForm listActionForm = (listaActionForm) form;
    	
       
        //********************************************************
        //************* ERROR DE VALIDACION **********************
        //********************************************************
        if (request.getSession().getAttribute("agendaForm")!=null && request.getAttribute(Globals.ERROR_KEY)!=null) {
        	agendaForm fdet=(agendaForm) request.getSession().getAttribute("agendaForm");
        	request.setAttribute("agendaForm", fdet);
            // Relleno el combo de Actividades
        	 ActividadDelegate bdActivi = DelegateUtil.getActividadagendaDelegate();
            request.setAttribute("actividadesCombo", bdActivi.listarCombo(((Microsite)request.getSession().getAttribute("MVS_microsite")).getId()));
            request.setAttribute("validacion", "si");
            return mapping.findForward("detalleAgenda");
        }
        //********************************************************
        //********************** CREAMOS *************************
        //********************************************************
        if ( listActionForm.getAccion().equals("crear")) {
            // Relleno el combo de Actividades
        	ActividadDelegate bdActivi = DelegateUtil.getActividadagendaDelegate();
            request.setAttribute("actividadesCombo", bdActivi.listarCombo(((Microsite)request.getSession().getAttribute("MVS_microsite")).getId()));
        	request.getSession().removeAttribute("agendaForm");
        	return mapping.findForward("detalleAgenda");
        }
        //********************************************************
        //********************* BORRAMOS *************************
        //********************************************************
        if ( listActionForm.getAccion().equals("borrar")) {
            Long id =null;
            AgendaDelegate bdAgenda = DelegateUtil.getAgendaDelegate();
        	
            String lis="";
	        for (int i=0;i<listActionForm.getSeleccionados().length;i++) {
	        	id = new Long(listActionForm.getSeleccionados()[i]);
	            bdAgenda.borrarAgenda(id);
	            lis+=id+", ";
	        }
	        lis=lis.substring(0,lis.length()-2);
	          
	        request.getSession().setAttribute("mensajeBorrarAgenda", new String(lis));
	          
	        return mapping.findForward("listarAgendas");
        }

        addMessage(request, "peticion.error");
        return mapping.findForward("info");

    }

}