package org.ibit.rol.sac.micropersistence.ejb;

import java.util.List;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import net.sf.hibernate.*;

import org.ibit.rol.sac.micromodel.Contacto;
import org.ibit.rol.sac.micromodel.Lineadatocontacto;


/**
 * SessionBean para manipular Contacto.
 *
 * @ejb.bean
 *  name="sac/micropersistence/ContactoFacade"
 *  jndi-name="org.ibit.rol.sac.micropersistence.ContactoFacade"
 *  type="Stateless"
 *  view-type="remote"
 *  transaction-type="Container"
 *
 * @ejb.transaction type="Required"
 * 
 * @author Indra
 */

public abstract class ContactoFacadeEJB extends HibernateEJB {

	private static final long serialVersionUID = -6869856489116757129L;


	/**
     * @ejb.create-method
     * @ejb.permission unchecked="true"
     */
    public void ejbCreate() throws CreateException {
        super.ejbCreate();
    }

    /**
     * Inicializo los parámetros de la consulta de Contacto....
     * No está bien hecho debería ser Statefull
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
    public void init(Long site) {
    	super.tampagina=10;
    	super.pagina=0;
    	super.select="";
    	super.from=" from Contacto contacto ";
    	super.where="where contacto.idmicrosite="+site.toString();
    	super.whereini=" ";
    	super.orderby="";
    	super.camposfiltro= new String[] {"contacto.email","contacto.visible","contacto.anexarch"};
    	super.cursor=0;
    	super.nreg=0;
    	super.npags=0;	
    }


    /**
     * Inicializo los parámetros de la consulta de Contacto....
     * No está bien hecho debería ser Statefull
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
    public void init() {
    	super.tampagina=10;
    	super.pagina=0;
    	super.select="";
    	super.from=" from Contacto contacto ";
    	super.where="";
    	super.whereini=" ";
    	super.orderby="";
    	super.camposfiltro= new String[] {"contacto.email","contacto.visible","contacto.anexarch"};
    	super.cursor=0;
    	super.nreg=0;
    	super.npags=0;	
    }    
    
    /**
     * Crea o actualiza un Contacto
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
     */
    public Long grabarContacto(Contacto contacto) {
        Session session = getSession();
        try {
            Transaction tx = session.beginTransaction();
            session.saveOrUpdate(contacto);
            session.flush();
            tx.commit();
            return contacto.getId();
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }

    /**
     * Obtiene una linea del Formulario
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public Lineadatocontacto obtenerLinea(Long id) {
        Session session = getSession();
        try {
        	Lineadatocontacto linea = (Lineadatocontacto) session.load(Lineadatocontacto.class, id);
            return linea;
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }

    /**
     * Obtiene un Formulario
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public Contacto obtenerContacto(Long id) {
        Session session = getSession();
        try {
        	Contacto contacto = (Contacto) session.load(Contacto.class, id);
            return contacto;
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }

    
    /**
     * Lista todos los Formularios
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
    public List<?> listarContactos() {
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
     * borra un Formulario
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
     */
    public void borrarContacto(Long id) {
        Session session = getSession();
        try {
        	Contacto contacto = (Contacto) session.load(Contacto.class, id);
            session.delete(contacto);
            session.flush();
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }
    
    /**
     * Añade una nueva línea al formulario o modifica la que existe
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
     */
    public void creamodificaLinea(Lineadatocontacto lin, Long idcontacto) {
        Session session = getSession();
        try {
        	// Alta linea
        	if (lin.getId()==null) {
            	Contacto contacto = (Contacto) session.load(Contacto.class, idcontacto);
        		contacto.getLineasdatocontacto().add(lin);
            	session.saveOrUpdate(contacto);
        	}
        	// Modificacion linea
            else {
            	session.saveOrUpdate(lin);		
            }
            session.flush();
        } catch (HibernateException e) {
            throw new EJBException(e);
        } finally {
            close(session);
        }
    }

    /**
     * elimina líneas del formulario
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
     */
    public void eliminarLineas(String[] lineas, Long contacto_id) {
        Session session = getSession();
        try {
         	Contacto contacto = (Contacto) session.load(Contacto.class, contacto_id);
        	Lineadatocontacto lin;
        	
        	for (int i=0;i<lineas.length;i++) {
        		lin = (Lineadatocontacto) session.load(Lineadatocontacto.class, new Long (lineas[i]));
            	contacto.getLineasdatocontacto().remove(lin);
            	session.delete(lin);
        	}
        	session.saveOrUpdate(contacto);
            session.flush();
            
        } catch (HibernateException e) {
            throw new EJBException(e);
        } finally {
            close(session);
        }
    }
    
    /**
     * elimina una línea del formulario
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
     */
    public void eliminarLinea(Long idLinea, Long idContacto) {
        Session session = getSession();
        try {
         	Contacto contacto = (Contacto) session.load(Contacto.class, idContacto);
        	Lineadatocontacto lin;
        	
        	lin = (Lineadatocontacto) session.load(Lineadatocontacto.class, idLinea);
            contacto.getLineasdatocontacto().remove(lin);
            session.delete(lin);
        	session.saveOrUpdate(contacto);
            session.flush();
            
        } catch (HibernateException e) {
            throw new EJBException(e);
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
        	Query query = session.createQuery("from Contacto con where con.idmicrosite="+site.toString()+" and con.id="+id.toString());
        	return query.list().isEmpty();
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }
    
}