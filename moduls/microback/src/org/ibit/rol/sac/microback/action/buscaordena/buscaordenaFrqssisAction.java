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
import org.ibit.rol.sac.microback.actionform.busca.BuscaOrdenaFrqssiActionForm;
import org.ibit.rol.sac.micromodel.Microsite;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;
import org.ibit.rol.sac.micropersistence.delegate.FrqssiDelegate;


/**
 * Action que prepara el listado de FrQssis <BR>
 * <P>
 * 	Definici�n Struts:<BR>
 *  action path="/frqssis" <BR> 
 *  name="BuscaOrdenaFrqssiActionForm" <BR> 
 *  forward name="listarFrqssis" path="/listaFrqssis.jsp"
 *  
 *  @author Indra
 */
public class buscaordenaFrqssisAction extends Action {


    protected static Log log = LogFactory.getLog(org.ibit.rol.sac.microback.action.buscaordena.buscaordenaFrqssisAction.class);
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
    	
    	FrqssiDelegate bdFrqssi = DelegateUtil.getFrqssiDelegate();
    	bdFrqssi.init(((Microsite)request.getSession().getAttribute("MVS_microsite")).getId());

        //Podemos recibir datos de filtro u ordenaci�n del listado
        BuscaOrdenaFrqssiActionForm f = (BuscaOrdenaFrqssiActionForm) form;
        
        if (f.getFiltro()!= null && f.getFiltro().length()>0)
        	bdFrqssi.setFiltro(f.getFiltro());
    
        if (f.getOrdenacion()!= null && f.getOrdenacion().length()>0)
        	bdFrqssi.setOrderby(f.getOrdenacion());
        else bdFrqssi.setOrderby("Atrad.nombre");

        // Indicamos la p�gina a visualizar
        if (request.getParameter("pagina")!=null)
        	bdFrqssi.setPagina(Integer.parseInt(request.getParameter("pagina")));
        else
        	bdFrqssi.setPagina(1);
            
        List<?> lista=bdFrqssi.listarFrqssis();
        request.setAttribute("parametros_pagina",bdFrqssi.getParametros());
        
        if (lista.size()!=0) // Si hay alg�n registro
            request.setAttribute("listado",lista);
        else  // Si no hay registros limpiamos el filtro
            f.setFiltro("");

        return mapping.findForward("listarFrqssis");
        
    }

}
