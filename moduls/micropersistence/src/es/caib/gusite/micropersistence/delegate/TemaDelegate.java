package es.caib.gusite.micropersistence.delegate;

import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.Handle;
import javax.naming.NamingException;

import es.caib.gusite.micromodel.Temafaq;
import es.caib.gusite.micropersistence.intf.TemaFacade;
import es.caib.gusite.micropersistence.intf.TemaFacadeHome;
import es.caib.gusite.micropersistence.util.TemaFacadeUtil;

/**
 * Business delegate para manipular Tema.
 *
 * @author Indra
 */
public class TemaDelegate implements StatelessDelegate {

	/* ========================================================= */
	/* ======================== MÉTODOS DE NEGOCIO ============= */
	/* ========================================================= */

	private static final long serialVersionUID = -7223968347348123941L;

	/**
	 * Inicializo los parámetros de la consulta.
	 *
	 * @throws DelegateException
	 */
	public void init() throws DelegateException {
		try {
			this.getFacade().init();
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Inicializo los parámetros de la consulta.
	 *
	 * @param id
	 *            Id de un Tema
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
	 * Crea o actualiza un tema
	 *
	 * @param tema
	 * @return Id de un tema
	 * @throws DelegateException
	 */
	public Long grabarTema(final Temafaq tema) throws DelegateException {
		try {
			return this.getFacade().grabarTema(tema);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Obtiene un tema
	 *
	 * @param id
	 *            Id de un tema
	 * @return Temafaq
	 * @throws DelegateException
	 */
	public Temafaq obtenerTema(final Long id) throws DelegateException {
		try {
			return this.getFacade().obtenerTema(id);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Lista todos los temas
	 *
	 * @return una lista de temas
	 * @throws DelegateException
	 */
	public List<?> listarTemas() throws DelegateException {
		try {
			return this.getFacade().listarTemas();
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * borra un tema
	 *
	 * @param id
	 *            Id de un Tema
	 * @throws DelegateException
	 */
	public void borrarTema(final Long id) throws DelegateException {
		try {
			this.getFacade().borrarTema(id);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * borra un tema
	 *
	 * @param id
	 *            Id de un Tema
	 * @param indexar
	 *            Indica si se indexa
	 * @throws DelegateException
	 */
	public void borrarTema(final Long id, final boolean indexar) throws DelegateException {
		try {
			this.getFacade().borrarTema(id);
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

	public List<?> listarCombo(final Long idmicrosite) throws DelegateException {
		try {
			return this.getFacade().listarCombo(idmicrosite);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Comprueba si un tema pertenece o no a un microsite
	 *
	 * @param site
	 *            Id de un site
	 * @param id
	 *            Id de un tema
	 * @return True si el tema no pertenece al site
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

	private TemaFacade getFacade() throws RemoteException {
		return (TemaFacade) this.facadeHandle.getEJBObject();
	}

	protected TemaDelegate() throws DelegateException {
		try {
			final TemaFacadeHome home = TemaFacadeUtil.getHome();
			final TemaFacade remote = home.create();
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