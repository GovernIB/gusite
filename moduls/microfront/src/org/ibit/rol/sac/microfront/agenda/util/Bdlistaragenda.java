package org.ibit.rol.sac.microfront.agenda.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ibit.rol.sac.microfront.Microfront;
import org.ibit.rol.sac.microfront.agenda.actionforms.BuscaOrdenaAgendaActionForm;
import org.ibit.rol.sac.microfront.base.Bdbase;
import org.ibit.rol.sac.microfront.base.bean.ErrorMicrosite;
import org.ibit.rol.sac.microfront.util.Fechas;
import org.ibit.rol.sac.microfront.util.microtag.MParserAgenda;
import org.ibit.rol.sac.micromodel.Agenda;
import org.ibit.rol.sac.micropersistence.delegate.AgendaDelegate;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;


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
	 * Metodo p�blico que borra las variables de agenda.
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
     * Metodo p�blico para listar la agenda a partir de la request
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
			beanerror = new ErrorMicrosite("Error", "En estos momentos no es posible mostrar la p�gina solicitada.");
			error=true;
		}		
	}

	/**
	 * Implementacion del m�todo abstracto.
	 * Se le indica que estamos en el servicio de contacto.
	 */
	public String setServicio() {
		return Microfront.RAGENDA;
	}
	
	/**
	 * M�todo privado para preparar Calendario.
	 */
	private void prepararCalendario() {
		MParserAgenda parseagenda = new MParserAgenda(microsite.getRestringido());
		tagHtmlAgenda=parseagenda.getHtmlAgendaCalendario(idsite,idioma,5).toString();
	}
	
	/**
	 * M�todo privado para crear el listado.
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
	    	agendadel.setWhere("where index(trad)='"+idioma+"' and agenda.visible='S' and agenda.idmicrosite=" + super.idsite + " and to_char(agenda.finicio,'yyyy-MM-dd')>='" + dt + "'");
	    	agendadel.setOrderby2(" order by agenda.finicio asc");
        	        
	        if (formulario.getFiltro()!= null && formulario.getFiltro().length()>0)
	        	agendadel.setFiltro(formulario.getFiltro());
	    
	        if (formulario.getOrdenacion()!= null && formulario.getOrdenacion().length()>0)
	        	agendadel.setOrderby(formulario.getOrdenacion());
	
	        // Indicamos la p�gina a visualizar
	        if (req.getParameter("pagina")!=null)
	        	agendadel.setPagina(Integer.parseInt(req.getParameter("pagina")));
	        else
	        	agendadel.setPagina(1);
	        
	        listaeventos=agendadel.listarAgendas(idioma);
	        traducelista();
	        parametros=agendadel.getParametros();
	        
	        //Si hay alg�n registro limpiamos el filtro
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
	 * m�todo que quita el parametro 'pagina' de la lista de parametros del servlet
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
	 * M�todo p�blico que devuelve los eventos de una Agenda.
	 * @return  ArrayList una lista de eventos
	 */
	public ArrayList<?> getListaeventos() {
		return listaeventos;
	}
	
	/**
	 * M�todo p�blico que devuelve un booleano para conocer si hay error
	 * @return  boolean  
	 */
	public boolean isError() {
		return error;
	}

    /**
     * Metodo p�blico que devuelve los par�metros
     * @return Hashtable
     */	
	public Hashtable<?, ?> getParametros() {
		return parametros;
	}
	
    /**
     * Metodo p�blico que devuelve tagHtmlAgenda
     * @return String
     */	
	public String getTagHtmlAgenda() {
		return tagHtmlAgenda;
	}


	/**
	 * M�todo p�blico que establece el TagHtmlAgenda
	 * @param tagHtmlAgenda C�digo Html de la Agenda
	 */
	public void setTagHtmlAgenda(String tagHtmlAgenda) {
		this.tagHtmlAgenda = tagHtmlAgenda;
	}
	
	/**
	 * M�todo p�blico que devuelve la url sin p�gina
	 * @return String una url sinpagina
	 */
	public String getUrl_sinpagina() {
		return url_sinpagina;
	}
	
}
