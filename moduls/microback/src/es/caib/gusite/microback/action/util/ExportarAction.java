package es.caib.gusite.microback.action.util;

import java.io.*;
import java.util.*;
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
 * Action que exporta un microsite <P>
 * 	Definición Struts:<BR>
 *  action path="exportar"<BR> 
 *	scope="request" <BR>
 *  unknown="false"
 *  
 *  @author - Indra
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
	            String nombreZIP = prefijoFechaHora + NOMBRE_BASE_ZIP;
	    		String rutaZIP = tmpDir + nombreZIP;
	    		
	    		// Creamos el archivo ZIP.
			    ZipOutputStream out = new ZipOutputStream(new FileOutputStream(rutaZIP));
			    
			    // Agregamos XML al ZIP.
			    agregaXmlAZIP(rutaXML, nombreXML, out);
			    
			    // Agregamos, si los hubiese, los archivos asociados al XML dentro de un directorio aparte dentro del ZIP.
			    Set<Archivo> documentos = extraerArchivosMicrosite(micro);
		    	
		    	// Creamos la entrada en el ZIP para el directorio que contendrá los archivos.
		    	// El API de archivos ZIP de Java detecta que es un directorio al acabar en "/".
		    	out.putNextEntry(new ZipEntry(NOMBRE_DIR_ARCHIVOS));
		    			    	
		    	for (Archivo docu : documentos) {
		    		agregaArchivoAZIP(NOMBRE_DIR_ARCHIVOS, docu, out);
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

	private Set<Archivo> extraerArchivosMicrosite(MicrositeCompleto microsite) {

		ArchivoDelegate archivoDelegate = DelegateUtil.getArchivoDelegate();
		Set<Archivo> archivos = new HashSet<Archivo>();
		try {
			for (Iterator iter = microsite.getDocus().iterator(); iter.hasNext();) {
				Archivo archivo = (Archivo) iter.next();
				archivos.add(obtenerArchivo(archivo, archivoDelegate));
			}

			if (microsite.getImagenPrincipal() != null) {
				archivos.add(obtenerArchivo(microsite.getImagenPrincipal(), archivoDelegate));
			}

			if (microsite.getImagenCampanya() != null) {
				archivos.add(obtenerArchivo(microsite.getImagenCampanya(), archivoDelegate));
			}

			if (microsite.getEstiloCSS() != null) {
				if (microsite.getEstiloCSS().getDatos() != null) {
					archivos.add(obtenerArchivo(microsite.getEstiloCSS(), archivoDelegate));
				}
			}

			for (Object menu : microsite.getMenus()) {
				if (((Menu) menu).getImagenmenu() != null) {
					archivos.add(obtenerArchivo(((Menu) menu).getImagenmenu(), archivoDelegate));
				}
				for (Contenido contenido : ((Menu) menu).getContenidos()) {
					if (contenido.getImagenmenu() != null) {
						archivos.add(obtenerArchivo(contenido.getImagenmenu(), archivoDelegate));
					}
				}
			}

			for (Object agenda : microsite.getAgendas()) {
				for (TraduccionAgenda trad : ((Agenda) agenda).getTraducciones().values()) {
					if (trad.getDocumento() != null) {
						archivos.add(obtenerArchivo(trad.getDocumento(), archivoDelegate));
					}
					if (trad.getImagen() != null) {
						archivos.add(obtenerArchivo(trad.getImagen(), archivoDelegate));
					}
				}
			}

			for (Object noticia : microsite.getNoticias()) {
				if (((Noticia) noticia).getImagen() != null) {
					archivos.add(obtenerArchivo(((Noticia) noticia).getImagen(), archivoDelegate));
				}
				for (TraduccionNoticia trad : ((Noticia) noticia).getTraducciones().values()) {
					if (trad.getDocu() != null) {
						archivos.add(obtenerArchivo(trad.getDocu(), archivoDelegate));
					}
				}
			}

			for (Object componente : microsite.getComponentes()) {
				if (((Componente) componente).getImagenbul() != null) {
					archivos.add(obtenerArchivo(((Componente) componente).getImagenbul(), archivoDelegate));
				}
			}

			for (Object encuesta : microsite.getEncuestas()) {
				for (Pregunta pregunta : ((Encuesta) encuesta).getPreguntas()) {
					if (pregunta.getImagen() != null) {
						archivos.add(obtenerArchivo(pregunta.getImagen(), archivoDelegate));
					}
				}
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

	private void agregaArchivoAZIP(String nombreDirArchivos, Archivo docu, ZipOutputStream out) throws IOException {
		
		if (docu != null && docu.getDatos() != null) {
			byte[] datosDocumento = docu.getDatos();
			ByteArrayInputStream in = new ByteArrayInputStream(datosDocumento);

			// Ponemos como prefijo el ID del documento en la BD, por si hay algún documento
			// relacionado con el microsite que coincida en nombre con otro.
	        out.putNextEntry(new ZipEntry(nombreDirArchivos + docu.getId() + "_" + docu.getNombre()));

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
