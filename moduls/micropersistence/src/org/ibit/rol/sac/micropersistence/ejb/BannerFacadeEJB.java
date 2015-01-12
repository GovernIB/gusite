package org.ibit.rol.sac.micropersistence.ejb;

import java.util.List;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import net.sf.hibernate.*;

import org.ibit.rol.sac.micromodel.Banner;
import org.ibit.rol.sac.micromodel.Idioma;

/**
 * SessionBean para consultar Banner.
 *
 * @ejb.bean
 *  name="sac/micropersistence/BannerFacade"
 *  jndi-name="org.ibit.rol.sac.micropersistence.BannerFacade"
 *  type="Stateless"
 *  view-type="remote"
 *  transaction-type="Container"
 *
 * @ejb.transaction type="Required"
 * 
 * @author Indra
 */
public abstract class BannerFacadeEJB extends HibernateEJB
{

	private static final long serialVersionUID = 1883818416695018254L;

	/**
     * @ejb.create-method
     * @ejb.permission unchecked="true"
     */
    public void ejbCreate() throws CreateException {
        super.ejbCreate();
    }

    /**
     * Inicializo los parámetros de la consulta de Banner.... 
     * No está bien hecho debería ser Statefull
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
    public void init(Long site) {
    	super.tampagina=10;
    	super.pagina=0;
    	super.select="";
    	super.from=" from Banner banner join banner.traducciones trad ";
    	super.where=" where index(trad)='"+Idioma.DEFAULT+"' and banner.idmicrosite="+site.toString();
    	super.whereini=" ";
    	super.orderby="";

    	super.camposfiltro= new String[] {"banner.fpublicacion", "banner.fcaducidad", "trad.titulo", "trad.url", "trad.alt"};
    	super.cursor=0;
    	super.nreg=0;
    	super.npags=0;	
    }  

    /**
     * Inicializo los parámetros de la consulta de Banner.... 
     * No está bien hecho debería ser Statefull
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
    public void init() {
    	super.tampagina=10;
    	super.pagina=0;
    	super.select="";
    	super.from=" from Banner banner join banner.traducciones trad ";
    	super.where=" where index(trad)='"+Idioma.DEFAULT+"'";
    	super.whereini=" ";
    	super.orderby="";

    	super.camposfiltro= new String[] {"banner.fpublicacion", "banner.fcaducidad", "trad.titulo", "trad.url", "trad.alt"};
    	super.cursor=0;
    	super.nreg=0;
    	super.npags=0;	
    }      
    
    /**
     * Crea o actualiza un Banner
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
     */
    public Long grabarBanner(Banner banner) {
        Session session = getSession();
        try {
            Transaction tx = session.beginTransaction();
            session.saveOrUpdate(banner);
            session.flush();
            tx.commit();
            return banner.getId();
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }

    /**
     * Obtiene un Banner
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public Banner obtenerBanner(Long id) {
        Session session = getSession();
        try {
        	Banner banner = (Banner) session.load(Banner.class, id);
            return banner;
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }

    /**
     * Lista todos los Banners
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
    public List<?> listarBanners() {
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
     * Borra un Banner
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
     */
    public void borrarBanner(Long id) {
        Session session = getSession();
        try {
        	Transaction tx = session.beginTransaction();
        	Banner banner = (Banner) session.load(Banner.class, id);
            session.delete(banner);
            session.flush();
            tx.commit();
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
        	Query query = session.createQuery("from Banner ban where ban.idmicrosite="+site.toString()+" and ban.id="+id.toString());
        	return query.list().isEmpty();
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }

}