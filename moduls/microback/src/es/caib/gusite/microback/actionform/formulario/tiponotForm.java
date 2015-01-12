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
import es.caib.gusite.micromodel.Tipo;
import es.caib.gusite.micromodel.TraduccionTipo;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.IdiomaDelegate;

/**
 *	Formulario para tipos de noticias (listados)
 *
 *@author Indra
 *
 */
public class tiponotForm extends TraDynaActionForm {

	private static final long serialVersionUID = -1169178114562035877L;
	protected static Log log = LogFactory.getLog(tiponotForm.class);

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
    	if (request.getParameter("espera") == null) 
    		super.reset(mapping, request);
    }

   
    /* (non-Javadoc)
     * @see org.apache.struts.action.ActionForm#validate(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
     */
    public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest httpServletRequest) {

    	ActionErrors errors = new ActionErrors();
    	String accion = ""+httpServletRequest.getParameter("accion");
    	
    	if(accion.equals(propertiesMessages.getString("operacion.guardar"))) {
    		
    		String tipoelemento = (String)get("tipoelemento");
    		if(!tipoelemento.equals(Tipo.TIPO_CONEXIO_EXTERNA))
    		{
    			if (!esEntero(""+get("tampagina")) || (get("tampagina")==null) )
    				errors.add("tampagina", new ActionError("error.tiponot.tampagina"));
    		}
    		else
    		{
        		String url = (String)get("xurl");
    			if((url == null) || (url.equals("")))
    				errors.add("xurl", new ActionError("error.tiponot.xurl"));
    		}
        
    		try {
	        	IdiomaDelegate idiomaDelegate = DelegateUtil.getIdiomaDelegate();
	        	List lang = idiomaDelegate.listarLenguajes();
	        	
	        	for (int i=0;i<lang.size();i++) {
	        		if (lang.get(i).equals(Idioma.getIdiomaPorDefecto())) {
	        			TraduccionTipo  trad = (TraduccionTipo)((ArrayList)get("traducciones")).get(i);
	        			if (trad.getNombre().length()==0)
	        				errors.add("titulo", new ActionError("error.tiponot.nombre"));
	        		}
	        	}
				
	        } catch (Throwable t) {
	            log.error("Error comprobando la validación de un tipo de elemento.", t);
	        }    
    		
    	}
    	
    	return errors;

    }
        

    
}
