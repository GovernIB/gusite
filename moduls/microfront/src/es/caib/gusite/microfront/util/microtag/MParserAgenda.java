package es.caib.gusite.microfront.util.microtag;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.gusite.microfront.util.Fechas;
import es.caib.gusite.micromodel.Agenda;
import es.caib.gusite.micromodel.TraduccionAgenda;
import es.caib.gusite.micropersistence.delegate.AgendaDelegate;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;

/**
 * Esta clase contiene métodos que parsea los tags especiales de los microsites.
 * Devuelven trozos de código HTML pertenecientes a la agenda
 * 
 * @author Indra
 *
 */
public class MParserAgenda extends MParserHTML {

	protected static Log log = LogFactory.getLog(MParserAgenda.class);
	
	public MParserAgenda(String version) {
		super(version);
	}

	/**
	 * Método que devuelve un string preparado para insertar en un html.
	 * Ese string contiene el listado de los proximos eventos de la agenda.
	 * Se le pasa el numero de actos que se quiere mostrar.
	 * @param idmicrosite
	 * @param idioma
	 * @param numeventos
	 * @return StringBuffer código HTML
	 */	
	public StringBuffer getHtmlAgendaListado(Long idmicrosite, String idioma, int numeventos) {
		StringBuffer retorno = new StringBuffer();
		
		try {
	    	AgendaDelegate agendadel = DelegateUtil.getAgendaDelegate();
	    	agendadel.init();
	    	GregorianCalendar gc = new GregorianCalendar();
	    	//retrocedemos 2 meses
			gc = Fechas.anteriorMes(gc);
			gc = Fechas.anteriorMes(gc);
	    	
			GregorianCalendar gc2 = new GregorianCalendar();
			java.sql.Date dt = new java.sql.Date(gc2.getTime().getTime());
	    	agendadel.setWhere("where trad.id.codigoIdioma='" + idioma + "' and agenda.visible='S' and agenda.idmicrosite=" + idmicrosite + " and to_char(agenda.finicio,'yyyy-MM-dd')>='" + dt + "'");
	    	agendadel.setOrderby2(" order by agenda.finicio asc");
	    	agendadel.setPagina(1);agendadel.setTampagina(numeventos);
        	List<?> listaagenda = agendadel.listarAgendas();
        		
        	ResourceBundle rb =	ResourceBundle.getBundle("ApplicationResources_front", new Locale(idioma.toUpperCase(), idioma.toUpperCase()));
        	retorno.append("<div id=\"agendaLlistat\">");
           	if (listaagenda.size()!=0) {
           		
        		retorno.append("<p><strong>" + rb.getString("microhtml.proximosactos") + "</strong>:</p><ul>");
        		Iterator<?> iter = listaagenda.iterator();
        		while (iter.hasNext()) {
        				java.text.SimpleDateFormat dia = new java.text.SimpleDateFormat("HH:mm dd/MM/yyyy");
        				java.text.SimpleDateFormat diaitem = new java.text.SimpleDateFormat("yyyyMMdd");
        				Agenda agenda = (Agenda)iter.next();
        				retorno.append("<li>");
        				String midia = dia.format(agenda.getFinicio()).substring(5);
        				String mihora= dia.format(agenda.getFinicio()).substring(0,5);
        				retorno.append(midia);
        				if(!(mihora.equals("00:00"))){
        					retorno.append(" "+rb.getString("agenda.cuando")+mihora);
        				}	
        				retorno.append("<br />");
        				retorno.append("<a href=\"" + MicroURI.uriAgenda(idmicrosite, "" +diaitem.format(agenda.getFinicio()), idioma) + "\">");
        				retorno.append(((TraduccionAgenda)agenda.getTraduccion(idioma)).getTitulo() + "</a></li>");
        		}
        		retorno.append("</ul>");
        	} else {
        		retorno.append("<p>" + rb.getString("microhtml.noactos") + "</p>");
        	}
           	retorno.append("</div>");
		} catch (Exception e) {
			log.error("[getHtmlAgendaListado]: " + e.getMessage());
			retorno= new StringBuffer("");
		}
		return retorno;
	}
	
	/**
	 * Método que devuelve un string preparado para insertar en un html.
	 * Ese string contiene el calendario de la agenda.
	 * Se le pasa el numero de meses que se quiere mostrar.
	 * @param idmicrosite
	 * @param idioma
	 * @param nummeses de meses que se desea visualizar
	 * @return StringBuffer con el codigo HTML del calendario de la agenda
	 */
	public StringBuffer getHtmlAgendaCalendario(Long idmicrosite, String idioma, int nummeses) {
		StringBuffer retorno = new StringBuffer();
		
		try {
	    	AgendaDelegate agendadel = DelegateUtil.getAgendaDelegate();
	    	agendadel.init();
	    	agendadel.setWhere("where trad.id.codigoIdioma='" + idioma + "' and agenda.visible='S' and agenda.idmicrosite=" + idmicrosite);
	    	agendadel.setPagina(1);agendadel.setTampagina(Integer.MAX_VALUE);
        	List<?> listaagenda=agendadel.listarAgendas();
	
			GregorianCalendar gc = new GregorianCalendar();
			Hashtable<Object, Object> listahash = new Hashtable<Object, Object>();
			int anyo; int mes;

			// Establecemos el valor al día 1
			gc.set(Calendar.DAY_OF_MONTH, 1);
			
			int nummesesaretroceder=2;
			//retrocedemos 2 meses
			gc = Fechas.anteriorMes(gc);
			gc = Fechas.anteriorMes(gc);
	// -------------------- (Inici JOG Feb 2013). Cas especial microsite: 2013 Any Juniper Serra (idsite = 4445)----------
			if (idmicrosite==4445){
				GregorianCalendar gc1 = new GregorianCalendar();
				int mesact = gc1.get(Calendar.MONTH)+1; 
				nummesesaretroceder = mesact+13;
				for (int i=2;i<nummesesaretroceder;i++){
					 gc = Fechas.anteriorMes(gc);
				} 
				nummeses=26-nummesesaretroceder;
			}	
	// -------------------- (Fi JOG) ------------------
			retorno.append(getSelectoragenda(gc,idioma,nummeses+nummesesaretroceder,nummesesaretroceder));
			retorno.append("<div id=\"agendaCalendaris\">");
			for (int i=1;i<=nummeses+nummesesaretroceder;i++){
				
				if (i==(nummesesaretroceder+1)){
					retorno.append("<div>");
				} else {
					retorno.append("<div style=\"display:none;\">");
				}
				listahash = getHashMonthagenda(listaagenda, gc, idmicrosite, idioma);
				anyo=gc.get(Calendar.YEAR); mes=gc.get(Calendar.MONTH)+1;
				retorno.append(MicroCalendarHTML.getTableCalendarEvent(idmicrosite, anyo, mes, idioma, listahash));
				retorno.append("</div>");
				gc = Fechas.siguienteMes(gc);
			}
			retorno.append("</div>");

		} catch (Exception e) {
			log.error("[getHtmlAgendaCalendario]: " + e.getMessage());
			retorno= new StringBuffer("");
		}
		return retorno;
	}	
		
	/* ***************************** métodos auxiliares ************************************************ */
	
	/**
	 * Devuelve el tag "SELECT" de html con los valores preparados para el calendario de la agenda
	 */
	private static String getSelectoragenda(GregorianCalendar gc, String idioma, int nummeses, int mesactual) {
		String retorno="";
		GregorianCalendar gi= new GregorianCalendar();
		gi.setTime(gc.getTime());
		ResourceBundle rb =	ResourceBundle.getBundle("ApplicationResources_front", new Locale(idioma.toUpperCase(), idioma.toUpperCase()));
		// Establecemos el valor al día 1
		gi.set(Calendar.DAY_OF_MONTH, 1);
		
		retorno="<p>" + rb.getString("calendario.mostrarmes") + ":<br /><select name=\"agendaSelect\" id=\"agendaSelect\">";
		for (int i=0;i<nummeses;i++){
			if (i!=0) gi = Fechas.siguienteMes(gi);
			if (i==mesactual)
				retorno+= "<option selected value=\"" + i + "\">";
			else
				retorno+= "<option value=\"" + i + "\">";
			retorno+= rb.getString("calendario.mes." + (gi.get(Calendar.MONTH)+1)) + " " + gi.get(Calendar.YEAR) + "</option>";
		}
		retorno+="</select><input name=\"\" type=\"button\" value=\"" + rb.getString("calendario.mostrar") + "\" onclick=\"mostrarCalendari();\" /></p>";
		return retorno;
	}
	
	/**
	 * Devuelve una hash con el listado de eventos de un mes determinado.
	 * Key: dia del mes (long)
	 * Value: fecha (String formateado)
	 */
	private static Hashtable<Object, Object> getHashMonthagenda(List<?> listaagenda, GregorianCalendar gc, Long idmicrosite, String idioma) {
		Hashtable<Object, Object> listahash = new Hashtable<Object, Object>();
		
		try {
			
	    	java.text.SimpleDateFormat fech = new java.text.SimpleDateFormat("yyyyMMdd");
	    	for(int i=1;i<=gc.getActualMaximum(Calendar.DAY_OF_MONTH);i++) {
	    		GregorianCalendar fecha2 = new GregorianCalendar();
	    		fecha2.setTime(gc.getTime());
	    		fecha2.set(Calendar.DAY_OF_MONTH, i);
	    		fecha2.set(Calendar.HOUR_OF_DAY,0);
	    		fecha2.set(Calendar.MINUTE,0);
	    		String f_format2 = fech.format(fecha2.getTime());
	        	
	    		Iterator<?> iter = listaagenda.iterator();
	    		while (iter.hasNext()) {
	    			Agenda agenda = (Agenda)iter.next();
	    			if (Fechas.between(agenda.getFinicio(),agenda.getFfin(),fecha2.getTime())) listahash.put(new Long(fecha2.get(java.util.Calendar.DAY_OF_MONTH)),f_format2);
	    		}
	    		
	    	}
		} catch (Exception e) {
			log.error("[getHashMonthagenda]: " + e.getMessage());
		}
 		return listahash;
	}
	
}
