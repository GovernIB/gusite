package es.caib.gusite.micropersistence.ejb;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import es.caib.gusite.micromodel.RespuestaDato;

/**
 * SessionBean para manipular datos de las respuestas.
 * 
 * @ejb.bean name="sac/micropersistence/RespuestaDatoFacade"
 *           jndi-name="es.caib.gusite.micropersistence.RespuestaDatoFacade"
 *           type="Stateless" view-type="remote" transaction-type="Container"
 * 
 * @ejb.transaction type="Required"
 */
public abstract class RespuestaDatoFacadeEJB extends HibernateEJB {

	private static final long serialVersionUID = 6661917618076931960L;

	/**
	 * @ejb.create-method
	 * @ejb.permission unchecked="true"
	 */
	@Override
	public void ejbCreate() throws CreateException {
		super.ejbCreate();
	}

	/**
	 * Crea o actualiza una respuesta dato
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public void grabarRespuestaDato(RespuestaDato resdat) {

		Session session = this.getSession();
		try {
			Transaction tx = session.beginTransaction();
			session.save(resdat);
			session.flush();
			tx.commit();

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Lista todas las respuestas datos de una encuesta
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public List<?> listarDatosByEnc(Long id) {

		Session session = this.getSession();
		try {
			String hql = " from RespuestaDato resdat where resdat.idencuesta = "
					+ id.toString();
			Query query = session.createQuery(hql);
			return query.list();

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Lista todas las respuestas datos de una pregunta
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public List<?> listarDatosByPreg(Long id) {

		Session session = this.getSession();
		try {
			String hql = " from RespuestaDato resdat where resdat.idpregunta = "
					+ id.toString();
			Query query = session.createQuery(hql);
			return query.list();

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Lista todas las respuestas datos de una respuesta
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public List<?> listarDatosByResp(Long id) {

		Session session = this.getSession();
		try {
			String hql = " from RespuestaDato resdat where resdat.idrespueta = "
					+ id.toString();
			Query query = session.createQuery(hql);
			return query.list();

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Lista todas las respuestas datos de una respuesta. Devuelve un listado
	 * agrupado por datos, ordenado descendentemente por numero de votos.
	 * Devuelve un hash donde la 'clave' es el dato y el 'valor' es el numero de
	 * repeticiones
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public ArrayList<RespuestaDato> listarDatosByResp2(Long id) {

		ArrayList<RespuestaDato> listadatos = new ArrayList<RespuestaDato>();
		Session session = this.getSession();
		try {
			String hql = " select resdat.dato, count(resdat.dato) from RespuestaDato resdat where resdat.idrespueta = "
					+ id.toString()
					+ " group by resdat.dato order by count(resdat.dato) desc";

			Query query = session.createQuery(hql);
			Iterator<?> res = query.iterate();
			while (res.hasNext()) {
				Object[] fila = (Object[]) res.next();

				RespuestaDato resdat = new RespuestaDato();
				String valor = "" + fila[0];
				String ncount = "" + fila[1];
				if (valor == null || valor.equals("null")
						|| valor.length() == 0) {
					valor = "[vacio]";
				}
				resdat.setDato(valor);
				if (ncount != null && !ncount.equals("null")
						&& ncount.length() > 0 && !ncount.equals("0")) {
					resdat.setIdrespueta(new Long(ncount)); // se utiliza la
															// propiedad
															// idrespuesta para
															// almacenar las
															// repeticiones
					listadatos.add(resdat);
				}
			}
			return listadatos;

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

}
