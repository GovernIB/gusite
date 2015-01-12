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
import org.ibit.rol.sac.microback.actionform.busca.BuscaOrdenaEncuestaActionForm;
import org.ibit.rol.sac.micromodel.Microsite;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;
import org.ibit.rol.sac.micropersistence.delegate.EncuestaDelegate;

/**
 * Action que prepara el listado de Encuestas <BR>
 * <P> 
 * 	Definici�n Struts:<BR>
 *  action path="/encuestas" <BR> 
 *  name="BuscaOrdenaEncuestaActionForm" <BR> 
 *  forward name="listarEncuestas" path="/listaEncuestas.jsp"
 *  
 *  @author Indra
 */
public class buscaordenaEncuestasAction extends Action {

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

    protected static Log log = LogFactory.getLog(org.ibit.rol.sac.microback.action.buscaordena.buscaordenaEncuestasAction.class);
     
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
    	Microsite mic = (Microsite)request.getSession().getAttribute("MVS_microsite");
    	if (mic==null) {
    		mic = new Microsite();
    		mic.setId(new Long(1));
    		request.getSession().setAttribute("MVS_microsite",mic);
    	}
    	
    	EncuestaDelegate bdEncuesta = DelegateUtil.getEncuestaDelegate();
    	bdEncuesta.init(((Microsite)request.getSession().getAttribute("MVS_microsite")).getId());

        //Podemos recibir datos de filtro u ordenaci�n del listado
        BuscaOrdenaEncuestaActionForm f = (BuscaOrdenaEncuestaActionForm) form;

        if (f.getFiltro()!= null && f.getFiltro().length()>0)
        	bdEncuesta.setFiltro(f.getFiltro());
    
        if (f.getOrdenacion()!= null && f.getOrdenacion().length()>0)
        	bdEncuesta.setOrderby(f.getOrdenacion());

        // Indicamos la p�gina a visualizar
        if (request.getParameter("pagina")!=null)
        	bdEncuesta.setPagina(Integer.parseInt(request.getParameter("pagina")));
        else
        	bdEncuesta.setPagina(1);
            
        List<?> lista=bdEncuesta.listarEncuestas();
        request.setAttribute("parametros_pagina",bdEncuesta.getParametros());
        
        if (lista.size()!=0) // Si hay alg�n registro
            request.setAttribute("listado",lista);
        else  // Si no hay registros limpiamos el filtro
            f.setFiltro("");

        return mapping.findForward("listarEncuestas");
        
    }

    
}

