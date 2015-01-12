package org.ibit.rol.sac.microback.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.upload.FormFile;
import org.apache.struts.Globals;
import org.ibit.rol.sac.micromodel.Archivo;

import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Action Base con métodos de utilidad de la que heredan las demás Acciones 
 */
public abstract class BaseAction extends Action {
	
	protected static String [] dateAndHour = {DateFormat.getDateInstance(DateFormat.LONG, new Locale("ca","ES")).format(new Date()).replaceAll("/", "de"),
							   				  DateFormat.getTimeInstance(DateFormat.LONG, new Locale("ca","ES")).format(new Date()).replaceAll("/", "de")};
	
    
	/**
	 * 
	 * Método que genera un Archivo a partir de un Formfile
	 * 
	 * @param archivo
	 * @param formFile
	 * @param microsite
	 * @param pagina
	 * @return
	 * @throws IOException
	 */
	protected static Archivo populateArchivo(Archivo archivo, FormFile formFile, Long microsite, Long pagina ) throws IOException {

        if (archivo == null) archivo = new Archivo();
        archivo.setMime(formFile.getContentType());
        archivo.setNombre(formFile.getFileName());
        archivo.setPeso(formFile.getFileSize());
        archivo.setDatos(formFile.getFileData());
        if (pagina!=null) archivo.setPagina(pagina);
        if (microsite!=null) archivo.setIdmicrosite(microsite.longValue());
        return archivo;
    }
    
    /**
     * Método que genera un FormFile a partir de un Archivo
     * 
     * @param archivo
     * @param formFile
     * @return
     * @throws IOException
     */
    protected static FormFile describeArchivo(Archivo archivo, FormFile formFile) throws IOException {
        
    	formFile.setContentType(archivo.getMime());
    	formFile.setFileName(archivo.getNombre());  	
        return formFile;
    }

    /**
     * Método que crear un nuevo archivo en una página de Microsite
     * @param archivo
     * @param microsite
     * @param pagina
     * @return
     * @throws IOException
     */
    protected static Archivo crearNuevoArchivo(Archivo archivo, Long microsite, Long pagina) throws IOException {
    	
    	Archivo archivoResult = new Archivo();
    	if (archivo != null){
    		archivoResult.setMime(archivo.getMime());
    		archivoResult.setNombre(archivo.getNombre());
    		archivoResult.setPeso(archivo.getPeso());
    		archivoResult.setDatos(archivo.getDatos());
    	    if (pagina!=null) archivoResult.setPagina(pagina);
    	    if (microsite!=null) archivoResult.setIdmicrosite(microsite.longValue());
    		
    		return archivoResult;
    	}else{
    		return null;
    	}
    	
    	
    }   

    
    protected boolean archivoValido(FormFile formFile) {
        return (formFile != null && formFile.getFileSize() > 0);
    }

    protected void addMessage(HttpServletRequest request, String message) {
    	request.setAttribute("mensajes", "si");
    	addMessage(request, new ActionMessage(message));
    }
    
    protected void addMessageAlert(HttpServletRequest request, String message) {
    	request.setAttribute("alertas", "si");
    	addMessage(request, new ActionMessage(message));
    }
    
    protected void addMessageError(HttpServletRequest request, String message) {
    	request.setAttribute("errores", "si");
    	addMessage(request, new ActionMessage(message));
    }

    protected void addMessage(HttpServletRequest request, String message, Object o1) {
        addMessage(request, new ActionMessage(message, o1));
    }

    protected void addMessage(HttpServletRequest request, String message, Object o1, Object o2) {
        addMessage(request, new ActionMessage(message, o1, o2));
    }

    protected void addMessage(HttpServletRequest request, String message, Object o1, Object o2, Object o3) {
        addMessage(request, new ActionMessage(message, o1, o2, o3));
    }

    protected void addMessage(HttpServletRequest request, String message, Object o1, Object o2, Object o3, Object o4) {
        addMessage(request, new ActionMessage(message, o1, o2, o3, o4));
    }

    protected void addMessage(HttpServletRequest request, String message, Object[] o) {
        addMessage(request, new ActionMessage(message, o));
    }
    
    protected void addMessageWithDate(HttpServletRequest request, String message) {
    	request.setAttribute("mensajes", "si");
    	addMessage(request, new ActionMessage(message, dateAndHour));
    }
    protected void addMessageWithId(HttpServletRequest request, String message, String id) {
    	request.setAttribute("mensajes", "si");
    	addMessage(request, new ActionMessage(message, id));
    }
    
    protected void clearMessages(HttpServletRequest request) {
        saveMessages(request, new ActionMessages());
    }

    private void addMessage(HttpServletRequest request, ActionMessage message) {
        ActionMessages messages = (ActionMessages) request.getAttribute(Globals.MESSAGE_KEY);
        if (messages == null) {
            messages = new ActionMessages();
        }
        messages.add(ActionMessages.GLOBAL_MESSAGE, message);
        saveMessages(request, messages);
    }
    
    protected void addAlert(HttpServletRequest request, String Message) {
    	request.setAttribute("alert", getResources(request).getMessage(Message));
    }
    
}
