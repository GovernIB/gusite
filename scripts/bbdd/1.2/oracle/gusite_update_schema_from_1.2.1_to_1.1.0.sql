
-- Eliminar funcionalitats de banners 
    -----> sin back: DELETE FROM GUS_TIPSER WHERE TPS_CODI = 3;
    -----> sin back: DELETE FROM GUS_TIPSER WHERE TPS_CODI = 6;

-- Un bug en las versiones anteriores hacía que el servicio 100 se repitiese como 9 veces
    -----> sin back

-- Corregimos algunos "not null" de versiones anteriores 
 ALTER TABLE GUS_ACTIVI modify (ACT_MICCOD null);
 /
ALTER TABLE GUS_AGENDA modify (AGE_MICCOD null);
/
 ALTER TABLE GUS_COMPOS modify (CMP_MICCOD null);
/
 ALTER TABLE GUS_CONVOCATORIA modify (MICROSITE_ID null);
/
 ALTER TABLE GUS_ENCUST modify (ENC_MICCOD null);
/
 ALTER TABLE GUS_FAQ modify (FAQ_MICCOD null);
/
 ALTER TABLE GUS_LDISTRIBUCION modify (MICROSITE_ID null);
/
 ALTER TABLE GUS_MENU modify (MNU_MICCOD null);
/
 ALTER TABLE GUS_MICUSU modify (MIU_CODMIC null);
/
 ALTER TABLE GUS_NOTICS modify (NOT_MICCOD null);
/
 ALTER TABLE GUS_TEMAS modify (TEM_MICCOD null);
 /
  -----> Se tiran las FKs creadas con el tema de idioma.
ALTER TABLE GUS_ACTIDI DROP constraint GUS_ATIIDI_FK;
/
ALTER TABLE GUS_AGEIDI DROP CONSTRAINT GUS_AIDIDI_FK;
/
ALTER TABLE GUS_CMPIDI DROP CONSTRAINT GUS_CPIIDI_FK;
/
ALTER TABLE GUS_CONIDI DROP CONSTRAINT GUS_CIDIDI_FK;
/
ALTER TABLE GUS_ENCIDI DROP CONSTRAINT GUS_EIDIDI_FK;
/
ALTER TABLE GUS_FAQIDI DROP CONSTRAINT GUS_FIDIDI_FK;
/
ALTER TABLE GUS_FRMIDI DROP CONSTRAINT GUS_RIDIDI_FK;
/
ALTER TABLE GUS_FRQIDI DROP CONSTRAINT GUS_FQIIDI_FK;
/
ALTER TABLE GUS_MICIDI DROP CONSTRAINT GUS_MIDIDI_FK;
/
ALTER TABLE GUS_MNUIDI DROP CONSTRAINT GUS_MDIIDI_FK;
/
ALTER TABLE GUS_NOTIDI DROP CONSTRAINT GUS_NIDIDI_FK;
/
ALTER TABLE GUS_PREIDI DROP CONSTRAINT GUS_PIDIDI_FK;
/
ALTER TABLE GUS_RESIDI DROP CONSTRAINT GUS_REIIDI_FK;
/
ALTER TABLE GUS_TEMIDI DROP CONSTRAINT GUS_TIDIDI_FK;
/
ALTER TABLE GUS_TPNIDI DROP CONSTRAINT GUS_TPIIDI_FK;
/ 
   ----> Se borran la tabla de auditoria y la secuencia asociada.
DROP TABLE GUS_AUDITORIA CASCADE CONSTRAINTS;
/
DROP SEQUENCE "GUS_SEQAUD";
/
	----> Borramos nueva columnas sobre uris
ALTER TABLE  "GUS_CONIDI" DROP COLUMN "CID_URI";
/
ALTER TABLE  "GUS_ENCIDI" DROP COLUMN "EID_URI";
/
ALTER TABLE  "GUS_MICROS" DROP COLUMN "MIC_URI";
/
ALTER TABLE  "GUS_MICROS" DROP COLUMN "MIC_ANALYTICS";
/
ALTER TABLE  "GUS_MICIDI" DROP COLUMN "MID_KEYWORDS";
/
ALTER TABLE  "GUS_MICIDI" DROP COLUMN "MID_DESCRIPTION";
/
ALTER TABLE  "GUS_NOTIDI" DROP COLUMN "NID_URI";
/
ALTER TABLE  "GUS_TPNIDI" DROP COLUMN "TPI_URI";
/

	-----> Borramos indices. (los indices desaparecen por borrar la columna)
--DROP INDEX  "GUS_CIDURI_I";
--/
--DROP INDEX  "GUS_EIDURI_I";
--/
--DROP INDEX  "GUS_NIDURI_I";
--/
--DROP INDEX  "GUS_TPNURI_I";
--/

	-----> Desactualizar los Roles
UPDATE GUS_MUSUAR SET msu_perfil = REPLACE(msu_perfil, 'gussuper', 'sacsuper');
UPDATE GUS_MUSUAR SET msu_perfil = REPLACE(msu_perfil, 'gusoper', 'sacoper');
UPDATE GUS_MUSUAR SET msu_perfil = REPLACE(msu_perfil, 'gusadmin', 'sacadmin');
UPDATE GUS_MUSUAR SET msu_perfil = REPLACE(msu_perfil, 'gussystem', 'sacsystem');
/
	-----> Quitamos campos en el microsite para el control de las versiones
ALTER TABLE GUS_MICROS DROP COLUMN MIC_VERSION;
/
ALTER TABLE GUS_MICROS DROP COLUMN MIC_TIPO_ACCESO;
/
 
	----> Dejamos la versión anterior cuando actualizamos a versión 5 los microsites antiguos sin css personalizado
update GUS_MICROS
  set MIC_RESTRI = (select MIC_RESTRI from GUS_MICROS_BACKUP where GUS_MICROS_BACKUP.mic_codi = gus_micros.mic_codi)
  ;


	----> Quitamos los campos añadidos.
ALTER TABLE GUS_MICROS DROP COLUMN MIC_FTECOD;
/
ALTER TABLE GUS_MICROS DROP COLUMN MIC_DESARROLLO;
/
ALTER TABLE GUS_COMPOS DROP COLUMN CMP_PPLCOD;
/
ALTER TABLE GUS_TPNOTI DROP COLUMN TPN_PPLCOD;

	----> Borramos todo lo asociado a las plantillas 
DROP TABLE "GUS_FR_TEMA" CASCADE CONSTRAINTS;
/
DROP TABLE "GUS_FR_PLANTILLA" CASCADE CONSTRAINTS;
/
DROP TABLE "GUS_FR_VERSION" CASCADE CONSTRAINTS;
/
DROP TABLE "GUS_FR_PERPLA" CASCADE CONSTRAINTS;
/
DROP TABLE "GUS_FR_ARCHIVO" CASCADE CONSTRAINTS;
/

	----> BORRAMOS SEQUÈNCIES
 DROP SEQUENCE "GUS_SEQFTE";
 /
 DROP SEQUENCE "GUS_SEQPLA";
 /
DROP SEQUENCE "GUS_SEQFVE";
/
 DROP SEQUENCE "GUS_SEQPPL";
 /
DROP SEQUENCE "GUS_SEQARC";
/
UPDATE GUS_TPNOTI SET TPN_FOTOSPORFILA = 1 WHERE TPN_FOTOSPORFILA = NULL;
UPDATE GUS_TPNOTI SET TPN_TAMPAG = 0 WHERE TPN_TAMPAG = NULL;

