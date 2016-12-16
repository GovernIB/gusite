package es.caib.gusite.micropersistence.ejb;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import javax.ejb.CreateException;
import javax.ejb.EJBException;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import es.caib.gusite.micromodel.Agenda;
import es.caib.gusite.micromodel.Archivo;
import es.caib.gusite.micromodel.Contenido;
import es.caib.gusite.micromodel.Encuesta;
import es.caib.gusite.micromodel.Faq;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.Noticia;
import es.caib.gusite.micromodel.SolrPendiente;
import es.caib.gusite.micromodel.SolrPendienteJob;
import es.caib.gusite.micromodel.SolrPendienteResultado;
import es.caib.gusite.micromodel.Traduccion;
import es.caib.gusite.micromodel.TraduccionAgenda;
import es.caib.gusite.micromodel.TraduccionNoticia;
import es.caib.gusite.micropersistence.delegate.AgendaDelegate;
import es.caib.gusite.micropersistence.delegate.ArchivoDelegate;
import es.caib.gusite.micropersistence.delegate.ContenidoDelegate;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.EncuestaDelegate;
import es.caib.gusite.micropersistence.delegate.FaqDelegate;
import es.caib.gusite.micropersistence.delegate.MicrositeDelegate;
import es.caib.gusite.micropersistence.delegate.NoticiaDelegate;
import es.caib.gusite.micropersistence.delegate.SolrPendienteJobDelegate;
import es.caib.gusite.micropersistence.util.IndexacionUtil;
import es.caib.gusite.utilities.property.GusitePropertiesUtil;
import es.caib.solr.api.SolrFactory;
import es.caib.solr.api.SolrIndexer;
import es.caib.solr.api.exception.ExcepcionSolrApi;
import es.caib.solr.api.model.types.EnumAplicacionId;
import es.caib.solr.api.model.types.EnumCategoria;



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

    public Boolean indexarPendientes(SolrPendienteJob solrJob) throws DelegateException {
    	
    	SolrIndexer solrIndexer = null;
        try {
        	
        	final String username = GusitePropertiesUtil.getUserSOLR();
            final String password = GusitePropertiesUtil.getPassSOLR();
            final String index = GusitePropertiesUtil.getIndexSOLR();
            final String urlSolr = GusitePropertiesUtil.getUrlSOLR();

            solrIndexer = (SolrIndexer) SolrFactory.getIndexer(urlSolr, index, EnumAplicacionId.GUSITE,  username, password);
    	
         	final List<SolrPendiente> listPendientes = DelegateUtil.getSolrPendienteDelegate().getPendientes();
         	final AgendaDelegate agendadel = DelegateUtil.getAgendaDelegate(); 	                	 
         	final NoticiaDelegate noticiadel = DelegateUtil.getNoticiasDelegate();                 	
         	final ContenidoDelegate contenidodel = DelegateUtil.getContenidoDelegate();                 
        	final MicrositeDelegate micrositedel = DelegateUtil.getMicrositeDelegate();
    		final FaqDelegate faqdel = DelegateUtil.getFaqDelegate(); 
        	final EncuestaDelegate encuestadel = DelegateUtil.getEncuestaDelegate(); 
        	final SolrPendienteJobDelegate solrPendienteJob = DelegateUtil.getSolrPendienteJobDelegate();
    	    
        	StringBuffer info = new StringBuffer();
        	
         	int i = 0;
    		for (SolrPendiente solrPendiente : listPendientes) {
    			i++;
    			SolrPendienteResultado solrPendienteResultado = null;
    			
    			try {
	    			if (solrPendiente.getAccion() == 1) {
		                if (solrPendiente.getTipo().equals(EnumCategoria.GUSITE_AGENDA.toString())){
		                	 if (solrPendiente.getIdArchivo() != null ){
		                		 solrPendienteResultado = agendadel.indexarSolrArchivo(solrIndexer, solrPendiente.getIdElem(), EnumCategoria.GUSITE_AGENDA, solrPendiente.getIdArchivo());
		                		 info.append(" Vamos a enviar una indexación de un archivo de agenda (id:"+solrPendiente.getIdElem() +", idArchivo:"+solrPendiente.getIdArchivo()+")  <br /> ");	
		                	 }
		                	 else {
		                		 solrPendienteResultado = agendadel.indexarSolr(solrIndexer, solrPendiente.getIdElem(),EnumCategoria.GUSITE_AGENDA);
		                		 info.append(" Vamos a enviar una indexación de una agenda (id:"+solrPendiente.getIdElem() +")  <br /> ");
		                	 }
		                }                	 
		                
		                if (solrPendiente.getTipo().equals(EnumCategoria.GUSITE_NOTICIA.toString())){
		                	if (solrPendiente.getIdArchivo() != null ){
		                		solrPendienteResultado = noticiadel.indexarSolrArchivo(solrIndexer, solrPendiente.getIdElem(), EnumCategoria.GUSITE_NOTICIA, solrPendiente.getIdArchivo());
		                		info.append(" Vamos a enviar una indexación de un archivo de noticia (id:"+solrPendiente.getIdElem() +", idArchivo:"+solrPendiente.getIdArchivo()+")  <br /> ");
		                	}
		                	else {
		                		solrPendienteResultado = noticiadel.indexarSolr(solrIndexer, solrPendiente.getIdElem(), EnumCategoria.GUSITE_NOTICIA);
		                		info.append(" Vamos a enviar una indexación de una agenda (id:"+solrPendiente.getIdElem() +")  <br /> ");	
		                	}
		                } 
		                
		                if (solrPendiente.getTipo().equals(EnumCategoria.GUSITE_CONTENIDO.toString())){
		                	if (solrPendiente.getIdArchivo() != null ){
		                		solrPendienteResultado = contenidodel.indexarSolrArchivo(solrIndexer, solrPendiente.getIdElem(), EnumCategoria.GUSITE_CONTENIDO, solrPendiente.getIdArchivo());
		                		info.append(" Vamos a enviar una indexación de un archivo de contenido (id:"+solrPendiente.getIdElem() +", idArchivo:"+solrPendiente.getIdArchivo()+")  <br /> ");
		                	}
		                	else {
		                		solrPendienteResultado = contenidodel.indexarSolr(solrIndexer, solrPendiente.getIdElem(), EnumCategoria.GUSITE_CONTENIDO);
		                		info.append(" Vamos a enviar una indexación de un archivo de contenido (id:"+solrPendiente.getIdElem() +")  <br /> ");
		                	}
		                } 
		                
		                //Microsite tiene que tener archivo para poder indexarlo
		                if (solrPendiente.getTipo().equals(EnumCategoria.GUSITE_MICROSITE.toString())){                	                 	
		                	if (solrPendiente.getIdArchivo() != null ){
		                		solrPendienteResultado = micrositedel.indexarSolrArchivo(solrIndexer, solrPendiente.getIdElem(), EnumCategoria.GUSITE_MICROSITE, solrPendiente.getIdArchivo());
		                		info.append(" Vamos a enviar una indexación de un archivo de microsite (id:"+solrPendiente.getIdElem() +", idArchivo:"+solrPendiente.getIdArchivo()+")  <br /> ");
		                	} else {
		                    	boolean resultado = indexarMicrosite(solrPendiente.getIdElem(),solrJob);
		                    	solrPendienteResultado = new SolrPendienteResultado(resultado);
		                    	info.append(" Vamos a enviar una indexación de un microsite (id:"+solrPendiente.getIdElem() +")  <br /> ");
		                    }
		                } 
		                
		                if (solrPendiente.getTipo().equals(EnumCategoria.GUSITE_FAQ.toString())){
		                	solrPendienteResultado = faqdel.indexarSolr(solrIndexer, solrPendiente.getIdElem(), EnumCategoria.GUSITE_FAQ);
		                	info.append(" Vamos a enviar una indexación de una faq (id:"+solrPendiente.getIdElem() +")  <br /> ");
	        				
		                }
		                
		                if (solrPendiente.getTipo().equals(EnumCategoria.GUSITE_ENCUESTA.toString())){
		                	solrPendienteResultado = encuestadel.indexarSolr(solrIndexer, solrPendiente.getIdElem(), EnumCategoria.GUSITE_ENCUESTA);
		                	info.append(" Vamos a enviar una indexación de una encuesta (id:"+solrPendiente.getIdElem() +")  <br /> ");
	        				
		                } 
		                
	    			} else {
	    				if (solrPendiente.getIdArchivo() == null) {
	    					try {
	    						if (solrPendiente.getTipo().equals(EnumCategoria.GUSITE_MICROSITE.toString())) {
	    							solrIndexer.desindexarRaiz(solrPendiente.getIdElem().toString(), EnumCategoria.GUSITE_MICROSITE);
	    							solrPendienteResultado = new SolrPendienteResultado(true, "Desindexació completa");
	    						}  else {
		    						solrIndexer.desindexar(solrPendiente.getIdElem().toString(), EnumCategoria.fromString(solrPendiente.getTipo()));
		    						solrPendienteResultado = new SolrPendienteResultado(true);
	    						}
	    					} catch (Exception exception) {
	    						solrPendienteResultado = new SolrPendienteResultado(false, exception.getMessage());
	    					}
	    				} else {
	    					try {
	        					solrIndexer.desindexar(solrPendiente.getIdArchivo().toString(), EnumCategoria.GUSITE_ARCHIVO);
	        					solrPendienteResultado = new SolrPendienteResultado(true);
	    					} catch (Exception exception) {
	    						solrPendienteResultado = new SolrPendienteResultado(false, exception.getMessage());
	    					}
	    				}
	    			}
    			
    			} catch (Exception exception ) {
    				log.error("Exception indexando " + solrPendiente, exception);
    				solrPendienteResultado = new SolrPendienteResultado(false,exception.getMessage());
    				info.append(" Exception indexando "+solrPendiente.getIdElem().toString() +")  <br /> ");
    				
    			}
    			
    			if (solrPendienteResultado != null) {
    				if (solrPendienteResultado.isCorrecto()) {
    					solrPendiente.setResultado(1);
    					solrPendiente.setMensajeError(solrPendienteResultado.getMensaje());
    					solrPendienteJob.actualizarSolrPendiente(solrPendiente);
    				} else {
    					
    					long diff = new Date().getTime() - solrPendiente.getFechaCreacion().getTime();
    					final int dias = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    					//Si hace 10 dias o + que se crea se marca como erronea porque no se ha podido endexar
    					if ( dias >= 10) {
    						solrPendiente.setResultado(-1);   
    						info.append(" Exception indexando "+solrPendiente.getIdElem().toString() +") hace 10 días o más  <br /> ");
    					} else {
    						log.error("No se ha podido realizar la operación (dias ejecutandose:"+dias+")con el registro : "+solrPendiente.getId());
    						info.append(" No se ha podido realizar la operación (dias ejecutandose:"+dias+")con el registro : "+solrPendiente.getId() +")  <br /> ");
    					}
    					solrPendiente.setMensajeError(solrPendienteResultado.getMensaje());   
    					solrPendienteJob.actualizarSolrPendiente(solrPendiente);
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
    		
    	   
    	   solrJob.setDescripcion(Hibernate.createClob(info.toString()));
    	   
    	   return true;
    	}        
         catch (Exception he) {
            throw new EJBException(he);
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

    public Boolean indexarMicrosite(Long idMicrosite ,SolrPendienteJob solrPendienteJob) throws DelegateException {
    	
    	Session session = null;
    	SolrIndexer solrIndexer = null;
        try {
        	
        	final String username = GusitePropertiesUtil.getUserSOLR();
            final String password = GusitePropertiesUtil.getPassSOLR();
            final String index = GusitePropertiesUtil.getIndexSOLR();
            final String urlSolr = GusitePropertiesUtil.getUrlSOLR();

        	solrIndexer = (SolrIndexer) SolrFactory.getIndexer(urlSolr, index, EnumAplicacionId.GUSITE,  username, password);
        	//Desindexamos el microsite
        	try {
        		solrIndexer.desindexarRaiz(idMicrosite.toString(), EnumCategoria.GUSITE_MICROSITE);
			} catch (ExcepcionSolrApi e) {
				log.error("Se ha producido un error en desindexar el MICROSITE con id " + idMicrosite);
			}
        	
         	// Verificamos si es indexable
        	MicrositeDelegate micrositedel = DelegateUtil.getMicrositeDelegate();
        	Microsite micro = micrositedel.obtenerMicrosite(idMicrosite);
        	
        	StringBuffer info = new StringBuffer();
        	
        	if (IndexacionUtil.isIndexable(micro)) {
        		  	
	        	//Obtenemos los ARCHIVOS del microsite
	        	ArchivoDelegate archivodel = DelegateUtil.getArchivoDelegate();
	        	List<Archivo> listArchivos = archivodel.obtenerArchivoByMicrositeId(idMicrosite);
	        	for ( Archivo archivo : listArchivos){
	        		if (archivo !=null && archivo.getId()!=null){  
	        			try{//Indexamos las ARCHIVOS del microsite
	        				micrositedel.indexarSolrArchivo(solrIndexer, idMicrosite, EnumCategoria.GUSITE_MICROSITE, archivo.getId());	
	        				info.append(" Vamos a enviar una indexación de un archivo de microsite (id:"+idMicrosite +", idArchivo:"+archivo.getId()+")  <br /> ");
			                
	        			} catch (DelegateException e) {
	        				log.error("Se ha producido un error en desindexar el MICROSITE con id " + idMicrosite);
	        				info.append("Se ha producido un error en indexar el MICROSITE con id " + idMicrosite+"  <br /> ");
			                
	        			}      					
	        			
	        		}
	        	}
	
	        	//Obtenemos las ENCUESTAS del microsite
	        	EncuestaDelegate encuestadel = DelegateUtil.getEncuestaDelegate();        	
	        	List<Encuesta> listEncuestas = encuestadel.obtenerEncuestasByMicrositeId(idMicrosite);
	        	
	        	for ( Encuesta encuesta : listEncuestas){
	        		if (encuesta !=null && encuesta.getId()!=null){        			
	        			//Indexamos las ENCUESTAS del microsite     
	        			try{
	        				encuestadel.indexarSolr(solrIndexer, encuesta.getId(), EnumCategoria.GUSITE_ENCUESTA);
	        				info.append(" Vamos a enviar una indexación de una encuesta del microsite (id:"+idMicrosite +",idEncuesta:"+encuesta.getId() +")  <br /> "); 
	        			}catch (DelegateException e) {
	        				log.error("Se ha producido un error en indexar la ENCUESTA con id " + encuesta.getId());
	        				info.append("Se ha producido un error en indexar la ENCUESTA con id " + encuesta.getId()+"  <br /> ");
	        			}    
	        		}
	        	}    
	
	        	//Obtenemos los FAQ's del microsite
	        	FaqDelegate faqdel = DelegateUtil.getFaqDelegate();        	        	
	        	List<Faq> listFaqs = faqdel.obtenerFaqsByMicrositeId(idMicrosite);
	        	
	        	for ( Faq faq : listFaqs){
	        		if (faq !=null && faq.getId()!=null){        			
	        			//Indexamos las FAQ'S del microsite    
	        			try{
	        				faqdel.indexarSolr(solrIndexer, faq.getId(), EnumCategoria.GUSITE_FAQ);
	        				info.append(" Vamos a enviar una indexación de una faq del microsite (id:"+idMicrosite +",idFaq:"+faq.getId() +")  <br /> ");
	        			}catch (DelegateException e) {
	        				log.error("Se ha producido un error en indexar el FAQ con id " + faq.getId());
	        				info.append("Se ha producido un error en indexar el FAQ con id " + faq.getId() +"  <br /> ");
	        			}   
	        			
	        		}
	        	}  
	        	        	        
	        	
	        	//Obtenemos las NOTICIAS del microsite        	
	            NoticiaDelegate noticiadel = DelegateUtil.getNoticiasDelegate();        	        	      
	        	List<Noticia> listNoticias = noticiadel.obtenerNoticiasByMicrositeId(idMicrosite);
	        	        	
	        	for ( Noticia noticia : listNoticias){
	        		//Indexamos las NOTICIAS del microsite
	        		try{        			        			
	        			noticiadel.indexarSolr(solrIndexer, noticia.getId(), EnumCategoria.GUSITE_NOTICIA);
	        			info.append(" Vamos a enviar una indexación de una noticia del microsite (id:"+idMicrosite +",idNoticia:"+noticia.getId() +")  <br /> ");
	        				
	        	    }catch (DelegateException e) {
						log.error("Se ha producido un error en noticia con id " +  noticia.getId());
						info.append("Se ha producido un error en indexar la noticia con id " + noticia.getId()+"  <br /> ");
					}
	        		
	        		if (noticia.getTraducciones() != null){  
	        			Iterator<Entry<String, Traduccion>> itTradNoticia = noticia.getTraduccionMap().entrySet().iterator();
	        			
	        			while (itTradNoticia.hasNext()) {
	            			Map.Entry mapTrad = (Map.Entry)itTradNoticia.next();
	            			TraduccionNoticia tradNoticia=(TraduccionNoticia) mapTrad.getValue();        			
	            			Archivo arc = tradNoticia != null && tradNoticia.getDocu() != null ? tradNoticia.getDocu() : null;
	                		if (arc != null){
	                			try{
	                				//Indexamos las NOTICIAS con archivo 
	                				noticiadel.indexarSolrArchivo(solrIndexer,  noticia.getId(), EnumCategoria.GUSITE_NOTICIA,tradNoticia.getDocu().getId());
	                				info.append(" Vamos a enviar una indexación de un archivo de noticia del microsite (id:"+idMicrosite +",idNoticia:"+noticia.getId()+",idArchivo:"+tradNoticia.getDocu().getId() +")  <br /> ");
	                			} catch (Exception e) {
	            					log.error("Se ha producido un error en documento noticia con id " + arc.getId());
	            					info.append(" Se ha producido un error en documento noticia con id " + arc.getId() +"  <br /> ");
		                			
	            				}
	                		}
	                		
	            		}
	        		}
	        	}  
	        	
	        	//Obtenemos las AGENDAS del microsite
	            AgendaDelegate agendadel = DelegateUtil.getAgendaDelegate();        	        	
	        	List<Agenda> listAgendas = agendadel.obtenerAgendasByMicrositeId(idMicrosite);
	        	
	        	
	        	for ( Agenda agenda : listAgendas){
	        		try{
	        			agendadel.indexarSolr(solrIndexer, agenda.getId(), EnumCategoria.GUSITE_AGENDA);	
	        			info.append(" Vamos a enviar una indexación de una agenda del microsite (id:"+idMicrosite +",idAgenda:"+agenda.getId() +")  <br /> ");
		                
	        	    }catch (DelegateException e) {
						log.error("Se ha producido un error en agenda con id " +  agenda.getId());
						info.append(" Se ha producido un error en agenda con id " +  agenda.getId() +"  <br /> ");
		                
					}
	        		
	        		if (agenda.getTraducciones() != null){  
	        			Iterator<Entry<String, Traduccion>> itTradAgenda = agenda.getTraduccionMap().entrySet().iterator();
	        			
	        			while (itTradAgenda.hasNext()) {
	            			Map.Entry mapTrad = (Map.Entry)itTradAgenda.next();
	            			TraduccionAgenda tradAgen=(TraduccionAgenda) mapTrad.getValue();        			
	            			Archivo arc = tradAgen != null && tradAgen.getDocumento() != null ? tradAgen.getDocumento() : null;
	                		if (arc != null){
	                			try{
	                				//Indexamos las AGENDAS con archivo 
	                				agendadel.indexarSolrArchivo(solrIndexer,  agenda.getId(), EnumCategoria.GUSITE_AGENDA,tradAgen.getDocumento().getId());
	                				info.append(" Vamos a enviar una indexación de un archivo de agenda del microsite (id:"+idMicrosite +",idAgenda:"+agenda.getId()+",idArchivo:"+tradAgen.getDocumento().getId() +")  <br /> ");
	    			                
	                			} catch (DelegateException e) {
	            					log.error("Se ha producido un error en documento agenda con id " + arc.getId());
	            					info.append(" Vamos a enviar una indexación de un archivo de agenda del microsite (id:"+idMicrosite +",idAgenda:"+agenda.getId()+",idArchivo:"+tradAgen.getDocumento().getId() +")  <br /> "); 
	            				}
	                		}
	                		
	            		}
	        		}
	        	}  
	        	
	        	//Obtenemos los CONTENIDOS del microsite
	            ContenidoDelegate contenidodel = DelegateUtil.getContenidoDelegate();                	        
	        	List<?> listContenidos = contenidodel.listarAllContenidos(idMicrosite.toString());
	        	        	
	        	for (Object contenido : listContenidos) {
	        		        	
					if (((Contenido) contenido).getId() != null){
						try{
							contenidodel.indexarSolr(solrIndexer, ((Contenido) contenido).getId(), EnumCategoria.GUSITE_CONTENIDO);	
							info.append(" Vamos a enviar una indexación de un contenido del microsite (id:"+idMicrosite +",idContenido:"+ ((Contenido) contenido).getId()  +")  <br /> ");
			                
		        	    }catch (DelegateException e) {
							log.error("Se ha producido un error en contenido con id " +  ((Contenido) contenido).getId());
							info.append(" Se ha producido un error en contenido con id " +  ((Contenido) contenido).getId()+"  <br /> "); 
						}
						//Obtenemos los DOCUMENTOS de cada CONTENIDO
						List<?> listaDocu = contenidodel.listarDocumentos(idMicrosite.toString(), ((Contenido) contenido).getId().toString());
						if ( listaDocu != null){
							for ( Object docu : listaDocu ){
								if ( docu != null &&  ((Archivo) docu).getId()!=null){
									try{
		                				//Indexamos las CONTENIDOS con archivo 
		                				contenidodel.indexarSolrArchivo(solrIndexer,  ((Contenido) contenido).getId(), EnumCategoria.GUSITE_CONTENIDO,((Archivo) docu).getId());
		                				info.append(" Vamos a enviar una indexación de un archivo de contenido del microsite (id:"+idMicrosite +",idContenido:"+((Contenido) contenido).getId()+",idArchivo:"+ ((Archivo) docu).getId() +")  <br /> "); 
		                			} catch (DelegateException e) {
		            					log.error("Se ha producido un error en documento contenido con id " + ((Archivo) docu).getId());
		            					info.append(" Se ha producido un error en documento contenido con id " +  ((Archivo) docu).getId()+"  <br /> ");
		            				}
									
								}						
						
	        		        }	
				        }   
					}
	        	}
	        	
	    solrPendienteJob.setDescripcion(Hibernate.createClob(info.toString()));
        
        }
				
    	//Comiteamos los cambios.
    	try {
			solrIndexer.commit();
			log.debug("Se ha indexado correctamente el microsite " + idMicrosite);			
	    } catch (ExcepcionSolrApi e) {
			log.error("No se ha podido comitear la indexación" + e.getMessage());
		}
        return true;     
    	    	    		
    	}        
         catch (Exception he) {
            throw new EJBException(he);
        } finally {
            close(session);
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

    public Boolean indexarTodo(SolrPendienteJob solrPendienteJob) throws DelegateException {
    	
    	Session session = null;
    	try {
        	
    		StringBuffer info = new StringBuffer();
            MicrositeDelegate micrositedel = DelegateUtil.getMicrositeDelegate();
            List<?> listaMicro = micrositedel.listarMicrosites();
            
            for ( Object micro : listaMicro){
            	if (((Microsite) micro).getId() != null){
            		try{
            			indexarMicrosite(((Microsite) micro).getId(),solrPendienteJob);	
            			info.append(" Vamos a enviar a indexación el microsite (id:"+((Microsite) micro).getId() +")  <br /> ");
            		}        
                    catch (HibernateException he) {                       
                       log.error("No se ha indexado el microsite con id " + ((Microsite) micro).getId());
                       info.append(" No se ha indexado el microsite con id " + ((Microsite) micro).getId()+"  <br /> ");
                    }
            	}
            	
            }
            
            solrPendienteJob.setDescripcion(Hibernate.createClob(info.toString()));
            return true;
            
        }        
        catch (Exception he) {
           throw new EJBException(he);
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

    public Boolean indexarMicrositeByUA(String idUAdministrativa, SolrPendienteJob solrPendienteJob) throws DelegateException, RemoteException {
    	
    	Session session = null;
    	try {
        	
    		StringBuffer info = new StringBuffer();
            MicrositeDelegate micrositedel = DelegateUtil.getMicrositeDelegate();
          
            List<?> listaMicro = micrositedel.obtenerMicrositesbyUA(idUAdministrativa);
            
            for ( Object micro : listaMicro){
            	if (((Microsite) micro).getId() != null){
            		try{
            			indexarMicrosite(((Microsite) micro).getId(),solrPendienteJob);	
            			info.append(" Vamos a enviar a indexación el microsite (id:"+((Microsite) micro).getId()+", idUA: "+idUAdministrativa +")  <br /> ");
            		}        
                    catch (HibernateException he) {
                    	log.error("No se ha indexado el microsite con id " + ((Microsite) micro).getId());   
                    	info.append("No se ha indexado el microsite con id " + ((Microsite) micro).getId());
                    }
            	}
            	
            }  
            
            solrPendienteJob.setDescripcion(Hibernate.createClob(info.toString()));
            return true;
            
        }        
        catch (Exception he) {
           throw new EJBException(he);
       } finally {
           close(session);
       }
	  
    
    }
    
    

}
