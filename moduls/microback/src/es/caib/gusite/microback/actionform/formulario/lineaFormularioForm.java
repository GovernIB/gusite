package es.caib.gusite.microback.actionform.formulario;

import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.*;

import es.caib.gusite.microback.actionform.TraDynaActionForm;
import es.caib.gusite.micromodel.Lineadatocontacto;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.IdiomaDelegate;

/**
 *	Formulario para las lineas de formulario de contacto
 *
 *@author Indra
 *
 */
public class lineaFormularioForm extends DynaActionForm  {

	private static final long serialVersionUID = -1130789843523865011L;
	protected static Log log = LogFactory.getLog(lineaFormularioForm.class);
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
	
	        set("obligatorio", new Integer(0));
	        set("orden", new Integer(0));
	        set("tamano", new Integer(0));
	        set("lineas", new Integer(0));
	        set("visible", "S");
	        
	        String[] etis;
	        String[] textos;
	        try {
	            IdiomaDelegate delegate = DelegateUtil.getIdiomaDelegate();
	            int numLangs = delegate.listarLenguajes().size();
	            etis = new String[numLangs];
	            textos= new String[numLangs*Lineadatocontacto.NUMERO_MAXIMO_SELECTOR];
	            for (int i = 0; i < numLangs; i++) {
	            	etis[i] = "";
	            	for (int j=0;j<10;j++)
	            		textos[(10*i)+j]="";
	            }
	            set("etiq", etis);
	            set("textos", textos);
	            set("numOptions","");
	        } catch (Throwable t) {
	            log.error("Error en formularioconForm", t);
	        }
    	}
        
    }
    
    
    /**
     * Método que resetea los campos del formulario
     * @param mapping	mapeo
     * @param request	petición de usuario
     */
    public void resetForm(ActionMapping mapping, HttpServletRequest request) {
        //log.info("reset.name=" + mapping.getName() + ", path=" + mapping.getPath());
        
        super.reset(mapping, request);

	    initialize(mapping);

        set("obligatorio", new Integer(0));
        set("orden", new Integer(0));
        set("tamano", new Integer(0));
        set("lineas", new Integer(0));
        set("visible", "S");
        
        String[] etis;
        String[] textos;
        try {
            IdiomaDelegate delegate = DelegateUtil.getIdiomaDelegate();
            int numLangs = delegate.listarLenguajes().size();
            etis = new String[numLangs];
            textos= new String[numLangs*Lineadatocontacto.NUMERO_MAXIMO_SELECTOR];
            for (int i = 0; i < numLangs; i++) {
            	etis[i] = "";
            	for (int j=0;j<10;j++)
            		textos[(10*i)+j]="";
            }
            set("etiq", etis);
            set("textos", textos);
            set("numOptions","");
        } catch (Throwable t) {
            log.error("Error en formularioconForm", t);
        }
 
    }  

    /* (non-Javadoc)
     * @see org.apache.struts.action.ActionForm#validate(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
     */
    public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest httpServletRequest) {

    	ActionErrors errors = new ActionErrors();
    	
    	String accion = ""+httpServletRequest.getParameter("accion");
    	
    	if(accion.equals(propertiesMessages.getString("operacion.guardar"))) {
        
    		if (!TraDynaActionForm.esEntero(""+get("orden")) )
            	errors.add("orden", new ActionError("error.lineaformu.orden"));
    		
    		if (!TraDynaActionForm.esEntero(""+get("tamano")) )
            	errors.add("tamano", new ActionError("error.lineaformu.tamano"));

    		if (!TraDynaActionForm.esEntero(""+get("lineas")) )
            	errors.add("lineas", new ActionError("error.lineaformu.lineas"));

    		String[] etis=(String[])get("etiq");
    		if ( etis[0].length()==0)
    			errors.add("etiq", new ActionError("error.lineaformu.etiq"));

    	}

    	return errors;

    }
    
}
