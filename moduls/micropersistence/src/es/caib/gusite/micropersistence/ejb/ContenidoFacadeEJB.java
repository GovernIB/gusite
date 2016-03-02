package es.caib.gusite.micropersistence.ejb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.store.Directory;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import au.id.jericho.lib.html.Element;
import au.id.jericho.lib.html.HTMLElementName;
import au.id.jericho.lib.html.Source;
import es.caib.gusite.lucene.analysis.Analizador;
import es.caib.gusite.lucene.model.Catalogo;
import es.caib.gusite.lucene.model.ModelFilterObject;
import es.caib.gusite.micromodel.Archivo;
import es.caib.gusite.micromodel.Auditoria;
import es.caib.gusite.micromodel.Contenido;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.IndexObject;
import es.caib.gusite.micromodel.Menu;
import es.caib.gusite.micromodel.TraduccionContenido;
import es.caib.gusite.micromodel.TraduccionMenu;
import es.caib.gusite.micropersistence.delegate.ArchivoDelegate;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.IndexerDelegate;
import es.caib.gusite.micropersistence.delegate.MenuDelegate;

/**
 * SessionBean para consultar Contenido.
 * 
 * @ejb.bean name="sac/micropersistence/ContenidoFacade"
 *           jndi-name="es.caib.gusite.micropersistence.ContenidoFacade"
 *           type="Stateless" view-type="remote" transaction-type="Container"
 * 
 * @ejb.transaction type="Required"
 * 
 * @author Indra
 */
@SuppressWarnings({"deprecation", "unchecked"})
public abstract class ContenidoFacadeEJB extends HibernateEJB {

	private static final long serialVersionUID = 1665804059893814046L;

	/**
	 * @ejb.create-method
	 * @ejb.permission unchecked="true"
	 */
	@Override
	public void ejbCreate() throws CreateException {
		super.ejbCreate();
		try {
			super.langs = DelegateUtil.getIdiomaDelegate().listarLenguajes();
		} catch (Exception ex) {
			throw new EJBException(ex);
		}
	}

	/**
	 * Inicializo los parámetros de la consulta de Contenido....
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public void init(String menu) {
		super.tampagina = 10;
		super.pagina = 0;
		super.select = "";
		super.from = " from Contenido contenido join contenido.traducciones trad ";
		super.where = " where trad.id.codigoIdioma = '"
				+ Idioma.getIdiomaPorDefecto() + "' and contenido.idmenu = "
				+ menu;
		super.whereini = " ";
		super.orderby = " order by contenido.orden";

		super.camposfiltro = new String[] { "contenido.fpublicacion",
				"contenido.fcaducidad", "trad.titulo", "trad.texto",
				"trad.txbeta", "trad.url" };
		super.cursor = 0;
		super.nreg = 0;
		super.npags = 0;
	}

	/**
	 * Inicializo los parámetros de la consulta de Contenido....
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public void init() {
		super.tampagina = 10;
		super.pagina = 0;
		super.select = "";
		super.from = " from Contenido contenido join contenido.traducciones trad ";
		super.where = " where trad.id.codigoIdioma = '"
				+ Idioma.getIdiomaPorDefecto() + "'";
		super.whereini = " ";
		super.orderby = "";

		super.camposfiltro = new String[] { "contenido.fpublicacion",
				"contenido.fcaducidad", "trad.titulo", "trad.url" };
		super.cursor = 0;
		super.nreg = 0;
		super.npags = 0;
	}

	/**
	 * Crea o actualiza un Contenido
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public Long grabarContenido(Contenido contenido) throws DelegateException {

		Session session = this.getSession();
		try {
			boolean nuevo = (contenido.getId() == null) ? true : false;
			Transaction tx = session.beginTransaction();

			Map<String, TraduccionContenido> listaTraducciones = new HashMap<String, TraduccionContenido>();
			if (nuevo) {
				Iterator<TraduccionContenido> it = contenido.getTraducciones()
						.values().iterator();
				while (it.hasNext()) {
					TraduccionContenido trd = it.next();
					listaTraducciones.put(trd.getId().getCodigoIdioma(), trd);
				}
				contenido.setTraducciones(null);
			} else {//TODO: Redmine:1628- buscar solución mediante JPA (traduccion contenido) 
				//buscamos los idiomas que deben permanecer en el contenido
				String listIdiomaBorrar = "";
				Iterator<TraduccionContenido> it = contenido.getTraducciones()
						.values().iterator();
				while (it.hasNext()) {
					TraduccionContenido trd = it.next();
					listIdiomaBorrar += "'" +trd.getId().getCodigoIdioma()+"'";
					if(it.hasNext()){
						listIdiomaBorrar += "," ;						
					}
				}
				// Borramos los idiomas que no pertenecen a contenido y existen en la BBDD
				if(!listIdiomaBorrar.isEmpty()){ 
					Query query = session.createQuery("select tradCon from TraduccionContenido tradCon where tradCon.id.codigoContenido = " + contenido.getId() + " and tradCon.id.codigoIdioma not in (" + listIdiomaBorrar + ") ");
					List<TraduccionContenido> traduciones = query.list();
					for(TraduccionContenido traducI : traduciones ) {
						session.delete(traducI);	
					}
					session.flush();
				}				
			}

			session.saveOrUpdate(contenido);
			session.flush();

			if (nuevo) {
				for (TraduccionContenido trad : listaTraducciones.values()) {
					trad.getId().setCodigoContenido(contenido.getId());
					session.saveOrUpdate(trad);
				}
				session.flush();
				contenido.setTraducciones(listaTraducciones);
			}

			tx.commit();
			this.close(session);

			int op = (nuevo) ? Auditoria.CREAR : Auditoria.MODIFICAR;
			this.grabarAuditoria(contenido, op);

			return contenido.getId();

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Obtiene un Contenido
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Contenido obtenerContenido(Long id) {

		Session session = this.getSession();
		try {
			Contenido contenido = (Contenido) session.get(Contenido.class, id);
			return contenido;

		} catch (ObjectNotFoundException oNe) {
			log.error(oNe.getMessage());
			return new Contenido();
		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Obtiene un Contenido a partir de la URI
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Contenido obtenerContenidoDesdeUri(String idioma, String uri, String site) {

		Session session = this.getSession();
		try {
			Query query;
			if (idioma != null) {
				query = session
						.createQuery("select con from Contenido con JOIN con.traducciones tc where tc.id.codigoIdioma = :idioma and tc.uri = :uri and con.menu.microsite.id = :site ");
				query.setParameter("idioma", idioma);
			} else {
				query = session
						.createQuery("select con from Contenido con JOIN con.traducciones tc where tc.uri = :uri and con.menu.microsite.id = :site ");
			}
			query.setParameter("uri", uri);
			query.setParameter("site", Long.valueOf(site));
			query.setMaxResults(1);
			return (Contenido) query.uniqueResult();
		} catch (ObjectNotFoundException oNe) {
			log.error(oNe.getMessage());
			return new Contenido();
		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Comprueba si existe un contenido
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public boolean existeContenido(Long id) {

		Session session = this.getSession();
		try {
			List<?> lista = session.createQuery(
					"from Contenido conte where conte.id = " + id.toString())
					.list();
			return (lista.size() > 0);

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Lista todos los Contenidos
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<?> listarContenidos() {

		Session session = this.getSession();
		try {
			this.parametrosCons(); // Establecemos los parámetros de la
									// paginación

			Query query = session.createQuery(this.select + this.from
					+ this.where + this.orderby);
			query.setFirstResult(this.cursor - 1);
			query.setMaxResults(this.tampagina);
			return query.list();

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Lista todos los contenidos poniendole un idioma por defecto
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public ArrayList<Contenido> listarContenidos(String idioma) {

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
	 * borra un Contenido
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public void borrarContenido(Long id) throws DelegateException {

		Session session = this.getSession();
		
		try {
			
			Transaction tx = session.beginTransaction();
			Contenido contenido = (Contenido) session.get(Contenido.class, id);
						
			// Borrado de archivos.
			ArchivoDelegate archivoDelegate = DelegateUtil.getArchivoDelegate();
			
			// Imagen de menú, si existe.
			if (contenido.getImagenmenu() != null && contenido.getImagenmenu().getId() != null)
				archivoDelegate.borrarArchivo(contenido.getImagenmenu().getId());
			
			// Archivos relacionados.		
			List<Archivo> listaArchivos = (List<Archivo>)session.createQuery("select arch from Archivo arch where arch.pagina = " + id.toString()).list();
			archivoDelegate.borrarArchivos(listaArchivos);

			session.createQuery("delete TraduccionContenido conte where conte.id.codigoContenido = " + id.toString()).executeUpdate();
			session.createQuery("delete Contenido conte where conte.id = " + id.toString()).executeUpdate();

			session.flush();
			tx.commit();
			this.close(session);

			this.grabarAuditoria(contenido, Auditoria.ELIMINAR);

		} catch (HibernateException he) {
			
			throw new EJBException(he);
			
		} finally {
			
			this.close(session);
			
		}
		
	}

	/**
	 * metodo privado que devuelve un arraylist 'nuevo'. Vuelca el contenido del
	 * listado e inicializa el idioma del bean.
	 * 
	 * @param listado
	 * @param idioma
	 * @return
	 */
	private ArrayList<Contenido> crearlistadostateful(List<?> listado,
			String idioma) {

		ArrayList<Contenido> lista = new ArrayList<Contenido>();
		Iterator<?> iter = listado.iterator();
		Contenido contenido;
		while (iter.hasNext()) {
			contenido = new Contenido();
			contenido = (Contenido) iter.next();
			contenido.setIdi(idioma);
			lista.add(contenido);
		}
		return lista;
	}

	/**
	 * Elimina todos los documentos de la pagina
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public void eliminarDocumentos(String micro, String pagina)
			throws DelegateException {
			
		List<Archivo> lista = this.listarDocumentos(micro, pagina);
		
		// Borrado de archivos.
		ArchivoDelegate archivoDelegate = DelegateUtil.getArchivoDelegate();
		archivoDelegate.borrarArchivos(lista);
		
	}

	/**
	 * Devuelve el titulo del listado de contenidos, la miga de pan de donde se
	 * encuentra. Podrá ser: Menu padre > Menu hijo > Contenido o bien Menu
	 * padre > Contenido según exista idmenu y/o idcontenido
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public String migapan(String idmenu, Long idcontenido)
			throws DelegateException {

		String ret = "";
		String contnombre = "";
		if (idmenu.equals("")) {
			return ret;

		} else {
			if (idcontenido != null) {
				Contenido cont = this.obtenerContenido(idcontenido);
				if (((TraduccionContenido) cont.getTraduccion(Idioma
						.getIdiomaPorDefecto())).getTitulo() != null) {
					contnombre = ((TraduccionContenido) cont
							.getTraduccion(Idioma.getIdiomaPorDefecto()))
							.getTitulo();
				}
			}

			MenuDelegate bdMenu = DelegateUtil.getMenuDelegate();
			Menu menu1 = bdMenu.obtenerMenu(new Long(idmenu));
			Long idmenupadre = menu1.getPadre();
			String padre1 = "";

			if ((menu1.getTraduccion(Idioma.getIdiomaPorDefecto())) != null) {
				padre1 = ((TraduccionMenu) menu1.getTraduccion(Idioma
						.getIdiomaPorDefecto())).getNombre();
			}

			if (idmenupadre.intValue() == 0) { // Era el padre
				ret = padre1 + " > " + contnombre;
			}

			if (idmenupadre.intValue() != 0) { // Era el hijo
				Menu menu2 = bdMenu.obtenerMenu(idmenupadre);
				if (menu2.getTraduccion(Idioma.getIdiomaPorDefecto()) != null) {
					ret = ((TraduccionMenu) menu2.getTraduccion(Idioma
							.getIdiomaPorDefecto())).getNombre()
							+ " > "
							+ padre1 + " > " + contnombre;
				} else {
					ret = " > " + padre1 + " > " + contnombre;
				}
			}
		}

		return ret;
	}

	/**
	 * Lista todos los contenidos de un microsite
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<?> listarAllContenidos(String micro) throws DelegateException {

		Session session = this.getSession();
		try {
			String sql = "select contenido from Contenido contenido where contenido.menu.microsite.id = "
					+ micro;
			Query query = session.createQuery(sql);
			return query.list();

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Lista todos los documentos del microsite si pagina es null, o bien los
	 * del microsite que pertenezcan a una página de contenido
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<Archivo> listarDocumentos(String micro, String pagina) {

		Session session = this.getSession();
		try {
			String sql = "select ar.id, ar.nombre, ar.mime, ar.peso from Archivo ar ";

			if (pagina == null) {
				sql += " where ar.idmicrosite = " + micro
						+ " and ar.pagina is null";
			} else {
				if (micro == null) {
					sql += " where ar.pagina = " + pagina;
				} else {
					sql += " where ar.idmicrosite = " + micro
							+ " and ar.pagina = " + pagina;
				}
			}

			sql += " order by ar.nombre";

			Query query = session.createQuery(sql);

			ArrayList<Archivo> lista = new ArrayList<Archivo>();
			Iterator<?> res = query.iterate();
			int i = 0;
			while (res.hasNext()) {
				Object[] fila = (Object[]) res.next();
				Archivo archi = new Archivo();
				archi.setId((Long) fila[0]);
				archi.setNombre((String) fila[1]);
				archi.setMime(((String) fila[2]).toUpperCase());
				archi.setPeso((((Long) fila[3]).longValue() / 1024) + 1);
				lista.add(i, archi);
				i++;
			}

			return lista;

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Comprueba que el contenido pertenece al microsite
	 * 
	 * @throws DelegateException
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public boolean checkSite(Long id, Long idsite) throws DelegateException {

		// ************COMPROBACION DE MICROSITE*************
		Contenido con = this.obtenerContenido(id);
		MenuDelegate bdMenu = DelegateUtil.getMenuDelegate();
		Long idmicro = bdMenu.obtenerMenu(con.getMenu().getId()).getMicrosite()
				.getId();

		return idmicro.longValue() != idsite.longValue();

	}

	/***************************************************************************************/
	/******************* INDEXACION ************************************/
	/***************************************************************************************/

	/**
	 * Añaade el contenido al indice en todos los idiomas
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public void indexInsertaContenido(Contenido con, ModelFilterObject filter) {

		IndexObject io = new IndexObject();
		try {
			if (filter == null) {
				Long idmicro = DelegateUtil.getMenuDelegate()
						.obtenerMenu(con.getMenu().getId()).getMicrosite()
						.getId();
				filter = DelegateUtil.getMicrositeDelegate()
						.obtenerFilterObject(idmicro);
			}

			if (filter != null && filter.getBuscador().equals("N")) {
				return;
			}

			IndexerDelegate indexerDelegate = DelegateUtil.getIndexerDelegate();
			for (int i = 0; i < this.langs.size(); i++) {
				String idioma = (String) this.langs.get(i);
				io = new IndexObject();

				// Configuración del writer
				Directory directory = indexerDelegate
						.getHibernateDirectory(idioma);
				IndexWriter writer = new IndexWriter(directory,
						Analizador.getAnalizador(idioma), false,
						MaxFieldLength.UNLIMITED);
				writer.setMergeFactor(20);
				writer.setMaxMergeDocs(Integer.MAX_VALUE);

				try {
					io.setId(Catalogo.SRVC_MICRO_CONTENIDOS + "." + con.getId());
					io.setClasificacion(Catalogo.SRVC_MICRO_CONTENIDOS);

					io.setMicro(filter.getMicrosite_id());
					io.setRestringido(filter.getRestringido());
					io.setUo(filter.getUo_id());
					io.setMateria(filter.getMateria_id());
					io.setSeccion(filter.getSeccion_id());
					io.setFamilia(filter.getFamilia_id());

					io.setTitulo("");
					io.setUrl("/sacmicrofront/contenido.do?lang=" + idioma
							+ "&idsite=" + filter.getMicrosite_id().toString()
							+ "&cont=" + con.getId());
					io.setCaducidad("");
					io.setPublicacion("");
					io.setDescripcion("");
					io.setTituloserviciomain(filter.getTraduccion(idioma)
							.getMaintitle());

					if (con.getFcaducidad() != null) {
						io.setCaducidad(new java.text.SimpleDateFormat(
								"yyyyMMdd").format(con.getFcaducidad()));
					}

					if (con.getFpublicacion() != null) {
						io.setPublicacion(new java.text.SimpleDateFormat(
								"yyyyMMdd").format(con.getFpublicacion()));
					}

					TraduccionContenido trad = ((TraduccionContenido) con
							.getTraduccion(idioma));
					if (trad != null) {
						io.addTextLine(trad.getTitulo());

						// Si hay URL no indexamos el contenido
						if ((trad.getUrl() != null)
								&& (trad.getUrl().length() != 0)) {
							io.addDescripcionLine(trad.getUrl());

						} else if (trad.getTexto() != null) {
							// si el texto no es nulo, palante
							// Añadimos los archivos que pudiera haber
							// referenciados en el contenido
							// io = extraeArchivosHTML (trad.getTexto(), io);
							// Transformamos el texto del contenido en un bean
							// Archivo de tipo HTML
							Archivo archi = new Archivo();
							archi.setMime("text/html");
							archi.setPeso(trad.getTexto().length());
							archi.setDatos(trad.getTexto().getBytes());
							io.addArchivo(archi);
							io.addDescripcion(archi);
						}

						io.addTextopcionalLine(filter.getTraduccion(idioma)
								.getMateria_text());
						io.addTextopcionalLine(filter.getTraduccion(idioma)
								.getSeccion_text());
						io.addTextopcionalLine(filter.getTraduccion(idioma)
								.getUo_text());

						if (trad.getTitulo() != null) {
							io.setTitulo(trad.getTitulo());
						}
					}

					if (io.getText().length() > 0) {
						indexerDelegate.insertaObjeto(io, idioma, writer);
					}
				} finally {
					writer.close();
					directory.close();
				}
			}

		} catch (DelegateException ex) {
			log.warn("[indexInsertaContenido:" + con.getId()
					+ "] No se ha podido indexar contenido. " + ex.getMessage());
			// throw new EJBException(ex);
		} catch (Exception e) {
			log.warn("[indexInsertaContenido:" + con.getId()
					+ "] No se ha podido indexar elemento. " + e.getMessage());
		}
	}

	/**
	 * Elimina el contenido en el indice en todos los idiomas
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public void indexBorraContenido(Long id) {

		try {
			for (int i = 0; i < this.langs.size(); i++) {
				DelegateUtil.getIndexerDelegate().borrarObjeto(
						Catalogo.SRVC_MICRO_CONTENIDOS + "." + id,
						"" + this.langs.get(i));
			}

		} catch (DelegateException ex) {
			throw new EJBException(ex);
		}
	}

	/**
	 * Extrae los archivos referenciados que pudiera haber en el HTML del
	 * contenido
	 */
	@SuppressWarnings("unused")
	private IndexObject extraeArchivosHTML(String html, IndexObject io) {

		try {
			Source fuente = new Source(html);
			fuente.fullSequentialParse();

			List<?> elementList = fuente.findAllElements(HTMLElementName.A);
			for (Object name : elementList) {
				Element link = (Element) name;
				String laurl = link.getAttributeValue("href");
				if (laurl != null
						&& laurl.startsWith("archivopub.do?ctrl=CNTSP")) {
					int ini = laurl.lastIndexOf("&id=");
					if (ini != -1) {
						String id = laurl.substring(ini + 4);
						Archivo archi = DelegateUtil.getArchivoDelegate()
								.obtenerArchivo(new Long(id));
						if (archi.getPeso() > 0 && archi.getDatos() != null) {
							io.addArchivo(archi);
						}
					}
				}
			}

		} catch (Exception ex) {
			io.addTextLine("");
			log.warn("[Error extrayendo html de " + io.getMicro() + "."
					+ io.getTitulo() + "] " + ex.getMessage());
		}

		return io;
	}

}
