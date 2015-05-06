package es.caib.gusite.front.archivo;

import java.io.IOException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import es.caib.gusite.front.general.ExceptionFrontMicro;
import es.caib.gusite.front.general.ExceptionFrontPagina;
import es.caib.gusite.front.general.FrontController;
import es.caib.gusite.front.general.Microfront;
import es.caib.gusite.micromodel.Archivo;
import es.caib.gusite.micromodel.ArchivoTemaFront;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.TemaFront;
import es.caib.gusite.micropersistence.util.ArchivoUtil;

/**
 * Controlador para servir archivos subidos al microsite, contenidos, etcétera.
 * 
 * @author brujula-at4
 */
@Controller
public class ArchivoController extends FrontController {

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
	 * @throws IOException
	 */
	@RequestMapping("{uri}/f/{id}")
	@ResponseBody
	public ResponseEntity<byte[]> archivo(@PathVariable("uri") SiteId URI, @PathVariable("id") Long idFile) throws IOException {

		Microsite microsite = null;
		try {
			microsite = this.dataService.getMicrositeByUri(URI.uri, DEFAULT_IDIOMA);
			if (microsite == null) {
				log.error("Microsite " + URI.uri + " no encontrado");
				// TODO: 404?
				// return getForwardError (microsite, lang, model,
				// ErrorMicrosite.ERROR_AMBIT_MICRO);
				return null;
			}

			// TODO: if (this.checkControl(ctrl, idFile)) {
			return this.sirveArchivo(this.dataService.obtenerArchivo(idFile));

		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			// TODO: 404?
			// return getForwardError (microsite, lang, model,
			// ErrorMicrosite.ERROR_AMBIT_MICRO);
			return null;
		} catch (ExceptionFrontPagina e) {
			// TODO: 404?
			// return getForwardError (microsite, lang, model,
			// ErrorMicrosite.ERROR_AMBIT_PAGINA);
			return null;
		}

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
	 */
	@RequestMapping(value = "{uri}/f/", params = Microfront.PNAME)
	@ResponseBody
	public ResponseEntity<byte[]> archivo(@PathVariable("uri") SiteId URI, @RequestParam(Microfront.PNAME) String filename) throws IOException {

		Microsite microsite = null;
		try {
			microsite = this.dataService.getMicrositeByUri(URI.uri, DEFAULT_IDIOMA);
			if (microsite == null) {
				log.error("Microsite " + URI.uri + " no encontrado");
				// TODO: 404?
				// return getForwardError (microsite, lang, model,
				// ErrorMicrosite.ERROR_AMBIT_MICRO);
				return null;
			}

			// TODO: if (this.checkControl(ctrl, idFile)) {
			return this.sirveArchivo(this.dataService.obtenerArchivo(microsite.getId(), filename));

		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			// TODO: 404?
			// return getForwardError (microsite, lang, model,
			// ErrorMicrosite.ERROR_AMBIT_MICRO);
			return null;
		} catch (ExceptionFrontPagina e) {
			// TODO: 404?
			// return getForwardError (microsite, lang, model,
			// ErrorMicrosite.ERROR_AMBIT_PAGINA);
			return null;
		}

	}

	/**
	 * 
	 * Este método comprueba que en el parametro 'ctrl' del request se ha pasado
	 * el siguiente formato: SSSSSxxxZIyyy donde SSSSS es el tipo de servicio
	 * sacado de la clase es.caib.gusite.microfront.Microfront. donde xxx es el
	 * id del elemento al que pertenece el documento donde ZI es el separador
	 * donde yyy es el id del documento En el parametro id se ha pasado también
	 * el id de la imagen.
	 * 
	 * @return boolean
	 */
	private boolean checkControl(String ctrl, Long idelemento) {

		/*
		 * TODO: //comprobar si solicitamos por nombre el archivo. Si es
		 * así----> no hay chequeo //String bynombre = "" +
		 * req.getParameter(Microfront.PNAME); if (!bynombre.equals("null"))
		 * return retorno;
		 */

		Long yyy;
		ctrl.substring(0, 5);
		new Long(ctrl.substring(5, ctrl.indexOf(Microfront.separatordocs)));
		yyy = new Long(ctrl.substring(ctrl.indexOf(Microfront.separatordocs) + 2, ctrl.length()));

		if (!idelemento.equals(yyy)) {
			return false;
		}

		/*
		 * TODO_ if (SSSSS.equals(Microfront.RAGENDA)) { AgendaDelegate
		 * agendadel = DelegateUtil.getAgendaDelegate(); Agenda agenda =
		 * agendadel.obtenerAgenda(xxx); String previ=
		 * ""+req.getSession().getAttribute("previsualiza"); if
		 * (!previ.equals("si")) { if (!agenda.getVisible().equals("S")) {
		 * retorno=false; } } }
		 * 
		 * if ((retorno) && (SSSSS.equals(Microfront.RCONTENIDO))) {
		 * ContenidoDelegate contenidodel = DelegateUtil.getContenidoDelegate();
		 * Contenido contenido = contenidodel.obtenerContenido(xxx); String
		 * previ= ""+req.getSession().getAttribute("previsualiza"); if
		 * (!previ.equals("si")) { if (!contenido.getVisible().equals("S")) {
		 * retorno=false; } } }
		 * 
		 * if ((retorno) && (SSSSS.equals(Microfront.RNOTICIA))) {
		 * NoticiaDelegate noticiadel = DelegateUtil.getNoticiasDelegate();
		 * Noticia noticia = noticiadel.obtenerNoticia(xxx); String previ=
		 * ""+req.getSession().getAttribute("previsualiza"); if
		 * (!previ.equals("si")) { if (!noticia.getVisible().equals("S")) {
		 * retorno=false; } } }
		 * 
		 * if ((retorno) && (SSSSS.equals(Microfront.RMICROSITE))) {
		 * MicrositeDelegate micrositedel = DelegateUtil.getMicrositeDelegate();
		 * Microsite microsit = micrositedel.obtenerMicrosite(xxx); String
		 * previ= ""+req.getSession().getAttribute("previsualiza"); if
		 * (!previ.equals("si")) { if (!microsit.getVisible().equals("S")) {
		 * retorno=false; } }
		 * 
		 * }
		 */
		return true;

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
	public ResponseEntity<byte[]> archivoTema(@PathVariable("uriTema") String uriTema, @RequestParam(Microfront.PNAME) String filename) throws IOException {

		try {
			List<ArchivoTemaFront> archivos  = this.dataService.getArchivoTema(uriTema, filename);
			if (archivos == null || archivos.size() < 1) {
				log.error("Archivo " + filename + " no encontrado en tema " + uriTema);
				// TODO: 404?
				// return getForwardError (microsite, lang, model,
				// ErrorMicrosite.ERROR_AMBIT_MICRO);
				return null;
			}

			Archivo archivo = null;
			for (ArchivoTemaFront a : archivos) {
				if (a.getArchivo().getNombre().equals(filename)) {
					archivo = a.getArchivo();
				}
			}
			return this.sirveArchivo(archivo);

		} catch (ExceptionFrontPagina e) {
			log.error(e.getMessage());
			// TODO: 404?
			// return getForwardError (microsite, lang, model,
			// ErrorMicrosite.ERROR_AMBIT_MICRO);
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
	public ResponseEntity<byte[]> cssTema(@PathVariable("uriTema") String uriTema) throws IOException {

		TemaFront tema = null;
		try {
			tema = this.dataService.getTemaFrontByUri(uriTema);
			if (tema == null || tema.getCss() == null) {
				log.error("Css para tema " + uriTema + " no encontrado");
				// TODO: 404?
				// return getForwardError (microsite, lang, model,
				// ErrorMicrosite.ERROR_AMBIT_MICRO);
				return null;
			}

			return this.sirveArchivo(tema.getCss());

		} catch (ExceptionFrontPagina e) {
			log.error(e.getMessage());
			// TODO: 404?
			// return getForwardError (microsite, lang, model,
			// ErrorMicrosite.ERROR_AMBIT_MICRO);
			return null;
		}

	}

	private ResponseEntity<byte[]> sirveArchivo(Archivo archivo) throws IOException {
		if (archivo == null) {
			log.error("Archivo no encontrado");
			// TODO: 404?
			// return getForwardError (microsite, lang, model,
			// ErrorMicrosite.ERROR_AMBIT_MICRO);
			return null;
		}
		// amartin: Si los datos del archivo son nulos en la BD, vamos a
		// buscarlo a Filesystem.
		if (ArchivoUtil.almacenarEnFilesystem()) {

			if ((archivo != null) && (archivo.getDatos() == null)) {

				byte[] datos = ArchivoUtil.obtenerDatosArchivoEnFilesystem(archivo);
				archivo.setDatos(datos);

			}

		}

		// TODO:filename?
		HttpHeaders responseHeaders = new HttpHeaders();
		//responseHeaders.setContentType(MediaType.parseMediaType(archivo.getMime()));
		responseHeaders.setContentLength(new Long(archivo.getPeso()).intValue());
		return new ResponseEntity<byte[]>(archivo.getDatos(), responseHeaders, HttpStatus.OK);
	}
}
