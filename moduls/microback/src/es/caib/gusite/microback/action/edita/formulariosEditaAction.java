package es.caib.gusite.microback.action.edita;

import java.util.Iterator;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.gusite.microback.action.BaseAction;
import es.caib.gusite.microback.actionform.formulario.formularioconForm;
import es.caib.gusite.micromodel.Contacto;
import es.caib.gusite.micromodel.Lineadatocontacto;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.MicrositeCompleto;
import es.caib.gusite.micropersistence.delegate.ContactoDelegate;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;


/**
 * Action que edita los formularios de un microsite <BR>
 * <P>
 * 	Definición Struts:<BR>
 *  action path="/formularioEdita" <BR> 
 *  name="formularioconForm" <BR> 
 *  input="/formulariosAcc.do"  <BR>
 *	scope="session" <BR>
 *  unknown="false" <BR>
 *  forward name="detalle" path="/detalleFormulario.jsp" <BR>
 *  
 *  @author Indra
 */
public class formulariosEditaAction extends BaseAction 
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
	
	protected static Log log = LogFactory.getLog(formulariosEditaAction.class);
	
    public ActionForward doExecute(ActionMapping mapping, ActionForm form, 
    		HttpServletRequest request, HttpServletResponse response) throws Exception {
    	ContactoDelegate bdFormu = DelegateUtil.getContactoDelegate();
    	Contacto formu=null;
    	formularioconForm formularioconForm = (formularioconForm) form;
    		
    	/********************** CREAR ************************/
    	if((""+request.getParameter("accion")).equals(getResources(request).getMessage("operacion.crear"))) {
    		
    		formularioconForm.resetForm(mapping, request);
    		request.setAttribute("formularioconForm",formularioconForm);
    		return mapping.findForward("detalle");
		
		/******************* GUARDAR  ***************************/
    	}else if (("" + request.getParameter("accion")).equals(getResources(request).getMessage("operacion.guardar"))) {
    		
			boolean isNuevaFormulario= false;
	        if (formularioconForm.get("id") == null) {  // ALTA
	        	formu = new Contacto(); 
	        	isNuevaFormulario = true;
	        } else { 									// MODIFICACION 
	           	formu = bdFormu.obtenerContacto((Long)formularioconForm.get("id"));
	           	//COMPROBACION DE IDES
	           	if (formu.getIdmicrosite().longValue()!=((Microsite)request.getSession().getAttribute("MVS_microsite")).getId().longValue()){
	           		addMessageError(request, "peticion.error");
	                return mapping.findForward("info");
	           	}
	        }
	        
	       	formu.setIdmicrosite(((Microsite)request.getSession().getAttribute("MVS_microsite")).getId());
	       	formu.setVisible(""+formularioconForm.get("visible"));
	       	formu.setEmail(""+formularioconForm.get("email")); 	
	    	formu.setAnexarch(""+formularioconForm.get("anexarch"));
	        bdFormu.grabarContacto(formu);
	        
	        formularioconForm.set("id",  formu.getId());
	        
	       	if(isNuevaFormulario)	addMessageWithDate(request, "formu.mensa.nuevaagenda");
	       	else		       		addMessageWithDate(request, "formu.mensa.modifagenda");
	       	
	       			       	
	       	return mapping.findForward("detalle");
               
    	/********************** BORRAR ************************/	
    	}else if((""+request.getParameter("accion")).equals(getResources(request).getMessage("operacion.borrar"))) {
 	   
	        Long id = (Long)formularioconForm.get("id");
	        bdFormu.borrarContacto(id);
	        formularioconForm.resetForm(mapping, request);
	        addMessageAlert(request, "formu.borrar.sinid");
	        return mapping.findForward("detalle");

    	/********************** BORRAMOS LINEAS DEL FORMULARIO ************************/
    	}else if((""+request.getParameter("accion")).equals(getResources(request).getMessage("operacion.borrarlinea"))) {
	    	Long id = (Long)formularioconForm.get("id");
    		bdFormu.eliminarLineas((String[])formularioconForm.get("seleccionados") , id);
	    	addMessageAlert(request, "mensa.listaformulinborradas");
	    	formu = bdFormu.obtenerContacto(id);
	    	Iterator<?> it = formu.getLineasdatocontacto().iterator();	
            ArrayList<Lineadatocontacto> lineas= new ArrayList<Lineadatocontacto>();
            while (it.hasNext()) {
            	lineas.add((Lineadatocontacto)it.next());
            }
            formularioconForm.set("lineasdatocontacto",lineas);	
            return mapping.findForward("detalle");

        } else {
    		/********************** EDITAR ************************/
    	    		
        	Long id = null;
	    	if (request.getParameter("id")!=null)      
	    		id = new Long(""+request.getParameter("id"));
	        else if ((Long)formularioconForm.get("id")!=null)      
	            id = new Long(""+formularioconForm.get("id"));    
	
	        if (id != null){
	            if (bdFormu.checkSite(((Microsite)request.getSession().getAttribute("MVS_microsite")).getId(),id)) {
	                addMessageError(request, "info.seguridad");
	                return mapping.findForward("info");
	            }
	
	            formu = bdFormu.obtenerContacto(id);
	            //COMPROBACION DE IDES
	            if (formu.getIdmicrosite().longValue()!=((Microsite)request.getSession().getAttribute("MVS_microsite")).getId().longValue()){
	            	addMessageError(request, "peticion.error");
	                return mapping.findForward("info");
	            }

	            formularioconForm fdet=(formularioconForm) form;
	            fdet.set("visible", formu.getVisible());
	            fdet.set("email", formu.getEmail());
	            fdet.set("anexarch", formu.getAnexarch());
	                
	            Iterator<?> it = formu.getLineasdatocontacto().iterator();	
	            ArrayList<Lineadatocontacto> lineas= new ArrayList<Lineadatocontacto>();
	            while (it.hasNext()) {
	            	lineas.add((Lineadatocontacto)it.next());
	            }
	            fdet.set("lineasdatocontacto",lineas);
	            
	            if(request.getSession().getAttribute("mensajeBorrarlineasformulario")!=null){  
	        		request.getSession().setAttribute("mensajeBorrarlineasformulario", null);
	                addMessageAlert(request, "mensa.listaformulinborradas");
	            }
	             
	        } else {
	        	addMessageError(request, "info.noposible");
	        	return mapping.findForward("info");
	        }
	        return mapping.findForward("detalle");
        }
    }
    

    
}
