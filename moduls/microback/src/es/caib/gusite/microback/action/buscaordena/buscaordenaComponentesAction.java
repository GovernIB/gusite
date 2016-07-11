package es.caib.gusite.microback.action.buscaordena;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.gusite.microback.action.BaseAction;
import es.caib.gusite.microback.actionform.busca.BuscaOrdenaComponenteActionForm;
import es.caib.gusite.micromodel.Componente;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micropersistence.delegate.ComponenteDelegate;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;


/**
 * Action que prepara el listado de Componentes <BR>
 * <P>
 * 	Definición Struts:<BR>
 *  action path="/componentes" <BR> 
 *  name="BuscaOrdenaComponenteActionForm" <BR> 
 *  forward name="listarComponentes" path="/listaComponentes.jsp"
 *  
 *  @author Indra
 */
public class buscaordenaComponentesAction extends BaseAction {

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

    protected static Log log = LogFactory.getLog(es.caib.gusite.microback.action.buscaordena.buscaordenaComponentesAction.class);
     
    public ActionForward doExecute(ActionMapping mapping, ActionForm form, 
    		HttpServletRequest request, HttpServletResponse response) throws Exception
    {
    	ComponenteDelegate bdCompo = DelegateUtil.getComponentesDelegate();
    	bdCompo.init(((Microsite)request.getSession().getAttribute("MVS_microsite")).getId());

        //Podemos recibir datos de filtro u ordenación del listado
    	BuscaOrdenaComponenteActionForm f = (BuscaOrdenaComponenteActionForm) form;
        
        if (f.getFiltro()!= null && f.getFiltro().length()>0)
        	bdCompo.setFiltro(f.getFiltro());
    
        if (f.getOrdenacion()!= null && f.getOrdenacion().length()>0)
        	bdCompo.setOrderby(f.getOrdenacion());

        // Indicamos la página a visualizar
        if (request.getParameter("pagina")!=null)
        	bdCompo.setPagina(Integer.parseInt(request.getParameter("pagina")));
        else
        	bdCompo.setPagina(1);
            
        List<Componente> lista=bdCompo.listarComponentes();
        request.setAttribute("parametros_pagina",bdCompo.getParametros());
        
        if (lista.size()!=0) // Si hay algún registro
            request.setAttribute("listado",lista);
        else  {
        	// Si no hay registros limpiamos el filtro
        	if(f.getFiltro()!= null && f.getFiltro().length()>0){
        		addMessageAlert(request, "compo.elements.cerca.noreg");
        		bdCompo.init(((Microsite)request.getSession().getAttribute("MVS_microsite")).getId());
        		lista = bdCompo.listarComponentes();
        		request.setAttribute("parametros_pagina",bdCompo.getParametros());
        		request.setAttribute("listado",lista);
        	}else addMessageAlert(request, "compo.vacio");
            f.setFiltro("");
        }

        return mapping.findForward("listarComponentes");
        
    }

}

