package es.caib.gusite.micropersistence.delegate;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.Handle;
import javax.naming.NamingException;

import es.caib.gusite.micromodel.SolrPendienteJob;
import es.caib.gusite.micromodel.SolrPendiente;
import es.caib.gusite.micropersistence.intf.SolrPendienteFacade;
import es.caib.gusite.micropersistence.intf.SolrPendienteFacadeHome;
import es.caib.gusite.micropersistence.util.SolrPendienteFacadeUtil;
import es.caib.gusite.plugins.organigrama.UnidadListData;



/**
 * Business delegate para manipular SolrPendiente.
 * 
 * @author Indra
 */
public class SolrPendienteDelegate implements StatelessDelegate {

	/* ========================================================= */
	/* ======================== MÉTODOS DE NEGOCIO ============= */
	/* ========================================================= */

	private static final long serialVersionUID = -3572570976470092587L;

	/**
	 * Obtiene los SolrPendientes 
	 * 	
	 * @throws DelegateException
	 */
	public List<SolrPendiente> getPendientes()
			throws DelegateException {
		try {
			return this.getFacade().getPendientes();
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
		
	}
	
	/**
	 * Crear registro en solrPendiente
	 *  
	 * @throws DelegateException
	 */
	public SolrPendiente grabarSolrPendiente(String tipo, Long idElemento, Long idArchivo, Long accion)  throws DelegateException {
		try {
			return this.getFacade().grabarSolrPendiente(tipo, idElemento, idArchivo, accion);
		}catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}
	
	/**
	 * Obtiene las unidades administrativas 
	 *  
	 * @throws DelegateException
	 */
	public Collection<UnidadListData> getUnidadesAdministrativas(String lang) throws java.rmi.RemoteException  {		
			return this.getFacade().getUnidadesAdministrativas(lang);
		
	}		

	/**
	 * Genera un solr pendiente job. 
	 * 
	 * @param tipo
	 * @return
	 * @throws DelegateException
	 */
	 public SolrPendienteJob crearSorlPendienteJob(String tipo) throws DelegateException {
		 try {
				return this.getFacade().crearSorlPendienteJob(tipo);
		 }  catch (RemoteException e) {
				throw new DelegateException(e);
		 }
	 }
	    
	 /**
	  * Cierra un solr pendiente.  
	  * 
	  * @param solrpendienteJob
	  * @throws DelegateException
	  */
     public void cerrarSorlPendienteJob(SolrPendienteJob solrpendienteJob) throws DelegateException {
    	 try {
    			this.getFacade().cerrarSorlPendienteJob(solrpendienteJob);
    	 }  catch (RemoteException e) {
				throw new DelegateException(e);
		 }
     }

 	 /**
 	  * Método para actualizar el código solrpendiente job.
 	  * @param solrPendienteJob
 	  */
 	 public void actualizarSorlPendienteJob(SolrPendienteJob solrpendienteJob) throws DelegateException {
 		try {
			this.getFacade().actualizarSorlPendienteJob(solrpendienteJob);
	 }  catch (RemoteException e) {
			throw new DelegateException(e);
	 }
 	 }

     /**
	  * Obtiene la lista de jobs.  
	  * 
	  * @param cuantos La lista de elementos a devolver.
	  * @throws DelegateException
	  */
     public List<SolrPendienteJob> getListJobs(int cuantos, String tipo) throws DelegateException {
    	 try {
    			 return this.getFacade().getListJobs(cuantos, tipo);
    	 }  catch (RemoteException e) {
				throw new DelegateException(e);
		 }
     }
     
     /**
	  * Obtiene la lista de jobs.  
	  * 
	  * @param cuantos La lista de elementos a devolver.
	  * @throws DelegateException
	  */
     public List<SolrPendienteJob> getListJobs(int cuantos) throws DelegateException{
    	 try {
			 return this.getFacade().getListJobs(cuantos);
	 }  catch (RemoteException e) {
			throw new DelegateException(e);
	 }
     }
     
	/* ========================================================= */
	/* ======================== REFERENCIA AL FACADE ========== */
	/* ========================================================= */

	private Handle facadeHandle;

	private SolrPendienteFacade getFacade() throws RemoteException {
		return (SolrPendienteFacade) this.facadeHandle.getEJBObject();
	}

	protected SolrPendienteDelegate() throws DelegateException {
		try {
			SolrPendienteFacadeHome home = SolrPendienteFacadeUtil.getHome();
			SolrPendienteFacade remote = home.create();
			this.facadeHandle = remote.getHandle();
		} catch (NamingException e) {
			throw new DelegateException(e);
		} catch (CreateException e) {
			throw new DelegateException(e);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}


<<<<<<< HEAD

=======
>>>>>>> branch 'gusite-1.3' of https://github.com/GovernIB/gusite.git
}
