package org.ibit.rol.sac.microback.action;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.ibit.rol.sac.microback.process.Proceso;
import org.ibit.rol.sac.micromodel.Microsite;
import org.ibit.rol.sac.micropersistence.delegate.AccesibilidadDelegate;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;

/**
 * Action que realiza el test W3C en el microsite <P>
 * 
 * 	Definici�n Struts:<BR>
 *  action path="/procesow3c"<BR> 
 *  unknown="false" <BR>
 *  forward name="procesow3ctesteando" path="/procesow3ctesteando.jsp" 
 *  
 *  @author - Indra
 */
public class W3cAction extends BaseAction {

	protected static Log log = LogFactory.getLog(W3cAction.class);
	//	delegados a utilizar
	protected AccesibilidadDelegate _accDel = null;
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception {
		
		String lanzar = "" + request.getParameter("lanzar");
		String finalizado = "" + request.getParameter("finalizado");
		if (lanzar.equals("yes")) {
			//borramos Antes toda informacion previa de testeo para el microsite.
			_accDel = DelegateUtil.getAccesibilidadDelegate();
			_accDel.borrarAccesibilidadMicro(((Microsite)request.getSession().getAttribute("MVS_microsite")).getId());
			String idbarraestado = Proceso.repasarMicrositeW3C(request);
			request.setAttribute("MVS_idbarraestado", idbarraestado);
			request.setAttribute("testeando", "yes");
			return mapping.findForward("procesow3ctesteando");
		} else {
			if (!finalizado.equals("null")) request.setAttribute("finalizado", finalizado);
			//Adem�s cargamos los resultados de los ya existentes
			AccesibilidadDelegate accDel = DelegateUtil.getAccesibilidadDelegate();
			HashMap<?, ?> mapaListados = accDel.obtenerMapaListados(((Microsite)request.getSession().getAttribute("MVS_microsite")).getId());
			request.setAttribute("mapalistados", mapaListados);
			
		}
		
		return mapping.findForward("procesow3c");
	}
	
}
