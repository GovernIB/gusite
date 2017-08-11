package es.caib.gusite.micropersistence.delegate;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.Handle;
import javax.naming.NamingException;

import es.caib.gusite.micromodel.SolrPendienteJob;
import es.caib.gusite.micromodel.SolrPendienteResultado;
import es.caib.gusite.micropersistence.intf.SolrPendienteProcesoFacade;
import es.caib.gusite.micropersistence.intf.SolrPendienteProcesoFacadeHome;
import es.caib.gusite.micropersistence.util.SolrPendienteProcesoFacadeUtil;



/**
 * Business delegate para manipular SolrPendienteProceso.
 * 
 * @author Indra
 */
public class SolrPendienteProcesoDelegate implements StatelessDelegate {

	/* ========================================================= */
	/* ======================== MÉTODOS DE NEGOCIO ============= */
	/* ========================================================= */

	private static final long serialVersionUID = -3572570976470092587L;

	/**
	 * Indexa los pendientes de indexar
	 * @param solrPendienteJob 
	 *  
	 * @throws DelegateException
	 */
	public Boolean indexarPendientes(SolrPendienteJob solrPendienteJob) throws Exception {
		try {
			return this.getFacade().indexarPendientes(solrPendienteJob);
		}catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}
	
	/**
	 * Indexa un Microsite
	 * @param solrPendienteJob 
	 *  
	 * @throws DelegateException
	 */
	public SolrPendienteResultado indexarMicrosite(Long idMicrosite, SolrPendienteJob solrPendienteJob, StringBuffer info) throws Exception {
		try {
			return this.getFacade().indexarMicrosite( idMicrosite, solrPendienteJob, info);
		}catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}
	
	/**
	 * Indexa Todo
	 * @param solrPendienteJob 
	 *  
	 * @throws DelegateException
	 */
	public Boolean indexarTodo(SolrPendienteJob solrPendienteJob) throws Exception {
		try {
			return this.getFacade().indexarTodo(solrPendienteJob);
		}catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}
	
	/**
	 * Indexa Todo
	 * @param solrPendienteJob 
	 *  
	 * @throws DelegateException
	 */
	public Boolean indexarMicrositeByUA(String idUAdministrativa, SolrPendienteJob solrPendienteJob) throws Exception {
		try {
			return this.getFacade().indexarMicrositeByUA(idUAdministrativa,solrPendienteJob);
		}catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}
	

    

     
	/* ========================================================= */
	/* ======================== REFERENCIA AL FACADE ========== */
	/* ========================================================= */

	private Handle facadeHandle;

	private SolrPendienteProcesoFacade getFacade() throws RemoteException {
		return (SolrPendienteProcesoFacade) this.facadeHandle.getEJBObject();
	}

	protected SolrPendienteProcesoDelegate() throws DelegateException {
		try {
			SolrPendienteProcesoFacadeHome home = SolrPendienteProcesoFacadeUtil.getHome();
			SolrPendienteProcesoFacade remote = home.create();
			this.facadeHandle = remote.getHandle();
		} catch (NamingException e) {
			throw new DelegateException(e);
		} catch (CreateException e) {
			throw new DelegateException(e);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}



	
     

}
