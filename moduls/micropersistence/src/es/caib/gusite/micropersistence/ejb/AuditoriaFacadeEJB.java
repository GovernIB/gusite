package es.caib.gusite.micropersistence.ejb;

import es.caib.gusite.micromodel.*;
import org.hibernate.*;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import java.util.*;

/**
 * SessionBean para consultar Auditorias.
 *
 * @ejb.bean
 * name="sac/micropersistence/AuditoriaFacade"
 * jndi-name="es.caib.gusite.micropersistence.AuditoriaFacade"
 * type="Stateless"
 * view-type="remote"
 * transaction-type="Container"
 *
 * @ejb.transaction type="Required"
 *
 * Created by tcerda on 10/12/2014.
 */
public abstract class AuditoriaFacadeEJB extends HibernateEJB {

    /**
     * @ejb.create-method
     * @ejb.permission unchecked="true"
     */
    public void ejbCreate() throws CreateException {
        super.ejbCreate();
    }

    /**
     * Crea o actualiza una Auditoria
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
     */
    public Long grabarAuditoria(Auditoria auditoria) {

        Session session = getSession();
        try {
            Transaction tx = session.beginTransaction();
            auditoria.setFecha(new Date());
            auditoria.setUsuario(this.getUsuario(session).getUsername());
            session.saveOrUpdate(auditoria);
            session.flush();
            tx.commit();
            return auditoria.getId();

        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }

    /**
     * Listar auditorias por entidad
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public List<Auditoria> listarAuditoriasPorEntidad(String entidad) {

        Session session = getSession();
        try {
//            parametrosCons(); // Establecemos los parámetros de la paginación
//            String hql = "";
//            Query query = session.createQuery(hql);
//            query.setFirstResult(cursor - 1);
//            query.setMaxResults(tampagina);
//            return query.list();

            Criteria criteria = session.createCriteria(Auditoria.class);
            criteria.add(Expression.like("entidad", entidad));
            criteria.addOrder(Order.desc("fecha"));
            return criteria.list();

        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }

    /**
     * Listar auditorias por id entidad
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public List<Auditoria> listarAuditoriasPorIdEntidad(String idEntidad) {

        Session session = getSession();
        try {
//            parametrosCons(); // Establecemos los parámetros de la paginación
//            String hql = "";
//            Query query = session.createQuery(hql);
//            query.setFirstResult(cursor - 1);
//            query.setMaxResults(tampagina);
//            return query.list();

            Criteria criteria = session.createCriteria(Auditoria.class);
            criteria.add(Expression.eq("idEntidad", idEntidad));
            criteria.addOrder(Order.desc("fecha"));
            return criteria.list();

        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }

    /**
     * Listar auditorias por usuario
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public List<Auditoria> listarAuditoriasPorUsuario(String usuario) {

        Session session = getSession();
        try {
//            parametrosCons(); // Establecemos los parámetros de la paginación
//            String hql = "";
//            Query query = session.createQuery(hql);
//            query.setFirstResult(cursor - 1);
//            query.setMaxResults(tampagina);
//            return query.list();

            Criteria criteria = session.createCriteria(Auditoria.class);
            criteria.add(Expression.like("usuario", usuario));
            criteria.addOrder(Order.desc("fecha"));
            return criteria.list();

        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }

}
