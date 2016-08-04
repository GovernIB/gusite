package es.caib.gusite.micropersistence.delegate;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.Handle;
import javax.naming.NamingException;

import es.caib.gusite.lucene.model.ModelFilterObject;
import es.caib.gusite.micromodel.Archivo;
import es.caib.gusite.micropersistence.intf.ArchivoFacade;
import es.caib.gusite.micropersistence.intf.ArchivoFacadeHome;
import es.caib.gusite.micropersistence.util.ArchivoFacadeUtil;

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
	public Archivo obtenerArchivo(Long id) throws DelegateException {
		try {
			return this.getFacade().obtenerArchivo(id);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Obtiene un archivo por el nombre Comprobamos que pertenece al microsite o
	 * es público (microsite=0)
	 * 
	 * @param site
	 *            id del site
	 * @param nombre
	 *            nombre del archivo
	 * @return Archivo
	 * @throws DelegateException
	 */
	public Archivo obtenerArchivobyName(Long site, String nombre)
			throws DelegateException {
		try {
			return this.getFacade().obtenerArchivobyName(site, nombre);
		} catch (RemoteException e) {
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
	public boolean checkSite(Long id, Long idsite) throws DelegateException {
		try {
			return this.getFacade().checkSite(id, idsite);
		} catch (RemoteException e) {
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
	public Long insertarArchivo(Archivo archi) throws DelegateException {
		try {
			return this.getFacade().insertarArchivo(archi);
		} catch (RemoteException e) {
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
	public void borrarArchivo(Long id) throws DelegateException {
		try {
			this.getFacade().borrarArchivo(id);
		} catch (RemoteException e) {
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
	public void borrarArchivos(List<Archivo> lista) throws DelegateException {
		try {
			this.getFacade().borrarArchivos(lista);
		} catch (RemoteException e) {
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
	public Long grabarArchivo(Archivo archi) throws DelegateException {
		try {
			return this.getFacade().grabarArchivo(archi);
		} catch (RemoteException e) {
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
	public List<Object[]> obtenerTodosLosArchivosSinBlobs()
			throws DelegateException {
		try {
			return this.getFacade().obtenerTodosLosArchivosSinBlobs();
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Añade un archivo al indice en todos los idiomas
	 * 
	 * @param archi
	 * @param filter
	 * @throws DelegateException
	 */
	public void indexInsertaArchivo(Archivo archi, ModelFilterObject filter)
			throws DelegateException {
		try {
			this.getFacade().indexInsertaArchivo(archi, filter);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Elimina el archivo del indice en todos los idiomas
	 * 
	 * @param id
	 *            Id del archivo
	 * @throws DelegateException
	 */
	public void indexBorraArchivo(Long id) throws DelegateException {
		try {
			this.getFacade().indexBorraArchivo(id);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}
	
	/**
	 * Obtiene el contenido del fichero dependiente de si está en modo 
	 *   filesystem o base de datos. 
	 * 
	 * @param archivo Archivo
	 * @throws DelegateException
	 */
	public byte[] obtenerContenidoFichero(Archivo archivo) throws DelegateException, IOException  {
		try {
			return this.getFacade().obtenerContenidoFichero(archivo);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}
	
	
	/**
	 * Obtiene el contenido del fichero, obtiene el dato de filesystem o de ddbb dependiendo 
	 *   del parámetro de netrada.
	 * 
	 * @param archivo Archivo
	 * @param true = obtiene dato por filesystem , false = obtiene dato por bbdd
	 * @throws DelegateException
	 */
	public byte[] obtenerContenidoFichero(Archivo archivo, boolean isFileSystemActivo) throws DelegateException, IOException  {
		try {
			return this.getFacade().obtenerContenidoFichero(archivo, isFileSystemActivo);
		} catch (RemoteException e) {
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

	protected ArchivoDelegate() throws DelegateException {
		try {
			ArchivoFacadeHome home = ArchivoFacadeUtil.getHome();
			ArchivoFacade remote = home.create();
			this.facadeHandle = remote.getHandle();
		} catch (NamingException e) {
			throw new DelegateException(e);
		} catch (CreateException e) {
			throw new DelegateException(e);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}
}