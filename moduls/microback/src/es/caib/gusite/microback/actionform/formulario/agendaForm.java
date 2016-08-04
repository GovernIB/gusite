package es.caib.gusite.microback.actionform.formulario;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import org.apache.struts.upload.FormFile;
import org.apache.struts.action.*;

import es.caib.gusite.microback.actionform.*;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.TraduccionAgenda;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.IdiomaDelegate;

/**
 *	Formulario para las agendas (eventos)
 *
 *@author Indra
 *
 */
public class agendaForm extends TraDynaActionForm {

	private static final long serialVersionUID = 744736400436915121L;
    protected static Log log = LogFactory.getLog(agendaForm.class);

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
    	if (request.getParameter("espera") == null) {
	        super.reset(mapping, request);
	        inicio();
    	}
    }

    /**
     * Método que inicializa los campos del formulario
     * 
     */
    private void inicio() {
        FormFile[] ficheros;
        Long[] ficherosid;
        String[] ficherosnom;
        boolean[] ficherosbor;
        FormFile[] imagenes;
        Long[] imagenesid;
        String[] imagenesnom;
        boolean[] imagenesbor;

        
        try {
            
	            IdiomaDelegate delegate = DelegateUtil.getIdiomaDelegate();
	            int numLangs = delegate.listarLenguajes().size();
	            
	            ficheros = new FormFile[numLangs];
	            ficherosid = new Long[numLangs];
	            ficherosnom = new String[numLangs];
	            ficherosbor = new boolean[numLangs];
	            imagenes = new FormFile[numLangs];
	            imagenesid = new Long[numLangs];
	            imagenesnom = new String[numLangs];
	            imagenesbor = new boolean[numLangs];

	            for (int i = 0; i < numLangs; i++) {
	                ficheros[i] = null;
	                ficherosid[i] = null;
	                ficherosnom[i] = "";
	                ficherosbor[i] = false;
	                imagenes[i] = null;
	                imagenesid[i] = null;
	                imagenesnom[i] = "";
	                imagenesbor[i] = false;
	            }
	            set("ficheros", ficheros);
	            set("ficherosid", ficherosid);
	            set("ficherosnom", ficherosnom);
	            set("ficherosbor", ficherosbor);
	            set("imagenes", imagenes);
	            set("imagenesid", imagenesid);
	            set("imagenesnom", imagenesnom);
	            set("imagenesbor", imagenesbor);

         

        } catch (Throwable t) {
            log.error("Error creando ficheros", t);
        }

    }
   
    /* (non-Javadoc)
     * @see org.apache.struts.action.ActionForm#validate(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
     */
    public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest httpServletRequest) {

    	ActionErrors errors = new ActionErrors();
    	
    	String accion = ""+httpServletRequest.getParameter("accion");
    	
    	if(accion.equals(propertiesMessages.getString("operacion.guardar"))) {

	        if (!FechaValida(""+get("finicio"))) 
	            errors.add("finicio", new ActionError("error.agenda.inicio"));
	        
	        if ((""+get("idActividad")).equals("null"))
	        	errors.add("actividad", new ActionError("error.agenda.actividad"));
	
	        try {
	        	IdiomaDelegate idiomaDelegate = DelegateUtil.getIdiomaDelegate();
	        	List lang = idiomaDelegate.listarLenguajes();
	        	FormFile[] ficheros = (FormFile[])get("ficheros");
	        	FormFile[] imagenes = (FormFile[])get("imagenes");
	        	
	        	boolean doc_size=false;
	        	boolean img_size=false;
	        	boolean descr_size=false;
	        	for (int i=0;i<lang.size();i++) {
	        		TraduccionAgenda  trad = (TraduccionAgenda)((ArrayList)get("traducciones")).get(i);
	        		if (imagenes[i]!=null)
	        			if (imagenes[i].getFileSize()>1024*1024*10) 
	        				img_size=true;
	        		if (ficheros[i]!=null)
	        			if (ficheros[i].getFileSize()>1024*1024*10) 
	        				doc_size=true;
	        		if (trad.getDescripcion().length()>3500)	
	        			descr_size=true;
	        		
	        		if (lang.get(i).equals(Idioma.getIdiomaPorDefecto())) {
	        			if (trad.getTitulo().length()==0)
	        				errors.add("titulo", new ActionError("error.agenda.titulo"));
	        		}	
	        	}
	        	
				if (doc_size) errors.add("documento", new ActionError("error.agenda.documentoidioma"));
				if (img_size) errors.add("imagen", new ActionError("error.agenda.imagenidioma"));
				if (descr_size) errors.add("descripcion", new ActionError("error.agenda.descripcionidioma"));
	        } catch (Throwable t) {
	            log.error("Error comprobando validación del evento", t);
	        }
        
    	}else{
    		httpServletRequest.getSession().removeAttribute("imagenesTraducidas");
    		httpServletRequest.getSession().removeAttribute("ficherosTraducidos");
    	}
    	return errors;

    }
        
    public void setFinicio(Date fecha) {
    	if (fecha != null) {
    		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    		set("finicio", df.format(fecha));
    	}
    }

    public Date getFinicio() {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String fecha=(String) get("finicio");
        if (fecha.length()==10) fecha+=" 00:00";
        try {
            return df.parse(fecha);
        } catch (ParseException pe) {
            return null;
        }
    }

    public void setFfin(Date fecha) {
    	if (fecha != null) {
    		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    		set("ffin", df.format(fecha));
    	}
    }

    public Date getFfin() {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String fecha=(String) get("ffin");
        if (fecha.length()==10) fecha+=" 00:00";
        try {
            return df.parse(fecha);
        } catch (ParseException pe) {
            return null;
        }
    }    
    
}
