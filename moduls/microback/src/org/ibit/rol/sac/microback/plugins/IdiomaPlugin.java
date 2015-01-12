package org.ibit.rol.sac.microback.plugins;

import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ModuleConfig;
import org.ibit.rol.sac.micropersistence.delegate.DelegateException;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;
import org.ibit.rol.sac.micropersistence.delegate.IdiomaDelegate;

import javax.servlet.ServletException;
import javax.servlet.UnavailableException;

/**
 * Fija los lenguajes usados por la aplicación
 * 
 * @author Indra
 */
public class IdiomaPlugin implements PlugIn {

    public static final String LANGS_KEY = "org.ibit.rol.sac.microback.LANGS_KEY";

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
