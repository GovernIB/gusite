package es.caib.gusite.front.interceptor;

import es.caib.gusite.front.service.FrontUrlFactory;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.MicrositeDelegate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    protected FrontUrlFactory frontUrlFactory;

    private static Log log = LogFactory.getLog(LoginInterceptor.class);

    private static final String varName = "org.springframework.web.servlet.HandlerMapping.uriTemplateVariables";
    private static final String path = "org.springframework.web.servlet.HandlerMapping.pathWithinHandlerMapping";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        Map varMap = (Map) request.getAttribute(varName);
        String siteKey = (String) varMap.get("uri");
        try {
            MicrositeDelegate micrositeDelegate = DelegateUtil.getMicrositeDelegate();
            Microsite microsite = micrositeDelegate.obtenerMicrositebyUri(siteKey);
            if (redirigir(microsite, request)) {
                String redirect = (String) request.getAttribute(path);
                request.getSession().setAttribute("redirect", redirect.substring(1));
                response.sendRedirect(this.frontUrlFactory.intranetLogin());
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

        return microsite != null
                && microsite.getAcceso() != null
                && microsite.getAcceso().equals("R")
                && request.getUserPrincipal() == null;
    }
}
