package es.caib.gusite.micropersistence.ejb;

import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ejb.CreateException;
import javax.ejb.EJBException;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

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
import es.caib.gusite.plugins.PluginException;
import es.caib.gusite.plugins.PluginFactory;
import es.caib.gusite.plugins.organigrama.OrganigramaProvider;
import es.caib.gusite.plugins.organigrama.UnidadListData;
import es.caib.gusite.utilities.property.GusitePropertiesUtil;
import es.caib.solr.api.SolrFactory;
import es.caib.solr.api.SolrIndexer;
import es.caib.solr.api.exception.ExcepcionSolrApi;
import es.caib.solr.api.model.types.EnumAplicacionId;
import es.caib.solr.api.model.types.EnumCategoria;



/**
 * SessionBean para manipular datos de las respuestas.
 * 
 * @ejb.bean name="sac/micropersistence/SolrPendienteFacade"
 *           jndi-name="es.caib.gusite.micropersistence.SolrPendienteFacade"
 *           type="Stateless" view-type="remote" transaction-type="Container"
 * 
 * @ejb.transaction type="RequiresNew"
 */
public abstract class SolrPendienteFacadeEJB extends HibernateEJB {

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
     * Lista todos los SolrPendientes
     * 
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     * @ejb.transaction type="RequiresNew"
     *
     * @return Devuelve un listado de todos los SolrPendientes.
     */
    public List<SolrPendiente> getPendientes() {
    	final Session session = getSession();
        try {
            final Criteria criteri = session.createCriteria(SolrPendiente.class);
            criteri.add(Restrictions.eq("resultado", 0));            
            return  ( List<SolrPendiente>) criteri.list();
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }
    
    /**
     * Indexa Pendientes
     * 
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     * @ejb.transaction type="RequiresNew"
     * 
     * @return Booleano indicando si se indexan todos los procesos pendientes .
     * @throws Exception 
   	 */

    public Boolean indexarPendientes() throws DelegateException {
    	
    	SolrIndexer solrIndexer = null;
        try {
        	
        	final String username = GusitePropertiesUtil.getUserSOLR();
            final String password = GusitePropertiesUtil.getPassSOLR();
            final String index = GusitePropertiesUtil.getIndexSOLR();
            final String urlSolr = GusitePropertiesUtil.getUrlSOLR();

            solrIndexer = (SolrIndexer) SolrFactory.getIndexer(urlSolr, index, EnumAplicacionId.GUSITE,  username, password);
    	
         	final List<SolrPendiente> listPendientes = getPendientes();
         	final AgendaDelegate agendadel = DelegateUtil.getAgendaDelegate(); 	                	 
         	final NoticiaDelegate noticiadel = DelegateUtil.getNoticiasDelegate();                 	
         	final ContenidoDelegate contenidodel = DelegateUtil.getContenidoDelegate();                 
        	final MicrositeDelegate micrositedel = DelegateUtil.getMicrositeDelegate();
    		final FaqDelegate faqdel = DelegateUtil.getFaqDelegate(); 
        	final EncuestaDelegate encuestadel = DelegateUtil.getEncuestaDelegate(); 
        	final SolrPendienteJobDelegate solrPendienteJob = DelegateUtil.getSolrPendienteJobDelegate();
    	    		
         	int i = 0;
    		for (SolrPendiente solrPendiente : listPendientes) {
    			i++;
    			SolrPendienteResultado solrPendienteResultado = null;
    			
    			try {
	    			if (solrPendiente.getAccion() == 1) {
		                if (solrPendiente.getTipo().equals(EnumCategoria.GUSITE_AGENDA.toString())){
		                	 if (solrPendiente.getIdArchivo() != null ){
		                		 solrPendienteResultado = agendadel.indexarSolrArchivo(solrIndexer, solrPendiente.getIdElem(), EnumCategoria.GUSITE_AGENDA, solrPendiente.getIdArchivo());
		                	 }
		                	 else {
		                		 solrPendienteResultado = agendadel.indexarSolr(solrIndexer, solrPendiente.getIdElem(),EnumCategoria.GUSITE_AGENDA);
		                	 }
		                }                	 
		                
		                if (solrPendiente.getTipo().equals(EnumCategoria.GUSITE_NOTICIA.toString())){
		                	if (solrPendiente.getIdArchivo() != null ){
		                		solrPendienteResultado = noticiadel.indexarSolrArchivo(solrIndexer, solrPendiente.getIdElem(), EnumCategoria.GUSITE_NOTICIA, solrPendiente.getIdArchivo());
		                    }
		                	else {
		                		solrPendienteResultado = noticiadel.indexarSolr(solrIndexer, solrPendiente.getIdElem(), EnumCategoria.GUSITE_NOTICIA);
		                	}
		                } 
		                
		                if (solrPendiente.getTipo().equals(EnumCategoria.GUSITE_CONTENIDO.toString())){
		                	if (solrPendiente.getIdArchivo() != null ){
		                		solrPendienteResultado = contenidodel.indexarSolrArchivo(solrIndexer, solrPendiente.getIdElem(), EnumCategoria.GUSITE_CONTENIDO, solrPendiente.getIdArchivo());
		                    }
		                	else {
		                		solrPendienteResultado = contenidodel.indexarSolr(solrIndexer, solrPendiente.getIdElem(), EnumCategoria.GUSITE_CONTENIDO);
		                	}
		                } 
		                
		                //Microsite tiene que tener archivo para poder indexarlo
		                if (solrPendiente.getTipo().equals(EnumCategoria.GUSITE_MICROSITE.toString())){                	                 	
		                	if (solrPendiente.getIdArchivo() != null ){
		                		solrPendienteResultado = micrositedel.indexarSolrArchivo(solrIndexer, solrPendiente.getIdElem(), EnumCategoria.GUSITE_MICROSITE, solrPendiente.getIdArchivo());
		                    }
		                } 
		                
		                if (solrPendiente.getTipo().equals(EnumCategoria.GUSITE_FAQ.toString())){
		                	solrPendienteResultado = faqdel.indexarSolr(solrIndexer, solrPendiente.getIdElem(), EnumCategoria.GUSITE_FAQ);
		                	
		                }
		                
		                if (solrPendiente.getTipo().equals(EnumCategoria.GUSITE_ENCUESTA.toString())){
		                	solrPendienteResultado = encuestadel.indexarSolr(solrIndexer, solrPendiente.getIdElem(), EnumCategoria.GUSITE_ENCUESTA);
		                } 
		                
	    			} else {
	    				if (solrPendiente.getIdArchivo() == null) {
	    					try {
	    						solrIndexer.desindexar(solrPendiente.getIdElem().toString(), EnumCategoria.fromString(solrPendiente.getTipo()));
	    						solrPendienteResultado = new SolrPendienteResultado(true);
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
    			}
    			
    			if (solrPendienteResultado != null) {
    				if (solrPendienteResultado.isCorrecto()) {
    					solrPendiente.setResultado(1);
    					solrPendiente.setMensajeError(solrPendienteResultado.getMensaje());
    					solrPendienteJob.actualizarSolrPendiente(solrPendiente);
    				} else {
    					final Calendar fechaCalendar  = Calendar.getInstance();
    					fechaCalendar.setTime(solrPendiente.getFechaCreacion());
    					final Calendar hoyCalendar  = Calendar.getInstance();
    					hoyCalendar.setTime(new Date());
    					
    					final int dias = hoyCalendar.get(Calendar.DATE) - fechaCalendar.get(Calendar.DATE);
    					//Si hace 10 dias o + que se crea se marca como erronea porque no se ha podido endexar
    					if ( dias >= 10){
    						solrPendiente.setResultado(-1);
    						solrPendiente.setMensajeError(solrPendienteResultado.getMensaje());
    						solrPendienteJob.actualizarSolrPendiente(solrPendiente);
    					}else{
    						log.error("No se ha podido realizar la operación (dias ejecutandose:"+dias+")con el registro : "+solrPendiente.getId());
    					}
    				}
    			}
                
    			if (i % 20 == 0) {
    	    		try {
    	    			solrIndexer.commit();
	        	   } catch (ExcepcionSolrApi e) {
	    			log.error("No se ha podido comitear la indexación" + e.getMessage());
	        	   }        
    		}
           }
    		
    		try {
    			solrIndexer.commit();
    	   } catch (ExcepcionSolrApi e) {
			log.error("No se ha podido comitear la indexación" + e.getMessage());
    	   }
    		
    	   
    	   return true;
    	}        
         catch (HibernateException he) {
            throw new EJBException(he);
        } 
    }
    
    /**
     * Indexa Microsite
     * 
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     * @ejb.transaction type="RequiresNew"
     * 
     * @return Booleano indicando si se ha indexado el microsite .
     * @throws Exception 
   	 */

    public Boolean indexarMicrosite(Long idMicrosite ) throws DelegateException {
    	
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
				solrIndexer.desindexarMicrosite(idMicrosite);
			} catch (ExcepcionSolrApi e) {
				log.error("Se ha producido un error en desindexar el MICROSITE con id " + idMicrosite);
			}
        	
         	
              	
        	//Obtenemos los ARCHIVOS del microsite
        	MicrositeDelegate micrositedel = DelegateUtil.getMicrositeDelegate();
        	ArchivoDelegate archivodel = DelegateUtil.getArchivoDelegate();
        	List<Archivo> listArchivos = archivodel.obtenerArchivoByMicrositeId(idMicrosite);
        	for ( Archivo archivo : listArchivos){
        		if (archivo !=null && archivo.getId()!=null){  
        			try{//Indexamos las ARCHIVOS del microsite
        				micrositedel.indexarSolrArchivo(solrIndexer, idMicrosite, EnumCategoria.GUSITE_MICROSITE, archivo.getId());	
        			} catch (DelegateException e) {
        				log.error("Se ha producido un error en desindexar el MICROSITE con id " + idMicrosite);
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
        			}catch (DelegateException e) {
        				log.error("Se ha producido un error en indexar la ENCUESTA con id " + idMicrosite);
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
        			}catch (DelegateException e) {
        				log.error("Se ha producido un error en indexar el FAQ con id " + idMicrosite);
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
        				
        	    }catch (DelegateException e) {
					log.error("Se ha producido un error en noticia con id " +  noticia.getId());
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
                				
                			} catch (Exception e) {
            					log.error("Se ha producido un error en documento noticia con id " + arc.getId());
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
        	    }catch (DelegateException e) {
					log.error("Se ha producido un error en agenda con id " +  agenda.getId());
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
                				
                			} catch (DelegateException e) {
            					log.error("Se ha producido un error en documento agenda con id " + arc.getId());
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
	        	    }catch (DelegateException e) {
						log.error("Se ha producido un error en contenido con id " +  ((Contenido) contenido).getId());
					}
					//Obtenemos los DOCUMENTOS de cada CONTENIDO
					List<?> listaDocu = contenidodel.listarDocumentos(idMicrosite.toString(), ((Contenido) contenido).getId().toString());
					if ( listaDocu != null){
						for ( Object docu : listaDocu ){
							if ( docu != null &&  ((Archivo) docu).getId()!=null){
								try{
	                				//Indexamos las CONTENIDOS con archivo 
	                				contenidodel.indexarSolrArchivo(solrIndexer,  ((Contenido) contenido).getId(), EnumCategoria.GUSITE_CONTENIDO,((Archivo) docu).getId());
	                				
	                			} catch (DelegateException e) {
	            					log.error("Se ha producido un error en documento contenido con id " + ((Archivo) docu).getId());
	            				}
								
							}						
					
        		        }	
			        }   
				}
        	}
				
    	//Comiteamos los cambios.
    	try {
			solrIndexer.commit();
			log.error("Se ha indexado correctamente el microsite " + idMicrosite);			
	    } catch (ExcepcionSolrApi e) {
			log.error("No se ha podido comitear la indexación" + e.getMessage());
		}
        return true;     
    	    	    		
    	}        
         catch (HibernateException he) {
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
     * @ejb.transaction type="RequiresNew"
     *  
     * @return Booleano indicando si se ha indexado todo .
     * @throws Exception 
   	 */

    public Boolean indexarTodo() throws DelegateException {
    	
    	Session session = null;
    	try {
        	        	
            MicrositeDelegate micrositedel = DelegateUtil.getMicrositeDelegate();
            List<?> listaMicro = micrositedel.listarMicrosites();
            
            for ( Object micro : listaMicro){
            	if (((Microsite) micro).getId() != null){
            		try{
            			indexarMicrosite(((Microsite) micro).getId());	
            		}        
                    catch (HibernateException he) {                       
                       log.error("No se ha indexado el microsite con id " + ((Microsite) micro).getId());
                       
                    }
            	}
            	
            }
            return true;
            
        }        
        catch (HibernateException he) {
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
     * @ejb.transaction type="RequiresNew"
     * 
     * @return Booleano indicando si se ha indexado todo los de la condicion anterior .
     * @throws RemoteException 
     * @throws Exception 
   	 */

    public Boolean indexarMicrositeByUA(String idUAdministrativa) throws DelegateException, RemoteException {
    	
    	Session session = null;
    	try {
        	        	
            MicrositeDelegate micrositedel = DelegateUtil.getMicrositeDelegate();
          
            List<?> listaMicro = micrositedel.obtenerMicrositesbyUA(idUAdministrativa);
            
            for ( Object micro : listaMicro){
            	if (((Microsite) micro).getId() != null){
            		try{
            			indexarMicrosite(((Microsite) micro).getId());	
            		}        
                    catch (HibernateException he) {
                    	log.error("No se ha indexado el microsite con id " + ((Microsite) micro).getId());                   
                    }
            	}
            	
            }  
            
            return true;
            
        }        
        catch (HibernateException he) {
           throw new EJBException(he);
       } finally {
           close(session);
       }
	  
    
    }
    
    
    /**
     * Crear un solrpendiente
     * 
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     * @ejb.transaction type="RequiresNew"
     * 
     * @return Booleano indicando si se ha obtenido el registro .
     * @throws DelegateException 
   	 */
    public SolrPendiente grabarSolrPendiente(String tipo, Long idElemento, Long idArchivo, Long accion)  
 	throws DelegateException{
    	    	
    	 Session session = getSession();
         try
        	{
    			
    			final SolrPendiente solrpendiente = new SolrPendiente();
    	    	
    			solrpendiente.setTipo(tipo);
    			solrpendiente.setIdElem(idElemento);
    			solrpendiente.setIdArchivo(idArchivo);
    			solrpendiente.setAccion((int)(long)accion);
    			solrpendiente.setFechaCreacion(new Date());
    			solrpendiente.setResultado(0);
    			session.save(solrpendiente);
    			session.flush();
    			
    			return solrpendiente;
    		           		     
            } catch (HibernateException he) {
                throw new EJBException(he);
            } finally {
                close(session);
            }
    }
    
    /**
     * Obtener Unidades Administrativas
     * 
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     * @ejb.transaction type="RequiresNew"
     * 
     * @return lista de Unidades Administrativas . 
   	 */
    public Collection<UnidadListData> getUnidadesAdministrativas(String lang){
    	OrganigramaProvider op = PluginFactory.getInstance().getOrganigramaProvider();
    	Collection<UnidadListData> list = null;
    	try{
    		list = op.getUnidadesPrincipales(lang);	
    	} catch (PluginException e1) {    
    		log.error("No se han podido obtener la Unidades Administrativas");
    		return null;
		}
    	
    	return list;
    	
    }
    
    /**
     * Lista todos los SolrPendientesJob según cuantos.
     * 
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     * @ejb.transaction type="RequiresNew"
     *
     * @return Devuelve un listado de todos los SolrPendientes.
     */
    public List<SolrPendienteJob> getListJobs(final int cuantos, final String tipoIndexacion) {

        Session session = getSession();
        try {
        	Query query = session.createQuery(" from SolrPendienteJob solrpendientejob where solrpendientejob.tipo = '"+tipoIndexacion+"' order by solrpendientejob.id desc");
        	query.setMaxResults(cuantos);
        	return query.list();
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }
    
    
    /**
     * Crear una solr pendiente job. 
     * 
   	 * @ejb.interface-method
   	 * @ejb.permission unchecked="true"
   	 * @ejb.transaction type="RequiresNew"
   	 * 
   	 * @return Booleano indicando si se indexan todos los procesos pendientes .     *  
   	 */
	public SolrPendienteJob crearSorlPendienteJob(String tipo) {
		try
    	{
			final Session session = getSession();
			final SolrPendienteJob solrpendienteJob = new SolrPendienteJob();
	    	solrpendienteJob.setFechaIni(new Date());
	    	solrpendienteJob.setTipo(tipo);
	    	
	    	session.save(solrpendienteJob); 
			session.flush();
			session.close();
			return solrpendienteJob;
    	 } catch(Exception exception) {
 			throw new EJBException(exception);
 		}
	}
	
	/**
	 * Cerrando el pendiente job.
	 * 
   	 * @ejb.interface-method
   	 * @ejb.permission unchecked="true"
   	 * @ejb.transaction type="RequiresNew"
   	 *   
   	 */
    public void cerrarSorlPendienteJob(SolrPendienteJob solrpendienteJob)  {
    	try
    	{
    		Session session = getSession();
	    	solrpendienteJob.setFechaFin(new Date());
	    	session.update(solrpendienteJob); 
			session.flush();
			session.close();
	    } catch(Exception exception) {
			throw new EJBException(exception);
		}
    }

}
