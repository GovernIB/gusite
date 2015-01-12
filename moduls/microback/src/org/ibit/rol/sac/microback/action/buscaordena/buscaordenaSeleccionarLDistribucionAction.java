package org.ibit.rol.sac.microback.action.buscaordena;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.ibit.rol.sac.microback.action.BaseAction;
import org.ibit.rol.sac.microback.actionform.busca.BuscaOrdenaSeleccionarLDistribucionActionForm;
import org.ibit.rol.sac.micromodel.Convocatoria;
import org.ibit.rol.sac.micromodel.DistribucionConvocatoria;
import org.ibit.rol.sac.micromodel.Microsite;
import org.ibit.rol.sac.micropersistence.delegate.ConvocatoriaDelegate;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;
import org.ibit.rol.sac.micropersistence.delegate.LDistribucionDelegate;

/**
 * Action que prepara el listado de microsites de Usuario <BR>
 * <P>
 * 	Definici�n Struts:<BR>
 *  action path="/microUsuarios" <BR> 
 *  name="BuscaOrdenaMicroUsuarioActionForm" <BR> 
 *  forward name="listarUsuarios" path="/listaUsuariosMicrosite.jsp"<BR>
 *  forward name="listarMicrositesdelusu" path="/listaMicrositesdelusu.jsp"
 *  
 *  @author Indra
 */
public class buscaordenaSeleccionarLDistribucionAction extends BaseAction {

	protected static Log log = LogFactory.getLog(org.ibit.rol.sac.microback.action.buscaordena.BuscaOrdenaMicroUsuariosAction.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		    {
		    	LDistribucionDelegate distribDel = DelegateUtil.getLlistaDistribucionDelegate();
		    	distribDel.init(((Microsite)request.getSession().getAttribute("MVS_microsite")).getId());
		    	
		    	BuscaOrdenaSeleccionarLDistribucionActionForm f = (BuscaOrdenaSeleccionarLDistribucionActionForm)form;
	    		
	    		if (request.getParameter("id")!=null)
	    			f.setIdConvocatoria(new Long(request.getParameter("id")));
	    		
		    	//Podemos recibir datos de filtro u ordenaci�n del listado
		        if (f.getFiltro()!= null && f.getFiltro().length()>0)
		        	distribDel.setFiltro(f.getFiltro());
		    
		        if (f.getOrdenacion()!= null && f.getOrdenacion().length()>0)
		        	distribDel.setOrderby(f.getOrdenacion());

		        // Indicamos la p�gina a visualizar
		        if (request.getParameter("pagina")!=null)
		        	distribDel.setPagina(Integer.parseInt(request.getParameter("pagina")));
		        else
		        	distribDel.setPagina(1);
		            
		        request.setAttribute("parametros_pagina",distribDel.getParametros());

		        List<?> lista=distribDel.listarListaDistribucion();
		        if (lista.size()!=0){ // Si hay alg�n registro
		        	f.setDestinatarios(lista);
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
		        
		    	// Lista de destinatarios que tiene una convocatoria		    		
		    	ConvocatoriaDelegate convDel = DelegateUtil.getConvocatoriaDelegate();		    			            
		        Convocatoria conv = convDel.obtenerConvocatoria(f.getIdConvocatoria());		        		        		      
		        
		        Set dest = conv.getDestinatarios();
		        String[] sel = new String[dest.size()];
		        int i = 0;
		        for (Iterator it = dest.iterator();it.hasNext();i++){ // Si hay alg�n registro
		            sel[i] = ((DistribucionConvocatoria)it.next()).getIdDistribucion().toString();
		    	}
		        f.setSeleccionados(sel);
		        request.setAttribute("seleccionLDistribucion", f);
		        return mapping.findForward("listadistribucion");
		    }
	 }
}
