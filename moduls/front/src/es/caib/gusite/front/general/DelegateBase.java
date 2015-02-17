package es.caib.gusite.front.general;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.gusite.front.general.bean.MenuFront;
import es.caib.gusite.front.util.Cadenas;
import es.caib.gusite.front.util.Fechas;
import es.caib.gusite.micromodel.Contenido;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.Menu;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.Noticia;
import es.caib.gusite.micromodel.Tiposervicio;
import es.caib.gusite.micromodel.TraduccionContenido;
import es.caib.gusite.micromodel.Traducible;
import es.caib.gusite.micropersistence.delegate.ContenidoDelegate;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.LDistribucionDelegate;
import es.caib.gusite.micropersistence.delegate.MenuDelegate;
import es.caib.gusite.micropersistence.delegate.MicrositeDelegate;
import es.caib.gusite.micropersistence.delegate.NoticiaDelegate;
import es.caib.gusite.micropersistence.delegate.TiposervicioDelegate;
import es.caib.gusite.utilities.rolsacAPI.APIUtil;
import es.caib.rolsac.api.v2.edifici.EdificiCriteria;
import es.caib.rolsac.api.v2.rolsac.RolsacQueryService;
import es.caib.rolsac.api.v2.unitatAdministrativa.UnitatAdministrativaCriteria;
import es.caib.rolsac.api.v2.unitatAdministrativa.UnitatAdministrativaQueryServiceAdapter;

/**
 * Manejador de obtener objetos listos para ser visualizados en la web. Utiliza
 * ObjectCache para ir cacheando todos los objetos que se van demandando.
 * 
 * @author Indra
 * 
 */
public class DelegateBase {

	protected static Log _log = LogFactory.getLog(DelegateBase.class);

	public DelegateBase() {
	}

	/**
	 * Método que devuelve un microsite a partir de su clave unica.
	 * 
	 * @param key
	 *            String con el mkey del microsite
	 * @param idioma
	 *            String con el idioma
	 * @return Microsites
	 * @throws ExceptionFrontMicro
	 */
	public Microsite obtenerMicrositebyKey(String key, String idioma)
			throws DelegateException {
		MicrositeDelegate microdel = DelegateUtil.getMicrositeDelegate();

		Microsite micro = microdel.obtenerMicrositebyKey(key);
		if (micro != null) {
			micro.setIdi(idioma);
		}
		return micro;

	}

	/**
	 * Método que devuelve un microsite a partir de su URI.
	 * 
	 * @param uri
	 *            String con el URI del microsite
	 * @param idioma
	 *            String con el idioma
	 * @return Microsites
	 * @throws ExceptionFrontMicro
	 */
	public Microsite obtenerMicrositebyUri(String uri, String idioma)
			throws DelegateException {
		MicrositeDelegate microdel = DelegateUtil.getMicrositeDelegate();

		Microsite micro = microdel.obtenerMicrositebyUri(uri);
		if (micro != null) {
			micro.setIdi(idioma);
		}
		return micro;

	}

	/**
	 * Método que devuelve el menú principal de un microsite listo para ser
	 * visualizado por el público.
	 * 
	 * @param idmicrosite
	 *            Long con el id del microsite
	 * @param idioma
	 *            String con el idioma
	 * @return ArrayList con objetos "Menufront"
	 * @throws Exception
	 */
	public ArrayList<?> obtenerMainMenu(Long idmicrosite, String idioma)
			throws DelegateException {
		ArrayList<?> listamenu = this.montarmenu(idmicrosite, idioma);
		return listamenu;
		// throw new Exception(" [obtenerMainMenu, " + idmicrosite + ", " +
		// idioma + " ] Error: " + e.getMessage());
	}

	/**
	 * Método que devuelve un contenido listo para ser visualizado por el
	 * público. Parsea todas los tags de HTML de microsite. También parsea la
	 * versión Beta del contenido.
	 * 
	 * @param idcontenido
	 *            Long con el id del contenido
	 * @param idioma
	 *            String con el idioma
	 * @param microsite
	 *            Microsite al que pertenece el contenido
	 * @return Contenido
	 * @throws Exception
	 */
	public Contenido obtenerContenido(Long idcontenido, String idioma,
			Microsite microsite) throws ExceptionFrontPagina {
		try {

			ContenidoDelegate contenidodel = DelegateUtil
					.getContenidoDelegate();
			Contenido contenido = contenidodel.obtenerContenido(idcontenido);
			if (contenido == null) {
				return null;
			}
			contenido.setIdi(idioma);
			TraduccionContenido traduccionContenido = ((TraduccionContenido) contenido
					.getTraduccion(idioma));
			if (traduccionContenido == null) {
				// Si no hay traduccion dada de alta para el contenido (lo cual
				// es una inconsistencia de bd, damos un not found)
				return null;
			}
			return contenido;

		} catch (DelegateException e) {
			throw new ExceptionFrontPagina(" [obtenerContenido, idsite="
					+ microsite.getId() + ", cont=" + idcontenido + ", idioma="
					+ idioma + " ] Error=" + e.getMessage() + "\n Stack="
					+ Cadenas.statcktrace2String(e.getStackTrace(), 3));
		}

	}

	/**
	 * Obtiene una rama de menú completa lista para ser visualizada por el
	 * público.
	 * 
	 * @param idmenu
	 *            Long con el identificador del menu
	 * @param idioma
	 *            String con el idioma
	 * @return Menu
	 * @throws DelegateException
	 * @throws Exception
	 */
	public Menu obtenerMenuBranch(Long idmenu, String idioma)
			throws DelegateException {
		MenuDelegate menudel = DelegateUtil.getMenuDelegate();
		Menu menu = menudel.obtenerMenuThin(idmenu, idioma);
		return menu;

	}

	/**
	 * Obtiene una noticia lista para ser visualizada por el público.
	 * 
	 * @param idnoticia
	 *            Long con el identificador de la noticia.
	 * @param idioma
	 *            String con el idioma
	 * @return Noticia
	 * @throws Exception
	 */
	public Noticia obtenerNoticia(Long idnoticia, String idioma)
			throws Exception {
		try {

			NoticiaDelegate noticiadel = DelegateUtil.getNoticiasDelegate();
			Noticia noticia = noticiadel.obtenerNoticia(idnoticia);
			noticia.setIdi(idioma);
			noticia.getTipo().setIdi(idioma);
			return noticia;

		} catch (Exception e) {
			throw new Exception(" [obtenerNoticia, idnoticia=" + idnoticia
					+ ", idioma=" + idioma + " ] Error: " + e.getMessage()
					+ "\n Stack="
					+ Cadenas.statcktrace2String(e.getStackTrace(), 3));
		}

	}

	/**
	 * Metodo que devuleve un listado de las listas de distribucion de un
	 * microsite
	 * 
	 * @param microsite
	 *            Microsite
	 * @param idioma
	 *            String con el idioma
	 * @return ArrayList Lista de objetos "Tiposervicio".
	 * @throws Exception
	 */
	public List<?> obtenerListadoDistribucionMicrosite(Long idmicrosite)
			throws Exception {
		try {
			LDistribucionDelegate distribDel = DelegateUtil
					.getLlistaDistribucionDelegate();
			distribDel.init(idmicrosite);
			return distribDel.listarListaDistribucion();
		} catch (Exception e) {
			throw new Exception(
					" [obtenerListadoDistribucionMicrosite, idsite="
							+ idmicrosite + " ] Error: " + e.getMessage()
							+ "\n Stack="
							+ Cadenas.statcktrace2String(e.getStackTrace(), 3));
		}
	}

	/**
	 * Metodo que devuleve un listado de servicios ofrecidos por un microsite
	 * 
	 * @param microsite
	 *            Microsite
	 * @param idioma
	 *            String con el idioma
	 * @return ArrayList Lista de objetos "Tiposervicio".
	 * @throws Exception
	 */
	public ArrayList<Object> obtenerListadoServiciosMicrosite(
			Microsite microsite, String idioma) throws DelegateException {

		ArrayList<Object> listserofrtoken = new ArrayList<Object>();
		ArrayList<Object> listserofr = new ArrayList<Object>();
		if (microsite.getServiciosOfrecidos() != null) {
			listserofrtoken = Cadenas.getArrayListFromString(microsite
					.getServiciosOfrecidos());
		}

		Iterator<Object> iter = listserofrtoken.iterator();
		while (iter.hasNext()) {
			TiposervicioDelegate tiposerdel = DelegateUtil
					.getTiposervicioDelegate();
			Tiposervicio tiposervicio = tiposerdel
					.obtenerTiposervicio(new Long((String) iter.next()));
			listserofr.add(tiposervicio);
		}

		return listserofr;
	}

	/**
	 * Obtiene un listado de centros asociados a una Unidad Administrativa.
	 * 
	 * @param coduo
	 *            Long con el id de la ua.
	 * @param idioma
	 *            String con el idioma
	 * @return Collection Coleccion de objetos
	 *         "UnitatAdministrativaQueryServiceAdapter"
	 * @throws Exception
	 */
	public Collection<?> obtenerUacentres(Long coduo, String idioma)
			throws Exception {

		try {

			RolsacQueryService rqs = APIUtil.getRolsacQueryService();

			// Obtener UA.
			UnitatAdministrativaCriteria uaCriteria = new UnitatAdministrativaCriteria();
			uaCriteria.setId(coduo.toString());
			uaCriteria.setIdioma(idioma);
			UnitatAdministrativaQueryServiceAdapter ua = rqs
					.obtenirUnitatAdministrativa(uaCriteria);

			// Obtener edificios asociados y construir dirección.
			EdificiCriteria edificiCriteria = new EdificiCriteria();
			List<?> direcciones = ua.llistarEdificis(edificiCriteria);

			return direcciones;

		} catch (Exception e) {

			throw new Exception(" [obtenerUacentres, coduo=" + coduo
					+ ", idioma=" + idioma + " ] Error: " + e.getMessage()
					+ "\n Stack="
					+ Cadenas.statcktrace2String(e.getStackTrace(), 3));

		}

	}

	/**
	 * Obtenr los detalles de una Unidad Administrativa
	 * 
	 * @param coduo
	 *            codigo de la Unidad Administrativa
	 * @param idioma
	 *            idioma String con el idioma
	 * @return UnitatAdministrativaQueryServiceAdapter Objeto
	 *         UnitatAdministrativaQueryServiceAdapter
	 * @throws Exception
	 */
	public UnitatAdministrativaQueryServiceAdapter getUaDetails(String coduo,
			String idioma) throws Exception {

		try {

			RolsacQueryService rqs = APIUtil.getRolsacQueryService();

			// Obtener UA.
			UnitatAdministrativaCriteria uaCriteria = new UnitatAdministrativaCriteria();
			uaCriteria.setId(coduo.toString());
			uaCriteria.setIdioma(idioma);
			UnitatAdministrativaQueryServiceAdapter ua = rqs
					.obtenirUnitatAdministrativa(uaCriteria);

			return ua;

		} catch (Exception e) {

			throw new Exception(" [getUaDetails, coduo=" + coduo + ", idioma="
					+ idioma + " ] Error: " + e.getMessage() + "\n Stack="
					+ Cadenas.statcktrace2String(e.getStackTrace(), 3));

		}

	}

	/* **************** METODOS PRIVADOS ******************* */

	/**
	 * Método que devuelve una lista con el menú según el idioma.
	 * 
	 * @param idi
	 * @return ArrayList
	 */
	private ArrayList<MenuFront> montarmenu(Long idmicrosite, String idi)
			throws DelegateException {
		ArrayList<MenuFront> listadomenu = new ArrayList<MenuFront>();

		// los menus de primer nivel ya salen ordenados
		MenuDelegate menudel = DelegateUtil.getMenuDelegate();
		// recorrer los menus
		Iterator<?> iter = menudel.listarMenuMicrosite(idmicrosite,
				new Long(0), "S", idi).iterator();
		while (iter.hasNext()) {
			Menu menu = (Menu) iter.next();
			MenuFront menufront = new MenuFront(menu);
			menufront.setIdi(idi);

			// recorrer las paginas y coger las visibles y no caducadas
			Iterator<?> iterpaginas = menu.getContenidos().iterator();
			while (iterpaginas.hasNext()) {
				String iditmp = idi;
				Contenido conte = (Contenido) iterpaginas.next();
				TraduccionContenido tracon = (TraduccionContenido) conte
						.getTraduccion(idi);
				if (tracon == null) {
					iditmp = Idioma.getIdiomaPorDefecto();
					tracon = (TraduccionContenido) conte.getTraduccion(iditmp);
				}
				if ((tracon != null) && (tracon.getTitulo() != null)) {
					if ((conte.getVisible().equals("S"))
							&& (Fechas.vigente(conte.getFpublicacion(),
									conte.getFcaducidad()))) {
						conte.setIdi(iditmp);
						if ((tracon.getUrl() != null)
								&& (tracon.getUrl().indexOf("http") != -1)) {
							conte.setUrlExterna("true");
						} else {
							conte.setUrlExterna("false");
						}
						menufront.getListacosas().add(conte);
					}
				}
			}

			// recoger los submenus. y dentro de los submenus recorrer las
			// paginas y coger las visibles y no caducadas.
			Iterator<?> itermenus = menudel.listarMenuMicrosite(idmicrosite,
					menu.getId(), "S", idi).iterator();
			while (itermenus.hasNext()) {
				Menu submenu = (Menu) itermenus.next();
				MenuFront menufrontsub = new MenuFront(submenu);
				menufrontsub.setIdi(idi);

				// recorrer las paginas y coger las visibles y no caducadas
				Iterator<?> iterpaginassub = submenu.getContenidos().iterator();
				while (iterpaginassub.hasNext()) {
					String iditmp = idi;
					Contenido contesub = (Contenido) iterpaginassub.next();
					TraduccionContenido tracon = (TraduccionContenido) contesub
							.getTraduccion(idi);
					if (tracon == null) {
						iditmp = Idioma.getIdiomaPorDefecto();
						tracon = (TraduccionContenido) contesub
								.getTraduccion(iditmp);
					}
					if ((tracon != null) && (tracon.getTitulo() != null)) {
						if ((contesub.getVisible().equals("S"))
								&& (Fechas.vigente(contesub.getFpublicacion(),
										contesub.getFcaducidad()))) {
							contesub.setIdi(iditmp);
							if ((tracon.getUrl() != null)
									&& (tracon.getUrl().indexOf("http") != -1)) {
								contesub.setUrlExterna("true");
							} else {
								contesub.setUrlExterna("false");
							}
							menufrontsub.getListacosas().add(contesub);
						}
					}
				}

				// los elementos que cuelgan del primer nivel tienen que
				// reordenarse
				menufront.getListacosas().add(menufrontsub);
			}

			menufront
					.setListacosas(this.ordenarlista(menufront.getListacosas()));
			menufront.setIdi(idi);
			listadomenu.add(menufront);

		}
		return listadomenu;

	}

	/**
	 * Comparar Menu
	 */
	private static class ComparatorMenu implements Comparator {
		@Override
		public int compare(Object element1, Object element2) {

			Integer lower1 = new Integer(0);
			Integer lower2 = new Integer(0);
			if (element1 instanceof Contenido) {
				lower1 = new Integer(((Contenido) element1).getOrden());
			} else {
				lower1 = new Integer(((MenuFront) element1).getOrden());
			}

			if (element2 instanceof Contenido) {
				lower2 = new Integer(((Contenido) element2).getOrden());
			} else {
				lower2 = new Integer(((MenuFront) element2).getOrden());
			}

			return lower1.compareTo(lower2);
		}
	}

	/**
	 * Método privado utilizado para ordenar una lista
	 * 
	 * @param listaoriginal
	 * @return Arraylist lista ordenada
	 */
	private ArrayList<Traducible> ordenarlista(
			ArrayList<Traducible> listaoriginal) {

		ArrayList<Traducible> listaresultante = new ArrayList<Traducible>(
				listaoriginal);
		Comparator comp = new ComparatorMenu();
		Collections.sort(listaresultante, comp);

		return listaresultante;

	}

}
