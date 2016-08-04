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
import es.caib.gusite.microback.actionform.TraDynaActionForm;
import es.caib.gusite.microback.utils.VOUtils;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.Temafaq;
import es.caib.gusite.micromodel.TraduccionEncuestaPK;
import es.caib.gusite.micromodel.TraduccionTemafaq;
import es.caib.gusite.micromodel.TraduccionTemafaqPK;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.TemaDelegate;

/**
 * Action que edita los temas de un microsite <BR>
 * <P>
 * 	Definición Struts:<BR>
 *  action path="/temaEdita" <BR> 
 *  name="temaForm" <BR> 
 *  input="/temasAcc.do"   <BR>
 *	scope="session" <BR>
 *  unknown="false" <BR>
 *  forward name="detalle" path="/detalleTema.jsp"
 *  
 *  @author Indra
 */
public class temasEditaAction extends BaseAction 
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
	
	protected static Log log = LogFactory.getLog(temasEditaAction.class);
	
    public ActionForward doExecute(ActionMapping mapping, ActionForm form, 
    		HttpServletRequest request, HttpServletResponse response) throws Exception {
    	TemaDelegate bdTema = DelegateUtil.getTemafaqDelegate();
    	Temafaq tema=null;
    	TraDynaActionForm f = (TraDynaActionForm) form;
    	
      	if(request.getParameter("modifica")!=null || request.getParameter("anyade")!=null) {

        	if (f.get("id") == null) {  
        		tema = new Temafaq(); // Es Alta
            } else {  // Es modificacion
            	tema = bdTema.obtenerTema((Long)f.get("id"));
            	//************COMPROBACION DE IDES*************
            	if (tema.getIdmicrosite().longValue()!=((Microsite)request.getSession().getAttribute("MVS_microsite")).getId().longValue())
            	{
            		addMessage(request, "peticion.error");
                    return mapping.findForward("info");
            	}
            	//*********************************************
            }
        	
        tema.setIdmicrosite(((Microsite)request.getSession().getAttribute("MVS_microsite")).getId());
        if (f.get("id") == null) {
        	VOUtils.populate(tema, f);  // form --> bean
        } else {
        	List<TraduccionTemafaq> llista = (List<TraduccionTemafaq>)((DynaActionForm) form).get("traducciones");
    		List<?> langs = DelegateUtil.getIdiomaDelegate().listarIdiomas();
    		
    		for (int i=0; i<llista.size(); i++)
    		{
    			if (tema.getTraducciones().containsKey(((Idioma)langs.get(i)).getLang()))
    			{
    				tema.getTraducciones().get(((Idioma)langs.get(i)).getLang()).setNombre(llista.get(i).getNombre());
    			} else {
    				TraduccionTemafaq traduccio = new TraduccionTemafaq();
    				TraduccionTemafaqPK idt = new TraduccionTemafaqPK();
    				idt.setCodigoTema(tema.getId());
    				idt.setCodigoIdioma( ((Idioma)langs.get(i)).getLang());
    				traduccio.setId(idt);
    				traduccio.setNombre(llista.get(i).getNombre());
    				
    				tema.getTraducciones().put(((Idioma)langs.get(i)).getLang(), traduccio);
    			}
    		}
        }
        bdTema.grabarTema(tema);

       	//log.info("Creado/Actualizado " + tema.getId());
      
       	if(request.getParameter("anyade")!=null) 
       		addMessage(request, "mensa.nuevotema");
       	if(request.getParameter("modifica")!=null)	
       		addMessage(request, "mensa.modiftema");	
       	
   		addMessage(request, "mensa.editartema", "" + tema.getId().longValue());
       	addMessage(request, "mensa.listatemas");
       	
       	return mapping.findForward("info");
               
       }
        
        //********************************************************
        //********************** EDITAMOS ************************
        //********************************************************
        if (request.getParameter("id")!=null) {     
            Long id = new Long(""+request.getParameter("id"));
            	
                if (bdTema.checkSite(((Microsite)request.getSession().getAttribute("MVS_microsite")).getId(),id)) {
                	addMessage(request, "info.seguridad");
                	return mapping.findForward("info");
                }

            	tema = bdTema.obtenerTema(id);
            	//************COMPROBACION DE IDES*************
            	if (tema.getIdmicrosite().longValue()!=((Microsite)request.getSession().getAttribute("MVS_microsite")).getId().longValue())
            	{
            		addMessage(request, "peticion.error");
                    return mapping.findForward("info");
            	}
            	//*********************************************
            	TraDynaActionForm fdet=(TraDynaActionForm) form;
                VOUtils.describe(fdet, tema);  // bean --> form

            return mapping.findForward("detalle");

        }

        addMessage(request, "peticion.error");
        return mapping.findForward("info");
    }
}
