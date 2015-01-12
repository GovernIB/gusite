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
import org.ibit.rol.sac.microback.actionform.formulario.noticiaForm;
import org.ibit.rol.sac.micromodel.Microsite;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;
import org.ibit.rol.sac.micropersistence.delegate.NoticiaDelegate;
import org.ibit.rol.sac.micropersistence.delegate.TipoDelegate;

/**
 * Action que valida y trata el listado de noticias de un microsite <BR>
 * <P>
 * 	Definición Struts:<BR>
 *  action path="/noticiasAcc" <BR> 
 *  name="listaActionForm" <BR> 
 *  scope="request" <BR>
 *  forward name="detalleNoti" path="/detalleNoticia.jsp"<BR> 
 *  forward name="listarNoticias" path="/noticias.do"
 *  
 *  @author Indra
 */
public class listaNoticiasAction extends BaseAction {


	protected static Log log = LogFactory.getLog(listaNoticiasAction.class);
	
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception  {

        listaActionForm f = (listaActionForm) form;


        TipoDelegate bdTipo = DelegateUtil.getTipoDelegate();
        if(request.getSession().getAttribute("MVS_idtipo")!=null) 
        	request.setAttribute("MVS_tipolistado", bdTipo.obtenerTipo( new Long(""+request.getSession().getAttribute("MVS_idtipo")) ) );

     
        //********************************************************
        //************* ERROR DE VALIDACION **********************
        //********************************************************
        if (request.getSession().getAttribute("noticiaForm")!=null && request.getAttribute(Globals.ERROR_KEY)!=null) {
        	noticiaForm fdet=(noticiaForm) request.getSession().getAttribute("noticiaForm");
        	request.setAttribute("noticiaForm", fdet);
            // Relleno el combo de Tipos de Noticias
            request.setAttribute("tiposCombo", bdTipo.listarCombo(((Microsite)request.getSession().getAttribute("MVS_microsite")).getId()));
            request.setAttribute("validacion", "si");
            return mapping.findForward("detalleNoti");
        }
        //********************************************************
        //********************** CREAMOS *************************
        //********************************************************
        if ( f.getAccion().equals("crear")) {
            // Relleno el combo de Tipos de Noticias
            request.setAttribute("tiposCombo", bdTipo.listarCombo(((Microsite)request.getSession().getAttribute("MVS_microsite")).getId()));
        	request.getSession().removeAttribute("noticiaForm");
        	return mapping.findForward("detalleNoti");
        }
        //********************************************************
        //********************* BORRAMOS *************************
        //********************************************************
        if ( f.getAccion().equals("borrar")) {
            Long id =null;
        	NoticiaDelegate bdNoticia = DelegateUtil.getNoticiasDelegate();
            
            String lis="";
            for (int i=0;i<f.getSeleccionados().length;i++) {
                id = new Long(f.getSeleccionados()[i]);
                bdNoticia.borrarNoticia(id);
                lis+=id+", ";
            }
            lis=lis.substring(0,lis.length()-2);
            
            request.getSession().setAttribute("mensajeBorrarNoticia", new String(lis));
            
            return mapping.findForward("listarNoticias");
        }

        //********************************************************
        //********************* CLONAMOS *************************
        //********************************************************
        if ( f.getAccion().equals("clonar")) {
            Long id =null;
        	NoticiaDelegate bdNoticia = DelegateUtil.getNoticiasDelegate();
            
            id = new Long(f.getSeleccionados()[0]);
            String newid = "" + bdNoticia.clonarNoticia(id);
            
            request.getSession().setAttribute("mensajeClonarNoticia", newid);
            
            return mapping.findForward("listarNoticias");

        }        
        
        
        addMessageError(request, "peticion.error");
        return mapping.findForward("info");

    }

}