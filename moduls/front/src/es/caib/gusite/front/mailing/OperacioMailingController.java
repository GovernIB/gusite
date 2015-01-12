package es.caib.gusite.front.mailing;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

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
import es.caib.gusite.front.general.DelegateBase;
import es.caib.gusite.front.service.CorreoEngineService;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.ListaDistribucion;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.Tiposervicio;
import es.caib.gusite.micromodel.TraduccionMicrosite;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.LDistribucionDelegate;

@Controller
public class OperacioMailingController extends BaseController {
	
	private static Log log = LogFactory.getLog(OperacioMailingController.class);
	Microsite microsite = null;
	DelegateBase _delegateBase;
	List listaDistrib = null;

	/**
	 * TODO: mkey debería ser el uri del site TODO: tipo debería ser el
	 * nemotecnico del tipo
	 * 
	 * @param lang
	 * @param mkey
	 * @param model
	 * @return
	 */	
	@RequestMapping(method=RequestMethod.GET,value="{mkey}/{lang}/msggenerico/")
	public String mailing(
			@PathVariable("mkey") SiteId siteId, 
			@PathVariable("lang") Idioma lang,
			Model model,
			@RequestParam(value=Microfront.MCONT, required = false, defaultValue="") String mcont,
			@RequestParam(value=Microfront.PCAMPA, required = false, defaultValue="") String pcampa,
			HttpServletRequest req) {

	  	ResourceBundle rb = ResourceBundle.getBundle("ApplicationResources_front", req.getLocale());
		try {
			microsite = super.loadMicrosite(siteId.mkey, lang, model, pcampa);
			//bdSuscripcion constructor
			try {
				_delegateBase = new DelegateBase();
				
				if ((microsite!=null) && (existeServicio(Microfront.RLDISTRIB, lang.getLang()))) {	
					//recogerlista de bdSuscripcion
					listaDistrib = _delegateBase.obtenerListadoDistribucionMicrosite(microsite.getId());
					//hasta aqui recogerlista de bdSuscripcion
				}	
			} catch (Exception e) {
				log.error("Error en la busqueda: " + e);
				return getForwardError(microsite, lang, model,
						ErrorMicrosite.ERROR_AMBIT_ACCES);	
			}
			//hasta aqui bdSuscripcion
			
			StringBuffer body = new StringBuffer();
			String assumpte = "";
			if ("alta".equals(req.getParameter(Microfront.PACTION))){
				//metodo alta() de bdSuscripcion
				LDistribucionDelegate distribDel = DelegateUtil.getLlistaDistribucionDelegate();
				if (req.getParameter(Microfront.PEMAIL) != null &&
						((String)req.getParameter(Microfront.PEMAIL)).matches("^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$") &&
						req.getParameter(Microfront.PDISTRIB) != null){
					Long idLista = Long.parseLong(req.getParameter(Microfront.PDISTRIB));
					if(DelegateUtil.getLlistaDistribucionDelegate().obtenerListaDistribucion(idLista).getPublico()){
						String email = req.getParameter(Microfront.PEMAIL);
						distribDel.anadeCorreo(idLista, email);
						if ("no".equals(""+req.getSession().getAttribute("MVS_stat"))) log.info("Skip Estadistica, preview conten"); 
					}else{
						throw new Exception("Faltan parámetros");
					}
				}else{
					throw new Exception("Faltan parámetros");
				}
				//hasta aqui metodo alta() de bdSuscripcion
				
				assumpte = rb.getString("mailing.assumpte.alta");
				body.append(rb.getString("mailing.body.alta"));
			}else if("baixa".equals(req.getParameter(Microfront.PACTION))){
				//metodo baixa() de bdSuscripcion
				if(req.getParameter(Microfront.PEMAIL) != null){
					for(Object l:listaDistrib){
						LDistribucionDelegate distribDel = DelegateUtil.getLlistaDistribucionDelegate();
						distribDel.borrarCorreo(((ListaDistribucion)l).getId(), req.getParameter(Microfront.PEMAIL));
						if ("no".equals(""+req.getSession().getAttribute("MVS_stat"))) log.info("Skip Estadistica, preview conten"); 
					}
					model.addAttribute("msg", rb.getString("mailing.baixa"));
				}else 
					throw new Exception("Faltan parámetros");
				//hasta aqui metodo baixa() de bdSuscripcion
				assumpte = rb.getString("mailing.assumpte.baixa");
				body.append(rb.getString("mailing.body.baixa"));
			}
			if(microsite.getIdiomas().contains(req.getLocale().toString()))
				body.append(((TraduccionMicrosite)microsite.getTraduccion(req.getLocale().toString())).getTitulo());
			else
				body.append(((TraduccionMicrosite)microsite.getTraduce()).getTitulo());
			
			CorreoEngineService mail = new CorreoEngineService();
			mail.initCorreo(req.getParameter(Microfront.PEMAIL), assumpte, false, body);
			mail.enviarCorreo();	
		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return getForwardError(microsite, lang, model,
					ErrorMicrosite.ERROR_AMBIT_MICRO);
		} catch (Exception e) {
			log.error(e.getMessage());
			return getForwardError(microsite, lang, model,
					ErrorMicrosite.ERROR_AMBIT_MICRO);
		}
		return this.templateNameFactory.mailing(microsite);	
}
	
	/**
	 * Método que devuelve si el servicio que se solicita es ofrecido o no por el microsite.
	 *
	 * @param refservicio una referencia a un servicio
	 * @return boolean true si el tipo de servicio del microsite es igual a refservicio
	 */
	public boolean existeServicio(String refservicio, String idioma) {
		boolean tmp=false;
		try {
	    	ArrayList<?> listserofr = _delegateBase.obtenerListadoServiciosMicrosite(microsite, idioma);
			//chequeamos el servicio
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
}
