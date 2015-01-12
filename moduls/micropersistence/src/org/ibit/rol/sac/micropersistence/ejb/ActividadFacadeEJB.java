package org.ibit.rol.sac.micropersistence.ejb;

import java.util.List;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import net.sf.hibernate.*;

import org.ibit.rol.sac.micromodel.Idioma;
import org.ibit.rol.sac.micromodel.Actividadagenda;

/**
 * SessionBean para manipular las actividades de la agenda.
 *
 * @ejb.bean
 *  name="sac/micropersistence/ActividadFacade"
 *  jndi-name="org.ibit.rol.sac.micropersistence.ActividadFacade"
 *  type="Stateless"
 *  view-type="remote"
 *  transaction-type="Container"
 *
 * @ejb.transaction type="Required"
 * 
 * @author Indra
 */

public abstract class ActividadFacadeEJB extends HibernateEJB {

	private static final long serialVersionUID = -2032209327451288969L;

	/**
     * @ejb.create-method
     * @ejb.permission unchecked="true"
     */
    public void ejbCreate() throws CreateException {
        super.ejbCreate();
    }

    /**
     * Inicializo los parámetros de la consulta....
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public void init(Long site) {
    	super.tampagina=10;
    	super.pagina=0;
    	super.select="";
    	super.from=" from Actividadagenda activi join activi.traducciones trad ";
    	super.where=" where index(trad)='"+Idioma.DEFAULT+"' and activi.idmicrosite="+site.toString();
    	super.whereini=" ";
    	super.orderby="";

    	super.camposfiltro= new String[] {"trad.nombre"};
    	super.cursor=0;
    	super.nreg=0;
    	super.npags=0;	
    }

    /**
     * Inicializo los parámetros de la consulta....
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
    public void init() {
    	super.tampagina=10;
    	super.pagina=0;
    	super.select="";
    	super.from=" from Actividadagenda activi join activi.traducciones trad ";
    	super.where=" where index(trad)='"+Idioma.DEFAULT+"'";
    	super.whereini=" ";
    	super.orderby="";

    	super.camposfiltro= new String[] {"trad.nombre"};
    	super.cursor=0;
    	super.nreg=0;
    	super.npags=0;	
    }    
    
    /**
     * Crea o actualiza una actividad
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
     */
    public Long grabarActividad(Actividadagenda activi) {
        Session session = getSession();
        try {
            Transaction tx = session.beginTransaction();
            session.saveOrUpdate(activi);
            session.flush();
            tx.commit();
            return activi.getId();
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }

    /**
     * Obtiene una Actividad
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public Actividadagenda obtenerActividad(Long id) {
        Session session = getSession();
        try {
        	Actividadagenda activi = (Actividadagenda) session.load(Actividadagenda.class, id);
            return activi;
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }

    /**
     * Lista todas las actividades
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
    public List<?> listarActividades() {
        Session session = getSession();
        try {
        	parametrosCons(); // Establecemos los parámetros de la paginación
       	
        	Query query = session.createQuery(select+from+where+orderby);
            query.setFirstResult(cursor-1);
            query.setMaxResults(tampagina);
        	return query.list();
        	
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }
 
    /**
     * Borra una actividad
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
     */
    public void borrarActividad(Long id) {
        Session session = getSession();
        try {
        	Actividadagenda activi = (Actividadagenda) session.load(Actividadagenda.class, id);
            session.delete(activi);
            session.flush();
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }
    
     /**
     * Lista todas las actividades de los eventos de la agenda para usar en Combos
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
    public List<?> listarCombo(Long idmicrosite) {
        Session session = getSession();
        try {
        	Query query = session.createQuery("from Actividadagenda activi join activi.traducciones trad where index(trad)='"+Idioma.DEFAULT+"' and activi.idmicrosite=" + idmicrosite.toString());
        	return query.list();
        	
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }
    
    /**
     * Comprueba que el elemento pertenece al Microsite
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
     */
    public boolean checkSite(Long site, Long id) {
        Session session = getSession();
        try {
        	Query query = session.createQuery("from Actividadagenda activi where activi.idmicrosite="+site.toString()+" and activi.id="+id.toString());
        	return query.list().isEmpty();
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }
    
    
}