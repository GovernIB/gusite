package es.caib.gusite.micropersistence.delegate;

import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.Handle;
import javax.naming.NamingException;

import es.caib.gusite.micromodel.Noticia;
import es.caib.gusite.micromodel.SolrPendienteResultado;
import es.caib.gusite.micropersistence.intf.NoticiaFacade;
import es.caib.gusite.micropersistence.intf.NoticiaFacadeHome;
import es.caib.gusite.micropersistence.util.NoticiaFacadeUtil;
import es.caib.gusite.micropersistence.util.PathUOResult;
import es.caib.solr.api.SolrIndexer;
import es.caib.solr.api.model.types.EnumCategoria;

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
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see es.caib.gusite.micropersistence.delegate.NotificaServiceItf#init(java
	 * .lang.Long)
	 */
	@Override
	public void init(final Long id) throws DelegateException {
		try {
			this.getFacade().init(id);
		} catch (final RemoteException e) {
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
	public Long grabarNoticia(final Noticia noticia) throws DelegateException {
		try {
			return this.getFacade().grabarNoticia(noticia);
		} catch (final RemoteException e) {
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
	public Noticia obtenerNoticia(final Long id) throws DelegateException {
		try {
			return this.getFacade().obtenerNoticia(id);
		} catch (final RemoteException e) {
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
		} catch (final RemoteException e) {
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
	public Long clonarNoticia(final Long id) throws DelegateException {
		try {
			return this.getFacade().clonarNoticia(id);
		} catch (final RemoteException e) {
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
	public Noticia obtenerNoticiaThin(final Long id, final String idioma) throws DelegateException {
		try {
			return this.getFacade().obtenerNoticiaThin(id, idioma);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * es.caib.gusite.micropersistence.delegate.NotificaServiceItf#listarNoticias ()
	 */
	@Override
	public List<?> listarNoticias() throws DelegateException {
		try {
			return this.getFacade().listarNoticias();
		} catch (final RemoteException e) {
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
	public List<Noticia> listarNoticiasThin(final String idioma) throws DelegateException {
		try {
			return this.getFacade().listarNoticiasThin(idioma);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	@Override
	@Deprecated
	public List<?> buscarElementos(final Map<?, ?> parametros, final Map<?, ?> traduccion, final String idmicrosite,
			final String idtipo, final String idioma) throws Exception {
		try {
			return this.getFacade().buscarElementos(parametros, traduccion, idmicrosite, idtipo, idioma);
		} catch (final RemoteException e) {
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
	public List<?> buscarElementos(final BuscarElementosParameter parameterObject) throws DelegateException {
		try {
			return this.getFacade().buscarElementos(parameterObject);
		} catch (final RemoteException e) {
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
	public void borrarNoticia(final Long id) throws DelegateException {
		try {
			this.getFacade().borrarNoticia(id);
		} catch (final RemoteException e) {
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
	public void borrarNoticia(final Long id, final boolean indexar) throws DelegateException {
		try {
			this.getFacade().borrarNoticia(id, indexar);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * es.caib.gusite.micropersistence.delegate.NotificaServiceItf#getParametros ()
	 */
	@Override
	public Hashtable<?, ?> getParametros() throws DelegateException {
		try {
			return this.getFacade().getParametros();
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * es.caib.gusite.micropersistence.delegate.NotificaServiceItf#parametrosCons ()
	 */
	@Override
	public void parametrosCons() throws DelegateException {
		try {
			this.getFacade().parametrosCons();
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see es.caib.gusite.micropersistence.delegate.NotificaServiceItf#getPagina()
	 */
	@Override
	public int getPagina() throws DelegateException {
		try {
			return this.getFacade().getPagina();
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see es.caib.gusite.micropersistence.delegate.NotificaServiceItf#setPagina
	 * (int)
	 */
	@Override
	public void setPagina(final int pagina) throws DelegateException {
		try {
			this.getFacade().setPagina(pagina);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see es.caib.gusite.micropersistence.delegate.NotificaServiceItf#setOrderby
	 * (java.lang.String)
	 */
	@Override
	public void setOrderby(final String orderby) throws DelegateException {
		try {
			this.getFacade().setOrderby(orderby);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see es.caib.gusite.micropersistence.delegate.NotificaServiceItf#setOrderby2
	 * (java.lang.String)
	 */
	@Override
	public void setOrderby2(final String orderby) throws DelegateException {
		try {
			this.getFacade().setOrderby2(orderby);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see es.caib.gusite.micropersistence.delegate.NotificaServiceItf#getValorBD
	 * (java.lang.String)
	 */
	@Override
	public String getValorBD(final String valor) throws DelegateException {
		try {
			return this.getFacade().getValorBD(valor);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see es.caib.gusite.micropersistence.delegate.NotificaServiceItf#setFiltro
	 * (java.lang.String)
	 */
	@Override
	public void setFiltro(final String valor) throws DelegateException {
		try {
			this.getFacade().setFiltro(valor);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see es.caib.gusite.micropersistence.delegate.NotificaServiceItf#getWhere()
	 */
	@Override
	public String getWhere() throws DelegateException {
		try {
			return this.getFacade().getWhere();
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see es.caib.gusite.micropersistence.delegate.NotificaServiceItf#setWhere(
	 * java.lang.String)
	 */
	@Override
	public void setWhere(final String valor) throws DelegateException {
		try {
			this.getFacade().setWhere(valor);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see es.caib.gusite.micropersistence.delegate.NotificaServiceItf#getTampagina
	 * ()
	 */
	@Override
	public int getTampagina() throws DelegateException {
		try {
			return this.getFacade().getTampagina();
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see es.caib.gusite.micropersistence.delegate.NotificaServiceItf#setTampagina
	 * (int)
	 */
	@Override
	public void setTampagina(final int tampagina) throws DelegateException {
		try {
			this.getFacade().setTampagina(tampagina);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see es.caib.gusite.micropersistence.delegate.NotificaServiceItf#checkSite
	 * (java.lang.Long, java.lang.Long)
	 */
	@Override
	public boolean checkSite(final Long site, final Long id) throws DelegateException {
		try {
			return this.getFacade().checkSite(site, id);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	@Override
	public List<String> listarAnyos() throws DelegateException {
		try {
			return this.getFacade().listarAnyos();
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	@Override
	public SolrPendienteResultado indexarSolr(final SolrIndexer solrIndexer, final Long idElemento,
			final EnumCategoria categoria, final PathUOResult iPathUO) throws DelegateException {
		try {
			return this.getFacade().indexarSolr(solrIndexer, idElemento, categoria, iPathUO);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	@Override
	public SolrPendienteResultado indexarSolrArchivo(final SolrIndexer solrIndexer, final Long idElemento,
			final EnumCategoria categoria, final Long idArchivo, final PathUOResult iPathUO) throws Exception {
		try {
			return this.getFacade().indexarSolrArchivo(solrIndexer, idElemento, categoria, idArchivo, iPathUO);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public List<Noticia> obtenerNoticiasByMicrositeId(final Long idMicrosite) throws DelegateException {
		try {
			return this.getFacade().obtenerNoticiasByMicrositeId(idMicrosite);
		} catch (final RemoteException e) {
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
			final NoticiaFacadeHome home = NoticiaFacadeUtil.getHome();
			final NoticiaFacade remote = home.create();
			this.facadeHandle = remote.getHandle();
		} catch (final NamingException e) {
			throw new DelegateException(e);
		} catch (final CreateException e) {
			throw new DelegateException(e);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

}