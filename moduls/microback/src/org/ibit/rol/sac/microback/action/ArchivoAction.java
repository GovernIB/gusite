package org.ibit.rol.sac.microback.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.ibit.rol.sac.micromodel.Archivo;
import org.ibit.rol.sac.micromodel.Microsite;
import org.ibit.rol.sac.micropersistence.delegate.ArchivoDelegate;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;

/**
 * Action Base para enviar un archivo<P>
 * 
 * 	Definici�n Struts:<BR>
 *  action path="/archivo"<BR> 
 *  
 *  @author - Indra
 */
public class ArchivoAction extends BaseAction {

    protected static Log log = LogFactory.getLog(ArchivoAction.class);

    public final ActionForward execute(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request, HttpServletResponse response)
            throws Exception {

       
        Long id = new Long(request.getParameter("id"));
    	ArchivoDelegate archi = DelegateUtil.getArchivoDelegate();

        if (archi.checkSite(((Microsite)request.getSession().getAttribute("MVS_microsite")).getId(),id)) {
        	addMessage(request, "info.seguridad");
        	return mapping.findForward("info");
        }
        Archivo archivo = archi.obtenerArchivo(id);
        
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
        }

        return null;
    }

    public boolean forzarDownload(ActionMapping mapping,
                                  ActionForm form,
                                  HttpServletRequest request) throws Exception {
        return false;
    }
}
