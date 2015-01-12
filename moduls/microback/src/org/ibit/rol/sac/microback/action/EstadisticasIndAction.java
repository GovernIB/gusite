package org.ibit.rol.sac.microback.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.ibit.rol.sac.microback.action.util.Bdestadisticasind;

/**
 * Action que visualiza estadisticas<P>
 * 
 * 	Definición Struts:<BR>
 *  action path="/estadisticaind"<BR> 
 *  name="BuscaOrdenaEstadisticaIndActionForm"
 *  unknown="false" <BR>
 *  forward name="listarestadistica" path="/listaEstadisticasInd.jsp"
 *  
 *  @author - Indra
 */
public class EstadisticasIndAction extends Action  {

	
	protected static Log log = LogFactory.getLog(EstadisticasIndAction.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
		String elforward="listarestadistica";
    	
		try {
			Bdestadisticasind bdestadisticasind = new Bdestadisticasind(request, form);
			//se mete en sesion para que es muy 'pesao'
			request.getSession().setAttribute("MVA_listaestadisticaind",bdestadisticasind.getListaresultante());
			request.getSession().setAttribute("MVA_statmicrositeind",bdestadisticasind.getStatmicrosite());
			request.setAttribute("MVA_listaanyos",bdestadisticasind.getListaanyos());
			
			
		} catch (Exception e) {
			elforward="error";
		}
        return mapping.findForward(elforward);
    }
	
}
