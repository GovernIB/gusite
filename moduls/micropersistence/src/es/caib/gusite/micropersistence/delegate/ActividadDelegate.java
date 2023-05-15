package es.caib.gusite.micropersistence.delegate;

import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.Handle;
import javax.naming.NamingException;

import es.caib.gusite.micromodel.Actividadagenda;
import es.caib.gusite.micropersistence.intf.ActividadFacade;
import es.caib.gusite.micropersistence.intf.ActividadFacadeHome;
import es.caib.gusite.micropersistence.util.ActividadFacadeUtil;

/**
 * Business delegate para manipular Actividades.
 *
 * @author Indra
 */
public class ActividadDelegate implements StatelessDelegate {

	private static final long serialVersionUID = 3120776484924556049L;

	/* ========================================================= */
	/* ======================== MÉTODOS DE NEGOCIO ============= */
	/* ========================================================= */

	public void init() throws DelegateException {
		try {
			this.getFacade().init();
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public void init(final Long id) throws DelegateException {
		try {
			this.getFacade().init(id);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Crea o actualiza una actividad
	 *
	 * @param activi
	 * @return Identificador
	 * @throws DelegateException
	 */
	public Long grabarActividad(final Actividadagenda activi) throws DelegateException {
		try {
			return this.getFacade().grabarActividad(activi);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Obtiene una Actividad
	 *
	 * @param id
	 *            Id de la actividad
	 * @return Actividadagenda
	 * @throws DelegateException
	 */
	public Actividadagenda obtenerActividad(final Long id) throws DelegateException {
		try {
			return this.getFacade().obtenerActividad(id);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Lista todas las actividades
	 *
	 * @return una lista de actividades
	 * @throws DelegateException
	 */
	public List<?> listarActividades() throws DelegateException {
		try {
			return this.getFacade().listarActividades();
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Borra una actividad
	 *
	 * @param id
	 *            Id de la actividad
	 *
	 * @throws DelegateException
	 */
	public void borrarActividad(final Long id) throws DelegateException {
		try {
			this.getFacade().borrarActividad(id);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Borra una actividad
	 *
	 * @param id
	 *            Id de la actividad
	 * @param indexar
	 *            Indica si se indexa
	 * @throws DelegateException
	 */
	public void borrarActividad(final Long id, final boolean indexar) throws DelegateException {
		try {
			this.getFacade().borrarActividad(id, indexar);
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

	public void setOrderby(final String orderby) throws DelegateException {
		try {
			this.getFacade().setOrderby(orderby);
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

	public List<?> listarCombo(final Long idmicrosite) throws DelegateException {
		try {
			return this.getFacade().listarCombo(idmicrosite);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

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

	private ActividadFacade getFacade() throws RemoteException {
		return (ActividadFacade) this.facadeHandle.getEJBObject();
	}

	protected ActividadDelegate() throws DelegateException {
		try {
			final ActividadFacadeHome home = ActividadFacadeUtil.getHome();
			final ActividadFacade remote = home.create();
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