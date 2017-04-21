-- Comentario sobre el nuevo tipo de listado mapa
COMMENT ON COLUMN GUS_TPNOTI.TPN_TPELEM IS 'Tipo de elemento: 0.Fitxa, 1.Enllaç, 2.Llista de Documents, 3.Conexió externa, 4.Galeria de fotos, 5.Mapa';

--Añadir campos para los elementos del nuevo listado tipo mapa.
ALTER table "GUS_NOTICS" ADD (
	"NOT_LATITUD" Varchar2 (100 CHAR),
	"NOT_LONGITUD" Varchar2 (100 CHAR),
	"NOT_ICOCOLOR" Varchar2 (20 CHAR)
); 
  
COMMENT ON COLUMN "GUS_NOTICS"."NOT_LATITUD" IS 'Indica la ubicación de la notícia (Latitud)';
COMMENT ON COLUMN "GUS_NOTICS"."NOT_LONGITUD" IS 'Indica la ubicación de la notícia (Longitud)';
COMMENT ON COLUMN "GUS_NOTICS"."NOT_ICOCOLOR" IS 'Indica el color hexadecimal del marcador de la ubicación (color del icono en el mapa)';


INSERT INTO GUS_FR_PLANTILLA (PLA_CODI, PLA_VERSION, PLA_NOMBRE, PLA_TITULO) values (GUS_NEXTVAL('GUS_SEQPLA'), '50', 'noticia/ubicacion', 'Ficha de ubicacion');
INSERT INTO GUS_FR_PLANTILLA (PLA_CODI, PLA_VERSION, PLA_NOMBRE, PLA_TITULO) values (GUS_NEXTVAL('GUS_SEQPLA'), '50', 'noticia/listarUbicaciones', 'Listado de Ubicaciones');