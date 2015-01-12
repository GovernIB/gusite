package es.caib.gusite.microback.action;

import java.util.Date;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.gusite.micromodel.Archivo;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.Usuario;
import es.caib.gusite.micropersistence.delegate.ArchivoDelegate;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.UsuarioDelegate;
import es.caib.gusite.micropersistence.util.ArchivoUtil;

/**
 * Action Base para enviar un archivo<P>
 * 
 * 	Definición Struts:<BR>
 *  action path="/archivo"<BR> 
 *  
 *  @author - Indra
 */
public class ArchivoAction extends BaseAction {

    protected static Log log = LogFactory.getLog(ArchivoAction.class);
    private static ResourceBundle rb =	ResourceBundle.getBundle("sac-microback-messages");
    
    public final ActionForward doExecute(ActionMapping mapping, ActionForm form, 
    		HttpServletRequest request, HttpServletResponse response) throws Exception {
    	    	
    	Microsite microsite = (Microsite)request.getSession().getAttribute("MVS_microsite");
    	
    	if ("exportarArchivosMicrosites".equals(request.getParameter("accion"))) {
    		
    		// Reseteamos valores de log.
    		request.getSession().removeAttribute("mensaje");
    		// Reaprovechamos el código de la clase MicroLog, aunque el nombre no sea el más adecuado siendo esto una exportación...
    		request.getSession().removeAttribute("MVS_importprocessor");
    		request.getSession().removeAttribute("MVS_fechaexportprocessor");
    		
    		// recoger usuario.....
    		if (request.getSession().getAttribute("MVS_usuario") == null) {
    			
    			UsuarioDelegate usudel = DelegateUtil.getUsuarioDelegate();
    			Usuario usu = usudel.obtenerUsuariobyUsername(request.getRemoteUser());
    			request.getSession().setAttribute("MVS_usuario", usu);
    			    			
    		}
    		
    		// Obtenemos la propiedad de sistema donde se exportarán los BLOB para mostrarla al usuario.
    		request.setAttribute("rutaArchivosEnFileSystem", System.getProperty("es.caib.gusite.archivos.rutaArchivosEnFileSystem"));
    		    		
    		return mapping.findForward("exportarArchivosMicrosites");
    		
    	} else if ("exportarArchivosDeTodosLosMicrosites".equals(request.getParameter("accion"))) {
    		        	
    		try {

    			// Si nos lo solicitan vía properties, almacenamos los archivos en Filesystem.
    			if (ArchivoUtil.almacenarEnFilesystem())
    				ArchivoUtil.exportarArchivosDeTodosLosMicrosites(request);
    			
    			String mensaje = "<strong>" + rb.getString("micro.exportar.todos") + "</strong>";
    			request.getSession().setAttribute("mensaje", mensaje);
    			
    			Date fecha = new Date();
    			request.getSession().setAttribute("MVS_fechaexportprocessor", fecha);
    			
    			return mapping.findForward("exportarArchivosMicrosites");
    			
    		} catch (DelegateException e) {
    			
    			log.error(e);
    			
    		}
    		    	
    	} else if (request.getParameter("id") != null) {
    	
			Long id = new Long(request.getParameter("id"));
			ArchivoDelegate archi = DelegateUtil.getArchivoDelegate();
			
			if (archi.checkSite(microsite.getId(), id)) {
				addMessage(request, "info.seguridad");
				return mapping.findForward("info");
			}
			
			Archivo archivo = archi.obtenerArchivo(id);
			
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

    public boolean forzarDownload(ActionMapping mapping, ActionForm form, HttpServletRequest request) 
    		throws Exception {
    	
    	// FIXME amartin: ¿qué proceso de decisión habría que realizar aquí? Hay un return false directamente.
    	return false;
    	
    }
            
}
