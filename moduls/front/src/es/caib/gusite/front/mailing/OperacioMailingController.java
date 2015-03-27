package es.caib.gusite.front.mailing;

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
import org.springframework.web.servlet.ModelAndView;

import es.caib.gusite.front.general.BaseViewController;
import es.caib.gusite.front.general.DelegateBase;
import es.caib.gusite.front.general.ExceptionFrontMicro;
import es.caib.gusite.front.general.Microfront;
import es.caib.gusite.front.general.bean.ErrorMicrosite;
import es.caib.gusite.front.service.CorreoEngineService;
import es.caib.gusite.front.view.MailingView;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.ListaDistribucion;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.TraduccionMicrosite;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.LDistribucionDelegate;

@Controller
public class OperacioMailingController extends BaseViewController {

	private static Log log = LogFactory.getLog(OperacioMailingController.class);

	/**
	 * 
	 * @param lang
	 * @param uri
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "{uri}/{lang}/msggenerico/")
	public ModelAndView mailing(@PathVariable("uri") SiteId URI, @PathVariable("lang") Idioma lang, 
			@RequestParam(value = Microfront.MCONT, required = false, defaultValue = "") String mcont,
			@RequestParam(value = Microfront.PCAMPA, required = false, defaultValue = "") String pcampa, HttpServletRequest req) {

		ResourceBundle rb = ResourceBundle.getBundle("ApplicationResources_front", req.getLocale());

		MailingView view = new MailingView();
		try {
			super.configureLayoutView(URI.uri, lang, view, pcampa);
			Microsite microsite = view.getMicrosite();
			List<?> listaDistrib;
			// bdSuscripcion constructor
			DelegateBase delegateBase = new DelegateBase();

			if ((microsite != null) && (this.existeServicio(microsite, lang, Microfront.RLDISTRIB))) {
				// recogerlista de bdSuscripcion
				listaDistrib = delegateBase.obtenerListadoDistribucionMicrosite(microsite.getId());
				// hasta aqui recogerlista de bdSuscripcion
			} else {
				log.error("No hay microsite o no existe el servicio");
				return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_ACCES);
			}
			// hasta aqui bdSuscripcion

			StringBuffer body = new StringBuffer();
			String assumpte = "";
			if ("alta".equals(req.getParameter(Microfront.PACTION))) {
				// metodo alta() de bdSuscripcion
				LDistribucionDelegate distribDel = DelegateUtil.getLlistaDistribucionDelegate();
				if (req.getParameter(Microfront.PEMAIL) != null
						&& (req.getParameter(Microfront.PEMAIL)).matches("^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$")
						&& req.getParameter(Microfront.PDISTRIB) != null) {
					Long idLista = Long.parseLong(req.getParameter(Microfront.PDISTRIB));
					if (DelegateUtil.getLlistaDistribucionDelegate().obtenerListaDistribucion(idLista).getPublico()) {
						String email = req.getParameter(Microfront.PEMAIL);
						distribDel.anadeCorreo(idLista, email);
						if ("no".equals("" + req.getSession().getAttribute("MVS_stat"))) {
							log.info("Skip Estadistica, preview conten");
						}
					} else {
						throw new Exception("Faltan parámetros");
					}
				} else {
					throw new Exception("Faltan parámetros");
				}
				// hasta aqui metodo alta() de bdSuscripcion

				assumpte = rb.getString("mailing.assumpte.alta");
				body.append(rb.getString("mailing.body.alta"));
			} else if ("baixa".equals(req.getParameter(Microfront.PACTION))) {
				// metodo baixa() de bdSuscripcion
				if (req.getParameter(Microfront.PEMAIL) != null) {
					for (Object l : listaDistrib) {
						LDistribucionDelegate distribDel = DelegateUtil.getLlistaDistribucionDelegate();
						distribDel.borrarCorreo(((ListaDistribucion) l).getId(), req.getParameter(Microfront.PEMAIL));
						if ("no".equals("" + req.getSession().getAttribute("MVS_stat"))) {
							log.info("Skip Estadistica, preview conten");
						}
					}
					view.setMsg(rb.getString("mailing.baixa"));
				} else {
					throw new Exception("Faltan parámetros");
				}
				// hasta aqui metodo baixa() de bdSuscripcion
				assumpte = rb.getString("mailing.assumpte.baixa");
				body.append(rb.getString("mailing.body.baixa"));
			}
			if (microsite.getIdiomas().contains(req.getLocale().toString())) {
				body.append(((TraduccionMicrosite) microsite.getTraduccion(req.getLocale().toString())).getTitulo());
			} else {
				body.append(((TraduccionMicrosite) microsite.getTraduce()).getTitulo());
			}

			CorreoEngineService mail = new CorreoEngineService();
			mail.initCorreo(req.getParameter(Microfront.PEMAIL), assumpte, false, body);
			mail.enviarCorreo();
			return this.modelForView(this.templateNameFactory.mailing(microsite), view);

		} catch (ExceptionFrontMicro e) {
			log.error(e.getMessage());
			return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_MICRO);
		} catch (Exception e) {
			log.error(e.getMessage());
			return this.getForwardError(view, ErrorMicrosite.ERROR_AMBIT_MICRO);
		}
	}

	@Override
	public String setServicio() {
		return Microfront.RLDISTRIB;
	}
}
