package es.caib.gusite.micropersistence.delegate;



import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.Handle;
import javax.naming.NamingException;

import es.caib.gusite.micromodel.Tiposervicio;
import es.caib.gusite.micropersistence.intf.TiposervicioFacade;
import es.caib.gusite.micropersistence.intf.TiposervicioFacadeHome;
import es.caib.gusite.micropersistence.util.TiposervicioFacadeUtil;

/**
 * Business delegate para manipular Tiposervicio.
 * @author Indra
 */
public class TiposervicioDelegate implements StatelessDelegate {

	private static final long serialVersionUID = -5876227131375842712L;

	/* ========================================================= */
    /* ======================== MÉTODOS DE NEGOCIO ============= */
    /* ========================================================= */
   
	/**
     * 
     * @param Crea o actualiza un Tiposervicio
     * @return Id de un tipo servicio
     * @throws DelegateException
     */
    public Long grabarTiposervicio(Tiposervicio tipo) throws DelegateException {
        try {
            return getFacade().grabarTiposervicio(tipo);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
    
    /**
     *  Obtiene un Tiposervicio
     * @param id Id de un tipo servicio
     * @return Tiposervicio
     * @throws DelegateException
     */
    public Tiposervicio obtenerTiposervicio(Long id) throws DelegateException {
        try {
            return getFacade().obtenerTiposervicio(id);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
 
    /**
     * Lista todos los Tiposervicio
     * @return una lista
     * @throws DelegateException
     */
    public List<?> listarTiposervicios() throws DelegateException {
        try {
            return getFacade().listarTiposervicios();
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
    
    /**
     * Lista todos los tipos para seleccionar los usados
     * @return una lista
     * @throws DelegateException
     */
    public List<?> listarTipos() throws DelegateException {
        try {
            return getFacade().listarTipos();
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }

    /**
     *  borra un Tiposervicio
     * @param id Id de un tipo servicio
     * @throws DelegateException
     */
    public void borrarTiposervicio(Long id) throws DelegateException {
        try {
            getFacade().borrarTiposervicio(id);
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

    /* ========================================================= */
    /* ======================== REFERENCIA AL FACADE  ========== */
    /* ========================================================= */

    private Handle facadeHandle;

    private TiposervicioFacade getFacade() throws RemoteException {
        return (TiposervicioFacade) facadeHandle.getEJBObject();
    }

    protected TiposervicioDelegate() throws DelegateException {
        try {
        	TiposervicioFacadeHome home = TiposervicioFacadeUtil.getHome();
        	TiposervicioFacade remote = home.create();
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



