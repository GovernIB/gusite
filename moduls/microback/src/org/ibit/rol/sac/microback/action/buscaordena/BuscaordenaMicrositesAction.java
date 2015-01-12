package org.ibit.rol.sac.microback.action.buscaordena;

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
import org.ibit.rol.sac.microback.base.Base;
import org.ibit.rol.sac.micromodel.Microsite;
import org.ibit.rol.sac.micromodel.Usuario;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;
import org.ibit.rol.sac.micropersistence.delegate.MicrositeDelegate;
import org.ibit.rol.sac.micropersistence.delegate.AccesibilidadDelegate;
import org.ibit.rol.sac.model.Idioma;
import org.ibit.rol.sac.model.TraduccionUA;
import org.ibit.rol.sac.model.UnidadAdministrativa;
import org.ibit.rol.sac.persistence.delegate.UnidadAdministrativaDelegate;
import org.ibit.rol.sac.micropersistence.delegate.UsuarioDelegate;


/**
 * Action que prepara el listado de Microsites <BR>
 * <P>
 * 	Definici�n Struts:<BR>
 *  action path="/microsites" <BR> 
 *  name="BuscaOrdenaMicrositeActionForm" <BR> 
 *  forward name="listarMicrosites" path="/listaMicrosites.jsp"
 *  
 *  @author Indra
 */
public class BuscaordenaMicrositesAction extends Action {

	protected static Log log = LogFactory.getLog(org.ibit.rol.sac.microback.action.buscaordena.BuscaordenaMicrositesAction.class);
	
	 public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		 
		 	siEstaEnMantenimientoAvisar(request);
		 
	    	/***************************************************************/
	    	/***************   RECOGER USUARIO Y ROLES   *******************/
	    	/***************************************************************/    	
	    	Base.usuarioRefresh(request);
	    	
	    	
	    	/***************************************************************/
	    	/***************     LISTADO  MICROSITES     *******************/
	    	/***************************************************************/    	
			//obtener usuario
	     	UsuarioDelegate usudel=DelegateUtil.getUsuarioDelegate();
	     	Usuario usu = usudel.obtenerUsuariobyUsername(request.getRemoteUser());
     	
	     	MicrositeDelegate micro = DelegateUtil.getMicrositeDelegate();
	     	List<?> listamicros = micro.listarMicrositesbyUser(usu);
	     	           
            //saco los nombres de uas para cada microsite
            Iterator<?> iter = listamicros.iterator();
        	while (iter.hasNext()) {
        		Microsite mic = (Microsite) iter.next();
        		UnidadAdministrativaDelegate uniad=org.ibit.rol.sac.persistence.delegate.DelegateUtil.getUADelegate();
            	UnidadAdministrativa uo = uniad.obtenerUnidadAdministrativa(new Long(mic.getUnidadAdministrativa()));            	                        	
            	mic.setNombreUA(((TraduccionUA)uo.getTraduccion(Idioma.DEFAULT)).getNombre());
            	
    	     	AccesibilidadDelegate acces = DelegateUtil.getAccesibilidadDelegate();
    	     	int nivelAcces = acces.existeAccesibilidadMicrosite(mic.getId());
    	     	mic.setNivelAccesibilidad(nivelAcces);
            	
        	} 
        	request.setAttribute("listado", listamicros);
		 
        	return mapping.findForward("listarMicrosites");
	 }
	 
	 
		protected void siEstaEnMantenimientoAvisar(HttpServletRequest request) {
			if(estaEnMantenimiento() ) {
				request.setAttribute("MVS_manteniment", new Object());
			}
		}


		protected boolean estaEnMantenimiento() {
			String mantenimiento = System.getProperty("es.caib.gusite.mantenimiento");
			return null!=mantenimiento && "si".equalsIgnoreCase(mantenimiento);
		}
}
