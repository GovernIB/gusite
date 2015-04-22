package es.caib.gusite.micropersistence.delegate;

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.Handle;
import javax.naming.NamingException;

import es.caib.gusite.micromodel.ArchivoTemaFront;
import es.caib.gusite.micromodel.PersonalizacionPlantilla;
import es.caib.gusite.micropersistence.intf.ArchivoTemaFrontFacade;
import es.caib.gusite.micropersistence.intf.ArchivoTemaFrontFacadeHome;
import es.caib.gusite.micropersistence.util.ArchivoTemaFrontFacadeUtil;

/**
 * Delegate para manipular ArchivoTemaFront.
 * 
 * @author at4.net
 * 
 */
public class ArchivoTemaFrontDelegate implements StatelessDelegate {

	private static final long serialVersionUID = -2076446869522196666L;

	/**
	 * Crea un ArchivoTemaFront
	 */
	public ArchivoTemaFront crearArchivoTemaFront(ArchivoTemaFront instance)
			throws DelegateException {
		try {
			return this.getFacade().crearArchivoTemaFront(instance);
		} catch (RemoteException re) {
			throw new DelegateException(re);
		}
	}

	/**
	 * Actualiza un ArchivoTemaFront
	 */
	public void actualizarArchivoTemaFront(ArchivoTemaFront instance)
			throws DelegateException {
		try {
			this.getFacade().actualizarArchivoTemaFront(instance);
		} catch (RemoteException re) {
			throw new DelegateException(re);
		}
	}

	/**
	 * Borra un ArchivoTemaFront
	 */
	public void borrarArchivoTemaFront(ArchivoTemaFront instance)
			throws DelegateException {
		try {
			this.getFacade().borrarArchivoTemaFront(instance);
		} catch (RemoteException re) {
			throw new DelegateException(re);
		}
	}

	/**
	 * Lista todos los ArchivoTemaFront
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	@SuppressWarnings("unchecked")
	public List<ArchivoTemaFront> listarArchivoTemaFront()
			throws DelegateException {
		try {
			return this.getFacade().listarArchivoTemaFront();
		} catch (RemoteException re) {
			throw new DelegateException(re);
		}
	}

	/**
	 * Obtiene un ArchivoTemaFront
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public ArchivoTemaFront obtenerArchivoTemaFront(java.lang.Long id)
			throws DelegateException {
		try {
			return this.getFacade().obtenerArchivoTemaFront(id);
		} catch (RemoteException re) {
			throw new DelegateException(re);
		}
	}

    /**
     * Borra un listado de PersonalizacionPlantilla por ids
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin}"
     */
    public void borrarArchivosTemaFront(List<Long> ids) throws DelegateException {
        try {
            this.getFacade().borrarArchivosTemaFront(ids);
        } catch (RemoteException re) {
            throw new DelegateException(re);
        }
    }

    /**
     * Busca Archivos por searchByPlantilla
     */
    public List<ArchivoTemaFront> searchByTema(Long tema) throws DelegateException {
        try {
            return getFacade().searchByTema(tema);
        }
        catch (RemoteException re) {
            throw new DelegateException(re);
        }
    }

    /**
     * Busca Archivos por searchByTemaNombre
     */
    public List<ArchivoTemaFront> searchByTemaNombre(String uriTema, String nombre) throws DelegateException {
        try {
            return getFacade().searchByTemaNombre(uriTema, nombre);
        }
        catch (RemoteException re) {
            throw new DelegateException(re);
        }
    }

	/* ========================================================= */
	/* ======================== REFERENCIA AL FACADE ========== */
	/* ========================================================= */

	private Handle facadeHandle;

	private ArchivoTemaFrontFacade getFacade() throws RemoteException {
		return (ArchivoTemaFrontFacade) this.facadeHandle.getEJBObject();
	}

	protected ArchivoTemaFrontDelegate() throws DelegateException {
		try {
			ArchivoTemaFrontFacadeHome home = ArchivoTemaFrontFacadeUtil
					.getHome();
			ArchivoTemaFrontFacade remote = home.create();
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
