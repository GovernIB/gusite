package es.caib.gusite.micropersistence.delegate;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.Handle;
import javax.naming.NamingException;

import es.caib.gusite.micromodel.Auditoria;
import es.caib.gusite.micropersistence.intf.AuditoriaFacade;
import es.caib.gusite.micropersistence.intf.AuditoriaFacadeHome;
import es.caib.gusite.micropersistence.util.AuditoriaFacadeUtil;

/**
 * Business delegate para manipular auditorias.
 * 
 * @author brujula
 */
public class AuditoriaDelegate implements StatelessDelegate {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2023741754825629339L;

	/**
	 * Inicializo los parámetros de la consulta de una auditoria.
	 * 
	 * @throws DelegateException
	 */
	public void init() throws DelegateException {
		try {
			this.getFacade().init();
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Crea o actualiza una auditoria
	 * 
	 * @param auditoria
	 * @return Id de la auditoria
	 * @throws DelegateException
	 */
	public Long grabarAuditoria(Auditoria auditoria) throws DelegateException {
		try {
			return this.getFacade().grabarAuditoria(auditoria);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Listar auditorias por entidad
	 * 
	 * @return una lista de menus
	 * @throws DelegateException
	 */
	public List<Auditoria> listarAuditorias(String entity, String idEntity,
			String user, Date date, String micro) throws DelegateException {
		try {
			return this.getFacade().listarAuditorias(entity, idEntity, user,
					date, micro);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public Hashtable<?, ?> getParametros() throws DelegateException {
		try {
			return this.getFacade().getParametros();
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public void setPagina(int pagina) throws DelegateException {
		try {
			this.getFacade().setPagina(pagina);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/* ========================================================= */
	/* ======================== REFERENCIA AL FACADE ========== */
	/* ========================================================= */

	private Handle facadeHandle;

	private AuditoriaFacade getFacade() throws RemoteException {
		return (AuditoriaFacade) this.facadeHandle.getEJBObject();
	}

	protected AuditoriaDelegate() throws DelegateException {
		try {
			AuditoriaFacadeHome home = AuditoriaFacadeUtil.getHome();
			AuditoriaFacade remote = home.create();
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
