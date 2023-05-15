package es.caib.gusite.micropersistence.delegate;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.Handle;
import javax.naming.NamingException;

import es.caib.gusite.micromodel.Tipo;
import es.caib.gusite.micropersistence.intf.TipoFacade;
import es.caib.gusite.micropersistence.intf.TipoFacadeHome;
import es.caib.gusite.micropersistence.util.TipoFacadeUtil;

/**
 * Business delegate para manipular Tipo.
 *
 * @author Indra
 */
public class TipoDelegate implements StatelessDelegate {

	private static final long serialVersionUID = 2074125506901467457L;

	/* ========================================================= */
	/* ======================== MÉTODOS DE NEGOCIO ============= */
	/* ========================================================= */

	/**
	 * Inicializo los parámetros de la consulta.
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
	 * Inicializo los parámetros de la consulta.
	 *
	 * @param id
	 *            Id de un tipo
	 * @throws DelegateException
	 */
	public void init(final Long id) throws DelegateException {
		try {
			this.getFacade().init(id);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Inicializo los parámetros de la consulta.
	 *
	 * @param id
	 *            Id de un tipo
	 * @param idiomapasado
	 * @throws DelegateException
	 */
	public void init(final Long id, final String idiomapasado) throws DelegateException {
		try {
			this.getFacade().init(id, idiomapasado);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Crea o actualiza un tipo de noticia
	 *
	 * @param tp
	 * @return Id de un tipo
	 * @throws DelegateException
	 */
	public Long grabarTipo(final Tipo tp) throws DelegateException {
		try {
			return this.getFacade().grabarTipo(tp);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Obtiene un tipo de noticia
	 *
	 * @param id
	 *            Id de un tipo
	 * @return Tipo
	 * @throws DelegateException
	 */
	public Tipo obtenerTipo(final Long id) throws DelegateException {
		try {
			return this.getFacade().obtenerTipo(id);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Obtiene un tipo a partir de la uri
	 *
	 * @return Tipo
	 * @throws DelegateException
	 */
	public Tipo obtenerTipoDesdeUri(final String idioma, final String uri, final String site) throws DelegateException {
		try {
			return this.getFacade().obtenerTipoDesdeUri(idioma, uri, site);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Obtiene los valores del dominio
	 *
	 * @param id
	 *            Id de un tipo
	 * @param parametros
	 * @return Map
	 * @throws DelegateException
	 */
	public Map<?, ?> obtenerListado(final Long id, final Map<?, ?> parametros) throws DelegateException {
		try {
			return this.getFacade().obtenerListado(id, parametros);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Obtiene los valores del dominio
	 *
	 * @param id
	 *            Id de tipo
	 * @param parametros
	 * @return String
	 * @throws DelegateException
	 */
	public String obtenerPegoteHTMLExterno(final Long id, final Map<?, ?> parametros) throws DelegateException {
		try {
			return this.getFacade().obtenerPegoteHTMLExterno(id, parametros);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Lista todos los tipos
	 *
	 * @return una lista de tipos
	 * @throws DelegateException
	 */
	public List<?> listarTipos() throws DelegateException {
		try {
			return this.getFacade().listarTipos();
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Lista todos los tipos
	 *
	 * @param idiomapasado
	 * @return una lista de tipos
	 * @throws DelegateException
	 */
	public List<?> listarTiposrec(final String idiomapasado) throws DelegateException {
		try {
			return this.getFacade().listarTiposrec(idiomapasado);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * borra un tipo de noticia
	 *
	 * @param id
	 *            Id de un tipo
	 * @throws DelegateException
	 */
	public void borrarTipo(final Long id) throws DelegateException {
		try {
			this.getFacade().borrarTipo(id);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * borra un tipo de noticia
	 *
	 * @param id
	 *            Id de un tipo
	 * @param indexar
	 *            Indica si se indexa
	 * @throws DelegateException
	 */
	public void borrarTipo(final Long id, final boolean indexar) throws DelegateException {
		try {
			this.getFacade().borrarTipo(id, indexar);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Establece el filtro del tipo. Si es true devolverá sólo los externos. Si es
	 * false, devolverá todos menos los externos.
	 *
	 * @param externos
	 *            boolean
	 * @throws DelegateException
	 */
	public void setFiltroExterno(final boolean externos) throws DelegateException {
		try {
			this.getFacade().setFiltroExterno(externos);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Lista todas las distintas clasificaciones de un tipo de noticias
	 *
	 * @param idmicrosite
	 *            Id de un Microsite
	 * @return una lista
	 * @throws DelegateException
	 */
	public List<?> comboClasificacion(final Long idmicrosite) throws DelegateException {
		try {
			return this.getFacade().comboClasificacion(idmicrosite);
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

	public List<?> listarCombo(final Long idmicrosite) throws DelegateException {
		try {
			return this.getFacade().listarCombo(idmicrosite);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public List<?> listarCombo(final Long idmicrosite, final ArrayList<String> tiposNoIncluidos)
			throws DelegateException {
		try {
			return this.getFacade().listarCombo(idmicrosite, tiposNoIncluidos);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public boolean checkSite(final Long site, final Long id) throws DelegateException {
		try {
			return this.getFacade().checkSite(site, id);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/* ========================================================= */
	/* ======================== REFERENCIA AL FACADE ========== */
	/* ========================================================= */

	private Handle facadeHandle;

	private TipoFacade getFacade() throws RemoteException {
		return (TipoFacade) this.facadeHandle.getEJBObject();
	}

	protected TipoDelegate() throws DelegateException {
		try {
			final TipoFacadeHome home = TipoFacadeUtil.getHome();
			final TipoFacade remote = home.create();
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