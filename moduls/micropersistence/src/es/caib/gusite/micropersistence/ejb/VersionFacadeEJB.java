package es.caib.gusite.micropersistence.ejb;

import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;

import org.hibernate.HibernateException;

import es.caib.gusite.micromodel.Auditoria;
import es.caib.gusite.micromodel.Version;

/**
 * SessionBean para consultar Version.
 * 
 * @author at4.net
 * 
 * @ejb.bean name="sac/micropersistence/VersionFacade"
 *           jndi-name="es.caib.gusite.micropersistence.VersionFacade"
 *           type="Stateless" view-type="remote" transaction-type="Container"
 * 
 * @ejb.transaction type="Required"
 * 
 */
public abstract class VersionFacadeEJB extends HibernateEJB {

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
	 * Crea un Version
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},\${role.admin},${role.super},${role.oper
	 *                 } "
	 */
	public Version crearVersion(Version instance) {
		log.debug("persisting Version instance");
		try {
			Version ret = (Version) this.getSession().get(Version.class,
					this.getSession().save(instance));
			this.grabarAuditoria(ret, Auditoria.CREAR);
			return ret;

		} catch (HibernateException re) {
			log.error("persist failed", re);
			throw new EJBException(re);
		} finally {
			log.debug("finished add Version instance");
		}
	}

	/**
	 * Actualiza un Version
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public void actualizarVersion(Version instance) {
		log.debug("updating Version instance");
		try {
			// Now update the data.
			this.getSession().update(instance);
			this.grabarAuditoria(instance, Auditoria.MODIFICAR);
		} catch (HibernateException e) {
			log.error("update failed", e);
			throw new EJBException(e);
		} finally {
			log.debug("finished updating Version instance");
		}
	}

	/**
	 * Borra un Version
	 * 
	 * @ejb.interface-method
	 * @ejb.permission role-name="${role.system},${role.admin}"
	 */
	public void borrarVersion(Version instance) {
		log.debug("deleting Version instance");
		try {
			this.getSession().delete(instance);
			this.grabarAuditoria(instance, Auditoria.ELIMINAR);
		} catch (HibernateException e) {
			log.error("delete failed", e);
			throw new EJBException(e);
		} finally {
			log.debug("finished deleting Version instance");
		}
	}

	/**
	 * Lista todos los Version
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	@SuppressWarnings("unchecked")
	public List<Version> listarVersion() {
		log.debug("listar Version");
		try {
			List<Version> instances = this.getSession()
					.createCriteria(Version.class).list();
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
	 * Obtiene un Version
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Version obtenerVersion(java.lang.String id) {
		log.debug("getting Version instance with id: " + id);
		try {
			Version instance = (Version) this.getSession().get(Version.class,
					id);
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
