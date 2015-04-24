package es.caib.gusite.microback.action.edita;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import es.caib.gusite.microback.action.BaseAction;
import es.caib.gusite.microback.actionform.formulario.faqForm;
import es.caib.gusite.microback.utils.VOUtils;
import es.caib.gusite.micromodel.Faq;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.Temafaq;
import es.caib.gusite.micromodel.TraduccionAgenda;
import es.caib.gusite.micromodel.TraduccionEncuestaPK;
import es.caib.gusite.micromodel.TraduccionFaq;
import es.caib.gusite.micromodel.TraduccionFaqPK;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.FaqDelegate;
import es.caib.gusite.micropersistence.delegate.TemaDelegate;

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
	
    public ActionForward doExecute(ActionMapping mapping, ActionForm form, 
    		HttpServletRequest request, HttpServletResponse response) throws Exception {
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
	        
	        if (f.get("id") == null) { 
	        	VOUtils.populate(faq, f);  // form --> bean
	        } else {
        	  	List<TraduccionFaq> llista = (List<TraduccionFaq>)((DynaActionForm) form).get("traducciones");
	    		List<?> langs1 = DelegateUtil.getIdiomaDelegate().listarIdiomas();
	    		
	    		for (int i=0; i<llista.size(); i++)
	    		{
	    			if (faq.getTraducciones().containsKey(((Idioma)langs1.get(i)).getLang()))
	    			{
	    				faq.getTraducciones().get(((Idioma)langs1.get(i)).getLang()).setPregunta(llista.get(i).getPregunta());
	    				faq.getTraducciones().get(((Idioma)langs1.get(i)).getLang()).setRespuesta(llista.get(i).getRespuesta());
	    				faq.getTraducciones().get(((Idioma)langs1.get(i)).getLang()).setUrl(llista.get(i).getUrl());
	    				faq.getTraducciones().get(((Idioma)langs1.get(i)).getLang()).setUrlnom(llista.get(i).getUrlnom());
	    			} else {
	    				TraduccionFaq traduccio = new TraduccionFaq();
	    				TraduccionFaqPK idt = new TraduccionFaqPK();
	    				idt.setCodigoFaq(faq.getId());
	    				idt.setCodigoIdioma( ((Idioma)langs1.get(i)).getLang());
	    				traduccio.setId(idt);
	    				traduccio.setPregunta(llista.get(i).getPregunta());
	    				traduccio.setRespuesta(llista.get(i).getRespuesta());
	    				traduccio.setUrl(llista.get(i).getUrl());
	    				traduccio.setUrlnom(llista.get(i).getUrlnom());
	    				faq.getTraducciones().put(((Idioma)langs1.get(i)).getLang(), traduccio);
	    			}
	    		}
	        }
	
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

