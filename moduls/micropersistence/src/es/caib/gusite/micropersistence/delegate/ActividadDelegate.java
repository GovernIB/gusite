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
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public void init(Long id) throws DelegateException {
		try {
			this.getFacade().init(id);
		} catch (RemoteException e) {
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
	public Long grabarActividad(Actividadagenda activi)
			throws DelegateException {
		try {
			return this.getFacade().grabarActividad(activi);
		} catch (RemoteException e) {
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
	public Actividadagenda obtenerActividad(Long id) throws DelegateException {
		try {
			return this.getFacade().obtenerActividad(id);
		} catch (RemoteException e) {
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
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Borra una actividad
	 * 
	 * @param id
	 *            Id de la actividad
	 * @throws DelegateException
	 */
	public void borrarActividad(Long id) throws DelegateException {
		try {
			this.getFacade().borrarActividad(id);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public Hashtable<?, ?> getParametros() throws DelegateException {
		try {
			return this.getFacade().getParametros();
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public void parametrosCons() throws DelegateException {
		try {
			this.getFacade().parametrosCons();
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public int getPagina() throws DelegateException {
		try {
			return this.getFacade().getPagina();
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public void setPagina(int pagina) throws DelegateException {
		try {
			this.getFacade().setPagina(pagina);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public int getTampagina() throws DelegateException {
		try {
			return this.getFacade().getTampagina();
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public void setTampagina(int tampagina) throws DelegateException {
		try {
			this.getFacade().setTampagina(tampagina);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public void setOrderby(String orderby) throws DelegateException {
		try {
			this.getFacade().setOrderby(orderby);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public String getValorBD(String valor) throws DelegateException {
		try {
			return this.getFacade().getValorBD(valor);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public void setFiltro(String valor) throws DelegateException {
		try {
			this.getFacade().setFiltro(valor);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public List<?> listarCombo(Long idmicrosite) throws DelegateException {
		try {
			return this.getFacade().listarCombo(idmicrosite);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public boolean checkSite(Long site, Long id) throws DelegateException {
		try {
			return this.getFacade().checkSite(site, id);
		} catch (RemoteException e) {
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
			ActividadFacadeHome home = ActividadFacadeUtil.getHome();
			ActividadFacade remote = home.create();
			this.facadeHandle = remote.getHandle();
		} catch (NamingException e) {
			throw new DelegateException(e);
		} catch (CreateException e) {
			throw new DelegateException(e);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}
}