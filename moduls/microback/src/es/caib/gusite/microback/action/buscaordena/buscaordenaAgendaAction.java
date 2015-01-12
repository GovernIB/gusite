package es.caib.gusite.microback.action.buscaordena;


import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.gusite.microback.Microback;
import es.caib.gusite.microback.action.BaseAction;
import es.caib.gusite.microback.actionform.busca.BuscaOrdenaAgendaActionForm;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micropersistence.delegate.ActividadDelegate;
import es.caib.gusite.micropersistence.delegate.AgendaDelegate;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;

/**
 * Action que prepara el listado de Agendas (Eventos) <BR>
 * <P> 
 * 	Definición Struts:<BR>
 *  action path="/agendas" <BR> 
 *  name="BuscaOrdenaAgendaActionForm" <BR> 
 *  forward name="listarAgendas" path="/listaAgendas.jsp"
 *  
 *  @author Indra
 */
public class buscaordenaAgendaAction extends BaseAction {

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

    protected static Log log = LogFactory.getLog(es.caib.gusite.microback.action.buscaordena.buscaordenaAgendaAction.class);
    
    public ActionForward doExecute(ActionMapping mapping, ActionForm form, 
    		HttpServletRequest request, HttpServletResponse response) throws Exception
    {
    	
    	AgendaDelegate bdAgenda = DelegateUtil.getAgendaDelegate();
    	bdAgenda.init(((Microsite)request.getSession().getAttribute("MVS_microsite")).getId());

    	preparaActividades(request);
    	
        //Podemos recibir datos de filtro u ordenación del listado
        BuscaOrdenaAgendaActionForm f = (BuscaOrdenaAgendaActionForm) form;
        
        if (f.getFiltro()!= null && f.getFiltro().length()>0)
        	bdAgenda.setFiltro(f.getFiltro());
    
        if (f.getOrdenacion()!= null && f.getOrdenacion().length()>0)
        	bdAgenda.setOrderby(f.getOrdenacion());

        // Indicamos la página a visualizar
        if (request.getParameter("pagina")!=null)
        	bdAgenda.setPagina(Integer.parseInt(request.getParameter("pagina")));
        else
        	bdAgenda.setPagina(1);
            
        List<?> lista=bdAgenda.listarAgendas();
        request.setAttribute("parametros_pagina",bdAgenda.getParametros());
        
        if(request.getSession().getAttribute("mensajeBorrarAgenda")!=null){
            
    		request.getSession().setAttribute("mensajeBorrarAgenda", null);
            addMessageAlert(request, "mensa.listaageborradas.sinid");
        }
        
        if (lista.size()!=0) // Si hay algún registro
            request.setAttribute("listado",lista);
        else { // Si no hay registros limpiamos el filtro
        	if(f.getFiltro()!= null && f.getFiltro().length()>0){
        		addMessageAlert(request, "agenda.cerca.noreg");
            	bdAgenda.init(((Microsite)request.getSession().getAttribute("MVS_microsite")).getId());
        		lista = bdAgenda.listarAgendas();
        		request.setAttribute("parametros_pagina",bdAgenda.getParametros());
        		request.setAttribute("listado",lista);
        	} else addMessageAlert(request, "agenda.vacio");
        	f.setFiltro("");
        	
        }

        return mapping.findForward("listarAgendas");
        
    }

    /**
     * Método que mete en el request un listado con los temas existentes
     * @param request
     */
    private void preparaActividades(HttpServletRequest request) {
    	
    	try {
    		
    		ActividadDelegate actidel = DelegateUtil.getActividadagendaDelegate();
    		actidel.init(((Microsite)request.getSession().getAttribute("MVS_microsite")).getId());
    		actidel.setTampagina(Microback.MAX_INTEGER); actidel.setPagina(1);
    		
    		if (actidel.listarActividades().size()>0)
    			request.setAttribute("MVS_lista_actividades",actidel.listarActividades());
    		
    	} catch (Exception ex) {
    		log.warn("[preparaTemas] Error recuperando actividades de agenda.");
    	}
    	
    	
    }        
    
}

