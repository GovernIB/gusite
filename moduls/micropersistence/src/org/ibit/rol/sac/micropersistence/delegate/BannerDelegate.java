package org.ibit.rol.sac.micropersistence.delegate;



import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.Handle;
import javax.naming.NamingException;
import org.ibit.rol.sac.micromodel.Banner;
import org.ibit.rol.sac.micropersistence.util.BannerFacadeUtil;
import org.ibit.rol.sac.micropersistence.intf.BannerFacade;
import org.ibit.rol.sac.micropersistence.intf.BannerFacadeHome;

/**
 * Business delegate para manipular banner.
 * @author Indra
 */
public class BannerDelegate implements StatelessDelegate {

    /* ========================================================= */
    /* ======================== MÉTODOS DE NEGOCIO ============= */
    /* ========================================================= */

	private static final long serialVersionUID = 1666259506865177676L;

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
     * Obtiene un Banner
     * @param banner
     * @return Id del banner
     * @throws DelegateException
     */
    public Long grabarBanner(Banner banner) throws DelegateException {
        try {
            return getFacade().grabarBanner(banner);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
    
    /**
     *  Obtiene un Banner
     * @param id del banner
     * @return Banner
     * @throws DelegateException
     */
    public Banner obtenerBanner(Long id) throws DelegateException {
        try {
            return getFacade().obtenerBanner(id);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
 
    /**
     *  Lista todos los Banners
     * @return una Lista de banners
     * @throws DelegateException
     */
    public List<?> listarBanners() throws DelegateException {
        try {
            return getFacade().listarBanners();
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
    
    /**
     * Borra un Banner
     * @param id id del banner
     * @throws DelegateException
     */
    public void borrarBanner(Long id) throws DelegateException {
        try {
            getFacade().borrarBanner(id);
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

    private BannerFacade getFacade() throws RemoteException {
        return (BannerFacade) facadeHandle.getEJBObject();
    }

    protected BannerDelegate() throws DelegateException {
        try {
        	BannerFacadeHome home = BannerFacadeUtil.getHome();
        	BannerFacade remote = home.create();
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



