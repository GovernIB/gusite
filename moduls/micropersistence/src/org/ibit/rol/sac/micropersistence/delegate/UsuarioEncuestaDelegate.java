package org.ibit.rol.sac.micropersistence.delegate;

import org.ibit.rol.sac.micromodel.UsuarioEncuesta;
import org.ibit.rol.sac.micropersistence.intf.UsuarioEncuestaFacade;
import org.ibit.rol.sac.micropersistence.intf.UsuarioEncuestaFacadeHome;
import org.ibit.rol.sac.micropersistence.util.UsuarioEncuestaFacadeUtil;


import javax.ejb.CreateException;
import javax.ejb.Handle;
import javax.naming.NamingException;
import java.rmi.RemoteException;
import java.util.Hashtable;


/**
 * Business delegate para manipular usurios encuesta.
 * 
 * @author Indra 
 */
public class UsuarioEncuestaDelegate implements StatelessDelegate {

    /* ========================================================= */
    /* ======================== MÉTODOS DE NEGOCIO ============= */
    /* ========================================================= */

	private static final long serialVersionUID = 5352695479625106385L;

	/**
	 * Inicializo los parámetros de los usuarios encuesta.
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
	 * Crea o actualiza un usuario de una encuesta
	 * @param usuario
	 * @return Id de un usuario de una encuesta
	 * @throws DelegateException
	 */
    public long grabarUsuarioEncuesta(UsuarioEncuesta usuario) throws DelegateException {
        try {
            return getFacade().grabarUsuarioEncuesta(usuario);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }

    /**
     * Obtiene un usuario de una encuesta.
     * @param id Id de un usuario de una encuesta
     * @return UsuarioEncuesta
     * @throws DelegateException
     */
    public UsuarioEncuesta obtenerUsuarioEncuesta(Long id) throws DelegateException {
        try {
            return getFacade().obtenerUsuarioEncuesta(id);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }

    /**
     * Borra un usuario de una encuesta.
     * @param id Id de un usuario de una encuesta
     * @throws DelegateException
     */
    public void borrarUsuarioEncuesta(Long id) throws DelegateException {
        try {
            getFacade().borrarUsuarioEncuesta(id);
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

    private UsuarioEncuestaFacade getFacade() throws RemoteException {
        return (UsuarioEncuestaFacade) facadeHandle.getEJBObject();
    }

    protected UsuarioEncuestaDelegate() throws DelegateException {
        try {
            UsuarioEncuestaFacadeHome home = UsuarioEncuestaFacadeUtil.getHome();
            UsuarioEncuestaFacade remote = home.create();
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
