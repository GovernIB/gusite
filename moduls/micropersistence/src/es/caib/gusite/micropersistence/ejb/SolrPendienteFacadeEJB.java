package es.caib.gusite.micropersistence.ejb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ejb.CreateException;
import javax.ejb.EJBException;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import es.caib.gusite.micromodel.Agenda;
import es.caib.gusite.micromodel.Archivo;
import es.caib.gusite.micromodel.Contenido;
import es.caib.gusite.micromodel.Encuesta;
import es.caib.gusite.micromodel.Faq;
import es.caib.gusite.micromodel.Idioma;
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
import es.caib.gusite.micropersistence.delegate.IdiomaDelegate;
import es.caib.gusite.micropersistence.delegate.MicrositeDelegate;
import es.caib.gusite.micropersistence.delegate.NoticiaDelegate;
import es.caib.gusite.micropersistence.delegate.SolrPendienteDelegate;
import es.caib.gusite.micropersistence.util.IndexacionUtil;
import es.caib.gusite.plugins.PluginException;
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
            criteri.addOrder(Order.asc("id"));
            return  ( List<SolrPendiente>) criteri.list();
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }
    
    
	 /**
     * Lista todos los SolrPendientes ordenados
     * 
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     * @ejb.transaction type="RequiresNew"
     *
     * @return Devuelve un listado de todos los SolrPendientes, ordenados por fecha creación.
     */
    public List<SolrPendiente> getPendientesOrdenFC() {
    	final Session session = getSession();
        try {
            final Criteria criteri = session.createCriteria(SolrPendiente.class);
            criteri.add(Restrictions.eq("resultado", 0));             
            criteri.addOrder(Order.desc("fechaCreacion"));
            return  ( List<SolrPendiente>) criteri.list();
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
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
    public List<SolrPendiente> getPendientes(Long idElemento, Long idArchivo, String tipo, Long accion, Integer resultado) {
    	final Session session = getSession();
        try {
            final Criteria criteri = session.createCriteria(SolrPendiente.class);
            if (idElemento != null) { 
            	criteri.add(Restrictions.eq("idElem", idElemento));
            }
                        
            if (tipo != null) {
            	criteri.add(Restrictions.eq("tipo", tipo));
            }
            
            /** Solo se añade el id Archivo para los de tipo microsite. **/
            if (idArchivo != null && tipo.equals(EnumCategoria.GUSITE_MICROSITE.toString())) {
            	criteri.add(Restrictions.eq("idArchivo", idArchivo));
            }
            
            if (accion != null) {
            	criteri.add(Restrictions.eq("accion", accion.intValue()));
            }
            
            if (resultado != null) {
            	criteri.add(Restrictions.eq("resultado", resultado));
            }
            return  ( List<SolrPendiente>) criteri.list();
        } catch (HibernateException he) {
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
        	 	//Paso 1. Si ya hay pendiente una acción igual, no se guarda
        	    List<SolrPendiente> pendientes = getPendientes(idElemento, idArchivo, tipo, accion, 0);
        	    if (pendientes.size() > 0) {
        	    	return null;
        	    }
        	 
        	 	SolrPendiente solrpendiente = new SolrPendiente();
        	 	solrpendiente.setTipo(tipo);
    			solrpendiente.setIdElem(idElemento);
    			solrpendiente.setIdArchivo(idArchivo);
    			solrpendiente.setAccion((int)(long)accion);
    			solrpendiente.setFechaCreacion(new Date());
    			solrpendiente.setResultado(0);
    			
    			// Paso 2. Si es un desindexar, se borra todas las reindexaciones pendientes.
    			 if (accion == IndexacionUtil.DESINDEXAR) {
	    			
    				pendientes = getPendientes(idElemento, idArchivo, tipo, IndexacionUtil.REINDEXAR, 0);
    				for (SolrPendiente pendiente : pendientes) {
    					session.delete(pendiente);
    				}
    			}
    			
    			 //Paso 3. Guardamos.
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
    public List<SolrPendienteJob> getListJobs(final int cuantos, final String tipoIndexacion, final String idElemento) {

        final Session session = getSession();
        try {
        	final StringBuffer sql = new StringBuffer();
        	sql.append(" from SolrPendienteJob solrpendientejob where 1 = 1 ");
        	if (tipoIndexacion != null && !tipoIndexacion.isEmpty()) {
        		sql.append(" and solrpendientejob.tipo = '"+tipoIndexacion+"' ");
        	}
        	if (idElemento != null && !idElemento.isEmpty()) {
        		sql.append(" and solrpendientejob.idElem = "+idElemento+" ");
        	}
        	sql.append(" order by solrpendientejob.id desc ");
        	final Query query = session.createQuery(sql.toString());
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
	public SolrPendienteJob crearSorlPendienteJob(String tipo, Long idElemento) {
		try
    	{
			final Session session = getSession();
			final SolrPendienteJob solrpendienteJob = new SolrPendienteJob();
	    	solrpendienteJob.setFechaIni(new Date());
	    	solrpendienteJob.setTipo(tipo);
	    	solrpendienteJob.setIdElem(idElemento);
	    	session.save(solrpendienteJob); 
			session.flush();
			session.close();
			return solrpendienteJob;
    	 } catch(Exception exception) {
 			throw new EJBException(exception);
 		}
	}
	
	
	
	/**
	 * Actualizar el pendiente job.
	 * 
   	 * @ejb.interface-method
   	 * @ejb.permission unchecked="true"
   	 * @ejb.transaction type="RequiresNew"
   	 *   
   	 */
    public void actualizarSorlPendienteJob(SolrPendienteJob solrpendienteJob)  {
    	try
    	{
    		Session session = getSession();
    		session.update(solrpendienteJob); 
			session.flush();
			session.close();
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

    /**
     * Lista todos los SolrPendientesJob según cuantos.
     * 
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     * @ejb.transaction type="RequiresNew"
     *
     * @return Devuelve un listado de todos los SolrPendientes.
     */
    public List<SolrPendienteJob> getListJobs(final int cuantos) {

        Session session = getSession();
        try {
        	Query query = session.createQuery("select solrpendientejob.id, solrpendientejob.fechaIni, solrpendientejob.fechaFin, solrpendientejob.tipo, solrpendientejob.idElem  from SolrPendienteJob solrpendientejob  order by solrpendientejob.id desc");
        	query.setMaxResults(cuantos);
        	List<SolrPendienteJob> jobs= new ArrayList<SolrPendienteJob>();
        	List<Object[]> lvalores = query.list();
        	for(int i = 0 ; i < lvalores.size() ; i++) {
        		Object[] valores = lvalores.get(i);
        		SolrPendienteJob job = new SolrPendienteJob();
        		if (valores[0] != null) { job.setId(Long.valueOf(valores[0].toString())); }
        		if (valores[1] != null) { job.setFechaIni((Date) valores[1]); }
        		if (valores[2] != null) { job.setFechaFin((Date) valores[2]); }
        		if (valores[3] != null) { job.setTipo(valores[3].toString()); }
        		if (valores[4] != null) { job.setIdElem(Long.valueOf(valores[4].toString())); }
        		jobs.add(job);
        	}
        	return jobs;
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }
    
    /**
     * Devuelve un solrPendienteJob a partir de su id.
     * 
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     * @ejb.transaction type="RequiresNew"
     *
     * @return Devuelve un listado de todos los SolrPendientes.
     */
    public SolrPendienteJob obtenerSolrPendienteJob(Long id) {
    	Session session = this.getSession();
		try {
			SolrPendienteJob solrPendienteJob = (SolrPendienteJob) session.get(SolrPendienteJob.class, id);
			return solrPendienteJob;

		} catch (HibernateException he) {
			throw new EJBException(he);
		} finally {
			this.close(session);
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


    /**
	 * Marca todas las tareas como finalizadas. 
	 * 
   	 * @ejb.interface-method
     * @ejb.permission unchecked="true"     
     * @return 
     * @throws DelegateException 
   	 */
     public void finalizarTodo() throws DelegateException {
    	 Session session = null;
    	 try {
			 session = getSession();
			 //session.createQuery("update SolrPendienteJob set fechaFin = sysdate where fechaFin is null").uniqueResult();
			 List<SolrPendienteJob> jobs = session.createQuery("Select solrjob from SolrPendienteJob solrjob where solrjob.fechaFin is null").list();
			 for(SolrPendienteJob job : jobs) {
				 job.setFechaFin(new Date());
				 session.update(job);
			 }
			 session.flush();
		 }  catch (Exception e) {
				throw new DelegateException(e);
		 }
    }
    
     /**
 	 * Borra los jobs antiguos.
 	 * 
     * @ejb.interface-method
     * @ejb.permission unchecked="true"     
     * @throws DelegateException 
     */
     public void limpiezaJobs(final Long minimoId) throws DelegateException {
     	 Session session = null;
     	 try {
 			 session = getSession();
 			 List<SolrPendienteJob> jobs = session.createQuery("Select solrjob from SolrPendienteJob solrjob where solrjob.id < " + minimoId).list();
 			 for(SolrPendienteJob job : jobs) {
 				session.delete(job);
 			 }
 			 session.flush();
 		 }  catch (Exception e) {
 				throw new DelegateException(e);
 		 }
     }
     
     /**
      * Indexa un solrpendiente.
      * 
      * @param solrPendiente
      * @param info
      * @return
      * @ejb.interface-method
      * @ejb.permission unchecked="true"     
      * @throws DelegateException 
      */
     public SolrPendienteResultado indexarPendiente(SolrPendiente solrPendiente, StringBuffer info) throws DelegateException {
    	 	final String username = GusitePropertiesUtil.getUserSOLR();
    	 	final String password = GusitePropertiesUtil.getPassSOLR();
    	 	final String index = GusitePropertiesUtil.getIndexSOLR();
    	 	final String urlSolr = GusitePropertiesUtil.getUrlSOLR();
         	final int diasMaximos = GusitePropertiesUtil.getDiasMaxSOLR();
         	SolrPendienteResultado solrPendienteResultado;
         	final SolrIndexer solrIndexer = (SolrIndexer) SolrFactory.getIndexer(urlSolr, index, EnumAplicacionId.GUSITE,  username, password);
 	
    	 	info.append(" Vamos a enviar una desindexación  (id:"+solrPendiente.getIdElem() +",tipo:"+solrPendiente.getTipo()+")  <br /> ");
			try {
				solrPendienteResultado = new SolrPendienteResultado(true);
				//Si es de tipo microsite y el archivo está relleno, es de tipo archivo
				if (solrPendiente.getTipo().equals(EnumCategoria.GUSITE_MICROSITE.toString()) && solrPendiente.getIdArchivo() != null) {							
					//Los ficheros de microsites están indexados varias veces, una para cada idioma, los desindexamos todos los idiomas 
					//(se pueden haber borrado los idiomas del microsite, por eso se buscan en todos los existentes)
					
					IdiomaDelegate idiomaDelegate = DelegateUtil.getIdiomaDelegate();
					List<Idioma> listaIdiomas = idiomaDelegate.listarIdiomas();	
					Boolean almenosUnElementoDesindexado = false;
					Exception ultimoError = null;
					for (Idioma idioma : listaIdiomas){
						try {
							solrIndexer.desindexar(solrPendiente.getIdArchivo().toString() +"_" + idioma.getLang(), EnumCategoria.GUSITE_ARCHIVO);
							almenosUnElementoDesindexado = true;
						} catch (Exception e) {
							log.error("  -- Error desindexando el archivo . "+ solrPendiente.getIdArchivo().toString() +"_" + idioma.getLang(), e);
							ultimoError = e;
						}
					}	
					
					if(!almenosUnElementoDesindexado && ultimoError!=null){
						//hay error y no hay almenos una desindexación 
						solrPendienteResultado = new SolrPendienteResultado(false, ExceptionUtils.getStackTrace(ultimoError));
					}
					
					
				} else {
					solrIndexer.desindexar(solrPendiente.getIdElem().toString(), EnumCategoria.fromString(solrPendiente.getTipo()));
				}
				
			} catch (Exception exception) {
				log.error("  -- Error desindexando la entidad. ", exception);
				solrPendienteResultado = new SolrPendienteResultado(false, ExceptionUtils.getStackTrace(exception));
			}
			
			if (solrPendienteResultado.isCorrecto()) {
				if(!(new Long(solrPendiente.getAccion())).equals(IndexacionUtil.DESINDEXAR)){
					//Si no es desindexar indexamos	
					// si es desindexar fichero se asume que debe existir un solrPendiente.indexar del padre del fichero encolado a posteriori. 
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
		                
						info.append(" Vamos a enviar una indexacion de un faq (id:"+solrPendiente.getIdElem() +")  <br /> ");
						solrPendienteResultado = indexarPendienteFAQ(solrIndexer, solrPendiente.getIdElem(), info);
						
					} else if (solrPendiente.getTipo().equals(EnumCategoria.GUSITE_ENCUESTA.toString())){
					
						info.append(" Vamos a enviar una indexacion de una encuesta (id:"+solrPendiente.getIdElem() +")  <br /> ");
						solrPendienteResultado = indexarPendienteEncuesta(solrIndexer, solrPendiente.getIdElem(), info);
						
					}  else {
						 log.error("Tipo de elemento no especificado correctamente");
				         solrPendienteResultado = new SolrPendienteResultado(false, "Tipo de elemento no existente ni controlado.");
					}
				}
         } else {
         	log.error("Error intentando desindexar, no ha sido capaz. Mensaje:" + solrPendienteResultado.getMensaje());
			}
			
			return solrPendienteResultado;
     }
     
     

     /**
      * Indexa un archivo de un microsite.
      * @param solrIndexer
      * @param solrPendiente
      * @param info
      * @return
      */
     private SolrPendienteResultado indexarPendienteArchivo(SolrIndexer solrIndexer, Long idMicrosite, Long idArchivo, StringBuffer info) {
     	SolrPendienteResultado resultado = null;
     	
     	final MicrositeDelegate micrositeDelegate = DelegateUtil.getMicrositeDelegate();
     	EnumCategoria tipo = EnumCategoria.GUSITE_ARCHIVO;
     	try{
     		resultado = micrositeDelegate.indexarSolrArchivo(solrIndexer, Long.valueOf(idMicrosite), tipo, Long.valueOf(idArchivo));
 			if (!resultado.isCorrecto()) {
 	    		log.error("Error indexando "+tipo+"(ID:"+idArchivo+"):"+ resultado.toString());
 	    		if (info != null) {
 	    			info.append(" ** Parece que no se ha indexado docMicrosite(ID:"+idArchivo+"):" + resultado.getMensaje().replace("\\","/")+"<br />");
 	    		}
 	    	 }
 			 
 	    } catch (Exception e) {
 			log.error("Se ha producido un error en "+tipo+" con id " +  idArchivo);
 			if (info != null) {
 				info.append(" Se ha producido un error en "+tipo+" con id " + idArchivo + "  <br /> ");
 			}
 			resultado =  new SolrPendienteResultado(false, ExceptionUtils.getStackTrace(e));
 		}	
     	return resultado;
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
     	
     	SolrPendienteResultado resultado = null;
     	EnumCategoria tipo = EnumCategoria.GUSITE_ENCUESTA;
     	
     	final EncuestaDelegate encuestaDelegate = DelegateUtil.getEncuestaDelegate();
     	try{
     		resultado = encuestaDelegate.indexarSolr(solrIndexer, idEncuesta, tipo);
 			if (!resultado.isCorrecto()) {
 	    		log.error("Error indexando "+tipo+"(ID:"+idEncuesta+"):"+ resultado.toString());
 	    		if (info != null) {
 		    		info.append(" ** Parece que no se ha indexado encuesta(ID:"+idEncuesta+"):" + resultado.getMensaje().replace("\\","/")+"<br />");
 	    		}
 	    	}
 			
 	    }catch (Exception e) {
 			log.error("Se ha producido un error en "+tipo+" con id " +  +idEncuesta);
 			if (info != null) {
 				info.append(" Se ha producido un error en "+tipo+" con id " + idEncuesta + "  <br /> ");
 			}
 			resultado = new SolrPendienteResultado(false, ExceptionUtils.getStackTrace(e));
 		}	
     	return resultado;
 	}

 	/***
 	 * Indexa un FAQ.
 	 * @param solrIndexer
 	 * @param solrPendiente
 	 * @param info
 	 * @return
 	 */
 	private SolrPendienteResultado indexarPendienteFAQ(SolrIndexer solrIndexer, Long idElemento, StringBuffer info) {
     	SolrPendienteResultado resultado = null;
     	
     	final FaqDelegate faqDelegate = DelegateUtil.getFaqDelegate();
     	final EnumCategoria tipo = EnumCategoria.GUSITE_FAQ;
     	try{
     		resultado = faqDelegate.indexarSolr(solrIndexer, idElemento, tipo);
 			if (!resultado.isCorrecto()) {
 	    		log.error("Error indexando "+tipo+"(ID:"+idElemento+"):"+ resultado.toString());
 	    		if (info != null) {
 	    			info.append(" ** Parece que no se ha indexado faq(ID:"+idElemento+"): " + resultado.getMensaje().replace("\\","/")+"<br />");
 	    		}
 	    	 }
 	    }catch (Exception e) {
 			log.error("Se ha producido un error en "+tipo+" con id " +  idElemento);
 			if (info != null) {
 				info.append(" Se ha producido un error en "+tipo+" con id " + idElemento + "  <br /> ");
 			}
 			resultado = new SolrPendienteResultado(false, ExceptionUtils.getStackTrace(e));
 		}
     	return resultado;
 	}
     

 	/***
      * Indexa todo una agenda, incluido sus documentos. 
      * @param solrIndexer
      * @param solrPendiente
      * @return
      */
     private SolrPendienteResultado indexarPendienteAgenda(final SolrIndexer solrIndexer, final Long idElemento, final StringBuffer info) {

     	SolrPendienteResultado resultado = null;
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
     		
 			resultado = agendaDelegate.indexarSolr(solrIndexer, idElemento,tipo);
 			if (!resultado.isCorrecto()) {
 	    		log.error("Error indexando "+tipo+"(ID:"+idElemento+"):"+ resultado.toString());
 	    		if (info != null) {
 		    		info.append(" ** Parece que no se ha indexado agenda(ID:"+idElemento+"): " + resultado.getMensaje().replace("\\","/")+"<br />");
 	    		}
 	    		return resultado;
 	    	 } 
 	    }catch (Exception e) {
 			log.error("Se ha producido un error en "+tipo+" con id " +  agenda.getId());
 			if (info != null) {
 				info.append(" Se ha producido un error en "+tipo+" con id " +  agenda.getId() +"  <br /> ");
 				info.append(" -- Parece que ha sido sido correcto. Resultado: " + resultado.toString().replace("\\","/")+"<br />");
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
         				 
         				//Indexamos las AGENDAS con archivo 
         				resultado = agendaDelegate.indexarSolrArchivo(solrIndexer,  agenda.getId(), EnumCategoria.GUSITE_AGENDA,tradAgen.getDocumento().getId());
         				
         				if (!resultado.isCorrecto()) {
         					 log.debug("Error indexando documento (DOC:"+arc.getId()+"):"+ resultado.toString());
 							 msgRetorno += "Error indexando documento "+arc.getId() + " (revise el log) <br />";
 							 info.append(" ** Parece que no se ha indexado docAgenda(ID:"+arc.getId() + "): " + resultado.getMensaje().replace("\\","/")+"<br />");
 						}
         				
         			} catch (Exception e) {
     					log.error("Se ha producido un error en documento con id " + arc.getId());
     					info.append(" Error enviando una indexacion de un archivo de "+tipo+" (idPadre:"+agenda.getId()+",idArchivo:"+tradAgen.getDocumento().getId() +")  <br /> ");
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
     		resultado = noticiaDelegate.indexarSolr(solrIndexer, idElemento, tipo);
 			if (!resultado.isCorrecto()) {
 	    		log.error("Error indexando "+tipo+"(ID:"+idElemento+"):"+ resultado.toString());
 	    		info.append(" ** Parece que no se ha indexado noticia(ID:"+idElemento+"): " + resultado.getMensaje().replace("\\","/")+"<br />");
 	    		return resultado;
 	    	 }
 	    } catch (Exception e) {
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
         				
         				//Indexamos las AGENDAS con archivo 
         				resultado = noticiaDelegate.indexarSolrArchivo(solrIndexer,  noticia.getId(), EnumCategoria.GUSITE_NOTICIA,tradNoticia.getDocu().getId());
         				
         				if (!resultado.isCorrecto()) {
         					 log.debug("Error indexando documento(DOC:"+arc.getId()+"):"+ resultado.toString());
 							 msgRetorno += "Error indexando documento "+arc.getId() + " (revise el log) <br />";
 							 info.append(" ** Parece que no se ha indexado docNoticia(ID:"+arc.getId()+"): " + resultado.getMensaje().replace("\\","/")+"<br />");
 						}
         				
         			} catch (Exception e) {
     					log.error("Se ha producido un error en documento agenda con id " + arc.getId());
     					info.append(" Error enviando una indexacion de un archivo de "+tipo+" del microsite (idPadre:"+noticia.getId()+",idArchivo:"+tradNoticia.getDocu().getId() +")  <br /> ");
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
     		resultado = contenidoDelegate.indexarSolr(solrIndexer, idElemento, tipo);
 			if (!resultado.isCorrecto()) {
 	    		log.error("Error indexando "+tipo+"(ID:"+idElemento+"):"+ resultado.toString());
 	    		info.append(" ** Parece que no se ha indexado contenido(ID:"+idElemento+"): " + resultado.getMensaje().replace("\\","/")+"<br />");
 	    		return resultado;
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
 							
 	        				//Indexamos las CONTENIDOS con archivo 
 							resultado = contenidoDelegate.indexarSolrArchivo(solrIndexer,  contenido.getId(), EnumCategoria.GUSITE_CONTENIDO, docu.getId());
 	        				
 							if (!resultado.isCorrecto()) {
 	        					 log.debug("Error indexando documento(DOC:"+docu.getId()+"):"+ resultado.toString());
 								 msgRetorno += "Error indexando documento "+docu.getId() + " (revise el log) <br />";
 								 info.append(" ** Parece que no se ha indexado docContenido(ID:"+docu.getId()+"): " + resultado.getMensaje().replace("\\","/")+"<br />");
 							}
 							
 	        			} catch (Exception e) {
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
     	GusiteJobUtil.interrumpirTarea = false;
     	SolrIndexer solrIndexer = null;
     	SolrPendienteResultado resultadoMicrosite = new SolrPendienteResultado(true);
     	boolean todoCorrecto = true;
     	
         try {
         	boolean actualizarSolrPendiente;
         	if (info == null) {
         		info = new StringBuffer();
         		actualizarSolrPendiente = true;
         	} else {
         		actualizarSolrPendiente = false;
         	}
         	
         	// Verificamos si es indexable
         	MicrositeDelegate micrositedel = DelegateUtil.getMicrositeDelegate();
         	Microsite micro = micrositedel.obtenerMicrosite(idMicrosite);
         	if (micro == null) {
         		info.append("Microsite "+idMicrosite+" <br />");
         	}else {
         		info.append("Microsite "+idMicrosite+":"+micro.getUri()+"<br />");
             }
         	
         	final String username = GusitePropertiesUtil.getUserSOLR();
             final String password = GusitePropertiesUtil.getPassSOLR();
             final String index = GusitePropertiesUtil.getIndexSOLR();
             final String urlSolr = GusitePropertiesUtil.getUrlSOLR();

         	solrIndexer = (SolrIndexer) SolrFactory.getIndexer(urlSolr, index, EnumAplicacionId.GUSITE,  username, password);
         	//Desindexamos el microsite
         	try {
         		info.append("-Anem a desindexar el microsite arrel.<br />");
         		solrIndexer.desindexarRaiz(idMicrosite.toString(), EnumCategoria.GUSITE_MICROSITE);
 			} catch (ExcepcionSolrApi e) {
 				log.error("S'ha produit un error dexindexant el MICROSITE amb id " + idMicrosite, e);
 				info.append("**S'ha produit un error dexindexant.");
 				return new SolrPendienteResultado(false, "S'ha produit un error dexindexant el MICROSITE amb id " + idMicrosite);
 			} 
         	
          	SolrPendienteDelegate solrpendientedel = DelegateUtil.getSolrPendienteDelegate();
         	
         	if (actualizarSolrPendiente && solrPendienteJob != null) {
 	        	solrPendienteJob.setDescripcion(GusiteClobUtil.getClob(info.toString()));
 	        	solrpendientedel.actualizarSorlPendienteJob(solrPendienteJob);
         	}
         	
         	int totalCorrectos, totalIncorrectos;
         	
         	if (IndexacionUtil.isIndexable(micro)) {
         		  
         		info.append("-Anem a indexar els components<br />");
         		
         		
         		
 	        	//Obtenemos los ARCHIVOS del microsite
         		info.append("** Arxius:<br />"); totalCorrectos = 0; totalIncorrectos = 0;
         		ArchivoDelegate archivodel = DelegateUtil.getArchivoDelegate();
 	        	List<Archivo> listArchivos = archivodel.obtenerArchivoByMicrositeId(idMicrosite);
 	        	
 	        	for ( Archivo archivo : listArchivos){
 	        		if (archivo !=null && archivo.getId()!=null){
 	        			SolrPendienteResultado resultado = this.indexarPendienteArchivo(solrIndexer, micro.getId(), archivo.getId(), info);
 	        			if (resultado.isCorrecto()) {
 	        				totalCorrectos ++;
 	        			} else {
 	        				totalIncorrectos ++;
 	        			}
 	        		}
 	        	}
 	        	if (totalIncorrectos > 0) { todoCorrecto = false; }
 	        	info.append("**** Total arxius "+(totalCorrectos + totalIncorrectos)+" (Incorrectes:"+totalIncorrectos+") <br />");
 	        	if (actualizarSolrPendiente) {
 		        	solrPendienteJob.setDescripcion(GusiteClobUtil.getClob(info.toString()));
 		        	solrpendientedel.actualizarSorlPendienteJob(solrPendienteJob);
 	        	}
 	        	
 	        	
 	        	if (GusiteJobUtil.interrumpirTarea) { //El semaforo para salir de esto.
 	        		return new SolrPendienteResultado(false, "Finalitzat per força mentre estava amb el MICROSITE id " + idMicrosite);
     			}
 	        	
 	        	
 	        	
 	        	//Obtenemos las ENCUESTAS del microsite
 	        	info.append("** Enquestes:<br />"); totalCorrectos = 0; totalIncorrectos = 0;
         		EncuestaDelegate encuestadel = DelegateUtil.getEncuestaDelegate();        	
 	        	List<Encuesta> listEncuestas = encuestadel.obtenerEncuestasByMicrositeId(idMicrosite);
 	        	
 	        	for ( Encuesta encuesta : listEncuestas){
 	        		if (encuesta != null && encuesta.getId() != null) {
 	        			SolrPendienteResultado resultado = this.indexarPendienteEncuesta(solrIndexer, encuesta.getId(), info);
 	        			if (resultado.isCorrecto()) {
 	        				totalCorrectos ++;
 	        			} else {
 	        				totalIncorrectos ++;
 	        			}
 	        		}
 	        	}    
 	        	if (totalIncorrectos > 0) { todoCorrecto = false; }
 	        	info.append("**** Total enquestes "+(totalCorrectos + totalIncorrectos)+" (Incorrectes:"+totalIncorrectos+") <br />");
 	        	if (actualizarSolrPendiente) {
 		        	solrPendienteJob.setDescripcion(GusiteClobUtil.getClob(info.toString()));
 		        	solrpendientedel.actualizarSorlPendienteJob(solrPendienteJob);
 	        	}
 	        	
 	        	if (GusiteJobUtil.interrumpirTarea) { //El semaforo para salir de esto.
 	        		return new SolrPendienteResultado(false, "Finalitzat per força mentre estava amb el MICROSITE id " + idMicrosite);
     			}
 	        	
 	        	
 	        	
 	        	//Obtenemos los FAQ's del microsite
 	        	info.append("** Faqs: <br />"); totalCorrectos = 0; totalIncorrectos = 0;
 	        	FaqDelegate faqdel = DelegateUtil.getFaqDelegate();        	        	
 	        	List<Faq> listFaqs = faqdel.obtenerFaqsByMicrositeId(idMicrosite);
 	        	
 	        	for ( Faq faq : listFaqs){
 	        		if (faq !=null && faq.getId() != null) {    
 	        			SolrPendienteResultado resultado = this.indexarPendienteFAQ(solrIndexer, faq.getId(), info);
 	        			if (resultado.isCorrecto()) {
 	        				totalCorrectos ++;
 	        			} else {
 	        				totalIncorrectos ++;
 	        			}
 	        		}
 	        	} 
 	        	if (totalIncorrectos > 0) { todoCorrecto = false; }
 	        	info.append("**** Total faqs "+(totalCorrectos + totalIncorrectos)+" (Incorrectes:"+totalIncorrectos+") <br />");
 	        	if (actualizarSolrPendiente) {
 		        	solrPendienteJob.setDescripcion(GusiteClobUtil.getClob(info.toString()));
 		        	solrpendientedel.actualizarSorlPendienteJob(solrPendienteJob);
 	        	}
 	        	
 	        	if (GusiteJobUtil.interrumpirTarea) { //El semaforo para salir de esto.
 	        		return new SolrPendienteResultado(false, "Finalitzat per força mentre estava amb el MICROSITE id " + idMicrosite);
     			}
 	        	
 	        	
 	        	//Obtenemos las NOTICIAS del microsite        	
 	        	info.append("** Noticies: <br />"); totalCorrectos = 0; totalIncorrectos = 0;
 	        	NoticiaDelegate noticiadel = DelegateUtil.getNoticiasDelegate();        	        	      
 	        	List<Noticia> listNoticias = noticiadel.obtenerNoticiasByMicrositeId(idMicrosite);
 	        	        	
 	        	for ( Noticia noticia : listNoticias){
 	        		if (noticia != null && noticia.getId() != null) {
 	        			SolrPendienteResultado resultado = this.indexarPendienteNoticia(solrIndexer, noticia.getId(), info);	
 	        			if (resultado.isCorrecto()) {
 	        				totalCorrectos ++;
 	        			} else {
 	        				totalIncorrectos ++;
 	        			}
 	        		}
 	        	}  
 	        	if (totalIncorrectos > 0) { todoCorrecto = false; }
 	        	info.append("**** Total noticias "+(totalCorrectos + totalIncorrectos)+" (Incorrectes:"+totalIncorrectos+") <br />");
 	        	if (actualizarSolrPendiente) {
 		        	solrPendienteJob.setDescripcion(GusiteClobUtil.getClob(info.toString()));
 		        	solrpendientedel.actualizarSorlPendienteJob(solrPendienteJob);
 	        	}
 	        	
 	        	if (GusiteJobUtil.interrumpirTarea) { //El semaforo para salir de esto.
 	        		return new SolrPendienteResultado(false, "Finalitzat per força mentre estava amb el MICROSITE id " + idMicrosite);
     			}
 	        	
 	        	
 	        	
 	        	
 	        	//Obtenemos las AGENDAS del microsite
 	        	info.append("** Agendas: <br />"); totalCorrectos = 0; totalIncorrectos = 0;
 	        	AgendaDelegate agendadel = DelegateUtil.getAgendaDelegate();        	        	
 	        	List<Agenda> listAgendas = agendadel.obtenerAgendasByMicrositeId(idMicrosite);
 	        	
 	        	for ( Agenda agenda : listAgendas){
 	        		if (agenda != null && agenda.getId() != null) {
 	        			SolrPendienteResultado resultado = this.indexarPendienteAgenda(solrIndexer, agenda.getId(), info);	 
 	        			if (resultado.isCorrecto()) {
 	        				totalCorrectos ++;
 	        			} else {
 	        				totalIncorrectos ++;
 	        			}
 	        		}	        		
 	        	}  
 	        	if (totalIncorrectos > 0) { todoCorrecto = false; }
 	        	info.append("**** Total agenda "+(totalCorrectos + totalIncorrectos)+" (Incorrectes:"+totalIncorrectos+") <br />");
 	        	if (actualizarSolrPendiente) {
 		        	solrPendienteJob.setDescripcion(GusiteClobUtil.getClob(info.toString()));
 		        	solrpendientedel.actualizarSorlPendienteJob(solrPendienteJob);
 	        	}
 	        	
 	        	if (GusiteJobUtil.interrumpirTarea) { //El semaforo para salir de esto.
 	        		return new SolrPendienteResultado(false, "Finalitzat per força mentre estava amb el MICROSITE id " + idMicrosite);
     			}
 	        	
 	        	
 	        	
 	        	
 	        	//Obtenemos los CONTENIDOS del microsite
 	        	info.append("** Continguts: <br />"); totalCorrectos = 0; totalIncorrectos = 0;
 	        	ContenidoDelegate contenidodel = DelegateUtil.getContenidoDelegate();                	        
 	        	List<Contenido> listContenidos = contenidodel.listarAllContenidos(idMicrosite.toString());
 	        	        	
 	        	for (Contenido contenido : listContenidos) {
 	        		if (contenido != null && contenido.getId() != null) {
 	        			SolrPendienteResultado resultado = this.indexarPendienteContenido(solrIndexer, contenido.getId(), info);
 	        			if (resultado.isCorrecto()) {
 	        				totalCorrectos ++;
 	        			} else {
 	        				totalIncorrectos ++;
 	        			}
 	        		}	        		
 	        	}
 	        	if (totalIncorrectos > 0) { todoCorrecto = false; }
 	        	info.append("**** Total continguts "+(totalCorrectos + totalIncorrectos)+" (Incorrectes:"+totalIncorrectos+") <br /><br />");
 	        	
 	        
 	        } else {
 	        	info.append("El microsite no es indexable con id " + idMicrosite +".<br />");
 	        	resultadoMicrosite.setIndexable(false);
 	        }
 	        
 			 	
 	    	//Comiteamos los cambios.
 	    	try {
 				solrIndexer.commit();
 				log.debug("Se ha indexado correctamente el microsite con id " + idMicrosite);			
 		    } catch (ExcepcionSolrApi e) {
 				log.error("No se ha podido comitear la indexación" + e.getMessage(), e);
 			}
 	    	return new SolrPendienteResultado(todoCorrecto, "Parece que ha funcionado correcto la indexación del MICROSITE con id " + idMicrosite+ ", ver la info para más información.");
     	}
          catch (Exception he) {
             throw new EJBException(he);
         } finally {
         	if (solrPendienteJob != null) {
         		solrPendienteJob.setDescripcion(GusiteClobUtil.getClob(info.toString()));
 	    	}
         }
     }
     
}