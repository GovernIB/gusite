package es.caib.gusite.micropersistence.delegate;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.Handle;
import javax.naming.NamingException;

import es.caib.gusite.lucene.model.ModelFilterObject;
import es.caib.gusite.micromodel.Contenido;
import es.caib.gusite.micropersistence.intf.ContenidoFacade;
import es.caib.gusite.micropersistence.intf.ContenidoFacadeHome;
import es.caib.gusite.micropersistence.util.ContenidoFacadeUtil;

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
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Inicializo los parámetros de la consulta de Contenido.
	 * 
	 * @param menu
	 * @throws DelegateException
	 */
	public void init(String menu) throws DelegateException {
		try {
			this.getFacade().init(menu);
		} catch (RemoteException e) {
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
	public Long grabarContenido(Contenido contenido) throws DelegateException {
		try {
			return this.getFacade().grabarContenido(contenido);
		} catch (RemoteException e) {
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
	public Contenido obtenerContenido(Long id) throws DelegateException {
		try {
			return this.getFacade().obtenerContenido(id);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Obtiene un Contenido a partir de la uri
	 * 
	 * @return Contenido
	 * @throws DelegateException
	 */
	public Contenido obtenerContenidoDesdeUri(String idioma, String uri)
			throws DelegateException {
		try {
			return this.getFacade().obtenerContenidoDesdeUri(idioma, uri);
		} catch (RemoteException e) {
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
	public boolean existeContenido(Long id) throws DelegateException {
		try {
			return this.getFacade().existeContenido(id);
		} catch (RemoteException e) {
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
		} catch (RemoteException e) {
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
	public ArrayList<?> listarContenidos(String idioma)
			throws DelegateException {
		try {
			return this.getFacade().listarContenidos(idioma);
		} catch (RemoteException e) {
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
	public List<?> listarAllContenidos(String micro) throws DelegateException {
		try {
			return this.getFacade().listarAllContenidos(micro);
		} catch (RemoteException e) {
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
	public void borrarContenido(Long id) throws DelegateException {
		try {
			this.getFacade().borrarContenido(id);
		} catch (RemoteException e) {
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
	public void eliminarDocumentos(String micro, String pagina)
			throws DelegateException {
		try {
			this.getFacade().eliminarDocumentos(micro, pagina);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Devuelve el titulo del listado de contenidos, la miga de pan de donde se
	 * encuentra. Podrí ser: Menu padre > Menu hijo > Contenido o bien Menu
	 * padre > Contenido según exista idmenu y/o idcontenido
	 * 
	 * @param idmenu
	 * @param idcontenido
	 * @return string con el titulo del listado de contenidos
	 * @throws DelegateException
	 */
	public String migapan(String idmenu, Long idcontenido)
			throws DelegateException {
		try {
			return this.getFacade().migapan(idmenu, idcontenido);
		} catch (RemoteException e) {
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
	public boolean checkSite(Long id, Long idsite) throws DelegateException {
		try {
			return this.getFacade().checkSite(id, idsite);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public Hashtable<?, ?> getParametros() throws DelegateException {
		try {
			return this.getFacade().getParametros();
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public void parametrosCons() throws DelegateException {
		try {
			this.getFacade().parametrosCons();
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public int getPagina() throws DelegateException {
		try {
			return this.getFacade().getPagina();
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public void setPagina(int pagina) throws DelegateException {
		try {
			this.getFacade().setPagina(pagina);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public void setOrderby(String orderby) throws DelegateException {
		try {
			this.getFacade().setOrderby(orderby);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public void setOrderby2(String orderby) throws DelegateException {
		try {
			this.getFacade().setOrderby2(orderby);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public String getValorBD(String valor) throws DelegateException {
		try {
			return this.getFacade().getValorBD(valor);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public void setFiltro(String valor) throws DelegateException {
		try {
			this.getFacade().setFiltro(valor);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public String getWhere() throws DelegateException {
		try {
			return this.getFacade().getWhere();
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public void setWhere(String valor) throws DelegateException {
		try {
			this.getFacade().setWhere(valor);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public int getTampagina() throws DelegateException {
		try {
			return this.getFacade().getTampagina();
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public void setTampagina(int tampagina) throws DelegateException {
		try {
			this.getFacade().setTampagina(tampagina);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public List<?> listarDocumentos(String mic, String pagina)
			throws DelegateException {
		try {
			return this.getFacade().listarDocumentos(mic, pagina);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public void indexInsertaContenido(Contenido con, ModelFilterObject filter)
			throws DelegateException {
		try {
			this.getFacade().indexInsertaContenido(con, filter);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}

	}

	public void indexBorraContenido(Long id) throws DelegateException {
		try {
			this.getFacade().indexBorraContenido(id);
		} catch (RemoteException e) {
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
			ContenidoFacadeHome home = ContenidoFacadeUtil.getHome();
			ContenidoFacade remote = home.create();
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
