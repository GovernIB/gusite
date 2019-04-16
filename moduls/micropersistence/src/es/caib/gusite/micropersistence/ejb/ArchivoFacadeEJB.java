package es.caib.gusite.micropersistence.ejb;

import java.io.IOException;
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

import es.caib.gusite.micromodel.Archivo;
import es.caib.gusite.micromodel.ArchivoFull;
import es.caib.gusite.micromodel.Auditoria;
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

@SuppressWarnings({"deprecation", "unchecked"})
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
		} catch (Exception ex) {
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
	public Archivo obtenerArchivo(Long id) {

		Session session = this.getSession();
		Archivo archi = null;

		try {
			
			Query query = session.createQuery("from Archivo a where a.id =" + id.toString());
			archi = (Archivo) query.uniqueResult();
			
			/*
			if (!ArchivoUtil.almacenarEnFilesystem()) {
				query = session.createQuery("from ArchivoFull a where a.id =" + id.toString());
				ArchivoFull archivoFull = (ArchivoFull) query.uniqueResult();
				archi.setDatos(archivoFull.getDatos());
			}*/
			
			return archi;

		} catch (HibernateException he) {
			
			throw new EJBException(he);
			
		}  finally {
			
			this.close(session);
			
		}
		
	}
	
	/**
	 * Obtiene los archivos de un microsite
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<Archivo> obtenerArchivoByMicrositeId(Long idMicrosite) throws DelegateException {

		Session session = this.getSession();		

		try {
			
			Query query = session.createQuery("from Archivo a where a.idmicrosite =" + idMicrosite.toString());
			List<Archivo> lista = query.list();
									
			return lista;

		} catch (HibernateException he) {
			
			throw new EJBException(he);
			
		}  finally {
			
			this.close(session);
			
		}
		
	}


	/**
	 * Devuelve una lista con todos los Ids de los archivos en la tabla
	 * GUS_DOCUS (necesario para proceso de exportación a disco).
	 * solo retorna los no exportados a File System (los que tienen flag exportadoAFileSystem <> 'S' ), es decir los pendientes de analizar (X), los que se encuentran en error(E), y los que no están en FS (N)
	 * - numeroMaximoElementos indica el numero máximo de elementos que se retornaran.
	 * - omitirErroresYaTratados permite indicar si se deben omitir los ficheros ya tratados que al procesarlos dieron error.
	 * @ejb.permission 
	 * 			role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public List<Object[]> obtenerTodosLosArchivosSinBlobs(int numeroMaximoElementos,boolean omitirErroresYaTratados) {

		Session session = this.getSession();
		
		try {

			// amartin: order by a.idmicrosite desc, a.id asc para que el último
			// microsite sea el 0 (¿archivos comunes?).
			
			String sQuery = "select a.id, a.nombre, a.idmicrosite from Archivo a where exportadoAFileSystem <> '" + Archivo.ESTADO_ENFILESYSTEM_SI + "' ";
			if(omitirErroresYaTratados) {
				sQuery +=  " and exportadoAFileSystem <> '" + Archivo.ESTADO_ENFILESYSTEM_ERROR_EXPORTACION + "' ";
			}
			sQuery += " order by a.idmicrosite desc, a.id asc";
			
			Query query = session.createQuery(sQuery);
			if(numeroMaximoElementos>0) {
				query.setMaxResults(numeroMaximoElementos);
			}
			
			List<Object[]> lista = query.list();
			
			return lista;

		} catch (HibernateException he) {
			
			throw new EJBException(he);
			
		} finally {
			
			this.close(session);			
		}		
	}
	
	
	/**
	 * Retorna el numero total de ficheros
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Long NumeroTotaldeFicheros() {

		Session session = this.getSession();
		
		try {
			Query query = session.createQuery("select count(a.id) as numficheros from Archivo a ");
			return (Long) query.uniqueResult();

		} catch (HibernateException he) {
			
			throw new EJBException(he);
			
		} finally {
			
			this.close(session);			
		}		
	}
	
	/**
	 * Retorna el numero total de ficheros pendientes de ser tratados por el proceso de exportación a filesystem, (estados X,N y E)
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Long NumeroPendientesExportarAFileSystem() {

		Session session = this.getSession();
		
		try {
			Query query = session.createQuery("select count(a.id) as numPendientesExportar from Archivo a where exportadoAFileSystem <> '" + Archivo.ESTADO_ENFILESYSTEM_SI + "'");
			return (Long) query.uniqueResult();

		} catch (HibernateException he) {
			
			throw new EJBException(he);
			
		} finally {
			
			this.close(session);			
		}		
	}
	
	/**
	 * Retorna el numero total de ficheros ya tratados por el proceso de exportación a filesystem
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Long NumeroExportadosAFileSystem() {

		Session session = this.getSession();
		
		try {
			Query query = session.createQuery("select count(a.id) as numPendientesExportar from Archivo a where exportadoAFileSystem = '"+ Archivo.ESTADO_ENFILESYSTEM_SI +"'");
			return (Long) query.uniqueResult();

		} catch (HibernateException he) {
			
			throw new EJBException(he);
			
		} finally {
			
			this.close(session);			
		}		
	}
	
	/**
	 * Retorna el numero total de ficheros ya tratados por el proceso de exportación a filesystem y que se encuentran en error
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Long NumeroExportadosAFileSystemConError() {

		Session session = this.getSession();
		
		try {
			Query query = session.createQuery("select count(a.id) as numPendientesExportar from Archivo a where exportadoAFileSystem = '"+ Archivo.ESTADO_ENFILESYSTEM_ERROR_EXPORTACION +"'");
			return (Long) query.uniqueResult();

		} catch (HibernateException he) {
			
			throw new EJBException(he);
			
		} finally {
			
			this.close(session);			
		}		
	}
	
	
	
	/**
	 * Actualiza los archivos indicados en listado y los marca con el estado indicado exportado a filesystem
	 * (S) Indica que el fichero ya ha sido exportado a File system  
	 * (N) Indica que el fichero esta pendiente de exportar, 
	 * (X) Indica que el fichero no se ha analizado  o 
	 * (E) Indica que ocurrio un error al exportar 
	 * @ejb.permission 
	 * 			role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public int MarcarcomoExportadosAFileSystem(List <Long> listado, String estado) {
		int res = 0;
		
		if(estado.equals("S") || estado.equals("N") || estado.equals("X") || estado.equals("E")) {

			if(listado!=null && listado.size()>0) {
				Session session = this.getSession();			
				try {
					
					StringBuilder ids = new StringBuilder();
					boolean primero = true;
					for (Long s : listado)
					{
						if(primero) {
							primero = false;
						}else {
							ids.append(",");
						}
						ids.append(s);
					}
					
					Query query = session.createQuery("Update Archivo a set a.exportadoAFileSystem = '" + estado + "' where a.id in ("+ ids.toString() +") ");
					res = query.executeUpdate();				
					session.flush();
		
				} catch (HibernateException he) {
					throw new EJBException(he);
				} finally {
					this.close(session);			
				}		
			}
		
		}
		
		return res;
	}
	
	
	

	/**
	 * Obtiene un archivo por el nombre Comprobamos que pertenece al microsite o
	 * es público (microsite=0)
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public Archivo obtenerArchivobyName(Long site, String nombre) {

		Session session = this.getSession();
		try {
			// TODO amartin: ¿usar método checkSite() aquí, propio de este mismo
			// EJB, para ahorrar código?

			Criteria criteriID = session.createCriteria(Archivo.class);
			criteriID.add(Restrictions.eq("idmicrosite", site));
			criteriID.add(Restrictions.eq("nombre", nombre));

			List<?> listArchi = criteriID.list();
			Iterator<?> iterArchi = listArchi.iterator();
			Archivo archi = null;

			while (iterArchi.hasNext()) {
				archi = (Archivo) iterArchi.next();

				//if (ArchivoUtil.almacenarEnFilesystem()) {
				//	this.obtenerDatosArchivoExportado(archi);
				//}

				return archi;
			}

			Long site0 = new Long(0);
			Criteria criteri = session.createCriteria(Archivo.class);
			criteri.add(Restrictions.eq("idmicrosite", site0));
			criteri.add(Restrictions.eq("nombre", nombre));

			listArchi = criteri.list();
			iterArchi = listArchi.iterator();

			while (iterArchi.hasNext()) {

				archi = (Archivo) iterArchi.next();
				String hql = "from Noticia noti join noti.traducciones trad join trad.docu doc where doc.id='"
						+ archi.getId()
						+ "' and noti.idmicrosite='"
						+ site
						+ "'";
				Query query = session.createQuery(hql);
				List<?> listNot = query.list();

				if (listNot.size() == 1) {

					//if (ArchivoUtil.almacenarEnFilesystem()) {
					//	this.obtenerDatosArchivoExportado(archi);
					//}

					return archi;
				}
			}

			return archi;

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
			
			Query query = session.createQuery("from Archivo archi where (archi.idmicrosite is null or archi.idmicrosite = 0 or archi.idmicrosite=" + site.toString() + ") and archi.id=" + id.toString());
			query.setMaxResults(1);
			return query.uniqueResult() == null;
			//Tendría que ser la comparación al revés?!?!
			//return query.list().isEmpty();

		} catch (HibernateException he) {
			
			throw new EJBException(he);
			
		} finally {
			
			this.close(session);
			
		}
		
	}

	/**
	 * Inserta un nuevo documento en la BD
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public Long insertarArchivo(Archivo a) throws DelegateException {

		final Session session = this.getSession();
		
		try {
			final Transaction tx = session.beginTransaction();

			if (a.getDatos() == null) {
				throw new FicheroVacioException("Fichero vacío.");
			}
			
			// Guardamos para obtener el ID del registro.
			session.saveOrUpdate(a);
			
			//actualizamos el check que indica si el fichero está en filesystem o no
			//por ahora se almacena tanto en FS como en BBDD, pero aún asi se marca como exportado o no exportado a fs
			if (ArchivoUtil.almacenarEnFilesystem()) {
				a.setExportadoAFileSystem(Archivo.ESTADO_ENFILESYSTEM_SI);	
				// Guardamos archivo en FS.
				ArchivoUtil.exportaArchivoAFilesystem(a);
			}else {
				a.setExportadoAFileSystem(Archivo.ESTADO_ENFILESYSTEM_NO);
				// Guardamos el archivo full. (BBDD)
				final ArchivoFull archivoFull = new ArchivoFull();
				archivoFull.setId(a.getId());
				archivoFull.setDatos(a.getDatos());
				//Guardamos el contenido del fichero.
				session.saveOrUpdate(archivoFull);
			}				
			
			// Null datos para evitar cache 
			a.setDatos(null);

			session.flush();
			
			tx.commit();
			
			return a.getId();

		} catch (HibernateException he) {
			
			throw new EJBException(he);
			
		} catch (IOException e) {
			
			throw new EJBException(e);
			
		} finally {
			
			this.close(session);
			
		}
		
	}
	
	/**
	 * Obtiene el contenido del fichero dependiendo de si está en modo filesystem 
	 *   o en modo base de datos.
	 * @throws IOException 
	 * 
	 * @ejb.interface-method	 * 
	 * @ejb.permission unchecked="true"
	 */
	public byte[] obtenerContenidoFichero(Archivo archivo) throws DelegateException, IOException {
		return obtenerContenidoFichero(archivo, ArchivoUtil.almacenarEnFilesystem());
	}
	
	/**
	 * Obtiene el contenido del fichero dependiendo de si está en modo filesystem 
	 *   o en modo base de datos (lo marca la variable de entrada). Importante, a 
	 *   este método sólo se le puede llamar directamente desde volcar datos de microsites a filesystem!.
	 * @throws IOException 
	 * 
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 */
	public byte[] obtenerContenidoFichero(Archivo archivo, boolean isAlmacenarEnFilesystem) throws DelegateException, IOException {
		
	
		byte[] datos;
		if (isAlmacenarEnFilesystem) {
			datos = ArchivoUtil.obtenerDatosArchivoEnFilesystem(archivo);
		} else {
			final Session session = this.getSession();
			final Criteria criteri = session.createCriteria(ArchivoFull.class);
			criteri.add(Restrictions.eq("id", archivo.getId()));
			ArchivoFull archivoFull = (ArchivoFull) criteri.uniqueResult();
			datos = archivoFull.getDatos();
		}
		return datos;
	}

	/**
	 * Borra un documento de la BD
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public void borrarArchivo(Long id) throws DelegateException {

		Session session = this.getSession();
		
		try {
			
			Archivo a = (Archivo) session.get(Archivo.class, id);

			if (ArchivoUtil.existeArchivoEnFilesystem(a)) {
				ArchivoUtil.borrarArchivo(a);
			}
			
			session.delete(a);
			session.flush();
            session.close();

            a.setIdmicrosite(null);
            a.setDatos(null);
            
			this.grabarAuditoria(a, Auditoria.ELIMINAR);

		} catch (HibernateException he) {
			
			throw new EJBException(he);
			
		} catch (IOException e) {
			
			throw new EJBException(e);
			
		} finally {
			
			this.close(session);
			
		}
		
	}
	
	/**
	 * Borra una lista de documentos de la BD
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 * 			role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 * @param id
	 * 			Id del archivo
	 * @throws DelegateException
	 */
	public void borrarArchivos(List<Archivo> lista) throws DelegateException {
		
		Session session = this.getSession();
		
		try {
			
			for (Archivo a : lista) {

				if (ArchivoUtil.existeArchivoEnFilesystem(a)) {
					ArchivoUtil.borrarArchivo(a);
				}
				
				session.delete(a);
				session.flush();
	
	            a.setIdmicrosite(null);
	            a.setDatos(null);
	            
				this.grabarAuditoria(a, Auditoria.ELIMINAR);
			
			}
			
		} catch (HibernateException he) {
			
			throw new EJBException(he);
			
		} catch (IOException e) {
			
			throw new EJBException(e);
			
		} finally {
			
			this.close(session);
			
		}
		
	}

	/**
	 * Crea o actualiza un archivo
	 * 
	 * @ejb.interface-method
	 * @ejb.permission 
	 *                 role-name="${role.system},${role.admin},${role.super},${role.oper}"
	 */
	public Long grabarArchivo(Archivo a) {

		Session session = this.getSession();
		boolean nuevo = (a.getId() == null) ? true : false;
		
		try {
			
			Transaction tx = session.beginTransaction();

			if (a.getDatos() == null) {
				throw new FicheroVacioException("Fichero vacío.");
			}
			
			if (!nuevo) {
				Archivo aOld = this.obtenerArchivo(a.getId());
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
			}else {
				a.setExportadoAFileSystem(Archivo.ESTADO_ENFILESYSTEM_NO);
				//Guardamos el fichero full.
				ArchivoFull archivoFull = new ArchivoFull();
				archivoFull.setId(a.getId());
				archivoFull.setDatos(a.getDatos());
				
				//Guardamos el contenido del fichero.
				session.saveOrUpdate(archivoFull);				
			}
			
			// Null datos para evitar cache 
			a.setDatos(null);
			
			//Guardamos el fichero
			session.saveOrUpdate(a);
						
			session.flush();
			
			tx.commit();

			int op = (nuevo) ? Auditoria.CREAR : Auditoria.MODIFICAR;
			this.grabarAuditoria(a, op);

			return a.getId();

		} catch (HibernateException he) {
			
			if (!nuevo) {
				//this.indexBorraArchivo(a.getId());
			}
			throw new EJBException(he);
			
		} catch (IOException e) {
			
			if (!nuevo) {
				//this.indexBorraArchivo(a.getId());
			}
			throw new EJBException(e);
			
		} finally {
			
			this.close(session);
			
		}
		
	}


}