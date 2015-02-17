package es.caib.gusite.micropersistence.delegate;

import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.Handle;
import javax.naming.NamingException;

import es.caib.gusite.micropersistence.intf.EstadisticaGroupFacade;
import es.caib.gusite.micropersistence.intf.EstadisticaGroupFacadeHome;
import es.caib.gusite.micropersistence.util.EstadisticaGroupFacadeUtil;

/**
 * Business delegate para manipular estadisticasgroup.
 * 
 * @author Indra
 */
public class EstadisticaGroupDelegate implements StatelessDelegate {

	private static final long serialVersionUID = -8625498818407268453L;

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

	/**
	 * Lista todas las estadisticas agrupadas por referencia.<br/>
	 * Se pueden filtrar por internet, intranet o ambas.<br/>
	 * 
	 * @param publico
	 *            [1=internet, 0=intranet, null=ambas]
	 * @return List, lista de beans `EstadisticaGroup´
	 * @throws DelegateException
	 */
	public List<?> listarEstadisticasbyRefThin(Integer publico)
			throws DelegateException {
		try {
			return this.getFacade().listarEstadisticasbyRefThin(publico);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Lista todas las estadisticas agrupadas por referencia
	 * 
	 * @param publico
	 * @return una lista de estadisticas
	 * @throws DelegateException
	 */
	public List<?> listarEstadisticasbyItemThin(Integer publico)
			throws DelegateException {
		try {
			return this.getFacade().listarEstadisticasbyItemThin(publico);
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

	public void setOrderby(String orderby) throws DelegateException {
		try {
			this.getFacade().setOrderby(orderby);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public void setOrderby2(String orderby) throws DelegateException {
		try {
			this.getFacade().setOrderby2(orderby);
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

	public String getWhere() throws DelegateException {
		try {
			return this.getFacade().getWhere();
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public void setWhere(String valor) throws DelegateException {
		try {
			this.getFacade().setWhere(valor);
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

	/* ========================================================= */
	/* ======================== REFERENCIA AL FACADE ========== */
	/* ========================================================= */

	private Handle facadeHandle;

	private EstadisticaGroupFacade getFacade() throws RemoteException {
		return (EstadisticaGroupFacade) this.facadeHandle.getEJBObject();
	}

	protected EstadisticaGroupDelegate() throws DelegateException {
		try {
			EstadisticaGroupFacadeHome home = EstadisticaGroupFacadeUtil
					.getHome();
			EstadisticaGroupFacade remote = home.create();
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
