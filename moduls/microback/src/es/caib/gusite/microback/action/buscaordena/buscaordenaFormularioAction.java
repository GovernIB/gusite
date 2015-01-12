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
import es.caib.gusite.microback.actionform.busca.BuscaOrdenaFormularioActionForm;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micropersistence.delegate.ContactoDelegate;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;


/**
 * Action que prepara el listado de Formularios <BR>
 * <P>
 * 	Definición Struts:<BR>
 *  action path="/formularios" <BR> 
 *  name="BuscaOrdenaFormularioActionForm" <BR> 
 *  forward name="listarFormus" path="/listaFormularios.jsp"
 *  
 *  @author Indra
 */
public class buscaordenaFormularioAction extends BaseAction {

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

    protected static Log log = LogFactory.getLog(es.caib.gusite.microback.action.buscaordena.buscaordenaFormularioAction.class);
    
    public ActionForward doExecute(ActionMapping mapping, ActionForm form, 
    		HttpServletRequest request, HttpServletResponse response) throws Exception
    {
    	
    	ContactoDelegate bdFormu = DelegateUtil.getContactoDelegate();
    	bdFormu.init(((Microsite)request.getSession().getAttribute("MVS_microsite")).getId());

        //Podemos recibir datos de filtro u ordenación del listado
        BuscaOrdenaFormularioActionForm f = (BuscaOrdenaFormularioActionForm) form;
        
        if (f.getFiltro()!= null && f.getFiltro().length()>0)
        	bdFormu.setFiltro(f.getFiltro());
    
        if (f.getOrdenacion()!= null && f.getOrdenacion().length()>0)
        	bdFormu.setOrderby(f.getOrdenacion());

        // Indicamos la página a visualizar
        if (request.getParameter("pagina")!=null)
        	bdFormu.setPagina(Integer.parseInt(request.getParameter("pagina")));
        else
        	bdFormu.setPagina(1);
            
        List<?> lista=bdFormu.listarContactos();
        request.setAttribute("parametros_pagina",bdFormu.getParametros());
        
        if(request.getSession().getAttribute("mensajeBorrarFormulario")!=null){  
    		request.getSession().setAttribute("mensajeBorrarFormulario", null);
            addMessage(request, "mensa.listaformusborrados.sinid");
        }
        
        if (lista.size()!=0) // Si hay algún registro
            request.setAttribute("listado",lista);
        else{ // Si no hay registros limpiamos el filtro
        	if(f.getFiltro()!= null && f.getFiltro().length()>0){
        		addMessage(request, "formu.cerca.noreg");
        		bdFormu.init(((Microsite)request.getSession().getAttribute("MVS_microsite")).getId());
        		lista = bdFormu.listarContactos();
        		request.setAttribute("parametros_pagina",bdFormu.getParametros());
        		request.setAttribute("listado",lista);
        	} else addMessageAlert(request, "formu.vacio");
        	f.setFiltro("");
        }

        return mapping.findForward("listarFormus");
        
    }

}
