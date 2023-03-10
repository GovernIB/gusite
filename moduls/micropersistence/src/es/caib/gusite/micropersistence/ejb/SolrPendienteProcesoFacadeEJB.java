package es.caib.gusite.micropersistence.ejb;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import javax.ejb.CreateException;
import javax.ejb.EJBException;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.hibernate.Session;

import es.caib.gusite.micromodel.Agenda;
import es.caib.gusite.micromodel.Archivo;
import es.caib.gusite.micromodel.Contenido;
import es.caib.gusite.micromodel.Encuesta;
import es.caib.gusite.micromodel.Faq;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.Noticia;
import es.caib.gusite.micromodel.SolrPendiente;
import es.caib.gusite.micromodel.SolrPendienteJob;
import es.caib.gusite.micromodel.SolrPendienteResultado;
import es.caib.gusite.micromodel.Traduccion;
import es.caib.gusite.micromodel.TraduccionAgenda;
import es.caib.gusite.micromodel.TraduccionNoticia;
import es.caib.gusite.micropersistence.delegate.AgendaDelegate;
import es.caib.gusite.micropersistence.delegate.ArchivoDelegate;
import es.caib.gusite.micropersistence.delegate.ContenidoDelegate;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.EncuestaDelegate;
import es.caib.gusite.micropersistence.delegate.FaqDelegate;
import es.caib.gusite.micropersistence.delegate.IdiomaDelegate;
import es.caib.gusite.micropersistence.delegate.MicrositeDelegate;
import es.caib.gusite.micropersistence.delegate.NoticiaDelegate;
import es.caib.gusite.micropersistence.delegate.SolrPendienteDelegate;
import es.caib.gusite.micropersistence.util.IndexacionUtil;
import es.caib.gusite.micropersistence.util.PathUOResult;
import es.caib.gusite.plugins.PluginFactory;
import es.caib.gusite.plugins.organigrama.OrganigramaProvider;
import es.caib.gusite.plugins.organigrama.UnidadListData;
import es.caib.gusite.utilities.date.Fechas;
import es.caib.gusite.utilities.job.GusiteJobUtil;
import es.caib.gusite.utilities.property.GusitePropertiesUtil;
import es.caib.solr.api.SolrFactory;
import es.caib.solr.api.SolrIndexer;
import es.caib.solr.api.exception.ExcepcionSolrApi;
import es.caib.solr.api.model.types.EnumAplicacionId;
import es.caib.solr.api.model.types.EnumCategoria;

/**
 * SessionBean para manipular datos de las respuestas.
 *
 * @ejb.bean name="sac/micropersistence/SolrPendienteProcesoFacade"
 *           jndi-name="es.caib.gusite.micropersistence.SolrPendienteProcesoFacade"
 *           type="Stateless" view-type="remote" transaction-type="Container"
 *
 * @ejb.transaction type="NotSupported"
 */
public abstract class SolrPendienteProcesoFacadeEJB extends HibernateEJB {

	private static final long serialVersionUID = 6661917618076931960L;

	/**
	 * @ejb.create-method
	 * @ejb.permission unchecked="true"
	 */
	@Override
	public void ejbCreate() throws CreateException {
		super.ejbCreate();
	}

	/**
	 * Indexa Pendientes
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 *
	 * @return Booleano indicando si se indexan todos los procesos pendientes .
	 * @throws Exception
	 */

	public Boolean indexarPendientes(final SolrPendienteJob solrJob) throws Exception {
		GusiteJobUtil.interrumpirTarea = false;
		SolrIndexer solrIndexer = null;
		try {

			final String username = GusitePropertiesUtil.getUserSOLR();
			final String password = GusitePropertiesUtil.getPassSOLR();
			final String index = GusitePropertiesUtil.getIndexSOLR();
			final String urlSolr = GusitePropertiesUtil.getUrlSOLR();
			final int diasMaximos = GusitePropertiesUtil.getDiasMaxSOLR();
			final String iactivo = GusitePropertiesUtil.getActivoSOLR();
			final boolean solrActivo = iactivo != null && !"S".equals(iactivo.toUpperCase());

			final String urlElastic = GusitePropertiesUtil.getUrlELASTIC();
			final String userElastic = GusitePropertiesUtil.getUserELASTIC();
			final String passElastic = GusitePropertiesUtil.getPassELASTIC();
			final String iactivoElastic = GusitePropertiesUtil.getActivoELASTIC();
			final boolean elasticActivo = iactivoElastic != null && !"S".equals(iactivo.toUpperCase());

			solrIndexer = SolrFactory.getIndexer(urlSolr, index, EnumAplicacionId.GUSITE, username, password,
					urlElastic, userElastic, passElastic, solrActivo, elasticActivo);

			final List<SolrPendiente> listPendientes = DelegateUtil.getSolrPendienteDelegate().getPendientes();

			final StringBuffer info = new StringBuffer();

			int i = 0;
			for (final SolrPendiente solrPendiente : listPendientes) {
				i++;
				SolrPendienteResultado solrPendienteResultado = null;

				if (GusiteJobUtil.interrumpirTarea) { // El semaforo para salir de esto.
					break;
				}
				try {

					solrPendienteResultado = indexarPendiente(solrPendiente, info);

				} catch (final Exception exception) {
					log.error("Exception indexando " + solrPendiente, exception);
					solrPendienteResultado = new SolrPendienteResultado(false, exception.getMessage());
					info.append(" Exception indexando " + solrPendiente.getIdElem().toString() + ")  <br /> ");

				}

				if (solrPendienteResultado != null) {
					if (solrPendienteResultado.isCorrecto()) {
						solrPendiente.setResultado(1);
						solrPendiente.setMensajeError(solrPendienteResultado.getMensaje());
						DelegateUtil.getSolrPendienteDelegate().actualizarSolrPendiente(solrPendiente);
					} else {

						final long diff = new Date().getTime() - solrPendiente.getFechaCreacion().getTime();
						final int dias = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

						// Si hace 10 dias o + que se crea se marca como erronea porque no se ha podido
						// endexar
						if (dias >= diasMaximos) {
							solrPendiente.setResultado(-1);
							info.append(" Exception indexando " + solrPendiente.getIdElem().toString() + ") hace "
									+ diasMaximos + " días o más  <br /> ");
						} else {
							log.error("No se ha podido realizar la operación (dias ejecutandose:" + dias
									+ ")con el registro : " + solrPendiente.getId());
							info.append(" No se ha podido realizar la operación (dias ejecutandose:" + dias
									+ ")con el registro : " + solrPendiente.getId() + ")  <br /> ");
						}
						solrPendiente.setMensajeError(solrPendienteResultado.getMensaje());
						DelegateUtil.getSolrPendienteDelegate().actualizarSolrPendiente(solrPendiente);
					}

				}

				if (i % 20 == 0) {
					try {
						solrIndexer.commit();
					} catch (final ExcepcionSolrApi e) {
						log.error("No se ha podido comitear la indexación" + e.getMessage());
						info.append(" No se ha podido comitear la indexación" + e.getMessage() + " <br /> ");
					}
				}
			}

			try {
				solrIndexer.commit();
			} catch (final ExcepcionSolrApi e) {
				log.error("No se ha podido comitear la indexación" + e.getMessage());
				info.append(" No se ha podido comitear la indexación" + e.getMessage() + " <br /> ");
			}

			// solrJob.setDescripcion(GusiteClobUtil.getClob(info.toString()));
			solrJob.setDescripcion(info.toString());

			return true;
		} catch (final Exception exception) {
			log.error("Error indexando pendientes.", exception);
			throw new Exception(exception);
		}
	}

	/**
	 * Indexa Microsite
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 *
	 * @return Booleano indicando si se ha indexado el microsite .
	 * @throws Exception
	 */
	public SolrPendienteResultado indexarMicrosite(final Long idMicrosite, final SolrPendienteJob solrPendienteJob,
			final StringBuffer info) throws Exception {
		try {
			return indexacionMicrosite(idMicrosite, solrPendienteJob, info);
		} catch (final Exception exception) {
			log.error("Error indexando microsite.", exception);
			throw new Exception(exception);
		}
	}

	/**
	 * Indexa Todos los microsites.
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 * @return Booleano indicando si se ha indexado todo .
	 * @throws Exception
	 */

	public Boolean indexarTodo(final SolrPendienteJob solrPendienteJob) throws Exception {
		GusiteJobUtil.interrumpirTarea = false;
		final Session session = null;
		try {
			final StringBuffer info = new StringBuffer();
			final MicrositeDelegate micrositedel = DelegateUtil.getMicrositeDelegate();
			final List<Long> listaMicro = micrositedel.listarMicrosites();

			int correctos = 0, incorrectos = 0;
			for (int i = 0; i < listaMicro.size(); i++) {

				final Long idMicro = listaMicro.get(i);
				if (idMicro != null) {

					SolrPendienteResultado resultado = null;
					try {
						if (GusiteJobUtil.interrumpirTarea) { // El semaforo para salir de esto.
							info.insert(0, "Finalizado a la fuerza!.<br />");
							break;
						}
						resultado = indexacionMicrosite(idMicro, solrPendienteJob, info);
					} catch (final Exception he) {
						log.error("No se ha indexado el microsite con id " + idMicro, he);
						info.append(" No se ha indexado el microsite con id " + idMicro + "  <br /> ");
					}

					// solrPendienteJob.setDescripcion(GusiteClobUtil.getClob("La indexació està
					// activa. Porta indexats "+(i+1)+" microsites
					// ("+(100*(i+1)/listaMicro.size())+"%). <br /> "+info.toString()));
					solrPendienteJob.setDescripcion("La indexació està activa. Porta indexats " + (i + 1)
							+ " microsites (" + (100 * (i + 1) / listaMicro.size()) + "%). <br /> " + info.toString());
					DelegateUtil.getSolrPendienteDelegate().actualizarSorlPendienteJob(solrPendienteJob);

					if (resultado != null && resultado.isIndexable()) {

						if (resultado.isCorrecto()) {
							correctos++;
						} else {
							incorrectos++;
						}

					}
				}

			}

			final String infoResumen = "La indexació ha finalitzat. Dels " + listaMicro.size()
					+ " microsites en total, eren indexables " + (correctos + incorrectos) + " dels quals " + correctos
					+ " han estat correctes i " + incorrectos + " estat incorrectes. <br />";
			// solrPendienteJob.setDescripcion(GusiteClobUtil.getClob(infoResumen+info.toString()));
			solrPendienteJob.setDescripcion(infoResumen + info.toString());
			return true;
		} catch (final Exception exception) {
			log.error("Error indexando todo", exception);
			throw new Exception(exception);
		} finally {
			close(session);
		}
	}

	/**
	 * Indexa Todos los microsites que no están indexados.
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 * @return Booleano indicando si se ha indexado todo .
	 * @throws Exception
	 */
	public Boolean indexarTodoSinIndexar(final SolrPendienteJob solrPendienteJob) throws Exception {
		GusiteJobUtil.interrumpirTarea = false;
		final Session session = null;
		try {
			final StringBuffer info = new StringBuffer();
			if (solrPendienteJob.getInfo() != null && !solrPendienteJob.getInfo().isEmpty()) {
				final int posicion = solrPendienteJob.getInfo().indexOf("<br ");
				if (posicion <= 0) {
					info.append(solrPendienteJob.getInfo());
				} else {
					info.append(solrPendienteJob.getInfo().substring(posicion + 5));
				}
			}

			final MicrositeDelegate micrositedel = DelegateUtil.getMicrositeDelegate();
			final int descansoMicrosite = GusitePropertiesUtil.getCuantosMicrosites();
			final List<Long> listaMicro = micrositedel.listarMicrositesSinIndexar(descansoMicrosite);

			Long correctos = 0l, incorrectos = 0l;
			correctos = micrositedel.getCuantosMicrosites(Microsite.INDEXADO, Microsite.INDEXADO_CORRECTAMENTE);
			incorrectos = micrositedel.getCuantosMicrosites(Microsite.INDEXADO, Microsite.INDEXADO_INCORRECTAMENTE);

			for (int i = 0; i < listaMicro.size(); i++) {

				final Long idMicro = listaMicro.get(i);
				if (idMicro != null) {

					SolrPendienteResultado resultado = null;
					try {
						if (GusiteJobUtil.interrumpirTarea) { // El semaforo para salir de esto.
							info.insert(0, "Finalizado a la fuerza!.<br />");
							break;
						}
						resultado = indexacionMicrosite(idMicro, solrPendienteJob, info);
					} catch (final Exception he) {
						log.error("No se ha indexado el microsite con id " + idMicro, he);
						info.append(" No se ha indexado el microsite con id " + idMicro + "  <br /> ");
					}

					// solrPendienteJob.setDescripcion(GusiteClobUtil.getClob("La indexació està
					// activa. Porta indexats "+(i+1)+" microsites
					// ("+(100*(i+1)/listaMicro.size())+"%). <br /> "+info.toString()));
					solrPendienteJob.setDescripcion("La indexació està activa. Porta indexats " + (i + 1)
							+ " microsites (" + (100 * (i + 1) / listaMicro.size()) + "%). <br /> " + info.toString());
					DelegateUtil.getSolrPendienteDelegate().actualizarSorlPendienteJob(solrPendienteJob);

					if (resultado != null && resultado.isIndexable()) {

						if (resultado.isCorrecto()) {
							correctos++;
						} else {
							incorrectos++;
						}

					}
				}

			}
			info.append(Fechas.formatFecha(Calendar.getInstance().getTime(), "dd-MM-yyyy HH:mm:ss")
					+ " - Descanso por finalización de indexación por bloque.<br /><br />");

			final String infoResumen = "La indexació ha finalitzat. S'han indexat " + (correctos + incorrectos)
					+ " microsites, dels quals " + incorrectos + " s'han indexat amb errors. <br />";
			// solrPendienteJob.setDescripcion(GusiteClobUtil.getClob(infoResumen+info.toString()));
			solrPendienteJob.setDescripcion(infoResumen + info.toString());
			return true;
		} catch (final Exception exception) {
			log.error("Error indexando todo", exception);
			throw new Exception(exception);
		} finally {
			close(session);
		}
	}

	/**
	 * Indexa Microsite
	 *
	 * @return Booleano indicando si se ha indexado el microsite .
	 * @throws Exception
	 */
	private SolrPendienteResultado indexacionMicrosite(final Long idMicrosite, final SolrPendienteJob solrPendienteJob,
			StringBuffer info) throws DelegateException {
		GusiteJobUtil.interrumpirTarea = false;
		SolrIndexer solrIndexer = null;
		final SolrPendienteResultado resultadoMicrosite = new SolrPendienteResultado(true);
		boolean todoCorrecto = true;

		try {
			boolean actualizarSolrPendiente;
			if (info == null) {
				info = new StringBuffer();
				actualizarSolrPendiente = true;
			} else {
				actualizarSolrPendiente = false;
			}

			// Verificamos si es indexable
			final MicrositeDelegate micrositedel = DelegateUtil.getMicrositeDelegate();
			final Microsite micro = micrositedel.obtenerMicrosite(idMicrosite);
			if (micro == null) {
				info.append("Microsite " + idMicrosite + " <br />");
			} else {
				info.append("Microsite " + idMicrosite + ":" + micro.getUri() + "<br />");
			}

			final String username = GusitePropertiesUtil.getUserSOLR();
			final String password = GusitePropertiesUtil.getPassSOLR();
			final String index = GusitePropertiesUtil.getIndexSOLR();
			final String urlSolr = GusitePropertiesUtil.getUrlSOLR();
			final String iactivo = GusitePropertiesUtil.getActivoSOLR();
			final boolean solrActivo = iactivo != null && !"S".equals(iactivo.toUpperCase());

			final String urlElastic = GusitePropertiesUtil.getUrlELASTIC();
			final String userElastic = GusitePropertiesUtil.getUserELASTIC();
			final String passElastic = GusitePropertiesUtil.getPassELASTIC();
			final String iactivoElastic = GusitePropertiesUtil.getActivoELASTIC();
			final boolean elasticActivo = iactivoElastic != null && !"S".equals(iactivo.toUpperCase());

			solrIndexer = SolrFactory.getIndexer(urlSolr, index, EnumAplicacionId.GUSITE, username, password,
					urlElastic, userElastic, passElastic, solrActivo, elasticActivo);
			// Desindexamos el microsite
			try {
				info.append(Fechas.formatFecha(Calendar.getInstance().getTime(), "dd-MM-yyyy HH:mm:ss")
						+ " - Anem a desindexar el microsite arrel.<br />");
				solrIndexer.desindexarRaiz(idMicrosite.toString(), EnumCategoria.GUSITE_MICROSITE);
			} catch (final ExcepcionSolrApi e) {
				log.error("S'ha produit un error dexindexant el MICROSITE amb id " + idMicrosite, e);
				info.append("**S'ha produit un error dexindexant.");
				return new SolrPendienteResultado(false,
						"S'ha produit un error dexindexant el MICROSITE amb id " + idMicrosite);
			}

			final SolrPendienteDelegate solrpendientedel = DelegateUtil.getSolrPendienteDelegate();

			if (actualizarSolrPendiente && solrPendienteJob != null) {
				// solrPendienteJob.setDescripcion(GusiteClobUtil.getClob(info.toString()));
				solrPendienteJob.setDescripcion(info.toString());
				solrpendientedel.actualizarSorlPendienteJob(solrPendienteJob);
			}

			int totalCorrectos, totalIncorrectos;

			if (IndexacionUtil.isIndexable(micro)) {

				final PathUOResult pathUO = IndexacionUtil.calcularPathUOsMicrosite(micro, "ca");

				if (pathUO == null) {

					info.append("El microsite amb id " + idMicrosite
							+ " està associat a una Unitat Orgànica inexistent o no visible.<br /><br />");
					resultadoMicrosite.setIndexable(false);

				} else {
					info.append(Fechas.formatFecha(Calendar.getInstance().getTime(), "dd-MM-yyyy HH:mm:ss")
							+ " - Anem a indexar els components<br />");

					// Obtenemos los ARCHIVOS del microsite
					info.append("** Arxius:<br />");
					totalCorrectos = 0;
					totalIncorrectos = 0;
					final ArchivoDelegate archivodel = DelegateUtil.getArchivoDelegate();
					final List<Archivo> listArchivos = archivodel.obtenerArchivoByMicrositeId(idMicrosite);

					for (final Archivo archivo : listArchivos) {
						if (archivo != null && archivo.getId() != null) {
							final SolrPendienteResultado resultado = this.indexarPendienteArchivo(solrIndexer,
									micro.getId(), archivo.getId(), info, pathUO);
							if (resultado.isCorrecto()) {
								totalCorrectos++;
							} else {
								totalIncorrectos++;
							}
						}
					}
					if (totalIncorrectos > 0) {
						todoCorrecto = false;
					}
					info.append("**** Total arxius " + (totalCorrectos + totalIncorrectos) + " (Incorrectes:"
							+ totalIncorrectos + ") <br />");
					if (actualizarSolrPendiente) {
						// solrPendienteJob.setDescripcion(GusiteClobUtil.getClob(info.toString()));
						solrPendienteJob.setDescripcion(info.toString());
						solrpendientedel.actualizarSorlPendienteJob(solrPendienteJob);
					}

					// Liberamos memoria.
					listArchivos.clear();

					if (GusiteJobUtil.interrumpirTarea) { // El semaforo para salir de esto.
						return new SolrPendienteResultado(false,
								"Finalitzat per força mentre estava amb el MICROSITE id " + idMicrosite);
					}

					// Obtenemos las ENCUESTAS del microsite
					info.append("** Enquestes:<br />");
					totalCorrectos = 0;
					totalIncorrectos = 0;
					final EncuestaDelegate encuestadel = DelegateUtil.getEncuestaDelegate();
					final List<Encuesta> listEncuestas = encuestadel.obtenerEncuestasByMicrositeId(idMicrosite);

					for (final Encuesta encuesta : listEncuestas) {
						if (encuesta != null && encuesta.getId() != null) {
							final SolrPendienteResultado resultado = this.indexarPendienteEncuesta(solrIndexer,
									encuesta.getId(), info, pathUO);
							if (resultado.isCorrecto()) {
								totalCorrectos++;
							} else {
								totalIncorrectos++;
							}
						}
					}
					if (totalIncorrectos > 0) {
						todoCorrecto = false;
					}
					info.append("**** Total enquestes " + (totalCorrectos + totalIncorrectos) + " (Incorrectes:"
							+ totalIncorrectos + ") <br />");
					if (actualizarSolrPendiente) {
						// solrPendienteJob.setDescripcion(GusiteClobUtil.getClob(info.toString()));
						solrPendienteJob.setDescripcion(info.toString());
						solrpendientedel.actualizarSorlPendienteJob(solrPendienteJob);
					}

					// Liberamos memoria.
					listEncuestas.clear();

					if (GusiteJobUtil.interrumpirTarea) { // El semaforo para salir de esto.
						return new SolrPendienteResultado(false,
								"Finalitzat per força mentre estava amb el MICROSITE id " + idMicrosite);
					}

					// Obtenemos los FAQ's del microsite
					info.append("** Faqs: <br />");
					totalCorrectos = 0;
					totalIncorrectos = 0;
					final FaqDelegate faqdel = DelegateUtil.getFaqDelegate();
					final List<Faq> listFaqs = faqdel.obtenerFaqsByMicrositeId(idMicrosite);

					for (final Faq faq : listFaqs) {
						if (faq != null && faq.getId() != null) {
							final SolrPendienteResultado resultado = this.indexarPendienteFAQ(solrIndexer, faq.getId(),
									info, pathUO);
							if (resultado.isCorrecto()) {
								totalCorrectos++;
							} else {
								totalIncorrectos++;
							}
						}
					}
					if (totalIncorrectos > 0) {
						todoCorrecto = false;
					}
					info.append("**** Total faqs " + (totalCorrectos + totalIncorrectos) + " (Incorrectes:"
							+ totalIncorrectos + ") <br />");
					if (actualizarSolrPendiente) {
						// solrPendienteJob.setDescripcion(GusiteClobUtil.getClob(info.toString()));
						solrPendienteJob.setDescripcion(info.toString());
						solrpendientedel.actualizarSorlPendienteJob(solrPendienteJob);
					}

					// Liberamos memoria.
					listFaqs.clear();

					if (GusiteJobUtil.interrumpirTarea) { // El semaforo para salir de esto.
						return new SolrPendienteResultado(false,
								"Finalitzat per força mentre estava amb el MICROSITE id " + idMicrosite);
					}

					// Obtenemos las NOTICIAS del microsite
					info.append("** Noticies: <br />");
					totalCorrectos = 0;
					totalIncorrectos = 0;
					final NoticiaDelegate noticiadel = DelegateUtil.getNoticiasDelegate();
					final List<Noticia> listNoticias = noticiadel.obtenerNoticiasByMicrositeId(idMicrosite);

					for (final Noticia noticia : listNoticias) {
						if (noticia != null && noticia.getId() != null) {
							final SolrPendienteResultado resultado = this.indexarPendienteNoticia(solrIndexer,
									noticia.getId(), info, pathUO);
							if (resultado.isCorrecto()) {
								totalCorrectos++;
							} else {
								totalIncorrectos++;
							}
						}
					}
					if (totalIncorrectos > 0) {
						todoCorrecto = false;
					}
					info.append("**** Total noticias " + (totalCorrectos + totalIncorrectos) + " (Incorrectes:"
							+ totalIncorrectos + ") <br />");
					if (actualizarSolrPendiente) {
						// solrPendienteJob.setDescripcion(GusiteClobUtil.getClob(info.toString()));
						solrPendienteJob.setDescripcion(info.toString());
						solrpendientedel.actualizarSorlPendienteJob(solrPendienteJob);
					}

					// Liberamos memoria.
					listNoticias.clear();

					if (GusiteJobUtil.interrumpirTarea) { // El semaforo para salir de esto.
						return new SolrPendienteResultado(false,
								"Finalitzat per força mentre estava amb el MICROSITE id " + idMicrosite);
					}

					// Obtenemos las AGENDAS del microsite
					info.append("** Agendas: <br />");
					totalCorrectos = 0;
					totalIncorrectos = 0;
					final AgendaDelegate agendadel = DelegateUtil.getAgendaDelegate();
					final List<Agenda> listAgendas = agendadel.obtenerAgendasByMicrositeId(idMicrosite);

					for (final Agenda agenda : listAgendas) {
						if (agenda != null && agenda.getId() != null) {
							final SolrPendienteResultado resultado = this.indexarPendienteAgenda(solrIndexer,
									agenda.getId(), info, pathUO);
							if (resultado.isCorrecto()) {
								totalCorrectos++;
							} else {
								totalIncorrectos++;
							}
						}
					}
					if (totalIncorrectos > 0) {
						todoCorrecto = false;
					}
					info.append("**** Total agenda " + (totalCorrectos + totalIncorrectos) + " (Incorrectes:"
							+ totalIncorrectos + ") <br />");
					if (actualizarSolrPendiente) {
						// solrPendienteJob.setDescripcion(GusiteClobUtil.getClob(info.toString()));
						solrPendienteJob.setDescripcion(info.toString());
						solrpendientedel.actualizarSorlPendienteJob(solrPendienteJob);
					}

					// Liberamos memoria.
					listAgendas.clear();

					if (GusiteJobUtil.interrumpirTarea) { // El semaforo para salir de esto.
						return new SolrPendienteResultado(false,
								"Finalitzat per força mentre estava amb el MICROSITE id " + idMicrosite);
					}

					// Obtenemos los CONTENIDOS del microsite
					info.append("** Continguts: <br />");
					totalCorrectos = 0;
					totalIncorrectos = 0;
					final ContenidoDelegate contenidodel = DelegateUtil.getContenidoDelegate();
					final List<Contenido> listContenidos = contenidodel.listarAllContenidos(idMicrosite.toString());

					for (final Contenido contenido : listContenidos) {
						if (contenido != null && contenido.getId() != null) {
							final SolrPendienteResultado resultado = this.indexarPendienteContenido(solrIndexer,
									contenido.getId(), info, pathUO);
							if (resultado.isCorrecto()) {
								totalCorrectos++;
							} else {
								totalIncorrectos++;
							}
						}
					}
					if (totalIncorrectos > 0) {
						todoCorrecto = false;
					}
					info.append("**** Total continguts " + (totalCorrectos + totalIncorrectos) + " (Incorrectes:"
							+ totalIncorrectos + ") <br />");
					info.append(Fechas.formatFecha(Calendar.getInstance().getTime(), "dd-MM-yyyy HH:mm:ss")
							+ " - Fi de la indexacio <br /><br />");
					if (actualizarSolrPendiente) {
						// solrPendienteJob.setDescripcion(GusiteClobUtil.getClob(info.toString()));
						solrPendienteJob.setDescripcion(info.toString());
						solrpendientedel.actualizarSorlPendienteJob(solrPendienteJob);
					}

					// Liberamos memoria.
					listContenidos.clear();
				}
			} else {
				info.append("El microsite no es indexable con id " + idMicrosite + ".<br /><br />");
				resultadoMicrosite.setIndexable(false);
			}

			// Comiteamos los cambios.
			try {
				solrIndexer.commit();
				log.debug("Se ha indexado correctamente el microsite con id " + idMicrosite);
			} catch (final ExcepcionSolrApi e) {
				log.error("No se ha podido comitear la indexación" + e.getMessage(), e);
			}

			// Marcamos el microsite como indexado
			micrositedel.marcarComoIndexado(micro.getId(), Microsite.INDEXADO, todoCorrecto);

			return new SolrPendienteResultado(todoCorrecto,
					"Parece que ha funcionado correcto la indexación del MICROSITE con id " + idMicrosite
							+ ", ver la info para más información.");
		} catch (final Exception he) {
			throw new EJBException(he);
		} finally {
			if (solrPendienteJob != null) {
				// solrPendienteJob.setDescripcion(GusiteClobUtil.getClob(info.toString()));
				solrPendienteJob.setDescripcion(info.toString());
			}
		}
	}

	/**
	 * Indexa Todos los microsites que tengan una unidad administrativa
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 * @return Booleano indicando si se ha indexado todo los de la condicion
	 *         anterior .
	 * @throws RemoteException
	 * @throws Exception
	 */

	public Boolean indexarMicrositeByUA(final String idUAdministrativa, final SolrPendienteJob solrPendienteJob)
			throws Exception {
		GusiteJobUtil.interrumpirTarea = false;
		final Session session = null;
		try {

			final StringBuffer info = new StringBuffer();
			final MicrositeDelegate micrositedel = DelegateUtil.getMicrositeDelegate();

			final OrganigramaProvider op = PluginFactory.getInstance().getOrganigramaProvider();
			final Collection<UnidadListData> unidades = op.getUnidadesHijas(idUAdministrativa, "ca");
			final List<Integer> listaIds = new ArrayList<Integer>();
			listaIds.add(Integer.parseInt((idUAdministrativa)));
			for (final UnidadListData unidadListData : unidades) {
				final Long id = (Long) unidadListData.getId();
				listaIds.add(Integer.valueOf(id.intValue()));
			}

			final List<Long> listaMicro = micrositedel.obtenerMicrositesbyUA(listaIds);

			for (final Long idMicro : listaMicro) {
				if (GusiteJobUtil.interrumpirTarea) { // El semaforo para salir de esto.
					info.insert(0, "Finalizado a la fuerza!.<br />");
					break;
				}
				final Microsite micro = micrositedel.obtenerMicrosite(idMicro);
				if (micro.getId() != null) {
					try {
						info.append(" Vamos a enviar a indexación el microsite (id:" + micro.getId() + ", idUA: "
								+ idUAdministrativa + ")  <br /> ");
						indexacionMicrosite(micro.getId(), solrPendienteJob, info);
					} catch (final Exception he) {
						log.error("No se ha indexado el microsite con id " + micro.getId(), he);
						info.append("No se ha indexado el microsite con id " + micro.getId());
					}
				}

			}

			// solrPendienteJob.setDescripcion(GusiteClobUtil.getClob(info.toString()));
			solrPendienteJob.setDescripcion(info.toString());
			return true;

		} catch (final Exception exception) {
			log.error("Error indexando microsite por ua", exception);
			throw new Exception(exception);
		} finally {
			close(session);
		}

	}

	/**
	 * Indexa un solrpendiente.
	 *
	 * @param solrPendiente
	 * @param info
	 * @return
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 * @throws DelegateException
	 */
	public SolrPendienteResultado indexarPendiente(final SolrPendiente solrPendiente, final StringBuffer info)
			throws DelegateException {
		SolrPendienteResultado solrPendienteResultado;

		final String username = GusitePropertiesUtil.getUserSOLR();
		final String password = GusitePropertiesUtil.getPassSOLR();
		final String index = GusitePropertiesUtil.getIndexSOLR();
		final String urlSolr = GusitePropertiesUtil.getUrlSOLR();
		final String iactivo = GusitePropertiesUtil.getActivoSOLR();
		final boolean solrActivo = iactivo != null && !"S".equals(iactivo.toUpperCase());

		final String urlElastic = GusitePropertiesUtil.getUrlELASTIC();
		final String userElastic = GusitePropertiesUtil.getUserELASTIC();
		final String passElastic = GusitePropertiesUtil.getPassELASTIC();
		final String iactivoElastic = GusitePropertiesUtil.getActivoELASTIC();
		final boolean elasticActivo = iactivoElastic != null && !"S".equals(iactivo.toUpperCase());

		final SolrIndexer solrIndexer = SolrFactory.getIndexer(urlSolr, index, EnumAplicacionId.GUSITE, username,
				password, urlElastic, userElastic, passElastic, solrActivo, elasticActivo);

		info.append(" Vamos a enviar una desindexación  (id:" + solrPendiente.getIdElem() + ",tipo:"
				+ solrPendiente.getTipo() + ")  <br /> ");
		try {
			solrPendienteResultado = new SolrPendienteResultado(true);
			// Si es de tipo microsite y el archivo está relleno, es de tipo archivo
			if (solrPendiente.getTipo().equals(EnumCategoria.GUSITE_MICROSITE.toString())
					&& solrPendiente.getIdArchivo() != null) {
				// Los ficheros de microsites están indexados varias veces, una para cada
				// idioma, los desindexamos todos los idiomas
				// (se pueden haber borrado los idiomas del microsite, por eso se buscan en
				// todos los existentes)

				final IdiomaDelegate idiomaDelegate = DelegateUtil.getIdiomaDelegate();
				final List<Idioma> listaIdiomas = idiomaDelegate.listarIdiomas();
				Boolean almenosUnElementoDesindexado = false;
				Exception ultimoError = null;
				for (final Idioma idioma : listaIdiomas) {
					try {
						solrIndexer.desindexar(solrPendiente.getIdArchivo().toString() + "_" + idioma.getLang(),
								EnumCategoria.GUSITE_ARCHIVO);
						almenosUnElementoDesindexado = true;
					} catch (final Exception e) {
						log.error("  -- Error desindexando el archivo . " + solrPendiente.getIdArchivo().toString()
								+ "_" + idioma.getLang(), e);
						ultimoError = e;
					}
				}

				if (!almenosUnElementoDesindexado && ultimoError != null) {
					// hay error y no hay almenos una desindexación
					solrPendienteResultado = new SolrPendienteResultado(false,
							ExceptionUtils.getStackTrace(ultimoError));
				}

			} else {
				solrIndexer.desindexar(solrPendiente.getIdElem().toString(),
						EnumCategoria.fromString(solrPendiente.getTipo()));
			}

		} catch (final Exception exception) {
			log.error("  -- Error desindexando la entidad. ", exception);
			solrPendienteResultado = new SolrPendienteResultado(false, ExceptionUtils.getStackTrace(exception));
		}

		if (solrPendienteResultado.isCorrecto()) {
			if (!(new Long(solrPendiente.getAccion())).equals(IndexacionUtil.DESINDEXAR)) {
				// Si no es desindexar indexamos
				// si es desindexar fichero se asume que debe existir un solrPendiente.indexar
				// del padre del fichero encolado a posteriori.
				if (solrPendiente.getTipo().equals(EnumCategoria.GUSITE_AGENDA.toString())) {

					info.append(" Vamos a enviar una indexacion de una agenda (id:" + solrPendiente.getIdElem()
							+ ")  <br /> ");
					solrPendienteResultado = indexarPendienteAgenda(solrIndexer, solrPendiente.getIdElem(), info, null);

				} else if (solrPendiente.getTipo().equals(EnumCategoria.GUSITE_NOTICIA.toString())) {

					info.append(" Vamos a enviar una indexacion de una noticia (id:" + solrPendiente.getIdElem()
							+ ")  <br /> ");
					solrPendienteResultado = indexarPendienteNoticia(solrIndexer, solrPendiente.getIdElem(), info,
							null);

				} else if (solrPendiente.getTipo().equals(EnumCategoria.GUSITE_CONTENIDO.toString())) {

					info.append(" Vamos a enviar una indexacion de un contenido (id:" + solrPendiente.getIdElem()
							+ ")  <br /> ");
					solrPendienteResultado = indexarPendienteContenido(solrIndexer, solrPendiente.getIdElem(), info,
							null);

				} else if (solrPendiente.getTipo().equals(EnumCategoria.GUSITE_MICROSITE.toString())) {
					if (solrPendiente.getIdArchivo() == null) {
						info.append(" Vamos a enviar una indexacion de un microsite (id:" + solrPendiente.getIdElem()
								+ ")  <br /> ");
						solrPendienteResultado = indexacionMicrosite(solrPendiente.getIdElem(), null, info);
					} else {
						info.append(" Vamos a enviar una indexacion de un archivo microsite (id:"
								+ solrPendiente.getIdElem() + ", idArchivo:" + solrPendiente.getIdArchivo()
								+ ")  <br /> ");
						solrPendienteResultado = indexarPendienteArchivo(solrIndexer, solrPendiente.getIdElem(),
								solrPendiente.getIdArchivo(), info, null);
					}
				} else if (solrPendiente.getTipo().equals(EnumCategoria.GUSITE_FAQ.toString())) {

					info.append(
							" Vamos a enviar una indexacion de un faq (id:" + solrPendiente.getIdElem() + ")  <br /> ");
					solrPendienteResultado = indexarPendienteFAQ(solrIndexer, solrPendiente.getIdElem(), info, null);

				} else if (solrPendiente.getTipo().equals(EnumCategoria.GUSITE_ENCUESTA.toString())) {

					info.append(" Vamos a enviar una indexacion de una encuesta (id:" + solrPendiente.getIdElem()
							+ ")  <br /> ");
					solrPendienteResultado = indexarPendienteEncuesta(solrIndexer, solrPendiente.getIdElem(), info,
							null);

				} else {
					log.error("Tipo de elemento no especificado correctamente");
					solrPendienteResultado = new SolrPendienteResultado(false,
							"Tipo de elemento no existente ni controlado.");
				}
			}
		} else {
			log.error("Error intentando desindexar, no ha sido capaz. Mensaje:" + solrPendienteResultado.getMensaje());
		}

		return solrPendienteResultado;
	}

	/**
	 * Indexa un archivo de un microsite.
	 *
	 * @param solrIndexer
	 * @param solrPendiente
	 * @param info
	 * @return
	 */
	private SolrPendienteResultado indexarPendienteArchivo(final SolrIndexer solrIndexer, final Long idMicrosite,
			final Long idArchivo, final StringBuffer info, final PathUOResult iPathUO) {
		SolrPendienteResultado resultado = null;

		final MicrositeDelegate micrositeDelegate = DelegateUtil.getMicrositeDelegate();
		final EnumCategoria tipo = EnumCategoria.GUSITE_ARCHIVO;
		try {
			resultado = micrositeDelegate.indexarSolrArchivo(solrIndexer, Long.valueOf(idMicrosite), tipo,
					Long.valueOf(idArchivo), iPathUO);
			if (!resultado.isCorrecto()) {
				log.error("Error indexando " + tipo + "(ID:" + idArchivo + "):" + resultado.toString());
				if (info != null) {
					info.append(" ** Parece que no se ha indexado docMicrosite(ID:" + idArchivo + "):"
							+ resultado.getMensajeCorto());
				}
			}

		} catch (final Exception exception) {
			log.error("Se ha producido un error en " + tipo + " con id " + idArchivo);
			if (info != null) {
				info.append(" **Parece que no se ha indexado docMicrosite(ID:" + idArchivo + "):"
						+ IndexacionUtil.getError(exception));
			}
			resultado = new SolrPendienteResultado(false, ExceptionUtils.getStackTrace(exception));
		}
		return resultado;
	}

	/**
	 * Indexa una encuesta.
	 *
	 * @param solrIndexer
	 * @param solrPendiente
	 * @param info
	 * @return
	 */
	private SolrPendienteResultado indexarPendienteEncuesta(final SolrIndexer solrIndexer, final Long idEncuesta,
			final StringBuffer info, final PathUOResult iPathUO) {

		SolrPendienteResultado resultado = null;
		final EnumCategoria tipo = EnumCategoria.GUSITE_ENCUESTA;

		final EncuestaDelegate encuestaDelegate = DelegateUtil.getEncuestaDelegate();
		try {
			resultado = encuestaDelegate.indexarSolr(solrIndexer, idEncuesta, tipo, iPathUO);
			if (!resultado.isCorrecto()) {
				log.error("Error indexando " + tipo + "(ID:" + idEncuesta + "):" + resultado.toString());
				if (info != null) {
					info.append(" ** Parece que no se ha indexado encuesta(ID:" + idEncuesta + "):"
							+ resultado.getMensajeCorto());
				}
			}

		} catch (final Exception e) {
			log.error("Se ha producido un error en " + tipo + " con id " + +idEncuesta);
			if (info != null) {
				info.append(" **Parece que no se ha indexado encuesta(ID:" + idEncuesta + "):"
						+ IndexacionUtil.getError(e));
			}
			resultado = new SolrPendienteResultado(false, ExceptionUtils.getStackTrace(e));
		}
		return resultado;
	}

	/***
	 * Indexa un FAQ.
	 *
	 * @param solrIndexer
	 * @param solrPendiente
	 * @param info
	 * @return
	 */
	private SolrPendienteResultado indexarPendienteFAQ(final SolrIndexer solrIndexer, final Long idElemento,
			final StringBuffer info, final PathUOResult iPathUO) {
		SolrPendienteResultado resultado = null;

		final FaqDelegate faqDelegate = DelegateUtil.getFaqDelegate();
		final EnumCategoria tipo = EnumCategoria.GUSITE_FAQ;
		try {
			resultado = faqDelegate.indexarSolr(solrIndexer, idElemento, tipo, iPathUO);
			if (!resultado.isCorrecto()) {
				log.error("Error indexando " + tipo + "(ID:" + idElemento + "):" + resultado.toString());
				if (info != null) {
					info.append(" ** Parece que no se ha indexado faq(ID:" + idElemento + "): "
							+ resultado.getMensajeCorto());
				}
			}
		} catch (final Exception e) {
			log.error("Se ha producido un error en " + tipo + " con id " + idElemento);
			if (info != null) {
				info.append(
						" **Parece que no se ha indexado faq(ID:" + idElemento + "): " + IndexacionUtil.getError(e));
			}
			resultado = new SolrPendienteResultado(false, ExceptionUtils.getStackTrace(e));
		}
		return resultado;
	}

	/***
	 * Indexa todo una agenda, incluido sus documentos.
	 *
	 * @param solrIndexer
	 * @param solrPendiente
	 * @return
	 */
	private SolrPendienteResultado indexarPendienteAgenda(final SolrIndexer solrIndexer, final Long idElemento,
			final StringBuffer info, final PathUOResult iPathUO) {

		SolrPendienteResultado resultado = null;
		final EnumCategoria tipo = EnumCategoria.GUSITE_AGENDA;

		// Paso 1. Indexamos la entidad.
		final AgendaDelegate agendaDelegate = DelegateUtil.getAgendaDelegate();
		Agenda agenda = null;
		try {
			agenda = agendaDelegate.obtenerAgenda(idElemento);
		} catch (final Exception exception) {
			return new SolrPendienteResultado(false, ExceptionUtils.getStackTrace(exception));
		}

		try {

			resultado = agendaDelegate.indexarSolr(solrIndexer, idElemento, tipo, iPathUO);
			if (!resultado.isCorrecto()) {
				log.error("Error indexando " + tipo + "(ID:" + idElemento + "):" + resultado.toString());
				if (info != null) {
					info.append(" ** Parece que no se ha indexado agenda(ID:" + idElemento + "): "
							+ resultado.getMensajeCorto());
				}
				return resultado;
			}
		} catch (final Exception e) {
			log.error("Se ha producido un error en " + tipo + " con id " + agenda.getId());
			if (info != null) {
				info.append(
						" **Parece que no se ha indexado agenda(ID:" + idElemento + "): " + IndexacionUtil.getError(e));
			}
			return new SolrPendienteResultado(false, ExceptionUtils.getStackTrace(e));
		}

		// Paso 2. Recorremos documento y los indexamos
		// En caso de que falle un documento, lo dejamos pasar por si da error al
		// indexar pero lo tenemos en cuenta en el mensaje de retorno
		String msgRetorno = "";
		if (agenda.getTraducciones() != null) {
			final Iterator<Entry<String, Traduccion>> itTradAgenda = agenda.getTraduccionMap().entrySet().iterator();

			while (itTradAgenda.hasNext()) {
				final Map.Entry<String, Traduccion> mapTrad = itTradAgenda.next();
				final TraduccionAgenda tradAgen = (TraduccionAgenda) mapTrad.getValue();
				final Archivo arc = tradAgen != null && tradAgen.getDocumento() != null ? tradAgen.getDocumento()
						: null;
				if (arc != null) {
					try {

						// Indexamos las AGENDAS con archivo
						resultado = agendaDelegate.indexarSolrArchivo(solrIndexer, agenda.getId(),
								EnumCategoria.GUSITE_AGENDA, tradAgen.getDocumento().getId(), iPathUO);

						if (!resultado.isCorrecto()) {
							log.debug("Error indexando documento (DOC:" + arc.getId() + "):" + resultado.toString());
							msgRetorno += "Error indexando documento " + arc.getId() + " (revise el log) <br />";
							info.append(" ** Parece que no se ha indexado docAgenda(ID:" + arc.getId() + "): "
									+ resultado.getMensajeCorto());
						}

					} catch (final Exception e) {
						log.error("Se ha producido un error en documento con id " + arc.getId());
						info.append(" **Parece que no se ha indexado docAgenda(ID:" + arc.getId() + "): "
								+ IndexacionUtil.getError(e));
						msgRetorno += "Error indexando documento de " + tipo + " con id:" + arc.getId()
								+ " (revise el log.) <br />";
					}
				}

			}
		}

		// Paso 3. Devolvemos resultado correcto con mensaje dependiendo si falla algun
		// documento
		return new SolrPendienteResultado(msgRetorno.isEmpty(), msgRetorno);
	}

	/***
	 * Indexa toda una noticia, incluido sus documentos.
	 *
	 * @param solrIndexer
	 * @param solrPendiente
	 * @return
	 */
	private SolrPendienteResultado indexarPendienteNoticia(final SolrIndexer solrIndexer, final Long idElemento,
			final StringBuffer info, final PathUOResult iPathUO) {

		SolrPendienteResultado resultado = new SolrPendienteResultado(true);
		final EnumCategoria tipo = EnumCategoria.GUSITE_NOTICIA;

		// Paso 1. Indexamos la noticia.
		final NoticiaDelegate noticiaDelegate = DelegateUtil.getNoticiasDelegate();
		Noticia noticia = null;
		try {
			noticia = noticiaDelegate.obtenerNoticia(idElemento);
		} catch (final Exception exception) {
			return new SolrPendienteResultado(false, ExceptionUtils.getStackTrace(exception));
		}

		try {
			resultado = noticiaDelegate.indexarSolr(solrIndexer, idElemento, tipo, iPathUO);
			if (!resultado.isCorrecto()) {
				log.error("Error indexando " + tipo + "(ID:" + idElemento + "):" + resultado.toString());
				info.append(" ** Parece que no se ha indexado noticia(ID:" + idElemento + "): "
						+ resultado.getMensajeCorto());
				return resultado;
			}
		} catch (final Exception e) {
			log.error("Se ha producido un error en " + tipo + " con id " + idElemento);
			if (info != null) {
				info.append(" **Parece que no se ha indexado noticia(ID:" + idElemento + "): "
						+ IndexacionUtil.getError(e));
			}
			return new SolrPendienteResultado(false, ExceptionUtils.getStackTrace(e));
		}

		// Paso 2. Recorremos documento y los indexamos
		// En caso de que falle un documento, lo dejamos pasar por si da error al
		// indexar pero lo tenemos en cuenta en el mensaje de retorno
		String msgRetorno = "";
		if (noticia.getTraducciones() != null) {
			final Iterator<Entry<String, Traduccion>> itTradNoticia = noticia.getTraduccionMap().entrySet().iterator();

			while (itTradNoticia.hasNext()) {
				final Map.Entry<String, Traduccion> mapTrad = itTradNoticia.next();
				final TraduccionNoticia tradNoticia = (TraduccionNoticia) mapTrad.getValue();
				final Archivo arc = tradNoticia != null && tradNoticia.getDocu() != null ? tradNoticia.getDocu() : null;
				if (arc != null) {
					try {

						// Indexamos las AGENDAS con archivo
						resultado = noticiaDelegate.indexarSolrArchivo(solrIndexer, noticia.getId(),
								EnumCategoria.GUSITE_NOTICIA, tradNoticia.getDocu().getId(), iPathUO);

						if (!resultado.isCorrecto()) {
							log.debug("Error indexando documento(DOC:" + arc.getId() + "):" + resultado.toString());
							msgRetorno += "Error indexando documento " + arc.getId() + " (revise el log) <br />";
							info.append(" ** Parece que no se ha indexado docNoticia(ID:" + arc.getId() + "): "
									+ resultado.getMensajeCorto());
						}

					} catch (final Exception e) {
						log.error("Se ha producido un error en documento agenda con id " + arc.getId());
						info.append(" **Parece que no se ha indexado docNoticia(ID:" + arc.getId() + "): "
								+ IndexacionUtil.getError(e));
						msgRetorno += "Error indexando un documento de " + tipo + " con id " + arc.getId()
								+ " (revise el log.) <br />";
					}
				}

			}
		}

		// Paso 3. Devolvemos resultado correcto con mensaje dependiendo si falla algun
		// documento
		return new SolrPendienteResultado(msgRetorno.isEmpty(), msgRetorno);
	}

	/***
	 * Indexa toda un contenido, incluido sus documentos.
	 *
	 * @param solrIndexer
	 * @param solrPendiente
	 * @return
	 */
	private SolrPendienteResultado indexarPendienteContenido(final SolrIndexer solrIndexer, final Long idElemento,
			final StringBuffer info, final PathUOResult iPathUO) {

		SolrPendienteResultado resultado = new SolrPendienteResultado(true);
		final EnumCategoria tipo = EnumCategoria.GUSITE_CONTENIDO;

		// Paso 1. Indexamos la noticia.
		final ContenidoDelegate contenidoDelegate = DelegateUtil.getContenidoDelegate();
		Contenido contenido = null;
		try {
			contenido = contenidoDelegate.obtenerContenido(idElemento);
			if (contenido == null) {
				return new SolrPendienteResultado(false, "Contenido con id " + idElemento + " es nulo");
			}
		} catch (final Exception exception) {
			log.error("Error obteniendo el contenido id:" + idElemento, exception);
			return new SolrPendienteResultado(false, ExceptionUtils.getStackTrace(exception));
		}

		try {

			resultado = contenidoDelegate.indexarSolr(solrIndexer, idElemento, tipo, iPathUO);
			if (!resultado.isCorrecto()) {
				log.error("Error indexando " + tipo + "(ID:" + idElemento + "):" + resultado.toString());
				info.append(" ** Parece que no se ha indexado contenido(ID:" + idElemento + "): "
						+ resultado.getMensajeCorto());
				return resultado;
			}

		} catch (final Exception e) {
			log.error("Se ha producido un error en " + tipo + " con id " + +idElemento, e);
			if (info != null) {
				info.append(" **Parece que no se ha indexado contenido(ID:" + idElemento + "): "
						+ IndexacionUtil.getError(e));
			}
			return new SolrPendienteResultado(false, ExceptionUtils.getStackTrace(e));
		}

		// Paso 2. Recorremos documento y los indexamos
		// En caso de que falle un documento, lo dejamos pasar por si da error al
		// indexar pero lo tenemos en cuenta en el mensaje de retorno
		String msgRetorno = "";

		// Obtenemos los DOCUMENTOS de cada CONTENIDO
		try {
			final List<Archivo> listaDocu = contenidoDelegate
					.listarDocumentos(contenido.getMicrosite().getId().toString(), contenido.getId().toString());
			if (listaDocu != null) {
				for (final Archivo docu : listaDocu) {
					if (docu != null && docu.getId() != null) {
						try {

							// Indexamos las CONTENIDOS con archivo
							resultado = contenidoDelegate.indexarSolrArchivo(solrIndexer, contenido.getId(),
									EnumCategoria.GUSITE_CONTENIDO, docu.getId(), iPathUO);

							if (!resultado.isCorrecto()) {
								log.debug(
										"Error indexando documento(DOC:" + docu.getId() + "):" + resultado.toString());
								msgRetorno += "Error indexando documento " + docu.getId() + " (revise el log) <br />";
								info.append(" ** Parece que no se ha indexado docContenido(ID:" + docu.getId() + "): "
										+ resultado.getMensajeCorto());
							}

						} catch (final Exception e) {
							log.error("Se ha producido un error en documento contenido con id " + docu.getId(), e);
							info.append(" **Parece que no se ha indexado docContenido(ID:" + docu.getId() + "): "
									+ IndexacionUtil.getError(e));
							msgRetorno += "Error indexando documento " + docu.getId() + " (revise el log) <br />";
						}

					}

				}
			}
		} catch (final Exception exception) {
			log.error("Se ha producido un error obteniendo la lista de archivos del contenido " + contenido.getId(),
					exception);
			info.append("Se ha producido un error obteniendo la lista de archivos del contenido " + contenido.getId()
					+ "  <br /> ");
			msgRetorno += "Error indexando documentos " + contenido.getId() + " (revise el log) <br />";
		}

		// Paso 3. Devolvemos resultado correcto con mensaje dependiendo si falla algun
		// documento
		/////// Si el documento está vacío, es que todo ha ido correcto.
		if (!msgRetorno.isEmpty()) {
			log.error("Error anexando documentos del contenido id:" + idElemento + " Mensaje:" + msgRetorno);
		}
		return new SolrPendienteResultado(msgRetorno.isEmpty(), msgRetorno);

	}

}
