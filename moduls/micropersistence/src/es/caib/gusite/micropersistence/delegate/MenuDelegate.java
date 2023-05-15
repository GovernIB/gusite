package es.caib.gusite.micropersistence.delegate;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.Handle;
import javax.naming.NamingException;

import org.apache.struts.upload.FormFile;

import es.caib.gusite.micromodel.Menu;
import es.caib.gusite.micropersistence.intf.MenuFacade;
import es.caib.gusite.micropersistence.intf.MenuFacadeHome;
import es.caib.gusite.micropersistence.util.MenuFacadeUtil;

/**
 * Business delegate para manipular menu.
 *
 * @author Indra
 */
public class MenuDelegate implements StatelessDelegate {

	private static final long serialVersionUID = 5472203499333747073L;

	/* ========================================================= */
	/* ======================== MÉTODOS DE NEGOCIO ============= */
	/* ========================================================= */

	/**
	 * Inicializo los parámetros de la consulta de Menu.
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
	 * Inicializo los parámetros de la consulta de Menu.
	 *
	 * @param id
	 *            del Menu
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
	 * Crea o actualiza un Menu
	 *
	 * @param menu
	 * @return Id del menu
	 * @throws DelegateException
	 */
	public Long grabarMenu(final Menu menu) throws DelegateException {
		try {
			return this.getFacade().grabarMenu(menu);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Obtiene un menu
	 *
	 * @param id
	 *            Id del Menu
	 * @return Menu
	 * @throws DelegateException
	 */
	public Menu obtenerMenu(final Long id) throws DelegateException {
		try {
			return this.getFacade().obtenerMenu(id);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Obtiene un menu sin cargar las paginas de contenido
	 *
	 * @param id
	 *            Id del Menu
	 * @param idioma
	 * @return Menu
	 * @throws DelegateException
	 */
	public Menu obtenerMenuThin(final Long id, final String idioma) throws DelegateException {
		try {
			return this.getFacade().obtenerMenuThin(id, idioma);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Listado plano de todos los objetos menu y contenidos de un microsite. Está
	 * ordenado por el campo orden.
	 *
	 * @param idsite
	 *            Id del site
	 * @return ArrayList
	 * @throws DelegateException
	 */
	public ArrayList<?> ObtenerObjetosMenu(final Long idsite) throws DelegateException {
		try {
			return this.getFacade().ObtenerObjetosMenu(idsite);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Lista todos los menus
	 *
	 * @return una lista de menus
	 * @throws DelegateException
	 */
	public List<?> listarMenus() throws DelegateException {
		try {
			return this.getFacade().listarMenus();
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Obtiene un menu.
	 *
	 * @param idmicrosite
	 *            Id del microsite
	 * @param padre
	 * @param visible
	 * @param idioma
	 * @return ArrayList con objetos "Menu"
	 * @throws DelegateException
	 */
	public ArrayList<?> listarMenuMicrosite(final Long idmicrosite, final Long padre, final String visible,
			final String idioma) throws DelegateException {
		try {
			return this.getFacade().listarMenuMicrosite(idmicrosite, padre, visible, idioma);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Lista todos los menus poniendole un idioma por defecto
	 *
	 * @param idioma
	 * @return ArrayList
	 * @throws DelegateException
	 */
	public ArrayList<?> listarMenus(final String idioma) throws DelegateException {
		try {
			return this.getFacade().listarMenus(idioma);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Lista un menu poniendole un idioma por defecto
	 *
	 * @param id
	 *            Id del menu
	 * @throws DelegateException
	 */
	public void borrarMenu(final Long id) throws DelegateException {
		try {
			this.getFacade().borrarMenu(id);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Lista un menu poniendole un idioma por defecto
	 *
	 * @param id
	 *            Id del menu
	 * @param indexar
	 *            Indica si se indexa
	 * @throws DelegateException
	 */
	public void borrarMenu(final Long id, final boolean indexar) throws DelegateException {
		try {
			this.getFacade().borrarMenu(id, indexar);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Actualizamos los ordenes de los objetos que están por debajo del nuevo menú
	 * creado o eliminado sumando 1 o restando 1
	 *
	 * @param orden
	 * @param op
	 * @param micro
	 * @throws DelegateException
	 */
	public void Reordena(final int orden, final char op, final Long micro) throws DelegateException {
		try {
			this.getFacade().Reordena(orden, op, micro);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Actualiza todo el arbol del menu
	 *
	 * @param idmicro
	 *            Id del microsite
	 * @param ids
	 *            Ids de los contenidos
	 * @param visibles
	 * @param modos
	 * @param ordenes
	 * @param idPadres
	 * @param tipos
	 * @param imagenes
	 * @param imagenesnom
	 * @param imagenesbor
	 * @param traducciones
	 * @throws DelegateException
	 */
	public void actualizarMenus(final Long idmicro, final Long[] ids, final String[] visibles, final String[] modos,
			final Integer[] ordenes, final Long[] idPadres, final String[] tipos, final FormFile[] imagenes,
			final String[] imagenesnom, final boolean[] imagenesbor, final String[] traducciones)
			throws DelegateException {
		try {
			this.getFacade().actualizarMenus(idmicro, ids, visibles, modos, ordenes, idPadres, tipos, imagenes,
					imagenesnom, imagenesbor, traducciones);
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

	public List<?> listarCombo(final String mic) throws DelegateException {
		try {
			return this.getFacade().listarCombo(mic);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public List<?> padreCombo(final String mic) throws DelegateException {
		try {
			return this.getFacade().padreCombo(mic);
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

	private MenuFacade getFacade() throws RemoteException {
		return (MenuFacade) this.facadeHandle.getEJBObject();
	}

	protected MenuDelegate() throws DelegateException {
		try {
			final MenuFacadeHome home = MenuFacadeUtil.getHome();
			final MenuFacade remote = home.create();
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
