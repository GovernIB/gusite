package es.caib.gusite.microback.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.Usuario;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.SolrPendienteDelegate;
import es.caib.gusite.micropersistence.delegate.UsuarioDelegate;

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
			indexarMicrosite(idSite);
			
			
			request.setAttribute("ok", true);
			addMessage(request, "indexa.ok");
		}
        return mapping.findForward(elforward);
	
	}	
		

	private Boolean indexarMicrosite(String idMicrosite ) throws Exception {
		
		try{					
			if (idMicrosite == null)
				return false;
			else {				
				SolrPendienteDelegate solrPendienteDel = DelegateUtil.getSolrPendienteDelegate();			
				solrPendienteDel.indexarMicrosite(Long.parseLong(idMicrosite));
				
			}	
		}catch(Exception e){
			log.error("Error indexando microsite: " + idMicrosite );
		}
	 		     	
		return true;
		
	
	}
	
}
