package es.caib.gusite.microback.action.lista;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.gusite.microback.action.BaseAction;
import es.caib.gusite.microback.actionform.listaActionForm;
import es.caib.gusite.microback.actionform.formulario.faqForm;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.FaqDelegate;
import es.caib.gusite.micropersistence.delegate.TemaDelegate;

/**
 * Action que valida y trata el listado de faqs de un microsite <BR>
 * <P>
 * 	Definición Struts:<BR>
 *  action path="/faqsAcc" <BR> 
 *  name="listaActionForm" <BR> 
 *  scope="request" <BR>
 *  forward name="detalleFaq" path="/detalleFaq.jsp" 
 *  
 *  @author Indra
 */
public class listaFaqsAction extends BaseAction {

    /**
     * This is the main action called from the Struts framework.
     * @param mapping The ActionMapping used to select this instance.
     * @param form The optional ActionForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     * @return 
     */

	protected static Log log = LogFactory.getLog(listaFaqsAction.class);
	
    public ActionForward doExecute(ActionMapping mapping, ActionForm form, 
    		HttpServletRequest request, HttpServletResponse response) throws Exception  {

        listaActionForm f = (listaActionForm) form;
        
  
        //********************************************************
        //************* ERROR DE VALIDACION **********************
        //********************************************************
        if (request.getSession().getAttribute("faqForm")!=null && request.getAttribute(Globals.ERROR_KEY)!=null) {
        	faqForm fdet=(faqForm) request.getSession().getAttribute("faqForm");
        	request.setAttribute("faqForm", fdet);
            // Relleno el combo de Temas
            TemaDelegate bdTema = DelegateUtil.getTemafaqDelegate();
            request.setAttribute("temasCombo", bdTema.listarCombo(((Microsite)request.getSession().getAttribute("MVS_microsite")).getId()));
            return mapping.findForward("detalleFaq");
        }
        //********************************************************
        //********************** CREAMOS *************************
        //********************************************************
        if ( f.getAccion().equals("crear")) {
        	request.getSession().removeAttribute("faqForm");
            // Relleno el combo de Temas
            TemaDelegate bdTema = DelegateUtil.getTemafaqDelegate();
            request.setAttribute("temasCombo", bdTema.listarCombo(((Microsite)request.getSession().getAttribute("MVS_microsite")).getId()));
        	return mapping.findForward("detalleFaq");
        }
        //********************************************************
        //********************* BORRAMOS *************************
        //********************************************************
        if ( f.getAccion().equals("borrar")) {
            Long id =null;
            FaqDelegate bdFaq = DelegateUtil.getFaqDelegate();
            
            String lis="";
            for (int i=0;i<f.getSeleccionados().length;i++) {
                id = new Long(f.getSeleccionados()[i]);
                bdFaq.borrarFaq(id);
                lis+=id+", ";
            }
            lis=lis.substring(0,lis.length()-2);
            
            addMessage(request, "mensa.listafaqs");
            addMessage(request, "mensa.listafaqborradas", new String(lis));
            
            return mapping.findForward("info");
        }

        addMessage(request, "peticion.error");
        return mapping.findForward("info");

    }

}