package es.caib.gusite.micropersistence.ejb;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import es.caib.gusite.micromodel.Auditoria;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.Temafaq;
import es.caib.gusite.micromodel.TraduccionTemafaq;

/**
 * SessionBean para manipular los temas de las FAQS.
 *
 * @ejb.bean name="sac/micropersistence/TemaFacade"
 *           jndi-name="es.caib.gusite.micropersistence.TemaFacade"
 *           type="Stateless" view-type="remote" transaction-type="Container"
 *
 * @ejb.transaction type="Required"
 *
 * @author Indra
 */
public abstract class TemaFacadeEJB extends HibernateEJB {

	private static final long serialVersionUID = 7011958120088531376L;

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
		super.select = "select tema";
		super.from = " from Temafaq tema join tema.traducciones trad ";
		super.where = " where trad.id.codigoIdioma='" + Idioma.getIdiomaPorDefecto() + "' and tema.idmicrosite="
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
	public void init() {
		super.tampagina = 10;
		super.pagina = 0;
		super.select = "select tema";
		super.from = " from Temafaq tema join tema.traducciones trad ";
		super.where = " where trad.id.codigoIdioma='" + Idioma.getIdiomaPorDefecto() + "'";
		super.whereini = " ";
		super.orderby = "";

		super.camposfiltro = new String[] { "trad.nombre" };
		super.cursor = 0;
		super.nreg = 0;
		super.npags = 0;
	}

	/**
	 * Crea o actualiza un tema
	 *
	 * @ejb.interface-method
	 * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public Long grabarTema(final Temafaq tema) {

		final Session session = this.getSession();
		try {
			final boolean nuevo = (tema.getId() == null) ? true : false;
			final Transaction tx = session.beginTransaction();

			final Map<String, TraduccionTemafaq> listaTraducciones = new HashMap<String, TraduccionTemafaq>();
			if (nuevo) {
				final Iterator<TraduccionTemafaq> it = tema.getTraducciones().values().iterator();
				while (it.hasNext()) {
					final TraduccionTemafaq trd = it.next();
					listaTraducciones.put(trd.getId().getCodigoIdioma(), trd);
				}
				tema.setTraducciones(null);
			}

			session.saveOrUpdate(tema);
			session.flush();

			if (nuevo) {
				for (final TraduccionTemafaq trad : listaTraducciones.values()) {
					trad.getId().setCodigoTema(tema.getId());
					session.saveOrUpdate(trad);
				}
				session.flush();
				tema.setTraducciones(listaTraducciones);
			}

			tx.commit();
			this.close(session);

			final int op = (nuevo) ? Auditoria.CREAR : Auditoria.MODIFICAR;
			this.grabarAuditoria(tema, op);

			return tema.getId();

		} catch (final HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Obtiene un tema
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Temafaq obtenerTema(final Long id) {

		final Session session = this.getSession();
		try {
			final Temafaq tema = (Temafaq) session.get(Temafaq.class, id);
			return tema;

		} catch (final HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Lista todos los temas
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<?> listarTemas() {

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
	 * borra un tema
	 *
	 * @ejb.interface-method
	 * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public void borrarTema(final Long id) {
		borrarTema(id, true);
	}

	/**
	 * borra un tema
	 *
	 * @ejb.interface-method
	 * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public void borrarTema(final Long id, final boolean indexar) {
		final Session session = this.getSession();
		try {
			final Temafaq temafaq = (Temafaq) session.get(Temafaq.class, id);

			session.createQuery("delete from TraduccionTemafaq ttema where ttema.id.codigoTema = " + id)
					.executeUpdate();
			session.createQuery("delete from Temafaq tfaq where tfaq.id = " + id).executeUpdate();
			session.flush();
			this.close(session);

			this.grabarAuditoria(temafaq, Auditoria.ELIMINAR);

		} catch (final HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Lista todos los temas para usar en Combos
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<?> listarCombo(final Long idmicrosite) {

		final Session session = this.getSession();
		try {
			final String consulta = "select tema from Temafaq tema join tema.traducciones trad where trad.id.codigoIdioma = '"
					+ Idioma.getIdiomaPorDefecto() + "' and tema.idmicrosite = " + idmicrosite.toString();
			final Query query = session.createQuery(consulta.toString());
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
			final Query query = session.createQuery("select tema from Temafaq tema where tema.idmicrosite = "
					+ site.toString() + " and tema.id = " + id.toString());
			return query.list().isEmpty();

		} catch (final HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

}
