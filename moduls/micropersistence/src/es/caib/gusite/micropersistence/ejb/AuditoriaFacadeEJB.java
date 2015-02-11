package es.caib.gusite.micropersistence.ejb;

import es.caib.gusite.micromodel.*;
import org.hibernate.*;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import java.text.SimpleDateFormat;
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
     * Inicializo los parámetros de la consulta de una auditoria...
     *
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public void init() {

        super.tampagina = 10;
        super.pagina = 0;
        super.select = "select aud ";
        super.from = " from Auditoria aud ";
        super.where = "";
        super.whereini = " ";
        super.orderby = " order by aud.fecha desc";

        super.camposfiltro = new String[]{"aud.entidad", "aud.idEntidad", "aud.usuario"};
        super.cursor = 0;
        super.nreg = 0;
        super.npags = 0;
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
    public List<Auditoria> listarAuditorias(String entity, String idEntity, String user, Date date, String micro) {

        Session session = getSession();
        try {
            if (entity != null && entity != "") {
                where += " where aud.entidad = '" + entity + "'";
            }
            if (idEntity != null && idEntity != "") {
                where += (where == "") ? " where" : " and";
                where += " aud.idEntidad = " + idEntity;
            }
            if (user != null && user != "") {
                where += (where == "") ? " where" : " and";
                where += " aud.usuario = '" + user + "'";
            }
            if (date != null) {

                Calendar c = Calendar.getInstance();
                c.setTime(date);
                c.add(Calendar.DATE, 1);

                where += (where == "") ? " where" : " and";
                where += " aud.fecha between to_date('" + new SimpleDateFormat("dd/mm/yyyy").format(date) + "', 'dd/mm/yyyy') and to_date('" + new SimpleDateFormat("dd/mm/yyyy").format(c.getTime()) + "', 'dd/mm/yyyy')" ;
            }
            if (micro != null && micro != "") {
                where += (where == "") ? " where" : " and";
                where += " aud.microsite.id = " + micro;
            }

            parametrosCons();
            Query query = session.createQuery(select + from + where + orderby);
            query.setFirstResult(cursor - 1);
            query.setMaxResults(tampagina);
            return query.list();

        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }

}
