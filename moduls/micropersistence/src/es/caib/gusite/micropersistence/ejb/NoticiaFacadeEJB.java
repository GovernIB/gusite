package es.caib.gusite.micropersistence.ejb;

import static org.apache.commons.lang.StringUtils.isNotEmpty;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;

import es.caib.gusite.micromodel.*;
import es.caib.gusite.micropersistence.delegate.*;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.store.Directory;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.Transaction;

import es.caib.gusite.lucene.analysis.Analizador;
import es.caib.gusite.lucene.model.Catalogo;
import es.caib.gusite.lucene.model.IndexEncontrado;
import es.caib.gusite.lucene.model.IndexResultados;
import es.caib.gusite.lucene.model.ModelFilterObject;
import es.caib.gusite.micropersistence.intf.DominioInterface;

/**
 * SessionBean para manipular noticias.
 * 
 * @ejb.bean name="sac/micropersistence/NoticiaFacade"
 *           jndi-name="es.caib.gusite.micropersistence.NoticiaFacade"
 *           type="Stateless" view-type="remote" transaction-type="Container"
 * 
 * @ejb.transaction type="Required"
 */
@SuppressWarnings({"deprecation", "unchecked", "rawtypes"})
public abstract class NoticiaFacadeEJB extends HibernateEJB implements
		DominioInterface, NoticiaServiceItf {

	private static final long serialVersionUID = -7037666449767486638L;
	protected static Log log = LogFactory.getLog(NoticiaFacadeEJB.class);

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
		super.select = "select noti.id, noti.fcaducidad, noti.fpublicacion, noti.tipo, trad.titulo, trad.subtitulo,trad.texto,noti.orden ";
		super.from = " from Noticia noti join noti.traducciones trad ";
		super.where = " where trad.id.codigoIdioma = '"
				+ Idioma.getIdiomaPorDefecto() + "' and noti.idmicrosite = "
				+ site.toString();
		super.whereini = " ";
		super.orderby = "";

		super.camposfiltro = new String[] { "trad.titulo", "trad.subtitulo",
				"trad.texto" };
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
		super.select = "select noti.id, noti.fcaducidad, noti.fpublicacion, noti.tipo, trad.titulo, trad.subtitulo,noti.orden ";
		super.from = " from Noticia noti join noti.traducciones trad ";
		super.where = " where trad.id.codigoIdioma = '"
				+ Idioma.getIdiomaPorDefecto() + "'";
		super.whereini = " ";
		super.orderby = "";

		super.camposfiltro = new String[] { "trad.titulo", "trad.subtitulo",
				"trad.texto" };
		super.cursor = 0;
		super.nreg = 0;
		super.npags = 0;
	}

	/**
	 * Crea o actualiza una noticia
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public Long grabarNoticia(Noticia noticia) {

		Session session = this.getSession();
		boolean nuevo = (noticia.getId() == null) ? true : false;
        Transaction tx = session.beginTransaction();
        
		try {
			
			ArchivoDelegate archivoDelegate = DelegateUtil.getArchivoDelegate();
			Noticia noticiaOriginal = null;
			Archivo imagenNoticia = null;
			List<Archivo> archivosPorBorrar = new ArrayList<Archivo>();
			
			Map<String, TraduccionNoticia> listaTraducciones = new HashMap<String, TraduccionNoticia>();

			if (nuevo) {
				
				// Preparamos traducciones para insert posterior.
				Iterator<TraduccionNoticia> it = noticia.getTraducciones().values().iterator();
				while (it.hasNext()) {
					TraduccionNoticia trd = it.next();
					listaTraducciones.put(trd.getId().getCodigoIdioma(), trd);
				}
				noticia.setTraducciones(null);
				
				// Preparamos imagen para insert posterior.
				if (noticia.getImagen() != null) {
					imagenNoticia = noticia.getImagen();
					noticia.setImagen(null);
				}
				
			} else {
				
				noticiaOriginal = this.obtenerNoticia(noticia.getId());
				
				if (noticia.getImagen() != null) {
										
					// Crear original
					imagenNoticia = noticia.getImagen();
					noticia.setImagen(null);
					
				}
				
				// Damos de alta los nuevos archivos
				for (TraduccionNoticia trad : noticia.getTraducciones().values()) {
					
					TraduccionNoticia tradOriginal = (TraduccionNoticia) noticiaOriginal.getTraduccion(trad.getId().getCodigoIdioma());
					
                    if (trad.getDocu() != null) {
                    	if (trad.getDocu().getId() == null) // Condición de nuevo documento.
                    		archivoDelegate.insertarArchivo(trad.getDocu());
                    	else
                    		if (trad.getDocu().getDatos() != null) // Condición de actualizar documento.
                    			archivoDelegate.grabarArchivo(trad.getDocu());
                    } else { 
                    	if(tradOriginal!=null){ //Error #1630 No se puede añadir elementos a los listados
	                		if ( tradOriginal.getDocu() != null) // Condición de borrado de documento.
		                		archivosPorBorrar.add(tradOriginal.getDocu());
                    	}
                    }
                    
				}
				
				//Error #1386 La traducción de los elementos de los listados provoca un bug en el servidor
				//buscamos los idiomas que deben permanecer en la noticia
				String listIdiomaBorrar = "";
				Iterator<TraduccionNoticia> it = noticia.getTraducciones()
						.values().iterator();
				while (it.hasNext()) {
					TraduccionNoticia trn = it.next();
					trn.getId().setCodigoNoticia(noticia.getId());
					listIdiomaBorrar += "'" +trn.getId().getCodigoIdioma()+"'";
					if(it.hasNext()){
						listIdiomaBorrar += "," ;						
					}
				}
				// Borramos los idiomas que no pertenecen a Noticia y existen en la BBDD
				if(!listIdiomaBorrar.isEmpty()){ 
					Query query = session.createQuery("select tradNot from TraduccionNoticia tradNot where tradNot.id.codigoNoticia = " + noticia.getId() + " and tradNot.id.codigoIdioma not in (" + listIdiomaBorrar + ") ");
					List<TraduccionNoticia> traduciones = query.list();
					for(TraduccionNoticia traducI : traduciones ) {
						session.delete(traducI);	
					}
					session.flush();
				}		
				
			}

			// Crear/actualizar noticia.
			session.saveOrUpdate(noticia);
			session.flush();

			if (nuevo) {
				
				for (TraduccionNoticia trad : listaTraducciones.values()) {
					trad.getId().setCodigoNoticia(noticia.getId());
                    if (trad.getDocu() != null) {
                        archivoDelegate.insertarArchivo(trad.getDocu());
                    }
					session.saveOrUpdate(trad);
				}
				session.flush();
				noticia.setTraducciones(listaTraducciones);
				
				if (imagenNoticia != null) {
					archivoDelegate.insertarArchivo(imagenNoticia);
					noticia.setImagen(imagenNoticia);
				}
				
			} else {
				
				if (imagenNoticia != null) {
					
					if (imagenNoticia.getId() != null) {
						if (imagenNoticia.getDatos() != null) {
							archivoDelegate.grabarArchivo(imagenNoticia);
						}
					} else {
						archivoDelegate.insertarArchivo(imagenNoticia);
					}
					
					noticia.setImagen(imagenNoticia);
					
				} else {
					
					// La original tenía imagen pero ahora ya no => toca borrar la imagen.
					if (noticiaOriginal.getImagen() != null)
						archivosPorBorrar.add(noticiaOriginal.getImagen());
					
				}
								
			}
			
			// Borramos archivos FKs de la Noticia que han solicitado que se borren.
			if (archivosPorBorrar.size() > 0)
				archivoDelegate.borrarArchivos(archivosPorBorrar);

			session.saveOrUpdate(noticia);
			
			tx.commit();
			session.close();

			int op = (nuevo) ? Auditoria.CREAR : Auditoria.MODIFICAR;
			this.grabarAuditoria(noticia, op);

			return noticia.getId();

		} catch (HibernateException he) {
			
            tx.rollback();
            session.close();
			throw new EJBException(he);
			
		} catch (DelegateException e) {
			
            tx.rollback();
            session.close();
            throw new EJBException(e);
            
        }
		
	}

	/**
	 * Obtiene una noticia
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Noticia obtenerNoticia(Long id) {

		Session session = this.getSession();
		try {
			Noticia noticia = (Noticia) session.get(Noticia.class, id);
			return noticia;

		} catch (ObjectNotFoundException oNe) {
			log.error(oNe.getMessage());
			return new Noticia();
		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Obtiene una Noticia a partir de la URI
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Noticia obtenerNoticiaDesdeUri(final String idioma, final String uri, final String site) {

		final Session session = this.getSession();
		try {
			Query query;
			if (idioma != null) {
				query = session
						.createQuery("select noticia from Noticia noticia JOIN noticia.traducciones tn where tn.id.codigoIdioma = :idioma and tn.uri = :uri and noticia.idmicrosite = :site");
				query.setParameter("idioma", idioma);
			} else {
				query = session
						.createQuery("select noticia from Noticia noticia JOIN noticia.traducciones tn where tn.uri = :uri and noticia.idmicrosite = :site");
			}
			query.setParameter("uri", uri);
			query.setParameter("site", Long.valueOf(site));
			query.setMaxResults(1);
			return (Noticia) query.uniqueResult();
		} catch (ObjectNotFoundException oNe) {
			log.error(oNe.getMessage());
			return new Noticia();
		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Clona o duplica una noticia dado un id. Devuelve el id de la nueva
	 * noticia
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public Long clonarNoticia(Long id) {

		Session session = this.getSession();
		try {
			Archivo archivo = null;
			Noticia noticia = (Noticia) session.get(Noticia.class, id);
			if(noticia.getImagen() !=null && noticia.getImagen().getId() !=null){            	
				archivo = clonarArchivo(noticia.getImagen().getId());
			}
            Noticia newnoticia = this.clonar4Hibernate(noticia);
            //El metodo de clonar ha puesto el id del original a null por eso lo obtenemos antes
            if(archivo !=null){
            	newnoticia.setImagen(archivo);
            }
            session.close();
            this.grabarNoticia(newnoticia);

			// indexInsertaNoticia(newnoticia, null);
			return newnoticia.getId();

		} catch (HibernateException he) {
			throw new EJBException(he);
		} catch (Exception he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Obtiene una noticia
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Noticia obtenerNoticiaThin(Long id, String idioma) {

		Noticia noticia;
		Session session = this.getSession();
		try {
			super.where = " where noti.id = " + id;
			super.from = " from Noticia noti ";
			List<Noticia> lista = this.listarNoticiasThin(idioma);
			if (!lista.isEmpty()) {
				noticia = lista.get(0);
			} else {
				noticia = new Noticia();
			}
			return noticia;

		} catch (Exception he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Lista todas las noticias
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<Noticia> listarNoticias() {

		Session session = this.getSession();
		try {
			this.parametrosCons(); // Establecemos los parámetros de la
									// paginación
			Query query = session.createQuery(this.select + this.from
					+ this.where + this.orderby);

			ScrollableResults scr = query.scroll();
			ArrayList<Noticia> lista = new ArrayList<Noticia>();
			scr.first();
			scr.scroll(this.cursor - 1);
			int i = 0;
			while (this.tampagina > i++) {
				Object[] fila = (Object[]) scr.get();
				Noticia not = new Noticia();
				not.setId((Long) fila[0]);
				not.setFcaducidad((java.util.Date) fila[1]);
				not.setFpublicacion((java.util.Date) fila[2]);
				not.setTipo((es.caib.gusite.micromodel.Tipo) fila[3]);
				TraduccionNoticia trad = new TraduccionNoticia();
				trad.setTitulo((String) fila[4]);
				trad.setSubtitulo((String) fila[5]);
				trad.setTexto((String) fila[6]);
				not.setTraduccion(Idioma.getIdiomaPorDefecto(), trad);
				if (fila[7] != null) {
					not.setOrden(((Integer) fila[7]).intValue());
				}
				// lista.add(i,not);
				lista.add(not);
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
	 * Lista los anyos que tienen noticias
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<String> listarAnyos() {

		List<String> anyos = new ArrayList<String>();
		Session session = this.getSession();
		try {
			String select = "select distinct to_char(noti.fpublicacion,'YYYY')"
					+ this.from + this.where
					+ " order by to_char(noti.fpublicacion,'YYYY') desc";
			Query query = session.createQuery(select);
			Iterator<String> res = query.iterate();
			while (res.hasNext()) {
				String anyo = res.next();
				if (this.anyoValido(anyo)) {
					anyos.add(anyo);
				}
			}

		} catch (HibernateException e) {
			log.error("", e);
			throw new EJBException(e);
		} finally {
			this.close(session);
		}

		return anyos;
	}

	private boolean anyoValido(String anyo) {

		if (null == anyo) {
			return false;
		}

		if (Integer.valueOf(anyo) < 1970) {
			return false;
		}

		return true;
	}

	/**
	 * Lista todas las noticias
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<Noticia> listarNoticiasThin(String idioma) {

		Session session = this.getSession();
		try {
			this.select = "select noti";
			this.from = " from Noticia noti join noti.traducciones trad ";
			this.where += " and trad.id.codigoIdioma = '" + idioma
					+ "' and trad.titulo is not null ";
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
	 * Buscar elementos
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	@Deprecated
	public List<?> buscarElementos(Map<?, ?> parametros, Map<?, ?> traduccion,
			String idmicrosite, String idtipo, String idioma) throws Exception {

		BuscarElementosParameter parameters = new BuscarElementosParameter(
				parametros, traduccion, idmicrosite, idtipo, idioma);
		return this.buscarElementos(parameters);
	}

	/**
	 * Buscar elementos
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<?> buscarElementos(BuscarElementosParameter parameter) {

		Session session = this.getSession();
		try {
			List<Object> params = new ArrayList<Object>();
			String camposQuery = this.populateQuery(parameter.parametros,
					parameter.traduccion, params);

			String sQuery = "select noti from Noticia as noti join noti.traducciones trad "
					+ "where noti.idmicrosite = "
					+ parameter.idmicrosite
					+ " and noti.tipo = "
					+ parameter.idtipo
					+ " and trad.id.codigoIdioma = '"
					+ parameter.idioma
					+ "'"
					+ " and (" + camposQuery + ")";

			if (isNotEmpty(parameter.where)) {
				sQuery += " and " + parameter.where;
			}

			Query query = session.createQuery(sQuery);
			for (int i = 0; i < params.size(); i++) {
				String o = (String) params.get(i);
				query.setString(i, o);
			}

			ArrayList<Noticia> elementos = new ArrayList<Noticia>();
			Iterator<?> res = query.iterate();
			int i = 0;
			while (res.hasNext()) {
				Noticia not = new Noticia();
				not = (Noticia) res.next();
				not.setIdi(parameter.idioma);
				elementos.add(i, not);
				i++;
			}
			this.nreg = i;
			return elementos;

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Buscar elementos en lucene
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<Noticia> buscarElementosLuc(String micro, String idi,
			String idlista, String cadena, boolean sugerir) {

		Session session = this.getSession();
		List<Noticia> listaelementos = new ArrayList<Noticia>();
		try {
			IndexResultados resultado = null;
			resultado = DelegateUtil.getIndexerDelegate().buscar(micro, idi,
					idlista, cadena, sugerir);

			if ((resultado != null) && (resultado.getLista() != null)
					&& (resultado.getLista().size() > 0)) {
				Iterator<?> iter = resultado.getLista().iterator();

				while (iter.hasNext()) {
					String id = ((IndexEncontrado) iter.next()).getId();// el id
																		// esta
																		// codificado
																		// MCR.NTCS0.3412
					String idtrue = id.substring(
							Catalogo.SRVC_MICRO_ELEMENTOS.length() + 1,
							id.length());
					Noticia noti = this.obtenerNoticia(new Long(idtrue));
					listaelementos.add(noti);
				}
			}

			this.nreg = listaelementos.size();
			return listaelementos;

		} catch (DelegateException e) {
			throw new EJBException(e);
		} finally {
			this.close(session);
		}
	}

	/**
	 * borra una noticia
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public void borrarNoticia(Long id) {

		Session session = this.getSession();
		
		try {
			
			Transaction tx = session.beginTransaction();
			Noticia noticia = (Noticia) session.get(Noticia.class, id);
			
			// Obtenemos los IDs de los archivos para borrarlos después.
			List<Archivo> listaArchivos = new ArrayList<Archivo>();
			
			// Primero traducciones.
			Map<String, TraduccionNoticia> traducciones = noticia.getTraducciones();
			for (TraduccionNoticia t : traducciones.values()) {
				if (t.getDocu() != null)
					listaArchivos.add(t.getDocu());
			}
			
			// Luego imagen de la noticia.
			if (noticia.getImagen() != null)
				listaArchivos.add(noticia.getImagen());

			session.createQuery("delete from TraduccionNoticia where id.codigoNoticia = " + id).executeUpdate();
			session.createQuery("delete from Noticia where id = " + id).executeUpdate();
			session.flush();
			
			// Borramos archivos asociados tras borrar los registros, evitando así los problemas de integridad.
			ArchivoDelegate archivoDelegate = DelegateUtil.getArchivoDelegate();
			archivoDelegate.borrarArchivos(listaArchivos);
			
			tx.commit();
			
			this.close(session);
			this.grabarAuditoria(noticia, Auditoria.ELIMINAR);

		} catch (HibernateException he) {
			
			throw new EJBException(he);
			
		} catch (DelegateException e) {
			
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
					.createQuery("from Noticia noti where noti.idmicrosite = "
							+ site.toString() + " and noti.id = "
							+ id.toString());
			return query.list().isEmpty();

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
		}
	}

	/**
	 * Construye el query de búsqueda segun los parámetros
	 */
	protected String populateQuery(Map<?, ?> parametros, Map<?, ?> traduccion,
			List<Object> params) {

		String aux = "";
		for (Object name : parametros.keySet()) {
			String key = (String) name;
			Object value = parametros.get(key);
			if (value != null) {
				if (value instanceof String) {
					String sValue = (String) value;
					if (sValue.length() > 0) {
						if (aux.length() > 0) {
							aux = aux + " or ";
						}
						if (sValue.startsWith("\"") && sValue.endsWith("\"")) {
							sValue = sValue.substring(1, (sValue.length() - 1));
							aux = aux + " upper( noti." + key + " ) like ? ";
							params.add(sValue);
						} else {
							aux = aux + " upper( noti." + key + " ) like ? ";
							params.add("%" + sValue + "%");
						}
					}
				} else if (value instanceof Date) {
					if (aux.length() > 0) {
						aux = aux + " or ";
					}
					aux = aux + "noti." + key + " = '" + value + "'";
				} else {
					if (aux.length() > 0) {
						aux = aux + " or ";
					}
					aux = aux + "noti." + key + " = " + value;
				}
			}
		}

		for (Object name : traduccion.keySet()) {
			String key = (String) name;
			Object value = traduccion.get(key);
			if (value != null) {
				if (value instanceof String) {
					String sValue = (String) value;
					if (sValue.length() > 0) {
						String term = " upper( trad." + key + " ) like ? ";
						if (sValue.startsWith("\"") && sValue.endsWith("\"")) {
							sValue = sValue.substring(1, (sValue.length() - 1));

							aux = this.afegirTraduccioAlQuery(aux, term);
							params.add(sValue);
						} else {
							aux = this.afegirTraduccioAlQuery(aux, term);
							params.add("%" + sValue + "%");
						}
					}
				} else {
					String term = " trad." + key + " = ? ";
					aux = this.afegirTraduccioAlQuery(aux, term);
					params.add(value);
				}
			}
		}

		return aux;
	}

	private String afegirTraduccioAlQuery(String aux, String term) {
		return (StringUtils.isEmpty(aux)) ? term : aux + " or " + term;
	}

	private Noticia clonar4Hibernate(Noticia noticia) throws HibernateException {

		Noticia newnoticia = new Noticia();
		try {
			newnoticia = (Noticia) BeanUtils.cloneBean(noticia);
			newnoticia.setId(null);
			if (newnoticia.getImagen() != null) {
				newnoticia.getImagen().setId(null);
			}

            Map<String, TraduccionNoticia> traducciones = new HashMap();
            for (TraduccionNoticia trad : noticia.getTraducciones().values()) {
                TraduccionNoticia traduccionNoticia = clonarTraduccion(trad, noticia.getIdmicrosite().toString());
                traduccionNoticia.getId().setCodigoIdioma(trad.getId().getCodigoIdioma());
                traducciones.put(trad.getId().getCodigoIdioma(), traduccionNoticia);
            }
            newnoticia.setTraduccionMap(traducciones);

			Iterator<?> iter = newnoticia.getTraduccionMap().keySet().iterator();
			while (iter.hasNext()) {
				String key = (String) iter.next();
				TraduccionNoticia tranot = (TraduccionNoticia) newnoticia.getTraduccionMap().get(key);
				if (tranot == null) {
					tranot = new TraduccionNoticia();
					tranot.setTitulo("");
				}
				newnoticia.setTraduccion(key, tranot);
			}

		} catch (Exception e) {
			throw new HibernateException("[clonar4Hibernate] " + e.getMessage());
		}

		return newnoticia;
	}

    private TraduccionNoticia clonarTraduccion(TraduccionNoticia traduccionNoticia, final String site) throws IOException {

        TraduccionNoticia tradNotiNew = new TraduccionNoticia();
        if (traduccionNoticia.getDocu() != null) {
            tradNotiNew.setDocu(clonarArchivo(traduccionNoticia.getDocu().getId()));
        }
        
        tradNotiNew.setUri(generarNuevaUri(traduccionNoticia.getId().getCodigoIdioma(), traduccionNoticia.getUri(), site, 0));
        tradNotiNew.setFuente(traduccionNoticia.getFuente());
        tradNotiNew.setLaurl(traduccionNoticia.getLaurl());
        tradNotiNew.setSubtitulo(traduccionNoticia.getSubtitulo());
        tradNotiNew.setTexto(traduccionNoticia.getTexto());
        tradNotiNew.setTitulo(traduccionNoticia.getTitulo());
        tradNotiNew.setUrlnom(traduccionNoticia.getUrlnom());
        tradNotiNew.setId(new TraduccionNoticiaPK());

        return tradNotiNew;
    }

    private String generarNuevaUri(String idioma, String uri, String site, int count) {

        String newUri = uri.concat("_").concat(String.valueOf(count));
        Noticia noticia = this.obtenerNoticiaDesdeUri(idioma, newUri, site);
        while (noticia != null) {
            count++;
            newUri = uri.concat("_").concat(String.valueOf(count));
            noticia = this.obtenerNoticiaDesdeUri(idioma, newUri, site);
        }

        return newUri;
    }

    private Archivo clonarArchivo(Long id) throws IOException {

        Archivo archivo;
        Archivo nuevoArchivo = new Archivo();
        try {
            archivo = DelegateUtil.getArchivoDelegate().obtenerArchivo(id);

            nuevoArchivo.setId(null);
            nuevoArchivo.setMime(archivo.getMime());
            nuevoArchivo.setNombre(archivo.getNombre());
            nuevoArchivo.setPeso(archivo.getPeso());
            nuevoArchivo.setDatos(DelegateUtil.getArchivoDelegate().obtenerContenidoFichero(archivo));
            nuevoArchivo.setPagina(archivo.getPagina());
            nuevoArchivo.setIdmicrosite(archivo.getIdmicrosite());
            nuevoArchivo.setTraduccionMap(archivo.getTraduccionMap());
            nuevoArchivo.setIdi(archivo.getIdi());

        } catch (DelegateException e) {
            nuevoArchivo = null;
        }

        return nuevoArchivo;
    }

    
    
	/***************************************************************************************/
	/******************* INDEXACION ************************************/
	/***************************************************************************************/

	/**
	 * Añade la noticia al indice en todos los idiomas
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public void indexInsertaNoticia(Noticia noti, ModelFilterObject filter) {

		IndexObject io = new IndexObject();
		try {
			if (filter == null) {
				filter = DelegateUtil.getMicrositeDelegate()
						.obtenerFilterObject(noti.getIdmicrosite());
			}

			if (filter != null && filter.getBuscador().equals("N")) {
				return;
			}

			IndexerDelegate indexerDelegate = DelegateUtil.getIndexerDelegate();
			for (int i = 0; i < this.langs.size(); i++) {
				String idi = (String) this.langs.get(i);
				io = new IndexObject();

				// Configuración del writer
				Directory directory = indexerDelegate
						.getHibernateDirectory(idi);
				IndexWriter writer = new IndexWriter(directory,
						Analizador.getAnalizador(idi), false,
						MaxFieldLength.UNLIMITED);
				writer.setMergeFactor(20);
				writer.setMaxMergeDocs(Integer.MAX_VALUE);

				try {
					io.setId(Catalogo.SRVC_MICRO_ELEMENTOS + "." + noti.getId());
					io.setClasificacion(Catalogo.SRVC_MICRO_ELEMENTOS + "."
							+ noti.getTipo().getId());

					io.setMicro(filter.getMicrosite_id());
					io.setRestringido(filter.getRestringido());
					io.setUo(filter.getUo_id());
					io.setMateria(filter.getMateria_id());
					io.setSeccion(filter.getSeccion_id());
					io.setFamilia(filter.getFamilia_id());

					io.setTitulo("");
					io.setUrl("/sacmicrofront/noticia.do?lang=" + idi
							+ "&idsite=" + noti.getIdmicrosite().toString()
							+ "&cont=" + noti.getId().toString());
					io.setCaducidad("");
					io.setPublicacion("");
					io.setDescripcion("");
					io.setTituloserviciomain(filter.getTraduccion(idi)
							.getMaintitle());

					if (noti.getFcaducidad() != null) {
						io.setCaducidad(new java.text.SimpleDateFormat(
								"yyyyMMdd").format(noti.getFcaducidad()));
					}

					if (noti.getFpublicacion() != null) {
						io.setPublicacion(new java.text.SimpleDateFormat(
								"yyyyMMdd").format(noti.getFpublicacion()));
					}

					TraduccionNoticia trad = ((TraduccionNoticia) noti
							.getTraduccion(idi));
					if (trad != null) {
						if (trad.getTexto() != null) { // simulamos un bean
														// Archivo con el
														// contenido
							Archivo archi = new Archivo();
							archi.setMime("text/html");
							archi.setPeso(trad.getTexto().length());
							archi.setDatos(trad.getTexto().getBytes());
							io.addArchivo(archi);

							// En elementos la descripcion sera el principio del
							// text
							if (io.getText().length() > 200) {
								io.addDescripcionLine(io.getText().substring(0,
										199)
										+ "...");
							} else {
								io.addDescripcionLine(io.getText());
							}
						}

						io.addTextLine(trad.getTitulo());
						io.addTextLine(trad.getFuente());
						io.addTextLine(trad.getSubtitulo());
						if (noti.getFpublicacion() != null) {
							io.addTextLine(new java.text.SimpleDateFormat(
									"dd/MM/yyyy").format(noti.getFpublicacion()));
						}

						io.addTextopcionalLine(filter.getTraduccion(idi)
								.getMateria_text());
						io.addTextopcionalLine(filter.getTraduccion(idi)
								.getSeccion_text());
						io.addTextopcionalLine(filter.getTraduccion(idi)
								.getUo_text());

						if (trad.getTitulo() != null) {
							io.setTitulo(trad.getTitulo());
						}

						if (trad.getDocu() != null) {
							io.addArchivo(trad.getDocu());
						}
					}

					TraduccionTipo trad1 = (TraduccionTipo) noti.getTipo()
							.getTraduccion(idi);
					if (trad1 != null) {
						io.addTextLine(trad1.getNombre());
					}

					if (io.getText().length() > 0) {
						indexerDelegate.insertaObjeto(io, idi, writer);
					}
				} finally {
					writer.close();
					directory.close();
				}
			}

		} catch (DelegateException ex) {
			log.warn("[indexInsertaNoticia:" + noti.getId()
					+ "] No se ha podido indexar elemento. " + ex.getMessage());
			// throw new EJBException(ex);
		} catch (Exception e) {
			log.warn("[indexInsertaNoticia:" + noti.getId()
					+ "] No se ha podido indexar elemento. " + e.getMessage());
		}
	}

	/**
	 * Elimina la noticia en el indice en todos los idiomas
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public void indexBorraNoticia(Long id) {

		try {
			for (int i = 0; i < this.langs.size(); i++) {
				DelegateUtil.getIndexerDelegate().borrarObjeto(
						Catalogo.SRVC_MICRO_ELEMENTOS + "." + id,
						"" + this.langs.get(i));
			}
		} catch (DelegateException ex) {
			throw new EJBException(ex);
		}
	}

	/**
	 * Prueba de utilizar lo de los listados externos
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Map<String, String> obtenerListado(String id, Map parametros) {

		Map<String, String> mapretorno = new HashMap<String, String>();
		String cabecera = "<h1>Cabecera de NOTICIA</h1></br><p>Esto es una prueba de cabecera:</p>";
		String cuerpo = "<p>Esto es una prueba de cuerpaaaaaaaaaaaaaaaaaaaaaaaaaaaaa</p>";
		mapretorno.put("Cabecera", cabecera);
		mapretorno.put("Cuerpo", cuerpo);
		mapretorno.put("Paginado", "true");
		mapretorno.put("Pagina", "2");
		mapretorno.put("NumeroPaginas", "6");
		mapretorno.put("ElementosPorPagina", "6");
		return mapretorno;
	}

}
