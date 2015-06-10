package es.caib.gusite.microback.action.util;

import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
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
 * Action que exporta un microsite <P>
 * 	Definición Struts:<BR>
 *  action path="exportar"<BR> 
 *	scope="request" <BR>
 *  unknown="false"
 *  
 */
public class ExportarAction extends BaseAction {

	private static Log log = LogFactory.getLog(ExportarAction.class);
	private static String[] roles = new String[]{"gussystem", "gusadmin"};
	
	// Constantes para la generación del archivo ZIP.
	private final String NOMBRE_BASE_ZIP = "_microsite.zip";
	private final String NOMBRE_DIR_ARCHIVOS = "archivos/";
	
	/**
     * This is the main action called from the Struts framework.
     * @param mapping The ActionMapping used to select this instance.
     * @param form The optional ActionForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     * @throws java.lang.Exception
     * @return org.apache.struts.action.ActionForward
     */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ActionForward doExecute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		MicrositeDelegate micrositeDelegate = DelegateUtil.getMicrositeDelegate();
		MicrositeCompleto micro = null;
		Hashtable<String, String> rolenames = null;
		
    	// recoger usuario.....
		if (request.getSession().getAttribute("MVS_usuario") == null) {
			UsuarioDelegate usudel = DelegateUtil.getUsuarioDelegate();
			Usuario usu = usudel.obtenerUsuariobyUsername(request.getRemoteUser());
			request.getSession().setAttribute("MVS_usuario", usu);
		}
    	
		if (request.getSession().getAttribute("rolenames") == null) {
			if (request.getRemoteUser() != null) {
				request.getSession().setAttribute("username", request.getRemoteUser());
				rolenames = new Hashtable<String, String>();
				
				for (int i = 0; i < roles.length; i++) {
                    if (request.isUserInRole(roles[i])) {
                        rolenames.put(roles[i], roles[i]);
                    }
                }
				
				request.getSession().setAttribute("rolenames", rolenames);
			}
		}
    	
		// Solo podrán exportar los roles gussystem y gusadmin
		rolenames = (Hashtable) request.getSession().getAttribute("rolenames");

		if (!(rolenames.contains(roles[0]) || rolenames.contains(roles[1]))) {
			addMessage(request, "peticion.error");
            return mapping.findForward("info");
		}
		
		if (request.getParameter("idsite") != null) {
    		
			MicroLog.addLog("Inici Exportació Microsite: [" + request.getParameter("idsite") + "] , Usuari: [" + request.getSession().getAttribute("username") + "]");
			
    		Long idmicrosite = new Long(request.getParameter("idsite"));
    		micro = micrositeDelegate.obtenerMicrositeCompleto(idmicrosite);
    		micro.setVersion(Microback.microsites_version);
    		
    		// TODO amartin 2014/07/01: ¿por qué no se exportan los servicios ofrecidos?
    		micro.setServiciosSeleccionados("");
    		    		
    		try {
    			
	    		// Generar archivo XML.
	    		String tmpDir = System.getProperty("java.io.tmpdir") + File.separator;
	    		String nombreXML = "microsite-" + ((TraduccionMicrosite) micro.getTraduccion()).getTitulo() + ".xml";
	    		String rutaXML = tmpDir + nombreXML;

                FileWriter file = new FileWriter(rutaXML);
                JAXBContext jaxbContext = JAXBContext.newInstance(MicrositeCompleto.class);
                Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

                // output pretty printed
                jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "ASCII");
                jaxbMarshaller.marshal(micro, file);
	    		
	    		// Generar ZIP con archivo XML y archivos adjuntos.
	            String prefijoFechaHora = obtenerFechaHora(); 
	            String nombreZIP = micro.getUri() + "_" + prefijoFechaHora + NOMBRE_BASE_ZIP;
	    		String rutaZIP = tmpDir + nombreZIP;
	    		
	    		// Creamos el archivo ZIP.
			    ZipOutputStream zipFile = new ZipOutputStream(new FileOutputStream(rutaZIP));
			    
			    // Agregamos XML al ZIP.
			    agregaXmlAZIP(rutaXML, nombreXML, zipFile);
			    
			    // Agregamos, si los hubiese, los archivos asociados al XML dentro de un directorio aparte dentro del ZIP.
			    incluirArchivosMicrosite(micro, zipFile);
		    	
			    // Ofrecemos ZIP al usuario.
		    	zipFile.close();
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
	
	private void agregaXmlAZIP(String rutaXML, String nombreXML, ZipOutputStream out) throws IOException {
		
	    File f = new File(rutaXML);
	    
    	if (f.exists()) {
    		
	    	FileInputStream in = new FileInputStream(f);
	        out.putNextEntry(new ZipEntry(nombreXML));

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

	@SuppressWarnings("unchecked")
	private void incluirArchivosMicrosite(MicrositeCompleto microsite, ZipOutputStream zipFile) throws IOException, DelegateException {

    	// Creamos la entrada en el ZIP para el directorio que contendrá los archivos.
    	// El API de archivos ZIP de Java detecta que es un directorio al acabar en "/".
	    zipFile.putNextEntry(new ZipEntry(NOMBRE_DIR_ARCHIVOS));
		ArchivoDelegate archivoDelegate = DelegateUtil.getArchivoDelegate();
		
		Iterator<Archivo> iter = (Iterator<Archivo>)microsite.getDocus().iterator();
		while (iter.hasNext()) {
			Archivo archivo = iter.next();
    		agregaArchivoAZIP(NOMBRE_DIR_ARCHIVOS, obtenerArchivo(archivo, archivoDelegate), zipFile);
		}

		if (microsite.getImagenPrincipal() != null) {
    		agregaArchivoAZIP(NOMBRE_DIR_ARCHIVOS, obtenerArchivo(microsite.getImagenPrincipal(), archivoDelegate), zipFile);
		}

		if (microsite.getImagenCampanya() != null) {
    		agregaArchivoAZIP(NOMBRE_DIR_ARCHIVOS, obtenerArchivo(microsite.getImagenCampanya(), archivoDelegate), zipFile);
		}

		if (microsite.getEstiloCSS() != null && microsite.getEstiloCSS().getDatos() != null) {
    		agregaArchivoAZIP(NOMBRE_DIR_ARCHIVOS, obtenerArchivo(microsite.getEstiloCSS(), archivoDelegate), zipFile);
		}

		for (Object menu : microsite.getMenus()) {
			if (((Menu) menu).getImagenmenu() != null) {
	    		agregaArchivoAZIP(NOMBRE_DIR_ARCHIVOS, obtenerArchivo(((Menu) menu).getImagenmenu(), archivoDelegate), zipFile);
			}
			for (Contenido contenido : ((Menu) menu).getContenidos()) {
				if (contenido.getImagenmenu() != null) {
		    		agregaArchivoAZIP(NOMBRE_DIR_ARCHIVOS, obtenerArchivo(contenido.getImagenmenu(), archivoDelegate), zipFile);
				}
			}
		}

		for (Object agenda : microsite.getAgendas()) {
			for (TraduccionAgenda trad : ((Agenda) agenda).getTraducciones().values()) {
				if (trad.getDocumento() != null) {
		    		agregaArchivoAZIP(NOMBRE_DIR_ARCHIVOS, obtenerArchivo(trad.getDocumento(), archivoDelegate), zipFile);
				}
				if (trad.getImagen() != null) {
		    		agregaArchivoAZIP(NOMBRE_DIR_ARCHIVOS, obtenerArchivo(trad.getImagen(), archivoDelegate), zipFile);
				}
			}
		}

		for (Object noticia : microsite.getNoticias()) {
			if (((Noticia) noticia).getImagen() != null) {
	    		agregaArchivoAZIP(NOMBRE_DIR_ARCHIVOS, obtenerArchivo(((Noticia) noticia).getImagen(), archivoDelegate), zipFile);
			}
			for (TraduccionNoticia trad : ((Noticia) noticia).getTraducciones().values()) {
				if (trad.getDocu() != null) {
		    		agregaArchivoAZIP(NOMBRE_DIR_ARCHIVOS, obtenerArchivo(trad.getDocu(), archivoDelegate), zipFile);
				}
			}
		}

		for (Object componente : microsite.getComponentes()) {
			if (((Componente) componente).getImagenbul() != null) {
	    		agregaArchivoAZIP(NOMBRE_DIR_ARCHIVOS, obtenerArchivo(((Componente) componente).getImagenbul(), archivoDelegate), zipFile);
			}
		}

		for (Object encuesta : microsite.getEncuestas()) {
			for (Pregunta pregunta : ((Encuesta) encuesta).getPreguntas()) {
				if (pregunta.getImagen() != null) {
		    		agregaArchivoAZIP(NOMBRE_DIR_ARCHIVOS, obtenerArchivo(pregunta.getImagen(), archivoDelegate), zipFile);
				}
			}
		}

	}

	private Archivo obtenerArchivo(Archivo archivo, ArchivoDelegate delegate) throws DelegateException {

		if (archivo.getDatos() != null) {
			return archivo;
		} else {
			return delegate.obtenerArchivo(archivo.getId());
		}
		
	}

	private void agregaArchivoAZIP(String nombreDirArchivos, Archivo docu, ZipOutputStream out) throws IOException {
		
		if (docu != null && docu.getDatos() != null) {
			
			byte[] datosDocumento = docu.getDatos();
			ByteArrayInputStream in = new ByteArrayInputStream(datosDocumento);

			// Ponemos como prefijo el ID del documento en la BD, por si hay algún documento
			// relacionado con el microsite que coincida en nombre con otro.
			try {
	        
				out.putNextEntry(new ZipEntry(nombreDirArchivos + docu.getId() + "_" + docu.getNombre()));
				
				byte[] buf = new byte[1024];
		        int len;
		        
		        while ((len = in.read(buf)) > 0) {
		            out.write(buf, 0, len);
		        }

		        // Complete the entry
		        out.closeEntry();
				
			} catch (ZipException e) {
				
				// Si el error es por entrada duplicada, lo ignoramos ya que, por ejemplo, los documentos de noticias
				// quedan como archivos comunes del Microsite y se leen dos veces: procesando los archivos comunes y las noticias.
				// En cualquier otro caso, lanzamos la excepción.
				if (!e.getMessage().contains("duplicate entry")) {
					throw new IOException(e);
				}
				
			} finally {

				in.close();
				
			}
	        
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
	    
	    while ((bytesRead = in.read(buffer)) != -1) 
	    	out.write(buffer, 0, bytesRead);
	    
	    in.close(); 
	    in = null; 
  
	    out.close();
	    
	}

}
