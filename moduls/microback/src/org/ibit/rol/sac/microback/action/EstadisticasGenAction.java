package org.ibit.rol.sac.microback.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.ibit.rol.sac.microback.action.util.Bdestadisticasgen;

/**
 * Action que visualiza estadisticas<P>
 * 
 * 	Definición Struts:<BR>
 *  action path="/estadisticagen"<BR> 
 *  name="BuscaOrdenaEstadisticaGenActionForm"<BR>
 *  unknown="false" <BR>
 *  forward name="listarestadistica" path="/listaEstadisticasGen.jsp"
 *  
 *  @author - Indra
 */
public class EstadisticasGenAction extends Action  {

	
	protected static Log log = LogFactory.getLog(EstadisticasGenAction.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
		String elforward="listarestadistica";
    	
		try {
			Bdestadisticasgen bdestadisticasgen = new Bdestadisticasgen(request, form);
			//se mete en sesion para que se pueda montar posteriormete el gráfico
			request.getSession().setAttribute("MVA_listaestadistica",bdestadisticasgen.getListaresultante());
			request.setAttribute("MVA_listaanyos",bdestadisticasgen.getListaanyos());
			request.setAttribute("MVA_statmicrosite",bdestadisticasgen.getStatmicrosite());
			
		} catch (Exception e) {
			elforward="error";
		}
        return mapping.findForward(elforward);
    }
	
}
