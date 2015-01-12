package org.ibit.rol.sac.microback.action.edita;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.ibit.rol.sac.microback.action.BaseAction;
import org.ibit.rol.sac.microback.actionform.formulario.faqForm;
import org.ibit.rol.sac.microback.utils.VOUtils;
import org.ibit.rol.sac.micromodel.Faq;
import org.ibit.rol.sac.micromodel.Microsite;
import org.ibit.rol.sac.micromodel.Temafaq;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;
import org.ibit.rol.sac.micropersistence.delegate.FaqDelegate;
import org.ibit.rol.sac.micropersistence.delegate.TemaDelegate;

/**
 * Action que edita las faqs de un microsite <BR>
 * <P>
 * 	Definición Struts:<BR>
 *  action path="/faqEdita" <BR> 
 *  name="faqForm" <BR> 
 *  input="/faqsAcc.do"  <BR>
 *	scope="session" <BR>
 *  unknown="false" <BR>
 *  forward name="detalle" path="/detalleFaq.jsp" <BR>
 *  
 *  @author Indra
 */
public class faqsEditaAction extends BaseAction 
{
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
	
	protected static Log log = LogFactory.getLog(faqsEditaAction.class);
	
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	FaqDelegate bdFaq = DelegateUtil.getFaqDelegate();
    	Faq faq=null;
    	faqForm f = (faqForm) form;
    	

    	if(request.getParameter("modifica")!=null || request.getParameter("anyade")!=null) {

        	if (f.get("id") == null) {  
        		faq = new Faq(); // Es Alta
            } else {  // Es modificacion
            	faq = bdFaq.obtenerFaq((Long)f.get("id"));
            	//************COMPROBACION DE IDES*************
            	if (faq.getIdmicrosite().longValue()!=((Microsite)request.getSession().getAttribute("MVS_microsite")).getId().longValue())
            	{
            		addMessage(request, "peticion.error");
                    return mapping.findForward("info");
            	}
            	//*********************************************
            }
        	
	        faq.setIdmicrosite(((Microsite)request.getSession().getAttribute("MVS_microsite")).getId());
	        faq.setFecha(f.getFecha());
	        // Establezco el objeto Tema
	        Temafaq tem=null;
	        TemaDelegate bdTema = DelegateUtil.getTemafaqDelegate();
	        if (f.get("id") == null)  	tem = bdTema.obtenerTema((Long)f.get("idTema"));
	        else 			        	tem=faq.getTema();
	        tem.setId((Long)f.get("idTema"));
	        faq.setTema(tem);
	        
	        faq.setVisible(""+f.get("visible"));
	        
	        VOUtils.populate(faq, f);  // form --> bean
	
	       	bdFaq.grabarFaq(faq);
	
	       	//log.info("Creado/Actualizado " + faq.getId());
	      
	       	if(request.getParameter("anyade")!=null) 
	       		addMessage(request, "mensa.nuevafaq");
	       	if(request.getParameter("modifica")!=null)	
	       		addMessage(request, "mensa.modiffaq");	
	       	
	   		addMessage(request, "mensa.editarfaq", "" + faq.getId().longValue());
	       	addMessage(request, "mensa.listafaqs");
	       	
	       	return mapping.findForward("info");
               
       }
        
        //********************************************************
        //********************** EDITAMOS ************************
        //********************************************************
        if (request.getParameter("id")!=null) {     
            Long id = new Long(""+request.getParameter("id"));
            	
                if (bdFaq.checkSite(((Microsite)request.getSession().getAttribute("MVS_microsite")).getId(),id)) {
                	addMessage(request, "info.seguridad");
                	return mapping.findForward("info");
                }

            	faq = bdFaq.obtenerFaq(id);
            	
            	//************COMPROBACION DE IDES*************
            	if (faq.getIdmicrosite().longValue()!=((Microsite)request.getSession().getAttribute("MVS_microsite")).getId().longValue())
            	{
            		addMessage(request, "peticion.error");
                    return mapping.findForward("info");
            	}
            	//*********************************************
            	
                f.setFecha(faq.getFecha());
                f.set("idTema",faq.getTema().getId());
                f.set("visible",faq.getVisible());

                VOUtils.describe(f, faq);  // bean --> form
                
                // Relleno el combo de Temas
                TemaDelegate bdTema = DelegateUtil.getTemafaqDelegate();
                request.setAttribute("temasCombo", bdTema.listarCombo(((Microsite)request.getSession().getAttribute("MVS_microsite")).getId()));
                
            return mapping.findForward("detalle");

        }

        addMessage(request, "peticion.error");
        return mapping.findForward("info");
    }
}

