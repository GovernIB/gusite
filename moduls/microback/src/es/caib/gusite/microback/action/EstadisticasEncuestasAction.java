package es.caib.gusite.microback.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.gusite.microback.action.util.Bdestadisticasenc;


/**
 * Action que visualiza las estadísticas de encuestas<P>
 *
 *  @author - Indra
 */

/**
 * Clase que maneja el action de visualizar las estadisticas de las encuestas
 * @author vroca
 *
 */
public class EstadisticasEncuestasAction extends Action  {

	
	protected static Log log = LogFactory.getLog(EstadisticasEncuestasAction.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
		String elforward="listarglobal";
    	
		try {
			Bdestadisticasenc bdestadisticasgen = new Bdestadisticasenc(request);
			
			if (bdestadisticasgen.isIndividual()) {
				request.setAttribute("MVS_encuesta",bdestadisticasgen.getEncuesta());
				elforward="listarindividual";
			} else {
				request.setAttribute("MVS_lista_encuesta",bdestadisticasgen.getListaencuestas());
				elforward="listarglobal";
			}
			
		} catch (Exception e) {
			elforward="error";
		}
        return mapping.findForward(elforward);
    }
	
}
