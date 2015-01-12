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
import es.caib.gusite.microback.actionform.busca.BuscaOrdenaLDistribucionActionForm;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.LDistribucionDelegate;

/**
 * Action que prepara el listado de microsites de Usuario <BR>
 * <P>
 * 	Definición Struts:<BR>
 *  action path="/microUsuarios" <BR> 
 *  name="BuscaOrdenaMicroUsuarioActionForm" <BR> 
 *  forward name="listarUsuarios" path="/listaUsuariosMicrosite.jsp"<BR>
 *  forward name="listarMicrositesdelusu" path="/listaMicrositesdelusu.jsp"
 *  
 *  @author Indra
 */
public class buscaordenaLDistribucionAction extends BaseAction {

	protected static Log log = LogFactory.getLog(es.caib.gusite.microback.action.buscaordena.BuscaOrdenaMicroUsuariosAction.class);
	
	 @SuppressWarnings({ "rawtypes", "unchecked" })
	public ActionForward doExecute(ActionMapping mapping, ActionForm form, 
    		HttpServletRequest request, HttpServletResponse response) throws Exception {
		    {
		    	LDistribucionDelegate distribDel = DelegateUtil.getLlistaDistribucionDelegate();
		    	distribDel.init(((Microsite)request.getSession().getAttribute("MVS_microsite")).getId());

		        //Podemos recibir datos de filtro u ordenación del listado
		        BuscaOrdenaLDistribucionActionForm f = (BuscaOrdenaLDistribucionActionForm) form;
		        
		        if (f.getFiltro()!= null && f.getFiltro().length()>0)
		        	distribDel.setFiltro(f.getFiltro());
		    
		        if (f.getOrdenacion()!= null && f.getOrdenacion().length()>0)
		        	distribDel.setOrderby(f.getOrdenacion());

		        // Indicamos la página a visualizar
		        if (request.getParameter("pagina")!=null)
		        	distribDel.setPagina(Integer.parseInt(request.getParameter("pagina")));
		        else
		        	distribDel.setPagina(1);
		            
		        List<?> lista=distribDel.listarListaDistribucion();
		        request.setAttribute("parametros_pagina",distribDel.getParametros());
		        
		        if(request.getSession().getAttribute("mensajeBorrarConvocatoria")!=null){
		            
		    		request.getSession().setAttribute("mensajeBorrarConvocatoria", null);
		    		addMessageAlert(request, "mensa.listaconvborradas.sinids");
		        }
		        
		        if (lista.size()!=0){ // Si hay algún registro
		            request.setAttribute("listado",lista);
		        }else { // Si no hay registros limpiamos el filtro
		        	
		        	if(f.getFiltro()!= null && f.getFiltro().length()>0){
		        		addMessageAlert(request, "convocatoria.cerca.noreg");
		        		distribDel.init(((Microsite)request.getSession().getAttribute("MVS_microsite")).getId());
		        		lista = distribDel.listarListaDistribucion();
		        		request.setAttribute("parametros_pagina",distribDel.getParametros());
		        		request.setAttribute("listado",lista);
		        	}else addMessageAlert(request, "ldistribucio.vacio");
		        	
		        	f.setFiltro("");
		       
		        }
		        return mapping.findForward("listadistribucion");
		        
		    }
	 }
}
