package es.caib.gusite.microback.action.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;

import es.caib.gusite.microback.Microback;
import es.caib.gusite.microback.actionform.busca.BuscaOrdenaEstadisticaGenActionForm;
import es.caib.gusite.microback.base.bean.Pardato;
import es.caib.gusite.micromodel.EstadisticaGroup;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.EstadisticaGroupDelegate;


/**
 * Clase que es llamada desde la action de mostrar las estadisticas generales de un site (EstadisticasGenAction)
 * y la action EstadisticasEncuestasAction
 * 
 * @author Indra
 *
 */
public class Bdestadisticasgen {

	private List<EstadisticaGroup> listaoriginal; //la lista maneja bean de EstadisticaGroup
	private List<EstadisticaGroup> listaresultante;//la lista maneja bean de EstadisticaGroup
	private List<Pardato> listaanyos;//la lista maneja string
	private EstadisticaGroup statmicrosite;
	private Microsite microsite;
	BuscaOrdenaEstadisticaGenActionForm formulario;
	private Integer publico=null;
	
	  private static class RefAscComparator implements Comparator<Object> {
		    public int compare(Object element1, Object element2) {
		      String lower1 = ((EstadisticaGroup)element1).getReferencia().toLowerCase();
		      String lower2 = ((EstadisticaGroup)element2).getReferencia().toLowerCase();
		      return lower1.compareTo(lower2);
		    }
		  }
		  
	  private static class AccesoAscComparator implements Comparator<Object> {
			    public int compare(Object element1, Object element2) {
			    	Integer lower1 = new Integer(((EstadisticaGroup)element1).getAccesos());
			    	Integer lower2 = new Integer(((EstadisticaGroup)element2).getAccesos());
			      return lower1.compareTo(lower2);
			    }
		  }

	  private static class RefDescComparator implements Comparator<Object> {
			    public int compare(Object element1, Object element2) {
			      String lower1 = ((EstadisticaGroup)element2).getReferencia().toLowerCase();
			      String lower2 = ((EstadisticaGroup)element1).getReferencia().toLowerCase();
			      return lower1.compareTo(lower2);
			    }
			  }
			  
	  private static class AccesoDescComparator implements Comparator<Object> {
				    public int compare(Object element1, Object element2) {
				    	Integer lower1 = new Integer(((EstadisticaGroup)element2).getAccesos());
				    	Integer lower2 = new Integer(((EstadisticaGroup)element1).getAccesos());
				      return lower1.compareTo(lower2);
				    }
		  }

	  public Bdestadisticasgen(HttpServletRequest request,  ActionForm form) {
			  microsite = (Microsite)request.getSession().getAttribute("MVS_microsite");
			  formulario = (BuscaOrdenaEstadisticaGenActionForm) form;
			  if (formulario.getFiltro2()!=null) { 
				  if (formulario.getFiltro2().equals("ALL")) publico = null; //internet intranet
				  if (formulario.getFiltro2().equals("PUB")) publico = new Integer(1);//internet
				  if (formulario.getFiltro2().equals("PRV")) publico = new Integer(2);//intranet
			  }
			  obtenervisitas();
			  montarlistaservicios();
			  ponernombreslista();
			  ordenarlista();  
			  crearlistaanyos();
		  }
	
	  /**
	   * Método que obtiene las visitas de un microsite
	   * 
	   */
	  private void obtenervisitas() {
		  try {
			 
			  	EstadisticaGroupDelegate estagdel = DelegateUtil.getEstadisticaGroupDelegate();
			  	estagdel.init();
			  	estagdel.setWhere("where stat.idmicrosite=" + microsite.getId() + " and stat.referencia='" + Microback.RMICROSITE + "' and " + obtenerfiltro());
			  	if (estagdel.listarEstadisticasbyRefThin(publico).size()==0) {
			  		statmicrosite = new EstadisticaGroup();
			  		statmicrosite.setAccesos(0);
			  	} else {
			  		statmicrosite =  (EstadisticaGroup)estagdel.listarEstadisticasbyRefThin(publico).iterator().next();
			  	}
			  	
		  } catch (Exception e) {
			  listaoriginal = new ArrayList<EstadisticaGroup>();
		  }
	  }
	  
	/**
	 * Método que pone los nombres de los servicios activos del microsite en una lista.
	 * 
	 */
	private void ponernombreslista() {
		
		Iterator<EstadisticaGroup> iter = listaoriginal.iterator();
		
		while (iter.hasNext()) {
			
			EstadisticaGroup statg = (EstadisticaGroup) iter.next();
			
			if (statg.getReferencia().equals(Microback.RAGENDA))
				statg.setNombreservicio((String) Microback.RSERVICIOS.get(Microback.RAGENDA));
			else if (statg.getReferencia().equals(Microback.RBANNER))
				statg.setNombreservicio((String) Microback.RSERVICIOS.get(Microback.RBANNER));
			else if (statg.getReferencia().equals(Microback.RCONTACTO))
				statg.setNombreservicio((String) Microback.RSERVICIOS.get(Microback.RCONTACTO));
			else if (statg.getReferencia().equals(Microback.RCONTENIDO))
				statg.setNombreservicio((String) Microback.RSERVICIOS.get(Microback.RCONTENIDO));
			else if (statg.getReferencia().equals(Microback.RNOTICIA))
				statg.setNombreservicio((String) Microback.RSERVICIOS.get(Microback.RNOTICIA));
			else if (statg.getReferencia().equals(Microback.RFAQ))
				statg.setNombreservicio((String) Microback.RSERVICIOS.get(Microback.RFAQ));			
			else if (statg.getReferencia().equals(Microback.RENCUESTA))
				statg.setNombreservicio((String) Microback.RSERVICIOS.get(Microback.RENCUESTA));
			else if (statg.getReferencia().equals(Microback.RQSSI))
				statg.setNombreservicio((String) Microback.RSERVICIOS.get(Microback.RQSSI));

		}

	}
	  
	  /**
	   * Método que consulta la base de datos y monta la lista de servicios de un microsite
	   * 
	   */
	  private void montarlistaservicios() {
		  try {
			 
			  	EstadisticaGroupDelegate estagdel = DelegateUtil.getEstadisticaGroupDelegate();
			  	estagdel.init();
			  	estagdel.setWhere("where stat.idmicrosite=" + microsite.getId() + " and stat.referencia!='" + Microback.RMICROSITE + "' and " + obtenerfiltro());
			  	estagdel.setPagina(1);
			  	estagdel.setTampagina(Microback.MAX_INTEGER);
			  	listaoriginal = (List<EstadisticaGroup>) estagdel.listarEstadisticasbyRefThin(publico);
			  	
		  } catch (Exception e) {
			  listaoriginal = new ArrayList<EstadisticaGroup>();
		  }
	  }

	  
	  /**
	   * Método que obtiene el filtro por fechas
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
	   * Método que ordena la lista según el parametro ordenación
	   *
	   */
	  private void ordenarlista() {
		    listaresultante = new ArrayList(listaoriginal);
		    Comparator comp = new AccesoDescComparator();
		    if (formulario.getOrdenacion()!= null && formulario.getOrdenacion().length()>0) {
			  	if (formulario.getOrdenacion().equals("Aref")) 
				  	comp = new RefAscComparator();
			  	if (formulario.getOrdenacion().equals("Aaccesos")) 
				  	comp = new AccesoAscComparator();
			  	if (formulario.getOrdenacion().equals("Dref")) 
				  	comp = new RefDescComparator();
			  	if (formulario.getOrdenacion().equals("Daccesos")) 
				  	comp = new AccesoDescComparator();
	        }
		    Collections.sort(listaresultante, comp);
		  	
	  }

	  /**
	   * Método que crea una lista con los años
	   *
	   */
	  private void crearlistaanyos() {
		  listaanyos=new ArrayList();
	        
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
	  
	  
	public List getListaresultante() {
		return listaresultante;
	}


	public List getListaanyos() {
		return listaanyos;
	}


	public EstadisticaGroup getStatmicrosite() {
		return statmicrosite;
	}
	
}
