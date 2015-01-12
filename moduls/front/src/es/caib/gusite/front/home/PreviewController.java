package es.caib.gusite.front.home;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import es.caib.gusite.front.general.BaseController;
import es.caib.gusite.front.general.ExceptionFrontMicro;
import es.caib.gusite.front.general.Microfront;
import es.caib.gusite.front.general.bean.ErrorMicrosite;
import es.caib.gusite.front.general.bean.PathItem;
import es.caib.gusite.front.general.DelegateBase;
import es.caib.gusite.front.microtag.MParserAgenda;
import es.caib.gusite.front.microtag.MParserElemento;
import es.caib.gusite.front.microtag.MParserHTML;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.Tiposervicio;
import es.caib.gusite.micropersistence.delegate.DelegateException;

@Controller
public class PreviewController extends BaseController {

	private static Log log = LogFactory.getLog(PreviewController.class);
	Microsite microsite = null;
	DelegateBase _delegateBase;
	String idioma;
	String tagHtmlCampanya;
	String tagHtmlAgendaCalendario="-1";
	String tagHtmlAgendaListado="-1";
	String tagHtmlNoticias="-1";

	/**
	 * TODO: mkey debería ser el uri del site TODO: tipo debería ser el
	 * nemotecnico del tipo
	 * 
	 * @param lang
	 * @param mkey
	 * @param model
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET,value="{mkey}/{lang}/menupreview/")
	public String menuPreview(
			@PathVariable("mkey") SiteId siteId, 
			@PathVariable("lang") Idioma lang,
			Model model,
			@RequestParam(value=Microfront.MCONT, required = false, defaultValue="") String mcont,
			@RequestParam(value=Microfront.PCAMPA, required = false, defaultValue="") String pcampa,
			HttpServletRequest req) {
		try{
			microsite = super.loadMicrosite(siteId.mkey, lang, model, pcampa);
			idioma = "" + req.getSession().getAttribute("MVS_idioma");
			//averiguar información de la home
			//Bdhome constructor
			String tipohome;
			tipohome = microsite.getPlantilla();	
			if (tipohome.equals("1")){
				montarplantilla12();	
			}
			if (tipohome.equals("2")){
				montarplantilla12();
			}
			if (tipohome.equals("3")){
				montarplantilla12();
			}
			if (tipohome.equals("4")){
				montarplantilla4();
			}
			if (tipohome.equals("5")){
				montarplantilla12();
			}
			//Bdhome constructor hasta aqui
			cargarMollapan(microsite, model, lang);
			return this.templateNameFactory.menuPreview(microsite);	
		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return getForwardError(microsite, lang, model,
					ErrorMicrosite.ERROR_AMBIT_MICRO);
		} catch (DelegateException e) {
			log.error(e.getMessage());
			return getForwardError(microsite, lang, model,
					ErrorMicrosite.ERROR_AMBIT_MICRO);
		}					
}
	/**
	 * Método privado para montar la plantilla12: campaña, agenda, noticias, banners
	 * @throws DelegateException
	 */
	private void montarplantilla12() throws DelegateException {
		//funcion de Bdhome prepararcampanya
		MParserHTML parsehtml = new MParserHTML(microsite.getRestringido());
		tagHtmlCampanya=parsehtml.getHtmlCampanya(microsite,idioma).toString();
		//hasta aqui funcion de Bdhome prepararcampanya
		
		//funcion de Bdhome prepararAgenda
		if (existeServicio(Microfront.RAGENDA, idioma)) {
			MParserAgenda parseagenda = new MParserAgenda(microsite.getRestringido());
			tagHtmlAgendaCalendario=parseagenda.getHtmlAgendaCalendario(microsite.getId(),idioma,3).toString();
			tagHtmlAgendaListado=parseagenda.getHtmlAgendaListado(microsite.getId(),idioma,3).toString();
		}
		//hasta aqui funcion de Bdhome prepararAgenda
		
		//funcion de Bdhome prepararNoticias
		if (existeServicio(Microfront.RNOTICIA, idioma)) {
			int noticias=3;
			if (microsite.getNumeronoticias()!=0)
				noticias=microsite.getNumeronoticias();
			MParserElemento parseelemento = new MParserElemento(microsite.getRestringido());
			tagHtmlNoticias=parseelemento.getHtmlNoticias(microsite.getId(),idioma, noticias).toString();
		}
		//hasta aqui funcion de Bdhome prepararNoticias
	}
	
	/**
	 * Método privado para montar plantilla4: preparar campaña
	 * @throws DelegateException
	 */
	private void montarplantilla4() throws DelegateException {	
		//funcion de Bdhome prepararcampanya
		MParserHTML parsehtml = new MParserHTML(microsite.getRestringido());
		tagHtmlCampanya=parsehtml.getHtmlCampanya(microsite,idioma).toString();
		//hasta aqui funcion de Bdhome prepararcampanya
	}
	
	public boolean existeServicio(String refservicio,String idioma) {
		boolean tmp=false;
		try {	
	    	ArrayList<?> listserofr = _delegateBase.obtenerListadoServiciosMicrosite(microsite, idioma);
			//chekeamos el servicio
			Iterator<?> iter2=listserofr.iterator();
			while (iter2.hasNext()) {
				Tiposervicio tiposervicio = (Tiposervicio)iter2.next();
				if (tiposervicio.getReferencia().equals(refservicio)){
					tmp=true;
				}
				if (tmp) break;
			}
		} catch (Exception e) {
			log.error("[existeServicio] con el servicio '" + refservicio + "' "  + e.getMessage());
		}
		return tmp;
	}

	@Override
	public String setServicio() {
		// TODO Auto-generated method stub
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
	private void cargarMollapan(Microsite microsite, Model model, Idioma lang) {
		List<PathItem> path =  super.getBasePath(microsite, model, lang);
		String titulo = "Molla";
		path.add(new PathItem(titulo, this.urlFactory.menuPreview(microsite, lang)));
	    //Datos para la plantilla
	    model.addAttribute("MVS2_pathdata", path);
	}	
}
