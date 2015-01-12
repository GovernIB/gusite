package org.ibit.rol.sac.micropersistence.ejb;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import net.sf.hibernate.*;
import net.sf.hibernate.expression.Expression;

import org.ibit.rol.sac.micromodel.MProcedimiento;

/**
 * SessionBean para manipular los procedimientos asociados a los microsites
 *
 * @ejb.bean
 *  name="sac/micropersistence/MProcedimientoFacade"
 *  jndi-name="org.ibit.rol.sac.micropersistence.MProcedimientoFacade"
 *  type="Stateless"
 *  view-type="remote"
 *  transaction-type="Container"
 *
 * @ejb.transaction type="Required"
 */

public abstract class MProcedimientoFacadeEJB extends HibernateEJB {

	private static final long serialVersionUID = 3893196270507040068L;

	/**
     * @ejb.create-method
     * @ejb.permission unchecked="true"
     */
    public void ejbCreate() throws CreateException {
        super.ejbCreate();
    }

    
    /**
     * Crea o actualiza un MProcedimiento
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
     */
    public Long grabarMProcedimiento(MProcedimiento mproc) {
        Session session = getSession();
        try {
            Transaction tx = session.beginTransaction();
            session.saveOrUpdate(mproc);
            session.flush();
            tx.commit();
            return mproc.getId();
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }

    /**
     * Obtiene un MProcedimiento
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public MProcedimiento obtenerMProcedimiento(Long id) {
        Session session = getSession();
        try {
        	MProcedimiento mproc = (MProcedimiento) session.load(MProcedimiento.class, id);
            return mproc;
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }

    /**
     * Obtiene un MProcedimiento de un microsite
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public MProcedimiento obtenerMProcedimientobyMic(Long idmicrosite) {
        Session session = getSession();
        try {
            Criteria criteri = session.createCriteria(MProcedimiento.class);
            criteri.add(Expression.eq("idmicrosite", idmicrosite));
            MProcedimiento proc = (MProcedimiento)criteri.uniqueResult();
            return proc;
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }

    /**
     * borra un tipo de noticia
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
     */
    public void borrarMProcedimiento(Long id) {
        Session session = getSession();
        try {
        	MProcedimiento mproc = (MProcedimiento) session.load(MProcedimiento.class, id);
            session.delete(mproc);
            session.flush();
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }
    

    
}