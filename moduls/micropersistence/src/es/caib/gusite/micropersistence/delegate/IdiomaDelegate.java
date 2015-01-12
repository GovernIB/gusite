package es.caib.gusite.micropersistence.delegate;

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.Handle;
import javax.naming.NamingException;

import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micropersistence.intf.IdiomaFacade;
import es.caib.gusite.micropersistence.intf.IdiomaFacadeHome;
import es.caib.gusite.micropersistence.util.IdiomaFacadeUtil;

/**
 * Business delegate pera consultar idiomas.
 * @author Indra
 */
public class IdiomaDelegate implements StatelessDelegate {


	private static final long serialVersionUID = -7211931791880635635L;
	// Cache de lengaujes
    private List<?> lenguajes = null;
    private List<?> lenguajesTraductor = null;
    private long timeLen = 0L;

    // Cache de lenguaje por defecto
    private String porDefecto = null;
    private long timeDef = 0L;

    private static long maxtime = 60000L; // 60 segundos

    private boolean timeout(long time) {
        return ((System.currentTimeMillis() - time) > maxtime);
    }

    /* ========================================================= */
    /* ======================== MÉTODOS DE NEGOCIO ============= */
    /* ========================================================= */

    /**
     * Devuelve una lista de {@link java.lang.String} con el codigo ISO los idiomas.
     * @return una Lista
     * @throws DelegateException
     */
    public List<?> listarLenguajes() throws DelegateException {
        try {
            if (lenguajes == null || timeout(timeLen)) {
                lenguajes = getFacade().listarLenguajes();
                timeLen = System.currentTimeMillis();
            }
            return lenguajes;
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
    
	/**
	 * Devuelve una lista de {@link java.lang.String} con el los Ids de Idiomas del traductor.
	 * @return
	 * @throws DelegateException
	 */
    public List listarLenguajesTraductor() throws DelegateException {
        try {
            if (lenguajesTraductor == null || timeout(timeLen)) {
                lenguajesTraductor = getFacade().listarLenguajesTraductor();
                timeLen = System.currentTimeMillis();
            }
            return lenguajesTraductor;
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }

    /**
     * Obtiene el codigo ISO del lenguaje por defecto.
     * @return un string
     * @throws DelegateException
     */
    public String lenguajePorDefecto() throws DelegateException {
        try {
            if (porDefecto == null || timeout(timeDef)) {
                porDefecto = getFacade().lenguajePorDefecto();
                timeDef = System.currentTimeMillis();
            }
            return porDefecto;
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
    
    /**
     * Devuelve una lista de idiomas
     * @return una Lista
     * @throws DelegateException
     */
    public List<Idioma> listarIdiomas() throws DelegateException {
        try {
            return getFacade().listarIdiomas();
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }    
    
    /**
     * Devuelve un idioma
     * @return un idioma
     * @throws DelegateException
     */
    public es.caib.gusite.micromodel.Idioma obtenerIdioma(String lang) throws DelegateException {
        try {
            return getFacade().obtenerIdioma(lang);
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }  

    /**
     * Devuelve el tiempo que tarda en listar los idiomas.<br/>
     * @return long, tiempo en milisegundos
     * @throws DelegateException
     */
    public long testeoHql() throws DelegateException {
        try {
            return getFacade().testeoHql();
        } catch (RemoteException e) {
            throw new DelegateException(e);
        }
    }
    
    /* ========================================================= */
    /* ======================== REFERENCIA AL FACADE  ========== */
    /* ========================================================= */

    private Handle facadeHandle;

    private IdiomaFacade getFacade() throws RemoteException {
        return (IdiomaFacade) facadeHandle.getEJBObject();
    }

    protected IdiomaDelegate() throws DelegateException {
        try {
            IdiomaFacadeHome home = IdiomaFacadeUtil.getHome();
            IdiomaFacade remote = home.create();
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
