package org.ibit.rol.sac.microback.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.ibit.rol.sac.micropersistence.delegate.AccesibilidadDelegate;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;

/**
 * Action que trata la accesibilidad de un microsite <BR>
 * <P>
 * Definición Struts:<BR>
 * action path="/accesibilitat" <BR>
 * unknown="false"<BR>
 * forward name="acce" path="/accesibilitat.jsp"
 * 
 * @author Indra
 */
public class AccesibilidadAction extends BaseAction {

	protected static Log log = LogFactory.getLog(AccesibilidadAction.class);
	protected AccesibilidadDelegate _accDel = null;

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String iditem = "" + request.getParameter("iditem");
		String tipo = "" + request.getParameter("tipo");
		// borramos Antes toda informacion previa de testeo para el microsite.
		_accDel = DelegateUtil.getAccesibilidadDelegate();
		List<?> lista = _accDel.obtenerAccesibilidadItem(new Long(iditem));
		request.setAttribute("lista", lista);
		request.setAttribute("tipo", tipo);
		return mapping.findForward("acce");
	}

}
