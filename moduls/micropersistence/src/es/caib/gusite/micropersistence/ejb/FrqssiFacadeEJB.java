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
	public void init(final Long site) {
		super.tampagina = 10;
		super.pagina = 0;
		super.select = "select frqssi";
		super.from = " from Frqssi frqssi join frqssi.traducciones trad ";
		super.where = " where trad.id.codigoIdioma = '" + Idioma.getIdiomaPorDefecto() + "' and frqssi.idmicrosite = "
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
	public void init(final Long site, final String idiomapasado) {
		super.tampagina = 10;
		super.pagina = 0;
		super.select = "select frqssi.id,trad.nombre ";
		super.from = " from Frqssi frqssi join frqssi.traducciones trad ";
		super.where = " where (trad.id.codigoIdioma = '" + Idioma.getIdiomaPorDefecto()
				+ "' or trad.id.codigoIdioma = '" + idiomapasado + "') and frqssi.idmicrosite = " + site.toString();
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
		super.where = " where trad.id.codigoIdioma = '" + Idioma.getIdiomaPorDefecto() + "'";
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
	 * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public Long grabarFrqssi(final Frqssi frqssi) throws DelegateException {

		final Session session = this.getSession();
		try {
			final boolean nuevo = (frqssi.getId() == null) ? true : false;
			final Transaction tx = session.beginTransaction();

			final Map<String, TraduccionFrqssi> listaTraducciones = new HashMap<String, TraduccionFrqssi>();
			if (nuevo) {
				final Iterator<TraduccionFrqssi> it = frqssi.getTraducciones().values().iterator();
				while (it.hasNext()) {
					final TraduccionFrqssi trd = it.next();
					listaTraducciones.put(trd.getId().getCodigoIdioma(), trd);
				}
				frqssi.setTraducciones(null);
			}

			session.saveOrUpdate(frqssi);
			session.flush();

			if (nuevo) {
				for (final TraduccionFrqssi trad : listaTraducciones.values()) {
					trad.getId().setCodigoFrqssi(frqssi.getId());
					session.saveOrUpdate(trad);
				}
				session.flush();
				frqssi.setTraducciones(listaTraducciones);
			}

			tx.commit();
			this.close(session);

			final int op = (nuevo) ? Auditoria.CREAR : Auditoria.MODIFICAR;
			this.grabarAuditoria(frqssi, op);

			return frqssi.getId();

		} catch (final HibernateException he) {
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
	public Frqssi obtenerFrqssi(final Long id) {

		final Session session = this.getSession();
		try {
			final Frqssi frqssi = (Frqssi) session.get(Frqssi.class, id);
			return frqssi;

		} catch (final HibernateException he) {
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

		final Session session = this.getSession();
		try {
			this.parametrosCons(); // Establecemos los parámetros de la
									// paginación
			final Query query = session.createQuery(this.select + this.from + this.where + this.orderby);
			query.setFirstResult(this.cursor - 1);
			query.setMaxResults(this.tampagina);
			return query.list();

		} catch (final HibernateException he) {
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
	public List<Frqssi> listarFrqssisrec(final String idiomapasado) {

		final Session session = this.getSession();
		try {
			this.parametrosCons(); // Establecemos los parámetros de la
									// paginación
			final Query query = session.createQuery(this.select + this.from + this.where + this.orderby);
			final ScrollableResults scr = query.scroll();
			final ArrayList<Frqssi> lista = new ArrayList<Frqssi>();
			scr.first();
			scr.scroll(this.cursor - 1);
			int i = 0;
			while (this.tampagina > i++) {
				final Object[] fila = scr.get();
				final Frqssi qss = new Frqssi();
				qss.setId((Long) fila[0]);
				final TraduccionFrqssi tradqss = new TraduccionFrqssi();
				tradqss.setNombre((String) fila[1]);
				qss.setTraduccion(idiomapasado, tradqss);
				lista.add(qss);
				if (!scr.next()) {
					break;
				}
			}
			scr.close();
			return lista;

		} catch (final HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * borra un Formulario QSSI
	 *
	 * @ejb.interface-method
	 * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public void borrarFrqssi(final Long id) throws DelegateException {
		borrarFrqssi(id, true);
	}

	/**
	 * borra un Formulario QSSI
	 *
	 * @ejb.interface-method
	 * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public void borrarFrqssi(final Long id, final boolean indexar) throws DelegateException {

		final Session session = this.getSession();
		try {
			final Frqssi frqssi = (Frqssi) session.get(Frqssi.class, id);

			session.createQuery("delete from TraduccionFrqssi tfrq where tfrq.id.codigoFrqssi = " + id).executeUpdate();
			session.createQuery("delete from Frqssi where id = " + id).executeUpdate();
			session.flush();
			this.close(session);

			this.grabarAuditoria(frqssi, Auditoria.ELIMINAR);

		} catch (final HibernateException he) {
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
	public List<?> listarCombo(final Long idmicrosite) {

		final Session session = this.getSession();
		try {
			final Query query = session.createQuery("from Frqssi frqssi" + " join frqssi.traducciones trad"
					+ " where trad.id.codigoIdioma = '" + Idioma.getIdiomaPorDefecto() + "'"
					+ " and frqssi.idmicrosite = " + idmicrosite.toString());
			return query.list();

		} catch (final HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Comprueba que el elemento pertenece al Microsite
	 *
	 * @ejb.interface-method
	 * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public boolean checkSite(final Long site, final Long id) {

		final Session session = this.getSession();
		try {
			final Query query = session.createQuery("from Frqssi frqssi where frqssi.idmicrosite = " + site.toString()
					+ " and frqssi.id = " + id.toString());
			return query.list().isEmpty();

		} catch (final HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

}
