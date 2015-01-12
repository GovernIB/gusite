package es.caib.gusite.microback.action;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Action que guarda la fecha actual en un atributo de petición MVS_fechaimportprocessor para mostrarlo en el log de importación <P>
 * 
 * 	Definición Struts:<BR>
 *  action path="/detallelogexport"<BR> 
 *  unknown="false" <BR>
 *  forward name="detalle" path="/detallelogexport.jsp"
 *  
 *  @author - Indra
 */
public class ExportRequestProcessorAction extends BaseAction {

	protected static Log log = LogFactory.getLog(ExportRequestProcessorAction.class);

	public ActionForward doExecute(ActionMapping mapping, ActionForm form, 
    		HttpServletRequest request, HttpServletResponse response) throws Exception {

		Date fecha = new Date();
		request.setAttribute("MVS_fechaexportprocessor", fecha);
		
		String elforward = "detalle";
		
		return mapping.findForward(elforward);

	}

}
