package es.caib.gusite.micropersistence.ejb;

import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;

import org.hibernate.HibernateException;

import es.caib.gusite.micromodel.Auditoria;
import es.caib.gusite.micromodel.Plantilla;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

/**
 * SessionBean para consultar Plantilla.
 * 
 * @author at4.net
 * 
 * @ejb.bean name="sac/micropersistence/PlantillaFacade"
 *           jndi-name="es.caib.gusite.micropersistence.PlantillaFacade"
 *           type="Stateless" view-type="remote" transaction-type="Container"
 * 
 * @ejb.transaction type="Required"
 * 
 */
public abstract class PlantillaFacadeEJB extends HibernateTrulyStatelessEJB {

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
	 * Crea un Plantilla
	 * 
	 * @ejb.interface-method
	 * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public Plantilla crearPlantilla(Plantilla instance) {

		log.debug("persisting Plantilla instance");
        Session session = this.getSession();
		try {
			Plantilla ret = (Plantilla) session.get(Plantilla.class, session.save(instance));
            session.flush();
            session.close();
			this.grabarAuditoria(ret, Auditoria.CREAR);
			return ret;

		} catch (HibernateException re) {
			log.error("persist failed", re);
			throw new EJBException(re);
		} finally {
			log.debug("finished add Plantilla instance");
		}
	}

	/**
	 * Actualiza un Plantilla
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public void actualizarPlantilla(Plantilla instance) {
		log.debug("updating Plantilla instance");
		Session session = this.getSession();
		try {
			// Now update the data.
			session.update(instance);
			this.grabarAuditoria(instance, Auditoria.MODIFICAR);
		} catch (HibernateException e) {
			log.error("update failed", e);
			throw new EJBException(e);
		} finally {
			this.close(session);
			log.debug("finished updating Plantilla instance");
		}
	}

	/**
	 * Borra un Plantilla
	 * 
	 * @ejb.interface-method
	 * @ejb.permission role-name="${role.system},${role.admin}"
	 */
	public void borrarPlantilla(Plantilla instance) {
		log.debug("deleting Plantilla instance");
		Session session = this.getSession();
		try {
			session.delete(instance);
			this.grabarAuditoria(instance, Auditoria.ELIMINAR);
		} catch (HibernateException e) {
			log.error("delete failed", e);
			throw new EJBException(e);
		} finally {
			this.close(session);
			log.debug("finished deleting Plantilla instance");
		}
	}

	/**
	 * Lista todos los Plantilla
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	@SuppressWarnings("unchecked")
	public List<Plantilla> listarPlantilla() {
		log.debug("listar Plantilla");
		Session session = this.getSession();
		try {
			List<Plantilla> instances = session
					.createCriteria(Plantilla.class).addOrder(Order.asc("nombre")).list();
			if (instances.size() == 0) {
				log.debug("get successful, no instance found");
			} else {
				log.debug("get successful, instances found");
			}
			return instances;
		} catch (HibernateException re) {
			log.error("get failed", re);
			throw new EJBException(re);
		} finally {
			this.close(session);
		}

	}

	/**
	 * Obtiene un Plantilla
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Plantilla obtenerPlantilla(java.lang.Long id) {
		log.debug("getting Plantilla instance with id: " + id);
		Session session = this.getSession();
		try {
			Plantilla instance = (Plantilla) session.get(Plantilla.class, id);
			if (instance == null) {
				log.debug("get successful, no instance found");
			} else {
				// forzar carga de colecciones para poder cerrar la conexión
				// sin riesgo a LazyInitializationException
				instance.getPersonalizacionesPlantilla().size();
				log.debug("get successful, instance found");
			}
			return instance;
		} catch (HibernateException re) {
			log.error("get failed", re);
			throw new EJBException(re);
		} finally {
			this.close(session);
		}
	}

    /**
     * Obtiene un Plantilla por nombre
     *
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public Plantilla obtenerPlantillaPorNombre(String nombre) {

        Query query = getNamedQuery("es.caib.gusite.micromodel.Plantilla.obtenerPlantillaPorNombre");
        query.setParameter("nombre", nombre);
        return (Plantilla) query.uniqueResult();
    }

}
