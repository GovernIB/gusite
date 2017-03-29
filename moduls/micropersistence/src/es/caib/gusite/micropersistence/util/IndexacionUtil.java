package es.caib.gusite.micropersistence.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.gusite.micromodel.Agenda;
import es.caib.gusite.micromodel.Archivo;
import es.caib.gusite.micromodel.Contenido;
import es.caib.gusite.micromodel.Encuesta;
import es.caib.gusite.micromodel.Faq;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.Noticia;
import es.caib.gusite.micromodel.Tipo;
import es.caib.gusite.micromodel.TraduccionContenido;
import es.caib.gusite.micromodel.TraduccionEncuesta;
import es.caib.gusite.micromodel.TraduccionMicrosite;
import es.caib.gusite.micromodel.TraduccionNoticia;
import es.caib.gusite.plugins.PluginFactory;
import es.caib.gusite.plugins.organigrama.OrganigramaProvider;
import es.caib.gusite.plugins.organigrama.UnidadData;
import es.caib.solr.api.model.PathUO;

public class IndexacionUtil {
	public static final String APLICACION_CALLER_ID = "GUSITE";
	
	public static Long REINDEXAR = 1l;
	public static Long DESINDEXAR = 0l;
	
	public static String TIPO_TODO = "IDX_TODO";
	public static String TIPO_UA ="IDX_UA";
	public static String TIPO_PENDIENTE="IDX_PDT";
	public static String TIPO_MICROSITE = "IDX_MIC";
	
	/** Extensiones. **/
	private static Map<String, String> extensiones = null;
	private static Long tamanyoMaximo = null;
	
	/** LOG. **/
	protected final static Log log = LogFactory.getLog(IndexacionUtil.class);

	public static PathUOResult calcularPathUOsMicrosite(Microsite micro, String lang) throws Exception {
		
		List<UnidadData> pathAscendente = new ArrayList<UnidadData>();
		OrganigramaProvider op = PluginFactory.getInstance().getOrganigramaProvider();
		UnidadData unidad = null;
		unidad = op.getUnidadData(micro.getUnidadAdministrativa(), lang);
		while (unidad != null) {
			pathAscendente.add(unidad);
			unidad = ( unidad.getIdUnidadPadre() != null ? op.getUnidadData(unidad.getIdUnidadPadre(), lang) : null);				
		}
		
		PathUO uo = new PathUO();
		uo.setPath(new ArrayList<String>());
		StringBuffer textPath = new StringBuffer();
		
		for (int i = (pathAscendente.size() - 1) ; i >= 0; i--) {
			unidad = pathAscendente.get(i);
			uo.getPath().add(String.valueOf(unidad.getId()));
			textPath.append(unidad.getNombre() + " ");
		}
		
		PathUOResult result = new PathUOResult();
		result.getUosPath().add(uo);
		result.setUosText(textPath.toString());
		
		return result;
	}
	
	
	public static boolean isIndexable(Archivo archivo)  {
		
		if (archivo == null || archivo.getPeso() <= 0) {
			return false;
		}
		
		// Tamaño máximo
		if (archivo.getPeso() > getTamanyoMaximo()*1024l*1024l) {
			return false;
		}
		
		// Extensiones
		if (!verificarExtension(archivo.getNombre())) {
			return false;
		}
		
		return true;
	}
	
	
	public static boolean isIndexable(Microsite micro) {
		
		if (!StringUtils.equalsIgnoreCase(micro.getVisible(), "S")) {
			return false;
		}
		
		if (micro.getUnidadAdministrativa() <= 0) {
			return false;
		}
		
		return true;
	}
	
	
	/**
	 * Comprueba si es indexable un contenido.
	 * @return
	 */
	public static boolean isIndexable(final Contenido contenido) {
		if (contenido == null) {
			return false;
		}
		
		if (!contenido.getVisible().equals("S") ) {
			return false;
		}
		
		if (contenido.getFcaducidad() != null && Calendar.getInstance().getTime().before(contenido.getFcaducidad())) {
			return false;
		}
		
		if (!IndexacionUtil.isIndexable(contenido.getMicrosite())) {
			return false;
		}
		
		return true;
	}
	
	
	public static String getUrlMicrosite(Microsite micro, String idioma) {
		String url;
		
		//v5 version 2015, IN intranet, v1 primera version, v4 segunda version
    	if (micro.getVersio().equals("v5")) {
    		url = "/"+ micro.getUri() + "/" + idioma;	    		
    	} else {
    		url = "/sacmicrofront/index.do?lang="+idioma +"&idsite="+ micro.getId();
    	}
    	
    	return url;
	}
	
	public static boolean isRestringidoMicrosite(Microsite micro) {
		return !micro.getAcceso().equals("P");
	}
	
	
	public static String getUrlContenido(Contenido contenido, String idioma) {
		String url = null;
		
		if (contenido.getMicrosite().getVersio().equals("v5")) {
			final TraduccionContenido traduccion = (TraduccionContenido) contenido.getTraduccion(idioma);
			if (traduccion != null) {
				url =  "/"+ contenido.getMicrosite().getUri() + "/" + idioma + "/" + traduccion.getUri();
			}				    	
    	} else {
    		url = "/sacmicrofront/contenido.do?lang="+idioma +"&idsite="+contenido.getMicrosite().getId() 
    				+"&cont="+contenido.getId();
    	}	
    	
    	return url;
	}
	
	
	public static String getTituloMicrosite(Microsite micro, String idioma) {
		String titulo = null;
		final TraduccionMicrosite traduccion = (TraduccionMicrosite) micro.getTraduccion(idioma);
		if (traduccion != null) {
				titulo = traduccion.getTitulo();
		}						
		return titulo;
	}
	
	
	public static String getUrlArchivo(Microsite micro, Archivo archivo, String idioma) {
		String url = null;
		
		if (micro.getVersio().equals("v5")) {
    		url = "/"+ micro.getUri()  + "/f/" + archivo.getId();	    		
    	} else {
    		url = "/sacmicrofront/archivopub.do?ctrl=MCRST"+micro.getId()+ "ZI" +archivo.getId() +"&id=" + archivo.getId();
    	}
    	
    	return url;
	}
	
	public static String getTituloArchivo(Archivo archivo) {
		// Obtenemos a partir del nombre de fichero el título
		String[] nombreArc = archivo.getNombre().split("\\."); 
		String tituloStr = nombreArc[0];
		return tituloStr;		
	}
	
	public static String getDescripcionArchivo(Archivo archivo) {
		return IndexacionUtil.getTituloArchivo(archivo) + " (" + FilenameUtils.getExtension(archivo.getNombre()) + ")";		
	}
	
	
	/**
	 * Comprueba si es indexable una agenda.
	 * @return
	 */
	public static boolean isIndexable(final Agenda agenda) {
		
		if (!agenda.getVisible().equals("S") ) {
			return false;
		}
		return true;
	}
	
	
	public static String getUrlAgenda(Microsite micro, Agenda agenda, String idioma) {
		String url = null;
		String fechaIni = new SimpleDateFormat("yyyyMMdd").format(agenda.getFinicio());
		
		//v5 version 2015, IN intranet, v1 primera version, v4 segunda version
		if (micro.getVersio().equals("v5")) {
			url = "/"+ micro.getUri() + "/" + idioma + "/agenda/" + fechaIni;	    		
		} else {
			url = "/sacmicrofront/agenda.do?lang="+idioma +"&idsite="+micro.getId() 
					+"&cont="+ fechaIni;
		}
    	
    	return url;
	}
	
	
	/**
	 * Comprueba si es indexable una agenda.
	 * @return
	 */
	public static boolean isIndexable(final Encuesta encuesta) {
		if (!encuesta.getVisible().equals("S") ) {
			return false;
		}		
		return true;
	}
	
	
	public static String getUrlEncuesta(Microsite micro, Encuesta encuesta, String idioma) {
		String url = null;
		//v5 version 2015, IN intranet, v1 primera version, v4 segunda version
		final TraduccionEncuesta traduccion = (TraduccionEncuesta) encuesta.getTraduccion(idioma);
		if (micro.getVersio().equals("v5")) {
			url =  "/"+ micro.getUri() + "/" + idioma + "/encuesta/" + traduccion.getUri();	    		
		} else {
			url = "/sacmicrofront/encuesta.do?lang="+idioma +"&idsite="+micro.getId() 
					+"&cont="+encuesta.getId();
		}
    	return url;
	}
	
	
	/**
	 * Comprueba si es indexable una faq.
	 * @return
	 */
	public static boolean isIndexable(final Faq faq) {
		
		if (!faq.getVisible().equals("S") ) {
			return false;
		}
		
		return true;
	}


	public static String getUrlFaq(Microsite micro, Faq faq, String keyIdioma) {
		String url = null;
		//v5 version 2015, IN intranet, v1 primera version, v4 segunda version
    	if (micro.getVersio().equals("v5")) {
    		url = "/"+ micro.getUri() + "/" + keyIdioma + "/faq/";	    		
    	} else {
    		url = "/sacmicrofront/faqs.do?lang="+keyIdioma +"&idsite="+micro.getId() 
    				+"&cont="+faq.getId();
    	}
    	return url;
	}
	
	
	/**
	 * Comprueba si es indexable una noticia.
	 * @return
	 */
	public static boolean isIndexable(final Noticia noticia) {
		boolean indexable = true;
		if (!noticia.getVisible().equals("S") ) {
			indexable = false;
		}
		
		return indexable;
	}


	public static String getUrlNoticia(Microsite micro, Noticia noticia,
			String keyIdioma) {
		String url = null;
		
		final TraduccionNoticia traduccion = (TraduccionNoticia) noticia.getTraduccion(keyIdioma);
		
		//v5 version 2015, IN intranet, v1 primera version, v4 segunda version
    	if (micro.getVersio().equals("v5")) {
    		if (noticia.getTipo().getTipoelemento().equals(Tipo.TIPO_FICHA)){
    			url = "/"+ micro.getUri() + "/" + keyIdioma + "/n/" + traduccion.getUri();	 
    		}else if (noticia.getTipo().getTipoelemento().equals(Tipo.TIPO_FOTO) ){
    			String idFoto = noticia.getImagen() !=null && noticia.getImagen().getId() !=null ? noticia.getImagen().getId().toString() : "";
    			url = "/"+ micro.getUri() +  "/f/" + idFoto;
    		}else if ( noticia.getTipo().getTipoelemento().equals(Tipo.TIPO_DOCUMENTOS)){
    			String idDocu = traduccion.getDocu() !=null && traduccion.getDocu().getId() !=null ? traduccion.getDocu().getId().toString() : "";
    			url =  "/"+ micro.getUri() + "/f/" + idDocu;
    		}else if (noticia.getTipo().getTipoelemento().equals(Tipo.TIPO_LINK)){
    			url = traduccion.getLaurl();
    		}
    			    		
    	} else {
    		if (noticia.getTipo().getTipoelemento().equals(Tipo.TIPO_FICHA)){
    			url = "/sacmicrofront/noticia.do?lang="+keyIdioma +"&idsite="+micro.getId() 
	    				+"&cont="+noticia.getId();
    		}else if (noticia.getTipo().getTipoelemento().equals(Tipo.TIPO_FOTO) ){
    			String idFoto = noticia.getImagen() !=null && noticia.getImagen().getId() !=null ? noticia.getImagen().getId().toString() : "";
    			url = "/sacmicrofront/archivopub.do?ctrl=MCRST"+micro.getId() +"ZI" + idFoto
	    				+"&id="+idFoto;
    		}else if (noticia.getTipo().getTipoelemento().equals(Tipo.TIPO_DOCUMENTOS)){
    			String idDocu = traduccion.getDocu() !=null && traduccion.getDocu().getId() !=null ? traduccion.getDocu().getId().toString() : "";
    			url = "/sacmicrofront/archivopub.do?ctrl=MCRST"+micro.getId() +"ZI" + idDocu
	    				+"&id="+idDocu;
    		}else if (noticia.getTipo().getTipoelemento().equals(Tipo.TIPO_LINK)){
    			url = traduccion.getLaurl();
    		}
    			
    	}

		
		return url;
	}
	
	
	public static Long getTamanyoMaximo() {
		if (tamanyoMaximo == null) {
			final String sTamanyoMaximo = System.getProperty("es.caib.gusite.solr.tamanyomaximo");
			try {
				tamanyoMaximo = Long.valueOf(sTamanyoMaximo.trim()); 
			} catch (Exception e) {
				log.error("Error tratanto de convertir a long el tamanyoMaximo"+sTamanyoMaximo, e);
				tamanyoMaximo = 10l;
			}
		}
		return tamanyoMaximo;
	}	
	
	public static Map<String, String> getExtensiones() {
		//Preparamos la variable extensiones si está a null.
		if (IndexacionUtil.extensiones == null) {
			final String ficheroPermitidos = System.getProperty("es.caib.gusite.solr.ficheros");
			IndexacionUtil.extensiones = new HashMap<String, String>();
			String[] extensionesSplit = ficheroPermitidos.split(",");
			for(String extensionSplit : extensionesSplit) {
				//Se limpian las extensiones.
				extensiones.put(extensionSplit.trim().toLowerCase(Locale.ITALIAN), extensionSplit);
			}
		}
		return extensiones;
	}
	
	public static boolean verificarExtension(String filename) {
		if (StringUtils.isBlank(filename)) {
			return false;
		}
				
		final String extension = FilenameUtils.getExtension(filename.trim()).toLowerCase(Locale.ITALIAN);
		
		if (StringUtils.isBlank(extension)) {
			return false;
		}
		
		if (!getExtensiones().containsKey(extension)) {
			return false;
		}
		
		return true;		
	}	
	
}
