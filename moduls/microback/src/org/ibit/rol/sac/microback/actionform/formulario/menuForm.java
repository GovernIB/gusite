package org.ibit.rol.sac.microback.actionform.formulario;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import org.ibit.rol.sac.microback.actionform.*;
import org.ibit.rol.sac.micromodel.Microsite;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;
import org.ibit.rol.sac.micropersistence.delegate.IdiomaDelegate;
import org.apache.struts.action.*;
import org.apache.struts.upload.FormFile;

/**
 *	Formulario para los menus
 *
 *@author Indra
 *
 */
public class menuForm extends TraDynaActionForm {

	private static final long serialVersionUID = -4538781477748263773L;
	protected static Log log = LogFactory.getLog(menuForm.class);

    /* (non-Javadoc)
     * @see org.apache.struts.action.DynaActionForm#initialize(org.apache.struts.action.ActionMapping)
     */
    public void initialize(ActionMapping actionMapping) {
        super.initialize(actionMapping);
    }

    /* (non-Javadoc)
     * @see org.ibit.rol.sac.microback.actionform.TraDynaActionForm#reset(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
     */
    public void reset(ActionMapping mapping, HttpServletRequest request){
        inicio(request);
    }

    /**
     * M�todo que inicializa los campos de men�
     * @param request	petici�n de usuario
     */
    private void inicio(HttpServletRequest request) {
        try {
        	
         if (request.getParameter("espera") == null) {

        	IdiomaDelegate delegate = DelegateUtil.getIdiomaDelegate();
            int numLangs = delegate.listarLenguajes().size();
            
            set("nombreCM", new String[numLangs]);
            
            String idmicro=""+((Microsite)request.getSession().getAttribute("MVS_microsite")).getId();
            int m=1000;
            if (request.getSession().getAttribute("cuenta_"+idmicro)!=null)
            	m=((Integer)request.getSession().getAttribute("cuenta_"+idmicro)).intValue();
            
            set("ids",new Long[m]);
            set("visibles",new String[m]);
            set("modos",new String[m]);
    	   	set("ordenes",new Integer[m]);
    	   	set("idPadres",new Long[m]);
        	set("tipos",new String[m]);
           	set("traducciones", new String[m*numLangs]);  // los de tipo contenido los dejaremos en blanco
           	set("imagenes",new FormFile[m]);
           	set("imagenesid",new Long[m]);		
           	set("imagenesnom",new String[m]);
           	set("imagenesbor", new boolean[m]);

        }
            
        } catch (Throwable t) {
            log.error("Error inicio menuForm", t);
        }
    	
    }
    
    
    /* (non-Javadoc)
     * @see org.apache.struts.action.ActionForm#validate(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
     */
    public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest request) {

    	ActionErrors errors = new ActionErrors();
    	String accion = ""+request.getParameter("accion");
    	
    	if(accion.equals(propertiesMessages.getString("operacion.guardar"))) {
    		
    		FormFile[] iconos=(FormFile[])get("imagenes");
    		
    		for (int i=0;i<iconos.length;i++) {
        		if (iconos[i]!=null) {
        			if (iconos[i].getFileSize()>1024*2)  // 2 Kb
        				errors.add("imagenMenu", new ActionError("error.menu.imagenmenu"));
        		}
    		}
    		
    		return errors;
    		
    	}
    	/************************************************************/
    	/*************** CREAMOS UN MENU NUEVO **********************/
    	/************************************************************/
    	else if(accion.equals(propertiesMessages.getString("menu.accion.crear"))) {

    		if (get("imagenCM")!=null)
    			if (((FormFile)get("imagenCM")).getFileSize()>1024*2)  // 2 Kb
        		errors.add("imagenMenu", new ActionError("error.menu.imagenmenu"));

    		String[] noms=(String[])get("nombreCM");
    		if (noms[0].length()==0)
    			errors.add("nombreCM", new ActionError("error.menu.nombreCM"));
    		
    	}
    	
    	return errors;

    	}
    
    
    /**
     * M�todo que resetea el formulario de creaci�n de menus
     * @throws Exception
     */
    public void resetCreateForm() throws Exception {

        int numLangs = DelegateUtil.getIdiomaDelegate().listarLenguajes().size();
    	String[] nombreCM = new String[numLangs];
    	
    	for (int i = 0; i < numLangs; i++) nombreCM[i] = "";
    	
    	this.set("nombreCM", nombreCM);
    	
    }
    

    
}