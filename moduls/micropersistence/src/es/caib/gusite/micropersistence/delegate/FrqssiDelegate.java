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

	public void init(final Long id, final String idiomapasado) throws DelegateException {
		try {
			this.getFacade().init(id, idiomapasado);
		} catch (final RemoteException e) {
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
	public Long grabarFrqssi(final Frqssi tp) throws DelegateException {
		try {
			return this.getFacade().grabarFrqssi(tp);
		} catch (final RemoteException e) {
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
	public Frqssi obtenerFrqssi(final Long id) throws DelegateException {
		try {
			return this.getFacade().obtenerFrqssi(id);
		} catch (final RemoteException e) {
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
		} catch (final RemoteException e) {
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
	public List<?> listarFrqssisrec(final String idiomapasado) throws DelegateException {
		try {
			return this.getFacade().listarFrqssisrec(idiomapasado);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Borra un Formulario QSSI
	 *
	 * @param id
	 * @throws DelegateException
	 */
	public void borrarFrqssi(final Long id) throws DelegateException {
		try {
			this.getFacade().borrarFrqssi(id);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Borra un Formulario QSSI
	 *
	 * @param id
	 * @param indexar
	 *            Indica si se indexa
	 * @throws DelegateException
	 */
	public void borrarFrqssi(final Long id, final boolean indexar) throws DelegateException {
		try {
			this.getFacade().borrarFrqssi(id, indexar);
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

	private FrqssiFacade getFacade() throws RemoteException {
		return (FrqssiFacade) this.facadeHandle.getEJBObject();
	}

	protected FrqssiDelegate() throws DelegateException {
		try {
			final FrqssiFacadeHome home = FrqssiFacadeUtil.getHome();
			final FrqssiFacade remote = home.create();
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