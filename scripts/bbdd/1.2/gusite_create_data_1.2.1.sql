-- Inserta la versión v5 y sus plantillas
INSERT INTO GUS_FR_VERSION (FVE_VERSION, FVE_NOMBRE) VALUES ('50', 'GUSITE 1.2: Versió 2015');

-- Layout general
INSERT INTO GUS_FR_PLANTILLA (PLA_CODI, PLA_VERSION, PLA_NOMBRE, PLA_TITULO) values (GUS_NEXTVAL('GUS_SEQPLA'), '50', 'layout', 'Plantilla base que monta la estructura general del resto de páginas web de microsite');
-- Módulos del layout general
INSERT INTO GUS_FR_PLANTILLA (PLA_CODI, PLA_VERSION, PLA_NOMBRE, PLA_TITULO) values (GUS_NEXTVAL('GUS_SEQPLA'), '50', 'general/cabecera', 'Plantilla para la cabecera general de microsite');
INSERT INTO GUS_FR_PLANTILLA (PLA_CODI, PLA_VERSION, PLA_NOMBRE, PLA_TITULO) values (GUS_NEXTVAL('GUS_SEQPLA'), '50', 'general/menu', 'Muestra el menú de microsite');
INSERT INTO GUS_FR_PLANTILLA (PLA_CODI, PLA_VERSION, PLA_NOMBRE, PLA_TITULO) values (GUS_NEXTVAL('GUS_SEQPLA'), '50', 'general/scriptmenu', 'Scripts necesarios para el menú de microsite');
INSERT INTO GUS_FR_PLANTILLA (PLA_CODI, PLA_VERSION, PLA_NOMBRE, PLA_TITULO) values (GUS_NEXTVAL('GUS_SEQPLA'), '50', 'general/pie', 'Pie de página');
INSERT INTO GUS_FR_PLANTILLA (PLA_CODI, PLA_VERSION, PLA_NOMBRE, PLA_TITULO) values (GUS_NEXTVAL('GUS_SEQPLA'), '50', 'general/mollaPan', 'Imprime la miga de pan');

-- Módulos reutilizados
INSERT INTO GUS_FR_PLANTILLA (PLA_CODI, PLA_VERSION, PLA_NOMBRE, PLA_TITULO) values (GUS_NEXTVAL('GUS_SEQPLA'), '50', 'general/componentes', 'Plantillas de componentes para incrustar en contenidos');
INSERT INTO GUS_FR_PLANTILLA (PLA_CODI, PLA_VERSION, PLA_NOMBRE, PLA_TITULO) values (GUS_NEXTVAL('GUS_SEQPLA'), '50', 'general/agendaCalendarListado', 'Tabla con el calendario y listado de eventos');
INSERT INTO GUS_FR_PLANTILLA (PLA_CODI, PLA_VERSION, PLA_NOMBRE, PLA_TITULO) values (GUS_NEXTVAL('GUS_SEQPLA'), '50', 'general/noticiasListado', 'Módulo de listado de noticias de portada');


INSERT INTO GUS_FR_PLANTILLA (PLA_CODI, PLA_VERSION, PLA_NOMBRE, PLA_TITULO) values (GUS_NEXTVAL('GUS_SEQPLA'), '50', 'index', 'Contenido de la HOME proporcionada por GUSITE');
INSERT INTO GUS_FR_PLANTILLA (PLA_CODI, PLA_VERSION, PLA_NOMBRE, PLA_TITULO) values (GUS_NEXTVAL('GUS_SEQPLA'), '50', 'agenda/agenda', 'Listado de eventos de la agenda para una fecha');
INSERT INTO GUS_FR_PLANTILLA (PLA_CODI, PLA_VERSION, PLA_NOMBRE, PLA_TITULO) values (GUS_NEXTVAL('GUS_SEQPLA'), '50', 'agenda/listaragenda', 'Página de agenda del microsite');
INSERT INTO GUS_FR_PLANTILLA (PLA_CODI, PLA_VERSION, PLA_NOMBRE, PLA_TITULO) values (GUS_NEXTVAL('GUS_SEQPLA'), '50', 'cercador/cercar', 'Resultados de una busqueda de texto');
INSERT INTO GUS_FR_PLANTILLA (PLA_CODI, PLA_VERSION, PLA_NOMBRE, PLA_TITULO) values (GUS_NEXTVAL('GUS_SEQPLA'), '50', 'contacto/contacto', 'Muestra un formulario de contacto');
INSERT INTO GUS_FR_PLANTILLA (PLA_CODI, PLA_VERSION, PLA_NOMBRE, PLA_TITULO) values (GUS_NEXTVAL('GUS_SEQPLA'), '50', 'contacto/enviocontacto', 'Página que se muestra tras enviar un contacto satisfactoriamente');
INSERT INTO GUS_FR_PLANTILLA (PLA_CODI, PLA_VERSION, PLA_NOMBRE, PLA_TITULO) values (GUS_NEXTVAL('GUS_SEQPLA'), '50', 'contacto/listarcontactos', 'Muestra la lista de formularios de contacto disponibles');
INSERT INTO GUS_FR_PLANTILLA (PLA_CODI, PLA_VERSION, PLA_NOMBRE, PLA_TITULO) values (GUS_NEXTVAL('GUS_SEQPLA'), '50', 'contenido/contenido', 'Página de contenido');
INSERT INTO GUS_FR_PLANTILLA (PLA_CODI, PLA_VERSION, PLA_NOMBRE, PLA_TITULO) values (GUS_NEXTVAL('GUS_SEQPLA'), '50', 'encuesta/encuesta', 'Página de formulario de encuesta');
INSERT INTO GUS_FR_PLANTILLA (PLA_CODI, PLA_VERSION, PLA_NOMBRE, PLA_TITULO) values (GUS_NEXTVAL('GUS_SEQPLA'), '50', 'encuesta/envioencuesta', 'Muestra información de haber realizado una encuesta así como sus resultados');
INSERT INTO GUS_FR_PLANTILLA (PLA_CODI, PLA_VERSION, PLA_NOMBRE, PLA_TITULO) values (GUS_NEXTVAL('GUS_SEQPLA'), '50', 'faq/listarfaqs', 'Listado de preguntas frecuentes');
INSERT INTO GUS_FR_PLANTILLA (PLA_CODI, PLA_VERSION, PLA_NOMBRE, PLA_TITULO) values (GUS_NEXTVAL('GUS_SEQPLA'), '50', 'home/accessibilitat', 'Información en cuanto a la accesibilidad del web');
INSERT INTO GUS_FR_PLANTILLA (PLA_CODI, PLA_VERSION, PLA_NOMBRE, PLA_TITULO) values (GUS_NEXTVAL('GUS_SEQPLA'), '50', 'home/mapa', 'Mapa del sitio web');
INSERT INTO GUS_FR_PLANTILLA (PLA_CODI, PLA_VERSION, PLA_NOMBRE, PLA_TITULO) values (GUS_NEXTVAL('GUS_SEQPLA'), '50', 'noticia/listarnoticias', 'Listado de noticias');
INSERT INTO GUS_FR_PLANTILLA (PLA_CODI, PLA_VERSION, PLA_NOMBRE, PLA_TITULO) values (GUS_NEXTVAL('GUS_SEQPLA'), '50', 'noticia/listardocumentos', 'Listado de documentos');
INSERT INTO GUS_FR_PLANTILLA (PLA_CODI, PLA_VERSION, PLA_NOMBRE, PLA_TITULO) values (GUS_NEXTVAL('GUS_SEQPLA'), '50', 'noticia/listarlinks', 'Listado de enlaces');
INSERT INTO GUS_FR_PLANTILLA (PLA_CODI, PLA_VERSION, PLA_NOMBRE, PLA_TITULO) values (GUS_NEXTVAL('GUS_SEQPLA'), '50', 'noticia/listarnoticiasexternas', 'Carga un contenido externo');
INSERT INTO GUS_FR_PLANTILLA (PLA_CODI, PLA_VERSION, PLA_NOMBRE, PLA_TITULO) values (GUS_NEXTVAL('GUS_SEQPLA'), '50', 'noticia/mostrarGaleriaFotos', 'Página de galería de fotos');
INSERT INTO GUS_FR_PLANTILLA (PLA_CODI, PLA_VERSION, PLA_NOMBRE, PLA_TITULO) values (GUS_NEXTVAL('GUS_SEQPLA'), '50', 'noticia/noticia', 'Ficha de noticia');
INSERT INTO GUS_FR_PLANTILLA (PLA_CODI, PLA_VERSION, PLA_NOMBRE, PLA_TITULO) values (GUS_NEXTVAL('GUS_SEQPLA'), '50', 'noticia/modulos', 'Módulos para los distintos listados de noticias');
INSERT INTO GUS_FR_PLANTILLA (PLA_CODI, PLA_VERSION, PLA_NOMBRE, PLA_TITULO) values (GUS_NEXTVAL('GUS_SEQPLA'), '50', 'error/errorgenerico', 'Pantalla de mensaje de error genérico');
INSERT INTO GUS_FR_PLANTILLA (PLA_CODI, PLA_VERSION, PLA_NOMBRE, PLA_TITULO) values (GUS_NEXTVAL('GUS_SEQPLA'), '50', 'usuariomsg/msggenerico', 'Plantilla de mensaje de email usada en el mailing');
