package es.caib.gusite.front.service;

import org.springframework.stereotype.Service;

import es.caib.gusite.micromodel.Microsite;

@Service
public class TemplateNameFactory {

	public String errorGenerico(Microsite microsite) {
		return "error/errorgenerico";
	}

	public String home(Microsite microsite) {
		return "index";
	}

	public String listarNoticiasExternas(Microsite microsite) {
		return "noticia/listarnoticiasexternas";
	}

	public String listarNoticiasAnyos(Microsite microsite) {
		return "noticia/listarnoticiasanual";
	}

	public String listarNoticias(Microsite microsite) {
		return "noticia/listarnoticias";
	}

	public String listarLinksAnyos(Microsite microsite) {
		return "noticia/listarlinksanual";
	}

	public String listarLinks(Microsite microsite) {
		return "noticia/listarlinksnormal";
	}

	public String listarDocumentosAnyos(Microsite microsite) {
		return "noticia/listardocumentosanual";
	}

	public String listarDocumentos(Microsite microsite) {
		return "noticia/listardocumentosnormal";
	}

	public String mostrarGaleriaFotosAnyos(Microsite microsite) {
		return "noticia/mostrarGaleriaFotosAnual";
	}

	public String mostrarGaleriaFotos(Microsite microsite) {
		return "noticia/mostrarGaleriaFotos";
	}

	public String listarFaqs(Microsite microsite) {
		return "faq/listarfaqs";
	}

	public String mapa(Microsite microsite) {
		return "home/mapa";
	}

	public String accessibilitat(Microsite microsite) {
		return "home/accessibilitat";
	}

	public String contenido(Microsite microsite) {
		return "contenido/contenido";
	}

	public String mostrarNoticia(Microsite microsite) {
		return "noticia/noticia";
	}

	public String listarAgendaFecha(Microsite microsite) {
		return "agenda/agenda";
	}

	public String listarAgenda(Microsite microsite) {
		return "agenda/listaragenda";
	}

	public String listarContactos(Microsite microsite) {
		return "contacto/listarcontactos";
	}

	public String contacto(Microsite microsite) {
		return "contacto/contacto";
	}

	public String envioContacto(Microsite microsite) {
		return "contacto/enviocontacto";
	}

	public String encuesta(Microsite microsite) {
		return "encuesta/encuesta";
	}

	public String envioEncuesta(Microsite microsite) {
		return "encuesta/envioencuesta";
	}

	public String cercar(Microsite microsite) {
		return "cercador/cercar";
	}

	public String tawItem(Microsite microsite) {
		return "general/tawitem";
	}

	public String mailing(Microsite microsite) {
		return "usuariomsg/msggenerico";
	}

	public String menuPreview(Microsite microsite) {
		return "home/menupreview";
	}

	public String moduloAgenda(Microsite microsite) {
		return "agendaCalendarListado :: calendario";
	}

	public String moduloNoticiasHome(Microsite microsite) {
		return "noticiasListado :: noticias";
	}

	public String componenteElementos(Microsite microsite) {
		return "componentes :: elementos";
	}

}
