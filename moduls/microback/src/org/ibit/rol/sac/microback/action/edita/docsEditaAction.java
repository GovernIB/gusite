package org.ibit.rol.sac.microback.action.edita;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.ibit.rol.sac.microback.action.BaseAction;
import org.ibit.rol.sac.microback.actionform.formulario.contenidoForm;
import org.ibit.rol.sac.micromodel.Archivo;
import org.ibit.rol.sac.micromodel.Microsite;
import org.ibit.rol.sac.micropersistence.delegate.ArchivoDelegate;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;

/**
 * Action que edita guarda o elimina documentos de un microsite <BR>
 * <P>
 * 	Definición Struts:<BR>
 *  action path="/docsEdita" <BR> 
 *  name="docsForm" <BR> 
 *	scope="request"
 *  
 *  @author Indra
 */
public class docsEditaAction extends BaseAction 
{
    /**
     * This is the main action called from the Struts framework.
     * @param mapping The ActionMapping used to select this instance.
     * @param form The optional ActionForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     * @return 
     */
	
	protected static Log log = LogFactory.getLog(docsEditaAction.class);
	
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		ArchivoDelegate bdArchi = DelegateUtil.getArchivoDelegate(); 
    	DynaActionForm f = (DynaActionForm) form;
    	
        ActionForward af=null;    	
    	Long pag=(Long)f.get("idPagina");
    	String operacion=""+f.get("operacion");

    	
   		if (operacion.equals("modificar")) {
   			
   			Long id=(Long)f.get("id");

   			Archivo archi=bdArchi.obtenerArchivo(id);
   			
   			if (f.get("archi")!=null) {
            	FormFile ff1 = (FormFile) f.get("archi");
                if (archivoValido(ff1)) 
                	bdArchi.insertarArchivo(populateArchivo(archi, ff1, ((Microsite)request.getSession().getAttribute("MVS_microsite")).getId(), pag )  );
            }
    			
   		}

   		if (operacion.equals("crear")) {
    			
   			if (f.get("archi")!=null) {
            	FormFile ff1 = (FormFile) f.get("archi");
                if (archivoValido(ff1)) 
                	bdArchi.insertarArchivo(populateArchivo(null, ff1, ((Microsite)request.getSession().getAttribute("MVS_microsite")).getId(), pag )  );
            }
   		}
    		
   		if (operacion.equals("borrar")) {

   			Long id =null;
            String[] selecs=(String[])f.get("seleccionados");
            
            for (int i=0;i<selecs.length;i++) {
                id = new Long(selecs[i]);
                bdArchi.borrarArchivo(id);
            }
    			
   		}
        
        // Llamada desde detalle Contenido
        if (pag!=null) {
        	contenidoForm contef= (contenidoForm)request.getSession().getAttribute("contenidoForm");
    		af = new ActionForward("/contenidoEdita.do?id=" + contef.get("id"));
        }
        // Llamada desde recursos
      	else {
      		af = new ActionForward("/recursosEdita.do"); 	
      	}
        return af;

              
    }
        
}

