package es.caib.gusite.micropersistence.ejb;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;

import es.caib.gusite.micromodel.*;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.Session;

/**
 * SessionBean para manipular Contacto.
 *
 * @ejb.bean
 *  name="sac/micropersistence/ContactoFacade"
 *  jndi-name="es.caib.gusite.micropersistence.ContactoFacade"
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
    	super.tampagina = 10;
    	super.pagina = 0;
    	super.select = "";
    	super.from = " from Contacto contacto ";
    	super.where = "where contacto.idmicrosite = " + site.toString();
    	super.whereini = " ";
    	super.orderby = "";
    	super.camposfiltro = new String[] {"contacto.email","contacto.visible","contacto.anexarch"};
    	super.cursor = 0;
    	super.nreg = 0;
    	super.npags = 0;
    }

    /**
     * Inicializo los parámetros de la consulta de Contacto....
     * No está bien hecho debería ser Statefull
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
    public void init() {
    	super.tampagina = 10;
    	super.pagina = 0;
    	super.select = "";
    	super.from = " from Contacto contacto ";
    	super.where = "";
    	super.whereini = " ";
    	super.orderby = "";
    	super.camposfiltro = new String[] {"contacto.email","contacto.visible","contacto.anexarch"};
    	super.cursor = 0;
    	super.nreg = 0;
    	super.npags = 0;
    }    
    
    /**
     * Crea o actualiza un Contacto
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
     */
    public Long grabarContacto(Contacto contacto) throws DelegateException {

        Session session = getSession();
        try {
            boolean nuevo = (contacto.getId() == null) ? true : false;

            Transaction tx = session.beginTransaction();
            session.saveOrUpdate(contacto);
            session.flush();
            tx.commit();

            this.microsite = (Microsite) session.get(Microsite.class, contacto.getIdmicrosite());
            close(session);

            int op = (nuevo) ? Auditoria.CREAR : Auditoria.MODIFICAR;
            gravarAuditoria(Contacto.class.getSimpleName(), contacto.getId().toString(), op);

            return contacto.getId();

        } catch (HibernateException he) {
            throw new EJBException(he);
        } catch (DelegateException e) {
            throw new DelegateException(e);
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
        	Lineadatocontacto linea = (Lineadatocontacto) session.get(Lineadatocontacto.class, id);
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
        	Contacto contacto = (Contacto) session.get(Contacto.class, id);
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
     * borra un Formulario
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
     */
    public void borrarContacto(Long id) throws DelegateException {

        Session session = getSession();
        try {
        	Contacto contacto = (Contacto) session.get(Contacto.class, id);
            this.microsite = (Microsite) session.get(Microsite.class, contacto.getIdmicrosite());

            Transaction tx = session.beginTransaction();
            Iterator<Lineadatocontacto> iter = contacto.getLineasdatocontacto().iterator();
            while (iter.hasNext()) {
                Lineadatocontacto linea = iter.next();
                session.createQuery("delete TraduccionLineadatocontacto tlinea where tlinea.id.codigoLineadatocontacto = " + linea.getId().toString()).executeUpdate();
                session.createQuery("delete Lineadatocontacto linea where linea.id = " + linea.getId().toString()).executeUpdate();
                session.flush();
            }

            session.createQuery("delete Contacto contact where contact.id = " + id.toString()).executeUpdate();
            session.flush();
            tx.commit();
            close(session);

            gravarAuditoria(Contacto.class.getSimpleName(), id.toString(), Auditoria.ELIMINAR);

        } catch (HibernateException he) {
            throw new EJBException(he);
        } catch (DelegateException e) {
            throw new DelegateException(e);
        } finally {
            close(session);
        }
    }
    
    /**
     * Añade una nueva línea al formulario o modifica la que existe
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
     */
    public void creamodificaLinea(Lineadatocontacto lin, Long idcontacto) throws DelegateException {

        Session session = getSession();
        try {
            boolean nuevo = (lin.getId() == null) ? true : false;

        	Transaction tx = session.beginTransaction();
            Map<String, TraduccionLineadatocontacto> listaTraducciones = new HashMap<String, TraduccionLineadatocontacto>();
            lin.setIdcontacto(idcontacto);

            if (nuevo) {
            	Iterator<TraduccionLineadatocontacto> it = lin.getTraducciones().values().iterator();
            	while (it.hasNext()) {
            		TraduccionLineadatocontacto trd = it.next();
            		listaTraducciones.put(trd.getId().getCodigoIdioma(), trd);
            	}
            	lin.setTraducciones(null);
            }
            
            session.saveOrUpdate(lin);
            session.flush();
            
            if (nuevo) {
	            for (TraduccionLineadatocontacto trad : listaTraducciones.values()) {
	            	trad.getId().setCodigoLineadatocontacto(lin.getId());
	            	session.saveOrUpdate(trad);
	            }
	            session.flush();
	            lin.setTraducciones(listaTraducciones);
            }
            
            tx.commit();
            Contacto contacto = (Contacto) session.get(Contacto.class, idcontacto);
            this.microsite = (Microsite) session.get(Microsite.class, contacto.getIdmicrosite());
            close(session);

            int op = (nuevo) ? Auditoria.CREAR : Auditoria.MODIFICAR;
            gravarAuditoria(Lineadatocontacto.class.getSimpleName(), lin.getId().toString(), op);
        	
        } catch (HibernateException he) {
            throw new EJBException(he);
        } catch (DelegateException e) {
            throw new DelegateException(e);
        } finally {
            close(session);
        }
    }

    /**
     * elimina líneas del formulario
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
     */
    public void eliminarLineas(String[] lineas, Long idContacto) throws DelegateException {

        Session session = getSession();
        try {
        	Transaction tx = session.beginTransaction();
            Contacto contacto = (Contacto) session.get(Contacto.class, idContacto);
            this.microsite = (Microsite) session.get(Microsite.class, contacto.getIdmicrosite());

        	for (int i = 0; i < lineas.length; i++) {
            	session.createQuery("delete from TraduccionLineadatocontacto tlin where tlin.id.codigoLineadatocontacto = " + lineas[i]).executeUpdate();
            	session.createQuery("delete from Lineadatocontacto lin where id = " + lineas[i]).executeUpdate();
        	}
            session.flush();
            tx.commit();
            close(session);

            for (String linea : lineas) {
                gravarAuditoria(Lineadatocontacto.class.getSimpleName(), linea, Auditoria.ELIMINAR);
            }

        } catch (HibernateException e) {
            throw new EJBException(e);
        } catch (DelegateException e) {
            throw new DelegateException(e);
        } finally {
            close(session);
        }
    }
    
    /**
     * elimina una línea del formulario
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
     */
    public void eliminarLinea(Long idLinea, Long idContacto) throws DelegateException {

        Session session = getSession();
        try {
            Contacto contacto = (Contacto) session.get(Contacto.class, idContacto);
            this.microsite = (Microsite) session.get(Microsite.class, contacto.getIdmicrosite());

        	session.createQuery("delete from TraduccionLineadatocontacto tlin where tlin.id.codigoLineadatocontacto = " + idLinea).executeUpdate();
        	session.createQuery("delete from Lineadatocontacto lin where id = " + idLinea).executeUpdate();
            session.flush();
            close(session);

            gravarAuditoria(Lineadatocontacto.class.getSimpleName(), idLinea.toString(), Auditoria.ELIMINAR);

        } catch (HibernateException e) {
            throw new EJBException(e);
        } catch (DelegateException e) {
            throw new DelegateException(e);
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
        	Query query = session.createQuery("from Contacto con where con.idmicrosite = " + site.toString() + " and con.id = " + id.toString());
        	return query.list().isEmpty();

        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }
    
}
