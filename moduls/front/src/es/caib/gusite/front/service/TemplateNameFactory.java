package es.caib.gusite.front.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.caib.gusite.front.general.ExceptionFront;
import es.caib.gusite.front.view.TemplateView;
import es.caib.gusite.micromodel.Microsite;

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
	public String getPlantilla(final Microsite microsite, final String plantillaOfragmento) {
		String plantilla = plantillaOfragmento;
		String fragmento = "";
		if (plantillaOfragmento.contains("::")) {
			// Se trata de un fragment
			final int index = plantillaOfragmento.indexOf("::");
			plantilla = plantillaOfragmento.substring(0, index).trim();
			fragmento = plantillaOfragmento.substring(index).trim();
		}
		try {

			final List<String> plantillas = this.templateDataService.getPlantillasAplicables(microsite.getId(),
					microsite.getDesarrollo());
			final String persPlant = getPlantillaByFragmento(plantillas, plantilla);
			if (persPlant == null) {
				return plantilla + fragmento;
			} else {
				if ("S".equals(microsite.getDesarrollo())) {
					// En caso de que el microsite esté en modo de desarrollo,
					// incluimos "template resolver" con caching deshabilitado
					return "devdb:" + persPlant + fragmento;
				} else if (!"S".equals(System.getProperty("es.caib.gusite.front.templateCaching"))) {
					// Lo mismo no está activado el caché de plantillas para la
					// instalación
					return "devdb:" + persPlant + fragmento;
				} else {
					return "db:" + persPlant + fragmento;
				}
			}
		} catch (final ExceptionFront e) {
			log.error("Problema obteniendo plantilla:" + plantilla + " para el microsite:" + microsite.getId());
			return plantilla + fragmento;
		}
	}

	private String getPlantillaByFragmento(final List<String> plantillas, final String fragmento) {
		for (final String pp : plantillas) {
			if (pp.split("-")[1].equals(fragmento)) {
				return pp.split("-")[0];
			}
		}
		return null;
	}

	public String errorGenerico(final Microsite microsite) {
		return getPlantilla(microsite, TemplateView.ERROR_GENERICO);
	}

	public String layout(final Microsite microsite) {
		return getPlantilla(microsite, TemplateView.LAYOUT);
	}

	public String home(final Microsite microsite) {
		return getPlantilla(microsite, TemplateView.INDEX);
	}

	public String listarNoticiasExternas(final Microsite microsite) {
		return getPlantilla(microsite, TemplateView.NOTICIA_LISTAR_NOTICIAS_EXTERNAS);
	}

	public String listarNoticias(final Microsite microsite) {
		return getPlantilla(microsite, TemplateView.NOTICIA_LISTAR_NOTICIAS);
	}

	public String listarLinks(final Microsite microsite) {
		return getPlantilla(microsite, TemplateView.NOTICIA_LISTAR_LINKS);
	}

	public String listarDocumentos(final Microsite microsite) {
		return getPlantilla(microsite, TemplateView.NOTICIA_LISTAR_DOCUMENTOS);
	}

	public String mostrarGaleriaFotos(final Microsite microsite) {
		return getPlantilla(microsite, TemplateView.NOTICIA_GALERIA_FOTOS);
	}

	public String listarUbicaciones(final Microsite microsite) {
		return getPlantilla(microsite, TemplateView.NOTICIA_LISTAR_UBICACIONES);
	}

	public String listarFaqs(final Microsite microsite) {
		return getPlantilla(microsite, TemplateView.FAQ_LISTAR_FAQS);
	}

	public String mapa(final Microsite microsite) {
		return getPlantilla(microsite, TemplateView.MAPA);
	}

	public String accessibilitat(final Microsite microsite) {
		return getPlantilla(microsite, TemplateView.ACCESIBILIDAD);
	}

	public String contenido(final Microsite microsite) {
		return getPlantilla(microsite, TemplateView.CONTENIDO);
	}

	public String mostrarNoticia(final Microsite microsite) {
		return getPlantilla(microsite, TemplateView.NOTICIA_FICHA);
	}

	public String mostrarUbicacion(final Microsite microsite) {
		return getPlantilla(microsite, TemplateView.NOTICIA_UBICACION);
	}

	public String listarAgendaFecha(final Microsite microsite) {
		return getPlantilla(microsite, TemplateView.AGENDA_FECHA);
	}

	public String listarAgenda(final Microsite microsite) {
		return getPlantilla(microsite, TemplateView.AGENDA_LISTADO);
	}

	public String listarContactos(final Microsite microsite) {
		return getPlantilla(microsite, TemplateView.CONTACTO_LISTAR_CONTACTOS);
	}

	public String contacto(final Microsite microsite) {
		return getPlantilla(microsite, TemplateView.CONTACTO_CONTACTO);
	}

	public String envioContacto(final Microsite microsite) {
		return getPlantilla(microsite, TemplateView.CONTACTO_ENVIO_CONTACTO);
	}

	public String encuesta(final Microsite microsite) {
		return getPlantilla(microsite, TemplateView.ENCUESTA_ENCUESTA);
	}

	public String envioEncuesta(final Microsite microsite) {
		return getPlantilla(microsite, TemplateView.ENCUESTA_ENVIO_ENCUESTA);
	}

	public String cercar(final Microsite microsite) {
		return getPlantilla(microsite, TemplateView.CERCAR);
	}

	public String tawItem(final Microsite microsite) {
		return getPlantilla(microsite, TemplateView.TAW_ITEM);
	}

	public String mailing(final Microsite microsite) {
		return getPlantilla(microsite, TemplateView.MSG_GENERICO);
	}

	public String menuPreview(final Microsite microsite) {
		return getPlantilla(microsite, TemplateView.MENU_PREVIEW);
	}

	public String moduloAgenda(final Microsite microsite) {
		return getPlantilla(microsite, "general/agendaCalendarListado :: calendario");
	}

	public String moduloNoticiasHome(final Microsite microsite) {
		return getPlantilla(microsite, "general/noticiasListado :: noticias");
	}

	public String moduloListaNoticias(final Microsite microsite) {
		return getPlantilla(microsite, "general/componentes :: componenteListaNoticias");
	}

	public String moduloListaUbicaciones(final Microsite microsite) {
		return getPlantilla(microsite, "general/componentes :: componenteListaUbicaciones");
	}

	public String moduloListaUbicacionesScript(final Microsite microsite) {
		return getPlantilla(microsite, "general/componentes :: componenteListaUbicacionesScript");
	}

	public String moduloListaEnlaces(final Microsite microsite) {
		return getPlantilla(microsite, "general/componentes :: componenteListaEnlaces");
	}

	public String moduloListaDocumentos(final Microsite microsite) {
		return getPlantilla(microsite, "general/componentes :: componenteListaDocumentos");
	}

	public String componenteElementos(final Microsite microsite) {
		return getPlantilla(microsite, "general/componentes :: elementos");
	}

	public String componenteEncuesta(final Microsite microsite) {
		return getPlantilla(microsite, TemplateView.ENCUESTA_ENCUESTA + " :: encuesta");
	}

	public String componenteExterno(final Microsite microsite) {
		return getPlantilla(microsite, "general/componentes :: componenteexterno");
	}

	public String cabecera(final Microsite microsite) {
		return getPlantilla(microsite, "general/cabecera::cabecera");
	}
}
