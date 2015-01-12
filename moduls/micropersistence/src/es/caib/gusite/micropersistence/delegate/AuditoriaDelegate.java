package es.caib.gusite.micropersistence.delegate;

import es.caib.gusite.micromodel.Auditoria;
import es.caib.gusite.micropersistence.intf.AuditoriaFacade;
import es.caib.gusite.micropersistence.intf.AuditoriaFacadeHome;
import es.caib.gusite.micropersistence.util.AuditoriaFacadeUtil;

import javax.ejb.CreateException;
import javax.ejb.Handle;
import javax.naming.NamingException;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Business delegate para manipular auditorias.
 * @author brujula
 */
public class AuditoriaDelegate implements StatelessDelegate {

    /**
     * Crea o actualiza una auditoria
     * @param auditoria
     * @return Id de la auditoria
     * @throws DelegateException
     */
    public Long grabarAuditoria(Auditoria auditoria) throws DelegateException {
        try {
            return getFacade().grabarAuditoria(auditoria);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }

    /**
     * Listar auditorias por entidad
     * @return una lista de menus
     * @throws DelegateException
     */
    public List<Auditoria> listarAuditoriasPorEntidad(String entidad) throws DelegateException {
        try {
            return getFacade().listarAuditoriasPorEntidad(entidad);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }

    /**
     * Listar auditorias por id entidad
     * @return una lista de auditorias
     * @throws DelegateException
     */
    public List<Auditoria> listarAuditoriasPorIdEntidad(String idEntidad) throws DelegateException {
        try {
            return getFacade().listarAuditoriasPorIdEntidad(idEntidad);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }

    /**
     * Listar auditorias por usuario
     * @return una lista de auditorias
     * @throws DelegateException
     */
    public List<Auditoria> listarAuditoriasPorUsuario(String usuario) throws DelegateException {
        try {
            return getFacade().listarAuditoriasPorUsuario(usuario);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }

    /* ========================================================= */
    /* ======================== REFERENCIA AL FACADE  ========== */
    /* ========================================================= */

    private Handle facadeHandle;

    private AuditoriaFacade getFacade() throws RemoteException {
        return (AuditoriaFacade) facadeHandle.getEJBObject();
    }

    protected AuditoriaDelegate() throws DelegateException {
        try {
            AuditoriaFacadeHome home = AuditoriaFacadeUtil.getHome();
            AuditoriaFacade remote = home.create();
            facadeHandle = remote.getHandle();
        } catch (NamingException e) {
            throw new DelegateException(e);
        } catch (CreateException e) {
            throw new DelegateException(e);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
}
