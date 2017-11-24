--Incluye un campo que indexa los microsites no indexados.
ALTER TABLE GUS_MICROS ADD MIC_INDEXADO NUMBER(1,0) DEFAULT 0;