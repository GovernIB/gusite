package org.ibit.rol.sac.micropersistence.delegate;

import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.List;
import javax.ejb.CreateException;
import javax.ejb.Handle;
import javax.naming.NamingException;

import org.ibit.rol.sac.micromodel.Contacto;
import org.ibit.rol.sac.micromodel.Lineadatocontacto;
import org.ibit.rol.sac.micropersistence.util.ContactoFacadeUtil;
import org.ibit.rol.sac.micropersistence.intf.ContactoFacade;
import org.ibit.rol.sac.micropersistence.intf.ContactoFacadeHome;

/**
 * Business delegate para manipular Accesibilidad.
 * 
 * @author Indra
 */
public class ContactoDelegate implements StatelessDelegate {

    /* ========================================================= */
    /* ======================== M�TODOS DE NEGOCIO ============= */
    /* ========================================================= */

	private static final long serialVersionUID = -9212510036435820243L;

	public void init() throws DelegateException {
        try {
        	getFacade().init();
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
	
	/**
	 * Inicializo los par�metros de la consulta de Contacto.
	 * @param id Id del contacto
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
     * Crea o actualiza un Contacto
     * @param contacto
     * @return Id del contacto
     * @throws DelegateException
     */
    public Long grabarContacto(Contacto contacto) throws DelegateException {
        try {
            return getFacade().grabarContacto(contacto);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
    
    /**
     * Obtiene una linea del Formulario
     * @param id Id de la linea de contacto
     * @return Lineadatocontacto
     * @throws DelegateException
     */
    public Lineadatocontacto obtenerLinea(Long id) throws DelegateException {
        try {
            return getFacade().obtenerLinea(id);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
     
    /**
     * Obtiene un contacto
     * @param id Id del contato
     * @return Contacto
     * @throws DelegateException
     */
    public Contacto obtenerContacto(Long id) throws DelegateException {
        try {
            return getFacade().obtenerContacto(id);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
 
    /**
     * Lista todos los Formularios
     * @return una Lista de contactos
     * @throws DelegateException
     */
    public List<?> listarContactos() throws DelegateException {
        try {
            return getFacade().listarContactos();
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
    
    /**
     *  borra un contacto
     * @param id
     * @throws DelegateException
     */
    public void borrarContacto(Long id) throws DelegateException {
        try {
        	getFacade().borrarContacto(id);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
    
    /**
     * A�ade una nueva l�nea al formulario o modifica la que existe
     * @param lin
     * @param idcontacto Id del contacto
     * @throws DelegateException
     */
    public void creamodificaLinea(Lineadatocontacto lin, Long idcontacto) throws DelegateException {
        try {
        	getFacade().creamodificaLinea(lin, idcontacto);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
    
    /**
     * elimina l�neas del formulario
     * @param lineas
     * @param contacto_id Id del contacto
     * @throws DelegateException
     */
    public void eliminarLineas(String[] lineas, Long contacto_id) throws DelegateException {
        try {
        	getFacade().eliminarLineas(lineas, contacto_id);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }

    }
    
    /**
     * elimina una l�nea del formulario
     * @param idLinea  Id de la linea de contacto
     * @param idContacto Id del contacto
     * @throws DelegateException
     */
    public void eliminarLinea(Long idLinea, Long idContacto) throws DelegateException {
        try {
        	getFacade().eliminarLinea(idLinea, idContacto);
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
        * Comprueba que el elemento pertenece al Microsite
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

    private ContactoFacade getFacade() throws RemoteException {
        return (ContactoFacade) facadeHandle.getEJBObject();
    }

    protected ContactoDelegate() throws DelegateException {
        try {
        	ContactoFacadeHome home = ContactoFacadeUtil.getHome();
        	ContactoFacade remote = home.create();
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