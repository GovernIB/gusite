package es.caib.gusite.front.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import es.caib.gusite.front.general.ExceptionFront;
import es.caib.gusite.micromodel.PersonalizacionPlantilla;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.PersonalizacionPlantillaDelegate;

@Service
public class TemplateDataService {

	protected static Log log = LogFactory.getLog(TemplateDataService.class);

	@Autowired
	private CacheManager cacheManager;

	public PersonalizacionPlantilla getPlantilla(final Long idPerPla) throws ExceptionFront {
		Cache cache = cacheManager.getCache("plantillas");
		Cache.ValueWrapper cached = cache.get(idPerPla);
		if (cached != null) {
			return (PersonalizacionPlantilla) cached.get();
		}
		try {
			final PersonalizacionPlantillaDelegate ppdel = DelegateUtil.getPersonalizacionPlantillaDelegate();
			final PersonalizacionPlantilla ret = ppdel.obtenerPersonalizacionPlantilla(idPerPla);
			if (ret != null) {
				cache.put(idPerPla, ret);
			}
			return ret;

		} catch (final DelegateException e) {
			throw new ExceptionFront(e);
		}
	}

	/**
	 * Plantillas aplicables al microsite
	 *
	 * @param microsite
	 * @return
	 * @throws ExceptionFront
	 */
	@Cacheable(value = "plantillasSite", condition = "#desarrollo != 'S'")
	public List<String> getPlantillasAplicables(final Long idmicrosite, final String desarrollo) throws ExceptionFront {
		try {
			// TODO: EJBs como beans spring
			final PersonalizacionPlantillaDelegate ppdel = DelegateUtil.getPersonalizacionPlantillaDelegate();
			final List<String> ret = ppdel.searchByMicrosite(idmicrosite);
			return ret;

		} catch (final DelegateException e) {
			throw new ExceptionFront(e);
		}
	}

}
