package es.caib.gusite.micropersistence.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.gusite.micromodel.Archivo;
import es.caib.gusite.micropersistence.delegate.ArchivoDelegate;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.util.log.MicroLog;

/**
 * @author amartin
 * 
 */
public class ArchivoUtil {

	private static Log log = LogFactory.getLog(ArchivoUtil.class);
	private static ResourceBundle rb = ResourceBundle.getBundle("sac-micropersistence-messages");

	/**
	 * Comprueba que la propiedad de sistema
	 * es.caib.gusite.archivos.almacenarEnFilesystem sea igual a [S|s].
	 * 
	 * @return
	 */
	public static boolean almacenarEnFilesystem() {

		String almacenarEnFilesystemString = System.getProperty("es.caib.gusite.archivos.almacenarEnFilesystem");
		boolean almacenarEnFilesystem = ("S".equals(almacenarEnFilesystemString) || "s".equals(almacenarEnFilesystemString));

		return almacenarEnFilesystem;

	}

	/**
	 * Exporta el BLOB asociado a un objeto de tipo Archivo al sistema de
	 * archivos.
	 * 
	 * @param archivo
	 * @throws IOException
	 */
	public static void exportaArchivoAFilesystem(Archivo archivo) throws IOException {

		if (almacenarEnFilesystem()) {

			ArchivoUtil.deleteDirArchivo(archivo); //Borramos el archivo anterior si existía
			
			Long idMicrosite = archivo.getIdmicrosite();

			// Comprobar que existe el directorio de la aplicación donde se guardarán los archivos de cada microsite.
			String rutaArchivosEnFileSystem = obtenerRutaArchivosEnFileSystem();
			checkDirArchivos(rutaArchivosEnFileSystem);

			// Directorio de archivos del propio microsite.
			String rutaArchivosEnFileSystemMicrosite = obtenerRutaArchivosEnFileSystemMicrosite(idMicrosite);
			checkDirArchivosMicrosite(rutaArchivosEnFileSystemMicrosite);

			// Directorio con el nombre archivo.getId(), por si hay archivos con el mismo nombre asociados a un mismo Microsite.
			checkDirArchivo(archivo);

			// Comprobar si el archivo ya existe en FS. Si no es así, lo exportamos.
			File f = new File(obtenerRutaArchivoExportadoEnFilesystem(archivo));
			escribeArchivoAFilesystem(f, archivo.getDatos());

		}

	}

	/**
	 * Exporta todos los BLOB de la base de datos al sistema de archivos.
	 * 
	 * @throws DelegateException
	 * @throws IOException
	 */
	public static void exportarArchivosDeTodosLosMicrosites(HttpServletRequest request) throws DelegateException {

		log.info("Comenzando proceso de exportación de archivos de los microsites");
		addImportLogVisual(request, "Comenzando proceso de exportación de archivos de los microsites");

		ArchivoDelegate archivoDelegate = DelegateUtil.getArchivoDelegate();
		List<Object[]> listaArchivos = archivoDelegate.obtenerTodosLosArchivosSinBlobs();
		Iterator<Object[]> it = listaArchivos.iterator();

		// Primero detectamos qué archivos hay que exportar y cuáles ya han sido
		// exportados.
		while (it.hasNext()) {

			Object[] a = it.next();

			Long idArchivo = (Long) a[0];
			String nombreArchivo = (String) a[1];
			Long idMicrosite = (Long) a[2];

			try {

				// Si el archivo ya existe en el FS, lo eliminamos de la lista
				// de archivos pendientes de exportar.
				if (existeArchivoEnFilesystem(idArchivo, nombreArchivo, obtenerRutaArchivosEnFileSystemMicrosite(idMicrosite))) {

					String mensaje = "El archivo " + obtenerRutaArchivosEnFileSystemMicrosite(idMicrosite) + File.separator + idArchivo.toString()
							+ "" + " ya existe. Lo eliminamos de los pendientes de exportar.";
					log.info(mensaje);
					addImportLogVisual(request, mensaje);

					it.remove();

				}

			} catch (IOException e) {

				String mensaje = "Error tratando el archivo con id = " + idArchivo;
				log.error(mensaje, e);
				addImportLogVisualStackTrace(request, mensaje, e.getStackTrace());

				// Eliminamos de la lista el archivo que no se ha podido tratar.
				it.remove();

			}

		}

		log.info("Procesados los archivos pendientes de tratar. Quedan por escribir en disco: " + listaArchivos.size() + " archivos");
		addImportLogVisual(request, "Procesados los archivos pendientes de tratar. Quedan por escribir en disco: " + listaArchivos.size()
				+ " archivos");

		// Exportamos los que quedan en la lista.
		List<String> exportados = new ArrayList<String>();

		for (Object[] a : listaArchivos) {

			Long idArchivo = (Long) a[0];

			// Obtenemos archivo con BLOB incluido.
			Archivo archivo = archivoDelegate.obtenerArchivo(idArchivo);

			try {

				exportaArchivoAFilesystem(archivo);
				exportados.add(obtenerRutaArchivoExportadoEnFilesystem(archivo));

			} catch (IOException e) {

				String mensaje = "Error exportando el archivo con id = " + idArchivo;
				log.error(mensaje, e);
				addImportLogVisualStackTrace(request, mensaje, e.getStackTrace());

			}

		}

		log.info("Finalizado el proceso de exportación de archivos a disco. Se han exportado " + exportados.size() + " archivos");
		addImportLogVisual(request, "Finalizado el proceso de exportación de archivos a disco. Se han exportado " + exportados.size() + " archivos");

	}

	/**
	 * Lee los datos de un archivo que ya ha sido exportado al sistema de
	 * archivos.
	 * 
	 * @param archivo
	 * @return
	 * @throws IOException
	 */
	public static byte[] obtenerDatosArchivoEnFilesystem(Archivo archivo) throws IOException {

		byte[] datos = null;

		InputStream in = new FileInputStream(obtenerRutaArchivoExportadoEnFilesystem(archivo));
		datos = IOUtils.toByteArray(in);
		in.close();

		return datos;

	}

	/**
	 * Comprueba si un archivo existe en la estructura de archivos exportados de
	 * los microsites.
	 * 
	 * @param idArchivo
	 * @param nombreArchivo
	 * @param rutaArchivosEnFileSystemMicrosite
	 * @return
	 */
	public static boolean existeArchivoEnFilesystem(Long idArchivo, String nombreArchivo, String rutaArchivosEnFileSystemMicrosite) {

		File f = new File(rutaArchivosEnFileSystemMicrosite + File.separator + idArchivo.toString() + File.separator + nombreArchivo);
		return f.exists();

	}

	public static boolean existeArchivoEnFilesystem(Archivo a) throws IOException {

		String rutaArchivosEnFileSystemMicrosite = obtenerRutaArchivosEnFileSystemMicrosite(a.getIdmicrosite());
		File f = new File(rutaArchivosEnFileSystemMicrosite + File.separator + a.getId().toString() + File.separator + a.getNombre());

		return f.exists();

	}

	/**
	 * Obtiene la ruta en la que se almacenarán los archivos de un microsite.
	 * 
	 * @param idMicrosite
	 * @return
	 * @throws IOException
	 */
	public static String obtenerRutaArchivosEnFileSystemMicrosite(Long idMicrosite) throws IOException {

		String rutaArchivosEnFileSystem = obtenerRutaArchivosEnFileSystem();
		if (idMicrosite != null) {
			return rutaArchivosEnFileSystem + File.separator + idMicrosite.toString();
		} else {
			return rutaArchivosEnFileSystem + File.separator + "nomic";
		}

	}

	public static void borrarArchivo(Archivo a) throws IOException {

		if (existeArchivoEnFilesystem(a.getId(), a.getNombre(), obtenerRutaArchivosEnFileSystemMicrosite(a.getIdmicrosite()))) {

			// Directorio que contiene el propio archivo.
			File fDir = new File(obtenerRutaDirArchivoExportadoEnFilesystem(a));
			// El archivo a borrar.
			File f = new File(obtenerRutaArchivoExportadoEnFilesystem(a));

			try {

				f.delete();
				fDir.delete();

			} catch (SecurityException e) {

				log.error(e);
				throw new IOException(e);

			}

		}

	}

	private static void escribeArchivoAFilesystem(File f, byte[] datos) throws IOException {

		if (datos != null) {

			InputStream in = new ByteArrayInputStream(datos);
			OutputStream out = new FileOutputStream(f);

			byte[] buf = new byte[1024];
			int len;

			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}

			out.close();
			in.close();

		}

	}

	private static String obtenerRutaArchivosEnFileSystem() throws IOException {

		String rutaArchivosEnFileSystem = System.getProperty("es.caib.gusite.archivos.rutaArchivosEnFileSystem");
		if (rutaArchivosEnFileSystem == null || rutaArchivosEnFileSystem.length() == 0) {
			throw new IOException(rb.getString("errors.propiedadRutaArchivosEnFileSystem") + " " + rutaArchivosEnFileSystem);
		}

		return rutaArchivosEnFileSystem;

	}

	private static String obtenerRutaDirArchivoExportadoEnFilesystem(Archivo archivo) throws IOException {

		String rutaArchivosMicrosite = obtenerRutaArchivosEnFileSystemMicrosite(archivo.getIdmicrosite());

		// Ruta: [rutaArchivosMicrosites]/idMicrosite/idArchivo/nombreArchivo
		return rutaArchivosMicrosite + File.separator + archivo.getId().toString();

	}

	private static String obtenerRutaArchivoExportadoEnFilesystem(Archivo archivo) throws IOException {

		String rutaArchivosMicrosite = obtenerRutaArchivosEnFileSystemMicrosite(archivo.getIdmicrosite());

		// Ruta: [rutaArchivosMicrosites]/idMicrosite/idArchivo/nombreArchivo
		return rutaArchivosMicrosite + File.separator + archivo.getId().toString() + File.separator + archivo.getNombre();

	}

	private static void checkDirArchivos(String rutaArchivosEnFileSystem) throws IOException {

		File dirArchivos = new File(rutaArchivosEnFileSystem);

		if (!dirArchivos.exists()) {

			try {

				dirArchivos.mkdir();

			} catch (SecurityException e) {

				e.printStackTrace();
				throw new IOException(rb.getString("error.creacionDirRutaArchivosEnFileSystem") + " " + rutaArchivosEnFileSystem);

			}

		} else {

			if (!dirArchivos.isDirectory()) {
				throw new IOException(rb.getString("error.noEsDirectorioRutaArchivosEnFileSystem") + " " + rutaArchivosEnFileSystem);
			}

		}

	}

	private static void checkDirArchivosMicrosite(String rutaArchivosEnFileSystemMicrosite) throws IOException {

		File dirArchivosMicrosite = new File(rutaArchivosEnFileSystemMicrosite);

		if (!dirArchivosMicrosite.exists()) {

			try {

				dirArchivosMicrosite.mkdir();

			} catch (SecurityException e) {

				e.printStackTrace();
				throw new IOException(rb.getString("error.creacionRutaArchivosEnFileSystemMicrosite") + " " + rutaArchivosEnFileSystemMicrosite);

			}

		} else {

			if (!dirArchivosMicrosite.isDirectory()) {
				throw new IOException(rb.getString("error.noEsDirectorioRutaArchivosEnFileSystemMicrosite") + " " + rutaArchivosEnFileSystemMicrosite);
			}

		}

	}

	private static void deleteDirArchivo(Archivo archivo) throws IOException {

		String rutaDirArchivo = obtenerRutaArchivosEnFileSystemMicrosite(archivo.getIdmicrosite()) + File.separator + archivo.getId().toString();
		File dirArchivo = new File(rutaDirArchivo);

		if (dirArchivo.exists()) {
			for(String s: dirArchivo.list()){
			    File currentFile = new File(dirArchivo.getPath(),s);
			    currentFile.delete();
			}			
			dirArchivo.delete();
		}

	}

	private static void checkDirArchivo(Archivo archivo) throws IOException {

		String rutaDirArchivo = obtenerRutaArchivosEnFileSystemMicrosite(archivo.getIdmicrosite()) + File.separator + archivo.getId().toString();
		File dirArchivo = new File(rutaDirArchivo);

		if (!dirArchivo.exists()) {
			dirArchivo.mkdir();
		}

	}

	private static void addImportLogVisual(HttpServletRequest request, String mensaje) {
		MicroLog.addLogVisual(request, mensaje);
	}

	private static void addImportLogVisualStackTrace(HttpServletRequest request, String mensaje, StackTraceElement[] mensajes) {
		MicroLog.addLogVisualStackTrace(request, mensaje, mensajes);
	}

}
