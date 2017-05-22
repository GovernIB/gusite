package es.caib.gusite.micropersistence.ejb;

import java.util.Calendar;
import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EJBException;

import es.caib.gusite.micromodel.SolrPendiente;
import org.hibernate.Session;

/**
 * SessionBean para ejecutar los solr pendientes de manera unitaria.
 *
 * @ejb.bean
 *  name="sac/micropersistence/SolrPendienteJobFacade"
 *  jndi-name="es.caib.gusite.micropersistence.SolrPendienteJobFacade"
 *  type="Stateless"
 *  view-type="remote"
 *  transaction-type="Container"
 *
 * @ejb.transaction type="RequiresNew"
 */
public abstract class SolrPendienteJobFacadeEJB extends HibernateEJB {

	private static final long serialVersionUID = 2372863171398481198L;

	/**
	 * @ejb.create-method
	 * @ejb.permission unchecked="true"
	 */
	public void ejbCreate() throws CreateException {

		super.ejbCreate();
	}
	


    /**
     * Lista todos los SolrPendientes
     * 
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     *
     * @return Devuelve un listado de todos los SolrPendientes.
     *
    public List<SolrPendiente> getPendientes() {

        Session session = getSession();
        try {
            Criteria criteri = session.createCriteria(SolrPendiente.class);
            criteri.add(Expression.eq("resultado", 0));
            
            List<SolrPendiente> solrPendientes = castList(SolrPendiente.class, criteri.list());

            return solrPendientes;

        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }
    
    /**
	 * @ejb.interface-method
	 * @ejb.permission unchecked="true"
	 * 
	 * @return Booleano indicando si se indexan todos los procesos .
	 *
	public Boolean indexarTodo() throws DelegateException{
    	
        Session session = getSession();
        try {
        	
        	SolrIndexer solrIndexer = obtenerParamIndexer();
        	solrIndexer.desindexarAplicacion();
        	
        	FichaDelegate fichaDelegate = DelegateUtil.getFichaDelegate();
        	ProcedimientoDelegate procDelegate = DelegateUtil.getProcedimientoDelegate();
        	NormativaDelegate normDelegate = DelegateUtil.getNormativaDelegate();
        	UnidadAdministrativaDelegate uaDelegate = DelegateUtil.getUADelegate();
        	TramiteDelegate tramDelegate = DelegateUtil.getTramiteDelegate();
        	DocumentoDelegate docuDelegate = DelegateUtil.getDocumentoDelegate();
        	
        	
			//Obtiene las fichas
        	List<Long> listFicha = fichaDelegate.buscarIdsFichas();
        	for (Long idFicha : listFicha) {
        		try {
					fichaDelegate.indexarSolr(solrIndexer, idFicha, EnumCategoria.ROLSAC_FICHA);
					
					//Obtiene Documento ficha
	        		Ficha ficha = fichaDelegate.obtenerFicha(idFicha);
	        		for (Documento docu : ficha.getDocumentos()) {
	        			try{
	        				docuDelegate.indexarSolrFichaDoc(solrIndexer, docu.getId(), EnumCategoria.ROLSAC_FICHA_DOCUMENTO);
	        			} catch (Exception e) {
	        				e.printStackTrace();
	    					log.error("Se ha producido un error en documento ficha con id " + docu.getId());
	    				}
					}
				} catch (Exception e) {
					log.error("Se ha producido un error en la ficha con id " + idFicha);
				}
			}
        	
        	//Obtiene los procedimientos
        	List<Long> listProc = procDelegate.buscarIdsProcedimientos();
        	for (Long idProc : listProc) {
        		try {
					procDelegate.indexarSolr(solrIndexer, idProc, EnumCategoria.ROLSAC_PROCEDIMIENTO);
					//Obtiene Documento procedimiento
	        		ProcedimientoLocal proc = procDelegate.obtenerProcedimiento(idProc);
	        		for (Documento docu : proc.getDocumentos()) {
	        			try{
	        				docuDelegate.indexarSolrFichaDoc(solrIndexer, docu.getId(), EnumCategoria.ROLSAC_PROCEDIMIENTO_DOCUMENTO);
	        			} catch (Exception e) {
	    					log.error("Se ha producido un error en documento procedimiento con id " + docu.getId());
	    				}
					}
				} catch (Exception e) {
					log.error("Se ha producido un error en el procedimiento con id " + idProc);
				}
        		
			}
        	
        	//Obtiene las normativas
        	List<Long> listNorm = normDelegate.buscarIdsNormativas();
        	for (Long idNorm : listNorm) {
        		Normativa normativa;
				try {
					normDelegate.indexarSolrNormativa(solrIndexer, idNorm, EnumCategoria.ROLSAC_NORMATIVA);
					
					//Obtiene documentos normativa
					normativa=normDelegate.obtenerNormativa(idNorm);
	        		Iterator<Entry<String, Traduccion>> itTradNorm = normativa.getTraduccionMap().entrySet().iterator();
	        		while (itTradNorm.hasNext()) {
	        			Map.Entry mapTrad = (Map.Entry)itTradNorm.next();
	        			TraduccionNormativa tradNorm=(TraduccionNormativa) mapTrad.getValue();
	        			
	        			Archivo arc = tradNorm != null && tradNorm.getArchivo() != null ? tradNorm.getArchivo() : null;
	            		if (arc != null){
	            			try{		
	            				normDelegate.indexarSolrNormativaDocumento(solrIndexer, arc.getId(), EnumCategoria.ROLSAC_NORMATIVA_DOCUMENTO);
	            			} catch (Exception e) {
	        					log.error("Se ha producido un error en documento normativa con id " + arc.getId());
	        				}
	            		}
	        		}
				} catch (Exception e) {
					log.error("Se ha producido un error en la normativa con id " + idNorm);
				}
 	
        	}
			
        	
        	//Obtiene los trámites
        	List<Long> listTram = tramDelegate.buscarIdsTramites();
        	for (Long idTramite : listTram) {
        		try {
					tramDelegate.indexarSolr(solrIndexer, idTramite, EnumCategoria.ROLSAC_TRAMITE);
				} catch (Exception e) {
					log.error("Se ha producido un error en el trámite con id " + idTramite);
				}
			}
        	
        	//Obtiene las UA´s
        	List<Long> listUas = uaDelegate.buscarIdsUas();
        	for (Long idUa : listUas) {
        		try {
					uaDelegate.indexarSolr(solrIndexer, idUa, EnumCategoria.ROLSAC_UNIDAD_ADMINISTRATIVA);
				} catch (Exception e) {
					log.error("Se ha producido un error en la unidad administrativa con id " + idUa);
				}
			}
        	
        	try {
				solrIndexer.commit();
			} catch (ExcepcionSolrApi e) {
				log.error("Ha dado un error intentando comitear en pendientes", e);				
			}
        	return true;
        } catch (ExcepcionSolrApi e1) {
			e1.printStackTrace();
			throw new DelegateException(e1);
		} finally {
            close(session);
        }
        
    }**/
    

	/**
   	 * @ejb.interface-method
   	 * @ejb.permission unchecked="true"
   	 * @ejb.transaction type="RequiresNew"
   	 *
   	 * Actualiza el job si está activo
     * @param actualizarJob
     * @param sorlPendienteJob
     *
    public void actualizarJob(SolrPendienteJob sorlPendienteJob) {
    	try
    	{
			final Session session = getSession();
	    	session.update(sorlPendienteJob); 
			session.flush();
			session.close();
    	} catch(Exception exception) {
    		throw new EJBException(exception);
    	}
	}
	
	
	
    

	
    
    
    /**
     * Método que se encarga de realizar las acciones segun si ha sido correcto o no.
     * @param solrpendiente
     * 
     * @throws HibernateException
     * @ejb.interface-method
   	 * @ejb.permission unchecked="true"
	 * @ejb.transaction type="RequiresNew" 
   	 */
    public void actualizarSolrPendiente(final SolrPendiente solrpendiente)  {
    	Session session = null;
    	try {
	    	 	session = getSession();
	    	 	//Para evitar problemas por tamaño demasiado grande.
	    	 	if (solrpendiente.getMensajeError() != null && solrpendiente.getMensajeError().length() > 300) {
	    	 		solrpendiente.setMensajeError(solrpendiente.getMensajeError().substring(0, 299));
	    	 	}
	    	 	session.update(solrpendiente);
	    	 	session.flush();
    	} catch(Exception e) {
    		log.error("Error resolver pendiente", e);
    	} finally {
    		session.close(); 
    	}
	}



	    
}
