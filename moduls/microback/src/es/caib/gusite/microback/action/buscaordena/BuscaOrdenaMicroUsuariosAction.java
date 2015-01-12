package es.caib.gusite.microback.action.buscaordena;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.gusite.microback.action.BaseAction;
import es.caib.gusite.microback.base.Base;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.Usuario;
import es.caib.gusite.micromodel.UsuarioPropietarioMicrosite;
import es.caib.gusite.micropersistence.delegate.AccesibilidadDelegate;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.MicrositeDelegate;
import es.caib.gusite.micropersistence.delegate.UsuarioDelegate;
import es.caib.gusite.utilities.rolsacAPI.APIUtil;
import es.caib.rolsac.api.v2.exception.QueryServiceException;
import es.caib.rolsac.api.v2.rolsac.RolsacQueryService;
import es.caib.rolsac.api.v2.unitatAdministrativa.UnitatAdministrativaCriteria;
import es.caib.rolsac.api.v2.unitatAdministrativa.UnitatAdministrativaDTO;
import es.caib.rolsac.api.v2.unitatAdministrativa.UnitatAdministrativaQueryServiceAdapter;

/**
 * Action que prepara el listado de microsites de Usuario <BR>
 * <P>
 * Definición Struts:<BR>
 * action path="/microUsuarios" <BR>
 * name="BuscaOrdenaMicroUsuarioActionForm" <BR>
 * forward name="listarUsuarios" path="/listaUsuariosMicrosite.jsp"<BR>
 * forward name="listarMicrositesdelusu" path="/listaMicrositesdelusu.jsp"
 * 
 * @author Indra
 */
public class BuscaOrdenaMicroUsuariosAction extends BaseAction {

	protected static Log log = LogFactory.getLog(es.caib.gusite.microback.action.buscaordena.BuscaOrdenaMicroUsuariosAction.class);
	
	private static List<UnitatAdministrativaQueryServiceAdapter> cacheListaUAs = null;
	private static Date fechaUltimaComprobacionCacheUas = null;
	private static final int MAX_MILISEGUNDOS_COMPROBACION_UAS = 300000; // 5 minutos
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ActionForward doExecute(ActionMapping mapping, ActionForm form, 
    		HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		/***************************************************************/
		/*************** RECOGER USUARIO Y ROLES *******************/
		/***************************************************************/
		Base.usuarioRefresh(request);

		// Procesamos petición.
		String accion = request.getParameter("accion");
		
		if ("nuevouser".equals(accion)) {
			nuevoUser(request);
			return mapping.findForward("info");
		}
		
		else if ("nuevomicro".equals(accion)) {
			nuevoMicro(request);
			return mapping.findForward("microusu");
		}
		
		else if ("lista".equals(accion)) {
			listadoMicroSitesUsuario(request);
			return mapping.findForward("listarMicrositesdelusu");
		}
		
		else {
			listadoUsuarios(request);
			return mapping.findForward("listarUsuarios");
		}
		
	}

	private void listadoUsuarios(HttpServletRequest request) throws DelegateException {
		
		/***************************************************************/
		/*************** LISTADO USUARIOS *******************/
		/***************************************************************/
		MicrositeDelegate micro = DelegateUtil.getMicrositeDelegate();
		List listausuarios = micro.listarUsernamesbyMicrosite(((Microsite)request.getSession().getAttribute("MVS_microsite")).getId());

		UsuarioDelegate uDel = DelegateUtil.getUsuarioDelegate();
		List listaresultante = uDel.listarUsuariosPerfil("sacoper");
		List lista = uDel.listarUsuariosPerfil("sacsuper");
		listaresultante.addAll(lista);
		lista = uDel.listarUsuariosPerfil("sacadmin");
		listaresultante.addAll(lista);

		Comparator comp = new UsuarioComparator();
		Collections.sort(listaresultante, comp);

		request.setAttribute("listatodos", listaresultante);
		request.setAttribute("listado", listausuarios);
		
	}
	
	private static List<UnitatAdministrativaQueryServiceAdapter> obtenerListaUAsCacheada() throws QueryServiceException {
		
		Date fechaLlamada = new Date();
		
		// Si es la primera llamada o se ha sobrepasado el tiempo de caché, actualizamos lista de UAs obtenidas vía WS. 
		if (fechaUltimaComprobacionCacheUas == null || 
				(fechaLlamada.getTime() - fechaUltimaComprobacionCacheUas.getTime() > MAX_MILISEGUNDOS_COMPROBACION_UAS)) {
			
			// Obtener UAs.
			RolsacQueryService rqs = APIUtil.getRolsacQueryService();
			UnitatAdministrativaCriteria uaCriteria = new UnitatAdministrativaCriteria();
			uaCriteria.setIdioma(Idioma.getIdiomaPorDefecto());
			
			// Actualizar caché de UAs.
			cacheListaUAs = rqs.llistarUnitatsAdministratives(uaCriteria);
			
			// Actualizar fecha de modificación de caché.
			fechaUltimaComprobacionCacheUas = new Date();
			
		}
		
		return cacheListaUAs;
		
	}

	private void listadoMicroSitesUsuario(HttpServletRequest request) throws DelegateException, QueryServiceException {
		
		/***************************************************************/
		/*************** LISTADO MICROSITES POR USUARIO *****/
		/***************************************************************/

		MicrositeDelegate microde = DelegateUtil.getMicrositeDelegate();
		AccesibilidadDelegate acces = DelegateUtil.getAccesibilidadDelegate();
		
		List listamicros = microde.listarMicrodeluser("" + request.getParameter("id"));
		Iterator iter = listamicros.iterator();
		
		while (iter.hasNext()) {
			
			Microsite mic = (Microsite) iter.next();
			RolsacQueryService rqs = APIUtil.getRolsacQueryService();
			UnitatAdministrativaCriteria uaCriteria = new UnitatAdministrativaCriteria();
			uaCriteria.setId(String.valueOf(mic.getUnidadAdministrativa()));
			uaCriteria.setIdioma("ca");

			UnitatAdministrativaDTO ua = rqs.obtenirUnitatAdministrativa(uaCriteria);
			mic.setNombreUA(ua.getNombre());

			// FIXME amartin: esto hace lentísimo el proceso de carga de los microsites asignados al usuario
			// si éste posee un número elevado de los mismos.
			int nivelAcces = acces.existeAccesibilidadMicrosite(mic.getId());
			mic.setNivelAccesibilidad(nivelAcces);

		}
		
		// Generamos el mapa de idUA => nombreUA (así sólo hacemos una llamda al WS).			
		List<UnitatAdministrativaQueryServiceAdapter> listaUAs = obtenerListaUAsCacheada();
		Map<Long, String> mapaUAs = new HashMap<Long, String>();
		
		for (UnitatAdministrativaQueryServiceAdapter ua : listaUAs) {
			mapaUAs.put(ua.getId(), ua.getNombre());
		}
		
		List listaresultante = microde.listarMicrositesThin();
		iter = listaresultante.iterator();
					
		while (iter.hasNext()) {
			
			Microsite mic = (Microsite)iter.next();
			String nombreUA = mapaUAs.get(new Long(mic.getUnidadAdministrativa()));
			mic.setNombreUA(nombreUA);
			
		}
		
		request.setAttribute("listatodos", listaresultante);
		request.setAttribute("listado", listamicros);
		request.setAttribute("elusu", ("" + request.getParameter("id")));

		UsuarioDelegate usudel = DelegateUtil.getUsuarioDelegate();
		Usuario usu = usudel.obtenerUsuario(Long.valueOf(request.getParameter("id")));

		request.setAttribute("nomusu", ("" + usu.getNombre()));
		
	}
		
	private void nuevoUser(HttpServletRequest request) throws DelegateException {
		
		Long idsite = ((Microsite) request.getSession().getAttribute("MVS_microsite")).getId();
		Long iduser = new Long("" + request.getParameter("iduser"));
		UsuarioPropietarioMicrosite upm = new UsuarioPropietarioMicrosite(idsite, iduser);
		MicrositeDelegate bdMicro = DelegateUtil.getMicrositeDelegate();
		bdMicro.grabarUsuarioPropietarioMicrosite(upm);

		addMessage(request, "micro.usuario.mensa.listausuarios");
		addMessage(request, "micro.usuario.mensa.newuser.ok");
		
	}
	
	private void nuevoMicro(HttpServletRequest request) throws DelegateException {
		
		Long idsite = new Long("" + request.getParameter("idmicro"));
		Long iduser = new Long("" + request.getParameter("iduser"));
		UsuarioPropietarioMicrosite upm = new UsuarioPropietarioMicrosite(idsite, iduser);
		MicrositeDelegate bdMicro = DelegateUtil.getMicrositeDelegate();
		bdMicro.grabarUsuarioPropietarioMicrosite(upm);

		String destino = ("<a href=\"microUsuarios.do?accion=lista&id=" + iduser + "\">Llista de microsites de l'usuari</a>");
		request.setAttribute("destino", destino);
		// addMessage(request, destino);
		addMessage(request, "micro.usuario.mensa.newmicro.ok");
		
	}
	
	private static class UsuarioComparator implements Comparator<Object> {
		public int compare(Object element1, Object element2) {

			String nom1 = (((Usuario) element1).getNombre() != null) ? ((Usuario) element1).getNombre() : "";
			String nom2 = (((Usuario) element2).getNombre() != null) ? ((Usuario) element2).getNombre() : "";

			return nom1.toLowerCase().compareTo(nom2.toLowerCase());
			
		}
	}
	
}
