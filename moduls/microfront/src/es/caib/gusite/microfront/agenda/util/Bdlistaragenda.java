package es.caib.gusite.microfront.agenda.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.gusite.microfront.base.bean.ErrorMicrosite;
import es.caib.gusite.microfront.Microfront;
import es.caib.gusite.microfront.agenda.actionforms.BuscaOrdenaAgendaActionForm;
import es.caib.gusite.microfront.base.Bdbase;
import es.caib.gusite.microfront.util.Fechas;
import es.caib.gusite.microfront.util.microtag.MParserAgenda;
import es.caib.gusite.micromodel.Agenda;
import es.caib.gusite.micropersistence.delegate.AgendaDelegate;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;


/**
 * Clase Bdlistaragenda. Recoge los datos para mostrarlos en el front.
 * @author Indra
 */
public class Bdlistaragenda  extends Bdbase  {

	protected static Log log = LogFactory.getLog(Bdlistaragenda.class);
	
	private String url_sinpagina="";
	private ArrayList<?> listaeventos = new ArrayList<Object>();//contiene los eventos del dia
	private BuscaOrdenaAgendaActionForm formulario = new BuscaOrdenaAgendaActionForm();
	private HttpServletRequest req;
	private String tagHtmlAgenda="";
	private Hashtable<?, ?> parametros = new Hashtable<Object, Object>();
	private boolean error = false;
	
	public void finalize() {
		this.dispose();
	}
	
	/**
	 * Metodo público que borra las variables de agenda.
	 */
	public void dispose() {
		url_sinpagina = null;
		listaeventos = null;
		formulario = null;
		req = null;
		tagHtmlAgenda = null;
		parametros = null;
		super.dispose();
	}
	
	/**
     * Metodo público para listar la agenda a partir de la request
     * @param request HttpServletRequest
     * @exception  IOException, ServletException, Exception
     */
	public Bdlistaragenda(HttpServletRequest request) throws Exception {
		super(request);
		req = request;
		if (microsite!=null) {
			prepararCalendario();
			crearlistado();
			preparaseulet();
		} else {
			beanerror = new ErrorMicrosite("Error", "En estos momentos no es posible mostrar la página solicitada.");
			error=true;
		}		
	}

	/**
	 * Implementacion del método abstracto.
	 * Se le indica que estamos en el servicio de contacto.
	 */
	public String setServicio() {
		return Microfront.RAGENDA;
	}
	
	/**
	 * Método privado para preparar Calendario.
	 */
	private void prepararCalendario() {
		MParserAgenda parseagenda = new MParserAgenda(microsite.getRestringido());
		tagHtmlAgenda=parseagenda.getHtmlAgendaCalendario(idsite,idioma,5).toString();
	}
	
	/**
	 * Método privado para crear el listado.
	 */
	private void crearlistado() {
		try {
			
			AgendaDelegate agendadel = DelegateUtil.getAgendaDelegate();
	    	agendadel.init();
	    	agendadel.setTampagina(3);
	    	GregorianCalendar gc = new GregorianCalendar();
	    	//retrocedemos 2 meses
			gc = Fechas.anteriorMes(gc);
			gc = Fechas.anteriorMes(gc);
	    	
			GregorianCalendar gc2 = new GregorianCalendar();
			java.sql.Date dt = new java.sql.Date(gc2.getTime().getTime());
	    	agendadel.setWhere("where trad.id.codigoIdioma='"+idioma+"' and agenda.visible='S' and agenda.idmicrosite=" + super.idsite + " and to_char(agenda.finicio,'yyyy-MM-dd')>='" + dt + "'");
	    	agendadel.setOrderby2(" order by agenda.finicio asc");
        	        
	        if (formulario.getFiltro()!= null && formulario.getFiltro().length()>0)
	        	agendadel.setFiltro(formulario.getFiltro());
	    
	        if (formulario.getOrdenacion()!= null && formulario.getOrdenacion().length()>0)
	        	agendadel.setOrderby(formulario.getOrdenacion());
	
	        // Indicamos la página a visualizar
	        if (req.getParameter("pagina")!=null)
	        	agendadel.setPagina(Integer.parseInt(req.getParameter("pagina")));
	        else
	        	agendadel.setPagina(1);
	        
	        listaeventos=agendadel.listarAgendas(idioma);
	        traducelista();
	        parametros=agendadel.getParametros();
	        
	        //Si hay algún registro limpiamos el filtro
	        if (listaeventos.size()==0) formulario.setFiltro("");
	        
			if (idsite.longValue()==0) error=true;
		        
		} catch (Exception e) {
			log.error(e.getMessage());
			beanerror = new ErrorMicrosite("Error", "Se ha producido un error desconocido en el listado de la agenda.");
			error=true;
			listaeventos = null;
			parametros = new Hashtable<Object, Object>();
		}

	}	
	
	private void traducelista() {
		ArrayList<Agenda> lista2 = new ArrayList<Agenda>();
		Iterator<?> iter = listaeventos.iterator();
		while (iter.hasNext()) {
			Agenda agenda = (Agenda)iter.next();
			if (agenda.getActividad()!=null) 
				agenda.getActividad().setIdi(idioma);
			 lista2.add(agenda);
		}
		listaeventos=lista2;
	}
	
	/**
	 * método que quita el parametro 'pagina' de la lista de parametros del servlet
	 *
	 */
	private void preparaseulet() {
		url_sinpagina = url;
		int pospagina = url.indexOf(Microfront.PPAGINA);
		if (pospagina!=-1) {
			url_sinpagina = url.substring(0,pospagina);
		}
	}
	
	/**
	 * Método público que devuelve los eventos de una Agenda.
	 * @return  ArrayList una lista de eventos
	 */
	public ArrayList<?> getListaeventos() {
		return listaeventos;
	}
	
	/**
	 * Método público que devuelve un booleano para conocer si hay error
	 * @return  boolean  
	 */
	public boolean isError() {
		return error;
	}

    /**
     * Método público que devuelve los parámetros
     * @return Hashtable
     */	
	public Hashtable<?, ?> getParametros() {
		return parametros;
	}
	
    /**
     * Metodo público que devuelve tagHtmlAgenda
     * @return String
     */	
	public String getTagHtmlAgenda() {
		return tagHtmlAgenda;
	}


	/**
	 * Método público que establece el TagHtmlAgenda
	 * @param tagHtmlAgenda Código Html de la Agenda
	 */
	public void setTagHtmlAgenda(String tagHtmlAgenda) {
		this.tagHtmlAgenda = tagHtmlAgenda;
	}
	
	/**
	 * Método público que devuelve la url sin página
	 * @return String una url sinpagina
	 */
	public String getUrl_sinpagina() {
		return url_sinpagina;
	}
	
}
