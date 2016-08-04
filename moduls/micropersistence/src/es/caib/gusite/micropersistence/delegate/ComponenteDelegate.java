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
		} catch (RemoteException e) {
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
	public void init(Long id) throws DelegateException {
		try {
			this.getFacade().init(id);
		} catch (RemoteException e) {
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
	public Long grabarComponente(Componente compo) throws DelegateException, IOException {
		try {
			return this.getFacade().grabarComponente(compo);
		} catch (RemoteException e) {
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
	public Componente obtenerComponente(Long id) throws DelegateException {
		try {
			return this.getFacade().obtenerComponente(id);
		} catch (RemoteException e) {
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
		} catch (RemoteException e) {
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
	public void borrarComponente(Long id) throws DelegateException {
		try {
			this.getFacade().borrarComponente(id);
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

	/**
	 * Comprueba que el componente pertenece al Microsite
	 * 
	 * @param site
	 * @param id
	 * @return true si no pertenece
	 * @throws DelegateException
	 */
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

	private ComponenteFacade getFacade() throws RemoteException {
		return (ComponenteFacade) this.facadeHandle.getEJBObject();
	}

	protected ComponenteDelegate() throws DelegateException {
		try {
			ComponenteFacadeHome home = ComponenteFacadeUtil.getHome();
			ComponenteFacade remote = home.create();
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