package es.caib.gusite.micropersistence.delegate;

import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.Handle;
import javax.naming.NamingException;

import es.caib.gusite.micromodel.Frqssi;
import es.caib.gusite.micropersistence.intf.FrqssiFacade;
import es.caib.gusite.micropersistence.intf.FrqssiFacadeHome;
import es.caib.gusite.micropersistence.util.FrqssiFacadeUtil;

/**
 * Business delegate para manipular Frqssi.
 * 
 * @author Indra
 */
public class FrqssiDelegate implements StatelessDelegate {

	private static final long serialVersionUID = -2506858131994160733L;

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

	public void init(Long id, String idiomapasado) throws DelegateException {
		try {
			this.getFacade().init(id, idiomapasado);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Crea o actualiza un Formulario QSSI
	 * 
	 * @param tp
	 * @return Id del Frqssi
	 * @throws DelegateException
	 */
	public Long grabarFrqssi(Frqssi tp) throws DelegateException {
		try {
			return this.getFacade().grabarFrqssi(tp);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Obtiene un Formulario QSSI
	 * 
	 * @param id
	 *            Id del Frqssi
	 * @return Frqssi
	 * @throws DelegateException
	 */
	public Frqssi obtenerFrqssi(Long id) throws DelegateException {
		try {
			return this.getFacade().obtenerFrqssi(id);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Lista todos los Formularios QSSI
	 * 
	 * @return una Lista de Frqssis
	 * @throws DelegateException
	 */
	public List<?> listarFrqssis() throws DelegateException {
		try {
			return this.getFacade().listarFrqssis();
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Lista todos los Formularios QSSI
	 * 
	 * @param idiomapasado
	 * @return una lista de Frqssisrec
	 * @throws DelegateException
	 */
	public List<?> listarFrqssisrec(String idiomapasado)
			throws DelegateException {
		try {
			return this.getFacade().listarFrqssisrec(idiomapasado);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Borra un Formulario QSSI
	 * 
	 * @param id
	 * @throws DelegateException
	 */
	public void borrarFrqssi(Long id) throws DelegateException {
		try {
			this.getFacade().borrarFrqssi(id);
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

	private FrqssiFacade getFacade() throws RemoteException {
		return (FrqssiFacade) this.facadeHandle.getEJBObject();
	}

	protected FrqssiDelegate() throws DelegateException {
		try {
			FrqssiFacadeHome home = FrqssiFacadeUtil.getHome();
			FrqssiFacade remote = home.create();
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