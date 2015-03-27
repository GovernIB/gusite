package es.caib.gusite.front.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Clase que contiene diferentes utilidades relacionadas con fechas
 * 
 * @author Indra
 * 
 */
public class Fechas extends es.caib.gusite.utilities.date.Fechas {

	public Fechas() {
	}

	/**
	 * Calcula si 'algo' estará vigente o no. método que devuelve true o false
	 * en función de las fechas que se le pasan con respecto a fechaacomparar.
	 * El parametro conrango indica si utilizar la fecha de inicio como fecha
	 * "a partir de la cual" o "solo en ese dia"
	 * 
	 * @sobrecarga de método.<br/>
	 * 
	 * @param fechaini
	 * @param fechafin
	 * @param fechaacomparar
	 * @param conrango
	 * @return boolean
	 */
	public static boolean vigente(Date fechaini, Date fechafin, Date fechaacomparar, boolean conrango) {
		boolean retorno = false;

		Calendar ca = Calendar.getInstance();
		Calendar ci = new GregorianCalendar();
		Calendar cf = new GregorianCalendar();

		// se ponen fechas imaginarias en el caso de que hayan nulos
		// ci --> antes de la actual
		// cf --> un anyo más con respecto al actual
		if (fechaini == null) {
			ci = new GregorianCalendar(1972, Calendar.FEBRUARY, 19);
		} else {
			if (!conrango) {
				ci.setTime(fechaini);
				ci.set(Calendar.HOUR_OF_DAY, 0);
				ci.set(Calendar.MINUTE, 0);
			} else {
				ci.setTime(fechaini);
			}
		}

		if (fechafin == null) {
			cf = new GregorianCalendar(ca.get(Calendar.YEAR) + 1, ca.get(Calendar.MONTH) + 1, ca.get(Calendar.DATE));
		} else {
			if (!conrango) {
				cf.setTime(fechafin);
				cf.set(Calendar.HOUR_OF_DAY, 23);
				cf.set(Calendar.MINUTE, 59);
			} else {
				cf.setTime(fechafin);
			}
		}

		if ((fechaacomparar.getTime() > ci.getTime().getTime()) && (fechaacomparar.getTime() < cf.getTime().getTime())) {
			retorno = true;
		}

		return retorno;
	}

	/**
	 * Calcula si una fecha está entre dos fechas. No importan horas y minutos.
	 * En caso de que fecha fin sea nula, se comparan fecha de inicio y fecha a
	 * comparar
	 * 
	 * @param fechaini
	 * @param fechafin
	 * @param fechaacomparar
	 * @return boolean
	 */
	public static boolean between(Date fechaini, Date fechafin, Date fechaacomparar) {
		boolean retorno = false;

		Calendar ca = new GregorianCalendar();
		Calendar ci = new GregorianCalendar();
		Calendar cf = new GregorianCalendar();

		// si la fecha de inicio o la de comparar son nulas----->>> salimos
		// indicando false.
		if ((fechaini == null) || (fechaacomparar == null)) {
			return false;
		}

		// si fecha fin es nula comparamos directamente dia, mes y anyo entre
		// fechaini y fechaacomparar
		if (fechafin == null) {
			ca.setTime(fechaacomparar);
			ci.setTime(fechaini);
			if ((ci.get(Calendar.YEAR) == ca.get(Calendar.YEAR)) && (ci.get(Calendar.MONTH) == ca.get(Calendar.MONTH))
					&& (ci.get(Calendar.DATE) == ca.get(Calendar.DATE))) {
				retorno = true;
			}
		} else {
			ci.setTime(fechaini);
			ci.set(Calendar.HOUR_OF_DAY, 0);
			ci.set(Calendar.MINUTE, 0);
			cf.setTime(fechafin);
			cf.set(Calendar.HOUR_OF_DAY, 23);
			cf.set(Calendar.MINUTE, 59);
			if ((fechaacomparar.getTime() > ci.getTime().getTime()) && (fechaacomparar.getTime() < cf.getTime().getTime())) {
				retorno = true;
			}
		}

		return retorno;
	}

	/**
	 * Calcula si una fecha está entre un rango de horas.
	 * 
	 * @param fechaacomparar
	 * @param horaInicio
	 * @param minutosInicio
	 * @param horaFin
	 * @param minutosFin
	 * @return boolean
	 */
	public static boolean betweenHours(Date fechaacomparar, int horaInicio, int minutosInicio, int horaFin, int minutosFin) {

		boolean retorno = false;

		// si la fecha a comparar es null salimos con false
		if (fechaacomparar == null) {
			return false;
		}

		Calendar cInicio = new GregorianCalendar();
		cInicio.setTime(fechaacomparar);
		cInicio.set(Calendar.HOUR_OF_DAY, horaInicio);
		cInicio.set(Calendar.MINUTE, minutosInicio);

		Calendar cFin = new GregorianCalendar();
		cFin.setTime(fechaacomparar);
		cFin.set(Calendar.HOUR_OF_DAY, horaFin);
		cFin.set(Calendar.MINUTE, minutosFin);

		if ((fechaacomparar.getTime() >= cInicio.getTime().getTime()) && (fechaacomparar.getTime() <= cFin.getTime().getTime())) {
			retorno = true;
		}

		return retorno;
	}

	/**
	 * Método que devuelve la clase Date. Se le pasa un string con el formato
	 * yyyyMMdd
	 * 
	 * @param ftxt
	 * @return java.util.Date
	 */
	public static Date string2date(String ftxt) {
		GregorianCalendar ci = new GregorianCalendar();
		int anyo = Integer.parseInt(ftxt.substring(0, 4));
		int mes = Integer.parseInt(ftxt.substring(4, 6)) - 1;
		int dia = Integer.parseInt(ftxt.substring(6, 8));
		ci.set(anyo, mes, dia);
		return ci.getTime();
	}

	/**
	 * procedimiento que devuelve la fecha actual en formato aaaamm
	 */
	public static int formatfechaactual2Stats() {

		String txretorno;
		java.util.GregorianCalendar fecha = new java.util.GregorianCalendar();
		int mes = fecha.get(java.util.Calendar.MONTH) + 1;
		int anyo = fecha.get(java.util.Calendar.YEAR);
		String mestxt = "";
		mestxt = (mes < 10) ? "0" + mes : "" + mes;

		txretorno = anyo + mestxt;

		return Integer.parseInt(txretorno);
	}

	/**
	 * Calcula si 'algo' está caducado respecto a la fecha actual. método que
	 * devuelve true o false en función de la fecha de caducidad que se le pasa
	 * por parametro.
	 * 
	 * @sobrecarga de método.<br/>
	 * 
	 * @param fechaCaducidad
	 * @return boolean
	 */
	public static boolean caducada(Date fechaCaducidad) {
		boolean retorno = false;

		Calendar ca = Calendar.getInstance();
		Calendar ci = new GregorianCalendar();

		// ci --> antes de la actual
		// ca --> fecha actual
		if (fechaCaducidad == null) {
			retorno = false;
		} else {
			ci.setTime(fechaCaducidad);
		}

		if (ca.getTime().getTime() > ci.getTime().getTime()) {
			retorno = true;
		}

		return retorno;
	}

	/**
	 * Calcula si 'algo' está postergado respecto a la fecha actual. método que
	 * devuelve true o false en función de la fecha de entrada que se le pasa
	 * por parametro.
	 * 
	 * @sobrecarga de método.<br/>
	 * 
	 * @param fechaCaducidad
	 * @return boolean
	 */
	public static boolean postergada(Date fechaacomparar) {
		boolean retorno = false;

		Calendar ca = Calendar.getInstance();
		Calendar cf = new GregorianCalendar();

		// se ponen fechas imaginarias en el caso de que hayan nulos
		// cf --> un anyo más con respecto al actual
		if (fechaacomparar == null) {
			cf = new GregorianCalendar(ca.get(Calendar.YEAR) + 1, ca.get(Calendar.MONTH) + 1, ca.get(Calendar.DATE));
		} else {
			cf.setTime(fechaacomparar);
		}

		if (ca.getTime().getTime() < cf.getTime().getTime()) {
			retorno = true;
		}

		return retorno;
	}

}
