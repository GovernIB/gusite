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
import org.ibit.rol.sac.micromodel.Frqssi;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;
import org.ibit.rol.sac.micropersistence.delegate.FrqssiDelegate;

/**
 * Action que edita las frqssis de un microsite <BR>
 * <P>
 * 	Definición Struts:<BR>
 *  action path="/frqssiEdita" <BR> 
 *  name="frqssiForm" <BR> 
 *  input="/frqssisAcc.do"<BR>
 *	scope="session" <BR>
 *  unknown="false" <BR>
 *  forward name="detalle" path="/detalleFrqssi.jsp"
 *  
 *  @author Indra
 */
public class frqssisEditaAction extends BaseAction 
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
	
	protected static Log log = LogFactory.getLog(frqssisEditaAction.class);
	
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	FrqssiDelegate bdFrqssi = DelegateUtil.getFrqssiDelegate();
    	Frqssi frqssi=null;
    	TraDynaActionForm f = (TraDynaActionForm) form;
    	
    	if(request.getParameter("modifica")!=null || request.getParameter("anyade")!=null) {

        	if (f.get("id") == null) {  
        		frqssi = new Frqssi(); // Es Alta
            } else {  // Es modificacion
            	frqssi = bdFrqssi.obtenerFrqssi((Long)f.get("id"));
            	//************COMPROBACION DE IDES*************
            	if (frqssi.getIdmicrosite().longValue()!=((Microsite)request.getSession().getAttribute("MVS_microsite")).getId().longValue())
            	{
            		addMessage(request, "peticion.error");
                    return mapping.findForward("info");
            	}
            	//*********************************************
            }
        	
        frqssi.setIdmicrosite(((Microsite)request.getSession().getAttribute("MVS_microsite")).getId());
        frqssi.setCentro((String)f.get("centro"));
        frqssi.setTipoescrito((String)f.get("tipoescrito"));
        VOUtils.populate(frqssi, f);  // form --> bean
        bdFrqssi.grabarFrqssi(frqssi);

       	if(request.getParameter("anyade")!=null) 
       		addMessage(request, "mensa.nuevofrqssi");
       	if(request.getParameter("modifica")!=null)	
       		addMessage(request, "mensa.modiffrqssi");	

       	addMessage(request, "mensa.editarfrqssi", "" + frqssi.getId().longValue());
       	addMessage(request, "mensa.listafrqssis");
       	
   		org.ibit.rol.sac.microback.base.Base.micrositeRefresh(((Microsite)request.getSession().getAttribute("MVS_microsite")).getId(), request);
   		org.ibit.rol.sac.microback.base.Base.menuRefresh(request);
       	
       	return mapping.findForward("info");
               
       }
        
        //********************************************************
        //********************** EDITAMOS ************************
        //********************************************************
        if (request.getParameter("id")!=null) {     
            Long id = new Long(""+request.getParameter("id"));

                if (bdFrqssi.checkSite(((Microsite)request.getSession().getAttribute("MVS_microsite")).getId(),id)) {
                	addMessage(request, "info.seguridad");
                	return mapping.findForward("info");
                }

            	frqssi = bdFrqssi.obtenerFrqssi(id);
            	//************COMPROBACION DE IDES*************
            	if (frqssi.getIdmicrosite().longValue()!=((Microsite)request.getSession().getAttribute("MVS_microsite")).getId().longValue())
            	{
            		addMessage(request, "peticion.error");
                    return mapping.findForward("info");
            	}
            	//*********************************************
            	
               	f.set("centro", (String)frqssi.getCentro());
            	f.set("tipoescrito", (String)frqssi.getTipoescrito());
            	            	
            	TraDynaActionForm fdet=(TraDynaActionForm) form;
                VOUtils.describe(fdet, frqssi);  // bean --> form
                
           		org.ibit.rol.sac.microback.base.Base.micrositeRefresh(((Microsite)request.getSession().getAttribute("MVS_microsite")).getId(), request);
           		org.ibit.rol.sac.microback.base.Base.menuRefresh(request);

            return mapping.findForward("detalle");

        }

        addMessage(request, "peticion.error");
        return mapping.findForward("info");
    }
}
