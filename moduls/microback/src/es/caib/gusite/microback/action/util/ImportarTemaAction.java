package es.caib.gusite.microback.action.util;

import java.io.IOException;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.caib.gusite.micromodel.*;
import es.caib.gusite.micropersistence.delegate.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.gusite.microback.action.BaseAction;
import es.caib.gusite.microback.actionform.formulario.ImportarTemaForm;
import es.caib.gusite.micropersistence.util.log.MicroLog;

/**
 * Action que importa los archivos de un tema<P>
 * Definición Struts:<BR>
 * action path="importarTema"<BR>
 * name="importarTemaForm"<BR>
 * scope="request" <BR>
 * unknown="false" <BR>
 * forward name="detalle" path="/importarTema.jsp"
 */
public class ImportarTemaAction extends BaseAction {

	private static Log log = LogFactory.getLog(ImportarTemaAction.class);
	private static String[] roles = new String[] { "gussystem", "gusadmin" };
	
	// Constantes para la descompresión del archivo ZIP.
	private final String NOMBRE_BASE_DIR_ZIP = "_importacion_tema";
    private static final String CONTEXT = "es.caib.gusite.context.front";
	
	private String mensaje = "";
	private String _hashcode;
	
	public ActionForward doExecute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

	    _hashcode = "" + this.hashCode() + new SimpleDateFormat("hhmmss").format(new java.util.Date()); //un poco chapuza, pero sirve perfectamente
        recogerUsuario(request);

		// Solo podrán importar los roles gussystem y gusadmin
        Hashtable<String, String> rolenames = recogerRoles(request);
		if (!(rolenames.contains(roles[0]) || rolenames.contains(roles[1]))) {
			addMessage(request, "peticion.error");
			return mapping.findForward("info");
		}

		request.getSession().removeAttribute("MVS_importprocessor");
		request.getSession().removeAttribute("MVS_avisoindexadorimportprocessor");

		ImportarTemaForm f = (ImportarTemaForm) form;
		ResourceBundle rb = ResourceBundle.getBundle("sac-microback-messages");
		
		try {
        	request.setAttribute("idtema", request.getParameter("idtema"));
            if (f != null && f.getIdtema()!= null && f.getArchi() != null) {
            	
        		TemaFrontDelegate tfDelegate = DelegateUtil.getTemaFrontDelegate();
        		TemaFront tema = tfDelegate.obtenerTemaFront(f.getIdtema());

                addImportLog("Inici importació, USUARI: [" + request.getSession().getAttribute("username") + "]");
                addImportLog("Importar arxius tema, TEMA: [" + tema.getUri() + "]");
                addImportLogVisual(request, (String) rb.getObject("logimport.upload"));
                addImportLogVisual(request, (String) rb.getObject("logimport.tamano") + ": " + f.getArchi().getFileSize() + " bytes");
                addImportLogVisual(request, (String) rb.getObject("logimport.integridad.ini"));

                String tmpDir = System.getProperty("java.io.tmpdir") + File.separator;
                String prefijoFechaHora = obtenerFechaHora();
                String dirDescompresionZIP = tmpDir + prefijoFechaHora + NOMBRE_BASE_DIR_ZIP;

                File fileDirDescompresionZIP = new File(dirDescompresionZIP);
                fileDirDescompresionZIP.mkdir();
                ZipInputStream zis = new ZipInputStream(f.getArchi().getInputStream());

                addImportLogVisual(request, (String) rb.getObject("frontTemas.import.zip"));
                Map<String, String> listaArchivos = zipReaderArchivos(dirDescompresionZIP, zis);
                tema = importarArchivosTema(tema, listaArchivos, request, rb);
                addImportLogVisual(request, (String) rb.getObject("logimport.integridad.fin"));

                // Actualizo la fecha de creación
                mensaje = "";
                addImportLog("Fi importació, USUARI: [" + request.getSession().getAttribute("username") + "], nou TEMA: [" + tema.getUri() + "]");
                request.setAttribute("mensaje", mensaje);
                
            }

		} catch (Exception e) {
			log.error((String) rb.getObject("frontTemas.import.error"), e);
			addMessage(request, "frontTemas.import.error");
			addImportLogVisualStackTrace(request, (String) rb.getObject("frontTemas.import.error"), e.getStackTrace());
			return mapping.findForward("info");
		}
		
		return mapping.findForward("detalle");
	}

    private void recogerUsuario(HttpServletRequest request) throws DelegateException {

        if (request.getSession().getAttribute("MVS_usuario") == null) {
            UsuarioDelegate usudel = DelegateUtil.getUsuarioDelegate();
            Usuario usu = usudel.obtenerUsuariobyUsername(request.getRemoteUser());
            request.getSession().setAttribute("MVS_usuario", usu);
        }
    }

    private Hashtable<String, String> recogerRoles(HttpServletRequest request) {

        Hashtable<String, String> rolenames = new Hashtable<String, String>();
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

        } else {
            rolenames = (Hashtable<String, String>) request.getSession().getAttribute("rolenames");
        }

        return rolenames;
    }



    private byte[] leerDatosPath (String path) throws IOException {

    	File newFile = new File(path);
    	return Files.readAllBytes(newFile.toPath());
    	
    }

    private Map<String, String> zipReaderArchivos(String dirDescompresionZIP, ZipInputStream zis) throws IOException {

        Map<String, String> listaArchivos = new HashMap<String, String>();
        try {
            byte[] buffer = new byte[1024];

            ZipEntry ze = zis.getNextEntry();

            while (ze != null) {
                String fileName = ze.getName();
                String path = dirDescompresionZIP + File.separator + fileName;
                
                File newFile = new File(dirDescompresionZIP + File.separator + fileName);
                log.debug("file unzip : " + newFile.getAbsoluteFile());

                // create all non exists folders else you will hit FileNotFoundException for compressed folder
                new File(newFile.getParent()).mkdirs();

                if (ze.isDirectory()) {
                    newFile.mkdir();

                } else {
                    FileOutputStream fos = new FileOutputStream(newFile);
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                    fos.close();
                    listaArchivos.put(fileName, path);

                }

                ze = zis.getNextEntry();
            }

            zis.closeEntry();
            zis.close();
            return listaArchivos;

        } catch (SecurityException e) {
            log.error(e);
            throw e;
        } catch (IOException e) {
            log.error(e);
            throw e;
        }
    }


    private TemaFront importarArchivosTema(TemaFront tema, Map<String, String> listaArchivos, HttpServletRequest request, ResourceBundle rb) throws DelegateException, IOException {

        for (Entry<String, String> nombrePath : listaArchivos.entrySet()) {
            addImportLogVisual(request, nombrePath.getKey());
        	if (nombrePath.getKey().startsWith(ExportarTemaAction.NOMBRE_DIR_ARCHIVOS)) {
        		//Es un archivo
        		subirArchivo(tema, nombrePath.getKey(), nombrePath.getValue(), request, rb);
        		log.debug("Importando archivo:" + nombrePath.getKey());
        	} else if (nombrePath.getKey().startsWith(ExportarTemaAction.NOMBRE_DIR_CSS)) {
        		//Es el css del tema
        		subirCss(tema, nombrePath.getKey(), nombrePath.getValue(), request, rb);
        		log.debug("Importando css:" + nombrePath.getKey());
        	} else if (nombrePath.getKey().endsWith(".html")) {
        		//Es una plantilla thymeleaf
        		subirPlantilla(tema, nombrePath.getKey(), nombrePath.getValue(), request, rb);
        		log.debug("Importando plantila:" + nombrePath.getKey());
        	} else {
                addImportLogVisual(request, rb.getString("frontTemas.import.arxiuNoValid"));
        	}
        }

        return tema;
    }

	
	private void subirCss(TemaFront temaFront, String archivo, String fullpath, HttpServletRequest request, ResourceBundle rb) throws IOException, DelegateException {
        Archivo css = temaFront.getCss();
        long oldCssId = 0;
        if (css != null) {
            addImportLogVisual(request, rb.getString("frontTemas.import.arxiuDuplicat"));
        	oldCssId = css.getId();
        }
        css = populateArchivo(ExportarTemaAction.NOMBRE_CSS, fullpath);
        DelegateUtil.getArchivoDelegate().insertarArchivo(css);
        temaFront.setCss(css);
        DelegateUtil.getTemaFrontDelegate().actualizarTemaFront(temaFront);
        if (oldCssId > 0) {
        	DelegateUtil.getArchivoDelegate().borrarArchivo(oldCssId);
        }
		
	}

	
	private void subirPlantilla(TemaFront tema, String archivo, String fullpath, HttpServletRequest request, ResourceBundle rb) throws DelegateException, IOException {

        PersonalizacionPlantillaDelegate personalizacionPlantillaDelegate = DelegateUtil.getPersonalizacionPlantillaDelegate();
        String nombrePlantilla = archivo.substring(0, archivo.indexOf(".html"));
        
        PersonalizacionPlantilla perPlantilla = this.findPersonalizacionPlantilla(nombrePlantilla, tema);
        if (perPlantilla == null) {
            perPlantilla = new PersonalizacionPlantilla();
            Plantilla plantilla = getPlantilla(nombrePlantilla);
            if (plantilla == null) {
                addImportLogVisual(request, rb.getString("frontTemas.import.plantillaNoValida"));
            	return;
            }
			perPlantilla.setPlantilla(plantilla);
            perPlantilla.setTitulo(nombrePlantilla);
            perPlantilla.setOrden((long)1);
            perPlantilla.setTema(tema);
            perPlantilla.setContenido(new String(this.leerDatosPath(fullpath)));
            personalizacionPlantillaDelegate.crearPersonalizacionPlantilla(perPlantilla);
            tema.getPersonalizacionesPlantilla().add(perPlantilla);
        } else {
            addImportLogVisual(request, rb.getString("frontTemas.import.plantillaDuplicada"));
            perPlantilla.setContenido(new String(this.leerDatosPath(fullpath)));
            personalizacionPlantillaDelegate.actualizarPersonalizacionPlantilla(perPlantilla);
        }
    }
	
	private Plantilla getPlantilla(String nombrePlantilla) throws DelegateException {
        return DelegateUtil.getPlantillaDelegate().obtenerPlantillaPorNombre(nombrePlantilla);
	}

	private void subirArchivo(TemaFront tema, String archivo, String fullpath, HttpServletRequest request, ResourceBundle rb) throws DelegateException, IOException {

        ArchivoTemaFrontDelegate archivoTemaFrontDelegate = DelegateUtil.getArchivoTemaFrontDelegate();
        String nombreArchivo = archivo.substring(ExportarTemaAction.NOMBRE_DIR_ARCHIVOS.length());
        
        ArchivoTemaFront archivoTema = this.findArchivo(nombreArchivo, tema);
        if (archivoTema != null) {
        	//Borramos el archivo anterior
            addImportLogVisual(request, rb.getString("frontTemas.import.arxiuDuplicat"));
            archivoTemaFrontDelegate.borrarArchivoTemaFront(archivoTema);
        }
        archivoTema = new ArchivoTemaFront();
        archivoTema.setTema(tema);
        archivoTema.setArchivo(populateArchivo(nombreArchivo, fullpath));
        archivoTema.setPath(generarPath(tema.getUri(), archivoTema.getArchivo().getNombre()));
        archivoTemaFrontDelegate.crearArchivoTemaFront(archivoTema);

    }
	
	/**
	 * 
	 * Método que genera un Archivo a partir del path
	 * 
	 */
	protected Archivo populateArchivo(String filename, String fullpath) throws IOException {

        Archivo archivo = new Archivo();
        archivo.setMime(Files.probeContentType(new File(fullpath).toPath()));
        archivo.setNombre(filename);
        archivo.setDatos(this.leerDatosPath(fullpath));
        archivo.setPeso(archivo.getDatos().length);
        return archivo;
    }
	

    private String generarPath(String temaUri, String nom) {

        return System.getProperty(CONTEXT).concat("/ft/").concat(temaUri).concat("/files/").concat(nom);
    }
	
	
	private ArchivoTemaFront findArchivo(String nombreArchivo, TemaFront tema) {
		for (ArchivoTemaFront a : tema.getArchivoTemaFronts()) {
			if (a.getArchivo().getNombre().equals(nombreArchivo)) {
				return a;
			}
		}
		return null;
	}

	
	private PersonalizacionPlantilla findPersonalizacionPlantilla(String nombre, TemaFront tema) {
		for (PersonalizacionPlantilla a : tema.getPersonalizacionesPlantilla()) {
			if (a.getPlantilla().getNombre().equals(nombre)) {
				return a;
			}
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


	private void addImportLog(String mensaje) {
		MicroLog.addLog("{i" + _hashcode + "} " + mensaje);
	}

	@SuppressWarnings("unused")
	private void addImportLogStackTrace(String mensaje, StackTraceElement[] mensajes) {
		MicroLog.addLogStackTrace("{i" + _hashcode + "} " + mensaje, mensajes);
	}

	private void addImportLogVisual(HttpServletRequest request, String mensaje) {
		MicroLog.addLogVisual(request, "{i" + _hashcode + "} " + mensaje);
	}

	private void addImportLogVisualStackTrace(HttpServletRequest request, String mensaje, StackTraceElement[] mensajes) {
		MicroLog.addLogVisualStackTrace(request, "{i" + _hashcode + "} " + mensaje, mensajes);
	}

		 
		 


}
