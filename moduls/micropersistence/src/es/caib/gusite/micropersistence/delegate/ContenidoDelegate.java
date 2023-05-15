package es.caib.gusite.micropersistence.delegate;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.Handle;
import javax.naming.NamingException;

import es.caib.gusite.micromodel.Archivo;
import es.caib.gusite.micromodel.Contenido;
import es.caib.gusite.micromodel.SolrPendienteResultado;
import es.caib.gusite.micropersistence.intf.ContenidoFacade;
import es.caib.gusite.micropersistence.intf.ContenidoFacadeHome;
import es.caib.gusite.micropersistence.util.ContenidoFacadeUtil;
import es.caib.gusite.micropersistence.util.PathUOResult;
import es.caib.solr.api.SolrIndexer;
import es.caib.solr.api.model.types.EnumCategoria;

/**
 * Business delegate para manipular contenido.
 *
 * @author Indra
 */
public class ContenidoDelegate implements StatelessDelegate {

	private static final long serialVersionUID = -1652208405910819789L;

	/* ========================================================= */
	/* ======================== MÉTODOS DE NEGOCIO ============= */
	/* ========================================================= */

	/**
	 * Inicializo los parámetros de la consulta de Contenido.
	 *
	 * @throws DelegateException
	 */
	public void init() throws DelegateException {
		try {
			this.getFacade().init();
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Inicializo los parámetros de la consulta de Contenido.
	 *
	 * @param menu
	 * @throws DelegateException
	 */
	public void init(final String menu) throws DelegateException {
		try {
			this.getFacade().init(menu);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Crea o actualiza un Contenido
	 *
	 * @param contenido
	 * @return Id del contenido
	 * @throws DelegateException
	 */
	public Long grabarContenido(final Contenido contenido) throws DelegateException {
		try {
			return this.getFacade().grabarContenido(contenido);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Obtiene un Contenido
	 *
	 * @param id
	 *            Id del contenido
	 * @return Contenido
	 * @throws DelegateException
	 */
	public Contenido obtenerContenido(final Long id) throws DelegateException {
		try {
			return this.getFacade().obtenerContenido(id);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Obtiene un Contenido a partir de la uri
	 *
	 * @return Contenido
	 * @throws DelegateException
	 */
	public Contenido obtenerContenidoDesdeUri(final String idioma, final String uri, final String site)
			throws DelegateException {
		try {
			return this.getFacade().obtenerContenidoDesdeUri(idioma, uri, site);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Existe contenido
	 *
	 * @param id
	 *            Id del contenido
	 * @return True si existe el contenido
	 * @throws DelegateException
	 */
	public boolean existeContenido(final Long id) throws DelegateException {
		try {
			return this.getFacade().existeContenido(id);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Lista todos los Contenidos
	 *
	 * @return una lista de contenidos
	 * @throws DelegateException
	 */
	public List<?> listarContenidos() throws DelegateException {
		try {
			return this.getFacade().listarContenidos();
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Lista todos los contenidos poniendole un idioma por defecto
	 *
	 * @param idioma
	 * @return ArrayList con los contenidos
	 * @throws DelegateException
	 */
	public ArrayList<?> listarContenidos(final String idioma) throws DelegateException {
		try {
			return this.getFacade().listarContenidos(idioma);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Lista todos los contenidos de un microsite<br/>
	 *
	 * @param micro
	 *            Identificador del microsite
	 * @return List de beans <em>Contenido</em>
	 * @throws DelegateException
	 */
	public List<Contenido> listarAllContenidos(final String micro) throws DelegateException {
		try {
			return this.getFacade().listarAllContenidos(micro);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * borra un Contenido
	 *
	 * @param id
	 *            Id de un contenido
	 * @throws DelegateException
	 */
	public void borrarContenido(final Long id) throws DelegateException {
		try {
			this.getFacade().borrarContenido(id);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * borra un Contenido
	 *
	 * @param id
	 *            Id de un contenido
	 * @param indexar
	 *            Indica si se indexa
	 * @throws DelegateException
	 */
	public void borrarContenido(final Long id, final boolean indexar) throws DelegateException {
		try {
			this.getFacade().borrarContenido(id, indexar);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Elimina todos los documentos de la pagina
	 *
	 * @param micro
	 *            Id del microsite
	 * @param pagina
	 * @throws DelegateException
	 */
	public void eliminarDocumentos(final String micro, final String pagina) throws DelegateException {
		try {
			this.getFacade().eliminarDocumentos(micro, pagina);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Devuelve el titulo del listado de contenidos, la miga de pan de donde se
	 * encuentra. Podrí ser: Menu padre > Menu hijo > Contenido o bien Menu padre >
	 * Contenido según exista idmenu y/o idcontenido
	 *
	 * @param idmenu
	 * @param idcontenido
	 * @return string con el titulo del listado de contenidos
	 * @throws DelegateException
	 */
	public String migapan(final String idmenu, final Long idcontenido) throws DelegateException {
		try {
			return this.getFacade().migapan(idmenu, idcontenido);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Comprueba que el contenido pertenece al microsite
	 *
	 * @param id
	 *            Id del contenido
	 * @param idsite
	 *            Id del site
	 * @return true si no pertenece
	 * @throws DelegateException
	 */
	public boolean checkSite(final Long id, final Long idsite) throws DelegateException {
		try {
			return this.getFacade().checkSite(id, idsite);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public Hashtable<?, ?> getParametros() throws DelegateException {
		try {
			return this.getFacade().getParametros();
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public void parametrosCons() throws DelegateException {
		try {
			this.getFacade().parametrosCons();
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public int getPagina() throws DelegateException {
		try {
			return this.getFacade().getPagina();
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public void setPagina(final int pagina) throws DelegateException {
		try {
			this.getFacade().setPagina(pagina);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public void setOrderby(final String orderby) throws DelegateException {
		try {
			this.getFacade().setOrderby(orderby);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public void setOrderby2(final String orderby) throws DelegateException {
		try {
			this.getFacade().setOrderby2(orderby);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public String getValorBD(final String valor) throws DelegateException {
		try {
			return this.getFacade().getValorBD(valor);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public void setFiltro(final String valor) throws DelegateException {
		try {
			this.getFacade().setFiltro(valor);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public String getWhere() throws DelegateException {
		try {
			return this.getFacade().getWhere();
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public void setWhere(final String valor) throws DelegateException {
		try {
			this.getFacade().setWhere(valor);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public int getTampagina() throws DelegateException {
		try {
			return this.getFacade().getTampagina();
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public void setTampagina(final int tampagina) throws DelegateException {
		try {
			this.getFacade().setTampagina(tampagina);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public List<Archivo> listarDocumentos(final String mic, final String pagina) throws DelegateException {
		try {
			return this.getFacade().listarDocumentos(mic, pagina);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public SolrPendienteResultado indexarSolr(final SolrIndexer solrIndexer, final Long idElemento,
			final EnumCategoria categoria, final PathUOResult iPathUO) throws DelegateException {
		try {
			return this.getFacade().indexarSolr(solrIndexer, idElemento, categoria, iPathUO);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public SolrPendienteResultado indexarSolrArchivo(final SolrIndexer solrIndexer, final Long idElemento,
			final EnumCategoria categoria, final Long idArchivo, final PathUOResult iPathUO) throws DelegateException {
		try {
			return this.getFacade().indexarSolrArchivo(solrIndexer, idElemento, categoria, idArchivo, iPathUO);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}
	/* ========================================================= */
	/* ======================== REFERENCIA AL FACADE ========== */
	/* ========================================================= */

	private Handle facadeHandle;

	private ContenidoFacade getFacade() throws RemoteException {
		return (ContenidoFacade) this.facadeHandle.getEJBObject();
	}

	protected ContenidoDelegate() throws DelegateException {
		try {
			final ContenidoFacadeHome home = ContenidoFacadeUtil.getHome();
			final ContenidoFacade remote = home.create();
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
