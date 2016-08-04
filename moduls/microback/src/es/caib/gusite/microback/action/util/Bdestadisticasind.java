package es.caib.gusite.microback.action.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import es.caib.gusite.micromodel.*;
import es.caib.gusite.micropersistence.delegate.*;
import org.apache.struts.action.ActionForm;

import es.caib.gusite.microback.Microback;
import es.caib.gusite.microback.actionform.busca.BuscaOrdenaEstadisticaIndActionForm;
import es.caib.gusite.microback.base.bean.Pardato;


/**
 * Clase que es llamada desde el action de mostrar las estadisticas generales de un site (EstadisticasIndAction).
 * 
 * @author Indra
 *
 */
public class Bdestadisticasind {

	private List<?> listaoriginal; //la lista maneja bean de EstadisticaGroup
	private List<?> listaresultante; //la lista maneja bean de EstadisticaGroup
	private List<Pardato> listaanyos;	//la lista maneja string
	private EstadisticaGroup statmicrosite;
	private HttpServletRequest req;
	private Microsite microsite;
	BuscaOrdenaEstadisticaIndActionForm formulario;
	private Integer publico=null;
	
	  private static class RefAscComparator implements Comparator {
		    public int compare(Object element1, Object element2) {
		      String lower1 = ((EstadisticaGroup)element1).getReferencia().toLowerCase();
		      String lower2 = ((EstadisticaGroup)element2).getReferencia().toLowerCase();
		      return lower1.compareTo(lower2);
		    }
		  }
		  
	  private static class TituloAscComparator implements Comparator {
		    public int compare(Object element1, Object element2) {
		      String lower1 = ((EstadisticaGroup)element1).getTituloitem().toLowerCase();
		      String lower2 = ((EstadisticaGroup)element2).getTituloitem().toLowerCase();
		      return lower1.compareTo(lower2);
		    }
		  }
	  
	  private static class AccesoAscComparator implements Comparator {
			    public int compare(Object element1, Object element2) {
			    	Integer lower1 = new Integer(((EstadisticaGroup)element1).getAccesos());
			    	Integer lower2 = new Integer(((EstadisticaGroup)element2).getAccesos());
			      return lower1.compareTo(lower2);
			    }
		  }

	  private static class RefDescComparator implements Comparator {
			    public int compare(Object element1, Object element2) {
			      String lower1 = ((EstadisticaGroup)element2).getReferencia().toLowerCase();
			      String lower2 = ((EstadisticaGroup)element1).getReferencia().toLowerCase();
			      return lower1.compareTo(lower2);
			    }
			  }
			  
	  private static class TituloDescComparator implements Comparator {
		    public int compare(Object element1, Object element2) {
		      String lower1 = ((EstadisticaGroup)element2).getTituloitem().toLowerCase();
		      String lower2 = ((EstadisticaGroup)element1).getTituloitem().toLowerCase();
		      return lower1.compareTo(lower2);
		    }
		  }
	  
	  private static class AccesoDescComparator implements Comparator {
				    public int compare(Object element1, Object element2) {
				    	Integer lower1 = new Integer(((EstadisticaGroup)element2).getAccesos());
				    	Integer lower2 = new Integer(((EstadisticaGroup)element1).getAccesos());
				      return lower1.compareTo(lower2);
				    }
		  }

	  public Bdestadisticasind(HttpServletRequest request,  ActionForm form) {
			  microsite = (Microsite)request.getSession().getAttribute("MVS_microsite");
			  formulario = (BuscaOrdenaEstadisticaIndActionForm) form;
			  req=request;
			  if (formulario.getFiltro2()!=null) {
				  if (formulario.getFiltro2().equals("ALL")) publico = null; //internet intranet
				  if (formulario.getFiltro2().equals("PUB")) publico = new Integer(1);//internet
				  if (formulario.getFiltro2().equals("PRV")) publico = new Integer(2);//intranet
			  }
			  obtenervisitas();
			  montarlistaservicios();
			  ordenarlista();  
			  crearlistaanyos();
		  }

	/**
	 * Método que comprueba si las estadisticas almacenadas en session
	 * pertenecen al mismo microsite
	 */
	private boolean estadisticaInvalida() {
		if ((statmicrosite != null) && (microsite != null)
				&& (statmicrosite.getMicrosite().getId() != microsite.getId()))
			return true;
		else
			return false;
	}
	 /**
	 * Método que obtiene las visitas de un microsite
	 */
	private void obtenervisitas() {
		  try {
			  	
			  	statmicrosite = (EstadisticaGroup)req.getSession().getAttribute("MVA_statmicrositeind");
			  	String filtroant = "" + req.getSession().getAttribute("MVA_filtrostatind");
			  	String filtroant2 = "" + req.getSession().getAttribute("MVA_filtrostatind2");
			  	boolean calculodenuevo=false;
			  	calculodenuevo = (statmicrosite==null) || estadisticaInvalida() || !filtroant.equals(formulario.getFiltro()) || !filtroant2.equals(formulario.getFiltro2()); 
			  	if (calculodenuevo) {
				  	EstadisticaGroupDelegate estagdel = DelegateUtil.getEstadisticaGroupDelegate();
				  	estagdel.init();
				  	estagdel.setWhere("where stat.idmicrosite=" + microsite.getId() + " and stat.referencia='" + Microback.RMICROSITE + "' and " + obtenerfiltro());
				  	if (estagdel.listarEstadisticasbyRefThin(publico).size()==0) {
				  		statmicrosite = new EstadisticaGroup();
				  		statmicrosite.setAccesos(0);
				  	} else {
				  		statmicrosite =  (EstadisticaGroup)estagdel.listarEstadisticasbyRefThin(publico).iterator().next();
				  	}				  	
				  	
			  	}
			  	
		  } catch (Exception e) {
			  listaoriginal = new ArrayList<Object>();
		  }
	  }
	  

	 /**
	 * Método que monta una lista con los servicios activos de un microsite
	 */	  
	  private void montarlistaservicios() {
		  try {
			  	listaoriginal = (ArrayList<?>)req.getSession().getAttribute("MVA_listaestadisticaind");
			  	String filtroant = "" + req.getSession().getAttribute("MVA_filtrostatind");
			  	String filtroant2 = "" + req.getSession().getAttribute("MVA_filtrostatind2");
			  	boolean calculodenuevo=false;
			  	calculodenuevo = (listaoriginal==null) || (listaoriginal.size()==0) || !filtroant.equals(formulario.getFiltro()) || !filtroant2.equals(formulario.getFiltro2()); 
			  	if (calculodenuevo) {
				  	EstadisticaGroupDelegate estagdel = DelegateUtil.getEstadisticaGroupDelegate();
				  	estagdel.init();
				  	//estagdel.setWhere("where stat.idmicrosite=" + microsite.getId() + " and stat.referencia!='" + Microback.RMICROSITE + "' and stat.referencia!='" + Microback.RFAQ + "' and "  + obtenerfiltro());
				  	estagdel.setWhere("where stat.idmicrosite=" + microsite.getId() + " and stat.referencia!='" + Microback.RMICROSITE + "' and "  + obtenerfiltro());
				  	estagdel.setPagina(1);
				  	estagdel.setTampagina(Microback.MAX_INTEGER);
				  	listaoriginal = estagdel.listarEstadisticasbyItemThin(publico);
				  	ponernombreslista();
				  	req.getSession().setAttribute("MVA_filtrostatind", formulario.getFiltro());
				  	req.getSession().setAttribute("MVA_filtrostatind2", formulario.getFiltro2());
			  	}
		  } catch (Exception e) {
			  listaoriginal = new ArrayList<Object>();
		  }
	  }

	/**
	 * Método que pone los nombres de los servicios activos de un microsite en una lista
	 */
	private void ponernombreslista() {

		AgendaDelegate agendadel = DelegateUtil.getAgendaDelegate();
		NoticiaDelegate noticiadel = DelegateUtil.getNoticiasDelegate();
		ContenidoDelegate contenidodel = DelegateUtil.getContenidoDelegate();
		ContactoDelegate contactodel = DelegateUtil.getContactoDelegate();
		EncuestaDelegate encuestadel = DelegateUtil.getEncuestaDelegate();
			  
			  Iterator<?> iter = listaoriginal.iterator();
			  			
			  int hist = -1; // Estadisticas provenientes del site antes de la importación
			  
			  while (iter.hasNext()) {
				  EstadisticaGroup statg = (EstadisticaGroup)iter.next();
				  if (statg.getReferencia().equals(Microback.RAGENDA)) {
					  statg.setNombreservicio((String)Microback.RSERVICIOS.get(Microback.RAGENDA));
					  if (statg.getItem().intValue() == hist) 
						  statg.setTituloitem("[ Accesos microsite reemplazado ]");
					  else
					  {
						  try {
						  Agenda agenda = agendadel.obtenerAgenda(statg.getItem());
						  //statg.setTituloitem( ((TraduccionAgenda)agenda.getTraduccion()).getTitulo() );
						  statg.setTituloitem( (((TraduccionAgenda)agenda.getTraduccion()).getTitulo()==null)? "[ sin titulo ]" : ((TraduccionAgenda)agenda.getTraduccion()).getTitulo() );
						  } catch (Exception e) {
							  statg.setTituloitem("[ evento " + statg.getItem() + " eliminado ]");
						  }
					  }
				  }

				  if (statg.getReferencia().equals(Microback.RCONTACTO)) {
					  statg.setNombreservicio((String)Microback.RSERVICIOS.get(Microback.RCONTACTO));
					  if (statg.getItem().intValue() == hist) 
						  statg.setTituloitem("[ Accesos microsite reemplazado ]");
					  else
					  {
						  try {
						  Contacto contacto = contactodel.obtenerContacto(statg.getItem());
						  statg.setTituloitem(contacto.getTitulocontacto(Idioma.getIdiomaPorDefecto()) );
						  } catch (Exception e) {
							  statg.setTituloitem("[ contacto " + statg.getItem() + " eliminado ]");
						  }
					  }
				  }
				  if (statg.getReferencia().equals(Microback.RCONTENIDO)) {
					  statg.setNombreservicio((String)Microback.RSERVICIOS.get(Microback.RCONTENIDO));
					  if (statg.getItem().intValue() == hist) 
						  statg.setTituloitem("[ Accesos microsite reemplazado ]");
					  else
					  {
						  try {
						  Contenido contenido = contenidodel.obtenerContenido(statg.getItem());
						  statg.setTituloitem( (((TraduccionContenido)contenido.getTraduccion()).getTitulo()==null)? "[ sin titulo ]" : ((TraduccionContenido)contenido.getTraduccion()).getTitulo() );
						  } catch (Exception e) {
							  statg.setTituloitem("[ contenido " + statg.getItem() + " eliminado ]");
						  }
					  }
				  }
				  if (statg.getReferencia().equals(Microback.RNOTICIA)) {
					  statg.setNombreservicio((String)Microback.RSERVICIOS.get(Microback.RNOTICIA));
					  if (statg.getItem().intValue() == hist) 
						  statg.setTituloitem("[ Accesos microsite reemplazado ]");
					  else
					  {
						  try {
						  Noticia noticia = noticiadel.obtenerNoticiaThin(statg.getItem(), Idioma.getIdiomaPorDefecto());
						  statg.setTituloitem( (((TraduccionNoticia)noticia.getTraduccion()).getTitulo()==null)? "[ sin titulo ]" : ((TraduccionNoticia)noticia.getTraduccion()).getTitulo() );
						  } catch (Exception e) {
							  statg.setTituloitem("[ Elemento " + statg.getItem() + " eliminado ]");
						  }
					  }
				  }
				  if (statg.getReferencia().equals(Microback.RENCUESTA)) {
					  statg.setNombreservicio((String)Microback.RSERVICIOS.get(Microback.RENCUESTA));
					  if (statg.getItem().intValue() == hist) 
						  statg.setTituloitem("[ Accesos microsite reemplazado ]");
					  else
					  {
						  try {
						  Encuesta encuesta = encuestadel.obtenerEncuesta(statg.getItem());
						  statg.setTituloitem( (((TraduccionEncuesta)encuesta.getTraduccion()).getTitulo()==null)? "[ sin titulo ]" : ((TraduccionEncuesta)encuesta.getTraduccion()).getTitulo() );
						  } catch (Exception e) {
							  statg.setTituloitem("[ encuesta " + statg.getItem() + " eliminada ]");
						  }
					  }
				  }
				  /* Se mantienen los procedimientos por histórico */
				  if (statg.getReferencia().equals(Microback.RPROCEDIMIENTO)) {
					  statg.setNombreservicio((String)Microback.RSERVICIOS.get(Microback.RPROCEDIMIENTO));
					  if (statg.getItem().intValue() == hist) 
						  statg.setTituloitem("[ Accesos microsite reemplazado ]");

				  }
				  if (statg.getReferencia().equals(Microback.RFAQ)) {
					  statg.setNombreservicio((String)Microback.RSERVICIOS.get(Microback.RFAQ));
					  if (statg.getItem().intValue() == hist) {
						  statg.setTituloitem("[ Accesos microsite reemplazado ]");
					  }
				  }		
				  if (statg.getReferencia().equals(Microback.RQSSI)) {
					  statg.setNombreservicio((String)Microback.RSERVICIOS.get(Microback.RQSSI));
					  if (statg.getItem().intValue() == hist) 
						  statg.setTituloitem("[ Accesos microsite reemplazado ]");

				  }
			  }
	  }
	  
	  /**
	   * Método que obtiene el filtro según las fechas
	   *
	   */
	  private String obtenerfiltro() {
		  	String filtro=" 1=1";
	        
		    if (formulario.getFiltro()!= null && formulario.getFiltro().length()>0) {
			  	String valor=formulario.getFiltro();
		        if (valor.equals("NOW")) {
		          filtro = " (stat.mes=" + mesActual() + " OR stat.mes=" + mesAnterior() + ") ";
		        } else if (valor.equals("ALL")) {
		          filtro = " stat.mes LIKE '%' ";
		        } else {
		          filtro = " stat.mes LIKE '" + valor + "%' ";
		        }
	        }
		  	return filtro;
	  }
	  
	  /**
	   * Método que ordena la lista según el parametro ordenacion
	   *
	   */
	  private void ordenarlista() {
		    listaresultante = new ArrayList(listaoriginal);
		    Comparator comp = new AccesoDescComparator();
		    if (formulario.getOrdenacion()!= null && formulario.getOrdenacion().length()>0) {
			  	if (formulario.getOrdenacion().equals("Aref")) 
				  	comp = new RefAscComparator();
			  	if (formulario.getOrdenacion().equals("Atitulo")) 
				  	comp = new TituloAscComparator();
			  	if (formulario.getOrdenacion().equals("Aaccesos")) 
				  	comp = new AccesoAscComparator();
			  	if (formulario.getOrdenacion().equals("Dref")) 
				  	comp = new RefDescComparator();
			  	if (formulario.getOrdenacion().equals("Dtitulo")) 
				  	comp = new TituloDescComparator();
			  	if (formulario.getOrdenacion().equals("Daccesos")) 
				  	comp = new AccesoDescComparator();
	        }
		    Collections.sort(listaresultante, comp);
		  	
	  }

	  /**
	   * Método que devuelve una lista con los años
	   *
	   */
	  private void crearlistaanyos() {
		  listaanyos=new ArrayList<Pardato>();
	        
		  java.util.GregorianCalendar fecha = new java.util.GregorianCalendar();
		  java.util.GregorianCalendar fechamicrosite = new java.util.GregorianCalendar();
		  fechamicrosite.setTime(microsite.getFecha());
	      int anyo=fecha.get(java.util.Calendar.YEAR);
	      int anyomicrosite=fechamicrosite.get(java.util.Calendar.YEAR);
	      
	      for (int i=anyo;i>=anyomicrosite;i--) {
	    	  
	    	  Pardato elemento = new Pardato();
	    	  elemento.setKey("" + i);
	    	  elemento.setValue("" + i);
	    	  listaanyos.add(elemento);
	      }
	  }
	  
	  /**
	   * Método que devuelve el mes actual
	   *
	   */	  
	    private String mesActual() {
	        String retorno = "";
	        java.text.SimpleDateFormat dia = new java.text.SimpleDateFormat("yyyyMM");
	        java.util.Date fecha_ahora = new java.util.Date();
	        retorno = dia.format(fecha_ahora);
	        return retorno;
	    }
	    
	  /**
	   * Método que devuelve el mes anterior
	   *
	   */	    
	    private String mesAnterior() {
	        String retorno = "";
	        java.util.GregorianCalendar fecha = new java.util.GregorianCalendar();
	        int mes=fecha.get(java.util.Calendar.MONTH)+1; 
	        int anyo=fecha.get(java.util.Calendar.YEAR); 
	        if (mes==1) {
	          mes=12;
	          anyo--;
	        } else {
	          mes--;
	        }
	        if (mes<10) 
	          retorno = "" + anyo + "0" + mes;
	        else
	          retorno = "" + anyo + mes;
	        return retorno;
	      
	    }
	  
	  
	public List<?> getListaresultante() {
		return listaresultante;
	}


	public List<Pardato> getListaanyos() {
		return listaanyos;
	}


	public EstadisticaGroup getStatmicrosite() {
		return statmicrosite;
	}
	
}
