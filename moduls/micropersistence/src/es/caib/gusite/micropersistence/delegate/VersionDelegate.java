package es.caib.gusite.micropersistence.delegate;

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.Handle;
import javax.naming.NamingException;

import es.caib.gusite.micromodel.Version;
import es.caib.gusite.micropersistence.intf.VersionFacade;
import es.caib.gusite.micropersistence.intf.VersionFacadeHome;
import es.caib.gusite.micropersistence.util.VersionFacadeUtil;

/**
 * Delegate para manipular Version.
 * 
 * @author at4.net
 * 
 */
public class VersionDelegate implements StatelessDelegate {

	private static final long serialVersionUID = -2076446869522196666L;

	/**
	 * Crea un Version
	 */
	public Version crearVersion(Version instance) throws DelegateException {
		try {
			return this.getFacade().crearVersion(instance);
		} catch (RemoteException re) {
			throw new DelegateException(re);
		}
	}

	/**
	 * Actualiza un Version
	 */
	public void actualizarVersion(Version instance) throws DelegateException {
		try {
			this.getFacade().actualizarVersion(instance);
		} catch (RemoteException re) {
			throw new DelegateException(re);
		}
	}

	/**
	 * Borra un Version
	 */
	public void borrarVersion(Version instance) throws DelegateException {
		try {
			this.getFacade().actualizarVersion(instance);
		} catch (RemoteException re) {
			throw new DelegateException(re);
		}
	}

	/**
	 * Lista todos los Version
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	@SuppressWarnings("unchecked")
	public List<Version> listarVersion() throws DelegateException {
		try {
			return this.getFacade().listarVersion();
		} catch (RemoteException re) {
			throw new DelegateException(re);
		}
	}

	/**
	 * Obtiene un Version
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Version obtenerVersion(java.lang.String id) throws DelegateException {
		try {
			return this.getFacade().obtenerVersion(id);
		} catch (RemoteException re) {
			throw new DelegateException(re);
		}
	}

	/* ========================================================= */
	/* ======================== REFERENCIA AL FACADE ========== */
	/* ========================================================= */

	private Handle facadeHandle;

	private VersionFacade getFacade() throws RemoteException {
		return (VersionFacade) this.facadeHandle.getEJBObject();
	}

	protected VersionDelegate() throws DelegateException {
		try {
			VersionFacadeHome home = VersionFacadeUtil.getHome();
			VersionFacade remote = home.create();
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
