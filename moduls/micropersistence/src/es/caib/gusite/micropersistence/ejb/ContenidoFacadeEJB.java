package es.caib.gusite.micropersistence.ejb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;

import org.apache.commons.io.FilenameUtils;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import es.caib.gusite.micromodel.Archivo;
import es.caib.gusite.micromodel.Auditoria;
import es.caib.gusite.micromodel.Contenido;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.Menu;
import es.caib.gusite.micromodel.SolrPendienteResultado;
import es.caib.gusite.micromodel.TraduccionContenido;
import es.caib.gusite.micromodel.TraduccionMenu;
import es.caib.gusite.micromodel.TraduccionMicrosite;
import es.caib.gusite.micropersistence.delegate.ArchivoDelegate;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.MenuDelegate;
import es.caib.gusite.micropersistence.delegate.SolrPendienteDelegate;
import es.caib.gusite.micropersistence.util.IndexacionUtil;
import es.caib.gusite.micropersistence.util.PathUOResult;
import es.caib.gusite.plugins.PluginFactory;
import es.caib.gusite.plugins.organigrama.OrganigramaProvider;
import es.caib.gusite.plugins.organigrama.UnidadData;
import es.caib.gusite.plugins.organigrama.UnidadListData;
import es.caib.solr.api.SolrIndexer;
import es.caib.solr.api.model.IndexData;
import es.caib.solr.api.model.IndexFile;
import es.caib.solr.api.model.MultilangLiteral;
import es.caib.solr.api.model.PathUO;
import es.caib.solr.api.model.types.EnumAplicacionId;
import es.caib.solr.api.model.types.EnumCategoria;
import es.caib.solr.api.model.types.EnumIdiomas;

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

			//Indexamos
			SolrPendienteDelegate pendienteDel = DelegateUtil.getSolrPendienteDelegate();
			pendienteDel.grabarSolrPendiente(EnumCategoria.GUSITE_CONTENIDO.toString(), contenido.getId(), null, 1L);
			
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
	 * Obtiene un Contenido Solr
	 * devuelve null en lugar de EJBException
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Contenido obtenerContenidoBySolr(Long id) {

		final Session session = this.getSession();
		try {
			return (Contenido) session.get(Contenido.class, id);
		} catch (Exception exception) {
			log.error("Error obteniendo el contenido para solr", exception);
			return null;
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
			
			//Desindexamos
			SolrPendienteDelegate pendienteDel = DelegateUtil.getSolrPendienteDelegate();
			pendienteDel.grabarSolrPendiente(EnumCategoria.GUSITE_CONTENIDO.toString(), contenido.getId(), null, 0L);

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
			//TODO revisar a eliminar
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

	/**
	 * Método para indexar según la id y la categoria. 
	 * @param solrIndexer
	 * @param idElemento
	 * @param categoria
	 * @ejb.interface-method
     * @ejb.permission unchecked="true"
     * @ejb.transaction type="RequiresNew"
	 */
	public SolrPendienteResultado indexarSolr(final SolrIndexer solrIndexer, final Long idElemento, final EnumCategoria categoria) {
		log.debug("ContenidofacadeEJB.indexarSolr. idElemento:" + idElemento +" categoria:"+categoria);
		
		try {
			
			//Paso 0. Obtenemos el contenido y comprobamos si se puede indexar.
			final Contenido contenido = obtenerContenidoBySolr(idElemento);
			if (contenido == null) {
				return new SolrPendienteResultado(true, "Error no se ha podido obtener el contenido.");
			}
			
			if (!IndexacionUtil.isIndexable(contenido)) {
				return new SolrPendienteResultado(true, "No se puede indexar");
			}
			
			//Preparamos la información básica: id elemento, aplicacionID = GUSITE y la categoria de tipo ficha.
			final IndexData indexData = new IndexData();
			indexData.setCategoria(categoria);
			indexData.setAplicacionId(EnumAplicacionId.GUSITE);
			indexData.setElementoId(idElemento.toString());
			
			//Iteramos las traducciones
			final MultilangLiteral titulo = new MultilangLiteral();
			final MultilangLiteral descripcion = new MultilangLiteral();
			final MultilangLiteral urls = new MultilangLiteral();
			final MultilangLiteral searchText = new MultilangLiteral();
			final MultilangLiteral searchTextOptional = new MultilangLiteral();
			final List<EnumIdiomas> idiomas = new ArrayList<EnumIdiomas>();
			final MultilangLiteral tituloPadre = new MultilangLiteral();
			final MultilangLiteral urlPadre = new MultilangLiteral();
			
			
			//Recorremos las traducciones
			for (String keyIdioma : contenido.getTraducciones().keySet()) {
				final EnumIdiomas enumIdioma = EnumIdiomas.fromString(keyIdioma);
				final TraduccionContenido traduccion = (TraduccionContenido) contenido.getTraduccion(keyIdioma);
		    	
				if (traduccion != null && enumIdioma != null) {
					
					//Seteamos los primeros campos multiidiomas: Titulo, Descripción y el search text.
					if (traduccion.getTitulo() == null || traduccion.getTitulo().isEmpty()) {
						continue;
					}
					
					// Path UO
					PathUOResult pathUO = IndexacionUtil.calcularPathUOsMicrosite(contenido.getMicrosite(), keyIdioma);
					
					// Idioma
					idiomas.add(enumIdioma);
					
					// Titulo
					titulo.addIdioma(enumIdioma, traduccion.getTitulo());
					
					// Descripcion
					String descTrad = traduccion.getUrl();
					if(descTrad == null ||descTrad.isEmpty()){
						descTrad = traduccion.getTexto() !=null ? solrIndexer.htmlToText(traduccion.getTexto()) : "";
					}
			    	descripcion.addIdioma(enumIdioma, descTrad);
			    	
			    	// Texto busqueda
			    	searchText.addIdioma(enumIdioma, solrIndexer.htmlToText((traduccion.getTitulo()==null?"":traduccion.getTitulo())+ " " + descTrad));
			    	
			    	//Texto busqueda opcional
			    	searchTextOptional.addIdioma(enumIdioma, pathUO.getUosText());
			    	
			    	// URL
			    	urls.addIdioma(enumIdioma, IndexacionUtil.getUrlContenido(contenido, keyIdioma));
			    	
			    	// Padre
			    	urlPadre.addIdioma(enumIdioma, IndexacionUtil.getUrlMicrosite(contenido.getMicrosite(), keyIdioma));	    		
			    	tituloPadre.addIdioma(enumIdioma, IndexacionUtil.getTituloMicrosite(contenido.getMicrosite(), keyIdioma));
			    	
			    	// UOS (no es un dato multiidioma, pero es el mismo para todos)
			    	indexData.setUos(pathUO.getUosPath());
			    	
				}
			}
			
			//Seteamos datos multidioma.
			indexData.setTitulo(titulo);
			indexData.setDescripcion(descripcion);
			indexData.setUrl(urls);
			indexData.setSearchText(searchText);
			indexData.setSearchTextOptional(searchTextOptional);
			indexData.setIdiomas(idiomas);
			indexData.setFechaPublicacion(contenido.getFpublicacion());
			indexData.setFechaCaducidad(contenido.getFcaducidad());
			indexData.setCategoriaPadre(EnumCategoria.GUSITE_MICROSITE);
			indexData.setDescripcionPadre(tituloPadre);
			indexData.setUrlPadre(urlPadre);
			indexData.setMicrositeId(contenido.getMicrosite().getId().toString());
			indexData.setInterno(IndexacionUtil.isRestringidoMicrosite(contenido.getMicrosite()));
								
			solrIndexer.indexarContenido(indexData);			

			return new SolrPendienteResultado(true);
		} catch(Exception exception) {
			log.error("Error intentando indexar idElemento:" + idElemento + " categoria:" + categoria, exception);
			return new SolrPendienteResultado(false, exception.getMessage());
		}
	}
	
	
	
	/**
	 * Método para indexar según la id y la categoria. 
	 * @param solrIndexer
	 * @param idElemento
	 * @param categoria
	 * @ejb.interface-method
     * @ejb.permission unchecked="true"
     * @ejb.transaction type="RequiresNew"
	 */
	public SolrPendienteResultado indexarSolrArchivo(final SolrIndexer solrIndexer, final Long idElemento, final EnumCategoria categoria,final Long idArchivo) {
		log.debug("ContenidofacadeEJB.indexarSolrArchivo. idElemento:" + idElemento +" categoria:"+categoria +" idArchivo:"+idArchivo);
		
		try {
			ArchivoDelegate archi = DelegateUtil.getArchivoDelegate();
			
			//Paso 0. Obtenemos el contenido y comprobamos si se puede indexar.
			final Contenido contenido = obtenerContenidoBySolr(idElemento);
			if (contenido == null) {
				return new SolrPendienteResultado(false, "Error obteniendo contenido.");
			}
			
			final Archivo archivo = archi.obtenerArchivo(idArchivo);
			
			if (!IndexacionUtil.isIndexable(archivo) || !IndexacionUtil.isIndexable(contenido)) {
				return new SolrPendienteResultado(true, "No se puede indexar");
			}
			
			byte[] contenidoFichero = archi.obtenerContenidoFichero(archivo);
			
			// Los archivos solo se indexa en un idioma, por lo que si se quiere que se encuentren en todos los idiomas,
			// habrá que indexarse en todos los idiomas.
			
			//Recorremos las traducciones
			for (String keyIdioma : contenido.getTraducciones().keySet()) {
				
				final MultilangLiteral titulo = new MultilangLiteral();
				final MultilangLiteral descripcion = new MultilangLiteral();
				final MultilangLiteral urls = new MultilangLiteral();
				final MultilangLiteral searchTextOptional = new MultilangLiteral();
				final List<EnumIdiomas> idiomas = new ArrayList<EnumIdiomas>();
				final MultilangLiteral extension = new MultilangLiteral();
				final MultilangLiteral urlPadre = new MultilangLiteral();
				final MultilangLiteral descPadre = new MultilangLiteral();
				
				final EnumIdiomas enumIdioma = EnumIdiomas.fromString(keyIdioma);
				final TraduccionContenido traduccion = (TraduccionContenido) contenido.getTraduccion(keyIdioma);
		    	
				if (traduccion != null && enumIdioma != null) {
					
					// Si el contenido no tiene titulo no indexamos fichero
					if (traduccion.getTitulo() == null || traduccion.getTitulo().isEmpty()) {
						continue;
					}
					
					PathUOResult pathUo = IndexacionUtil.calcularPathUOsMicrosite(contenido.getMicrosite(), keyIdioma);
					
					idiomas.add(enumIdioma);
					titulo.addIdioma(enumIdioma, IndexacionUtil.getTituloArchivo(archivo));
			    	descripcion.addIdioma(enumIdioma, IndexacionUtil.getDescripcionArchivo(archivo));
			    	extension.addIdioma(enumIdioma, FilenameUtils.getExtension(archivo.getNombre()));
			    	searchTextOptional.addIdioma(enumIdioma, pathUo.getUosText());
			    	urls.addIdioma(enumIdioma, IndexacionUtil.getUrlArchivo(contenido.getMicrosite(), archivo, keyIdioma));
			    	urlPadre.addIdioma(enumIdioma, IndexacionUtil.getUrlContenido(contenido, keyIdioma));
			    	
			    	String dPadre = traduccion.getUrl();
					if(dPadre == null ||dPadre.isEmpty()){
						dPadre = solrIndexer.htmlToText(traduccion.getTexto());
					}
			    	descPadre.addIdioma(enumIdioma, dPadre);
			    	
			    	final IndexFile indexFile = new IndexFile();
					indexFile.setCategoria(EnumCategoria.GUSITE_ARCHIVO);
					indexFile.setAplicacionId(EnumAplicacionId.GUSITE);
					indexFile.setElementoId(idArchivo.toString() + "_" + enumIdioma.toString());
					indexFile.setTitulo(titulo);
					indexFile.setDescripcion(descripcion);
					indexFile.setUrl(urls);
					indexFile.setFileContent(contenidoFichero);
					indexFile.setSearchTextOptional(searchTextOptional);
					indexFile.setIdioma(enumIdioma);
					indexFile.setFechaPublicacion(contenido.getFpublicacion());
					indexFile.setFechaCaducidad(contenido.getFcaducidad());
					indexFile.setCategoriaPadre(EnumCategoria.GUSITE_CONTENIDO);
					indexFile.setElementoIdPadre(contenido.getId().toString());
					indexFile.setUrlPadre(urlPadre);
					indexFile.setExtension(extension);				
					indexFile.setDescripcionPadre(descPadre);
					indexFile.setUos(pathUo.getUosPath());
					indexFile.setMicrositeId(contenido.getMicrosite().getId().toString());
					indexFile.setInterno(!contenido.getMicrosite().getRestringido().equals("N") ? true : false);
					
					solrIndexer.indexarFichero(indexFile);
				}
			}
						
			return new SolrPendienteResultado(true);
		} catch(Exception exception) {
			log.error("Error intentando indexar idElemento:" + idElemento +" categoria:"+categoria +" idArchivo:"+idArchivo, exception);
			return new SolrPendienteResultado(false, exception.getMessage());
		}
	}


}
