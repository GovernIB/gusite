package es.caib.gusite.front.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.caib.gusite.front.general.ExceptionFront;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.PersonalizacionPlantilla;

@Service
public class TemplateNameFactory {

	private static Log log = LogFactory.getLog(TemplateNameFactory.class);
	
	@Autowired
	private TemplateDataService templateDataService;
	

	/**
	 * Carga la plantilla para el microsite
	 * @param microsite
	 * @param plantilla
	 * @return
	 */
	public String getPlantilla (Microsite microsite, String plantilla) {
		try {
			PersonalizacionPlantilla persPlant = this.templateDataService.getPlantilla(microsite, plantilla);
			if (persPlant != null) {
				if ("S".equals(microsite.getDesarrollo())) {
					//En caso de que el microsite esté en modo de desarrollo, incluimos "template resolver" con caching deshabilitado
					return "devdb:" + persPlant.getId();
				} else if (!"S".equals( System.getProperty("es.caib.gusite.front.templateCaching") )) {
					//Lo mismo no está activado el caché de plantillas para la instalación
					return "devdb:" + persPlant.getId();
				} else {
					return "db:" + persPlant.getId();
				}
			} else {
				return plantilla;
			}
		} catch (ExceptionFront e) {
			log.error("Problema obteniendo plantilla:" + plantilla + " para el microsite:" + microsite.getId());
			return plantilla;
		}
	}
	

	public String errorGenerico(Microsite microsite) {
		return getPlantilla(microsite, "error/errorgenerico");
	}

	public String home(Microsite microsite) {
		return getPlantilla(microsite, "index");
	}

	public String listarNoticiasExternas(Microsite microsite) {
		return getPlantilla(microsite, "noticia/listarnoticiasexternas");
	}

	public String listarNoticiasAnyos(Microsite microsite) {
		return getPlantilla(microsite, "noticia/listarnoticiasanual");
	}

	public String listarNoticias(Microsite microsite) {
		return getPlantilla(microsite, "noticia/listarnoticias");
	}

	public String listarLinksAnyos(Microsite microsite) {
		return getPlantilla(microsite, "noticia/listarlinksanual");
	}

	public String listarLinks(Microsite microsite) {
		return getPlantilla(microsite, "noticia/listarlinksnormal");
	}

	public String listarDocumentosAnyos(Microsite microsite) {
		return getPlantilla(microsite, "noticia/listardocumentosanual");
	}

	public String listarDocumentos(Microsite microsite) {
		return getPlantilla(microsite, "noticia/listardocumentosnormal");
	}

	public String mostrarGaleriaFotosAnyos(Microsite microsite) {
		return getPlantilla(microsite, "noticia/mostrarGaleriaFotosAnual");
	}

	public String mostrarGaleriaFotos(Microsite microsite) {
		return getPlantilla(microsite, "noticia/mostrarGaleriaFotos");
	}

	public String listarFaqs(Microsite microsite) {
		return getPlantilla(microsite, "faq/listarfaqs");
	}

	public String mapa(Microsite microsite) {
		return getPlantilla(microsite, "home/mapa");
	}

	public String accessibilitat(Microsite microsite) {
		return getPlantilla(microsite, "home/accessibilitat");
	}

	public String contenido(Microsite microsite) {
		return getPlantilla(microsite, "contenido/contenido");
	}

	public String mostrarNoticia(Microsite microsite) {
		return getPlantilla(microsite, "noticia/noticia");
	}

	public String listarAgendaFecha(Microsite microsite) {
		return getPlantilla(microsite, "agenda/agenda");
	}

	public String listarAgenda(Microsite microsite) {
		return getPlantilla(microsite, "agenda/listaragenda");
	}

	public String listarContactos(Microsite microsite) {
		return getPlantilla(microsite, "contacto/listarcontactos");
	}

	public String contacto(Microsite microsite) {
		return getPlantilla(microsite, "contacto/contacto");
	}

	public String envioContacto(Microsite microsite) {
		return getPlantilla(microsite, "contacto/enviocontacto");
	}

	public String encuesta(Microsite microsite) {
		return getPlantilla(microsite, "encuesta/encuesta");
	}

	public String envioEncuesta(Microsite microsite) {
		return getPlantilla(microsite, "encuesta/envioencuesta");
	}

	public String cercar(Microsite microsite) {
		return getPlantilla(microsite, "cercador/cercar");
	}

	public String tawItem(Microsite microsite) {
		return getPlantilla(microsite, "general/tawitem");
	}

	public String mailing(Microsite microsite) {
		return getPlantilla(microsite, "usuariomsg/msggenerico");
	}

	public String menuPreview(Microsite microsite) {
		return getPlantilla(microsite, "home/menupreview");
	}

	public String moduloAgenda(Microsite microsite) {
		return getPlantilla(microsite, "agendaCalendarListado :: calendario");
	}

	public String moduloNoticiasHome(Microsite microsite) {
		return getPlantilla(microsite, "noticiasListado :: noticias");
	}

	public String componenteElementos(Microsite microsite) {
		return getPlantilla(microsite, "componentes :: elementos");
	}

	
	public String cabecera(Microsite microsite) {
		return getPlantilla(microsite, "general/cabecera::cabecera");
	}
}
