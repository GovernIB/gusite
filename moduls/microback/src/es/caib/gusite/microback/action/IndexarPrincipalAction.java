package es.caib.gusite.microback.action;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.gusite.micromodel.TemaFront;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.SolrPendienteDelegate;
import es.caib.gusite.plugins.organigrama.OrganigramaProvider;
import es.caib.gusite.plugins.organigrama.UnidadListData;

/**
 * Action que redirige al indexarPrincipal<P>
 * 
 * 	Definición Struts:<BR>
 *  action path="/indexarPrincipal"<BR> 
 *  unknown="false" <BR>
 *  forward name="indexarPrincipal" path="/indexarPrincipal.jsp"
 *  
 *  @author - Indra
 */
public class IndexarPrincipalAction extends BaseAction {

	
	protected static Log log = LogFactory.getLog(IndexarPrincipalAction.class);
	
		
	public ActionForward doExecute(ActionMapping mapping, ActionForm form, 
    		HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		String elforward="indexarPrincipal";
		
		List<UnidadListData> listaUA = new ArrayList<UnidadListData>();
				
		
		List<UnidadListData>  list = (List<UnidadListData>) getUnidadesAdministrativas("ca");
					
    	for (UnidadListData uAdministrativa : list) {
    		if (uAdministrativa.getId()!=null){
    			listaUA.add(uAdministrativa);
    		}
    	}
	        		
		request.setAttribute("listaUA", listaUA);
		
		if (request.getParameter("indexar") != null && request.getParameter("indexar").equals("todo")) {
						
			if (indexarTodo() == false){
				request.setAttribute("nok", true);
				addMessage(request, "indexa.nok");
			}
			else {			
				request.setAttribute("ok", true);
				addMessage(request, "indexa.ok");
			}					
		}
        if (request.getParameter("indexar") != null && request.getParameter("indexar").equals("byUA")) {
			
			String idUA = request.getParameter("uaId");
			
			if (indexarByUA(idUA) == false){
				request.setAttribute("nok", true);
				addMessage(request, "indexa.nok");
			}
			else {			
				request.setAttribute("ok", true);
				addMessage(request, "indexa.ok");
			}
		}
        
        if (request.getParameter("indexar") != null && request.getParameter("indexar").equals("pendientes")) {
						
			
			if (indexarPendiente() == false){
				request.setAttribute("nok", true);
				addMessage(request, "indexa.nok");
			}
			else {			
				request.setAttribute("ok", true);
				addMessage(request, "indexa.ok");
			}
		}
		
		
		
        return mapping.findForward(elforward);
	
	}	
	
    private Boolean indexarTodo() throws Exception {
		 
		try{									
			SolrPendienteDelegate solrPendienteDel = DelegateUtil.getSolrPendienteDelegate();			
			solrPendienteDel.indexarTodo();
							
		   }catch(Exception e){
			log.error("Error indexando todo" );
			return false;
		   }
	 		     	
		return true;
			
	}
    private Boolean indexarByUA(String idUAdministrativa) throws Exception {
		 
		try{									
			SolrPendienteDelegate solrPendienteDel = DelegateUtil.getSolrPendienteDelegate();			
			solrPendienteDel.indexarMicrositeByUA(idUAdministrativa);
							
		   }catch(Exception e){
			log.error("Error indexando UA " + idUAdministrativa );
			return false;
		   }
	 		     	
		return true;
			
	}
    
    private Boolean indexarPendiente() throws Exception {
		 
		try{									
			SolrPendienteDelegate solrPendienteDel = DelegateUtil.getSolrPendienteDelegate();			
			solrPendienteDel.indexarPendientes();
							
		   }catch(Exception e){
			log.error("Error indexando pendientes ");
			return false;
		   }
	 		     	
		return true;
			
	}
    
   
	private Collection<UnidadListData> getUnidadesAdministrativas(String lang) throws RemoteException {
		Collection<UnidadListData> lista = null;
		SolrPendienteDelegate solrPendienteDel = DelegateUtil.getSolrPendienteDelegate();
		lista = (Collection<UnidadListData>) solrPendienteDel.getUnidadesAdministrativas("ca");
		
		return lista;
	}
	

}
