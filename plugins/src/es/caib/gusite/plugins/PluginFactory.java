package es.caib.gusite.plugins;


import org.springframework.context.support.ClassPathXmlApplicationContext;
import es.caib.gusite.plugins.organigrama.OrganigramaProvider;

/**
 * Factoría necesaria para obtener los plugins en contextos no-spring (microback, microfront, etc)
 * @author at4.net
 *
 */
public class PluginFactory  {

	private static PluginFactory INSTANCE = null;
    private OrganigramaProvider organigramaProvider;

    private PluginFactory () {
    	this.loadPlugins();
    }

    private void loadPlugins() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        this.setOrganigramaProvider((OrganigramaProvider) applicationContext.getBean("OrganigramaProvider"));
        applicationContext.close();
	}

	public static PluginFactory getInstance() {
    	if (INSTANCE == null) {
    		INSTANCE = new PluginFactory();
    	}
        return INSTANCE;
    }

	public void setOrganigramaProvider(OrganigramaProvider organigramaProvider) {
		this.organigramaProvider = organigramaProvider;
	}

	public OrganigramaProvider getOrganigramaProvider() {
		if (this.organigramaProvider == null) {
			this.loadPlugins();
		}
		return organigramaProvider;
	}
}
