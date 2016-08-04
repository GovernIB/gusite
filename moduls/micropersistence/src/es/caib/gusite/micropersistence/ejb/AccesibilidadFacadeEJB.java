package es.caib.gusite.micropersistence.ejb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import es.caib.gusite.micromodel.Accesibilidad;
import es.caib.gusite.micromodel.Agenda;
import es.caib.gusite.micromodel.Auditoria;
import es.caib.gusite.micromodel.Contenido;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.Noticia;
import es.caib.gusite.micromodel.TraduccionAgenda;
import es.caib.gusite.micromodel.TraduccionContenido;
import es.caib.gusite.micromodel.TraduccionNoticia;

/**
 * SessionBean para consultar Accesibilidad.
 * 
 * @ejb.bean name="sac/micropersistence/AccesibilidadFacade"
 *           jndi-name="es.caib.gusite.micropersistence.AccesibilidadFacade"
 *           type="Stateless" view-type="remote" transaction-type="Container"
 * 
 * @ejb.transaction type="Required"
 * 
 * @author Indra
 */

public abstract class AccesibilidadFacadeEJB extends HibernateEJB {

	private static final long serialVersionUID = -5418440926294056000L;

	/**
	 * @ejb.create-method
	 * @ejb.permission unchecked="true"
	 */
	@Override
	public void ejbCreate() throws CreateException {
		super.ejbCreate();
	}

	/**
	 * Crea o actualiza una accesibilidad
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Long grabarAccesibilidad(Accesibilidad accesibilidad) {

		Session session = this.getSession();
		try {
			boolean nuevo = (accesibilidad.getId() == null) ? true : false;
			Transaction tx = session.beginTransaction();
			session.saveOrUpdate(accesibilidad);
			session.flush();
			tx.commit();
			this.close(session);

			int op = (nuevo) ? Auditoria.CREAR : Auditoria.MODIFICAR;
			this.grabarAuditoria(accesibilidad.getCodmicro(), accesibilidad, op);

			return accesibilidad.getId();

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Método que borra una accesibilidad
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public void borrarAccesibilidad(Accesibilidad accesibilidad) {

		Session session = this.getSession();
		try {
			Transaction tx = session.beginTransaction();
			session.delete(accesibilidad);
			session.flush();
			tx.commit();
			this.close(session);

			this.grabarAuditoria(accesibilidad.getCodmicro(), accesibilidad,
					Auditoria.ELIMINAR);

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Obtiene una accesibilidad
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Accesibilidad obtenerAccesibilidad(Long id) {

		Session session = this.getSession();
		try {
			Accesibilidad accesibilidad = (Accesibilidad) session.get(
					Accesibilidad.class, id);
			return accesibilidad;

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Obtiene una accesibilidad
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Accesibilidad obtenerAccesibilidad(String servicio, Long iditem,
			String idioma) {

		Session session = this.getSession();
		try {
			Criteria criteri = session.createCriteria(Accesibilidad.class);
			criteri.add(Restrictions.eq("servicio", servicio));
			criteri.add(Restrictions.eq("iditem", iditem));
			criteri.add(Restrictions.eq("idioma", idioma));
			Accesibilidad accesibilidad = null;
			if (criteri.list().size() > 0) {
				accesibilidad = (Accesibilidad) criteri.list().get(0);
			}
			return accesibilidad;

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Obtiene toda accesibilidad del site
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<?> obtenerAccesibilidadMicro(Long id) {

		Session session = this.getSession();
		try {
			Criteria criteri = session.createCriteria(Accesibilidad.class);
			criteri.add(Restrictions.eq("codmicro", id));
			criteri.addOrder(Order.asc("iditem"));
			return criteri.list();

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Obtiene toda accesibilidad del site
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<?> obtenerAccesibilidadItem(Long id) {

		Session session = this.getSession();
		try {
			Criteria criteri = session.createCriteria(Accesibilidad.class);
			criteri.add(Restrictions.eq("iditem", id));
			return criteri.list();

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Determina si existe accesibilidad del item
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Boolean existeAccesibilidadContenido(Long id) {

		Session session = this.getSession();
		try {
			Criteria criteri = session.createCriteria(Accesibilidad.class);
			criteri.add(Restrictions.eq("iditem", id));
			if (criteri.list().size() > 0) {
				return true;
			} else {
				return false;
			}

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Determina si existe accesibilidad del microsite
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public int existeAccesibilidadMicrosite(Long id) {

		Session session = this.getSession();
		try {
			int nivelXHTML = 0;
			int nivelTaw = 0;
			Criteria criteri = session.createCriteria(Accesibilidad.class);
			criteri.add(Restrictions.eq("codmicro", id));
			List<?> lista = criteri.list();

			if (lista.size() > 0) {
				List<?> Lista = criteri.list();
				Iterator<?> iter = Lista.iterator();

				while (iter.hasNext()) {
					Accesibilidad acces = (Accesibilidad) iter.next();
					if (acces.getTawresultado() != null
							&& Integer.parseInt(acces.getTawresultado()) > nivelTaw) {
						nivelTaw = Integer.parseInt(acces.getTawresultado());
					}

					if (acces.getResultado() != null
							&& Integer.parseInt(acces.getResultado()) > nivelXHTML) {
						nivelXHTML = Integer.parseInt(acces.getResultado());
					}
				}

				if (nivelTaw > 1) {
					return nivelTaw;
				} else if (nivelXHTML > 1) {
					return nivelXHTML + 3;
				} else {
					return 0;
				}
			} else {
				return 0;
			}

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * borra una accesibilidad
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public void borrarAccesibilidad(Long id) {

		Session session = this.getSession();
		try {
			Accesibilidad accesibilidad = (Accesibilidad) session.get(
					Accesibilidad.class, id);
			session.delete(accesibilidad);
			session.flush();
			this.close(session);

			this.grabarAuditoria(accesibilidad.getCodmicro(), accesibilidad,
					Auditoria.ELIMINAR);

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * borra accesibilidad completa del microsite
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public void borrarAccesibilidadMicro(Long id) {

		Session session = this.getSession();
		try {
			Criteria criteri = session.createCriteria(Accesibilidad.class);
			criteri.add(Restrictions.eq("codmicro", id));
			List<?> lista = criteri.list();
			Iterator<?> iter = lista.iterator();

			while (iter.hasNext()) {
				Accesibilidad accesibilidad = (Accesibilidad) iter.next();
				Transaction tx = session.beginTransaction();
				session.delete(accesibilidad);
				session.flush();
				tx.commit();
			}
			this.close(session);

			iter = lista.iterator();
			while (iter.hasNext()) {
				Accesibilidad accesibilidad = (Accesibilidad) iter.next();
				this.grabarAuditoria(accesibilidad.getCodmicro(),
						accesibilidad, Auditoria.ELIMINAR);
			}

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Devuelve un mapa con listados de diferentes elementos (contenidos,
	 * noticias, agendas)
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public HashMap<String, ArrayList<Accesibilidad>> obtenerMapaListados(
			Long idmicrosite) {

		Session session = this.getSession();
		try {
			HashMap<String, ArrayList<Accesibilidad>> maparetorno = new HashMap<String, ArrayList<Accesibilidad>>();

			// obtenemos los contenidos
			String sql = "select contenido, accesi"
					+ " from Contenido contenido, Accesibilidad accesi"
					+ " where accesi.codmicro = " + idmicrosite.toString()
					+ " and accesi.servicio = 'MCS.CNTSP'"
					+ " and accesi.iditem = contenido.id"
					+ " order by contenido.id";

			Query query = session.createQuery(sql);
			ArrayList<Accesibilidad> listaretorno = new ArrayList<Accesibilidad>();
			Iterator<?> iter = query.list().iterator();
			while (iter.hasNext()) {
				Object[] fila = (Object[]) iter.next();
				Contenido conte = (Contenido) fila[0];
				Accesibilidad accesi = (Accesibilidad) fila[1];
				// ponemos servicio el titulo del contenido
				TraduccionContenido tracon = (TraduccionContenido) conte
						.getTraduccion(Idioma.getIdiomaPorDefecto());
				if ((tracon != null) && (tracon.getTitulo() != null)) {
					accesi.setServicio(tracon.getTitulo());
				}
				listaretorno.add(accesi);
			}
			maparetorno.put("kContenidos", listaretorno);

			// obtenemos las noticias
			sql = "select noticia, accesi"
					+ " from Noticia noticia, Accesibilidad accesi"
					+ " where accesi.codmicro = " + idmicrosite.toString()
					+ " and accesi.servicio = 'MCS.NTCS0'"
					+ " and accesi.iditem = noticia.id"
					+ " order by noticia.id";

			query = session.createQuery(sql);
			listaretorno = new ArrayList<Accesibilidad>();
			iter = query.list().iterator();
			while (iter.hasNext()) {
				Object[] fila = (Object[]) iter.next();
				Noticia noticia = (Noticia) fila[0];
				Accesibilidad accesi = (Accesibilidad) fila[1];
				// ponemos servicio el titulo de la noticia
				TraduccionNoticia tranot = (TraduccionNoticia) noticia
						.getTraduccion(Idioma.getIdiomaPorDefecto());
				if ((tranot != null) && (tranot.getTitulo() != null)) {
					accesi.setServicio(tranot.getTitulo());
				}
				listaretorno.add(accesi);
			}
			maparetorno.put("kNoticias", listaretorno);

			// obtenemos los eventos
			sql = "select agenda, accesi"
					+ " from Agenda agenda, Accesibilidad accesi"
					+ " where accesi.codmicro = " + idmicrosite.toString()
					+ " and accesi.servicio = 'MCS.GND00'"
					+ " and accesi.iditem = agenda.id" + " order by agenda.id";

			query = session.createQuery(sql);
			listaretorno = new ArrayList<Accesibilidad>();
			iter = query.list().iterator();
			while (iter.hasNext()) {
				Object[] fila = (Object[]) iter.next();
				Agenda agenda = (Agenda) fila[0];
				Accesibilidad accesi = (Accesibilidad) fila[1];
				// ponemos servicio el titulo de la agenda
				TraduccionAgenda traage = (TraduccionAgenda) agenda
						.getTraduccion(Idioma.getIdiomaPorDefecto());
				if ((traage != null) && (traage.getTitulo() != null)) {
					accesi.setServicio(traage.getTitulo());
				}
				listaretorno.add(accesi);
			}
			maparetorno.put("kAgendas", listaretorno);

			return maparetorno;

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

}
