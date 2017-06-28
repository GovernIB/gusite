package es.caib.gusite.microfront.archivo.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.gusite.microfront.BaseAction;
import es.caib.gusite.microfront.Microfront;
import es.caib.gusite.microfront.archivo.util.Bdarchivopub;
import es.caib.gusite.microfront.base.bean.ErrorMicrosite;
import es.caib.gusite.micromodel.Archivo;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micropersistence.delegate.ArchivoDelegate;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.util.ArchivoUtil;

/**
 *  Action que consulta una agenda <P>
 *  Definición Struts:<BR>
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
    	 * donde SSSSS es el tipo de servicio sacado de la clase es.caib.gusite.microfront.Microfront.
    	 * donde xxx es el id del elemento al que pertenece el documento
    	 * donde ZI es el separador
    	 * donde yyy es el id del documento
    	 * 
    	 */
    	
    	try {
		 	
	    	Bdarchivopub bdarchivo = new Bdarchivopub(request);
	    	
			if (bdarchivo.checkcontrol()) {

				Archivo archivo = obtenerArchivo(mapping, form, request);

				if (archivo != null) {
					response.reset();
					if (!forzarDownload(mapping, form, request)) {
						response.setContentType(archivo.getMime());
						response.setHeader("Content-Disposition", "inline; filename=\"" + archivo.getNombre() + "\"");
					} else {
						response.setContentType("application/octet-stream");
						response.setHeader("Content-Disposition", "attachment; filename=\"" + archivo.getNombre() + "\"");
					}
					response.setContentLength(new Long(archivo.getPeso()).intValue());
					ArchivoDelegate archivoDelegate = DelegateUtil.getArchivoDelegate();
					byte[] datos = archivoDelegate.obtenerContenidoFichero(archivo);
					response.getOutputStream().write(datos);
				} else {
					throw new Exception();
				}
			} else {
				throw new Exception();
			}
		
        } catch (Exception e) {  	
        	log.info(e.getMessage());
        	return mapping.findForward(getForwardError (request, new Microsite(), ErrorMicrosite.ERROR_AMBIT_DOCUMENT, response));
        }  
   	
        return null;
    }

    
    /**
     * Método público que devuelve un archivo
     * @param mapping
     * @param form  ActionForm
     * @param request
     * @return Archivo un Archivo 
     * @throws Exception
     */
    public Archivo obtenerArchivo(ActionMapping mapping, ActionForm form, HttpServletRequest request) throws Exception {
    	
		Archivo archivo = new Archivo();
		ArchivoDelegate archi = DelegateUtil.getArchivoDelegate();

		String bynombre = "" + request.getParameter(Microfront.PNAME);
		String idsite = "" + request.getParameter(Microfront.PIDSITE);

		if (!bynombre.equals("null")) {
			if (!idsite.equals("null") && !idsite.equals("0")) {
				archivo = archi.obtenerArchivobyName(new Long(idsite), bynombre);
			} else {
				// recogemos el site de sesion
				if (request.getSession().getAttribute("MVS_idsite") != null) {
					Long site = new Long((String)request.getSession().getAttribute("MVS_idsite"));
					archivo = archi.obtenerArchivobyName(site, bynombre);
				} else
					archivo = null;
			}
		} else {
			Long id = new Long(request.getParameter("id"));
			archivo = archi.obtenerArchivo(id);
		}

		return archivo;
		
    }
    
    /**
     * Método público forzarDownload que devuelve un boolean
     * @param mapping
     * @param form
     * @param request
     * @return boolean Siempre devuelve false
     * @throws Exception
     */
	public boolean forzarDownload(ActionMapping mapping, ActionForm form, HttpServletRequest request) 
			throws Exception {
		// FIXME amartin: ¿qué proceso de decisión habría que realizar aquí? Hay un return false directamente.
		return false;
	}
	   
}
