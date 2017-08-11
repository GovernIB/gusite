package es.caib.gusite.micropersistence.ejb;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.ejb.CreateException;

import org.hibernate.Session;

import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.SolrPendiente;
import es.caib.gusite.micromodel.SolrPendienteJob;
import es.caib.gusite.micromodel.SolrPendienteResultado;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.MicrositeDelegate;
import es.caib.gusite.plugins.PluginFactory;
import es.caib.gusite.plugins.organigrama.OrganigramaProvider;
import es.caib.gusite.plugins.organigrama.UnidadListData;
import es.caib.gusite.utilities.clob.GusiteClobUtil;
import es.caib.gusite.utilities.job.GusiteJobUtil;
import es.caib.gusite.utilities.property.GusitePropertiesUtil;
import es.caib.solr.api.SolrFactory;
import es.caib.solr.api.SolrIndexer;
import es.caib.solr.api.exception.ExcepcionSolrApi;
import es.caib.solr.api.model.types.EnumAplicacionId;



/**
 * SessionBean para manipular datos de las respuestas.
 * 
 * @ejb.bean name="sac/micropersistence/SolrPendienteProcesoFacade"
 *           jndi-name="es.caib.gusite.micropersistence.SolrPendienteProcesoFacade"
 *           type="Stateless" view-type="remote" transaction-type="Container"
 * 
 *  @ejb.transaction type="NotSupported"
 */
public abstract class SolrPendienteProcesoFacadeEJB extends HibernateEJB {

	private static final long serialVersionUID = 6661917618076931960L;
	
	/**
	 * @ejb.create-method
	 * @ejb.permission unchecked="true"
	 */
	@Override
	public void ejbCreate() throws CreateException {
		super.ejbCreate();
	}

    /**
     * Indexa Pendientes
     * 
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     * 
     * @return Booleano indicando si se indexan todos los procesos pendientes .
     * @throws Exception 
   	 */

    public Boolean indexarPendientes(SolrPendienteJob solrJob) throws Exception {
    	GusiteJobUtil.interrumpirTarea = false;
    	SolrIndexer solrIndexer = null;
        try {
        	
        	final String username = GusitePropertiesUtil.getUserSOLR();
            final String password = GusitePropertiesUtil.getPassSOLR();
            final String index = GusitePropertiesUtil.getIndexSOLR();
            final String urlSolr = GusitePropertiesUtil.getUrlSOLR();
            final int diasMaximos = GusitePropertiesUtil.getDiasMaxSOLR();

            solrIndexer = (SolrIndexer) SolrFactory.getIndexer(urlSolr, index, EnumAplicacionId.GUSITE,  username, password);
    	
         	final List<SolrPendiente> listPendientes = DelegateUtil.getSolrPendienteDelegate().getPendientes();
         	
        	StringBuffer info = new StringBuffer();
        	
         	int i = 0;
    		for (SolrPendiente solrPendiente : listPendientes) {
    			i++;
    			SolrPendienteResultado solrPendienteResultado = null;
    			
    			if (GusiteJobUtil.interrumpirTarea) { //El semaforo para salir de esto.
    				break;
    			}
    			try 
    			{
    				
    				solrPendienteResultado = DelegateUtil.getSolrPendienteDelegate().indexarPendiente(solrPendiente, info);
    			
    			} catch (Exception exception ) {
    				log.error("Exception indexando " + solrPendiente, exception);
    				solrPendienteResultado = new SolrPendienteResultado(false,exception.getMessage());
    				info.append(" Exception indexando "+solrPendiente.getIdElem().toString() +")  <br /> ");
    				
    			}
    			
    			if (solrPendienteResultado != null) {
    				if (solrPendienteResultado.isCorrecto()) {
    					solrPendiente.setResultado(1);
    					solrPendiente.setMensajeError(solrPendienteResultado.getMensaje());
    					DelegateUtil.getSolrPendienteDelegate().actualizarSolrPendiente(solrPendiente);
    				} else {
    					
    					long diff = new Date().getTime() - solrPendiente.getFechaCreacion().getTime();
    					final int dias = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    					
    					//Si hace 10 dias o + que se crea se marca como erronea porque no se ha podido endexar
    					if ( dias >= diasMaximos) {
    						solrPendiente.setResultado(-1);   
    						info.append(" Exception indexando "+solrPendiente.getIdElem().toString() +") hace "+diasMaximos+" días o más  <br /> ");
    					} else {
    						log.error("No se ha podido realizar la operación (dias ejecutandose:"+dias+")con el registro : "+solrPendiente.getId());
    						info.append(" No se ha podido realizar la operación (dias ejecutandose:"+dias+")con el registro : "+solrPendiente.getId() +")  <br /> ");
    					}
    					solrPendiente.setMensajeError(solrPendienteResultado.getMensaje());   
    					DelegateUtil.getSolrPendienteDelegate().actualizarSolrPendiente(solrPendiente);
    				}
    				
    			}
    			
    			if (i % 20 == 0) {
    	    		try {
    	    			solrIndexer.commit();
	        	   } catch (ExcepcionSolrApi e) {
	        		   log.error("No se ha podido comitear la indexación" + e.getMessage());
	        		   info.append(" No se ha podido comitear la indexación" + e.getMessage()+" <br /> ");
	        	   }        
    		}
           }
    		
    		try {
    			solrIndexer.commit();
    	   } catch (ExcepcionSolrApi e) {
    		   log.error("No se ha podido comitear la indexación" + e.getMessage());
    		   info.append(" No se ha podido comitear la indexación" + e.getMessage()+" <br /> ");
    	   }
    		
    	   
    	   solrJob.setDescripcion(GusiteClobUtil.getClob(info.toString()));
    	   
    	   return true;
    	}        
         catch (Exception exception) {
        	log.error("Error indexando pendientes.", exception);
            throw new Exception(exception);
        } 
    }
    
	/**
     * Indexa Microsite
     * 
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     * 
     * @return Booleano indicando si se ha indexado el microsite .
     * @throws Exception 
   	 */
    public SolrPendienteResultado indexarMicrosite(Long idMicrosite ,SolrPendienteJob solrPendienteJob, StringBuffer info) throws Exception {
    	try {
    		return DelegateUtil.getSolrPendienteDelegate().indexarMicrosite(idMicrosite, solrPendienteJob, info);
    	}        
        catch (Exception exception) {
        	log.error("Error indexando microsite.", exception);
           throw new Exception(exception);
       }
    }
    
    /**
     * Indexa Todos los microsites.
     * 
     * @ejb.interface-method
     * @ejb.permission unchecked="true"      
     * @return Booleano indicando si se ha indexado todo .
     * @throws Exception 
   	 */

    public Boolean indexarTodo(SolrPendienteJob solrPendienteJob) throws Exception {
    	GusiteJobUtil.interrumpirTarea = false;
    	Session session = null;
    	try 
    	{
        	final StringBuffer info = new StringBuffer();
        	final MicrositeDelegate micrositedel = DelegateUtil.getMicrositeDelegate();
        	final List<Long> listaMicro = micrositedel.listarMicrosites();
            
        	int correctos = 0, incorrectos = 0;
        	for (int i = 0; i < listaMicro.size(); i++) {
        		final Long idMicro = listaMicro.get(i);
        		if (idMicro != null){
        			SolrPendienteResultado resultado = null;
            		try{
            			if (GusiteJobUtil.interrumpirTarea) { //El semaforo para salir de esto.
            				info.insert(0, "Finalizado a la fuerza!.<br />");
            				break;
            			}
            			resultado = DelegateUtil.getSolrPendienteDelegate().indexarMicrosite(idMicro,solrPendienteJob, info);	
            		} catch (Exception he) {                       
                       log.error("No se ha indexado el microsite con id " + idMicro);
                       info.append(" No se ha indexado el microsite con id " + idMicro+"  <br /> ");
                    }
            		
            		solrPendienteJob.setDescripcion(GusiteClobUtil.getClob("La indexació està activa. Porta indexats "+(i+1)+" microsites ("+(100*(i+1)/listaMicro.size())+"%). <br /> "+info.toString()));
    	        	DelegateUtil.getSolrPendienteDelegate().actualizarSorlPendienteJob(solrPendienteJob);
    	        	
    	        	if (resultado != null && resultado.isIndexable()) {
    	        		if (resultado.isCorrecto()) {
    	        			correctos ++;
    	        		} else {
    	        			incorrectos ++;
    	        		}
    	        	}
            	}
            	
            }
            
        	String infoResumen = "La indexació ha finalitzat. Dels "+listaMicro.size()+" microsites en total, eren indexables "+(correctos + incorrectos)+" dels quals "+correctos+" han estat correctes i "+incorrectos+" estat incorrectes. <br />";
            solrPendienteJob.setDescripcion(GusiteClobUtil.getClob(infoResumen+info.toString()));
            return true;
        }        
        catch (Exception exception) {
           log.error("Error indexando todo" , exception);
           throw new Exception(exception);
        } finally {
           close(session);
        }
    }
    
    /**
     * Indexa Todos los microsites que tengan una unidad administrativa
     * 
     * @ejb.interface-method
     * @ejb.permission unchecked="true"     
     * @return Booleano indicando si se ha indexado todo los de la condicion anterior .
     * @throws RemoteException 
     * @throws Exception 
   	 */

    public Boolean indexarMicrositeByUA(String idUAdministrativa, SolrPendienteJob solrPendienteJob) throws Exception {
    	GusiteJobUtil.interrumpirTarea = false;
    	Session session = null;
    	try {
        	
    		StringBuffer info = new StringBuffer();
            MicrositeDelegate micrositedel = DelegateUtil.getMicrositeDelegate();
          
            OrganigramaProvider op = PluginFactory.getInstance().getOrganigramaProvider();
            Collection<UnidadListData> unidades = op.getUnidadesHijas(idUAdministrativa, "ca");
            List<Integer> listaIds = new ArrayList<Integer>();
            listaIds.add(Integer.parseInt((idUAdministrativa)));
            for (UnidadListData unidadListData : unidades) {
            	Long id= (Long) unidadListData.getId();
            	listaIds.add(Integer.valueOf(id.intValue() ));
			}
            
            List<Long> listaMicro = micrositedel.obtenerMicrositesbyUA(listaIds);
            
            for ( Long idMicro : listaMicro){
            	if (GusiteJobUtil.interrumpirTarea) { //El semaforo para salir de esto.
            		info.insert(0, "Finalizado a la fuerza!.<br />");
    				break;
    			}
            	Microsite micro = micrositedel.obtenerMicrosite(idMicro);
            	if (micro.getId() != null){
            		try{
            			info.append(" Vamos a enviar a indexación el microsite (id:"+((Microsite) micro).getId()+", idUA: "+idUAdministrativa +")  <br /> ");
            			DelegateUtil.getSolrPendienteDelegate().indexarMicrosite(((Microsite) micro).getId(),solrPendienteJob, info);            			
            		}        
                    catch (Exception he) {
                    	log.error("No se ha indexado el microsite con id " + ((Microsite) micro).getId());   
                    	info.append("No se ha indexado el microsite con id " + ((Microsite) micro).getId());
                    }
            	}
            	
            }  
            
            solrPendienteJob.setDescripcion(GusiteClobUtil.getClob(info.toString()));
            return true;
            
        }        
        catch (Exception exception) {
        	log.error("Error indexando microsite por ua", exception);
           throw new Exception(exception);
       } finally {
           close(session);
       }
	  
    
    }
    
    
     
}
