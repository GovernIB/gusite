package org.ibit.rol.sac.microback.action.buscaordena;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.ibit.rol.sac.microback.action.BaseAction;
import org.ibit.rol.sac.microback.actionform.busca.BuscaOrdenaTipoActionForm;
import org.ibit.rol.sac.micromodel.Microsite;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;
import org.ibit.rol.sac.micropersistence.delegate.TipoDelegate;

/**
 * Action que prepara el listado de Tipos (Listados) <BR>
 * <P>
 * 	Definición Struts:<BR>
 *  action path="/tipos" <BR> 
 *  name="BuscaOrdenaTipoActionForm" <BR> 
 *  forward name="listarTipos" path="/listaTipos.jsp"<BR>
 *  forward name="listarTiposMantenimiento" path="/listaTiposMantenimientos.jsp"
 *  
 *  @author Indra
 */
public class buscaordenaTiposAction extends BaseAction {


    protected static Log log = LogFactory.getLog(org.ibit.rol.sac.microback.action.buscaordena.buscaordenaTiposAction.class);
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
    	String esmatenimiento ="";
    	TipoDelegate bdTipo = DelegateUtil.getTipoDelegate();
    	bdTipo.init(((Microsite)request.getSession().getAttribute("MVS_microsite")).getId());
    	
    	
        //Podemos recibir datos de filtro u ordenación del listado
        BuscaOrdenaTipoActionForm f = (BuscaOrdenaTipoActionForm) form;
        
        if (request.getParameter("mntnmnt") != null)  esmatenimiento = "" + request.getParameter("mntnmnt");
        else if (request.getSession().getAttribute("mntnmnt") != null) esmatenimiento = "" + request.getSession().getAttribute("mntnmnt");
        
        if (f.getFiltro()!= null && f.getFiltro().length()>0)
        	bdTipo.setFiltro(f.getFiltro());
    
        if (f.getOrdenacion()!= null && f.getOrdenacion().length()>0)
        	bdTipo.setOrderby(f.getOrdenacion());
        else bdTipo.setOrderby("Atrad.nombre");

        // Indicamos la página a visualizar
        if (request.getParameter("pagina")!=null)
        	bdTipo.setPagina(Integer.parseInt(request.getParameter("pagina")));
        else
        	bdTipo.setPagina(1);
            
        List<?> lista=bdTipo.listarTipos();
        request.setAttribute("parametros_pagina",bdTipo.getParametros());
        
        if(request.getSession().getAttribute("mensajeBorrarTipo")!=null){
            
    		request.getSession().setAttribute("mensajeBorrarTipo", null);
    		request.getSession().setAttribute("mntnmnt", null);
    		addMessageAlert(request, "mensa.listatipborrados.sinids");
        } else if (request.getSession().getAttribute("elementosNoBorrados")!=null) {
        	
    		request.getSession().setAttribute("elementosNoBorrados", null);
    		request.getSession().setAttribute("mntnmnt", null);
    		addAlert(request, "tipo.alert.elementosnoborrados");
        } else if (request.getSession().getAttribute("connexioExterna")!=null) {
        	
    		request.getSession().setAttribute("connexioExterna", null);
    		request.getSession().setAttribute("mntnmnt", null);
    		addAlert(request, "tipo.alert.connexioexterna");
        }
        
        if (lista.size()!=0) // Si hay algún registro
            request.setAttribute("listado",lista);
        else  {
        	// Si no hay registros limpiamos el filtro
        	if(f.getFiltro()!= null && f.getFiltro().length()>0){
        		addMessageAlert(request, "tipo.cerca.noreg");
        		bdTipo.init(((Microsite)request.getSession().getAttribute("MVS_microsite")).getId());
        		lista = bdTipo.listarTipos();
        		request.setAttribute("parametros_pagina",bdTipo.getParametros());
        		request.setAttribute("listado",lista);
        	} else addMessageAlert(request, "tipo.vacio");
            f.setFiltro("");
        }
        
        if (esmatenimiento.equals("yes")) {
        	request.setAttribute("listaClasificacion", bdTipo.comboClasificacion(((Microsite)request.getSession().getAttribute("MVS_microsite")).getId()));        	
        	return mapping.findForward("listarTiposMantenimiento"); 
        }
        else { 
            request.setAttribute("listaClasificacion", bdTipo.comboClasificacion(((Microsite)request.getSession().getAttribute("MVS_microsite")).getId()));
        	return mapping.findForward("listarTipos");
        }
        
    }

}
