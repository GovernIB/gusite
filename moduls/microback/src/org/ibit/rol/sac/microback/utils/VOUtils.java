package org.ibit.rol.sac.microback.utils;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.struts.action.DynaActionForm;
import org.ibit.rol.sac.microback.actionform.TraDynaActionForm;
import org.ibit.rol.sac.microback.plugins.HibernateDelegateConverter;
import org.ibit.rol.sac.micromodel.Traduccion;
import org.ibit.rol.sac.micromodel.Traducible;
import org.ibit.rol.sac.micromodel.ValueObject;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;

/**
 * Utilidades para pasar datos entre <code>ValueObject</code> i
 * <code>ActionForm</code>.
 * 
 * @author Indra
 */
public class VOUtils {

	public static void populate(ValueObject vo, DynaActionForm form) throws Exception {
        //BeanUtils.populate(vo, form.getMap());
    }

    public static void populate(Traducible vo, TraDynaActionForm form) throws Exception {
    	//BeanUtils.populate(vo, form.getMap());

    	List<?> llista = (List<?>) form.get("traducciones");
        List<?> langs = DelegateUtil.getIdiomaDelegate().listarLenguajes();

        
        Map traduccions = new HashMap(langs.size());
        for (int i = 0; i < langs.size(); i++) {
            String lang = (String) langs.get(i);
            traduccions.put(lang, llista.get(i));
        }
        vo.setTraduccionMap(traduccions);
        
    }

    public static void describe(DynaActionForm form, ValueObject vo) throws Exception {
        testBeanUtils();
        //BeanUtils.populate(form, BeanUtils.describe(vo));
    }

    public static void describe(TraDynaActionForm form, Traducible vo) throws Exception {
        testBeanUtils();
        
        //BeanUtils.populate(form, BeanUtils.describe(vo));
        List<Traduccion> traducciones = (List) form.get("traducciones");
        List<?> langs = DelegateUtil.getIdiomaDelegate().listarLenguajes();
        for (int i = 0; i < langs.size(); i++) {
            String lang = (String) langs.get(i);
            Traduccion traduccion = vo.getTraduccion(lang);
            if (traduccion != null) {
                traducciones.set(i, traduccion);
            }
        }
    }

    private static void testBeanUtils() {
        Converter currentConverter = ConvertUtils.lookup(String.class);
        if (!(currentConverter instanceof HibernateDelegateConverter)) {
            Converter newConverter = new HibernateDelegateConverter(currentConverter);
            ConvertUtils.register(newConverter, String.class);
        }
    }
}
