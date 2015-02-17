package es.caib.gusite.front.general;

import es.caib.gusite.micromodel.Tipo;

/**
 * Clase básica de los microsites. Contiene variables estáticas que se utilizan
 * por toda la aplicación
 * 
 * @author Indra
 * 
 */
public class Front {

	// estas variables coinciden con el campo TPS_REF de la tabla RSC_TIPSER
	static public final String RAGENDA = "GND00";
	static public final String RQSSI = "FQSSI";
	static public final String RBANNER = "BNNR0";
	static public final String RCONTACTO = "CNTCT";
	static public final String RCONTENIDO = "CNTSP";
	static public final String RFAQ = "FQS00";
	static public final String RNOTICIA = "NTCS0";
	static public final String RENCUESTA = "NCSTS";
	static public final String RMAILING = "MAIL0";
	static public final String RLDISTRIB = "DISTR";

	// maximo entero
	static public final int MAX_INTEGER = 32000;

	// microsite
	static public final String RMICROSITE = "MCRST";

	// separador en el control del parametro para los archivos
	static public final String separatordocs = "ZI";

	// separador de palabras o texto en strings
	static public final String separatorwords = ";";

	// separador de palabras o texto en strings
	static public final String separatorwordsform = "#";

	// contador global de banner
	static public Long contadorbanner = new Long(1);

	// parametros que nos podemos encontrar en las diferentes actions
	static public final String PLANG = "lang"; // idioma
	static public final String PIDSITE = "idsite"; // id microsite
	static public final String PCONT = "cont"; // elemento de contenido
	static public final String PTIPO = "tipo"; // tipo de elemento de bd
	static public final String PCTRL = "ctrl"; // elemento de control
	static public final String PPAGINA = "pagina"; // elemento de paginacion de
													// una lista
	static public final String PCAMPA = "campa"; // elemento de campaña
	static public final String PBTNANAR = "btnanar"; // boton enviar formulario
	static public final String PTELEM = "telem"; // tipo de elemento predefinido
													// (0,1,2)
	static public final String PANYO = "tanyo"; // anyo
	static public final String PSEARCH = "filtro"; // busquedas
	static public final String PSTAT = "stat"; // parametro que servirá para
												// indicar que no se hagan
												// estadísticas en la sesion
	static public final String PNAME = "name"; // parametro que servirá para
												// recoger una imagen por nombre
	static public final String PENCCOMP = "enccomp"; // parametro que indicará
														// que se está
														// contestando una
														// encuesta de un
														// componente
	static public final String PVIEW = "view"; // indicará que solo
												// visualizaremos resultados
	static public final String PMKEY = "mkey"; // iddentificador unico de
												// microsite
	static public final String PEMAIL = "email"; // parametro que servirá para
													// recoger ek email
	static public final String PACTION = "accio"; // parametro que servirá para
													// indicar la operacion a
													// realizar
	static public final String PDISTRIB = "distrib"; // identificador de la
														// lista de distribucion
	static public final String ENCPARAM = "encParam"; // preguntas que vienen
														// perfijadas en una
														// encuesta

	// clases de los elementos
	// OJO! duplicacion con Tipo.java
	static public final String ELEM_NOTICIA = "0"; // elemento predefinido
													// noticia
	static public final String ELEM_LINK = "1"; // elemento predefinido link
	static public final String ELEM_DOCUMENTO = "2"; // elemento predefinido
														// documento
	static public final String ELEM_CONEXIO_EXTERNA = "3"; // elemento
															// predefinido link
															// externo
	static public final String ELEM_FOTO = Tipo.TIPO_FOTO;

	// tipo de paginacion
	static public final String ELEM_PAG_NORMAL = "0"; // paginacion tradicional
	static public final String ELEM_PAG_ANUAL = "1"; // paginacion anual

	// string de campo requerido en formulario
	static public final String VCAMPO_REQUERIDO = "required";

	// strings de tags html
	static public final String TAG_GENERICO_DUMMY = "MCRST_DUMMY_";
	static public final String TAG_AGENDA = "MCRST_DUMMY_AGE";
	static public final String TAG_NOTICIAS = "MCRST_DUMMY_NOT";
	static public final String TAG_BANNER = "MCRST_DUMMY_BAN";
	static public final String TAG_QSSI = "MCRST_DUMMY_FQSSI";
	// usuario sin autenticar
	static public final String _NOBODY = "nobody";

	// estar en zona de intranet o de internet
	static public final String _DMZ_PUBLICO = "1";
	static public final String _DMZ_INTRANET = "2";

	static public final String _HOSTCAIB = "www.caib.es";

	// nombre de los keys en el mapa externo
	static public final String MAP_EXT_MCHTML = "mcHTML";

	static public final String UO_PORTAVOZ = "270677"; // caib: 270677 nuestra:
														// 51079

}
