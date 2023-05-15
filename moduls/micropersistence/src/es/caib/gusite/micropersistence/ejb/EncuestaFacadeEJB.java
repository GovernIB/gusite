package es.caib.gusite.micropersistence.ejb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.Transaction;

import es.caib.gusite.micromodel.Archivo;
import es.caib.gusite.micromodel.Auditoria;
import es.caib.gusite.micromodel.Encuesta;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.Pregunta;
import es.caib.gusite.micromodel.Respuesta;
import es.caib.gusite.micromodel.RespuestaDato;
import es.caib.gusite.micromodel.SolrPendienteResultado;
import es.caib.gusite.micromodel.TraduccionEncuesta;
import es.caib.gusite.micromodel.TraduccionPregunta;
import es.caib.gusite.micromodel.TraduccionRespuesta;
import es.caib.gusite.micromodel.UsuarioPropietarioRespuesta;
import es.caib.gusite.micropersistence.delegate.ArchivoDelegate;
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
 * SessionBean para manipular encuestas.
 *
 * @ejb.bean name="sac/micropersistence/EncuestaFacade"
 *           jndi-name="es.caib.gusite.micropersistence.EncuestaFacade"
 *           type="Stateless" view-type="remote" transaction-type="Container"
 *
 * @ejb.transaction type="Required"
 *
 * @author Indra
 */
@SuppressWarnings({ "unchecked", "deprecation" })
public abstract class EncuestaFacadeEJB extends HibernateEJB {

	private static final long serialVersionUID = -8128896105109064587L;

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
	 * Inicializo los parámetros de la consulta....
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public void init(final Long site) {
		super.tampagina = 10;
		super.pagina = 0;
		// super.select="";
		super.select = "select enc.id, enc.fcaducidad, enc.fpublicacion, enc.visible, trad.titulo ";
		super.from = " from Encuesta enc join enc.traducciones trad ";
		super.where = " where trad.id.codigoIdioma = '" + Idioma.getIdiomaPorDefecto() + "' and enc.idmicrosite = "
				+ site.toString();
		super.whereini = " ";
		super.orderby = "";

		super.camposfiltro = new String[] { "trad.titulo" };
		super.cursor = 0;
		super.nreg = 0;
		super.npags = 0;
	}

	/**
	 * Inicializo los parámetros de la consulta....
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public void initra(final Long site, final String idiomapasado) {
		super.tampagina = 10;
		super.pagina = 0;
		// super.select="";
		super.select = "select enc.id, enc.fcaducidad, enc.fpublicacion, enc.visible, trad.titulo ";
		super.from = " from Encuesta enc join enc.traducciones trad ";
		super.where = " where (trad.id.codigoIdioma = '" + Idioma.getIdiomaPorDefecto()
				+ "' or trad.id.codigoIdioma = '" + idiomapasado + "') and enc.idmicrosite = " + site.toString();
		super.whereini = " ";
		super.orderby = " order by enc.id,trad.id.codigoIdioma desc";

		super.camposfiltro = new String[] { "trad.titulo" };
		super.cursor = 0;
		super.nreg = 0;
		super.npags = 0;
	}

	/**
	 * Inicializo los parámetros de la consulta....
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public void init() {
		super.tampagina = 10;
		super.pagina = 0;
		// super.select="";
		super.select = "select enc.id, enc.fcaducidad, enc.fpublicacion, enc.visible, trad.titulo ";
		super.from = " from Encuesta enc join enc.traducciones trad ";
		super.where = " where trad.id.codigoIdioma = '" + Idioma.getIdiomaPorDefecto() + "'";
		super.whereini = " ";
		super.orderby = "";

		super.camposfiltro = new String[] { "trad.titulo" };
		super.cursor = 0;
		super.nreg = 0;
		super.npags = 0;
	}

	/**
	 * Crea o actualiza una encuesta
	 *
	 * @ejb.interface-method
	 * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public Long grabarEncuesta(final Encuesta enc) throws DelegateException {

		final Session session = this.getSession();
		try {
			final boolean nuevo = (enc.getId() == null) ? true : false;
			final Transaction tx = session.beginTransaction();

			final Map<String, TraduccionEncuesta> listaTraducciones = new HashMap<String, TraduccionEncuesta>();

			if (nuevo) {
				final Iterator<TraduccionEncuesta> it = enc.getTraducciones().values().iterator();
				while (it.hasNext()) {
					final TraduccionEncuesta trd = it.next();
					listaTraducciones.put(trd.getId().getCodigoIdioma(), trd);
				}
				enc.setTraducciones(null);
			} else {// #28 Comentario sobre que da error borrando la URI de una traduccion
				String listIdiomaBorrar = "";
				final Iterator<TraduccionEncuesta> it = enc.getTraducciones().values().iterator();
				while (it.hasNext()) {
					final TraduccionEncuesta trd = it.next();
					listIdiomaBorrar += "'" + trd.getId().getCodigoIdioma() + "'";
					if (it.hasNext()) {
						listIdiomaBorrar += ",";
					}
				}
				// Borramos los idiomas que no pertenecen a contenido y existen en la BBDD
				if (!listIdiomaBorrar.isEmpty()) {
					final Query query = session.createQuery(
							"select tradEnc from TraduccionEncuesta tradEnc where tradEnc.id.codigoEncuesta = "
									+ enc.getId() + " and tradEnc.id.codigoIdioma not in (" + listIdiomaBorrar + ") ");
					final List<TraduccionEncuesta> traduciones = query.list();
					for (final TraduccionEncuesta traducI : traduciones) {
						session.delete(traducI);
					}
					session.flush();
				}
			}

			session.saveOrUpdate(enc);
			session.flush();

			if (nuevo) {
				for (final TraduccionEncuesta trad : listaTraducciones.values()) {
					trad.getId().setCodigoEncuesta(enc.getId());
					session.saveOrUpdate(trad);
				}
				session.flush();
				enc.setTraducciones(listaTraducciones);
			}

			tx.commit();
			this.close(session);

			final int op = (nuevo) ? Auditoria.CREAR : Auditoria.MODIFICAR;
			this.grabarAuditoria(enc, op);

			// Indexamos
			final SolrPendienteDelegate pendienteDel = DelegateUtil.getSolrPendienteDelegate();
			pendienteDel.grabarSolrPendiente(EnumCategoria.GUSITE_ENCUESTA.toString(), enc.getId(), null,
					IndexacionUtil.REINDEXAR);

			return enc.getId();

		} catch (final HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Obtiene una encuesta
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Encuesta obtenerEncuesta(final Long id) {

		final Session session = this.getSession();
		try {
			final Encuesta enc = (Encuesta) session.get(Encuesta.class, id);
			return enc;

		} catch (final HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Obtiene una encuesta a partir de la URI
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Encuesta obtenerEncuestaDesdeUri(final String idioma, final String uri, final String site) {

		final Session session = this.getSession();
		try {
			Query query;
			if (idioma != null) {
				query = session.createQuery(
						"select encuesta from Encuesta encuesta JOIN encuesta.traducciones te where te.id.codigoIdioma = :idioma and te.uri = :uri and encuesta.idmicrosite = :site");
				query.setParameter("idioma", idioma);
			} else {
				query = session.createQuery(
						"select encuesta from Encuesta encuesta JOIN encuesta.traducciones te where te.uri = :uri and encuesta.idmicrosite = :site");
			}
			query.setParameter("uri", uri);
			query.setParameter("site", Long.valueOf(site));

			query.setMaxResults(1);
			return (Encuesta) query.uniqueResult();
		} catch (final ObjectNotFoundException oNe) {
			log.error(oNe.getMessage());
			return new Encuesta();
		} catch (final HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Lista todas las encuestas
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<Encuesta> listarEncuestas() {

		final Session session = this.getSession();
		try {
			this.parametrosCons(); // Establecemos los parámetros de la
									// paginación

			final Query query = session.createQuery(this.select + this.from + this.where + this.orderby);

			final ScrollableResults scr = query.scroll();
			final ArrayList<Encuesta> lista = new ArrayList<Encuesta>();
			scr.first();
			scr.scroll(this.cursor - 1);
			int i = 0;
			while (this.tampagina > i++) {
				final Object[] fila = scr.get();
				final Encuesta enc = new Encuesta();
				enc.setId((Long) fila[0]);
				enc.setFcaducidad((java.util.Date) fila[1]);
				enc.setFpublicacion((java.util.Date) fila[2]);
				enc.setVisible((String) fila[3]);

				final TraduccionEncuesta trad = new TraduccionEncuesta();
				trad.setTitulo((String) fila[4]);
				enc.setTraduccion(Idioma.getIdiomaPorDefecto(), trad);

				lista.add(enc);
				if (!scr.next()) {
					break;
				}
			}
			scr.close();
			return lista;

		} catch (final HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Lista todas las encuestas de recursos
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<?> listarEncuestasrec(final String idiomapasado) {

		final Session session = this.getSession();
		try {
			this.parametrosCons(); // Establecemos los parámetros de la
									// paginación
			final Query query = session.createQuery(this.select + this.from + this.where + this.orderby);

			final ScrollableResults scr = query.scroll();
			final ArrayList<Encuesta> lista = new ArrayList<Encuesta>();
			scr.first();
			scr.scroll(this.cursor - 1);
			int i = 0;
			while (this.tampagina > i++) {
				final Object[] fila = scr.get();
				final Encuesta enc = new Encuesta();
				enc.setId((Long) fila[0]);
				enc.setFcaducidad((java.util.Date) fila[1]);
				enc.setFpublicacion((java.util.Date) fila[2]);
				enc.setVisible((String) fila[3]);

				final TraduccionEncuesta trad = new TraduccionEncuesta();
				trad.setTitulo((String) fila[4]);
				enc.setTraduccion(idiomapasado, trad);

				lista.add(enc);
				if (!scr.next()) {
					break;
				}
			}
			scr.close();
			return lista;

		} catch (final HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * borra una encuesta
	 *
	 * @ejb.interface-method
	 * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public void borrarEncuesta(final Long id) throws DelegateException {
		borrarEncuesta(id, true);
	}

	/**
	 * borra una encuesta
	 *
	 * @ejb.interface-method
	 * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public void borrarEncuesta(final Long id, final boolean indexar) throws DelegateException {

		final Session session = this.getSession();

		try {

			final Transaction tx = session.beginTransaction();
			final Encuesta encuesta = (Encuesta) session.get(Encuesta.class, id);

			final List<Pregunta> preguntas = session.createQuery("from Pregunta where idencuesta = " + id).list();
			for (int p = 0; p < preguntas.size(); p++) {

				final List<?> respuestas = this.listarRespuestas(new Long(preguntas.get(p).getId()));

				for (int i = 0; i < respuestas.size(); i++) {
					session.createQuery("delete from UsuarioPropietarioRespuesta where id.idrespuesta = "
							+ ((Respuesta) respuestas.get(i)).getId()).executeUpdate();
				}

				// Segundo: Borramos las respuestas de las preguntas.
				for (int i = 0; i < respuestas.size(); i++) {
					session.createQuery("delete from TraduccionRespuesta where id.codigoRespuesta = "
							+ ((Respuesta) respuestas.get(i)).getId()).executeUpdate();

					session.createQuery(
							"delete from RespuestaDato where idrespueta = " + ((Respuesta) respuestas.get(i)).getId())
							.executeUpdate();

					session.createQuery("delete from Respuesta where id = " + ((Respuesta) respuestas.get(i)).getId())
							.executeUpdate();
				}

				session.createQuery(
						"delete from TraduccionPregunta where id.codigoPregunta = " + preguntas.get(p).getId())
						.executeUpdate();
				session.createQuery("delete from Pregunta where id = " + preguntas.get(p).getId()).executeUpdate();

				// Borrar imagen asociada a pregunta, si existe.
				if (preguntas.get(p).getImagen() != null)
					DelegateUtil.getArchivoDelegate().borrarArchivo(preguntas.get(p).getImagen().getId());

			}

			session.createQuery("delete from TraduccionEncuesta where id.codigoEncuesta = " + id).executeUpdate();
			session.createQuery("delete from Encuesta where id = " + id).executeUpdate();

			session.flush();
			tx.commit();
			this.close(session);

			this.grabarAuditoria(encuesta, Auditoria.ELIMINAR);

			// DesIndexamos
			if (indexar) {
				final SolrPendienteDelegate pendienteDel = DelegateUtil.getSolrPendienteDelegate();
				pendienteDel.grabarSolrPendiente(EnumCategoria.GUSITE_ENCUESTA.toString(), encuesta.getId(), null,
						IndexacionUtil.DESINDEXAR);
			}
		} catch (final HibernateException he) {

			throw new EJBException(he);

		} finally {

			this.close(session);

		}

	}

	/**
	 * Crea o actualiza una pregunta
	 *
	 * @ejb.interface-method
	 * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public void grabarPregunta(final Pregunta pre) throws DelegateException {

		final Session session = this.getSession();
		Boolean nuevo = false;
		Pregunta preOriginal = null;

		final ArchivoDelegate archivoDelegate = DelegateUtil.getArchivoDelegate();
		Archivo imagen = null;
		final List<Archivo> archivosPorBorrar = new ArrayList<Archivo>();

		try {

			if (pre.getId() == null) {
				nuevo = true;
			}

			final Transaction tx = session.beginTransaction();
			final Map<String, TraduccionPregunta> listaTraducciones = new HashMap<String, TraduccionPregunta>();

			if (nuevo) {

				final Iterator<TraduccionPregunta> it = pre.getTraducciones().values().iterator();
				while (it.hasNext()) {
					final TraduccionPregunta trd = it.next();
					listaTraducciones.put(trd.getId().getCodigoIdioma(), trd);
				}
				pre.setTraducciones(null);

				if (pre.getImagen() != null) {

					imagen = pre.getImagen();
					pre.setImagen(null);

				}

			} else {

				preOriginal = this.obtenerPregunta(pre.getId());

				if (pre.getImagen() != null) {
					if (pre.getImagen().getId() != null)
						archivoDelegate.grabarArchivo(pre.getImagen());
					else
						archivoDelegate.insertarArchivo(pre.getImagen());
				} else {
					// Archivo a null pero anterior no lo era: solicitan borrado
					if (preOriginal.getImagen() != null) {
						archivosPorBorrar.add(preOriginal.getImagen());
					}
				}

			}

			session.saveOrUpdate(pre);
			session.flush();

			if (nuevo) {

				for (final TraduccionPregunta trad : listaTraducciones.values()) {
					trad.getId().setCodigoPregunta(pre.getId());
					session.saveOrUpdate(trad);
				}
				session.flush();
				pre.setTraducciones(listaTraducciones);

				if (imagen != null) {
					archivoDelegate.insertarArchivo(imagen);
				}

				pre.setImagen(imagen);

				session.saveOrUpdate(pre);
				session.flush();

			}

			tx.commit();

			// Borramos archivo FK de la pregunta solicitado para borrar.
			if (archivosPorBorrar.size() > 0)
				archivoDelegate.borrarArchivos(archivosPorBorrar);

			// refrescamos la encuesta para que actualice la lista de preguntas del
			// backoffice (#82)
			final Encuesta enc = (Encuesta) session.get(Encuesta.class, pre.getIdencuesta());
			session.refresh(enc);

			final int op = (nuevo) ? Auditoria.CREAR : Auditoria.MODIFICAR;
			this.grabarAuditoria(enc.getIdmicrosite(), pre, op);

			// Indexamos
			final SolrPendienteDelegate pendienteDel = DelegateUtil.getSolrPendienteDelegate();
			pendienteDel.grabarSolrPendiente(EnumCategoria.GUSITE_ENCUESTA.toString(), enc.getId(), null,
					IndexacionUtil.REINDEXAR);

		} catch (final HibernateException e) {

			throw new EJBException(e);

		} finally {

			this.close(session);

		}

	}

	/**
	 * Obtiene una pregunta
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Pregunta obtenerPregunta(final Long id) {

		final Session session = this.getSession();
		try {
			final Pregunta pre = (Pregunta) session.get(Pregunta.class, id);
			Hibernate.initialize(pre);
			return pre;

		} catch (final HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Lista todas las preguntas de una encuesta
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<?> listarPreguntas(final Long id) {

		final Session session = this.getSession();
		try {
			final String hql = "select pre" + " from Pregunta pre" + " join pre.traducciones trad"
					+ " where trad.id.codigoIdioma = '" + Idioma.getIdiomaPorDefecto() + "'" + " and pre.idencuesta = "
					+ id.toString();

			final Query query = session.createQuery(hql);
			return query.list();

		} catch (final HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Lista todas las preguntas de una encuesta ordenadas ascendentemente
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<?> listarPreguntasOrdAsc(final Long id) {

		final Session session = this.getSession();
		try {
			final String hql = "select pre" + " from Pregunta pre" + " join pre.traducciones trad"
					+ " where trad.id.codigoIdioma = '" + Idioma.getIdiomaPorDefecto() + "'" + " and pre.idencuesta = "
					+ id.toString() + " order by pre.orden asc";

			final Query query = session.createQuery(hql);
			return query.list();

		} catch (final HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Elimina una o varias preguntas de la encuesta
	 *
	 * @ejb.interface-method
	 * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public void eliminarPreguntas(final String[] idpreguntas, final Long encuesta_id) throws DelegateException {

		final Session session = this.getSession();

		try {

			final Transaction tx = session.beginTransaction();
			final List<Pregunta> preguntas = new ArrayList<Pregunta>();

			for (final String idpregunta : idpreguntas) {

				final List<?> respuestas = this.listarRespuestas(new Long(idpregunta));
				preguntas.add((Pregunta) session.get(Pregunta.class, Long.parseLong(idpregunta)));

				for (int i = 0; i < respuestas.size(); i++) {
					session.createQuery("delete from UsuarioPropietarioRespuesta where id.idrespuesta = "
							+ ((Respuesta) respuestas.get(i)).getId()).executeUpdate();
				}

				// Segundo: Borramos las respuestas de las preguntas.
				for (int i = 0; i < respuestas.size(); i++) {
					session.createQuery("delete from TraduccionRespuesta where id.codigoRespuesta = "
							+ ((Respuesta) respuestas.get(i)).getId()).executeUpdate();
					session.createQuery("delete from Respuesta where id = " + ((Respuesta) respuestas.get(i)).getId())
							.executeUpdate();
				}

				session.createQuery(
						"delete from TraduccionPregunta where id.codigoPregunta = " + Long.parseLong(idpregunta))
						.executeUpdate();
				session.createQuery("delete from Pregunta where id = " + Long.parseLong(idpregunta)).executeUpdate();

			}

			session.flush();
			tx.commit();

			// Actualizamos el indice
			final Encuesta enc = (Encuesta) session.get(Encuesta.class, encuesta_id);
			this.close(session);

			for (final Pregunta p : preguntas) {

				// Borramos archivos asociados.
				if (p.getImagen() != null)
					DelegateUtil.getArchivoDelegate().borrarArchivo(p.getImagen().getId());

				// Grabamos operación de borrado.
				this.grabarAuditoria(enc.getIdmicrosite(), p, Auditoria.ELIMINAR);

			}

			// Indexamos
			final SolrPendienteDelegate pendienteDel = DelegateUtil.getSolrPendienteDelegate();
			pendienteDel.grabarSolrPendiente(EnumCategoria.GUSITE_ENCUESTA.toString(), enc.getId(), null,
					IndexacionUtil.REINDEXAR);

		} catch (final HibernateException e) {

			throw new EJBException(e);

		} finally {

			this.close(session);

		}

	}

	/**
	 * Crea o actualiza una respuesta
	 *
	 * @ejb.interface-method
	 * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public void grabarRespuesta(final Respuesta res) throws DelegateException {

		final Session session = this.getSession();
		try {
			final boolean nuevo = (res.getId() == null) ? true : false;

			final Transaction tx = session.beginTransaction();
			final Map<String, TraduccionRespuesta> listaTraducciones = new HashMap<String, TraduccionRespuesta>();

			if (nuevo) {
				final Iterator<TraduccionRespuesta> it = res.getTraducciones().values().iterator();
				while (it.hasNext()) {
					final TraduccionRespuesta trd = it.next();
					listaTraducciones.put(trd.getId().getCodigoIdioma(), trd);
				}
				res.setTraducciones(null);
			}

			session.saveOrUpdate(res);
			session.flush();

			if (nuevo) {
				for (final TraduccionRespuesta trad : listaTraducciones.values()) {
					trad.getId().setCodigoRespuesta(res.getId());
					session.saveOrUpdate(trad);
				}
				session.flush();
				res.setTraducciones(listaTraducciones);
			}

			tx.commit();
			// refrescamos la encuesta para que actualice la lista de respuestas del
			// backoffice (#82)
			final Encuesta enc = this.obtenerEncuesta(this.obtenerPregunta(res.getIdpregunta()).getIdencuesta());
			session.refresh(enc);
			this.close(session);

			final int op = (nuevo) ? Auditoria.CREAR : Auditoria.MODIFICAR;
			this.grabarAuditoria(enc.getIdmicrosite(), res, op);

			// Indexamos
			final SolrPendienteDelegate pendienteDel = DelegateUtil.getSolrPendienteDelegate();
			pendienteDel.grabarSolrPendiente(EnumCategoria.GUSITE_ENCUESTA.toString(), enc.getId(), null,
					IndexacionUtil.REINDEXAR);

		} catch (final HibernateException e) {
			throw new EJBException(e);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Obtiene una respuesta
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Respuesta obtenerRespuesta(final Long id) {

		final Session session = this.getSession();
		try {
			final Respuesta res = (Respuesta) session.get(Respuesta.class, id);
			return res;

		} catch (final HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Obtiene una respuesta libre del usuario
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public RespuestaDato obtenerRespuestaDato(final Long idRespuesta, final Long idUsuario) {

		final Session session = this.getSession();
		try {
			final String hql = "from RespuestaDato res" + " where res.idusuari = " + idUsuario.toString()
					+ " and res.idrespueta = " + idRespuesta.toString();

			final Query query = session.createQuery(hql);
			return (RespuestaDato) query.uniqueResult();

		} catch (final HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Obtiene la llista de respuestes libre del usuario
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Hashtable<Long, RespuestaDato> listarRespuestasDato(final Long idEncuesta, final Long idUsuario) {

		final Session session = this.getSession();
		try {
			final String hql = "from RespuestaDato res" + " where res.idusuari = " + idUsuario.toString()
					+ " and res.idencuesta = " + idEncuesta.toString();

			final Query query = session.createQuery(hql);
			final List<RespuestaDato> list = query.list();
			final Hashtable<Long, RespuestaDato> hash = new Hashtable<Long, RespuestaDato>();
			for (final RespuestaDato res : list) {
				hash.put(res.getIdrespueta(), res);
			}
			return hash;

		} catch (final HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Lista todas las respuestas de una pregunta
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<?> listarRespuestas(final Long id) {

		final Session session = this.getSession();
		try {
			final String hql = "select res" + " from Respuesta res" + " join res.traducciones trad"
					+ " where trad.id.codigoIdioma = '" + Idioma.getIdiomaPorDefecto() + "'" + " and res.idpregunta="
					+ id.toString();

			final Query query = session.createQuery(hql);
			return query.list();

		} catch (final HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * elimina una o varias respuestas de la pregunta
	 *
	 * @ejb.interface-method
	 * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public void eliminarRespuestas(final String[] idrespuestas, final Long pregunta_id) throws DelegateException {

		final Session session = this.getSession();
		try {
			final Transaction tx = session.beginTransaction();

			// Primero: En el caso que la pregunta tenga respuesta, borraremos
			// la relación de usuarios con las respuestas de la pregunta
			for (final String idrespuesta : idrespuestas) {
				session.createQuery("delete from UsuarioPropietarioRespuesta where id.idrespuesta = " + idrespuesta)
						.executeUpdate();
			}

			final List<Respuesta> respuestas = new ArrayList<Respuesta>();
			// Segundo: Borramos las respuestas de las preguntas.
			for (final String idrespuesta : idrespuestas) {
				respuestas.add((Respuesta) session.get(Respuesta.class, Long.parseLong(idrespuesta)));
				session.createQuery("delete from TraduccionRespuesta where id.codigoRespuesta = " + idrespuesta)
						.executeUpdate();

				session.createQuery("delete from RespuestaDato where idrespueta = " + idrespuesta).executeUpdate();

				session.createQuery("delete from Respuesta where id = " + idrespuesta).executeUpdate();
			}

			session.flush();
			tx.commit();
			// Actualizamos el indice
			final Encuesta enc = this.obtenerEncuesta(this.obtenerPregunta(pregunta_id).getIdencuesta());
			this.close(session);

			for (final Respuesta r : respuestas) {
				this.grabarAuditoria(enc.getIdmicrosite(), r, Auditoria.ELIMINAR);
			}

			// Indexamos
			final SolrPendienteDelegate pendienteDel = DelegateUtil.getSolrPendienteDelegate();
			pendienteDel.grabarSolrPendiente(EnumCategoria.GUSITE_ENCUESTA.toString(), enc.getId(), null,
					IndexacionUtil.REINDEXAR);

		} catch (final HibernateException e) {
			throw new EJBException(e);
		} finally {
			this.close(session);
		}
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
					"from Encuesta enc where enc.idmicrosite = " + site.toString() + " and enc.id = " + id.toString());
			return query.list().isEmpty();

		} catch (final HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Suma 1 al numero de veces que es respondida una respuesta
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public void sumarRespuesta(final Long id) {

		final Session session = this.getSession();
		try {
			final Respuesta res = (Respuesta) session.get(Respuesta.class, id);
			if (res != null) {
				if (res.getNrespuestas() != null) {
					res.setNrespuestas(new Integer(res.getNrespuestas().intValue() + 1));
				} else {
					res.setNrespuestas(new Integer(1));
				}
			}
			session.saveOrUpdate(res);
			session.flush();

		} catch (final HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Suma 1 al numero de veces respondida una pregunta
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public void sumarPregunta(final Long id) {

		final Session session = this.getSession();
		try {
			final Pregunta pre = (Pregunta) session.get(Pregunta.class, id);
			if (pre != null) {
				if (pre.getNrespuestas() != null) {
					pre.setNrespuestas(new Integer(pre.getNrespuestas().intValue() + 1));
				} else {
					pre.setNrespuestas(new Integer(1));
				}
				session.saveOrUpdate(pre);
				session.flush();
			}

		} catch (final HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Crea o actualiza grabarUsuarioPropietarioRespuesta
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Long grabarUsuarioPropietarioRespuesta(final UsuarioPropietarioRespuesta upm) {

		final Session session = this.getSession();
		try {
			session.save(upm);
			session.flush();
			return upm.getId().getIdusuario();

		} catch (final HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Obtener todos los usuarios que han seleccionado una respuesta
	 *
	 * @ejb.interface-method
	 * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public List<?> obtenerUsuariosRespuesta(final Long idRespuesta) {

		final Session session = this.getSession();
		try {
			final String hql = " from UsuarioPropietarioRespuesta res where res.id.idrespuesta = "
					+ idRespuesta.toString();
			final Query query = session.createQuery(hql);
			return query.list();

		} catch (final HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Obtener todas las respuestas de un usuario
	 *
	 * @ejb.interface-method
	 * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public List<?> obtenerRespuestasDeUsuario(final Long idUsuario) {

		final Session session = this.getSession();
		try {
			final String hql = "select res.id.idrespuesta from UsuarioPropietarioRespuesta res where res.id.idusuario = "
					+ idUsuario.toString();
			final Query query = session.createQuery(hql);
			return query.list();

		} catch (final HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Obtiene los usuarios de una encuesta.
	 *
	 * @ejb.interface-method
	 * @ejb.permission role-name="${role.system},${role.admin},${role.oper},${role.super}"
	 */
	public List<?> obtenerUsuariosEncuesta(final Long id) {

		final Session session = this.getSession();
		try {
			final String hql = "select distinct u.id.idusuario" + " from UsuarioPropietarioRespuesta u"
					+ " where u.id.idrespuesta in (" + "select r.id" + " from Pregunta p, Respuesta r"
					+ " where r.idpregunta = p.id" + " and p.idencuesta = " + id + ")";

			final Query query = session.createQuery(hql);
			return query.list();

		} catch (final HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Obtener el número de votos por respuesta en función de un grupo de usuarios
	 *
	 * @ejb.interface-method
	 * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public Hashtable<String, String> obtenerNumVotosByResp(final Collection<?> condicioUsu) {

		final Session session = this.getSession();
		final Hashtable<String, String> hash = new Hashtable<String, String>();
		try {
			String filtro = "";
			final Iterator<?> iter = condicioUsu.iterator();
			while (iter.hasNext()) {
				final String valor = (String) iter.next();
				filtro += "res.id.idusuario =" + valor + " OR ";
			}

			if (filtro.length() > 0) {
				filtro = filtro.substring(0, filtro.length() - 3);
			}

			if (filtro.length() > 0) {
				final String hql = "select count(*), res.id.idrespuesta" + " from UsuarioPropietarioRespuesta res"
						+ " where (" + filtro + ")" + " group by res.id.idrespuesta";

				final Query query = session.createQuery(hql);
				final Iterator<?> res = query.iterate();
				while (res.hasNext()) {
					final Object[] fila = (Object[]) res.next();
					final String ncount = "" + fila[0];
					final String valor = "" + fila[1];

					if (valor != null && !valor.equals("null") && valor.length() > 0) {
						if (ncount != null && !ncount.equals("null") && ncount.length() > 0) {
							hash.put(valor, ncount);
						}
					}
				}
			}
			return hash;

		} catch (final HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Obtiene una encuesta
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Encuesta obtenerEncuestaBySolr(final Long id) {

		final Session session = this.getSession();
		try {
			final Encuesta enc = (Encuesta) session.get(Encuesta.class, id);
			return enc;
		} catch (final Exception e) {
			log.error("Error obtenido la encuesta", e);
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
		log.debug("EncuestafacadeEJB.indexarSolr. idElemento:" + idElemento + " categoria:" + categoria);

		try {
			final MicrositeDelegate micrositedel = DelegateUtil.getMicrositeDelegate();

			// Paso 0. Obtenemos el contenido y comprobamos si se puede indexar.
			final Encuesta encuesta = obtenerEncuestaBySolr(idElemento);
			if (encuesta == null) {
				return new SolrPendienteResultado(true, "Error obteniendo la encuesta.");
			}

			if (!IndexacionUtil.isIndexable(encuesta)) {
				return new SolrPendienteResultado(true, "No se puede indexar");
			}

			final Microsite micro = micrositedel.obtenerMicrosite(encuesta.getIdmicrosite());

			if (!IndexacionUtil.isIndexable(micro)) {
				return new SolrPendienteResultado(true, "No se puede indexar");
			}

			// Iteramos las traducciones
			final MultilangLiteral titulo = new MultilangLiteral();
			final MultilangLiteral descripcion = new MultilangLiteral();
			final MultilangLiteral urls = new MultilangLiteral();
			final MultilangLiteral searchText = new MultilangLiteral();
			final MultilangLiteral searchTextOptional = new MultilangLiteral();
			final List<EnumIdiomas> idiomas = new ArrayList<EnumIdiomas>();
			final MultilangLiteral tituloPadre = new MultilangLiteral();
			final MultilangLiteral urlPadre = new MultilangLiteral();
			List<PathUO> uosPath = null;

			for (final String keyIdioma : encuesta.getTraducciones().keySet()) {
				final EnumIdiomas enumIdioma = EnumIdiomas.fromString(keyIdioma);
				final TraduccionEncuesta traduccion = (TraduccionEncuesta) encuesta.getTraduccion(keyIdioma);

				if (traduccion != null && enumIdioma != null) {

					if (StringUtils.isBlank(traduccion.getTitulo())) {
						continue;
					}

					// Path UO

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
					titulo.addIdioma(enumIdioma, traduccion.getTitulo());
					descripcion.addIdioma(enumIdioma, traduccion.getTitulo());
					urls.addIdioma(enumIdioma, IndexacionUtil.getUrlEncuesta(micro, encuesta, keyIdioma));

					// Pregunta traducción en el idioma que estamos
					String preguntas = "";
					String respuestas = "";
					for (final Pregunta preg : encuesta.getPreguntas()) {
						final TraduccionPregunta tradPreg = (TraduccionPregunta) preg.getTraduccion(keyIdioma);
						preguntas += " " + tradPreg.getTitulo();

						for (final Respuesta resp : preg.getRespuestas()) {
							final TraduccionRespuesta tradResp = (TraduccionRespuesta) resp.getTraduccion(keyIdioma);
							respuestas += " " + tradResp.getTitulo();
						}
					}
					searchText.addIdioma(enumIdioma, solrIndexer
							.htmlToText((traduccion.getTitulo() == null ? "" : traduccion.getTitulo()) + preguntas));

					searchTextOptional.addIdioma(enumIdioma, pathUO.getUosText() + respuestas);

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
			indexData.setFechaPublicacion(encuesta.getFpublicacion());
			indexData.setFechaCaducidad(encuesta.getFcaducidad());
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
	 * Obtiene las encuestas de un microsite
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<Encuesta> obtenerEncuestasByMicrositeId(final Long idMicrosite) {

		final Session session = this.getSession();

		try {

			final Query query = session.createQuery("from Encuesta en where en.idmicrosite =" + idMicrosite.toString());

			final List<Encuesta> lista = query.list();

			return lista;

		} catch (final HibernateException he) {

			throw new EJBException(he);

		} finally {

			this.close(session);

		}

	}

}
