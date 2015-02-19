package es.caib.gusite.micropersistence.ejb;

import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;

import org.hibernate.HibernateException;

import es.caib.gusite.micromodel.Auditoria;
import es.caib.gusite.micromodel.TemaFront;

/**
 * SessionBean para consultar TemaFront.
 * 
 * @author at4.net
 * 
 * @ejb.bean name="sac/micropersistence/TemaFrontFacade"
 *           jndi-name="es.caib.gusite.micropersistence.TemaFrontFacade"
 *           type="Stateless" view-type="remote" transaction-type="Container"
 * 
 * @ejb.transaction type="Required"
 * 
 */
public abstract class TemaFrontFacadeEJB extends HibernateTrulyStatelessEJB {

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
	 * Crea un TemaFront
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},\${role.admin},${role.super},${role.oper
	 *                 } "
	 */
	public TemaFront crearTemaFront(TemaFront instance) {
		log.debug("persisting TemaFront instance");
		try {
			TemaFront ret = (TemaFront) this.getSession().get(TemaFront.class,
					this.getSession().save(instance));
			this.grabarAuditoria(ret, Auditoria.CREAR);
			return ret;

		} catch (HibernateException re) {
			log.error("persist failed", re);
			throw new EJBException(re);
		} finally {
			log.debug("finished add TemaFront instance");
		}
	}

	/**
	 * Actualiza un TemaFront
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public void actualizarTemaFront(TemaFront instance) {
		log.debug("updating TemaFront instance");
		try {
			// Now update the data.
			this.getSession().update(instance);
			this.grabarAuditoria(instance, Auditoria.MODIFICAR);
		} catch (HibernateException e) {
			log.error("update failed", e);
			throw new EJBException(e);
		} finally {
			log.debug("finished updating TemaFront instance");
		}
	}

	/**
	 * Borra un TemaFront
	 * 
	 * @ejb.interface-method
	 * @ejb.permission role-name="${role.system},${role.admin}"
	 */
	public void borrarTemaFront(TemaFront instance) {
		log.debug("deleting TemaFront instance");
		try {
			this.getSession().delete(instance);
			this.grabarAuditoria(instance, Auditoria.ELIMINAR);
		} catch (HibernateException e) {
			log.error("delete failed", e);
			throw new EJBException(e);
		} finally {
			log.debug("finished deleting TemaFront instance");
		}
	}

	/**
	 * Lista todos los TemaFront
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	@SuppressWarnings("unchecked")
	public List<TemaFront> listarTemaFront() {
		log.debug("listar TemaFront");
		try {
			List<TemaFront> instances = this.getSession()
					.createCriteria(TemaFront.class).list();
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
	 * Obtiene un TemaFront
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public TemaFront obtenerTemaFront(java.lang.Long id) {
		log.debug("getting TemaFront instance with id: " + id);
		try {
			TemaFront instance = (TemaFront) this.getSession().get(
					TemaFront.class, id);
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
