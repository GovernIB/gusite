package es.caib.gusite.microback.action;

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

/**
 * Action que redirige al indexarMicrosite<P>
 * 
 * 	Definición Struts:<BR>
 *  action path="/indexarMicrosite"<BR> 
 *  unknown="false" <BR>
 *  forward name="indexarMicrosite" path="/indexarMicrosite.jsp"
 *  
 *  @author - Indra
 */
public class IndexarMicrositeAction extends BaseAction {

	
	protected static Log log = LogFactory.getLog(IndexarMicrositeAction.class);
	
	
	public ActionForward doExecute(ActionMapping mapping, ActionForm form, 
    		HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		String elforward="indexarMicrosite";
		
		if (request.getParameter("indexar") != null && request.getParameter("indexar").equals("si")) {
			
			String idSite = request.getParameter("site");
			if (indexarMicrosite(idSite)) {
				request.setAttribute("ok", true);
				addMessage(request, "indexa.ok");
			} else {
				request.setAttribute("nok", true);
				addMessage(request, "indexa.ok");
			}
			request.setAttribute("mostrarBoton", true);
		}
		
		if(request.getParameter("indexar") != null && request.getParameter("indexar").equals("verinfo")){
			request.setAttribute("mostrarinfo", true);
			request.setAttribute("mostrarBoton", true);
			String idSite = request.getParameter("site");
			final List<SolrPendienteJob> listInfo = verListaJobs(idSite);	
        	request.setAttribute("listado",listInfo);	
		}
        return mapping.findForward(elforward);
	
	}	
		

	private List<SolrPendienteJob> verListaJobs(String idMicrosite) {
		try {
			SolrPendienteDelegate solrPendienteDel = DelegateUtil.getSolrPendienteDelegate();	
			List<SolrPendienteJob> resultados = solrPendienteDel.getListJobs(5, IndexacionUtil.TIPO_MICROSITE, idMicrosite);
			for(SolrPendienteJob resultado : resultados) {
				resultado.setDescripcion(null);
			}
			return resultados;
		} catch(Exception exception) {
			return null;
		}
	}
	
	private Boolean indexarMicrosite(String idMicrosite ) throws Exception {
		
		try{					
			if (idMicrosite == null)
				return false;
			else {				
				IndexacionJobUtil.crearJob(IndexacionUtil.TIPO_MICROSITE, IndexacionUtil.TIPO_MICROSITE, Long.valueOf(idMicrosite));                
			}	
		}catch(Exception e){
			log.error("Error indexando microsite: " + idMicrosite );
			return false;
		}
	 		     	
		return true;
		
	
	}
	
}
