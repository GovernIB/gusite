package es.caib.gusite.front.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import es.caib.gusite.front.general.ExceptionFront;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.PersonalizacionPlantilla;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.PersonalizacionPlantillaDelegate;

@Service
public class TemplateDataService {

	protected static Log log = LogFactory.getLog(TemplateDataService.class);



	public PersonalizacionPlantilla getPlantilla(Long idPerPla) throws ExceptionFront {
		try {
			PersonalizacionPlantillaDelegate ppdel = DelegateUtil.getPersonalizacionPlantillaDelegate();
			PersonalizacionPlantilla ret = ppdel.obtenerPersonalizacionPlantilla(idPerPla);
			return ret;

		} catch (DelegateException e) {
			throw new ExceptionFront(e);
		}
	}


	/**
	 * Plantillas aplicables al microsite
	 * @param microsite
	 * @return
	 * @throws ExceptionFront
	 */
	@Cacheable(value="plantillasSite", condition="#microsite.desarrollo != 'S'")
	public List<PersonalizacionPlantilla> getPlantillasAplicables(Microsite microsite) throws ExceptionFront {
		try {
			//TODO: EJBs como beans spring
			PersonalizacionPlantillaDelegate ppdel = DelegateUtil.getPersonalizacionPlantillaDelegate();
			List<PersonalizacionPlantilla> ret = ppdel.searchByMicrosite(microsite.getId());
			return ret;

		} catch (DelegateException e) {
			throw new ExceptionFront(e);
		}
	}

	
	public PersonalizacionPlantilla getPlantilla(Microsite microsite, String plantilla) throws ExceptionFront {
		for (PersonalizacionPlantilla pp : this.getPlantillasAplicables(microsite)) {
			if (pp.getPlantilla().getNombre().equals(plantilla)) {
				return pp;
			}
		}
		return null;
	}


}
