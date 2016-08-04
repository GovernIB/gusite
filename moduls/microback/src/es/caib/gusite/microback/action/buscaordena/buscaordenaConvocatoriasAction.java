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
import es.caib.gusite.microback.actionform.busca.BuscaOrdenaConvocatoriaActionForm;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micropersistence.delegate.ConvocatoriaDelegate;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;

/**
 * Action que prepara el listado de Convocatoriaes <BR>
 * <P>
 * 	Definición Struts:<BR>
 *  action path="/actividades" <BR> 
 *  name="BuscaOrdenaConvocatoriaActionForm" <BR> 
 *  forward name="listarActivis" path="/listaConvocatoriaes.jsp"
 *  
 *  @author Indra
 */
public class buscaordenaConvocatoriasAction extends BaseAction {

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

    protected static Log log = LogFactory.getLog(es.caib.gusite.microback.action.buscaordena.buscaordenaNoticiasAction.class);
    
    public ActionForward doExecute(ActionMapping mapping, ActionForm form, 
    		HttpServletRequest request, HttpServletResponse response) throws Exception
    {
    	ConvocatoriaDelegate bdConvocotria = DelegateUtil.getConvocatoriaDelegate();
    	bdConvocotria.init(((Microsite)request.getSession().getAttribute("MVS_microsite")).getId());

        //Podemos recibir datos de filtro u ordenación del listado
        BuscaOrdenaConvocatoriaActionForm f = (BuscaOrdenaConvocatoriaActionForm) form;
        
        if (f.getFiltro()!= null && f.getFiltro().length()>0)
        	bdConvocotria.setFiltro(f.getFiltro());
    
        if (f.getOrdenacion()!= null && f.getOrdenacion().length()>0)
        	bdConvocotria.setOrderby(f.getOrdenacion());

        // Indicamos la página a visualizar
        if (request.getParameter("pagina")!=null)
        	bdConvocotria.setPagina(Integer.parseInt(request.getParameter("pagina")));
        else
        	bdConvocotria.setPagina(1);
            
        List<?> lista=bdConvocotria.listarConvocatorias();
        request.setAttribute("parametros_pagina",bdConvocotria.getParametros());
        
        if(request.getSession().getAttribute("mensajeBorrarConvocatoria")!=null){
            
    		request.getSession().setAttribute("mensajeBorrarConvocatoria", null);
    		addMessageAlert(request, "mensa.listaconvborradas.sinids");
        }
        
        if (lista.size()!=0) // Si hay algún registro
            request.setAttribute("listado",lista);
        else { // Si no hay registros limpiamos el filtro
        	
        	if(f.getFiltro()!= null && f.getFiltro().length()>0){
        		addMessageAlert(request, "convocatoria.cerca.noreg");
        		bdConvocotria.init(((Microsite)request.getSession().getAttribute("MVS_microsite")).getId());
        		lista = bdConvocotria.listarConvocatorias();
        		request.setAttribute("parametros_pagina",bdConvocotria.getParametros());
        		request.setAttribute("listado",lista);
        	}else addMessageAlert(request, "convocatoria.vacio");
        	
        	f.setFiltro("");
       
        }
        return mapping.findForward("listarconvocatorias");
        
    }

}
