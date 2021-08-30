package es.caib.gusite.micropersistence.delegate;

import java.io.IOException;
import java.rmi.RemoteException;
import java.text.Normalizer;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.Handle;
import javax.naming.NamingException;

import es.caib.gusite.micromodel.Archivo;
import es.caib.gusite.micropersistence.intf.ArchivoFacade;
import es.caib.gusite.micropersistence.intf.ArchivoFacadeHome;
import es.caib.gusite.micropersistence.util.ArchivoFacadeUtil;
import es.caib.gusite.micropersistence.util.IndexacionUtil;
import es.caib.solr.api.model.types.EnumCategoria;

/**
 * Business delegate para manipular Archivos.
 *
 * @author Indra
 */
public class ArchivoDelegate implements StatelessDelegate {

	private static final long serialVersionUID = 8576421542430044922L;

	/* ========================================================= */
	/* ======================== MÉTODOS DE NEGOCIO ============= */
	/* ========================================================= */

	/**
	 * Obtiene el archivo Comprobamos que pertenece al microsite o es público
	 * (microsite=0)
	 *
	 * @param id
	 *            id del Archivo
	 * @return Archivo
	 * @throws DelegateException
	 */
	public Archivo obtenerArchivo(final Long id) throws DelegateException {
		try {
			return this.getFacade().obtenerArchivo(id);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Obtiene un archivo por el nombre Comprobamos que pertenece al microsite o es
	 * público (microsite=0)
	 *
	 * @param site
	 *            id del site
	 * @param nombre
	 *            nombre del archivo
	 * @return Archivo
	 * @throws DelegateException
	 */
	public Archivo obtenerArchivobyName(final Long site, final String nombre) throws DelegateException {
		try {
			return this.getFacade().obtenerArchivobyName(site, nombre);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Comprueba que el elemento pertenece al Microsite
	 *
	 * @param id
	 *            Id del archivo
	 * @param idsite
	 *            Id del site
	 * @return true si no esta relacionado
	 * @throws DelegateException
	 */
	public boolean checkSite(final Long id, final Long idsite) throws DelegateException {
		try {
			return this.getFacade().checkSite(id, idsite);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Comprueba que el elemento es visible
	 *
	 * @param id
	 *            Id del archivo
	 * @param idsite
	 *            Id del site
	 * @return true si no esta relacionado
	 * @throws DelegateException
	 */
	public boolean visible(final Long id) throws DelegateException {
		try {
			return this.getFacade().visible(id);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Inserta un nuevo documento en la BD
	 *
	 * @param archi
	 * @return Id del documento
	 * @throws DelegateException
	 */
	public Long insertarArchivo(final Archivo archi) throws DelegateException {
		try {
			archi.setNombre(normalizador(archi.getNombre()));
			return this.getFacade().insertarArchivo(archi);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Borrar un archivo
	 *
	 * @param id
	 *            Id del archivo
	 * @throws DelegateException
	 */
	public void borrarArchivo(final Long id) throws DelegateException {
		try {
			this.getFacade().borrarArchivo(id);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Borrar un archivo
	 *
	 * @param lista
	 *            Lista con los archivos a borrar.
	 * @throws DelegateException
	 */
	public void borrarArchivos(final List<Archivo> lista) throws DelegateException {
		try {
			this.getFacade().borrarArchivos(lista);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Crea o actualiza un archivo
	 *
	 * @param archi
	 * @return Long Id del archivo
	 * @throws DelegateException
	 */
	public Long grabarArchivo(final Archivo archi) throws DelegateException {
		try {
			archi.setNombre(normalizador(archi.getNombre()));
			return this.getFacade().grabarArchivo(archi);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Crea o actualiza un archivo
	 *
	 * @param archi
	 * @return Long Id del archivo
	 * @throws DelegateException
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> obtenerTodosLosArchivosSinBlobs(final int numeroMaximoElementos,
			final boolean omitirErroresYaTratados) throws DelegateException {
		try {
			return this.getFacade().obtenerTodosLosArchivosSinBlobs(numeroMaximoElementos, omitirErroresYaTratados);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public Long NumeroPendientesExportarAFileSystem() throws DelegateException {

		try {
			return this.getFacade().NumeroPendientesExportarAFileSystem();
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public Long NumeroExportadosAFileSystem() throws DelegateException {

		try {
			return this.getFacade().NumeroExportadosAFileSystem();
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public int MarcarcomoExportadosAFileSystem(final List<Long> listado, final String estado) throws DelegateException {
		try {
			return this.getFacade().MarcarcomoExportadosAFileSystem(listado, estado);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public Long NumeroExportadosAFileSystemConError() throws DelegateException {
		try {
			return this.getFacade().NumeroExportadosAFileSystemConError();
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public Long NumeroTotaldeFicheros() throws DelegateException {
		try {
			return this.getFacade().NumeroTotaldeFicheros();
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Obtiene el contenido del fichero dependiente de si está en modo filesystem o
	 * base de datos.
	 *
	 * @param archivo
	 *            Archivo
	 * @throws DelegateException
	 */
	public byte[] obtenerContenidoFichero(final Archivo archivo) throws DelegateException, IOException {
		try {
			return this.getFacade().obtenerContenidoFichero(archivo);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Obtiene el contenido del fichero, obtiene el dato de filesystem o de ddbb
	 * dependiendo del parámetro de netrada.
	 *
	 * @param archivo
	 *            Archivo
	 * @param true
	 *            = obtiene dato por filesystem , false = obtiene dato por bbdd
	 * @throws DelegateException
	 */
	public byte[] obtenerContenidoFichero(final Archivo archivo, final boolean isFileSystemActivo)
			throws DelegateException, IOException {
		try {
			return this.getFacade().obtenerContenidoFichero(archivo, isFileSystemActivo);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Obtiene los archivos de un microsite
	 *
	 * @param idMicrosite
	 * @throws DelegateException
	 */
	public List<Archivo> obtenerArchivoByMicrositeId(final Long idMicrosite) throws DelegateException {
		try {
			return this.getFacade().obtenerArchivoByMicrositeId(idMicrosite);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Inserta un nuevo documento en la BD e indexa
	 *
	 * @param archi
	 * @param idContenido
	 *            si está informado es archivo del documento
	 * @param idMicrosite
	 * @return Id del documento
	 * @throws DelegateException
	 */
	public Long insertarArchivoIndexar(final Archivo archi, final Long idContenido, final Long idMicrosite)
			throws DelegateException {
		try {
			archi.setNombre(normalizador(archi.getNombre()));
			final Long idArchivo = this.getFacade().insertarArchivo(archi);
			final SolrPendienteDelegate pendienteDel = DelegateUtil.getSolrPendienteDelegate();
			Long idElemento = idMicrosite;

			String tipo = EnumCategoria.GUSITE_MICROSITE.toString();

			if (idContenido != null) {
				tipo = EnumCategoria.GUSITE_CONTENIDO.toString();
				idElemento = idContenido;
			}

			pendienteDel.grabarSolrPendiente(tipo, idElemento, idArchivo, IndexacionUtil.REINDEXAR);

			return idArchivo;
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}

	}

	/**
	 *
	 * @param idArchivo
	 * @param idMicrosite
	 * @param idContenido
	 * @throws DelegateException
	 */
	public void borrarArchivoDesindexar(final Long idArchivo, final Long idMicrosite, final Long idContenido)
			throws DelegateException {
		try {
			this.getFacade().borrarArchivo(idArchivo);
			final SolrPendienteDelegate pendienteDel = DelegateUtil.getSolrPendienteDelegate();
			Long idElemento = idMicrosite;

			String tipo = EnumCategoria.GUSITE_MICROSITE.toString();

			if (idContenido != null) {
				tipo = EnumCategoria.GUSITE_CONTENIDO.toString();
				idElemento = idContenido;
			}

			pendienteDel.grabarSolrPendiente(tipo, idElemento, idArchivo, IndexacionUtil.DESINDEXAR);

		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}

	}

	/**
	 * Crea o actualiza un archivo para indexar
	 *
	 * @param archi
	 * @return Long Id del archivo
	 * @throws DelegateException
	 */
	public void grabarArchivoIndexar(final Archivo archi, final Long idContenido, final Long idMicrosite)
			throws DelegateException {
		try {
			archi.setNombre(normalizador(archi.getNombre()));
			this.getFacade().grabarArchivo(archi);
			final SolrPendienteDelegate pendienteDel = DelegateUtil.getSolrPendienteDelegate();
			Long idElemento = idMicrosite;

			String tipo = EnumCategoria.GUSITE_MICROSITE.toString();

			if (idContenido != null) {
				tipo = EnumCategoria.GUSITE_CONTENIDO.toString();
				idElemento = idContenido;
			}

			pendienteDel.grabarSolrPendiente(tipo, idElemento, archi.getId(), IndexacionUtil.REINDEXAR);

		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}

	}

	/* ========================================================= */
	/* ======================== REFERENCIA AL FACADE ========== */
	/* ========================================================= */

	private Handle facadeHandle;

	private ArchivoFacade getFacade() throws RemoteException {
		return (ArchivoFacade) this.facadeHandle.getEJBObject();
	}
	  private static String normalizador(String str)
	  {
	    return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "").replaceAll(" ", "_");
	   }
	protected ArchivoDelegate() throws DelegateException {
		try {
			final ArchivoFacadeHome home = ArchivoFacadeUtil.getHome();
			final ArchivoFacade remote = home.create();
			this.facadeHandle = remote.getHandle();
		} catch (final NamingException e) {
			throw new DelegateException(e);
		} catch (final CreateException e) {
			throw new DelegateException(e);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

}
