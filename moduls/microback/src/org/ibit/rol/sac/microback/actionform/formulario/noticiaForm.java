package org.ibit.rol.sac.microback.actionform.formulario;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.List;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import org.ibit.rol.sac.microback.actionform.*;
import org.ibit.rol.sac.micromodel.Idioma;
import org.ibit.rol.sac.micromodel.TraduccionNoticia;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;
import org.ibit.rol.sac.micropersistence.delegate.IdiomaDelegate;
import org.apache.struts.upload.FormFile;
import org.apache.struts.action.*;

/**
 *	Formulario para las noticias
 *
 *@author Indra
 *
 */
public class noticiaForm extends TraDynaActionForm {

	private static final long serialVersionUID = -5673184255653688291L;
    protected static Log log = LogFactory.getLog(noticiaForm.class);

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
	        
	        // Valores para altas
	    	if(request.getSession().getAttribute("MVS_idtipo")!=null) {
	    		set("idTipo", new Long(""+request.getSession().getAttribute("MVS_idtipo")));
	    	}
			set("fpublicacion", new SimpleDateFormat("dd/MM/yyyy 00:00").format(new java.util.Date()));
	        
	        inicio();
    	}
    }

    /**
     * M�todo que inicializa los campos del formulario
     * 
     */
    private void inicio() {
        FormFile[] ficheros;
        Long[] ficherosid;
        String[] ficherosnom;
        boolean[] ficherosbor;
        try {
            IdiomaDelegate delegate = DelegateUtil.getIdiomaDelegate();
            int numLangs = delegate.listarLenguajes().size();
            ficheros = new FormFile[numLangs];
            ficherosid = new Long[numLangs];
            ficherosnom = new String[numLangs];
            ficherosbor = new boolean[numLangs];
            for (int i = 0; i < numLangs; i++) {
                ficheros[i] = null;
                ficherosid[i] = null;
                ficherosnom[i] = "";
                ficherosbor[i] = false;
            }
            set("ficheros", ficheros);
            set("ficherosid", ficherosid);
            set("ficherosnom", ficherosnom);
            set("ficherosbor", ficherosbor);
            
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

    	String fechapublicacion = "" + get("fpublicacion");
    	if (!fechapublicacion.equals("null") && fechapublicacion.length()>0)
	        if (!FechaValida(""+get("fpublicacion"))) 
	            errors.add("fpublicacion", new ActionError("error.noticias.fpublicacion"));
        
		if (get("imagen")!=null) {
        	if (((FormFile)get("imagen")).getFileSize()>1024*1024*10)
        		errors.add("imagen", new ActionError("error.noticias.imagen"));
		}
        if ((""+get("idTipo")).equals("0") || (""+get("idTipo")).equals("null"))
        	errors.add("tipo", new ActionError("error.noticias.tipo"));
        
        if (!esEntero(""+get("orden")) )
        	errors.add("orden", new ActionError("error.noticias.orden"));
            
        
        try {
        	IdiomaDelegate idiomaDelegate = DelegateUtil.getIdiomaDelegate();
        	List<?> lang = idiomaDelegate.listarLenguajes();
        	FormFile[] ficheros = (FormFile[])get("ficheros");
        	
        	boolean doc_size=false;
        	boolean texto_size=false;
        	for (int i=0;i<lang.size();i++) {
        		TraduccionNoticia  trad = (TraduccionNoticia)((ArrayList<?>)get("traducciones")).get(i);
        		if (ficheros[i]!=null)
        			if (ficheros[i].getFileSize()>1024*1024*10)	doc_size=true;
        		if (trad.getTexto().length()> 40000000)texto_size=true;
        		
    			if (lang.get(i).equals(Idioma.DEFAULT)) {
        			if (trad.getTitulo().length()==0)
        				errors.add("titulo", new ActionError("error.noticias.titulo"));
        		}
        		
        	}
			if (doc_size) errors.add("documento", new ActionError("error.noticias.documentoidioma"));
			if (texto_size) errors.add("texto", new ActionError("error.noticias.textoidioma"));
			
		
        } catch (Throwable t) {
            log.error("Error comprobando la validaci�n del elemento", t);
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
