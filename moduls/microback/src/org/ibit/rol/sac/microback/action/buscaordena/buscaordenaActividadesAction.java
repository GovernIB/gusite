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
import org.ibit.rol.sac.microback.actionform.busca.BuscaOrdenaActividadActionForm;
import org.ibit.rol.sac.micromodel.Microsite;
import org.ibit.rol.sac.micropersistence.delegate.ActividadDelegate;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;

/**
 * Action que prepara el listado de Actividades <BR>
 * <P>
 * 	Definición Struts:<BR>
 *  action path="/actividades" <BR> 
 *  name="BuscaOrdenaActividadActionForm" <BR> 
 *  forward name="listarActivis" path="/listaActividades.jsp"
 *  
 *  @author Indra
 */
public class buscaordenaActividadesAction extends BaseAction {

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

    protected static Log log = LogFactory.getLog(org.ibit.rol.sac.microback.action.buscaordena.buscaordenaNoticiasAction.class);
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
    	ActividadDelegate bdActivi = DelegateUtil.getActividadagendaDelegate();
    	bdActivi.init(((Microsite)request.getSession().getAttribute("MVS_microsite")).getId());

        //Podemos recibir datos de filtro u ordenación del listado
        BuscaOrdenaActividadActionForm f = (BuscaOrdenaActividadActionForm) form;
        
        if (f.getFiltro()!= null && f.getFiltro().length()>0)
        	bdActivi.setFiltro(f.getFiltro());
    
        if (f.getOrdenacion()!= null && f.getOrdenacion().length()>0)
        	bdActivi.setOrderby(f.getOrdenacion());

        // Indicamos la página a visualizar
        if (request.getParameter("pagina")!=null)
        	bdActivi.setPagina(Integer.parseInt(request.getParameter("pagina")));
        else
        	bdActivi.setPagina(1);
            
        List<?> lista=bdActivi.listarActividades();
        request.setAttribute("parametros_pagina",bdActivi.getParametros());
        
        if(request.getSession().getAttribute("mensajeBorrarActividad")!=null){
            
    		request.getSession().setAttribute("mensajeBorrarActividad", null);
    		addMessageAlert(request, "mensa.listaactborradas.sinids");
        }else if (request.getSession().getAttribute("elementosNoBorrados")!=null) {
        	
    		request.getSession().setAttribute("elementosNoBorrados", null);
    		addAlert(request, "agenda.activitat.alert.elementosnoborrados");
        }
        
        if (lista.size()!=0) // Si hay algún registro
            request.setAttribute("listado",lista);
        else { // Si no hay registros limpiamos el filtro
        	
        	if(f.getFiltro()!= null && f.getFiltro().length()>0){
        		addMessageAlert(request, "agenda.activitat.cerca.noreg");
        		bdActivi.init(((Microsite)request.getSession().getAttribute("MVS_microsite")).getId());
        		lista = bdActivi.listarActividades();
        		request.setAttribute("parametros_pagina",bdActivi.getParametros());
        		request.setAttribute("listado",lista);
        	}else addMessageAlert(request, "actividad.vacio");
        	
        	f.setFiltro("");
       
        }
        return mapping.findForward("listarActivis");
        
    }

}
