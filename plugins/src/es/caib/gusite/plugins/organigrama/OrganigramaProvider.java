package es.caib.gusite.plugins.organigrama;

import java.io.Serializable;
import java.util.Collection;

import es.caib.gusite.plugins.PluginDefinition;
import es.caib.gusite.plugins.PluginException;

/**
 * Plugin gusite que obtiene los datos de organigrama
 * @author agarcia
 *
 */
@PluginDefinition
public interface OrganigramaProvider {

	/**
	 * Obtiene la lista completa de unidades
	 * @param lang
	 * @return
	 * @throws PluginException
	 */
	Collection<UnidadListData> getUnidades(String lang) throws PluginException;
	
	/**
	 * Obtiene las unidades administrativas principales (o de primer nivel)
	 * @param lang
	 * @return
	 * @throws PluginException
	 */
	Collection<UnidadListData> getUnidadesPrincipales(String lang) throws PluginException;
	
	/**
	 * Obtiene los datos completos de una unidad orgánica
	 * @param unidadId
	 * @param lang
	 * @return
	 * @throws PluginException
	 */
	UnidadData getUnidadData (Serializable unidadId, String lang) throws PluginException;

	/**
	 * Obtiene la lista de unidades hijas de una unidad determinada
	 * @param unidadId
	 * @param lang
	 * @return
	 * @throws PluginException
	 */
	Collection<UnidadListData> getUnidadesHijas(Serializable unidadId, String lang) throws PluginException;
	

}
