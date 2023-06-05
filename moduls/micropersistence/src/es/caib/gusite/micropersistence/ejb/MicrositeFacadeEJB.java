package es.caib.gusite.micropersistence.ejb;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.ejb.CreateException;
import javax.ejb.EJBException;

import org.apache.commons.io.FilenameUtils;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import es.caib.gusite.micromodel.Actividadagenda;
import es.caib.gusite.micromodel.Agenda;
import es.caib.gusite.micromodel.Archivo;
import es.caib.gusite.micromodel.ArchivoLite;
import es.caib.gusite.micromodel.Auditoria;
import es.caib.gusite.micromodel.Componente;
import es.caib.gusite.micromodel.Contacto;
import es.caib.gusite.micromodel.Contenido;
import es.caib.gusite.micromodel.Encuesta;
import es.caib.gusite.micromodel.Faq;
import es.caib.gusite.micromodel.Frqssi;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.IdiomaMicrosite;
import es.caib.gusite.micromodel.Menu;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.MicrositeCompleto;
import es.caib.gusite.micromodel.Noticia;
import es.caib.gusite.micromodel.SolrPendienteResultado;
import es.caib.gusite.micromodel.Temafaq;
import es.caib.gusite.micromodel.Tipo;
import es.caib.gusite.micromodel.TraduccionMicrosite;
import es.caib.gusite.micromodel.Usuario;
import es.caib.gusite.micromodel.UsuarioPropietarioMicrosite;
import es.caib.gusite.micromodel.UsuarioPropietarioRespuesta;
import es.caib.gusite.micropersistence.delegate.ActividadDelegate;
import es.caib.gusite.micropersistence.delegate.AgendaDelegate;
import es.caib.gusite.micropersistence.delegate.ArchivoDelegate;
import es.caib.gusite.micropersistence.delegate.ComponenteDelegate;
import es.caib.gusite.micropersistence.delegate.ContactoDelegate;
import es.caib.gusite.micropersistence.delegate.ContenidoDelegate;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.EncuestaDelegate;
import es.caib.gusite.micropersistence.delegate.FaqDelegate;
import es.caib.gusite.micropersistence.delegate.FrqssiDelegate;
import es.caib.gusite.micropersistence.delegate.MenuDelegate;
import es.caib.gusite.micropersistence.delegate.NoticiaDelegate;
import es.caib.gusite.micropersistence.delegate.SolrPendienteDelegate;
import es.caib.gusite.micropersistence.delegate.TemaDelegate;
import es.caib.gusite.micropersistence.delegate.TipoDelegate;
import es.caib.gusite.micropersistence.delegate.UsuarioDelegate;
import es.caib.gusite.micropersistence.util.ArchivoUtil;
import es.caib.gusite.micropersistence.util.IndexacionUtil;
import es.caib.gusite.micropersistence.util.PathUOResult;
import es.caib.solr.api.SolrIndexer;
import es.caib.solr.api.model.IndexFile;
import es.caib.solr.api.model.MultilangLiteral;
import es.caib.solr.api.model.types.EnumAplicacionId;
import es.caib.solr.api.model.types.EnumCategoria;
import es.caib.solr.api.model.types.EnumIdiomas;

/**
 * SessionBean para consultar Microsite.
 *
 * @ejb.bean name="sac/micropersistence/MicrositeFacade"
 *           jndi-name="es.caib.gusite.micropersistence.MicrositeFacade"
 *           type="Stateless" view-type="remote" transaction-type="Container"
 *
 * @ejb.transaction type="Required"
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public abstract class MicrositeFacadeEJB extends HibernateEJB {

	private static final long serialVersionUID = -2076446869522196666L;

	/**
	 * @ejb.create-method
	 * @ejb.permission unchecked="true"
	 */
	@Override
	public void ejbCreate() throws CreateException {
		super.ejbCreate();
	}

	/**
	 * Inicializo los parámetros de la consulta de Microsite.... No está bien hecho
	 * debería ser Statefull
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public void init() {
		super.tampagina = 10;
		super.pagina = 0;
		super.select = "";
		super.from = " from Microsite site join site.traducciones trad ";
		super.where = " where trad.id.codigoIdioma = '" + Idioma.getIdiomaPorDefecto() + "'";
		super.whereini = " ";
		super.orderby = "";

		super.camposfiltro = new String[] { "trad.titulo" };
		super.cursor = 0;
		super.nreg = 0;
		super.npags = 0;
	}

	/**
	 * Crea o actualiza un Microsite
	 *
	 * @ejb.interface-method
	 * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public Long grabarMicrosite(final Microsite site) {

		final Session session = this.getSession();

		try {

			final Transaction tx = session.beginTransaction();
			final Map<String, TraduccionMicrosite> listaTraducciones = new HashMap<String, TraduccionMicrosite>();
			final Set<IdiomaMicrosite> idiomas = new HashSet<IdiomaMicrosite>();

			Microsite siteOriginal = null;

			final ArchivoDelegate archivoDelegate = DelegateUtil.getArchivoDelegate();
			final List<Archivo> archivosPorBorrar = new ArrayList<Archivo>();
			Archivo estiloCSS = null;
			Archivo imagenPrincipal = null;
			Archivo imagenCampanya = null;

			final boolean nuevo = (site.getId() == null) ? true : false;

			if (!nuevo)
				siteOriginal = DelegateUtil.getMicrositeDelegate().obtenerMicrosite(site.getId());

			if (nuevo) {

				final Iterator<TraduccionMicrosite> it = site.getTraducciones().values().iterator();
				while (it.hasNext()) {
					final TraduccionMicrosite trd = it.next();
					listaTraducciones.put(trd.getId().getCodigoIdioma(), trd);
				}
				site.setTraducciones(null);

				final Iterator<IdiomaMicrosite> iter = site.getIdiomas().iterator();
				while (iter.hasNext()) {
					final IdiomaMicrosite idi = iter.next();
					idiomas.add(idi);
				}
				site.setIdiomas((Set<IdiomaMicrosite>) null);

				site.setClaveunica(this.obtenerClaveUnica(site));

				// Archivos nuevos: toca guardar referencia y poner a null antes de guardado ya
				// que
				// es una entidad aún sin guardar. Los crearemos tras el guardado del microsite.
				if (site.getEstiloCSS() != null) {
					estiloCSS = site.getEstiloCSS();
					site.setEstiloCSS(null);
				}

				if (site.getImagenPrincipal() != null) {
					imagenPrincipal = site.getImagenPrincipal();
					site.setImagenPrincipal(null);
				}

				if (site.getImagenCampanya() != null) {
					imagenCampanya = site.getImagenCampanya();
					site.setImagenCampanya(null);
				}

			} else {

				if (site.getEstiloCSS() != null) {

					if (site.getEstiloCSS().getId() != null) {
						if (site.getEstiloCSS().getDatos() != null)
							archivoDelegate.grabarArchivo(site.getEstiloCSS());
					} else {
						archivoDelegate.insertarArchivo(site.getEstiloCSS());
					}

				} else {

					// Archivo a null pero anterior no lo era: solicitan borrado
					if (siteOriginal.getEstiloCSS() != null) {
						archivosPorBorrar.add(siteOriginal.getEstiloCSS());
					}

				}

				if (site.getImagenPrincipal() != null) {

					if (site.getImagenPrincipal().getId() != null) {
						if (site.getImagenPrincipal().getDatos() != null)
							archivoDelegate.grabarArchivo(site.getImagenPrincipal());
					} else {
						archivoDelegate.insertarArchivo(site.getImagenPrincipal());
					}

				} else {

					// Archivo a null pero anterior no lo era: solicitan borrado
					if (siteOriginal.getImagenPrincipal() != null) {
						archivosPorBorrar.add(siteOriginal.getImagenPrincipal());
					}

				}

				if (site.getImagenCampanya() != null) {

					if (site.getImagenCampanya().getId() != null) {
						if (site.getImagenCampanya().getDatos() != null)
							archivoDelegate.grabarArchivo(site.getImagenCampanya());
					} else {
						archivoDelegate.insertarArchivo(site.getImagenCampanya());
					}

				} else {

					// Archivo a null pero anterior no lo era: solicitan borrado
					if (siteOriginal.getImagenCampanya() != null) {
						archivosPorBorrar.add(siteOriginal.getImagenCampanya());
					}

				}

			}

			if (site.getUri() == null || site.getUri().equals("")) {
				site.setUri(site.getClaveunica());
			}

			session.saveOrUpdate(site);
			session.flush();

			if (nuevo) {

				// Traducciones e idiomas.
				for (final TraduccionMicrosite trad : listaTraducciones.values()) {
					trad.getId().setCodigoMicrosite(site.getId());
					session.saveOrUpdate(trad);
				}
				session.flush();
				site.setTraducciones(listaTraducciones);

				final Iterator<IdiomaMicrosite> iterIdi = idiomas.iterator();
				while (iterIdi.hasNext()) {
					final IdiomaMicrosite idi = iterIdi.next();
					idi.getId().setCodigoMicrosite(site.getId());
					session.saveOrUpdate(idi);
				}
				session.flush();
				site.setIdiomas(idiomas);

				// Ahora se asocian todos los usuarios admin
				final UsuarioDelegate uDel = DelegateUtil.getUsuarioDelegate();
				List<?> listau = uDel.listarUsuariosPerfil("gusadmin");
				Iterator<?> iter = listau.iterator();
				while (iter.hasNext()) {
					final Usuario user = (Usuario) iter.next();
					final UsuarioPropietarioMicrosite upm = new UsuarioPropietarioMicrosite(site.getId(), user.getId());
					session.save(upm);
				}

				// Ahora se asocian todos los usuarios system
				listau = uDel.listarUsuariosPerfil("gussystem");
				iter = listau.iterator();
				while (iter.hasNext()) {
					final Usuario user = (Usuario) iter.next();
					final UsuarioPropietarioMicrosite upm = new UsuarioPropietarioMicrosite(site.getId(), user.getId());
					session.save(upm);
				}

				// Archivos
				if (estiloCSS != null) {
					archivoDelegate.insertarArchivo(estiloCSS);
					site.setEstiloCSS(estiloCSS);
				}

				if (imagenPrincipal != null) {
					archivoDelegate.insertarArchivo(imagenPrincipal);
					site.setImagenPrincipal(imagenPrincipal);
				}

				if (imagenCampanya != null) {
					archivoDelegate.insertarArchivo(imagenCampanya);
					site.setImagenCampanya(imagenCampanya);
				}

				if (estiloCSS != null || imagenPrincipal != null || imagenCampanya != null) {
					session.saveOrUpdate(site);
				}

			}

			// Borramos archivos FKs del Microsite que han solicitado que se borren.
			if (archivosPorBorrar.size() > 0)
				archivoDelegate.borrarArchivos(archivosPorBorrar);

			session.flush();
			tx.commit();
			this.close(session);

			final int op = (nuevo) ? Auditoria.CREAR : Auditoria.MODIFICAR;
			this.grabarAuditoria(site, op);

			final SolrPendienteDelegate pendienteDel = DelegateUtil.getSolrPendienteDelegate();
			pendienteDel.grabarSolrPendiente(EnumCategoria.GUSITE_MICROSITE.toString(), site.getId(), null,
					IndexacionUtil.REINDEXAR);

			return site.getId();

		} catch (final HibernateException he) {

			throw new EJBException(he);

		} catch (final DelegateException e) {

			throw new EJBException(e);

		} finally {

			this.close(session);

		}

	}

	/**
	 * Crea o actualiza UsuarioPropietarioMicrosite
	 *
	 * @ejb.interface-method
	 * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public Long grabarUsuarioPropietarioMicrosite(final UsuarioPropietarioMicrosite upm) {

		final Session session = this.getSession();
		try {
			session.save(upm);
			session.flush();
			return upm.getPk().getIdmicrosite();

		} catch (final HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * borra un UsuarioPropietarioMicrosite
	 *
	 * @ejb.interface-method
	 * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public void borrarUsuarioPropietarioMicrosite(final UsuarioPropietarioMicrosite upm) {

		final Session session = this.getSession();
		try {
			final Criteria criteri = session.createCriteria(UsuarioPropietarioMicrosite.class);
			criteri.add(Restrictions.eq("pk.idmicrosite", upm.getPk().getIdmicrosite()));
			criteri.add(Restrictions.eq("pk.idusuario", upm.getPk().getIdusuario()));
			final UsuarioPropietarioMicrosite upm2 = (UsuarioPropietarioMicrosite) criteri.uniqueResult();

			session.delete(upm2);
			session.flush();

		} catch (final HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Obtiene un Microsite
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Microsite obtenerMicrosite(final Long id) {

		final Session session = this.getSession();
		try {
			final Microsite site = (Microsite) session.get(Microsite.class, id);
			return site;

		} catch (final ObjectNotFoundException oNe) {
			log.error(oNe.getMessage());
			return new Microsite();
		} catch (final HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Obtiene un Microsite a partir de su clave de identificación.
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Microsite obtenerMicrositebyKey(final String key) {

		final Session session = this.getSession();
		try {
			final Criteria criteri = session.createCriteria(Microsite.class);
			criteri.add(Restrictions.eq("claveunica", key));
			final Microsite site = (Microsite) criteri.uniqueResult();
			return site;

		} catch (final HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Obtiene un Microsite a partir de su URI.
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Microsite obtenerMicrositebyUri(final String uri) {

		final Session session = this.getSession();
		try {
			final Criteria criteri = session.createCriteria(Microsite.class);
			criteri.add(Restrictions.eq("uri", uri));
			final Microsite site = (Microsite) criteri.uniqueResult();
			return site;

		} catch (final HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Obtiene un Microsite para la exportación
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public MicrositeCompleto obtenerMicrositeCompleto(final Long id) {

		final Session session = this.getSession();
		try {
			final MicrositeCompleto site = (MicrositeCompleto) session.get(MicrositeCompleto.class, id);
			if (site.getTema() != null) {
				Hibernate.initialize(site.getTema().getArchivoTemaFronts());
			}
			return site;

		} catch (final HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Obtiene un Microsite para la exportación
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public MicrositeCompleto obtenerMicrositeCompletobyKey(final String key) {

		final Session session = this.getSession();
		try {
			final String hql = "select mic" + " from MicrositeCompleto mic" + " where mic.claveunica = '" + key + "'";
			final Query query = session.createQuery(hql);
			// List list = query.list();
			// Criteria criteri =
			// session.createCriteria(MicrositeCompleto.class);
			// criteri.add(Expression.eq("claveunica", "'" + key + "'"));
			// return (MicrositeCompleto) criteri.uniqueResult();
			// return (list.size() > 0) ? (MicrositeCompleto) list.get(0) :
			// null;
			return (MicrositeCompleto) query.uniqueResult();

		} catch (final HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Crea un Microsite Completo, durante importación
	 *
	 * @ejb.interface-method
	 * @ejb.permission role-name="${role.system},${role.admin}"
	 */
	public Long grabarMicrositeCompleto(final MicrositeCompleto site) {

		final Session session = this.getSession();

		try {

			final Transaction tx = session.beginTransaction();
			final Map<String, TraduccionMicrosite> listaTraducciones = new HashMap<String, TraduccionMicrosite>();
			final Set<IdiomaMicrosite> idiomas = new HashSet<IdiomaMicrosite>();

			Microsite siteOriginal = null;

			final ArchivoDelegate archivoDelegate = DelegateUtil.getArchivoDelegate();
			final List<Archivo> archivosPorBorrar = new ArrayList<Archivo>();
			Archivo estiloCSS = null;
			Archivo imagenPrincipal = null;
			Archivo imagenCampanya = null;

			final boolean nuevo = (site.getId() == null) ? true : false;

			if (!nuevo)
				siteOriginal = DelegateUtil.getMicrositeDelegate().obtenerMicrosite(site.getId());

			if (nuevo) {

				final Iterator<TraduccionMicrosite> it = site.getTraducciones().values().iterator();
				while (it.hasNext()) {
					final TraduccionMicrosite trd = it.next();
					listaTraducciones.put(trd.getId().getCodigoIdioma(), trd);
				}
				site.setTraducciones(null);

				final Iterator<IdiomaMicrosite> iter = site.getIdiomas().iterator();
				while (iter.hasNext()) {
					final IdiomaMicrosite idi = iter.next();
					idiomas.add(idi);
				}
				site.setIdiomas((Set<IdiomaMicrosite>) null);

				site.setClaveunica(this.obtenerClaveUnica(site));
				if (site.getUri() == null || site.getUri().equals("")) {
					site.setUri(site.getClaveunica());
				}

				// Archivos nuevos: toca guardar referencia y poner a null antes de guardado ya
				// que
				// es una entidad aún sin guardar. Los crearemos tras el guardado del microsite.
				if (site.getEstiloCSS() != null) {
					estiloCSS = site.getEstiloCSS();
					site.setEstiloCSS(null);
				}

				if (site.getImagenPrincipal() != null) {
					imagenPrincipal = site.getImagenPrincipal();
					site.setImagenPrincipal(null);
				}

				if (site.getImagenCampanya() != null) {
					imagenCampanya = site.getImagenCampanya();
					site.setImagenCampanya(null);
				}

			} else {

				if (site.getEstiloCSS() != null) {

					if (site.getEstiloCSS().getId() != null) {
						if (site.getEstiloCSS().getDatos() != null)
							archivoDelegate.grabarArchivo(site.getEstiloCSS());
					} else {
						archivoDelegate.insertarArchivo(site.getEstiloCSS());
					}

				} else {

					// Archivo a null pero anterior no lo era: solicitan borrado
					if (siteOriginal.getEstiloCSS() != null) {
						archivosPorBorrar.add(siteOriginal.getEstiloCSS());
					}

				}

				if (site.getImagenPrincipal() != null) {

					if (site.getImagenPrincipal().getId() != null) {
						if (site.getImagenPrincipal().getDatos() != null)
							archivoDelegate.grabarArchivo(site.getImagenPrincipal());
					} else {
						archivoDelegate.insertarArchivo(site.getImagenPrincipal());
					}

				} else {

					// Archivo a null pero anterior no lo era: solicitan borrado
					if (siteOriginal.getImagenPrincipal() != null) {
						archivosPorBorrar.add(siteOriginal.getImagenPrincipal());
					}

				}

				if (site.getImagenCampanya() != null) {

					if (site.getImagenCampanya().getId() != null) {
						if (site.getImagenCampanya().getDatos() != null)
							archivoDelegate.grabarArchivo(site.getImagenCampanya());
					} else {
						archivoDelegate.insertarArchivo(site.getImagenCampanya());
					}

				} else {

					// Archivo a null pero anterior no lo era: solicitan borrado
					if (siteOriginal.getImagenCampanya() != null) {
						archivosPorBorrar.add(siteOriginal.getImagenCampanya());
					}

				}

			}

			session.saveOrUpdate(site);
			session.flush();

			if (nuevo) {

				for (final TraduccionMicrosite trad : listaTraducciones.values()) {
					trad.getId().setCodigoMicrosite(site.getId());
					session.saveOrUpdate(trad);
				}
				session.flush();
				site.setTraducciones(listaTraducciones);

				final Iterator<IdiomaMicrosite> iterIdi = idiomas.iterator();
				while (iterIdi.hasNext()) {
					final IdiomaMicrosite idi = iterIdi.next();
					idi.getId().setCodigoMicrosite(site.getId());
					session.saveOrUpdate(idi);
				}
				session.flush();
				site.setIdiomas(idiomas);

				final UsuarioDelegate usudel = DelegateUtil.getUsuarioDelegate();
				final Usuario usu = usudel.obtenerUsuariobyUsername(this.getUsuario(session).getUsername());
				final UsuarioPropietarioMicrosite upm = new UsuarioPropietarioMicrosite(site.getId(), usu.getId());
				session.save(upm);

				// Archivos
				if (estiloCSS != null) {
					archivoDelegate.insertarArchivo(estiloCSS);
					site.setEstiloCSS(estiloCSS);
				}

				if (imagenPrincipal != null) {
					archivoDelegate.insertarArchivo(imagenPrincipal);
					site.setImagenPrincipal(imagenPrincipal);
				}

				if (imagenCampanya != null) {
					archivoDelegate.insertarArchivo(imagenCampanya);
					site.setImagenCampanya(imagenCampanya);
				}

				if (estiloCSS != null || imagenPrincipal != null || imagenCampanya != null) {
					session.saveOrUpdate(site);
				}

			}

			// Borramos archivos FKs del Microsite que han solicitado que se borren.
			if (archivosPorBorrar.size() > 0)
				archivoDelegate.borrarArchivos(archivosPorBorrar);

			session.flush();
			tx.commit();
			this.close(session);

			final int op = (nuevo) ? Auditoria.CREAR : Auditoria.MODIFICAR;
			this.grabarAuditoria(site, op);

			return site.getId();

		} catch (final HibernateException he) {

			throw new EJBException(he);

		} catch (final DelegateException e) {

			throw new EJBException(e);

		} catch (final Exception e) {

			throw new EJBException(e);

		} finally {

			this.close(session);

		}

	}

	/**
	 * borra un Microsite Completo
	 *
	 * @ejb.interface-method
	 * @ejb.permission role-name="${role.system},${role.admin}"
	 */
	public void borrarMicrositeCompleto(final Long id) {

		final Session session = this.getSession();

		try {

			final Transaction tx = session.beginTransaction();

			// Primero: borrar los usuarios asociados
			final Criteria criteri = session.createCriteria(UsuarioPropietarioMicrosite.class);
			criteri.add(Restrictions.eq("pk.idmicrosite", id));

			final Iterator<?> iter = criteri.list().iterator();
			while (iter.hasNext()) {
				final UsuarioPropietarioMicrosite upm = (UsuarioPropietarioMicrosite) iter.next();
				session.delete(upm);
			}

			// Segundo: En el caso que el microsite tenga encuestas, borraremos
			// la relación de usuarios con las respuestas de la encuesta
			final List<?> listIdRespu = this.idRespDeEncDelMicrosite(id);
			if (!listIdRespu.isEmpty()) {
				final Criteria criteriUsuPropiResp = session.createCriteria(UsuarioPropietarioRespuesta.class);
				criteriUsuPropiResp.add(Restrictions.in("id.idrespuesta", listIdRespu));
				final Iterator<?> iterUsuPropiResp = criteriUsuPropiResp.list().iterator();
				while (iterUsuPropiResp.hasNext()) {
					final UsuarioPropietarioRespuesta upm = (UsuarioPropietarioRespuesta) iterUsuPropiResp.next();
					session.delete(upm);
				}
			}

			// Tercero: borrar el microsite completo
			final MicrositeCompleto site = (MicrositeCompleto) session.get(MicrositeCompleto.class, id);

			// Obtenemos archivos comunes del Microsite y los borramos.
			final ArchivoDelegate archivoDelegate = DelegateUtil.getArchivoDelegate();
			final Set<ArchivoLite> listaArchivosLite = site.getDocus();

			final List<Archivo> listaArchivos = new ArrayList<Archivo>();
			for (final ArchivoLite al : listaArchivosLite)
				listaArchivos.add(ArchivoUtil.archivoLite2Archivo(al));

			archivoDelegate.borrarArchivos(listaArchivos);

			final FaqDelegate faqDelegate = DelegateUtil.getFaqDelegate();
			for (final Iterator it = site.getFaqs().iterator(); it.hasNext();) {
				faqDelegate.borrarFaq(((Faq) it.next()).getId(), false);
			}

			final TemaDelegate temaDelegate = DelegateUtil.getTemafaqDelegate();
			for (final Object tema : temaDelegate.listarCombo(id)) {
				temaDelegate.borrarTema(((Temafaq) tema).getId(), false);
			}

			final AgendaDelegate agendaDelegate = DelegateUtil.getAgendaDelegate();
			for (final Iterator it = site.getAgendas().iterator(); it.hasNext();) {
				agendaDelegate.borrarAgenda(((Agenda) it.next()).getId(), false);
			}

			final ActividadDelegate actividadDelegate = DelegateUtil.getActividadagendaDelegate();
			for (final Iterator it = site.getActividades().iterator(); it.hasNext();) {
				actividadDelegate.borrarActividad(((Actividadagenda) it.next()).getId(), false);
			}

			final NoticiaDelegate noticiaDelegate = DelegateUtil.getNoticiasDelegate();
			for (final Iterator it = site.getNoticias().iterator(); it.hasNext();) {
				noticiaDelegate.borrarNoticia(((Noticia) it.next()).getId(), false);
			}

			final ComponenteDelegate componenteDelegate = DelegateUtil.getComponentesDelegate();
			for (final Iterator it = site.getComponentes().iterator(); it.hasNext();) {
				componenteDelegate.borrarComponente(((Componente) it.next()).getId(), false);
			}

			final TipoDelegate tipoDelegate = DelegateUtil.getTipoDelegate();
			for (final Iterator it = site.getTiponotis().iterator(); it.hasNext();) {
				tipoDelegate.borrarTipo(((Tipo) it.next()).getId(), false);
			}

			final FrqssiDelegate frqssiDelegate = DelegateUtil.getFrqssiDelegate();
			for (final Iterator it = site.getFrqssis().iterator(); it.hasNext();) {
				frqssiDelegate.borrarFrqssi(((Frqssi) it.next()).getId(), false);
			}

			final ContactoDelegate contactoDelegate = DelegateUtil.getContactoDelegate();
			for (final Iterator it = site.getFormularioscontacto().iterator(); it.hasNext();) {
				contactoDelegate.borrarContacto(((Contacto) it.next()).getId(), false);
			}

			final EncuestaDelegate encuestaDelegate = DelegateUtil.getEncuestaDelegate();
			for (final Iterator it = site.getEncuestas().iterator(); it.hasNext();) {
				encuestaDelegate.borrarEncuesta(((Encuesta) it.next()).getId(), false);
			}

			final ContenidoDelegate contenidoDelegate = DelegateUtil.getContenidoDelegate();
			for (final Object contenido : contenidoDelegate.listarAllContenidos(id.toString())) {
				contenidoDelegate.borrarContenido(((Contenido) contenido).getId(), false);
			}

			final MenuDelegate menuDelegate = DelegateUtil.getMenuDelegate();
			for (final Iterator it = site.getMenus().iterator(); it.hasNext();) {
				menuDelegate.borrarMenu(((Menu) it.next()).getId(), false);
			}

			session.createQuery("delete TraduccionMicrosite tra where tra.id.codigoMicrosite = " + id.toString())
					.executeUpdate();
			session.createQuery("delete UsuarioPropietarioMicrosite upm where upm.pk.idmicrosite = " + id.toString())
					.executeUpdate();
			session.createQuery("delete IdiomaMicrosite imic where imic.id.codigoMicrosite = " + id.toString())
					.executeUpdate();
			session.createQuery("delete PersonalizacionPlantilla perPla where perPla.microsite.id = " + id.toString())
					.executeUpdate();
			session.createQuery("delete Microsite mic where mic.id = " + id.toString()).executeUpdate();

			session.flush();

			// Borrado de archivos que son FKs (ha de ser posterior, debido al modelo de
			// datos).
			if (site.getEstiloCSS() != null)
				archivoDelegate.borrarArchivo(site.getEstiloCSS().getId(), false);

			if (site.getImagenPrincipal() != null)
				archivoDelegate.borrarArchivo(site.getImagenPrincipal().getId(), false);

			if (site.getImagenCampanya() != null)
				archivoDelegate.borrarArchivo(site.getImagenCampanya().getId(), false);

			ArchivoUtil.borrarDirMicrosite(site.getId());

			tx.commit();
			this.close(session);

			final SolrPendienteDelegate pendienteDel = DelegateUtil.getSolrPendienteDelegate();
			pendienteDel.grabarSolrPendiente(EnumCategoria.GUSITE_MICROSITE.toString(), site.getId(), null,
					IndexacionUtil.DESINDEXAR);

			/**
			 * Ojo, el site está eliminado así que hay que asegurarse de que no se intenta
			 * enlazar
			 */
			this.grabarAuditoria(null, site, Auditoria.ELIMINAR);

		} catch (final HibernateException he) {

			throw new EJBException(he);

		} catch (final DelegateException e) {

			throw new EJBException(e);

		} catch (final IOException e) {

			throw new EJBException(e);

		} finally {

			this.close(session);

		}

	}

	/**
	 * Lista todos los Ids de las respuestas de las encuestas de un microsite.
	 */
	private List<?> idRespDeEncDelMicrosite(final Long idMicrosite) {

		final Session session = this.getSession();
		try {
			final String hql = "SELECT RESP.id " + "FROM Encuesta ENC,  Pregunta PRE, Respuesta RESP "
					+ "WHERE RESP.idpregunta = PRE.id " + "AND PRE.idencuesta = ENC.id " + "AND ENC.idmicrosite = "
					+ idMicrosite.toString();

			final Query query = session.createQuery(hql);
			final List<?> queryList = query.list();
			return queryList;

		} catch (final HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Reemplaza un microsite basandose en la clave unica del microsite.
	 *
	 * @ejb.interface-method
	 * @ejb.permission role-name="${role.system},${role.admin}"
	 */
	public Long reemplazarMicrositeCompleto(final MicrositeCompleto site) {

		final Session session = this.getSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();

			final MicrositeCompleto oldsite = this.obtenerMicrositeCompletobyKey(site.getClaveunica());
			final ArrayList<Long> listausuariosold = new ArrayList<Long>();

			if (oldsite != null) {
				// Primero: recoger todos los usuarios asociados al antiguo
				// microsite y además borrar upm
				final String hql = "select usuMic" + " from UsuarioPropietarioMicrosite usuMic"
						+ " where usuMic.pk.idmicrosite = " + oldsite.getId().toString();
				final Query query = session.createQuery(hql);
				final Iterator<?> iter = query.list().iterator();
				while (iter.hasNext()) {
					final UsuarioPropietarioMicrosite upm = (UsuarioPropietarioMicrosite) iter.next();
					listausuariosold.add(upm.getPk().getIdusuario());
					session.delete(upm);
				}

				// Segundo: En el caso que el microsite tenga encuestas,
				// borraremos la relación de usuarios con las respuestas de la encuesta
				final List<?> listIdRespu = this.idRespDeEncDelMicrosite(oldsite.getId());
				if (!listIdRespu.isEmpty()) {
					final Criteria criteriUsuPropiResp = session.createCriteria(UsuarioPropietarioRespuesta.class);
					criteriUsuPropiResp.add(Restrictions.in("id.idrespuesta", listIdRespu));

					final Iterator<?> iterUsuPropiResp = criteriUsuPropiResp.list().iterator();
					while (iterUsuPropiResp.hasNext()) {
						final UsuarioPropietarioRespuesta upm = (UsuarioPropietarioRespuesta) iterUsuPropiResp.next();
						session.delete(upm);
					}
				}

				// Tercero: borrar, si procede, el microsite antiguo
				this.borrarMicrositeCompleto(oldsite.getId());
				session.flush();
			}
			// Tercero: grabar el nuevo microsite
			this.grabarMicrositeCompleto(site);
			session.flush();
			tx.commit();
			this.close(session);

			this.grabarAuditoria(site, Auditoria.MODIFICAR);

			return site.getId();

		} catch (final HibernateException he) {
			try {
				if (tx != null) {
					tx.rollback();
				}
			} catch (final HibernateException e) {
				throw new EJBException(e);
			}
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Lista todos los Microsites
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<Long> listarMicrosites() {

		final Session session = this.getSession();
		try {
			final Query query = session.createQuery(" select micro.id from Microsite micro ");
			return query.list();
		} catch (final HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Marcar microsite como indexado. En caso de no pasar ID, se da por hecho que
	 * se quiere marcar todos como NO indexados.
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public void marcarComoIndexado(final Long id, final int indexado, final Boolean todoCorrecto) {

		final Session session = this.getSession();
		try {
			int indexadoCorrecto;
			if (todoCorrecto == null) {
				indexadoCorrecto = Microsite.INDEXADO_INICIAL;
			} else if (todoCorrecto) {
				indexadoCorrecto = Microsite.INDEXADO_CORRECTAMENTE;
			} else {
				indexadoCorrecto = Microsite.INDEXADO_INCORRECTAMENTE;
			}

			final Query query;
			if (id == null) {
				query = session.createQuery("update Microsite set indexado = " + indexado
						+ " , indexadoCorrectamente = " + indexadoCorrecto);
			} else {
				query = session.createQuery("update Microsite set indexado = " + indexado
						+ " , indexadoCorrectamente = " + indexadoCorrecto + " where id = " + id);
			}
			query.executeUpdate();
		} catch (final HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Resumen de los microsites indexados.
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public String getResumenMicrositesIndexados(final boolean conResumen) {
		final Session session = this.getSession();
		try {
			final Query query = session.createQuery("select id, indexado, indexadoCorrectamente, uri from Microsite");
			final List<Object[]> objetos = query.list();
			final StringBuffer respuesta = new StringBuffer();
			int indexados = 0;
			int indexadosNO = 0;
			final StringBuffer micrositeErrores = new StringBuffer();
			int indexadosErroneamente = 0;
			for (final Object[] objeto : objetos) {
				Long indexado = null;
				if (objeto[1] != null) {
					indexado = Long.valueOf(objeto[1].toString());
				}
				if (indexado == null || indexado == Microsite.NO_INDEXADO) {
					indexadosNO++;
				} else {
					indexados++;
					Long indexadoCorrectamente = null;
					if (objeto[2] != null) {
						indexadoCorrectamente = Long.valueOf(objeto[2].toString());
					}
					if (indexadoCorrectamente != null && indexadoCorrectamente == Microsite.INDEXADO_INCORRECTAMENTE) {

						if (conResumen) {
							indexadosErroneamente++;
						} else {
							final String id = objeto[0].toString();
							final String uri = objeto[3].toString();
							if (micrositeErrores.length() == 0) {
								micrositeErrores.append("<ul>");
							}
							micrositeErrores.append("<li><a target='_blank' href='index.do?idsite=" + id + "'>" + id
									+ ":" + uri + "</a></li>");
						}
					}
				}
			}

			final int total = indexados + indexadosNO;
			respuesta.append("Hi ha un total de " + total + " microsites ( " + indexadosNO + " no indexats , "
					+ indexados + " indexats ). ");
			if (conResumen) {
				respuesta.append("\n Amb errors un total de " + indexadosErroneamente + " microsites");
			} else {
				if (micrositeErrores.length() > 0) {
					respuesta.append("\n Amb errors: " + micrositeErrores + " </ul>");
				}
			}
			return respuesta.toString();
		} catch (final HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Obtiene el total de microsites indexado, o no, y en que estado.
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Long getCuantosMicrosites(final Integer indexado, final Integer estadoIndexacion)
			throws java.rmi.RemoteException, DelegateException {
		final Session session = this.getSession();
		try {
			final Query query = session.createQuery(" select count(micro) from Microsite micro where micro.indexado = "
					+ indexado + " and micro.indexadoCorrectamente = " + estadoIndexacion);

			return (Long) query.uniqueResult();
		} catch (final HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Lista todos los Microsites sin indexar.
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<Long> listarMicrositesSinIndexar(final int cuantos) {

		final Session session = this.getSession();
		try {
			final Query query = session.createQuery(
					" select micro.id from Microsite micro where micro.indexado = " + Microsite.NO_INDEXADO);
			query.setMaxResults(cuantos);
			return query.list();
		} catch (final HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Listado ligero de todos los Microsites. Solo rellena el idioma por defecto
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<?> listarMicrositesThin() {

		final Session session = this.getSession();
		try {
			final String sql = "select mic.id, mic.unidadAdministrativa, trad.titulo "
					+ "from Microsite mic join mic.traducciones trad " + "where trad.id.codigoIdioma = '"
					+ Idioma.getIdiomaPorDefecto() + "' order by trad.titulo";

			final Query query = session.createQuery(sql);

			final ArrayList<Microsite> lista = new ArrayList<Microsite>();
			final Iterator<?> res = query.iterate();
			int i = 0;
			while (res.hasNext()) {
				final Object[] fila = (Object[]) res.next();
				final Microsite mic = new Microsite();
				mic.setId((Long) fila[0]);
				final Integer idua = (Integer) fila[1];
				mic.setUnidadAdministrativa(idua.intValue());
				final TraduccionMicrosite trad = new TraduccionMicrosite();
				trad.setTitulo((String) fila[2]);
				mic.setTraduccion(Idioma.getIdiomaPorDefecto(), trad);
				lista.add(i, mic);
				i++;
			}

			return lista;

		} catch (final HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Lista todos los Microsites a los que el usuario puede acceder
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<?> listarMicrositesFiltro(final Usuario usu, final Map<?, ?> param) {

		final Session session = this.getSession();
		try {
			final Criteria criteri = session.createCriteria(Microsite.class);
			this.populateCriteria(criteri, param);
			final List<?> microlista = criteri.list();

			final ArrayList<?> microlistaOrd = new ArrayList<Object>(microlista);
			final Comparator<Object> comp = new MicrosComparator();
			Collections.sort(microlistaOrd, comp);

			return microlistaOrd;

		} catch (final HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	private static class MicrosComparator implements Comparator<Object> {
		@Override
		public int compare(final Object element1, final Object element2) {

			final String nom1 = (((TraduccionMicrosite) ((Microsite) element1).getTraduccion("ca")).getTitulo() != null)
					? ((TraduccionMicrosite) ((Microsite) element1).getTraduccion("ca")).getTitulo()
					: "";
			final String nom2 = (((TraduccionMicrosite) ((Microsite) element2).getTraduccion("ca")).getTitulo() != null)
					? ((TraduccionMicrosite) ((Microsite) element2).getTraduccion("ca")).getTitulo()
					: "";

			return nom1.toLowerCase().compareTo(nom2.toLowerCase());
		}
	}

	/**
	 * borra un Microsite
	 *
	 * @ejb.interface-method
	 * @ejb.permission role-name="${role.system},${role.admin}"
	 */
	public void borrarMicrosite(final Long id) {

		final Session session = this.getSession();
		try {
			final Transaction tx = session.beginTransaction();
			final Microsite site = (Microsite) session.get(Microsite.class, id);
			session.delete(site);
			session.flush();
			tx.commit();
			this.close(session);

			/**
			 * Ojo, el site está eliminado así que hay que asegurarse de que no se intenta
			 * enlazar
			 */
			this.grabarAuditoria(null, site, Auditoria.ELIMINAR);

			final SolrPendienteDelegate pendienteDel = DelegateUtil.getSolrPendienteDelegate();
			pendienteDel.grabarSolrPendiente(EnumCategoria.GUSITE_MICROSITE.toString(), site.getId(), null,
					IndexacionUtil.DESINDEXAR);

		} catch (final Exception he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Anyade un id de contenido al listado de los ultimos modificados en el
	 * microsite
	 *
	 * @ejb.interface-method
	 * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public void grabarUltimoIdcontenido(final Microsite site, final Long idcontenido) {

		final Session session = this.getSession();
		try {
			site.setServiciosSeleccionados(
					this.manejaListadoUltimosIds(site.getServiciosSeleccionados(), "" + idcontenido.longValue()));
			final Transaction tx = session.beginTransaction();
			session.saveOrUpdate(site);
			session.flush();
			tx.commit();

		} catch (final HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 *
	 * @ejb.interface-method
	 * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public List<?> listarMicrositesbyUser(final Usuario usuario) {

		final Session session = this.getSession();
		try {
			String hql = "select mic.id, mic.unidadAdministrativa, trad.titulo ";
			hql += " from UsuarioPropietarioMicrosite upm, Microsite mic join mic.traducciones trad ";
			hql += " where upm.pk.idusuario=" + usuario.getId().longValue()
					+ " and mic.id = upm.pk.idmicrosite and trad.id.codigoIdioma = '" + Idioma.getIdiomaPorDefecto()
					+ "' order by trad.titulo";

			final Query query = session.createQuery(hql);

			final ArrayList<Microsite> lista = new ArrayList<Microsite>();
			final Iterator<?> res = query.iterate();
			int i = 0;
			while (res.hasNext()) {
				final Object[] fila = (Object[]) res.next();
				final Microsite mic = new Microsite();
				mic.setId((Long) fila[0]);
				final Integer idua = (Integer) fila[1];
				mic.setUnidadAdministrativa(idua.intValue());
				final TraduccionMicrosite trad = new TraduccionMicrosite();
				trad.setTitulo((String) fila[2]);
				mic.setTraduccion(Idioma.getIdiomaPorDefecto(), trad);
				lista.add(i, mic);
				i++;
			}

			return lista;

		} catch (final HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 *
	 * @ejb.interface-method
	 * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public List<?> llistarMicrodeluser(final String id) {

		final Session session = this.getSession();
		try {
			String hql = "select mic.id, mic.unidadAdministrativa, trad.titulo ";
			hql += " from UsuarioPropietarioMicrosite upm, Microsite mic join mic.traducciones trad ";
			hql += " where upm.pk.idusuario = " + id + " and  mic.id = upm.pk.idmicrosite and trad.id.codigoIdioma = '"
					+ Idioma.getIdiomaPorDefecto() + "' order by trad.titulo";

			final Query query = session.createQuery(hql);

			final ArrayList<Microsite> lista = new ArrayList<Microsite>();
			final Iterator<?> res = query.iterate();
			int i = 0;
			while (res.hasNext()) {
				final Object[] fila = (Object[]) res.next();
				final Microsite mic = new Microsite();
				mic.setId((Long) fila[0]);
				final Integer idua = (Integer) fila[1];
				mic.setUnidadAdministrativa(idua.intValue());
				final TraduccionMicrosite trad = new TraduccionMicrosite();
				trad.setTitulo((String) fila[2]);
				mic.setTraduccion(Idioma.getIdiomaPorDefecto(), trad);
				lista.add(i, mic);
				i++;
			}

			return lista;

		} catch (final HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Obtiene un Microsite para la exportación
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<Long> obtenerIdsArchivosMicrosite(final Long id) {

		final Session session = this.getSession();

		final List<Long> lista = new ArrayList<Long>();

		try {

			// Archivos comunes
			final Query query = session.createQuery("select a.id from Archivo a where a.idmicrosite = ?");
			query.setLong(0, id);
			final List<Long> listaQuery = query.list();

			for (final Long l : listaQuery) {
				// Si no es nulo ni repetido, lo añadimos a la lista.
				if (l != null && lista.indexOf(l) == -1)
					lista.add(l);
			}

			return lista;

		} catch (final HibernateException he) {

			throw new EJBException(he);

		} finally {

			this.close(session);

		}

	}

	/**
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<Usuario> listarUsernamesbyMicrosite(final Long idmicrosite) {

		final Session session = this.getSession();
		try {
			String hql = "select usuario";
			hql += " from UsuarioPropietarioMicrosite upm, Usuario usuario";
			hql += " where upm.pk.idmicrosite = ";
			hql += idmicrosite.longValue();
			hql += " and  usuario.id = upm.pk.idusuario order by usuario.username";

			final Query query = session.createQuery(hql);
			return query.list();

		} catch (final HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	private void populateCriteria(final Criteria criteri, final Map<?, ?> parametros) {

		parametros.remove("id");
		for (final Object name : parametros.keySet()) {
			final String key = (String) name;
			final Object value = parametros.get(key);
			if (value != null) {
				if (value instanceof String) {
					final String sValue = (String) value;
					if (sValue.length() > 0) {
						criteri.add(Restrictions.ilike(key, value));
					}
				} else {
					criteri.add(Restrictions.eq(key, value));
				}
			}
		}
	}

	/**
	 * Metodo privado que se le pasa un string con ids separados por ;, y le añade
	 * en la primera posicion el nuevo id
	 *
	 * @param oldlistado
	 * @param newIdcontenido
	 * @return
	 */
	private String manejaListadoUltimosIds(final String oldlistado, final String newIdcontenido) {

		final int _numMax = 5;
		final String _nulo = "-1";
		final Hashtable<String, String> hshlistaids = new Hashtable<String, String>();

		// inicializo 5 entradas a nulo
		for (int i = 0; i < _numMax; i++) {
			hshlistaids.put("" + i, _nulo);
		}

		String servs = oldlistado;

		if (servs != null) {
			final StringTokenizer st = new StringTokenizer(servs, ";");
			final int n = st.countTokens();

			// recoger el hash
			for (int i = 0; i < n; i++) {
				hshlistaids.put("" + i, st.nextToken());
			}

			// comprobar que no se repita.
			int repe = -1; // obtendrá la posicion repetida
			for (int i = 0; i < n; i++) {
				if (hshlistaids.get("" + i).equals(newIdcontenido)) {
					repe = i;
				}
			}
			if (repe != -1) {
				// es repetido
				hshlistaids.put("" + repe, _nulo);
				for (int i = repe; i < _numMax - 1; i++) {
					hshlistaids.put("" + i, hshlistaids.get("" + (i + 1)));
				}
			}

			// ahora, desplazar el hash
			for (int i = _numMax - 1; i >= 0; i--) {
				hshlistaids.put("" + (i + 1), hshlistaids.get("" + i));
			}
		}

		// anyadir el id que se ha pasado
		hshlistaids.put("0", newIdcontenido);

		// volcar el hash en el string final
		servs = "";
		for (int i = 0; i < _numMax; i++) {
			if (!hshlistaids.get("" + i).equals(_nulo)) {
				servs += hshlistaids.get("" + i) + ";";
			}
		}
		if (servs.length() > 0) {
			servs = servs.substring(0, servs.length() - 1);
		}

		return servs;
	}

	/**
	 * Metodo que genera una clave única.
	 *
	 * @param site
	 * @return
	 */
	private String obtenerClaveUnica(final Object site) {

		String retorno = "";

		final SimpleDateFormat fmt = new SimpleDateFormat("yyMMddHHmmss");
		retorno += "M" + fmt.format(new Date());
		retorno += site.hashCode();

		return retorno;
	}

	/**
	 * Obtiene un Microsite
	 *
	 */
	private Microsite obtenerMicrositeBySolr(final Long id) {

		final Session session = this.getSession();
		try {
			return (Microsite) session.get(Microsite.class, id);
		} catch (final Exception exception) {
			log.error(exception.getMessage());
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
	public SolrPendienteResultado indexarSolrArchivo(final SolrIndexer solrIndexer, final Long idElemento,
			final EnumCategoria categoria, final Long idArchivo, final PathUOResult iPathUO) {

		log.debug("MicrositeEJB.indexarSolrArchivo. idElemento:" + idElemento + " categoria:" + categoria
				+ " idArchivo:" + idArchivo);

		try {
			final ArchivoDelegate archi = DelegateUtil.getArchivoDelegate();

			// Paso 0. Obtenemos el contenido y comprobamos si se puede indexar.
			final Microsite micro = obtenerMicrositeBySolr(idElemento);
			if (micro == null) {
				return new SolrPendienteResultado(true, "Error obteniendo el microsite.");
			}

			if (!IndexacionUtil.isIndexable(micro)) {
				return new SolrPendienteResultado(true, "No se puede indexar");
			}

			final Archivo archivo = archi.obtenerArchivo(idArchivo);
			if (!IndexacionUtil.isIndexable(archivo)) {
				return new SolrPendienteResultado(true, "No se puede indexar");
			}

			final byte[] contenidoFichero = archi.obtenerContenidoFichero(archivo);

			if (contenidoFichero == null) {
				return new SolrPendienteResultado(false,
						"No se obtiene el contenido del archivo " + archivo.getNombre() + ".");
			}

			// Los archivos solo se indexa en un idioma, por lo que si se quiere que se
			// encuentren en todos los idiomas,
			// habrá que indexarse en todos los idiomas.

			for (final String keyIdioma : micro.getTraducciones().keySet()) {

				final MultilangLiteral extension = new MultilangLiteral();
				final MultilangLiteral titulo = new MultilangLiteral();
				final MultilangLiteral descripcion = new MultilangLiteral();
				final MultilangLiteral urls = new MultilangLiteral();
				final MultilangLiteral urlPadre = new MultilangLiteral();
				final MultilangLiteral tituloPadre = new MultilangLiteral();
				final MultilangLiteral searchTextOptional = new MultilangLiteral();
				final List<EnumIdiomas> idiomas = new ArrayList<EnumIdiomas>();

				final EnumIdiomas enumIdioma = EnumIdiomas.fromString(keyIdioma);

				final TraduccionMicrosite traduccion = (TraduccionMicrosite) micro.getTraduccion(keyIdioma);

				if (traduccion != null && enumIdioma != null) {

					// Si no tiene titulo microsite no indexamos
					if (traduccion.getTitulo() == null || traduccion.getTitulo().isEmpty()) {
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

					// Anyadimos idioma al enumerado.
					idiomas.add(enumIdioma);

					// Seteamos los primeros campos multiidiomas: Titulo, Descripción
					titulo.addIdioma(enumIdioma, IndexacionUtil.getTituloArchivo(archivo));
					descripcion.addIdioma(enumIdioma, IndexacionUtil.getDescripcionArchivo(archivo));

					// Extension
					extension.addIdioma(enumIdioma, FilenameUtils.getExtension(archivo.getNombre()));

					// Text optional
					searchTextOptional.addIdioma(enumIdioma, pathUO.getUosText());

					// URL
					urls.addIdioma(enumIdioma, IndexacionUtil.getUrlArchivo(micro, archivo, keyIdioma));

					// Padre
					urlPadre.addIdioma(enumIdioma, IndexacionUtil.getUrlMicrosite(micro, keyIdioma));
					tituloPadre.addIdioma(enumIdioma, IndexacionUtil.getTituloMicrosite(micro, keyIdioma));

					final IndexFile indexFile = new IndexFile();
					indexFile.setCategoria(EnumCategoria.GUSITE_ARCHIVO);
					indexFile.setAplicacionId(EnumAplicacionId.GUSITE);
					indexFile.setElementoId(idArchivo.toString() + "_" + enumIdioma.toString());
					indexFile.setTitulo(titulo);
					indexFile.setDescripcion(descripcion);
					indexFile.setUrl(urls);
					indexFile.setCategoriaPadre(EnumCategoria.GUSITE_MICROSITE);
					indexFile.setElementoIdPadre(micro.getId().toString());
					indexFile.setUrlPadre(urlPadre);
					indexFile.setUos(pathUO.getUosPath());
					indexFile.setSearchTextOptional(searchTextOptional);
					indexFile.setIdioma(enumIdioma);
					indexFile.setFileContent(contenidoFichero);
					indexFile.setExtension(extension);
					indexFile.setCategoriaRaiz(EnumCategoria.GUSITE_MICROSITE);
					indexFile.setElementoIdRaiz(micro.getId().toString());
					indexFile.setInterno(!micro.getRestringido().equals("N") ? true : false);

					solrIndexer.indexarFichero(indexFile);

				}
			}

			return new SolrPendienteResultado(true);

		} catch (final Exception exception) {
			log.error("Error intentando indexar idElemento:" + idElemento + " categoria:" + categoria + " idArchivo:"
					+ idArchivo, exception);
			return new SolrPendienteResultado(false, exception.getMessage());
		}
	}

	/**
	 * Obtiene lista de microsites para una Unidad Administrativa
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<Long> obtenerMicrositesbyUA(final List<Integer> listIds) {

		final Session session = this.getSession();
		try {
			final String hql = "select mic.id from Microsite mic where mic.unidadAdministrativa in  (:list)";
			final Query query = session.createQuery(hql);
			query.setParameterList("list", listIds);
			final List list = query.list();
			return list;

		} catch (final HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Comprueba si estan todos los microsites indexados.
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public boolean isTodosIndexados() {

		final Session session = this.getSession();
		try {
			final String hql = "select count(mic.id) from Microsite mic where mic.indexado = " + Microsite.NO_INDEXADO;
			final Query query = session.createQuery(hql);
			final int idCuantos = Integer.parseInt(query.uniqueResult().toString());
			boolean todosIndexados;
			if (idCuantos == 0) {
				todosIndexados = true;
			} else {
				todosIndexados = false;
			}
			return todosIndexados;

		} catch (final HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}
}
