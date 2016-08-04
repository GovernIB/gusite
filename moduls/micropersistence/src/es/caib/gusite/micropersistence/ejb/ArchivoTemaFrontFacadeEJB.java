package es.caib.gusite.micropersistence.ejb;

import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import es.caib.gusite.micromodel.ArchivoTemaFront;
import es.caib.gusite.micromodel.Auditoria;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;

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
@SuppressWarnings("unchecked")
public abstract class ArchivoTemaFrontFacadeEJB extends HibernateTrulyStatelessEJB {

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
	 *                 role-name="${role.system},${role.admin}"
	 */
	public ArchivoTemaFront crearArchivoTemaFront(ArchivoTemaFront instance) {

		log.debug("persisting ArchivoTemaFront instance");
        Session session = this.getSession();
		try {
            if (instance.getArchivo().getIdmicrosite() == null) {
                instance.getArchivo().setIdmicrosite(new Long(0));
            }
            DelegateUtil.getArchivoDelegate().insertarArchivo(instance.getArchivo());
			ArchivoTemaFront ret = (ArchivoTemaFront) session.get(ArchivoTemaFront.class, session.save(instance));
            session.flush();
            session.close();
			this.grabarAuditoria(ret, Auditoria.CREAR);
			return ret;

		} catch (HibernateException re) {
			log.error("persist failed", re);
			throw new EJBException(re);
		} catch (DelegateException re) {
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
	 *                 role-name="${role.system},${role.admin}"
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
        Session session = this.getSession();
		try {
			session.delete(instance);
            session.flush();
            session.close();
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
	public ArchivoTemaFront obtenerArchivoTemaFront(Long id) {

		log.debug("getting ArchivoTemaFront instance with id: " + id);
        Session session = this.getSession();
		try {
			ArchivoTemaFront instance = (ArchivoTemaFront) session.get(ArchivoTemaFront.class, id);
			if (instance == null) {
				log.debug("get successful, no instance found");
			} else {
				log.debug("get successful, instance found");
			}
            session.close();
			return instance;

		} catch (HibernateException re) {
			log.error("get failed", re);
			throw new EJBException(re);
		}
	}

    /**
     * Borra un listado de ArchivoTemaFront por ids.
     * 
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin}"
     */
    public void borrarArchivosTemaFront(List<Long> ids) {

		log.debug("deleting ArchivoTemaFront list");
		Session session = this.getSession();
		
		try {
			
			String hql = "delete from ArchivoTemaFront as archTF where archTF.id in (:list)";
			
			Query query = session.createQuery(hql);
			query.setParameterList("list", ids);
			query.executeUpdate();
			
			session.close();

		} catch (HibernateException e) {
			
			log.error("delete failed", e);
			throw new EJBException(e);
			
		} finally {
			
			log.debug("finished deleting ArchivoTemaFront instance");
			
		}
        
    }

    /**
     * Busca Archivos por searchByTema
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
	public List<ArchivoTemaFront> searchByTema(Long tema) {
        Query query = getNamedQuery("es.caib.gusite.micromodel.ArchivoTemaFront.searchByTema");
        query.setParameter("tema", tema);
        return (List<ArchivoTemaFront>) query.list();
    }

    /**
     * Busca Archivos por searchByTemaNombre
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public List<ArchivoTemaFront> searchByTemaNombre(String uriTema, String nombre) {
        Query query = getNamedQuery("es.caib.gusite.micromodel.ArchivoTemaFront.searchByTemaNombre");
        query.setParameter("uriTema", uriTema);
        query.setParameter("nombre", nombre);
        return (List<ArchivoTemaFront>) query.list();
    }

}
