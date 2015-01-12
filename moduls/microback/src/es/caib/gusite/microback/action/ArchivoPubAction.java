package es.caib.gusite.microback.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.gusite.microback.action.util.Bdarchivopub;
import es.caib.gusite.micromodel.Archivo;
import es.caib.gusite.micropersistence.delegate.ArchivoDelegate;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.util.ArchivoUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Action que descarga un archivo<P>
 * 
 * 	Definición Struts:<BR>
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
    	 * donde SSSSS es el tipo de servicio sacado de la clase es.caib.gusite.microfront.Microfront.
    	 * donde xxx es el id del elemento al que pertenece el documento
    	 * donde ZI es el separador
    	 * donde yyy es el id del documento
    	 * 
    	 */
		Bdarchivopub bdarchivo = new Bdarchivopub(request);
		if (bdarchivo.checkcontrol()) {

			Archivo archivo = obtenerArchivo(mapping, form, request);

			// amartin: Si los datos del archivo son nulos en la BD, vamos a buscarlo a Filesystem.
			if (ArchivoUtil.almacenarEnFilesystem()) {

				if ((archivo != null) && (archivo.getDatos() == null)) {

					byte[] datos = ArchivoUtil.obtenerDatosArchivoEnFilesystem(archivo);
					archivo.setDatos(datos);

				}

			}

			if ((archivo != null) && (archivo.getDatos() != null)) {
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
