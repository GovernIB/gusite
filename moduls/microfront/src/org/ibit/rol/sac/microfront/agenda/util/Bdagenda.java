package org.ibit.rol.sac.microfront.agenda.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ibit.rol.sac.microfront.Microfront;
import org.ibit.rol.sac.microfront.base.Bdbase;
import org.ibit.rol.sac.microfront.base.bean.ErrorMicrosite;
import org.ibit.rol.sac.microfront.estadistica.util.StatManager;
import org.ibit.rol.sac.microfront.util.Fechas;
import org.ibit.rol.sac.micromodel.Agenda;
import org.ibit.rol.sac.micromodel.Estadistica;
import org.ibit.rol.sac.micropersistence.delegate.AgendaDelegate;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;

/**
 * Clase Bdagenda. Recoge los datos para mostrarlos en el front.
 * @author Indra
 */
public class Bdagenda extends Bdbase  {

	protected static Log log = LogFactory.getLog(Bdagenda.class);
	
	private ArrayList<?> listaeventos = new ArrayList<Object>(); //contiene los eventos del dia
	private boolean error = false;
	private Date fecha = new Date();
	private Hashtable<?, ?> parametros = new Hashtable<Object, Object>();
	private String url_sinpagina="";
	
	private HttpServletRequest req;
	
	public void finalize() {
		this.dispose();
	}
	
	/**
	 * Metodo p�blico que borra las variables de agenda
	 */
	public void dispose() {
		url_sinpagina = null;
		listaeventos = null;
		fecha = null;
		req = null;
		parametros = null;
		super.dispose();
	}
	
    /**
     * Constructor de la clase, carga la agenda a partir de la request
     * @param request
     * @exception  IOException, ServletException, Exception
     */
	public Bdagenda(HttpServletRequest request) throws Exception {
		super(request);
		req = request;
		if ((microsite!=null) && (existeServicio(Microfront.RAGENDA))) {
			recogeragenda();
			preparaseulet();
		} else {
			beanerror = new ErrorMicrosite("Error", "En estos momentos no es posible mostrar la p�gina solicitada.");
			error=true;
		}		
	}
	

    /**
     * Metodo p�blico que carga los datos de la agenda
     */
	private void recogeragenda() {
		try {
			fecha = Fechas.string2date("" + req.getParameter(Microfront.PCONT));
	    	AgendaDelegate agendadel = DelegateUtil.getAgendaDelegate();
	    	agendadel.init();
	    	agendadel.setWhere("where index(trad)='"+idioma+"' and agenda.visible='S' and agenda.idmicrosite=" + super.idsite);
	    	agendadel.setOrderby2(" ORDER BY agenda.actividad, NVL(agenda.ffin,SYSDATE)");

	        // Indicamos la p�gina a visualizar
	        if (req.getParameter("pagina")!=null)
	        	agendadel.setPagina(Integer.parseInt(req.getParameter("pagina")));
	        else
	        	agendadel.setPagina(1);
	    	
	        listaeventos=agendadel.listarAgendas(fecha,idioma);
	        traducelista();
	        parametros=agendadel.getParametros();
 
			//si llegamos aqui.... todo ok, as� que grabamos la estadistica
			if (idsite.longValue()==0) error=true;
			 else if ("no".equals(""+req.getSession().getAttribute("MVS_stat"))) log.info("Skip Estadistica, preview conten"); 
				 		else grabarestadistica();
			
		} catch (Exception e) {
			log.error(e.getMessage());
			beanerror = new ErrorMicrosite("Error", "Se ha producido un error desconocido en la agenda.");
			error=true;
			listaeventos = null;
		}
	}

	private void traducelista() {
		ArrayList<Agenda> lista2 = new ArrayList<Agenda>();
		boolean tmpadd=false;
		Iterator<?> iter = listaeventos.iterator();
		while (iter.hasNext()) {
			Agenda agenda = (Agenda)iter.next();
			if (agenda.getActividad()!=null) 
				agenda.getActividad().setIdi(idioma);
			
			tmpadd=Fechas.vigente(agenda.getFinicio(),agenda.getFfin(),fecha,false);
			
			if (tmpadd) lista2.add(agenda);
		}
		listaeventos=lista2;
	}

	private void grabarestadistica() throws Exception {
		Iterator<?> iter = listaeventos.iterator();
		while (iter.hasNext()) {
			Agenda agenda = (Agenda)iter.next();
			req.getSession().getServletContext().setAttribute("bufferStats", 
					StatManager.grabarestadistica(agenda, super.publico, 
							(List<Estadistica>) req.getSession().getServletContext().getAttribute("bufferStats")));
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
	 * Implementacion del m�todo abstracto.
	 * Se le indica que estamos en el servicio de contacto.
	 * @return Date una fecha
	 */
	public String setServicio() {
		return Microfront.RAGENDA;
	}

	/**
	 * M�todo p�blico que devuelve la fecha
	 * @return Date una fecha
	 */
	public Date getFecha() {
		return fecha;
	}

    /**
     * Metodo p�blico que devuelve los par�metros
     * @return Hashtable
     */	
	public Hashtable<?, ?> getParametros() {
		return parametros;
	}
	
	/**
	 * M�todo privado que quita el parametro 'pagina' de la lista de parametros del servlet
	 */
	private void preparaseulet() {
		url_sinpagina = url;
		int pospagina = url.indexOf(Microfront.PPAGINA);
		if (pospagina!=-1) {
			url_sinpagina = url.substring(0,pospagina);
		}
	}
	
	/**
	 * M�todo p�blico que devuelve la url si p�gina
     * @return String
	 */
	public String getUrl_sinpagina() {
		return url_sinpagina;
	}
	
	
}
