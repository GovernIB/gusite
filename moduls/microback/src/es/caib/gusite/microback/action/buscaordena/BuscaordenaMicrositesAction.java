package es.caib.gusite.microback.action.buscaordena;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.gusite.microback.base.Base;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.Usuario;
import es.caib.gusite.micropersistence.delegate.AccesibilidadDelegate;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.MicrositeDelegate;
import es.caib.gusite.micropersistence.delegate.UsuarioDelegate;
import es.caib.gusite.plugins.PluginFactory;
import es.caib.gusite.plugins.organigrama.OrganigramaProvider;
import es.caib.gusite.plugins.organigrama.UnidadData;

/**
 * Action que prepara el listado de Microsites <BR>
 * <P>
 * 	Definición Struts:<BR>
 *  action path="/microsites" <BR> 
 *  name="BuscaOrdenaMicrositeActionForm" <BR> 
 *  forward name="listarMicrosites" path="/listaMicrosites.jsp"
 *  
 *  @author Indra
 */
public class BuscaordenaMicrositesAction extends Action {

	protected static Log log = LogFactory.getLog(es.caib.gusite.microback.action.buscaordena.BuscaordenaMicrositesAction.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		 
	 	siEstaEnMantenimientoAvisar(request);
	 
    	/***************************************************************/
    	/***************   RECOGER USUARIO Y ROLES   *******************/
    	/***************************************************************/    	
    	Base.usuarioRefresh(request);
    	
    	/***************************************************************/
    	/***************     LISTADO  MICROSITES     *******************/
    	/***************************************************************/    	
		//obtener usuario
     	UsuarioDelegate usudel = DelegateUtil.getUsuarioDelegate();
     	Usuario usu = usudel.obtenerUsuariobyUsername(request.getRemoteUser());
     	usudel.isUsuarioNulo(usu);
 	
     	MicrositeDelegate micro = DelegateUtil.getMicrositeDelegate();
     	List<?> listamicros = micro.listarMicrositesbyUser(usu);
     	           
        //saco los nombres de uas para cada microsite
		Iterator<?> iter = listamicros.iterator();
		OrganigramaProvider op = PluginFactory.getInstance().getOrganigramaProvider();
		
		while (iter.hasNext()) {
    		Microsite mic = (Microsite) iter.next();
    		UnidadData ua = op.getUnidadData(mic.getUnidadAdministrativa(), "ca");
			if (ua != null) {
				mic.setNombreUA(ua.getNombre());

				AccesibilidadDelegate acces = DelegateUtil.getAccesibilidadDelegate();
				int nivelAcces = acces.existeAccesibilidadMicrosite(mic.getId());
				mic.setNivelAccesibilidad(nivelAcces);
			}
    	} 
    	
    	request.setAttribute("listado", listamicros);
    	return mapping.findForward("listarMicrosites");
	}
	 
	protected void siEstaEnMantenimientoAvisar(HttpServletRequest request) {
		if (estaEnMantenimiento()) {
			request.setAttribute("MVS_manteniment", new Object());
		}
	}

	protected boolean estaEnMantenimiento() {
		String mantenimiento = System.getProperty("es.caib.gusite.mantenimiento");
		return null != mantenimiento && "si".equalsIgnoreCase(mantenimiento);
	}
	
}
