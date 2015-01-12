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
import org.ibit.rol.sac.microback.actionform.formulario.bannerForm;
import org.ibit.rol.sac.micromodel.Microsite;
import org.ibit.rol.sac.micromodel.UsuarioPropietarioMicrosite;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;
import org.ibit.rol.sac.micropersistence.delegate.MicrositeDelegate;


/**
 * Action que trata el listado de microsites de un usuario <P>
 * 	Definición Struts:<BR>
 *  action path="/microusuariosAcc"<BR> 
 *  name="listaActionForm"<BR>
 *	scope="request" <BR>
 *  forward name="detalleMicrousuario" path="/detalleMicrousuario.jsp"
 *  
 *  @author - Indra
 */
public class ListaMicroUsuariosAction extends BaseAction {

	protected static Log log = LogFactory.getLog(ListaMicroUsuariosAction.class);
	
	
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception  {

        listaActionForm f = (listaActionForm) form;
        
  
        //********************************************************
        //************* ERROR DE VALIDACION **********************
        //********************************************************
        if (request.getSession().getAttribute("microusuarioForm")!=null && request.getAttribute(Globals.ERROR_KEY)!=null) {
        	bannerForm fdet=(bannerForm) request.getSession().getAttribute("microusuarioForm");
        	request.setAttribute("microusuarioForm", fdet);
            return mapping.findForward("detalleMicrousuario");
        }
        //********************************************************
        //********************** CREAMOS *************************
        //********************************************************
        if ( f.getAccion().equals("crear")) {
        	request.getSession().removeAttribute("microusuarioForm");
        	return mapping.findForward("detalleMicrousuario");
        }
        //********************************************************
        //********************** CREAMOS *************************
        //********************************************************
        String accion= ""+request.getParameter("accion");
        if ( accion.equals("nuevouser")) {
        	Long idsite=((Microsite)request.getSession().getAttribute("MVS_microsite")).getId();
        	Long iduser = new Long(""+request.getParameter("iduser"));
        	UsuarioPropietarioMicrosite upm = new UsuarioPropietarioMicrosite(idsite,iduser);
        	MicrositeDelegate bdMicro = DelegateUtil.getMicrositeDelegate();
        	bdMicro.grabarUsuarioPropietarioMicrosite(upm);
        	
        	addMessage(request, "micro.usuario.mensa.listausuarios");
            addMessage(request, "micro.usuario.mensa.newuser.ok");
            
            return mapping.findForward("info");
        }
        
         
        //***************************************************************************
        //********************* BORRAMOS MICROS DEL USUAIRO *************************
        //***************************************************************************
        if ( f.getAccion().equals("borrarmicro")) {
            Long idmicro =null;
            MicrositeDelegate bdMicro = DelegateUtil.getMicrositeDelegate();
              Long iduser= new Long (request.getParameter("iduser"));
            String lis="";
            for (int i=0;i<f.getSeleccionados().length;i++) {
                idmicro = new Long(f.getSeleccionados()[i]);
                UsuarioPropietarioMicrosite upm = new UsuarioPropietarioMicrosite(idmicro,iduser);
                bdMicro.borrarUsuarioPropietarioMicrosite(upm);
                lis+=idmicro+", ";
            }
            lis=lis.substring(0,lis.length()-2);
            
            addMessage(request, "micro.usuario.mensa.listausuborrados", new String(lis));
            String destino = ("<a href=\"microUsuarios.do?accion=lista&id="+iduser+"\">Llista de microsites de l'usuari</a>");
        	request.setAttribute("destino",destino);
            return mapping.findForward("microusu");
        }

        //********************************************************
        //********************* BORRAMOS *************************
        //********************************************************
        if ( f.getAccion().equals("borrar")) {
            Long id =null;
            MicrositeDelegate bdMicro = DelegateUtil.getMicrositeDelegate();
            
            Long idsite=((Microsite)request.getSession().getAttribute("MVS_microsite")).getId();
            String lis="";
            for (int i=0;i<f.getSeleccionados().length;i++) {
                id = new Long(f.getSeleccionados()[i]);
                UsuarioPropietarioMicrosite upm = new UsuarioPropietarioMicrosite(idsite,id);
                bdMicro.borrarUsuarioPropietarioMicrosite(upm);
                lis+=id+", ";
            }
            lis=lis.substring(0,lis.length()-2);
            
            addMessage(request, "micro.usuario.mensa.listausuarios");
            addMessage(request, "micro.usuario.mensa.listausuborrados", new String(lis));
            
            return mapping.findForward("info");
        }

        addMessage(request, "peticion.error");
        return mapping.findForward("info");

    }
	
}
