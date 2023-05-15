package es.caib.gusite.micropersistence.delegate;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.Handle;
import javax.naming.NamingException;

import es.caib.gusite.micromodel.Componente;
import es.caib.gusite.micropersistence.intf.ComponenteFacade;
import es.caib.gusite.micropersistence.intf.ComponenteFacadeHome;
import es.caib.gusite.micropersistence.util.ComponenteFacadeUtil;

/**
 * Business delegate para manipular Componentes.
 *
 * @author Indra
 *
 */
public class ComponenteDelegate implements StatelessDelegate {

	private static final long serialVersionUID = 8327711002117555757L;

	/* ========================================================= */
	/* ======================== MÉTODOS DE NEGOCIO ============= */
	/* ========================================================= */

	/**
	 * Inicializo los parámetros de la consulta de Componente.
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
	 * Inicializo los parámetros de la consulta de Componente.
	 *
	 * @param id
	 *            del componente
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
	 * Crea o actualiza un Componente
	 *
	 * @param compo
	 * @return Id del componente
	 * @throws DelegateException
	 */
	public Long grabarComponente(final Componente compo) throws DelegateException, IOException {
		try {
			return this.getFacade().grabarComponente(compo);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Obtiene un Componente
	 *
	 * @param id
	 *            del componente
	 * @return Componente
	 * @throws DelegateException
	 */
	public Componente obtenerComponente(final Long id) throws DelegateException {
		try {
			return this.getFacade().obtenerComponente(id);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Lista todos los Componentes
	 *
	 * @return una lista de componentes
	 * @throws DelegateException
	 */
	public List<Componente> listarComponentes() throws DelegateException {
		try {
			return this.getFacade().listarComponentes();
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * borra un Componente
	 *
	 * @param id
	 *            del componente
	 * @throws DelegateException
	 */
	public void borrarComponente(final Long id) throws DelegateException {
		try {
			this.getFacade().borrarComponente(id);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * borra un Componente
	 *
	 * @param id
	 *            del componente
	 * @param indexar
	 *            Indica si se indexa
	 * @throws DelegateException
	 */
	public void borrarComponente(final Long id, final boolean indexar) throws DelegateException {
		try {
			this.getFacade().borrarComponente(id, indexar);
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
	 * Comprueba que el componente pertenece al Microsite
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

	private ComponenteFacade getFacade() throws RemoteException {
		return (ComponenteFacade) this.facadeHandle.getEJBObject();
	}

	protected ComponenteDelegate() throws DelegateException {
		try {
			final ComponenteFacadeHome home = ComponenteFacadeUtil.getHome();
			final ComponenteFacade remote = home.create();
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