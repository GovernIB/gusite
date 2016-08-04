-- Afegeix els rols per gusite als usuaris que els tenien anteriorment per rolsac
INSERT INTO SC_WL_USUGRU
 SELECT UGR_CODUSU, REPLACE(UGR_CODGRU, 'RSC_', 'GUS_') FROM SC_WL_USUGRU
 WHERE UGR_CODGRU LIKE 'RSC_%';