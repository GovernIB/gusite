package org.ibit.rol.sac.micropersistence.ejb;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import net.sf.hibernate.*;

import org.ibit.rol.sac.micromodel.Idioma;
import org.ibit.rol.sac.micromodel.Frqssi;
import org.ibit.rol.sac.micromodel.TraduccionFrqssi;

/**
 * SessionBean para manipular los Formularios QSSI
 *
 * @ejb.bean
 *  name="sac/micropersistence/FrqssiFacade"
 *  jndi-name="org.ibit.rol.sac.micropersistence.FrqssiFacade"
 *  type="Stateless"
 *  view-type="remote"
 *  transaction-type="Container"
 *
 * @ejb.transaction type="Required"
 * 
 * @author Indra
 */

public abstract class FrqssiFacadeEJB extends HibernateEJB {

	private static final long serialVersionUID = 131057491326620369L;

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
    	super.from=" from Frqssi frqssi join frqssi.traducciones trad ";
    	super.where=" where index(trad)='"+Idioma.DEFAULT+"' and frqssi.idmicrosite="+site.toString();
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
    public void init(Long site,String idiomapasado) {
    	super.tampagina=10;
    	super.pagina=0;
    	super.select="select frqssi.id,trad.nombre ";
    	super.from=" from Frqssi frqssi join frqssi.traducciones trad ";
    	super.where=" where (index(trad)='"+Idioma.DEFAULT+"' or index(trad)='"+idiomapasado+"') and frqssi.idmicrosite="+site.toString();
    	super.whereini=" ";
    	super.orderby=" order by frqssi.id, index(trad) desc";

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
    	super.from=" from Frqssi frqssi join frqssi.traducciones trad ";
    	super.where=" where index(trad)='"+Idioma.DEFAULT+"'";
    	super.whereini=" ";
    	super.orderby="";

    	super.camposfiltro= new String[] {"trad.nombre"};
    	super.cursor=0;
    	super.nreg=0;
    	super.npags=0;	
    }    
    
    /**
     * Crea o actualiza un Formulario QSSI
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
     */
    public Long grabarFrqssi(Frqssi frqssi) {
        Session session = getSession();
        try {
            Transaction tx = session.beginTransaction();
            session.saveOrUpdate(frqssi);
            session.flush();
            tx.commit();
            return frqssi.getId();
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }

    /**
     * Obtiene un Formulario QSSI
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public Frqssi obtenerFrqssi(Long id) {
        Session session = getSession();
        try {
        	Frqssi frqssi = (Frqssi) session.load(Frqssi.class, id);
            return frqssi;
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }

    /**
     * Lista todos los Formularios QSSI
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
    public List<?> listarFrqssis() {
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
     * Lista todos los Formularios QSSI
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
    public List<Frqssi> listarFrqssisrec(String idiomapasado) {
        Session session = getSession();
      
        try {
        	parametrosCons(); // Establecemos los parámetros de la paginación
        	Query query = session.createQuery(select+from+where+orderby);
        	ScrollableResults scr = query.scroll();
        	ArrayList<Frqssi> lista= new ArrayList<Frqssi>();        	
        	scr.first();
        	scr.scroll(cursor-1);
        	int i = 0;
            while (tampagina > i++) {
            Object[] fila = (Object[]) scr.get();
            
           	Frqssi qss = new Frqssi();
           	qss.setId((Long)fila[0]);
           	TraduccionFrqssi tradqss = new TraduccionFrqssi();
           	tradqss.setNombre((String)fila[1]);
          	qss.setTraduccion(idiomapasado,tradqss);
          	lista.add(qss);
          	if (!scr.next()) break;
            }
        	scr.close();
        	return lista;
        	  
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }



    /**
     * borra un Formulario QSSI
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
     */
    public void borrarFrqssi(Long id) {
        Session session = getSession();
        try {
        	Frqssi frqssi = (Frqssi) session.load(Frqssi.class, id);
            session.delete(frqssi);
            session.flush();
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }
    
   /**
     * Lista todos los Formularios QSSI para usar en Combos
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
    public List<?> listarCombo(Long idmicrosite) {
        Session session = getSession();
        try {
        	Query query = session.createQuery("from Frqssi frqssi join frqssi.traducciones trad where index(trad)='"+Idioma.DEFAULT+"' and frqssi.idmicrosite=" + idmicrosite.toString());
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
        	Query query = session.createQuery("from Frqssi frqssi where frqssi.idmicrosite="+site.toString()+" and frqssi.id="+id.toString());
        	return query.list().isEmpty();
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }
    
}