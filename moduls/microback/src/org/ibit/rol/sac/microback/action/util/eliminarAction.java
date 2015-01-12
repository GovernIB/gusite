package org.ibit.rol.sac.microback.action.util;

import java.util.Hashtable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ibit.rol.sac.microback.action.BaseAction;
import org.apache.struts.action.*;
import org.ibit.rol.sac.micromodel.Usuario;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;
import org.ibit.rol.sac.micropersistence.delegate.EstadisticaDelegate;
import org.ibit.rol.sac.micropersistence.delegate.MicrositeDelegate;
import org.ibit.rol.sac.micropersistence.delegate.UsuarioDelegate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Action que elimina un microsite <P>
 * 	Definición Struts:<BR>
 *  action path="eliminarmicro"<BR> 
 *	scope="request" <BR>
 *  unknown="false"
 *  
 *  @author - Indra
 */
public class eliminarAction extends BaseAction {

	protected static Log log = LogFactory.getLog(eliminarAction.class);
	private static String[] roles = new String[]{"sacsystem", "sacadmin"};
	protected String mensaje="";
	
	@SuppressWarnings("unchecked")
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

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
    	
		// Solo podrán eliminar los roles sacsystem y sacadmin
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
    	
       	org.ibit.rol.sac.microback.base.Base.borrarVSession(request);
		request.setAttribute("MVS_index_con_info", "yes");
		
    	return mapping.findForward("info");
	}

}
