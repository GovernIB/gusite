package es.caib.gusite.microback.action.util;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import es.caib.gusite.micromodel.*;
import es.caib.gusite.micropersistence.delegate.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.gusite.microback.Microback;
import es.caib.gusite.microback.action.BaseAction;
import es.caib.gusite.micropersistence.util.log.MicroLog;

/**
 * Action que exporta un TemaFront <P>
 * 	Definición Struts:<BR>
 *  action path="exportarTema"<BR> 
 *	scope="request" <BR>
 *  unknown="false"
 *  
 *  @author - at4.net
 */
public class ExportarTemaAction extends BaseAction {

	private static Log log = LogFactory.getLog(ExportarTemaAction.class);
	
	// Constantes para la generación del archivo ZIP.
	private final static String NOMBRE_BASE_ZIP = "_tema.zip";
	protected final static String NOMBRE_DIR_ARCHIVOS = "files/";
	protected final static String NOMBRE_DIR_CSS = "css/";
	protected final static String NOMBRE_CSS = "estils-tema.css";
	
	/**
	 * Exporta el tema a un zip
     */
	public ActionForward doExecute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		TemaFrontDelegate tfDelegate = DelegateUtil.getTemaFrontDelegate();
		TemaFront tema = null;
		
		if (request.getParameter("idtema") != null) {
    		
			MicroLog.addLog("Inici Exportació Tema: [" + request.getParameter("idtema") + "] , Usuari: [" + request.getSession().getAttribute("username") + "]");
			
    		Long idtema = new Long(request.getParameter("idtema"));
    		tema = tfDelegate.obtenerTemaFront(idtema);
    		
    		try {
	    		String tmpDir = System.getProperty("java.io.tmpdir") + File.separator;

	    		// Generar ZIP con archivos adjuntos.
	            String prefijoFechaHora = obtenerFechaHora(); 
	            String nombreZIP = tema.getUri() + "_" + prefijoFechaHora + NOMBRE_BASE_ZIP;
	    		String rutaZIP = tmpDir + nombreZIP;
	    		
	    		// Creamos el archivo ZIP.
			    ZipOutputStream out = new ZipOutputStream(new FileOutputStream(rutaZIP));
			    
		    	// Creamos la entrada en el ZIP para los directorios que contendrán los archivos.
		    	out.putNextEntry(new ZipEntry(NOMBRE_DIR_ARCHIVOS));
		    	out.putNextEntry(new ZipEntry(NOMBRE_DIR_CSS));
		    			    	
			    // Agregamos, si los hubiese, los archivos del tema
			    Map<String, Archivo> documentos = extraerArchivos(tema);
		    	for (Entry<String, Archivo> docu : documentos.entrySet()) {
		    		agregaArchivoAZIP(docu.getKey(), docu.getValue().getDatos(), out);
		    	}
	            
		    	for (PersonalizacionPlantilla plantilla : tema.getPersonalizacionesPlantilla()) {
		    		agregaArchivoAZIP(plantilla.getPlantilla().getNombre() + ".html", plantilla.getContenido().getBytes(), out);
		    	}
		    	
			    // Ofrecemos ZIP al usuario.
		    	out.close();
			    ofreceZIP(response, tmpDir, nombreZIP);
			    
    		} catch (SecurityException e) {
    			// Capturamos para posible tratamiento posterior.
    			log.error(e);
    			throw e;
    		} catch (IOException e) {
    			// Capturamos para posible tratamiento posterior.
    			log.error(e);
    			throw e;
    		}
		    
            MicroLog.addLog("Fi Exportació Microsite: [" + request.getParameter("idsite") + "] , Usuari: [" + request.getSession().getAttribute("username") + "]");
    	}
		
        return null;
	}
	
	private String obtenerFechaHora() {
		
		Calendar cal = Calendar.getInstance();
		
        // Cero inicial para meses o días menores que 10.
        String mes = ((cal.get(Calendar.MONTH) + 1) > 10) ? String.valueOf((cal.get(Calendar.MONTH) + 1)) : "0" + String.valueOf((cal.get(Calendar.MONTH) + 1));
        String dia = (cal.get(Calendar.DAY_OF_MONTH) > 10) ? String.valueOf(cal.get(Calendar.DAY_OF_MONTH)) : "0" + String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
        String fecha = cal.get(Calendar.YEAR) + mes + dia; // Formato: YYYYMMDD
        String hora = cal.get(Calendar.HOUR_OF_DAY) + "" + cal.get(Calendar.MINUTE) + "" + cal.get(Calendar.SECOND); // Formato: HHmmss
        String prefijoFechaHora = fecha + "_" + hora;
        
        return prefijoFechaHora;
	}
	
	private Map<String, Archivo> extraerArchivos(TemaFront tema) {

		ArchivoDelegate archivoDelegate = DelegateUtil.getArchivoDelegate();
		Map<String, Archivo> archivos = new HashMap<String, Archivo>();
		try {
			for (ArchivoTemaFront at : tema.getArchivoTemaFronts()) {
				Archivo archivo = at.getArchivo();
				archivos.put(NOMBRE_DIR_ARCHIVOS + archivo.getNombre(), obtenerArchivo(archivo, archivoDelegate));
			}

			if (tema.getCss() != null) {
				archivos.put(NOMBRE_DIR_CSS + NOMBRE_CSS, obtenerArchivo(tema.getCss(), archivoDelegate));
			}

		} catch (DelegateException e) {
			e.printStackTrace();
		}

		return archivos;
	}

	private Archivo obtenerArchivo(Archivo archivo, ArchivoDelegate delegate) throws DelegateException {

		if (archivo.getDatos() != null) {
			return archivo;
		} else {
			return delegate.obtenerArchivo(archivo.getId());
		}
	}

	private void agregaArchivoAZIP(String path, byte[] datosDocumento, ZipOutputStream out) throws IOException {
		
		if (datosDocumento != null) {
			ByteArrayInputStream in = new ByteArrayInputStream(datosDocumento);

	        out.putNextEntry(new ZipEntry(path));

	        byte[] buf = new byte[1024];
	        int len;
	        
	        while ((len = in.read(buf)) > 0) {
	            out.write(buf, 0, len);
	        }

	        // Complete the entry
	        out.closeEntry();
	        in.close();
		}
	}
	
	/**
	 * Deuelve al usuario el ZIP con con la ruta tmpDir + nombreZIP.
	 * @param response objeto de respuesta del servlet.
	 * @param tmpDir directorio temporal del usuario que ejecuta el software.
	 * @param nombreZIP cadena de texto con el nombre del ZIP que queremos generar.
	 * @throws IOException 
	 * @throws Exception en caso de algún error, se pasa el error a un nivel superior.
	 */
	private void ofreceZIP(HttpServletResponse response, String tmpDir, String nombreZIP) throws IOException {
		
		final ServletOutputStream out = response.getOutputStream();
		  
	    //Preparamos el tipo de respuesta: 
	    response.setHeader("Expires", "0");
	    response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
	    response.setHeader("Pragma", "public");
	    response.setContentType("application/octet-stream");
	    response.setHeader("Content-Disposition", "attachment;filename=" + nombreZIP); 
	    
	    InputStream in = new FileInputStream(tmpDir + nombreZIP);		    
	    byte[] buffer = new byte[4 * 1024]; // 4K buffer 
	    int bytesRead; 	
	    
	    while ( (bytesRead = in.read(buffer)) != -1) 
	    	out.write(buffer, 0, bytesRead);
	    
	    in.close(); 
	    in = null; 
  
	    out.close();
	}

}
