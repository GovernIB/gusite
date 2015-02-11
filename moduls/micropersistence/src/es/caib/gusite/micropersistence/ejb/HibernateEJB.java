package es.caib.gusite.micropersistence.ejb;

import java.security.Principal;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import es.caib.gusite.micromodel.Auditoria;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;

import es.caib.gusite.micromodel.Usuario;
import es.caib.gusite.micropersistence.util.HibernateLocator;
import es.caib.gusite.micropersistence.util.JBossUtils;

/**
 * Bean con la funcionalidad básica para interactuar con HIBERNATE.
 *
 * @ejb.bean
 *  view-type="remote"
 *  generate="false"
 * @ejb.security-role-ref role-name="gussystem" role-link="${role.system}"
 * @ejb.security-role-ref role-name="gusadmin" role-link="${role.admin}"
 * @ejb.security-role-ref role-name="gussuper" role-link="${role.super}"
 * @ejb.security-role-ref role-name="gusoper" role-link="${role.oper}"
 * 
 * @author Indra
 */
public abstract class HibernateEJB implements SessionBean {

	private static final long serialVersionUID = -3971398698181242398L;

	public static int RESULTATS_CERCA_TOTS = 99999;   

	protected static Log log = LogFactory.getLog(HibernateEJB.class);

    protected SessionContext ctx = null;
    
    //  Parámetros de consulta paginada
    protected int tampagina = 0;   // tamaño de la página
    protected int pagina = 0;      // página actual
   
    protected String select = "";
    protected String from = "";
    protected String where = "";
    protected String whereini = "";
    protected String orderby = "";  // ordenación de la consulta

    protected String[] camposfiltro;
    protected int cursor = 0;
    protected int nreg = 0;
    protected int npags = 0;

    // List de idiomas
    protected List<?> langs;

    // Microsite para las auditoria
    protected Microsite microsite;
    
    public void setSessionContext(SessionContext ctx) {
        this.ctx = ctx;
    }

    public void ejbCreate() throws CreateException {
    	getSessionFactory();
    }

	protected SessionFactory getSessionFactory() {
		return HibernateLocator.getSessionFactory();
	}

    public void ejbRemove() {
    }

    protected Session getSession() {

        try {
        	restartSessionFactoryIfDatasourceModified();
            Session session = getSessionFactory().openSession();
            session.setFlushMode(FlushMode.COMMIT);
            
            return session;

        } catch (HibernateException e) {
            throw new EJBException(e);
        }
    }

	private void restartSessionFactoryIfDatasourceModified() {
		
		if (isDatasourceModified()) {
			HibernateLocator.resetSessionFactory();
		}
	}

    static Object ds; //jboss.resource.adapter.jdbc.WrappedDataSource

    private boolean isDatasourceModified() {

    	String dsJndiname = System.getProperty("es.caib.gusite.db.jndiname");
		Object ds = JBossUtils.lookupLocal("java:" + dsJndiname);
		if (null == this.ds) {
			this.ds = ds;
			return false;
		} else {
			if (ds != this.ds) {
				this.ds = ds;
				return true;
			}
		}
		return false;
	}

	protected void close(Session session) {

        if (session != null && session.isOpen()) {
            try {
                if (session.isDirty()) {
                    log.warn("Closing dirty session!!");
                }
                session.close();
            } catch (HibernateException e) {
                log.error(e);
            }
        }
    }
    
    /**
     * Devuelve los parámetros de la consulta en una Hash
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public Hashtable<String, Integer> getParametros() {

        Hashtable<String, Integer> tabla = new Hashtable<String, Integer>();
    
        tabla.put("cursor", new Integer(cursor));
        
        // Calculo del cursor final
        int cursor_final = cursor;
        if (getPagina() == npags) {
            cursor_final = nreg;
        } else {
            cursor_final = cursor + tampagina - 1;
        }
        tabla.put("cursor_final", new Integer(cursor_final));
        
        tabla.put("nreg", new Integer(nreg));
        tabla.put("tampagina", new Integer(tampagina));
        tabla.put("actual", new Integer(getPagina()));
        
        if (getPagina() != 1)      tabla.put("inicio", new Integer(1));
        if (getPagina() > 1)       tabla.put("anterior", new Integer(getPagina()-1));
        if (getPagina() < npags)   tabla.put("siguiente", new Integer(getPagina()+1));
        if (getPagina() != npags)  tabla.put("final", new Integer(npags));
        
        return tabla;
    }

    /**
     * Establece distintos parámetros para la paginación tras realizar la
     * consulta
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public void parametrosCons() {

    	boolean paginar = false;                      // hay paginación o no
        
        Session ses = getSession();
        try {
        	nreg = ((Long) ses.createQuery("select count(*) " + from + where).iterate().next()).intValue();
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(ses);
        }

		if (getPagina() == 0) {
            setPagina(1);
        }
		
		cursor = ((getPagina() - 1) * tampagina) + 1; // posición del cursor
		npags = 1; // número total de páginas
		
		if (nreg > tampagina && tampagina > 0) {
			paginar = true;
			npags = ((nreg % tampagina) > 0 ? (nreg / tampagina) + 1 : nreg / tampagina);
		}

		if (!paginar) {
            tampagina = nreg;
        }
    }

    /**
     * @ejb.interface-method
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
    public int getPagina() {
        return pagina;
    }

    /**
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public void setPagina(int pagina) {
        this.pagina = pagina;
    }

    /**
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public void setOrderby(String orderby) {
     	
        String tipo = "";

        if (orderby.length() > 0) {
            tipo=orderby.substring(0, 1);
            if (tipo.equals("A")) {
                tipo = "ASC";
            } else {
                tipo = "DESC";
            }
            this.orderby = " ORDER BY " + orderby.substring(1) + " " + tipo;
        }
    }

    /**
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public void setOrderby2(String orderby) {
           this.orderby = orderby;
    }

    /**
     * Ejecuta una consulta y devuelve el primer valor del primer registro
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public String getValorBD(String hql) {

        Session ses = getSession();
        try {
        	return ((String)(ses.createQuery(hql).iterate().next())).toString();
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(ses);
        }
    }

    /**
     * Establece el filtro de la consulta
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public void setFiltro(String valor) {
        // Establece el filtro de la consulta
        
        if (valor.equals("")) {
            where = whereini;
            return;
        }

        String filtro = " ";
        for (int i = 0; i < camposfiltro.length; i++) {
            filtro += camposfiltro[i] + " like '%" + valor + "%' OR ";
        }

        if (filtro.length() > 0) {
            filtro = filtro.substring(0, filtro.length() - 3);
        }

        if (where.length() > 0) {
            where = where + " AND (" + filtro + ")";
        } else {
            where = " where " + filtro;
        }
    }

    /**
     * Obtiene la condicion 'where' de de la consulta
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
	public String getWhere() {
		return where;
	}

    /**
     * Establece la condicion 'where' de la consulta
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
	public void setWhere(String where) {
		this.where = where;
	}    

    /**
     * Obtiene el tamano de la paginacion de de la consulta
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
	public int getTampagina() {
		return tampagina;
	}

    /**
     * Establece la paginación de la consulta
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
	public void setTampagina(int tampagina) {
		this.tampagina = tampagina;
	}    	

    /**
     * Devuelve el usuario del EJB
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
	public String getUsuarioEJB() {
        Principal principal = ctx.getCallerPrincipal();
        return principal.getName();
	}

	/*
    Los roles de usuario son inclusivos.
    Los siguientes métodos permiten saber si un usuario tiene los permisos
    de un determinado rol, bien porque sea su rol, o porque tenga uno superior. 
    */

	protected boolean userIsSystem() {
		return ctx.isCallerInRole("gussystem");
	}

	protected boolean userIsAdmin() {
		return userIsSystem() || ctx.isCallerInRole("gusadmin");
	}

	protected boolean userIsSuper() {
		return userIsAdmin() || ctx.isCallerInRole("gussuper");
	}

	protected boolean userIsOper() {
		return userIsSuper() || ctx.isCallerInRole("gusoper");
	}

	protected boolean userIs(String role) {
		if ("gusoper".equals(role)) {
			return userIsOper();
		} else if ("gussuper".equals(role)) {
			return userIsSuper();
		} else if ("gusadmin".equals(role)) {
			return userIsAdmin();
		} else if ("gussystem".equals(role)) {
			return userIsSystem();
		} else {
			return false;
		}
	}

    /**
     * Comprueba si un usuario puede acceder a un Microsite
     */
    protected Usuario getUsuario(Session session) throws HibernateException {

        Criteria criteriUsu = session.createCriteria(Usuario.class);
        criteriUsu.add(Expression.eq("username", ctx.getCallerPrincipal().getName()));
        //List usuaris = criteriUsu.list();
        List<?> usuaris;
        
        try {
        	usuaris = criteriUsu.list();
        } catch (HibernateException he) {
            throw new EJBException(he);
        }

        if (usuaris.isEmpty()) {
            throw new EJBException("El usuario actual no existe!");
        }
        return (Usuario) usuaris.get(0);
    }

    protected void gravarAuditoria(String entidad, String id, int operacion) throws DelegateException {

        Auditoria auditoria = new Auditoria();
        auditoria.setEntidad(entidad);
        auditoria.setIdEntidad(id);
        auditoria.setMicrosite(this.microsite);
        auditoria.setOperacion(operacion);
        DelegateUtil.getAuditoriaDelegate().grabarAuditoria(auditoria);
    }

}