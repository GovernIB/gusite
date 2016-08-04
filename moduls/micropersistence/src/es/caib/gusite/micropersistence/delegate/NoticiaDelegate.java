package es.caib.gusite.micropersistence.delegate;

import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.Handle;
import javax.naming.NamingException;

import es.caib.gusite.lucene.model.ModelFilterObject;
import es.caib.gusite.micromodel.Noticia;
import es.caib.gusite.micropersistence.intf.NoticiaFacade;
import es.caib.gusite.micropersistence.intf.NoticiaFacadeHome;
import es.caib.gusite.micropersistence.util.NoticiaFacadeUtil;

/**
 * Business delegate para manipular Noticias.
 * 
 * @author Indra
 */
public class NoticiaDelegate implements StatelessDelegate, NoticiaServiceItf {

	private static final long serialVersionUID = -1342790045052841948L;

	/* ========================================================= */
	/* ======================== MÉTODOS DE NEGOCIO ============= */
	/* ========================================================= */

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.caib.gusite.micropersistence.delegate.NotificaServiceItf#init()
	 */
	@Override
	public void init() throws DelegateException {
		try {
			this.getFacade().init();
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.caib.gusite.micropersistence.delegate.NotificaServiceItf#init(java
	 * .lang.Long)
	 */
	@Override
	public void init(Long id) throws DelegateException {
		try {
			this.getFacade().init(id);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.caib.gusite.micropersistence.delegate.NotificaServiceItf#grabarNoticia
	 * (es.caib.gusite.micromodel.Noticia)
	 */
	@Override
	public Long grabarNoticia(Noticia noticia) throws DelegateException {
		try {
			return this.getFacade().grabarNoticia(noticia);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.caib.gusite.micropersistence.delegate.NotificaServiceItf#obtenerNoticia
	 * (java.lang.Long)
	 */
	@Override
	public Noticia obtenerNoticia(Long id) throws DelegateException {
		try {
			return this.getFacade().obtenerNoticia(id);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.caib.gusite.micropersistence.delegate.NotificaServiceItf#
	 * obtenerNoticiaDesdeUri(java.lang.Idioma, java.lang.String)
	 */
	public Noticia obtenerNoticiaDesdeUri(final String lang, final String uri, final String site)
			throws DelegateException {
		try {
			return this.getFacade().obtenerNoticiaDesdeUri(lang, uri, site);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.caib.gusite.micropersistence.delegate.NotificaServiceItf#clonarNoticia
	 * (java.lang.Long)
	 */
	@Override
	public Long clonarNoticia(Long id) throws DelegateException {
		try {
			return this.getFacade().clonarNoticia(id);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.caib.gusite.micropersistence.delegate.NotificaServiceItf#
	 * obtenerNoticiaThin(java.lang.Long)
	 */
	@Override
	public Noticia obtenerNoticiaThin(Long id, String idioma)
			throws DelegateException {
		try {
			return this.getFacade().obtenerNoticiaThin(id, idioma);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.caib.gusite.micropersistence.delegate.NotificaServiceItf#listarNoticias
	 * ()
	 */
	@Override
	public List<?> listarNoticias() throws DelegateException {
		try {
			return this.getFacade().listarNoticias();
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.caib.gusite.micropersistence.delegate.NotificaServiceItf#
	 * listarNoticiasThin()
	 */
	@Override
	public List<Noticia> listarNoticiasThin(String idioma)
			throws DelegateException {
		try {
			return this.getFacade().listarNoticiasThin(idioma);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	@Override
	@Deprecated
	public List<?> buscarElementos(Map<?, ?> parametros, Map<?, ?> traduccion,
			String idmicrosite, String idtipo, String idioma) throws Exception {
		try {
			return this.getFacade().buscarElementos(parametros, traduccion,
					idmicrosite, idtipo, idioma);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.caib.gusite.micropersistence.delegate.NotificaServiceItf#buscarElementos
	 * (java.util.Map, java.util.Map, java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public List<?> buscarElementos(BuscarElementosParameter parameterObject)
			throws DelegateException {
		try {
			return this.getFacade().buscarElementos(parameterObject);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.caib.gusite.micropersistence.delegate.NotificaServiceItf#
	 * buscarElementosLuc(java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, boolean)
	 */
	@Override
	public List<?> buscarElementosLuc(String micro, String idi, String idlista,
			String cadena, boolean sugerir) throws DelegateException {
		try {
			return this.getFacade().buscarElementosLuc(micro, idi, idlista,
					cadena, sugerir);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.caib.gusite.micropersistence.delegate.NotificaServiceItf#borrarNoticia
	 * (java.lang.Long)
	 */
	@Override
	public void borrarNoticia(Long id) throws DelegateException {
		try {
			this.getFacade().borrarNoticia(id);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.caib.gusite.micropersistence.delegate.NotificaServiceItf#getParametros
	 * ()
	 */
	@Override
	public Hashtable<?, ?> getParametros() throws DelegateException {
		try {
			return this.getFacade().getParametros();
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.caib.gusite.micropersistence.delegate.NotificaServiceItf#parametrosCons
	 * ()
	 */
	@Override
	public void parametrosCons() throws DelegateException {
		try {
			this.getFacade().parametrosCons();
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.caib.gusite.micropersistence.delegate.NotificaServiceItf#getPagina()
	 */
	@Override
	public int getPagina() throws DelegateException {
		try {
			return this.getFacade().getPagina();
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.caib.gusite.micropersistence.delegate.NotificaServiceItf#setPagina
	 * (int)
	 */
	@Override
	public void setPagina(int pagina) throws DelegateException {
		try {
			this.getFacade().setPagina(pagina);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.caib.gusite.micropersistence.delegate.NotificaServiceItf#setOrderby
	 * (java.lang.String)
	 */
	@Override
	public void setOrderby(String orderby) throws DelegateException {
		try {
			this.getFacade().setOrderby(orderby);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.caib.gusite.micropersistence.delegate.NotificaServiceItf#setOrderby2
	 * (java.lang.String)
	 */
	@Override
	public void setOrderby2(String orderby) throws DelegateException {
		try {
			this.getFacade().setOrderby2(orderby);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.caib.gusite.micropersistence.delegate.NotificaServiceItf#getValorBD
	 * (java.lang.String)
	 */
	@Override
	public String getValorBD(String valor) throws DelegateException {
		try {
			return this.getFacade().getValorBD(valor);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.caib.gusite.micropersistence.delegate.NotificaServiceItf#setFiltro
	 * (java.lang.String)
	 */
	@Override
	public void setFiltro(String valor) throws DelegateException {
		try {
			this.getFacade().setFiltro(valor);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.caib.gusite.micropersistence.delegate.NotificaServiceItf#getWhere()
	 */
	@Override
	public String getWhere() throws DelegateException {
		try {
			return this.getFacade().getWhere();
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.caib.gusite.micropersistence.delegate.NotificaServiceItf#setWhere(
	 * java.lang.String)
	 */
	@Override
	public void setWhere(String valor) throws DelegateException {
		try {
			this.getFacade().setWhere(valor);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.caib.gusite.micropersistence.delegate.NotificaServiceItf#getTampagina
	 * ()
	 */
	@Override
	public int getTampagina() throws DelegateException {
		try {
			return this.getFacade().getTampagina();
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.caib.gusite.micropersistence.delegate.NotificaServiceItf#setTampagina
	 * (int)
	 */
	@Override
	public void setTampagina(int tampagina) throws DelegateException {
		try {
			this.getFacade().setTampagina(tampagina);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.caib.gusite.micropersistence.delegate.NotificaServiceItf#checkSite
	 * (java.lang.Long, java.lang.Long)
	 */
	@Override
	public boolean checkSite(Long site, Long id) throws DelegateException {
		try {
			return this.getFacade().checkSite(site, id);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.caib.gusite.micropersistence.delegate.NotificaServiceItf#
	 * indexInsertaNoticia(es.caib.gusite.micromodel.Noticia,
	 * es.caib.gusite.model.ModelFilterObject)
	 */
	@Override
	public void indexInsertaNoticia(Noticia noti, ModelFilterObject filter)
			throws DelegateException {
		try {
			this.getFacade().indexInsertaNoticia(noti, filter);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.caib.gusite.micropersistence.delegate.NotificaServiceItf#indexBorraNoticia
	 * (java.lang.Long)
	 */
	@Override
	public void indexBorraNoticia(Long id) throws DelegateException {
		try {
			this.getFacade().indexBorraNoticia(id);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	@Override
	public List<String> listarAnyos() throws DelegateException {
		try {
			return this.getFacade().listarAnyos();
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/* ========================================================= */
	/* ======================== REFERENCIA AL FACADE ========== */
	/* ========================================================= */

	private Handle facadeHandle;

	private NoticiaFacade getFacade() throws RemoteException {
		return (NoticiaFacade) this.facadeHandle.getEJBObject();
	}

	protected NoticiaDelegate() throws DelegateException {
		try {
			NoticiaFacadeHome home = NoticiaFacadeUtil.getHome();
			NoticiaFacade remote = home.create();
			this.facadeHandle = remote.getHandle();
		} catch (NamingException e) {
			throw new DelegateException(e);
		} catch (CreateException e) {
			throw new DelegateException(e);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}
}