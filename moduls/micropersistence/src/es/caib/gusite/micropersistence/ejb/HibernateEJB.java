package es.caib.gusite.micropersistence.ejb;

import java.security.Principal;
import java.util.Date;
import java.util.Hashtable;
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
 * 
 * @ejb.bean view-type="remote" generate="false"
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

	// Parámetros de consulta paginada
	protected int tampagina = 0; // tamaño de la página
	protected int pagina = 0; // página actual

	protected String select = "";
	protected String from = "";
	protected String where = "";
	protected String whereini = "";
	protected String orderby = ""; // ordenación de la consulta

	protected String[] camposfiltro;
	protected int cursor = 0;
	protected int nreg = 0;
	protected int npags = 0;

	// List de idiomas
	protected List<?> langs;

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

	private void restartSessionFactoryIfDatasourceModified() {

		if (this.isDatasourceModified()) {
			HibernateLocator.resetSessionFactory();
		}
	}

	static Object ds; // jboss.resource.adapter.jdbc.WrappedDataSource

	private boolean isDatasourceModified() {

		String dsJndiname = System.getProperty("es.caib.gusite.db.jndiname");
		Object ds = JBossUtils.lookupLocal("java:" + dsJndiname);
		if (null == HibernateEJB.ds) {
			HibernateEJB.ds = ds;
			return false;
		} else {
			if (ds != HibernateEJB.ds) {
				HibernateEJB.ds = ds;
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
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Hashtable<String, Integer> getParametros() {

		Hashtable<String, Integer> tabla = new Hashtable<String, Integer>();

		tabla.put("cursor", new Integer(this.cursor));

		// Calculo del cursor final
		int cursor_final = this.cursor;
		if (this.getPagina() == this.npags) {
			cursor_final = this.nreg;
		} else {
			cursor_final = this.cursor + this.tampagina - 1;
		}
		tabla.put("cursor_final", new Integer(cursor_final));

		tabla.put("nreg", new Integer(this.nreg));
		tabla.put("tampagina", new Integer(this.tampagina));
		tabla.put("actual", new Integer(this.getPagina()));

		if (this.getPagina() != 1) {
			tabla.put("inicio", new Integer(1));
		}
		if (this.getPagina() > 1) {
			tabla.put("anterior", new Integer(this.getPagina() - 1));
		}
		if (this.getPagina() < this.npags) {
			tabla.put("siguiente", new Integer(this.getPagina() + 1));
		}
		if (this.getPagina() != this.npags) {
			tabla.put("final", new Integer(this.npags));
		}

		return tabla;
	}

	/**
	 * Establece distintos parámetros para la paginación tras realizar la
	 * consulta
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public void parametrosCons() {

		boolean paginar = false; // hay paginación o no

		Session ses = this.getSession();
		try {
			this.nreg = ((Long) ses
					.createQuery("select count(*) " + this.from + this.where)
					.iterate().next()).intValue();
		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(ses);
		}

		if (this.getPagina() == 0) {
			this.setPagina(1);
		}

		this.cursor = ((this.getPagina() - 1) * this.tampagina) + 1; // posición
																		// del
																		// cursor
		this.npags = 1; // número total de páginas

		if (this.nreg > this.tampagina && this.tampagina > 0) {
			paginar = true;
			this.npags = ((this.nreg % this.tampagina) > 0 ? (this.nreg / this.tampagina) + 1
					: this.nreg / this.tampagina);
		}

		if (!paginar) {
			this.tampagina = this.nreg;
		}
	}

	/**
	 * @ejb.interface-method
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public int getPagina() {
		return this.pagina;
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
			tipo = orderby.substring(0, 1);
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
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public String getValorBD(String hql) {

		Session ses = this.getSession();
		try {
			return ((String) (ses.createQuery(hql).iterate().next()))
					.toString();
		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(ses);
		}
	}

	/**
	 * Establece el filtro de la consulta
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public void setFiltro(String valor) {
		// Establece el filtro de la consulta

		if (valor.equals("")) {
			this.where = this.whereini;
			return;
		}

		String filtro = " ";
		for (String element : this.camposfiltro) {
			filtro += element + " like '%" + valor + "%' OR ";
		}

		if (filtro.length() > 0) {
			filtro = filtro.substring(0, filtro.length() - 3);
		}

		if (this.where.length() > 0) {
			this.where = this.where + " AND (" + filtro + ")";
		} else {
			this.where = " where " + filtro;
		}
	}

	/**
	 * Obtiene la condicion 'where' de de la consulta
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public String getWhere() {
		return this.where;
	}

	/**
	 * Establece la condicion 'where' de la consulta
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public void setWhere(String where) {
		this.where = where;
	}

	/**
	 * Obtiene el tamano de la paginacion de de la consulta
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public int getTampagina() {
		return this.tampagina;
	}

	/**
	 * Establece la paginación de la consulta
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public void setTampagina(int tampagina) {
		this.tampagina = tampagina;
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