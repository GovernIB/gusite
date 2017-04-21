package es.caib.gusite.front.view;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import es.caib.gusite.micromodel.Microsite;

@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TemplateView {

	public static final String LAYOUT 									= "layout";
	public static final String INDEX 									= "index";
	public static final String MAPA 									= "home/mapa";
	public static final String ACCESIBILIDAD							= "home/accessibilitat";
	public static final String NOTICIA_LISTAR_NOTICIAS					= "noticia/listarnoticias";
	public static final String NOTICIA_LISTAR_LINKS						= "noticia/listarlinks";
	public static final String NOTICIA_LISTAR_DOCUMENTOS				= "noticia/listardocumentos";
	public static final String NOTICIA_LISTAR_NOTICIAS_EXTERNAS 		= "noticia/listarnoticiasexternas";
	public static final String NOTICIA_GALERIA_FOTOS 					= "noticia/mostrarGaleriaFotos";
	public static final String NOTICIA_LISTAR_UBICACIONES				= "noticia/listarUbicaciones";
	public static final String NOTICIA_FICHA 							= "noticia/noticia";
	public static final String NOTICIA_UBICACION						= "noticia/ubicacion";
	public static final String AGENDA_FECHA 							= "agenda/agenda";
	public static final String AGENDA_LISTADO 							= "agenda/listaragenda";
	public static final String FAQ_LISTAR_FAQS 							= "faq/listarfaqs";
	public static final String CONTENIDO	 							= "contenido/contenido";
	public static final String CONTACTO_LISTAR_CONTACTOS				= "contacto/listarcontactos";
	public static final String CONTACTO_CONTACTO						= "contacto/contacto";
	public static final String CONTACTO_ENVIO_CONTACTO					= "contacto/enviocontacto";
	public static final String ENCUESTA_ENCUESTA 						= "encuesta/encuesta";
	public static final String ENCUESTA_ENVIO_ENCUESTA					= "encuesta/envioencuesta";
	public static final String CERCAR		 							= "cercador/cercar";
	public static final String TAW_ITEM		 							= "general/tawitem";
	public static final String MSG_GENERICO 							= "usuariomsg/msggenerico";
	public static final String ERROR_GENERICO 							= "error/errorgenerico";
	public static final String MENU_PREVIEW 							= "home/menupreview";
	
	

	String value();
	String[] extraViews() default {};

}
