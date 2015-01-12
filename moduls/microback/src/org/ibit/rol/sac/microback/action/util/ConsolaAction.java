package org.ibit.rol.sac.microback.action.util;


import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.ibit.rol.sac.microback.action.BaseAction;
import org.ibit.rol.sac.microback.utils.log.MicroLog;
import org.ibit.rol.sac.micromodel.Usuario;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;
import org.ibit.rol.sac.micropersistence.delegate.UsuarioDelegate;

/**
 * Action que carga la consola de eventos <P>
 * 	Definici�n Struts:<BR>
 *  action path="consola"<BR> 
 *  unknown="false"<BR>
 *  forward name="detalle" path="/consola.jsp"
 *  
 *  @author - Indra
 */
public class ConsolaAction extends BaseAction {
	
	protected static Log log = LogFactory.getLog(ConsolaAction.class);
	private static String[] roles = new String[]{"sacsystem", "sacadmin"};
	
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
    	
		// Solo podr�n visualizar la consola sacsystem y sacadmin
		rolenames=(Hashtable<String,String>)request.getSession().getAttribute("rolenames");

		if (!(rolenames.contains(roles[0]) || rolenames.contains(roles[1]))) {
			addMessage(request, "peticion.error");
            return mapping.findForward("info");
		}
		
		request.setAttribute("logs",MicroLog.getLogs());

		
    	return mapping.findForward("detalle");
	}

}
