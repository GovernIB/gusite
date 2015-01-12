package es.caib.gusite.microback.plugins;

import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ModuleConfig;

import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.IdiomaDelegate;

import javax.servlet.ServletException;
import javax.servlet.UnavailableException;

/**
 * Fija los lenguajes usados por la aplicación
 * 
 * @author Indra
 * @deprecated Eliminar del código, cuando se compruebe que funciona en BaseAction
 */
public class IdiomaPlugin implements PlugIn {

    public static final String LANGS_KEY = "es.caib.gusite.microback.LANGS_KEY";

    ActionServlet servlet;

    public void init(ActionServlet servlet, ModuleConfig config) throws ServletException {
        IdiomaDelegate delegate = DelegateUtil.getIdiomaDelegate();
        this.servlet = servlet;

        try {
            this.servlet.getServletContext().setAttribute(LANGS_KEY, delegate.listarLenguajes());
        } catch (DelegateException e) {
            throw new UnavailableException("No se pudieron encontrar los idiomas.");
        }
    }

    public void destroy() {
        servlet.getServletContext().removeAttribute(LANGS_KEY);
        servlet = null;
    }

}
