package es.caib.gusite.micropersistence.ejb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;

import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import es.caib.gusite.micromodel.Auditoria;
import es.caib.gusite.micromodel.Contacto;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.Lineadatocontacto;
import es.caib.gusite.micromodel.TraduccionFContacto;
import es.caib.gusite.micromodel.TraduccionLineadatocontacto;
import es.caib.gusite.micropersistence.delegate.DelegateException;

/**
 * SessionBean para manipular Contacto.
 *
 * @ejb.bean name="sac/micropersistence/ContactoFacade"
 *           jndi-name="es.caib.gusite.micropersistence.ContactoFacade"
 *           type="Stateless" view-type="remote" transaction-type="Container"
 *
 * @ejb.transaction type="Required"
 *
 * @author Indra
 */

public abstract class ContactoFacadeEJB extends HibernateEJB {

	private static final long serialVersionUID = -6869856489116757129L;

	/**
	 * @ejb.create-method
	 * @ejb.permission unchecked="true"
	 */
	@Override
	public void ejbCreate() throws CreateException {
		super.ejbCreate();
	}

	/**
	 * Inicializo los parámetros de la consulta de Contacto.... No está bien hecho
	 * debería ser Statefull
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public void init(final Long site) {
		super.tampagina = 10;
		super.pagina = 0;
		super.select = "select contacto ";
		super.from = " from Contacto contacto join contacto.traducciones trad ";
		super.where = "where contacto.idmicrosite = " + site.toString() + " and trad.id.codigoIdioma = '"
				+ Idioma.getIdiomaPorDefecto() + "'";
		super.whereini = " ";
		super.orderby = "";
		super.camposfiltro = new String[] { "contacto.email", "contacto.visible", "contacto.anexarch" };
		super.cursor = 0;
		super.nreg = 0;
		super.npags = 0;
	}

	/**
	 * Inicializo los parámetros de la consulta de Contacto.... No está bien hecho
	 * debería ser Statefull
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public void init() {
		super.tampagina = 10;
		super.pagina = 0;
		super.select = "select contacto ";
		super.from = " from Contacto contacto join contacto.traducciones trad ";
		super.where = "where trad.id.codigoIdioma = '" + Idioma.getIdiomaPorDefecto() + "'";
		super.whereini = " ";
		super.orderby = "";
		super.camposfiltro = new String[] { "contacto.email", "contacto.visible", "contacto.anexarch" };
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
	public void init(final Long site, final String idiomapasado) {
		super.tampagina = 10;
		super.pagina = 0;
		super.select = "select contacto.id,trad.nombre ";
		super.from = " from Contacto contacto join contacto.traducciones trad ";
		super.where = " where (trad.id.codigoIdioma = '" + Idioma.getIdiomaPorDefecto()
				+ "' or trad.id.codigoIdioma = '" + idiomapasado + "') and contacto.idmicrosite = " + site.toString();
		super.whereini = " ";
		super.orderby = "";

		super.camposfiltro = new String[] { "trad.nombre" };
		super.cursor = 0;
		super.nreg = 0;
		super.npags = 0;
	}

	/**
	 * Crea o actualiza un Contacto
	 *
	 * @ejb.interface-method
	 * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public Long grabarContacto(final Contacto contacto) throws DelegateException {

		final Session session = this.getSession();
		try {
			final boolean nuevo = (contacto.getId() == null) ? true : false;
			final Transaction tx = session.beginTransaction();

			final Map<String, TraduccionFContacto> listaTraducciones = new HashMap<String, TraduccionFContacto>();
			if (nuevo) {
				final Iterator<TraduccionFContacto> it = contacto.getTraducciones().values().iterator();
				while (it.hasNext()) {
					final TraduccionFContacto trd = it.next();
					listaTraducciones.put(trd.getId().getCodigoIdioma(), trd);
				}
				contacto.setTraducciones(null);
			} else {// #28 Incidencia borrando traducciones
				String listIdiomaBorrar = "";
				final Iterator<TraduccionFContacto> it = contacto.getTraducciones().values().iterator();
				while (it.hasNext()) {
					final TraduccionFContacto trd = it.next();
					listIdiomaBorrar += "'" + trd.getId().getCodigoIdioma() + "'";
					if (it.hasNext()) {
						listIdiomaBorrar += ",";
					}
				}
				// Borramos los idiomas que no pertenecen a contenido y existen en la BBDD
				if (!listIdiomaBorrar.isEmpty()) {
					final Query query = session.createQuery(
							"select tradFC from TraduccionFContacto tradFC where tradFC.id.codigoFContacto = "
									+ contacto.getId() + " and tradFC.id.codigoIdioma not in (" + listIdiomaBorrar
									+ ") ");
					final List<TraduccionFContacto> traduciones = query.list();
					for (final TraduccionFContacto traducI : traduciones) {
						session.delete(traducI);
					}
					session.flush();
				}
			}

			session.saveOrUpdate(contacto);
			session.flush();

			if (nuevo) {
				for (final TraduccionFContacto trad : listaTraducciones.values()) {
					trad.getId().setCodigoFContacto(contacto.getId());
					session.saveOrUpdate(trad);
				}
				session.flush();
				contacto.setTraducciones(listaTraducciones);
			}

			tx.commit();
			this.close(session);

			final int op = (nuevo) ? Auditoria.CREAR : Auditoria.MODIFICAR;
			this.grabarAuditoria(contacto, op);

			return contacto.getId();

		} catch (final HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Obtiene una linea del Formulario
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Lineadatocontacto obtenerLinea(final Long id) {

		final Session session = this.getSession();
		try {
			final Lineadatocontacto linea = (Lineadatocontacto) session.get(Lineadatocontacto.class, id);
			return linea;

		} catch (final HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Obtiene un Formulario
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Contacto obtenerContacto(final Long id) {

		final Session session = this.getSession();
		try {
			final Contacto contacto = (Contacto) session.get(Contacto.class, id);
			return contacto;

		} catch (final HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Obtiene el Contacto de ese microsite que tiene la uri especificada en el
	 * idioma indicado
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Contacto obtenerContactoByUri(final String idioma, final String uri, final String site) {

		final Session session = this.getSession();
		try {
			Query query;
			if (idioma != null) {
				query = session.createQuery(
						"select contacto from Contacto contacto JOIN contacto.traducciones ct where ct.id.codigoIdioma = :idioma and ct.uri = :uri and contacto.idmicrosite = :site");
				query.setParameter("idioma", idioma);

			} else {
				query = session.createQuery(
						"select contacto from Contacto contacto JOIN contacto.traducciones ct where ct.uri = :uri and contacto.idmicrosite = :site");
			}
			query.setParameter("uri", uri);
			query.setParameter("site", Long.valueOf(site));
			query.setMaxResults(1);
			return (Contacto) query.uniqueResult();

		} catch (final ObjectNotFoundException oNe) {
			log.error(oNe.getMessage());
			return new Contacto();
		} catch (final HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Lista todos los Formularios
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<?> listarContactos() {

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
	 * borra un Formulario
	 *
	 * @ejb.interface-method
	 * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public void borrarContacto(final Long id) throws DelegateException {
		borrarContacto(id, true);
	}

	/**
	 * borra un Formulario
	 *
	 * @ejb.interface-method
	 * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public void borrarContacto(final Long id, final boolean indexar) throws DelegateException {

		final Session session = this.getSession();
		try {
			final Contacto contacto = (Contacto) session.get(Contacto.class, id);

			final Transaction tx = session.beginTransaction();
			final Iterator<Lineadatocontacto> iter = contacto.getLineasdatocontacto().iterator();
			while (iter.hasNext()) {
				final Lineadatocontacto linea = iter.next();
				session.createQuery(
						"delete TraduccionLineadatocontacto tlinea where tlinea.id.codigoLineadatocontacto = "
								+ linea.getId().toString())
						.executeUpdate();
				session.createQuery("delete Lineadatocontacto linea where linea.id = " + linea.getId().toString())
						.executeUpdate();
				session.flush();
			}

			// Borramos las traducciones de ese Formulario de contacto
			session.createQuery("delete TraduccionFContacto tFC where tFC.id.codigoFContacto = " + id.toString())
					.executeUpdate();
			session.flush();

			session.createQuery("delete Contacto contact where contact.id = " + id.toString()).executeUpdate();
			session.flush();
			tx.commit();
			this.close(session);

			this.grabarAuditoria(contacto, Auditoria.ELIMINAR);

		} catch (final HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Añade una nueva línea al formulario o modifica la que existe
	 *
	 * @ejb.interface-method
	 * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public void creamodificaLinea(final Lineadatocontacto lin, final Long idcontacto) throws DelegateException {

		final Session session = this.getSession();
		try {
			final boolean nuevo = (lin.getId() == null) ? true : false;

			final Transaction tx = session.beginTransaction();
			final Map<String, TraduccionLineadatocontacto> listaTraducciones = new HashMap<String, TraduccionLineadatocontacto>();
			lin.setIdcontacto(idcontacto);

			if (nuevo) {
				final Iterator<TraduccionLineadatocontacto> it = lin.getTraducciones().values().iterator();
				while (it.hasNext()) {
					final TraduccionLineadatocontacto trd = it.next();
					listaTraducciones.put(trd.getId().getCodigoIdioma(), trd);
				}
				lin.setTraducciones(null);
			}

			session.saveOrUpdate(lin);
			session.flush();

			if (nuevo) {
				for (final TraduccionLineadatocontacto trad : listaTraducciones.values()) {
					trad.getId().setCodigoLineadatocontacto(lin.getId());
					session.saveOrUpdate(trad);
				}
				session.flush();
				lin.setTraducciones(listaTraducciones);
			}

			tx.commit();
			final Contacto contacto = (Contacto) session.get(Contacto.class, idcontacto);
			this.close(session);

			final int op = (nuevo) ? Auditoria.CREAR : Auditoria.MODIFICAR;
			this.grabarAuditoria(contacto.getIdmicrosite(), lin, op);

		} catch (final HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * elimina líneas del formulario
	 *
	 * @ejb.interface-method
	 * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public void eliminarLineas(final String[] lineas, final Long idContacto) throws DelegateException {

		Session session = this.getSession();
		try {
			final Transaction tx = session.beginTransaction();
			final Contacto contacto = (Contacto) session.get(Contacto.class, idContacto);
			final List<Lineadatocontacto> ldatas = new ArrayList<Lineadatocontacto>();

			for (final String linea : lineas) {
				ldatas.add((Lineadatocontacto) session.get(Lineadatocontacto.class, Long.parseLong(linea)));
				session.createQuery(
						"delete from TraduccionLineadatocontacto tlin where tlin.id.codigoLineadatocontacto = " + linea)
						.executeUpdate();
				session.createQuery("delete from Lineadatocontacto lin where id = " + linea).executeUpdate();
			}
			session.flush();
			tx.commit();
			this.close(session);

			session = this.getSession();
			for (final Lineadatocontacto ldata : ldatas) {
				this.grabarAuditoria(contacto.getIdmicrosite(), ldata, Auditoria.ELIMINAR);
			}

		} catch (final HibernateException e) {
			throw new EJBException(e);
		} finally {
			this.close(session);
		}
	}

	/**
	 * elimina una línea del formulario
	 *
	 * @ejb.interface-method
	 * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public void eliminarLinea(final Long idLinea, final Long idContacto) throws DelegateException {

		final Session session = this.getSession();
		try {
			final Contacto contacto = (Contacto) session.get(Contacto.class, idContacto);
			final Lineadatocontacto ldata = (Lineadatocontacto) session.get(Lineadatocontacto.class, idLinea);

			session.createQuery(
					"delete from TraduccionLineadatocontacto tlin where tlin.id.codigoLineadatocontacto = " + idLinea)
					.executeUpdate();
			session.createQuery("delete from Lineadatocontacto lin where id = " + idLinea).executeUpdate();
			session.flush();
			this.close(session);

			this.grabarAuditoria(contacto.getIdmicrosite(), ldata, Auditoria.ELIMINAR);

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
					"from Contacto con where con.idmicrosite = " + site.toString() + " and con.id = " + id.toString());
			return query.list().isEmpty();

		} catch (final HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

}
