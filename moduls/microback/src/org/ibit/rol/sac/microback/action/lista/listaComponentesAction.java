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
import org.ibit.rol.sac.microback.actionform.formulario.componenteForm;
import org.ibit.rol.sac.micromodel.Microsite;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;
import org.ibit.rol.sac.micropersistence.delegate.ComponenteDelegate;
import org.ibit.rol.sac.micropersistence.delegate.TipoDelegate;

/**
 * Action que valida y trata el listado de componentes de un microsite <BR>
 * <P>
 * 	Definición Struts:<BR>
 *  action path="/componentesAcc" <BR> 
 *  name="listaActionForm" <BR> 
 *  scope="request" <BR>
 *  forward name="detalleCompo" path="/detalleComponente.jsp"<BR> 
 *  forward name="listarComponentes" path="/componentes.do"
 *  
 *  @author Indra
 */
public class listaComponentesAction extends BaseAction {

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

	protected static Log log = LogFactory.getLog(listaComponentesAction.class);
	
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception  {

        listaActionForm f = (listaActionForm) form;
        
     
        //********************************************************
        //************* ERROR DE VALIDACION **********************
        //********************************************************
        if (request.getSession().getAttribute("componenteForm")!=null && request.getAttribute(Globals.ERROR_KEY)!=null) {
        	componenteForm fdet=(componenteForm) request.getSession().getAttribute("componenteForm");
        	request.setAttribute("componenteForm", fdet);
            // Relleno el combo de Tipos de Noticias
            TipoDelegate bdTipo = DelegateUtil.getTipoDelegate();
            request.setAttribute("tiposCombo", bdTipo.listarCombo(((Microsite)request.getSession().getAttribute("MVS_microsite")).getId()));
            request.setAttribute("validacion", "si");
            return mapping.findForward("detalleCompo");
        }
        //********************************************************
        //********************** CREAMOS *************************
        //********************************************************
        if ( f.getAccion().equals("crear")) {
            // Relleno el combo de Tipos de Noticias
            TipoDelegate bdTipo = DelegateUtil.getTipoDelegate();
            request.setAttribute("tiposCombo", bdTipo.listarCombo(((Microsite)request.getSession().getAttribute("MVS_microsite")).getId()));
        	request.getSession().removeAttribute("componenteForm");
        	return mapping.findForward("detalleCompo");
        }
        //********************************************************
        //********************* BORRAMOS *************************
        //********************************************************
        if ( f.getAccion().equals("borrar")) {
            Long id = null;
            ComponenteDelegate bdCompo = DelegateUtil.getComponentesDelegate();
            
            String lis="";
            for (int i=0;i<f.getSeleccionados().length;i++) {
                id = new Long(f.getSeleccionados()[i]);
                bdCompo.borrarComponente(id);
                lis+=id+", ";
            }
            lis=lis.substring(0,lis.length()-2);
            
            request.getSession().setAttribute("mensajeBorrarComponente", new String(lis));
            
            return mapping.findForward("listarComponentes");
        }

        addMessageError(request, "peticion.error");
        return mapping.findForward("info");

    }

}