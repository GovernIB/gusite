package es.caib.gusite.front.microtag;


import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Clase MicroCalendar. Monta el código HTML para montar el calendario que se mostrará en los Microsites.
 * @author pmelia
 *
 */
public class MicroCalendarHTML {

	/**
	 *  Méto Público que devuelve el calendario
	 * @param idsite
	 * @param anyo
	 * @param mes
	 * @param idioma
	 * @return String
	 */
	public static String getTableCalendar(Long idsite, int anyo, int mes, String idioma) {
		return getTableCalendarEvent(idsite, anyo, mes, idioma, new Hashtable<Object, Object>());
	}
	
	/**
	 * Método público que devuelve el evento del calendario, HEAD, TH(tag HTML th define una celda en una tabla) y Contenido
	 * @param idsite
	 * @param anyo
	 * @param mes
	 * @param idioma
	 * @param diaseventos
	 * @return String Código HTML
	 */
	public static String getTableCalendarEvent(Long idsite, int anyo, int mes, String idioma, Hashtable<Object, Object> diaseventos) {
		String retorno="";
		retorno+=getTableCalendarHead(anyo, mes, idioma);
		retorno+=getTableCalendarTH(idioma);
		retorno+=getTableCalendarContenido(idsite, anyo, mes, idioma, diaseventos);
		retorno+="</table>";
		return retorno;
	}
	
	/**
	 * Método privado que devuelve el Head del calendario
	 * @param anyo
	 * @param mes
	 * @param idioma
	 * @return String Código HTML
	 */
	private static String getTableCalendarHead(int anyo, int mes,String idioma) {
		String retorno="";
		ResourceBundle rb =	ResourceBundle.getBundle("ApplicationResources_front", new Locale(idioma.toUpperCase(), idioma.toUpperCase()));
		
		retorno+="<table class=\"calendariTabla\" cellpadding=\"0\" cellspacing=\"0\" summary=\"" + rb.getString("calendario.sumario") + "\">";
		retorno+="<caption>";
		switch(mes) {
			case 1:retorno+=rb.getString("calendario.enero");
					break;
			case 2:retorno+=rb.getString("calendario.febrero");
				break;
			case 3:retorno+=rb.getString("calendario.marzo");
				break;
			case 4:retorno+=rb.getString("calendario.abril");
				break;
			case 5:retorno+=rb.getString("calendario.mayo");
				break;
			case 6:retorno+=rb.getString("calendario.junio");
				break;
			case 7:retorno+=rb.getString("calendario.julio");
				break;
			case 8:retorno+=rb.getString("calendario.agosto");
				break;
			case 9:retorno+=rb.getString("calendario.septiembre");
				break;
			case 10:retorno+=rb.getString("calendario.octubre");
				break;
			case 11:retorno+=rb.getString("calendario.noviembre");
				break;
			case 12:retorno+=rb.getString("calendario.diciembre");
				break;
		}
		retorno+=" " + anyo; 
		retorno+="</caption>";
	
		return retorno;
	}
	
	/**
	 * Método privado que devuelve el TH del calendario
	 * @param idioma
	 * @return String Código HTML
	 */
	private static String getTableCalendarTH(String idioma) {
		String retorno="";
		
		ResourceBundle rb =	ResourceBundle.getBundle("ApplicationResources_front", new Locale(idioma.toUpperCase(), idioma.toUpperCase()));
		retorno="<tr>";
		retorno+="<th scope=\"col\"><abbr title=\""+ rb.getString("calendario.lunes") +"\">"+ rb.getString("calendario.lunes.abrv") +"</abbr></th>";
		retorno+="<th scope=\"col\"><abbr title=\""+ rb.getString("calendario.martes") +"\">"+ rb.getString("calendario.martes.abrv") +"</abbr></th>";
		retorno+="<th scope=\"col\"><abbr title=\""+ rb.getString("calendario.miercoles") +"\">"+ rb.getString("calendario.miercoles.abrv") +"</abbr></th>";
		retorno+="<th scope=\"col\"><abbr title=\""+ rb.getString("calendario.jueves") +"\">"+ rb.getString("calendario.jueves.abrv") +"</abbr></th>";
		retorno+="<th scope=\"col\"><abbr title=\""+ rb.getString("calendario.viernes") +"\">"+ rb.getString("calendario.viernes.abrv") +"</abbr></th>";
		retorno+="<th scope=\"col\"><abbr title=\""+ rb.getString("calendario.sabado") +"\">"+ rb.getString("calendario.sabado.abrv") +"</abbr></th>";
		retorno+="<th scope=\"col\"><abbr title=\""+ rb.getString("calendario.domingo") +"\">"+ rb.getString("calendario.domingo.abrv") +"</abbr></th>";
		retorno+="</tr>";
		
		return retorno;
	}
	
	/**
	 * Método privado que devuelve el contenido del calendario para un site, un mes y un año en concreto
	 * @param idsite
	 * @param anyo
	 * @param mes
	 * @param idioma
	 * @param diaseventos
	 * @return String
	 */
	private static String getTableCalendarContenido(Long idsite, int anyo, int mes, String idioma, Hashtable<Object, Object> diaseventos) {
		String retorno="";
		int cuentadias=1;
		int cuentapares=1;
		
		GregorianCalendar cf = new GregorianCalendar(anyo,mes-1,1,0,0,0);
		int diadelasemana=cf.get(Calendar.DAY_OF_WEEK)-1;
		if (diadelasemana==0) diadelasemana=7;
		int diasmes=cf.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		retorno+="<tr>";
		for (int i=1;i<diadelasemana;i++) {
			cuentadias++;
			retorno+="<td></td>";
		}
		
		for (int j=1;j<diasmes+1;j++) {
			if (diaseventos.get(new Long(j))!=null) {
				retorno+="<td class=\"acte\"><a href=\"" + MicroURI.uriAgenda(idsite,(String)diaseventos.get(new Long(j)),idioma) + "\">" + j + "</td>";
			} else {
				retorno+="<td>"+j+"</td>";
			}
			if ((cuentadias % 7)==0) {
				retorno+="</tr>";
				cuentapares++;
				if ((cuentapares % 2)==0) retorno+="<tr class=\"par\">";
					else retorno+="<tr>";
				
			}
			cuentadias++;
		}
		int restodiasmes=((cuentadias--) % 7);
		if (restodiasmes==0) retorno+="</tr>";
		else {
			for (int x=restodiasmes;x<8;x++){
				retorno+="<td></td>";
			}
			retorno+="</tr>";
		}
		
		return retorno;
	}
	
}
