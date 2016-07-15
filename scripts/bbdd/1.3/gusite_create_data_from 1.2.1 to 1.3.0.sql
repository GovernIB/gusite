--INTRODUCIR SOLRPD.
CREATE TABLE GUS_SOLRPD
  (
    SLP_ID     NUMBER NOT NULL ENABLE,
    SLP_TIPO   VARCHAR2(128 CHAR),
    SLP_IDELEM NUMBER,
    SLP_ACCION NUMBER,
    SLP_FECCRE DATE,
    SLP_FECIDX DATE,
    SLP_RESULT    NUMBER,
    SLP_TXTERR    VARCHAR2(300 BYTE),
    SLP_IDARCHIVO NUMBER,
    CONSTRAINT GUS_SOLRPD_PK PRIMARY KEY (SLP_ID)
  );
CREATE SEQUENCE GUS_SEQSOLR;

--INTRODUCIR SOLR JOB.
CREATE TABLE GUS_SOLJOB
  (
    JOB_ID NUMBER(19,0) NOT NULL ENABLE,
    JOB_FECINI DATE DEFAULT SYSDATE,
    JOB_FECFIN DATE,
    JOB_TIPO   NUMBER,
    CONSTRAINT "RSC_JOB_PK" PRIMARY KEY ("JOB_ID")
  );
  CREATE SEQUENCE GUS_SEQSOLJ;
    
COMMENT ON COLUMN "GUS_SOLJOB"."JOB_ID"
IS
  'Identificador de la clase.';
  COMMENT ON COLUMN "GUS_SOLJOB"."JOB_FECINI"
IS
  'Fecha de inicio';
  COMMENT ON COLUMN "GUS_SOLJOB"."JOB_FECFIN"
IS
  'Fecha fin';
  COMMENT ON COLUMN "GUS_SOLJOB"."JOB_TIPO"
IS
  'Tipo de job, siendo 1 TODOS LOS MICROSITES, 2 INDEXANDO SEGÚN .';