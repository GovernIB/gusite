package org.ibit.rol.sac.microback.action.edita;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.ibit.rol.sac.microback.action.BaseAction;
import org.ibit.rol.sac.microback.actionform.formulario.UsuarioForm;
import org.ibit.rol.sac.microback.base.Base;
import org.ibit.rol.sac.micromodel.Usuario;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;
import org.ibit.rol.sac.micropersistence.delegate.UsuarioDelegate;

/**
 * Action que edita los usuarios <BR>
 * <P>
 * 	Definición Struts:<BR>
 *  action path="/usuarioEdita" <BR> 
 *  name="ususarioForm" <BR> 
 *  input="/usuarioAcc.do"<BR>
 *	scope="session" <BR>
 *  unknown="false" <BR>
 *  forward name="detalle" path="/detalleUsuario.jsp"
 *  
 *  @author Indra
 */
public class UsuarioEditaAction extends BaseAction {

	protected static Log log = LogFactory.getLog(UsuarioEditaAction.class);
	
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	UsuarioDelegate bdUsuari = DelegateUtil.getUsuarioDelegate();
    	Usuario usuari=null;
    	UsuarioForm f = (UsuarioForm) form;
    	

    	if(request.getParameter("modifica")!=null || request.getParameter("anyade")!=null) {

        	if (f.getId() == null) {  
        		usuari = new Usuario(); // Es Alta
            } else {  // Es modificacion
            	usuari = bdUsuari.obtenerUsuario(f.getId());
            	//************COMPROBACION DE PERMISOS *************
                if (!Base.isUserSysOradmin(request)) {
                	addMessage(request, "info.user.seguridad");
                	return mapping.findForward("infoscreen");
                }
            	//*********************************************
            }
        	
        	usuari.setNombre(f.getNombre());
        	usuari.setObservaciones(f.getObservaciones());
        	usuari.setPerfil(f.getPerfil());
        	usuari.setUsername(f.getUsername());
        	usuari.setPassword(f.getPassword());
     	 
	        bdUsuari.grabarUsuario(usuari);
	
      
	       	if(request.getParameter("anyade")!=null)  addMessage(request, "mensa.nuevousuari");
	       	if(request.getParameter("modifica")!=null)	addMessage(request, "mensa.modifusuari");	
	       	
	   		addMessage(request, "mensa.editarusuari", "" + usuari.getId().longValue());
	       	addMessage(request, "mensa.listausuaris");
	       	
	       	return mapping.findForward("infoscreen");
               
       }
        
        //********************************************************
        //********************** EDITAMOS ************************
        //********************************************************
        if (request.getParameter("id")!=null) {     
            Long id = new Long(""+request.getParameter("id"));
          	
            	//************COMPROBACION DE PERMISOS *************
            	
                if (!Base.isUserSysOradmin(request)) {
                	addMessage(request, "info.user.seguridad");
                	return mapping.findForward("infoscreen");
                }

            	usuari = bdUsuari.obtenerUsuario(id);
            	
            	f.setNombre(usuari.getNombre());
            	f.setObservaciones(usuari.getObservaciones());
            	f.setPerfil(usuari.getPerfil());
            	f.setUsername(usuari.getUsername());
            	f.setPassword(usuari.getPassword());
            	f.setRepitepwd(usuari.getPassword());
               
                
            return mapping.findForward("detalle");

        }

        addMessage(request, "peticion.error");
        return mapping.findForward("infoscreen");
    }
	
}
