package es.caib.gusite.micropersistence.ejb;

import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import es.caib.gusite.micromodel.Auditoria;
import es.caib.gusite.micromodel.Tiposervicio;

/**
 * SessionBean para consultar Tiposervicio.
 * 
 * @ejb.bean name="sac/micropersistence/TiposervicioFacade"
 *           jndi-name="es.caib.gusite.micropersistence.TiposervicioFacade"
 *           type="Stateless" view-type="remote" transaction-type="Container"
 * 
 * @ejb.transaction type="Required"
 * 
 * @author Indra
 */
public abstract class TiposervicioFacadeEJB extends HibernateEJB {

	private static final long serialVersionUID = 246508220900910393L;

	/**
	 * @ejb.create-method
	 * @ejb.permission unchecked="true"
	 */
	@Override
	public void ejbCreate() throws CreateException {
		super.ejbCreate();
	}

	/**
	 * Inicializo los parámetros de la consulta de Tiposervicio.... No está bien
	 * hecho debería ser Statefull
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public void init() {
		super.tampagina = 50;
		super.pagina = 0;
		super.select = "";
		super.from = " from Tiposervicio tipo ";
		super.where = " ";
		super.whereini = " ";
		super.orderby = "";

		super.camposfiltro = new String[] { "tipo.nombre" };
		super.cursor = 0;
		super.nreg = 0;
		super.npags = 0;
	}

	/**
	 * Crea o actualiza un Tiposervicio
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public Long grabarTiposervicio(Tiposervicio tipo) {

		Session session = this.getSession();
		try {
			boolean nuevo = (tipo.getId() == null) ? true : false;
			Transaction tx = session.beginTransaction();
			session.saveOrUpdate(tipo);
			session.flush();
			tx.commit();
			this.close(session);

			int op = (nuevo) ? Auditoria.CREAR : Auditoria.MODIFICAR;
			this.grabarAuditoria(null, tipo, op);

			return tipo.getId();

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Obtiene un Tiposervicio
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Tiposervicio obtenerTiposervicio(Long id) {

		Session session = this.getSession();
		try {
			Tiposervicio tipo = (Tiposervicio) session.get(Tiposervicio.class,
					id);
			return tipo;

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Lista todos los Tiposervicio
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<?> listarTiposervicios() {

		Session session = this.getSession();
		try {
			this.parametrosCons(); // Establecemos los parámetros de la
									// paginación
			Query query = session.createQuery(this.select + this.from
					+ this.where + this.orderby);
			query.setFirstResult(this.cursor - 1);
			query.setMaxResults(this.tampagina);
			return query.list();

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Lista todos los tipos para seleccionar los usados
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<?> listarTipos() {

		Session session = this.getSession();
		try {
			Query query = session
					.createQuery("from Tiposervicio tp where tp.visible = 'S'");
			return query.list();

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * borra un Tiposervicio
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public void borrarTiposervicio(Long id) {

		Session session = this.getSession();
		try {
			Transaction tx = session.beginTransaction();
			Tiposervicio tipo = (Tiposervicio) session.get(Tiposervicio.class,
					id);
			session.delete(tipo);
			session.flush();
			tx.commit();
			this.close(session);

			this.grabarAuditoria(null, tipo, Auditoria.ELIMINAR);

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

}
