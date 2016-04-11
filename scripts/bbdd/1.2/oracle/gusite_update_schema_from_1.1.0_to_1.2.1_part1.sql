--DROP TABLE gus_banidi cascade constraints;
--DROP TABLE gus_banner cascade constraints;
alter table gus_banner DISABLE constraint GUS_BANMIC_FK;

--DROP TABLE gus_micpro cascade constraints;
alter table gus_micpro DISABLE constraint GUS_MPRMIC_FK;

-- Corregimos algunos "not null" de versiones anteriores 
ALTER TABLE GUS_ACTIVI modify (ACT_MICCOD not null);
ALTER TABLE GUS_AGENDA modify (AGE_MICCOD not null);
ALTER TABLE GUS_COMPOS modify (CMP_MICCOD not null);
ALTER TABLE GUS_CONVOCATORIA modify (MICROSITE_ID not null);
ALTER TABLE GUS_ENCUST modify (ENC_MICCOD not null);
ALTER TABLE GUS_FAQ modify (FAQ_MICCOD not null);
ALTER TABLE GUS_LDISTRIBUCION modify (MICROSITE_ID not null);
ALTER TABLE GUS_MENU modify (MNU_MICCOD not null);
ALTER TABLE GUS_MICUSU modify (MIU_CODMIC not null);
ALTER TABLE GUS_NOTICS modify (NOT_MICCOD not null);
ALTER TABLE GUS_TEMAS modify (TEM_MICCOD not null);


-------------------------------------------------
-- redmine #659 Cambios auditoria
------------------------------------------------

Create table "GUS_AUDITORIA" (
	"AUD_CODI" Number(19,0) NOT NULL ,
	"AUD_MICCOD" Number,
	"AUD_USUARI" Varchar2 (256 CHAR),
	"AUD_FECHA" Date,
	"AUD_OPERACION" Number(1,0),
	"AUD_ENTIDAD" Varchar2 (50 CHAR) NOT NULL ,
	"AUD_INFORMACION" Clob,
	"AUD_ID_ENTIDAD" Varchar2 (255 CHAR),
primary key ("AUD_CODI") 
) 
STORAGE(
	INITIAL 5888K
	MAXEXTENTS 2147483645
	BUFFER_POOL DEFAULT
	)
LOGGING

Alter table "GUS_AUDITORIA" add  foreign key ("AUD_MICCOD") references "GUS_MICROS" ("MIC_CODI")  on delete set null

Create Index "GUS_AUDMIC_I" ON "GUS_AUDITORIA" ("AUD_MICCOD") 

CREATE SEQUENCE "GUS_SEQAUD"


-------------------------------------------------
-- redmine #655 Cambios SEO
------------------------------------------------
--Convierte una cadena a un formato utilizable en url.
--Por ejemplo, convertiría:
--    "Perfil del t(u)rista britànic 2002´''"
--     a:
--     Perfil_del_turista_britanic_2002

CREATE OR REPLACE Function GUS_STRING2URI_F ( nombre IN varchar2 ) RETURN  varchar2
IS
 tmp VARCHAR2(1000);
 uri VARCHAR2(1000);
 origin VARCHAR2(1000);
 dest VARCHAR2(1000);
 validAbc VARCHAR2(1000);
 charAtI VARCHAR2(1000);

BEGIN

  origin     := 'ÁÉÍÓÚÀÈÌÒÙÄËÏÖÜÂÊÎÔÛÃÕÑÝÇáéíóúàèìòùäëïöüâêîôûãõñýç ';
  dest       := 'AEIOUAEIOUAEIOUAEIOUAONYCaeiouaeiouaeiouaeiouaonyc_';
  validAbc   := 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_-';

  IF (nombre IS null) THEN
    RETURN '';
  END IF;

  tmp := lower(translate(
                    nombre,
                    origin,
                    dest)
                    );

  uri := '';
  FOR i IN 1..length(tmp) LOOP
    -- filtramos los caracteres no validos
    charAtI := substr(tmp, i, 1);
    IF INSTR (validAbc, charAtI) != 0 THEN
      uri := uri || substr(tmp, i, 1);
    END IF;
  END LOOP;

RETURN uri;

END;



-------------------------------------
-- INICIO DEPENDENCIA 2 PARTE 1-2-3

ALTER table "GUS_CONIDI" ADD (
    "CID_URI" Varchar2 (270 CHAR)
)

ALTER table "GUS_ENCIDI" ADD (
	"EID_URI" Varchar2 (270 CHAR)
) 
	
ALTER table "GUS_MICROS" ADD (
	"MIC_URI" Varchar2 (32 CHAR),
	"MIC_ANALYTICS" Varchar2 (60 CHAR)
) 

ALTER table "GUS_MICIDI" ADD (
	"MID_KEYWORDS" Varchar2 (1000 CHAR),
	"MID_DESCRIPTION" Varchar2 (4000 CHAR)
) 
    
ALTER table "GUS_NOTIDI" ADD (
	"NID_URI" Varchar2 (530 CHAR)
)     
    
ALTER table "GUS_TPNIDI" ADD (
	"TPI_URI" Varchar2 (125 CHAR)
)    

-- FIN DEPENDENCIA 2 PARTE 1-2-3
-------------------------------------

-- Añadir nuevos campos en el microsite para el control de las versiones
ALTER TABLE GUS_MICROS ADD MIC_VERSION VARCHAR2(2);

ALTER TABLE GUS_MICROS ADD MIC_TIPO_ACCESO VARCHAR2(1);


-------------------------------------
-- INICIO DEPENDENCIA 3 PARTE 1-2

-- Actualizamos a versión 5 los microsites antiguos sin css personalizado
-- Se añade la tabla backup para volver atrás los microsite en el backup
CREATE TABLE GUS_MICROS_BACKUP(
 "MIC_CODI"   NUMBER,
 "MIC_RESTRI"      VARCHAR2(1 CHAR)
);

-- FIN DEPENDENCIA 3 PARTE 1-2
-------------------------------------    
 