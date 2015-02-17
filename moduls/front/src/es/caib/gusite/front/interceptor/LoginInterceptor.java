package es.caib.gusite.front.interceptor;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import es.caib.gusite.front.service.FrontUrlFactory;
import es.caib.gusite.micromodel.Microsite;
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
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) {

		Map varMap = (Map) request.getAttribute(varName);
		String siteKey = (String) varMap.get("uri");
		try {
			MicrositeDelegate micrositeDelegate = DelegateUtil
					.getMicrositeDelegate();
			Microsite microsite = micrositeDelegate
					.obtenerMicrositebyUri(siteKey);
			if (this.redirigir(microsite, request)) {
				String redirect = (String) request.getAttribute(path);
				request.getSession().setAttribute("redirect",
						redirect.substring(1));
				response.sendRedirect(this.frontUrlFactory
						.intranetLogin(request.getContextPath()));
			} else if (this.denegarAcceso(microsite, request)) {
				return false;
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

	private boolean redirigir(Microsite microsite, HttpServletRequest request) {

		return (microsite != null && microsite.getAcceso() != null && request
				.getUserPrincipal() == null)
				&& (microsite.getAcceso().equals("R") || microsite.getAcceso()
						.equals("M"));
	}

	private boolean denegarAcceso(Microsite microsite,
			HttpServletRequest request) {

		return microsite != null && microsite.getAcceso() != null
				&& microsite.getAcceso().equals("M")
				&& !request.isUserInRole(microsite.getRol());
	}
}
