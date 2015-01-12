package org.ibit.rol.sac.microback.actionform.formulario;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import org.ibit.rol.sac.microback.actionform.*;
import org.ibit.rol.sac.micromodel.*;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;
import org.ibit.rol.sac.micropersistence.delegate.IdiomaDelegate;
import org.apache.struts.action.*;

/**
 *	Formulario para los contenidos
 *
 *@author Indra
 *
 */
public class contenidoForm extends TraDynaActionForm {


	private static final long serialVersionUID = -3575980351954309067L;
    protected static Log log = LogFactory.getLog(contenidoForm.class);

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
        super.reset(mapping, request);
        
        String fecha=(String) get("fpublicacion");
        if (fecha.length()==0){
        	set("fpublicacion", new SimpleDateFormat("dd/MM/yyyy 00:00").format(new java.util.Date()));
        }	
        inicio();
    }
    
    /* (non-Javadoc)
     * @see org.ibit.rol.sac.microback.actionform.TraDynaActionForm#resetForm(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
     */
    public void resetForm(ActionMapping mapping, HttpServletRequest request){
        super.resetForm(mapping, request);
        
         set("fpublicacion", new SimpleDateFormat("dd/MM/yyyy 00:00").format(new java.util.Date()));
        
        inicio();
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

    	if (!FechaValida(""+get("fpublicacion"))) 
            errors.add("fpublicacion", new ActionError("error.conte.fpublicacion"));
        
        if (!esEntero(""+get("orden")))
        	errors.add("orden", new ActionError("error.conte.orden"));
        
        try {
        	IdiomaDelegate idiomaDelegate = DelegateUtil.getIdiomaDelegate();
        	List lang = idiomaDelegate.listarLenguajes();
        	
        	for (int i=0;i<lang.size();i++) {
        		if (lang.get(i).equals(Idioma.DEFAULT)) {
        			TraduccionContenido  trad = (TraduccionContenido)((ArrayList)get("traducciones")).get(i);
        			if (trad.getTitulo().length()==0)
        				errors.add("titulo", new ActionError("error.conte.titulo"));
        		}
        	}
			
        } catch (Throwable t) {
            log.error("Error comprobando titulo del contenido", t);
        }    
        
        
    }
    	return errors;

    }
        

    public void setFcaducidad(Date fecha) {
    	if (fecha != null) {
    		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    		set("fcaducidad", df.format(fecha));
    	}
    }

    public Date getFcaducidad() {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String fecha=(String) get("fcaducidad");
        if (fecha.length()==10) fecha+=" 00:00";
        try {
            return df.parse(fecha);
        } catch (ParseException pe) {
            return null;
        }
    }

    public void setFpublicacion(Date fecha) {
    	if (fecha != null) {
    		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    		set("fpublicacion", df.format(fecha));
    	}
    }

    public Date getFpublicacion() {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String fecha=(String) get("fpublicacion");
        if (fecha.length()==10) fecha+=" 00:00";
        try {
            return df.parse(fecha);
        } catch (ParseException pe) {
            return null;
        }
    }    
    
}
