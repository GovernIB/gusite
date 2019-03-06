/** Incluye un nuevo campo para comprobar si el archivo ha sido exportado a file system **/
ALTER TABLE GUS_DOCUS ADD DCM_INFSYS Varchar2 (1 CHAR) DEFAULT 'X';
COMMENT ON COLUMN GUS_DOCUS.DCM_INFSYS is 'Indica si el fichero ya ha sido exportado a File system (S) si esta pendiente de exportar(N), si no se ha analizado (X) o ocurrio un error al exportar (E)';