package es.caib.gusite.micropersistence.ejb;

import java.security.Principal;
import java.util.Date;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import es.caib.gusite.micromodel.Auditable;
import es.caib.gusite.micromodel.Auditoria;
import es.caib.gusite.micromodel.Usuario;
import es.caib.gusite.micropersistence.util.HibernateLocator;
import es.caib.gusite.micropersistence.util.JBossUtils;

/**
 * Bean con la funcionalidad básica para interactuar con HIBERNATE.
 * Los beans preexistentes de gusite 1.1 están definidos como stateless pero, como se ve ya en HibernateEJB, guardan estado.
 * Esta clase base, no guarda estado  
 * 
 * 
 * @ejb.bean view-type="remote" generate="false"
 * @ejb.security-role-ref role-name="gussystem" role-link="${role.system}"
 * @ejb.security-role-ref role-name="gusadmin" role-link="${role.admin}"
 * @ejb.security-role-ref role-name="gussuper" role-link="${role.super}"
 * @ejb.security-role-ref role-name="gusoper" role-link="${role.oper}"
 * 
 * @author at4.net
 */
public abstract class HibernateTrulyStatelessEJB implements SessionBean {

	private static final long serialVersionUID = -3971398698181242398L;
	protected static Log log = LogFactory.getLog(HibernateTrulyStatelessEJB.class);

	protected SessionContext ctx = null;

	@Override
	public void setSessionContext(SessionContext ctx) {
		this.ctx = ctx;
	}

	public void ejbCreate() throws CreateException {
		this.getSessionFactory();
	}

	protected SessionFactory getSessionFactory() {
		return HibernateLocator.getSessionFactory();
	}

	@Override
	public void ejbRemove() {
	}

	protected Session getSession() {

		try {
			this.restartSessionFactoryIfDatasourceModified();
			Session session = this.getSessionFactory().openSession();
			session.setFlushMode(FlushMode.COMMIT);

			return session;

		} catch (HibernateException e) {
			throw new EJBException(e);
		}
	}

	protected Session getCurrentSession() {

		try {
			this.restartSessionFactoryIfDatasourceModified();
			Session session = this.getSessionFactory().getCurrentSession();
			return session;

		} catch (HibernateException e) {
			throw new EJBException(e);
		}
	}

	private void restartSessionFactoryIfDatasourceModified() {

		if (this.isDatasourceModified()) {
			HibernateLocator.resetSessionFactory();
		}
	}

	static Object ds; // jboss.resource.adapter.jdbc.WrappedDataSource

	private boolean isDatasourceModified() {

		String dsJndiname = System.getProperty("es.caib.gusite.db.jndiname");
		Object ds = JBossUtils.lookupLocal("java:" + dsJndiname);
		if (null == HibernateTrulyStatelessEJB.ds) {
			HibernateTrulyStatelessEJB.ds = ds;
			return false;
		} else {
			if (ds != HibernateTrulyStatelessEJB.ds) {
				HibernateTrulyStatelessEJB.ds = ds;
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

	protected Query getNamedQuery(String name) {
		return this.getSessionFactory().getCurrentSession().getNamedQuery(name);
	}
	


	/**
	 * Devuelve el usuario del EJB
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public String getUsuarioEJB() {
		Principal principal = this.ctx.getCallerPrincipal();
		return principal.getName();
	}

	/*
	 * Los roles de usuario son inclusivos. Los siguientes métodos permiten
	 * saber si un usuario tiene los permisos de un determinado rol, bien porque
	 * sea su rol, o porque tenga uno superior.
	 */

	protected boolean userIsSystem() {
		return this.ctx.isCallerInRole("gussystem");
	}

	protected boolean userIsAdmin() {
		return this.userIsSystem() || this.ctx.isCallerInRole("gusadmin");
	}

	protected boolean userIsSuper() {
		return this.userIsAdmin() || this.ctx.isCallerInRole("gussuper");
	}

	protected boolean userIsOper() {
		return this.userIsSuper() || this.ctx.isCallerInRole("gusoper");
	}

	protected boolean userIs(String role) {
		if ("gusoper".equals(role)) {
			return this.userIsOper();
		} else if ("gussuper".equals(role)) {
			return this.userIsSuper();
		} else if ("gusadmin".equals(role)) {
			return this.userIsAdmin();
		} else if ("gussystem".equals(role)) {
			return this.userIsSystem();
		} else {
			return false;
		}
	}

	/**
	 * Comprueba si un usuario puede acceder a un Microsite
	 */
	protected Usuario getUsuario(Session session) throws HibernateException {

		Criteria criteriUsu = session.createCriteria(Usuario.class);
		criteriUsu.add(Restrictions.eq("username", this.ctx.getCallerPrincipal()
				.getName()));
		// List usuaris = criteriUsu.list();
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

	protected void grabarAuditoria(Auditable entidad, int operacion) {
		if (entidad.getIdmicrosite() != null) {
			this.grabarAuditoria(entidad.getIdmicrosite(), entidad, operacion);
		} else {
			// No hay microsite
			this.grabarAuditoria(null, entidad, operacion);
		}

	}

	protected void grabarAuditoria(Long idmicrosite, Auditable entidad,
			int operacion) {

		Auditoria auditoria = new Auditoria();
		auditoria.setEntidad(entidad.getClass().getSimpleName());
		auditoria.setIdEntidad(entidad.getAuditKey());
		auditoria.setIdmicrosite(idmicrosite);
		auditoria.setOperacion(operacion);
		this.grabarAuditoria(auditoria);

	}

	/**
	 * Crea o actualiza una Auditoria
	 */
	private Long grabarAuditoria(Auditoria auditoria) {

		Session session = this.getSession();
		
		try {
			
			Transaction tx = session.beginTransaction();
			
			// El caso del "antiguo microsite 0" (no microsite asociado) hay que tratarlo como si fuera null ahora.
			// Si no, cuando se va a grabar ua auditoria la PK 0 no se encuentra en la tabla de Microsites y se
			// produce un error de integridad por la FK establecida en la tabla de auditoría.
			if (auditoria.getIdmicrosite() != null && auditoria.getIdmicrosite() == 0L)
				auditoria.setIdmicrosite(null);
			
			auditoria.setFecha(new Date());
			auditoria.setUsuario(this.getUsuario(session).getUsername());
			session.saveOrUpdate(auditoria);
			session.flush();
			
			tx.commit();
			
			return auditoria.getId();

		} catch (HibernateException he) {
			
			throw new EJBException(he);
			
		} finally {
			
			this.close(session);
			
		}
		
	}

}