package es.caib.gusite.micropersistence.ejb;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.Transaction;

import es.caib.gusite.micromodel.Agenda;
import es.caib.gusite.micromodel.Archivo;
import es.caib.gusite.micromodel.Auditoria;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.SolrPendienteResultado;
import es.caib.gusite.micromodel.TraduccionActividadagenda;
import es.caib.gusite.micromodel.TraduccionAgenda;
import es.caib.gusite.micropersistence.delegate.ArchivoDelegate;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.MicrositeDelegate;
import es.caib.gusite.micropersistence.delegate.SolrPendienteDelegate;
import es.caib.gusite.micropersistence.util.IndexacionUtil;
import es.caib.gusite.micropersistence.util.PathUOResult;
import es.caib.gusite.plugins.PluginFactory;
import es.caib.gusite.plugins.organigrama.OrganigramaProvider;
import es.caib.solr.api.SolrIndexer;
import es.caib.solr.api.model.IndexData;
import es.caib.solr.api.model.IndexFile;
import es.caib.solr.api.model.MultilangLiteral;
import es.caib.solr.api.model.PathUO;
import es.caib.solr.api.model.types.EnumAplicacionId;
import es.caib.solr.api.model.types.EnumCategoria;
import es.caib.solr.api.model.types.EnumIdiomas;

/**
 * SessionBean para consultar Agenda.
 * 
 * @ejb.bean name="sac/micropersistence/AgendaFacade"
 *           jndi-name="es.caib.gusite.micropersistence.AgendaFacade"
 *           type="Stateless" view-type="remote" transaction-type="Container"
 * 
 * @ejb.transaction type="Required"
 * 
 * @author Indra
 */
@SuppressWarnings({"deprecation", "unchecked"})
public abstract class AgendaFacadeEJB extends HibernateEJB {

	private static final long serialVersionUID = 441274285622365185L;

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
	 * Inicializo los parámetros de la consulta de Agenda....
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public void init(Long site) {
		super.tampagina = 10;
		super.pagina = 0;
		super.select = "select agenda";
		super.from = " from Agenda agenda join agenda.traducciones trad ";
		super.where = " where trad.id.codigoIdioma = '" + Idioma.getIdiomaPorDefecto() + "' and agenda.idmicrosite=" + site.toString();
		super.whereini = " ";
		super.orderby = "";

		super.camposfiltro = new String[] { "agenda.organizador", "agenda.finicio", "agenda.ffin", "trad.titulo" };
		super.cursor = 0;
		super.nreg = 0;
		super.npags = 0;
	}

	/**
	 * Inicializo los parámetros de la consulta de Agenda....
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public void init(Long site, String idiomapasado) {
		super.tampagina = 10;
		super.pagina = 0;
		super.select = "select agenda.id,trad.titulo ,agenda.finicio,agenda.idmicrosite";
		super.from = " from Agenda agenda join agenda.traducciones trad ";
		super.where = " where (trad.id.codigoIdioma='" + Idioma.getIdiomaPorDefecto() + "' or trad.id.codigoIdioma='" + idiomapasado
				+ "')  and agenda.idmicrosite=" + site.toString();
		super.whereini = " ";
		super.orderby = "";

		super.camposfiltro = new String[] { "agenda.organizador", "agenda.finicio", "agenda.ffin", "trad.titulo" };
		super.cursor = 0;
		super.nreg = 0;
		super.npags = 0;
	}

	/**
	 * Inicializo los parámetros de la consulta de Agenda.... No está bien hecho
	 * debería ser Statefull
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public void init() {
		super.tampagina = 10;
		super.pagina = 0;
		super.select = "select agenda ";
		super.from = " from Agenda agenda join agenda.traducciones trad ";
		super.where = " where trad.id.codigoIdioma='" + Idioma.getIdiomaPorDefecto() + "'";
		super.whereini = " ";
		super.orderby = "";
		super.camposfiltro = new String[] { "agenda.organizador", "agenda.finicio", "agenda.ffin", "trad.titulo" };
		super.cursor = 0;
		super.nreg = 0;
		super.npags = 0;
	}

	/**
	 * Crea o actualiza una agenda
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public Long grabarAgenda(Agenda agenda) throws DelegateException {
				
		Session session = this.getSession();
		boolean nuevo = false;
		
		Agenda agendaOriginal = null;
		
		ArchivoDelegate archivoDelegate = DelegateUtil.getArchivoDelegate();
		List<Archivo> archivosPorBorrar = new ArrayList<Archivo>();
		
		try {
			
			Transaction tx = session.beginTransaction();
			
			if (agenda.getId() == null) {
				nuevo = true;
			}

			Map<String, TraduccionAgenda> listaTraducciones = new HashMap<String, TraduccionAgenda>();

			if (nuevo) {

				Iterator<TraduccionAgenda> it = agenda.getTraducciones().values().iterator();
				while (it.hasNext()) {
					TraduccionAgenda trd = it.next();
					listaTraducciones.put(trd.getId().getCodigoIdioma(), trd);
				}
				agenda.setTraducciones(null);
				
			} else {
				
				agendaOriginal = this.obtenerAgenda(agenda.getId());
				
				//Damos de alta los nuevos archivos
				for (TraduccionAgenda trad : agenda.getTraducciones().values()) {
					
					TraduccionAgenda tradOriginal = (TraduccionAgenda) agendaOriginal.getTraduccion(trad.getId().getCodigoIdioma());
					
	                if (trad.getDocumento() != null) {
	                	if (trad.getDocumento().getId() == null){ // Condición de nuevo documento.
	                		archivoDelegate.insertarArchivo(trad.getDocumento());
	                		//Indexamos documento
	                		SolrPendienteDelegate pendienteDel = DelegateUtil.getSolrPendienteDelegate();
	                		pendienteDel.grabarSolrPendiente(EnumCategoria.GUSITE_AGENDA.toString(), agenda.getId(), trad.getDocumento().getId(), 1L);
	                		}
	                	else
	                		if (trad.getDocumento().getDatos() != null){ // Condición de actualizar documento.
	                			archivoDelegate.grabarArchivo(trad.getDocumento());
	                			//Indexamos documento
		                		SolrPendienteDelegate pendienteDel = DelegateUtil.getSolrPendienteDelegate();
		                		pendienteDel.grabarSolrPendiente(EnumCategoria.GUSITE_AGENDA.toString(), agenda.getId(), trad.getDocumento().getId(), 1L);
	                		}
	                } else {
	                	if (tradOriginal.getDocumento() != null){ // Condición de borrado de documento.
	                		archivosPorBorrar.add(tradOriginal.getDocumento());
	                		//DesIndexamos documento
	            			SolrPendienteDelegate pendienteDel = DelegateUtil.getSolrPendienteDelegate();
	            			pendienteDel.grabarSolrPendiente(EnumCategoria.GUSITE_AGENDA.toString(), agenda.getId(),tradOriginal.getDocumento().getId(), 0L);
	                	}
	                }
	                
	                if (trad.getImagen() != null) {
	                	if (trad.getImagen().getId() == null) // Condición de nuevo documento.
	                		archivoDelegate.insertarArchivo(trad.getImagen());
	                	else
	                		if (trad.getImagen().getDatos() != null) // Condición de actualizar documento.
	                			archivoDelegate.grabarArchivo(trad.getImagen());
	                } else {
	                	if (tradOriginal.getImagen() != null) // Condición de borrado de documento.
	                		archivosPorBorrar.add(tradOriginal.getImagen());
	                }
	                
				}
				
			}

			session.saveOrUpdate(agenda);
			session.flush();

			if (nuevo) {
				
				for (TraduccionAgenda trad : listaTraducciones.values()) {
					
					trad.getId().setCodigoAgenda(agenda.getId());
					
	                if (trad.getDocumento() != null && trad.getDocumento().getId() == null) {
	                    archivoDelegate.insertarArchivo(trad.getDocumento());
	                }
	                
	                if (trad.getImagen() != null && trad.getImagen().getId() == null) {
	                    archivoDelegate.insertarArchivo(trad.getImagen());
	                }
	                
					session.saveOrUpdate(trad);
					
				}
				
				session.flush();
				agenda.setTraducciones(listaTraducciones);
				
			}
			
			// Borramos archivos FKs del Microsite que han solicitado que se borren.
			if (archivosPorBorrar.size() > 0)
				archivoDelegate.borrarArchivos(archivosPorBorrar);

			tx.commit();
			// if (!nuevo) indexBorraAgenda(agenda.getId());
			// indexInsertaAgenda(agenda, null);
			this.close(session);

			int op = (nuevo) ? Auditoria.CREAR : Auditoria.MODIFICAR;
			this.grabarAuditoria(agenda, op);
			
			//Indexamos
			SolrPendienteDelegate pendienteDel = DelegateUtil.getSolrPendienteDelegate();
			pendienteDel.grabarSolrPendiente(EnumCategoria.GUSITE_AGENDA.toString(), agenda.getId(), null, 1L);

			return agenda.getId();

		} catch (HibernateException he) {
			
			if (!nuevo) {
				//this.indexBorraAgenda(agenda.getId());
			}
			throw new EJBException(he);
			
		} finally {
			
			this.close(session);
			
		}
		
	}

	/**
	 * Obtiene una Agenda
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Agenda obtenerAgenda(Long id) {
		Session session = this.getSession();
		try {
			Agenda agenda = (Agenda) session.get(Agenda.class, id);
			return agenda;
		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Lista todas las Agendas
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<?> listarAgendas() {
		Session session = this.getSession();
		try {
			this.parametrosCons(); // Establecemos los parámetros de la
									// paginación

			Query query = session.createQuery(this.select + this.from + this.where + this.orderby);
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
	 * Lista todas las Agenda
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<Agenda> listarAgendasrec(String idiomapasado) {
		Session session = this.getSession();
		try {
			this.parametrosCons(); // Establecemos los parámetros de la
									// paginación
			Query query = session.createQuery(this.select + this.from + this.where + this.orderby);
			ArrayList<Agenda> lista = new ArrayList<Agenda>();
			ScrollableResults scr = query.scroll();
			scr.first();
			scr.scroll(this.cursor - 1);
			int i = 0;
			while (this.tampagina > i++) {
				Object[] fila = scr.get();
				Agenda age = new Agenda();
				age.setIdmicrosite((Long) fila[3]);
				age.setId((Long) fila[0]);
				age.setFinicio((Date) fila[2]);
				TraduccionAgenda tradage = new TraduccionAgenda();
				tradage.setTitulo((String) fila[1]);
				age.setTraduccion(idiomapasado, tradage);
				lista.add(age);
				if (!scr.next()) {
					break;
				}
			}
			scr.close();
			return lista;
		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Lista todos los eventos de la agenda poniendole un idioma por defecto
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public ArrayList<Agenda> listarAgendas(String idioma) {
		Session session = this.getSession();
		try {
			this.parametrosCons(); // Establecemos los parámetros de la
									// paginación

			Query query = session.createQuery(this.select + this.from + this.where + this.orderby);
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
	 * Lista todos las Agenda
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public ArrayList<?> listarAgendas(Date fecha, String idioma) {
		Session session = this.getSession();
		try {
			// parametrosCons(); // Establecemos los parámetros de la paginación
			java.sql.Date dt = new java.sql.Date(fecha.getTime());

			if (this.where.toLowerCase().indexOf("where") != -1) {
				this.where += " and to_char(agenda.finicio,'yyyy-MM-dd')<='" + dt + "'";
				this.where += " and ( (agenda.ffin is null) OR (to_char(agenda.ffin , 'yyyy-MM-dd')>='" + dt + "') )";
			} else {
				this.where = " where to_char(agenda.finicio,'yyyy-MM-dd')<='" + dt + "'";
				this.where += " and ( (agenda.ffin is null) OR (to_char(agenda.ffin , 'yyyy-MM-dd')>='" + dt + "') )";
			}

			this.parametrosCons(); // Establecemos los parámetros de la
									// paginación

			Query query = session.createQuery(this.select + this.from + this.where + this.orderby);
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
	 * Borra una Agenda
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public void borrarAgenda(Long id) throws DelegateException {

		Session session = this.getSession();
		
		try {
			
			Transaction tx = session.beginTransaction();
			Agenda agenda = (Agenda) session.get(Agenda.class, id);

			List<TraduccionAgenda> traducciones = session.createQuery("select tage from TraduccionAgenda tage where tage.id.codigoAgenda=" + id).list();
			
			session.createQuery("delete from TraduccionAgenda tage where tage.id.codigoAgenda=" + id).executeUpdate();
			session.createQuery("delete from Agenda age where age.id=" + id).executeUpdate();
			
			// TODO amartin: ¿por qué esto está comentado?
			// indexBorraAgenda(agenda.getId());
			
			session.flush();
			
			if (traducciones != null) {
				
				ArchivoDelegate archivoDelegate = DelegateUtil.getArchivoDelegate();
				
				for (TraduccionAgenda tra : traducciones) {
					
					if (tra.getImagen() != null)
						archivoDelegate.borrarArchivo(tra.getImagen().getId());
					
					if (tra.getDocumento() != null){						
						archivoDelegate.borrarArchivo(tra.getDocumento().getId());
						//DesIndexamos documento
						SolrPendienteDelegate pendienteDel = DelegateUtil.getSolrPendienteDelegate();
						pendienteDel.grabarSolrPendiente(EnumCategoria.GUSITE_AGENDA.toString(), agenda.getId(), tra.getDocumento().getId(), 0L);
					}
					
				}
				
			}
						
			tx.commit();
			this.close(session);

			this.grabarAuditoria(agenda, Auditoria.ELIMINAR);
			
			//DesIndexamos
			SolrPendienteDelegate pendienteDel = DelegateUtil.getSolrPendienteDelegate();
			pendienteDel.grabarSolrPendiente(EnumCategoria.GUSITE_AGENDA.toString(), agenda.getId(), null, 0L);

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
	 * @ejb.permission unchecked="true"
	 */
	private ArrayList<Agenda> crearlistadostateful(List<?> listado, String idioma) {
		ArrayList<Agenda> lista = new ArrayList<Agenda>();
		Iterator<?> iter = listado.iterator();
		Agenda agenda;
		while (iter.hasNext()) {
			agenda = new Agenda();
			agenda = (Agenda) iter.next();
			agenda.setIdi(idioma);
			lista.add(agenda);
		}
		return lista;
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
			Query query = session.createQuery("from Agenda age where age.idmicrosite=" + site.toString() + " and age.id=" + id.toString());
			return query.list().isEmpty();
		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}
	

	/**
	 * Obtiene una Agenda para solr.
	 * 
	 */
	private Agenda obtenerAgendaBySolr(Long id) {
		final Session session = this.getSession();
		try {
			return (Agenda) session.get(Agenda.class, id);
		} catch (Exception exception) {
			log.error("Error obteniendo agenda" , exception);
			return null;
		} finally {
			this.close(session);
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
	public SolrPendienteResultado indexarSolr(final SolrIndexer solrIndexer, final Long idElemento, final EnumCategoria categoria) {
		log.debug("AgendafacadeEJB.indexarSolr. idElemento:" + idElemento +" categoria:"+categoria);
		
		try {
			OrganigramaProvider op = PluginFactory.getInstance().getOrganigramaProvider();
			MicrositeDelegate micrositedel = DelegateUtil.getMicrositeDelegate();
			
			//Paso 0. Obtenemos el contenido y comprobamos si se puede indexar.
			final Agenda agenda = obtenerAgendaBySolr(idElemento);
			if (agenda == null) {
				return new SolrPendienteResultado(true, "");
			}
			
			if (!IndexacionUtil.isIndexable(agenda) ) {
				return new SolrPendienteResultado(true, "No se puede indexar");
			}
			
			Microsite micro = micrositedel.obtenerMicrosite(agenda.getIdmicrosite());
			if (!IndexacionUtil.isIndexable(micro) ) {
				return new SolrPendienteResultado(true, "No se puede indexar");
			}
			
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
			List<PathUO> uosPath = null;
			for (String keyIdioma : agenda.getTraducciones().keySet()) {
				final EnumIdiomas enumIdioma = EnumIdiomas.fromString(keyIdioma);
				final TraduccionAgenda traduccion = (TraduccionAgenda) agenda.getTraduccion(keyIdioma);
		    	
				if (traduccion != null && enumIdioma != null) {
					
					if (traduccion.getTitulo() == null || traduccion.getTitulo().isEmpty()) {
						continue;
					}
					
					// Path UO
					PathUOResult pathUO = IndexacionUtil.calcularPathUOsMicrosite(micro, keyIdioma);
					
					idiomas.add(enumIdioma);
					titulo.addIdioma(enumIdioma, traduccion.getTitulo());
					descripcion.addIdioma(enumIdioma, traduccion.getDescripcion() != null ? solrIndexer.htmlToText(traduccion.getDescripcion()) : "");
			    	
					TraduccionActividadagenda tradActividad = (TraduccionActividadagenda) agenda.getActividad().getTraduccion(keyIdioma);
			    	String search= StringUtils.defaultString(traduccion.getTitulo()) + " " +
			    			StringUtils.defaultString(agenda.getOrganizador()) + " " +
			    			(tradActividad != null? StringUtils.defaultString(tradActividad.getNombre()) : "");
			    	searchText.addIdioma(enumIdioma,solrIndexer.htmlToText(search));

			    	searchTextOptional.addIdioma(enumIdioma, pathUO.getUosText());
			    	
			    	urls.addIdioma(enumIdioma, IndexacionUtil.getUrlAgenda(micro, agenda, keyIdioma));
			    	
			    	// Padre
			    	urlPadre.addIdioma(enumIdioma, IndexacionUtil.getUrlMicrosite(micro, keyIdioma));	    		
			    	tituloPadre.addIdioma(enumIdioma, IndexacionUtil.getTituloMicrosite(micro, keyIdioma));
			    	
			    	uosPath = pathUO.getUosPath();
			    	
				}
			}
			
			
			final IndexData indexData = new IndexData();
			indexData.setCategoria(categoria);
			indexData.setAplicacionId(EnumAplicacionId.GUSITE);
			indexData.setElementoId(idElemento.toString());
			indexData.setFechaPlazoIni(agenda.getFinicio());
			indexData.setFechaPlazoFin(agenda.getFfin());
			indexData.setTitulo(titulo);
			indexData.setDescripcion(descripcion);
			indexData.setUrl(urls);
			indexData.setSearchText(searchText);
			indexData.setSearchTextOptional(searchTextOptional);
			indexData.setIdiomas(idiomas);
			indexData.setElementoIdPadre(micro.getId().toString());
			indexData.setCategoriaPadre(EnumCategoria.GUSITE_MICROSITE);
			indexData.setUos(uosPath);
			indexData.setDescripcionPadre(tituloPadre);
			indexData.setUrlPadre(urlPadre);
			indexData.setCategoriaRaiz(EnumCategoria.GUSITE_MICROSITE);
			indexData.setElementoIdRaiz(micro.getId().toString());
			indexData.setInterno(IndexacionUtil.isRestringidoMicrosite(micro));
				
			solrIndexer.indexarContenido(indexData);
			
			return new SolrPendienteResultado(true);
		} catch(Exception exception) {
			log.error("Error intentando indexar idElemento:" + idElemento + " categoria:" + categoria, exception);
			return new SolrPendienteResultado(false, exception.getMessage());
		}
	}
	
	
	
	
	/**
	 * Método para indexar según la id, idArchivo y la categoria. 
	 * @param solrIndexer
	 * @param idElemento
	 * @param categoria
	 * @ejb.interface-method
     * @ejb.permission unchecked="true"
     * @ejb.transaction type="RequiresNew"
	 */
	public SolrPendienteResultado indexarSolrArchivo(final SolrIndexer solrIndexer, final Long idElemento, final EnumCategoria categoria, final Long idArchivo) {
		log.debug("AgendafacadeEJB.indexarSolrArchivo. idElemento:" + idElemento +" categoria:"+categoria +" idArchivo:"+idArchivo);
		
		try {
			
			MicrositeDelegate micrositedel = DelegateUtil.getMicrositeDelegate();
			ArchivoDelegate archi = DelegateUtil.getArchivoDelegate();
			
			//Paso 0. Obtenemos el contenido y comprobamos si se puede indexar.
			final Agenda agenda = obtenerAgendaBySolr(idElemento);
			if (agenda == null) {
				return new SolrPendienteResultado(true, "Error obteniendo la agenda.");
			}
			
			if (!IndexacionUtil.isIndexable(agenda)) {
				return new SolrPendienteResultado(true, "No se puede indexar");
			}
			
			Microsite micro = micrositedel.obtenerMicrosite(agenda.getIdmicrosite());
			if (!IndexacionUtil.isIndexable(micro)) {
				return new SolrPendienteResultado(true, "No se puede indexar");
			}
			
			
			final Archivo archivo = archi.obtenerArchivo(idArchivo);
			if (!IndexacionUtil.isIndexable(archivo)) {
				return new SolrPendienteResultado(true, "No se puede indexar");
			}
			
			byte[] contenidoFichero = archi.obtenerContenidoFichero(archivo);
			
			// Hay que buscar el archivo en la traduccion correspondiente
			// Recorremos las traducciones			
			for (String keyIdioma : agenda.getTraducciones().keySet()) {
				
				final MultilangLiteral titulo = new MultilangLiteral();
				final MultilangLiteral descripcion = new MultilangLiteral();
				final MultilangLiteral urls = new MultilangLiteral();
				final MultilangLiteral searchTextOptional = new MultilangLiteral();
				final List<EnumIdiomas> idiomas = new ArrayList<EnumIdiomas>();
				final MultilangLiteral descripcionPadre = new MultilangLiteral();
				final MultilangLiteral extension = new MultilangLiteral();
				final MultilangLiteral urlPadre = new MultilangLiteral();
				
				final EnumIdiomas enumIdioma = EnumIdiomas.fromString(keyIdioma);
				final TraduccionAgenda traduccion = (TraduccionAgenda) agenda.getTraduccion(keyIdioma);
		    	
				if (traduccion != null && enumIdioma != null) {

					// Si no tiene titulo la agenda no indexamos fichero
					if (traduccion.getTitulo() == null || traduccion.getTitulo().isEmpty()) {
						continue;
					}
					
					// Debe ser el documento asociado a la traduccion
					if (traduccion.getDocumento() == null || traduccion.getDocumento().getId().longValue() != idArchivo.longValue()) {
						continue;
					}
					
					PathUOResult pathUo = IndexacionUtil.calcularPathUOsMicrosite(micro, keyIdioma);
					
					idiomas.add(enumIdioma); 
					
					titulo.addIdioma(enumIdioma, IndexacionUtil.getTituloArchivo(archivo));
			    	descripcion.addIdioma(enumIdioma, IndexacionUtil.getDescripcionArchivo(archivo));
			    	extension.addIdioma(enumIdioma, FilenameUtils.getExtension(archivo.getNombre()));
			    	searchTextOptional.addIdioma(enumIdioma, pathUo.getUosText());
			    	urls.addIdioma(enumIdioma, IndexacionUtil.getUrlArchivo(micro, archivo, keyIdioma));
			    	urlPadre.addIdioma(enumIdioma, IndexacionUtil.getUrlAgenda(micro, agenda, keyIdioma));
					descripcionPadre.addIdioma(enumIdioma, traduccion.getDescripcion() !=null ? solrIndexer.htmlToText(traduccion.getDescripcion()):"");

					final IndexFile indexFile = new IndexFile();
					indexFile.setCategoria(EnumCategoria.GUSITE_ARCHIVO);
					indexFile.setAplicacionId(EnumAplicacionId.GUSITE);
					indexFile.setElementoId(idArchivo.toString());
					indexFile.setTitulo(titulo);
					indexFile.setDescripcion(descripcion);
					indexFile.setUrl(urls);
					indexFile.setSearchTextOptional(searchTextOptional);
					indexFile.setIdioma(enumIdioma);
					indexFile.setFileContent(contenidoFichero);
					indexFile.setElementoIdPadre(agenda.getIdi());
					indexFile.setCategoriaPadre(EnumCategoria.GUSITE_MICROSITE);
					indexFile.setDescripcionPadre(descripcionPadre);
					indexFile.setExtension(extension);
					indexFile.setUrlPadre(urlPadre);
					indexFile.setUos(pathUo.getUosPath());
					indexFile.setCategoriaRaiz(EnumCategoria.GUSITE_MICROSITE);
					indexFile.setElementoIdRaiz(micro.getId().toString());
					indexFile.setInterno(IndexacionUtil.isRestringidoMicrosite(micro));
						
					solrIndexer.indexarFichero(indexFile);
					
				}
			}
			
			return new SolrPendienteResultado(true);
		} catch(Exception exception) {
			log.error("Error intentando indexar idElemento:" + idElemento +" categoria:"+categoria +" idArchivo:"+idArchivo, exception);
			return new SolrPendienteResultado(false, exception.getMessage());
		}
	}
	
	/**
	 * Obtiene las agendas de un microsite
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<Agenda> obtenerAgendasByMicrositeId(Long idMicrosite) {

		Session session = this.getSession();		

		try {
			
			Query query = session.createQuery("from Agenda a where a.idmicrosite =" + idMicrosite.toString());
			
			List<Agenda> lista = query.list();
									
			return lista;

		} catch (HibernateException he) {
			
			throw new EJBException(he);
			
		}  finally {
			
			this.close(session);
			
		}
	

    }

	
}
