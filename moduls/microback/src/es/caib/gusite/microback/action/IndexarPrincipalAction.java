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

import es.caib.gusite.microback.utils.job.IndexacionJobUtil;
import es.caib.gusite.micromodel.SolrPendienteJob;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.SolrPendienteDelegate;
import es.caib.gusite.micropersistence.util.IndexacionUtil;
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
        
        
        if (request.getParameter("indexar") != null && request.getParameter("indexar").equals("verpendientes")) {
        	final List<?> pendientes = verListaPendientes();	
        	request.setAttribute("listado",pendientes);			
		}
		
        if(request.getParameter("indexar") != null && request.getParameter("indexar").equals("verinfo")){
			final List<SolrPendienteJob> listInfo = (List<SolrPendienteJob>) verListaJobs();	
        	request.setAttribute("listInfo",listInfo);	
		}
		
        return mapping.findForward(elforward);
	
	}	
	
	private List<?> verListaJobs() {
		try {
			SolrPendienteDelegate solrPendienteDel = DelegateUtil.getSolrPendienteDelegate();	
			return solrPendienteDel.getListJobs(10);			
		} catch(Exception exception) {
			return null;
		}
	}
	
    private List<?> verListaPendientes() {
		try {
			SolrPendienteDelegate solrPendienteDel = DelegateUtil.getSolrPendienteDelegate();			
			return solrPendienteDel.getPendientes();			
		} catch(Exception exception) {
			return null;
		}
	}

	private Boolean indexarTodo() throws Exception {
		 
		try{									
			IndexacionJobUtil.crearJob(IndexacionUtil.TIPO_TODO, null, null);						
		   }catch(Exception e){
			log.error("Error indexando todo" );
			return false;
		   }
	 		     	
		return true;
			
	}
    private Boolean indexarByUA(String idUAdministrativa) throws Exception {
		 
		try{									
			IndexacionJobUtil.crearJob(IndexacionUtil.TIPO_UA, idUAdministrativa, null);						
		   }catch(Exception e){
			log.error("Error indexando UA " + idUAdministrativa );
			return false;
		   }
	 		     	
		return true;
			
	}
    
    private Boolean indexarPendiente() throws Exception {
		 
		try{									
			IndexacionJobUtil.crearJob(IndexacionUtil.TIPO_PENDIENTE, null, null);							
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
