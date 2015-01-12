package es.caib.gusite.microback.config;

import org.apache.struts.action.ActionFormBean;

/**
 * Bean de configuración para formulario multilenguaje
 * 
 *@author Indra
 */
public class TraFormBeanConfig extends ActionFormBean {

	private static final long serialVersionUID = 8822249202802921954L;
	protected String traduccionClassName;

    public String getTraduccionClassName() {
        return traduccionClassName;
    }

    public void setTraduccionClassName(String traduccionClassName) {
        if (configured) {
            throw new IllegalStateException("Configuration is frozen");
        }
        this.traduccionClassName = traduccionClassName;
    }

}
