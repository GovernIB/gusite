package es.caib.gusite.front.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import es.caib.gusite.front.general.DelegateBase;
import es.caib.gusite.front.general.ExceptionFrontPagina;
import es.caib.gusite.micromodel.Contenido;
import es.caib.gusite.micromodel.Menu;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.TraduccionContenido;
import es.caib.gusite.micropersistence.delegate.ContenidoDelegate;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;

@Service
public class ContenidoDataService {

	protected static Log log = LogFactory.getLog(ContenidoDataService.class);

	public Contenido getContenido(Microsite microsite, Long idContenido,
			String idioma) throws ExceptionFrontPagina {
		DelegateBase _delegateBase = new DelegateBase();
		Contenido contenidolocal = _delegateBase.obtenerContenido(idContenido,
				idioma, microsite);
		return contenidolocal;
	}

	public Menu obtenerMenuBranch(Contenido contenido, String idioma)
			throws ExceptionFrontPagina {
		DelegateBase _delegateBase = new DelegateBase();
		try {
			return _delegateBase.obtenerMenuBranch(contenido.getMenu().getId(),
					idioma);
		} catch (DelegateException e) {
			log.error(e);
			throw new ExceptionFrontPagina(e);
		}
	}

	public Contenido getContenido(Microsite microsite, String uriContenido,
			String lang) throws ExceptionFrontPagina {

		try {
			ContenidoDelegate contenidodel = DelegateUtil
					.getContenidoDelegate();
			Contenido contenido = contenidodel.obtenerContenidoDesdeUri(lang,
					uriContenido);
			if (contenido == null) {
				// Si no lo encontramos por idioma, buscamos cualquiera. Esto
				// sirve para el cambio de idioma sencillo
				contenido = contenidodel.obtenerContenidoDesdeUri(null,
						uriContenido);
			}
			if (contenido == null) {
				throw new ExceptionFrontPagina("Contenido no encontrado: "
						+ uriContenido, ExceptionFrontPagina.HTTP_NOT_FOUND);
			}
			TraduccionContenido traduccionContenido = ((TraduccionContenido) contenido
					.getTraduccion(lang));
			if (traduccionContenido == null) {
				// Si no hay traduccion dada de alta para el contenido (lo cual
				// es una inconsistencia de bd, damos un not found)
				throw new ExceptionFrontPagina("Contenido no encontrado: "
						+ uriContenido, ExceptionFrontPagina.HTTP_NOT_FOUND);
			}
			contenido.setIdi(lang);

			return contenido;
		} catch (DelegateException e) {
			log.error(e);
			throw new ExceptionFrontPagina(e);
		}

	}

}
