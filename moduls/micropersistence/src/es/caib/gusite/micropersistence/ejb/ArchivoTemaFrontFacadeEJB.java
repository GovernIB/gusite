package es.caib.gusite.micropersistence.ejb;

import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;

import org.hibernate.HibernateException;

import es.caib.gusite.micromodel.ArchivoTemaFront;
import es.caib.gusite.micromodel.Auditoria;

/**
 * SessionBean para consultar ArchivoTemaFront.
 * 
 * @author at4.net
 * 
 * @ejb.bean name="sac/micropersistence/ArchivoTemaFrontFacade"
 *           jndi-name="es.caib.gusite.micropersistence.ArchivoTemaFrontFacade"
 *           type="Stateless" view-type="remote" transaction-type="Container"
 * 
 * @ejb.transaction type="Required"
 * 
 */
public abstract class ArchivoTemaFrontFacadeEJB extends HibernateEJB {

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
	 * Crea un ArchivoTemaFront
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},\${role.admin},${role.super},${role.oper
	 *                 } "
	 */
	public ArchivoTemaFront crearArchivoTemaFront(ArchivoTemaFront instance) {
		log.debug("persisting ArchivoTemaFront instance");
		try {
			ArchivoTemaFront ret = (ArchivoTemaFront) this.getSession().get(
					ArchivoTemaFront.class, this.getSession().save(instance));
			this.grabarAuditoria(ret, Auditoria.CREAR);
			return ret;

		} catch (HibernateException re) {
			log.error("persist failed", re);
			throw new EJBException(re);
		} finally {
			log.debug("finished add ArchivoTemaFront instance");
		}
	}

	/**
	 * Actualiza un ArchivoTemaFront
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public void actualizarArchivoTemaFront(ArchivoTemaFront instance) {
		log.debug("updating ArchivoTemaFront instance");
		try {
			// Now update the data.
			this.getSession().update(instance);
			this.grabarAuditoria(instance, Auditoria.MODIFICAR);
		} catch (HibernateException e) {
			log.error("update failed", e);
			throw new EJBException(e);
		} finally {
			log.debug("finished updating ArchivoTemaFront instance");
		}
	}

	/**
	 * Borra un ArchivoTemaFront
	 * 
	 * @ejb.interface-method
	 * @ejb.permission role-name="${role.system},${role.admin}"
	 */
	public void borrarArchivoTemaFront(ArchivoTemaFront instance) {
		log.debug("deleting ArchivoTemaFront instance");
		try {
			this.getSession().delete(instance);
			this.grabarAuditoria(instance, Auditoria.ELIMINAR);
		} catch (HibernateException e) {
			log.error("delete failed", e);
			throw new EJBException(e);
		} finally {
			log.debug("finished deleting ArchivoTemaFront instance");
		}
	}

	/**
	 * Lista todos los ArchivoTemaFront
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	@SuppressWarnings("unchecked")
	public List<ArchivoTemaFront> listarArchivoTemaFront() {
		log.debug("listar ArchivoTemaFront");
		try {
			List<ArchivoTemaFront> instances = this.getSession()
					.createCriteria(ArchivoTemaFront.class).list();
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
	 * Obtiene un ArchivoTemaFront
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public ArchivoTemaFront obtenerArchivoTemaFront(java.lang.Long id) {
		log.debug("getting ArchivoTemaFront instance with id: " + id);
		try {
			ArchivoTemaFront instance = (ArchivoTemaFront) this.getSession()
					.get(ArchivoTemaFront.class, id);
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
