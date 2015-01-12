package org.ibit.rol.sac.microback.action.edita;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.ibit.rol.sac.microback.action.BaseAction;
import org.ibit.rol.sac.microback.actionform.formulario.bannerForm;
import org.ibit.rol.sac.micromodel.Banner;
import org.ibit.rol.sac.micropersistence.delegate.BannerDelegate;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;

public class MicroUsuarioEditaAction extends BaseAction{

	protected static Log log = LogFactory.getLog(MicroUsuarioEditaAction.class);
		
    @SuppressWarnings("unused")
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	BannerDelegate bdBanner = DelegateUtil.getBannerDelegate();
    	Banner ban=null;
    	bannerForm f = (bannerForm) form;
    	
        addMessage(request, "peticion.error");
        return mapping.findForward("info");
    }
}
