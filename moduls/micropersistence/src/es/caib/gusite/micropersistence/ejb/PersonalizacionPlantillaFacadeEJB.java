package es.caib.gusite.micropersistence.ejb;

import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;

import org.hibernate.HibernateException;

import es.caib.gusite.micromodel.Auditoria;
import es.caib.gusite.micromodel.PersonalizacionPlantilla;

/**
 * SessionBean para consultar PersonalizacionPlantilla.
 * 
 * @author at4.net
 * 
 * @ejb.bean name="sac/micropersistence/PersonalizacionPlantillaFacade"
 *           jndi-name
 *           ="es.caib.gusite.micropersistence.PersonalizacionPlantillaFacade"
 *           type="Stateless" view-type="remote" transaction-type="Container"
 * 
 * @ejb.transaction type="Required"
 * 
 */
public abstract class PersonalizacionPlantillaFacadeEJB extends HibernateEJB {

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
	 * @ejb.permission 
	 *                 role-name="${role.system},\${role.admin},${role.super},${role.oper
	 *                 } "
	 */
	public PersonalizacionPlantilla crearPersonalizacionPlantilla(
			PersonalizacionPlantilla instance) {
		log.debug("persisting PersonalizacionPlantilla instance");
		try {
			PersonalizacionPlantilla ret = (PersonalizacionPlantilla) this
					.getSession().get(PersonalizacionPlantilla.class,
							this.getSession().save(instance));
			this.grabarAuditoria(ret, Auditoria.CREAR);
			return ret;

		} catch (HibernateException re) {
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
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public void actualizarPersonalizacionPlantilla(
			PersonalizacionPlantilla instance) {
		log.debug("updating PersonalizacionPlantilla instance");
		try {
			// Now update the data.
			this.getSession().update(instance);
			this.grabarAuditoria(instance, Auditoria.MODIFICAR);
		} catch (HibernateException e) {
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
	public void borrarPersonalizacionPlantilla(PersonalizacionPlantilla instance) {
		log.debug("deleting PersonalizacionPlantilla instance");
		try {
			this.getSession().delete(instance);
			this.grabarAuditoria(instance, Auditoria.ELIMINAR);
		} catch (HibernateException e) {
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
		try {
			List<PersonalizacionPlantilla> instances = this.getSession()
					.createCriteria(PersonalizacionPlantilla.class).list();
			if (instances.size() == 0) {
				log.debug("get successful, no instance found");
			} else {
				log.debug("get successful, instances found");
			}
			return instances;
		} catch (HibernateException re) {
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
	public PersonalizacionPlantilla obtenerPersonalizacionPlantilla(
			java.lang.Long id) {
		log.debug("getting PersonalizacionPlantilla instance with id: " + id);
		try {
			PersonalizacionPlantilla instance = (PersonalizacionPlantilla) this
					.getSession().get(PersonalizacionPlantilla.class, id);
			if (instance == null) {
				log.debug("get successful, no instance found");
			} else {
				log.debug("get successful, instance found");
			}
			return instance;
		} catch (HibernateException re) {
			log.error("get failed", re);
			throw new EJBException(re);
		}
	}

}
