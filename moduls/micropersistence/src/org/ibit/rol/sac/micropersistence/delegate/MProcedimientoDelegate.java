package org.ibit.rol.sac.micropersistence.delegate;

import java.rmi.RemoteException;
import java.util.Hashtable;

import javax.ejb.CreateException;
import javax.ejb.Handle;
import javax.naming.NamingException;

import org.ibit.rol.sac.micromodel.MProcedimiento;
import org.ibit.rol.sac.micropersistence.util.MProcedimientoFacadeUtil;
import org.ibit.rol.sac.micropersistence.intf.MProcedimientoFacade;
import org.ibit.rol.sac.micropersistence.intf.MProcedimientoFacadeHome;

/**
 * Business delegate para manipular Procediminetos de un microsite.
 * @author Indra
 */
public class MProcedimientoDelegate implements StatelessDelegate {

    /* ========================================================= */
    /* ======================== MÉTODOS DE NEGOCIO ============= */
    /* ========================================================= */

	private static final long serialVersionUID = -7121742644565931796L;

	/**
	 * Crea o actualiza un MProcedimiento
	 * @param mproc
	 * @return identificador
	 * @throws DelegateException
	 */
	public Long grabarMProcedimiento(MProcedimiento mproc) throws DelegateException {
        try {
            return getFacade().grabarMProcedimiento(mproc);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
    
	/**
	 * Obtiene un MProcedimiento
	 * @param id
	 * @return MProcedimiento
	 * @throws DelegateException
	 */
    public MProcedimiento obtenerMProcedimiento(Long id) throws DelegateException {
        try {
            return getFacade().obtenerMProcedimiento(id);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
 
    /**
     * Obtiene un MProcedimiento de un microsite
     * @param idmicrosite
     * @return MProcedimiento
     * @throws DelegateException
     */
    public MProcedimiento obtenerMProcedimientobyMic(Long idmicrosite) throws DelegateException {
        try {
            return getFacade().obtenerMProcedimientobyMic(idmicrosite);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }  
    
    /**
     *  borra un tipo de noticia
     * @param id
     * @throws DelegateException
     */
    public void borrarMProcedimiento(Long id) throws DelegateException {
        try {
        	getFacade().borrarMProcedimiento(id);
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

       public void setOrderby2(String orderby) throws DelegateException {
    	   	try {
    	   		getFacade().setOrderby2(orderby);
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
       
       public String getWhere() throws DelegateException {
    	   	try {
    	   		return getFacade().getWhere();
    	       } catch (RemoteException e) {
    	           throw new DelegateException(e);
    	       }
    	   }
    	   
       public void setWhere(String valor) throws DelegateException {
    	   	try {
    	   		getFacade().setWhere(valor);
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
    
       
    /* ========================================================= */
    /* ======================== REFERENCIA AL FACADE  ========== */
    /* ========================================================= */

    private Handle facadeHandle;

    private MProcedimientoFacade getFacade() throws RemoteException {
        return (MProcedimientoFacade) facadeHandle.getEJBObject();
    }

    protected MProcedimientoDelegate() throws DelegateException {
        try {
        	MProcedimientoFacadeHome home = MProcedimientoFacadeUtil.getHome();
        	MProcedimientoFacade remote = home.create();
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