package org.ibit.rol.sac.micropersistence.ejb;

import java.util.List;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import net.sf.hibernate.*;

import org.ibit.rol.sac.micromodel.Idioma;
import org.ibit.rol.sac.micromodel.Temafaq;

/**
 * SessionBean para manipular los temas de las FAQS.
 *
 * @ejb.bean
 *  name="sac/micropersistence/TemaFacade"
 *  jndi-name="org.ibit.rol.sac.micropersistence.TemaFacade"
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
    	super.tampagina=10;
    	super.pagina=0;
    	super.select="";
    	super.from=" from Temafaq tema join tema.traducciones trad ";
    	super.where=" where index(trad)='"+Idioma.DEFAULT+"' and tema.idmicrosite="+site.toString();
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
    	super.from=" from Temafaq tema join tema.traducciones trad ";
    	super.where=" where index(trad)='"+Idioma.DEFAULT+"'";
    	super.whereini=" ";
    	super.orderby="";

    	super.camposfiltro= new String[] {"trad.nombre"};
    	super.cursor=0;
    	super.nreg=0;
    	super.npags=0;	
    }    
    
    /**
     * Crea o actualiza un tema
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
     */
    public Long grabarTema(Temafaq tema) {
        Session session = getSession();
        try {
            Transaction tx = session.beginTransaction();
            session.saveOrUpdate(tema);
            session.flush();
            tx.commit();
            return tema.getId();
        } catch (HibernateException he) {
            throw new EJBException(he);
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
        	Temafaq tema = (Temafaq) session.load(Temafaq.class, id);
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
     * borra un tema
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
     */
    public void borrarTema(Long id) {
        Session session = getSession();
        try {
        	Temafaq tema = (Temafaq) session.load(Temafaq.class, id);
            session.delete(tema);
            session.flush();
        } catch (HibernateException he) {
            throw new EJBException(he);
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
        	Query query = session.createQuery("from Temafaq tema join tema.traducciones trad where index(trad)='"+Idioma.DEFAULT+"' and tema.idmicrosite=" + idmicrosite.toString());
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
        	Query query = session.createQuery("from Temafaq tema where tema.idmicrosite="+site.toString()+" and tema.id="+id.toString());
        	return query.list().isEmpty();
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }
}