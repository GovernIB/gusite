--Actualiza el lob a otro tablespace más acorde a los standard.
ALTER TABLE GUS_MICROS MODIFY MIC_TIPO_ACCESO NOT NULL; 
--Incluye un campo que indexa los microsites no indexados.
ALTER TABLE GUS_MICROS ADD MIC_INDEXADO NUMBER(1,0) DEFAULT 0;
--Incluye un campo en job para saber si ha finalizado, principalmente para las tareas de jobs.
ALTER TABLE GUS_SOLJOB ADD JOB_FINALI NUMBER DEFAULT 0;
--Incluye un campo para realizar un resumen de cuantos microsites lleva indexados.
ALTER TABLE GUS_SOLJOB ADD JOB_RESUME VARCHAR2(2000);
--Indica si un microsite se ha indexado correctamente o no.
ALTER TABLE GUS_MICROS ADD MIC_IDXCOR NUMBER(2) DEFAULT -1;

--Aprovechamos e incluimos comentarios en la tabla de jobs de solr.
COMMENT ON TABLE GUS_SOLJOB IS 'Tabla para las tareas que se han ejecutado';
COMMENT ON COLUMN GUS_SOLJOB.JOB_ID     IS 'Id. del job';
COMMENT ON COLUMN GUS_SOLJOB.JOB_FECINI IS 'Fecha de inicio';
COMMENT ON COLUMN GUS_SOLJOB.JOB_FECFIN IS 'Fecha de fin';
COMMENT ON COLUMN GUS_SOLJOB.JOB_TIPO   IS 'Tipo de tarea. IDX_TODO (todo), IDX_TSI (Todo sin indexar), IDX_UA (Indexacio por microsite) , IDX_PDT (pendientes) y IDX_MIC(un microsite en concreto)';
COMMENT ON COLUMN GUS_SOLJOB.JOB_IDELEM IS 'Id. del microsite.';
COMMENT ON COLUMN GUS_SOLJOB.JOB_DESCRI IS 'Descripcion de la indexacion.';
COMMENT ON COLUMN GUS_SOLJOB.JOB_FINALI IS 'Indica si ha finalizado o no (0 es NO , 1 SI). Se utiliza para la indexacio de tots els pendents';
COMMENT ON COLUMN GUS_SOLJOB.JOB_RESUME IS 'Resumen para la indexacion completa de no indexados, muy basica en plan hay indexados X de Y en total.';
COMMENT ON COLUMN GUS_MICROS.MIC_IDXCOR IS 'Indica si se ha indexado correctamente, siendo -1 aun no, 0 incorrecto y 1 correcto.';