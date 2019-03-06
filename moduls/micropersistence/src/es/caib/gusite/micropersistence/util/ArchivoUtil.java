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
import es.caib.gusite.micromodel.ArchivoLite;
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

	/**
	 * Exporta todos los BLOB de la base de datos al sistema de archivos.
	 * 
	 * @throws DelegateException
	 * @throws IOException
	 */
	public static void exportarArchivosDeTodosLosMicrosites(HttpServletRequest request) throws DelegateException {

		final int numelementosMaximosATratarPorDefecto = 10000;
		String paramMaxelemtratar = request.getParameter("numMaxElemTratar");
		
		String somitirEnError = request.getParameter("omitirEnError");
		boolean omitirEnError = !somitirEnError.isEmpty() && "1".equals(somitirEnError);
		
		int numelementosMaximosATratar = numelementosMaximosATratarPorDefecto;
		try {
			numelementosMaximosATratar = Integer.parseInt(paramMaxelemtratar);
			if(numelementosMaximosATratar<0) {
				log.error("Error al recuperar del request numMaxElemTratar, se ha obtenido un número negativo, se setea manualmente a 10.000 elementos");	
				numelementosMaximosATratar = numelementosMaximosATratarPorDefecto;
			}
			
		} catch (Exception e) {
			log.error("Error al recuperar del request numMaxElemTratar, se setea manualmente a 10.000 elementos", e);	
			numelementosMaximosATratar = numelementosMaximosATratarPorDefecto;
		}
		
		int porcentaje = 0;
		
		
		
		log.info("Comenzando proceso de exportación de archivos de los microsites");	
		

		ArchivoDelegate archivoDelegate = DelegateUtil.getArchivoDelegate();
		List<Object[]> listaArchivos = archivoDelegate.obtenerTodosLosArchivosSinBlobs(numelementosMaximosATratar,omitirEnError);
		int totalElementos = listaArchivos.size();
		int elementosTratadosCada1PorCiento = (totalElementos/100)+1;
		
		
		long numeroTotaldeFicheros = archivoDelegate.NumeroTotaldeFicheros();
		Long numtotalPendientes = archivoDelegate.NumeroPendientesExportarAFileSystem();
		Long numtotalYaExportados = archivoDelegate.NumeroExportadosAFileSystem();
		Long numtotalYaExportadosConError = archivoDelegate.NumeroExportadosAFileSystemConError();		
	
		addImportLogVisual(request, "Numero total de archivos: " + numeroTotaldeFicheros);
		addImportLogVisual(request, "Numero total Pendientes a Exportar al iniciar el proceso (estados X ,N y E): " + numtotalPendientes);
		addImportLogVisual(request, "Numero total ficheros ya Exportados a Filesystem al iniciar el proceso (estado S): " + numtotalYaExportados);
		addImportLogVisual(request, "Numero total ficheros con error al exportar a Filesystem al iniciar el proceso (estado E): " + numtotalYaExportadosConError);		
		addImportLogVisual(request, "Comenzando proceso de exportación de archivos de los microsites. Numero elementos a tratar(max: " + numelementosMaximosATratar + "): " + listaArchivos.size() + "; Omitir archivos con error  : " +( omitirEnError?"SI (se omiten los archivos en estado E)":"NO") );
		
		log.info( "Numero total de archivos: " + numeroTotaldeFicheros);
		log.info( "Numero total Pendientes a Exportar al iniciar el proceso (estados X ,N y E): " + numtotalPendientes);
		log.info( "Numero total ficheros ya Exportados a Filesystem al iniciar el proceso (estado S): " + numtotalYaExportados);
		log.info( "Numero total ficheros con error al exportar a Filesystem al iniciar el proceso (estado E): " + numtotalYaExportadosConError);		
		log.info( "Comenzando proceso de exportación de archivos de los microsites. Numero elementos a tratar(" + (numelementosMaximosATratar==0?"Todos":"max:"+numelementosMaximosATratar) + "): " + listaArchivos.size() + "; Omitir archivos con error  : " +( omitirEnError?"SI (se omiten los archivos en estado E)":"NO") );
		
		
		
		
		Iterator<Object[]> it = listaArchivos.iterator();
		
		//lista con los id que se van a marcar como exportados a filsystem
		List<Long> archivosProcesados= new ArrayList();
		List<Long> archivosConError= new ArrayList();
		//numero máximo de elementos que se van a actualizar en BBDD de una sola vez.
		int maxElementosActualizarPorVuelta = 150;
		int numElemtratados=0;
		int numElemtratadosError=0;
		int indice = 0;
		
		

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
					//addImportLogVisual(request, mensaje);
					archivosProcesados.add(idArchivo);
					it.remove();
					indice++;
				}
				
				try {
					if(archivosProcesados.size() >= maxElementosActualizarPorVuelta) {
						numElemtratados += archivoDelegate.MarcarcomoExportadosAFileSystem(archivosProcesados,Archivo.ESTADO_ENFILESYSTEM_SI);
						archivosProcesados.clear();
					}
				} catch (Exception e) {
					log.error("Error al tratar de marcar los ficheros como exportados a filesystem(1)", e);
				}
				
				try {
					if(archivosConError.size() >= maxElementosActualizarPorVuelta) {
						numElemtratadosError += archivoDelegate.MarcarcomoExportadosAFileSystem(archivosConError,Archivo.ESTADO_ENFILESYSTEM_ERROR_EXPORTACION);
						archivosConError.clear();
					}
				} catch (Exception e) {
					log.error("Error al tratar de marcar los ficheros como Error al exportarlos a filesystem(2)", e);
				}
				
				if((indice % elementosTratadosCada1PorCiento) == 0 && (indice >0)) {
					porcentaje=(100*indice)/totalElementos;
					addImportLogVisual(request, "Completado: "+ porcentaje + "%");
					log.error("Porcentaje completado proceso exportación a file system: "+ porcentaje + "%; ("+indice+" tratados de "+totalElementos+" )");
				}
				

			} catch (IOException e) {

				String mensaje = "Error tratando el archivo con id = " + idArchivo;
				log.error(mensaje, e);
				addImportLogVisualStackTrace(request, mensaje, e.getStackTrace());

				archivosConError.add(idArchivo);
				// Eliminamos de la lista el archivo que no se ha podido tratar.
				it.remove();
				indice++;

			}
			
		}
		
		try {
			if(archivosProcesados.size() > 0) {
				numElemtratados += archivoDelegate.MarcarcomoExportadosAFileSystem(archivosProcesados,Archivo.ESTADO_ENFILESYSTEM_SI);
				archivosProcesados.clear();
			}
		} catch (Exception e) {
			log.error("Error al tratar de marcar los ficheros como exportados a filesystem(3)", e);
		}
		
		try {
			if(archivosConError.size() > 0) {
				numElemtratadosError += archivoDelegate.MarcarcomoExportadosAFileSystem(archivosConError,Archivo.ESTADO_ENFILESYSTEM_ERROR_EXPORTACION);
				archivosConError.clear();
			}			
		} catch (Exception e) {
			log.error("Error al tratar de marcar los ficheros como Error al exportarlos a filesystem (4)", e);
		}
		
		
		
		log.info("Se han encontrado " + numElemtratados + " elementos que ya estaban en file system y se han marcado en BBDD como ya exportados. Se han marcado con error "+ numElemtratadosError +" elementos. Quedan por escribir en disco: " + listaArchivos.size() + " archivos, de los " + numelementosMaximosATratar + " elementos a tratar. Iniciamos el proceso de volcado a Filesystem");
		
		addImportLogVisual(request, "Se han encontrado " + numElemtratados + " elementos que ya estaban en file system y se han marcado en BBDD como ya exportados. Se han marcado con error "+ numElemtratadosError +" elementos. Quedan por escribir en disco: " + listaArchivos.size() + " archivos, de los " + numelementosMaximosATratar + " elementos a tratar");
		addImportLogVisual(request, "Iniciamos el proceso de volcado a Filesystem");
		
		// Exportamos los que quedan en la lista.
		List<String> exportados = new ArrayList<String>();

		for (Object[] a : listaArchivos) {

			Long idArchivo = (Long) a[0];

			// Obtenemos archivo con BLOB incluido.
			Archivo archivo = archivoDelegate.obtenerArchivo(idArchivo);
			
			try {
				//Importante, es el único método que se salta la comprobación de si extraer datos por
				// filesystem o base datos, sólo porque estamos haciendo una exportación de ficheros!!
				// La lógica es que tiene que coger los datos de bbdd (forzado) y pasarlos al filesystem,
				//   pero si en la propiedad almacenarEnFilesystem está a True (es la única manera de llegar aquí)
				//   cogería erróneamente el dato de filesystem.
				archivo.setDatos(archivoDelegate.obtenerContenidoFichero(archivo, false));
				exportaArchivoAFilesystem(archivo);
				String ruta = obtenerRutaArchivoExportadoEnFilesystem(archivo);
				log.info("Copiando fichero a disco :" + ruta +";"+(archivo.getDatos()==null?" Archivo sin datos (DCM_DATOS = NULL)":""));
				exportados.add(ruta);
				archivosProcesados.add(idArchivo);
			} catch (IOException e) {
				String mensaje = "Error exportando el archivo con id = " + idArchivo;
				log.error(mensaje, e);
				addImportLogVisualStackTrace(request, mensaje, e.getStackTrace());
				archivosConError.add(idArchivo);
			}
			
			
			try {
				if(archivosProcesados.size() >= maxElementosActualizarPorVuelta) {
					numElemtratados += archivoDelegate.MarcarcomoExportadosAFileSystem(archivosProcesados,Archivo.ESTADO_ENFILESYSTEM_SI);
					archivosProcesados.clear();
				}
			} catch (Exception e) {
				log.error("Error al tratar de marcar los ficheros como exportados a filesystem(5)", e);
			}
			
			try {
				if(archivosConError.size() >= maxElementosActualizarPorVuelta) {
					archivoDelegate.MarcarcomoExportadosAFileSystem(archivosConError,Archivo.ESTADO_ENFILESYSTEM_ERROR_EXPORTACION);
					archivosConError.clear();
				}
			} catch (Exception e) {
				log.error("Error al tratar de marcar los ficheros como Error al exportarlos a filesystem(6)", e);
			}
			
			if((indice % elementosTratadosCada1PorCiento) == 0) {
				porcentaje=(100*indice)/totalElementos;
				addImportLogVisual(request, "Completado: "+ porcentaje + "%");
				log.error("Porcentaje completado proceso exportación a file system: "+ porcentaje + "%; ("+indice+" tratados de "+totalElementos+" )");
			}
			indice++;
		}
		
		
		try {
			if(archivosProcesados.size() > 0) {
				numElemtratados += archivoDelegate.MarcarcomoExportadosAFileSystem(archivosProcesados,Archivo.ESTADO_ENFILESYSTEM_SI);
				archivosProcesados.clear();
			}
		} catch (Exception e) {
			log.error("Error al tratar de marcar los ficheros como exportados a filesystem(7)", e);
		}
		
		try {
			if(archivosConError.size() > 0) {
				archivoDelegate.MarcarcomoExportadosAFileSystem(archivosConError,Archivo.ESTADO_ENFILESYSTEM_ERROR_EXPORTACION);
				archivosConError.clear();
			}			
		} catch (Exception e) {
			log.error("Error al tratar de marcar los ficheros como Error al exportarlos a filesystem (8)", e);
		}

		
		if(porcentaje < 100) {
			addImportLogVisual(request, "Completado: 100 %");
			log.error("Porcentaje completado proceso exportación a file system: 100%");
		}
		
		log.info("Finalizado el proceso de exportación de archivos a disco. Se han copiado en file System " + exportados.size() + " archivos");
		addImportLogVisual(request, "Finalizado el proceso de exportación de archivos a disco. Se han copiado en file System " + exportados.size() + " archivos");
		
		Long numtotalPendientes2 = archivoDelegate.NumeroPendientesExportarAFileSystem();
		Long numtotalYaExportados2 = archivoDelegate.NumeroExportadosAFileSystem();
		Long numtotalYaExportadosConError2 = archivoDelegate.NumeroExportadosAFileSystemConError();
		
		log.info( "Numero total de archivos: " + numeroTotaldeFicheros);
		log.info( "Numero total Pendientes a Exportar al finalizar el proceso (estados X ,N y E): " + numtotalPendientes2);
		log.info( "Numero total ficheros ya Exportados a Filesystem al finalizar el proceso (estado S): " + numtotalYaExportados2);
		log.info( "Numero total ficheros con error al exportar a Filesystem al finalizar el proceso (estado E): " + numtotalYaExportadosConError2);
		
		addImportLogVisual(request, "Numero total de archivos: " + numeroTotaldeFicheros);
		addImportLogVisual(request, "Numero total Pendientes a Exportar al finalizar el proceso (estados X ,N y E): " + numtotalPendientes2);
		addImportLogVisual(request, "Numero total ficheros ya Exportados a Filesystem al finalizar el proceso (estado S): " + numtotalYaExportados2);
		addImportLogVisual(request, "Numero total ficheros con error al exportar a Filesystem al finalizar el proceso (estado E): " + numtotalYaExportadosConError2);
		
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
		
		if (a == null)
			return false;

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
	
	public static Archivo archivoLite2Archivo(ArchivoLite a) {
    	
    	Archivo result = new Archivo();
    	
    	result.setDatos(null);
    	result.setId(a.getId());
    	result.setIdi(a.getIdi());
    	result.setIdmicrosite(a.getIdmicrosite());
    	result.setMime(a.getMime());
    	result.setNombre(a.getNombre());
    	result.setPagina(a.getPagina());
    	result.setPeso(a.getPeso());
    	result.setTraduccionMap(a.getTraduccionMap());
    	result.setExportadoAFileSystem(a.getExportadoAFileSystem());
    	
    	return result;
    	
    }
	
	public static void borrarDirMicrosite(Long idMicrosite) throws IOException {
		
		if (idMicrosite != null && idMicrosite > 0) {
		
			// Directorio que contiene el propio archivo.
			File fDir = new File(obtenerRutaArchivosEnFileSystemMicrosite(idMicrosite));
	
			try {
	
				if (fDir.exists())
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

		if (archivo != null && archivo.getId() != null) {
			
			String rutaArchivosMicrosite = obtenerRutaArchivosEnFileSystemMicrosite(archivo.getIdmicrosite());
	
			// Ruta: [rutaArchivosMicrosites]/idMicrosite/idArchivo/nombreArchivo
			return rutaArchivosMicrosite + File.separator + archivo.getId().toString() + File.separator + archivo.getNombre();
			
		} else {
			
			return "";
			
		}

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

		if (archivo != null && archivo.getId() != null) {
			
			String rutaDirArchivo = obtenerRutaArchivosEnFileSystemMicrosite(archivo.getIdmicrosite()) + File.separator + archivo.getId().toString();
			File dirArchivo = new File(rutaDirArchivo);
	
			if (dirArchivo.exists()) {
				for (String s : dirArchivo.list()) {
					File currentFile = new File(dirArchivo.getPath(), s);
					currentFile.delete();
				}
				dirArchivo.delete();
			}
			
		}

	}

	private static void checkDirArchivo(Archivo archivo) throws IOException {

		if (archivo != null && archivo.getId() != null) {
			
			String rutaDirArchivo = obtenerRutaArchivosEnFileSystemMicrosite(archivo.getIdmicrosite()) + File.separator + archivo.getId().toString();
			File dirArchivo = new File(rutaDirArchivo);
	
			if (!dirArchivo.exists()) {
				dirArchivo.mkdir();
			}
		
		}

	}

	private static void addImportLogVisual(HttpServletRequest request, String mensaje) {
		MicroLog.addLogVisual(request, mensaje);
	}

	private static void addImportLogVisualStackTrace(HttpServletRequest request, String mensaje, StackTraceElement[] mensajes) {
		MicroLog.addLogVisualStackTrace(request, mensaje, mensajes);
	}

}
