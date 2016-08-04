package es.caib.gusite.microback.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.gusite.microback.action.util.BdestadisticasCerca;

/**
 * Action que busca resultados de encuesta en función de una respuesta<P>
 * 
 * 	Definición Struts:<BR>
 *  action path="/enccerca"<BR> 
 *  unknown="false" <BR>
 *  forward name="mostraresultcerca" path="/listaEstadisitcasEncCerca.jsp"
 *  
 *  @author - Indra
 */
public class EstadisticaEncuestaCercaAction extends Action  {
	
	
	protected static Log log = LogFactory.getLog(EstadisticaEncuestaCercaAction.class);

	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
{
		String elforward="mostraresultcerca";
	
	try {
        
		BdestadisticasCerca bdestadisticascerca = new BdestadisticasCerca(request);

		request.setAttribute("MVS_encuesta",bdestadisticascerca.getEncuesta());
		request.setAttribute("MVS_Preguntas",bdestadisticascerca.getPreguntasOrdAsc());
		request.setAttribute("MVS_idPregSelected", bdestadisticascerca.getIdPregSelected());
		request.setAttribute("MVS_idRespSelected", bdestadisticascerca.getIdRespSelected());
		
		
	} catch (Exception e) {
		log.error("Error en la accion de busqueda de resultados de una encuesta");
		elforward="error";
	}
    return mapping.findForward(elforward);
}	
	
}
