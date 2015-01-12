package org.ibit.rol.sac.micropersistence.ejb;

import java.util.List;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import net.sf.hibernate.*;

import org.ibit.rol.sac.micromodel.Componente;
import org.ibit.rol.sac.micromodel.Idioma;

/**
 * SessionBean para consultar Componente.
 *
 * @ejb.bean
 *  name="sac/micropersistence/ComponenteFacade"
 *  jndi-name="org.ibit.rol.sac.micropersistence.ComponenteFacade"
 *  type="Stateless"
 *  view-type="remote"
 *  transaction-type="Container"
 *
 * @ejb.transaction type="Required"
 * 
 * @author Indra
 */
public abstract class ComponenteFacadeEJB extends HibernateEJB
{

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
    	super.tampagina=10;
    	super.pagina=0;
    	super.select="";
    	super.from=" from Componente compo join compo.traducciones trad ";
    	super.where=" where index(trad)='"+Idioma.DEFAULT+"' and compo.idmicrosite="+site.toString();
    	super.whereini=" ";
    	super.orderby="";

    	super.camposfiltro= new String[] {"compo.nombre", "trad.titulo"};
    	super.cursor=0;
    	super.nreg=0;
    	super.npags=0;	
    }  

    /**
     * Inicializo los parámetros de la consulta de Componente.... 
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
    public void init() {
    	super.tampagina=10;
    	super.pagina=0;
    	super.select="";
    	super.from=" from Componente compo join compo.traducciones trad ";
    	super.where=" where index(trad)='"+Idioma.DEFAULT+"'";
    	super.whereini=" ";
    	super.orderby="";

    	super.camposfiltro= new String[] {"compo.nombre", "trad.titulo"};
    	super.cursor=0;
    	super.nreg=0;
    	super.npags=0;
    }      
    
    /**
     * Crea o actualiza un Componente
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
     */
    public Long grabarComponente(Componente compo) {
        Session session = getSession();
        try {
            Transaction tx = session.beginTransaction();
            session.saveOrUpdate(compo);
            session.flush();
            tx.commit();
            return compo.getId();
        } catch (HibernateException he) {
            throw new EJBException(he);
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
        	Componente compo = (Componente) session.load(Componente.class, id);
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
     * borra un Componente
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
     */
    public void borrarComponente(Long id) {
        Session session = getSession();
        try {
        	Transaction tx = session.beginTransaction();
        	Componente compo = (Componente) session.load(Componente.class, id);
            session.delete(compo);
            session.flush();
            tx.commit();
        } catch (HibernateException he) {
            throw new EJBException(he);
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
        	Query query = session.createQuery("from Componente compo where compo.idmicrosite="+site.toString()+" and compo.id="+id.toString());
        	return query.list().isEmpty();
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }

}