package org.ibit.rol.sac.microback.action.edita;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.ibit.rol.sac.microback.action.BaseAction;
import org.ibit.rol.sac.microback.actionform.TraDynaActionForm;
import org.ibit.rol.sac.microback.utils.VOUtils;
import org.ibit.rol.sac.micromodel.Microsite;
import org.ibit.rol.sac.micromodel.Temafaq;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;
import org.ibit.rol.sac.micropersistence.delegate.TemaDelegate;

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
	
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
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
        VOUtils.populate(tema, f);  // form --> bean
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
