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
	public PersonalizacionPlantilla crearPersonalizacionPlantilla(final PersonalizacionPlantilla instance)
			throws DelegateException {
		try {
			return getFacade().crearPersonalizacionPlantilla(instance);
		} catch (final RemoteException re) {
			throw new DelegateException(re);
		}
	}

	/**
	 * Actualiza un PersonalizacionPlantilla
	 */
	public void actualizarPersonalizacionPlantilla(final PersonalizacionPlantilla instance) throws DelegateException {
		try {
			getFacade().actualizarPersonalizacionPlantilla(instance);
		} catch (final RemoteException re) {
			throw new DelegateException(re);
		}
	}

	/**
	 * Borra un PersonalizacionPlantilla
	 */
	public void borrarPersonalizacionPlantilla(final PersonalizacionPlantilla instance) throws DelegateException {
		try {
			getFacade().borrarPersonalizacionPlantilla(instance);
		} catch (final RemoteException re) {
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
	public List<PersonalizacionPlantilla> listarPersonalizacionPlantilla() throws DelegateException {
		try {
			return getFacade().listarPersonalizacionPlantilla();
		} catch (final RemoteException re) {
			throw new DelegateException(re);
		}
	}

	/**
	 * Obtiene un PersonalizacionPlantilla
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public PersonalizacionPlantilla obtenerPersonalizacionPlantilla(final java.lang.Long id) throws DelegateException {
		try {
			return getFacade().obtenerPersonalizacionPlantilla(id);
		} catch (final RemoteException re) {
			throw new DelegateException(re);
		}
	}

	/**
	 * Busca PersonalizacionPlantilla por searchByMicrosite
	 */
	public List<String> searchByMicrosite(final Long microsite) throws DelegateException {
		try {
			return getFacade().searchByMicrosite(microsite);
		} catch (final RemoteException re) {
			throw new DelegateException(re);
		}
	}

	/**
	 * Busca PersonalizacionPlantilla por searchByMicrosite
	 */
	public List<PersonalizacionPlantilla> searchByMicrosite(final Long microsite, final String ordre,
			final Integer pagina, final Integer max) throws DelegateException {
		try {
			return getFacade().searchByMicrosite(microsite, ordre, pagina, max);
		} catch (final RemoteException re) {
			throw new DelegateException(re);
		}
	}

	/**
	 * Busca PersonalizacionPlantilla por searchByMicrosite
	 */
	public List<PersonalizacionPlantilla> searchByMicrositePlantilla(final long microsite, final String plantilla)
			throws DelegateException {
		try {
			return getFacade().searchByMicrositePlantilla(microsite, plantilla);
		} catch (final RemoteException re) {
			throw new DelegateException(re);
		}
	}

	/**
	 * Busca PersonalizacionPlantilla por searchByPlantilla
	 */
	public List<PersonalizacionPlantilla> searchByTema(final Long tema) throws DelegateException {
		try {
			return getFacade().searchByTema(tema);
		} catch (final RemoteException re) {
			throw new DelegateException(re);
		}
	}

	/**
	 * Eliminar una lista de plantillas
	 */
	public void borrarPersonalizacionPlantillas(final List<Long> ids) throws DelegateException {
		try {
			getFacade().borrarPersonalizacionPlantillas(ids);
		} catch (final RemoteException re) {
			throw new DelegateException(re);
		}
	}

	/**
	 * Contar PersonalizacionPlantilla por Microsite
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Long countByMicrosite(final Long microsite) throws DelegateException {
		try {
			return getFacade().countByMicrosite(microsite);
		} catch (final RemoteException re) {
			throw new DelegateException(re);
		}
	}
	/* ========================================================= */
	/* ======================== REFERENCIA AL FACADE ========== */
	/* ========================================================= */

	private Handle facadeHandle;

	private PersonalizacionPlantillaFacade getFacade() throws RemoteException {
		return (PersonalizacionPlantillaFacade) facadeHandle.getEJBObject();
	}

	protected PersonalizacionPlantillaDelegate() throws DelegateException {
		try {
			final PersonalizacionPlantillaFacadeHome home = PersonalizacionPlantillaFacadeUtil.getHome();
			final PersonalizacionPlantillaFacade remote = home.create();
			facadeHandle = remote.getHandle();
		} catch (final NamingException e) {
			throw new DelegateException(e);
		} catch (final CreateException e) {
			throw new DelegateException(e);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

}
