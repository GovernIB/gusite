package org.ibit.rol.sac.microback.action.buscaordena;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.ibit.rol.sac.microback.actionform.busca.BuscaOrdenaUsuarioActionForm;
import org.ibit.rol.sac.micropersistence.delegate.UsuarioDelegate;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;


/**
 * Action que prepara el listado de Usuarios <BR>
 * <P>
 * 	Definici�n Struts:<BR>
 *  action path="/usuarios" <BR> 
 *  name="BuscaOrdenaUsuarioActionForm" <BR> 
 *  forward name="listarUsuarios" path="/listaUsuarios.jsp"
 *  
 *  @author Indra
 */
public class BuscaordenaUsuarioAction extends Action {

	protected static Log log = LogFactory.getLog(BuscaordenaUsuarioAction.class);
	
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
    	
    	UsuarioDelegate bdUsuari = DelegateUtil.getUsuarioDelegate();
    	bdUsuari.init();

    	
        //Podemos recibir datos de filtro u ordenaci�n del listado
        BuscaOrdenaUsuarioActionForm f = (BuscaOrdenaUsuarioActionForm) form;
        
        if (f.getFiltro()!= null && f.getFiltro().length()>0)
        	bdUsuari.setFiltro(f.getFiltro());
    
        if (f.getOrdenacion()!= null && f.getOrdenacion().length()>0)
        	bdUsuari.setOrderby(f.getOrdenacion());

        // Indicamos la p�gina a visualizar
        if (request.getParameter("pagina")!=null)
        	bdUsuari.setPagina(Integer.parseInt(request.getParameter("pagina")));
        else
        	bdUsuari.setPagina(1);
            
        List<?> lista=bdUsuari.listarUsuarios();
        request.setAttribute("parametros_pagina",bdUsuari.getParametros());
        
        if (lista.size()!=0) // Si hay alg�n registro
            request.setAttribute("listado",lista);
        else  // Si no hay registros limpiamos el filtro
            f.setFiltro("");

        return mapping.findForward("listarUsuarios");
        
    }
    
	
}
