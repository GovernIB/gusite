package es.caib.gusite.microfront.faq.actions;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import org.apache.struts.action.ActionForward;


import es.caib.gusite.micromodel.Faq;
import es.caib.gusite.micromodel.TraduccionTemafaq;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.FaqDelegate;

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
