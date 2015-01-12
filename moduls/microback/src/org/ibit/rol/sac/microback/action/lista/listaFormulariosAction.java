package org.ibit.rol.sac.microback.action.lista;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.ibit.rol.sac.microback.action.BaseAction;
import org.ibit.rol.sac.microback.actionform.listaActionForm;
import org.ibit.rol.sac.microback.actionform.formulario.formularioconForm;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;
import org.ibit.rol.sac.micropersistence.delegate.ContactoDelegate;

/**
 * Action que valida y trata el listado de formularios de un microsite <BR>
 * <P>
 * 	Definición Struts:<BR>
 *  action path="/formulariosAcc" <BR> 
 *  name="listaActionForm" <BR> 
 *  scope="request" <BR>
 *  forward name="detalleFormu" path="/detalleFormulario.jsp"<BR> 
 *  forward name="listarFormu" path="/formularios.do"
 *  
 *  @author Indra
 */
public class listaFormulariosAction extends BaseAction {

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

	protected static Log log = LogFactory.getLog(listaFormulariosAction.class);
	
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception  {

        listaActionForm listaActionForm = (listaActionForm) form;
        
        //********************************************************
        //*********** ERROR DE VALIDACION FORMUALRIO *************
        //********************************************************
        if (request.getSession().getAttribute("formularioconForm")!=null && request.getAttribute(Globals.ERROR_KEY)!=null) {
        	formularioconForm fdet=(formularioconForm) request.getSession().getAttribute("formularioconForm");
        	request.setAttribute("formularioconForm", fdet);
        	request.setAttribute("validacion", "si");
            return mapping.findForward("detalleFormu");
        }
        
        //********************************************************
        //********************** CREAMOS *************************
        //********************************************************
        if ( listaActionForm.getAccion().equals("crear")) {
        	request.getSession().removeAttribute("formularioconForm");
        	return mapping.findForward("detalleFormu");
        }
        
        //********************************************************
        //********************* BORRAMOS *************************
        //********************************************************
        if ( listaActionForm.getAccion().equals("borrar")) {
            Long id =null;
        	ContactoDelegate bdFormu = DelegateUtil.getContactoDelegate();
            
            String lis="";
            for (int i=0;i<listaActionForm.getSeleccionados().length;i++) {
                id = new Long(listaActionForm.getSeleccionados()[i]);
                bdFormu.borrarContacto(id);
                lis+=id+", ";
            }
            lis=lis.substring(0,lis.length()-2);
                       
            request.getSession().setAttribute("mensajeBorrarFormulario", new String(lis));
            
            return mapping.findForward("listarFormu");           
        }

        addMessage(request, "peticion.error");
        return mapping.findForward("info");

    }

}