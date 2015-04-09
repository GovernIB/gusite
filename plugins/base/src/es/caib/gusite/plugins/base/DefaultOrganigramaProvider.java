package es.caib.gusite.plugins.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import es.caib.gusite.plugins.PluginException;
import es.caib.gusite.plugins.organigrama.OrganigramaProvider;
import es.caib.gusite.plugins.organigrama.UnidadData;
import es.caib.gusite.plugins.organigrama.UnidadListData;

@Service("OrganigramaProvider")
public class DefaultOrganigramaProvider implements OrganigramaProvider {

	private static Log log = LogFactory.getLog(DefaultOrganigramaProvider.class);
	
	@Override
	public UnidadData getUnidadData(Serializable unidadId, String lang) throws PluginException {
		log.debug("Cargando unidad data");
		UnidadData unidadData = new UnidadData();
		unidadData.setNombre("Corporación Acme");
		unidadData.setAbreviatura("Corporación Acme");
		unidadData.setIdUnidad(unidadId);
		unidadData.setIdUnidadPadre(null);
		unidadData.setUrl("http://es.wikipedia.org/wiki/Corporaci%C3%B3n_Acme");
		unidadData.setUrlPlano("http://es.wikipedia.org/wiki/Corporaci%C3%B3n_Acme");
		unidadData.setDireccion("Road Runner/Wile E. Coyote cartoons");
		unidadData.setCodigoPostal("07012");
		unidadData.setPoblacion("Arizona");
		unidadData.setTelefono("971971971");
		unidadData.setFax("971971971");
		return unidadData;
	
	}

	@Override
	public Collection<UnidadListData> getUnidadesPrincipales(String lang) throws PluginException {
		Collection<UnidadListData> ret = new ArrayList<UnidadListData>();
		ret.add(getUnidadData(1, lang));
		ret.add(getUnidadData(2, lang));
		return ret;
	}

	@Override
	public Collection<UnidadListData> getUnidades(String lang) throws PluginException {
		return getUnidadesPrincipales(lang);
	}

	@Override
	public Collection<UnidadListData> getUnidadesHijas(Serializable unidadId, String lang) throws PluginException {
		return new ArrayList<UnidadListData>();
	}

	
	
}
