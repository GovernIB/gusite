package es.caib.gusite.micropersistence.ejb;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;

import es.caib.gusite.micromodel.*;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.Session;

/**
 * SessionBean para consultar Componente.
 *
 * @ejb.bean
 *  name="sac/micropersistence/ComponenteFacade"
 *  jndi-name="es.caib.gusite.micropersistence.ComponenteFacade"
 *  type="Stateless"
 *  view-type="remote"
 *  transaction-type="Container"
 *
 * @ejb.transaction type="Required"
 * 
 * @author Indra
 */
public abstract class ComponenteFacadeEJB extends HibernateEJB {

	private static final long serialVersionUID = -1492166558649126596L;

	/**
     * @ejb.create-method
     * @ejb.permission unchecked="true"
     */
    public void ejbCreate() throws CreateException {
        super.ejbCreate();
    }

    /**
     * Inicializo los parámetros de la consulta de Componente.... 
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
    public void init(Long site) {
    	super.tampagina = 10;
    	super.pagina = 0;
    	super.select = "";
    	super.from = " from Componente compo join compo.traducciones trad ";
    	super.where = " where trad.id.codigoIdioma = '" + Idioma.getIdiomaPorDefecto() + "' and compo.idmicrosite = " + site.toString();
    	super.whereini = " ";
    	super.orderby = "";

    	super.camposfiltro = new String[] {"compo.nombre", "trad.titulo"};
    	super.cursor = 0;
    	super.nreg = 0;
    	super.npags = 0;
    }  

    /**
     * Inicializo los parámetros de la consulta de Componente.... 
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
    public void init() {
    	super.tampagina = 10;
    	super.pagina = 0;
    	super.select = "";
    	super.from = " from Componente compo join compo.traducciones trad ";
    	super.where = " where trad.id.codigoIdioma = '" + Idioma.getIdiomaPorDefecto() + "'";
    	super.whereini = " ";
    	super.orderby = "";

    	super.camposfiltro = new String[] {"compo.nombre", "trad.titulo"};
    	super.cursor = 0;
    	super.nreg = 0;
    	super.npags = 0;
    }      
    
    /**
     * Crea o actualiza un Componente
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
     */
    public Long grabarComponente(Componente compo) throws DelegateException {

        Session session = getSession();
        Boolean nuevo = false;
        try {
            Transaction tx = session.beginTransaction();
            if (compo.getId() == null) {
                nuevo = true;
            }

            Map<String, TraduccionComponente> listaTraducciones = new HashMap<String, TraduccionComponente>();
            if (nuevo) {
                Iterator<TraduccionComponente> it = compo.getTraducciones().values().iterator();
                while (it.hasNext()) {
                    TraduccionComponente trd = it.next();
             		listaTraducciones.put(trd.getId().getCodigoIdioma(), trd);
             	}
                compo.setTraducciones(null);
            }

            session.saveOrUpdate(compo);
            session.flush();

            if (nuevo) {
                for (TraduccionComponente trad : listaTraducciones.values()) {
                    trad.getId().setCodigoComponente(compo.getId());
                    session.saveOrUpdate(trad);
                }
                session.flush();
                compo.setTraducciones(listaTraducciones);
            }

            tx.commit();
            Microsite site = (Microsite) session.get(Microsite.class, compo.getIdmicrosite());
            close(session);

            Auditoria auditoria = new Auditoria();
            auditoria.setEntidad(Componente.class.getSimpleName());
            auditoria.setIdEntidad(compo.getId().toString());
            auditoria.setMicrosite(site);
            int op = (nuevo) ? Auditoria.CREAR : Auditoria.MODIFICAR;
            auditoria.setOperacion(op);
            DelegateUtil.getAuditoriaDelegate().grabarAuditoria(auditoria);

            return compo.getId();

        } catch (HibernateException he) {
            throw new EJBException(he);
        } catch (DelegateException e) {
            throw new DelegateException(e);
        } finally {
            close(session);
        }
    }

    /**
     * Obtiene un Componente
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public Componente obtenerComponente(Long id) {

        Session session = getSession();
        try {
        	Componente compo = (Componente) session.get(Componente.class, id);
            return compo;

        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }

    /**
     * Lista todos los Componentes
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
    public List<?> listarComponentes() {

        Session session = getSession();
        try {
        	parametrosCons(); // Establecemos los parámetros de la paginación
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

    /**
     * borra un Componente
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
     */
    public void borrarComponente(Long id) throws DelegateException {

        Session session = getSession();
        try {
            Transaction tx = session.beginTransaction();
            Componente componente = (Componente) session.get(Componente.class, id);
            Microsite site = (Microsite) session.get(Microsite.class, componente.getIdmicrosite());

        	session.createQuery("delete from TraduccionComponente tc where tc.id.codigoComponente = " + id).executeUpdate();
        	session.createQuery("delete from Componente compo where compo.id = " + id).executeUpdate();
            session.flush();
            tx.commit();
            close(session);

            Auditoria auditoria = new Auditoria();
            auditoria.setEntidad(Componente.class.getSimpleName());
            auditoria.setIdEntidad(id.toString());
            auditoria.setMicrosite(site);
            auditoria.setOperacion(Auditoria.ELIMINAR);
            DelegateUtil.getAuditoriaDelegate().grabarAuditoria(auditoria);

        } catch (HibernateException he) {
            throw new EJBException(he);
        } catch (DelegateException e) {
            throw new DelegateException(e);
        } finally {
            close(session);
        }
    }

    /**
     * Comprueba que el componente pertenece al Microsite
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
     */
    public boolean checkSite(Long site, Long id) {

        Session session = getSession();
        try {
        	Query query = session.createQuery("from Componente compo where compo.idmicrosite = " + site.toString() + " and compo.id = " + id.toString());
        	return query.list().isEmpty();

        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }

}
