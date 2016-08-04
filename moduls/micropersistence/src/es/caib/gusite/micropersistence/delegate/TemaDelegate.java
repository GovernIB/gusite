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
		} catch (RemoteException e) {
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
	public void init(Long id) throws DelegateException {
		try {
			this.getFacade().init(id);
		} catch (RemoteException e) {
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
	public Long grabarTema(Temafaq tema) throws DelegateException {
		try {
			return this.getFacade().grabarTema(tema);
		} catch (RemoteException e) {
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
	public Temafaq obtenerTema(Long id) throws DelegateException {
		try {
			return this.getFacade().obtenerTema(id);
		} catch (RemoteException e) {
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
		} catch (RemoteException e) {
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
	public void borrarTema(Long id) throws DelegateException {
		try {
			this.getFacade().borrarTema(id);
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

	public List<?> listarCombo(Long idmicrosite) throws DelegateException {
		try {
			return this.getFacade().listarCombo(idmicrosite);
		} catch (RemoteException e) {
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

	private TemaFacade getFacade() throws RemoteException {
		return (TemaFacade) this.facadeHandle.getEJBObject();
	}

	protected TemaDelegate() throws DelegateException {
		try {
			TemaFacadeHome home = TemaFacadeUtil.getHome();
			TemaFacade remote = home.create();
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