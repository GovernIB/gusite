DROP SEQUENCE "GUS_SEQUSUENC";
DROP SEQUENCE "GUS_SEQCMP";
DROP SEQUENCE "GUS_SEQCON";
DROP SEQUENCE "GUS_SEQBAN";
DROP SEQUENCE "GUS_SEQAGE";
DROP SEQUENCE "GUS_SEQACT";
DROP SEQUENCE "GUS_SEQ_ALL";
DROP SEQUENCE "GUS_SEQTPN";
DROP SEQUENCE "GUS_SEQTIP";
DROP SEQUENCE "GUS_SEQTEM";
DROP SEQUENCE "GUS_SEQSTA";
DROP SEQUENCE "GUS_SEQRES";
DROP SEQUENCE "GUS_SEQRDA";
DROP SEQUENCE "GUS_SEQPRE";
DROP SEQUENCE "GUS_SEQNOT";
DROP SEQUENCE "GUS_SEQMPR";
DROP SEQUENCE "GUS_SEQMIC";
DROP SEQUENCE "GUS_SEQMEN";
DROP SEQUENCE "GUS_SEQFRQ";
DROP SEQUENCE "GUS_SEQFRM";
DROP SEQUENCE "GUS_SEQFLI";
DROP SEQUENCE "GUS_SEQFAQ";
DROP SEQUENCE "GUS_SEQENC";
DROP SEQUENCE "GUS_SEQDOC";
DROP SEQUENCE "GUS_SQM_ALL";
DROP SEQUENCE "GUS_CONV_SEQ";
DROP SEQUENCE "GUS_LDISTRB_SEQ";
DROP SEQUENCE "GUS_SEQAUD";
DROP SEQUENCE "GUS_SEQFTE";
DROP SEQUENCE "GUS_SEQPLA";
DROP SEQUENCE "GUS_SEQFVE";
DROP SEQUENCE "GUS_SEQPPL";
DROP SEQUENCE "GUS_SEQARC";
DROP SEQUENCE "GUS_SEQSOLR";
DROP SEQUENCE "GUS_SEQSOLJ";

Drop Function "GUS_STRING2URI_F";
Drop Function "GUS_NEXTVAL";

Drop index "GUS_AUDMIC_I";
Drop index "GUS_TPNURI_I";
Drop index "GUS_STAITM_I";
Drop index "GUS_STAMCD_FK_I";
Drop index "GUS_STAMES_I";
Drop index "GUS_STAREF_I";
Drop index "GUS_NIDURI_I";
Drop index "GUS_EIDURI_I";
Drop index "GUS_DCMMCD_FK_I";
Drop index "GUS_DCMNOM_I";
Drop index "GUS_CIDURI_I";
Drop index "GUS_ARC_DCM_I";
Drop index "GUS_FTE_ARC_I";

Drop index "GUS_MICROS_IMAGEN_I";
Drop index "GUS_MICROS_IMGCAM_I";
Drop index "GUS_MICROS_CSS_I";   
Drop index "GUS_NOTICS_IMAGEN_I";
Drop index "GUS_PREGUN_IMAGEN_I";
Drop index "GUS_COMPOS_IMGBUL_I";
Drop index "GUS_AGEIDI_DOCU_I";  
Drop index "GUS_AGEIDI_IMAGEN_I";
Drop index "GUS_MENU_IMGMEN_I";
Drop index "GUS_ARC_PATH_I";


Drop table "GUS_AUDITORIA" CASCADE CONSTRAINTS;
Drop table "GUS_W3C" CASCADE CONSTRAINTS;
Drop table "GUS_USURESP" CASCADE CONSTRAINTS;
Drop table "GUS_USUARIENC" CASCADE CONSTRAINTS;
Drop table "GUS_TPNOTI" CASCADE CONSTRAINTS;
Drop table "GUS_TPNIDI" CASCADE CONSTRAINTS;
Drop table "GUS_TIPSER" CASCADE CONSTRAINTS;
Drop table "GUS_TEMIDI" CASCADE CONSTRAINTS;
Drop table "GUS_TEMAS" CASCADE CONSTRAINTS;
Drop table "GUS_STATS" CASCADE CONSTRAINTS;
Drop table "GUS_RESPUS" CASCADE CONSTRAINTS;
Drop table "GUS_RESIDI" CASCADE CONSTRAINTS;
Drop table "GUS_PREIDI" CASCADE CONSTRAINTS;
Drop table "GUS_PREGUN" CASCADE CONSTRAINTS;
Drop table "GUS_NOTIDI" CASCADE CONSTRAINTS;
Drop table "GUS_NOTICS" CASCADE CONSTRAINTS;
Drop table "GUS_MUSUAR" CASCADE CONSTRAINTS;
Drop table "GUS_MNUIDI" CASCADE CONSTRAINTS;
Drop table "GUS_MICUSU" CASCADE CONSTRAINTS;
Drop table "GUS_MICROS" CASCADE CONSTRAINTS;
Drop table "GUS_MICIDI" CASCADE CONSTRAINTS;
Drop table "GUS_MENU" CASCADE CONSTRAINTS;
Drop table "GUS_LDISTRIBUCION_CORREO" CASCADE CONSTRAINTS;
Drop table "GUS_LDISTRIBUCION" CASCADE CONSTRAINTS;
Drop table "GUS_IDIOMA" CASCADE CONSTRAINTS;
Drop table "GUS_IDIMIC" CASCADE CONSTRAINTS;
Drop table "GUS_FRQSSI" CASCADE CONSTRAINTS;
Drop table "GUS_FRQIDI" CASCADE CONSTRAINTS;
Drop table "GUS_FRMLIN" CASCADE CONSTRAINTS;
Drop table "GUS_FRMIDI" CASCADE CONSTRAINTS;
Drop table "GUS_FRMCON" CASCADE CONSTRAINTS;
Drop table "GUS_FAQIDI" CASCADE CONSTRAINTS;
Drop table "GUS_FAQ" CASCADE CONSTRAINTS;
Drop table "GUS_ENCVOT" CASCADE CONSTRAINTS;
Drop table "GUS_ENCUST" CASCADE CONSTRAINTS;
Drop table "GUS_ENCIDI" CASCADE CONSTRAINTS;
Drop table "GUS_DOCUS" CASCADE CONSTRAINTS;
Drop table "GUS_DISTRIB_CONVOCATORIA" CASCADE CONSTRAINTS;
Drop table "GUS_CORREO" CASCADE CONSTRAINTS;
Drop table "GUS_CONVOCATORIA" CASCADE CONSTRAINTS;
Drop table "GUS_CONTEN" CASCADE CONSTRAINTS;
Drop table "GUS_CONIDI" CASCADE CONSTRAINTS;
Drop table "GUS_COMPOS" CASCADE CONSTRAINTS;
Drop table "GUS_CMPIDI" CASCADE CONSTRAINTS;
Drop table "GUS_AGENDA" CASCADE CONSTRAINTS;
Drop table "GUS_AGEIDI" CASCADE CONSTRAINTS;
Drop table "GUS_ACTIVI" CASCADE CONSTRAINTS;
Drop table "GUS_ACTIDI" CASCADE CONSTRAINTS;

Drop table "GUS_FR_TEMA" CASCADE CONSTRAINTS;
Drop table "GUS_FR_PLANTILLA" CASCADE CONSTRAINTS;
Drop table "GUS_FR_VERSION" CASCADE CONSTRAINTS;
Drop table "GUS_FR_PERPLA" CASCADE CONSTRAINTS;
Drop table "GUS_FR_ARCHIVO" CASCADE CONSTRAINTS;
Drop table "GUS_SOLRPD" CASCADE CONSTRAINTS;
Drop table "GUS_SOLJOB" CASCADE CONSTRAINTS;


Create table "GUS_ACTIDI" (
	"ATI_CODIDI" Varchar2 (6 CHAR) NOT NULL ,
	"ATI_ACTCOD" Number NOT NULL ,
	"ATI_NOMBRE" Varchar2 (150 CHAR),
primary key ("ATI_CODIDI","ATI_ACTCOD") 
) 
STORAGE(
	INITIAL 256K
	MAXEXTENTS 2147483645
	BUFFER_POOL DEFAULT
	)
LOGGING
;

Create table "GUS_ACTIVI" (
	"ACT_CODI" Number NOT NULL ,
	"ACT_MICCOD" Number NOT NULL ,
primary key ("ACT_CODI") 
) 
STORAGE(
	INITIAL 256K
	MAXEXTENTS 2147483645
	BUFFER_POOL DEFAULT
	)
LOGGING
;

Create table "GUS_AGEIDI" (
	"AID_CODIDI" Varchar2 (6 CHAR) NOT NULL ,
	"AID_AGECOD" Number NOT NULL ,
	"AID_DESCRI" Varchar2 (4000 CHAR),
	"AID_DOCU" Number,
	"AID_TITULO" Varchar2 (256 CHAR),
	"AID_URL" Varchar2 (256 CHAR),
	"AID_IMAGEN" Number,
	"AID_URLNOM" Varchar2 (512 CHAR),
primary key ("AID_CODIDI","AID_AGECOD") 
) 
STORAGE(
	INITIAL 768K
	MAXEXTENTS 2147483645
	BUFFER_POOL DEFAULT
	)
LOGGING
;

Create table "GUS_AGENDA" (
	"AGE_CODI" Number NOT NULL ,
	"AGE_MICCOD" Number NOT NULL ,
	"AGE_ACTIVI" Number NOT NULL ,
	"AGE_ORGANI" Varchar2 (256 CHAR),
	"AGE_INICIO" Date NOT NULL ,
	"AGE_FIN" Date,
	"AGE_VISIB" Varchar2 (1 CHAR) Default 'S'
,
primary key ("AGE_CODI") 
) 
STORAGE(
	INITIAL 256K
	MAXEXTENTS 2147483645
	BUFFER_POOL DEFAULT
	)
LOGGING
;

Create table "GUS_CMPIDI" (
	"CPI_CODIDI" Varchar2 (6 CHAR) NOT NULL ,
	"CPI_CMPCOD" Number NOT NULL ,
	"CPI_TITULO" Varchar2 (256 CHAR),
primary key ("CPI_CODIDI","CPI_CMPCOD") 
) 
STORAGE(
	INITIAL 256K
	MAXEXTENTS 2147483645
	BUFFER_POOL DEFAULT
	)
LOGGING
;

Create table "GUS_COMPOS" (
	"CMP_CODI" Number NOT NULL ,
	"CMP_TIPO" Number NOT NULL ,
	"CMP_MICCOD" Number NOT NULL ,
	"CMP_NOMBRE" Varchar2 (256 CHAR) NOT NULL ,
	"CMP_SOLOIM" Varchar2 (1 CHAR) Default 'N',
	"CMP_NUMELE" Number Default 0,
	"CMP_ORDEN" Number,
	"CMP_IMGBUL" Number,
	"CMP_FILAS" Varchar2 (1 CHAR) Default 'S',
	"CMP_PPLCOD" Number(19,0),
primary key ("CMP_CODI") 
) 
STORAGE(
	INITIAL 256K
	MAXEXTENTS 2147483645
	BUFFER_POOL DEFAULT
	)
LOGGING
;

Create table "GUS_CONIDI" (
	"CID_CODIDI" Varchar2 (6 CHAR) NOT NULL ,
	"CID_CONCOD" Number NOT NULL ,
	"CID_TITULO" Varchar2 (256 CHAR),
	"CID_TEXTO" Clob,
	"CID_URL" Varchar2 (512 CHAR),
	"CID_TXBETA" Clob,
	"CID_URI" Varchar2 (270 CHAR) NOT NULL ,
primary key ("CID_CODIDI","CID_CONCOD") 
) 
STORAGE(
	INITIAL 15360K
	MAXEXTENTS 2147483645
	BUFFER_POOL DEFAULT
	)
LOGGING
LOB("CID_TEXTO") STORE AS GUS_CONIDI_CID_TEXTO_LOB
	(CHUNK 8192)
LOB("CID_TXBETA") STORE AS GUS_CONIDI_CID_TXBETA_LOB
	(CHUNK 8192)
;

Create table "GUS_CONTEN" (
	"CON_CODI" Number NOT NULL ,
	"CON_CADUCA" Date,
	"CON_PUBLIC" Date NOT NULL ,
	"CON_VISIB" Varchar2 (1 CHAR) Default 'S',
	"CON_ORDEN" Number,
	"CON_MNUCOD" Number,
	"CON_IMGMEN" Number,
	"CON_PPLCOD" Number(19,0),
primary key ("CON_CODI") 
) 
STORAGE(
	INITIAL 512K
	MAXEXTENTS 2147483645
	BUFFER_POOL DEFAULT
	)
LOGGING
;

Create table "GUS_CONVOCATORIA" (
	"CODI" Number(19,0) NOT NULL ,
	"NOMBRE" Varchar2 (255),
	"DESCRIPCION" Varchar2 (255),
	"DATA_ENVIO" Date,
	"MICROSITE_ID" Number,
	"REENVIO_ERROR" Number(1,0),
	"ENCUESTA_ID" Number(19,0),
	"REENVIO_CONFIRMADOS" Number(1,0),
	"PREGUNTA_CORREO_ID" Number(19,0),
	"RESPUESTA_CORREO_ID" Number,
	"PREGUNTA_CONFIRMACION_ID" Number(19,0),
	"RESPUESTA_CONFIRMACION_ID" Number,
	"OTROS_DEST" Clob,
	"ASUNTO_MSG" Varchar2 (255),
	"TEXTO_MSG" Clob,
primary key ("CODI") 
) 
STORAGE(
	INITIAL 256K
	MAXEXTENTS 2147483645
	BUFFER_POOL DEFAULT
	)
LOGGING
LOB("OTROS_DEST") STORE AS 
	(CHUNK 8192)
LOB("TEXTO_MSG") STORE AS 
	(CHUNK 8192)
;

Create table "GUS_CORREO" (
	"CORREO" Varchar2 (255) NOT NULL ,
	"NOENVIAR" Number(1,0),
	"NOMBRE" Varchar2 (255),
	"APELLIDOS" Varchar2 (512),
	"ULTIMO_ENVIO" Date,
	"ERROR_ENVIO" Varchar2 (2000),
	"INTENTO_ENVIO" Number,
primary key ("CORREO") 
) 
STORAGE(
	INITIAL 256K
	MAXEXTENTS 2147483645
	BUFFER_POOL DEFAULT
	)
LOGGING
;

Create table "GUS_DISTRIB_CONVOCATORIA" (
	"CONVOCATORIA_ID" Number(19,0) NOT NULL ,
	"DISTRIB_ID" Number(19,0) NOT NULL ,
	"ULTIMO_ENVIO" Date,
	"ERROR_ENVIO" Varchar2 (255),
primary key ("CONVOCATORIA_ID","DISTRIB_ID") 
) 
STORAGE(
	INITIAL 256K
	MAXEXTENTS 2147483645
	BUFFER_POOL DEFAULT
	)
LOGGING
;

Create table "GUS_DOCUS" (
	"DCM_CODI" Number NOT NULL ,
	"DCM_MICCOD" Number,
	"DCM_DATOS" Blob,
	"DCM_NOMBRE" Varchar2 (256 CHAR),
	"DCM_TIPO" Varchar2 (256 CHAR),
	"DCM_TAMANO" Number,
	"DCM_PAGINA" Number,
primary key ("DCM_CODI") 
) 
STORAGE(
	INITIAL 14336K
	MAXEXTENTS 2147483645
	BUFFER_POOL DEFAULT
	)
LOGGING
LOB("DCM_DATOS") STORE AS GUS_DOCUS_DCM_DATOS_LOB
	(CHUNK 8192)
;

Create table "GUS_ENCIDI" (
	"EID_CODIDI" Varchar2 (6 CHAR) NOT NULL ,
	"EID_ENCCOD" Number NOT NULL ,
	"EID_TITULO" Varchar2 (256 CHAR),
	"EID_URI" Varchar2 (270 CHAR) NOT NULL ,
primary key ("EID_CODIDI","EID_ENCCOD") 
) 
STORAGE(
	INITIAL 256K
	MAXEXTENTS 2147483645
	BUFFER_POOL DEFAULT
	)
LOGGING
;

Create table "GUS_ENCUST" (
	"ENC_CODI" Number NOT NULL ,
	"ENC_MICCOD" Number NOT NULL ,
	"ENC_PUBLIC" Date,
	"ENC_CADUCA" Date,
	"ENC_VISIB" Varchar2 (1 CHAR) Default 'S',
	"ENC_INDIV" Varchar2 (1 CHAR) Default 'S',
	"ENC_PAGINA" Number,
	"ENC_MUESTR" Varchar2 (1 CHAR) Default 'S'
,
	"ENC_IDENTIF" Varchar2 (1),
primary key ("ENC_CODI") 
) 
STORAGE(
	INITIAL 256K
	MAXEXTENTS 2147483645
	BUFFER_POOL DEFAULT
	)
LOGGING
;

Create table "GUS_ENCVOT" (
	"VOT_CODI" Number NOT NULL ,
	"VOT_IDENCU" Number,
	"VOT_IDPREG" Number,
	"VOT_IDRESP" Number NOT NULL ,
	"VOT_INPUT" Varchar2 (2000),
	"VOT_CODUSU" Number(19,0),
primary key ("VOT_CODI") 
) 
STORAGE(
	INITIAL 256K
	MAXEXTENTS 2147483645
	BUFFER_POOL DEFAULT
	)
LOGGING
;

Create table "GUS_FAQ" (
	"FAQ_CODI" Number NOT NULL ,
	"FAQ_MICCOD" Number NOT NULL ,
	"FAQ_CODTEM" Number,
	"FAQ_FECHA" Date,
	"FAQ_VISIB" Varchar2 (1 CHAR) Default 'S'
,
primary key ("FAQ_CODI") 
) 
STORAGE(
	INITIAL 256K
	MAXEXTENTS 2147483645
	BUFFER_POOL DEFAULT
	)
LOGGING
;

Create table "GUS_FAQIDI" (
	"FID_CODIDI" Varchar2 (6 CHAR) NOT NULL ,
	"FID_FAQCOD" Number NOT NULL ,
	"FID_PREGUN" Varchar2 (1024 CHAR),
	"FID_RESPU" Varchar2 (4000 CHAR),
	"FID_URL" Varchar2 (1024 CHAR),
	"FID_URLNOM" Varchar2 (512 CHAR),
primary key ("FID_CODIDI","FID_FAQCOD") 
) 
STORAGE(
	INITIAL 256K
	MAXEXTENTS 2147483645
	BUFFER_POOL DEFAULT
	)
LOGGING
;

Create table "GUS_FRMCON" (
	"FRM_CODI" Number NOT NULL ,
	"FRM_MICCOD" Number,
	"FRM_EMAIL" Varchar2 (256 CHAR),
	"FRM_VISIB" Varchar2 (1 CHAR) Default 'S'
,
	"FRM_ANEXARCH" Varchar2 (1),
primary key ("FRM_CODI") 
) 
STORAGE(
	INITIAL 256K
	MAXEXTENTS 2147483645
	BUFFER_POOL DEFAULT
	)
LOGGING
;

Create table "GUS_FRMIDI" (
	"RID_CODIDI" Varchar2 (6 CHAR) NOT NULL ,
	"RID_FLICOD" Number NOT NULL ,
	"RID_TEXTO" Varchar2 (4000 CHAR),
primary key ("RID_CODIDI","RID_FLICOD") 
) 
STORAGE(
	INITIAL 512K
	MAXEXTENTS 2147483645
	BUFFER_POOL DEFAULT
	)
LOGGING
;

Create table "GUS_FRMLIN" (
	"FLI_CODI" Number NOT NULL ,
	"FLI_FRMCOD" Number NOT NULL ,
	"FLI_VISIB" Varchar2 (1 CHAR) Default 'S',
	"FLI_TAMANO" Number,
	"FLI_LINEAS" Number,
	"FLI_ORDEN" Number,
	"FLI_OBLIGA" Number,
	"FLI_TIPO" Varchar2 (1 CHAR) Default '2'
,
primary key ("FLI_CODI") 
) 
STORAGE(
	INITIAL 256K
	MAXEXTENTS 2147483645
	BUFFER_POOL DEFAULT
	)
LOGGING
;

Create table "GUS_FRQIDI" (
	"FQI_CODIDI" Varchar2 (6 CHAR) NOT NULL ,
	"FQI_FRQCOD" Number NOT NULL ,
	"FQI_NOMBRE" Varchar2 (100 CHAR),
primary key ("FQI_CODIDI","FQI_FRQCOD") 
) 
STORAGE(
	INITIAL 256K
	MAXEXTENTS 2147483645
	BUFFER_POOL DEFAULT
	)
LOGGING
;

Create table "GUS_FRQSSI" (
	"FRQ_CODI" Number NOT NULL ,
	"FRQ_MICCOD" Number,
	"FRQ_CENTRE" Varchar2 (25 CHAR),
	"FRQ_TPESCR" Varchar2 (25 CHAR),
primary key ("FRQ_CODI") 
) 
STORAGE(
	INITIAL 256K
	MAXEXTENTS 2147483645
	BUFFER_POOL DEFAULT
	)
LOGGING
;

Create table "GUS_IDIMIC" (
	"IMI_CODIDI" Varchar2 (6 CHAR) NOT NULL ,
	"IMI_MICCOD" Number NOT NULL ,
primary key ("IMI_CODIDI","IMI_MICCOD") 
) 
STORAGE(
	INITIAL 256K
	MAXEXTENTS 2147483645
	BUFFER_POOL DEFAULT
	)
LOGGING
;

Create table "GUS_IDIOMA" (
	"IDI_CODI" Varchar2 (2 CHAR) NOT NULL ,
	"IDI_ORDEN" Number(10,0) NOT NULL ,
	"IDI_CODEST" Varchar2 (128 CHAR),
	"IDI_NOMBRE" Varchar2 (128 CHAR),
	"IDI_TRADUCTOR" Varchar2 (128 CHAR),
primary key ("IDI_CODI") 
) 
STORAGE(
	INITIAL 256K
	MAXEXTENTS 2147483645
	BUFFER_POOL DEFAULT
	)
LOGGING
;

Create table "GUS_LDISTRIBUCION" (
	"CODI" Number(19,0) NOT NULL ,
	"NOMBRE" Varchar2 (255),
	"DESCRIPCION" Varchar2 (255),
	"MICROSITE_ID" Number,
	"PUBLICO" Number(1,0),
primary key ("CODI") 
) 
STORAGE(
	INITIAL 256K
	MAXEXTENTS 2147483645
	BUFFER_POOL DEFAULT
	)
LOGGING
;

Create table "GUS_LDISTRIBUCION_CORREO" (
	"LISTADISTRIB_ID" Number(19,0) NOT NULL ,
	"CORREO_ID" Varchar2 (255) NOT NULL ,
primary key ("LISTADISTRIB_ID","CORREO_ID") 
) 
STORAGE(
	INITIAL 256K
	MAXEXTENTS 2147483645
	BUFFER_POOL DEFAULT
	)
LOGGING
;

Create table "GUS_MENU" (
	"MNU_CODI" Number NOT NULL ,
	"MNU_MICCOD" Number NOT NULL ,
	"MNU_ORDEN" Number Default 0,
	"MNU_IMGMEN" Number,
	"MNU_PADRE" Number Default 0                      NOT NULL ,
	"MNU_VISIB" Varchar2 (1 CHAR) Default 'S',
	"MNU_MODO" Varchar2 (1 CHAR) Default 'F'
,
primary key ("MNU_CODI") 
) 
STORAGE(
	INITIAL 256K
	MAXEXTENTS 2147483645
	BUFFER_POOL DEFAULT
	)
LOGGING
;

Create table "GUS_MICIDI" (
	"MID_CODIDI" Varchar2 (6 CHAR) NOT NULL ,
	"MID_MICCOD" Number NOT NULL ,
	"MID_TXTOP1" Varchar2 (64 CHAR),
	"MID_URLOP1" Varchar2 (256 CHAR),
	"MID_TXTOP2" Varchar2 (64 CHAR),
	"MID_URLOP2" Varchar2 (256 CHAR),
	"MID_TXTOP3" Varchar2 (64 CHAR),
	"MID_URLOP3" Varchar2 (256 CHAR),
	"MID_TXTOP4" Varchar2 (64 CHAR),
	"MID_URLOP4" Varchar2 (256 CHAR),
	"MID_TXTOP5" Varchar2 (64 CHAR),
	"MID_URLOP5" Varchar2 (256 CHAR),
	"MID_TXTOP6" Varchar2 (64 CHAR),
	"MID_URLOP6" Varchar2 (256 CHAR),
	"MID_TXTOP7" Varchar2 (64 CHAR),
	"MID_URLOP7" Varchar2 (256 CHAR),
	"MID_TITCAM" Varchar2 (256 CHAR),
	"MID_SUBTCA" Varchar2 (256 CHAR),
	"MID_CABPER" Varchar2 (4000 CHAR),
	"MID_PIEPER" Varchar2 (4000 CHAR),
	"MID_TITULO" Varchar2 (256 CHAR),
	"MID_KEYWORDS" Varchar2 (1000 CHAR),
	"MID_DESCRIPTION" Varchar2 (4000 CHAR),
primary key ("MID_CODIDI","MID_MICCOD") 
) 
STORAGE(
	INITIAL 256K
	MAXEXTENTS 2147483645
	BUFFER_POOL DEFAULT
	)
LOGGING
;

Create table "GUS_MICROS" (
	"MIC_CODI" Number NOT NULL ,
	"MIC_CODUNI" Number,
	"MIC_FECHA" Date Default sysdate,
	"MIC_VISIB" Varchar2 (1 CHAR) Default 'S'                    NOT NULL ,
	"MIC_IMAGEN" Number,
	"MIC_MENU" Varchar2 (1 CHAR) Default 'N'                    NOT NULL ,
	"MIC_PLANTI" Varchar2 (1 CHAR) Default '1',
	"MIC_OPFAQ" Varchar2 (1 CHAR) Default 'N',
	"MIC_OPMAPA" Varchar2 (1 CHAR) Default 'N',
	"MIC_OPCONT" Varchar2 (1 CHAR) Default 'N',
	"MIC_OPT1" Varchar2 (1 CHAR) Default 'N',
	"MIC_OPT2" Varchar2 (1 CHAR) Default 'N',
	"MIC_OPT3" Varchar2 (1 CHAR) Default 'N',
	"MIC_OPT4" Varchar2 (1 CHAR) Default 'N',
	"MIC_OPT5" Varchar2 (1 CHAR) Default 'N',
	"MIC_OPT6" Varchar2 (1 CHAR) Default 'N',
	"MIC_OPT7" Varchar2 (1 CHAR) Default 'N',
	"MIC_URLHOM" Varchar2 (512 CHAR),
	"MIC_IMGCAM" Number,
	"MIC_RESTRI" Varchar2 (1 CHAR) Default 'N',
	"MIC_ROL" Varchar2 (150 CHAR),
	"MIC_SERSEL" Varchar2 (1024 CHAR),
	"MIC_URLCAM" Varchar2 (512 CHAR),
	"MIC_SEROFR" Varchar2 (1024 CHAR),
	"MIC_CSS" Number,
	"MIC_CAB" Varchar2 (1 CHAR),
	"MIC_PIE" Varchar2 (1 CHAR),
	"MIC_NUMNOT" Number,
	"MIC_BUSCA" Varchar2 (1 CHAR) Default 'N',
	"MIC_CLAVE" Varchar2 (150 CHAR) Constraint "UQ_GUS_MICROS_1" UNIQUE ,
	"MIC_MNUCRP" Varchar2 (1 CHAR) Default 'N',
	"MIC_CSSPTR" Varchar2 (1 CHAR),
	"MIC_DOMINI" Varchar2 (150),
	"MIC_URI" Varchar2 (32 CHAR) NOT NULL Constraint "UQ_GUS_MICROS_2" UNIQUE ,
	"MIC_ANALYTICS" Varchar2 (60 CHAR),
	"MIC_VERSION" Varchar2 (2),
	"MIC_TIPO_ACCESO" Varchar2 (1),
	"MIC_FTECOD" Number(19,0),
	"MIC_DESARROLLO" Varchar2 (1) Default 'N' NOT NULL ,
primary key ("MIC_CODI") 
) 
STORAGE(
	INITIAL 256K
	MAXEXTENTS 2147483645
	BUFFER_POOL DEFAULT
	)
LOGGING
;

Create table "GUS_MICUSU" (
	"MIU_CODUSU" Number(19,0) NOT NULL ,
	"MIU_CODMIC" Number NOT NULL ,
primary key ("MIU_CODUSU","MIU_CODMIC") 
) 
STORAGE(
	INITIAL 256K
	MAXEXTENTS 2147483645
	BUFFER_POOL DEFAULT
	)
LOGGING
;

Create table "GUS_MNUIDI" (
	"MDI_MNUCOD" Number NOT NULL ,
	"MDI_CODIDI" Varchar2 (6 CHAR) NOT NULL ,
	"MDI_NOMBRE" Varchar2 (256 CHAR),
primary key ("MDI_MNUCOD","MDI_CODIDI") 
) 
STORAGE(
	INITIAL 512K
	MAXEXTENTS 2147483645
	BUFFER_POOL DEFAULT
	)
LOGGING
;

Create table "GUS_MUSUAR" (
	"MSU_CODI" Number(19,0) NOT NULL ,
	"MSU_USERNA" Varchar2 (128 CHAR) NOT NULL  Constraint "GUS_MUS_UNI" UNIQUE ,
	"MSU_PASSWO" Varchar2 (128 CHAR),
	"MSU_NOMBRE" Varchar2 (256 CHAR),
	"MSU_OBSERV" Varchar2 (4000 CHAR),
	"MSU_PERFIL" Varchar2 (64 CHAR),
    "MSU_PERMIS" Number(19,0),
primary key ("MSU_CODI") 
) 
STORAGE(
	INITIAL 256K
	MAXEXTENTS 2147483645
	BUFFER_POOL DEFAULT
	)
LOGGING
;

Create table "GUS_NOTICS" (
	"NOT_CODI" Number NOT NULL ,
	"NOT_MICCOD" Number NOT NULL ,
	"NOT_IMAGEN" Number,
	"NOT_CADUCA" Date,
	"NOT_PUBLIC" Date,
	"NOT_VISIB" Varchar2 (1 CHAR) Default 'S',
	"NOT_VISWEB" Varchar2 (1 CHAR) Default 'N',
	"NOT_TIPO" Number NOT NULL ,
	"NOT_ORDEN" Number,
	"NOT_LATITUD" Varchar2 (100 CHAR),
	"NOT_LONGITUD" Varchar2 (100 CHAR),
	"NOT_ICOCOLOR" Varchar2 (20 CHAR),
primary key ("NOT_CODI") 
) 
STORAGE(
	INITIAL 768K
	MAXEXTENTS 2147483645
	BUFFER_POOL DEFAULT
	)
LOGGING
;

Create table "GUS_NOTIDI" (
	"NID_CODIDI" Varchar2 (6 CHAR) NOT NULL ,
	"NID_NOTCOD" Number NOT NULL ,
	"NID_TITULO" Varchar2 (512 CHAR),
	"NID_SUBTIT" Varchar2 (256 CHAR),
	"NID_FUENTE" Varchar2 (100 CHAR),
	"NID_URL" Varchar2 (512 CHAR),
	"NID_DOCU" Number,
	"NID_TEXTO" Clob,
	"NID_URLNOM" Varchar2 (512 CHAR),
	"NID_URI" Varchar2 (530 CHAR) NOT NULL ,
primary key ("NID_CODIDI","NID_NOTCOD") 
) 
STORAGE(
	INITIAL 8448K
	MAXEXTENTS 2147483645
	BUFFER_POOL DEFAULT
	)
LOGGING
;

Create table "GUS_PREGUN" (
	"PRE_CODI" Number NOT NULL ,
	"PRE_ENCCOD" Number,
	"PRE_IMAGEN" Number,
	"PRE_MULTIR" Varchar2 (1 CHAR) Default 'S',
	"PRE_VISCMP" Varchar2 (1 CHAR) Default 'S',
	"PRE_OBLIGA" Varchar2 (1 CHAR) Default 'S',
	"PRE_VISIB" Varchar2 (1 CHAR) Default 'S',
	"PRE_ORDEN" Number,
	"PRE_NRESP" Number,
	"PRE_MINCONT" Number(10,0),
	"PRE_MAXCONT" Number(10,0),
primary key ("PRE_CODI") 
) 
STORAGE(
	INITIAL 256K
	MAXEXTENTS 2147483645
	BUFFER_POOL DEFAULT
	)
LOGGING
;

Create table "GUS_PREIDI" (
	"PID_CODIDI" Varchar2 (6 CHAR) NOT NULL ,
	"PID_PRECOD" Number NOT NULL ,
	"PID_TITULO" Varchar2 (256 CHAR),
primary key ("PID_CODIDI","PID_PRECOD") 
) 
STORAGE(
	INITIAL 256K
	MAXEXTENTS 2147483645
	BUFFER_POOL DEFAULT
	)
LOGGING
;

Create table "GUS_RESIDI" (
	"REI_CODIDI" Varchar2 (6 CHAR) NOT NULL ,
	"REI_RESCOD" Number NOT NULL ,
	"REI_TITULO" Varchar2 (256 CHAR),
primary key ("REI_CODIDI","REI_RESCOD") 
) 
STORAGE(
	INITIAL 512K
	MAXEXTENTS 2147483645
	BUFFER_POOL DEFAULT
	)
LOGGING
;

Create table "GUS_RESPUS" (
	"RES_CODI" Number NOT NULL ,
	"RES_PRECOD" Number,
	"RES_ENTRAD" Varchar2 (256 CHAR),
	"RES_ORDEN" Number,
	"RES_NRESP" Number,
	"RES_TIPO" Varchar2 (1 CHAR),
primary key ("RES_CODI") 
) 
STORAGE(
	INITIAL 256K
	MAXEXTENTS 2147483645
	BUFFER_POOL DEFAULT
	)
LOGGING
;

Create table "GUS_STATS" (
	"STA_CODI" Number NOT NULL ,
	"STA_ITEM" Number NOT NULL ,
	"STA_MES" Number NOT NULL ,
	"STA_REF" Varchar2 (5 CHAR) NOT NULL ,
	"STA_NACCES" Number NOT NULL ,
	"STA_MICCOD" Number NOT NULL ,
	"STA_PUB" Number Default 1                      NOT NULL ,
primary key ("STA_CODI") 
) 
STORAGE(
	INITIAL 11776K
	MAXEXTENTS 2147483645
	BUFFER_POOL DEFAULT
	)
LOGGING
;

Create table "GUS_TEMAS" (
	"TEM_CODI" Number NOT NULL ,
	"TEM_MICCOD" Number NOT NULL ,
primary key ("TEM_CODI") 
) 
STORAGE(
	INITIAL 256K
	MAXEXTENTS 2147483645
	BUFFER_POOL DEFAULT
	)
LOGGING
;

Create table "GUS_TEMIDI" (
	"TID_CODIDI" Varchar2 (6 CHAR) NOT NULL ,
	"TID_TEMCOD" Number NOT NULL ,
	"TID_NOMBRE" Varchar2 (100 CHAR),
primary key ("TID_CODIDI","TID_TEMCOD") 
) 
STORAGE(
	INITIAL 256K
	MAXEXTENTS 2147483645
	BUFFER_POOL DEFAULT
	)
LOGGING
;

Create table "GUS_TIPSER" (
	"TPS_CODI" Number NOT NULL ,
	"TPS_NOMBRE" Varchar2 (64 CHAR) NOT NULL ,
	"TPS_VISIB" Varchar2 (1 CHAR) Default 'S'                    NOT NULL ,
	"TPS_REF" Varchar2 (5 CHAR) NOT NULL ,
	"TPS_URL" Varchar2 (64 CHAR),
	"TPS_TIPO" Varchar2 (1 CHAR),
primary key ("TPS_CODI") 
) 
STORAGE(
	INITIAL 256K
	MAXEXTENTS 2147483645
	BUFFER_POOL DEFAULT
	)
LOGGING
;

Create table "GUS_TPNIDI" (
	"TPI_CODIDI" Varchar2 (6 CHAR) NOT NULL ,
	"TPI_TIPCOD" Number NOT NULL ,
	"TPI_NOMBRE" Varchar2 (100 CHAR),
	"TPI_URI" Varchar2 (125) NOT NULL ,
primary key ("TPI_CODIDI","TPI_TIPCOD") 
) 
STORAGE(
	INITIAL 256K
	MAXEXTENTS 2147483645
	BUFFER_POOL DEFAULT
	)
LOGGING
;

Create table "GUS_TPNOTI" (
	"TPN_CODI" Number NOT NULL ,
	"TPN_MICCOD" Number,
	"TPN_TAMPAG" Number Default 0,
	"TPN_TIPPAG" Varchar2 (1 CHAR) Default '0',
	"TPN_TPELEM" Varchar2 (1 CHAR) Default '0',
	"TPN_BUSCAR" Varchar2 (1 CHAR) Default 'N',
	"TPN_ORDEN" Varchar2 (1 CHAR) Default '1',
	"TPN_XURL" Varchar2 (1000 CHAR),
	"TPN_XJNDI" Varchar2 (500 CHAR),
	"TPN_XLOCAL" Varchar2 (1 CHAR),
	"TPN_XAUTH" Varchar2 (1 CHAR),
	"TPN_XUSR" Varchar2 (50 CHAR),
	"TPN_XPWD" Varchar2 (50 CHAR),
	"TPN_XID" Varchar2 (50 CHAR),
	"TPN_CLASIF" Varchar2 (100 CHAR),
	"TPN_FOTOSPORFILA" Number Default 3,
	"TPN_PPLCOD" Number(19,0),
primary key ("TPN_CODI") 
) 
STORAGE(
	INITIAL 256K
	MAXEXTENTS 2147483645
	BUFFER_POOL DEFAULT
	)
LOGGING
;

Create table "GUS_USUARIENC" (
	"USE_CODI" Number(19,0) NOT NULL ,
	"USE_NOMBRE" Varchar2 (256 CHAR) Default 'anonim'  NOT NULL ,
	"USE_OBSERV" Varchar2 (4000 CHAR),
	"USE_DNI" Varchar2 (10),
primary key ("USE_CODI") 
) 
STORAGE(
	INITIAL 256K
	MAXEXTENTS 2147483645
	BUFFER_POOL DEFAULT
	)
LOGGING
;

Create table "GUS_USURESP" (
	"USR_CODUSU" Number(19,0) NOT NULL ,
	"USR_CODRESP" Number NOT NULL ,
primary key ("USR_CODUSU","USR_CODRESP") 
) 
STORAGE(
	INITIAL 256K
	MAXEXTENTS 2147483645
	BUFFER_POOL DEFAULT
	)
LOGGING
;

Create table "GUS_W3C" (
	"W3C_SERVIC" Varchar2 (25 CHAR),
	"W3C_RESULT" Varchar2 (1 CHAR),
	"W3C_MENSA" Varchar2 (2000 CHAR),
	"W3C_MESURA" Varchar2 (1 CHAR),
	"W3C_CODI" Number NOT NULL ,
	"W3C_IDITEM" Number,
	"W3C_IDIOMA" Varchar2 (2 CHAR),
	"W3C_MICCOD" Number,
	"W3C_TAWRES" Number,
	"W3C_TAWMEN" Varchar2 (2000 CHAR)
) 
STORAGE(
	INITIAL 4096K
	MAXEXTENTS 2147483645
	BUFFER_POOL DEFAULT
	)
LOGGING
;

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
;

Create table "GUS_FR_TEMA" (
	"FTE_CODI" Number(19,0) NOT NULL ,
	"FTE_VERSION" Varchar2 (2) NOT NULL ,
	"FTE_TEMA_PADRE" Number(19,0) ,
	"FTE_NOMBRE" Varchar2 (255) NOT NULL  UNIQUE ,
	"FTE_CSS" Number,
	"FTE_ACTUALIZACION" Date Default SYSDATE NOT NULL ,
	"FTE_URI" Varchar2 (32 CHAR) NOT NULL  UNIQUE ,
 Constraint "GUS_FTE_PK" primary key ("FTE_CODI") 
);

Create table "GUS_FR_PLANTILLA" (
	"PLA_CODI" Number(19,0) NOT NULL ,
	"PLA_VERSION" Varchar2 (2) NOT NULL ,
	"PLA_NOMBRE" Varchar2 (255) NOT NULL  UNIQUE ,
	"PLA_DESCRIPCION" Clob,
	"PLA_TITULO" VARCHAR2(255) NOT NULL,
 Constraint "GUS_PLA_PK" primary key ("PLA_CODI") 
);

Create table "GUS_FR_VERSION" (
	"FVE_VERSION" Varchar2 (2) NOT NULL ,
	"FVE_NOMBRE" Varchar2 (255) NOT NULL  UNIQUE ,
 Constraint "GUS_FVE_PK" primary key ("FVE_VERSION") 
);

Create table "GUS_FR_PERPLA" (
	"PPL_CODI" Number(19,0) NOT NULL ,
	"PPL_TITULO" Varchar2 (255) NOT NULL ,
	"PPL_PLACOD" Number(19,0) NOT NULL ,
	"PPL_ORDEN" Integer,
	"PPL_FTECOD" Number(19,0),
	"PPL_MICCOD" Number,
	"PPL_CONTENIDO" Clob,
 Constraint "GUS_PLT_PK" primary key ("PPL_CODI") 
);

Create table "GUS_FR_ARCHIVO" (
	"ARC_CODI" Number NOT NULL ,
	"ARC_FTECOD" Number(19,0) NOT NULL ,
	"ARC_PATH" Varchar2 (256 CHAR),
	"ARC_DOCCOD" Number,
 Constraint "GUS_ARC_PK" primary key ("ARC_CODI") 
);

  
--INTRODUCIR SOLRPD.
CREATE TABLE "GUS_SOLRPD"
  (
    "SLP_ID"     NUMBER NOT NULL ENABLE,
    "SLP_TIPO"   VARCHAR2(128 CHAR),
    "SLP_IDELEM" NUMBER,
    "SLP_ACCION" NUMBER,
    "SLP_FECCRE" DATE,
    "SLP_FECIDX" DATE,
    "SLP_RESULT"    NUMBER,
    "SLP_TXTERR"    VARCHAR2(300 CHAR),
    "SLP_IDARCHIVO" NUMBER,
    CONSTRAINT "GUS_SOLRPD_PK" PRIMARY KEY ("SLP_ID")
  );


--INTRODUCIR SOLR JOB.
CREATE TABLE "GUS_SOLJOB"
  (
    "JOB_ID" NUMBER(19,0) NOT NULL ENABLE,
    "JOB_FECINI" DATE DEFAULT SYSDATE,
    "JOB_FECFIN" DATE,
    "JOB_TIPO"   VARCHAR2(20),
    "JOB_DESCRI" clob,
    "JOB_IDELEM" NUMBER(19,0),
    CONSTRAINT "RSC_JOB_PK" PRIMARY KEY ("JOB_ID")
  );

  
-- Create Indexes section
Create Index "GUS_CIDURI_I"    ON "GUS_CONIDI" ("CID_CODIDI","CID_URI") ;
Create Index "GUS_DCMMCD_FK_I" ON "GUS_DOCUS"  ("DCM_MICCOD") ;
Create Index "GUS_DCMNOM_I"    ON "GUS_DOCUS"  ("DCM_NOMBRE") ;
Create Index "GUS_EIDURI_I"    ON "GUS_ENCIDI" ("EID_CODIDI","EID_URI") ;
Create Index "GUS_NIDURI_I"    ON "GUS_NOTIDI" ("NID_CODIDI","NID_URI") ;
Create Index "GUS_STAITM_I"    ON "GUS_STATS"  ("STA_ITEM") ;
Create Index "GUS_STAMCD_FK_I" ON "GUS_STATS"  ("STA_MICCOD") ;
Create Index "GUS_STAMES_I"    ON "GUS_STATS"  ("STA_MES") ;
Create Index "GUS_STAREF_I"    ON "GUS_STATS"  ("STA_REF") ;
Create Index "GUS_TPNURI_I"    ON "GUS_TPNIDI" ("TPI_CODIDI","TPI_URI") ;
Create Index "GUS_AUDMIC_I"    ON "GUS_AUDITORIA" ("AUD_MICCOD") ;
Create UNIQUE Index "GUS_ARC_PATH_I" ON "GUS_FR_ARCHIVO" ("ARC_FTECOD","ARC_PATH") ;


-- Create Foreign keys section
Alter table "GUS_ACTIDI"               add  foreign key ("ATI_ACTCOD")      references "GUS_ACTIVI" ("ACT_CODI") ;
Alter table "GUS_AGENDA"               add  foreign key ("AGE_ACTIVI")      references "GUS_ACTIVI" ("ACT_CODI") ;
Alter table "GUS_AGEIDI"               add  foreign key ("AID_AGECOD")      references "GUS_AGENDA" ("AGE_CODI") ;
Alter table "GUS_CMPIDI"               add  foreign key ("CPI_CMPCOD")      references "GUS_COMPOS" ("CMP_CODI") ;
Alter table "GUS_CONIDI"               add  foreign key ("CID_CONCOD")      references "GUS_CONTEN" ("CON_CODI") ;
Alter table "GUS_LDISTRIBUCION_CORREO" add  foreign key ("CORREO_ID")       references "GUS_CORREO" ("CORREO") ;
Alter table "GUS_AGEIDI"               add  foreign key ("AID_DOCU")        references "GUS_DOCUS" ("DCM_CODI") ;
Alter table "GUS_AGEIDI"               add  foreign key ("AID_IMAGEN")      references "GUS_DOCUS" ("DCM_CODI") ;
Alter table "GUS_COMPOS"               add  foreign key ("CMP_IMGBUL")      references "GUS_DOCUS" ("DCM_CODI") ;
Alter table "GUS_CONTEN"               add  foreign key ("CON_IMGMEN")      references "GUS_DOCUS" ("DCM_CODI") ;
Alter table "GUS_MENU"                 add  foreign key ("MNU_IMGMEN")      references "GUS_DOCUS" ("DCM_CODI") ;
Alter table "GUS_MICROS"               add  foreign key ("MIC_IMAGEN")      references "GUS_DOCUS" ("DCM_CODI") ;
Alter table "GUS_MICROS"               add  foreign key ("MIC_IMGCAM")      references "GUS_DOCUS" ("DCM_CODI") ;
Alter table "GUS_MICROS"               add  foreign key ("MIC_CSS")         references "GUS_DOCUS" ("DCM_CODI") ;
Alter table "GUS_NOTICS"               add  foreign key ("NOT_IMAGEN")      references "GUS_DOCUS" ("DCM_CODI") ;
Alter table "GUS_NOTIDI"               add  foreign key ("NID_DOCU")        references "GUS_DOCUS" ("DCM_CODI") ;
Alter table "GUS_PREGUN"               add  foreign key ("PRE_IMAGEN")      references "GUS_DOCUS" ("DCM_CODI") ;
Alter table "GUS_CONVOCATORIA"         add  foreign key ("ENCUESTA_ID")     references "GUS_ENCUST" ("ENC_CODI") ;
Alter table "GUS_ENCIDI"               add  foreign key ("EID_ENCCOD")      references "GUS_ENCUST" ("ENC_CODI") ;
Alter table "GUS_PREGUN"               add  foreign key ("PRE_ENCCOD")      references "GUS_ENCUST" ("ENC_CODI") ;
Alter table "GUS_FAQIDI"               add  foreign key ("FID_FAQCOD")      references "GUS_FAQ" ("FAQ_CODI") ;
Alter table "GUS_FRMLIN"               add  foreign key ("FLI_FRMCOD")      references "GUS_FRMCON" ("FRM_CODI") ;
Alter table "GUS_FRMIDI"               add  foreign key ("RID_FLICOD")      references "GUS_FRMLIN" ("FLI_CODI") ;
Alter table "GUS_FRQIDI"               add  foreign key ("FQI_FRQCOD")      references "GUS_FRQSSI" ("FRQ_CODI") ;
Alter table "GUS_LDISTRIBUCION_CORREO" add  foreign key ("LISTADISTRIB_ID") references "GUS_LDISTRIBUCION" ("CODI") ;
Alter table "GUS_CONTEN"               add  foreign key ("CON_MNUCOD")      references "GUS_MENU" ("MNU_CODI") ;
Alter table "GUS_MNUIDI"               add  foreign key ("MDI_MNUCOD")      references "GUS_MENU" ("MNU_CODI") ;
Alter table "GUS_ACTIVI"               add  foreign key ("ACT_MICCOD")      references "GUS_MICROS" ("MIC_CODI") ;
Alter table "GUS_AGENDA"               add  foreign key ("AGE_MICCOD")      references "GUS_MICROS" ("MIC_CODI") ;
Alter table "GUS_COMPOS"               add  foreign key ("CMP_MICCOD")      references "GUS_MICROS" ("MIC_CODI") ;
Alter table "GUS_CONVOCATORIA"         add  foreign key ("MICROSITE_ID")    references "GUS_MICROS" ("MIC_CODI") ;
Alter table "GUS_ENCUST"               add  foreign key ("ENC_MICCOD")      references "GUS_MICROS" ("MIC_CODI") ;
Alter table "GUS_FAQ"                  add  foreign key ("FAQ_MICCOD")      references "GUS_MICROS" ("MIC_CODI") ;
Alter table "GUS_FRMCON"               add  foreign key ("FRM_MICCOD")      references "GUS_MICROS" ("MIC_CODI") ;
Alter table "GUS_FRQSSI"               add  foreign key ("FRQ_MICCOD")      references "GUS_MICROS" ("MIC_CODI") ;
Alter table "GUS_IDIMIC"               add  foreign key ("IMI_MICCOD")      references "GUS_MICROS" ("MIC_CODI") ;
Alter table "GUS_LDISTRIBUCION"        add  foreign key ("MICROSITE_ID")    references "GUS_MICROS" ("MIC_CODI") ;
Alter table "GUS_MENU"                 add  foreign key ("MNU_MICCOD")      references "GUS_MICROS" ("MIC_CODI") ;
Alter table "GUS_MICIDI"               add  foreign key ("MID_MICCOD")      references "GUS_MICROS" ("MIC_CODI") ;
Alter table "GUS_MICUSU"               add  foreign key ("MIU_CODMIC")      references "GUS_MICROS" ("MIC_CODI") ;
Alter table "GUS_NOTICS"               add  foreign key ("NOT_MICCOD")      references "GUS_MICROS" ("MIC_CODI") ;
Alter table "GUS_TEMAS"                add  foreign key ("TEM_MICCOD")      references "GUS_MICROS" ("MIC_CODI") ;
Alter table "GUS_TPNOTI"               add  foreign key ("TPN_MICCOD")      references "GUS_MICROS" ("MIC_CODI") ;
Alter table "GUS_AUDITORIA"            add  foreign key ("AUD_MICCOD")      references "GUS_MICROS" ("MIC_CODI")  on delete set null;
Alter table "GUS_MICUSU"               add  foreign key ("MIU_CODUSU")      references "GUS_MUSUAR" ("MSU_CODI") ;
Alter table "GUS_NOTIDI"               add  foreign key ("NID_NOTCOD")      references "GUS_NOTICS" ("NOT_CODI") ;
Alter table "GUS_CONVOCATORIA"         add  foreign key ("PREGUNTA_CONFIRMACION_ID") references "GUS_PREGUN" ("PRE_CODI") ;
Alter table "GUS_CONVOCATORIA"         add  foreign key ("PREGUNTA_CORREO_ID") references "GUS_PREGUN" ("PRE_CODI") ;
Alter table "GUS_PREIDI"               add  foreign key ("PID_PRECOD")          references "GUS_PREGUN" ("PRE_CODI") ;
Alter table "GUS_RESPUS"               add  foreign key ("RES_PRECOD")          references "GUS_PREGUN" ("PRE_CODI") ;
Alter table "GUS_CONVOCATORIA"         add  foreign key ("RESPUESTA_CONFIRMACION_ID") references "GUS_RESPUS" ("RES_CODI") ;
Alter table "GUS_ENCVOT"               add  foreign key ("VOT_IDRESP")          references "GUS_RESPUS" ("RES_CODI") ;
Alter table "GUS_RESIDI"               add  foreign key ("REI_RESCOD")          references "GUS_RESPUS" ("RES_CODI") ;
Alter table "GUS_USURESP"              add  foreign key ("USR_CODRESP")         references "GUS_RESPUS" ("RES_CODI") ;
Alter table "GUS_CONVOCATORIA"         add  foreign key ("RESPUESTA_CORREO_ID") references "GUS_RESPUS" ("RES_CODI") ;
Alter table "GUS_FAQ"                  add  foreign key ("FAQ_CODTEM")          references "GUS_TEMAS" ("TEM_CODI") ;
Alter table "GUS_TEMIDI"               add  foreign key ("TID_TEMCOD")          references "GUS_TEMAS" ("TEM_CODI") ;
Alter table "GUS_COMPOS"               add  foreign key ("CMP_TIPO")            references "GUS_TPNOTI" ("TPN_CODI") ;
Alter table "GUS_NOTICS"               add  foreign key ("NOT_TIPO")            references "GUS_TPNOTI" ("TPN_CODI") ;
Alter table "GUS_TPNIDI"               add  foreign key ("TPI_TIPCOD")          references "GUS_TPNOTI" ("TPN_CODI") ;
Alter table "GUS_USURESP"              add  foreign key ("USR_CODUSU")          references "GUS_USUARIENC" ("USE_CODI") ;



Create Index "GUS_FTE_DOC_I" ON "GUS_FR_TEMA" ("FTE_CSS");
Alter table "GUS_FR_TEMA" add Constraint "GUS_FTE_DOC_FK" foreign key ("FTE_CSS") references "GUS_DOCUS" ("DCM_CODI") ;
Create Index "GUS_ARC_DCM_I" ON "GUS_FR_ARCHIVO" ("ARC_DOCCOD");
Alter table "GUS_FR_ARCHIVO" add Constraint "GUS_ARC_DCM_FK" foreign key ("ARC_DOCCOD") references "GUS_DOCUS" ("DCM_CODI") ;
Create Index "GUS_MIC_PLT_I" ON "GUS_FR_PERPLA" ("PPL_MICCOD");
Alter table "GUS_FR_PERPLA" add Constraint "GUS_MIC_PLT_FK" foreign key ("PPL_MICCOD") references "GUS_MICROS" ("MIC_CODI") ;
Create Index "GUS_FTE_FTE_I" ON "GUS_FR_TEMA" ("FTE_TEMA_PADRE");
Alter table "GUS_FR_TEMA" add Constraint "GUS_FTE_FTE_FK" foreign key ("FTE_TEMA_PADRE") references "GUS_FR_TEMA" ("FTE_CODI") ;
Create Index "GUS_FTE_PLT_I" ON "GUS_FR_PERPLA" ("PPL_FTECOD");
Alter table "GUS_FR_PERPLA" add Constraint "GUS_FTE_PLT_FK" foreign key ("PPL_FTECOD") references "GUS_FR_TEMA" ("FTE_CODI") ;
Create Index "GUS_FTE_ARC_I" ON "GUS_FR_ARCHIVO" ("ARC_FTECOD");
Alter table "GUS_FR_ARCHIVO" add Constraint "GUS_FTE_ARC_FK" foreign key ("ARC_FTECOD") references "GUS_FR_TEMA" ("FTE_CODI") ;
Create Index "GUS_FTE_MIC_I" ON "GUS_MICROS" ("MIC_FTECOD");
Alter table "GUS_MICROS" add Constraint "GUS_FTE_MIC_FK" foreign key ("MIC_FTECOD") references "GUS_FR_TEMA" ("FTE_CODI") ;
Create Index "GUS_PLA_PLT_I" ON "GUS_FR_PERPLA" ("PPL_PLACOD");
Alter table "GUS_FR_PERPLA" add Constraint "GUS_PLA_PLT_FK" foreign key ("PPL_PLACOD") references "GUS_FR_PLANTILLA" ("PLA_CODI"); 
Create Index "GUS_FVE_FAR_I" ON "GUS_FR_PLANTILLA" ("PLA_VERSION");
Alter table "GUS_FR_PLANTILLA" add Constraint "GUS_FVE_FAR_FK" foreign key ("PLA_VERSION") references "GUS_FR_VERSION" ("FVE_VERSION") ;
Create Index "GUS_FVE_FTE_I" ON "GUS_FR_TEMA" ("FTE_VERSION");
Alter table "GUS_FR_TEMA" add Constraint "GUS_FVE_FTE_FK" foreign key ("FTE_VERSION") references "GUS_FR_VERSION" ("FVE_VERSION") ;
Create Index "GUS_PPL_CON_I" ON "GUS_CONTEN" ("CON_PPLCOD");
Alter table "GUS_CONTEN" add Constraint "GUS_PPL_CON_FK" foreign key ("CON_PPLCOD") references "GUS_FR_PERPLA" ("PPL_CODI") ;
Create Index "GUS_PPL_TPN_I" ON "GUS_TPNOTI" ("TPN_PPLCOD");
Alter table "GUS_TPNOTI" add Constraint "GUS_PPL_TPN_FK" foreign key ("TPN_PPLCOD") references "GUS_FR_PERPLA" ("PPL_CODI") ;
Create Index "GUS_PPL_CMP_I" ON "GUS_COMPOS" ("CMP_PPLCOD");
Alter table "GUS_COMPOS" add Constraint "GUS_PPL_CMP_FK" foreign key ("CMP_PPLCOD") references "GUS_FR_PERPLA" ("PPL_CODI") ;

-- Indices para mejorar el rendimiento #41
CREATE INDEX "GUS_MICROS_IMAGEN_I" ON "GUS_MICROS" ("MIC_IMAGEN");
CREATE INDEX "GUS_MICROS_IMGCAM_I" ON "GUS_MICROS" ("MIC_IMGCAM");
CREATE INDEX "GUS_MICROS_CSS_I"    ON "GUS_MICROS" ("MIC_CSS");
CREATE INDEX "GUS_NOTICS_IMAGEN_I" ON "GUS_NOTICS" ("NOT_IMAGEN");
CREATE INDEX "GUS_PREGUN_IMAGEN_I" ON "GUS_PREGUN" ("PRE_IMAGEN");
CREATE INDEX "GUS_COMPOS_IMGBUL_I" ON "GUS_COMPOS" ("CMP_IMGBUL");
CREATE INDEX "GUS_AGEIDI_DOCU_I"   ON "GUS_AGEIDI" ("AID_DOCU");
CREATE INDEX "GUS_AGEIDI_IMAGEN_I" ON "GUS_AGEIDI" ("AID_IMAGEN");
CREATE INDEX "GUS_MENU_IMGMEN_I"   ON "GUS_MENU"   ("MNU_IMGMEN");

-- INDICES SOBRE LAS ENCUESTAS PARA SOLVENTAR PROBLEMAS DE RENDIMIENTO
CREATE INDEX "GUS_ENCUST_ENC_MICCOD_FK_I" ON "GUS_ENCUST" ("ENC_MICCOD");
CREATE INDEX "GUS_PREGUN_PRE_ENCCOD_FK_I" ON "GUS_PREGUN" ("PRE_ENCCOD");
CREATE INDEX "GUS_RESPUS_RES_PRECOD_FK_I" ON "GUS_RESPUS" ("RES_PRECOD");
CREATE INDEX "GUS_ENCVOT_VOT_IDRESP_FK_I" ON "GUS_ENCVOT" ("VOT_IDRESP");
CREATE INDEX "GUS_ENCVOT_VOT_IDENCU_I" ON "GUS_ENCVOT" ("VOT_IDENCU");
CREATE INDEX "GUS_ENCVOT_VOT_IDPREG_I" ON "GUS_ENCVOT" ("VOT_IDPREG");
CREATE INDEX "GUS_ENCVOT_VOT_CODUSU_I" ON "GUS_ENCVOT" ("VOT_CODUSU");  


-- Create Functions section

--Convierte una cadena a un formato utilizable en url.
--Por ejemplo, convertiría:
--    "Perfil del t(u)rista británic 2002''"
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
/

-- Nextval portable
create or replace
Function GUS_NEXTVAL ( secuencia IN varchar2 ) RETURN  varchar2
IS
 dynsql VARCHAR2(1000);
 ret NUMBER;

BEGIN

  dynsql := 'SELECT ' || secuencia || '.NEXTVAL FROM DUAL';
  EXECUTE IMMEDIATE dynsql into ret;

RETURN ret;

END;
/

Comment on table "GUS_ACTIDI" is 'Tabla que contiene los atributos dependientes de idioma de los tipos de registros que incluiye una agenda de un microsite.';
Comment on table "GUS_ACTIVI" is 'Tabla que contiene los tipos de registros que incluiye una agenda de un microsite.';
Comment on table "GUS_AGEIDI" is 'Tabla que contiene los atributos dependientes de idioma de los registros que incluiye una agenda de un microsite.';
Comment on table "GUS_AGENDA" is 'Tabla que contiene los registros de la agenda de un microsite.';
Comment on table "GUS_CMPIDI" is 'Tabla que contiene los atributos dependientes de idioma del registro de componentes de un microsite.';
Comment on table "GUS_COMPOS" is 'Tabla que contiene el registro de componentes sobre los listados definidos para un microsite. Son distintas presentaciones para un informe, que pueden incluirse en un contenido.';
Comment on table "GUS_CONIDI" is 'Tabla que contiene los atributos dependientes de idioma de los contenidos de un microsite.';
Comment on table "GUS_CONTEN" is 'Tabla que contiene los contenidos de un microsite. Son los contenidos estáticos o los enlaces externos asociados al menú.';
Comment on table "GUS_DOCUS"  is 'Tabla que contiene los archivos (imágenes, pdf, doc, etc..) utilizados en los distintos elementos de un microsite.';
Comment on table "GUS_ENCIDI" is 'Tabla que contiene los atributos dependientes de idioma para las encuestas de un microsite.';
Comment on table "GUS_ENCUST" is 'Tabla que contiene el registro de encuestas definidas para un microsite. Se compone de una relación de preguntas y respuestas.';
Comment on table "GUS_ENCVOT" is 'Tabla que contiene los resultados de las contestaciones sobre las preguntas asociadas a una encuesta publicadas para un microsite.';
Comment on table "GUS_FAQ"    is 'Tabla que contiene el registro de preguntas frecuentes creadas para un microsite.';
Comment on table "GUS_FAQIDI" is 'Tabla que contiene los atributos dependientes de idioma para las preguntas frecuentes de un microsite.';
Comment on table "GUS_FRMCON" is 'Tabla que contiene el registro de los formularios de contacto definidos para un microsite. Se compone de un número de campos que incluirá el formulario.';
Comment on table "GUS_FRMIDI" is 'Tabla que contiene los atributos dependientes de idioma del registro de campos de un formulario de un microsite.';
Comment on table "GUS_FRMLIN" is 'Tabla que contiene la relación de campos y su tipo asociados a un formularios de contacto definidos para un microsite.';
Comment on table "GUS_IDIOMA" is 'Tabla que contiene la lista de idiomas gestionados por los microsites. La activación de un nuevo idioma no solo supone el registro de esta tabla.';
Comment on table "GUS_MENU"   is 'Tabla que contiene la definición del arbol de menú asociado a un microsite. Define los nodos de menú, su tipo y funcionamiento y enlaza con los contenidos del microsite.';
Comment on table "GUS_MICIDI" is 'Tabla que contiene los atributos dependientes de idioma para la definición de un elementos de un tipo de listado de un microsite.';
Comment on table "GUS_MICROS" is 'Tabla que contiene la definición del microsite con todos los atributos de caracter general que determinan su funcionamiento.';
Comment on table "GUS_MICUSU" is 'Tabla que relaciona un usuario con todos los microsites en los que tiene permiso, y el perfil con el que actual sobre él.';
Comment on table "GUS_MNUIDI" is 'Tabla que contiene los atributos dependientes de idioma para el menú de un microsite.';
Comment on table "GUS_MUSUAR" is 'Tabla que contiene la ficha de los usuarios del entorno de los microsite. El alta se realiza coincidir con un usuario de CAIB. Se utiliza para controlar que un usuario CAIB acceda un microsite con un perfil determinado.';
Comment on table "GUS_NOTICS" is 'Tabla que contiene el registro de elemetos a publicar en la lista para un tipo de listado definidos en un microsite.';
Comment on table "GUS_PREGUN" is 'Tabla que contiene la relación de preguntas asociadas a una encuesta definidas para un microsite.';
Comment on table "GUS_PREIDI" is 'Tabla que contiene los atributos dependientes de idioma para las preguntas asociadas a una encuesta de un microsite.';
Comment on table "GUS_RESIDI" is 'Tabla que contiene los atributos dependientes de idioma para las respuestas tabuladas de las preguntas asociadas a una encuesta de un microsite.';
Comment on table "GUS_RESPUS" is 'Tabla que contiene la relación de respuestas tabuladas a una pregunta asociadas a una encuesta definidas para un microsite.';
Comment on table "GUS_STATS"  is 'Tabla que contiene el registro de las estadísticas de acceso a los distintos elementos de un microsite.';
Comment on table "GUS_TEMAS"  is 'Tabla que contiene el registro de agrupadores definidos para las preguntas frecuentes que se creen para un microsite.';
Comment on table "GUS_TEMIDI" is 'Tabla que contiene los atributos dependientes de idioma para la definición de los agrupadores de preguntas frecuentes de un microsite.';
Comment on table "GUS_TIPSER" is 'Tabla que contiene el registro de los servicios de un microsite. La creación de un nuevo servicio no solo supone el registro de esta tabla.';
Comment on table "GUS_TPNIDI" is 'Tabla que contiene los atributos dependientes de idioma para la definición de un tipo de listado de un microsite.';
Comment on table "GUS_TPNOTI" is 'Tabla que contiene el registro de los distintos tipos de listados definidos para un microsite y que se utilizarán para la Publicación de listas de elementos de los distintos tipo posible.';
Comment on table "GUS_USUARIENC" is 'Tabla que contiene la ficha de los usuarios de los usuarios que rellenan una encuesta.';
Comment on table "GUS_USURESP"   is 'Tabla que relaciona un usuario que rellena una encuesta con las respuestas.';
Comment on table "GUS_FR_TEMA"   is 'Tema del front';
Comment on table "GUS_FR_PLANTILLA" is 'Tabla que identifica las plantillas que se pueden implementar (sobreescribir) para una versión determinada de front';
Comment on table "GUS_FR_VERSION"   is 'Versión de front / identifica las versiones para su implementación en temas';
Comment on table "GUS_FR_PERPLA"    is 'Personalización de una plantilla';
Comment on table "GUS_FR_ARCHIVO"   is 'Tabla que contiene los archivos (imágenes, js, etc.) utilizados en los distintos elementos de un tema';

-- Create Attribute comments section

Comment on column "GUS_ACTIDI"."ATI_CODIDI" is 'Identificador del idioma ca,es,de,en';
Comment on column "GUS_ACTIDI"."ATI_ACTCOD" is 'Identificador de la actividad';
Comment on column "GUS_ACTIDI"."ATI_NOMBRE" is 'Nombre de la actividad';
Comment on column "GUS_ACTIVI"."ACT_CODI"   is 'Identificador de la Actividad';
Comment on column "GUS_ACTIVI"."ACT_MICCOD" is 'Identificador del microsite';
Comment on column "GUS_AGEIDI"."AID_CODIDI" is 'Identificador del idioma ca,es,de,en';
Comment on column "GUS_AGEIDI"."AID_AGECOD" is 'Código Agenda';
Comment on column "GUS_AGEIDI"."AID_DESCRI" is 'Descripción evento de la Agenda';
Comment on column "GUS_AGEIDI"."AID_DOCU"   is 'Código documento asociado';
Comment on column "GUS_AGEIDI"."AID_TITULO" is 'Título del registro';
Comment on column "GUS_AGEIDI"."AID_URL"    is 'URL de información asociada al registro';
Comment on column "GUS_AGEIDI"."AID_IMAGEN" is 'Código imagen';
Comment on column "GUS_AGEIDI"."AID_URLNOM" is 'Descripción donde se implementará el enlace de la URL';
Comment on column "GUS_AGENDA"."AGE_CODI"   is 'Identificador Registro de Agenda';
Comment on column "GUS_AGENDA"."AGE_MICCOD" is 'Código de Microsite';
Comment on column "GUS_AGENDA"."AGE_ACTIVI" is 'Identificador de la actividad';
Comment on column "GUS_AGENDA"."AGE_ORGANI" is 'Organisnmo del Evento';
Comment on column "GUS_AGENDA"."AGE_INICIO" is 'Fecha Inicio del Evento';
Comment on column "GUS_AGENDA"."AGE_FIN"    is 'Fecha de fin del Evento';
Comment on column "GUS_AGENDA"."AGE_VISIB"  is 'Visibilidad del evento';
Comment on column "GUS_CMPIDI"."CPI_CODIDI" is 'Identificador del idioma ca,es,de,en';
Comment on column "GUS_CMPIDI"."CPI_CMPCOD" is 'Identificador del componente';
Comment on column "GUS_CMPIDI"."CPI_TITULO" is 'Nombre del componente';
Comment on column "GUS_COMPOS"."CMP_CODI"   is 'Identificador del componente';
Comment on column "GUS_COMPOS"."CMP_TIPO"   is 'Tipo de componente';
Comment on column "GUS_COMPOS"."CMP_MICCOD" is 'Identificador de microsite';
Comment on column "GUS_COMPOS"."CMP_NOMBRE" is 'titulo del componente';
Comment on column "GUS_COMPOS"."CMP_SOLOIM" is 'Indica si es de tipo imagen';
Comment on column "GUS_COMPOS"."CMP_NUMELE" is 'Número de elementos';
Comment on column "GUS_COMPOS"."CMP_ORDEN"  is 'Orden de aparición';
Comment on column "GUS_COMPOS"."CMP_FILAS"  is 'Número de filas';
Comment on column "GUS_CONIDI"."CID_CODIDI" is 'Identificador del idioma ca,es,de,en';
Comment on column "GUS_CONIDI"."CID_CONCOD" is 'Identificador de contenido';
Comment on column "GUS_CONIDI"."CID_TITULO" is 'Título del contenido';
Comment on column "GUS_CONIDI"."CID_TEXTO"  is 'Para contenido HTML. Código HTML asociado principal al contenido que se mostrará al acceder a él';
Comment on column "GUS_CONIDI"."CID_URL"    is 'Para contenido externo. URL asociado al contenido al que se redireccionará al acceder a él';
Comment on column "GUS_CONIDI"."CID_TXBETA" is 'Para contenido HTML. Código HTML asociado al contenido en versión Beta hasta su validación y paso a CID_TEXTO';
Comment on column "GUS_CONIDI"."CID_URI"    is 'Uri para formar las urls';
Comment on column "GUS_CONTEN"."CON_CODI"   is 'Identificador de contenido';
Comment on column "GUS_CONTEN"."CON_CADUCA" is 'Fecha de caducidad';
Comment on column "GUS_CONTEN"."CON_PUBLIC" is 'Fecha de Publicación';
Comment on column "GUS_CONTEN"."CON_VISIB"  is 'Indica si es visible';
Comment on column "GUS_CONTEN"."CON_ORDEN"  is 'Orden aparición';
Comment on column "GUS_CONTEN"."CON_MNUCOD" is 'Identificador menú';
Comment on column "GUS_CONTEN"."CON_IMGMEN" is 'Indentificador imagen';
Comment on column "GUS_DOCUS"."DCM_CODI"    is 'Identificador de documento';
Comment on column "GUS_DOCUS"."DCM_MICCOD"  is 'Identificador del microsite';
Comment on column "GUS_DOCUS"."DCM_DATOS"   is 'Identificador archivo';
Comment on column "GUS_DOCUS"."DCM_NOMBRE"  is 'Nombre del documento físico';
Comment on column "GUS_DOCUS"."DCM_TIPO"    is 'Identificacor de ficha';
Comment on column "GUS_DOCUS"."DCM_TAMANO"  is 'identificador de procedimiento';
Comment on column "GUS_DOCUS"."DCM_PAGINA"  is 'Orden aparición';
Comment on column "GUS_ENCIDI"."EID_CODIDI" is 'Identificador del idioma ca,es,de,en';
Comment on column "GUS_ENCIDI"."EID_ENCCOD" is 'Identificador de contenido';
Comment on column "GUS_ENCIDI"."EID_TITULO" is 'Título de la Encuesta';
Comment on column "GUS_ENCIDI"."EID_URI"    is 'Uri para formar las urls';
Comment on column "GUS_ENCUST"."ENC_CODI"   is 'Identidicador Encuesta';
Comment on column "GUS_ENCUST"."ENC_MICCOD" is 'Identificador de Microsite';
Comment on column "GUS_ENCUST"."ENC_PUBLIC" is 'Fecha de Publicación';
Comment on column "GUS_ENCUST"."ENC_CADUCA" is 'Fecha de Caducidad';
Comment on column "GUS_ENCUST"."ENC_VISIB"  is 'Indica si es visible';
Comment on column "GUS_ENCUST"."ENC_PAGINA" is 'Número de página';
Comment on column "GUS_ENCUST"."ENC_MUESTR" is 'Indica si se muestran los resultados de las encuestas al publico';
Comment on column "GUS_ENCVOT"."VOT_CODI"   is 'Identificador Dato Respuesta';
Comment on column "GUS_ENCVOT"."VOT_IDENCU" is 'Identificador Encuesta';
Comment on column "GUS_ENCVOT"."VOT_IDPREG" is 'Identificador Pregunta';
Comment on column "GUS_ENCVOT"."VOT_IDRESP" is 'Identificador de Respuesta';
Comment on column "GUS_ENCVOT"."VOT_INPUT"  is 'Respuesta a la encuesta';
Comment on column "GUS_FAQ"."FAQ_CODI"      is 'Identificador de Faq';
Comment on column "GUS_FAQ"."FAQ_MICCOD"    is 'Identificador de Microsite';
Comment on column "GUS_FAQ"."FAQ_CODTEM"    is 'Identificador del Tema';
Comment on column "GUS_FAQ"."FAQ_FECHA"     is 'Fecha faq';
Comment on column "GUS_FAQ"."FAQ_VISIB"     is 'Indica si es o no visible';
Comment on column "GUS_FAQIDI"."FID_CODIDI" is 'Identificador del idioma ca,es,de,en';
Comment on column "GUS_FAQIDI"."FID_FAQCOD" is 'Identificador de contenido';
Comment on column "GUS_FAQIDI"."FID_PREGUN" is 'Texto de la Pregunta frecuente';
Comment on column "GUS_FAQIDI"."FID_RESPU" 	is 'Texto de la Respuesta frecuente';
Comment on column "GUS_FAQIDI"."FID_URL"    is 'URL asociado a la pregunta con información de interés';
Comment on column "GUS_FAQIDI"."FID_URLNOM" is 'Texto del link para la URL asociado a la pregunta con información de interés';
Comment on column "GUS_FRMCON"."FRM_CODI"   is 'Identidicador Formulario de Contacto';
Comment on column "GUS_FRMCON"."FRM_MICCOD" is 'Identificador de microsite';
Comment on column "GUS_FRMCON"."FRM_EMAIL"  is 'Direccion de correo';
Comment on column "GUS_FRMCON"."FRM_VISIB"  is 'Indica si es visibile';
Comment on column "GUS_FRMIDI"."RID_CODIDI" is 'Identificador del idioma ca,es,de,en';
Comment on column "GUS_FRMIDI"."RID_FLICOD" is 'Identificador de contenido';
Comment on column "GUS_FRMIDI"."RID_TEXTO"  is 'Nomber del campo del formulario';
Comment on column "GUS_FRMLIN"."FLI_CODI"   is 'Identificador de la linea';
Comment on column "GUS_FRMLIN"."FLI_VISIB"  is 'Indica si es visible';
Comment on column "GUS_FRMLIN"."FLI_TAMANO" is 'Indica el tamaño';
Comment on column "GUS_FRMLIN"."FLI_LINEAS" is 'Número de líneas';
Comment on column "GUS_FRMLIN"."FLI_ORDEN"  is 'Orden aparición';
Comment on column "GUS_FRMLIN"."FLI_OBLIGA" is 'Indica Obligacion de rellenar este campo en el formulario';
Comment on column "GUS_IDIOMA"."IDI_CODI"   is 'Identificador Idioma';
Comment on column "GUS_IDIOMA"."IDI_ORDEN"  is 'Ordenación';
Comment on column "GUS_IDIOMA"."IDI_CODEST" is 'Identificador para estadísticas';
Comment on column "GUS_IDIOMA"."IDI_NOMBRE" is 'Nombre del Idioma';
Comment on column "GUS_MENU"."MNU_CODI"     is 'Identificador del menú';
Comment on column "GUS_MENU"."MNU_MICCOD"   is 'Identificador de microsite';
Comment on column "GUS_MENU"."MNU_ORDEN"    is 'Orden aparición menú';
Comment on column "GUS_MENU"."MNU_IMGMEN"   is 'Imagen menú';
Comment on column "GUS_MENU"."MNU_PADRE"    is 'Nodo padre.  0=raiz o sin padre';
Comment on column "GUS_MENU"."MNU_VISIB"    is 'Visiblidad del menu';
Comment on column "GUS_MENU"."MNU_MODO"     is 'F - Fijo, C - Carpeta';
Comment on column "GUS_MICIDI"."MID_CODIDI" is 'Identificador del idioma ca,es,de,en';
Comment on column "GUS_MICIDI"."MID_MICCOD" is 'Identificador del microsite';
Comment on column "GUS_MICIDI"."MID_TITULO" is 'Nomber del microsite';
Comment on column "GUS_MICIDI"."MID_KEYWORDS" is 'información semántica: keywords';
Comment on column "GUS_MICIDI"."MID_DESCRIPTION" is 'información semántica: description';
Comment on column "GUS_MICROS"."MIC_CODI"   is 'Identificador del microsite';
Comment on column "GUS_MICROS"."MIC_CODUNI" is 'Codigo Unidad orgánica';
Comment on column "GUS_MICROS"."MIC_FECHA"  is 'Fecha mircrisite';
Comment on column "GUS_MICROS"."MIC_VISIB"  is 'Indica si es visible.';
Comment on column "GUS_MICROS"."MIC_RESTRI" is 'N=internet, S=intranet';
Comment on column "GUS_MICROS"."MIC_SERSEL" is 'Ultimos contenidos modificados. Listado de ids de contenidos separados por ;';
Comment on column "GUS_MICROS"."MIC_BUSCA"  is 'Incluir buscador';
Comment on column "GUS_MICROS"."MIC_CLAVE"  is 'Identificador de clave unica';
Comment on column "GUS_MICROS"."MIC_MNUCRP" is 'Menu corporativo';
Comment on column "GUS_MICROS"."MIC_CSSPTR" is 'CSS genérico base utilizado por el microsite si no hay css personalizado. (A)zul, (R)ojo, (V)erde, (N)egro, (G)amarillo y (M)agenta';
Comment on column "GUS_MICROS"."MIC_URI"    is 'URI única del microsite, usada para formar la url completa';
Comment on column "GUS_MICROS"."MIC_ANALYTICS" is 'Clave de google analytics';
Comment on column "GUS_MICROS"."MIC_FTECOD" is 'Tema a aplicar';
Comment on column "GUS_MICROS"."MIC_DESARROLLO" is 'Pone el sitio web en modo desarrollo, deshabilitando caché de plantillas.';
Comment on column "GUS_MICUSU"."MIU_CODUSU" is 'Usuario del Microsite';
Comment on column "GUS_MICUSU"."MIU_CODMIC" is 'Identificador Microsite';
Comment on column "GUS_MNUIDI"."MDI_MNUCOD" is 'Identificador de contenido';
Comment on column "GUS_MNUIDI"."MDI_CODIDI" is 'Identificador del idioma ca,es,de,en';
Comment on column "GUS_MNUIDI"."MDI_NOMBRE" is 'Nomber del nodo del menú';
Comment on column "GUS_MUSUAR"."MSU_CODI"   is 'Identificador usuario de microsites';
Comment on column "GUS_MUSUAR"."MSU_USERNA" is 'Usuario de acceso al back de microsites. Debe coincidir con un usuario CAIB';
Comment on column "GUS_MUSUAR"."MSU_PASSWO" is 'No utilizado en la gestión actual: Para usuarios externos a CAIB: Password del usuario';
Comment on column "GUS_MUSUAR"."MSU_NOMBRE" is 'Nombre completo del usuario';
Comment on column "GUS_MUSUAR"."MSU_PERFIL" is 'Perfil del usuriario. "sacadmin"-Administrador, "sacsuper"-Supervisor, "sacoper"-Operador';
Comment on column "GUS_MUSUAR"."MSU_PERMIS" is 'Permisos para Tiny';
Comment on column "GUS_NOTICS"."NOT_CODI"   is 'Identificador de noticia';
Comment on column "GUS_NOTICS"."NOT_MICCOD" is 'Identificador de microsite';
Comment on column "GUS_NOTICS"."NOT_IMAGEN" is 'Imagen noticia';
Comment on column "GUS_NOTICS"."NOT_CADUCA" is 'Fecha caducidad';
Comment on column "GUS_NOTICS"."NOT_PUBLIC" is 'Fecha de Publicación';
Comment on column "GUS_NOTICS"."NOT_VISIB"  is 'Indica si es visible';
Comment on column "GUS_NOTICS"."NOT_VISWEB" is 'Indica si es visible en web';
Comment on column "GUS_NOTICS"."NOT_TIPO"   is 'Tipo de noticia';
Comment on column "GUS_NOTICS"."NOT_ORDEN"  is 'Orden de aparicion';
Comment on column "GUS_NOTIDI"."NID_CODIDI" is 'Identificador del idioma ca,es,de,en';
Comment on column "GUS_NOTIDI"."NID_NOTCOD" is 'Identificador del documento';
Comment on column "GUS_NOTIDI"."NID_TITULO" is 'Titulo del documento';
Comment on column "GUS_NOTIDI"."NID_SUBTIT" is 'SubTitulo del documento';
Comment on column "GUS_NOTIDI"."NID_FUENTE" is 'Nombre de la fuente del origen del documento';
Comment on column "GUS_NOTIDI"."NID_URL"    is 'URL del enlace a información externa';
Comment on column "GUS_NOTIDI"."NID_TEXTO"  is 'Texto Descripción del documento';
Comment on column "GUS_NOTIDI"."NID_URLNOM" is 'Nombre del enlace a información externa';
Comment on column "GUS_NOTIDI"."NID_URI"    is 'Uri para formar las urls';
Comment on column "GUS_PREGUN"."PRE_CODI"   is 'Identificador de pregunta';
Comment on column "GUS_PREGUN"."PRE_ENCCOD" is 'Identificador de encuesta';
Comment on column "GUS_PREGUN"."PRE_IMAGEN" is 'Imagen pregunta';
Comment on column "GUS_PREGUN"."PRE_MULTIR" is 'Mutirespuesta';
Comment on column "GUS_PREGUN"."PRE_VISCMP" is 'Visible el componente';
Comment on column "GUS_PREGUN"."PRE_OBLIGA" is 'Obligacion responder';
Comment on column "GUS_PREGUN"."PRE_VISIB"  is 'Indica si es visible';
Comment on column "GUS_PREGUN"."PRE_ORDEN"  is 'Orden de Aparicion';
Comment on column "GUS_PREGUN"."PRE_NRESP"  is 'Numero de Respuesta';
Comment on column "GUS_PREIDI"."PID_CODIDI" is 'Identificador del idioma ca,es,de,en';
Comment on column "GUS_PREIDI"."PID_PRECOD" is 'Identificador de la preguta en la ecuenta';
Comment on column "GUS_PREIDI"."PID_TITULO" is 'Título de la pregunta de la encuesta';
Comment on column "GUS_RESIDI"."REI_CODIDI" is 'Identificador del idioma ca,es,de,en';
Comment on column "GUS_RESIDI"."REI_RESCOD" is 'Codigo Respuesta';
Comment on column "GUS_RESIDI"."REI_TITULO" is 'Título de la respuesta';
Comment on column "GUS_RESPUS"."RES_CODI"   is 'Codigo Respuesta';
Comment on column "GUS_RESPUS"."RES_PRECOD" is 'Codigo de Pregunta';
Comment on column "GUS_RESPUS"."RES_ORDEN"  is 'Orden aparicion';
Comment on column "GUS_RESPUS"."RES_NRESP"  is 'Número Respuesta';
Comment on column "GUS_RESPUS"."RES_TIPO"   is 'Tipo respuesta';
Comment on column "GUS_STATS"."STA_CODI"    is 'Identificador de estadistica';
Comment on column "GUS_STATS"."STA_ITEM"    is 'Identificador del Item';
Comment on column "GUS_STATS"."STA_MES"     is 'Mes';
Comment on column "GUS_STATS"."STA_REF"     is 'Referencia';
Comment on column "GUS_STATS"."STA_NACCES"  is 'Número de Accesos';
Comment on column "GUS_STATS"."STA_MICCOD"  is 'Identificador de Microsite';
Comment on column "GUS_STATS"."STA_PUB"     is '0=privado, 1=publico';
Comment on column "GUS_TEMAS"."TEM_CODI"    is 'Identificador del Tema agrupador de las faq';
Comment on column "GUS_TEMAS"."TEM_MICCOD"  is 'Identificador Microsite';
Comment on column "GUS_TEMIDI"."TID_CODIDI" is 'Identificador del idioma ca,es,de,en';
Comment on column "GUS_TEMIDI"."TID_TEMCOD" is 'Codigo Tema';
Comment on column "GUS_TEMIDI"."TID_NOMBRE" is 'Nombre del tema que agrupa las faq';
Comment on column "GUS_TIPSER"."TPS_CODI"   is 'Identificador tipo de servicio';
Comment on column "GUS_TIPSER"."TPS_NOMBRE" is 'Nombre de servicio';
Comment on column "GUS_TIPSER"."TPS_VISIB"  is 'Indica si es o no visible';
Comment on column "GUS_TIPSER"."TPS_REF"    is 'Referencia significativa';
Comment on column "GUS_TIPSER"."TPS_URL"    is 'URL del action que lo gestiona';
Comment on column "GUS_TIPSER"."TPS_TIPO"   is 'Tipo de servicio 0, 1, 2';
Comment on column "GUS_TPNIDI"."TPI_CODIDI" is 'Identificador del idioma ca,es,de,en';
Comment on column "GUS_TPNIDI"."TPI_TIPCOD" is 'Codigo tipo listado';
Comment on column "GUS_TPNIDI"."TPI_NOMBRE" is 'Nombre del listado';
Comment on column "GUS_TPNIDI"."TPI_URI"    is 'Uri para formar las urls del tipo';
Comment on column "GUS_TPNOTI"."TPN_CODI"   is 'Identificador tipo de listado';
Comment on column "GUS_TPNOTI"."TPN_MICCOD" is 'Identificador Microsite';
Comment on column "GUS_TPNOTI"."TPN_TAMPAG" is 'Numero de registros si se aplica paginación';
Comment on column "GUS_TPNOTI"."TPN_TIPPAG" is 'Indica si se aplica paginación al listado 0.per nombre de registres/1.per any, segons data de publicació';
Comment on column "GUS_TPNOTI"."TPN_TPELEM" is 'Tipo de elemento: 0.Fitxa, 1.Enllaç, 2.Llista de Documents, 3.Conexió externa, 4.Galeria de fotos, 5.Mapa';
Comment on column "GUS_TPNOTI"."TPN_BUSCAR" is 'Indicador de si se aplica buscador al listado o no';
Comment on column "GUS_TPNOTI"."TPN_ORDEN"  is '0=por campo orden; 1=por fecha publicacion ascendente; 2=por fecha publicacion descendente;3=por titulo';
Comment on column "GUS_TPNOTI"."TPN_XURL"   is 'URL de conexion listado externo';
Comment on column "GUS_TPNOTI"."TPN_XJNDI"  is 'No se utiliza: JNDI de conexion';
Comment on column "GUS_TPNOTI"."TPN_XLOCAL" is 'No se utiliza: EJB Local o Remoto, (L o R)';
Comment on column "GUS_TPNOTI"."TPN_XAUTH"  is 'No se utiliza: Tipo de autenticacion, 1=no, 2=estandar, 3=caib';
Comment on column "GUS_TPNOTI"."TPN_CLASIF" is 'Clasificador de listados para el BackOffice de microsites';
Comment on column "GUS_USUARIENC"."USE_CODI"   is 'Identificador usuario de encuestaa';
Comment on column "GUS_USUARIENC"."USE_NOMBRE" is 'Nombre completo del usuario';
Comment on column "GUS_USUARIENC"."USE_OBSERV" is 'Observaciones ';
Comment on column "GUS_USURESP"."USR_CODUSU"   is 'Identificador Usuario';
Comment on column "GUS_USURESP"."USR_CODRESP"  is 'Identificador Respuesta';
Comment on column "GUS_FR_TEMA"."FTE_NOMBRE"   is 'Nom del tema';
Comment on column "GUS_FR_TEMA"."FTE_CSS" is 'CSS General del tema';
Comment on column "GUS_FR_TEMA"."FTE_ACTUALIZACION" is 'Data d''actualització del tema';
Comment on column "GUS_FR_TEMA"."FTE_URI" is 'URI única del tema, usada para formar urls de recursos';
Comment on column "GUS_FR_PLANTILLA"."PLA_NOMBRE" is 'Nombre (identificador) de la plantilla';
Comment on column "GUS_FR_PLANTILLA"."PLA_DESCRIPCION" is 'Descripción y documentación de la plantilla';
Comment on column "GUS_FR_PLANTILLA"."PLA_TITULO" is 'Título de la plantilla';
Comment on column "GUS_FR_VERSION"."FVE_VERSION"  is 'versión de GUSITE para la que se ha desarrollado el tema';
Comment on column "GUS_FR_VERSION"."FVE_NOMBRE"   is 'Nom del tema';
Comment on column "GUS_FR_PERPLA"."PPL_TITULO"    is 'Título de la plantilla';
Comment on column "GUS_FR_PERPLA"."PPL_ORDEN"     is 'Número de orden de la personalización de plantilla';
Comment on column "GUS_FR_PERPLA"."PPL_CONTENIDO" is 'Contenido de la plantilla';
Comment on column "GUS_FR_ARCHIVO"."ARC_CODI" is 'Identificador de documento';
Comment on column "GUS_FR_ARCHIVO"."ARC_PATH" is 'Ruta virtual del archivo';
Comment on column "GUS_SOLJOB"."JOB_ID" is 'Identificador de la clase.';
Comment on column "GUS_SOLJOB"."JOB_FECINI" is 'Fecha de inicio';
Comment on column "GUS_SOLJOB"."JOB_FECFIN" is 'Fecha fin';
Comment on column "GUS_SOLJOB"."JOB_TIPO" is 'Tipo de job, siendo IDX_TODO - TODO, IDX_UA - INDEX BY UA, IDX_PDT - INDEXAR PENDIENTE, IDX_MIC - INDEXAR MICROSITE.';
Comment on column "GUS_SOLJOB"."JOB_DESCRI" is 'Info adicional';
Comment on column "GUS_SOLJOB"."JOB_IDELEM" is 'Identificador del id elemento, microsite/ua';  
Comment on column "GUS_NOTICS"."NOT_LATITUD" is 'Indica la ubicación de la notícia (Latitud)';
Comment on column "GUS_NOTICS"."NOT_LONGITUD" is 'Indica la ubicación de la notícia (Longitud)';
Comment on column "GUS_NOTICS"."NOT_ICOCOLOR" is 'Indica el color hexadecimal del marcador de la ubicación (color del icono en el mapa)';

-- After section
ALTER TABLE GUS_ACTIDI ADD (
  CONSTRAINT GUS_ATIIDI_FK 
  FOREIGN KEY (ATI_CODIDI) 
  REFERENCES GUS_IDIOMA (IDI_CODI)
  ENABLE VALIDATE);

ALTER TABLE GUS_AGEIDI ADD (
  CONSTRAINT GUS_AIDIDI_FK 
  FOREIGN KEY (AID_CODIDI) 
  REFERENCES GUS_IDIOMA (IDI_CODI)
  ENABLE VALIDATE);
  
ALTER TABLE GUS_CMPIDI ADD (
  CONSTRAINT GUS_CPIIDI_FK 
  FOREIGN KEY (CPI_CODIDI) 
  REFERENCES GUS_IDIOMA (IDI_CODI)
  ENABLE VALIDATE);
  
ALTER TABLE GUS_CONIDI ADD (
  CONSTRAINT GUS_CIDIDI_FK 
  FOREIGN KEY (CID_CODIDI) 
  REFERENCES GUS_IDIOMA (IDI_CODI)
  ENABLE VALIDATE);
  
ALTER TABLE GUS_ENCIDI ADD (
  CONSTRAINT GUS_EIDIDI_FK 
  FOREIGN KEY (EID_CODIDI) 
  REFERENCES GUS_IDIOMA (IDI_CODI)
  ENABLE VALIDATE);
  
ALTER TABLE GUS_FAQIDI ADD (
  CONSTRAINT GUS_FIDIDI_FK 
  FOREIGN KEY (FID_CODIDI) 
  REFERENCES GUS_IDIOMA (IDI_CODI)
  ENABLE VALIDATE);

ALTER TABLE GUS_FRMIDI ADD (
  CONSTRAINT GUS_RIDIDI_FK 
  FOREIGN KEY (RID_CODIDI) 
  REFERENCES GUS_IDIOMA (IDI_CODI)
 ENABLE VALIDATE);
  
ALTER TABLE GUS_FRQIDI ADD (
  CONSTRAINT GUS_FQIIDI_FK 
  FOREIGN KEY (FQI_CODIDI) 
  REFERENCES GUS_IDIOMA (IDI_CODI)
  ENABLE VALIDATE);
  
ALTER TABLE GUS_MICIDI ADD (
  CONSTRAINT GUS_MIDIDI_FK 
  FOREIGN KEY (MID_CODIDI) 
  REFERENCES GUS_IDIOMA (IDI_CODI)
  ENABLE VALIDATE);
  
ALTER TABLE GUS_MNUIDI ADD (
  CONSTRAINT GUS_MDIIDI_FK 
  FOREIGN KEY (MDI_CODIDI) 
  REFERENCES GUS_IDIOMA (IDI_CODI)
  ENABLE VALIDATE);
  
ALTER TABLE GUS_NOTIDI ADD (
  CONSTRAINT GUS_NIDIDI_FK 
  FOREIGN KEY (NID_CODIDI) 
  REFERENCES GUS_IDIOMA (IDI_CODI)
  ENABLE VALIDATE);
  
ALTER TABLE GUS_PREIDI ADD (
  CONSTRAINT GUS_PIDIDI_FK 
  FOREIGN KEY (PID_CODIDI) 
  REFERENCES GUS_IDIOMA (IDI_CODI)
  ENABLE VALIDATE);

ALTER TABLE GUS_RESIDI ADD (
  CONSTRAINT GUS_REIIDI_FK 
  FOREIGN KEY (REI_CODIDI) 
  REFERENCES GUS_IDIOMA (IDI_CODI)
  ENABLE VALIDATE);
  
ALTER TABLE GUS_TEMIDI ADD (
  CONSTRAINT GUS_TIDIDI_FK 
  FOREIGN KEY (TID_CODIDI) 
  REFERENCES GUS_IDIOMA (IDI_CODI)
  ENABLE VALIDATE);

ALTER TABLE GUS_TPNIDI ADD (
  CONSTRAINT GUS_TPIIDI_FK 
  FOREIGN KEY (TPI_CODIDI) 
  REFERENCES GUS_IDIOMA (IDI_CODI)
  ENABLE VALIDATE);

  
-- SEQUENCIES
CREATE SEQUENCE "GUS_SEQSOLR";
CREATE SEQUENCE "GUS_SEQUSUENC";
CREATE SEQUENCE "GUS_SEQCMP";
CREATE SEQUENCE "GUS_SEQCON";
CREATE SEQUENCE "GUS_SEQBAN";
CREATE SEQUENCE "GUS_SEQAGE";
CREATE SEQUENCE "GUS_SEQACT";
CREATE SEQUENCE "GUS_SEQ_ALL";
CREATE SEQUENCE "GUS_SEQTPN";
CREATE SEQUENCE "GUS_SEQTIP";
CREATE SEQUENCE "GUS_SEQTEM";
CREATE SEQUENCE "GUS_SEQSTA";
CREATE SEQUENCE "GUS_SEQRES";
CREATE SEQUENCE "GUS_SEQRDA";
CREATE SEQUENCE "GUS_SEQPRE";
CREATE SEQUENCE "GUS_SEQNOT";
CREATE SEQUENCE "GUS_SEQMPR";
CREATE SEQUENCE "GUS_SEQMIC";
CREATE SEQUENCE "GUS_SEQMEN";
CREATE SEQUENCE "GUS_SEQFRQ";
CREATE SEQUENCE "GUS_SEQFRM";
CREATE SEQUENCE "GUS_SEQFLI";
CREATE SEQUENCE "GUS_SEQFAQ";
CREATE SEQUENCE "GUS_SEQENC";
CREATE SEQUENCE "GUS_SEQDOC";
CREATE SEQUENCE "GUS_SQM_ALL";
CREATE SEQUENCE "GUS_CONV_SEQ";
CREATE SEQUENCE "GUS_LDISTRB_SEQ";
CREATE SEQUENCE "GUS_SEQAUD"; 
CREATE SEQUENCE "GUS_SEQFTE";
CREATE SEQUENCE "GUS_SEQPLA";
CREATE SEQUENCE "GUS_SEQFVE";
CREATE SEQUENCE "GUS_SEQPPL";
CREATE SEQUENCE "GUS_SEQARC";
CREATE SEQUENCE "GUS_SEQSOLJ";


