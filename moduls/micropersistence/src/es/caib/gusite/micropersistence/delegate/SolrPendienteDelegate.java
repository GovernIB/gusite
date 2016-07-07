package es.caib.gusite.micropersistence.delegate;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.Handle;
import javax.naming.NamingException;

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

}
