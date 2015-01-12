package es.caib.gusite.microback.actionform.formulario;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.*;

import es.caib.gusite.microback.actionform.*;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.TraduccionFrqssi;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.IdiomaDelegate;

/**
 *	Formulario para las frqssi
 *
 *@author Indra
 *
 */
public class frqssiForm extends TraDynaActionForm {

	private static final long serialVersionUID = -4097559986091952835L;
	protected static Log log = LogFactory.getLog(frqssiForm.class);

    /* (non-Javadoc)
     * @see org.apache.struts.action.DynaActionForm#initialize(org.apache.struts.action.ActionMapping)
     */
    public void initialize(ActionMapping actionMapping) {
        super.initialize(actionMapping);
    }

    /* (non-Javadoc)
     * @see es.caib.gusite.microback.actionform.TraDynaActionForm#reset(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
     */
    public void reset(ActionMapping mapping, HttpServletRequest request){
        super.reset(mapping, request);
    }

   
    /* (non-Javadoc)
     * @see org.apache.struts.action.ActionForm#validate(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
     */
    public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest httpServletRequest) {

    	ActionErrors errors = new ActionErrors();
    	
    	if(httpServletRequest.getParameter("modifica")!=null || httpServletRequest.getParameter("anyade")!=null) {

    		/*if (get("centro").toString().length()==0 )
            	errors.add("centro", new ActionError("error.frqssi.centro"));
    		
       		if (get("tipoescrito").toString().length()==0) 
            	errors.add("tipoescrito", new ActionError("error.frqssi.tipoescrito"));    */		
        
    		try {
	        	IdiomaDelegate idiomaDelegate = DelegateUtil.getIdiomaDelegate();
	        	List lang = idiomaDelegate.listarLenguajes();
	        	
	        	for (int i=0;i<lang.size();i++) {
	        		if (lang.get(i).equals(Idioma.getIdiomaPorDefecto())) {
	        			TraduccionFrqssi  trad = (TraduccionFrqssi)((ArrayList)get("traducciones")).get(i);
	        			if (trad.getNombre().length()==0)
	        				errors.add("titulo", new ActionError("error.frqssi.nombre"));
	        		}
	        	}
				
	        } catch (Throwable t) {
	            log.error("Error comprobando la validación de un Formulario QSSI.", t);
	        }    
    		
    	}
    	
    	return errors;

    }
        

    
}
