package es.caib.gusite.micropersistence.delegate;

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.Handle;
import javax.naming.NamingException;

import es.caib.gusite.micromodel.TemaFront;
import es.caib.gusite.micropersistence.intf.TemaFrontFacade;
import es.caib.gusite.micropersistence.intf.TemaFrontFacadeHome;
import es.caib.gusite.micropersistence.util.TemaFrontFacadeUtil;

/**
 * Delegate para manipular TemaFront.
 * 
 * @author at4.net
 * 
 */
public class TemaFrontDelegate implements StatelessDelegate {

	private static final long serialVersionUID = -2076446869522196666L;

	/**
	 * Crea un TemaFront
	 */
	public TemaFront crearTemaFront(TemaFront instance)
			throws DelegateException {
		try {
			return this.getFacade().crearTemaFront(instance);
		} catch (RemoteException re) {
			throw new DelegateException(re);
		}
	}

	/**
	 * Actualiza un TemaFront
	 */
	public void actualizarTemaFront(TemaFront instance)
			throws DelegateException {
		try {
			this.getFacade().actualizarTemaFront(instance);
		} catch (RemoteException re) {
			throw new DelegateException(re);
		}
	}

	/**
	 * Borra un TemaFront
	 */
	public void borrarTemaFront(TemaFront instance) throws DelegateException {
		try {
			this.getFacade().actualizarTemaFront(instance);
		} catch (RemoteException re) {
			throw new DelegateException(re);
		}
	}

	/**
	 * Lista todos los TemaFront
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	@SuppressWarnings("unchecked")
	public List<TemaFront> listarTemaFront() throws DelegateException {
		try {
			return this.getFacade().listarTemaFront();
		} catch (RemoteException re) {
			throw new DelegateException(re);
		}
	}

    /**
     * Lista todos los TemaFront ordenados
     *
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    @SuppressWarnings("unchecked")
    public List<TemaFront> listarTemaFrontOrden(String ordre) throws DelegateException {
        try {
            return this.getFacade().listarTemaFrontOrden(ordre);
        } catch (RemoteException re) {
            throw new DelegateException(re);
        }
    }

	/**
	 * Obtiene un TemaFront
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public TemaFront obtenerTemaFront(java.lang.Long id)
			throws DelegateException {
		try {
			return this.getFacade().obtenerTemaFront(id);
		} catch (RemoteException re) {
			throw new DelegateException(re);
		}
	}

    /**
     * Eliminar una lista de plantillas
     */
    public void borrarTemaFronts(List<Long> ids) throws DelegateException {
        try {
            getFacade().borrarTemaFronts(ids);
        } catch (RemoteException re) {
            throw new DelegateException(re);
        }
    }

    /**
     * Lista todos los TemaFront padres
     */
    public List<TemaFront> listarTemaFrontPadres() throws DelegateException {
        try {
            return getFacade().listarTemaFrontPadres();
        } catch (RemoteException re) {
            throw new DelegateException(re);
        }
    }

	/* ========================================================= */
	/* ======================== REFERENCIA AL FACADE ========== */
	/* ========================================================= */

	private Handle facadeHandle;

	private TemaFrontFacade getFacade() throws RemoteException {
		return (TemaFrontFacade) this.facadeHandle.getEJBObject();
	}

	protected TemaFrontDelegate() throws DelegateException {
		try {
			TemaFrontFacadeHome home = TemaFrontFacadeUtil.getHome();
			TemaFrontFacade remote = home.create();
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
