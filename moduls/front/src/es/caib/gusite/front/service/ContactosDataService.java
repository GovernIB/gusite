package es.caib.gusite.front.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import es.caib.gusite.front.general.BaseCriteria;
import es.caib.gusite.front.general.ExceptionFrontContactos;
import es.caib.gusite.front.general.ExceptionFrontPagina;
import es.caib.gusite.front.general.bean.ResultadoBusqueda;
import es.caib.gusite.micromodel.Contacto;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.Tipo;
import es.caib.gusite.micropersistence.delegate.ContactoDelegate;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;

@Service
public class ContactosDataService {

	protected static Log log = LogFactory.getLog(ContactosDataService.class);

	@SuppressWarnings("unchecked")
	public ResultadoBusqueda<Contacto> getListadoFormularios(Microsite microsite, Idioma lang, BaseCriteria formulario) throws DelegateException {

		ContactoDelegate contactodel = DelegateUtil.getContactoDelegate();
		contactodel.init();
		contactodel.setWhere("where contacto.visible='S' and contacto.idmicrosite=" + microsite.getId() + " and trad.id.codigoIdioma = '" + 
								 lang.getCodigoEstandar() + "'" );

		if (formulario.getFiltro() != null && formulario.getFiltro().length() > 0) {
			contactodel.setFiltro(formulario.getFiltro());
		}

		if (formulario.getOrdenacion() != null && formulario.getOrdenacion().length() > 0) {
			contactodel.setOrderby(formulario.getOrdenacion());
		}

		// Indicamos la página a visualizar
		contactodel.setPagina(formulario.getPagina());

		List<Contacto> listacontactos = (List<Contacto>) contactodel.listarContactos();
		ResultadoBusqueda<Contacto> ret = new ResultadoBusqueda<Contacto>(listacontactos, (Map<String, Integer>) contactodel.getParametros());
		return ret;

	}

	public Contacto getFormulario(Microsite microsite, Idioma lang, long idContacto) throws DelegateException {
		ContactoDelegate contactoldel = DelegateUtil.getContactoDelegate();
		return contactoldel.obtenerContacto(idContacto);
	}
	
	public Contacto getFormularioByUri(Microsite microsite, Idioma lang, String uri) throws DelegateException, ExceptionFrontContactos, ExceptionFrontPagina {
		ContactoDelegate contactoldel = DelegateUtil.getContactoDelegate();

		try {
			Contacto contacto = contactoldel.obtenerContactoByUri(lang.getCodigoEstandar(), uri, microsite.getId().toString());
			if (contacto == null) {
				// Si no lo encontramos por idioma, buscamos cualquiera. Esto
				// sirve para el cambio de idioma sencillo
				contacto = contactoldel.obtenerContactoByUri(null, uri, microsite.getId().toString());
			}
			if (contacto == null) {
				throw new ExceptionFrontContactos("Contacto no encontrado: " + uri);
			}
			return contacto;
		} catch (DelegateException e) {
			throw new ExceptionFrontContactos("problemas al cargar el contacto (DelegateException): " + uri);
		}
		
	}

}
