package es.caib.gusite.microback.action;

import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.IndexerDelegate;
import es.caib.gusite.micropersistence.util.log.MicroLog;

/**
 * Action que indexa un microsite  <P>
 * 
 * 	Definición Struts:<BR>
 *  action path="/indexa"<BR> 
 *  unknown="false" <BR>
 *  
 *  @author - Indra
 */
public class IndexaAction extends BaseAction  {

	protected static Log log = LogFactory.getLog(IndexaAction.class);
	private static String[] roles = new String[]{"sacsystem", "sacadmin"};
		
	public ActionForward doExecute(ActionMapping mapping, ActionForm form, 
    		HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// Solo podrán indexar los roles sacsystem y sacadmin
		Hashtable<?, ?> rolenames = null;
		rolenames = (Hashtable<?, ?>) request.getSession().getAttribute("rolenames");
		if (!(rolenames.contains(roles[0]) || rolenames.contains(roles[1]))) {
			addMessage(request, "peticion.error");
			return mapping.findForward("info");
		}
		
		IndexerDelegate indexo = DelegateUtil.getIndexerDelegate();
		
		if (request.getParameter("site") != null) {
			MicroLog.addLog("Inici Indexació Microsite: ["
					+ request.getParameter("site") + "] , Usuari: ["
					+ request.getSession().getAttribute("username") + "]");
			Long site = new Long("" + request.getParameter("site"));
			
			indexo.reindexarMicrosite(site);
			
			MicroLog.addLog("Fi Indexació Microsite: ["
					+ request.getParameter("site") + "] , Usuari: ["
					+ request.getSession().getAttribute("username") + "]");
			addMessage(request, "indexa.ok");
		} else {
			addMessage(request, "indexa.nook");
		}
	
		return mapping.findForward("info");

	}
	
}