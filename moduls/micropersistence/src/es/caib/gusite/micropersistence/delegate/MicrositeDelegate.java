package es.caib.gusite.micropersistence.delegate;

import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.Handle;
import javax.naming.NamingException;

import es.caib.gusite.lucene.model.ModelFilterObject;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.MicrositeCompleto;
import es.caib.gusite.micromodel.Usuario;
import es.caib.gusite.micromodel.UsuarioPropietarioMicrosite;
import es.caib.gusite.micropersistence.intf.MicrositeFacade;
import es.caib.gusite.micropersistence.intf.MicrositeFacadeHome;
import es.caib.gusite.micropersistence.util.MicrositeFacadeUtil;
import es.caib.rolsac.api.v2.exception.QueryServiceException;


/**
 * Business delegate para manipular Microsite.
 * @author Indra
 */
public class MicrositeDelegate implements StatelessDelegate {

    /* ========================================================= */
    /* ======================== MÉTODOS DE NEGOCIO ============= */
    /* ========================================================= */

	private static final long serialVersionUID = 3017269661850900982L;

	public Long grabarMicrosite(Microsite site) throws DelegateException {
        try {
            return getFacade().grabarMicrosite(site);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
    
    public Microsite obtenerMicrosite(Long id) throws DelegateException {
        try {
            return getFacade().obtenerMicrosite(id);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
    
    /**
     * Crea o actualiza UsuarioPropietarioMicrosite
     * @param upm
     * @return Id del Microsite
     * @throws DelegateException
     */
    public Long grabarUsuarioPropietarioMicrosite(UsuarioPropietarioMicrosite upm) throws DelegateException {
        try {
            return getFacade().grabarUsuarioPropietarioMicrosite(upm);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
    
    
    /**
     * Elimina borrarUsuarioPropietarioMicrosite
     * @param upm
     * @throws DelegateException
     */
    public void borrarUsuarioPropietarioMicrosite(UsuarioPropietarioMicrosite upm) throws DelegateException {
        try {
            getFacade().borrarUsuarioPropietarioMicrosite(upm);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
    
    
    /**
     * Obtiene un Microsite a partir de su clave de identificación.
     * @param key Clave única del microsite
     * @return Microsite
     * @throws DelegateException, en caso de cualquier error
     */
    public Microsite obtenerMicrositebyKey(String key) throws DelegateException {
        try {
            return getFacade().obtenerMicrositebyKey(key);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
    
    /**
     * Obtiene un Microsite a partir de su URI.
     * @param uri del microsite
     * @return Microsite
     * @throws DelegateException, en caso de cualquier error
     */
    public Microsite obtenerMicrositebyUri(String uri) throws DelegateException {
        try {
            return getFacade().obtenerMicrositebyUri(uri);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
 
    
    /**
     * Reemplaza un microsite basandose en la clave unica del microsite.
     * Aunque se reemplace el microsite, internamente se genera un nuevo microsite: por lo que el id será diferente, aunque
     * la clave única sí que es la misma.
     * @param site MicrositeCompleto a reemplazar
     * @return Id del nuevo microsite.
     * @throws DelegateException, en caso de cualquier error
     */
    public Long reemplazarMicrositeCompleto(MicrositeCompleto site) throws DelegateException {
        try {
            return getFacade().reemplazarMicrositeCompleto(site);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
    
    /**
     * Crea un Microsite Completo, durante importación
     * @param site
     * @return Id del Microsite
     * @throws DelegateException
     */
    public Long grabarMicrositeCompleto(MicrositeCompleto site) throws DelegateException {
        try {
            return getFacade().grabarMicrositeCompleto(site);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }

    /**
     * Obtiene un Microsite para la exportación
     * @param id Id del Microsite
     * @return MicrositeCompleto
     * @throws DelegateException
     */
    public MicrositeCompleto obtenerMicrositeCompleto(Long id) throws DelegateException {
        try {
            return getFacade().obtenerMicrositeCompleto(id);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
    
    /**
     * Obtiene un Microsite para la exportación
     * @param key Clave del Microsite
     * @return MicrositeCompleto
     * @throws DelegateException
     */
    public MicrositeCompleto obtenerMicrositeCompletobyKey(String key) throws DelegateException {
        try {
            return getFacade().obtenerMicrositeCompletobyKey(key);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }

    /**
     * Borra un microsite completamente.<br/>
     * Además de borrarlo, elimina la indexación del microsite y elimina también la relación entre usuarios y microsites.<br/>
     * 
     * @param id con el idmicrosite
     * @throws DelegateException
     */
    public void borrarMicrositeCompleto(Long id) throws DelegateException {
        try {
            getFacade().borrarMicrositeCompleto(id);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }

    /**
     *  Lista todos los Microsites
     * @return una lista de microsites
     * @throws DelegateException
     */
    public List<?> listarMicrosites() throws DelegateException {
        try {
            return getFacade().listarMicrosites();
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
    
    /**
     * Listado ligero de todos los Microsites. Solo rellena el idioma por defecto
     * @return listado de microsites
     * @throws DelegateException
     */
    public List<?> listarMicrositesThin() throws DelegateException {
        try {
            return getFacade().listarMicrositesThin();
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
     
    /**
     * Lista los microsites a los que tiene acceso un usuario.<br/>
     * El bean Microsite devuelto en el listado está casi vacio: sólo tiene el id, la ua, y la traducción por defecto.
     * 
     * @param id
     * @return lista de beans Microsite
     * @throws DelegateException
     */
	public List<?> listarMicrodeluser(String id) throws DelegateException {
        try {
            return getFacade().llistarMicrodeluser(id);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
	/**
     * Lista los microsites a los que tiene acceso un usuario.<br/>
     * El bean Microsite devuelto en el listado está casi vacio: sólo tiene el id, la ua, y la traducción por defecto.
     * 
     * @param usuario
     * @return lista de beans Microsite
     * @throws DelegateException
     */
	public List<?> listarMicrositesbyUser(Usuario usuario) throws DelegateException {
        try {
            return getFacade().listarMicrositesbyUser(usuario);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }	
	
    /**
     * Lista los usuarios asociados a un microsite.<br/>
     * Es un listado de Strings con el username.
     * 
     * @param idmicrosite
     * @return lista de Strings
     * @throws DelegateException
     */
	public List<?> listarUsernamesbyMicrosite(Long idmicrosite) throws DelegateException {
        try {
            return getFacade().listarUsernamesbyMicrosite(idmicrosite);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
	
	/**
	 * Lista todos los Microsites a los que el usuario puede acceder
	 * @param usu
	 * @param param
	 * @return una Lista
	 * @throws DelegateException
	 */
	public List<?> listarMicrositesFiltro(Usuario usu, Map<?, ?> param) throws DelegateException {
        try {
            return getFacade().listarMicrositesFiltro(usu, param);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
    
	/**
	 * borra un Microsite
	 * @param id Id del Microsite
	 * @throws DelegateException
	 */
    public void borrarMicrosite(Long id) throws DelegateException {
        try {
            getFacade().borrarMicrosite(id);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
    
    /**
     * Anyade un id de contenido al listado de los ultimos modificados en el microsite
     * @param site Id del Microsite
     * @param idcontenido Id del contenido
     * @throws DelegateException
     */
    public void grabarUltimoIdcontenido(Microsite site, Long idcontenido) throws DelegateException {
        try {
            getFacade().grabarUltimoIdcontenido(site, idcontenido);
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

   

   public ModelFilterObject obtenerFilterObject(Long idsite) throws DelegateException, QueryServiceException {
	   	try {
	           return getFacade().obtenerFilterObject(idsite);
	       } catch (RemoteException e) {
	           throw new DelegateException(e);
	       }
	   	
	   }   
   
   
   public String getUsuarioEJB() throws DelegateException {
	   	try {
	   		return getFacade().getUsuarioEJB();
	       } catch (RemoteException e) {
	           throw new DelegateException(e);
	       }
   }   
   
 
  
  
   
    /* ========================================================= */
    /* ======================== REFERENCIA AL FACADE  ========== */
    /* ========================================================= */

    private Handle facadeHandle;

    private MicrositeFacade getFacade() throws RemoteException {
        return (MicrositeFacade) facadeHandle.getEJBObject();
    }

    protected MicrositeDelegate() throws DelegateException {
        try {
        	MicrositeFacadeHome home = MicrositeFacadeUtil.getHome();
        	MicrositeFacade remote = home.create();
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



