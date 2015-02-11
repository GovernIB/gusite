package es.caib.gusite.micropersistence.delegate;

import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.Handle;
import javax.naming.NamingException;

import es.caib.gusite.lucene.model.ModelFilterObject;
import es.caib.gusite.micromodel.Contenido;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.Noticia;
import es.caib.gusite.micropersistence.intf.NoticiaFacade;
import es.caib.gusite.micropersistence.intf.NoticiaFacadeHome;
import es.caib.gusite.micropersistence.util.NoticiaFacadeUtil;

/**
 * Business delegate para manipular Noticias.
 * @author Indra
 */
public class NoticiaDelegate implements StatelessDelegate, NoticiaServiceItf {

	private static final long serialVersionUID = -1342790045052841948L;

	/* ========================================================= */
    /* ======================== MÉTODOS DE NEGOCIO ============= */
    /* ========================================================= */

	/* (non-Javadoc)
	 * @see es.caib.gusite.micropersistence.delegate.NotificaServiceItf#init()
	 */
    public void init() throws DelegateException {
        try {
        	getFacade().init();
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }	
	
    /* (non-Javadoc)
	 * @see es.caib.gusite.micropersistence.delegate.NotificaServiceItf#init(java.lang.Long)
	 */
    public void init(Long id) throws DelegateException {
        try {
        	getFacade().init(id);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
	
    /* (non-Javadoc)
	 * @see es.caib.gusite.micropersistence.delegate.NotificaServiceItf#grabarNoticia(es.caib.gusite.micromodel.Noticia)
	 */
    public Long grabarNoticia(Noticia noticia) throws DelegateException {
        try {
            return getFacade().grabarNoticia(noticia);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
    
    /* (non-Javadoc)
	 * @see es.caib.gusite.micropersistence.delegate.NotificaServiceItf#obtenerNoticia(java.lang.Long)
	 */
    public Noticia obtenerNoticia(Long id) throws DelegateException {
        try {
            return getFacade().obtenerNoticia(id);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
    /* (non-Javadoc)
   	 * @see es.caib.gusite.micropersistence.delegate.NotificaServiceItf#obtenerNoticiaDesdeUri(java.lang.Idioma, java.lang.String)
   	 */
       public Noticia obtenerNoticiaDesdeUri(String lang, String uri) throws DelegateException {
           try {
               return getFacade().obtenerNoticiaDesdeUri(lang, uri);
           } catch (RemoteException e) {
               throw new DelegateException(e);
           }
       }       

   

    /* (non-Javadoc)
	 * @see es.caib.gusite.micropersistence.delegate.NotificaServiceItf#clonarNoticia(java.lang.Long)
	 */
    public Long clonarNoticia(Long id) throws DelegateException {
        try {
            return getFacade().clonarNoticia(id);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }    
    
    /* (non-Javadoc)
	 * @see es.caib.gusite.micropersistence.delegate.NotificaServiceItf#obtenerNoticiaThin(java.lang.Long)
	 */
    public Noticia obtenerNoticiaThin(Long id, String idioma) throws DelegateException {
        try {
            return getFacade().obtenerNoticiaThin(id, idioma);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }   
    
    /* (non-Javadoc)
	 * @see es.caib.gusite.micropersistence.delegate.NotificaServiceItf#listarNoticias()
	 */
    public List<?> listarNoticias() throws DelegateException {
        try {
            return getFacade().listarNoticias();
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
    
    /* (non-Javadoc)
	 * @see es.caib.gusite.micropersistence.delegate.NotificaServiceItf#listarNoticiasThin()
	 */
    public List<Noticia> listarNoticiasThin(String idioma) throws DelegateException {
        try {
            return getFacade().listarNoticiasThin(idioma);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }  
    
    @Deprecated
    public List<?> buscarElementos(Map<?, ?> parametros, Map<?, ?> traduccion,
    		String idmicrosite, String idtipo, String idioma) throws Exception {
        try {
            return getFacade().buscarElementos(parametros,traduccion,idmicrosite,idtipo,idioma);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }

    }
    
    /* (non-Javadoc)
	 * @see es.caib.gusite.micropersistence.delegate.NotificaServiceItf#buscarElementos(java.util.Map, java.util.Map, java.lang.String, java.lang.String, java.lang.String)
	 */
    public List<?> buscarElementos(BuscarElementosParameter parameterObject) throws DelegateException {
        try {
            return getFacade().buscarElementos(parameterObject);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }    
    
    /* (non-Javadoc)
	 * @see es.caib.gusite.micropersistence.delegate.NotificaServiceItf#buscarElementosLuc(java.lang.String, java.lang.String, java.lang.String, java.lang.String, boolean)
	 */
    public List<?> buscarElementosLuc(String micro, String idi, String idlista, String cadena, boolean sugerir) throws DelegateException {
        try {
            return getFacade().buscarElementosLuc(micro, idi, idlista, cadena, sugerir);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }     
    
    
    /* (non-Javadoc)
	 * @see es.caib.gusite.micropersistence.delegate.NotificaServiceItf#borrarNoticia(java.lang.Long)
	 */
    public void borrarNoticia(Long id) throws DelegateException {
        try {
        	getFacade().borrarNoticia(id);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }

    /* (non-Javadoc)
	 * @see es.caib.gusite.micropersistence.delegate.NotificaServiceItf#getParametros()
	 */
    public Hashtable<?, ?> getParametros() throws DelegateException {
    	 try {
    		 return getFacade().getParametros();
         } catch (RemoteException e) {
             throw new DelegateException(e);
         }
    }

    /* (non-Javadoc)
	 * @see es.caib.gusite.micropersistence.delegate.NotificaServiceItf#parametrosCons()
	 */
    public void parametrosCons() throws DelegateException {
    	try {
    		getFacade().parametrosCons();
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
    
    /* (non-Javadoc)
	 * @see es.caib.gusite.micropersistence.delegate.NotificaServiceItf#getPagina()
	 */
    public int getPagina() throws DelegateException {
    	try {
    		return getFacade().getPagina();
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }

    /* (non-Javadoc)
	 * @see es.caib.gusite.micropersistence.delegate.NotificaServiceItf#setPagina(int)
	 */
    public void setPagina(int pagina) throws DelegateException {
    	try {
    		getFacade().setPagina(pagina);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }

    /* (non-Javadoc)
	 * @see es.caib.gusite.micropersistence.delegate.NotificaServiceItf#setOrderby(java.lang.String)
	 */
    public void setOrderby(String orderby) throws DelegateException {
       	try {
       		getFacade().setOrderby(orderby);
           } catch (RemoteException e) {
               throw new DelegateException(e);
           }
       }

       /* (non-Javadoc)
	 * @see es.caib.gusite.micropersistence.delegate.NotificaServiceItf#setOrderby2(java.lang.String)
	 */
    public void setOrderby2(String orderby) throws DelegateException {
    	   	try {
    	   		getFacade().setOrderby2(orderby);
    	       } catch (RemoteException e) {
    	           throw new DelegateException(e);
    	       }
       }   
       
       /* (non-Javadoc)
	 * @see es.caib.gusite.micropersistence.delegate.NotificaServiceItf#getValorBD(java.lang.String)
	 */
    public String getValorBD(String valor) throws DelegateException {
       	try {
       		return getFacade().getValorBD(valor);
           } catch (RemoteException e) {
               throw new DelegateException(e);
           }
       }
       
       /* (non-Javadoc)
	 * @see es.caib.gusite.micropersistence.delegate.NotificaServiceItf#setFiltro(java.lang.String)
	 */
    public void setFiltro(String valor) throws DelegateException {
       	try {
       		getFacade().setFiltro(valor);
           } catch (RemoteException e) {
               throw new DelegateException(e);
           }
       }
       
       /* (non-Javadoc)
	 * @see es.caib.gusite.micropersistence.delegate.NotificaServiceItf#getWhere()
	 */
    public String getWhere() throws DelegateException {
    	   	try {
    	   		return getFacade().getWhere();
    	       } catch (RemoteException e) {
    	           throw new DelegateException(e);
    	       }
    	   }
    	   
       /* (non-Javadoc)
	 * @see es.caib.gusite.micropersistence.delegate.NotificaServiceItf#setWhere(java.lang.String)
	 */
    public void setWhere(String valor) throws DelegateException {
    	   	try {
    	   		getFacade().setWhere(valor);
    	       } catch (RemoteException e) {
    	           throw new DelegateException(e);
    	       }
    	   }   

       /* (non-Javadoc)
	 * @see es.caib.gusite.micropersistence.delegate.NotificaServiceItf#getTampagina()
	 */
    public int getTampagina() throws DelegateException {
    	   	try {
    	   		return getFacade().getTampagina();
    	       } catch (RemoteException e) {
    	           throw new DelegateException(e);
    	       }
    	   }
    	   
       /* (non-Javadoc)
	 * @see es.caib.gusite.micropersistence.delegate.NotificaServiceItf#setTampagina(int)
	 */
    public void setTampagina(int tampagina) throws DelegateException {
    	   	try {
    	   		getFacade().setTampagina(tampagina);
    	       } catch (RemoteException e) {
    	           throw new DelegateException(e);
    	       }
       } 
    
       /* (non-Javadoc)
	 * @see es.caib.gusite.micropersistence.delegate.NotificaServiceItf#checkSite(java.lang.Long, java.lang.Long)
	 */
    public boolean checkSite(Long site, Long id) throws DelegateException {
       	try {
       		return getFacade().checkSite(site,id);
       	} catch (RemoteException e) {
       		throw new DelegateException(e);
       	}
       }

       /* (non-Javadoc)
	 * @see es.caib.gusite.micropersistence.delegate.NotificaServiceItf#indexInsertaNoticia(es.caib.gusite.micromodel.Noticia, es.caib.gusite.model.ModelFilterObject)
	 */
    public void indexInsertaNoticia(Noticia noti, ModelFilterObject filter) throws DelegateException {
       	try {
       		getFacade().indexInsertaNoticia(noti, filter);
       	} catch (RemoteException e) {
       		throw new DelegateException(e);
       	}
       }
		
	
       /* (non-Javadoc)
	 * @see es.caib.gusite.micropersistence.delegate.NotificaServiceItf#indexBorraNoticia(java.lang.Long)
	 */
    public void indexBorraNoticia(Long id) throws DelegateException {
       	try {
       		getFacade().indexBorraNoticia(id);
       	} catch (RemoteException e) {
       		throw new DelegateException(e);
       	}
       }     
       
     

    public List<String> listarAnyos() throws DelegateException {
    	try {
       		return getFacade().listarAnyos();
       	} catch (RemoteException e) {
       		throw new DelegateException(e);
       	}
	}

    
    /* ========================================================= */
    /* ======================== REFERENCIA AL FACADE  ========== */
    /* ========================================================= */

    private Handle facadeHandle;

    private NoticiaFacade getFacade() throws RemoteException {
        return (NoticiaFacade) facadeHandle.getEJBObject();
    }

	protected NoticiaDelegate() throws DelegateException {
        try {
        	NoticiaFacadeHome home = NoticiaFacadeUtil.getHome();
        	NoticiaFacade remote = home.create();
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