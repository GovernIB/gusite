package org.ibit.rol.sac.microfront.archivo.actions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.ibit.rol.sac.microfront.BaseAction;
import org.ibit.rol.sac.microfront.Microfront;
import org.ibit.rol.sac.microfront.archivo.util.Bdarchivopub;
import org.ibit.rol.sac.microfront.base.bean.ErrorMicrosite;
import org.ibit.rol.sac.micromodel.Archivo;
import org.ibit.rol.sac.micromodel.Microsite;
import org.ibit.rol.sac.micropersistence.delegate.ArchivoDelegate;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *  Action que consulta una agenda <P>
 *  Definici�n Struts:<BR>
 *  unknown="false" <BR>
 *  action path="/archivopub" <BR> 
 *  
 *  @author - Indra
 */
public class ArchivoPubAction extends BaseAction {

    protected static Log log = LogFactory.getLog(ArchivoPubAction.class);

    public final ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

    	/*
    	 * OJO.... es MUY IMPORTANTE COMPROBAR QUE ES CORRECTO EL ID DEL DOCUMENTO.
    	 * Hay que comprobar el id, que pertenezca al microsite o a un servicio del microsite
    	 * y que sea visible el elemento al que pertenece
    	 * formato: SSSSSxxxZIyyy
    	 * donde SSSSS es el tipo de servicio sacado de la clase org.ibit.rol.sac.microfront.Microfront.
    	 * donde xxx es el id del elemento al que pertenece el documento
    	 * donde ZI es el separador
    	 * donde yyy es el id del documento
    	 * 
    	 */
    	
    	try {
		 	
	    	Bdarchivopub bdarchivo = new Bdarchivopub(request);
	    	
	    	if (bdarchivo.checkcontrol()) {
		    	
		        Archivo archivo = obtenerArchivo(mapping, form, request);
		        if ((archivo != null) && (archivo.getDatos()!=null)) {
		            response.reset();
		            if (!forzarDownload(mapping, form, request)) {
		                response.setContentType(archivo.getMime());
		                response.setHeader("Content-Disposition", "inline; filename=\"" + archivo.getNombre() + "\"");
		            } else {
		                response.setContentType("application/octet-stream");
		                response.setHeader("Content-Disposition", "attachment; filename=\"" + archivo.getNombre() + "\"");
		            }
		            response.setContentLength(new Long(archivo.getPeso()).intValue());
			        response.getOutputStream().write(archivo.getDatos());
		        }else {
		        	throw new Exception();
		        }
	    	} else {
	    		throw new Exception();
		    } 
		
        } catch (Exception e) {  	
        	log.info(e.getMessage());
        	return mapping.findForward(getForwardError (request, new Microsite(), ErrorMicrosite.ERROR_AMBIT_DOCUMENT));
        }  
   	
        return null;
    }

    
    /**
     * M�todo p�blico que devuelve un archivo
     * @param mapping
     * @param form  ActionForm
     * @param request
     * @return Archivo un Archivo 
     * @throws Exception
     */
    public Archivo obtenerArchivo(ActionMapping mapping, ActionForm form, HttpServletRequest request) throws Exception {
    	
    	Archivo archivo = new Archivo();
    	ArchivoDelegate archi = DelegateUtil.getArchivoDelegate();
    	
    	String bynombre = "" +  request.getParameter(Microfront.PNAME);
    	String idsite = "" +  request.getParameter(Microfront.PIDSITE);
    	
    	if (!bynombre.equals("null")) {
    		if (!idsite.equals("null") && !idsite.equals("0")) {
    			archivo = archi.obtenerArchivobyName(new Long(idsite),bynombre);
    		} else {
    			//recogemos el site de sesion
    			if (request.getSession().getAttribute("MVS_idsite")!=null) 
    			{	
    				Long site = new Long((String)request.getSession().getAttribute("MVS_idsite"));
    				archivo = archi.obtenerArchivobyName(site,bynombre);
    			} else archivo = null;
    		}
    	} else {
    		Long id = new Long(request.getParameter("id"));
        	archivo = archi.obtenerArchivo(id);
    	}

    	return archivo;
    }
    
    /**
     * M�todo p�blico forzarDownload que devuelve un boolean
     * @param mapping
     * @param form
     * @param request
     * @return boolean Siempre devuelve false
     * @throws Exception
     */
    public boolean forzarDownload(ActionMapping mapping,
                                  ActionForm form,
                                  HttpServletRequest request) throws Exception {
        return false;
    }
	   
}
