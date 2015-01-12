package org.ibit.rol.sac.micropersistence.delegate;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import javax.ejb.CreateException;
import javax.ejb.Handle;
import javax.naming.NamingException;

import org.ibit.rol.sac.micromodel.Accesibilidad;
import org.ibit.rol.sac.micropersistence.util.AccesibilidadFacadeUtil;
import org.ibit.rol.sac.micropersistence.intf.AccesibilidadFacade;
import org.ibit.rol.sac.micropersistence.intf.AccesibilidadFacadeHome;

/**
 * Business delegate para manipular Accesibilidad.
 * 
 *  @author Indra
 */
public class AccesibilidadDelegate implements StatelessDelegate {

    /* ========================================================= */
    /* ======================== MÉTODOS DE NEGOCIO ============= */
    /* ========================================================= */


	private static final long serialVersionUID = -7250453006609349151L;

	/**
	 * Crea o actualiza una accesibilidad
	 * @param accesibilidad
	 * @return Id Identificador de la entrada de accesibilidad
	 * @throws DelegateException
	 */
	public Long grabarAccesibilidad(Accesibilidad accesibilidad) throws DelegateException {
        try {
            return getFacade().grabarAccesibilidad(accesibilidad);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
	
	/**
	 * Método que borra una accesibilidad
	 * @param accesibilidad
	 * @throws DelegateException
	 */
    public void borrarAccesibilidad(Accesibilidad accesibilidad) throws DelegateException {
        try {
              getFacade().borrarAccesibilidad(accesibilidad);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
    
    /**
     * Obtiene una accesibilidad, en función de un identificador
     * @param id Id de la accesibilidad
     * @return una Accesibilidad
     * @throws DelegateException
     */  
    public Accesibilidad obtenerAccesibilidad(Long id) throws DelegateException {
        try {
            return getFacade().obtenerAccesibilidad(id);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
    
    /**
     * Determina si existe accesibilidad del microsite
     * @param iditem
     * @return 0 no existe accesibilidad 
     * @throws DelegateException
     */
    public int existeAccesibilidadMicrosite( Long iditem) throws DelegateException {
        try {
            return getFacade().existeAccesibilidadMicrosite(iditem);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
    
    /**
     * Determina si existe accesibilidad del item
     * @param iditem id del item
     * @return true si existe, false no existe
     * @throws DelegateException
     */
    public boolean existeAccesibilidadContenido( Long iditem) throws DelegateException {
        try {
            return getFacade().existeAccesibilidadContenido(iditem);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
    /**
     * Obtiene una accesibilidad
     * @param servicio  
     * @param iditem id del item
     * @param idioma
     * @return Accesibilidad
     * @throws DelegateException
     */
    public Accesibilidad obtenerAccesibilidad(String servicio, Long iditem, String idioma) throws DelegateException {
        try {
            return getFacade().obtenerAccesibilidad(servicio, iditem, idioma);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
    
    /**
     * Obtiene accesibilidad microsite
     * @param id del microsite
     * @return una lista con la accesibilidad
     * @throws DelegateException
     */
    public List<?> obtenerAccesibilidadMicro(Long id) throws DelegateException {
        try {
            return getFacade().obtenerAccesibilidadMicro(id);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
    /**
     * Obtiene toda accesibilidad del site
     * @param id id del item
     * @return List
     * @throws DelegateException
     */
    public List<?> obtenerAccesibilidadItem(Long id) throws DelegateException {
        try {
            return getFacade().obtenerAccesibilidadItem(id);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
    
    /**
     * Borra un registro de accesibilidad a partir de su identificador único interno.<br/>
     * @param id Id de una accesibilidad
     * @throws DelegateException
     */
    public void borrarAccesibilidad(Long id) throws DelegateException {
        try {
            getFacade().borrarAccesibilidad(id);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    } 
    
    /**
     * Borra los registros completos de accesibilidad de un microsite<br/>
     * @param id microsite
     * @throws DelegateException
     */
    public void borrarAccesibilidadMicro(Long id) throws DelegateException {
        try {
            getFacade().borrarAccesibilidadMicro(id);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
    

    /**
     * Devuelve un mapa con listados de diferentes elementos (contenidos, noticias, agendas)
     * @param idmicrosite id del microsite
     * @return HashMap
     * @throws DelegateException
     */
    public HashMap<?, ?> obtenerMapaListados(Long idmicrosite) throws DelegateException {
        try {
            return getFacade().obtenerMapaListados(idmicrosite);
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

    private AccesibilidadFacade getFacade() throws RemoteException {
        return (AccesibilidadFacade) facadeHandle.getEJBObject();
    }

    protected AccesibilidadDelegate() throws DelegateException {
        try {
        	AccesibilidadFacadeHome home = AccesibilidadFacadeUtil.getHome();
        	AccesibilidadFacade remote = home.create();
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