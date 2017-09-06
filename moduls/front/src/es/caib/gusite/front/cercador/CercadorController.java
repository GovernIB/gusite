package es.caib.gusite.front.cercador;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import es.caib.gusite.front.general.BaseViewController;
import es.caib.gusite.front.general.ExceptionFrontMicro;
import es.caib.gusite.front.general.Microfront;
import es.caib.gusite.front.general.bean.ErrorMicrosite;
import es.caib.gusite.front.general.bean.PathItem;
import es.caib.gusite.front.view.CercarView;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.SolrDelegate;
import es.caib.gusite.solrutiles.solr.model.IndexEncontrado;
import es.caib.gusite.solrutiles.solr.model.IndexResultados;
import es.caib.solr.api.exception.ExcepcionSolrApi;



/**
 * Gestiona las búsquedas textuales en el microsite
 * 
 * @author at4.net
 * 
 */
@Controller
public class CercadorController extends BaseViewController {

	private static Log log = LogFactory.getLog(CercadorController.class);

	/**
	 * Post de búsqueda textual en el microsite
	 * 
	 * @param uri
	 *            Uri de microsite
	 * @param lang
	 *            Idioma de la petición
	 * @param cerca
	 *            Texto a buscar
	 * @return
	 * @throws ExcepcionSolrApi 
	 */
	@RequestMapping(method = RequestMethod.POST, value = "{uri}/{lang:[a-zA-Z][a-zA-Z]}/search/")
	public ModelAndView cercar(@PathVariable("uri") SiteId URI, @PathVariable("lang") Idioma lang,
			@RequestParam(value = "cerca", required = true, defaultValue = "") String cerca,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa, HttpServletRequest req, HttpServletResponse response)   {

		CercarView view = new CercarView();
		Microsite microsite = new Microsite();
		try {
			super.configureLayoutView(URI.uri, lang, view, pcampa);
			microsite = view.getMicrosite();
			if (microsite == null) {
				throw new ExceptionFrontMicro(ErrorMicrosite.ERROR_MICRO_URI_MSG + URI);				
			}

			// metodo buscar(); de Bdcercador.java
			SolrDelegate indexo = DelegateUtil.getSolrDelegate();
			IndexResultados resultado;
			
			resultado = indexo.buscar(req.getSession().getId(), "" + microsite.getId().longValue(), lang.getLang(), null, cerca, true);
				
			// hasta aqui metodo buscar();
			
			if (resultado.getLista() != null) {				
				for (IndexEncontrado res : resultado.getLista()) {
					//Las urls están "hard-coded" en formato legacy
					String url = res.getUrl();
					if(url!=null){
						if (url.startsWith("/sacmicrofront/")) {
							//Quitamos el contextpath hardcoded
							url = url.substring(15);
						}
						
						if(!res.isDisponible()){
							res.setUrl("");
						}else{
							res.setUrl( this.urlFactory.legacyToFrontUri(url, lang));
						}						
					}else{
						//Caso especial, la url obtenida es null y no debería.
						String error = "";
						error += "ERROR AL BUSCAR, la url del resultado está vacia: CercadorControler.java-->cercar;";
						error += "BUSQUEDA:idsesion:"+req.getSession().getId()+", Microsite:" + microsite.getId().longValue() + ", idioma:" + lang.getLang() +", words:'" + cerca + "', booleano:true.";		
						error += "VALOR INDEXENCONTRADO:"+ res.getStringValores();		
						log.error(error);
						res.setUrl("");
						res.setDisponible(false);
					}
				}
			}
			
			view.setBusqueda(cerca);
			view.setListado(resultado);
			this.cargarMollapan(view);
			return this.modelForView(this.templateNameFactory.cercar(microsite), view);

		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_MICRO, response,URI.uri,lang,req);
		} catch (DelegateException e) {
			log.error("Error en la busqueda: " + e);
			return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_ACCES, response);
		} catch (ExcepcionSolrApi e) {
			log.error("Error en la busqueda: " + e);
			return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_SOLR, response);
		} catch (Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_SERVER, response);
		} 
	}

	/**
	 * Post de búsqueda textual en el microsite en castellano
	 * 
	 * @param uri
	 *            Uri de microsite
	 * @param cerca
	 *            Texto a buscar
	 * @return
	 * @throws ExcepcionSolrApi 
	 */
	@RequestMapping(method = RequestMethod.POST, value = "{uri}/buscar/")
	public ModelAndView cercarEs(@PathVariable("uri") SiteId URI, @RequestParam(value = "cerca", required = true, defaultValue = "") String cerca,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa, HttpServletRequest req, HttpServletResponse response)  {

		return this.cercar(URI, new Idioma(LANG_ES), cerca, mcont, pcampa, req, response);
	}

	/**
	 * Post de búsqueda textual en el microsite en catalán
	 * 
	 * @param uri
	 *            Uri de microsite
	 * @param cerca
	 *            Texto a buscar
	 * @return
	 * @throws ExcepcionSolrApi 
	 */
	@RequestMapping(method = RequestMethod.POST, value = "{uri}/cercar/")
	public ModelAndView cercarCa(@PathVariable("uri") SiteId URI, @RequestParam(value = "cerca", required = true, defaultValue = "") String cerca,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa, HttpServletRequest req, HttpServletResponse response)  {

		return this.cercar(URI, new Idioma(LANG_CA), cerca, mcont, pcampa, req, response);
	}

	/**
	 * Post de búsqueda textual en el microsite en inglés
	 * 
	 * @param uri
	 *            Uri de microsite
	 * @param cerca
	 *            Texto a buscar
	 * @return
	 * @throws ExcepcionSolrApi 
	 */
	@RequestMapping(method = RequestMethod.POST, value = "{uri}/search/")
	public ModelAndView cercarEn(@PathVariable("uri") SiteId URI, @RequestParam(value = "cerca", required = true, defaultValue = "") String cerca,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa, HttpServletRequest req, HttpServletResponse response)  {

		return this.cercar(URI, new Idioma(LANG_EN), cerca, mcont, pcampa, req, response);
	}

	/**
	 * Get de búsqueda textual en el microsite
	 * 
	 * @param uri
	 *            Uri de microsite
	 * @param lang
	 *            Idioma de la petición
	 * @param cerca
	 *            Texto a buscar
	 * @return
	 * @throws ExcepcionSolrApi 
	 */
	@RequestMapping(method = RequestMethod.GET, value = "{uri}/{lang:[a-zA-Z][a-zA-Z]}/search/")
	public ModelAndView cercarGet(@PathVariable("uri") SiteId URI, @PathVariable("lang") Idioma lang,
			@RequestParam(value = "cerca", required = true, defaultValue = "") String cerca,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa, HttpServletRequest req, HttpServletResponse response)  {

		return this.cercar(URI, lang, cerca, mcont, pcampa, req, response);

	}

	/**
	 * Get de búsqueda textual en el microsite en castellano
	 * 
	 * @param uri
	 *            Uri de microsite
	 * @param cerca
	 *            Texto a buscar
	 * @return
	 * @throws ExcepcionSolrApi 
	 */
	@RequestMapping(method = RequestMethod.GET, value = "{uri}/buscar/")
	public ModelAndView cercarEsGet(@PathVariable("uri") SiteId URI, @RequestParam(value = "cerca", required = true, defaultValue = "") String cerca,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa, HttpServletRequest req, HttpServletResponse response)  {

		return this.cercarGet(URI, new Idioma(LANG_ES), cerca, mcont, pcampa, req, response);
	}

	/**
	 * Get de búsqueda textual en el microsite en catalán
	 * 
	 * @param uri
	 *            Uri de microsite
	 * @param cerca
	 *            Texto a buscar
	 * @return
	 * @throws ExcepcionSolrApi 
	 */
	@RequestMapping(method = RequestMethod.GET, value = "{uri}/cercar/")
	public ModelAndView cercarCaGet(@PathVariable("uri") SiteId URI, @RequestParam(value = "cerca", required = true, defaultValue = "") String cerca,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa, HttpServletRequest req, HttpServletResponse response)  {

		return this.cercarGet(URI, new Idioma(LANG_CA), cerca, mcont, pcampa, req, response);
	}

	/**
	 * Get de búsqueda textual en el microsite en inglés
	 * 
	 * @param uri
	 *            Uri de microsite
	 * @param cerca
	 *            Texto a buscar
	 * @return
	 * @throws ExcepcionSolrApi 
	 */
	@RequestMapping(method = RequestMethod.GET, value = "{uri}/search/")
	public ModelAndView cercarEnGet(@PathVariable("uri") SiteId URI, @RequestParam(value = "cerca", required = true, defaultValue = "") String cerca,
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa, HttpServletRequest req, HttpServletResponse response)  {

		return this.cercarGet(URI, new Idioma(LANG_EN), cerca, mcont, pcampa, req, response);
	}

	@Override
	public String setServicio() {
		return null;
	}

	/**
	 * Método privado para guardar el recorrido que ha realizado el usuario por
	 * el microsite.
	 * 
	 * @param microsite
	 * @param model
	 * @param lang
	 */

	private void cargarMollapan(CercarView view) {

		List<PathItem> path = super.getBasePath(view);

		String titulo = this.getMessage("cercar.resultados", view.getLang());

		path.add(new PathItem(titulo, this.urlFactory.cercar(view.getMicrosite(), view.getLang())));

		// Datos para la plantilla
		view.setPathData(path);

	}

}
