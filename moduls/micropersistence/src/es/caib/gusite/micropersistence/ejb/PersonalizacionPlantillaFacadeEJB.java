package es.caib.gusite.micropersistence.ejb;


import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;

import es.caib.gusite.micromodel.PersonalizacionPlantilla;
import es.caib.gusite.micromodel.Auditoria;
import javax.ejb.CreateException;
import javax.ejb.EJBException;

/**
 * SessionBean para consultar PersonalizacionPlantilla.
 * @author at4.net
 *
 * @ejb.bean
 *  name="sac/micropersistence/PersonalizacionPlantillaFacade"
 *  jndi-name="es.caib.gusite.micropersistence.PersonalizacionPlantillaFacade"
 *  type="Stateless"
 *  view-type="remote"
 *  transaction-type="Container"
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
    public void ejbCreate() throws CreateException {
        super.ejbCreate();
    }

	
    /**
     * Crea un PersonalizacionPlantilla
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},\${role.admin},${role.super},${role.oper}"
     */
    public PersonalizacionPlantilla crearPersonalizacionPlantilla(PersonalizacionPlantilla instance) {
        log.debug("persisting PersonalizacionPlantilla instance");
        try {
			PersonalizacionPlantilla ret = (PersonalizacionPlantilla) getSession().get(
				PersonalizacionPlantilla.class, 
				getSession().save(instance)
				);
            grabarAuditoria(ret, Auditoria.CREAR);
            return ret;
				
        }
        catch (HibernateException re) {
            log.error("persist failed", re);
			throw new EJBException(re);
		} finally {
			log.debug("finished add PersonalizacionPlantilla instance");
		}
    }

    /**
     * Actualiza un PersonalizacionPlantilla
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
     */
    public void actualizarPersonalizacionPlantilla(PersonalizacionPlantilla instance) {
        log.debug("updating PersonalizacionPlantilla instance");
		try {
			// Now update the data.
			getSession().update(instance);
            grabarAuditoria(instance, Auditoria.MODIFICAR);
		}
		catch (HibernateException e) {
			log.error("update failed", e);
			throw new EJBException(e);
		} finally {
			log.debug("finished updating PersonalizacionPlantilla instance");
		}
    }
    
    /**
     * Borra un PersonalizacionPlantilla
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin}"
     */
    public void borrarPersonalizacionPlantilla(PersonalizacionPlantilla instance) {
        log.debug("deleting PersonalizacionPlantilla instance");
		try {
			getSession().delete(instance);
            grabarAuditoria(instance, Auditoria.ELIMINAR);
		}
		catch (HibernateException e) {
			log.error("delete failed", e);
			throw new EJBException(e);
		} finally {
			log.debug("finished deleting PersonalizacionPlantilla instance");
		}
    }

    /**
     * Lista todos los PersonalizacionPlantilla
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
    @SuppressWarnings("unchecked")
    public List<PersonalizacionPlantilla> listarPersonalizacionPlantilla() {
        log.debug("listar PersonalizacionPlantilla");
        try {
			List<PersonalizacionPlantilla> instances = (List<PersonalizacionPlantilla>)getSession().createCriteria(PersonalizacionPlantilla.class).list();
            if (instances.size()==0) {
                log.debug("get successful, no instance found");
            }
            else {
                log.debug("get successful, instances found");
            }
            return instances;
        }
        catch (HibernateException re) {
            log.error("get failed", re);
            throw new EJBException(re);
        }

    }
    
    /**
     * Obtiene un PersonalizacionPlantilla
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public PersonalizacionPlantilla obtenerPersonalizacionPlantilla( java.lang.Long id) {
        log.debug("getting PersonalizacionPlantilla instance with id: " + id);
        try {
			PersonalizacionPlantilla instance = (PersonalizacionPlantilla) getSession().get(PersonalizacionPlantilla.class, id);
            if (instance==null) {
                log.debug("get successful, no instance found");
            }
            else {
                log.debug("get successful, instance found");
            }
            return instance;
        }
        catch (HibernateException re) {
            log.error("get failed", re);
            throw new EJBException(re);
        }
    }
    
    /**
     * Busca PersonalizacionPlantilla por searchByMicrosite
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public List<PersonalizacionPlantilla> searchByMicrosite(long microsite) {
        Query query = getNamedQuery("es.caib.gusite.micromodel.PersonalizacionPlantilla.searchByMicrosite");
        query.setParameter("microsite", microsite);
        return (List<PersonalizacionPlantilla>) query.list();
    }

    /**
     * Busca PersonalizacionPlantilla por searchByMicrositePlantilla
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public List<PersonalizacionPlantilla> searchByMicrositePlantilla(long microsite, String plantilla) {
        Query query = getNamedQuery("es.caib.gusite.micromodel.PersonalizacionPlantilla.searchByMicrositePlantilla");
        query.setParameter("microsite", microsite);
        query.setParameter("plantilla", plantilla);
        return (List<PersonalizacionPlantilla>) query.list();
    }
    

}

