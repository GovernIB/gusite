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
import org.ibit.rol.sac.micromodel.Idioma;
import org.ibit.rol.sac.micromodel.TraduccionBanner;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;
import org.ibit.rol.sac.micropersistence.delegate.IdiomaDelegate;
import org.apache.struts.upload.FormFile;
import org.apache.struts.action.*;

/**
 *	Formulario para los banners
 *
 *@author Indra
 *
 */
public class bannerForm extends TraDynaActionForm {

	private static final long serialVersionUID = -5727785544924307673L;

    protected static Log log = LogFactory.getLog(bannerForm.class);

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
        set("fpublicacion", new SimpleDateFormat("dd/MM/yyyy 00:00").format(new java.util.Date()));
        inicio();
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
            log.error("Error creando ficheros de imagen", t);
        }
    	
    }
   
    /* (non-Javadoc)
     * @see org.apache.struts.action.ActionForm#validate(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
     */
    public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest httpServletRequest) {

    	ActionErrors errors = new ActionErrors();
    	
    	if(httpServletRequest.getParameter("modifica")!=null || httpServletRequest.getParameter("anyade")!=null) {

        if (!FechaValida(""+get("fpublicacion"))) 
            errors.add("fpublicacion", new ActionError("error.banner.fpublicacion"));
        
        try {
        	IdiomaDelegate idiomaDelegate = DelegateUtil.getIdiomaDelegate();
        	List lang = idiomaDelegate.listarLenguajes();
        	FormFile[] ficheros = (FormFile[])get("ficheros");
        	
        	boolean img_size=false;
        	for (int i=0;i<lang.size();i++) {
        		if (ficheros[i]!=null)
        			if (ficheros[i].getFileSize()>1024*50) img_size=true;
        		
        		if (lang.get(i).equals(Idioma.DEFAULT)) {
        			TraduccionBanner  trad = (TraduccionBanner)((ArrayList)get("traducciones")).get(i);
        			if (trad.getTitulo().length()==0)
        				errors.add("titulo", new ActionError("error.banner.titulo"));
        		}
        		
        	}
			if (img_size) errors.add("imagen", new ActionError("error.banner.imagen"));
        } catch (Throwable t) {
            log.error("Error comprobando tamaño de imagen de banners", t);
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
