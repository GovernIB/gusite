package org.ibit.rol.sac.microback.plugins;

import javax.servlet.ServletException;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ModuleConfig;

/**
 * Plugin para hacer funcionar BeanUtils con objetos hibernate.
 * 
 * @author Indra
 */
public class HibernateConverterPlugin implements PlugIn {

    private Converter formerStringConverter = null;

    public void init(ActionServlet servlet, ModuleConfig moduleConfig) throws ServletException {
        formerStringConverter = ConvertUtils.lookup(String.class);
        Converter newStringConverter = new HibernateDelegateConverter(formerStringConverter);
        ConvertUtils.register(newStringConverter, String.class);
    }

    public void destroy() {
        ConvertUtils.register(formerStringConverter, String.class);
    }

}
