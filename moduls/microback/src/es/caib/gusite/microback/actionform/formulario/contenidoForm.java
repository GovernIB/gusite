package es.caib.gusite.microback.actionform.formulario;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import es.caib.gusite.microback.actionform.TraDynaActionForm;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.TraduccionContenido;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.IdiomaDelegate;

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
     * @see es.caib.gusite.microback.actionform.TraDynaActionForm#reset(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
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
     * @see es.caib.gusite.microback.actionform.TraDynaActionForm#resetForm(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
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
        		TraduccionContenido  trad = (TraduccionContenido)((ArrayList)get("traducciones")).get(i);
        		if (lang.get(i).equals(Idioma.getIdiomaPorDefecto())) {        			
        			if (trad.getTitulo().length()==0) // || trad.getUri().length() == 0)
        				errors.add("titulo", new ActionError("error.conte.titulo"));        		
        		}else{ 
        			if (trad.getTitulo().length()==0 && trad.getUri().length() > 0) {
        				errors.add("titulo", new ActionError("error.conte.titulo2", idiomaDelegate.obtenerIdioma("" + lang.get(i)).getNombre() ));
            		} else if ((trad.getTexto().length() > 0 && trad.getTitulo().length() == 0)) {
        				//el título y  la uri son obligatorios si se especifica el campo contenido.
	    				errors.add("titulo", new ActionError("error.conte.tituloContenido", idiomaDelegate.obtenerIdioma("" + lang.get(i)).getNombre()));
        			}
        			/**
        			// si se especifica titulo la uri es obligatoria y viceversa.
        			if ((trad.getTitulo().length()==0 && trad.getUri().length() > 0) ||
        				(trad.getTitulo().length() > 0 && trad.getUri().length() == 0)){
        				errors.add("titulo", new ActionError("error.conte.titulo2", idiomaDelegate.obtenerIdioma("" + lang.get(i)).getNombre() ));
        			}else if ((trad.getTexto().length() > 0 && (trad.getTitulo().length() == 0 || trad.getUri().length() == 0))) {
        				//el título y  la uri son obligatorios si se especifica el campo contenido.
	    				errors.add("titulo", new ActionError("error.conte.tituloContenido", idiomaDelegate.obtenerIdioma("" + lang.get(i)).getNombre()));
        			}**/
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
