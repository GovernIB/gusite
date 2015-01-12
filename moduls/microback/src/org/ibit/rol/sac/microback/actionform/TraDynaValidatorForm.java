package org.ibit.rol.sac.microback.actionform;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.util.RequestUtils;
import org.apache.struts.validator.DynaValidatorForm;
import org.ibit.rol.sac.microback.config.TraFormBeanConfig;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;
import org.ibit.rol.sac.micropersistence.delegate.IdiomaDelegate;

/**
 *	Formulario que a�ade funcionalidad multilenguaje 
 *
 *@author Indra
 */
public class TraDynaValidatorForm extends DynaValidatorForm {

	private static final long serialVersionUID = 1232790162647704654L;

	protected static Log log = LogFactory.getLog(TraDynaValidatorForm.class);

    protected String traClassName = null;

    /* (non-Javadoc)
     * @see org.apache.struts.validator.DynaValidatorForm#reset(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {

        super.reset(mapping, request);
        initialize(mapping);

        if (traClassName == null) {
            traClassName = getTraduccionClassName(request, mapping);
        }

        List traducciones = (List) this.get("traducciones");
        try {
            IdiomaDelegate delegate = DelegateUtil.getIdiomaDelegate();
            int numLangs = delegate.listarLenguajes().size();
            for (int i = 0; i < numLangs; i++) {
                traducciones.add(RequestUtils.applicationInstance(traClassName));
            }
        } catch (Throwable t) {
            log.error("Error creando traducciones", t);
        }
    }

    /**
     * M�todo que devuelve el nombre de la clase multilenguaje
     * @param request	petici�n de usuario
     * @param mapping	mapeo
     * @return String	nombre de la clase multilenguaje
     */
    private String getTraduccionClassName(HttpServletRequest request, ActionMapping mapping) {
        ModuleConfig config = RequestUtils.getModuleConfig(request, getServlet().getServletContext());
        TraFormBeanConfig beanConfig = (TraFormBeanConfig) config.findFormBeanConfig(mapping.getName());
        String className = beanConfig.getTraduccionClassName();
        return className;
    }
}
