package es.caib.gusite.micropersistence.delegate;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.Handle;
import javax.naming.NamingException;

import es.caib.gusite.micropersistence.intf.SolrPendienteProcesoFacade;
import es.caib.gusite.micropersistence.intf.SolrPendienteProcesoFacadeHome;
import es.caib.gusite.micropersistence.util.SolrPendienteProcesoFacadeUtil;
import es.caib.gusite.plugins.organigrama.UnidadListData;



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
	 *  
	 * @throws DelegateException
	 */
	public Boolean indexarPendientes() throws DelegateException {
		try {
			return this.getFacade().indexarPendientes();
		}catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}
	
	/**
	 * Indexa un Microsite
	 *  
	 * @throws DelegateException
	 */
	public Boolean indexarMicrosite(Long idMicrosite) throws DelegateException {
		try {
			return this.getFacade().indexarMicrosite( idMicrosite);
		}catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}
	
	/**
	 * Indexa Todo
	 *  
	 * @throws DelegateException
	 */
	public Boolean indexarTodo() throws DelegateException {
		try {
			return this.getFacade().indexarTodo();
		}catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}
	
	/**
	 * Indexa Todo
	 *  
	 * @throws DelegateException
	 */
	public Boolean indexarMicrositeByUA(String idUAdministrativa) throws DelegateException, RemoteException {
		try {
			return this.getFacade().indexarMicrositeByUA(idUAdministrativa);
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
