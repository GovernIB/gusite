package es.caib.gusite.front.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import es.caib.gusite.front.general.DelegateBase;
import es.caib.gusite.front.general.ExceptionFrontPagina;
import es.caib.gusite.micromodel.Contenido;
import es.caib.gusite.micromodel.Menu;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micropersistence.delegate.DelegateException;

@Service
public class ContenidoDataService {

	protected static Log log = LogFactory.getLog(ContenidoDataService.class);
	

	public Contenido getContenido(Microsite microsite, Long idContenido, String idioma) throws ExceptionFrontPagina {
		DelegateBase _delegateBase = new DelegateBase();
		Contenido contenidolocal = _delegateBase.obtenerContenido(idContenido, idioma, microsite);
		return contenidolocal;
	}


	public Menu obtenerMenuBranch(Contenido contenido, String idioma) throws ExceptionFrontPagina {
		DelegateBase _delegateBase = new DelegateBase();
		try {
			return _delegateBase.obtenerMenuBranch(contenido.getMenu().getId(), idioma);
		} catch (DelegateException e) {
			log.error(e);
			throw new ExceptionFrontPagina(e);
		}
	}


	
}
