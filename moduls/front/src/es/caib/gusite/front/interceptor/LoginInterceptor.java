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
	public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response,
			final Object handler) {

		if (request.getServletPath().startsWith("/resources/")) {
			// No hacemos nada con los recursos estáticos
			return true;
		}
		final Map varMap = (Map) request.getAttribute(varName);

		final String siteKey = (String) varMap.get("uri");
		final String lang = (String) varMap.get("lang");

		try {
			final MicrositeDelegate micrositeDelegate = DelegateUtil.getMicrositeDelegate();
			final Microsite microsite = micrositeDelegate.obtenerMicrositebyUri(siteKey);
			if (microsite != null) {

				final String urlErrorRol = this.frontUrlFactory.errorRol(microsite, lang);
				if (request.getServletPath().startsWith(urlErrorRol)) {
					// Para las urls de error no se comprueba el usuario
					return true;
				}

				if (this.redirigir(microsite, request)) {
					// String redirect = (String) request.getAttribute(path);
					// request.getSession().setAttribute("redirect", redirect.substring(1));
					request.getSession().setAttribute("redirect", obtenerRedirect(request));
					response.sendRedirect(this.frontUrlFactory.intranetLogin(request.getContextPath()));
					return false;
				} else if (this.denegarAcceso(microsite, request)) {
					response.sendRedirect(request.getContextPath() + urlErrorRol);
					return false;
				} else if (!this.siteIsVisible(microsite, request)) {
					response.sendError(HttpServletResponse.SC_FORBIDDEN);
					return false;
				}
			}

		} catch (final DelegateException e) {
			log.error("Error interceptor", e);
			return false;
		} catch (final IOException e) {
			log.error("Error redirect", e);
			return false;
		}
		return true;
	}

	private Object obtenerRedirect(final HttpServletRequest request) {
		String redirect = ((String) request.getAttribute(path)).substring(1);

		if (request.getParameter("tipo") != null || request.getParameter("previsual") != null) {
			redirect += "?";
		}

		if (request.getParameter("tipo") != null) {
			redirect += "tipo=" + request.getParameter("tipo");
		}

		if (request.getParameter("previsual") != null) {
			if (request.getParameter("tipo") != null) {
				redirect += "&";
			}
			redirect += "previsual=" + request.getParameter("previsual");
		}

		return redirect;
	}

	private boolean siteIsVisible(final Microsite microsite, final HttpServletRequest request)
			throws DelegateException {
		if ("S".equals(microsite.getVisible())) {
			return true;
		} else {
			// Microsite restringido, sólo tienen acceso los gestores
			if (request.getUserPrincipal() == null) {
				// No debería llegar aquí, porque este caso se trata en this.redirigir
				return false;
			}
			// Comprueba que el usuario es gestor del microsite
			final MicrositeDelegate bdMicro = DelegateUtil.getMicrositeDelegate();
			final List<Usuario> usuarios = bdMicro.listarUsernamesbyMicrosite(microsite.getId());
			for (final Usuario u : usuarios) {
				if (u.getUsername().equals(request.getUserPrincipal().getName())) {
					return true;
				}
			}

			return false;
		}
	}

	private boolean redirigir(final Microsite microsite, final HttpServletRequest request) {

		// Si no hay usuario logueado y el acceso es restringido o no visible,
		// entonces hay que redirigir al login
		return request.getUserPrincipal() == null
				&& (("R".equals(microsite.getAcceso()) || "M".equals(microsite.getAcceso())) // Microsite restringido
						|| !"S".equals(microsite.getVisible()) // Microsite no visible
						||
						// Es una previsualizacion
						(request.getParameter("previsual") != null
								&& "previsual".equals(request.getParameter("previsual"))));
	}

	private boolean denegarAcceso(final Microsite microsite, final HttpServletRequest request) {

		return microsite.getAcceso() != null && microsite.getAcceso().equals("M")
				&& !request.isUserInRole(microsite.getRol());
	}
}
