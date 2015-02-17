package es.caib.gusite.micropersistence.delegate;

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.Handle;
import javax.naming.NamingException;

import es.caib.gusite.micromodel.PersonalizacionPlantilla;
import es.caib.gusite.micropersistence.intf.PersonalizacionPlantillaFacade;
import es.caib.gusite.micropersistence.intf.PersonalizacionPlantillaFacadeHome;
import es.caib.gusite.micropersistence.util.PersonalizacionPlantillaFacadeUtil;

/**
 * Delegate para manipular PersonalizacionPlantilla.
 * 
 * @author at4.net
 * 
 */
public class PersonalizacionPlantillaDelegate implements StatelessDelegate {

	private static final long serialVersionUID = -2076446869522196666L;

	/**
	 * Crea un PersonalizacionPlantilla
	 */
	public PersonalizacionPlantilla crearPersonalizacionPlantilla(
			PersonalizacionPlantilla instance) throws DelegateException {
		try {
			return this.getFacade().crearPersonalizacionPlantilla(instance);
		} catch (RemoteException re) {
			throw new DelegateException(re);
		}
	}

	/**
	 * Actualiza un PersonalizacionPlantilla
	 */
	public void actualizarPersonalizacionPlantilla(
			PersonalizacionPlantilla instance) throws DelegateException {
		try {
			this.getFacade().actualizarPersonalizacionPlantilla(instance);
		} catch (RemoteException re) {
			throw new DelegateException(re);
		}
	}

	/**
	 * Borra un PersonalizacionPlantilla
	 */
	public void borrarPersonalizacionPlantilla(PersonalizacionPlantilla instance)
			throws DelegateException {
		try {
			this.getFacade().actualizarPersonalizacionPlantilla(instance);
		} catch (RemoteException re) {
			throw new DelegateException(re);
		}
	}

	/**
	 * Lista todos los PersonalizacionPlantilla
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	@SuppressWarnings("unchecked")
	public List<PersonalizacionPlantilla> listarPersonalizacionPlantilla()
			throws DelegateException {
		try {
			return this.getFacade().listarPersonalizacionPlantilla();
		} catch (RemoteException re) {
			throw new DelegateException(re);
		}
	}

	/**
	 * Obtiene un PersonalizacionPlantilla
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public PersonalizacionPlantilla obtenerPersonalizacionPlantilla(
			java.lang.Long id) throws DelegateException {
		try {
			return this.getFacade().obtenerPersonalizacionPlantilla(id);
		} catch (RemoteException re) {
			throw new DelegateException(re);
		}
	}

	/* ========================================================= */
	/* ======================== REFERENCIA AL FACADE ========== */
	/* ========================================================= */

	private Handle facadeHandle;

	private PersonalizacionPlantillaFacade getFacade() throws RemoteException {
		return (PersonalizacionPlantillaFacade) this.facadeHandle
				.getEJBObject();
	}

	protected PersonalizacionPlantillaDelegate() throws DelegateException {
		try {
			PersonalizacionPlantillaFacadeHome home = PersonalizacionPlantillaFacadeUtil
					.getHome();
			PersonalizacionPlantillaFacade remote = home.create();
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
