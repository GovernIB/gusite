package es.caib.gusite.microback.action;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.gusite.microback.utils.job.IndexacionJobUtil;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.SolrPendiente;
import es.caib.gusite.micromodel.SolrPendienteJob;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.SolrPendienteDelegate;
import es.caib.gusite.micropersistence.util.IndexacionUtil;
import es.caib.gusite.plugins.organigrama.UnidadListData;
import es.caib.gusite.utilities.property.GusitePropertiesUtil;

/**
 * Action que redirige al indexarPrincipal<P>
 * 
 * 	Definición Struts:<BR>
 *  action path="/indexarPrincipal"<BR> 
 *  unknown="false" <BR>
 *  forward name="indexarPrincipal" path="/indexarPrincipal.jsp"
 *  
 *  @author - Indra
 */
public class IndexarPrincipalAction extends BaseAction {

	/** Log. **/
	protected static Log log = LogFactory.getLog(IndexarPrincipalAction.class);
	
	/**
	 * Método principal para leer y ejecutar la instrucción de la interfaz.
	 */
	public ActionForward doExecute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
	
		final String elforward="indexarPrincipal";
		
		final List<UnidadListData> listaUA = new ArrayList<UnidadListData>();
				
		
		final List<UnidadListData>  list = (List<UnidadListData>) getUnidadesAdministrativas("ca");
					
    	for (UnidadListData uAdministrativa : list) {
    		if (uAdministrativa.getId()!=null){
    			listaUA.add(uAdministrativa);
    		}
    	}
	        		
		request.setAttribute("listaUA", listaUA);
		
		final String accion = request.getParameter("indexar");
		if (accion != null) {
			if ("todo".equals(accion)) {
							
				if (indexarTodo() == false){
					request.setAttribute("nok", true);
					addMessage(request, "indexa.nok");
				}
				else {			
					request.setAttribute("ok", true);
					addMessage(request, "indexa.ok");
				}					
			}if ("todoSIN".equals(accion)) {
				
				if (indexarTodoSinIndexar() == false){
					request.setAttribute("nok", true);
					addMessage(request, "indexa.nok");
				} else {			
					request.setAttribute("ok", true);
					addMessage(request, "indexa.ok");
				}					
			} else if ("byUA".equals(accion)) {
				
				String idUA = request.getParameter("uaId");
				
				if (indexarByUA(idUA) == false){
					request.setAttribute("nok", true);
					addMessage(request, "indexa.nok");
				}
				else {			
					request.setAttribute("ok", true);
					addMessage(request, "indexa.ok");
				}
			} else  if ("pendientes".equals(accion)) { 
							
				
				if (indexarPendiente() == false){
					request.setAttribute("nok", true);
					addMessage(request, "indexa.nok");
				}
				else {			
					request.setAttribute("ok", true);
					addMessage(request, "indexa.ok");
				}
				
			} else if ("verpendientes".equals(accion)) { 
				
	        	final List<SolrPendiente> pendientes = verListaPendientes();	
	        	request.setAttribute("listado",pendientes);
	        	
			} else if("verinfo".equals(accion)) { 
				
				final List<SolrPendienteJob> listInfo = verListaJobs();	
	        	request.setAttribute("listInfo", listInfo);

			}  else if("verinfoSIN".equals(accion)) { 
				
				final String listInfo = verInfoSIN();	
	        	request.setAttribute("listInfoSIN",listInfo);
	        	
			}  else if("marcarSIN".equals(accion)) { 
				
				marcarMicrositesSIN();
				addMessage(request, "marcarSIN.ok");
				
			} else if ("cerrar".equals(accion)) { 
				try {
					cerrarJobs();
					request.setAttribute("ok", true);
					addMessage(request, "cierrajob.ok");
				} catch (Exception exception) {
					log.error(exception);
					request.setAttribute("nok", true);
					addMessage(request, "cierrajob.nook");
				}
	        	
			}  else if ("limpiar".equals(accion)) { 
				try {
					limpiarJobs();
					request.setAttribute("ok", true);
					addMessage(request, "limpiajob.ok");
				} catch (Exception exception) {
					log.error(exception);
					request.setAttribute("nok", true);
					addMessage(request, "limpiajob.nook");
				}	        	
			}
		}
		
        return mapping.findForward(elforward);
	
	}	
	
	/**
	 * Marca todos los microsites como NO indexados.
	 * @throws DelegateException 
	 * @throws RemoteException 
	 */
	private void marcarMicrositesSIN() throws RemoteException, DelegateException {
		DelegateUtil.getMicrositeDelegate().marcarComoIndexado(null, Microsite.NO_INDEXADO, null);
	}

	/**
	 * Devuelve un resumen básico de los microsites indexados.
	 * @return
	 * @throws DelegateException 
	 * @throws RemoteException 
	 */
	private String verInfoSIN() throws RemoteException, DelegateException { 
		return DelegateUtil.getMicrositeDelegate().getResumenMicrositesIndexados();
	}

	/** 
	 * Limpia los jobs.
	 * @throws DelegateException
	 */
	private void limpiarJobs() throws DelegateException {
		        final int tamanyoMaximo = GusitePropertiesUtil.getDiasMaximoJobs();
		         DelegateUtil.getSolrPendienteDelegate().limpiezaJobs(tamanyoMaximo);
	      
	        
	}
	
	/**
	 * Método para cerrar a la fuerza todos los jobs.
	 * @throws Exception 
	 */
	private void cerrarJobs() throws Exception {
		// DelegateUtil.getSolrPendienteProcesoDelegate().finalizarTodo();
		IndexacionJobUtil.matarJobs(null);
	}

	/**
	 * Obtiene la lista de jobs.
	 * @return
	 */
	private List<SolrPendienteJob> verListaJobs() {
		
		try 
		{
			SolrPendienteDelegate solrPendienteDel = DelegateUtil.getSolrPendienteDelegate();	
			return solrPendienteDel.getListJobs(40);			
		} catch(Exception exception) {
			return null;
		}
	}
	
    private List<SolrPendiente> verListaPendientes() {
		
    	try 
		{
			SolrPendienteDelegate solrPendienteDel = DelegateUtil.getSolrPendienteDelegate();			
			return solrPendienteDel.getPendientesOrdenFC();			
		} catch(Exception exception) {
			return null;
		}
	}

    /**
     * Indexa todos los microsites.
     * @return
     * @throws Exception
     */
    private Boolean indexarTodo() throws Exception {
		 /*
		try
		{									
			IndexacionJobUtil.crearJob(IndexacionUtil.TIPO_TODO, null, null);						
		} catch(Exception e){
			log.error("Error indexando todo", e );
			return false;
		}
	 		     	
		return true;
			*/
    	return true;
	}
    
    

    /**
     * Indexa todos los microsites.
     * @return
     * @throws Exception
     */
    private Boolean indexarTodoSinIndexar() throws Exception {
		 
		try
		{									
			IndexacionJobUtil.crearJob(IndexacionUtil.TIPO_TODO_SIN_INDEXAR, null, null);						
		} catch(Exception e){
			log.error("Error indexando todo", e );
			return false;
		}
	 		     	
		return true;
			
	}
    
    
    
    /**
     * Indexa por ua.
     * @param idUAdministrativa
     * @return
     * @throws Exception
     */
    private Boolean indexarByUA(final String idUAdministrativa) throws Exception {
		 
		try
		{									
			IndexacionJobUtil.crearJob(IndexacionUtil.TIPO_UA, idUAdministrativa, null);						
		
		} catch(Exception e){
			log.error("Error indexando UA " + idUAdministrativa, e );
			return false;
		}
	 		     	
		return true;
			
	}
    
    /**
     * Indexa los que haya pendientes.
     * @return
     * @throws Exception
     */
    private Boolean indexarPendiente() throws Exception {
		 
		try
		{									
			IndexacionJobUtil.crearJob(IndexacionUtil.TIPO_PENDIENTE, null, null);							
		}catch(Exception e){
			log.error("Error indexando pendientes ", e);
			return false;
		}
	 		     	
		return true;
			
	}
    
   /**
    * Obtiene las unidades administrativas.
    * @param lang
    * @return
    * @throws RemoteException
    */
	private Collection<UnidadListData> getUnidadesAdministrativas(final String lang) throws RemoteException {
		
		final SolrPendienteDelegate solrPendienteDel = DelegateUtil.getSolrPendienteDelegate();
		return (Collection<UnidadListData>) solrPendienteDel.getUnidadesAdministrativas("ca");		
	}
	

}
