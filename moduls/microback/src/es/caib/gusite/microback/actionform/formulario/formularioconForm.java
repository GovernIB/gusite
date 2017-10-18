package es.caib.gusite.microback.actionform.formulario;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.*;
import es.caib.gusite.microback.actionform.TraDynaActionForm;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.TraduccionFContacto;
import es.caib.gusite.micromodel.TraduccionTipo;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.IdiomaDelegate;
/**
 *	Formulario para los formularios de contacto
 *
 *@author Indra
 *
 */
public class formularioconForm extends TraDynaActionForm {


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
    	
			try {
				IdiomaDelegate idiomaDelegate = DelegateUtil.getIdiomaDelegate();
				List<?> lang = idiomaDelegate.listarLenguajes();
				
				for (int i=0;i<lang.size();i++) {
					TraduccionFContacto  trad = (TraduccionFContacto)((ArrayList) get("traducciones")).get(i);
					if (lang.get(i).equals(Idioma.getIdiomaPorDefecto())) {
						if (trad.getNombre().length()==0)
							errors.add("titulo", new ActionError("error.tiponot.nombre"));
					} else if ((trad.getNombre().length()==0 && trad.getUri().length() > 0) || 
							(trad.getUri().length()==0 && trad.getNombre().length() > 0)) {
							errors.add("titulo", new ActionError("error.conte.titulo2", idiomaDelegate.obtenerIdioma("" + lang.get(i)).getNombre() ));
			    	} 
				}
			} catch (Throwable t) {
			    log.error("Error comprobando la validación de un tipo de elemento.", t);
			} 
    	
    	
    	
	    	return errors;

    }

    
}
