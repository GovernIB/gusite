package es.caib.gusite.micropersistence.delegate;

import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.Handle;
import javax.naming.NamingException;


import es.caib.gusite.micromodel.Correo;
import es.caib.gusite.micromodel.ListaDistribucion;
import es.caib.gusite.micropersistence.intf.ListaDistribucionFacade;
import es.caib.gusite.micropersistence.intf.ListaDistribucionFacadeHome;
import es.caib.gusite.micropersistence.util.ListaDistribucionFacadeUtil;

/** 
 * Business delegate para manipular listaDistribucion.
 * @author Salvador Antich
 */
public class LDistribucionDelegate implements StatelessDelegate {

	private static final long serialVersionUID = -1652208405910819789L;

	/* ========================================================= */
    /* ======================== MÉTODOS DE NEGOCIO ============= */
    /* ========================================================= */

    /**
     * Inicializo los parámetros de la consulta de una listaDistribucion.
     * @throws DelegateException
     */
    public void init(Long idConv, Long idMicro) throws DelegateException {
        try {
        	getFacade().init(idConv, idMicro);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }	
	
    /**
     * Inicializo los parámetros de la consulta de una listaDistribucion.
     * @param menu
     * @throws DelegateException
     */
    public void init(Long idMicro) throws DelegateException {
        try {
        	getFacade().init(idMicro);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }	
    
    /**
     * Crea o actualiza una listaDistribucion
     * @param convocatoria
     * @return Id de la listaDistribucion
     * @throws DelegateException
     */
    public Long grabarListaDistribucion(ListaDistribucion listaDistribucion) throws DelegateException {
        try {
            return getFacade().grabarListaDistribucion(listaDistribucion);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
    
    /**
     * Obtiene una listaDistribucion
     * @param id Id de la listaDistribucion
     * @return listaDistribucion
     * @throws DelegateException
     */
    public ListaDistribucion obtenerListaDistribucion(Long id) throws DelegateException {
        try {
            return getFacade().obtenerListaDistribucion(id);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
 
    /**
     * Existe listaDistribucion
     * @param id Id de la listaDistribucion
     * @return True si existe la listaDistribucion
     * @throws DelegateException
     */
    public boolean existeListaDistribucion (Long id) throws DelegateException {
        try {
            return getFacade().existeListaDistribucion(id);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
    
    /**
     * Añade un correo a una listaDistribucion
     * @return si el correo se ha andido a una lista de listaDistribucion
     * @throws DelegateException
     */
    public boolean anadeCorreo(Long id, String correo) throws DelegateException{
    	try {
            return getFacade().anadeCorreo(id, correo);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
    
    /**
     * Añade un correo en el sistema
     * @return si el correo se ha anadido al sistema
     * @throws DelegateException
     */
    public void anadeCorreo(Correo correo) throws DelegateException{
    	try {
            getFacade().anadeCorreo(correo);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
    
    /**
     * Añade un correo en el sistema
     * @return si el correo se ha anadido al sistema
     * @throws DelegateException
     */
    public void actualizaCorreo(Correo correo) throws DelegateException{
    	try {
            getFacade().actualizaCorreo(correo);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
    
    /**
     * Consulta un correo en el sistema
     * @return si el correo se ha anadido al sistema
     * @throws DelegateException
     */
    public Correo consultaCorreo(String correo) throws DelegateException{
    	try {
            return getFacade().consultaCorreo(correo);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
        
    /**
     * Borra un correo de una listaDistribucion
     * @return si el correo se ha borrado
     * @throws DelegateException
     */
    public boolean borrarCorreo(Long id, String correo) throws DelegateException{
    	try {
            return getFacade().borrarCorreo(id, correo);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
    
    /**
     * Lista todos los listaDistribucion
     * @return una lista de listaDistribucion
     * @throws DelegateException
     */
    public List<?> listarListaDistribucion() throws DelegateException {
        try {
            return getFacade().listarListaDistribucion();
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
    
    /**
     * Lista todas las listaDistribucion de un microsite<br/>
     * @param micro Identificador del microsite
     * @return List de beans <em>listaDistribucion</em>
     * @throws DelegateException
     */
    public List<?> listarAllListaDistribucion() throws DelegateException {
        try {
            return getFacade().listarAllListaDistribucion();
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }   
    
    /**
     * borra una listaDistribucion
     * @throws DelegateException
     */
    public void borrarListaDistribucion(Long id) throws DelegateException {
        try {
            getFacade().borrarListaDistribucion(id);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
    
    public StringBuffer exportarListaDistribucion(Long id) throws DelegateException {
    	try {
            return getFacade().exportarListaDistribucion(id);
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

    private ListaDistribucionFacade getFacade() throws RemoteException {
        return (ListaDistribucionFacade) facadeHandle.getEJBObject();
    }

    protected LDistribucionDelegate() throws DelegateException {
        try {
        	ListaDistribucionFacadeHome home = ListaDistribucionFacadeUtil.getHome();
        	ListaDistribucionFacade remote = home.create();
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



