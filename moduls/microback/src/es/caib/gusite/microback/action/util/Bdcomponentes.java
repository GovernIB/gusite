package es.caib.gusite.microback.action.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;

import es.caib.gusite.microback.Microback;
import es.caib.gusite.microback.base.bean.Recurso;
import es.caib.gusite.microback.utils.Cadenas;
import es.caib.gusite.micromodel.Componente;
import es.caib.gusite.micromodel.Encuesta;
import es.caib.gusite.micromodel.Frqssi;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.Tiposervicio;
import es.caib.gusite.micromodel.TraduccionEncuesta;
import es.caib.gusite.micromodel.TraduccionFrqssi;
import es.caib.gusite.micropersistence.delegate.ComponenteDelegate;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.EncuestaDelegate;
import es.caib.gusite.micropersistence.delegate.FrqssiDelegate;
import es.caib.gusite.micropersistence.delegate.TiposervicioDelegate;


/**
 * Clase que es llamada desde el action de mostrar los componentes de un site (ComponentesTinyAction).
 * 
 * @author Indra
 *
 */
public class Bdcomponentes {
	
	private List<Recurso> listaoriginal;
	private Microsite microsite;
	  
	  public Bdcomponentes(HttpServletRequest request,  ActionForm form) {
		  microsite = (Microsite)request.getSession().getAttribute("MVS_microsite");
		  montartodo();
	  }
	  
	/**
	 * 	Método que guarda un listado con todos los componentes Tiny de un microsite
	 */
	private void montartodo() {
		  try {
			  
		  	listaoriginal = new ArrayList<Recurso>();
		  	
			ArrayList<?> listserofr = new ArrayList<Object>();
			if (microsite.getServiciosOfrecidos()!=null)
				listserofr = Cadenas.getArrayListFromString(microsite.getServiciosOfrecidos());
			
			Iterator<?> iter=listserofr.iterator();
			while (iter.hasNext()) {

				TiposervicioDelegate tiposerdel= DelegateUtil.getTiposervicioDelegate();
				Tiposervicio tiposervicio = tiposerdel.obtenerTiposervicio(new Long((String)iter.next()));
				
			
				if (tiposervicio.getReferencia().equals(Microback.RAGENDA)) {
					listaoriginal.addAll(listaragenda());
				}
				if (tiposervicio.getReferencia().equals(Microback.RNOTICIA)) {
					listaoriginal.addAll(listarcomponentes());
				}	
				if (tiposervicio.getReferencia().equals(Microback.RENCUESTA)) {
					listaoriginal.addAll(listarencuestas());
				}		
				if (tiposervicio.getReferencia().equals(Microback.RQSSI)) {
					listaoriginal.addAll(listarqssi());
				}		
				
			}
			
		  } catch (Exception e) {
			  listaoriginal = new ArrayList<Recurso>();
		  }
	  }
	
	  
	/** 
	 * 	Método que guarda un listado con todos los componentes qssi de un microsite
	 * @return ArrayList
	 * @throws Exception
	 */
	private ArrayList<Recurso> listarqssi() throws Exception {
		  ArrayList<Recurso> lista = new ArrayList<Recurso>();
		  Recurso recurso = new Recurso();
  
	 	  
	 	   FrqssiDelegate qssidel = DelegateUtil.getFrqssiDelegate();
	 	   qssidel.init(microsite.getId());
	 	   qssidel.setPagina(1);qssidel.setTampagina(Microback.MAX_INTEGER);
		   Iterator<?> iter = qssidel.listarFrqssis().iterator();
		  
		   ResourceBundle rb =	ResourceBundle.getBundle("sac-microback-messages");
		   String Urlqssi = (String)rb.getObject("frqssi.url");
			 
		   while (iter.hasNext()) {
			   Frqssi qssi = (Frqssi)iter.next();
		    	recurso = new Recurso();
				recurso.setTipo(Microback.RQSSI);
				recurso.setHead("1");
				recurso.setTitulo( ((TraduccionFrqssi)qssi.getTraduce()).getNombre() ); // titulo qssi
				recurso.setUrl("" + qssi.getId());
				lista.add(recurso);
		   }

		  return lista;
	  }		  
  
	  
	/** 
	 * 	Método que guarda un listado con todos los componentes encuestas de un microsite
	 * @return ArrayList
	 * @throws Exception
	 */
	private ArrayList<Recurso> listarencuestas() throws Exception {
		  ArrayList<Recurso> lista = new ArrayList<Recurso>();
		  Recurso recurso = new Recurso();
  
		  EncuestaDelegate encdel = DelegateUtil.getEncuestaDelegate();
		  encdel.init(microsite.getId());
		  encdel.setPagina(1);encdel.setTampagina(Microback.MAX_INTEGER);
		  Iterator<?> iter = encdel.listarEncuestas().iterator();
		   while (iter.hasNext()) {
		        Encuesta encuesta = (Encuesta)iter.next();
		    	recurso = new Recurso();
				recurso.setTipo(Microback.RENCUESTA);
				recurso.setTitulo( (encuesta.getTraduccion()!=null)?((TraduccionEncuesta)encuesta.getTraduccion()).getTitulo():"[sin titol] id=" + encuesta.getId() );
				recurso.setHead("1");
				recurso.setUrl("" + encuesta.getId());
				lista.add(recurso);
		   }

		  return lista;
	  }		  
	  
	/** 
	 * 	Método que guarda un listado con todos los componentes noticia de un microsite
	 * @return ArrayList
	 * @throws Exception
	 */
	private ArrayList<Recurso> listarcomponentes() throws Exception {
		  ArrayList<Recurso> lista = new ArrayList<Recurso>();
		  Recurso recurso = new Recurso();
  
		  ComponenteDelegate compodel = DelegateUtil.getComponentesDelegate();
		  compodel.init(microsite.getId());
		  compodel.setPagina(1);compodel.setTampagina(Microback.MAX_INTEGER);
		  Iterator<?> iter = compodel.listarComponentes().iterator();
		   while (iter.hasNext()) {
		        Componente componente = (Componente)iter.next();
		    	recurso = new Recurso();
				recurso.setTipo(Microback.RNOTICIA);
				recurso.setTitulo(componente.getNombre());
				recurso.setHead("1");
				recurso.setUrl("" + componente.getId());
				lista.add(recurso);
		   }

		  return lista;
	  }		  
	  
	/** 
	 * 	Método que guarda un listado con todos los componentes agenda de un microsite
	 * @return ArrayList
	 * @throws Exception
	 */
	private ArrayList<Recurso> listaragenda() throws Exception {
		  ArrayList<Recurso> lista = new ArrayList<Recurso>();
		  Recurso recurso = new Recurso();
		  recurso.setTipo(Microback.RAGENDA);
		  recurso.setTitulo("Agenda");
		  recurso.setHead("1");
		  recurso.setUrl("");
		  lista.add(recurso);
		  return lista;
	  }
	  
	public List<Recurso> getListaoriginal() {
		return listaoriginal;
	}
  
	  
}

