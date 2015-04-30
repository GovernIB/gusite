package es.caib.gusite.front.interceptor;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import es.caib.gusite.front.service.FrontUrlFactory;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.Usuario;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.MicrositeDelegate;

public class LoginInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	protected FrontUrlFactory frontUrlFactory;

	private static Log log = LogFactory.getLog(LoginInterceptor.class);

	private static final String varName = "org.springframework.web.servlet.HandlerMapping.uriTemplateVariables";
	private static final String path = "org.springframework.web.servlet.HandlerMapping.pathWithinHandlerMapping";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

		if (request.getServletPath().startsWith("/resources/")) {
			// No hacemos nada con los recursos estáticos
			return true;
		}
		Map varMap = (Map) request.getAttribute(varName);
		
		String siteKey = (String) varMap.get("uri");
		String lang = (String)varMap.get("lang");
	
		try {
			MicrositeDelegate micrositeDelegate = DelegateUtil.getMicrositeDelegate();
			Microsite microsite = micrositeDelegate.obtenerMicrositebyUri(siteKey);
			if (microsite != null) {
				
				String urlErrorRol = this.frontUrlFactory.errorRol(microsite, lang);
				if (request.getServletPath().startsWith(urlErrorRol)) {
					//Para las urls de error no se comprueba el usuario
					return true;
				}
				
				if (this.redirigir(microsite, request)) {
					String redirect = (String) request.getAttribute(path);
					request.getSession().setAttribute("redirect", redirect.substring(1));
					response.sendRedirect(this.frontUrlFactory.intranetLogin(request.getContextPath()));
					return false;
				} else if (this.denegarAcceso(microsite, request)) {
					response.sendRedirect(request.getContextPath()+ urlErrorRol);
					return false;
				} else if (!this.siteIsVisible(microsite, request)) {
					response.sendError(HttpServletResponse.SC_FORBIDDEN);
					return false;
				}
			}

		} catch (DelegateException e) {
			log.error("Error interceptor", e);
			return false;
		} catch (IOException e) {
			log.error("Error redirect", e);
			return false;
		}
		return true;
	}

	private boolean siteIsVisible(Microsite microsite, HttpServletRequest request) throws DelegateException {
		if ("S".equals(microsite.getVisible())) {
			return true;
		} else {
			//Microsite restringido, sólo tienen acceso los gestores
			if (request.getUserPrincipal() == null) {
				//No debería llegar aquí, porque este caso se trata en this.redirigir
				return false;
			}			
			//Comprueba que el usuario es gestor del microsite
			MicrositeDelegate bdMicro = DelegateUtil.getMicrositeDelegate();
			List<Usuario> usuarios = bdMicro.listarUsernamesbyMicrosite(microsite.getId());
			for (Usuario u : usuarios) {
				if (u.getUsername().equals(request.getUserPrincipal().getName())) {
					return true;
				}
			}
			
			return false;
		}
	}

	private boolean redirigir(Microsite microsite, HttpServletRequest request) {

		//Si no hay usuario logueado y  el acceso es restringido o no visible,
		//entonces hay que redirigir al login 
		return 	request.getUserPrincipal() == null &&
				(
				("R".equals(microsite.getAcceso()) || "M".equals(microsite.getAcceso())) //Microsite restringido
				||
				!"S".equals(microsite.getVisible()) //Microsite no visible
				);
	}

	private boolean denegarAcceso(Microsite microsite, HttpServletRequest request) {

		return microsite.getAcceso() != null && microsite.getAcceso().equals("M") && !request.isUserInRole(microsite.getRol());
	}
}
