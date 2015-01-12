package es.caib.gusite.microback.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.gusite.microback.action.util.Bdestadisticasdatos;

/**
 * Action que visualiza estadisticas<P>
 * 
 * 	Definición Struts:<BR>
 *  action path="/encdatos"<BR> 
 *  unknown="false" <BR>
 *  forward name="listardatos" path="/listaEstadisticasEncDatos.jsp"
 *  
 *  @author - Indra
 */
public class EstadisticasEncuestasDatosAction extends Action  {
	
	
	protected static Log log = LogFactory.getLog(EstadisticasEncuestasDatosAction.class);

	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
{
	String elforward="listardatos";
	
	try {

		Bdestadisticasdatos bdestadisticasdatos = new Bdestadisticasdatos(request);
		request.setAttribute("MVS_lista_encuesta_datos",bdestadisticasdatos.getListadatos());
		request.setAttribute("MVS_encuesta",bdestadisticasdatos.getEncuesta());
		request.setAttribute("MVS_pregunta",bdestadisticasdatos.getPregunta());
		request.setAttribute("MVS_respuesta",bdestadisticasdatos.getRespuesta());
		
	} catch (Exception e) {
		elforward="error";
	}
    return mapping.findForward(elforward);
}	
	
}
