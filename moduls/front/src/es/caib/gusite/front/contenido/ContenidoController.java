package es.caib.gusite.front.contenido;


import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.caib.gusite.front.general.BaseController;
import es.caib.gusite.front.general.ExceptionFrontMicro;
import es.caib.gusite.front.general.ExceptionFrontPagina;
import es.caib.gusite.front.general.Microfront;
import es.caib.gusite.front.general.bean.ErrorMicrosite;
import es.caib.gusite.front.general.bean.PathItem;
import es.caib.gusite.front.service.ContenidoDataService;
import es.caib.gusite.front.util.Fechas;
import es.caib.gusite.micromodel.Contenido;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.Menu;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.TraduccionContenido;
import es.caib.gusite.micromodel.TraduccionMenu;

/**
 * 
 * @author brujula-at4
 *
 */
@Controller
public class ContenidoController extends BaseController {
	
	private static Log log = LogFactory.getLog(ContenidoController.class);

	@Autowired
    protected ContenidoDataService contenidoDataService;
	
	
	/**
	 * TODO: mkey debería ser el uri del site
	 * TODO: tipo debería ser el nemotecnico del tipo
	 * @param lang
	 * @param mkey
	 * @param model
	 * @return
	 */
	@RequestMapping("{mkey}/{lang}/c/{idContenido}") 
	public String contenido (
					@PathVariable("mkey") SiteId siteId, 
					@PathVariable("lang") Idioma lang,
					@PathVariable("idContenido") Long idContenido,
					Model model,
					@RequestParam(value=Microfront.MCONT, required = false, defaultValue="") String mcont,
					@RequestParam(value=Microfront.PCAMPA, required = false, defaultValue="") String pcampa,
					@RequestParam(value="previsual", required = false, defaultValue="") String previsual,
					@RequestParam(value="tipo", required = false, defaultValue="") String tipobeta,
					@RequestParam(value="redi", required = false, defaultValue="") String redi) {
		
		Microsite microsite = null;
	  	try {
	  		
		  	microsite =  super.loadMicrosite(siteId.mkey, lang, model, pcampa);
			Contenido contenido = this.contenidoDataService.getContenido(microsite, idContenido, lang.getLang());

			if (contenido == null) {
				//TODO: 404
				return getForwardError (microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_PAGINA);
			}
			
			String urlredireccionada = ((TraduccionContenido)contenido.getTraduccion(lang.getLang())).getUrl();
			if (!StringUtils.isEmpty(urlredireccionada)) {
		    	String sMenuCont = contenido.getId().toString();
				
	    		if ((sMenuCont != null) && (!sMenuCont.equals("")) &&
						( urlredireccionada.indexOf("idsite=") >=0 
								|| urlredireccionada.indexOf("mkey=") >=0) ){
					return "redirect:" + urlredireccionada + "&mcont="+ sMenuCont;
				}else {
					return "redirect:" + urlredireccionada;
				}
				
			}
			
			//obtenemos el menu padre 
			Menu menu = this.contenidoDataService.obtenerMenuBranch(contenido, lang.getLang());
			boolean previsualizar = !StringUtils.isEmpty(previsual);
			
			//comprobacion de menu en el microsite
			if (!menu.getMicrosite().getId().equals(microsite.getId())) {
					log.error("[error logico] idsite.longValue=" + microsite.getId() + ", menu.getIdmicrosite.longValue=" + menu.getMicrosite().getId().longValue());
					//beanerror.setAviso("Aviso");
					//beanerror.setMensaje("El contenido solicitado no pertenece al site.");
					//error=true;
					return getForwardError (microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO);
			}
			
			//o bien comprobacion de que no esté vacio
			if (contenido.getTraduccion(lang.getLang())==null) {
					//beanerror.setAviso("Aviso");
					//beanerror.setMensaje("El contenido solicitado no contiene información.");
					//error=true;
				//TODO: 404?
				return getForwardError (microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_PAGINA);
			}
			
			//o bien comprobacion de que esté vigente
			if (!previsualizar && !Fechas.vigente(contenido.getFpublicacion(), contenido.getFcaducidad())) {
					//beanerror.setAviso("Aviso");
					//beanerror.setMensaje("El contenido solicitado está caducado.");
					//error=true;
				//TODO: 404?
				return getForwardError (microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_PAGINA);
			}			
			
			//o bien comprobacion de que no esté vacio
			if (!previsualizar && !contenido.getVisible().equals("S")) {
					//beanerror.setAviso("Aviso");
					//beanerror.setMensaje("El contenido solicitado no está disponible al público");
					//error=true;
				//TODO: 404?
				return getForwardError (microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_PAGINA);
			}				
			
		    cargarMollapan(microsite, contenido, menu, model, lang, redi);

    		model.addAttribute("MVS_contenido", contenido);
    		model.addAttribute("MVS_tipobeta", tipobeta);
		    
		    
		    return this.templateNameFactory.contenido(microsite);
	
        } catch (ExceptionFrontMicro e) {
        	log.error(e.getMessage());
        	return getForwardError (microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_MICRO);
		} catch (ExceptionFrontPagina e) {
        	log.error(e.getMessage());
        	return getForwardError (microsite, lang, model, ErrorMicrosite.ERROR_AMBIT_PAGINA);
		}      

	}

	/**
	 * Método privado para guardar el recorrido que ha realizado el usuario por el microsite.
	 * @param microsite
	 * @param menu 
	 * @param model
	 * @param lang
	 * @param redi 
	 * @return string recorrido en el microsite
	 */
	private void cargarMollapan(Microsite microsite, Contenido contenido, Menu menu, Model model, Idioma lang, String redi) {
		
		List<PathItem> path = super.getBasePath(microsite, model, lang);

		String titulomollapa = (menu.getVisible().equals("S"))?((TraduccionMenu)menu.getTraduccion(lang.getLang())).getNombre():"";
    	String titol=((TraduccionContenido)contenido.getTraduccion(lang.getLang())).getTitulo();
    	String submolla = ((titulomollapa.length()>0) && (!redi.equals("yes")))?titulomollapa:"";
		if (!StringUtils.isEmpty(submolla)) {
			path.add(new PathItem(submolla));
	    	if (!redi.equals("yes")) {
	    		model.addAttribute("MVS_titulomollapa", submolla);
	    	}
			
		}
		path.add( new PathItem(titol));
		
		//TODO: eliminar cuando se actualicen las plantillas
	    model.addAttribute("MVS2_mollapan", mollapan(path));

	    //Datos para la plantilla
	    model.addAttribute("MVS2_pathdata", path);
		
	}	
	


	@Override
	public String setServicio() {
		
		return Microfront.RCONTENIDO;
	}

	
	
	
}
