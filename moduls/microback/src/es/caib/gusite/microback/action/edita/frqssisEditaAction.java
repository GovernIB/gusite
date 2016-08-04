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
import es.caib.gusite.micromodel.Frqssi;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.TraduccionContenido;
import es.caib.gusite.micromodel.TraduccionEncuestaPK;
import es.caib.gusite.micromodel.TraduccionFrqssi;
import es.caib.gusite.micromodel.TraduccionFrqssiPK;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.FrqssiDelegate;

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
	
    public ActionForward doExecute(ActionMapping mapping, ActionForm form, 
    		HttpServletRequest request, HttpServletResponse response) throws Exception {
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
	        
	        if (f.get("id") == null) {
	        	VOUtils.populate(frqssi, f);  // form --> bean
	        } else {
	    		List<TraduccionFrqssi> llista = (List<TraduccionFrqssi>) ((DynaActionForm) form).get("traducciones");
	    		List<?> langs = DelegateUtil.getIdiomaDelegate().listarIdiomas();
	    		
	    		for (int i=0; i<llista.size(); i++)
	    		{
	    			if (frqssi.getTraducciones().containsKey(((Idioma)langs.get(i)).getLang()))
	    			{
	    				frqssi.getTraducciones().get(((Idioma)langs.get(i)).getLang()).setNombre(llista.get(i).getNombre());
	    			} else {
	    				TraduccionFrqssi traduccio = new TraduccionFrqssi();
	    				TraduccionFrqssiPK idt = new TraduccionFrqssiPK();
	    				idt.setCodigoFrqssi(frqssi.getId());
	    				idt.setCodigoIdioma( ((Idioma)langs.get(i)).getLang());
	    				traduccio.setId(idt);
	    				traduccio.setNombre(llista.get(i).getNombre());
	    				
	    				frqssi.getTraducciones().put(((Idioma)langs.get(i)).getLang(), traduccio);
	    			}
	    		}
	    	}
	        bdFrqssi.grabarFrqssi(frqssi);
	
	       	if(request.getParameter("anyade")!=null) 
	       		addMessage(request, "mensa.nuevofrqssi");
	       	if(request.getParameter("modifica")!=null)	
	       		addMessage(request, "mensa.modiffrqssi");	
	
	       	addMessage(request, "mensa.editarfrqssi", "" + frqssi.getId().longValue());
	       	addMessage(request, "mensa.listafrqssis");
	       	
	   		es.caib.gusite.microback.base.Base.micrositeRefresh(((Microsite)request.getSession().getAttribute("MVS_microsite")).getId(), request);
	   		es.caib.gusite.microback.base.Base.menuRefresh(request);
	       	
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
                
           		es.caib.gusite.microback.base.Base.micrositeRefresh(((Microsite)request.getSession().getAttribute("MVS_microsite")).getId(), request);
           		es.caib.gusite.microback.base.Base.menuRefresh(request);

            return mapping.findForward("detalle");

        }

        addMessage(request, "peticion.error");
        return mapping.findForward("info");
    }
}
