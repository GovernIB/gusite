package es.caib.gusite.micropersistence.ejb;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;

import es.caib.gusite.micromodel.Auditoria;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;

import es.caib.gusite.micromodel.Usuario;

/**
 * SessionBean para gestionar usuarios.
 *
 * @ejb.bean
 *  name="sac/micropersistence/UsuarioFacade"
 *  jndi-name="es.caib.gusite.micropersistence.UsuarioFacade"
 *  type="Stateless"
 *  view-type="remote"
 *  transaction-type="Container"
 *
 * @ejb.transaction type="Required"
 *
 * @author Indra
 */
public abstract class UsuarioFacadeEJB extends HibernateEJB {

	private static final long serialVersionUID = -3671714824083479106L;

	/**
     * @ejb.create-method
     * @ejb.permission unchecked="true"
     */
    public void ejbCreate() throws CreateException {
        super.ejbCreate();
    }

    /**
     * Inicializo los parámetros de la consulta de un usuario.... 
     * 
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
    public void init() {
        super.tampagina = 10;
        super.pagina = 0;
        super.select = "";
        super.from = " from Usuario usu  ";
        super.where = "";
        super.whereini = " ";
        super.orderby = "";

        super.camposfiltro = new String[]{"usu.username", "usu.nombre", "usu.perfil"};
        super.cursor = 0;
        super.nreg = 0;
        super.npags = 0;
    }

    /**
     * Lista todos los usuarios
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
    public List<?> listarUsuarios() {

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
     * Crea o actualiza un nuevo usuario.
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin}"
     */
    public void grabarUsuario(Usuario usuario) {

        Session session = getSession();
        try {
            boolean nuevo = (usuario.getId() == null) ? true : false;
            if ("gussystem".equals(usuario.getPerfil())) {
                throw new SecurityException("No puede crear usuarios de sistema");
            }
            session.saveOrUpdate(usuario);
            session.flush();
            close(session);

            int op = (nuevo) ? Auditoria.CREAR : Auditoria.MODIFICAR;
            gravarAuditoria(Usuario.class.getSimpleName(), usuario.getId().toString(), op);

        } catch (HibernateException he) {
            throw new EJBException(he);
        } catch (DelegateException e) {
            throw new EJBException(e);
        } finally {
            close(session);
        }
    }

    /**
     * Obtiene una lista de usuarios.
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin}"
     */
    public List<?> buscarUsuarios(Map<?, ?> parametros) {

        Session session = getSession();
        try {         
            Criteria criteri = session.createCriteria(Usuario.class);
            populateCriteria(criteri, parametros);
            return criteri.list();

        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }

    /**
     * Obtiene un usuario.
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin},${role.oper},${role.super}"
     */
    public Usuario obtenerUsuario(Long id) {

        Session session = getSession();
        try {
            Usuario usuario = (Usuario) session.get(Usuario.class, id);
            Hibernate.initialize(usuario);
            return usuario;

        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }

    /**
     * Obtiene un usuario dado el username
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin},${role.oper},${role.super}"
     */
    public Usuario obtenerUsuariobyUsername(String username) {

        Session session = getSession();
        try {
            Criteria criteri = session.createCriteria(Usuario.class);
            criteri.add(Expression.eq("username", username));
            Usuario usu = (Usuario) criteri.uniqueResult();
            return usu;

        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }    
    
    /**
     * Obtiene un usuario.
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin}"
     */
    public List<?> listarUsuariosPerfil(String perfil) {

        Session session = getSession();
        try {
            Criteria criteri = session.createCriteria(Usuario.class);
            if (perfil != null) {
                criteri.add(Expression.eq("perfil", perfil));
            }
            return criteri.list();

        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }

    /**
     * Borra un usuario.
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin}"
     */
    public void borrarUsuario(Long id) {

        Session session = getSession();
        try {
            Usuario usuario = (Usuario) session.get(Usuario.class, id);
            if (!userIs(usuario.getPerfil())) {
                throw new SecurityException("No puede borrar usuarios de un perfil superior al suyo");
            }
            session.delete(usuario);
            session.flush();
            close(session);

            gravarAuditoria(Usuario.class.getSimpleName(), id.toString(), Auditoria.ELIMINAR);

        } catch (HibernateException he) {
            throw new EJBException(he);
        } catch (DelegateException e) {
            throw new EJBException(e);
        } finally {
            close(session);
        }
    }

    private void populateCriteria(Criteria criteri, Map<?, ?> parametros) {

        parametros.remove("id");
        for (Iterator<?> iterator = parametros.keySet().iterator(); iterator.hasNext();) {
            String key = (String) iterator.next();
            Object value = parametros.get(key);
            if (value != null) {
                if (value instanceof String) {
                    String sValue = (String) value;
                    if (sValue.length() > 0) {
                        criteri.add(Expression.ilike(key, value));
                    }
                } else {
                    criteri.add(Expression.eq(key, value));
                }
            }
        }
    }

}
