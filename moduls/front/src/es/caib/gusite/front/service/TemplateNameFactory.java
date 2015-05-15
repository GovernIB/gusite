package es.caib.gusite.front.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.caib.gusite.front.general.ExceptionFront;
import es.caib.gusite.front.view.TemplateView;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.PersonalizacionPlantilla;

@Component
public class TemplateNameFactory {

	private static Log log = LogFactory.getLog(TemplateNameFactory.class);

	@Autowired
	private TemplateDataService templateDataService;

	/**
	 * Carga la plantilla para el microsite
	 * 
	 * @param microsite
	 * @param plantillaOfragmento
	 * @return
	 */
	public String getPlantilla(Microsite microsite, String plantillaOfragmento) {
		String plantilla = plantillaOfragmento;
		String fragmento = "";
		if (plantillaOfragmento.contains("::")) {
			// Se trata de un fragment
			int index = plantillaOfragmento.indexOf("::");
			plantilla = plantillaOfragmento.substring(0, index).trim();
			fragmento = plantillaOfragmento.substring(index).trim();
		}
		try {
			PersonalizacionPlantilla persPlant = this.templateDataService.getPlantilla(microsite, plantilla);
			if (persPlant != null) {
				if ("S".equals(microsite.getDesarrollo())) {
					// En caso de que el microsite esté en modo de desarrollo,
					// incluimos "template resolver" con caching deshabilitado
					return "devdb:" + persPlant.getId() + fragmento;
				} else if (!"S".equals(System.getProperty("es.caib.gusite.front.templateCaching"))) {
					// Lo mismo no está activado el caché de plantillas para la
					// instalación
					return "devdb:" + persPlant.getId() + fragmento;
				} else {
					return "db:" + persPlant.getId() + fragmento;
				}
			} else {
				return plantilla + fragmento;
			}
		} catch (ExceptionFront e) {
			log.error("Problema obteniendo plantilla:" + plantilla + " para el microsite:" + microsite.getId());
			return plantilla + fragmento;
		}
	}

	public String errorGenerico(Microsite microsite) {
		return getPlantilla(microsite, TemplateView.ERROR_GENERICO);
	}

	public String layout(Microsite microsite) {
		return getPlantilla(microsite, TemplateView.LAYOUT);
	}

	public String home(Microsite microsite) {
		return getPlantilla(microsite, TemplateView.INDEX);
	}

	public String listarNoticiasExternas(Microsite microsite) {
		return getPlantilla(microsite, TemplateView.NOTICIA_LISTAR_NOTICIAS_EXTERNAS);
	}

	public String listarNoticias(Microsite microsite) {
		return getPlantilla(microsite, TemplateView.NOTICIA_LISTAR_NOTICIAS);
	}

	public String listarLinks(Microsite microsite) {
		return getPlantilla(microsite, TemplateView.NOTICIA_LISTAR_LINKS);
	}

	public String listarDocumentos(Microsite microsite) {
		return getPlantilla(microsite, TemplateView.NOTICIA_LISTAR_DOCUMENTOS);
	}

	public String mostrarGaleriaFotos(Microsite microsite) {
		return getPlantilla(microsite, TemplateView.NOTICIA_GALERIA_FOTOS);
	}

	public String listarFaqs(Microsite microsite) {
		return getPlantilla(microsite, TemplateView.FAQ_LISTAR_FAQS);
	}

	public String mapa(Microsite microsite) {
		return getPlantilla(microsite, TemplateView.MAPA);
	}

	public String accessibilitat(Microsite microsite) {
		return getPlantilla(microsite, TemplateView.ACCESIBILIDAD);
	}

	public String contenido(Microsite microsite) {
		return getPlantilla(microsite, TemplateView.CONTENIDO);
	}

	public String mostrarNoticia(Microsite microsite) {
		return getPlantilla(microsite, TemplateView.NOTICIA_FICHA);
	}

	public String listarAgendaFecha(Microsite microsite) {
		return getPlantilla(microsite, TemplateView.AGENDA_FECHA);
	}

	public String listarAgenda(Microsite microsite) {
		return getPlantilla(microsite, TemplateView.AGENDA_LISTADO);
	}

	public String listarContactos(Microsite microsite) {
		return getPlantilla(microsite, TemplateView.CONTACTO_LISTAR_CONTACTOS);
	}

	public String contacto(Microsite microsite) {
		return getPlantilla(microsite, TemplateView.CONTACTO_CONTACTO);
	}

	public String envioContacto(Microsite microsite) {
		return getPlantilla(microsite, TemplateView.CONTACTO_ENVIO_CONTACTO);
	}

	public String encuesta(Microsite microsite) {
		return getPlantilla(microsite, TemplateView.ENCUESTA_ENCUESTA);
	}

	public String envioEncuesta(Microsite microsite) {
		return getPlantilla(microsite, TemplateView.ENCUESTA_ENVIO_ENCUESTA);
	}

	public String cercar(Microsite microsite) {
		return getPlantilla(microsite, TemplateView.CERCAR);
	}

	public String tawItem(Microsite microsite) {
		return getPlantilla(microsite, TemplateView.TAW_ITEM);
	}

	public String mailing(Microsite microsite) {
		return getPlantilla(microsite, TemplateView.MSG_GENERICO);
	}

	public String menuPreview(Microsite microsite) {
		return getPlantilla(microsite, TemplateView.MENU_PREVIEW);
	}

	public String moduloAgenda(Microsite microsite) {
		return getPlantilla(microsite, "general/agendaCalendarListado :: calendario");
	}

	public String moduloNoticiasHome(Microsite microsite) {
		return getPlantilla(microsite, "general/noticiasListado :: noticias");
	}
	
	public String moduloListaNoticias(Microsite microsite) {
		return getPlantilla(microsite, "general/componentes :: componenteListaNoticias");
	}	
	public String moduloListaEnlaces(Microsite microsite) {
		return getPlantilla(microsite, "general/componentes :: componenteListaEnlaces");
	}
	public String moduloListaDocumentos(Microsite microsite) {
		return getPlantilla(microsite, "general/componentes :: componenteListaDocumentos");
	}

	public String componenteElementos(Microsite microsite) {
		return getPlantilla(microsite, "general/componentes :: elementos");
	}

	public String componenteEncuesta(Microsite microsite) {
		return getPlantilla(microsite, TemplateView.ENCUESTA_ENCUESTA + " :: encuesta");
	}

	public String componenteExterno(Microsite microsite) {
		return getPlantilla(microsite, "general/componentes :: componenteexterno");
	}

	public String cabecera(Microsite microsite) {
		return getPlantilla(microsite, "general/cabecera::cabecera");
	}
}
