package es.caib.gusite.microback.action.util;

import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.gusite.microback.action.BaseAction;
import es.caib.gusite.micromodel.Usuario;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.EstadisticaDelegate;
import es.caib.gusite.micropersistence.delegate.MicrositeDelegate;
import es.caib.gusite.micropersistence.delegate.UsuarioDelegate;

/**
 * Action que elimina un microsite <P>
 * 	Definición Struts:<BR>
 *  action path="eliminarmicro"<BR> 
 *	scope="request" <BR>
 *  unknown="false"
 *  
 *  @author - Indra
 */
public class EliminarAction extends BaseAction {

	protected static Log log = LogFactory.getLog(EliminarAction.class);
	private static String[] roles = new String[]{"gussystem", "gusadmin"};
	protected String mensaje="";
	
	@SuppressWarnings("unchecked")
	public ActionForward doExecute(ActionMapping mapping, ActionForm form, 
    		HttpServletRequest request, HttpServletResponse response) throws Exception {

		Hashtable<String,String> rolenames=null;
    	// recoger usuario.....
    	if (request.getSession().getAttribute("MVS_usuario")==null) {
        	UsuarioDelegate usudel=DelegateUtil.getUsuarioDelegate();
        	Usuario usu = usudel.obtenerUsuariobyUsername(request.getRemoteUser());
        	request.getSession().setAttribute("MVS_usuario", usu);	
    	}
    	if (request.getSession().getAttribute("rolenames")==null) {
           	if (request.getRemoteUser() != null) {
        		request.getSession().setAttribute("username", request.getRemoteUser());
                rolenames = new Hashtable<String,String>();
                for (int i = 0; i < roles.length; i++) 
                    if (request.isUserInRole(roles[i])) rolenames.put(roles[i],roles[i]);
                request.getSession().setAttribute("rolenames", rolenames);
            }        
    	}
    	
		// Solo podrán eliminar los roles gussystem y gusadmin
		rolenames=(Hashtable<String,String>)request.getSession().getAttribute("rolenames");

		if (!(rolenames.contains(roles[0]) || rolenames.contains(roles[1]))) {
			addMessage(request, "peticion.error");
            return mapping.findForward("info");
		}

		if (request.getParameter("idsite")!=null) {
    		Long idmicrosite= new Long(""+request.getParameter("idsite"));
		
    		try {
    			MicrositeDelegate bdMicro = DelegateUtil.getMicrositeDelegate();
    			bdMicro.borrarMicrositeCompleto(idmicrosite);
    			// Eliminamos sus estadisticas
    			EstadisticaDelegate estdel=DelegateUtil.getEstadisticaDelegate();
    			estdel.borrarEstadisticasMicroSite(idmicrosite);
    			
        		addMessage(request, "micro.eliminar");
    		}
    		catch (Exception ex) {
    			addMessage(request, "error.micro.eliminar",ex.toString());	
    		}
		}
    	
       	es.caib.gusite.microback.base.Base.borrarVSession(request);
		request.setAttribute("MVS_index_con_info", "yes");
		
    	return mapping.findForward("info");
	}

}
