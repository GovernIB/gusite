
-------------------------------------------------
-- redmine #874 Persistencia de los temas y extensiones por sitio web.
------------------------------------------------

ALTER table "GUS_MICROS" ADD (
	"MIC_FTECOD" Number(19,0),
	"MIC_DESARROLLO" Varchar2 (1) Default 'N' NOT NULL 
);

ALTER table "GUS_COMPOS" ADD  (
	"CMP_PPLCOD" Number(19,0)
) 
/

ALTER table "GUS_CONTEN" ADD (
	"CON_PPLCOD" Number(19,0)
) 
/

ALTER table "GUS_TPNOTI" ADD (
	"TPN_PPLCOD" Number(19,0)
) 
/

Create table "GUS_FR_TEMA" (
	"FTE_CODI" Number(19,0) NOT NULL ,
	"FTE_VERSION" Varchar2 (2) NOT NULL ,
	"FTE_TEMA_PADRE" Number(19,0) ,
	"FTE_NOMBRE" Varchar2 (255) NOT NULL  UNIQUE ,
	"FTE_CSS" Number,
	"FTE_ACTUALIZACION" Date Default SYSDATE NOT NULL ,
 Constraint "GUS_FTE_PK" primary key ("FTE_CODI") 
) 
/

-- ALTER table gus_fr_tema modify(fte_tema_padre null);

Create table "GUS_FR_PLANTILLA" (
	"PLA_CODI" Number(19,0) NOT NULL ,
	"PLA_VERSION" Varchar2 (2) NOT NULL ,
	"PLA_NOMBRE" Varchar2 (255) NOT NULL  UNIQUE ,
	"PLA_DESCRIPCION" Clob,
	"PLA_TITULO" VARCHAR2(255) NOT NULL,
 Constraint "GUS_PLA_PK" primary key ("PLA_CODI") 
) 
/

Create table "GUS_FR_VERSION" (
	"FVE_VERSION" Varchar2 (2) NOT NULL ,
	"FVE_NOMBRE" Varchar2 (255) NOT NULL  UNIQUE ,
 Constraint "GUS_FVE_PK" primary key ("FVE_VERSION") 
) 
/

Create table "GUS_FR_PERPLA" (
	"PPL_CODI" Number(19,0) NOT NULL ,
	"PPL_TITULO" Varchar2 (255) NOT NULL ,
	"PPL_PLACOD" Number(19,0) NOT NULL ,
	"PPL_ORDEN" Integer,
	"PPL_FTECOD" Number(19,0),
	"PPL_MICCOD" Number,
	"PPL_CONTENIDO" Clob,
 Constraint "GUS_PLT_PK" primary key ("PPL_CODI") 
) 
/

Create table "GUS_FR_ARCHIVO" (
	"ARC_CODI" Number NOT NULL ,
	"ARC_FTECOD" Number(19,0) NOT NULL ,
	"ARC_PATH" Varchar2 (256 CHAR),
	"ARC_DOCCOD" Number,
 Constraint "GUS_ARC_PK" primary key ("ARC_CODI") 
) 
/


Create UNIQUE Index "GUS_ARC_PATH_I" ON "GUS_FR_ARCHIVO" ("ARC_FTECOD","ARC_PATH") 
/



-- Create Foreign keys section
Create Index "GUS_FTE_DOC_I" ON "GUS_FR_TEMA" ("FTE_CSS")
/
Alter table "GUS_FR_TEMA" add Constraint "GUS_FTE_DOC_FK" foreign key ("FTE_CSS") references "GUS_DOCUS" ("DCM_CODI") 
/
Create Index "GUS_ARC_DCM_I" ON "GUS_FR_ARCHIVO" ("ARC_DOCCOD")
/
Alter table "GUS_FR_ARCHIVO" add Constraint "GUS_ARC_DCM_FK" foreign key ("ARC_DOCCOD") references "GUS_DOCUS" ("DCM_CODI") 
/
Create Index "GUS_MIC_PLT_I" ON "GUS_FR_PERPLA" ("PPL_MICCOD")
/
Alter table "GUS_FR_PERPLA" add Constraint "GUS_MIC_PLT_FK" foreign key ("PPL_MICCOD") references "GUS_MICROS" ("MIC_CODI") 
/
Create Index "GUS_FTE_FTE_I" ON "GUS_FR_TEMA" ("FTE_TEMA_PADRE")
/
Alter table "GUS_FR_TEMA" add Constraint "GUS_FTE_FTE_FK" foreign key ("FTE_TEMA_PADRE") references "GUS_FR_TEMA" ("FTE_CODI") 
/
Create Index "GUS_FTE_PLT_I" ON "GUS_FR_PERPLA" ("PPL_FTECOD")
/
Alter table "GUS_FR_PERPLA" add Constraint "GUS_FTE_PLT_FK" foreign key ("PPL_FTECOD") references "GUS_FR_TEMA" ("FTE_CODI") 
/
Create Index "GUS_FTE_ARC_I" ON "GUS_FR_ARCHIVO" ("ARC_FTECOD")
/
Alter table "GUS_FR_ARCHIVO" add Constraint "GUS_FTE_ARC_FK" foreign key ("ARC_FTECOD") references "GUS_FR_TEMA" ("FTE_CODI") 
/
Create Index "GUS_FTE_MIC_I" ON "GUS_MICROS" ("MIC_FTECOD")
/
Alter table "GUS_MICROS" add Constraint "GUS_FTE_MIC_FK" foreign key ("MIC_FTECOD") references "GUS_FR_TEMA" ("FTE_CODI") 
/
Create Index "GUS_PLA_PLT_I" ON "GUS_FR_PERPLA" ("PPL_PLACOD")
/
Alter table "GUS_FR_PERPLA" add Constraint "GUS_PLA_PLT_FK" foreign key ("PPL_PLACOD") references "GUS_FR_PLANTILLA" ("PLA_CODI") 
/
Create Index "GUS_FVE_FAR_I" ON "GUS_FR_PLANTILLA" ("PLA_VERSION")
/
Alter table "GUS_FR_PLANTILLA" add Constraint "GUS_FVE_FAR_FK" foreign key ("PLA_VERSION") references "GUS_FR_VERSION" ("FVE_VERSION") 
/
Create Index "GUS_FVE_FTE_I" ON "GUS_FR_TEMA" ("FTE_VERSION")
/
Alter table "GUS_FR_TEMA" add Constraint "GUS_FVE_FTE_FK" foreign key ("FTE_VERSION") references "GUS_FR_VERSION" ("FVE_VERSION") 
/
Create Index "GUS_PPL_CON_I" ON "GUS_CONTEN" ("CON_PPLCOD")
/
Alter table "GUS_CONTEN" add Constraint "GUS_PPL_CON_FK" foreign key ("CON_PPLCOD") references "GUS_FR_PERPLA" ("PPL_CODI") 
/
Create Index "GUS_PPL_TPN_I" ON "GUS_TPNOTI" ("TPN_PPLCOD")
/
Alter table "GUS_TPNOTI" add Constraint "GUS_PPL_TPN_FK" foreign key ("TPN_PPLCOD") references "GUS_FR_PERPLA" ("PPL_CODI") 
/
Create Index "GUS_PPL_CMP_I" ON "GUS_COMPOS" ("CMP_PPLCOD")
/
Alter table "GUS_COMPOS" add Constraint "GUS_PPL_CMP_FK" foreign key ("CMP_PPLCOD") references "GUS_FR_PERPLA" ("PPL_CODI") 
/


-- Create Table comments section

Comment on table "GUS_FR_TEMA" is 'Tema del front'
/
Comment on table "GUS_FR_PLANTILLA" is 'Tabla que identifica las plantillas que se pueden implementar (sobreescribir) para una versión determinada de front'
/
Comment on table "GUS_FR_VERSION" is 'Versión de front / identifica las versiones para su implementación en temas'
/
Comment on table "GUS_FR_PERPLA" is 'Personalización de una plantilla'
/
Comment on table "GUS_FR_ARCHIVO" is 'Tabla que contiene los archivos (imágenes, js, etc.) utilizados en los distintos elementos de un tema'
/

-- Create Attribute comments section

Comment on column "GUS_FR_TEMA"."FTE_NOMBRE" is 'Nom del tema'
/
Comment on column "GUS_FR_TEMA"."FTE_CSS" is 'CSS General del tema'
/
Comment on column "GUS_FR_TEMA"."FTE_ACTUALIZACION" is 'Data d''actualització del tema'
/
Comment on column "GUS_FR_PLANTILLA"."PLA_NOMBRE" is 'Nombre (identificador) de la plantilla'
/
Comment on column "GUS_FR_PLANTILLA"."PLA_DESCRIPCION" is 'Descripción y documentación de la plantilla'
/
Comment on column "GUS_FR_PLANTILLA"."PLA_TITULO" is 'Título de la plantilla'
/
Comment on column "GUS_FR_VERSION"."FVE_VERSION" is 'Versión de GUSITE para la que se ha desarrollado el tema'
/
Comment on column "GUS_FR_VERSION"."FVE_NOMBRE" is 'Nom del tema'
/
Comment on column "GUS_FR_PERPLA"."PPL_TITULO" is 'Título de la plantilla'
/
Comment on column "GUS_FR_PERPLA"."PPL_ORDEN" is 'Número de orden de la personalización de plantilla'
/
Comment on column "GUS_FR_PERPLA"."PPL_CONTENIDO" is 'Contenido de la plantilla'
/
Comment on column "GUS_FR_ARCHIVO"."ARC_CODI" is 'Identificador de documento'
/
Comment on column "GUS_FR_ARCHIVO"."ARC_PATH" is 'Ruta virtual del archivo'
/


-- SEQUÈNCIES

CREATE SEQUENCE "GUS_SEQFTE"
/

CREATE SEQUENCE "GUS_SEQPLA"
/

CREATE SEQUENCE "GUS_SEQFVE"
/

CREATE SEQUENCE "GUS_SEQPPL"
/

CREATE SEQUENCE "GUS_SEQARC"
/


