package es.caib.gusite.micropersistence.ejb;

import java.util.ArrayList;
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
import es.caib.gusite.micromodel.TraduccionTemafaq;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.MicrositeDelegate;
import es.caib.gusite.micropersistence.delegate.SolrPendienteDelegate;
import es.caib.gusite.micropersistence.util.IndexacionUtil;
import es.caib.gusite.micropersistence.util.PathUOResult;
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
		} catch (final Exception ex) {
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
	public void init(final Long site) {
		super.tampagina = 10;
		super.pagina = 0;
		super.select = "select faq";
		super.from = " from Faq faq join faq.traducciones trad ";
		super.where = " where trad.id.codigoIdioma = '" + Idioma.getIdiomaPorDefecto() + "' and faq.idmicrosite = "
				+ site.toString();
		super.whereini = " ";
		super.orderby = "";

		super.camposfiltro = new String[] { "trad.pregunta", "trad.respuesta", "trad.url", "trad.urlnom" };
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
		super.where = " where trad.id.codigoIdioma = '" + Idioma.getIdiomaPorDefecto() + "'";
		super.whereini = " ";
		super.orderby = "";

		super.camposfiltro = new String[] { "trad.pregunta", "trad.respuesta", "trad.url", "trad.urlnom" };
		super.cursor = 0;
		super.nreg = 0;
		super.npags = 0;
	}

	/**
	 * Crea o actualiza una faq
	 *
	 * @ejb.interface-method
	 * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public Long grabarFaq(final Faq faq) throws DelegateException {

		final Session session = this.getSession();
		try {
			final boolean nuevo = (faq.getId() == null) ? true : false;
			final Transaction tx = session.beginTransaction();

			final Map<String, TraduccionFaq> listaTraducciones = new HashMap<String, TraduccionFaq>();
			if (nuevo) {
				final Iterator<TraduccionFaq> it = faq.getTraducciones().values().iterator();
				while (it.hasNext()) {
					final TraduccionFaq trd = it.next();
					listaTraducciones.put(trd.getId().getCodigoIdioma(), trd);
				}
				faq.setTraducciones(null);
			}

			session.saveOrUpdate(faq);
			session.flush();

			if (nuevo) {
				for (final TraduccionFaq trad : listaTraducciones.values()) {
					trad.getId().setCodigoFaq(faq.getId());
					session.saveOrUpdate(trad);
				}
				session.flush();
				faq.setTraducciones(listaTraducciones);
			}

			tx.commit();
			this.close(session);

			final int op = (nuevo) ? Auditoria.CREAR : Auditoria.MODIFICAR;
			this.grabarAuditoria(faq, op);

			// Indexamos
			final SolrPendienteDelegate pendienteDel = DelegateUtil.getSolrPendienteDelegate();
			pendienteDel.grabarSolrPendiente(EnumCategoria.GUSITE_FAQ.toString(), faq.getId(), null,
					IndexacionUtil.REINDEXAR);
			return faq.getId();

		} catch (final HibernateException he) {
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
	public Faq obtenerFaq(final Long id) {

		final Session session = this.getSession();
		try {
			final Faq faq = (Faq) session.get(Faq.class, id);
			return faq;

		} catch (final HibernateException he) {
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

		final Session session = this.getSession();
		try {
			this.parametrosCons(); // Establecemos los parámetros de la
									// paginación
			final Query query = session.createQuery(this.select + this.from + this.where + this.orderby);
			query.setFirstResult(this.cursor - 1);
			query.setMaxResults(this.tampagina);
			return query.list();

		} catch (final HibernateException he) {
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
	public ArrayList<Faq> listarFaqs(final String idioma) {

		final Session session = this.getSession();
		try {
			this.parametrosCons(); // Establecemos los parámetros de la
									// paginación
			final Query query = session.createQuery(this.select + this.from + this.where + this.orderby);
			query.setFirstResult(this.cursor - 1);
			query.setMaxResults(this.tampagina);
			return this.crearlistadostateful(query.list(), idioma);

		} catch (final HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * borra una faq
	 *
	 * @ejb.interface-method
	 * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public void borrarFaq(final Long id) throws DelegateException {
		borrarFaq(id, true);
	}

	/**
	 * borra una faq
	 *
	 * @ejb.interface-method
	 * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public void borrarFaq(final Long id, final boolean indexar) throws DelegateException {

		final Session session = this.getSession();
		try {
			final Transaction tx = session.beginTransaction();
			final Faq faq = (Faq) session.get(Faq.class, id);

			session.createQuery("delete from TraduccionFaq tfaq where tfaq.id.codigoFaq = " + id).executeUpdate();
			session.createQuery("delete from Faq faq where faq.id = " + id).executeUpdate();
			session.flush();
			tx.commit();
			this.close(session);

			this.grabarAuditoria(faq, Auditoria.ELIMINAR);

			// DesIndexamos
			if (indexar) {
				final SolrPendienteDelegate pendienteDel = DelegateUtil.getSolrPendienteDelegate();
				pendienteDel.grabarSolrPendiente(EnumCategoria.GUSITE_FAQ.toString(), faq.getId(), null,
						IndexacionUtil.DESINDEXAR);
			}
		} catch (final HibernateException he) {
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
	private ArrayList<Faq> crearlistadostateful(final List<?> listado, final String idioma) {

		final ArrayList<Faq> lista = new ArrayList<Faq>();
		final Iterator<?> iter = listado.iterator();
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
	 * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public boolean checkSite(final Long site, final Long id) {

		final Session session = this.getSession();
		try {
			final Query query = session.createQuery(
					"from Faq faq where faq.idmicrosite = " + site.toString() + " and faq.id = " + id.toString());
			return query.list().isEmpty();

		} catch (final HibernateException he) {
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
	public Faq obtenerFaqBySolr(final Long id) {

		final Session session = this.getSession();
		try {
			return (Faq) session.get(Faq.class, id);
		} catch (final Exception exception) {
			log.error("Error obteniendo faq.", exception);
			return null;
		} finally {
			this.close(session);
		}
	}

	/**
	 * Método para indexar según la id y la categoria.
	 * 
	 * @param solrIndexer
	 * @param idElemento
	 * @param categoria
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 * @ejb.transaction type="RequiresNew"
	 */
	public SolrPendienteResultado indexarSolr(final SolrIndexer solrIndexer, final Long idElemento,
			final EnumCategoria categoria, final PathUOResult iPathUO) {
		log.debug("FaqfacadeEJB.indexarSolr. idElemento:" + idElemento + " categoria:" + categoria);

		try {
			final MicrositeDelegate micrositedel = DelegateUtil.getMicrositeDelegate();

			// Paso 0. Obtenemos el contenido y comprobamos si se puede indexar.
			final Faq faq = obtenerFaqBySolr(idElemento);
			if (faq == null) {
				return new SolrPendienteResultado(true, "Error obteniendo el faq.");
			}

			if (!IndexacionUtil.isIndexable(faq)) {
				return new SolrPendienteResultado(true, "No se puede indexar");
			}

			final Microsite micro = micrositedel.obtenerMicrosite(faq.getIdmicrosite());
			if (!IndexacionUtil.isIndexable(micro)) {
				return new SolrPendienteResultado(true, "No se puede indexar");
			}

			// Recorremos las traducciones
			final MultilangLiteral titulo = new MultilangLiteral();
			final MultilangLiteral descripcion = new MultilangLiteral();
			final MultilangLiteral urls = new MultilangLiteral();
			final MultilangLiteral searchText = new MultilangLiteral();
			final MultilangLiteral searchTextOptional = new MultilangLiteral();
			final List<EnumIdiomas> idiomas = new ArrayList<EnumIdiomas>();
			final MultilangLiteral tituloPadre = new MultilangLiteral();
			final MultilangLiteral urlPadre = new MultilangLiteral();
			List<PathUO> uosPath = null;
			for (final String keyIdioma : faq.getTraducciones().keySet()) {
				final EnumIdiomas enumIdioma = EnumIdiomas.fromString(keyIdioma);
				final TraduccionFaq traduccion = (TraduccionFaq) faq.getTraduccion(keyIdioma);

				if (traduccion != null && enumIdioma != null) {

					if (StringUtils.isBlank(traduccion.getPregunta())) {
						continue;
					}

					PathUOResult pathUO;
					if (iPathUO == null) {
						pathUO = IndexacionUtil.calcularPathUOsMicrosite(micro, keyIdioma);
					} else {
						pathUO = iPathUO;
					}

					// Si sigue a nulo, es que no es visible o no existe la UO.
					if (pathUO == null) {
						return new SolrPendienteResultado(false,
								"El microsite està associat a una Unitat Orgànica inexistent, per favor, posis en contacte amb l'administrador.");
					}

					idiomas.add(enumIdioma);
					titulo.addIdioma(enumIdioma,
							traduccion.getPregunta() != null ? solrIndexer.htmlToText(traduccion.getPregunta()) : "");
					descripcion.addIdioma(enumIdioma,
							traduccion.getPregunta() != null ? solrIndexer.htmlToText(traduccion.getPregunta()) : "");

					// Texto busqueda
					final TraduccionTemafaq tradTema = (TraduccionTemafaq) faq.getTema().getTraduccion(keyIdioma);
					final String search = solrIndexer
							.htmlToText((traduccion.getPregunta() == null ? "" : traduccion.getPregunta()) + " "
									+ (traduccion.getRespuesta() == null ? "" : traduccion.getRespuesta()) + " "
									+ (tradTema.getNombre() == null ? "" : tradTema.getNombre()));
					searchText.addIdioma(enumIdioma, solrIndexer.htmlToText(search));

					// Texto busqueda opcional
					searchTextOptional.addIdioma(enumIdioma, pathUO.getUosText());

					// URL
					urls.addIdioma(enumIdioma, IndexacionUtil.getUrlFaq(micro, faq, keyIdioma));

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
			indexData.setCategoriaRaiz(EnumCategoria.GUSITE_MICROSITE);
			indexData.setElementoIdRaiz(micro.getId().toString());
			indexData.setInterno(IndexacionUtil.isRestringidoMicrosite(micro));

			solrIndexer.indexarContenido(indexData);

			return new SolrPendienteResultado(true);
		} catch (final Exception exception) {
			log.error("Error intentando indexar idElemento:" + idElemento + " categoria:" + categoria, exception);
			return new SolrPendienteResultado(false, exception.getMessage());
		}
	}

	/**
	 * Obtiene los faqs de un microsite
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<Faq> obtenerFaqsByMicrositeId(final Long idMicrosite) {

		final Session session = this.getSession();

		try {

			final Query query = session.createQuery("from Faq f where f.idmicrosite =" + idMicrosite.toString());

			final List<Faq> lista = query.list();

			return lista;

		} catch (final HibernateException he) {

			throw new EJBException(he);

		} finally {

			this.close(session);

		}

	}
}
