-- És necessari insertar l'usuari administrador amb el que s'accedirà al backoffice
Insert into GUS_MUSUAR (MSU_CODI,MSU_USERNA,MSU_PASSWO,MSU_NOMBRE,MSU_OBSERV,MSU_PERFIL) values (10000000,'rolsac','rolsac','Usuario tests','agarcia@at4.net','sacadmin');


-- Insertar els idiomes 
Insert into GUS_IDIOMA (IDI_CODI,IDI_ORDEN,IDI_CODEST,IDI_NOMBRE,IDI_TRADUCTOR) values ('ca',1,'ca','Català','CATALAN');
Insert into GUS_IDIOMA (IDI_CODI,IDI_ORDEN,IDI_CODEST,IDI_NOMBRE,IDI_TRADUCTOR) values ('es',2,'es','Castellano','SPANISH');
Insert into GUS_IDIOMA (IDI_CODI,IDI_ORDEN,IDI_CODEST,IDI_NOMBRE,IDI_TRADUCTOR) values ('en',3,'en','English','ENGLISH');
Insert into GUS_IDIOMA (IDI_CODI,IDI_ORDEN,IDI_CODEST,IDI_NOMBRE,IDI_TRADUCTOR) values ('de',4,'de','Deutsch','GERMAN');
Insert into GUS_IDIOMA (IDI_CODI,IDI_ORDEN,IDI_CODEST,IDI_NOMBRE,IDI_TRADUCTOR) values ('fr',5,'fr','French','FRENCH');

-- Insertar els tipus de servei 
Insert into GUS_TIPSER (TPS_CODI,TPS_NOMBRE,TPS_VISIB,TPS_REF,TPS_URL,TPS_TIPO) values (1,'Llistats','S','NTCS0','noticias.do','2');
Insert into GUS_TIPSER (TPS_CODI,TPS_NOMBRE,TPS_VISIB,TPS_REF,TPS_URL,TPS_TIPO) values (2,'Agenda','S','GND00','agendas.do','1');
Insert into GUS_TIPSER (TPS_CODI,TPS_NOMBRE,TPS_VISIB,TPS_REF,TPS_URL,TPS_TIPO) values (4,'Faqs','S','FQS00','faqs.do','1');
Insert into GUS_TIPSER (TPS_CODI,TPS_NOMBRE,TPS_VISIB,TPS_REF,TPS_URL,TPS_TIPO) values (5,'Contacte','S','CNTCT','formularios.do','1');
Insert into GUS_TIPSER (TPS_CODI,TPS_NOMBRE,TPS_VISIB,TPS_REF,TPS_URL,TPS_TIPO) values (7,'Enquestes','S','NCSTS','encuestas.do','1');
Insert into GUS_TIPSER (TPS_CODI,TPS_NOMBRE,TPS_VISIB,TPS_REF,TPS_URL,TPS_TIPO) values (9,'Formularios QSSI','S','FQSSI','frqssis.do','1');
Insert into GUS_TIPSER (TPS_CODI,TPS_NOMBRE,TPS_VISIB,TPS_REF,TPS_URL,TPS_TIPO) values (10,'Mailing','S','MAIL0','convocatorias.do','1');
Insert into GUS_TIPSER (TPS_CODI,TPS_NOMBRE,TPS_VISIB,TPS_REF,TPS_URL,TPS_TIPO) values (11,'Llistes distribució','S','LDIS0','ldistribucion.do','1');
Insert into GUS_TIPSER (TPS_CODI,TPS_NOMBRE,TPS_VISIB,TPS_REF,TPS_URL,TPS_TIPO) values (100,'Continguts especifics','S','CNTSP','contenidos.do','0');
