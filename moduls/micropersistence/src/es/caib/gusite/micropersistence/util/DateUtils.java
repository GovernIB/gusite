package es.caib.gusite.micropersistence.util;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Utilidades de fechas.
 * @author INDRA 
 */
public class DateUtils {

	    /**
	     * Obtiene la fecha actual, con precisi&oacute;n de dias.
	     * Fijada a les 00:00:00 000ms.
	     * @return Fecha de hoy a media noche.
	     */
	    public static java.util.Date today() {
	        Calendar calendar = new GregorianCalendar();
	        calendar.set(Calendar.HOUR_OF_DAY, 0);
	        calendar.set(Calendar.MINUTE, 0);
	        calendar.set(Calendar.SECOND, 0);
	        calendar.set(Calendar.MILLISECOND, 0);
	
	        return calendar.getTime();
	    }

	  /** 
	   * procedimiento que devuelve la fecha actual en formato aaaamm 
	   */
	  public static int formatfechaactual2Stats() {
		
		String txretorno;
		java.util.GregorianCalendar fecha = new java.util.GregorianCalendar();
		int mes=fecha.get(java.util.Calendar.MONTH)+1; 
		int anyo=fecha.get(java.util.Calendar.YEAR);
		String mestxt = "";
		mestxt = (mes<10)?"0"+mes:""+mes;	
		txretorno=anyo + mestxt;
		
		return Integer.parseInt(txretorno);
	  }  
}
