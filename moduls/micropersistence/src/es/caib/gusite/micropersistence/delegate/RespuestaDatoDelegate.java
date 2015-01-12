package es.caib.gusite.micropersistence.delegate;

import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.List;
import java.util.ArrayList;

import javax.ejb.CreateException;
import javax.ejb.Handle;
import javax.naming.NamingException;


import es.caib.gusite.micromodel.RespuestaDato;
import es.caib.gusite.micropersistence.intf.RespuestaDatoFacade;
import es.caib.gusite.micropersistence.intf.RespuestaDatoFacadeHome;
import es.caib.gusite.micropersistence.util.RespuestaDatoFacadeUtil;

/**
 * Business delegate para manipular RespuestaDato.
 * @author Indra
 */
public class RespuestaDatoDelegate implements StatelessDelegate {


    /* ========================================================= */
    /* ======================== MÉTODOS DE NEGOCIO ============= */
    /* ========================================================= */

	private static final long serialVersionUID = -3572570976470092587L;

	/**
	 * Crea o actualiza una respuesta dato
	 * @param resdat
	 * @throws DelegateException
	 */
	public void grabarRespuestaDato(RespuestaDato resdat) throws DelegateException {
        try {
            getFacade().grabarRespuestaDato(resdat);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }

    
	 /**
	  * Lista todas las respuestas datos de una encuesta
	  * @param id Id de la respuesta
	  * @return
	  * @throws DelegateException
	  */
    public List<?> listarDatosByEnc(Long id) throws DelegateException {
        try {
            return getFacade().listarDatosByEnc(id);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
    
    /**
     *  Lista todas las respuestas datos de una pregunta
     * @param id Id de una pregunta
     * @return List
     * @throws DelegateException
     */
    public List<?> listarDatosByPreg(Long id) throws DelegateException {
        try {
            return getFacade().listarDatosByPreg(id);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
    
    /**
     * Lista todas las respuestas datos de una respuesta
     * @param id Id de una respuesta
     * @return una lista de respuestas
     * @throws DelegateException
     */
    public List<?> listarDatosByResp(Long id) throws DelegateException {
        try {
            return getFacade().listarDatosByResp(id);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }    

    /**
     * Lista todas las respuestas datos de una respuesta.
     * Devuelve un listado agrupado por datos, ordenado descendentemente por numero de votos.
     * Devuelve un hash donde la 'clave' es el dato y el 'valor' es el numero de repeticiones
     * @param id Id de una respuesta
     * @return ArrayList
     * @throws DelegateException
     */
    public ArrayList<?> listarDatosByResp2(Long id) throws DelegateException {
        try {
            return getFacade().listarDatosByResp2(id);
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

    private RespuestaDatoFacade getFacade() throws RemoteException {
        return (RespuestaDatoFacade) facadeHandle.getEJBObject();
    }

    protected RespuestaDatoDelegate() throws DelegateException {
        try {
        	RespuestaDatoFacadeHome home = RespuestaDatoFacadeUtil.getHome();
        	RespuestaDatoFacade remote = home.create();
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
