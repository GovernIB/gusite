Drop table "GUS_FCOIDI" CASCADE CONSTRAINTS;

CREATE TABLE "GUS_FCOIDI" (
    "FCI_CODIDI" Varchar2 (6 CHAR) NOT NULL ,
    "FCI_FCOCOD" Number NOT NULL ,
    "FCI_NOMBRE" Varchar2 (100 CHAR),
    "FCI_URI" Varchar2 (125) NOT NULL ,
primary key ("FCI_CODIDI","FCI_FCOCOD") 
);

Create Index "GUS_FCOIDI_I"    ON "GUS_FCOIDI" ("FCI_CODIDI","FCI_URI") ;
Alter table "GUS_FCOIDI"               add  foreign key ("FCI_FCOCOD")          references "GUS_FRMCON" ("FRM_CODI") ;
Comment on table "GUS_FCOIDI" is 'Tabla que contiene los atributos dependientes de idioma para la definición de un formulario de contacto de un microsite.';
Comment on column "GUS_FCOIDI"."FCI_CODIDI" is 'Identificador del idioma ca,es,de,en,fr';
Comment on column "GUS_FCOIDI"."FCI_FCOCOD" is 'Codigo formulario de contacto';
Comment on column "GUS_FCOIDI"."FCI_NOMBRE" is 'Nombre del formulario de contacto';
Comment on column "GUS_FCOIDI"."FCI_URI"    is 'Uri para formar las urls del formulario de contacto';


ALTER TABLE GUS_FCOIDI ADD (
  CONSTRAINT GUS_FCIIDI_FK 
  FOREIGN KEY (FCI_CODIDI) 
  REFERENCES GUS_IDIOMA (IDI_CODI)
  ENABLE VALIDATE);
