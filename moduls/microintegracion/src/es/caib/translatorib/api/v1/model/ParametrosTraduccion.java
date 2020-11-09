package es.caib.translatorib.api.v1.model;

public abstract class ParametrosTraduccion {

	public static final String PLUGIN = "plugin";
	public static final String PLUGIN_MOCKUP = "mock";
	public static final String PLUGIN_OPENTRAD = "opentrad";

	/** No es obligatorio el plugin, sino se selecciona la opción por defecto. **/
	private String plugin;

	public String getPlugin() {
		return plugin;
	}

	public void setPlugin(final String plugin) {
		this.plugin = plugin;
	}

}
