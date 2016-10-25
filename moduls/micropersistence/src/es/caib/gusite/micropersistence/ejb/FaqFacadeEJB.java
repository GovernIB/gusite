package es.caib.gusite.micropersistence.ejb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import es.caib.gusite.micromodel.Auditoria;
import es.caib.gusite.micromodel.Faq;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.SolrPendienteResultado;
import es.caib.gusite.micromodel.TraduccionFaq;
import es.caib.gusite.micromodel.TraduccionMicrosite;
import es.caib.gusite.micromodel.TraduccionTemafaq;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.MicrositeDelegate;
import es.caib.gusite.micropersistence.delegate.SolrPendienteDelegate;
import es.caib.gusite.micropersistence.util.IndexacionUtil;
import es.caib.gusite.micropersistence.util.PathUOResult;
import es.caib.gusite.plugins.PluginFactory;
import es.caib.gusite.plugins.organigrama.OrganigramaProvider;
import es.caib.gusite.plugins.organigrama.UnidadData;
import es.caib.gusite.plugins.organigrama.UnidadListData;
import es.caib.solr.api.SolrIndexer;
import es.caib.solr.api.model.IndexData;
import es.caib.solr.api.model.MultilangLiteral;
import es.caib.solr.api.model.PathUO;
import es.caib.solr.api.model.types.EnumAplicacionId;
import es.caib.solr.api.model.types.EnumCategoria;
import es.caib.solr.api.model.types.EnumIdiomas;

/**
 * SessionBean para consultar Faq.
 * 
 * @ejb.bean name="sac/micropersistence/FaqFacade"
 *           jndi-name="es.caib.gusite.micropersistence.FaqFacade"
 *           type="Stateless" view-type="remote" transaction-type="Container"
 * 
 * @ejb.transaction type="Required"
 * 
 * @author Indra
 */
public abstract class FaqFacadeEJB extends HibernateEJB {

	private static final long serialVersionUID = -836117598281319916L;

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
	 * Inicializo los parámetros de la consulta de Faq.... No está bien hecho
	 * debería ser Statefull
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public void init(Long site) {
		super.tampagina = 10;
		super.pagina = 0;
		super.select = "select faq";
		super.from = " from Faq faq join faq.traducciones trad ";
		super.where = " where trad.id.codigoIdioma = '"
				+ Idioma.getIdiomaPorDefecto() + "' and faq.idmicrosite = "
				+ site.toString();
		super.whereini = " ";
		super.orderby = "";

		super.camposfiltro = new String[] { "trad.pregunta", "trad.respuesta",
				"trad.url", "trad.urlnom" };
		super.cursor = 0;
		super.nreg = 0;
		super.npags = 0;
	}

	/**
	 * Inicializo los parámetros de la consulta de Faq.... No está bien hecho
	 * debería ser Statefull
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public void init() {
		super.tampagina = 10;
		super.pagina = 0;
		super.select = "select faq";
		super.from = " from Faq faq join faq.traducciones trad ";
		super.where = " where trad.id.codigoIdioma = '"
				+ Idioma.getIdiomaPorDefecto() + "'";
		super.whereini = " ";
		super.orderby = "";

		super.camposfiltro = new String[] { "trad.pregunta", "trad.respuesta",
				"trad.url", "trad.urlnom" };
		super.cursor = 0;
		super.nreg = 0;
		super.npags = 0;
	}

	/**
	 * Crea o actualiza una faq
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public Long grabarFaq(Faq faq) throws DelegateException {

		Session session = this.getSession();
		try {
			boolean nuevo = (faq.getId() == null) ? true : false;
			Transaction tx = session.beginTransaction();

			Map<String, TraduccionFaq> listaTraducciones = new HashMap<String, TraduccionFaq>();
			if (nuevo) {
				Iterator<TraduccionFaq> it = faq.getTraducciones().values()
						.iterator();
				while (it.hasNext()) {
					TraduccionFaq trd = it.next();
					listaTraducciones.put(trd.getId().getCodigoIdioma(), trd);
				}
				faq.setTraducciones(null);
			}

			session.saveOrUpdate(faq);
			session.flush();

			if (nuevo) {
				for (TraduccionFaq trad : listaTraducciones.values()) {
					trad.getId().setCodigoFaq(faq.getId());
					session.saveOrUpdate(trad);
				}
				session.flush();
				faq.setTraducciones(listaTraducciones);
			}

			tx.commit();
			this.close(session);

			int op = (nuevo) ? Auditoria.CREAR : Auditoria.MODIFICAR;
			this.grabarAuditoria(faq, op);

			//Indexamos
			SolrPendienteDelegate pendienteDel = DelegateUtil.getSolrPendienteDelegate();
			pendienteDel.grabarSolrPendiente(EnumCategoria.GUSITE_FAQ.toString(), faq.getId(), null, 1L);
			return faq.getId();

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Obtiene una faq
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Faq obtenerFaq(Long id) {

		Session session = this.getSession();
		try {
			Faq faq = (Faq) session.get(Faq.class, id);
			return faq;

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Lista todas las faqs
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<?> listarFaqs() {

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
	 * Lista todas las faqs poniendole un idioma por defecto
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public ArrayList<Faq> listarFaqs(String idioma) {

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
	 * borra una faq
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public void borrarFaq(Long id) throws DelegateException {

		Session session = this.getSession();
		try {
			Transaction tx = session.beginTransaction();
			Faq faq = (Faq) session.get(Faq.class, id);

			session.createQuery(
					"delete from TraduccionFaq tfaq where tfaq.id.codigoFaq = "
							+ id).executeUpdate();
			session.createQuery("delete from Faq faq where faq.id = " + id)
					.executeUpdate();
			session.flush();
			tx.commit();
			this.close(session);

			this.grabarAuditoria(faq, Auditoria.ELIMINAR);
			
			//DesIndexamos
			SolrPendienteDelegate pendienteDel = DelegateUtil.getSolrPendienteDelegate();
			pendienteDel.grabarSolrPendiente(EnumCategoria.GUSITE_FAQ.toString(), faq.getId(), null, 0L);

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
	private ArrayList<Faq> crearlistadostateful(List<?> listado, String idioma) {

		ArrayList<Faq> lista = new ArrayList<Faq>();
		Iterator<?> iter = listado.iterator();
		Faq faq;
		while (iter.hasNext()) {
			faq = new Faq();
			faq = (Faq) iter.next();
			faq.setIdi(idioma);
			lista.add(faq);
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
			Query query = session
					.createQuery("from Faq faq where faq.idmicrosite = "
							+ site.toString() + " and faq.id = "
							+ id.toString());
			return query.list().isEmpty();

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}
	
	/**
	 * Obtiene una faq
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Faq obtenerFaqBySolr(Long id) {

		final Session session = this.getSession();
		try {
			return (Faq) session.get(Faq.class, id);
		} catch (Exception exception) {
			log.error("Error obteniendo faq.", exception);
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
		log.debug("FaqfacadeEJB.indexarSolr. idElemento:" + idElemento +" categoria:"+categoria);
		
		try {
			MicrositeDelegate micrositedel = DelegateUtil.getMicrositeDelegate();
			
			//Paso 0. Obtenemos el contenido y comprobamos si se puede indexar.
			final Faq faq = obtenerFaqBySolr(idElemento);
			if (faq == null) {
				return new SolrPendienteResultado(true, "Error obteniendo el faq.");
			}
			
			if (!IndexacionUtil.isIndexable(faq)) {
				return new SolrPendienteResultado(true, "No se puede indexar");
			}
			
			Microsite micro = micrositedel.obtenerMicrosite(faq.getIdmicrosite());
			if (!IndexacionUtil.isIndexable(micro)) {
				return new SolrPendienteResultado(true, "No se puede indexar");
			}
			
						
			
			//Recorremos las traducciones
			final MultilangLiteral titulo = new MultilangLiteral();
			final MultilangLiteral descripcion = new MultilangLiteral();
			final MultilangLiteral urls = new MultilangLiteral();
			final MultilangLiteral searchText = new MultilangLiteral();
			final MultilangLiteral searchTextOptional = new MultilangLiteral();
			final List<EnumIdiomas> idiomas = new ArrayList<EnumIdiomas>();
			final MultilangLiteral tituloPadre = new MultilangLiteral();
			final MultilangLiteral urlPadre = new MultilangLiteral();
			List<PathUO> uosPath = null;
			for (String keyIdioma : faq.getTraducciones().keySet()) {
				final EnumIdiomas enumIdioma = EnumIdiomas.fromString(keyIdioma);
				final TraduccionFaq traduccion = (TraduccionFaq) faq.getTraduccion(keyIdioma);
		    	
				if (traduccion != null && enumIdioma != null) {
					
					if (StringUtils.isBlank(traduccion.getPregunta())) {
						continue;
					}
					
					PathUOResult pathUo = IndexacionUtil.calcularPathUOsMicrosite(micro, keyIdioma);
					
					idiomas.add(enumIdioma);
					titulo.addIdioma(enumIdioma, traduccion.getPregunta() != null ? solrIndexer.htmlToText(traduccion.getPregunta()):"");
					descripcion.addIdioma(enumIdioma, traduccion.getPregunta() != null ? solrIndexer.htmlToText(traduccion.getPregunta()):"");
					
					// Texto busqueda
					TraduccionTemafaq tradTema = (TraduccionTemafaq) faq.getTema().getTraduccion(keyIdioma);
					String search=solrIndexer.htmlToText((traduccion.getPregunta()==null?"":traduccion.getPregunta())  
			    			+ " " + (traduccion.getRespuesta()==null?"":traduccion.getRespuesta()) 
			    			+ " " + (tradTema.getNombre()==null?"":tradTema.getNombre()));
			    	searchText.addIdioma(enumIdioma, solrIndexer.htmlToText(search));
			    	
			    	// Texto busqueda opcional
			    	searchTextOptional.addIdioma(enumIdioma, pathUo.getUosText());

			    	// URL
			    	urls.addIdioma(enumIdioma, IndexacionUtil.getUrlFaq(micro, faq, keyIdioma));
			    	
			    	// Padre
			    	urlPadre.addIdioma(enumIdioma, IndexacionUtil.getUrlMicrosite(micro, keyIdioma));	    		
			    	tituloPadre.addIdioma(enumIdioma, IndexacionUtil.getTituloMicrosite(micro, keyIdioma));
			    	
			    	uosPath = pathUo.getUosPath();
				}
			}
			
			final IndexData indexData = new IndexData();
			indexData.setCategoria(categoria);
			indexData.setAplicacionId(EnumAplicacionId.GUSITE);
			indexData.setElementoId(idElemento.toString());
			indexData.setFechaPublicacion(faq.getFecha());			
			indexData.setTitulo(titulo);
			indexData.setDescripcion(descripcion);
			indexData.setUrl(urls);
			indexData.setSearchText(searchText);
			indexData.setSearchTextOptional(searchTextOptional);
			indexData.setIdiomas(idiomas);
			indexData.setElementoIdPadre(micro.getId().toString());
			indexData.setCategoriaPadre(EnumCategoria.GUSITE_MICROSITE);
			indexData.setDescripcionPadre(tituloPadre);
			indexData.setUrlPadre(urlPadre);
			indexData.setUos(uosPath);
			indexData.setMicrositeId(micro.getId().toString());
			indexData.setInterno(!micro.getRestringido().equals("N") ? true : false);
				
			solrIndexer.indexarContenido(indexData);
			
			return new SolrPendienteResultado(true);
		} catch(Exception exception) {
			log.error("Error en contenidofacade intentando indexar.", exception);
			return new SolrPendienteResultado(false, exception.getMessage());
		}
	}
	
	
	
	/**
	 * Obtiene los faqs de un microsite
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<Faq> obtenerFaqsByMicrositeId(Long idMicrosite) {

		Session session = this.getSession();		

		try {
			
			Query query = session.createQuery("from Faq f where f.idmicrosite =" + idMicrosite.toString());
			
			List<Faq> lista = query.list();
									
			return lista;

		} catch (HibernateException he) {
			
			throw new EJBException(he);
			
		}  finally {
			
			this.close(session);
			
		}
	

    }
}
