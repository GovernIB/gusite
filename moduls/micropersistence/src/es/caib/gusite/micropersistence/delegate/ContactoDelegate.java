package es.caib.gusite.micropersistence.delegate;

import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.Handle;
import javax.naming.NamingException;

import es.caib.gusite.micromodel.Contacto;
import es.caib.gusite.micromodel.Lineadatocontacto;
import es.caib.gusite.micropersistence.intf.ContactoFacade;
import es.caib.gusite.micropersistence.intf.ContactoFacadeHome;
import es.caib.gusite.micropersistence.util.ContactoFacadeUtil;

/**
 * Business delegate para manipular Accesibilidad.
 *
 * @author Indra
 */
public class ContactoDelegate implements StatelessDelegate {

	/* ========================================================= */
	/* ======================== MÉTODOS DE NEGOCIO ============= */
	/* ========================================================= */

	private static final long serialVersionUID = -9212510036435820243L;

	public void init() throws DelegateException {
		try {
			this.getFacade().init();
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Inicializo los parámetros de la consulta de Contacto.
	 *
	 * @param id
	 *            Id del contacto
	 * @throws DelegateException
	 */
	public void init(final Long id) throws DelegateException {
		try {
			this.getFacade().init(id);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Crea o actualiza un Contacto
	 *
	 * @param contacto
	 * @return Id del contacto
	 * @throws DelegateException
	 */
	public Long grabarContacto(final Contacto contacto) throws DelegateException {
		try {
			return this.getFacade().grabarContacto(contacto);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Obtiene una linea del Formulario
	 *
	 * @param id
	 *            Id de la linea de contacto
	 * @return Lineadatocontacto
	 * @throws DelegateException
	 */
	public Lineadatocontacto obtenerLinea(final Long id) throws DelegateException {
		try {
			return this.getFacade().obtenerLinea(id);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Obtiene un contacto
	 *
	 * @param id
	 *            Id del contato
	 * @return Contacto
	 * @throws DelegateException
	 */
	public Contacto obtenerContacto(final Long id) throws DelegateException {
		try {
			return this.getFacade().obtenerContacto(id);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Obtiene un contacto
	 *
	 * @param id
	 *            Id del contato
	 * @return Contacto
	 * @throws DelegateException
	 */
	public Contacto obtenerContactoByUri(final String idioma, final String uri, final String site)
			throws DelegateException {
		try {
			return this.getFacade().obtenerContactoByUri(idioma, uri, site);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Lista todos los Formularios
	 *
	 * @return una Lista de contactos
	 * @throws DelegateException
	 */
	public List<?> listarContactos() throws DelegateException {
		try {
			return this.getFacade().listarContactos();
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * borra un contacto
	 *
	 * @param id
	 * @throws DelegateException
	 */
	public void borrarContacto(final Long id) throws DelegateException {
		try {
			this.getFacade().borrarContacto(id);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * borra un contacto
	 *
	 * @param id
	 * @param indexar
	 *            Indica si se indexa
	 * @throws DelegateException
	 */
	public void borrarContacto(final Long id, final boolean indexar) throws DelegateException {
		try {
			this.getFacade().borrarContacto(id, indexar);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Añade una nueva línea al formulario o modifica la que existe
	 *
	 * @param lin
	 * @param idcontacto
	 *            Id del contacto
	 * @throws DelegateException
	 */
	public void creamodificaLinea(final Lineadatocontacto lin, final Long idcontacto) throws DelegateException {
		try {
			this.getFacade().creamodificaLinea(lin, idcontacto);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * elimina líneas del formulario
	 *
	 * @param lineas
	 * @param contacto_id
	 *            Id del contacto
	 * @throws DelegateException
	 */
	public void eliminarLineas(final String[] lineas, final Long contacto_id) throws DelegateException {
		try {
			this.getFacade().eliminarLineas(lineas, contacto_id);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}

	}

	/**
	 * elimina una línea del formulario
	 *
	 * @param idLinea
	 *            Id de la linea de contacto
	 * @param idContacto
	 *            Id del contacto
	 * @throws DelegateException
	 */
	public void eliminarLinea(final Long idLinea, final Long idContacto) throws DelegateException {
		try {
			this.getFacade().eliminarLinea(idLinea, idContacto);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}

	}

	public Hashtable<?, ?> getParametros() throws DelegateException {
		try {
			return this.getFacade().getParametros();
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public void parametrosCons() throws DelegateException {
		try {
			this.getFacade().parametrosCons();
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public int getPagina() throws DelegateException {
		try {
			return this.getFacade().getPagina();
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public void setPagina(final int pagina) throws DelegateException {
		try {
			this.getFacade().setPagina(pagina);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public void setOrderby(final String orderby) throws DelegateException {
		try {
			this.getFacade().setOrderby(orderby);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public void setOrderby2(final String orderby) throws DelegateException {
		try {
			this.getFacade().setOrderby2(orderby);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public String getValorBD(final String valor) throws DelegateException {
		try {
			return this.getFacade().getValorBD(valor);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public void setFiltro(final String valor) throws DelegateException {
		try {
			this.getFacade().setFiltro(valor);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public String getWhere() throws DelegateException {
		try {
			return this.getFacade().getWhere();
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public void setWhere(final String valor) throws DelegateException {
		try {
			this.getFacade().setWhere(valor);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public int getTampagina() throws DelegateException {
		try {
			return this.getFacade().getTampagina();
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public void setTampagina(final int tampagina) throws DelegateException {
		try {
			this.getFacade().setTampagina(tampagina);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Comprueba que el elemento pertenece al Microsite
	 *
	 * @param site
	 * @param id
	 * @return true si no pertenece
	 * @throws DelegateException
	 */
	public boolean checkSite(final Long site, final Long id) throws DelegateException {
		try {
			return this.getFacade().checkSite(site, id);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/* ========================================================= */
	/* ======================== REFERENCIA AL FACADE ========== */
	/* ========================================================= */

	private Handle facadeHandle;

	private ContactoFacade getFacade() throws RemoteException {
		return (ContactoFacade) this.facadeHandle.getEJBObject();
	}

	protected ContactoDelegate() throws DelegateException {
		try {
			final ContactoFacadeHome home = ContactoFacadeUtil.getHome();
			final ContactoFacade remote = home.create();
			this.facadeHandle = remote.getHandle();
		} catch (final NamingException e) {
			throw new DelegateException(e);
		} catch (final CreateException e) {
			throw new DelegateException(e);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}
}