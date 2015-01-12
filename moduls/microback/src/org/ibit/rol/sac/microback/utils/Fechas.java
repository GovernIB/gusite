package org.ibit.rol.sac.microback.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Clase que contiene diferentes utilidades relacionadas con
 * fechas
 * 
 * @author Indra
 *
 */
public class Fechas {

	public Fechas() {}
	
	/**
	 * Calcula si 'algo' estará vigente o no.
	 * método que devuelve true o false en función de las fechas que se le
	 * pasan con respecto a la actual.
	 * @param fechaini
	 * @param fechafin
	 * @return true si esta vigente
	 */
	public static boolean vigente(Date fechaini, Date fechafin) {
		boolean retorno=false;
		Date fa = new Date();
		Calendar ca = Calendar.getInstance();
		Calendar ci = new GregorianCalendar();
		Calendar cf = new GregorianCalendar();

		//se ponen fechas imaginarias en el caso de que hayan nulos
		//ci --> antes de la actual
		//cf --> un anyo más con respecto al actual
		if (fechaini==null) 
			ci = new GregorianCalendar(1972,Calendar.FEBRUARY,19);  
		else
			ci.setTime(fechaini);
			
		if (fechafin==null) 
			cf = new GregorianCalendar(ca.get( Calendar.YEAR  )+1,ca.get( Calendar.MONTH ) + 1,ca.get( Calendar.DATE  ));  
		else
			cf.setTime(fechafin);
			
		if ((fa.getTime()>ci.getTime().getTime()) && (fa.getTime()<cf.getTime().getTime()))
			retorno = true;
		
		return retorno;
	}

	/**
	 * Calcula si 'algo' estará vigente o no.
	 * método que devuelve true o false en función de las fechas que se le
	 * pasan con respecto a fechaacomparar.
	 * El parametro conrango indica si utilizar la fecha de inicio como fecha "a partir de la cual" o "solo en ese dia" 
	 * @param fechaini
	 * @param fechafin
	 * @param fechaacomparar
	 * @param conrango
	 * @return true si esta vigente
	 */
	public static boolean vigente(Date fechaini, Date fechafin, Date fechaacomparar, boolean conrango) {
		boolean retorno=false;
		
		Calendar ca = Calendar.getInstance();
		Calendar ci = new GregorianCalendar();
		Calendar cf = new GregorianCalendar();

		//se ponen fechas imaginarias en el caso de que hayan nulos
		//ci --> antes de la actual
		//cf --> un anyo más con respecto al actual
		if (fechaini==null) 
			ci = new GregorianCalendar(1972,Calendar.FEBRUARY,19);  
		else
			if (!conrango) {
				ci.setTime(fechaini);
				ci.set(Calendar.HOUR_OF_DAY,0);
				ci.set(Calendar.MINUTE,0);
			}	
			else
				ci.setTime(fechaini);
		
		if (fechafin==null)
			if (!conrango)
				cf = new GregorianCalendar(ci.get( Calendar.YEAR  ),ci.get( Calendar.MONTH ),ci.get( Calendar.DATE  ), 23, 59, 59);
			else
				cf = new GregorianCalendar(ca.get( Calendar.YEAR  )+1,ca.get( Calendar.MONTH ) + 1,ca.get( Calendar.DATE  ));  
		else {
			if (!conrango) {
				cf.setTime(fechafin);
				cf.set(Calendar.HOUR_OF_DAY,23);
				cf.set(Calendar.MINUTE,59);
			}	
			else
				cf.setTime(fechafin);
		}
			
		if ((fechaacomparar.getTime()>ci.getTime().getTime()) && (fechaacomparar.getTime()<cf.getTime().getTime()))
			retorno = true;
		
		return retorno;
	}

	public static String obtenAnyoFromDate(Date fecha) {
		String retorno="";
		if (fecha!=null) {
			Calendar ci = new GregorianCalendar();
			ci.setTime(fecha);
			retorno = "" + ci.get( Calendar.YEAR );
		}
		return retorno;
	}
	
	/**
	 * Calcula si una fecha está entre dos fechas. No importan horas y minutos.
	 * En caso de que fecha fin sea nula, se comparan fecha de inicio y fecha a comparar
	 * @param fechaini
	 * @param fechafin
	 * @param fechaacomparar
	 * @return boolean
	 */
	public static boolean between(Date fechaini, Date fechafin, Date fechaacomparar) {
		boolean retorno=false;
		
		Calendar ca = new GregorianCalendar();
		Calendar ci = new GregorianCalendar();
		Calendar cf = new GregorianCalendar();
		
		//si la fecha de inicio o la de comparar son nulas----->>> salimos indicando false.
		if ((fechaini==null) || (fechaacomparar==null)) {
			return false;
		}
				
		
		//si fecha fin es nula comparamos directamente dia, mes y anyo entre fechaini y fechaacomparar
		if (fechafin==null) {
			ca.setTime(fechaacomparar);
			ci.setTime(fechaini);
			if ( (ci.get( Calendar.YEAR  )==ca.get( Calendar.YEAR  )) &&
				 (ci.get( Calendar.MONTH  )==ca.get( Calendar.MONTH  )) &&
				 (ci.get( Calendar.DATE  )==ca.get( Calendar.DATE  ))
					) retorno=true;
		} else {
			ci.setTime(fechaini);
			ci.set(Calendar.HOUR_OF_DAY,0);
			ci.set(Calendar.MINUTE,0);			
			cf.setTime(fechafin);
			cf.set(Calendar.HOUR_OF_DAY,23);
			cf.set(Calendar.MINUTE,59);			
			if ((fechaacomparar.getTime()>ci.getTime().getTime()) && (fechaacomparar.getTime()<cf.getTime().getTime()))
				retorno = true;		
		}

		
		return retorno;
	}

	
	/**
	 * Método que devuelve el mes siguiente a la fecha que se le pasa.
	 * @param gc
	 * @return GregorianCalendar
	 */
	public static GregorianCalendar siguienteMes(GregorianCalendar gc) {
		GregorianCalendar ci = new GregorianCalendar();
		ci.setTime(gc.getTime());
		int mes=ci.get(Calendar.MONTH);
		int anyo=ci.get(Calendar.YEAR);
		mes++;
		if (mes>11) {
			mes=0;
			anyo++;
		}
		ci.set(Calendar.YEAR,anyo);
		ci.set(Calendar.MONTH,mes);
		return ci;
	}
	
	/**
	 * Método que devuelve el mes anterior a la fecha que se le pasa.
	 * @param gc
	 * @return GregorianCalendar
	 */
	public static GregorianCalendar anteriorMes(GregorianCalendar gc) {
		GregorianCalendar ci = new GregorianCalendar();
		ci.setTime(gc.getTime());
		int mes=ci.get(Calendar.MONTH);
		int anyo=ci.get(Calendar.YEAR);
		mes--;
		if (mes<0) {
			mes=11;
			anyo--;
		}
		ci.set(Calendar.YEAR,anyo);
		ci.set(Calendar.MONTH,mes);
		return ci;
	}	
	
	/**
	 * Método que devuelve la clase Date. Se le pasa un string
	 * con el formato yyyyMMdd
	 * @param ftxt
	 * @return java.util.Date
	 */
	public static Date string2date(String ftxt) {
		GregorianCalendar ci = new GregorianCalendar();
		int anyo=Integer.parseInt(ftxt.substring(0,4));
		int mes=Integer.parseInt(ftxt.substring(4,6))-1;
		int dia=Integer.parseInt(ftxt.substring(6,8));
		ci.set(anyo, mes, dia);
		return ci.getTime();
	}
	

	
	  /** 
	   * Método que devuelve la fecha actual en formato aaaamm 
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
