package org.ibit.rol.sac.microfront.faq.actions;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import org.apache.struts.action.ActionForward;
import org.ibit.rol.sac.micromodel.Faq;

import org.ibit.rol.sac.micromodel.TraduccionTemafaq;
import org.ibit.rol.sac.micropersistence.delegate.DelegateException;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;
import org.ibit.rol.sac.micropersistence.delegate.FaqDelegate;
import java.io.IOException;


/**
 * Action faqAction para probar FAQ
 * @author Indra
 *
 */
public class faqAction extends Action {
	  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, DelegateException {
		    
			//VRS: PROBAR FAQ
		    FaqDelegate faqdel = DelegateUtil.getFaqDelegate();
		    Faq faq = new Faq();
		    faq = faqdel.obtenerFaq( new Long(1));
		  
		    TraduccionTemafaq temafaqtra= (TraduccionTemafaq)faq.getTema().getTraduccion("ca");
		      
			//VRS: FIN PROBAR FAQ
		  
		    return mapping.findForward("errorhome");
	  }
}
