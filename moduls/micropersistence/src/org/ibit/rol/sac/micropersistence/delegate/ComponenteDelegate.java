package org.ibit.rol.sac.micropersistence.delegate;

import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.List;
import javax.ejb.CreateException;
import javax.ejb.Handle;
import javax.naming.NamingException;


import org.ibit.rol.sac.micromodel.Componente;
import org.ibit.rol.sac.micropersistence.util.ComponenteFacadeUtil;
import org.ibit.rol.sac.micropersistence.intf.ComponenteFacade;
import org.ibit.rol.sac.micropersistence.intf.ComponenteFacadeHome;

/**
 * Business delegate para manipular Componentes.
 * 
 * @author Indra
 *
 */
public class ComponenteDelegate implements StatelessDelegate {

	private static final long serialVersionUID = 8327711002117555757L;

	/* ========================================================= */
    /* ======================== MÉTODOS DE NEGOCIO ============= */
    /* ========================================================= */

    /**
     *  Inicializo los parámetros de la consulta de Componente.
     * @throws DelegateException
     */
    public void init() throws DelegateException {
        try {
        	getFacade().init();
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }	
	
    /**
     *  Inicializo los parámetros de la consulta de Componente.
     * @param id del componente
     * @throws DelegateException
     */
    public void init(Long id) throws DelegateException {
        try {
        	getFacade().init(id);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }		
	
    /**
     * Crea o actualiza un Componente
     * @param compo
     * @return Id del componente
     * @throws DelegateException
     */
    public Long grabarComponente(Componente compo) throws DelegateException {
        try {
            return getFacade().grabarComponente(compo);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
    
    /**
     * Obtiene un Componente
     * @param id del componente
     * @return Componente
     * @throws DelegateException
     */
    public Componente obtenerComponente(Long id) throws DelegateException {
        try {
            return getFacade().obtenerComponente(id);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
 
    /**
     * Lista todos los Componentes
     * @return una lista de componentes
     * @throws DelegateException
     */
    public List<?> listarComponentes() throws DelegateException {
        try {
            return getFacade().listarComponentes();
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }

    /**
     *  borra un Componente
     * @param id del componente
     * @throws DelegateException
     */
    public void borrarComponente(Long id) throws DelegateException {
        try {
            getFacade().borrarComponente(id);
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

      /**
       *Comprueba que el componente pertenece al Microsite
       * @param site
       * @param id
       * @return true si no pertenece
       * @throws DelegateException
       */
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

    private ComponenteFacade getFacade() throws RemoteException {
        return (ComponenteFacade) facadeHandle.getEJBObject();
    }

    protected ComponenteDelegate() throws DelegateException {
        try {
        	ComponenteFacadeHome home = ComponenteFacadeUtil.getHome();
        	ComponenteFacade remote = home.create();
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