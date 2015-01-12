package es.caib.gusite.microback.actionform.formulario;

import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.*;

/**
 *	Formulario para los formularios de contacto
 *
 *@author Indra
 *
 */
public class formularioconForm extends DynaActionForm {


	private static final long serialVersionUID = -7052220811314867078L;
	protected static Log log = LogFactory.getLog(formularioconForm.class);
    protected ResourceBundle propertiesMessages = ResourceBundle.getBundle("sac-microback-messages");
    
    /* (non-Javadoc)
     * @see org.apache.struts.action.DynaActionForm#initialize(org.apache.struts.action.ActionMapping)
     */
    public void initialize(ActionMapping actionMapping) {
        super.initialize(actionMapping);
    }

    /* (non-Javadoc)
     * @see org.apache.struts.action.DynaActionForm#reset(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
     */
    public void reset(ActionMapping mapping, HttpServletRequest request){
    	if (request.getParameter("espera") == null) {
    	super.reset(mapping, request);
        set("seleccionados",null);
    	}
    }
    
    /**
     * Método que resetea los campos del formulario
     * @param mapping	mapeo
     * @param request	petición de usuario
     */
    public void resetForm(ActionMapping mapping, HttpServletRequest request) {
        
        super.reset(mapping, request);

	    initialize(mapping);
	    set("seleccionados",null);
	
 
    }  
  
    /* (non-Javadoc)
     * @see org.apache.struts.action.ActionForm#validate(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
     */
    public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest httpServletRequest) {

    	ActionErrors errors = new ActionErrors();
    	
    	String accion = ""+httpServletRequest.getParameter("accion");
    	
    	if(accion.equals(propertiesMessages.getString("operacion.guardar"))) {

	        if (!(""+get("email")).matches(".+@.+\\.[a-z]+"))
	       		errors.add("email", new ActionError("error.formulario.email"));
	    	}
	    	return errors;

    }

    
}
