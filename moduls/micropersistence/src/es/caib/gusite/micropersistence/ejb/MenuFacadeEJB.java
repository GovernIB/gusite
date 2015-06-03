package es.caib.gusite.micropersistence.ejb;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;

import org.apache.struts.upload.FormFile;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import es.caib.gusite.micromodel.Archivo;
import es.caib.gusite.micromodel.Auditoria;
import es.caib.gusite.micromodel.Contenido;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.Menu;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.TraduccionMenu;
import es.caib.gusite.micromodel.TraduccionMenuPK;
import es.caib.gusite.micropersistence.delegate.ArchivoDelegate;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.IdiomaDelegate;

/**
 * SessionBean para consultar Menu.
 * 
 * @ejb.bean name="sac/micropersistence/MenuFacade"
 *           jndi-name="es.caib.gusite.micropersistence.MenuFacade"
 *           type="Stateless" view-type="remote" transaction-type="Container"
 * 
 * @ejb.transaction type="Required"
 * 
 * @author Indra
 */
@SuppressWarnings("unchecked")
public abstract class MenuFacadeEJB extends HibernateEJB {

	private static final long serialVersionUID = -2985954631796346221L;

	private static class ComparatorMenu implements Comparator<Object> {

		@Override
		public int compare(Object element1, Object element2) {

			Integer lower1 = new Integer(0);
			Integer lower2 = new Integer(0);
			if (element1 instanceof Contenido) {
				lower1 = new Integer(((Contenido) element1).getOrden());
			} else {
				lower1 = new Integer(((Menu) element1).getOrden());
			}

			if (element2 instanceof Contenido) {
				lower2 = new Integer(((Contenido) element2).getOrden());
			} else {
				lower2 = new Integer(((Menu) element2).getOrden());
			}

			return lower1.compareTo(lower2);
		}

	}

	/**
	 * @ejb.create-method
	 * @ejb.permission unchecked="true"
	 */
	@Override
	public void ejbCreate() throws CreateException {
		super.ejbCreate();
	}

	/**
	 * Inicializo los parámetros de la consulta de Menu.... No está bien hecho
	 * debería ser Statefull
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public void init(Long site) {
		super.tampagina = 10;
		super.pagina = 0;
		super.select = "";
		super.from = " from Menu menu join menu.traducciones trad ";
		super.where = "where trad.id.codigoIdioma = '"
				+ Idioma.getIdiomaPorDefecto() + "' and menu.microsite.id = "
				+ site.toString();
		super.whereini = " ";
		super.orderby = " order by menu.orden";

		super.camposfiltro = new String[] {};
		super.cursor = 0;
		super.nreg = 0;
		super.npags = 0;
	}

	/**
	 * Inicializo los parámetros de la consulta de Menu.... No está bien hecho
	 * debería ser Statefull
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public void init() {
		super.tampagina = 10;
		super.pagina = 0;
		super.select = "";
		super.from = " from Menu menu join menu.traducciones trad ";
		super.where = "";
		super.whereini = " ";
		super.orderby = "";

		super.camposfiltro = new String[] {};
		super.cursor = 0;
		super.nreg = 0;
		super.npags = 0;
	}

	/**
	 * Crea o actualiza un Menu
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public Long grabarMenu(Menu menu) throws DelegateException {

		Session session = this.getSession();
		boolean nuevo = false;
		try {
			Transaction tx = session.beginTransaction();
			Map<String, TraduccionMenu> listaTraducciones = new HashMap<String, TraduccionMenu>();

			if (menu.getId() == null) {
				nuevo = true;
			}

			if (nuevo) {
				Iterator<TraduccionMenu> it = menu.getTraducciones().values()
						.iterator();
				while (it.hasNext()) {
					TraduccionMenu trd = it.next();
					listaTraducciones.put(trd.getId().getCodigoIdioma(), trd);
				}
				menu.setTraducciones(null);
			}

			session.saveOrUpdate(menu);
			session.flush();

			if (nuevo) {
				for (TraduccionMenu trad : listaTraducciones.values()) {
					trad.getId().setCodigoMenu(menu.getId());
					session.saveOrUpdate(trad);
				}
				session.flush();
				menu.setTraducciones(listaTraducciones);
			}

			tx.commit();
			this.close(session);

			int op = (nuevo) ? Auditoria.CREAR : Auditoria.MODIFICAR;
			this.grabarAuditoria(menu, op);

			return menu.getId();

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Obtiene un menu
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Menu obtenerMenu(Long id) {

		Session session = this.getSession();
		try {
			Menu menu = (Menu) session.get(Menu.class, id);
			return menu;

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Obtiene un menu sin cargar las paginas de contenido
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Menu obtenerMenuThin(Long id, String idioma) {

		Session session = this.getSession();
		try {
			Menu men = new Menu();
			String sql = "select menu.id, menu.microsite.id, menu.orden, menu.visible, menu.modo, menu.padre, trad.nombre "
					+ "from Menu menu"
					+ " join menu.traducciones trad "
					+ "where trad.id.codigoIdioma = '"
					+ idioma
					+ "'"
					+ " and menu.id = " + id;

			Query query = session.createQuery(sql);

			ScrollableResults scr = query.scroll();
			scr.first();
			Object[] fila = scr.get();

			men.setId((Long) fila[0]);
			Microsite microsite = new Microsite();
			microsite.setId((Long) fila[1]);
			men.setMicrosite(microsite);
			men.setOrden(((Integer) fila[2]).intValue());
			men.setVisible((String) fila[3]);
			men.setModo((String) fila[4]);
			men.setPadre((Long) fila[5]);
			TraduccionMenu trad = new TraduccionMenu();
			trad.setNombre((String) fila[6]);
			men.setTraduccion(idioma, trad);

			scr.close();
			return men;

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Listado plano de todos los objetos menu y contenidos de un microsite.
	 * Está ordenado por el campo orden.
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public ArrayList<Object> ObtenerObjetosMenu(Long idsite) {

		ArrayList<Object> listaobjetos = new ArrayList<Object>();
		Session session = this.getSession();
		try {
			String sqlselect = "";
			String sqlfrom = " from Menu menu ";
			String sqlwhere = " where menu.microsite.id = " + idsite.toString()
					+ " and menu.padre = 0 ";
			String sqlorderby = " order by menu.orden";
			Query query = session.createQuery(sqlselect + sqlfrom + sqlwhere
					+ sqlorderby);

			// Recorrer los menus
			List<?> list = query.list();
			Iterator<?> iter = list.iterator();
			while (iter.hasNext()) {
				Menu menu = (Menu) iter.next();

				String hql = "from TraduccionMenu trad"
						+ " where trad.id.codigoMenu = "
						+ menu.getId().toString()
						+ " and trad.id.codigoIdioma = '"
						+ Idioma.getIdiomaPorDefecto() + "'";

				query = session.createQuery(hql);
				List<?> tradList = query.list();
				menu.getTraducciones().put(Idioma.getIdiomaPorDefecto(),
						(TraduccionMenu) tradList.get(0));

				listaobjetos.add(menu);

				// recorrer las paginas
				Iterator<?> iterpaginas = menu.getContenidos().iterator();
				while (iterpaginas.hasNext()) {
					Contenido conte = (Contenido) iterpaginas.next();
					listaobjetos.add(conte);
				}

				// recoger los submenus. y dentro de los submenus recorrer las
				// paginas.
				sqlwhere = " where menu.padre = " + menu.getId();
				Query querysubmenus = session.createQuery(sqlselect + sqlfrom
						+ sqlwhere + sqlorderby);

				Iterator<?> itermenus = querysubmenus.list().iterator();
				while (itermenus.hasNext()) {
					Menu submenu = (Menu) itermenus.next();

					hql = "from TraduccionMenu trad"
							+ " where trad.id.codigoMenu = "
							+ submenu.getId().toString()
							+ " and trad.id.codigoIdioma = '"
							+ Idioma.getIdiomaPorDefecto() + "'";
					query = session.createQuery(hql);
					tradList = query.list();
					submenu.getTraducciones().put(Idioma.getIdiomaPorDefecto(),
							(TraduccionMenu) tradList.get(0));

					listaobjetos.add(submenu);

					// recorrer las paginas
					Iterator<?> iterpaginassub = submenu.getContenidos()
							.iterator();
					while (iterpaginassub.hasNext()) {
						Contenido contesub = (Contenido) iterpaginassub.next();
						listaobjetos.add(contesub);
					}
				}
			}

			listaobjetos = this.ordenarlista(listaobjetos);

		} catch (HibernateException he) {
			throw new EJBException(he);
		} catch (Exception e) {
			throw new EJBException(e);
		} finally {
			this.close(session);
		}

		return listaobjetos;
	}

	private ArrayList<Object> ordenarlista(ArrayList<Object> listaoriginal) {

		ArrayList<Object> listaresultante = new ArrayList<Object>(listaoriginal);
		Comparator<Object> comp = new ComparatorMenu();
		Collections.sort(listaresultante, comp);
		return listaresultante;
	}

	/**
	 * Lista todos los menus
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<?> listarMenus() {

		Session session = this.getSession();
		try {
			Query query = session.createQuery(this.select + this.from
					+ this.where + this.orderby);
			return query.list();

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Lista todos los menus poniendole un idioma por defecto
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public ArrayList<Menu> listarMenus(String idioma) {

		Session session = this.getSession();
		try {
			this.parametrosCons(); // Establecemos los parámetros de la
									// paginación
			Query query = session.createQuery(this.select + this.from
					+ this.where + this.orderby);
			query.setFirstResult(this.cursor - 1);
			query.setMaxResults(this.tampagina);
			return this.crearlistadostateful(query.list(), idioma);

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Lista un menu poniendole un idioma por defecto
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public ArrayList<?> listarMenuMicrosite(Long idmicrosite, Long padre,
			String visible, String idioma) {

		Session session = this.getSession();
		try {
			Criteria criteri = session.createCriteria(Menu.class);
			criteri.add(Restrictions.eq("microsite.id", idmicrosite));
			criteri.add(Restrictions.eq("padre", padre));
			if (visible != "T") {
				criteri.add(Restrictions.eq("visible", visible));
			}
			criteri.addOrder(Order.asc("orden"));
			List<?> list = criteri.list();

			return this.crearlistadostateful(list, idioma);

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * borra un Menu
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public void borrarMenu(Long id) throws DelegateException {

		Session session = this.getSession();
		
		try {
			
			Transaction tx = session.beginTransaction();
			ArchivoDelegate archivoDelegate = DelegateUtil.getArchivoDelegate();
			Menu menu = (Menu) session.get(Menu.class, id);

			session.createQuery("delete TraduccionMenu tmenu where tmenu.id.codigoMenu = " + id.toString()).executeUpdate();
			
			// Tratar archivos de entidad Contenido.
			List<Contenido> contenidos = menu.getContenidos();
			for (Contenido c : contenidos) {
				// Archivos relacionados.
				List<Archivo> listaArchivos = (List<Archivo>)session.createQuery("select arch from Archivo arch where arch.pagina = " + c.getId()).list();
				archivoDelegate.borrarArchivos(listaArchivos);				
			}
			
			session.createQuery("delete Contenido conten where conten.menu.id = " + id.toString()).executeUpdate();
			session.createQuery("delete Menu menu where menu.id = " + id.toString()).executeUpdate();
			session.flush();
			
			// Tratar archivos de entidad Menu.
			Long idImagenMenu = null;
			if (menu.getImagenmenu() != null) {
				idImagenMenu = menu.getImagenmenu().getId();
			}
			archivoDelegate.borrarArchivo(idImagenMenu);
			
			tx.commit();
			
			this.close(session);

			this.grabarAuditoria(menu, Auditoria.ELIMINAR);

		} catch (HibernateException he) {
			
			throw new EJBException(he);
			
		} finally {
			
			this.close(session);
			
		}
		
	}

	/**
	 * Actualizamos los ordenes de los objetos que están por debajo del nuevo
	 * menú creado o eliminado sumando 1 o restando 1
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public void Reordena(int orden, char op, Long micro)
			throws DelegateException {

		ArrayList<?> listaobj = this.ObtenerObjetosMenu(micro);
		Session session = this.getSession();
		try {
			Iterator<?> it = listaobj.iterator();
			Object obj = null;
			Menu men = null;
			Contenido con = null;

			int sentido = 0;
			if (op == 'a') {
				sentido = 1;
			}
			if (op == 'b') {
				sentido = -1;
			}

			while (it.hasNext()) {
				obj = it.next();

				if (obj instanceof Menu) {
					men = (Menu) obj;
					if (men.getOrden() > orden) {
						men.setOrden(men.getOrden() + sentido);
						session.saveOrUpdate(men);
					}
				}

				if (obj instanceof Contenido) {
					con = (Contenido) obj;
					if (con.getOrden() > orden) {
						con.setOrden(con.getOrden() + sentido);
						session.saveOrUpdate(con);
					}
				}
			}
			session.flush();
			this.close(session);

			for (Object object : listaobj) {

				if (object instanceof Menu) {
					this.grabarAuditoria((Menu) object, Auditoria.MODIFICAR);
				} else {
					// Contenido
					this.grabarAuditoria((Contenido) object,
							Auditoria.MODIFICAR);
				}
			}

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	private ArrayList<Menu> crearlistadostateful(List<?> listado, String idioma) {

		ArrayList<Menu> lista = new ArrayList<Menu>();
		Iterator<?> iter = listado.iterator();
		Menu menu;
		while (iter.hasNext()) {
			menu = new Menu();
			menu = (Menu) iter.next();
			menu.setIdi(idioma);
			lista.add(menu);
		}
		return lista;
	}

	/**
	 * Lista los Menus para usar en Combos Obtenemos los menus de un microsite
	 * Nivel 2
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<?> listarCombo(String microsite) {

		Session session = this.getSession();
		try {
			String hql = "from Menu men" + " join men.traducciones trad"
					+ " where trad.id.codigoIdioma = '"
					+ Idioma.getIdiomaPorDefecto() + "'";

			if (microsite != null) {
				hql += " and men.microsite.id = " + microsite;
			}
			Query query = session.createQuery(hql);
			return query.list();

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Lista los Menus para usar en Combos Obtenemos los menus de un microsite
	 * Nivel 3
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<?> padreCombo(String microsite) {

		Session session = this.getSession();
		try {
			String hql = "from Menu men" + " join men.traducciones trad"
					+ " where trad.id.codigoIdioma = '"
					+ Idioma.getIdiomaPorDefecto() + "'";

			if (microsite != null) {
				hql += " and men.microsite.id = " + microsite;
			}
			Query query = session.createQuery(hql);
			return query.list();

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Comprueba que el elemento pertenece al Microsite
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public boolean checkSite(Long site, Long id) {

		Session session = this.getSession();
		try {
			Query query = session
					.createQuery("from Menu menu where menu.microsite.id = "
							+ site.toString() + " and menu.id = "
							+ id.toString());
			return query.list().isEmpty();

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Actualiza todo el arbol del menu
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public void actualizarMenus(Long idmicro, Long ids[], String[] visibles,
			String[] modos, Integer[] ordenes, Long[] idPadres, String[] tipos,
			FormFile[] imagenes, String[] imagenesnom, boolean[] imagenesbor,
			String[] traducciones) {

		Session session = this.getSession();
		
		try {
			
			IdiomaDelegate idiomaDelegate = DelegateUtil.getIdiomaDelegate();
			ArchivoDelegate archivoDelegate = DelegateUtil.getArchivoDelegate();
			Long idArchivoBorrar = null;
			
			List<?> lang = null;
			try {
				lang = idiomaDelegate.listarIdiomas();
			} catch (DelegateException e) {
				throw new EJBException(e);
			}

			// Obtenemos en una consulta todos los objetos
			List<Menu> menus = session.createQuery(
					"from Menu menu where menu.microsite.id = "
							+ idmicro.toString()).list();

			Map<Long, Menu> menusIdx = new HashMap<Long, Menu>();
			for (Menu m : menus) {
				menusIdx.put(m.getId(), m);
			}
			
			for (Menu m : menus) {

				int indice_m = -1;
				for (int j = 0; j < ids.length; j++) {
					if ((tipos[j].equals("m"))
							&& (ids[j].toString()).equals(m.getId().toString())) {
						indice_m = j;
					}
				}

				if (indice_m != -1) {
					
					m.setOrden(ordenes[indice_m].intValue());
					m.setPadre(idPadres[indice_m]);
					m.setVisible(visibles[indice_m]);
					m.setModo(modos[indice_m]);

					// actualizamos los iconos
					FormFile imagen = imagenes[indice_m];
					if (imagen != null && imagen.getFileSize() > 0) {
						
						Archivo arc = m.getImagenmenu();
						if (arc == null) {
							arc = new Archivo();
						}
						
						arc.setMime(imagen.getContentType());
						arc.setNombre(imagen.getFileName());
						arc.setPeso(imagen.getFileSize());
						arc.setIdmicrosite(m.getMicrosite().getId());
												
						try {
							arc.setDatos(imagen.getFileData());
						} catch (FileNotFoundException e) {
							throw new EJBException(e);
						} catch (IOException e) {
							throw new EJBException(e);
						}
						
						archivoDelegate.insertarArchivo(arc);
						
						m.setImagenmenu(arc);

					} else if (imagenesbor[indice_m]) {
						
						idArchivoBorrar = m.getImagenmenu().getId();
						m.setImagenmenu(null);
												
					}

					if (m.getImagenmenu() != null) {
						if (imagenesnom[indice_m] != null) {
							m.getImagenmenu().setNombre(
									"" + imagenesnom[indice_m]);
						}
					}

					for (int j = 0; j < lang.size(); j++) {
						TraduccionMenu tradm;
						if (m.getTraducciones().containsKey(
								((Idioma) lang.get(j)).getLang())) {
							tradm = m.getTraducciones().get(
									((Idioma) lang.get(j)).getLang());
							tradm.setNombre(traducciones[(indice_m * lang
									.size()) + j]);
						} else {
							tradm = new TraduccionMenu();
							tradm.setId(new TraduccionMenuPK());
							tradm.getId().setCodigoIdioma(
									((Idioma) lang.get(j)).getLang());
							tradm.getId().setCodigoMenu(m.getId());
							tradm.setNombre(traducciones[(indice_m * lang
									.size()) + j]);

							m.getTraducciones().put(
									((Idioma) lang.get(j)).getLang(), tradm);
						}
					}

					session.saveOrUpdate(m);
					session.flush();
					
					// Toca borrar archivo con imagen de menú asociada.
					if (idArchivoBorrar != null)
						archivoDelegate.borrarArchivo(idArchivoBorrar);

					// Actualizamos sus contenidos
					Iterator<?> itcon = m.getContenidos().iterator();
					while (itcon.hasNext()) {
						Contenido con = (Contenido) itcon.next();
						int indice = -1;
						for (int j = 0; j < ids.length; j++) {
							if ((tipos[j].equals("c1") || tipos[j].equals("c2"))
									&& (ids[j].toString()).equals(con.getId()
											.toString())) {
								indice = j;
							}
						}

						if (indice != -1) {
							con.setOrden(ordenes[indice].intValue());
							con.setMenu(menusIdx.get(idPadres[indice]));
							con.setVisible(visibles[indice]);
							session.saveOrUpdate(con);
							session.flush();
						}
					}

				}
				session.flush();
			}
			this.close(session);

			Iterator<Menu> iter = menus.iterator();
			while (iter.hasNext()) {
				Menu menu = iter.next();
				this.grabarAuditoria(menu, Auditoria.MODIFICAR);
			}

		} catch (HibernateException he) {
			throw new EJBException(he);
		} catch (DelegateException e) {
			throw new EJBException(e);
		} finally {
			this.close(session);
		}
	}

}
