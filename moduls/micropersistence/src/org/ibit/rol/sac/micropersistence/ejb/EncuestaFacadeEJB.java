package org.ibit.rol.sac.micropersistence.ejb;

import java.util.*;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.rmi.CORBA.UtilDelegate;

import net.sf.hibernate.*;
import net.sf.hibernate.expression.Expression;

import org.ibit.lucene.indra.model.Catalogo;
import org.ibit.lucene.indra.model.ModelFilterObject;
import org.ibit.rol.sac.micromodel.Idioma;
import org.ibit.rol.sac.micromodel.Encuesta;
import org.ibit.rol.sac.micromodel.IndexObject;
import org.ibit.rol.sac.micromodel.Pregunta;
import org.ibit.rol.sac.micromodel.Respuesta;
import org.ibit.rol.sac.micromodel.RespuestaDato;
import org.ibit.rol.sac.micromodel.TraduccionEncuesta;
import org.ibit.rol.sac.micromodel.TraduccionPregunta;
import org.ibit.rol.sac.micromodel.TraduccionRespuesta;
import org.ibit.rol.sac.micromodel.UsuarioEncuesta;
import org.ibit.rol.sac.micromodel.UsuarioPropietarioRespuesta;
import org.ibit.rol.sac.micropersistence.delegate.DelegateException;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;
import org.ibit.rol.sac.micropersistence.delegate.UsuarioEncuestaDelegate;


/**
 * SessionBean para manipular encuestas.
 *
 * @ejb.bean
 *  name="sac/micropersistence/EncuestaFacade"
 *  jndi-name="org.ibit.rol.sac.micropersistence.EncuestaFacade"
 *  type="Stateless"
 *  view-type="remote"
 *  transaction-type="Container"
 *
 * @ejb.transaction type="Required"
 * 
 * @author Indra
 */

public abstract class EncuestaFacadeEJB extends HibernateEJB {

	private static final long serialVersionUID = -8128896105109064587L;

	/**
	 * @ejb.create-method
	 * @ejb.permission unchecked="true"
	 */
	public void ejbCreate() throws CreateException {
		super.ejbCreate();
		try {
			super.langs= DelegateUtil.getIdiomaDelegate().listarLenguajes();
		}
		catch(Exception ex) {
			throw new EJBException(ex);
		}
	}

	/**
	 * Inicializo los par�metros de la consulta....
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public void init(Long site) {
		super.tampagina=10;
		super.pagina=0;
		//super.select="";
		super.select="select enc.id, enc.fcaducidad, enc.fpublicacion, enc.visible, trad.titulo ";
		super.from=" from Encuesta enc join enc.traducciones trad ";
		super.where=" where index(trad)='"+Idioma.DEFAULT+"' and enc.idmicrosite="+site.toString();
		super.whereini=" ";
		super.orderby="";

		super.camposfiltro= new String[] {"trad.titulo"};
		super.cursor=0;
		super.nreg=0;
		super.npags=0;	
	}

	/**
	 * Inicializo los par�metros de la consulta....
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */

	public void initra(Long site, String idiomapasado) {
		super.tampagina=10;
		super.pagina=0;
		//super.select="";
		super.select="select enc.id, enc.fcaducidad, enc.fpublicacion, enc.visible, trad.titulo ";
		super.from=" from Encuesta enc join enc.traducciones trad ";
		super.where=" where (index(trad)='"+Idioma.DEFAULT+"' or index(trad)='"+idiomapasado+"') and enc.idmicrosite="+site.toString();
		super.whereini=" ";
		super.orderby=" order by enc.id,index(trad) desc";

		super.camposfiltro= new String[] {"trad.titulo"};
		super.cursor=0;
		super.nreg=0;
		super.npags=0;	
	}

	/**
	 * Inicializo los par�metros de la consulta....
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public void init() {
		super.tampagina=10;
		super.pagina=0;
		//super.select="";
		super.select="select enc.id, enc.fcaducidad, enc.fpublicacion, enc.visible, trad.titulo ";
		super.from=" from Encuesta enc join enc.traducciones trad ";
		super.where=" where index(trad)='"+Idioma.DEFAULT+"'";
		super.whereini=" ";
		super.orderby="";

		super.camposfiltro= new String[] {"trad.titulo"};
		super.cursor=0;
		super.nreg=0;
		super.npags=0;	
	}    


	/**
	 * Crea o actualiza una encuesta
	 * @ejb.interface-method
	 * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public Long grabarEncuesta(Encuesta enc) {
		Session session = getSession();
		boolean nuevo=false;
		try {
			Transaction tx = session.beginTransaction();
			if (enc.getId()==null) nuevo=true;
			session.saveOrUpdate(enc);
			session.flush();
			tx.commit();

			//if (!nuevo) indexBorraEncuesta(enc.getId());
			//indexInsertaEncuesta(enc, null);

			return enc.getId();
		} catch (HibernateException he) {
			if (!nuevo) indexBorraEncuesta(enc.getId());
			throw new EJBException(he);
		} finally {
			close(session);
		}
	}

	/**
	 * Obtiene una encuesta
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Encuesta obtenerEncuesta(Long id) {
		Session session = getSession();
		try {
			Encuesta enc = (Encuesta) session.load(Encuesta.class, id);
			return enc;
		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			close(session);
		}
	}

	/**
	 * Lista todas las encuestas
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */

	public List<Encuesta> listarEncuestas() {
		Session session = getSession();
		try {
			parametrosCons(); // Establecemos los par�metros de la paginaci�n

			Query query = session.createQuery(select+from+where+orderby);

			ScrollableResults scr = query.scroll();
			ArrayList<Encuesta> lista=new ArrayList<Encuesta>();        	
			scr.first();
			scr.scroll(cursor-1);
			int i = 0;
			while (tampagina > i++) {
				Object[] fila = (Object[]) scr.get();
				Encuesta enc= new Encuesta();
				enc.setId((Long)fila[0]);
				enc.setFcaducidad((java.util.Date)fila[1]);
				enc.setFpublicacion((java.util.Date)fila[2]);
				enc.setVisible((String)fila[3]);

				TraduccionEncuesta trad= new TraduccionEncuesta();
				trad.setTitulo((String)fila[4]);
				enc.setTraduccion(Idioma.DEFAULT,trad);

				lista.add(enc);
				if (!scr.next()) break;
			}
			scr.close();

			return lista;

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			close(session);
		}
	}

	/**
	 * Lista todas las encuestas de recursos
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */

	public List<?> listarEncuestasrec(String idiomapasado) {
		Session session = getSession();
		try {
			parametrosCons(); // Establecemos los par�metros de la paginaci�n

			Query query = session.createQuery(select+from+where+orderby);

			ScrollableResults scr = query.scroll();
			ArrayList<Encuesta> lista=new ArrayList<Encuesta>();        	
			scr.first();
			scr.scroll(cursor-1);
			int i = 0;
			while (tampagina > i++) {
				Object[] fila =(Object[]) scr.get();
				Encuesta enc= new Encuesta();
				enc.setId((Long)fila[0]);
				enc.setFcaducidad((java.util.Date)fila[1]);
				enc.setFpublicacion((java.util.Date)fila[2]);
				enc.setVisible((String)fila[3]);

				TraduccionEncuesta trad= new TraduccionEncuesta();
				trad.setTitulo((String)fila[4]);
				enc.setTraduccion(idiomapasado,trad);

				lista.add(enc);
				if (!scr.next()) break;
			}
			scr.close();

			return lista;

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			close(session);
		}
	}


	/**
	 * borra una encuesta
	 * @ejb.interface-method
	 * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public void borrarEncuesta(Long id) {
		Session session = getSession();
		try {
			Transaction tx = session.beginTransaction();
			Encuesta enc = (Encuesta) session.load(Encuesta.class, id);

			//Primero: En el caso que la encuesta tenga respuestas, borraremos  la relaci�n de usuarios con las respuestas de la encuesta
			List<?> listIdRespu = idRespDeEnc(enc.getId());				
			if (!listIdRespu.isEmpty()){
				Criteria criteriUsuPropiResp = session.createCriteria(UsuarioPropietarioRespuesta.class);
				criteriUsuPropiResp.add(Expression.in("idrespuesta",listIdRespu));

				Iterator<?> iterUsuPropiResp = criteriUsuPropiResp.list().iterator();
				while (iterUsuPropiResp.hasNext()) {
					UsuarioPropietarioRespuesta upm = (UsuarioPropietarioRespuesta)iterUsuPropiResp.next();
					session.delete(upm);
				}
			}

			//Segundo: Borro la  encuesta
			session.delete(enc);

			//indexBorraEncuesta(enc.getId());
			session.flush();

			tx.commit();
		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			close(session);
		}
	}
	/**
	 * Lista todos los Ids de las respuestas de las encuestas.
	 */
	private List<?> idRespDeEnc(Long idEncuesta) {
		Session session = getSession();
		try {

			String hql ="SELECT RESP.Id FROM  Encuesta ENC,  Pregunta PRE, Respuesta RESP WHERE" +
			" RESP.idpregunta = PRE.Id AND PRE.idencuesta = ENC.Id AND ENC.Id =" + idEncuesta.toString();

			Query query = session.createQuery(hql);
			List<?> queryList = query.list();
			return queryList;

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			close(session);
		}
	}

	/**
	 * Crea o actualiza una pregunta
	 * @ejb.interface-method
	 * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public void grabarPregunta(Pregunta pre) {

		Session session = getSession();
		try {
			// Alta Pregunta
			if (pre.getId()==null) {
				Encuesta enc = (Encuesta) session.load(Encuesta.class, pre.getIdencuesta());
				enc.getPreguntas().add(pre);
				session.saveOrUpdate(enc);
			}
			// Modificacion pregunta
			else {
				session.saveOrUpdate(pre);		
			}
			session.flush();

			// Actualizamos el indice
			Encuesta enc = (Encuesta) session.load(Encuesta.class, pre.getIdencuesta());
			indexBorraEncuesta(enc.getId());
			indexInsertaEncuesta(enc, null);

		} catch (HibernateException e) {
			throw new EJBException(e);
		} finally {
			close(session);
		}
	}

	/**
	 * Obtiene una pregunta
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Pregunta obtenerPregunta(Long id) {
		Session session = getSession();
		try {
			Pregunta pre = (Pregunta) session.load(Pregunta.class, id);
			Hibernate.initialize(pre);
			//Hibernate.initialize(pre.getRespuestas());
			return pre;
		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			close(session);
		}
	}    	

	/**
	 * Lista todas las preguntas de una encuesta
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<?> listarPreguntas(Long id) {
		Session session = getSession();
		try {
			String hql=" from Pregunta pre join pre.traducciones trad where index(trad)='"
				+Idioma.DEFAULT+"' and pre.idencuesta=" + id.toString();

			Query query = session.createQuery(hql);
			return query.list();

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			close(session);
		}
	}

	/**
	 * Lista todas las preguntas de una encuesta ordenadas ascendentemente
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<?> listarPreguntasOrdAsc(Long id) {
		Session session = getSession();
		try {
			String hql=" from Pregunta pre join pre.traducciones trad where index(trad)='"
				+Idioma.DEFAULT+"' and pre.idencuesta=" + id.toString() + " order by pre_orden asc";


			Query query = session.createQuery(hql);
			return query.list();

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			close(session);
		}
	}

	/**
	 * Elimina una o varias preguntas de la encuesta
	 * @ejb.interface-method
	 * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public void eliminarPreguntas(String[] idpreguntas, Long encuesta_id) {
		Session session = getSession();
		try {

			Transaction tx = session.beginTransaction();

			Encuesta enc = (Encuesta) session.load(Encuesta.class, encuesta_id);

			//Primero: En el caso que la pregunta tenga respuesta, borraremos  la relaci�n de usuarios con las respuestas de la pregunta
			List<?> listIdRespu = idRespDePreguntas(idpreguntas);				
			if (!listIdRespu.isEmpty()){
				Criteria criteriUsuPropiResp = session.createCriteria(UsuarioPropietarioRespuesta.class);
				criteriUsuPropiResp.add(Expression.in("idrespuesta",listIdRespu));
				Iterator<?> iterUsuPropiResp = criteriUsuPropiResp.list().iterator();
				while (iterUsuPropiResp.hasNext()) {
					UsuarioPropietarioRespuesta upm = (UsuarioPropietarioRespuesta)iterUsuPropiResp.next();
					session.delete(upm);
				}
			}

			// Segundo: borramos la pregunta
			Pregunta pre;
			for (int i=0;i<idpreguntas.length;i++) {
				pre = (Pregunta) session.load(Pregunta.class, new Long (idpreguntas[i]));
				enc.getPreguntas().remove(pre);
				session.delete(pre);
			}
			session.saveOrUpdate(enc);
			session.flush();

			tx.commit();
			// Actualizamos el indice
			enc = (Encuesta) session.load(Encuesta.class, encuesta_id);
			indexBorraEncuesta(enc.getId());
			indexInsertaEncuesta(enc, null);


		} catch (HibernateException e) {
			throw new EJBException(e);
		} finally {
			close(session);
		}
	}    
	private List<?> idRespDePreguntas(String[] idpreguntas) {
		Session session = getSession();
		try {
			List<?> queryList=null;
			String filtro=""; 
			for (int i=0;i<idpreguntas.length;i++) {
				String valor = idpreguntas[i];
				filtro+= "PRE.Id ="+ valor +" OR "; 
			}

			if (filtro.length()>0)
				filtro=filtro.substring(0,filtro.length()-3);
			if(filtro.length() > 0){
				String hql ="SELECT RESP.Id FROM  Encuesta ENC,  Pregunta PRE, Respuesta RESP WHERE" +
				" RESP.idpregunta = PRE.Id AND "+ filtro;

				Query query = session.createQuery(hql);
				queryList = query.list();
			}	

			return queryList;


		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			close(session);
		}
	}
	/**
	 * Crea o actualiza una respuesta
	 * @ejb.interface-method
	 * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public void grabarRespuesta(Respuesta res) {

		Session session = getSession();
		try {
			// Alta Respuesta
			if (res.getId()==null) {
				Pregunta pre = (Pregunta) session.load(Pregunta.class, res.getIdpregunta());
				pre.getRespuestas().add(res);
				session.saveOrUpdate(pre);
			}
			// Modificacion respuesta
			else {
				session.saveOrUpdate(res);		
			}
			session.flush();

			// Actualizamos el indice
			Encuesta enc= obtenerEncuesta(obtenerPregunta(res.getIdpregunta()).getIdencuesta());
			indexBorraEncuesta(enc.getId());
			indexInsertaEncuesta(enc, null);

		} catch (HibernateException e) {
			throw new EJBException(e);
		} finally {
			close(session);
		}
	}

	/**
	 * Obtiene una respuesta
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Respuesta obtenerRespuesta(Long id) {
		Session session = getSession();
		try {
			Respuesta res = (Respuesta) session.load(Respuesta.class, id);
			return res;
		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			close(session);
		}
	}    	

	/**
	 * Obtiene una respuesta libre del usuario
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public RespuestaDato obtenerRespuestaDato(Long idRespuesta, Long idUsuario) {
		Session session = getSession();
		try {
			String hql="from RespuestaDato res where res.idusuari= " + idUsuario.toString() + " and res.idrespueta = " + idRespuesta.toString();

			Query query = session.createQuery(hql);

			return (RespuestaDato)query.uniqueResult();

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			close(session);
		}
	}
	/**
	 * Obtiene la llista de respuestes libre del usuario
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Hashtable<Long,RespuestaDato> listarRespuestasDato(Long idEncuesta, Long idUsuario) {
		Session session = getSession();
		try {
			String hql="from RespuestaDato res where res.idusuari= " + idUsuario.toString() + " and res.idencuesta = " + idEncuesta.toString();

			Query query = session.createQuery(hql);
			Iterator<?> it = query.iterate();
			Hashtable<Long, RespuestaDato> hash = new Hashtable<Long, RespuestaDato>(); 
			while ( it.hasNext() ) {
				RespuestaDato element = (RespuestaDato) it.next();
				hash.put(element.getIdrespueta(),element);
			}			
			return hash;

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			close(session);
		}
	}
	/**
	 * Lista todas las respuestas de una pregunta
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<?> listarRespuestas(Long id) {
		Session session = getSession();
		try {
			String hql=" from Respuesta res join res.traducciones trad where index(trad)='"
				+Idioma.DEFAULT+"' and res.idpregunta=" + id.toString();

			Query query = session.createQuery(hql);
			return query.list();

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			close(session);
		}
	}


	/**
	 * elimina una o varias respuestas de la pregunta
	 * @ejb.interface-method
	 * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public void eliminarRespuestas(String[] idrespuestas, Long pregunta_id) {
		Session session = getSession();
		try {

			Transaction tx = session.beginTransaction();
			Pregunta pre = (Pregunta) session.load(Pregunta.class, pregunta_id);

			//Primero: En el caso que la pregunta tenga respuesta, borraremos  la relaci�n de usuarios con las respuestas de la pregunta
			Collection<Long> collIdRespu = new ArrayList<Long>();
			for (int i=0;i<idrespuestas.length;i++)
				collIdRespu.add(new Long(idrespuestas[i]));				

			if(!collIdRespu.isEmpty()){
				Criteria criteriUsuPropiResp = session.createCriteria(UsuarioPropietarioRespuesta.class);
				criteriUsuPropiResp.add(Expression.in("idrespuesta",collIdRespu));
				Iterator<?> iterUsuPropiResp = criteriUsuPropiResp.list().iterator();
				while (iterUsuPropiResp.hasNext()) {
					UsuarioPropietarioRespuesta upm = (UsuarioPropietarioRespuesta)iterUsuPropiResp.next();
					session.delete(upm);
				}
			}

			//Segundo: Borramos las respuestas de las preguntas.
			Respuesta res;
			for (int i=0;i<idrespuestas.length;i++) {
				res = (Respuesta) session.load(Respuesta.class, new Long (idrespuestas[i]));	
				pre.getRespuestas().remove(res);
				session.delete(res);
			}
			session.saveOrUpdate(pre);
			session.flush();

			tx.commit();
			// Actualizamos el indice
			Encuesta enc= obtenerEncuesta(obtenerPregunta(pregunta_id).getIdencuesta());
			indexBorraEncuesta(enc.getId());
			indexInsertaEncuesta(enc, null);

		} catch (HibernateException e) {
			throw new EJBException(e);
		} finally {
			close(session);
		}
	}        


	/**
	 * Comprueba que el elemento pertenece al Microsite
	 * @ejb.interface-method
	 * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public boolean checkSite(Long site, Long id) {
		Session session = getSession();
		try {
			Query query = session.createQuery("from Encuesta enc where enc.idmicrosite="+site.toString()+" and enc.id="+id.toString());
			return query.list().isEmpty();
		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			close(session);
		}
	}


	/**
	 * Suma 1 al numero de veces que es respondida una respuesta
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public void sumarRespuesta(Long id) {
		Session session = getSession();
		try {
			Respuesta res = (Respuesta) session.load(Respuesta.class, id);
			if (res!=null) {
				if (res.getNrespuestas()!=null) 
					res.setNrespuestas(new Integer(res.getNrespuestas().intValue()+1));
				else res.setNrespuestas(new Integer(1));
			} else {
				res.setNrespuestas(new Integer(0));
			}
			session.saveOrUpdate(res);
			session.flush();
		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			close(session);
		}
	}    

	/**
	 * Suma 1 al numero de veces respondida una pregunta
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public void sumarPregunta(Long id) {
		Session session = getSession();
		try {
			Pregunta pre = (Pregunta) session.load(Pregunta.class, id);
			if (pre!=null) {
				if (pre.getNrespuestas()!=null) 
					pre.setNrespuestas(new Integer(pre.getNrespuestas().intValue()+1));
				else pre.setNrespuestas(new Integer(1));
			} else {
				pre.setNrespuestas(new Integer(0));
			}
			session.saveOrUpdate(pre);
			session.flush();
		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			close(session);
		}

	}  

	/**
	 * Crea o actualiza grabarUsuarioPropietarioRespuesta
	 * @ejb.interface-method
	 *  @ejb.permission unchecked="true"
	 */
	public Long grabarUsuarioPropietarioRespuesta(UsuarioPropietarioRespuesta upm) {
		Session session = getSession();
		try {
			session.save(upm);
			session.flush();
			return upm.getIdusuario();
		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			close(session);
		}
	}

	/**
	 * Obtener todos los usuarios que han seleccionado una respuesta
	 * @ejb.interface-method
	 * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public List<?> obtenerUsuariosRespuesta(Long idRespuesta) {
		Session session = getSession();
		try {
			String hql=" from UsuarioPropietarioRespuesta res where res.idrespuesta=" + idRespuesta.toString();

			Query query = session.createQuery(hql);
			return query.list();

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			close(session);
		}
	}
	
	/**
	 * Obtener todas las respuestas de un usuario
	 * @ejb.interface-method
	 * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public List<?> obtenerRespuestasDeUsuario(Long idUsuario) {
		Session session = getSession();
		try {
			String hql="select res.idrespuesta from UsuarioPropietarioRespuesta res where res.idusuario=" + idUsuario.toString();

			Query query = session.createQuery(hql);
			return query.list();

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			close(session);
		}
	}
	
	/**
     * Obtiene los usuarios de una encuesta.
     * @ejb.interface-method
     * @ejb.permission role-name="${role.system},${role.admin},${role.oper},${role.super}"
     */
    public List<?> obtenerUsuariosEncuesta(Long id) {
        Session session = getSession();
        try {
        	String hql = "select distinct u.idusuario from UsuarioPropietarioRespuesta u where u.idrespuesta in (select r.id from Pregunta p, Respuesta r where r.idpregunta = p.id and p.idencuesta = " + id + ")";        	
        	Query query = session.createQuery(hql);
        	
        	return query.list();
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }

	/**
	 * Obtener el n�mero de votos por respuesta en funci�n de un grupo de usuarios
	 * @ejb.interface-method
	 * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public Hashtable<String, String> obtenerNumVotosByResp(Collection<?> condicioUsu) {
		Session session = getSession();
		Hashtable<String, String> hash = new Hashtable<String, String>(); 
		try {
			String filtro=""; 
			Iterator<?> iter = condicioUsu.iterator();
			while (iter.hasNext()) { 
				String valor = (String) iter.next();
				filtro+= "res.idusuario ="+ valor +" OR "; 
			}

			if (filtro.length()>0)
				filtro=filtro.substring(0,filtro.length()-3);

			if(filtro.length() > 0){

				String hql ="select count(*),res.idrespuesta from UsuarioPropietarioRespuesta res where ("+ filtro +") group by res.idrespuesta";
				Query query = session.createQuery(hql);
				Iterator<?> res=query.iterate();
				while ( res.hasNext() ) {
					Object[] fila = (Object[]) res.next();
					String ncount = "" + fila[0];
					String valor = "" + fila[1];

					if (valor!=null && !valor.equals("null") && valor.length()>0){
						if (ncount!=null && !ncount.equals("null") && ncount.length()>0 ) {
							hash.put(valor, ncount);
						}
					}
				}
			}
			return hash;
		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			close(session);
		}
	}


	/***************************************************************************************/
	/*******************             INDEXACION         ************************************/
	/***************************************************************************************/        

	/**
	 * A�ade la encuesta al indice en todos los idiomas
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true" 
	 */
	public void indexInsertaEncuesta(Encuesta enc, ModelFilterObject filter)  {

		IndexObject io= new IndexObject();
		try {

			if (filter==null) 
				filter=DelegateUtil.getMicrositeDelegate().obtenerFilterObject(enc.getIdmicrosite());

			if (filter!=null) {
				if (filter.getBuscador().equals("N")) return;
			}

			for (int i = 0; i < langs.size(); i++) {
				String idi = (String) langs.get(i);



				io = new IndexObject();
				io.setId(Catalogo.SRVC_MICRO_ENCUESTAS + "." + enc.getId());
				io.setClasificacion(Catalogo.SRVC_MICRO_ENCUESTAS);				


				io.setMicro(filter.getMicrosite_id());
				io.setRestringido(filter.getRestringido());
				io.setUo(filter.getUo_id());
				io.setMateria(filter.getMateria_id());
				io.setSeccion(filter.getSeccion_id());
				io.setFamilia(filter.getFamilia_id());				

				io.setTitulo("");
				io.setUrl("/sacmicrofront/encuesta.do?lang="+idi+"&idsite="+enc.getIdmicrosite().toString()+"&cont="+enc.getId());
				io.setCaducidad("");
				io.setPublicacion("");
				io.setDescripcion("");
				io.setTituloserviciomain(filter.getTraduccion(idi).getMaintitle());				


				if (enc.getFcaducidad()!=null)		io.setCaducidad(new java.text.SimpleDateFormat("yyyyMMdd").format(enc.getFcaducidad()));
				if (enc.getFpublicacion()!=null)	io.setPublicacion(new java.text.SimpleDateFormat("yyyyMMdd").format(enc.getFpublicacion()));


				TraduccionEncuesta trad=(TraduccionEncuesta)enc.getTraduccion(idi);
				if (trad!=null)	{
					io.addTextLine(trad.getTitulo());
					io.setTitulo(trad.getTitulo());

					io.addTextopcionalLine(filter.getTraduccion(idi).getMateria_text());
					io.addTextopcionalLine(filter.getTraduccion(idi).getSeccion_text());
					io.addTextopcionalLine(filter.getTraduccion(idi).getUo_text());				   	
					io.addDescripcionLine(trad.getTitulo());
				}

				Iterator<?> itpreg =  enc.getPreguntas().iterator();
				while (itpreg.hasNext()) {
					Pregunta pre = (Pregunta)itpreg.next();

					TraduccionPregunta trad1=((TraduccionPregunta)pre.getTraduccion(idi));
					if (trad1!=null) io.addTextLine(trad1.getTitulo());

					Iterator<?> itresp=pre.getRespuestas().iterator();
					while (itresp.hasNext()) {
						Respuesta res = (Respuesta)itresp.next();

						TraduccionRespuesta trad2=((TraduccionRespuesta)res.getTraduccion(idi));
						if (trad2!=null) io.addTextLine(trad2.getTitulo());
					}
				}

				if (io.getText().length()>0)
					DelegateUtil.getIndexerDelegate().insertaObjeto(io, idi);
			}
		}
		catch (DelegateException ex) {
			log.warn("[indexInsertaEncuesta:" + enc.getId() + "] No se ha podido indexar encuesta. " + ex.getMessage());
			//throw new EJBException(ex);
		}
		catch (Exception e) {
			log.warn("[indexInsertaEncuesta:" + enc.getId() + "] No se ha podido indexar elemento. " + e.getMessage());
		}

	}

	/**
	 * Elimina la encuesta en el indice en todos los idiomas
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true" 
	 */
	public void indexBorraEncuesta(Long id)  {

		try {
			for (int i = 0; i < langs.size(); i++) {
				DelegateUtil.getIndexerDelegate().borrarObjeto(Catalogo.SRVC_MICRO_ENCUESTAS + "." + id, ""+langs.get(i));
			}
		}
		catch (DelegateException ex) {
			throw new EJBException(ex);
		}

	}
 
	
}