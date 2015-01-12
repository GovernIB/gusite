package org.ibit.rol.sac.microback.action;

import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.ibit.rol.sac.microback.utils.log.MicroLog;
import org.ibit.rol.sac.micromodel.Microsite;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;
import org.ibit.rol.sac.micropersistence.delegate.IndexerDelegate;

/**
 * Action que desindexa un microsite<P>
 * 
 * 	Definición Struts:<BR>
 *  path="/desindexa"<BR> 
 *  
 *  @author - Indra
 */
public class DesindexaAction extends BaseAction  {


	protected static Log log = LogFactory.getLog(IndexaAction.class);
	private static String[] roles = new String[]{"sacsystem", "sacadmin"};
		
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {

		
		// Solo podrán indexar los roles sacsystem y sacadmin
		Hashtable<?, ?> rolenames=null;
		rolenames=(Hashtable<?, ?>)request.getSession().getAttribute("rolenames");
		if (!(rolenames.contains(roles[0]) || rolenames.contains(roles[1]))) {
			addMessage(request, "peticion.error");
            return mapping.findForward("info");
		}		
		
		
		IndexerDelegate indexo = DelegateUtil.getIndexerDelegate();
		Microsite microsite = (Microsite)request.getSession().getAttribute("MVS_microsite");
		if (microsite.getId()!=null) {
			MicroLog.addLog("Inici DesIndexació Microsite: [" + microsite.getId() + "] , Usuari: [" + request.getSession().getAttribute("username") + "]");
            Long site = new Long(""+microsite.getId());
			indexo.desindexarMicrosite(site);
			MicroLog.addLog("Fi DesIndexació Microsite: [" + microsite.getId() + "] , Usuari: [" + request.getSession().getAttribute("username") + "]");
			addMessage(request, "desindexa.ok");
		} else {		
			addMessage(request, "desindexa.nook");
		}
	
		

		return mapping.findForward("info");

		
    }
	
}