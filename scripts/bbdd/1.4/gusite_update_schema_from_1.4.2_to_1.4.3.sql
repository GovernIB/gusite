--Actualiza el lob a otro tablespace más acorde a los standard.
ALTER TABLE GUS_MICROS MODIFY MIC_TIPO_ACCESO NOT NULL; 
--Incluye un campo que indexa los microsites no indexados.
ALTER TABLE GUS_MICROS ADD MIC_INDEXADO NUMBER(1,0) DEFAULT 0;