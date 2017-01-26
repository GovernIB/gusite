package es.caib.gusite.micropersistence.ejb;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import javax.ejb.CreateException;
import javax.ejb.EJBException;

import org.apache.commons.lang.exception.ExceptionUtils;
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
import es.caib.gusite.micropersistence.delegate.SolrPendienteDelegate;
import es.caib.gusite.micropersistence.delegate.SolrPendienteJobDelegate;
import es.caib.gusite.micropersistence.util.IndexacionUtil;
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
            final int diasMaximos = GusitePropertiesUtil.getDiasMaxSOLR();

            solrIndexer = (SolrIndexer) SolrFactory.getIndexer(urlSolr, index, EnumAplicacionId.GUSITE,  username, password);
    	
         	final List<SolrPendiente> listPendientes = DelegateUtil.getSolrPendienteDelegate().getPendientes();
         	final SolrPendienteJobDelegate solrPendienteJob = DelegateUtil.getSolrPendienteJobDelegate();
    	    
        	StringBuffer info = new StringBuffer();
        	
         	int i = 0;
    		for (SolrPendiente solrPendiente : listPendientes) {
    			i++;
    			SolrPendienteResultado solrPendienteResultado = null;
    			
    			try 
    			{
    				info.append(" Vamos a enviar una desindexación  (id:"+solrPendiente.getIdElem() +",tipo:"+solrPendiente.getTipo()+")  <br /> ");
					try {
						//Si es de tipo microsite y el archivo está relleno, es de tipo archivo
						if (solrPendiente.getTipo().equals(EnumCategoria.GUSITE_MICROSITE.toString()) && solrPendiente.getIdArchivo() != null) {
							solrIndexer.desindexar(solrPendiente.getIdArchivo().toString(), EnumCategoria.GUSITE_ARCHIVO);
						} else {
							solrIndexer.desindexar(solrPendiente.getIdElem().toString(), EnumCategoria.fromString(solrPendiente.getTipo()));
						}
    					solrPendienteResultado = new SolrPendienteResultado(true);
    				} catch (Exception exception) {
    					log.error("  -- Error desindexando la entidad. ", exception);
    					solrPendienteResultado = new SolrPendienteResultado(false, ExceptionUtils.getStackTrace(exception));
    				}
    				
					if (solrPendienteResultado.isCorrecto()) {
						if (solrPendiente.getTipo().equals(EnumCategoria.GUSITE_AGENDA.toString())){
							
							info.append(" Vamos a enviar una indexacion de una agenda (id:"+solrPendiente.getIdElem() +")  <br /> ");
    						solrPendienteResultado = indexarPendienteAgenda(solrIndexer, solrPendiente.getIdElem(), info);
    						
    					} else if (solrPendiente.getTipo().equals(EnumCategoria.GUSITE_NOTICIA.toString())){
    						
    						info.append(" Vamos a enviar una indexacion de una noticia (id:"+solrPendiente.getIdElem() +")  <br /> ");
    						solrPendienteResultado = indexarPendienteNoticia(solrIndexer, solrPendiente.getIdElem(), info);
    						
    					} else if (solrPendiente.getTipo().equals(EnumCategoria.GUSITE_CONTENIDO.toString())){
    						
    						info.append(" Vamos a enviar una indexacion de un contenido (id:"+solrPendiente.getIdElem() +")  <br /> ");
    						solrPendienteResultado = indexarPendienteContenido(solrIndexer, solrPendiente.getIdElem(), info);
    						
    					}else if (solrPendiente.getTipo().equals(EnumCategoria.GUSITE_MICROSITE.toString())){
    						if (solrPendiente.getIdArchivo() == null) {
    							info.append(" Vamos a enviar una indexacion de un microsite (id:"+solrPendiente.getIdElem() +")  <br /> ");
    							solrPendienteResultado = indexarMicrosite(solrPendiente.getIdElem(), null, info);
    						} else {
    							info.append(" Vamos a enviar una indexacion de un archivo microsite (id:"+solrPendiente.getIdElem() +", idArchivo:"+solrPendiente.getIdArchivo()+")  <br /> ");
        						solrPendienteResultado = indexarPendienteArchivo(solrIndexer, solrPendiente.getIdElem(), solrPendiente.getIdArchivo(), info);
    						}
    					} else  if (solrPendiente.getTipo().equals(EnumCategoria.GUSITE_FAQ.toString())){
    		                
    						info.append(" Vamos a enviar una indexacion de un microsite (id:"+solrPendiente.getIdElem() +")  <br /> ");
    						solrPendienteResultado = indexarPendienteFAQ(solrIndexer, solrPendiente.getIdElem(), info);
    						
    					} else if (solrPendiente.getTipo().equals(EnumCategoria.GUSITE_ENCUESTA.toString())){
    					
    						info.append(" Vamos a enviar una indexacion de un microsite (id:"+solrPendiente.getIdElem() +")  <br /> ");
    						solrPendienteResultado = indexarPendienteEncuesta(solrIndexer, solrPendiente.getIdElem(), info);
    						
    					}  /*else if (solrPendiente.getTipo().equals(EnumCategoria.GUSITE_ARCHIVO.toString())){
    					
    						info.append(" Vamos a enviar una indexacion de un microsite (id:"+solrPendiente.getIdElem() +")  <br /> ");
    						solrPendienteResultado = indexarPendienteArchivo(solrIndexer, solrPendiente.getIdElem(), solrPendiente.getIdArchivo(), info);
    						
    					}*/ else {
    						 log.error("Tipo de elemento no especificado correctamente");
    				         solrPendienteResultado = new SolrPendienteResultado(false, "Tipo de elemento no existente ni controlado.");
    					}
	                } else {
	                	log.error("Error intentando desindexar, no ha sido capaz. Mensaje:" + solrPendienteResultado.getMensaje());
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
    					if ( dias >= diasMaximos) {
    						solrPendiente.setResultado(-1);   
    						info.append(" Exception indexando "+solrPendiente.getIdElem().toString() +") hace "+diasMaximos+" días o más  <br /> ");
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
     * Indexa un archivo de un microsite.
     * @param solrIndexer
     * @param solrPendiente
     * @param info
     * @return
     */
    private SolrPendienteResultado indexarPendienteArchivo(SolrIndexer solrIndexer, Long idMicrosite, Long idArchivo, StringBuffer info) {
    	SolrPendienteResultado resultado = new SolrPendienteResultado(true);
    	
    	final MicrositeDelegate micrositeDelegate = DelegateUtil.getMicrositeDelegate();
    	EnumCategoria tipo = EnumCategoria.GUSITE_ARCHIVO;
    	//Paso 1. Indexamos la encuesta.
    	try{
    		if (info != null) {
				info.append(" Vamos a enviar una indexacion de una "+tipo+" del microsite (idElement:"+idArchivo +")  <br /> ");
			}
			resultado = micrositeDelegate.indexarSolrArchivo(solrIndexer, Long.valueOf(idMicrosite), tipo, Long.valueOf(idArchivo));
			if (!resultado.isCorrecto()) {
	    		log.error("Error indexando "+tipo+"(ID:"+idArchivo+"):"+ resultado.toString());
	    		info.append(" ** Parece que no se ha indexado. Resultado: " + resultado.toString().replace("\\","/"));
	    	 } else {
	    	 	log.debug("Resultado indexando "+tipo+"(ID:"+idArchivo+"):"+ resultado.toString());
	    	 	info.append(" -- Parece que ha sido sido correcto. Resultado: " + resultado.toString().replace("\\","/"));
	    	 }
			
			return resultado;
	    } catch (DelegateException e) {
			log.error("Se ha producido un error en "+tipo+" con id " +  idArchivo);
			if (info != null) {
				info.append(" Se ha producido un error en "+tipo+" con id " + idArchivo + "  <br /> ");
			}
			return new SolrPendienteResultado(false, ExceptionUtils.getStackTrace(e));
		}	
	}

    /**
     * Indexa una encuesta.
     * @param solrIndexer
     * @param solrPendiente
     * @param info
     * @return
     */
	private SolrPendienteResultado indexarPendienteEncuesta(
			SolrIndexer solrIndexer, Long idEncuesta,
			StringBuffer info) {
    	
    	SolrPendienteResultado resultado = new SolrPendienteResultado(true);
    	EnumCategoria tipo = EnumCategoria.GUSITE_ENCUESTA;
    	
    	final EncuestaDelegate encuestaDelegate = DelegateUtil.getEncuestaDelegate();
    	
    	//Paso 1. Indexamos la encuesta.
    	try{
    		if (info != null) {
				info.append(" Vamos a enviar una indexacion de una "+tipo+" del microsite (idElement:"+idEncuesta +")  <br /> ");
			}
			resultado = encuestaDelegate.indexarSolr(solrIndexer, idEncuesta, tipo);
			if (!resultado.isCorrecto()) {
	    		log.error("Error indexando "+tipo+"(ID:"+idEncuesta+"):"+ resultado.toString());
	    		info.append(" ** Parece que no se ha indexado. Resultado: " + resultado.toString().replace("\\","/"));
	    	 } else {
	    	 	log.debug("Resultado indexando "+tipo+"(ID:"+idEncuesta+"):"+ resultado.toString());
	    	 	info.append(" -- Parece que ha sido sido correcto. Resultado: " + resultado.toString().replace("\\","/"));
	    	 }
			
			return resultado;
	    }catch (DelegateException e) {
			log.error("Se ha producido un error en "+tipo+" con id " +  +idEncuesta);
			if (info != null) {
				info.append(" Se ha producido un error en "+tipo+" con id " + idEncuesta + "  <br /> ");
			}
			return new SolrPendienteResultado(false, ExceptionUtils.getStackTrace(e));
		}	
	}

	/***
	 * Indexa un FAQ.
	 * @param solrIndexer
	 * @param solrPendiente
	 * @param info
	 * @return
	 */
	private SolrPendienteResultado indexarPendienteFAQ(SolrIndexer solrIndexer, Long idElemento, StringBuffer info) {
    	SolrPendienteResultado resultado = new SolrPendienteResultado(true);
    	
    	final FaqDelegate faqDelegate = DelegateUtil.getFaqDelegate();
    	final EnumCategoria tipo = EnumCategoria.GUSITE_FAQ;
    	//Paso 1. Indexamos la encuesta.
    	try{
    		if (info != null) {
				info.append(" Vamos a enviar una indexacion de una "+tipo+" del microsite (idElement:"+idElemento +")  <br /> ");
			}
			resultado = faqDelegate.indexarSolr(solrIndexer, idElemento, tipo);
			if (!resultado.isCorrecto()) {
	    		log.error("Error indexando "+tipo+"(ID:"+idElemento+"):"+ resultado.toString());
	    		info.append(" ** Parece que no se ha indexado. Resultado: " + resultado.toString().replace("\\","/"));
	    	 } else {
	    	 	log.debug("Resultado indexando "+tipo+"(ID:"+idElemento+"):"+ resultado.toString());
	    	 	info.append(" -- Parece que ha sido sido correcto. Resultado: " + resultado.toString().replace("\\","/"));
	    	 }
			
			return resultado;
	    }catch (DelegateException e) {
			log.error("Se ha producido un error en "+tipo+" con id " +  idElemento);
			if (info != null) {
				info.append(" Se ha producido un error en "+tipo+" con id " + idElemento + "  <br /> ");
			}
			return new SolrPendienteResultado(false, ExceptionUtils.getStackTrace(e));
		}
	}
    

	/***
     * Indexa todo una agenda, incluido sus documentos. 
     * @param solrIndexer
     * @param solrPendiente
     * @return
     */
    private SolrPendienteResultado indexarPendienteAgenda(final SolrIndexer solrIndexer, final Long idElemento, final StringBuffer info) {

    	SolrPendienteResultado resultado = new SolrPendienteResultado(true);
    	EnumCategoria tipo = EnumCategoria.GUSITE_AGENDA;
    	
    	//Paso 1. Indexamos la entidad.
    	final AgendaDelegate agendaDelegate = DelegateUtil.getAgendaDelegate();
    	Agenda agenda = null;
    	try {
    		agenda =agendaDelegate.obtenerAgenda(idElemento);
    	} catch (Exception exception) {
    		return new SolrPendienteResultado(false, ExceptionUtils.getStackTrace(exception));
    	}
    	
    	try{
    		if (info != null) {
				info.append(" Vamos a enviar una indexacion de una "+tipo+" del microsite (id:"+agenda.getId() +")  <br /> ");
			}
			resultado = agendaDelegate.indexarSolr(solrIndexer, idElemento,tipo);
			if (!resultado.isCorrecto()) {
	    		log.error("Error indexando "+tipo+"(ID:"+idElemento+"):"+ resultado.toString());
	    		info.append(" ** Parece que no se ha indexado. Resultado: " + resultado.toString().replace("\\","/"));
	    		return resultado;
	    	 } else {
	    	 	log.debug("Resultado indexando "+tipo+"(ID:"+idElemento+"):"+ resultado.toString());
	    	 	info.append(" -- Parece que ha sido sido correcto. Resultado: " + resultado.toString().replace("\\","/"));
	    	 }
	    }catch (DelegateException e) {
			log.error("Se ha producido un error en "+tipo+" con id " +  agenda.getId());
			if (info != null) {
				info.append(" Se ha producido un error en "+tipo+" con id " +  agenda.getId() +"  <br /> ");
				info.append(" -- Parece que ha sido sido correcto. Resultado: " + resultado.toString().replace("\\","/"));
			}
			return new SolrPendienteResultado(false, ExceptionUtils.getStackTrace(e));
		}
		
    	//Paso 2. Recorremos documento y los indexamos
    	// En caso de que falle un documento, lo dejamos pasar por si da error al indexar pero lo tenemos en cuenta en el mensaje de retorno
    	String msgRetorno = "";
		if (agenda.getTraducciones() != null){  
			Iterator<Entry<String, Traduccion>> itTradAgenda = agenda.getTraduccionMap().entrySet().iterator();
			
			while (itTradAgenda.hasNext()) {
    			Map.Entry<String, Traduccion> mapTrad = (Map.Entry<String, Traduccion>)itTradAgenda.next();
    			TraduccionAgenda tradAgen=(TraduccionAgenda) mapTrad.getValue();        			
    			Archivo arc = tradAgen != null && tradAgen.getDocumento() != null ? tradAgen.getDocumento() : null;
        		if (arc != null){
        			try
        			{
        				info.append(" Vamos a enviar una indexacion de un archivo de agenda del microsite (idAgenda:"+agenda.getId()+",idArchivo:"+tradAgen.getDocumento().getId() +")  <br /> ");
		                
        				//Indexamos las AGENDAS con archivo 
        				resultado = agendaDelegate.indexarSolrArchivo(solrIndexer,  agenda.getId(), EnumCategoria.GUSITE_AGENDA,tradAgen.getDocumento().getId());
        				
        				if (resultado.isCorrecto()) {
        					log.debug("Resultado indexando documento (DOC:"+arc.getId()+"):"+ resultado.toString());
        					info.append(" -- Parece que ha sido sido correcto. Resultado: " + resultado.toString().replace("\\","/"));
        				} else {
							 log.debug("Error indexando documento (DOC:"+arc.getId()+"):"+ resultado.toString());
							 msgRetorno += "Error indexando documento "+arc.getId() + " (revise el log) <br />";
							 info.append(" ** Parece que no se ha indexado. Resultado: " + resultado.toString().replace("\\","/"));
						}
        				
        			} catch (Exception e) {
    					log.error("Se ha producido un error en documento con id " + arc.getId());
    					info.append(" Vamos a enviar una indexacion de un archivo de "+tipo+" (idPadre:"+agenda.getId()+",idArchivo:"+tradAgen.getDocumento().getId() +")  <br /> ");
    					msgRetorno += "Error indexando documento de "+tipo+" con id:"+arc.getId() + " (revise el log.) <br />";
    				}
        		}
        		
    		}
		}
    	
		//Paso 3. Devolvemos resultado correcto con mensaje dependiendo si falla algun documento
		return new SolrPendienteResultado(true, msgRetorno);
	}

    
    /***
     * Indexa toda una noticia, incluido sus documentos. 
     * @param solrIndexer
     * @param solrPendiente
     * @return
     */
    private SolrPendienteResultado indexarPendienteNoticia(final SolrIndexer solrIndexer, final Long idElemento, final StringBuffer info) {

    	SolrPendienteResultado resultado = new SolrPendienteResultado(true);
    	EnumCategoria tipo = EnumCategoria.GUSITE_NOTICIA;
    	
    	//Paso 1. Indexamos la noticia.
    	final NoticiaDelegate noticiaDelegate = DelegateUtil.getNoticiasDelegate();
    	Noticia noticia = null;
    	try {
    		noticia =noticiaDelegate.obtenerNoticia(idElemento);
    	} catch (Exception exception) {
    		return new SolrPendienteResultado(false, ExceptionUtils.getStackTrace(exception));
    	}
    	
    	try{
    		if (info != null) {
				info.append(" Vamos a enviar una indexacion de una "+tipo+" del microsite (idElement:"+idElemento +")  <br /> ");
			}
			resultado = noticiaDelegate.indexarSolr(solrIndexer, idElemento, tipo);
			if (!resultado.isCorrecto()) {
	    		log.error("Error indexando "+tipo+"(ID:"+idElemento+"):"+ resultado.toString());
	    		info.append(" ** Parece que no se ha indexado. Resultado: " + resultado.toString().replace("\\","/"));
	    		return resultado;
	    	 } else {
	    	 	log.debug("Resultado indexando "+tipo+"(ID:"+idElemento+"):"+ resultado.toString());
	    	 	info.append(" -- Parece que ha sido sido correcto. Resultado: " + resultado.toString().replace("\\","/"));
	    	 }
	    }catch (DelegateException e) {
			log.error("Se ha producido un error en "+tipo+" con id " +  idElemento);
			if (info != null) {
				info.append(" Se ha producido un error en "+tipo+" con id " + idElemento + "  <br /> ");
			}
			return new SolrPendienteResultado(false, ExceptionUtils.getStackTrace(e));
		}
		
    	//Paso 2. Recorremos documento y los indexamos
    	// En caso de que falle un documento, lo dejamos pasar por si da error al indexar pero lo tenemos en cuenta en el mensaje de retorno
    	String msgRetorno = "";
		if (noticia.getTraducciones() != null){  
			Iterator<Entry<String, Traduccion>> itTradNoticia = noticia.getTraduccionMap().entrySet().iterator();
			
			while (itTradNoticia.hasNext()) {
    			Map.Entry<String, Traduccion> mapTrad = (Map.Entry<String, Traduccion>)itTradNoticia.next();
    			TraduccionNoticia tradNoticia=(TraduccionNoticia) mapTrad.getValue();        			
    			Archivo arc = tradNoticia != null && tradNoticia.getDocu() != null ? tradNoticia.getDocu() : null;
        		if (arc != null){
        			try
        			{
        				info.append(" Vamos a enviar una indexacion de un archivo de "+tipo+" del microsite (idAgenda:"+noticia.getId()+",idArchivo:"+tradNoticia.getDocu().getId() +")  <br /> ");
		                
        				//Indexamos las AGENDAS con archivo 
        				resultado = noticiaDelegate.indexarSolrArchivo(solrIndexer,  noticia.getId(), EnumCategoria.GUSITE_NOTICIA,tradNoticia.getDocu().getId());
        				
        				if (resultado.isCorrecto()) {
        					log.debug("Resultado indexando documento(DOC:"+arc.getId()+"):"+ resultado.toString());
        					info.append(" -- Parece que ha sido sido correcto. Resultado: " + resultado.toString().replace("\\","/"));
        				} else {
							 log.debug("Error indexando documento(DOC:"+arc.getId()+"):"+ resultado.toString());
							 msgRetorno += "Error indexando documento "+arc.getId() + " (revise el log) <br />";
							 info.append(" ** Parece que no se ha indexado. Resultado: " + resultado.toString().replace("\\","/"));
						}
        				
        			} catch (Exception e) {
    					log.error("Se ha producido un error en documento agenda con id " + arc.getId());
    					info.append(" Vamos a enviar una indexacion de un archivo de "+tipo+" del microsite (idPadre:"+noticia.getId()+",idArchivo:"+tradNoticia.getDocu().getId() +")  <br /> ");
    					msgRetorno += "Error indexando un documento de "+tipo+" con id "+arc.getId() + " (revise el log.) <br />";
    				}
        		}
        		
    		}
		}
    	
		//Paso 3. Devolvemos resultado correcto con mensaje dependiendo si falla algun documento
		return new SolrPendienteResultado(true, msgRetorno);
	}
    
    
    /***
     * Indexa toda una noticia, incluido sus documentos. 
     * @param solrIndexer
     * @param solrPendiente
     * @return
     */
    private SolrPendienteResultado indexarPendienteContenido(final SolrIndexer solrIndexer, final Long idElemento, final StringBuffer info) {

    	SolrPendienteResultado resultado = new SolrPendienteResultado(true);
    	EnumCategoria tipo = EnumCategoria.GUSITE_CONTENIDO;
    	
    	//Paso 1. Indexamos la noticia.
    	final ContenidoDelegate contenidoDelegate = DelegateUtil.getContenidoDelegate();
    	Contenido contenido = null;
    	try {
    		contenido =contenidoDelegate.obtenerContenido(idElemento);
    	} catch (Exception exception) {
    		return new SolrPendienteResultado(false, ExceptionUtils.getStackTrace(exception));
    	}
    	
    	try{
    		if (info != null) {
				info.append(" Vamos a enviar una indexacion de una "+tipo+" del microsite (idElement:"+idElemento +")  <br /> ");
			}
			resultado = contenidoDelegate.indexarSolr(solrIndexer, idElemento, tipo);
			if (!resultado.isCorrecto()) {
	    		log.error("Error indexando "+tipo+"(ID:"+idElemento+"):"+ resultado.toString());
	    		info.append(" ** Parece que no se ha indexado. Resultado: " + resultado.toString().replace("\\","/"));
	    		return resultado;
	    	 } else {
	    	 	log.debug("Resultado indexando "+tipo+"(ID:"+idElemento+"):"+ resultado.toString());
	    	 	info.append(" -- Parece que ha sido sido correcto. Resultado: " + resultado.toString().replace("\\","/"));
	    	 }
	    } catch (Exception e) {
			log.error("Se ha producido un error en "+tipo+" con id " +  +idElemento);
			if (info != null) {
				info.append(" Se ha producido un error en "+tipo+" con id " + idElemento + "  <br /> ");
			}
			return new SolrPendienteResultado(false, ExceptionUtils.getStackTrace(e));
		}
		
    	//Paso 2. Recorremos documento y los indexamos
    	// En caso de que falle un documento, lo dejamos pasar por si da error al indexar pero lo tenemos en cuenta en el mensaje de retorno
    	String msgRetorno = "";
    	
		//Obtenemos los DOCUMENTOS de cada CONTENIDO
    	try {
			List<Archivo> listaDocu = contenidoDelegate.listarDocumentos(contenido.getMicrosite().getId().toString(), contenido.getId().toString());
			if ( listaDocu != null){
				for ( Archivo docu : listaDocu ){
					if ( docu != null &&  docu.getId()!=null){
						try{
							info.append(" Vamos a enviar una indexacion de un archivo de contenido del microsite (id:"+ contenido.getMicrosite().getId() +",idContenido:"+((Contenido) contenido).getId()+",idArchivo:"+ ((Archivo) docu).getId() +")  <br /> ");
							
	        				//Indexamos las CONTENIDOS con archivo 
							resultado = contenidoDelegate.indexarSolrArchivo(solrIndexer,  contenido.getId(), EnumCategoria.GUSITE_CONTENIDO, docu.getId());
	        				
							if (resultado.isCorrecto()) {
	        					log.debug("Resultado indexando documento(DOC:"+docu.getId()+"):"+ resultado.toString());
	        					info.append(" -- Parece que ha sido sido correcto. Resultado: " + resultado.toString().replace("\\","/"));
	        				} else {
								 log.debug("Error indexando documento(DOC:"+docu.getId()+"):"+ resultado.toString());
								 msgRetorno += "Error indexando documento "+docu.getId() + " (revise el log) <br />";
								 info.append(" ** Parece que no se ha indexado. Resultado: " + resultado.toString().replace("\\","/"));
							}
							
	        			} catch (DelegateException e) {
	    					log.error("Se ha producido un error en documento contenido con id " + docu.getId());
	    					info.append(" Se ha producido un error en documento contenido con id " +  docu.getId()+"  <br /> ");
	    					msgRetorno += "Error indexando documento "+docu.getId() + " (revise el log) <br />";
	    				}
						
					}						
			
		        }	
	        }
    	} catch(Exception exception) {
    		log.error("Se ha producido un error obteniendo la lista de archivos del contenido "+contenido.getId());
			info.append("Se ha producido un error obteniendo la lista de archivos del contenido "+contenido.getId()+"  <br /> ");
			msgRetorno += "Error indexando documentos "+contenido.getId() + " (revise el log) <br />";
    	}
    	
		//Paso 3. Devolvemos resultado correcto con mensaje dependiendo si falla algun documento
		return new SolrPendienteResultado(true, msgRetorno);
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
    public SolrPendienteResultado indexarMicrosite(Long idMicrosite ,SolrPendienteJob solrPendienteJob, StringBuffer info) throws DelegateException {
    	
    	Session session = null;
    	SolrIndexer solrIndexer = null;
        try {
        	
        	if (info == null) {
        		info = new StringBuffer();
        	}
        	
        	final String username = GusitePropertiesUtil.getUserSOLR();
            final String password = GusitePropertiesUtil.getPassSOLR();
            final String index = GusitePropertiesUtil.getIndexSOLR();
            final String urlSolr = GusitePropertiesUtil.getUrlSOLR();

        	solrIndexer = (SolrIndexer) SolrFactory.getIndexer(urlSolr, index, EnumAplicacionId.GUSITE,  username, password);
        	//Desindexamos el microsite
        	try {
        		info.append("Vamos a desindexar el microsite raiz  <br />");
        		solrIndexer.desindexarRaiz(idMicrosite.toString(), EnumCategoria.GUSITE_MICROSITE);
			} catch (ExcepcionSolrApi e) {
				log.error("Se ha producido un error en desindexar el MICROSITE con id " + idMicrosite, e);
				info.append("Se ha producido un error en desindexar el MICROSITE con id " + idMicrosite);
				return new SolrPendienteResultado(false, "Se ha producido un error en desindexar el MICROSITE con id " + idMicrosite);
			} 
        	
         	// Verificamos si es indexable
        	MicrositeDelegate micrositedel = DelegateUtil.getMicrositeDelegate();
        	Microsite micro = micrositedel.obtenerMicrosite(idMicrosite);
        	SolrPendienteDelegate solrpendientedel = DelegateUtil.getSolrPendienteDelegate();
        	
        	
        	solrPendienteJob.setDescripcion(Hibernate.createClob(info.toString()));
        	solrpendientedel.actualizarSorlPendienteJob(solrPendienteJob);
        	
        	if (IndexacionUtil.isIndexable(micro)) {
        		  
        		info.append("Vamos a indexar los compontentes del microsite <br />");
        		info.append("** ARCHIVOS DEL MICROSITE ** <br />");
        		
	        	//Obtenemos los ARCHIVOS del microsite
	        	ArchivoDelegate archivodel = DelegateUtil.getArchivoDelegate();
	        	List<Archivo> listArchivos = archivodel.obtenerArchivoByMicrositeId(idMicrosite);
	        	for ( Archivo archivo : listArchivos){
	        		if (archivo !=null && archivo.getId()!=null){
	        			this.indexarPendienteArchivo(solrIndexer, micro.getId(), archivo.getId(), info);	        			
	        		}
	        	}
	
	        	info.append("** Finalizado archivos ** <br />");
	        	info.append("  <br />");
	        	solrPendienteJob.setDescripcion(Hibernate.createClob(info.toString()));
	        	solrpendientedel.actualizarSorlPendienteJob(solrPendienteJob);
	        	info.append("** ENCUESTAS DEL MICROSITE ** <br />");
	        	
	        	//Obtenemos las ENCUESTAS del microsite
	        	EncuestaDelegate encuestadel = DelegateUtil.getEncuestaDelegate();        	
	        	List<Encuesta> listEncuestas = encuestadel.obtenerEncuestasByMicrositeId(idMicrosite);
	        	
	        	for ( Encuesta encuesta : listEncuestas){
	        		if (encuesta != null && encuesta.getId() != null) {
	        			this.indexarPendienteEncuesta(solrIndexer, encuesta.getId(), info);
	        		}
	        	}    
	        	
	        	info.append("** Finalizado encuestas ** <br />");
	        	info.append("  <br />");
	        	solrPendienteJob.setDescripcion(Hibernate.createClob(info.toString()));
	        	solrpendientedel.actualizarSorlPendienteJob(solrPendienteJob);
	        	info.append("** FAQ DEL MICROSITE ** <br />");
	        	
	
	        	//Obtenemos los FAQ's del microsite
	        	FaqDelegate faqdel = DelegateUtil.getFaqDelegate();        	        	
	        	List<Faq> listFaqs = faqdel.obtenerFaqsByMicrositeId(idMicrosite);
	        	
	        	for ( Faq faq : listFaqs){
	        		if (faq !=null && faq.getId() != null) {    
	        			this.indexarPendienteFAQ(solrIndexer, faq.getId(), info);
	        		}
	        	} 
	        	
	        	info.append("** Finalizado faq ** <br />");
	        	info.append("  <br />");
	        	solrPendienteJob.setDescripcion(Hibernate.createClob(info.toString()));
	        	solrpendientedel.actualizarSorlPendienteJob(solrPendienteJob);
	        	info.append("** NOTICIAS DEL MICROSITE ** <br />");
	        	
	        	
	        	//Obtenemos las NOTICIAS del microsite        	
	            NoticiaDelegate noticiadel = DelegateUtil.getNoticiasDelegate();        	        	      
	        	List<Noticia> listNoticias = noticiadel.obtenerNoticiasByMicrositeId(idMicrosite);
	        	        	
	        	for ( Noticia noticia : listNoticias){
	        		if (noticia != null && noticia.getId() != null) {
	        			this.indexarPendienteNoticia(solrIndexer, noticia.getId(), info);	        		
	        		}
	        	}  
	        	
	        	info.append("** Finalizado noticias ** <br />");
	        	info.append("  <br />");
	        	solrPendienteJob.setDescripcion(Hibernate.createClob(info.toString()));
	        	solrpendientedel.actualizarSorlPendienteJob(solrPendienteJob);
	        	info.append("** AGENDA DEL MICROSITE ** <br />");
	        	
	        	//Obtenemos las AGENDAS del microsite
	            AgendaDelegate agendadel = DelegateUtil.getAgendaDelegate();        	        	
	        	List<Agenda> listAgendas = agendadel.obtenerAgendasByMicrositeId(idMicrosite);
	        	
	        	
	        	for ( Agenda agenda : listAgendas){
	        		if (agenda != null && agenda.getId() != null) {
	        			this.indexarPendienteAgenda(solrIndexer, agenda.getId(), info);	        		
	        		}	        		
	        	}  
	        	
	        	info.append("** Finalizado agenda ** <br />");
	        	info.append("  <br />");
	        	solrPendienteJob.setDescripcion(Hibernate.createClob(info.toString()));
	        	solrpendientedel.actualizarSorlPendienteJob(solrPendienteJob);
	        	info.append("** CONTENIDOS DEL MICROSITE ** <br />");
	        	
	        	
	        	//Obtenemos los CONTENIDOS del microsite
	            ContenidoDelegate contenidodel = DelegateUtil.getContenidoDelegate();                	        
	        	List<Contenido> listContenidos = contenidodel.listarAllContenidos(idMicrosite.toString());
	        	        	
	        	for (Contenido contenido : listContenidos) {
	        		if (contenido != null && contenido.getId() != null) {
	        			this.indexarPendienteContenido(solrIndexer, contenido.getId(), info);	        		
	        		}	        		
	        	}
	        	
	        	info.append("** Finalizado contenido ** <br />");
	        	info.append("  <br />");
	        	
	        
	        } else {
	        	info.append("El microsite no es indexable con id " + idMicrosite);
	        }
	        
			 	
	    	//Comiteamos los cambios.
	    	try {
				solrIndexer.commit();
				log.debug("Se ha indexado correctamente el microsite con id " + idMicrosite);			
		    } catch (ExcepcionSolrApi e) {
				log.error("No se ha podido comitear la indexación" + e.getMessage(), e);
			}
	    	return new SolrPendienteResultado(true, "Parece que ha funcionado correcto la indexación del MICROSITE con id " + idMicrosite+ ", ver la info para más información.");
    	}
         catch (Exception he) {
            throw new EJBException(he);
        } finally {
        	if (solrPendienteJob != null) {
        		solrPendienteJob.setDescripcion(Hibernate.createClob(info.toString()));
	    	}
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
            			info.append(" Vamos a enviar a indexación el microsite (id:"+((Microsite) micro).getId() +")  <br /> ");
            			indexarMicrosite(((Microsite) micro).getId(),solrPendienteJob, info);	
            			
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
            	Microsite micro = micrositedel.obtenerMicrosite(idMicro);
            	if (micro.getId() != null){
            		try{
            			info.append(" Vamos a enviar a indexación el microsite (id:"+((Microsite) micro).getId()+", idUA: "+idUAdministrativa +")  <br /> ");
            			indexarMicrosite(((Microsite) micro).getId(),solrPendienteJob, info);            			
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
