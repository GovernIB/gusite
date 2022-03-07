package es.caib.gusite.micropersistence.ejb;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import es.caib.gusite.micromodel.Agenda;
import es.caib.gusite.micromodel.Archivo;
import es.caib.gusite.micromodel.ArchivoFull;
import es.caib.gusite.micromodel.Auditoria;
import es.caib.gusite.micromodel.Contenido;
import es.caib.gusite.micromodel.Encuesta;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.Noticia;
import es.caib.gusite.micromodel.Pregunta;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.exception.FicheroVacioException;
import es.caib.gusite.micropersistence.util.ArchivoUtil;

/**
 * SessionBean para obtener archivos de BD.
 *
 * @ejb.bean name="sac/micropersistence/ArchivoFacade"
 *           jndi-name="es.caib.gusite.micropersistence.ArchivoFacade"
 *           type="Stateless" view-type="remote" transaction-type="Container"
 *
 * @ejb.transaction type="Required"
 *
 * @author Indra
 */

@SuppressWarnings({ "unchecked" })
public abstract class ArchivoFacadeEJB extends HibernateEJB {

	private static final long serialVersionUID = 81125150632029055L;

	/**
	 * @ejb.create-method
	 * @ejb.permission unchecked="true"
	 */
	@Override
	public void ejbCreate() throws CreateException {

		super.ejbCreate();
		try {
			super.langs = DelegateUtil.getIdiomaDelegate().listarLenguajes();
		} catch (final Exception ex) {
			throw new EJBException(ex);
		}
	}

	/**
	 * Obtiene el archivo Comprobamos que pertenece al microsite o es público
	 * (microsite=0)
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Archivo obtenerArchivo(final Long id) {

		final Session session = this.getSession();
		Archivo archi = null;

		try {

			final Query query = session.createQuery("from Archivo a where a.id =" + id.toString());
			archi = (Archivo) query.uniqueResult();

			/*
			 * if (!ArchivoUtil.almacenarEnFilesystem()) { query =
			 * session.createQuery("from ArchivoFull a where a.id =" + id.toString());
			 * ArchivoFull archivoFull = (ArchivoFull) query.uniqueResult();
			 * archi.setDatos(archivoFull.getDatos()); }
			 */

			return archi;

		} catch (final HibernateException he) {

			throw new EJBException(he);

		} finally {

			this.close(session);

		}

	}

	/**
	 * Obtiene los archivos de un microsite
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<Archivo> obtenerArchivoByMicrositeId(final Long idMicrosite) throws DelegateException {

		final Session session = this.getSession();

		try {

			final Query query = session.createQuery("from Archivo a where a.idmicrosite =" + idMicrosite.toString());
			final List<Archivo> lista = query.list();

			return lista;

		} catch (final HibernateException he) {

			throw new EJBException(he);

		} finally {

			this.close(session);

		}

	}

	/**
	 * Devuelve una lista con todos los Ids de los archivos en la tabla GUS_DOCUS
	 * (necesario para proceso de exportación a disco). solo retorna los no
	 * exportados a File System (los que tienen flag exportadoAFileSystem <> 'S' ),
	 * es decir los pendientes de analizar (X), los que se encuentran en error(E), y
	 * los que no están en FS (N) - numeroMaximoElementos indica el numero máximo de
	 * elementos que se retornaran. - omitirErroresYaTratados permite indicar si se
	 * deben omitir los ficheros ya tratados que al procesarlos dieron error.
	 *
	 * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<Object[]> obtenerTodosLosArchivosSinBlobs(final int numeroMaximoElementos,
			final boolean omitirErroresYaTratados) {

		final Session session = this.getSession();

		try {

			// amartin: order by a.idmicrosite desc, a.id asc para que el último
			// microsite sea el 0 (¿archivos comunes?).

			String sQuery = "select a.id, a.nombre, a.idmicrosite from Archivo a where exportadoAFileSystem <> '"
					+ Archivo.ESTADO_ENFILESYSTEM_SI + "' ";
			if (omitirErroresYaTratados) {
				sQuery += " and exportadoAFileSystem <> '" + Archivo.ESTADO_ENFILESYSTEM_ERROR_EXPORTACION + "' ";
			}
			sQuery += " order by a.idmicrosite desc, a.id asc";

			final Query query = session.createQuery(sQuery);
			if (numeroMaximoElementos > 0) {
				query.setMaxResults(numeroMaximoElementos);
			}

			final List<Object[]> lista = query.list();

			return lista;

		} catch (final HibernateException he) {

			throw new EJBException(he);

		} finally {

			this.close(session);
		}
	}

	/**
	 * Retorna el numero total de ficheros
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Long NumeroTotaldeFicheros() {

		final Session session = this.getSession();

		try {
			final Query query = session.createQuery("select count(a.id) as numficheros from Archivo a ");
			return (Long) query.uniqueResult();

		} catch (final HibernateException he) {

			throw new EJBException(he);

		} finally {

			this.close(session);
		}
	}

	/**
	 * Retorna el numero total de ficheros pendientes de ser tratados por el proceso
	 * de exportación a filesystem, (estados X,N y E)
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Long NumeroPendientesExportarAFileSystem() {

		final Session session = this.getSession();

		try {
			final Query query = session.createQuery(
					"select count(a.id) as numPendientesExportar from Archivo a where exportadoAFileSystem <> '"
							+ Archivo.ESTADO_ENFILESYSTEM_SI + "'");
			return (Long) query.uniqueResult();

		} catch (final HibernateException he) {

			throw new EJBException(he);

		} finally {

			this.close(session);
		}
	}

	/**
	 * Retorna el numero total de ficheros ya tratados por el proceso de exportación
	 * a filesystem
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Long NumeroExportadosAFileSystem() {

		final Session session = this.getSession();

		try {
			final Query query = session.createQuery(
					"select count(a.id) as numPendientesExportar from Archivo a where exportadoAFileSystem = '"
							+ Archivo.ESTADO_ENFILESYSTEM_SI + "'");
			return (Long) query.uniqueResult();

		} catch (final HibernateException he) {

			throw new EJBException(he);

		} finally {

			this.close(session);
		}
	}

	/**
	 * Retorna el numero total de ficheros ya tratados por el proceso de exportación
	 * a filesystem y que se encuentran en error
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Long NumeroExportadosAFileSystemConError() {

		final Session session = this.getSession();

		try {
			final Query query = session.createQuery(
					"select count(a.id) as numPendientesExportar from Archivo a where exportadoAFileSystem = '"
							+ Archivo.ESTADO_ENFILESYSTEM_ERROR_EXPORTACION + "'");
			return (Long) query.uniqueResult();

		} catch (final HibernateException he) {

			throw new EJBException(he);

		} finally {

			this.close(session);
		}
	}

	/**
	 * Actualiza los archivos indicados en listado y los marca con el estado
	 * indicado exportado a filesystem (S) Indica que el fichero ya ha sido
	 * exportado a File system (N) Indica que el fichero esta pendiente de exportar,
	 * (X) Indica que el fichero no se ha analizado o (E) Indica que ocurrio un
	 * error al exportar
	 *
	 * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public int MarcarcomoExportadosAFileSystem(final List<Long> listado, final String estado) {
		int res = 0;

		if (estado.equals("S") || estado.equals("N") || estado.equals("X") || estado.equals("E")) {

			if (listado != null && listado.size() > 0) {
				final Session session = this.getSession();
				try {

					final StringBuilder ids = new StringBuilder();
					boolean primero = true;
					for (final Long s : listado) {
						if (primero) {
							primero = false;
						} else {
							ids.append(",");
						}
						ids.append(s);
					}

					final Query query = session.createQuery("Update Archivo a set a.exportadoAFileSystem = '" + estado
							+ "' where a.id in (" + ids.toString() + ") ");
					res = query.executeUpdate();
					session.flush();

				} catch (final HibernateException he) {
					throw new EJBException(he);
				} finally {
					this.close(session);
				}
			}

		}

		return res;
	}

	/**
	 * Obtiene un archivo por el nombre Comprobamos que pertenece al microsite o es
	 * público (microsite=0)
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Archivo obtenerArchivobyName(final Long site, final String nombre) {

		final Session session = this.getSession();
		try {
			// TODO amartin: ¿usar método checkSite() aquí, propio de este mismo
			// EJB, para ahorrar código?

			final Criteria criteriID = session.createCriteria(Archivo.class);
			criteriID.add(Restrictions.eq("idmicrosite", site));
			criteriID.add(Restrictions.eq("nombre", nombre));

			List<?> listArchi = criteriID.list();
			Iterator<?> iterArchi = listArchi.iterator();
			Archivo archi = null;

			while (iterArchi.hasNext()) {
				archi = (Archivo) iterArchi.next();

				// if (ArchivoUtil.almacenarEnFilesystem()) {
				// this.obtenerDatosArchivoExportado(archi);
				// }

				return archi;
			}

			final Long site0 = new Long(0);
			final Criteria criteri = session.createCriteria(Archivo.class);
			criteri.add(Restrictions.eq("idmicrosite", site0));
			criteri.add(Restrictions.eq("nombre", nombre));

			listArchi = criteri.list();
			iterArchi = listArchi.iterator();

			while (iterArchi.hasNext()) {

				archi = (Archivo) iterArchi.next();
				final String hql = "from Noticia noti join noti.traducciones trad join trad.docu doc where doc.id='"
						+ archi.getId() + "' and noti.idmicrosite='" + site + "'";
				final Query query = session.createQuery(hql);
				final List<?> listNot = query.list();

				if (listNot.size() == 1) {

					// if (ArchivoUtil.almacenarEnFilesystem()) {
					// this.obtenerDatosArchivoExportado(archi);
					// }

					return archi;
				}
			}

			return archi;

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

			final Query query = session.createQuery(
					"from Archivo archi where (archi.idmicrosite is null or archi.idmicrosite = 0 or archi.idmicrosite="
							+ site.toString() + ") and archi.id=" + id.toString());
			query.setMaxResults(1);
			return query.uniqueResult() == null;
			// Tendría que ser la comparación al revés?!?!
			// return query.list().isEmpty();

		} catch (final HibernateException he) {

			throw new EJBException(he);

		} finally {

			this.close(session);

		}

	}

	/**
	 * Comprueba que el elemento es accesible
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public boolean visible(final Long id) {

//		if (true) {
//			// #134 Se ha puesto este parche para subir una version que mejora el
//			// rendimiento
//			return true;
//		}

		final Session session = this.getSession();
		final String noVisible = "N";
		final String pagina = null;
		try {
			// comprobamos si es agenda, comun,listado o pagina
			final String hql = "select idmicrosite ,pagina from Archivo where id=" + id;

			final Query query = session.createQuery(hql);
			final List<Object[]> resultados = query.list();
			if (resultados.isEmpty()) {
				return true;
			}
			resultados.get(0);

			final Archivo archi = new Archivo();
			if (resultados != null && !resultados.isEmpty()) {
				for (final Object[] resultado : resultados) {

					if (resultado[0] != null) {
						archi.setIdmicrosite((Long) resultado[0]);
					}

					if (resultado[1] != null) {
						archi.setPagina((Long) resultado[1]);

					}
				}
			}
			if (archi.getIdmicrosite() != null && archi.getPagina() != null) {
				// espagina
				return esPagina(session, archi.getPagina());
			} else if (archi.getIdmicrosite() != null && archi.getPagina() == null) {
				// escomun
				return esComun(session, archi.getIdmicrosite());
			} else {
				// es agenda o listado
				return esListado(session, id);
			}

		} catch (final HibernateException he) {

			throw new EJBException(he);

		} finally {

			this.close(session);

		}

	}

	/**
	 * Inserta un nuevo documento en la BD
	 *
	 * @ejb.interface-method
	 * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public Long insertarArchivo(final Archivo a) throws DelegateException {

		final Session session = this.getSession();

		try {
			final Transaction tx = session.beginTransaction();

			if (a.getDatos() == null) {
				throw new FicheroVacioException("Fichero vacío.");
			}

			// Guardamos para obtener el ID del registro.
			session.saveOrUpdate(a);

			// actualizamos el check que indica si el fichero está en filesystem o no
			// por ahora se almacena tanto en FS como en BBDD, pero aún asi se marca como
			// exportado o no exportado a fs
			if (ArchivoUtil.almacenarEnFilesystem()) {
				a.setExportadoAFileSystem(Archivo.ESTADO_ENFILESYSTEM_SI);
				// Guardamos archivo en FS.
				ArchivoUtil.exportaArchivoAFilesystem(a);
			} else {
				a.setExportadoAFileSystem(Archivo.ESTADO_ENFILESYSTEM_NO);
				// Guardamos el archivo full. (BBDD)
				final ArchivoFull archivoFull = new ArchivoFull();
				archivoFull.setId(a.getId());
				archivoFull.setDatos(a.getDatos());
				// Guardamos el contenido del fichero.
				session.saveOrUpdate(archivoFull);
			}

			// Null datos para evitar cache
			a.setDatos(null);

			session.flush();

			tx.commit();

			return a.getId();

		} catch (final HibernateException he) {

			throw new EJBException(he);

		} catch (final IOException e) {

			throw new EJBException(e);

		} finally {

			this.close(session);

		}

	}

	/**
	 * Obtiene el contenido del fichero dependiendo de si está en modo filesystem o
	 * en modo base de datos.
	 *
	 * @throws IOException
	 *
	 * @ejb.interface-method *
	 * @ejb.permission unchecked="true"
	 */
	public byte[] obtenerContenidoFichero(final Archivo archivo) throws DelegateException, IOException {
		return obtenerContenidoFichero(archivo, ArchivoUtil.almacenarEnFilesystem());
	}

	/**
	 * Obtiene el contenido del fichero dependiendo de si está en modo filesystem o
	 * en modo base de datos (lo marca la variable de entrada). Importante, a este
	 * método sólo se le puede llamar directamente desde volcar datos de microsites
	 * a filesystem!.
	 *
	 * @throws IOException
	 *
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public byte[] obtenerContenidoFichero(final Archivo archivo, final boolean isAlmacenarEnFilesystem)
			throws DelegateException, IOException {

		byte[] datos;
		if (isAlmacenarEnFilesystem) {
			datos = ArchivoUtil.obtenerDatosArchivoEnFilesystem(archivo);
		} else {
			final Session session = this.getSession();
			final Criteria criteri = session.createCriteria(ArchivoFull.class);
			criteri.add(Restrictions.eq("id", archivo.getId()));
			final ArchivoFull archivoFull = (ArchivoFull) criteri.uniqueResult();
			datos = archivoFull.getDatos();
		}
		return datos;
	}

	/**
	 * Borra un documento de la BD
	 *
	 * @ejb.interface-method
	 * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public void borrarArchivo(final Long id) throws DelegateException {

		final Session session = this.getSession();

		try {

			final Archivo a = (Archivo) session.get(Archivo.class, id);

			if (ArchivoUtil.existeArchivoEnFilesystem(a)) {
				ArchivoUtil.borrarArchivo(a);
			}

			session.delete(a);
			session.flush();
			session.close();

			a.setIdmicrosite(null);
			a.setDatos(null);

			this.grabarAuditoria(a, Auditoria.ELIMINAR);

		} catch (final HibernateException he) {

			throw new EJBException(he);

		} catch (final IOException e) {

			throw new EJBException(e);

		} finally {

			this.close(session);

		}

	}

	/**
	 * Borra una lista de documentos de la BD
	 *
	 * @ejb.interface-method
	 * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 * @param id
	 *            Id del archivo
	 * @throws DelegateException
	 */
	public void borrarArchivos(final List<Archivo> lista) throws DelegateException {

		final Session session = this.getSession();

		try {

			for (final Archivo a : lista) {

				if (ArchivoUtil.existeArchivoEnFilesystem(a)) {
					ArchivoUtil.borrarArchivo(a);
				}

				session.delete(a);
				session.flush();

				a.setIdmicrosite(null);
				a.setDatos(null);

				this.grabarAuditoria(a, Auditoria.ELIMINAR);

			}

		} catch (final HibernateException he) {

			throw new EJBException(he);

		} catch (final IOException e) {

			throw new EJBException(e);

		} finally {

			this.close(session);

		}

	}

	/**
	 * Crea o actualiza un archivo
	 *
	 * @ejb.interface-method
	 * @ejb.permission role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public Long grabarArchivo(final Archivo a) {

		final Session session = this.getSession();
		final boolean nuevo = (a.getId() == null) ? true : false;

		try {

			final Transaction tx = session.beginTransaction();

			if (a.getDatos() == null) {
				throw new FicheroVacioException("Fichero vacío.");
			}

			if (!nuevo) {
				final Archivo aOld = this.obtenerArchivo(a.getId());
				if (aOld != null) {
					ArchivoUtil.borrarArchivo(aOld);
				}
			}

			// Actualizamos el check que indica si el fichero está en filesystem o no
			// y guardamos el fichero en el soporte correspondiente
			if (ArchivoUtil.almacenarEnFilesystem()) {
				a.setExportadoAFileSystem(Archivo.ESTADO_ENFILESYSTEM_SI);
				// Guardamos archivo en FS.
				ArchivoUtil.exportaArchivoAFilesystem(a);
			} else {
				a.setExportadoAFileSystem(Archivo.ESTADO_ENFILESYSTEM_NO);
				// Guardamos el fichero full.
				final ArchivoFull archivoFull = new ArchivoFull();
				archivoFull.setId(a.getId());
				archivoFull.setDatos(a.getDatos());

				// Guardamos el contenido del fichero.
				session.saveOrUpdate(archivoFull);
			}

			// Null datos para evitar cache
			a.setDatos(null);

			// Guardamos el fichero
			session.saveOrUpdate(a);

			session.flush();

			tx.commit();

			final int op = (nuevo) ? Auditoria.CREAR : Auditoria.MODIFICAR;
			this.grabarAuditoria(a, op);

			return a.getId();

		} catch (final HibernateException he) {

			if (!nuevo) {
				// this.indexBorraArchivo(a.getId());
			}
			throw new EJBException(he);

		} catch (final IOException e) {

			if (!nuevo) {
				// this.indexBorraArchivo(a.getId());
			}
			throw new EJBException(e);

		} finally {

			this.close(session);

		}

	}

	private boolean esPagina(final Session session, final Long pagina) {
		final String noVisible = "N";

		final String hql = "select fcaducidad,fpublicacion,visible from Contenido  where id=" + pagina;
		final Query query = session.createQuery(hql);
		final List<Object[]> resultados = query.list();
		if (resultados.isEmpty()) {
			return true;
		}
		resultados.get(0);
		if (resultados != null && !resultados.isEmpty()) {
			for (final Object[] resultado : resultados) {
				final Date actual = new Date();
				final Contenido conten = new Contenido();
				if (resultado[0] != null) {
					conten.setFcaducidad((java.util.Date) resultado[0]);
					if (conten.getFcaducidad().before(actual)) {
						return false;
					}
				}
				if (resultado[1] != null) {
					conten.setFpublicacion((java.util.Date) resultado[1]);
					if (conten.getFpublicacion().after(actual)) {
						return false;
					}
				}
				if (resultado[2] != null) {
					conten.setVisible((resultado[2].toString()));
					if (conten.getVisible().equals(noVisible)) {
						return false;
					}
				}

			}
		}

		return true;

	}

	private boolean esComun(final Session session, final Long idmicrosite) {
		final String noVisible = "N";
		final Date actual = new Date();
		final String hql = "select  fecha, visible from Microsite  where id=" + idmicrosite;
		final Query query = session.createQuery(hql);

		final List<Object[]> resultados = query.list();
		if (resultados.isEmpty()) {
			return true;
		}
		resultados.get(0);

		if (resultados != null && !resultados.isEmpty()) {
			for (final Object[] resultado : resultados) {

				final Microsite micro = new Microsite();
				if (resultado[0] != null) {
					micro.setFecha((java.util.Date) resultado[0]);
					if (micro.getFecha().after(actual)) {
						return false;
					}

					if (resultado[1] != null) {
						micro.setVisible((resultado[1].toString()));
						if (micro.getVisible().equals(noVisible)) {
							return false;
						}
					}
				}
			}
		}
		return true;

	}

	private boolean esListado(final Session session, final Long id) {
		final String noVisible = "N";

		final String hql = "select  noti.fcaducidad, noti.fpublicacion,noti.visible from Noticia noti join noti.traducciones trad join trad.docu doc where doc.id="
				+ id + " or noti.imagen=" + id;
		final Query query = session.createQuery(hql);

		final List<Object[]> resultados = query.list();
		if (resultados.isEmpty()) {
			return esAgenda(session, id);
		}
		resultados.get(0);

		if (resultados != null && !resultados.isEmpty()) {
			for (final Object[] resultado : resultados) {
				final Date actual = new Date();
				final Noticia not = new Noticia();
				if (resultado[0] != null) {
					not.setFcaducidad((java.util.Date) resultado[0]);
					if (not.getFcaducidad().before(actual)) {
						return false;
					}
				}
				if (resultado[1] != null) {
					not.setFpublicacion((java.util.Date) resultado[1]);
					if (not.getFpublicacion().after(actual)) {
						return false;
					}
				}
				if (resultado[2] != null) {
					not.setVisible((resultado[2].toString()));
					if (not.getVisible().equals(noVisible)) {
						return false;
					}
				}
			}
		}

		return true;

	}

	private boolean esAgenda(final Session session, final Long id) {
		final String noVisible = "N";
		final String hql = "select agenda.ffin, agenda.finicio,agenda.visible from Agenda agenda join agenda.traducciones trad join trad.documento archi where archi.id="
				+ id + " or trad.imagen=" + id;

		final Query query = session.createQuery(hql);

		final List<Object[]> resultados = query.list();
		if (resultados.isEmpty()) {
			return esMenu(session, id);
		}
		resultados.get(0);

		if (resultados != null && !resultados.isEmpty()) {
			for (final Object[] resultado : resultados) {
				final Date actual = new Date();
				final Agenda agen = new Agenda();
				/*if (resultado[0] != null) {
					agen.setFfin((java.util.Date) resultado[0]);
					if (agen.getFfin().before(actual)) {
						return false;
					}
				}
				if (resultado[1] != null) {
					agen.setFinicio((java.util.Date) resultado[1]);
					if (agen.getFinicio().after(actual)) {
						return false;
					}
				}*/
				if (resultado[2] != null) {
					agen.setVisible((resultado[2].toString()));
					if (agen.getVisible().equals(noVisible)) {
						return false;
					}
				}
			}
		}

		return true;

	}

	private boolean esMenu(final Session session, final Long id) {
		final String noVisible = "N";
		final String hql = "select menu.visible from Menu menu join  menu.imagenmenu archi where archi.id=" + id;

		final Query query = session.createQuery(hql);

		final List<Object[]> resultados = query.list();
		if (resultados.isEmpty()) {
			return esEncuesta(session, id);
		}

		return "S".equals(resultados.get(0));

	}

	private boolean esEncuesta(final Session session, final Long id) {
		final String noVisible = "N";
		final String hql = "select encuesta.fcaducidad, encuesta.fpublicacion,encuesta.visible,preg.visible from Encuesta encuesta join  encuesta.preguntas preg join  preg.imagen archi where archi.id="
				+ id;

		final Query query = session.createQuery(hql);

		final List<Object[]> resultados = query.list();
		if (resultados.isEmpty()) {
			return true;
		}
		resultados.get(0);

		if (resultados != null && !resultados.isEmpty()) {
			for (final Object[] resultado : resultados) {
				final Date actual = new Date();
				final Encuesta encuesta = new Encuesta();
				final Pregunta pregunta = new Pregunta();
				if (resultado[0] != null) {
					encuesta.setFcaducidad((java.util.Date) resultado[0]);
					if (encuesta.getFcaducidad().before(actual)) {
						return false;
					}
				}
				if (resultado[1] != null) {
					encuesta.setFpublicacion((java.util.Date) resultado[1]);
					if (encuesta.getFpublicacion().after(actual)) {
						return false;
					}
				}
				if (resultado[2] != null) {
					encuesta.setVisible((resultado[2].toString()));
					if (encuesta.getVisible().equals(noVisible)) {
						return false;
					}
				}
				if (resultado[3] != null) {
					pregunta.setVisible((resultado[3].toString()));
					if (pregunta.getVisible().equals(noVisible)) {
						return false;
					}
				}
			}
		}

		return true;

	}

}