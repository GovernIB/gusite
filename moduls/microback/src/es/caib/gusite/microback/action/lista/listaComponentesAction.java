package es.caib.gusite.microback.action.lista;

import java.util.ArrayList;

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
import es.caib.gusite.microback.actionform.formulario.componenteForm;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.Tipo;
import es.caib.gusite.micropersistence.delegate.ComponenteDelegate;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.TipoDelegate;

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
	
    public ActionForward doExecute(ActionMapping mapping, ActionForm form, 
    		HttpServletRequest request, HttpServletResponse response) throws Exception  {

        listaActionForm f = (listaActionForm) form;
      
        //los tipos 
        ArrayList<String> tiposNoIncluidosCompo = new ArrayList<String>();
		tiposNoIncluidosCompo.add(Tipo.TIPO_MAPA);
     
        //********************************************************
        //************* ERROR DE VALIDACION **********************
        //********************************************************
        if (request.getSession().getAttribute("componenteForm")!=null && request.getAttribute(Globals.ERROR_KEY)!=null) {
        	componenteForm fdet=(componenteForm) request.getSession().getAttribute("componenteForm");
        	request.setAttribute("componenteForm", fdet);
            // Relleno el combo de Tipos de Noticias
            TipoDelegate bdTipo = DelegateUtil.getTipoDelegate();
            request.setAttribute("tiposCombo", bdTipo.listarCombo(((Microsite)request.getSession().getAttribute("MVS_microsite")).getId(),tiposNoIncluidosCompo));
            request.setAttribute("validacion", "si");
            return mapping.findForward("detalleCompo");
        }
        //********************************************************
        //********************** CREAMOS *************************
        //********************************************************
        if ( f.getAccion().equals("crear")) {
            // Relleno el combo de Tipos de Noticias
            TipoDelegate bdTipo = DelegateUtil.getTipoDelegate();
            request.setAttribute("tiposCombo", bdTipo.listarCombo(((Microsite)request.getSession().getAttribute("MVS_microsite")).getId(),tiposNoIncluidosCompo));
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