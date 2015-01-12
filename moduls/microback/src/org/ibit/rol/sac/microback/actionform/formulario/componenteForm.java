package org.ibit.rol.sac.microback.actionform.formulario;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import org.ibit.rol.sac.microback.actionform.*;
import org.ibit.rol.sac.micromodel.Idioma;
import org.ibit.rol.sac.micromodel.TraduccionComponente;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;
import org.ibit.rol.sac.micropersistence.delegate.IdiomaDelegate;
import org.apache.struts.upload.FormFile;
import org.apache.struts.action.*;

/**
 *	Formulario para los componentes Tiny
 *
 *@author Indra
 *
 */
public class componenteForm extends TraDynaActionForm {

	private static final long serialVersionUID = 8658593847474478813L;
	protected static Log log = LogFactory.getLog(componenteForm.class);

    /* (non-Javadoc)
     * @see org.apache.struts.action.DynaActionForm#initialize(org.apache.struts.action.ActionMapping)
     */
    public void initialize(ActionMapping actionMapping) {
        super.initialize(actionMapping);
        inicio();
    }

    /* (non-Javadoc)
     * @see org.ibit.rol.sac.microback.actionform.TraDynaActionForm#reset(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
     */
    public void reset(ActionMapping mapping, HttpServletRequest request){
        
    	if (request.getParameter("espera") == null) {
    	super.reset(mapping, request);
        inicio();
    	}
    }

    private void inicio() {
    }
    
   
    /* (non-Javadoc)
     * @see org.apache.struts.action.ActionForm#validate(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
     */
    public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest httpServletRequest) {

    	ActionErrors errors = new ActionErrors();
    	String accion = ""+httpServletRequest.getParameter("accion");
    	
    	if(accion.equals(propertiesMessages.getString("operacion.guardar"))) {

    		if (get("imagen")!=null)
    			if (((FormFile)get("imagen")).getFileSize()>1024*100)
    				errors.add("imagen", new ActionError("error.componente.imagen"));
        
    		if ((""+get("idTipo")).equals("0") || (""+get("idTipo")).equals("null"))
    			errors.add("idTipo", new ActionError("error.componente.tipo"));
        
    		if (!esEntero(""+get("numelem")) )
    			errors.add("numelem", new ActionError("error.componente.numelem"));
    		
    		if ((""+get("numelem")).equals("null"))
    			errors.add("numelem", new ActionError("error.componente.numelem.obligatori"));
    		
    		if ( (""+get("nombre")).length()==0)
    			errors.add("nombre", new ActionError("error.componente.nombre"));
    		
            try {
            	IdiomaDelegate idiomaDelegate = DelegateUtil.getIdiomaDelegate();
            	List<?> lang = idiomaDelegate.listarLenguajes();

            	for (int i=0;i<lang.size();i++) {
            		TraduccionComponente  trad = (TraduccionComponente)((ArrayList<?>)get("traducciones")).get(i);

        			if (lang.get(i).equals(Idioma.DEFAULT)) {
            			if (trad.getTitulo().length()==0)
            				errors.add("titulo", new ActionError("error.componente.titulo"));
            		}
            		
            	}
    			
    		
            } catch (Throwable t) {
                log.error("Error comprobando la validación del elemento", t);
            }
    		
    		
    		
    	}

    	return errors;

    }
    
}
