package es.caib.gusite.micropersistence.delegate;

import java.util.List;

import es.caib.gusite.micromodel.PersonalizacionPlantilla;
import java.rmi.RemoteException;
import javax.ejb.CreateException;
import javax.ejb.Handle;
import javax.naming.NamingException;
import es.caib.gusite.micropersistence.intf.PersonalizacionPlantillaFacade;
import es.caib.gusite.micropersistence.intf.PersonalizacionPlantillaFacadeHome;
import es.caib.gusite.micropersistence.util.PersonalizacionPlantillaFacadeUtil;

/**
 * Delegate para manipular PersonalizacionPlantilla.
 * @author at4.net
 *
 */
public class PersonalizacionPlantillaDelegate implements StatelessDelegate{

	private static final long serialVersionUID = -2076446869522196666L;
	
    /**
     * Crea un PersonalizacionPlantilla
     */
    public PersonalizacionPlantilla crearPersonalizacionPlantilla(PersonalizacionPlantilla instance) throws DelegateException {
        try {
        	return getFacade().crearPersonalizacionPlantilla(instance);
        }
        catch (RemoteException re) {
			throw new DelegateException(re);
		}
    }

    /**
     * Actualiza un PersonalizacionPlantilla
     */
    public void actualizarPersonalizacionPlantilla(PersonalizacionPlantilla instance) throws DelegateException {
        try {
        	getFacade().actualizarPersonalizacionPlantilla(instance);
        }
        catch (RemoteException re) {
			throw new DelegateException(re);
		}
    }
    
    /**
     * Borra un PersonalizacionPlantilla
     */
    public void borrarPersonalizacionPlantilla(PersonalizacionPlantilla instance) throws DelegateException {
        try {
        	getFacade().borrarPersonalizacionPlantilla(instance);
        }
        catch (RemoteException re) {
			throw new DelegateException(re);
		}
    }

    /**
     * Lista todos los PersonalizacionPlantilla
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
    @SuppressWarnings("unchecked")
    public List<PersonalizacionPlantilla> listarPersonalizacionPlantilla() throws DelegateException {
        try {
        	return getFacade().listarPersonalizacionPlantilla();
        }
        catch (RemoteException re) {
			throw new DelegateException(re);
		}
    }
    
    /**
     * Obtiene un PersonalizacionPlantilla
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public PersonalizacionPlantilla obtenerPersonalizacionPlantilla( java.lang.Long id) throws DelegateException {
        try {
        	return getFacade().obtenerPersonalizacionPlantilla(id);
        }
        catch (RemoteException re) {
			throw new DelegateException(re);
		}
    }
    
    /**
     * Busca PersonalizacionPlantilla por searchByMicrosite
     */
    public List<PersonalizacionPlantilla> searchByMicrosite(long microsite) throws DelegateException {
        try {
        	return getFacade().searchByMicrosite(
		        microsite
        	);
        }
        catch (RemoteException re) {
			throw new DelegateException(re);
		}
    }

    /**
     * Busca PersonalizacionPlantilla por searchByMicrosite
     */
    public List<PersonalizacionPlantilla> searchByMicrosite(Long microsite, String ordre, Integer pagina, Integer max) throws DelegateException {
        try {
            return getFacade().searchByMicrosite(microsite, ordre, pagina, max);
        }
        catch (RemoteException re) {
            throw new DelegateException(re);
        }
    }

    /**
     * Busca PersonalizacionPlantilla por searchByMicrosite
     */
    public List<PersonalizacionPlantilla> searchByMicrositePlantilla(long microsite, String plantilla) throws DelegateException {
        try {
        	return getFacade().searchByMicrositePlantilla(
		        microsite, plantilla
        	);
        }
        catch (RemoteException re) {
			throw new DelegateException(re);
		}
    }

    /**
     * Busca PersonalizacionPlantilla por searchByPlantilla
     */
    public List<PersonalizacionPlantilla> searchByTema(Long tema) throws DelegateException {
        try {
            return getFacade().searchByTema(tema);
        }
        catch (RemoteException re) {
            throw new DelegateException(re);
        }
    }

    /**
     * Eliminar una lista de plantillas
     */
    public void borrarPersonalizacionPlantillas(List<Long> ids) throws DelegateException {
        try {
            getFacade().borrarPersonalizacionPlantillas(ids);
        } catch (RemoteException re) {
            throw new DelegateException(re);
        }
    }

    /**
     * Contar PersonalizacionPlantilla por Microsite
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public Long countByMicrosite(Long microsite) throws DelegateException {
        try {
            return getFacade().countByMicrosite(microsite);
        } catch (RemoteException re) {
            throw new DelegateException(re);
        }
    }
    /* ========================================================= */
    /* ======================== REFERENCIA AL FACADE  ========== */
    /* ========================================================= */

    private Handle facadeHandle;

    private PersonalizacionPlantillaFacade getFacade() throws RemoteException {
        return (PersonalizacionPlantillaFacade) facadeHandle.getEJBObject();
    }

    protected PersonalizacionPlantillaDelegate() throws DelegateException {
        try {
        	PersonalizacionPlantillaFacadeHome home = PersonalizacionPlantillaFacadeUtil.getHome();
        	PersonalizacionPlantillaFacade remote = home.create();
            facadeHandle = remote.getHandle();
        } catch (NamingException e) {
            throw new DelegateException(e);
        } catch (CreateException e) {
            throw new DelegateException(e);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }

}

