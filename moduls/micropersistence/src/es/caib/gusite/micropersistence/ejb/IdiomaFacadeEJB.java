package es.caib.gusite.micropersistence.ejb;

import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import es.caib.gusite.micromodel.Idioma;

/**
 * SessionBean para consultar idiomas.
 * 
 * @ejb.bean name="sac/micropersistence/IdiomaFacade"
 *           jndi-name="es.caib.gusite.micropersistence.IdiomaFacade"
 *           type="Stateless" view-type="remote" transaction-type="Container"
 * 
 * @ejb.transaction type="Required"
 * 
 * @author Indra
 */
public abstract class IdiomaFacadeEJB extends HibernateEJB {

	private static final long serialVersionUID = 5953591408981487226L;

	/**
	 * @ejb.create-method
	 * @ejb.permission unchecked="true"
	 */
	@Override
	public void ejbCreate() throws CreateException {
		super.ejbCreate();
	}

	/**
	 * Devuelve una lista de {@link java.lang.String} con el codigo ISO los
	 * idiomas.
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List /* String */<?> listarLenguajes() {

		Session session = this.getSession();
		try {
			Query query = session
					.createQuery("select idi.lang from Idioma idi order by idi.orden");
			query.setCacheable(true);
			return query.list();

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Devuelve una lista de {@link java.lang.String} con el los Ids de Idiomas
	 * del traductor.
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List /* String */<?> listarLenguajesTraductor() {

		Session session = this.getSession();
		try {
			Query query = session
					.createQuery("select idi.langTraductor from Idioma as idi order by idi.orden");
			query.setCacheable(true);
			return query.list();

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Obtiene el codigo ISO del lenguaje por defecto.
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public String lenguajePorDefecto() {

		Session session = this.getSession();
		try {
			Query query = session
					.createQuery("select idi.lang from Idioma as idi where idi.orden = 1");
			query.setCacheable(true);
			List<?> results = query.list();
			if (results.isEmpty()) {
				return null;
			} else {
				return (String) results.get(0);
			}

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Devuelve una lista de idiomas
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<?> listarIdiomas() {

		Session session = this.getSession();
		try {
			Query query = session
					.createQuery("from Idioma idi order by idi.orden");
			query.setCacheable(true);
			return query.list();

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Obtiene un Idioma
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Idioma obtenerIdioma(String lang) {

		Session session = this.getSession();
		try {
			Idioma idioma = (Idioma) session.get(Idioma.class, lang);
			return idioma;

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Devuelve el tiempo de ejecutar una select de los idiomas
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public long testeoHql() {

		Session session = this.getSession();
		try {
			Query query = session.createQuery("from Idioma");
			long ini = System.currentTimeMillis();
			query.list();
			long fin = System.currentTimeMillis();
			return (fin - ini);

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

}
