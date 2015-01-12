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
import org.ibit.rol.sac.microback.Microback;
import org.ibit.rol.sac.microback.actionform.busca.BuscaOrdenaFaqActionForm;
import org.ibit.rol.sac.micromodel.Microsite;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;
import org.ibit.rol.sac.micropersistence.delegate.FaqDelegate;
import org.ibit.rol.sac.micropersistence.delegate.TemaDelegate;


/**
 * Action que prepara el listado de Faqs <BR>
 * <P>
 * 	Definición Struts:<BR>
 *  action path="/faqs" <BR> 
 *  name="BuscaOrdenaFaqActionForm" <BR> 
 *  forward name="listarFaqs" path="/listaFaqs.jsp"
 *  
 *  @author Indra
 */
public class buscaordenaFaqsAction extends Action {

    protected static Log log = LogFactory.getLog(org.ibit.rol.sac.microback.action.buscaordena.buscaordenaFaqsAction.class);
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
    	FaqDelegate bdFaq = DelegateUtil.getFaqDelegate();
    	bdFaq.init(((Microsite)request.getSession().getAttribute("MVS_microsite")).getId());

    	
    	preparaTemas(request);
    	
        //Podemos recibir datos de filtro u ordenación del listado
        BuscaOrdenaFaqActionForm f = (BuscaOrdenaFaqActionForm) form;
        
        if (f.getFiltro()!= null && f.getFiltro().length()>0)
        	bdFaq.setFiltro(f.getFiltro());
    
        if (f.getOrdenacion()!= null && f.getOrdenacion().length()>0)
        	bdFaq.setOrderby(f.getOrdenacion());

        // Indicamos la página a visualizar
        if (request.getParameter("pagina")!=null)
        	bdFaq.setPagina(Integer.parseInt(request.getParameter("pagina")));
        else
        	bdFaq.setPagina(1);
            
        List<?> lista=bdFaq.listarFaqs();
        request.setAttribute("parametros_pagina",bdFaq.getParametros());
        
        if (lista.size()!=0) // Si hay algún registro
            request.setAttribute("listado",lista);
        else  // Si no hay registros limpiamos el filtro
            f.setFiltro("");

        return mapping.findForward("listarFaqs");
        
    }

    /**
     * Método que mete en el request un listado con los temas existentes
     * @param request
     */
    private void preparaTemas(HttpServletRequest request) {
    	
    	try {
    		
    		TemaDelegate temadel = DelegateUtil.getTemafaqDelegate();
    		temadel.init(((Microsite)request.getSession().getAttribute("MVS_microsite")).getId());
    		temadel.setTampagina(Microback.MAX_INTEGER); temadel.setPagina(1);
    		
    		if (temadel.listarTemas().size()>0)
    			request.setAttribute("MVS_lista_temasfaqs",temadel.listarTemas());
    		
    	} catch (Exception ex) {
    		log.warn("[preparaTemas] Error recuperando temas de faqs.");
    	}
    	
    	
    }        
    
}
