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
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.UsuarioDelegate;

/**
 * Action que valida y trata el listado usuarios <BR>
 * <P>
 * 	Definición Struts:<BR>
 *  action path="/usuariosAcc" <BR> 
 *  name="listaActionForm" <BR> 
 *  scope="request" <BR>
 *  forward name="detalleUsuario" path="/detalleUsuario.jsp"
 *  
 *  @author Indra
 */
public class ListaUsuariosAction extends BaseAction {

	protected static Log log = LogFactory.getLog(ListaUsuariosAction.class);
	
    public ActionForward doExecute(ActionMapping mapping, ActionForm form, 
    		HttpServletRequest request, HttpServletResponse response) throws Exception  {

        listaActionForm f = (listaActionForm) form;
        
        //********************************************************
        //************* ERROR DE VALIDACION **********************
        //********************************************************
        if (request.getSession().getAttribute("usuarioForm")!=null && request.getAttribute(Globals.ERROR_KEY)!=null) {
        	ActionForm fdet=(ActionForm) request.getSession().getAttribute("usuarioForm");
        	request.setAttribute("usuarioForm", fdet);
            return mapping.findForward("detalleUsuario");
        }
        //********************************************************
        //********************** CREAMOS *************************
        //********************************************************
        if ( f.getAccion().equals("crear")) {
        	request.getSession().removeAttribute("usuarioForm");
        	return mapping.findForward("detalleUsuario");
        }
        //********************************************************
        //********************* BORRAMOS *************************
        //********************************************************
        if ( f.getAccion().equals("borrar")) {
            Long id = new Long ("" + request.getParameter("id"));
        	UsuarioDelegate bdUsuari = DelegateUtil.getUsuarioDelegate();
            
            bdUsuari.borrarUsuario(id);
            
            addMessage(request, "mensa.listausuaris");
            addMessage(request, "mensa.listausuariborrados", "" + id.longValue());
            
            return mapping.findForward("infoscreen");
        }

        addMessage(request, "peticion.error");
        return mapping.findForward("infoscreen");

    }
	
}
