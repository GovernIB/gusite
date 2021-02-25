package es.caib.gusite.micropersistence.ejb;

import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import es.caib.gusite.micromodel.Auditoria;
import es.caib.gusite.micromodel.PersonalizacionPlantilla;

/**
 * SessionBean para consultar PersonalizacionPlantilla.
 *
 * @author at4.net
 *
 * @ejb.bean name="sac/micropersistence/PersonalizacionPlantillaFacade"
 *           jndi-name="es.caib.gusite.micropersistence.PersonalizacionPlantillaFacade"
 *           type="Stateless" view-type="remote" transaction-type="Container"
 *
 * @ejb.transaction type="Required"
 *
 */
public abstract class PersonalizacionPlantillaFacadeEJB extends HibernateTrulyStatelessEJB {

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
	 * Crea un PersonalizacionPlantilla
	 *
	 * @ejb.interface-method
	 * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public PersonalizacionPlantilla crearPersonalizacionPlantilla(final PersonalizacionPlantilla instance) {

		log.debug("persisting PersonalizacionPlantilla instance");
		final Session session = this.getSession();
		try {
			final PersonalizacionPlantilla ret = (PersonalizacionPlantilla) session.get(PersonalizacionPlantilla.class,
					session.save(instance));
			session.flush();
			session.close();
			grabarAuditoria(ret, Auditoria.CREAR);
			return ret;

		} catch (final HibernateException re) {
			log.error("persist failed", re);
			throw new EJBException(re);
		} finally {
			log.debug("finished add PersonalizacionPlantilla instance");
		}
	}

	/**
	 * Actualiza un PersonalizacionPlantilla
	 *
	 * @ejb.interface-method
	 * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public void actualizarPersonalizacionPlantilla(final PersonalizacionPlantilla instance) {

		log.debug("updating PersonalizacionPlantilla instance");
		final Session session = this.getSession();
		try {
			// Now update the data.
			session.update(instance);
			session.flush();
			session.close();
			grabarAuditoria(instance, Auditoria.MODIFICAR);

		} catch (final HibernateException e) {
			log.error("update failed", e);
			throw new EJBException(e);
		} finally {
			log.debug("finished updating PersonalizacionPlantilla instance");
		}
	}

	/**
	 * Borra un PersonalizacionPlantilla
	 *
	 * @ejb.interface-method
	 * @ejb.permission role-name="${role.system},${role.admin}"
	 */
	public void borrarPersonalizacionPlantilla(final PersonalizacionPlantilla instance) {

		log.debug("deleting PersonalizacionPlantilla instance");
		final Session session = this.getSession();
		try {
			session.delete(instance);
			session.flush();
			session.close();
			grabarAuditoria(instance, Auditoria.ELIMINAR);

		} catch (final HibernateException e) {
			log.error("delete failed", e);
			throw new EJBException(e);
		} finally {
			log.debug("finished deleting PersonalizacionPlantilla instance");
		}
	}

	/**
	 * Lista todos los PersonalizacionPlantilla
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	@SuppressWarnings("unchecked")
	public List<PersonalizacionPlantilla> listarPersonalizacionPlantilla() {

		log.debug("listar PersonalizacionPlantilla");
		final Session session = this.getSession();
		try {
			final List<PersonalizacionPlantilla> instances = session.createCriteria(PersonalizacionPlantilla.class)
					.list();
			if (instances.size() == 0) {
				log.debug("get successful, no instance found");
			} else {
				log.debug("get successful, instances found");
			}
			session.close();
			return instances;

		} catch (final HibernateException re) {
			log.error("get failed", re);
			throw new EJBException(re);
		}
	}

	/**
	 * Obtiene un PersonalizacionPlantilla
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public PersonalizacionPlantilla obtenerPersonalizacionPlantilla(final java.lang.Long id) {

		log.debug("getting PersonalizacionPlantilla instance with id: " + id);
		final Session session = this.getSession();
		try {
			final PersonalizacionPlantilla instance = (PersonalizacionPlantilla) session
					.get(PersonalizacionPlantilla.class, id);
			if (instance == null) {
				log.debug("get successful, no instance found");
			} else {
				log.debug("get successful, instance found");
			}
			session.close();
			return instance;

		} catch (final HibernateException re) {
			log.error("get failed", re);
			throw new EJBException(re);
		}
	}

	/**
	 * Busca PersonalizacionPlantilla por searchByMicrosite
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<String> searchByMicrosite(final long microsite) {
		final Query query = getNamedQuery("es.caib.gusite.micromodel.PersonalizacionPlantilla.searchByMicrosite");
		query.setParameter("microsite", microsite);
		return query.list();
	}

	/**
	 * Busca PersonalizacionPlantilla por searchByMicrosite
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<PersonalizacionPlantilla> searchByMicrosite(final Long microsite, final String ordre,
			final Integer pagina, final Integer max) {

		final String orden = (ordre == null) ? "Aid" : ordre;
		try {
			final Session session = this.getSession();
			final Criteria criteria = session.createCriteria(PersonalizacionPlantilla.class);
			criteria.add(Restrictions.eq("microsite.id", microsite));
			criteria.setMaxResults(max);
			criteria.setFirstResult(pagina * max);

			if (orden.substring(0, 1).equals("A")) {
				criteria.addOrder(Order.asc(orden.substring(1)));
			} else {
				criteria.addOrder(Order.desc(orden.substring(1)));
			}

			final List<PersonalizacionPlantilla> list = criteria.list();
			return list;

		} catch (final HibernateException re) {
			log.error("get failed", re);
			throw new EJBException(re);
		}
	}

	/**
	 * Busca PersonalizacionPlantilla por searchByMicrositePlantilla
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<PersonalizacionPlantilla> searchByMicrositePlantilla(final long microsite, final String plantilla) {
		final Query query = getNamedQuery(
				"es.caib.gusite.micromodel.PersonalizacionPlantilla.searchByMicrositePlantilla");
		query.setParameter("microsite", microsite);
		query.setParameter("plantilla", plantilla);
		return query.list();
	}

	/**
	 * Busca PersonalizacionPlantilla por searchByPlantilla
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<PersonalizacionPlantilla> searchByTema(final Long tema) {
		final Query query = getNamedQuery("es.caib.gusite.micromodel.PersonalizacionPlantilla.searchByTema");
		query.setParameter("tema", tema);
		return query.list();
	}

	/**
	 * Borra un listado de PersonalizacionPlantilla por ids
	 *
	 * @ejb.interface-method
	 * @ejb.permission role-name="${role.system},${role.admin},${role.super}"
	 */
	public void borrarPersonalizacionPlantillas(final List<Long> ids) {

		log.debug("deleting PersonalizacionPlantilla list");
		final Session session = this.getSession();
		try {
			String hql = "delete from PersonalizacionPlantilla as perPla";
			hql += " where perPla.id in (:list)";
			final Query query = session.createQuery(hql);
			query.setParameterList("list", ids);
			query.executeUpdate();
			session.close();

		} catch (final HibernateException e) {
			log.error("delete failed", e);
			throw new EJBException(e);
		} finally {
			log.debug("finished deleting PersonalizacionPlantilla instance");
		}
	}

	/**
	 * Contar PersonalizacionPlantilla por Microsite
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Long countByMicrosite(final Long microsite) {

		final Query query = getNamedQuery("es.caib.gusite.micromodel.PersonalizacionPlantilla.countByMicrosite");
		query.setParameter("microsite", microsite);
		return (Long) query.uniqueResult();
	}
}
