

Drop index "GUS_STAITM_I"
/
Drop index "GUS_STAMCD_FK_I"
/
Drop index "GUS_STAMES_I"
/
Drop index "GUS_STAREF_I"
/
Drop index "GUS_DCMMCD_FK_I"
/
Drop index "GUS_DCMNOM_I"
/


Alter table "GUS_ACTIDI" drop constraint "GUS_ATIACT_FK"
/
Alter table "GUS_AGENDA" drop constraint "GUS_AGEACT_FK"
/
Alter table "GUS_AGEIDI" drop constraint "GUS_AIDAGE_FK"
/
Alter table "GUS_BANIDI" drop constraint "GUS_BIDBAN_FK"
/
Alter table "GUS_CMPIDI" drop constraint "GUS_CPICMP_FK"
/
Alter table "GUS_CONIDI" drop constraint "GUS_CIDCON_FK"
/
Alter table "GUS_LDISTRIBUCION_CORREO" drop constraint "GUS_LDISTRICORR_CORREO_FK"
/
Alter table "GUS_AGEIDI" drop constraint "GUS_AIDDCM_FK"
/
Alter table "GUS_AGEIDI" drop constraint "GUS_AIDIMA_FK"
/
Alter table "GUS_BANIDI" drop constraint "GUS_BIDDCM_FK"
/
Alter table "GUS_COMPOS" drop constraint "GUS_CMPDCM_FK"
/
Alter table "GUS_CONTEN" drop constraint "GUS_CONDCM_FK"
/
Alter table "GUS_MENU" drop constraint "GUS_MNUDCM_FK"
/
Alter table "GUS_MICROS" drop constraint "GUS_MICDCM_FK"
/
Alter table "GUS_MICROS" drop constraint "GUS_MICDC2_FK"
/
Alter table "GUS_MICROS" drop constraint "GUS_MICDC3_FK"
/
Alter table "GUS_NOTICS" drop constraint "GUS_NOTDCM_FK"
/
Alter table "GUS_NOTIDI" drop constraint "GUS_NIDDCM_FK"
/
Alter table "GUS_PREGUN" drop constraint "GUS_PREDOC_FK"
/
Alter table "GUS_CONVOCATORIA" drop constraint "GUS_CONVOC_ENCUESTA_FK"
/
Alter table "GUS_ENCIDI" drop constraint "GUS_EIDENC_FK"
/
Alter table "GUS_PREGUN" drop constraint "GUS_PREENC_FK"
/
Alter table "GUS_FAQIDI" drop constraint "GUS_FIDFAQ_FK"
/
Alter table "GUS_FRMLIN" drop constraint "GUS_FLIFRM_FK"
/
Alter table "GUS_FRMIDI" drop constraint "GUS_RIDFLI_FK"
/
Alter table "GUS_FRQIDI" drop constraint "GUS_FQIFRQ_FK"
/
Alter table "GUS_LDISTRIBUCION_CORREO" drop constraint "GUS_LDISTRICORR_DISTRIB_FK"
/
Alter table "GUS_CONTEN" drop constraint "GUS_CONMNU_FK"
/
Alter table "GUS_MNUIDI" drop constraint "GUS_MDIMNU_FK"
/
Alter table "GUS_ACTIVI" drop constraint "GUS_ACTMIC_FK"
/
Alter table "GUS_AGENDA" drop constraint "GUS_AGEMIC_FK"
/
Alter table "GUS_BANNER" drop constraint "GUS_BANMIC_FK"
/
Alter table "GUS_COMPOS" drop constraint "GUS_CMPMIC_FK"
/
Alter table "GUS_CONVOCATORIA" drop constraint "GUS_CONVOC_MICROSITE_FK"
/
Alter table "GUS_ENCUST" drop constraint "GUS_ENCMIC_FK"
/
Alter table "GUS_FAQ" drop constraint "GUS_FAQMIC_FK"
/
Alter table "GUS_FRMCON" drop constraint "GUS_FRMMIC_FK"
/
Alter table "GUS_FRQSSI" drop constraint "GUS_FRQMIC_FK"
/
Alter table "GUS_IDIMIC" drop constraint "GUS_IMIMIC_FK"
/
Alter table "GUS_LDISTRIBUCION" drop constraint "GUS_LDISTRIB_MICROSITE_FK"
/
Alter table "GUS_MENU" drop constraint "GUS_MNUMIC_FK"
/
Alter table "GUS_MICIDI" drop constraint "GUS_MIDMIC_FK"
/
Alter table "GUS_MICPRO" drop constraint "GUS_MPRMIC_FK"
/
Alter table "GUS_MICUSU" drop constraint "GUS_MIUMIC_FK"
/
Alter table "GUS_NOTICS" drop constraint "GUS_NOTMIC_FK"
/
Alter table "GUS_TEMAS" drop constraint "GUS_TEMMIC_FK"
/
Alter table "GUS_TPNOTI" drop constraint "GUS_TPNMIC_FK"
/
Alter table "GUS_MICUSU" drop constraint "GUS_MIUUSU_FK"
/
Alter table "GUS_NOTIDI" drop constraint "GUS_NIDNOT_FK"
/
Alter table "GUS_CONVOCATORIA" drop constraint "GUS_CONVOC_PREGUNTA_CONF_FK"
/
Alter table "GUS_CONVOCATORIA" drop constraint "GUS_CONVOC_PREGUNTA_CORR_FK"
/
Alter table "GUS_PREIDI" drop constraint "GUS_PIDPRE_FK"
/
Alter table "GUS_RESPUS" drop constraint "GUS_RESPRE_FK"
/
Alter table "GUS_CONVOCATORIA" drop constraint "GUS_CONVOC_RESPUESTA_CONF_FK"
/
Alter table "GUS_ENCVOT" drop constraint "GUS_VOT_IDRESP_FK"
/
Alter table "GUS_RESIDI" drop constraint "GUS_REIRES_FK"
/
Alter table "GUS_USURESP" drop constraint "GUS_USRRESP_FK"
/
Alter table "GUS_CONVOCATORIA" drop constraint "GUS_CONVOC_RESPUESTA_CORR_FK"
/
Alter table "GUS_FAQ" drop constraint "GUS_FAQTEM_FK"
/
Alter table "GUS_TEMIDI" drop constraint "GUS_TIDTEM_FK"
/
Alter table "GUS_COMPOS" drop constraint "GUS_CMPTPN_FK"
/
Alter table "GUS_NOTICS" drop constraint "GUS_NOTTPN_FK"
/
Alter table "GUS_TPNIDI" drop constraint "GUS_TPITPN_FK"
/
Alter table "GUS_USURESP" drop constraint "GUS_USRUSU_FK"
/


Drop table "GUS_W3C"
/
Drop table "GUS_USURESP"
/
Drop table "GUS_USUARIENC"
/
Drop table "GUS_TPNOTI"
/
Drop table "GUS_TPNIDI"
/
Drop table "GUS_TIPSER"
/
Drop table "GUS_TEMIDI"
/
Drop table "GUS_TEMAS"
/
Drop table "GUS_STATS"
/
Drop table "GUS_RESPUS"
/
Drop table "GUS_RESIDI"
/
Drop table "GUS_PREIDI"
/
Drop table "GUS_PREGUN"
/
Drop table "GUS_NOTIDI"
/
Drop table "GUS_NOTICS"
/
Drop table "GUS_MUSUAR"
/
Drop table "GUS_MNUIDI"
/
Drop table "GUS_MICUSU"
/
Drop table "GUS_MICROS"
/
Drop table "GUS_MICPRO"
/
Drop table "GUS_MICIDI"
/
Drop table "GUS_MENU"
/
Drop table "GUS_LDISTRIBUCION_CORREO"
/
Drop table "GUS_LDISTRIBUCION"
/
Drop table "GUS_IDIOMA"
/
Drop table "GUS_IDIMIC"
/
Drop table "GUS_FRQSSI"
/
Drop table "GUS_FRQIDI"
/
Drop table "GUS_FRMLIN"
/
Drop table "GUS_FRMIDI"
/
Drop table "GUS_FRMCON"
/
Drop table "GUS_FAQIDI"
/
Drop table "GUS_FAQ"
/
Drop table "GUS_ENCVOT"
/
Drop table "GUS_ENCUST"
/
Drop table "GUS_ENCIDI"
/
Drop table "GUS_DOCUS"
/
Drop table "GUS_DISTRIB_CONVOCATORIA"
/
Drop table "GUS_CORREO"
/
Drop table "GUS_CONVOCATORIA"
/
Drop table "GUS_CONTEN"
/
Drop table "GUS_CONIDI"
/
Drop table "GUS_COMPOS"
/
Drop table "GUS_CMPIDI"
/
Drop table "GUS_BANNER"
/
Drop table "GUS_BANIDI"
/
Drop table "GUS_AGENDA"
/
Drop table "GUS_AGEIDI"
/
Drop table "GUS_ACTIVI"
/
Drop table "GUS_ACTIDI"
/


-- Create Types section


-- Create Tables section


Create table "GUS_ACTIDI" (
	"ATI_CODIDI" Varchar2 (6 CHAR) NOT NULL ,
	"ATI_ACTCOD" Number NOT NULL ,
	"ATI_NOMBRE" Varchar2 (150 CHAR),
 Constraint "GUS_ATI_PK" primary key ("ATI_CODIDI","ATI_ACTCOD") 
) 

/

Create table "GUS_ACTIVI" (
	"ACT_CODI" Number NOT NULL ,
	"ACT_MICCOD" Number,
 Constraint "GUS_ACT_PK" primary key ("ACT_CODI") 
) 

/

Create table "GUS_AGEIDI" (
	"AID_CODIDI" Varchar2 (6 CHAR) NOT NULL ,
	"AID_AGECOD" Number NOT NULL ,
	"AID_DESCRI" Varchar2 (4000 CHAR),
	"AID_DOCU" Number,
	"AID_TITULO" Varchar2 (256 CHAR),
	"AID_URL" Varchar2 (256 CHAR),
	"AID_IMAGEN" Number,
	"AID_URLNOM" Varchar2 (512 CHAR),
 Constraint "GUS_AGEIDI_PK" primary key ("AID_CODIDI","AID_AGECOD") 
) 

/

Create table "GUS_AGENDA" (
	"AGE_CODI" Number NOT NULL ,
	"AGE_MICCOD" Number,
	"AGE_ACTIVI" Number,
	"AGE_ORGANI" Varchar2 (256 CHAR),
	"AGE_INICIO" Date NOT NULL ,
	"AGE_FIN" Date,
	"AGE_VISIB" Varchar2 (1 CHAR) Default 'S'
,
 Constraint "GUS_AGE_PK" primary key ("AGE_CODI") 
) 

/

Create table "GUS_BANIDI" (
	"BID_CODIDI" Varchar2 (6 CHAR) NOT NULL ,
	"BID_BANCOD" Number NOT NULL ,
	"BID_TITULO" Varchar2 (50 CHAR),
	"BID_URL" Varchar2 (1024 CHAR),
	"BID_ALT" Varchar2 (100 CHAR),
	"BID_IMAGEN" Number,
 Constraint "GUS_BID_PK" primary key ("BID_CODIDI","BID_BANCOD") 
) 

/

Create table "GUS_BANNER" (
	"BAN_CODI" Number NOT NULL ,
	"BAN_MICCOD" Number,
	"BAN_CADUCA" Date,
	"BAN_PUBLIC" Date,
	"BAN_VISIB" Varchar2 (1 CHAR) Default 'S'
,
 Constraint "GUS_BAN_PK" primary key ("BAN_CODI") 
) 

/

Create table "GUS_CMPIDI" (
	"CPI_CODIDI" Varchar2 (6 CHAR) NOT NULL ,
	"CPI_CMPCOD" Number NOT NULL ,
	"CPI_TITULO" Varchar2 (256 CHAR),
 Constraint "GUS_CMPIDI_PK" primary key ("CPI_CODIDI","CPI_CMPCOD") 
) 

/

Create table "GUS_COMPOS" (
	"CMP_CODI" Number NOT NULL ,
	"CMP_TIPO" Number NOT NULL ,
	"CMP_MICCOD" Number,
	"CMP_NOMBRE" Varchar2 (256 CHAR) NOT NULL ,
	"CMP_SOLOIM" Varchar2 (1 CHAR) Default 'N',
	"CMP_NUMELE" Number Default 0,
	"CMP_ORDEN" Number,
	"CMP_IMGBUL" Number,
	"CMP_FILAS" Varchar2 (1 CHAR) Default 'S'
,
 Constraint "GUS_CMP_PK" primary key ("CMP_CODI") 
) 

/

Create table "GUS_CONIDI" (
	"CID_CODIDI" Varchar2 (6 CHAR) NOT NULL ,
	"CID_CONCOD" Number NOT NULL ,
	"CID_TITULO" Varchar2 (256 CHAR),
	"CID_TEXTO" Clob,
	"CID_URL" Varchar2 (512 CHAR),
	"CID_TXBETA" Clob,
 Constraint "GUS_CID_PK" primary key ("CID_CODIDI","CID_CONCOD") 
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

/

Create table "GUS_CONTEN" (
	"CON_CODI" Number NOT NULL ,
	"CON_CADUCA" Date,
	"CON_PUBLIC" Date NOT NULL ,
	"CON_VISIB" Varchar2 (1 CHAR) Default 'S',
	"CON_ORDEN" Number,
	"CON_MNUCOD" Number,
	"CON_IMGMEN" Number,
 Constraint "GUS_CON_PK" primary key ("CON_CODI") 
) 

/

Create table "GUS_CONVOCATORIA" (
	"CODI" Number(19,0) NOT NULL ,
	"NOMBRE" Varchar2 (255),
	"DESCRIPCION" Varchar2 (255),
	"DATA_ENVIO" Date,
	"MICROSITE_ID" Number(19,0),
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
 Constraint "PK_GUS_CONVOCATORIA" primary key ("CODI") 
) 
LOB("OTROS_DEST") STORE AS 
	(CHUNK 8192)
LOB("TEXTOMSG") STORE AS 
	(CHUNK 8192)

/

Create table "GUS_CORREO" (
	"CORREO" Varchar2 (255) NOT NULL ,
	"NOENVIAR" Number(1,0),
	"NOMBRE" Varchar2 (255),
	"APELLIDOS" Varchar2 (512),
	"ULTIMO_ENVIO" Date,
	"ERROR_ENVIO" Varchar2 (2000),
	"INTENTO_ENVIO" Number,
 Constraint "PK_GUS_CORREO" primary key ("CORREO") 
) 

/

Create table "GUS_DISTRIB_CONVOCATORIA" (
	"CONVOCATORIA_ID" Number(19,0) NOT NULL ,
	"DISTRIB_ID" Number(19,0) NOT NULL ,
	"ULTIMO_ENVIO" Date,
	"ERROR_ENVIO" Varchar2 (255),
 Constraint "PK_GUS_DISTRIB_CONVOCATORIA" primary key ("CONVOCATORIA_ID","DISTRIB_ID") 
) 

/

Create table "GUS_DOCUS" (
	"DCM_CODI" Number NOT NULL ,
	"DCM_MICCOD" Number,
	"DCM_DATOS" Blob,
	"DCM_NOMBRE" Varchar2 (256 CHAR),
	"DCM_TIPO" Varchar2 (256 CHAR),
	"DCM_TAMANO" Number,
	"DCM_PAGINA" Number,
 Constraint "GUS_DCM_PK" primary key ("DCM_CODI") 
) 
LOB("DCM_DATOS") STORE AS GUS_DOCUS_DCM_DATOS_LOB
	(CHUNK 8192)

/

Create table "GUS_ENCIDI" (
	"EID_CODIDI" Varchar2 (6 CHAR) NOT NULL ,
	"EID_ENCCOD" Number NOT NULL ,
	"EID_TITULO" Varchar2 (256 CHAR),
 Constraint "GUS_ENCIDI_PK" primary key ("EID_CODIDI","EID_ENCCOD") 
) 

/

Create table "GUS_ENCUST" (
	"ENC_CODI" Number NOT NULL ,
	"ENC_MICCOD" Number,
	"ENC_PUBLIC" Date,
	"ENC_CADUCA" Date,
	"ENC_VISIB" Varchar2 (1 CHAR) Default 'S',
	"ENC_INDIV" Varchar2 (1 CHAR) Default 'S',
	"ENC_PAGINA" Number,
	"ENC_MUESTR" Varchar2 (1 CHAR) Default 'S'
,
	"ENC_IDENTIF" Varchar2 (1),
 Constraint "GUS_ENCUST_PK" primary key ("ENC_CODI") 
) 

/

Create table "GUS_ENCVOT" (
	"VOT_CODI" Number NOT NULL ,
	"VOT_IDENCU" Number,
	"VOT_IDPREG" Number,
	"VOT_IDRESP" Number NOT NULL ,
	"VOT_INPUT" Varchar2 (2000),
	"VOT_CODUSU" Number(19,0),
 Constraint "GUS_VOT_RESDAT_PK" primary key ("VOT_CODI") 
) 

/

Create table "GUS_FAQ" (
	"FAQ_CODI" Number NOT NULL ,
	"FAQ_MICCOD" Number,
	"FAQ_CODTEM" Number,
	"FAQ_FECHA" Date,
	"FAQ_VISIB" Varchar2 (1 CHAR) Default 'S'
,
 Constraint "GUS_FAQ_PK" primary key ("FAQ_CODI") 
) 

/

Create table "GUS_FAQIDI" (
	"FID_CODIDI" Varchar2 (6 CHAR) NOT NULL ,
	"FID_FAQCOD" Number NOT NULL ,
	"FID_PREGUN" Varchar2 (1024 CHAR),
	"FID_RESPU" Varchar2 (4000 CHAR),
	"FID_URL" Varchar2 (1024 CHAR),
	"FID_URLNOM" Varchar2 (512 CHAR),
 Constraint "GUS_FID_PK" primary key ("FID_CODIDI","FID_FAQCOD") 
) 

/

Create table "GUS_FRMCON" (
	"FRM_CODI" Number NOT NULL ,
	"FRM_MICCOD" Number,
	"FRM_EMAIL" Varchar2 (256 CHAR),
	"FRM_VISIB" Varchar2 (1 CHAR) Default 'S'
,
	"FRM_ANEXARCH" Varchar2 (1),
 Constraint "GUS_FRM_PK" primary key ("FRM_CODI") 
) 

/

Create table "GUS_FRMIDI" (
	"RID_CODIDI" Varchar2 (6 CHAR) NOT NULL ,
	"RID_FLICOD" Number NOT NULL ,
	"RID_TEXTO" Varchar2 (4000 CHAR),
 Constraint "GUS_RID_PK" primary key ("RID_CODIDI","RID_FLICOD") 
) 

/

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
 Constraint "GUS_FLI_PK" primary key ("FLI_CODI") 
) 

/

Create table "GUS_FRQIDI" (
	"FQI_CODIDI" Varchar2 (6 CHAR) NOT NULL ,
	"FQI_FRQCOD" Number NOT NULL ,
	"FQI_NOMBRE" Varchar2 (100 CHAR),
 Constraint "GUS_FQI_PK" primary key ("FQI_CODIDI","FQI_FRQCOD") 
) 

/

Create table "GUS_FRQSSI" (
	"FRQ_CODI" Number NOT NULL ,
	"FRQ_MICCOD" Number,
	"FRQ_CENTRE" Varchar2 (25 CHAR),
	"FRQ_TPESCR" Varchar2 (25 CHAR),
 Constraint "GUS_FRQ_PK" primary key ("FRQ_CODI") 
) 

/

Create table "GUS_IDIMIC" (
	"IMI_CODIDI" Varchar2 (6 CHAR) NOT NULL ,
	"IMI_MICCOD" Number NOT NULL ,
 Constraint "GUS_IMI_PK" primary key ("IMI_CODIDI","IMI_MICCOD") 
) 

/

Create table "GUS_IDIOMA" (
	"IDI_CODI" Varchar2 (2 CHAR) NOT NULL ,
	"IDI_ORDEN" Number(10,0) NOT NULL ,
	"IDI_CODEST" Varchar2 (128 CHAR),
	"IDI_NOMBRE" Varchar2 (128 CHAR),
	"IDI_TRADUCTOR" Varchar2 (128 CHAR),
 Constraint "GUS_IDI_PK" primary key ("IDI_CODI") 
) 

/

Create table "GUS_LDISTRIBUCION" (
	"CODI" Number(19,0) NOT NULL ,
	"NOMBRE" Varchar2 (255),
	"DESCRIPCION" Varchar2 (255),
	"MICROSITE_ID" Number(19,0),
	"PUBLICO" Number(1,0),
 Constraint "PK_GUS_LDISTRIBUCION" primary key ("CODI") 
) 

/

Create table "GUS_LDISTRIBUCION_CORREO" (
	"LISTADISTRIB_ID" Number(19,0) NOT NULL ,
	"CORREO_ID" Varchar2 (255) NOT NULL ,
 Constraint "PK_GUS_LDISTRIBUCION_CORREO" primary key ("LISTADISTRIB_ID","CORREO_ID") 
) 

/

Create table "GUS_MENU" (
	"MNU_CODI" Number NOT NULL ,
	"MNU_MICCOD" Number,
	"MNU_ORDEN" Number Default 0,
	"MNU_IMGMEN" Number,
	"MNU_PADRE" Number Default 0 NOT NULL ,
	"MNU_VISIB" Varchar2 (1 CHAR) Default 'S',
	"MNU_MODO" Varchar2 (1 CHAR) Default 'F'
,
 Constraint "GUS_MNU_PK" primary key ("MNU_CODI") 
) 

/

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
 Constraint "GUS_MID_PK" primary key ("MID_CODIDI","MID_MICCOD") 
) 

/

Create table "GUS_MICPRO" (
	"MPR_CODI" Number NOT NULL ,
	"MPR_MICCOD" Number,
	"MPR_PROSEL" Varchar2 (4000 CHAR),
 Constraint "GUS_MPR_PK" primary key ("MPR_CODI") 
) 

/

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
 Constraint "GUS_MIC_PK" primary key ("MIC_CODI") 
) 

/

Create table "GUS_MICUSU" (
	"MIU_CODUSU" Number(19,0) NOT NULL ,
	"MIU_CODMIC" Number(19,0) NOT NULL ,
 Constraint "GUS_MIU_PK" primary key ("MIU_CODUSU","MIU_CODMIC") 
) 

/

Create table "GUS_MNUIDI" (
	"MDI_MNUCOD" Number NOT NULL ,
	"MDI_CODIDI" Varchar2 (6 CHAR) NOT NULL ,
	"MDI_NOMBRE" Varchar2 (256 CHAR),
 Constraint "GUS_MDI_PK" primary key ("MDI_MNUCOD","MDI_CODIDI") 
) 

/

Create table "GUS_MUSUAR" (
	"MSU_CODI" Number(19,0) NOT NULL ,
	"MSU_USERNA" Varchar2 (128 CHAR) Constraint "GUS_MUS_UNI" UNIQUE ,
	"MSU_PASSWO" Varchar2 (128 CHAR),
	"MSU_NOMBRE" Varchar2 (256 CHAR),
	"MSU_OBSERV" Varchar2 (4000 CHAR),
	"MSU_PERFIL" Varchar2 (64 CHAR),
 Constraint "GUS_MSU_PK" primary key ("MSU_CODI") 
) 

/

Create table "GUS_NOTICS" (
	"NOT_CODI" Number NOT NULL ,
	"NOT_MICCOD" Number,
	"NOT_IMAGEN" Number,
	"NOT_CADUCA" Date,
	"NOT_PUBLIC" Date,
	"NOT_VISIB" Varchar2 (1 CHAR) Default 'S',
	"NOT_VISWEB" Varchar2 (1 CHAR) Default 'N',
	"NOT_TIPO" Number NOT NULL ,
	"NOT_ORDEN" Number,
 Constraint "GUS_NOT_PK" primary key ("NOT_CODI") 
) 

/

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
 Constraint "GUS_NID_PK" primary key ("NID_CODIDI","NID_NOTCOD") 
) 

/

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
 Constraint "GUS_PREGUN_PK" primary key ("PRE_CODI") 
) 

/

Create table "GUS_PREIDI" (
	"PID_CODIDI" Varchar2 (6 CHAR) NOT NULL ,
	"PID_PRECOD" Number NOT NULL ,
	"PID_TITULO" Varchar2 (256 CHAR),
 Constraint "GUS_PREIDI_PK" primary key ("PID_CODIDI","PID_PRECOD") 
) 

/

Create table "GUS_RESIDI" (
	"REI_CODIDI" Varchar2 (6 CHAR) NOT NULL ,
	"REI_RESCOD" Number NOT NULL ,
	"REI_TITULO" Varchar2 (256 CHAR),
 Constraint "GUS_RESIDI_PK" primary key ("REI_CODIDI","REI_RESCOD") 
) 

/

Create table "GUS_RESPUS" (
	"RES_CODI" Number NOT NULL ,
	"RES_PRECOD" Number,
	"RES_ENTRAD" Varchar2 (256 CHAR),
	"RES_ORDEN" Number,
	"RES_NRESP" Number,
	"RES_TIPO" Varchar2 (1 CHAR),
 Constraint "GUS_RESPUS_PK" primary key ("RES_CODI") 
) 

/

Create table "GUS_STATS" (
	"STA_CODI" Number NOT NULL ,
	"STA_ITEM" Number NOT NULL ,
	"STA_MES" Number NOT NULL ,
	"STA_REF" Varchar2 (5 CHAR) NOT NULL ,
	"STA_NACCES" Number NOT NULL ,
	"STA_MICCOD" Number NOT NULL ,
	"STA_PUB" Number Default 1                      NOT NULL ,
 Constraint "GUS_STA_PK" primary key ("STA_CODI") 
) 

/

Create table "GUS_TEMAS" (
	"TEM_CODI" Number NOT NULL ,
	"TEM_MICCOD" Number,
 Constraint "GUS_TEM_PK" primary key ("TEM_CODI") 
) 

/

Create table "GUS_TEMIDI" (
	"TID_CODIDI" Varchar2 (6 CHAR) NOT NULL ,
	"TID_TEMCOD" Number NOT NULL ,
	"TID_NOMBRE" Varchar2 (100 CHAR),
 Constraint "GUS_TID_PK" primary key ("TID_CODIDI","TID_TEMCOD") 
) 

/

Create table "GUS_TIPSER" (
	"TPS_CODI" Number NOT NULL ,
	"TPS_NOMBRE" Varchar2 (64 CHAR) NOT NULL ,
	"TPS_VISIB" Varchar2 (1 CHAR) Default 'S'                    NOT NULL ,
	"TPS_REF" Varchar2 (5 CHAR) NOT NULL ,
	"TPS_URL" Varchar2 (64 CHAR),
	"TPS_TIPO" Varchar2 (1 CHAR),
 Constraint "GUS_TPS_PK" primary key ("TPS_CODI") 
) 

/

Create table "GUS_TPNIDI" (
	"TPI_CODIDI" Varchar2 (6 CHAR) NOT NULL ,
	"TPI_TIPCOD" Number NOT NULL ,
	"TPI_NOMBRE" Varchar2 (100 CHAR),
 Constraint "GUS_TPI_PK" primary key ("TPI_CODIDI","TPI_TIPCOD") 
) 

/

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
 Constraint "GUS_TPN_PK" primary key ("TPN_CODI") 
) 

/

Create table "GUS_USUARIENC" (
	"USE_CODI" Number(19,0) NOT NULL ,
	"USE_NOMBRE" Varchar2 (256 CHAR) Default 'anonim'  NOT NULL ,
	"USE_OBSERV" Varchar2 (4000 CHAR),
	"USE_DNI" Varchar2 (10),
 Constraint "GUS_USE_PK" primary key ("USE_CODI") 
) 

/

Create table "GUS_USURESP" (
	"USR_CODUSU" Number(19,0) NOT NULL ,
	"USR_CODRESP" Number NOT NULL ,
 Constraint "GUS_USR_PK" primary key ("USR_CODUSU","USR_CODRESP") 
) 

/

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

/


-- Create Alternate keys section


-- Create Indexes section

Create Index "GUS_DCMMCD_FK_I" ON "GUS_DOCUS" ("DCM_MICCOD") 
/
Create Index "GUS_DCMNOM_I" ON "GUS_DOCUS" ("DCM_NOMBRE") 
/
Create Index "GUS_STAITM_I" ON "GUS_STATS" ("STA_ITEM") 
/
Create Index "GUS_STAMCD_FK_I" ON "GUS_STATS" ("STA_MICCOD") 
/
Create Index "GUS_STAMES_I" ON "GUS_STATS" ("STA_MES") 
/
Create Index "GUS_STAREF_I" ON "GUS_STATS" ("STA_REF") 
/


-- Create Foreign keys section

Alter table "GUS_ACTIDI" add Constraint "GUS_ATIACT_FK" foreign key ("ATI_ACTCOD") references "GUS_ACTIVI" ("ACT_CODI") 
/

Alter table "GUS_AGENDA" add Constraint "GUS_AGEACT_FK" foreign key ("AGE_ACTIVI") references "GUS_ACTIVI" ("ACT_CODI") 
/

Alter table "GUS_AGEIDI" add Constraint "GUS_AIDAGE_FK" foreign key ("AID_AGECOD") references "GUS_AGENDA" ("AGE_CODI") 
/

Alter table "GUS_BANIDI" add Constraint "GUS_BIDBAN_FK" foreign key ("BID_BANCOD") references "GUS_BANNER" ("BAN_CODI") 
/

Alter table "GUS_CMPIDI" add Constraint "GUS_CPICMP_FK" foreign key ("CPI_CMPCOD") references "GUS_COMPOS" ("CMP_CODI") 
/

Alter table "GUS_CONIDI" add Constraint "GUS_CIDCON_FK" foreign key ("CID_CONCOD") references "GUS_CONTEN" ("CON_CODI") 
/

Alter table "GUS_LDISTRIBUCION_CORREO" add Constraint "GUS_LDISTRICORR_CORREO_FK" foreign key ("CORREO_ID") references "GUS_CORREO" ("CORREO") 
/

Alter table "GUS_AGEIDI" add Constraint "GUS_AIDDCM_FK" foreign key ("AID_DOCU") references "GUS_DOCUS" ("DCM_CODI") 
/

Alter table "GUS_AGEIDI" add Constraint "GUS_AIDIMA_FK" foreign key ("AID_IMAGEN") references "GUS_DOCUS" ("DCM_CODI") 
/

Alter table "GUS_BANIDI" add Constraint "GUS_BIDDCM_FK" foreign key ("BID_IMAGEN") references "GUS_DOCUS" ("DCM_CODI") 
/

Alter table "GUS_COMPOS" add Constraint "GUS_CMPDCM_FK" foreign key ("CMP_IMGBUL") references "GUS_DOCUS" ("DCM_CODI") 
/

Alter table "GUS_CONTEN" add Constraint "GUS_CONDCM_FK" foreign key ("CON_IMGMEN") references "GUS_DOCUS" ("DCM_CODI") 
/

Alter table "GUS_MENU" add Constraint "GUS_MNUDCM_FK" foreign key ("MNU_IMGMEN") references "GUS_DOCUS" ("DCM_CODI") 
/

Alter table "GUS_MICROS" add Constraint "GUS_MICDCM_FK" foreign key ("MIC_IMAGEN") references "GUS_DOCUS" ("DCM_CODI") 
/

Alter table "GUS_MICROS" add Constraint "GUS_MICDC2_FK" foreign key ("MIC_IMGCAM") references "GUS_DOCUS" ("DCM_CODI") 
/

Alter table "GUS_MICROS" add Constraint "GUS_MICDC3_FK" foreign key ("MIC_CSS") references "GUS_DOCUS" ("DCM_CODI") 
/

Alter table "GUS_NOTICS" add Constraint "GUS_NOTDCM_FK" foreign key ("NOT_IMAGEN") references "GUS_DOCUS" ("DCM_CODI") 
/

Alter table "GUS_NOTIDI" add Constraint "GUS_NIDDCM_FK" foreign key ("NID_DOCU") references "GUS_DOCUS" ("DCM_CODI") 
/

Alter table "GUS_PREGUN" add Constraint "GUS_PREDOC_FK" foreign key ("PRE_IMAGEN") references "GUS_DOCUS" ("DCM_CODI") 
/

Alter table "GUS_CONVOCATORIA" add Constraint "GUS_CONVOC_ENCUESTA_FK" foreign key ("ENCUESTA_ID") references "GUS_ENCUST" ("ENC_CODI") 
/

Alter table "GUS_ENCIDI" add Constraint "GUS_EIDENC_FK" foreign key ("EID_ENCCOD") references "GUS_ENCUST" ("ENC_CODI") 
/

Alter table "GUS_PREGUN" add Constraint "GUS_PREENC_FK" foreign key ("PRE_ENCCOD") references "GUS_ENCUST" ("ENC_CODI") 
/

Alter table "GUS_FAQIDI" add Constraint "GUS_FIDFAQ_FK" foreign key ("FID_FAQCOD") references "GUS_FAQ" ("FAQ_CODI") 
/

Alter table "GUS_FRMLIN" add Constraint "GUS_FLIFRM_FK" foreign key ("FLI_FRMCOD") references "GUS_FRMCON" ("FRM_CODI") 
/

Alter table "GUS_FRMIDI" add Constraint "GUS_RIDFLI_FK" foreign key ("RID_FLICOD") references "GUS_FRMLIN" ("FLI_CODI") 
/

Alter table "GUS_FRQIDI" add Constraint "GUS_FQIFRQ_FK" foreign key ("FQI_FRQCOD") references "GUS_FRQSSI" ("FRQ_CODI") 
/

Alter table "GUS_LDISTRIBUCION_CORREO" add Constraint "GUS_LDISTRICORR_DISTRIB_FK" foreign key ("LISTADISTRIB_ID") references "GUS_LDISTRIBUCION" ("CODI") 
/

Alter table "GUS_CONTEN" add Constraint "GUS_CONMNU_FK" foreign key ("CON_MNUCOD") references "GUS_MENU" ("MNU_CODI") 
/

Alter table "GUS_MNUIDI" add Constraint "GUS_MDIMNU_FK" foreign key ("MDI_MNUCOD") references "GUS_MENU" ("MNU_CODI") 
/

Alter table "GUS_ACTIVI" add Constraint "GUS_ACTMIC_FK" foreign key ("ACT_MICCOD") references "GUS_MICROS" ("MIC_CODI") 
/

Alter table "GUS_AGENDA" add Constraint "GUS_AGEMIC_FK" foreign key ("AGE_MICCOD") references "GUS_MICROS" ("MIC_CODI") 
/

Alter table "GUS_BANNER" add Constraint "GUS_BANMIC_FK" foreign key ("BAN_MICCOD") references "GUS_MICROS" ("MIC_CODI") 
/

Alter table "GUS_COMPOS" add Constraint "GUS_CMPMIC_FK" foreign key ("CMP_MICCOD") references "GUS_MICROS" ("MIC_CODI") 
/

Alter table "GUS_CONVOCATORIA" add Constraint "GUS_CONVOC_MICROSITE_FK" foreign key ("MICROSITE_ID") references "GUS_MICROS" ("MIC_CODI") 
/

Alter table "GUS_ENCUST" add Constraint "GUS_ENCMIC_FK" foreign key ("ENC_MICCOD") references "GUS_MICROS" ("MIC_CODI") 
/

Alter table "GUS_FAQ" add Constraint "GUS_FAQMIC_FK" foreign key ("FAQ_MICCOD") references "GUS_MICROS" ("MIC_CODI") 
/

Alter table "GUS_FRMCON" add Constraint "GUS_FRMMIC_FK" foreign key ("FRM_MICCOD") references "GUS_MICROS" ("MIC_CODI") 
/

Alter table "GUS_FRQSSI" add Constraint "GUS_FRQMIC_FK" foreign key ("FRQ_MICCOD") references "GUS_MICROS" ("MIC_CODI") 
/

Alter table "GUS_IDIMIC" add Constraint "GUS_IMIMIC_FK" foreign key ("IMI_MICCOD") references "GUS_MICROS" ("MIC_CODI") 
/

Alter table "GUS_LDISTRIBUCION" add Constraint "GUS_LDISTRIB_MICROSITE_FK" foreign key ("MICROSITE_ID") references "GUS_MICROS" ("MIC_CODI") 
/

Alter table "GUS_MENU" add Constraint "GUS_MNUMIC_FK" foreign key ("MNU_MICCOD") references "GUS_MICROS" ("MIC_CODI") 
/

Alter table "GUS_MICIDI" add Constraint "GUS_MIDMIC_FK" foreign key ("MID_MICCOD") references "GUS_MICROS" ("MIC_CODI") 
/

Alter table "GUS_MICPRO" add Constraint "GUS_MPRMIC_FK" foreign key ("MPR_MICCOD") references "GUS_MICROS" ("MIC_CODI") 
/

Alter table "GUS_MICUSU" add Constraint "GUS_MIUMIC_FK" foreign key ("MIU_CODMIC") references "GUS_MICROS" ("MIC_CODI") 
/

Alter table "GUS_NOTICS" add Constraint "GUS_NOTMIC_FK" foreign key ("NOT_MICCOD") references "GUS_MICROS" ("MIC_CODI") 
/

Alter table "GUS_TEMAS" add Constraint "GUS_TEMMIC_FK" foreign key ("TEM_MICCOD") references "GUS_MICROS" ("MIC_CODI") 
/

Alter table "GUS_TPNOTI" add Constraint "GUS_TPNMIC_FK" foreign key ("TPN_MICCOD") references "GUS_MICROS" ("MIC_CODI") 
/

Alter table "GUS_MICUSU" add Constraint "GUS_MIUUSU_FK" foreign key ("MIU_CODUSU") references "GUS_MUSUAR" ("MSU_CODI") 
/

Alter table "GUS_NOTIDI" add Constraint "GUS_NIDNOT_FK" foreign key ("NID_NOTCOD") references "GUS_NOTICS" ("NOT_CODI") 
/

Alter table "GUS_CONVOCATORIA" add Constraint "GUS_CONVOC_PREGUNTA_CONF_FK" foreign key ("PREGUNTA_CONFIRMACION_ID") references "GUS_PREGUN" ("PRE_CODI") 
/

Alter table "GUS_CONVOCATORIA" add Constraint "GUS_CONVOC_PREGUNTA_CORR_FK" foreign key ("PREGUNTA_CORREO_ID") references "GUS_PREGUN" ("PRE_CODI") 
/

Alter table "GUS_PREIDI" add Constraint "GUS_PIDPRE_FK" foreign key ("PID_PRECOD") references "GUS_PREGUN" ("PRE_CODI") 
/

Alter table "GUS_RESPUS" add Constraint "GUS_RESPRE_FK" foreign key ("RES_PRECOD") references "GUS_PREGUN" ("PRE_CODI") 
/

Alter table "GUS_CONVOCATORIA" add Constraint "GUS_CONVOC_RESPUESTA_CONF_FK" foreign key ("RESPUESTA_CONFIRMACION_ID") references "GUS_RESPUS" ("RES_CODI") 
/

Alter table "GUS_ENCVOT" add Constraint "GUS_VOT_IDRESP_FK" foreign key ("VOT_IDRESP") references "GUS_RESPUS" ("RES_CODI") 
/

Alter table "GUS_RESIDI" add Constraint "GUS_REIRES_FK" foreign key ("REI_RESCOD") references "GUS_RESPUS" ("RES_CODI") 
/

Alter table "GUS_USURESP" add Constraint "GUS_USRRESP_FK" foreign key ("USR_CODRESP") references "GUS_RESPUS" ("RES_CODI") 
/

Alter table "GUS_CONVOCATORIA" add Constraint "GUS_CONVOC_RESPUESTA_CORR_FK" foreign key ("RESPUESTA_CORREO_ID") references "GUS_RESPUS" ("RES_CODI") 
/

Alter table "GUS_FAQ" add Constraint "GUS_FAQTEM_FK" foreign key ("FAQ_CODTEM") references "GUS_TEMAS" ("TEM_CODI") 
/

Alter table "GUS_TEMIDI" add Constraint "GUS_TIDTEM_FK" foreign key ("TID_TEMCOD") references "GUS_TEMAS" ("TEM_CODI") 
/

Alter table "GUS_COMPOS" add Constraint "GUS_CMPTPN_FK" foreign key ("CMP_TIPO") references "GUS_TPNOTI" ("TPN_CODI") 
/

Alter table "GUS_NOTICS" add Constraint "GUS_NOTTPN_FK" foreign key ("NOT_TIPO") references "GUS_TPNOTI" ("TPN_CODI") 
/

Alter table "GUS_TPNIDI" add Constraint "GUS_TPITPN_FK" foreign key ("TPI_TIPCOD") references "GUS_TPNOTI" ("TPN_CODI") 
/

Alter table "GUS_USURESP" add Constraint "GUS_USRUSU_FK" foreign key ("USR_CODUSU") references "GUS_USUARIENC" ("USE_CODI") 
/


-- SEQUÈNCIES

CREATE SEQUENCE "GUS_SEQUSUENC"
/

CREATE SEQUENCE "GUS_SEQCMP"
/

CREATE SEQUENCE "GUS_SEQCON"
/

CREATE SEQUENCE "GUS_SEQBAN"
/

CREATE SEQUENCE "GUS_SEQAGE"
/

CREATE SEQUENCE "GUS_SEQACT"
/

CREATE SEQUENCE "GUS_SEQ_ALL"
/

CREATE SEQUENCE "GUS_SEQTPN"
/

CREATE SEQUENCE "GUS_SEQTIP"
/

CREATE SEQUENCE "GUS_SEQTEM"
/

CREATE SEQUENCE "GUS_SEQSTA"
/

CREATE SEQUENCE "GUS_SEQRES"
/

CREATE SEQUENCE "GUS_SEQRDA"
/

CREATE SEQUENCE "GUS_SEQPRE"
/

CREATE SEQUENCE "GUS_SEQNOT"
/

CREATE SEQUENCE "GUS_SEQMPR"
/

CREATE SEQUENCE "GUS_SEQMIC"
/

CREATE SEQUENCE "GUS_SEQMEN"
/

CREATE SEQUENCE "GUS_SEQFRQ"
/

CREATE SEQUENCE "GUS_SEQFRM"
/

CREATE SEQUENCE "GUS_SEQFLI"
/

CREATE SEQUENCE "GUS_SEQFAQ"
/

CREATE SEQUENCE "GUS_SEQENC"
/

CREATE SEQUENCE "GUS_SEQDOC"
/

CREATE SEQUENCE "GUS_SQM_ALL"
/

CREATE SEQUENCE "GUS_CONV_SEQ"
/

CREATE SEQUENCE "GUS_LDISTRB_SEQ"
/




-- Comentaris de taules

Comment on table "GUS_ACTIDI" is 'Tabla que contiene los atributos dependientes de idioma de los tipos de registros que incluiye una agenda de un microsite.'
/
Comment on table "GUS_ACTIVI" is 'Tabla que contiene los tipos de registros que incluiye una agenda de un microsite.'
/
Comment on table "GUS_AGEIDI" is 'Tabla que contiene los atributos dependientes de idioma de los registros que incluiye una agenda de un microsite.'
/
Comment on table "GUS_AGENDA" is 'Tabla que contiene los registros de la agenda de un microsite.'
/
Comment on table "GUS_BANIDI" is 'Tabla que contiene los atributos dependientes de idioma de los elementos de tipo banner de un microsite.'
/
Comment on table "GUS_BANNER" is 'Tabla que contiene los elementos de tipo banner definidos para un microsite.'
/
Comment on table "GUS_CMPIDI" is 'Tabla que contiene los atributos dependientes de idioma del registro de componentes de un microsite.'
/
Comment on table "GUS_COMPOS" is 'Tabla que contiene el registro de componentes sobre los listados definidos para un microsite. Son distintas presentaciones para un informe, que pueden incluirse en un contenido.'
/
Comment on table "GUS_CONIDI" is 'Tabla que contiene los atributos dependientes de idioma de los contenidos de un microsite.'
/
Comment on table "GUS_CONTEN" is 'Tabla que contiene los contenidos de un microsite. Son los contenidos estáticos o los enlaces externos asociados al menú.'
/
Comment on table "GUS_DOCUS" is 'Tabla que contiene los archivos (imágenes, pdf, doc, etc..) utilizados en los distintos elementos de un microsite.'
/
Comment on table "GUS_ENCIDI" is 'Tabla que contiene los atributos dependientes de idioma para las encuestas de un microsite.'
/
Comment on table "GUS_ENCUST" is 'Tabla que contiene el registro de encuestas definidas para un microsite. Se compone de una relación de preguntas y respuestas.'
/
Comment on table "GUS_ENCVOT" is 'Tabla que contiene los resultados de las contestaciones sobre las preguntas asociadas a una encuesta publicadas para un microsite.'
/
Comment on table "GUS_FAQ" is 'Tabla que contiene el registro de preguntas frecuentes creadas para un microsite.'
/
Comment on table "GUS_FAQIDI" is 'Tabla que contiene los atributos dependientes de idioma para las preguntas frecuentes de un microsite.'
/
Comment on table "GUS_FRMCON" is 'Tabla que contiene el registro de los formularios de contacto definidos para un microsite. Se compone de un número de campos que incluirá el formulario.'
/
Comment on table "GUS_FRMIDI" is 'Tabla que contiene los atributos dependientes de idioma del registro de campos de un formulario de un microsite.'
/
Comment on table "GUS_FRMLIN" is 'Tabla que contiene la relación de campos y su tipo asociados a un formularios de contacto definidos para un microsite.'
/
Comment on table "GUS_IDIOMA" is 'Tabla que contiene la lista de idiomas gestionados por los microsites. La activación de un nuevo idioma no solo supone el registro de esta tabla.'
/
Comment on table "GUS_MENU" is 'Tabla que contiene la definición del arbol de menú asociado a un microsite. Define los nodos de menú, su tipo y funcionamiento y enlaza con los contenidos del microsite.'
/
Comment on table "GUS_MICIDI" is 'Tabla que contiene los atributos dependientes de idioma para la definición de un elementos de un tipo de listado de un microsite.'
/
Comment on table "GUS_MICPRO" is 'FUNCIONALIDAD A ELIMINAR. Tabla que contiene la relación de procedimientos o trámites asociados a un microsite.'
/
Comment on table "GUS_MICROS" is 'Tabla que contiene la definición del microsite con todos los atributos de caracter general que determinan su funcionamiento.'
/
Comment on table "GUS_MICUSU" is 'Tabla que relaciona un usuario con todos los microsites en los que tiene permiso, y el perfil con el que actual sobre él.'
/
Comment on table "GUS_MNUIDI" is 'Tabla que contiene los atributos dependientes de idioma para el menú de un microsite.'
/
Comment on table "GUS_MUSUAR" is 'Tabla que contiene la ficha de los usuarios del entorno de los microsite. El alta se realiza coincidir con un usuario de CAIB. Se utiliza para controlar que un usuario CAIB acceda un microsite con un perfil determinado.'
/
Comment on table "GUS_NOTICS" is 'Tabla que contiene el registro de elemetos a publicar en la lista para un tipo de listado definidos en un microsite.'
/
Comment on table "GUS_PREGUN" is 'Tabla que contiene la relación de preguntas asociadas a una encuesta definidas para un microsite.'
/
Comment on table "GUS_PREIDI" is 'Tabla que contiene los atributos dependientes de idioma para las preguntas asociadas a una encuesta de un microsite.'
/
Comment on table "GUS_RESIDI" is 'Tabla que contiene los atributos dependientes de idioma para las respuestas tabuladas de las preguntas asociadas a una encuesta de un microsite.'
/
Comment on table "GUS_RESPUS" is 'Tabla que contiene la relación de respuestas tabuladas a una pregunta asociadas a una encuesta definidas para un microsite.'
/
Comment on table "GUS_STATS" is 'Tabla que contiene el registro de las estadísticas de acceso a los distintos elementos de un microsite.'
/
Comment on table "GUS_TEMAS" is 'Tabla que contiene el registro de agrupadores definidos para las preguntas frecuentes que se creen para un microsite.'
/
Comment on table "GUS_TEMIDI" is 'Tabla que contiene los atributos dependientes de idioma para la definición de los agrupadores de preguntas frecuentes de un microsite.'
/
Comment on table "GUS_TIPSER" is 'Tabla que contiene el registro de los servicios de un microsite. La creación de un nuevo servicio no solo supone el registro de esta tabla.'
/
Comment on table "GUS_TPNIDI" is 'Tabla que contiene los atributos dependientes de idioma para la definición de un tipo de listado de un microsite.'
/
Comment on table "GUS_TPNOTI" is 'Tabla que contiene el registro de los distintos tipos de listados definidos para un microsite y que se utilizarán para la publicación de listas de elementos de los distintos tipo posible.'
/
Comment on table "GUS_USUARIENC" is 'Tabla que contiene la ficha de los usuarios de los usuarios que rellenan una encuesta.'
/
Comment on table "GUS_USURESP" is 'Tabla que relaciona un usuario que rellena una encuesta con las respuestas.'
/

-- Comentaris de columnes

Comment on column "GUS_ACTIDI"."ATI_CODIDI" is 'Identificador del idioma ca,es,de,en'
/
Comment on column "GUS_ACTIDI"."ATI_ACTCOD" is 'Identificador de la actividad'
/
Comment on column "GUS_ACTIDI"."ATI_NOMBRE" is 'Nombre de la actividad'
/
Comment on column "GUS_ACTIVI"."ACT_CODI" is 'Identificador de la Actividad'
/
Comment on column "GUS_ACTIVI"."ACT_MICCOD" is 'Identificador del microsite'
/
Comment on column "GUS_AGEIDI"."AID_CODIDI" is 'Identificador del idioma ca,es,de,en'
/
Comment on column "GUS_AGEIDI"."AID_AGECOD" is 'Código Agenda'
/
Comment on column "GUS_AGEIDI"."AID_DESCRI" is 'Descripción evento de la Agenda'
/
Comment on column "GUS_AGEIDI"."AID_DOCU" is 'Código documento asociado'
/
Comment on column "GUS_AGEIDI"."AID_TITULO" is 'Título del registro'
/
Comment on column "GUS_AGEIDI"."AID_URL" is 'URL de información asociada al registro'
/
Comment on column "GUS_AGEIDI"."AID_IMAGEN" is 'Código imagen'
/
Comment on column "GUS_AGEIDI"."AID_URLNOM" is 'Descripción donde se implementará el enlace de la URL'
/
Comment on column "GUS_AGENDA"."AGE_CODI" is 'Identificador Registro de Agenda'
/
Comment on column "GUS_AGENDA"."AGE_MICCOD" is 'Código de Microsite'
/
Comment on column "GUS_AGENDA"."AGE_ACTIVI" is 'Identificador de la actividad'
/
Comment on column "GUS_AGENDA"."AGE_ORGANI" is 'Organisnmo del Evento'
/
Comment on column "GUS_AGENDA"."AGE_INICIO" is 'Fecha Inicio del Evento'
/
Comment on column "GUS_AGENDA"."AGE_FIN" is 'Fecha de fin del Evento'
/
Comment on column "GUS_AGENDA"."AGE_VISIB" is 'Visibilidad del evento'
/
Comment on column "GUS_BANIDI"."BID_CODIDI" is 'Identificador del idioma ca,es,de,en'
/
Comment on column "GUS_BANIDI"."BID_BANCOD" is 'Codigo Banner'
/
Comment on column "GUS_BANIDI"."BID_TITULO" is 'Título de la respuesta'
/
Comment on column "GUS_BANIDI"."BID_URL" is 'URL asociado al elemento Banner'
/
Comment on column "GUS_BANIDI"."BID_ALT" is 'Alt asociado a la imágen del Banner'
/
Comment on column "GUS_BANIDI"."BID_IMAGEN" is 'Código de archivo de la imágen'
/
Comment on column "GUS_BANNER"."BAN_CODI" is 'Identificador de Banner'
/
Comment on column "GUS_BANNER"."BAN_MICCOD" is 'Identificador de microsite'
/
Comment on column "GUS_BANNER"."BAN_CADUCA" is 'Fecha caducidad'
/
Comment on column "GUS_BANNER"."BAN_PUBLIC" is 'Fecha de publicación'
/
Comment on column "GUS_BANNER"."BAN_VISIB" is 'Visible'
/
Comment on column "GUS_CMPIDI"."CPI_CODIDI" is 'Identificador del idioma ca,es,de,en'
/
Comment on column "GUS_CMPIDI"."CPI_CMPCOD" is 'Identificador del componente'
/
Comment on column "GUS_CMPIDI"."CPI_TITULO" is 'Nombre del componente'
/
Comment on column "GUS_COMPOS"."CMP_CODI" is 'Identificador del componente'
/
Comment on column "GUS_COMPOS"."CMP_TIPO" is 'Tipo de componente'
/
Comment on column "GUS_COMPOS"."CMP_MICCOD" is 'Identificador de microsite'
/
Comment on column "GUS_COMPOS"."CMP_NOMBRE" is 'titulo del componente'
/
Comment on column "GUS_COMPOS"."CMP_SOLOIM" is 'Indica si es de tipo imagen'
/
Comment on column "GUS_COMPOS"."CMP_NUMELE" is 'Número de elementos'
/
Comment on column "GUS_COMPOS"."CMP_ORDEN" is 'Orden de aparición'
/
Comment on column "GUS_COMPOS"."CMP_FILAS" is 'Número de filas'
/
Comment on column "GUS_CONIDI"."CID_CODIDI" is 'Identificador del idioma ca,es,de,en'
/
Comment on column "GUS_CONIDI"."CID_CONCOD" is 'Identificador de contenido'
/
Comment on column "GUS_CONIDI"."CID_TITULO" is 'Título del contenido'
/
Comment on column "GUS_CONIDI"."CID_TEXTO" is 'Para contenido HTML. Código HTML asociado principal al contenido que se mostrará al acceder a él'
/
Comment on column "GUS_CONIDI"."CID_URL" is 'Para contenido externo. URL asociado al contenido al que se redireccionará al acceder a él'
/
Comment on column "GUS_CONIDI"."CID_TXBETA" is 'Para contenido HTML. Código HTML asociado al contenido en versión Beta hasta su validación y paso a CID_TEXTO'
/
Comment on column "GUS_CONTEN"."CON_CODI" is 'Identificador de contenido'
/
Comment on column "GUS_CONTEN"."CON_CADUCA" is 'Fecha de caducidad'
/
Comment on column "GUS_CONTEN"."CON_PUBLIC" is 'Fecha de Publicación'
/
Comment on column "GUS_CONTEN"."CON_VISIB" is 'Indica si es visible'
/
Comment on column "GUS_CONTEN"."CON_ORDEN" is 'Orden Aparición'
/
Comment on column "GUS_CONTEN"."CON_MNUCOD" is 'Identificador menú'
/
Comment on column "GUS_CONTEN"."CON_IMGMEN" is 'Indentificador imagen'
/
Comment on column "GUS_DOCUS"."DCM_CODI" is 'Identificador de documento'
/
Comment on column "GUS_DOCUS"."DCM_MICCOD" is 'Identificador del microsite'
/
Comment on column "GUS_DOCUS"."DCM_DATOS" is 'Identificador archivo'
/
Comment on column "GUS_DOCUS"."DCM_NOMBRE" is 'Nombre del documento físico'
/
Comment on column "GUS_DOCUS"."DCM_TIPO" is 'Identificacor de ficha'
/
Comment on column "GUS_DOCUS"."DCM_TAMANO" is 'identificador de procedimiento'
/
Comment on column "GUS_DOCUS"."DCM_PAGINA" is 'Orden aparición'
/
Comment on column "GUS_ENCIDI"."EID_CODIDI" is 'Identificador del idioma ca,es,de,en'
/
Comment on column "GUS_ENCIDI"."EID_ENCCOD" is 'Identificador de contenido'
/
Comment on column "GUS_ENCIDI"."EID_TITULO" is 'Título de la Encuesta'
/
Comment on column "GUS_ENCUST"."ENC_CODI" is 'Identidicador Encuesta'
/
Comment on column "GUS_ENCUST"."ENC_MICCOD" is 'Identificador de Microsite'
/
Comment on column "GUS_ENCUST"."ENC_PUBLIC" is 'Fecha de Publicación'
/
Comment on column "GUS_ENCUST"."ENC_CADUCA" is 'Fecha de Caducidad'
/
Comment on column "GUS_ENCUST"."ENC_VISIB" is 'Indica si es visible'
/
Comment on column "GUS_ENCUST"."ENC_PAGINA" is 'Número de página'
/
Comment on column "GUS_ENCUST"."ENC_MUESTR" is 'Indica si se muestran los resultados de las encuestas al publico'
/
Comment on column "GUS_ENCVOT"."VOT_CODI" is 'Identificador Dato Respuesta'
/
Comment on column "GUS_ENCVOT"."VOT_IDENCU" is 'Identificador Encuesta'
/
Comment on column "GUS_ENCVOT"."VOT_IDPREG" is 'Identificador Pregunta'
/
Comment on column "GUS_ENCVOT"."VOT_IDRESP" is 'Identificador de Respuesta'
/
Comment on column "GUS_ENCVOT"."VOT_INPUT" is 'Respuesta a la encuesta'
/
Comment on column "GUS_FAQ"."FAQ_CODI" is 'Identificador de Faq'
/
Comment on column "GUS_FAQ"."FAQ_MICCOD" is 'Identificador de Microsite'
/
Comment on column "GUS_FAQ"."FAQ_CODTEM" is 'Identificador del Tema'
/
Comment on column "GUS_FAQ"."FAQ_FECHA" is 'Fecha faq'
/
Comment on column "GUS_FAQ"."FAQ_VISIB" is 'Indica si es o no visible'
/
Comment on column "GUS_FAQIDI"."FID_CODIDI" is 'Identificador del idioma ca,es,de,en'
/
Comment on column "GUS_FAQIDI"."FID_FAQCOD" is 'Identificador de contenido'
/
Comment on column "GUS_FAQIDI"."FID_PREGUN" is 'Texto de la Pregunta frecuente'
/
Comment on column "GUS_FAQIDI"."FID_RESPU" is 'Texto de la Respuesta frecuente'
/
Comment on column "GUS_FAQIDI"."FID_URL" is 'URL asociado a la pregunta con información de interés'
/
Comment on column "GUS_FAQIDI"."FID_URLNOM" is 'Texto del link para la URL asociado a la pregunta con información de interés'
/
Comment on column "GUS_FRMCON"."FRM_CODI" is 'Identidicador Formulario de Contacto'
/
Comment on column "GUS_FRMCON"."FRM_MICCOD" is 'Identificador de microsite'
/
Comment on column "GUS_FRMCON"."FRM_EMAIL" is 'Direccion de correo'
/
Comment on column "GUS_FRMCON"."FRM_VISIB" is 'Indica si es visibile'
/
Comment on column "GUS_FRMIDI"."RID_CODIDI" is 'Identificador del idioma ca,es,de,en'
/
Comment on column "GUS_FRMIDI"."RID_FLICOD" is 'Identificador de contenido'
/
Comment on column "GUS_FRMIDI"."RID_TEXTO" is 'Nomber del campo del formulario'
/
Comment on column "GUS_FRMLIN"."FLI_CODI" is 'Identificador de la linea'
/
Comment on column "GUS_FRMLIN"."FLI_VISIB" is 'Indica si es visible'
/
Comment on column "GUS_FRMLIN"."FLI_TAMANO" is 'Indica el tamaño'
/
Comment on column "GUS_FRMLIN"."FLI_LINEAS" is 'Número de líneas'
/
Comment on column "GUS_FRMLIN"."FLI_ORDEN" is 'Orden Aparición'
/
Comment on column "GUS_FRMLIN"."FLI_OBLIGA" is 'Indica Obligacion de rellenar este campo en el formulario'
/
Comment on column "GUS_IDIOMA"."IDI_CODI" is 'Identificador Idioma'
/
Comment on column "GUS_IDIOMA"."IDI_ORDEN" is 'Ordenación'
/
Comment on column "GUS_IDIOMA"."IDI_CODEST" is 'Identificador para estadísticas'
/
Comment on column "GUS_IDIOMA"."IDI_NOMBRE" is 'Nombre del Idioma'
/
Comment on column "GUS_MENU"."MNU_CODI" is 'Identificador del menú'
/
Comment on column "GUS_MENU"."MNU_MICCOD" is 'Identificador de microsite'
/
Comment on column "GUS_MENU"."MNU_ORDEN" is 'Orden Aparición menú'
/
Comment on column "GUS_MENU"."MNU_IMGMEN" is 'Imagen menú'
/
Comment on column "GUS_MENU"."MNU_PADRE" is 'Nodo padre.  0=raiz o sin padre'
/
Comment on column "GUS_MENU"."MNU_VISIB" is 'Visiblidad del menu'
/
Comment on column "GUS_MENU"."MNU_MODO" is 'F - Fijo, C - Carpeta'
/
Comment on column "GUS_MICIDI"."MID_CODIDI" is 'Identificador del idioma ca,es,de,en'
/
Comment on column "GUS_MICIDI"."MID_MICCOD" is 'Identificador del microsite'
/
Comment on column "GUS_MICIDI"."MID_TITULO" is 'Nomber del microsite'
/
Comment on column "GUS_MICPRO"."MPR_CODI" is 'Identificador Procedimiento'
/
Comment on column "GUS_MICPRO"."MPR_MICCOD" is 'Identificador de microsite'
/
Comment on column "GUS_MICPRO"."MPR_PROSEL" is 'Procedimiento'
/
Comment on column "GUS_MICROS"."MIC_CODI" is 'Identificador del microsite'
/
Comment on column "GUS_MICROS"."MIC_CODUNI" is 'Codigo Unidad orgánica'
/
Comment on column "GUS_MICROS"."MIC_FECHA" is 'Fecha mircrisite'
/
Comment on column "GUS_MICROS"."MIC_VISIB" is 'Indica si es visible.'
/
Comment on column "GUS_MICROS"."MIC_RESTRI" is 'N=internet, S=intranet'
/
Comment on column "GUS_MICROS"."MIC_SERSEL" is 'Ultimos contenidos modificados. Listado de ids de contenidos separados por ;'
/
Comment on column "GUS_MICROS"."MIC_BUSCA" is 'Incluir buscador'
/
Comment on column "GUS_MICROS"."MIC_CLAVE" is 'Identificador de clave unica'
/
Comment on column "GUS_MICROS"."MIC_MNUCRP" is 'Menu corporativo'
/
Comment on column "GUS_MICROS"."MIC_CSSPTR" is 'CSS genérico base utilizado por el microsite si no hay css personalizado. (A)zul, (R)ojo, (V)erde, (N)egro, (G)amarillo y (M)agenta'
/
Comment on column "GUS_MICUSU"."MIU_CODUSU" is 'Usuario del Microsite'
/
Comment on column "GUS_MICUSU"."MIU_CODMIC" is 'Identificador Microsite'
/
Comment on column "GUS_MNUIDI"."MDI_MNUCOD" is 'Identificador de contenido'
/
Comment on column "GUS_MNUIDI"."MDI_CODIDI" is 'Identificador del idioma ca,es,de,en'
/
Comment on column "GUS_MNUIDI"."MDI_NOMBRE" is 'Nomber del nodo del menú'
/
Comment on column "GUS_MUSUAR"."MSU_CODI" is 'Identificador usuario de microsites'
/
Comment on column "GUS_MUSUAR"."MSU_USERNA" is 'Usuario de acceso al back de microsites. Debe coincidir con un usuario CAIB'
/
Comment on column "GUS_MUSUAR"."MSU_PASSWO" is 'No utilizado en la gestión actual: Para usuarios externos a CAIB: Password del usuario'
/
Comment on column "GUS_MUSUAR"."MSU_NOMBRE" is 'Nombre completo del usuario'
/
Comment on column "GUS_MUSUAR"."MSU_PERFIL" is 'Perfil del usuriario. "sacadmin"-Administrador, "sacsuper"-Supervisor, "sacoper"-Operador'
/
Comment on column "GUS_NOTICS"."NOT_CODI" is 'Identificador de noticia'
/
Comment on column "GUS_NOTICS"."NOT_MICCOD" is 'Identificador de microsite'
/
Comment on column "GUS_NOTICS"."NOT_IMAGEN" is 'Imagen noticia'
/
Comment on column "GUS_NOTICS"."NOT_CADUCA" is 'Fecha caducidad'
/
Comment on column "GUS_NOTICS"."NOT_PUBLIC" is 'Fecha de publicación'
/
Comment on column "GUS_NOTICS"."NOT_VISIB" is 'Indica si es visible'
/
Comment on column "GUS_NOTICS"."NOT_VISWEB" is 'Indica si es visible en web'
/
Comment on column "GUS_NOTICS"."NOT_TIPO" is 'Tipo de noticia'
/
Comment on column "GUS_NOTICS"."NOT_ORDEN" is 'Orden de aparicion'
/
Comment on column "GUS_NOTIDI"."NID_CODIDI" is 'Identificador del idioma ca,es,de,en'
/
Comment on column "GUS_NOTIDI"."NID_NOTCOD" is 'Identificador del documento'
/
Comment on column "GUS_NOTIDI"."NID_TITULO" is 'Titulo del documento'
/
Comment on column "GUS_NOTIDI"."NID_SUBTIT" is 'SubTitulo del documento'
/
Comment on column "GUS_NOTIDI"."NID_FUENTE" is 'Nombre de la fuente del origen del documento'
/
Comment on column "GUS_NOTIDI"."NID_URL" is 'URL del enlace a información externa'
/
Comment on column "GUS_NOTIDI"."NID_TEXTO" is 'Texto descripción del documento'
/
Comment on column "GUS_NOTIDI"."NID_URLNOM" is 'Nombre del enlace a información externa'
/
Comment on column "GUS_PREGUN"."PRE_CODI" is 'Identificador de pregunta'
/
Comment on column "GUS_PREGUN"."PRE_ENCCOD" is 'Identificador de encuesta'
/
Comment on column "GUS_PREGUN"."PRE_IMAGEN" is 'Imagen pregunta'
/
Comment on column "GUS_PREGUN"."PRE_MULTIR" is 'Mutirespuesta'
/
Comment on column "GUS_PREGUN"."PRE_VISCMP" is 'Visible el componente'
/
Comment on column "GUS_PREGUN"."PRE_OBLIGA" is 'Obligacion responder'
/
Comment on column "GUS_PREGUN"."PRE_VISIB" is 'Indica si es visible'
/
Comment on column "GUS_PREGUN"."PRE_ORDEN" is 'Orden de Aparicion'
/
Comment on column "GUS_PREGUN"."PRE_NRESP" is 'Numero de Respuesta'
/
Comment on column "GUS_PREIDI"."PID_CODIDI" is 'Identificador del idioma ca,es,de,en'
/
Comment on column "GUS_PREIDI"."PID_PRECOD" is 'Identificador de la preguta en la ecuenta'
/
Comment on column "GUS_PREIDI"."PID_TITULO" is 'Título de la pregunta de la encuesta'
/
Comment on column "GUS_RESIDI"."REI_CODIDI" is 'Identificador del idioma ca,es,de,en'
/
Comment on column "GUS_RESIDI"."REI_RESCOD" is 'Codigo Respuesta'
/
Comment on column "GUS_RESIDI"."REI_TITULO" is 'Título de la respuesta'
/
Comment on column "GUS_RESPUS"."RES_CODI" is 'Codigo Respuesta'
/
Comment on column "GUS_RESPUS"."RES_PRECOD" is 'Codigo de Pregunta'
/
Comment on column "GUS_RESPUS"."RES_ORDEN" is 'Orden aparicion'
/
Comment on column "GUS_RESPUS"."RES_NRESP" is 'Número Respuesta'
/
Comment on column "GUS_RESPUS"."RES_TIPO" is 'Tipo respuesta'
/
Comment on column "GUS_STATS"."STA_CODI" is 'Identificador de estadistica'
/
Comment on column "GUS_STATS"."STA_ITEM" is 'Identificador del Item'
/
Comment on column "GUS_STATS"."STA_MES" is 'Mes'
/
Comment on column "GUS_STATS"."STA_REF" is 'Referencia'
/
Comment on column "GUS_STATS"."STA_NACCES" is 'Número de Accesos'
/
Comment on column "GUS_STATS"."STA_MICCOD" is 'Identificador de Microsite'
/
Comment on column "GUS_STATS"."STA_PUB" is '0=privado, 1=publico'
/
Comment on column "GUS_TEMAS"."TEM_CODI" is 'Identificador del Tema agrupador de las faq'
/
Comment on column "GUS_TEMAS"."TEM_MICCOD" is 'Identificador Microsite'
/
Comment on column "GUS_TEMIDI"."TID_CODIDI" is 'Identificador del idioma ca,es,de,en'
/
Comment on column "GUS_TEMIDI"."TID_TEMCOD" is 'Codigo Tema'
/
Comment on column "GUS_TEMIDI"."TID_NOMBRE" is 'Nombre del tema que agrupa las faq'
/
Comment on column "GUS_TIPSER"."TPS_CODI" is 'Identificador tipo de servicio'
/
Comment on column "GUS_TIPSER"."TPS_NOMBRE" is 'Nombre de servicio'
/
Comment on column "GUS_TIPSER"."TPS_VISIB" is 'Indica si es o no visible'
/
Comment on column "GUS_TIPSER"."TPS_REF" is 'Referencia significativa'
/
Comment on column "GUS_TIPSER"."TPS_URL" is 'URL del action que lo gestiona'
/
Comment on column "GUS_TIPSER"."TPS_TIPO" is 'Tipo de servicio 0, 1, 2'
/
Comment on column "GUS_TPNIDI"."TPI_CODIDI" is 'Identificador del idioma ca,es,de,en'
/
Comment on column "GUS_TPNIDI"."TPI_TIPCOD" is 'Codigo tipo listado'
/
Comment on column "GUS_TPNIDI"."TPI_NOMBRE" is 'Nombre del listado'
/
Comment on column "GUS_TPNOTI"."TPN_CODI" is 'Identificador tipo de listado'
/
Comment on column "GUS_TPNOTI"."TPN_MICCOD" is 'Identificador Microsite'
/
Comment on column "GUS_TPNOTI"."TPN_TAMPAG" is 'Numero de registros si se aplica paginación'
/
Comment on column "GUS_TPNOTI"."TPN_TIPPAG" is 'Indica si se aplica paginación al listado 0.per nombre de registres/1.per any, segons data de publicació'
/
Comment on column "GUS_TPNOTI"."TPN_TPELEM" is 'Tipo de elemento: 0.Fitxa, 1.Enllaç, 2.Llista de Documents, 3.Conexió externa'
/
Comment on column "GUS_TPNOTI"."TPN_BUSCAR" is 'Indicador de si se aplica buscador al listado o no'
/
Comment on column "GUS_TPNOTI"."TPN_ORDEN" is '0=por campo orden; 1=por fecha publicacion ascendente; 2=por fecha publicacion descendente;3=por titulo'
/
Comment on column "GUS_TPNOTI"."TPN_XURL" is 'URL de conexion listado externo'
/
Comment on column "GUS_TPNOTI"."TPN_XJNDI" is 'No se utiliza: JNDI de conexion'
/
Comment on column "GUS_TPNOTI"."TPN_XLOCAL" is 'No se utiliza: EJB Local o Remoto, (L o R)'
/
Comment on column "GUS_TPNOTI"."TPN_XAUTH" is 'No se utiliza: Tipo de autenticacion, 1=no, 2=estandar, 3=caib'
/
Comment on column "GUS_TPNOTI"."TPN_CLASIF" is 'Clasificador de listados para el BackOffice de microsites'
/
Comment on column "GUS_USUARIENC"."USE_CODI" is 'Identificador usuario de encuestaa'
/
Comment on column "GUS_USUARIENC"."USE_NOMBRE" is 'Nombre completo del usuario'
/
Comment on column "GUS_USUARIENC"."USE_OBSERV" is 'Observaciones '
/
Comment on column "GUS_USURESP"."USR_CODUSU" is 'Identificador Usuario'
/
Comment on column "GUS_USURESP"."USR_CODRESP" is 'Identificador Respuesta'
/




