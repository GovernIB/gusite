package es.caib.gusite.micropersistence.delegate;

import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.Handle;
import javax.naming.NamingException;


import es.caib.gusite.micromodel.Tipo;
import es.caib.gusite.micropersistence.intf.TipoFacade;
import es.caib.gusite.micropersistence.intf.TipoFacadeHome;
import es.caib.gusite.micropersistence.util.TipoFacadeUtil;

/**
 * Business delegate para manipular Tipo.
 * @author Indra
 */
public class TipoDelegate implements StatelessDelegate {

	private static final long serialVersionUID = 2074125506901467457L;

	/* ========================================================= */
    /* ======================== MÉTODOS DE NEGOCIO ============= */
    /* ========================================================= */

    /**
     * Inicializo los parámetros de la consulta.
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
     * Inicializo los parámetros de la consulta.
     * @param id Id de un tipo
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
     * Inicializo los parámetros de la consulta.
     * @param id Id de un tipo
     * @param idiomapasado
     * @throws DelegateException
     */
    public void init(Long id,String idiomapasado) throws DelegateException {
        try {
        	getFacade().init(id,idiomapasado);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
    
    /**
     * Crea o actualiza un tipo de noticia
     * @param tp
     * @return Id de un tipo
     * @throws DelegateException
     */
    public Long grabarTipo(Tipo tp) throws DelegateException {
        try {
            return getFacade().grabarTipo(tp);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
    
    /**
     *  Obtiene un tipo de noticia
     * @param id Id de un tipo
     * @return Tipo
     * @throws DelegateException
     */
    public Tipo obtenerTipo(Long id) throws DelegateException {
        try {
            return getFacade().obtenerTipo(id);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
 
    /**
     * Obtiene los valores del dominio
     * @param id Id de un tipo
     * @param parametros
     * @return Map
     * @throws DelegateException
     */
    public Map<?, ?> obtenerListado(Long id, Map<?, ?> parametros) throws DelegateException {
        try {
            return getFacade().obtenerListado(id,parametros);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }

    /**
     *  Obtiene los valores del dominio
     * @param id Id de tipo
     * @param parametros
     * @return String
     * @throws DelegateException
     */
    public String obtenerPegoteHTMLExterno(Long id, Map<?, ?> parametros) throws DelegateException {
        try {
            return getFacade().obtenerPegoteHTMLExterno(id,parametros);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
    
    /**
     * Lista todos los tipos  
     * @return una lista de tipos
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
     * Lista todos los tipos  
     * @param idiomapasado
     * @return una lista de tipos
     * @throws DelegateException
     */
    public List<?> listarTiposrec(String idiomapasado) throws DelegateException {
        try {
            return getFacade().listarTiposrec(idiomapasado);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
    
    /**
     * borra un tipo de noticia
     * @param id Id de un tipo
     * @throws DelegateException
     */
    public void borrarTipo(Long id) throws DelegateException {
        try {
        	getFacade().borrarTipo(id);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }

    /**
     * Establece el filtro del tipo. 
     * Si es true devolverá sólo los externos. 
     * Si es false, devolverá todos menos los externos.
     * 
     * @param externos boolean
     * @throws DelegateException
     */
    public void setFiltroExterno(boolean externos) throws DelegateException {
	   	try {
	   		getFacade().setFiltroExterno(externos);
	       } catch (RemoteException e) {
	           throw new DelegateException(e);
	       }
	}
    
    /**
     * Lista todas las distintas clasificaciones de un tipo de noticias
     * @param idmicrosite Id de un Microsite
     * @return una lista
     * @throws DelegateException
     */
    public List<?> comboClasificacion(Long idmicrosite) throws DelegateException {
   	 try {
   		 return getFacade().comboClasificacion(idmicrosite);
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

    private TipoFacade getFacade() throws RemoteException {
        return (TipoFacade) facadeHandle.getEJBObject();
    }

    protected TipoDelegate() throws DelegateException {
        try {
        	TipoFacadeHome home = TipoFacadeUtil.getHome();
        	TipoFacade remote = home.create();
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