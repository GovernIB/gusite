package es.caib.gusite.micropersistence.ejb;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import javax.ejb.CreateException;
import javax.ejb.EJBException;

import es.caib.gusite.micromodel.*;
import org.apache.struts.upload.FormFile;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;

import es.caib.gusite.micropersistence.delegate.ContenidoDelegate;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.IdiomaDelegate;

/**
 * SessionBean para consultar Menu.
 *
 * @ejb.bean
 *  name="sac/micropersistence/MenuFacade"
 *  jndi-name="es.caib.gusite.micropersistence.MenuFacade"
 *  type="Stateless"
 *  view-type="remote"
 *  transaction-type="Container"
 *
 * @ejb.transaction type="Required"
 * 
 * @author Indra
 */
public abstract class MenuFacadeEJB extends HibernateEJB {

	private static final long serialVersionUID = -2985954631796346221L;

	private static class ComparatorMenu implements Comparator<Object> {

	    public int compare(Object element1, Object element2) {
	    	
	    	Integer lower1 = new Integer(0) ;
	    	Integer lower2 = new Integer(0) ;
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
    public void ejbCreate() throws CreateException {
        super.ejbCreate();
    }

    /**
     * Inicializo los parámetros de la consulta de Menu....
     * No está bien hecho debería ser Statefull
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public void init(Long site) {
    	super.tampagina = 10;
    	super.pagina = 0;
    	super.select = "";
    	super.from = " from Menu menu join menu.traducciones trad ";
    	super.where = "where trad.id.codigoIdioma = '" + Idioma.getIdiomaPorDefecto() + "' and menu.microsite.id = " + site.toString();
    	super.whereini = " ";
    	super.orderby = " order by menu.orden";

    	super.camposfiltro = new String[] {};
    	super.cursor = 0;
    	super.nreg = 0;
    	super.npags = 0;
    }

    /**
     * Inicializo los parámetros de la consulta de Menu....
     * No está bien hecho debería ser Statefull
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
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
     */
    public Long grabarMenu(Menu menu) throws DelegateException {

        Session session = getSession();
        boolean nuevo = false;
        try {
            Transaction tx = session.beginTransaction();
            Map<String, TraduccionMenu> listaTraducciones = new HashMap<String, TraduccionMenu>();

            if (menu.getId() == null) {
                nuevo = true;
            }

            if (nuevo) {
                Iterator<TraduccionMenu> it = menu.getTraducciones().values().iterator();
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
            close(session);

            Auditoria auditoria = new Auditoria();
            auditoria.setMicrosite(menu.getMicrosite());
            auditoria.setEntidad(Menu.class.getSimpleName());
            auditoria.setIdEntidad(menu.getId().toString());
            int op = (nuevo) ? Auditoria.CREAR : Auditoria.MODIFICAR;
            auditoria.setOperacion(op);
            DelegateUtil.getAuditoriaDelegate().grabarAuditoria(auditoria);

            return menu.getId();

        } catch (HibernateException he) {
            throw new EJBException(he);
        } catch (DelegateException e) {
            throw new DelegateException(e);
        } finally {
            close(session);
        }
    }

    /**
     * Obtiene un menu
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public Menu obtenerMenu(Long id) {

        Session session = getSession();
        try {
        	Menu menu = (Menu) session.get(Menu.class, id);
            return menu;

        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }

    /**
     * Obtiene un menu sin cargar las paginas de contenido
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public Menu obtenerMenuThin(Long id, String idioma) {

        Session session = getSession();
        try {
        	Menu men = new Menu();
            String sql = "select menu.id, menu.microsite.id, menu.orden, menu.visible, menu.modo, menu.padre, trad.nombre " +
                    "from Menu menu" +
                    " join menu.traducciones trad " +
                    "where trad.id.codigoIdioma = '" + idioma + "'" +
                    " and menu.id = " + id;
        	
        	Query query = session.createQuery(sql);
        	
        	ScrollableResults scr = query.scroll();
        	scr.first();
            Object[] fila = (Object[]) scr.get();
        	
        	men.setId((Long) fila[0]);
        	Microsite microsite = new Microsite();
        	microsite.setId((Long) fila[1]);
        	men.setMicrosite(microsite);
        	men.setOrden(((Integer) fila[2]).intValue() );
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
            close(session);
        }
    }    
    
    /**
     * Listado plano de todos los objetos menu y contenidos de un microsite. Está ordenado por el campo orden.
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */    
    public ArrayList<Object> ObtenerObjetosMenu(Long idsite) {

        ArrayList<Object> listaobjetos = new ArrayList<Object>();
        Session session = getSession();
        try {
            String sqlselect = "";
            String sqlfrom = " from Menu menu ";
            String sqlwhere = " where menu.microsite.id = " + idsite.toString() + " and menu.padre = 0 ";
            String sqlorderby = " order by menu.orden";
            Query query = session.createQuery(sqlselect + sqlfrom + sqlwhere + sqlorderby);

            // Recorrer los menus
            List<?> list = query.list();
            Iterator<?> iter = list.iterator();
            while (iter.hasNext()) {
                Menu menu = (Menu) iter.next();

                String hql = "from TraduccionMenu trad" +
                        " where trad.id.codigoMenu = " + menu.getId().toString() +
                        " and trad.id.codigoIdioma = '" + Idioma.getIdiomaPorDefecto() + "'";

                query = session.createQuery(hql);
                List<?> tradList = query.list();
                menu.getTraducciones().put(Idioma.getIdiomaPorDefecto(), (TraduccionMenu) tradList.get(0));

                listaobjetos.add(menu);

                // recorrer las paginas
                Iterator<?> iterpaginas = menu.getContenidos().iterator();
                while (iterpaginas.hasNext()) {
                    Contenido conte = (Contenido) iterpaginas.next();
                    listaobjetos.add(conte);
                }

                // recoger los submenus. y dentro de los submenus recorrer las paginas.
                sqlwhere = " where menu.padre = " + menu.getId();
                Query querysubmenus = session.createQuery(sqlselect + sqlfrom + sqlwhere + sqlorderby);

                Iterator<?> itermenus = querysubmenus.list().iterator();
                while (itermenus.hasNext()) {
                    Menu submenu = (Menu) itermenus.next();

                    hql = "from TraduccionMenu trad" +
                            " where trad.id.codigoMenu = " + submenu.getId().toString() +
                            " and trad.id.codigoIdioma = '" + Idioma.getIdiomaPorDefecto() + "'";
                    query = session.createQuery(hql);
                    tradList = query.list();
                    submenu.getTraducciones().put(Idioma.getIdiomaPorDefecto(), (TraduccionMenu) tradList.get(0));

                    listaobjetos.add(submenu);

                    // recorrer las paginas
                    Iterator<?> iterpaginassub = submenu.getContenidos().iterator();
                    while (iterpaginassub.hasNext()) {
                        Contenido contesub = (Contenido) iterpaginassub.next();
                        listaobjetos.add(contesub);
                    }
                }
            }

            listaobjetos = ordenarlista(listaobjetos);

        } catch (HibernateException he) {
            throw new EJBException(he);
        } catch (Exception e) {
            throw new EJBException(e);
        } finally {
            close(session);
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
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
    public List<?> listarMenus() {

        Session session = getSession();
        try {
        	Query query = session.createQuery(select + from + where + orderby);
        	return query.list();

        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }

    /**
     * Lista todos los menus poniendole un idioma por defecto
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
    public ArrayList<Menu> listarMenus(String idioma) {

        Session session = getSession();
        try {
        	parametrosCons(); // Establecemos los parámetros de la paginación
        	Query query = session.createQuery(select + from + where + orderby);
            query.setFirstResult(cursor - 1);
            query.setMaxResults(tampagina);
            return crearlistadostateful(query.list(), idioma);

        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }    

    /**
     * Lista un menu poniendole un idioma por defecto
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
    public ArrayList<?> listarMenuMicrosite(Long idmicrosite, Long padre, String visible, String idioma) {

        Session session = getSession();
        try {
            Criteria criteri = session.createCriteria(Menu.class);
            criteri.add(Expression.eq("microsite.id", idmicrosite));
            criteri.add(Expression.eq("padre", padre));
            if (visible != "T") {
                criteri.add(Expression.eq("visible", visible));
            }
            criteri.addOrder(Order.asc("orden"));
            List<?> list = criteri.list();

            return crearlistadostateful(list, idioma);

        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }

    /**
     * borra un Menu
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
     */
    public void borrarMenu(Long id) throws DelegateException {

        Session session = getSession();
        try {
        	Transaction tx = session.beginTransaction();
            Menu menu = (Menu) session.get(Menu.class, id);
            Microsite site = menu.getMicrosite();

        	session.createQuery("delete TraduccionMenu tmenu where tmenu.id.codigoMenu = " + id.toString()).executeUpdate();
        	session.createQuery("delete Contenido conten where conten.menu.id = " + id.toString()).executeUpdate();
        	session.createQuery("delete Menu menu where menu.id = " + id.toString()).executeUpdate();
            session.flush();
            tx.commit();
            close(session);

            Auditoria auditoria = new Auditoria();
            auditoria.setMicrosite(site);
            auditoria.setEntidad(Menu.class.getSimpleName());
            auditoria.setIdEntidad(id.toString());
            auditoria.setOperacion(Auditoria.ELIMINAR);
            DelegateUtil.getAuditoriaDelegate().grabarAuditoria(auditoria);

        } catch (HibernateException he) {
            throw new EJBException(he);
        } catch (DelegateException e) {
            throw new DelegateException(e);
        } finally {
            close(session);
        }
    }

    /**
     * Actualizamos los ordenes de los objetos que están por debajo del nuevo menú creado o eliminado
     * sumando 1 o restando 1
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
     */
    public void Reordena (int orden, char op, Long micro) throws DelegateException  {

        ContenidoDelegate bdConte = DelegateUtil.getContenidoDelegate();
        ArrayList<?> listaobj = ObtenerObjetosMenu(micro);
        Session session = getSession();
        try {
            Microsite site = (Microsite) session.get(Microsite.class, micro);
            Iterator<?> it = listaobj.iterator();
            Object obj = null;
       		Menu men = null;
       		Contenido con = null;
     		
       		int sentido = 0;
       		if (op == 'a') sentido = 1;
       		if (op == 'b') sentido = -1;

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
            close(session);

            for (Object object : listaobj) {
                Auditoria auditoria = new Auditoria();
                auditoria.setOperacion(Auditoria.MODIFICAR);
                auditoria.setEntidad(object.getClass().getSimpleName());
                auditoria.setMicrosite(site);
                String id = (object instanceof Menu) ? ((Menu) object).getId().toString() : ((Contenido) object).getId().toString();
                auditoria.setIdEntidad(id);
                DelegateUtil.getAuditoriaDelegate().grabarAuditoria(auditoria);
            }
    	
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
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
     * Lista los Menus para usar en Combos
     * Obtenemos los menus de un microsite
     * Nivel 2
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
    public List<?> listarCombo(String microsite) {

        Session session = getSession();
        try {
        	String hql = "from Menu men" +
                    " join men.traducciones trad" +
                    " where trad.id.codigoIdioma = '" + Idioma.getIdiomaPorDefecto() + "'";

        	if (microsite != null) {
                hql += " and men.microsite.id = " + microsite;
            }
        	Query query = session.createQuery(hql);
        	return query.list();
        	
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }

    /**
     * Lista los Menus para usar en Combos
     * Obtenemos los menus de un microsite
     * Nivel 3
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
    public List<?> padreCombo(String microsite) {

        Session session = getSession();
        try {
        	String hql = "from Menu men" +
                    " join men.traducciones trad" +
                    " where trad.id.codigoIdioma = '" + Idioma.getIdiomaPorDefecto() + "'";

            if (microsite != null) {
                hql += " and men.microsite.id = " + microsite;
            }
        	Query query = session.createQuery(hql);
        	return query.list();
        	
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }

    /**
     * Comprueba que el elemento pertenece al Microsite
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
     */
    public boolean checkSite(Long site, Long id) {

        Session session = getSession();
        try {
        	Query query = session.createQuery("from Menu menu where menu.microsite.id = " + site.toString() + " and menu.id = " + id.toString());
        	return query.list().isEmpty();

        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }

    /**
     * Actualiza todo el arbol del menu
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
     */
    public void actualizarMenus(Long idmicro, Long ids[], String[] visibles, String[] modos, Integer[] ordenes, Long[] idPadres, String[] tipos, FormFile[] imagenes, String[] imagenesnom, boolean[] imagenesbor, String[] traducciones) throws DelegateException {

        Session session = getSession();
        try {
            IdiomaDelegate idiomaDelegate = DelegateUtil.getIdiomaDelegate();
            List<?> lang = null;
            try {
                lang = idiomaDelegate.listarIdiomas();
            } catch (DelegateException e) {
                throw new EJBException(e);
            }

            // Obtenemos en una consulta todos los objetos
            List<?> menus = session.createQuery("from Menu menu where menu.microsite.id = " + idmicro.toString()).list();

            for (int i = 0; i < menus.size(); i++) {
                Menu m = (Menu) menus.get(i);

                int indice_m = -1;
                for (int j = 0; j < ids.length; j++) {
                    if ((tipos[j].equals("m")) && (ids[j].toString()).equals(m.getId().toString())) {
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
    	            	try {
							arc.setDatos(imagen.getFileData());
						} catch (FileNotFoundException e) {
							throw new EJBException(e);
						} catch (IOException e) {
							throw new EJBException(e);
						}
    	            	m.setImagenmenu(arc);

    	            } else if (imagenesbor[indice_m]) {
                        m.setImagenmenu(null);
                    }
    	        
    	            if (m.getImagenmenu() != null) {
                        if (imagenesnom[indice_m] != null) {
                            m.getImagenmenu().setNombre("" + imagenesnom[indice_m]);
                        }
                    }

                    Map<String, TraduccionMenu> traduccionesMap = new HashMap<String, TraduccionMenu>();
                    for (int j = 0; j < lang.size(); j++) {
                        TraduccionMenu tradm;
                        if (m.getTraducciones().containsKey(((Idioma) lang.get(j)).getLang())) {
                            tradm = m.getTraducciones().get(((Idioma) lang.get(j)).getLang());
                            tradm.setNombre(traducciones[(indice_m * lang.size()) + j]);
                        } else {
                            tradm = new TraduccionMenu();
                            tradm.setId(new TraduccionMenuPK());
                            tradm.getId().setCodigoIdioma(((Idioma) lang.get(j)).getLang());
                            tradm.getId().setCodigoMenu(m.getId());
                            tradm.setNombre(traducciones[(indice_m * lang.size()) + j]);

                            m.getTraducciones().put(((Idioma) lang.get(j)).getLang(), tradm);
    	            	}
    	            }

                    session.saveOrUpdate(m);
                    session.flush();

    	            // Actualizamos sus contenidos
            		Iterator<?> itcon = m.getContenidos().iterator();
            		while (itcon.hasNext()) {
            			Contenido con = (Contenido) itcon.next();
                    	int indice = -1;
                    	for (int j = 0; j < ids.length; j++) {
                            if ((tipos[j].equals("c1") || tipos[j].equals("c2")) && (ids[j].toString()).equals(con.getId().toString())) {
                                indice = j;
                            }
                        }

                    	if (indice != -1) {
           		   			con.setOrden(ordenes[indice].intValue());
           		   			con.getMenu().setId(idPadres[indice]);
           		   			con.setVisible(visibles[indice]);
           		   			session.saveOrUpdate(con);
           		   			session.flush();
                    	}
            		}
    	            
            	}
        		session.flush();
        	}
            close(session);

            Iterator iter = menus.iterator();
            while (iter.hasNext()) {
                Menu menu = (Menu) iter.next();
                Auditoria auditoria = new Auditoria();
                auditoria.setEntidad(Menu.class.getSimpleName());
                auditoria.setIdEntidad(menu.getId().toString());
                auditoria.setOperacion(Auditoria.MODIFICAR);
                auditoria.setMicrosite(menu.getMicrosite());
                DelegateUtil.getAuditoriaDelegate().grabarAuditoria(auditoria);
            }

        } catch (HibernateException he) {
            throw new EJBException(he);
        } catch (DelegateException e) {
            throw new DelegateException(e);
        } finally {
            close(session);
        }
    }

}
