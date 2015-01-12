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
import org.ibit.rol.sac.microback.actionform.busca.BuscaOrdenaBannerActionForm;
import org.ibit.rol.sac.micromodel.Microsite;
import org.ibit.rol.sac.micropersistence.delegate.BannerDelegate;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;


/**
 * Action que prepara el listado de Banners <BR>
 * <P>
 * 	Definición Struts:<BR>
 *  action path="/banners" <BR> 
 *  name="BuscaOrdenaBannerActionForm" <BR> 
 *  forward name="listarBanners" path="/listaBanners.jsp"
 *  
 *  @author Indra
 */
public class buscaordenaBannerAction extends Action {

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

    protected static Log log = LogFactory.getLog(org.ibit.rol.sac.microback.action.buscaordena.buscaordenaBannerAction.class);
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
    	
    	BannerDelegate bdBanner = DelegateUtil.getBannerDelegate();
    	bdBanner.init(((Microsite)request.getSession().getAttribute("MVS_microsite")).getId());

        //Podemos recibir datos de filtro u ordenación del listado
        BuscaOrdenaBannerActionForm f = (BuscaOrdenaBannerActionForm) form;
        
        if (f.getFiltro()!= null && f.getFiltro().length()>0)
        	bdBanner.setFiltro(f.getFiltro());
    
        if (f.getOrdenacion()!= null && f.getOrdenacion().length()>0)
        	bdBanner.setOrderby(f.getOrdenacion());

        // Indicamos la página a visualizar
        if (request.getParameter("pagina")!=null)
        	bdBanner.setPagina(Integer.parseInt(request.getParameter("pagina")));
        else
        	bdBanner.setPagina(1);
            
        List<?> lista=bdBanner.listarBanners();
        request.setAttribute("parametros_pagina",bdBanner.getParametros());
        
        if (lista.size()!=0) // Si hay algún registro
            request.setAttribute("listado",lista);
        else  // Si no hay registros limpiamos el filtro
            f.setFiltro("");

        return mapping.findForward("listarBanners");
        
    }

}
