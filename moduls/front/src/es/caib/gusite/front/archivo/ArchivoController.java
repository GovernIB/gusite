package es.caib.gusite.front.archivo;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import es.caib.gusite.front.general.BaseViewController;
import es.caib.gusite.front.general.ExceptionFrontMicro;
import es.caib.gusite.front.general.ExceptionFrontPagina;
import es.caib.gusite.front.general.Microfront;
import es.caib.gusite.front.general.bean.ErrorMicrosite;
import es.caib.gusite.front.view.ErrorGenericoView;
import es.caib.gusite.micromodel.Archivo;
import es.caib.gusite.micromodel.ArchivoTemaFront;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.TemaFront;

/**
 * Controlador para servir archivos subidos al microsite, contenidos, etcétera.
 * 
 * @author brujula-at4
 */
@Controller
public class ArchivoController extends BaseViewController {

	private static Log log = LogFactory.getLog(ArchivoController.class);

	/**
	 * Archivo {id} del microsite {uri}
	 * 
	 * @param uri
	 *            Uri de microsite
	 * @param lang
	 *            Idioma de la petición
	 * @param id
	 *            Identificador de archivo
	 * @return bytes y encabezados para la petición del archivo
	 * @throws Exception 
	 */
	@RequestMapping("{uri}/f/{id}")
	@ResponseBody
	public ResponseEntity<byte[]> archivo(@PathVariable("uri") SiteId URI, @PathVariable("id") Long idFile, Model model, HttpServletResponse response, final HttpServletRequest req) throws Exception {

			final Microsite microsite = this.dataService.getMicrositeByUri(URI.uri, DEFAULT_IDIOMA);
			if (microsite == null) {
				log.error("Microsite " + URI.uri + " no encontrado");
				throw new ExceptionFrontPagina(ErrorMicrosite.ERROR_MICRO_URI_MSG + URI.uri);
			}
			
			return this.sirveArchivo(this.dataService.obtenerArchivo(idFile), response);
	}
	
	/**
	 * Archivo de nombre "filename" del microsite {uri}. El nombre se recibe en
	 * el RequestParam {@link Microfront.PNAME}
	 * 
	 * @param uri
	 *            Uri de microsite
	 * @param lang
	 *            Idioma de la petición
	 * @param filename
	 *            Nombre del archivo
	 * @return bytes y encabezados para la petición del archivo
	 * @throws IOException
	 * @throws ExceptionFrontPagina 
	 * @throws ExceptionFrontMicro 
	 */
	@RequestMapping(value = "{uri}/f/", params = Microfront.PNAME)
	@ResponseBody
	public ResponseEntity<byte[]> archivo(@PathVariable("uri") SiteId URI, @RequestParam(Microfront.PNAME) String filename, HttpServletResponse response) throws IOException, ExceptionFrontPagina, ExceptionFrontMicro {

		Microsite microsite = this.dataService.getMicrositeByUri(URI.uri, DEFAULT_IDIOMA);
		if (microsite == null) {
			log.error("Microsite " + URI.uri + " no encontrado");
			throw new ExceptionFrontPagina(ErrorMicrosite.ERROR_MICRO_URI_MSG + URI.uri);
		}
		return this.sirveArchivo(this.dataService.obtenerArchivo(microsite.getId(), filename), response);	
	}

	
	/**
	 * Archivo de nombre "filename" del tema {uri}. El nombre se recibe en
	 * el RequestParam {@link Microfront.PNAME}
	 * OJO: La url externa es ft/{uriTema}/files/{name}, pero esto no es soportado por spring, así que se modifica en UrlRewrite
	 * @param filename
	 *            Nombre del archivo
	 * @return bytes y encabezados para la petición del archivo
	 * @throws IOException
	 */
	@RequestMapping(value = "ft/{uriTema}/", params = Microfront.PNAME)
	@ResponseBody
	public ResponseEntity<byte[]> archivoTema(@PathVariable("uriTema") String uriTema, @RequestParam(Microfront.PNAME) String filename, HttpServletResponse response) throws IOException {

		try {
			List<ArchivoTemaFront> archivos  = this.dataService.getArchivoTema(uriTema, filename);
			if (archivos == null || archivos.size() < 1) {
				log.error("Archivo " + filename + " no encontrado en tema " + uriTema);
				response.setStatus(404);
				return null;
			}

			Archivo archivo = null;
			for (ArchivoTemaFront a : archivos) {
				if (a.getArchivo().getNombre().equals(filename)) {
					archivo = a.getArchivo();
				}
			}
			return this.sirveArchivo(archivo, response);

		} catch (ExceptionFrontPagina e) {
			log.error(e.getMessage(), e);
			response.setStatus(404);
			return null;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setStatus(500);
			return null;
		}

	}
	
	/**
	 * Css del tema {uri}.
	 * OJO: La url externa es ft/{uriTema}/css/estils-tema.css, pero esto no es soportado por spring, así que se modifica en UrlRewrite
	 * @return bytes y encabezados para la petición del archivo
	 * @throws IOException
	 */
	@RequestMapping(value = "ft/{uriTema}/css")
	@ResponseBody
	public ResponseEntity<byte[]> cssTema(@PathVariable("uriTema") String uriTema, HttpServletResponse response) throws IOException {

		TemaFront tema = null;
		try {
			tema = this.dataService.getTemaFrontByUri(uriTema);
			if (tema == null || tema.getCss() == null) {
				log.error("Css para tema " + uriTema + " no encontrado");
				response.setStatus(404);
				return null;
			}

			return this.sirveArchivo(tema.getCss(), response);

		} catch (ExceptionFrontPagina e) {
			log.error(e.getMessage(), e);
			response.setStatus(404);
			return null;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setStatus(500);
			return null;
		}

	}

	/** 
	 * Obtiene el archivo.
	 * 
	 * @param archivo
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ExceptionFrontPagina
	 */
	private ResponseEntity<byte[]> sirveArchivo(Archivo archivo, HttpServletResponse response) throws IOException, ExceptionFrontPagina {
		if (archivo == null) {
			log.error("Archivo no encontrado");
			response.setStatus(404);
			return null;
		}

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.parseMediaType( this.parseMime(archivo) ));
		responseHeaders.setContentLength(new Long(archivo.getPeso()).intValue());
		responseHeaders.add("content-disposition", "inline; filename=\"" + archivo.getNombre() + "\"");
		return new ResponseEntity<byte[]>(this.dataService.obtenerContenidoArchivo(archivo), responseHeaders, HttpStatus.OK);
	}
	
	/** 
	 * Mime extensions
	 */
	private static final Map<String, String> MIME_EXTENSIONES;  
	 static {
	        Map<String, String> aMap = new HashMap<String, String>();
	        aMap.put("css", "text/css");
	        aMap.put("js", "application/javascript");
			MIME_EXTENSIONES = Collections.unmodifiableMap(aMap);
	    }
	 
	 /** 
	  * Prase mime.
	  *  
	  * @param archivo
	  * @return
	  */
	  
	private String parseMime(Archivo archivo) {
		if (archivo.getNombre() != null) {
			int i = archivo.getNombre().lastIndexOf('.');
			if (i > 0) {
			    String extension = archivo.getNombre().substring(i+1);
			    if (MIME_EXTENSIONES.containsKey(extension)) {
			    	return MIME_EXTENSIONES.get(extension);
			    }
			} 
		}
		return archivo.getMime();
	}

	@Override
	public String setServicio() {
		return Microfront.RARCHIVO;
	}
	
	
	@ExceptionHandler(ExceptionFrontPagina.class)
	public ModelAndView handleExceptionFrontPagina(ExceptionFrontPagina ex, HttpServletResponse response) {

		Microsite microsite = null;
		ErrorGenericoView view = new ErrorGenericoView();
		Idioma lang = new Idioma();
		lang.setLang(LANG_CA);
		view.setLang(lang);
		view.setIdioma(LANG_CA);
		ErrorMicrosite errorMicrosite = new ErrorMicrosite();
		errorMicrosite.setAviso("Aviso:" + ex.getMessage());
		errorMicrosite.setMensaje(ex.getMessage());
		view.setErrParam(errorMicrosite);
		view.setErrEstado(errorMicrosite.getEstado());
		
		//El 404
		response.setStatus(HttpStatus.NOT_FOUND.value());
				
		return this.modelForView(this.templateNameFactory.errorGenerico(microsite ), view);		

	}
	
	
	
}
