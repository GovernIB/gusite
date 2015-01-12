package org.ibit.rol.sac.micropersistence.delegate;

import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.Handle;
import javax.naming.NamingException;

import org.ibit.rol.sac.micromodel.Frqssi;
import org.ibit.rol.sac.micropersistence.util.FrqssiFacadeUtil;
import org.ibit.rol.sac.micropersistence.intf.FrqssiFacade;
import org.ibit.rol.sac.micropersistence.intf.FrqssiFacadeHome;

/**
 * Business delegate para manipular Frqssi.
 * @author Indra
 */
public class FrqssiDelegate implements StatelessDelegate {

	private static final long serialVersionUID = -2506858131994160733L;

	/* ========================================================= */
    /* ======================== M�TODOS DE NEGOCIO ============= */
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
    public void init(Long id,String idiomapasado) throws DelegateException {
        try {
        	getFacade().init(id,idiomapasado);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
	
    /**
     * Crea o actualiza un Formulario QSSI
     * @param tp
     * @return Id del Frqssi
     * @throws DelegateException
     */
    public Long grabarFrqssi(Frqssi tp) throws DelegateException {
        try {
            return getFacade().grabarFrqssi(tp);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
    
    /**
     * Obtiene un Formulario QSSI
     * @param id Id del Frqssi
     * @return Frqssi
     * @throws DelegateException
     */
    public Frqssi obtenerFrqssi(Long id) throws DelegateException {
        try {
            return getFacade().obtenerFrqssi(id);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
 
    /**
     * Lista todos los Formularios QSSI
     * @return una Lista de Frqssis
     * @throws DelegateException
     */
    public List<?> listarFrqssis() throws DelegateException {
        try {
            return getFacade().listarFrqssis();
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
    
    /**
     * Lista todos los Formularios QSSI
     * @param idiomapasado
     * @return una lista de Frqssisrec
     * @throws DelegateException
     */
    public List<?> listarFrqssisrec(String idiomapasado) throws DelegateException {
        try {
            return getFacade().listarFrqssisrec(idiomapasado);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
    
    /**
     *  Borra un Formulario QSSI
     * @param id
     * @throws DelegateException
     */
    public void borrarFrqssi(Long id) throws DelegateException {
        try {
        	getFacade().borrarFrqssi(id);
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

    private FrqssiFacade getFacade() throws RemoteException {
        return (FrqssiFacade) facadeHandle.getEJBObject();
    }

    protected FrqssiDelegate() throws DelegateException {
        try {
        	FrqssiFacadeHome home = FrqssiFacadeUtil.getHome();
        	FrqssiFacade remote = home.create();
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