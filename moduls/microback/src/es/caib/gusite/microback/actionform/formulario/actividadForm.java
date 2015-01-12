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
import es.caib.gusite.micromodel.TraduccionActividadagenda;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.IdiomaDelegate;

/**
 *	Formulario para las actividades en agendas
 *
 *@author Indra
 *
 */
public class actividadForm extends TraDynaActionForm {

	private static final long serialVersionUID = 2458936701605163965L;
	protected static Log log = LogFactory.getLog(actividadForm.class);

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
    	
    	String accion = ""+httpServletRequest.getParameter("accion");
    	
    	if(accion.equals(propertiesMessages.getString("operacion.guardar"))) {
    	       try {
    	        	IdiomaDelegate idiomaDelegate = DelegateUtil.getIdiomaDelegate();
    	        	List lang = idiomaDelegate.listarLenguajes();
    	        	
    	        	for (int i=0;i<lang.size();i++) {
    	        		if (lang.get(i).equals(Idioma.getIdiomaPorDefecto())) {
    	        			TraduccionActividadagenda  trad = (TraduccionActividadagenda)((ArrayList)get("traducciones")).get(i);
    	        			if (trad.getNombre().length()==0)
    	        				errors.add("titulo", new ActionError("error.actividad.nombre"));
    	        		}
    	        	}
    				
    	        } catch (Throwable t) {
    	            log.error("Error comprobando la validacion del nombre de la actividad", t);
    	        }    
    	 }
    	
    	
    	return errors;

    }
        

    
}