package es.caib.gusite.microback.action.edita;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.gusite.microback.action.BaseAction;

public class MicroUsuarioEditaAction extends BaseAction{

	protected static Log log = LogFactory.getLog(MicroUsuarioEditaAction.class);
		
    @SuppressWarnings("unused")
	public ActionForward doExecute(ActionMapping mapping, ActionForm form, 
    		HttpServletRequest request, HttpServletResponse response) throws Exception {
//    	BannerDelegate bdBanner = DelegateUtil.getBannerDelegate();
//    	Banner ban=null;
//    	bannerForm f = (bannerForm) form;
    	
        addMessage(request, "peticion.error");
        return mapping.findForward("info");
    }
}
