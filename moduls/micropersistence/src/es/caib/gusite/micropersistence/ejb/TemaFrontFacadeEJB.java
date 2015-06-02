package es.caib.gusite.micropersistence.ejb;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;

import es.caib.gusite.micromodel.ArchivoTemaFront;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.PersonalizacionPlantilla;
import es.caib.gusite.micropersistence.delegate.ArchivoTemaFrontDelegate;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.PersonalizacionPlantillaDelegate;
import org.hibernate.*;

import es.caib.gusite.micromodel.Auditoria;
import es.caib.gusite.micromodel.TemaFront;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

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
	 *                 role-name="${role.system},${role.admin}"
	 */
	public TemaFront crearTemaFront(TemaFront instance) {

		log.debug("persisting TemaFront instance");
        Session session = this.getSession();
		try {
			TemaFront ret = (TemaFront) session.get(TemaFront.class, session.save(instance));
            session.flush();
            session.close();
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
	 *                 role-name="${role.system},${role.admin}"
	 */
	public void actualizarTemaFront(TemaFront instance) {

		log.debug("updating TemaFront instance");
        Session session = this.getSession();
		try {
			// Now update the data.
			session.update(instance);
            session.flush();
            session.close();
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
        Session session = this.getSession();
		try {
            log.debug("deleting PersonalizacionPlantilla list");
            PersonalizacionPlantillaDelegate personalizacionPlantillaDelegate = DelegateUtil.getPersonalizacionPlantillaDelegate();
            ArchivoTemaFrontDelegate archivoTemaFrontDelegate = DelegateUtil.getArchivoTemaFrontDelegate();
            
            List<Long> idsBorrar;
            List<PersonalizacionPlantilla> personalizacionPlantillas = personalizacionPlantillaDelegate.searchByTema(instance.getId());
            idsBorrar = extraerIdsObject(personalizacionPlantillas);
            if (idsBorrar.size() > 0) {
                personalizacionPlantillaDelegate.borrarPersonalizacionPlantillas(idsBorrar);
            }

            List<ArchivoTemaFront> archivoTemaFronts = archivoTemaFrontDelegate.searchByTema(instance.getId());
            idsBorrar = extraerIdsObject(archivoTemaFronts);
            if (idsBorrar.size() > 0) {
                archivoTemaFrontDelegate.borrarArchivosTemaFront(idsBorrar);
            }

            session.delete(instance);
            session.flush();
            session.close();
			this.grabarAuditoria(instance, Auditoria.ELIMINAR);

		} catch (HibernateException e) {
			log.error("delete failed", e);
			throw new EJBException(e);
		} catch (DelegateException e) {
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
        Session session = this.getSession();
		try {
			List<TemaFront> instances = session.createCriteria(TemaFront.class).list();
			if (instances.size() == 0) {
				log.debug("get successful, no instance found");
			} else {
				log.debug("get successful, instances found");
			}
            session.close();
			return instances;

		} catch (HibernateException re) {
			log.error("get failed", re);
			throw new EJBException(re);
		}
	}

    /**
     * Lista todos los TemaFront
     *
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    @SuppressWarnings("unchecked")
    public List<TemaFront> listarTemaFrontOrden(String ordre) {

        String orden = (ordre == null || ordre.equals("")) ? "Did" : ordre;
        log.debug("listar TemaFront");
        Session session = this.getSession();
        try {
            Criteria criteria = session.createCriteria(TemaFront.class);
            if (orden.substring(0, 1).equals("A")) {
                criteria.addOrder(Order.asc(orden.substring(1)));
            } else {
                criteria.addOrder(Order.desc(orden.substring(1)));
            }
            List<TemaFront> instances = criteria.list();
            if (instances.size() == 0) {
                log.debug("get successful, no instance found");
            } else {
                log.debug("get successful, instances found");
            }

            for (TemaFront temaFront : instances) {
                Hibernate.initialize(temaFront.getArchivoTemaFronts());
            }
            session.close();
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
	public TemaFront obtenerTemaFront(Long id) {

		log.debug("getting TemaFront instance with id: " + id);
        Session session = this.getSession();
		try {
			TemaFront instance = (TemaFront) session.get(TemaFront.class, id);
            if (instance != null) {
                Hibernate.initialize(instance.getPersonalizacionesPlantilla());
                Hibernate.initialize(instance.getArchivoTemaFronts());
            }
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
	 * Obtiene un TemaFront
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public TemaFront obtenerTemaFrontCompleto(Long id) {

		log.debug("getting TemaFront instance with id: " + id);
        Session session = this.getSession();
		try {
			TemaFront instance = (TemaFront) session.get(TemaFront.class, id);
            if (instance != null) {
                Hibernate.initialize(instance.getPersonalizacionesPlantilla());
                Hibernate.initialize(instance.getArchivoTemaFronts());
                Hibernate.initialize(instance.getTemasHijos());
                Hibernate.initialize(instance.getMicrosites());
            }
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
	 * Obtiene un Tema a partir de su URI.
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public TemaFront obtenerTemabyUri(String uri) {

		Session session = this.getSession();
		try {
			Criteria criteri = session.createCriteria(TemaFront.class);
			criteri.add(Restrictions.eq("uri", uri));
			TemaFront tema = (TemaFront) criteri.uniqueResult();
			return tema;

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

    

    private List<Long> extraerIdsPerPlantilla(List<PersonalizacionPlantilla> perPlantillas) {

        List<Long> ids = new ArrayList<Long>();
        for (PersonalizacionPlantilla pp : perPlantillas) {
            ids.add(pp.getId());
        }
        return ids;
    }

    private List<Long> extraerIdsObject(List objects) {

        List<Long> ids = new ArrayList<Long>();
        try {
            for (Object obj : objects) {
                Method m = obj.getClass().getMethod("getId");
                Long id = (Long) m.invoke(obj);
                ids.add(id);
            }

        } catch (NoSuchMethodException e) {
            ids = new ArrayList<Long>();
        } catch (InvocationTargetException e) {
            ids = new ArrayList<Long>();
        } catch (IllegalAccessException e) {
            ids = new ArrayList<Long>();
        }
        return ids;
    }

    /**
     * Lista todos los TemaFront padres
     *
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    @SuppressWarnings("unchecked")
    public List<TemaFront> listarTemaFrontPadres() {

        log.debug("listar TemaFront");
        try {
            Query query = getNamedQuery("es.caib.gusite.micromodel.TemaFront.listarTemaFrontPadres");
            return (List<TemaFront>) query.list();

        } catch (HibernateException re) {
            log.error("get failed", re);
            throw new EJBException(re);
        }
    }

}
