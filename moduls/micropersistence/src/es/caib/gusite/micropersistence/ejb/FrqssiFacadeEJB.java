package es.caib.gusite.micropersistence.ejb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.Transaction;

import es.caib.gusite.micromodel.Auditoria;
import es.caib.gusite.micromodel.Frqssi;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.TraduccionFrqssi;
import es.caib.gusite.micropersistence.delegate.DelegateException;

/**
 * SessionBean para manipular los Formularios QSSI
 * 
 * @ejb.bean name="sac/micropersistence/FrqssiFacade"
 *           jndi-name="es.caib.gusite.micropersistence.FrqssiFacade"
 *           type="Stateless" view-type="remote" transaction-type="Container"
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
	@Override
	public void ejbCreate() throws CreateException {
		super.ejbCreate();
	}

	/**
	 * Inicializo los parámetros de la consulta....
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public void init(Long site) {
		super.tampagina = 10;
		super.pagina = 0;
		super.select = "select frqssi";
		super.from = " from Frqssi frqssi join frqssi.traducciones trad ";
		super.where = " where trad.id.codigoIdioma = '"
				+ Idioma.getIdiomaPorDefecto() + "' and frqssi.idmicrosite = "
				+ site.toString();
		super.whereini = " ";
		super.orderby = "";

		super.camposfiltro = new String[] { "trad.nombre" };
		super.cursor = 0;
		super.nreg = 0;
		super.npags = 0;
	}

	/**
	 * Inicializo los parámetros de la consulta....
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public void init(Long site, String idiomapasado) {
		super.tampagina = 10;
		super.pagina = 0;
		super.select = "select frqssi.id,trad.nombre ";
		super.from = " from Frqssi frqssi join frqssi.traducciones trad ";
		super.where = " where (trad.id.codigoIdioma = '"
				+ Idioma.getIdiomaPorDefecto()
				+ "' or trad.id.codigoIdioma = '" + idiomapasado
				+ "') and frqssi.idmicrosite = " + site.toString();
		super.whereini = " ";
		super.orderby = " order by frqssi.id, trad.id.codigoIdioma desc";

		super.camposfiltro = new String[] { "trad.nombre" };
		super.cursor = 0;
		super.nreg = 0;
		super.npags = 0;
	}

	/**
	 * Inicializo los parámetros de la consulta....
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public void init() {
		super.tampagina = 10;
		super.pagina = 0;
		super.select = "select frqssi";
		super.from = " from Frqssi frqssi join frqssi.traducciones trad ";
		super.where = " where trad.id.codigoIdioma = '"
				+ Idioma.getIdiomaPorDefecto() + "'";
		super.whereini = " ";
		super.orderby = "";

		super.camposfiltro = new String[] { "trad.nombre" };
		super.cursor = 0;
		super.nreg = 0;
		super.npags = 0;
	}

	/**
	 * Crea o actualiza un Formulario QSSI
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public Long grabarFrqssi(Frqssi frqssi) throws DelegateException {

		Session session = this.getSession();
		try {
			boolean nuevo = (frqssi.getId() == null) ? true : false;
			Transaction tx = session.beginTransaction();

			Map<String, TraduccionFrqssi> listaTraducciones = new HashMap<String, TraduccionFrqssi>();
			if (nuevo) {
				Iterator<TraduccionFrqssi> it = frqssi.getTraducciones()
						.values().iterator();
				while (it.hasNext()) {
					TraduccionFrqssi trd = it.next();
					listaTraducciones.put(trd.getId().getCodigoIdioma(), trd);
				}
				frqssi.setTraducciones(null);
			}

			session.saveOrUpdate(frqssi);
			session.flush();

			if (nuevo) {
				for (TraduccionFrqssi trad : listaTraducciones.values()) {
					trad.getId().setCodigoFrqssi(frqssi.getId());
					session.saveOrUpdate(trad);
				}
				session.flush();
				frqssi.setTraducciones(listaTraducciones);
			}

			tx.commit();
			this.close(session);

			int op = (nuevo) ? Auditoria.CREAR : Auditoria.MODIFICAR;
			this.grabarAuditoria(frqssi, op);

			return frqssi.getId();

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Obtiene un Formulario QSSI
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Frqssi obtenerFrqssi(Long id) {

		Session session = this.getSession();
		try {
			Frqssi frqssi = (Frqssi) session.get(Frqssi.class, id);
			return frqssi;

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Lista todos los Formularios QSSI
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<?> listarFrqssis() {

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
	 * Lista todos los Formularios QSSI
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<Frqssi> listarFrqssisrec(String idiomapasado) {

		Session session = this.getSession();
		try {
			this.parametrosCons(); // Establecemos los parámetros de la
									// paginación
			Query query = session.createQuery(this.select + this.from
					+ this.where + this.orderby);
			ScrollableResults scr = query.scroll();
			ArrayList<Frqssi> lista = new ArrayList<Frqssi>();
			scr.first();
			scr.scroll(this.cursor - 1);
			int i = 0;
			while (this.tampagina > i++) {
				Object[] fila = scr.get();
				Frqssi qss = new Frqssi();
				qss.setId((Long) fila[0]);
				TraduccionFrqssi tradqss = new TraduccionFrqssi();
				tradqss.setNombre((String) fila[1]);
				qss.setTraduccion(idiomapasado, tradqss);
				lista.add(qss);
				if (!scr.next()) {
					break;
				}
			}
			scr.close();
			return lista;

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * borra un Formulario QSSI
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public void borrarFrqssi(Long id) throws DelegateException {

		Session session = this.getSession();
		try {
			Frqssi frqssi = (Frqssi) session.get(Frqssi.class, id);

			session.createQuery(
					"delete from TraduccionFrqssi tfrq where tfrq.id.codigoFrqssi = "
							+ id).executeUpdate();
			session.createQuery("delete from Frqssi where id = " + id)
					.executeUpdate();
			session.flush();
			this.close(session);

			this.grabarAuditoria(frqssi, Auditoria.ELIMINAR);

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Lista todos los Formularios QSSI para usar en Combos
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<?> listarCombo(Long idmicrosite) {

		Session session = this.getSession();
		try {
			Query query = session.createQuery("from Frqssi frqssi"
					+ " join frqssi.traducciones trad"
					+ " where trad.id.codigoIdioma = '"
					+ Idioma.getIdiomaPorDefecto() + "'"
					+ " and frqssi.idmicrosite = " + idmicrosite.toString());
			return query.list();

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Comprueba que el elemento pertenece al Microsite
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public boolean checkSite(Long site, Long id) {

		Session session = this.getSession();
		try {
			Query query = session
					.createQuery("from Frqssi frqssi where frqssi.idmicrosite = "
							+ site.toString()
							+ " and frqssi.id = "
							+ id.toString());
			return query.list().isEmpty();

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

}
