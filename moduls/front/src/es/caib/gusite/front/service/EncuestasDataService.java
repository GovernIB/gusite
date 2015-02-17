package es.caib.gusite.front.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import es.caib.gusite.front.general.ExceptionFrontPagina;
import es.caib.gusite.micromodel.Encuesta;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.EncuestaDelegate;

@Service
public class EncuestasDataService {

	protected static Log log = LogFactory.getLog(EncuestasDataService.class);

	public Encuesta getEncuesta(Microsite microsite, Idioma lang,
			long idEncuesta) throws ExceptionFrontPagina {

		try {
			EncuestaDelegate encuestadel = DelegateUtil.getEncuestaDelegate();
			Encuesta encuesta = encuestadel.obtenerEncuesta(idEncuesta);
			if (encuesta == null) {
				throw new ExceptionFrontPagina("Encuesta no encontrada: "
						+ idEncuesta, ExceptionFrontPagina.HTTP_NOT_FOUND);
			}
			encuesta.setIdi(lang.getLang());
			return encuesta;
		} catch (DelegateException e) {
			log.error(e);
			throw new ExceptionFrontPagina(e);
		}

	}

	public Encuesta getEncuesta(Microsite microsite, String uriEncuesta,
			String lang) throws ExceptionFrontPagina {

		try {
			EncuestaDelegate encuestadel = DelegateUtil.getEncuestaDelegate();
			Encuesta encuesta = encuestadel.obtenerEncuestaDesdeUri(lang,
					uriEncuesta);
			if (encuesta == null) {
				// Si no lo encontramos por idioma, buscamos cualquiera. Esto
				// sirve para el cambio de idioma sencillo
				encuesta = encuestadel.obtenerEncuestaDesdeUri(null,
						uriEncuesta);
			}
			if (encuesta == null) {
				throw new ExceptionFrontPagina("Encuesta no encontrada: "
						+ uriEncuesta, ExceptionFrontPagina.HTTP_NOT_FOUND);
			}
			encuesta.setIdi(lang);
			return encuesta;
		} catch (DelegateException e) {
			log.error(e);
			throw new ExceptionFrontPagina(e);
		}

	}

}
