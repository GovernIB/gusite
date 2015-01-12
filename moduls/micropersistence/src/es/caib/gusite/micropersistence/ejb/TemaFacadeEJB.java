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
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * SessionBean para manipular los temas de las FAQS.
 *
 * @ejb.bean
 *  name="sac/micropersistence/TemaFacade"
 *  jndi-name="es.caib.gusite.micropersistence.TemaFacade"
 *  type="Stateless"
 *  view-type="remote"
 *  transaction-type="Container"
 *
 * @ejb.transaction type="Required"
 * 
 * @author Indra
 */
public abstract class TemaFacadeEJB extends HibernateEJB {

	private static final long serialVersionUID = 7011958120088531376L;

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
    	super.tampagina = 10;
    	super.pagina = 0;
    	super.select = "select tema";
    	super.from = " from Temafaq tema join tema.traducciones trad ";
    	super.where = " where trad.id.codigoIdioma='" + Idioma.getIdiomaPorDefecto() + "' and tema.idmicrosite=" + site.toString();
    	super.whereini = " ";
    	super.orderby = "";

    	super.camposfiltro = new String[] {"trad.nombre"};
    	super.cursor = 0;
    	super.nreg = 0;
    	super.npags = 0;
    }

    /**
     * Inicializo los parámetros de la consulta....
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
    public void init() {
    	super.tampagina = 10;
    	super.pagina = 0;
    	super.select = "select tema";
    	super.from = " from Temafaq tema join tema.traducciones trad ";
    	super.where = " where trad.id.codigoIdioma='" + Idioma.getIdiomaPorDefecto() + "'";
    	super.whereini = " ";
    	super.orderby = "";

    	super.camposfiltro = new String[] {"trad.nombre"};
    	super.cursor = 0;
    	super.nreg = 0;
    	super.npags = 0;
    }    
    
    /**
     * Crea o actualiza un tema
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
     */
    public Long grabarTema(Temafaq tema) {

        Session session = getSession();
        try {
            boolean nuevo = (tema.getId() == null) ? true : false;
            Transaction tx = session.beginTransaction();
            Microsite site = (Microsite) session.get(Microsite.class, tema.getIdmicrosite());

            Map<String, TraduccionTemafaq> listaTraducciones = new HashMap<String, TraduccionTemafaq>();
            if (nuevo) {
                Iterator<TraduccionTemafaq> it = tema.getTraducciones().values().iterator();
                while (it.hasNext()) {
                    TraduccionTemafaq trd = it.next();
                    listaTraducciones.put(trd.getId().getCodigoIdioma(), trd);
                }
                tema.setTraducciones(null);
            }

            session.saveOrUpdate(tema);
            session.flush();

            if (nuevo) {
                for (TraduccionTemafaq trad : listaTraducciones.values()) {
                    trad.getId().setCodigoTema(tema.getId());
                    session.saveOrUpdate(trad);
                }
                session.flush();
                tema.setTraducciones(listaTraducciones);
            }

            tx.commit();
            close(session);

            Auditoria auditoria = new Auditoria();
            auditoria.setEntidad(Temafaq.class.getSimpleName());
            auditoria.setIdEntidad(tema.getId().toString());
            auditoria.setMicrosite(site);
            int op = (nuevo) ? Auditoria.CREAR : Auditoria.MODIFICAR;
            auditoria.setOperacion(op);
            DelegateUtil.getAuditoriaDelegate().grabarAuditoria(auditoria);

            return tema.getId();

        } catch (HibernateException he) {
            throw new EJBException(he);
        } catch (DelegateException e) {
            throw new EJBException(e);
        } finally {
            close(session);
        }
    }

    /**
     * Obtiene un tema
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public Temafaq obtenerTema(Long id) {

        Session session = getSession();
        try {
        	Temafaq tema = (Temafaq) session.get(Temafaq.class, id);
            return tema;

        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }

    /**
     * Lista todos los temas
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
    public List<?> listarTemas() {

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
     * borra un tema
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
     */
    public void borrarTema(Long id) {

        Session session = getSession();
        try {
            Temafaq temafaq = (Temafaq) session.get(Temafaq.class, id);
            Microsite site = (Microsite) session.get(Microsite.class, temafaq.getIdmicrosite());

        	session.createQuery("delete from TraduccionTemafaq ttema where ttema.id.codigoTema = " + id).executeUpdate();
        	session.createQuery("delete from Temafaq tfaq where tfaq.id = " + id).executeUpdate();
            session.flush();
            close(session);

            Auditoria auditoria = new Auditoria();
            auditoria.setEntidad(Actividadagenda.class.getSimpleName());
            auditoria.setIdEntidad(id.toString());
            auditoria.setMicrosite(site);
            auditoria.setOperacion(Auditoria.ELIMINAR);
            DelegateUtil.getAuditoriaDelegate().grabarAuditoria(auditoria);

        } catch (HibernateException he) {
            throw new EJBException(he);
        } catch (DelegateException e) {
            throw new EJBException(e);
        } finally {
            close(session);
        }
    }
    
    /**
     * Lista todos los temas para usar en Combos
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
    public List<?> listarCombo(Long idmicrosite) {

        Session session = getSession();
        try {
            String consulta = "select tema from Temafaq tema join tema.traducciones trad where trad.id.codigoIdioma = '" + Idioma.getIdiomaPorDefecto() + "' and tema.idmicrosite = " + idmicrosite.toString();
        	Query query = session.createQuery(consulta.toString());
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
        	Query query = session.createQuery("select tema from Temafaq tema where tema.idmicrosite = " + site.toString() + " and tema.id = " + id.toString());
        	return query.list().isEmpty();

        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }

}
