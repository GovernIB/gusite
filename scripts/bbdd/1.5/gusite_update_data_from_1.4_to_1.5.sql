--SET SERVEROUTPUT ON;
-- Actualización de campos de titulo y uri para la nueva tabla
-- si el formulario tiene titulo usa ese campo para crear el nombre y la uri
-- si no, usa el texto contacto.
DECLARE
  titulo varchar2(1000 CHAR) := '';
  uri varchar2(1000 BYTE) := '';
  contador number := 0;
BEGIN
  -- BORRAMOS EL CONTENIDO DE LA TABLA DE FORMULARIO-IDIOMA		
  DELETE FROM GUS_FCOIDI;
  
  -- REGENERAMOS LA INFORMACION.
  -- buscamos todos los formularios
  FOR FCON IN (select  FRM_MICCOD FMICRO, FRM_CODI FCOD, FRM_EMAIL FEMAIL from GUS_FRMCON order by frm_miccod) LOOP
    -- PARA CADA FORMULARIO BUSCAMOS LOS IDIOMAS DEL MICROSITE
      FOR IDIOMA IN (SELECT IMI_CODIDI IDI, IMI_MICCOD MICRO FROM GUS_IDIMIC WHERE IMI_MICCOD = FCON.FMICRO) LOOP
        -- buscamos el titulo en la linea
        select (select RID_TEXTO 
                     from GUS_FRMLIN l,GUS_FRMIDI i 
                     where  l.FLI_FRMCOD = FCON.FCOD
                            AND l.FLI_TIPO = 1 
                            AND i.RID_CODIDI = IDIOMA.IDI  and i.RID_FLICOD = l.FLI_CODI 
                            AND rownum = 1) into titulo from dual;
              
      -- si el titulo no existe, añadimos uno por defecto                      
      IF (titulo is null) or (titulo = '') THEN
        IF IDIOMA.IDI = 'es' THEN
          titulo := 'Contacto';
        ELSIF IDIOMA.IDI = 'ca' THEN
          titulo := 'Contacte';
        ELSE 
          titulo := 'Contact'; 
        END IF;                      
      END IF; 
      
      titulo := SUBSTR(titulo,1,100);
      IF (length(titulo)=100) THEN
        titulo := SUBSTR(titulo,1,length(titulo)-length(TO_CHAR(FCON.FCOD)))||TO_CHAR(FCON.FCOD);
         --dbms_output.put_line( 'Titulo largo:' || titulo);
      END IF;
      
      -- verificamos si el titulo ya existe en este microsite para este idioma       
      select (select count(*) 
                    from GUS_FCOIDI I, GUS_FRMCON F 
                    where  I.FCI_NOMBRE = titulo
                      and I.FCI_CODIDI = IDIOMA.idi 
                      and F.FRM_CODI = I.FCI_FCOCOD
                      and F.FRM_MICCOD = FCON.FMICRO) 
              into contador from dual;
              
       IF contador>0 THEN
            -- SI YA EXISTIA EL TITULO EN ESTE MICROISTE E IDIOMA AÑADIMOS EL CODIGO AL TITULO
            IF (length(titulo)+ length(TO_CHAR(FCON.FCOD)))>100  THEN
              titulo := SUBSTR(titulo,1,length(titulo)-length(TO_CHAR(FCON.FCOD)))||TO_CHAR(FCON.FCOD);
            ELSE
              titulo := titulo || TO_CHAR(FCON.FCOD);
            END IF;       
       END IF;
      
      -- verificamos si la uri ya existe en este microsite para este idioma       
      select (select count(*) 
                    from GUS_FCOIDI I, GUS_FRMCON F 
                    where  I.FCI_URI = GUS_STRING2URI_F(titulo)
                      and I.FCI_CODIDI = IDIOMA.idi 
                      and F.FRM_CODI = I.FCI_FCOCOD
                      and F.FRM_MICCOD = FCON.FMICRO) 
              into contador from dual;
      -- si la uri existe, la concatenamos con un valor unico
      IF contador > 0 THEN
        uri := GUS_STRING2URI_F(titulo) || FCON.FCOD;  
      ELSE
        uri := GUS_STRING2URI_F(titulo);        
      END IF;
      
      --imprimimos los datos a insertar
      --dbms_output.put_line(IDIOMA.IDI ||  '-' || FCON.FCOD ||  '-' || titulo || '-' || uri);
      
      --insertamos los datos
      INSERT INTO GUS_FCOIDI
        (FCI_CODIDI,FCI_FCOCOD,FCI_NOMBRE,FCI_URI )
      VALUES
        (IDIOMA.IDI, FCON.FCOD ,SUBSTR(titulo,1,100), SUBSTR(uri,1,125) );         
    END LOOP; 
  END LOOP;
  commit;
END;