package org.ibit.rol.sac.micropersistence.delegate;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.Handle;
import javax.naming.NamingException;

import org.ibit.lucene.indra.model.ModelFilterObject;
import org.ibit.rol.sac.micromodel.Faq;
import org.ibit.rol.sac.micropersistence.util.FaqFacadeUtil;
import org.ibit.rol.sac.micropersistence.intf.FaqFacade;
import org.ibit.rol.sac.micropersistence.intf.FaqFacadeHome;


/**
 * Business delegate para manipular faq.
 * @author Indra
 */
public class FaqDelegate implements StatelessDelegate {

	private static final long serialVersionUID = 3261818919990875910L;

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
     *  Crea o actualiza una faq
     * @param faq
     * @return Id de una faq
     * @throws DelegateException
     */
    public Long grabarFaq(Faq faq) throws DelegateException {
        try {
            return getFacade().grabarFaq(faq);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
    
    /**
     * Obtiene una faq
     * @param id Id de la faq
     * @return Faq
     * @throws DelegateException
     */
    public Faq obtenerFaq(Long id) throws DelegateException {
        try {
            return getFacade().obtenerFaq(id);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
 
    /**
     * Lista todas las faqs
     * @return una Lista
     * @throws DelegateException
     */
    public List<?> listarFaqs() throws DelegateException {
        try {
            return getFacade().listarFaqs();
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
    
    /**
     * Lista todas las faqs poniendole un idioma por defecto
     * @param idioma
     * @return ArrayList
     * @throws DelegateException
     */
    public ArrayList<?> listarFaqs(String idioma) throws DelegateException {
        try {
            return getFacade().listarFaqs(idioma);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }    
    
    /**
     * borra una faq
     * @param id Id de la faq
     * @throws DelegateException
     */
    public void borrarFaq(Long id) throws DelegateException {
        try {
            getFacade().borrarFaq(id);
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
   
   public void indexInsertaFaqs(Long idsite, ModelFilterObject filter) throws DelegateException {
	   	try {
	   		getFacade().indexInsertaFaqs(idsite, filter);
	   	} catch (RemoteException e) {
	   		throw new DelegateException(e);
	   	}
	}
	
   public void indexBorraFaqs(Long idsite) throws DelegateException {
	   	try {
	   		getFacade().indexBorraFaqs(idsite);
	   	} catch (RemoteException e) {
	   		throw new DelegateException(e);
	   	}
	}
	
    /* ========================================================= */
    /* ======================== REFERENCIA AL FACADE  ========== */
    /* ========================================================= */

    private Handle facadeHandle;

    private FaqFacade getFacade() throws RemoteException {
        return (FaqFacade) facadeHandle.getEJBObject();
    }

    protected FaqDelegate() throws DelegateException {
        try {
        	FaqFacadeHome home = FaqFacadeUtil.getHome();
        	FaqFacade remote = home.create();
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
