package es.caib.gusite.utilities.property;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Clase de propiedades que lee del jboss-service.xml asociado a gusite. 
 * @author slromero
 *
 */
public class GusitePropertiesUtil
{
	/** Usuario SOLR. **/
	  private static final String USER_SOLR = "es.caib.gusite.user.solr";
	  /** Contrasenya SOLR. **/
	  private static final String PASS_SOLR = "es.caib.gusite.pass.solr";
	  /** Url SOLR. **/
	  private static final String URL_SOLR = "es.caib.gusite.url.solr";
	  /** Dias SOLR. **/
	  private static final String DIAS_SOLR = "es.caib.gusite.dias.solr";
	  /** Descanso SOLR en segundos. **/
	  private static final String DESCANSO_MIN_SOLR = "es.caib.gusite.solr.descanso.minutos";
	  /** Descanso SOLR en microsite. **/
	  private static final String DESCANSO_MIC_SOLR = "es.caib.gusite.solr.descanso.microsites";
	  /** Tamanyo máximo Job. **/
	  private static final String DIAS_MAX_JOB = "es.caib.gusite.diasjobs.solr";
	  /** Tamanyo máximo Clob. **/
	  private static final String TAMANYO_MAX_CLOB = "es.caib.gusite.maxclob.solr";
	  /** Index SOLR. **/
	  private static final String INDEX_SOLR = "es.caib.gusite.index.solr";
	  /** Clave de google maps. **/
	  private static final String KEY_GOOGLEMAPS = "es.caib.gusite.key.google.maps";
	  /** Clave de google maps. **/
	  private static final String KEY_ENLACES = "es.caib.gusite.enlaces.";
	  
	  /** Mensaje de error. **/
	  private static final String ERROR_MESSAGE = "Error obteniendo la propiedad ";
	  
	  /** Log. **/
	  private static Log log = LogFactory.getLog(GusitePropertiesUtil.class);
	  /** El valor alto para empezar. **/
	  public static Long minimoIdMax = 99999999999999l;
	  
	
	  /**
	   * Devuelve el usuario de solr.
	   * @return
	   */
	  public static final String getUserSOLR() {
	      return getProperty(USER_SOLR);
	  }
	  
	
	  /**
	   * Devuelve el password de solr. 
	   * @return
	   */
	  public static final String getPassSOLR() {
	      return getProperty(PASS_SOLR);
	  }
	  
	  /**
	   * Obtiene el valor de index solr (por ejemplo, CAIB)
	   * @return
	   */
	  public static final String getIndexSOLR() {
	      return getProperty(INDEX_SOLR);
	  }
	  
	   
	   /**
	   * Devuelve el tamanyo máximo del clob.
	   * @return
	   */
	  public static final Long getTamanyoMaximoClob() {
		    final Long numeroMaximo;
			final String sNumeroMaximo = getProperty(TAMANYO_MAX_CLOB);
			if (sNumeroMaximo == null) {
				numeroMaximo = 1048576l*20; //20MB
			} else { 
				numeroMaximo = (long) (Double.valueOf(sNumeroMaximo) * 1048576l); //Para pasarlo a MB
			}
			
			return numeroMaximo;
	  }
  
   
	  /**
	   * Devuelve los días máximos que puede estar los jobs en las tablas.
	   * 
	   * @return
	   */
	  public static final int getDiasMaximoJobs() {
		   final int numeroMaximo;
			final String sNumeroMaximo = getProperty(DIAS_MAX_JOB);
			if (sNumeroMaximo == null) {
				numeroMaximo = 10;
			} else {
				numeroMaximo = Integer.valueOf(sNumeroMaximo);
			}
			
			return numeroMaximo;
	  }
  
	  
	  /**
	   * Obtiene la propiedad de solr url. 
	   * @return
	   */
	  public static final String getUrlSOLR() {
	
	      return getProperty(URL_SOLR);
	  }

	  /**
	   * Dias máximo de solr.
	   * @return
	   */
	  public static int getDiasMaxSOLR() {
			final int dias;
			final String sDias = getProperty(DIAS_SOLR);
			if (sDias == null) {
				dias = 10 ;
			} else {
				dias = Integer.valueOf(sDias);
			}
			
			return dias;
	  }

	/**
	 * Obtiene la clave de google maps.
	 * @return
	 */
	public static String getKeyGooglemaps() {
		 return getProperty( KEY_GOOGLEMAPS);
	}
	
	
	/**
	 * Retorna el enlace (url) indicado a través del fichero properties.
	 * La propiedad debe ser: es.caib.gusite.enlaces. + nombreEnlace
	 * Si la propiedad no existe retorna "". 
	 * Usado para establecer enlaces en las plantillas sin que sea dependiente de modificaciones de código.
	 * @param nombreEnlace nombre de la propiedad
	 * @return url
	 */
	public static String getEnlace(String nombreEnlace) {
		String p = getProperty( KEY_ENLACES + nombreEnlace);
		if (p==null){
			return "";
		}else{
			return p;
		}		 
	}

 

	  /** 
	   * Obtiene la propiedad.
	   * @param property
	   * @return
	   */
	  private static String getProperty(String property) {
	        try {
	            return System.getProperty(property);
	        } catch (Exception e) {
	        	log.error(ERROR_MESSAGE + property, e);
	            return null;
	        }
	    }

	/**
	   * Devuelve cada cuantos microsites se tiene que realizar el descanso. Por defecto, 5 segundos.
	   * @return
	   */
	public static int getTiempoEspera() {
		 try {
			 	int retorno =  Integer.valueOf(getProperty(DESCANSO_MIN_SOLR));
	            //Si hubiese un dato error, 0 o negativo, ponemos 5.
			 	if (retorno <= 0) { retorno = 5; }
	            return retorno;
	      } catch (Exception e) {
	      //	log.error(ERROR_MESSAGE, e);
	    	  log.warn("Falta introducir la propiedad DESCANSO_MIN_SOLR");
	          return 10;
	      }
	}


	public static int getCuantosMicrosites() {
		try {
		 	int retorno =  Integer.valueOf(getProperty(DESCANSO_MIC_SOLR));
            //Si hubiese un dato error, 0 o negativo, ponemos 5.
		 	if (retorno <= 0) { retorno = 5; }
            return retorno;
      } catch (Exception e) {
//    		log.error(ERROR_MESSAGE, e);
    	  log.warn("Falta introducir la propiedad DESCANSO_MIC_SOLR");
            return 15;
      }
	} 
}
