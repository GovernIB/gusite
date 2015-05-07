package org.ibit.rol.sac.microback.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.ibit.rol.sac.microback.action.util.Bdarchivopub;
import org.ibit.rol.sac.microback.utils.Cadenas;
import org.ibit.rol.sac.micromodel.Archivo;
import org.ibit.rol.sac.micropersistence.delegate.ArchivoDelegate;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Action que descarga un archivo<P>
 * 
 * 	Definici�n Struts:<BR>
 *  action path="/archivopub"<BR> 
 *  
 *  @author - Indra
 */
public class ArchivoPubAction extends Action {

    protected static Log log = LogFactory.getLog(ArchivoPubAction.class);

    public final ActionForward execute(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request, HttpServletResponse response)
            throws Exception {

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
    	Bdarchivopub bdarchivo = new Bdarchivopub(request);
    	if (bdarchivo.checkcontrol()) {
	    	
	        Archivo archivo = obtenerArchivo(mapping, form, request);
	        if ((archivo != null) && (archivo.getDatos()!=null)) {
	            response.reset();
	            String file_name = Cadenas.toAsciiString(archivo.getNombre());
	            file_name = Cadenas.replaceSpecialCharacters(file_name);
	            if (!forzarDownload(mapping, form, request)) {
	                response.setContentType(archivo.getMime());
	                response.setHeader("Content-Disposition", "inline; filename=\"" + file_name + "\"");
	            } else {
	                response.setContentType("application/octet-stream");
	                response.setHeader("Content-Disposition", "attachment; filename=\"" + file_name + "\"");
	            }
	            response.setContentLength(new Long(archivo.getPeso()).intValue());
	            response.getOutputStream().write(archivo.getDatos());
	        }
    	}
        return null;
    }

    public Archivo obtenerArchivo(ActionMapping mapping, ActionForm form,
            HttpServletRequest request) throws Exception {
    	
    	Long id = new Long(request.getParameter("id"));
    	ArchivoDelegate archi = DelegateUtil.getArchivoDelegate();

    	return archi.obtenerArchivo(id);
    }
    
    public boolean forzarDownload(ActionMapping mapping,
                                  ActionForm form,
                                  HttpServletRequest request) throws Exception {
        return false;
    }
	
    
}
