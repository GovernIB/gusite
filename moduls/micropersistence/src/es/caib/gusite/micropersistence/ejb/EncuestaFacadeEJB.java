package es.caib.gusite.micropersistence.ejb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.Transaction;

import es.caib.gusite.micromodel.Archivo;
import es.caib.gusite.micromodel.Auditoria;
import es.caib.gusite.micromodel.Encuesta;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.Pregunta;
import es.caib.gusite.micromodel.Respuesta;
import es.caib.gusite.micromodel.RespuestaDato;
import es.caib.gusite.micromodel.TraduccionEncuesta;
import es.caib.gusite.micromodel.TraduccionMicrosite;
import es.caib.gusite.micromodel.TraduccionPregunta;
import es.caib.gusite.micromodel.TraduccionRespuesta;
import es.caib.gusite.micromodel.UsuarioPropietarioRespuesta;
import es.caib.gusite.micropersistence.delegate.ArchivoDelegate;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.MicrositeDelegate;
import es.caib.gusite.micropersistence.delegate.SolrPendienteDelegate;
import es.caib.gusite.micropersistence.util.SolrPendienteResultado;
import es.caib.gusite.plugins.PluginFactory;
import es.caib.gusite.plugins.organigrama.OrganigramaProvider;
import es.caib.gusite.plugins.organigrama.UnidadData;
import es.caib.gusite.plugins.organigrama.UnidadListData;
import es.caib.solr.api.SolrIndexer;
import es.caib.solr.api.model.IndexData;
import es.caib.solr.api.model.MultilangLiteral;
import es.caib.solr.api.model.PathUO;
import es.caib.solr.api.model.types.EnumAplicacionId;
import es.caib.solr.api.model.types.EnumCategoria;
import es.caib.solr.api.model.types.EnumIdiomas;

/**
 * SessionBean para manipular encuestas.
 * 
 * @ejb.bean name="sac/micropersistence/EncuestaFacade"
 *           jndi-name="es.caib.gusite.micropersistence.EncuestaFacade"
 *           type="Stateless" view-type="remote" transaction-type="Container"
 * 
 * @ejb.transaction type="Required"
 * 
 * @author Indra
 */
@SuppressWarnings({"unchecked", "deprecation"})
public abstract class EncuestaFacadeEJB extends HibernateEJB {

	private static final long serialVersionUID = -8128896105109064587L;

	/**
	 * @ejb.create-method
	 * @ejb.permission unchecked="true"
	 */
	public void ejbCreate() throws CreateException {
		super.ejbCreate();
		try {
			super.langs = DelegateUtil.getIdiomaDelegate().listarLenguajes();
		} catch (Exception ex) {
			throw new EJBException(ex);
		}
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
		// super.select="";
		super.select = "select enc.id, enc.fcaducidad, enc.fpublicacion, enc.visible, trad.titulo ";
		super.from = " from Encuesta enc join enc.traducciones trad ";
		super.where = " where trad.id.codigoIdioma = '"
				+ Idioma.getIdiomaPorDefecto() + "' and enc.idmicrosite = "
				+ site.toString();
		super.whereini = " ";
		super.orderby = "";

		super.camposfiltro = new String[] { "trad.titulo" };
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
	public void initra(Long site, String idiomapasado) {
		super.tampagina = 10;
		super.pagina = 0;
		// super.select="";
		super.select = "select enc.id, enc.fcaducidad, enc.fpublicacion, enc.visible, trad.titulo ";
		super.from = " from Encuesta enc join enc.traducciones trad ";
		super.where = " where (trad.id.codigoIdioma = '"
				+ Idioma.getIdiomaPorDefecto()
				+ "' or trad.id.codigoIdioma = '" + idiomapasado
				+ "') and enc.idmicrosite = " + site.toString();
		super.whereini = " ";
		super.orderby = " order by enc.id,trad.id.codigoIdioma desc";

		super.camposfiltro = new String[] { "trad.titulo" };
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
		// super.select="";
		super.select = "select enc.id, enc.fcaducidad, enc.fpublicacion, enc.visible, trad.titulo ";
		super.from = " from Encuesta enc join enc.traducciones trad ";
		super.where = " where trad.id.codigoIdioma = '"
				+ Idioma.getIdiomaPorDefecto() + "'";
		super.whereini = " ";
		super.orderby = "";

		super.camposfiltro = new String[] { "trad.titulo" };
		super.cursor = 0;
		super.nreg = 0;
		super.npags = 0;
	}

	/**
	 * Crea o actualiza una encuesta
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public Long grabarEncuesta(Encuesta enc) throws DelegateException {

		Session session = this.getSession();
		try { 
			boolean nuevo = (enc.getId() == null) ? true : false;
			Transaction tx = session.beginTransaction();

			Map<String, TraduccionEncuesta> listaTraducciones = new HashMap<String, TraduccionEncuesta>();

			if (nuevo) {
				Iterator<TraduccionEncuesta> it = enc.getTraducciones()
						.values().iterator();
				while (it.hasNext()) {
					TraduccionEncuesta trd = it.next();
					listaTraducciones.put(trd.getId().getCodigoIdioma(), trd);
				}
				enc.setTraducciones(null);
			}  else {// #28 Comentario sobre que da error borrando la URI de una traduccion
				String listIdiomaBorrar = "";
				Iterator<TraduccionEncuesta> it = enc.getTraducciones()
						.values().iterator();
				while (it.hasNext()) {
					TraduccionEncuesta trd = it.next();
					listIdiomaBorrar += "'" +trd.getId().getCodigoIdioma()+"'";
					if(it.hasNext()){
						listIdiomaBorrar += "," ;						
					}
				}
				// Borramos los idiomas que no pertenecen a contenido y existen en la BBDD
				if(!listIdiomaBorrar.isEmpty()){ 
					Query query = session.createQuery("select tradEnc from TraduccionEncuesta tradEnc where tradEnc.id.codigoEncuesta = " + enc.getId() + " and tradEnc.id.codigoIdioma not in (" + listIdiomaBorrar + ") ");
					final List<TraduccionEncuesta> traduciones = query.list();
					for(TraduccionEncuesta traducI : traduciones ) {
						session.delete(traducI);	
					}
					session.flush();
				}				
			}

			session.saveOrUpdate(enc);
			session.flush();

			if (nuevo) {
				for (TraduccionEncuesta trad : listaTraducciones.values()) {
					trad.getId().setCodigoEncuesta(enc.getId());
					session.saveOrUpdate(trad);
				}
				session.flush();
				enc.setTraducciones(listaTraducciones);
			}

			tx.commit();
			this.close(session);

			int op = (nuevo) ? Auditoria.CREAR : Auditoria.MODIFICAR;
			this.grabarAuditoria(enc, op);

			//Indexamos
			SolrPendienteDelegate pendienteDel = DelegateUtil.getSolrPendienteDelegate();
			pendienteDel.grabarSolrPendiente(EnumCategoria.GUSITE_ENCUESTA.toString(), enc.getId(), null, 1L);
			
			return enc.getId();

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Obtiene una encuesta
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Encuesta obtenerEncuesta(Long id) {

		Session session = this.getSession();
		try {
			Encuesta enc = (Encuesta) session.get(Encuesta.class, id);
			session.refresh(enc);
			return enc;

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}
	

	/**
	 * Obtiene una encuesta a partir de la URI
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Encuesta obtenerEncuestaDesdeUri(String idioma, String uri, String site) {

		Session session = this.getSession();
		try {
			Query query;
			if (idioma != null) {
				query = session
						.createQuery("select encuesta from Encuesta encuesta JOIN encuesta.traducciones te where te.id.codigoIdioma = :idioma and te.uri = :uri and encuesta.idmicrosite = :site");
				query.setParameter("idioma", idioma);
			} else {
				query = session
						.createQuery("select encuesta from Encuesta encuesta JOIN encuesta.traducciones te where te.uri = :uri and encuesta.idmicrosite = :site");				
			}
			query.setParameter("uri", uri);
			query.setParameter("site",Long.valueOf(site));
			
			query.setMaxResults(1);
			return (Encuesta) query.uniqueResult();
		} catch (ObjectNotFoundException oNe) {
			log.error(oNe.getMessage());
			return new Encuesta();
		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Lista todas las encuestas
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<Encuesta> listarEncuestas() {

		Session session = this.getSession();
		try {
			this.parametrosCons(); // Establecemos los parámetros de la
									// paginación

			Query query = session.createQuery(this.select + this.from
					+ this.where + this.orderby);

			ScrollableResults scr = query.scroll();
			ArrayList<Encuesta> lista = new ArrayList<Encuesta>();
			scr.first();
			scr.scroll(this.cursor - 1);
			int i = 0;
			while (this.tampagina > i++) {
				Object[] fila = (Object[]) scr.get();
				Encuesta enc = new Encuesta();
				enc.setId((Long) fila[0]);
				enc.setFcaducidad((java.util.Date) fila[1]);
				enc.setFpublicacion((java.util.Date) fila[2]);
				enc.setVisible((String) fila[3]);

				TraduccionEncuesta trad = new TraduccionEncuesta();
				trad.setTitulo((String) fila[4]);
				enc.setTraduccion(Idioma.getIdiomaPorDefecto(), trad);

				lista.add(enc);
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
	 * Lista todas las encuestas de recursos
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<?> listarEncuestasrec(String idiomapasado) {

		Session session = this.getSession();
		try {
			this.parametrosCons(); // Establecemos los parámetros de la
									// paginación
			Query query = session.createQuery(this.select + this.from
					+ this.where + this.orderby);

			ScrollableResults scr = query.scroll();
			ArrayList<Encuesta> lista = new ArrayList<Encuesta>();
			scr.first();
			scr.scroll(this.cursor - 1);
			int i = 0;
			while (this.tampagina > i++) {
				Object[] fila = (Object[]) scr.get();
				Encuesta enc = new Encuesta();
				enc.setId((Long) fila[0]);
				enc.setFcaducidad((java.util.Date) fila[1]);
				enc.setFpublicacion((java.util.Date) fila[2]);
				enc.setVisible((String) fila[3]);

				TraduccionEncuesta trad = new TraduccionEncuesta();
				trad.setTitulo((String) fila[4]);
				enc.setTraduccion(idiomapasado, trad);

				lista.add(enc);
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
	 * borra una encuesta
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public void borrarEncuesta(Long id) throws DelegateException {

		Session session = this.getSession();
		
		try {
			
			Transaction tx = session.beginTransaction();
			Encuesta encuesta = (Encuesta) session.get(Encuesta.class, id);

			List<Pregunta> preguntas = session.createQuery("from Pregunta where idencuesta = " + id).list();
			for (int p = 0; p < preguntas.size(); p++) {
								
				List<?> respuestas = this.listarRespuestas(new Long(preguntas.get(p).getId()));

				for (int i = 0; i < respuestas.size(); i++) {
					session.createQuery("delete from UsuarioPropietarioRespuesta where id.idrespuesta = " 
							+ ((Respuesta) respuestas.get(i)).getId())
							.executeUpdate();
				}

				// Segundo: Borramos las respuestas de las preguntas.
				for (int i = 0; i < respuestas.size(); i++) {
					session.createQuery("delete from TraduccionRespuesta where id.codigoRespuesta = "
							+ ((Respuesta) respuestas.get(i)).getId())
							.executeUpdate();
					
					session.createQuery(
							"delete from RespuestaDato where idrespueta = " + ((Respuesta) respuestas.get(i)).getId())
							.executeUpdate();
					
					session.createQuery("delete from Respuesta where id = "
							+ ((Respuesta) respuestas.get(i)).getId())
							.executeUpdate();
				}

				session.createQuery("delete from TraduccionPregunta where id.codigoPregunta = " + preguntas.get(p).getId())
						.executeUpdate();
				session.createQuery("delete from Pregunta where id = " + preguntas.get(p).getId())
						.executeUpdate();
				
				// Borrar imagen asociada a pregunta, si existe.
				if (preguntas.get(p).getImagen() != null)
					DelegateUtil.getArchivoDelegate().borrarArchivo(preguntas.get(p).getImagen().getId());
				
			}

			session.createQuery("delete from TraduccionEncuesta where id.codigoEncuesta = " + id)
					.executeUpdate();
			session.createQuery("delete from Encuesta where id = " + id)
					.executeUpdate();

			session.flush();
			tx.commit();
			this.close(session);

			this.grabarAuditoria(encuesta, Auditoria.ELIMINAR);
			
			//DesIndexamos
			SolrPendienteDelegate pendienteDel = DelegateUtil.getSolrPendienteDelegate();
			pendienteDel.grabarSolrPendiente(EnumCategoria.GUSITE_ENCUESTA.toString(), encuesta.getId(), null, 0L);

		} catch (HibernateException he) {
			
			throw new EJBException(he);
			
		} finally {
			
			this.close(session);
			
		}
		
	}

	/**
	 * Crea o actualiza una pregunta
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public void grabarPregunta(Pregunta pre) throws DelegateException {
		
		Session session = this.getSession();
		Boolean nuevo = false;
		Pregunta preOriginal = null;
		
		ArchivoDelegate archivoDelegate = DelegateUtil.getArchivoDelegate();
		Archivo imagen = null;
		List<Archivo> archivosPorBorrar = new ArrayList<Archivo>();
		
		try {
			
			if (pre.getId() == null) {
				nuevo = true;
			}

			Transaction tx = session.beginTransaction();
			Map<String, TraduccionPregunta> listaTraducciones = new HashMap<String, TraduccionPregunta>();

			if (nuevo) {
				
				Iterator<TraduccionPregunta> it = pre.getTraducciones().values().iterator();
				while (it.hasNext()) {
					TraduccionPregunta trd = it.next();
					listaTraducciones.put(trd.getId().getCodigoIdioma(), trd);
				}
				pre.setTraducciones(null);
				
				if (pre.getImagen() != null) {
					
					imagen = pre.getImagen();
					pre.setImagen(null);
										
				}
			
			} else {
				
				preOriginal = this.obtenerPregunta(pre.getId());
				
				if (pre.getImagen() != null) {
					if (pre.getImagen().getId() != null)
						archivoDelegate.grabarArchivo(pre.getImagen());
					else
						archivoDelegate.insertarArchivo(pre.getImagen());
				} else {
					// Archivo a null pero anterior no lo era: solicitan borrado 
					if (preOriginal.getImagen() != null) {
						archivosPorBorrar.add(preOriginal.getImagen());
					}
				}
				
			}

			session.saveOrUpdate(pre);
			session.flush();

			if (nuevo) {
				
				for (TraduccionPregunta trad : listaTraducciones.values()) {
					trad.getId().setCodigoPregunta(pre.getId());
					session.saveOrUpdate(trad);
				}
				session.flush();
				pre.setTraducciones(listaTraducciones);
				
				if (imagen != null) {
					archivoDelegate.insertarArchivo(imagen);
				}
				
				pre.setImagen(imagen);
				
				session.saveOrUpdate(pre);
				session.flush();
				
			}

			tx.commit();
			
			// Borramos archivo FK de la pregunta solicitado para borrar.
			if (archivosPorBorrar.size() > 0)
				archivoDelegate.borrarArchivos(archivosPorBorrar);

			// Actualizamos el indice
			Encuesta enc = (Encuesta) session.get(Encuesta.class, pre.getIdencuesta());
			int op = (nuevo) ? Auditoria.CREAR : Auditoria.MODIFICAR;
			this.grabarAuditoria(enc.getIdmicrosite(), pre, op);

		} catch (HibernateException e) {
			
			throw new EJBException(e);
			
		} finally {
			
			this.close(session);
			
		}
		
	}

	/**
	 * Obtiene una pregunta
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Pregunta obtenerPregunta(Long id) {

		Session session = this.getSession();
		try {
			Pregunta pre = (Pregunta) session.get(Pregunta.class, id);
			Hibernate.initialize(pre);
			return pre;

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Lista todas las preguntas de una encuesta
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<?> listarPreguntas(Long id) {

		Session session = this.getSession();
		try {
			String hql = "select pre" + " from Pregunta pre"
					+ " join pre.traducciones trad"
					+ " where trad.id.codigoIdioma = '"
					+ Idioma.getIdiomaPorDefecto() + "'"
					+ " and pre.idencuesta = " + id.toString();

			Query query = session.createQuery(hql);
			return query.list();

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Lista todas las preguntas de una encuesta ordenadas ascendentemente
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<?> listarPreguntasOrdAsc(Long id) {

		Session session = this.getSession();
		try {
			String hql = "select pre" + " from Pregunta pre"
					+ " join pre.traducciones trad"
					+ " where trad.id.codigoIdioma = '"
					+ Idioma.getIdiomaPorDefecto() + "'"
					+ " and pre.idencuesta = " + id.toString()
					+ " order by pre.orden asc";

			Query query = session.createQuery(hql);
			return query.list();

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Elimina una o varias preguntas de la encuesta
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public void eliminarPreguntas(String[] idpreguntas, Long encuesta_id)
			throws DelegateException {
		
		Session session = this.getSession();
		
		try {
			
			Transaction tx = session.beginTransaction();
			List<Pregunta> preguntas = new ArrayList<Pregunta>();
			
			for (String idpregunta : idpreguntas) {
				
				List<?> respuestas = this.listarRespuestas(new Long(idpregunta));
				preguntas.add((Pregunta) session.get(Pregunta.class, Long.parseLong(idpregunta)));

				for (int i = 0; i < respuestas.size(); i++) {
					session.createQuery("delete from UsuarioPropietarioRespuesta where id.idrespuesta = "
							+ ((Respuesta) respuestas.get(i)).getId()).executeUpdate();
				}

				// Segundo: Borramos las respuestas de las preguntas.
				for (int i = 0; i < respuestas.size(); i++) {
					session.createQuery("delete from TraduccionRespuesta where id.codigoRespuesta = "
							+ ((Respuesta) respuestas.get(i)).getId()).executeUpdate();
					session.createQuery("delete from Respuesta where id = "
							+ ((Respuesta) respuestas.get(i)).getId()).executeUpdate();
				}

				session.createQuery("delete from TraduccionPregunta where id.codigoPregunta = "
						+ Long.parseLong(idpregunta)).executeUpdate();
				session.createQuery("delete from Pregunta where id = "
						+ Long.parseLong(idpregunta)).executeUpdate();
				
			}

			session.flush();
			tx.commit();			
			
			// Actualizamos el indice
			Encuesta enc = (Encuesta) session.get(Encuesta.class, encuesta_id);
			this.close(session);

			for (Pregunta p : preguntas) {
				
				// Borramos archivos asociados.
				if (p.getImagen() != null)
					DelegateUtil.getArchivoDelegate().borrarArchivo(p.getImagen().getId());
				
				// Grabamos operación de borrado.
				this.grabarAuditoria(enc.getIdmicrosite(), p, Auditoria.ELIMINAR);
				
			}

		} catch (HibernateException e) {
			
			throw new EJBException(e);
			
		} finally {
			
			this.close(session);
			
		}
		
	}

	/**
	 * Crea o actualiza una respuesta
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public void grabarRespuesta(Respuesta res) throws DelegateException {

		Session session = this.getSession();
		try {
			boolean nuevo = (res.getId() == null) ? true : false;

			Transaction tx = session.beginTransaction();
			Map<String, TraduccionRespuesta> listaTraducciones = new HashMap<String, TraduccionRespuesta>();

			if (nuevo) {
				Iterator<TraduccionRespuesta> it = res.getTraducciones()
						.values().iterator();
				while (it.hasNext()) {
					TraduccionRespuesta trd = it.next();
					listaTraducciones.put(trd.getId().getCodigoIdioma(), trd);
				}
				res.setTraducciones(null);
			}

			session.saveOrUpdate(res);
			session.flush();

			if (nuevo) {
				for (TraduccionRespuesta trad : listaTraducciones.values()) {
					trad.getId().setCodigoRespuesta(res.getId());
					session.saveOrUpdate(trad);
				}
				session.flush();
				res.setTraducciones(listaTraducciones);
			}

			tx.commit();
			// Actualizamos el indice
			Encuesta enc = this.obtenerEncuesta(this.obtenerPregunta(
					res.getIdpregunta()).getIdencuesta());
			this.close(session);

			int op = (nuevo) ? Auditoria.CREAR : Auditoria.MODIFICAR;
			this.grabarAuditoria(enc.getIdmicrosite(), res, op);

		} catch (HibernateException e) {
			throw new EJBException(e);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Obtiene una respuesta
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Respuesta obtenerRespuesta(Long id) {

		Session session = this.getSession();
		try {
			Respuesta res = (Respuesta) session.get(Respuesta.class, id);
			return res;

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Obtiene una respuesta libre del usuario
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public RespuestaDato obtenerRespuestaDato(Long idRespuesta, Long idUsuario) {

		Session session = this.getSession();
		try {
			String hql = "from RespuestaDato res" + " where res.idusuari = "
					+ idUsuario.toString() + " and res.idrespueta = "
					+ idRespuesta.toString();

			Query query = session.createQuery(hql);
			return (RespuestaDato) query.uniqueResult();

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Obtiene la llista de respuestes libre del usuario
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Hashtable<Long, RespuestaDato> listarRespuestasDato(Long idEncuesta,
			Long idUsuario) {

		Session session = this.getSession();
		try {
			String hql = "from RespuestaDato res" + " where res.idusuari = "
					+ idUsuario.toString() + " and res.idencuesta = "
					+ idEncuesta.toString();

			Query query = session.createQuery(hql);
			List<RespuestaDato> list = query.list();
            Hashtable<Long, RespuestaDato> hash = new Hashtable<Long, RespuestaDato>();
            for (RespuestaDato res : list) {
                hash.put(res.getIdrespueta(), res);
            }
			return hash;

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Lista todas las respuestas de una pregunta
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<?> listarRespuestas(Long id) {

		Session session = this.getSession();
		try {
			String hql = "select res" + " from Respuesta res"
					+ " join res.traducciones trad"
					+ " where trad.id.codigoIdioma = '"
					+ Idioma.getIdiomaPorDefecto() + "'"
					+ " and res.idpregunta=" + id.toString();

			Query query = session.createQuery(hql);
			return query.list();

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * elimina una o varias respuestas de la pregunta
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public void eliminarRespuestas(String[] idrespuestas, Long pregunta_id)
			throws DelegateException {

		Session session = this.getSession();
		try {
			Transaction tx = session.beginTransaction();

			// Primero: En el caso que la pregunta tenga respuesta, borraremos
			// la relación de usuarios con las respuestas de la pregunta
			for (String idrespuesta : idrespuestas) {
				session.createQuery(
						"delete from UsuarioPropietarioRespuesta where id.idrespuesta = "
								+ idrespuesta).executeUpdate();
			}

			List<Respuesta> respuestas = new ArrayList<Respuesta>();
			// Segundo: Borramos las respuestas de las preguntas.
			for (String idrespuesta : idrespuestas) {
				respuestas.add((Respuesta) session.get(Respuesta.class,
						Long.parseLong(idrespuesta)));
				session.createQuery(
						"delete from TraduccionRespuesta where id.codigoRespuesta = "
								+ idrespuesta).executeUpdate();
				
				session.createQuery(
						"delete from RespuestaDato where idrespueta = " + idrespuesta)
						.executeUpdate();
				
				session.createQuery(
						"delete from Respuesta where id = " + idrespuesta)
						.executeUpdate();
			}

			session.flush();
			tx.commit();
			// Actualizamos el indice
			Encuesta enc = this.obtenerEncuesta(this.obtenerPregunta(
					pregunta_id).getIdencuesta());
			this.close(session);

			for (Respuesta r : respuestas) {
				this.grabarAuditoria(enc.getIdmicrosite(), r,
						Auditoria.ELIMINAR);
			}

		} catch (HibernateException e) {
			throw new EJBException(e);
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
					.createQuery("from Encuesta enc where enc.idmicrosite = "
							+ site.toString() + " and enc.id = "
							+ id.toString());
			return query.list().isEmpty();

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Suma 1 al numero de veces que es respondida una respuesta
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public void sumarRespuesta(Long id) {

		Session session = this.getSession();
		try {
			Respuesta res = (Respuesta) session.get(Respuesta.class, id);
			if (res != null) {
				if (res.getNrespuestas() != null) {
					res.setNrespuestas(new Integer(res.getNrespuestas()
							.intValue() + 1));
				} else {
					res.setNrespuestas(new Integer(1));
				}
			}
			session.saveOrUpdate(res);
			session.flush();

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Suma 1 al numero de veces respondida una pregunta
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public void sumarPregunta(Long id) {

		Session session = this.getSession();
		try {
			Pregunta pre = (Pregunta) session.get(Pregunta.class, id);
			if (pre != null) {
				if (pre.getNrespuestas() != null) {
					pre.setNrespuestas(new Integer(pre.getNrespuestas()
							.intValue() + 1));
				} else {
					pre.setNrespuestas(new Integer(1));
				}
				session.saveOrUpdate(pre);
				session.flush();
			}

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Crea o actualiza grabarUsuarioPropietarioRespuesta
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Long grabarUsuarioPropietarioRespuesta(
			UsuarioPropietarioRespuesta upm) {

		Session session = this.getSession();
		try {
			session.save(upm);
			session.flush();
			return upm.getId().getIdusuario();

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Obtener todos los usuarios que han seleccionado una respuesta
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public List<?> obtenerUsuariosRespuesta(Long idRespuesta) {

		Session session = this.getSession();
		try {
			String hql = " from UsuarioPropietarioRespuesta res where res.id.idrespuesta = "
					+ idRespuesta.toString();
			Query query = session.createQuery(hql);
			return query.list();

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Obtener todas las respuestas de un usuario
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public List<?> obtenerRespuestasDeUsuario(Long idUsuario) {

		Session session = this.getSession();
		try {
			String hql = "select res.id.idrespuesta from UsuarioPropietarioRespuesta res where res.id.idusuario = "
					+ idUsuario.toString();
			Query query = session.createQuery(hql);
			return query.list();

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Obtiene los usuarios de una encuesta.
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.oper},${role.super}"
	 */
	public List<?> obtenerUsuariosEncuesta(Long id) {

		Session session = this.getSession();
		try {
			String hql = "select distinct u.id.idusuario"
					+ " from UsuarioPropietarioRespuesta u"
					+ " where u.id.idrespuesta in (" + "select r.id"
					+ " from Pregunta p, Respuesta r"
					+ " where r.idpregunta = p.id" + " and p.idencuesta = "
					+ id + ")";

			Query query = session.createQuery(hql);
			return query.list();

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Obtener el número de votos por respuesta en función de un grupo de
	 * usuarios
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public Hashtable<String, String> obtenerNumVotosByResp(
			Collection<?> condicioUsu) {

		Session session = this.getSession();
		Hashtable<String, String> hash = new Hashtable<String, String>();
		try {
			String filtro = "";
			Iterator<?> iter = condicioUsu.iterator();
			while (iter.hasNext()) {
				String valor = (String) iter.next();
				filtro += "res.id.idusuario =" + valor + " OR ";
			}

			if (filtro.length() > 0) {
				filtro = filtro.substring(0, filtro.length() - 3);
			}

			if (filtro.length() > 0) {
				String hql = "select count(*), res.id.idrespuesta"
						+ " from UsuarioPropietarioRespuesta res" + " where ("
						+ filtro + ")" + " group by res.id.idrespuesta";

				Query query = session.createQuery(hql);
				Iterator<?> res = query.iterate();
				while (res.hasNext()) {
					Object[] fila = (Object[]) res.next();
					String ncount = "" + fila[0];
					String valor = "" + fila[1];

					if (valor != null && !valor.equals("null")
							&& valor.length() > 0) {
						if (ncount != null && !ncount.equals("null")
								&& ncount.length() > 0) {
							hash.put(valor, ncount);
						}
					}
				}
			}
			return hash;

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Método para indexar según la id y la categoria. 
	 * @param solrIndexer
	 * @param idElemento
	 * @param categoria
	 * @ejb.interface-method
     * @ejb.permission unchecked="true"
	 */
	public SolrPendienteResultado indexarSolr(final SolrIndexer solrIndexer, final Long idElemento, final EnumCategoria categoria) {
		log.debug("EncuestafacadeEJB.indexarSolr. idElemento:" + idElemento +" categoria:"+categoria);
		
		try {
			OrganigramaProvider op = PluginFactory.getInstance().getOrganigramaProvider();
			MicrositeDelegate micrositedel = DelegateUtil.getMicrositeDelegate();
			
			//Paso 0. Obtenemos el contenido y comprobamos si se puede indexar.
			final Encuesta encuesta = obtenerEncuesta(idElemento);
			boolean isIndexable = this.isIndexable(encuesta);
			if (!isIndexable) {
				return new SolrPendienteResultado(true, "No se puede indexar");
			}
			
			//Preparamos la información básica: id elemento, aplicacionID = GUSITE y la categoria de tipo ficha.
			final IndexData indexData = new IndexData();
			indexData.setCategoria(categoria);
			indexData.setAplicacionId(EnumAplicacionId.GUSITE);
			indexData.setElementoId(idElemento.toString());
			
			indexData.setFechaPublicacion(encuesta.getFpublicacion());
			indexData.setFechaCaducidad(encuesta.getFcaducidad());
			
			//Iteramos las traducciones
			final MultilangLiteral titulo = new MultilangLiteral();
			final MultilangLiteral descripcion = new MultilangLiteral();
			final MultilangLiteral urls = new MultilangLiteral();
			final MultilangLiteral searchText = new MultilangLiteral();
			final MultilangLiteral searchTextOptional = new MultilangLiteral();
			final List<EnumIdiomas> idiomas = new ArrayList<EnumIdiomas>();
			
			Microsite micro = micrositedel.obtenerMicrosite(encuesta.getIdmicrosite());
			
			
			//Recorremos las traducciones
			for (String keyIdioma : encuesta.getTraducciones().keySet()) {
				final EnumIdiomas enumIdioma = EnumIdiomas.fromString(keyIdioma);
				final TraduccionEncuesta traduccion = (TraduccionEncuesta) encuesta.getTraduccion(keyIdioma);
		    	
				if (traduccion != null && enumIdioma != null) {
					//Anyadimos idioma al enumerado.
					idiomas.add(enumIdioma);
					
					//Seteamos los primeros campos multiidiomas: Titulo, Descripción y el search text.
					titulo.addIdioma(enumIdioma, traduccion.getTitulo());
					
			    	descripcion.addIdioma(enumIdioma, traduccion.getTitulo());
			    	//Pregunta traducción en el idioma que estamos
			    	String preguntas = "";
			    	String respuestas = "";
			    	for (Pregunta preg : encuesta.getPreguntas()) {
			    		TraduccionPregunta tradPreg = (TraduccionPregunta) preg.getTraduccion(keyIdioma);
						preguntas +=  " " + tradPreg.getTitulo();
						
						for (Respuesta resp : preg.getRespuestas()) {
							TraduccionRespuesta tradResp = (TraduccionRespuesta) resp.getTraduccion(keyIdioma);
							respuestas +=  " " + tradResp.getTitulo();
						}
					}
			    	searchText.addIdioma(enumIdioma,solrIndexer.htmlToText((traduccion.getTitulo()==null?"":traduccion.getTitulo()) + preguntas));
			    	
			    	
			    	
			    	//StringBuffer que tendrá el contenido a agregar en textOptional
			    	final StringBuffer textoOptional = new StringBuffer();	
			    	
					
					Collection<UnidadListData> unidades = op.getUnidadesHijas(String.valueOf(micro.getIdUA()),keyIdioma);
					for(UnidadListData ua : unidades) {
						textoOptional.append(" ");
				    	textoOptional.append(ua.getNombre());	
					}
					
					textoOptional.append(" ");
					UnidadData unidadData = op.getUnidadData(String.valueOf(micro.getIdUA()), keyIdioma);
			    	textoOptional.append(unidadData.getNombre());
			    	
			    	textoOptional.append(" ");
			    	textoOptional.append(respuestas);
									
			    	searchTextOptional.addIdioma(enumIdioma, textoOptional.toString());
			    	

			    	//v5 version 2015, IN intranet, v1 primera version, v4 segunda version
			    	if (micro.getVersio().equals("v5")) {
			    		urls.addIdioma(enumIdioma, "/"+ micro.getUri() + "/" + keyIdioma + "/encuesta/" + traduccion.getUri());	    		
			    	} else {
			    		urls.addIdioma(enumIdioma, "/sacmicrofront/encuesta.do?lang="+keyIdioma +"&idsite="+micro.getId() 
			    				+"&cont="+encuesta.getId());
			    	}
			    	
			    	
				}
			}
			
			//Seteamos datos multidioma.
			indexData.setTitulo(titulo);
			indexData.setDescripcion(descripcion);
			indexData.setUrl(urls);
			indexData.setSearchText(searchText);
			indexData.setSearchTextOptional(searchTextOptional);
			indexData.setIdiomas(idiomas);
	
			indexData.setCategoriaPadre(EnumCategoria.GUSITE_MICROSITE);
			
			//Recorremos las traducciones del microsite padre
			final MultilangLiteral tituloPadre = new MultilangLiteral();
			final MultilangLiteral urlPadre = new MultilangLiteral();
			for (String keyIdioma : micro.getTraducciones().keySet()) {
				final EnumIdiomas enumIdioma = EnumIdiomas.fromString(keyIdioma);
				final TraduccionMicrosite traduccion = (TraduccionMicrosite) micro.getTraduccion(keyIdioma);
		    	
				if (traduccion != null && enumIdioma != null) {
					
					//Seteamos los primeros campos multiidiomas: Titulo.
					tituloPadre.addIdioma(enumIdioma, traduccion.getTitulo());
					
					//v5 version 2015, IN intranet, v1 primera version, v4 segunda version
			    	if (micro.getVersio().equals("v5")) {
			    		urlPadre.addIdioma(enumIdioma, "/"+ micro.getUri() + "/" + keyIdioma );	    		
			    	} else {
			    		urlPadre.addIdioma(enumIdioma, "/sacmicrofront/index.do?lang="+keyIdioma +"&idsite="+ micro.getId() 
			    				+"&cont="+encuesta.getId());
			    	}
				}
					
			}
			indexData.setDescripcionPadre(tituloPadre);
			indexData.setUrlPadre(urlPadre);
			
			if (String.valueOf(micro.getIdUA()) != null){				
				List<PathUO> uos = new ArrayList<PathUO>();
				PathUO uo = new PathUO();
				List<String> path = new ArrayList<String>();
				path.add(String.valueOf(micro.getIdUA()));
				uo.setPath(path);
				uos.add(uo);
				indexData.setUos(uos);
			}
			
			indexData.setMicrositeId(micro.getId().toString());
			indexData.setInterno(!micro.getRestringido().equals("N") ? true : false);
								
			solrIndexer.indexarContenido(indexData);
			
			return new SolrPendienteResultado(true);
		} catch(Exception exception) {
			log.error("Error en contenidofacade intentando indexar.", exception);
			return new SolrPendienteResultado(false, exception.getMessage());
		}
	}
	
	/**
	 * Comprueba si es indexable una agenda.
	 * @return
	 */
	private boolean isIndexable(final Encuesta encuesta) {
		boolean indexable = true;
		if (!encuesta.getVisible().equals("S") ) {
			indexable = false;
		}
		
		return indexable;
	}
	

	/**
	 * Obtiene las encuestas de un microsite
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<Encuesta> obtenerEncuestasByMicrositeId(Long idMicrosite) {

		Session session = this.getSession();		

		try {
			
			Query query = session.createQuery("from Encuesta en where en.idmicrosite =" + idMicrosite.toString());
			
			List<Encuesta> lista = query.list();
									
			return lista;

		} catch (HibernateException he) {
			
			throw new EJBException(he);
			
		}  finally {
			
			this.close(session);
			
		}
		
	}
	

}
