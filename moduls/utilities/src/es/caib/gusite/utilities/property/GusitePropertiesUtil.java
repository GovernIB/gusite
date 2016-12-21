package es.caib.gusite.utilities.property;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GusitePropertiesUtil
{
	  private static final String USER_SOLR = "es.caib.gusite.user.solr";
	  private static final String PASS_SOLR = "es.caib.gusite.pass.solr";
	  private static final String URL_SOLR = "es.caib.gusite.url.solr";
	  private static final String DIAS_SOLR = "es.caib.gusite.dias.solr";
	  private static final String INDEX_SOLR = "es.caib.gusite.index.solr";
	  
	  private static final String ERROR_MESSAGE = "Error obteniendo la propiedad ";

	  private static Log log = LogFactory.getLog(GusitePropertiesUtil.class);
	  
	 
	  private static String getProperty(String property) {
	        try {
	            return System.getProperty(property);
	        } catch (Exception e) {
	        	log.error(ERROR_MESSAGE + property, e);
	            return null;
	        }
	    }
	  
	  public static final String getUserSOLR() {
	
	      return getProperty(USER_SOLR);
	  }
	  
	  public static final String getPassSOLR() {
	
	      return getProperty(PASS_SOLR);
	  }
	  public static final String getIndexSOLR() {
	
	      return getProperty(INDEX_SOLR);
	  }
	  public static final String getUrlSOLR() {
	
	      return getProperty(URL_SOLR);
	  }

	public static int getDiasMaxSOLR() {
		int dias;
		String sDias = getProperty(DIAS_SOLR);
		if (sDias == null) {
			dias = 10;
		} else {
			dias = Integer.valueOf(sDias);
		}
		
		return dias;
	}

 
 
}
