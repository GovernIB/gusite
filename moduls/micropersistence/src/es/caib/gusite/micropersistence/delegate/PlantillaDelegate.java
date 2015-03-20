package es.caib.gusite.micropersistence.delegate;

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.Handle;
import javax.naming.NamingException;

import es.caib.gusite.micromodel.Plantilla;
import es.caib.gusite.micropersistence.intf.PlantillaFacade;
import es.caib.gusite.micropersistence.intf.PlantillaFacadeHome;
import es.caib.gusite.micropersistence.util.PlantillaFacadeUtil;

/**
 * Delegate para manipular Plantilla.
 * 
 * @author at4.net
 * 
 */
public class PlantillaDelegate implements StatelessDelegate {

	private static final long serialVersionUID = -2076446869522196666L;

	/**
	 * Crea un Plantilla
	 */
	public Plantilla crearPlantilla(Plantilla instance)
			throws DelegateException {
		try {
			return this.getFacade().crearPlantilla(instance);
		} catch (RemoteException re) {
			throw new DelegateException(re);
		}
	}

	/**
	 * Actualiza un Plantilla
	 */
	public void actualizarPlantilla(Plantilla instance)
			throws DelegateException {
		try {
			this.getFacade().actualizarPlantilla(instance);
		} catch (RemoteException re) {
			throw new DelegateException(re);
		}
	}

	/**
	 * Borra un Plantilla
	 */
	public void borrarPlantilla(Plantilla instance) throws DelegateException {
		try {
			this.getFacade().actualizarPlantilla(instance);
		} catch (RemoteException re) {
			throw new DelegateException(re);
		}
	}

	/**
	 * Lista todos los Plantilla
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	@SuppressWarnings("unchecked")
	public List<Plantilla> listarPlantilla() throws DelegateException {
		try {
			return this.getFacade().listarPlantilla();
		} catch (RemoteException re) {
			throw new DelegateException(re);
		}
	}

	/**
	 * Obtiene un Plantilla
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Plantilla obtenerPlantilla(java.lang.Long id)
			throws DelegateException {
		try {
			return this.getFacade().obtenerPlantilla(id);
		} catch (RemoteException re) {
			throw new DelegateException(re);
		}
	}

    /**
     * Obtiene un Plantilla por nombre
     *
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public Plantilla obtenerPlantillaPorNombre(String nombre) throws DelegateException {
        try {
            return this.getFacade().obtenerPlantillaPorNombre(nombre);
        } catch (RemoteException re) {
            throw new DelegateException(re);
        }
    }

	/* ========================================================= */
	/* ======================== REFERENCIA AL FACADE ========== */
	/* ========================================================= */

	private Handle facadeHandle;

	private PlantillaFacade getFacade() throws RemoteException {
		return (PlantillaFacade) this.facadeHandle.getEJBObject();
	}

	protected PlantillaDelegate() throws DelegateException {
		try {
			PlantillaFacadeHome home = PlantillaFacadeUtil.getHome();
			PlantillaFacade remote = home.create();
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
