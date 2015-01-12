package org.ibit.rol.sac.micropersistence.delegate;

import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.Handle;
import javax.naming.NamingException;


import org.ibit.rol.sac.micromodel.Actividadagenda;
import org.ibit.rol.sac.micropersistence.util.ActividadFacadeUtil;

import org.ibit.rol.sac.micropersistence.intf.ActividadFacade;
import org.ibit.rol.sac.micropersistence.intf.ActividadFacadeHome;

/**
 * Business delegate para manipular Actividades.
 * 
 * @author Indra
 */
public class ActividadDelegate implements StatelessDelegate {

	private static final long serialVersionUID = 3120776484924556049L;

	/* ========================================================= */
    /* ======================== MÉTODOS DE NEGOCIO ============= */
    /* ========================================================= */

    public void init() throws DelegateException {
        try {
        	getFacade().init();
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }	
	
    public void init(Long id) throws DelegateException {
        try {
        	getFacade().init(id);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
	
    /**
     * Crea o actualiza una actividad
     * @param activi
     * @return Identificador
     * @throws DelegateException
     */
    public Long grabarActividad(Actividadagenda activi) throws DelegateException {
        try {
            return getFacade().grabarActividad(activi);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
    
    /**
     * Obtiene una Actividad
     * @param id Id de la actividad
     * @return Actividadagenda
     * @throws DelegateException
     */
    public Actividadagenda obtenerActividad(Long id) throws DelegateException {
        try {
            return getFacade().obtenerActividad(id);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
 
    /**
     * Lista todas las actividades
     * @return una lista de actividades
     * @throws DelegateException
     */
    public List<?> listarActividades() throws DelegateException {
        try {
            return getFacade().listarActividades();
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
    
    /**
     * Borra una actividad
     * @param id Id de la actividad
     * @throws DelegateException
     */
    public void borrarActividad(Long id) throws DelegateException {
        try {
        	getFacade().borrarActividad(id);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
    
    public Hashtable<?, ?> getParametros() throws DelegateException {
    	 try {
    		 return getFacade().getParametros();
         } catch (RemoteException e) {
             throw new DelegateException(e);
         }
    }

    public void parametrosCons() throws DelegateException {
    	try {
    		getFacade().parametrosCons();
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
    
    public int getPagina() throws DelegateException {
    	try {
    		return getFacade().getPagina();
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }

    public void setPagina(int pagina) throws DelegateException {
    	try {
    		getFacade().setPagina(pagina);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }

    public int getTampagina() throws DelegateException {
   	   	try {
   	   		return getFacade().getTampagina();
   	       } catch (RemoteException e) {
   	           throw new DelegateException(e);
   	       }
   	   }
   	   
      public void setTampagina(int tampagina) throws DelegateException {
   	   	try {
   	   		getFacade().setTampagina(tampagina);
   	       } catch (RemoteException e) {
   	           throw new DelegateException(e);
   	       }
      }     
    
    public void setOrderby(String orderby) throws DelegateException {
    	try {
    		getFacade().setOrderby(orderby);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }

    public String getValorBD(String valor) throws DelegateException {
    	try {
    		return getFacade().getValorBD(valor);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
    
    public void setFiltro(String valor) throws DelegateException {
    	try {
    		getFacade().setFiltro(valor);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }

    public List<?> listarCombo(Long idmicrosite) throws DelegateException {
    	try {
    		return getFacade().listarCombo(idmicrosite);
    	} catch (RemoteException e) {
    		throw new DelegateException(e);
    	}
    }
    
    
    
    public boolean checkSite(Long site, Long id) throws DelegateException {
    	try {
    		return getFacade().checkSite(site,id);
    	} catch (RemoteException e) {
    		throw new DelegateException(e);
    	}
    }
    /* ========================================================= */
    /* ======================== REFERENCIA AL FACADE  ========== */
    /* ========================================================= */

    private Handle facadeHandle;

    private ActividadFacade getFacade() throws RemoteException {
        return (ActividadFacade) facadeHandle.getEJBObject();
    }

    protected ActividadDelegate() throws DelegateException {
        try {
        	ActividadFacadeHome home = ActividadFacadeUtil.getHome();
        	ActividadFacade remote = home.create();
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