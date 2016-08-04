package es.caib.gusite.micropersistence.delegate;

import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.Handle;
import javax.naming.NamingException;

import es.caib.gusite.micromodel.Usuario;
import es.caib.gusite.micropersistence.intf.UsuarioFacade;
import es.caib.gusite.micropersistence.intf.UsuarioFacadeHome;
import es.caib.gusite.micropersistence.util.UsuarioFacadeUtil;

/**
 * Business delegate para manipular usurios.
 * 
 * @author Indra
 */
public class UsuarioDelegate implements StatelessDelegate {

	/* ========================================================= */
	/* ======================== MÉTODOS DE NEGOCIO ============= */
	/* ========================================================= */

	private static final long serialVersionUID = 300340885063059316L;

	/**
	 * Inicializo los parámetros de la consulta de un usuario.
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
	 * Lista todos los usuarios
	 * 
	 * @return una lista de usuarios
	 * @throws DelegateException
	 */
	public List<?> listarUsuarios() throws DelegateException {
		try {
			return this.getFacade().listarUsuarios();
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Crea o actualiza un nuevo usuario.
	 * 
	 * @param usuario
	 * @throws DelegateException
	 */
	public void grabarUsuario(Usuario usuario) throws DelegateException {
		try {
			this.getFacade().grabarUsuario(usuario);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Obtiene una lista de usuarios.
	 * 
	 * @param parametros
	 * @return una lista de usuarios
	 * @throws DelegateException
	 */
	public List<?> buscarUsuarios(Map parametros) throws DelegateException {
		try {
			return this.getFacade().buscarUsuarios(parametros);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Obtiene un usuario.
	 * 
	 * @param id
	 * @return Usuario
	 * @throws DelegateException
	 */
	public Usuario obtenerUsuario(Long id) throws DelegateException {
		try {
			return this.getFacade().obtenerUsuario(id);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Obtiene un usuario dado el username
	 * 
	 * @param username
	 * @return Usuario
	 * @throws DelegateException
	 */
	public Usuario obtenerUsuariobyUsername(String username)
			throws DelegateException {
		try {
			return this.getFacade().obtenerUsuariobyUsername(username);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Obtiene un usuario.
	 * 
	 * @param perfil
	 * @return una lista de usuarios
	 * @throws DelegateException
	 */
	public List<?> listarUsuariosPerfil(String perfil) throws DelegateException {
		try {
			return this.getFacade().listarUsuariosPerfil(perfil);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Borra un usuario.
	 * 
	 * @param id
	 *            Id de un usuario
	 * @throws DelegateException
	 */
	public void borrarUsuario(Long id) throws DelegateException {
		try {
			this.getFacade().borrarUsuario(id);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}
	
	/**
	 * Comprueba si el usuario es nulo.
	 * 
	 * @throws DelegateException
	 */
	public void isUsuarioNulo(Usuario usu) throws DelegateException {
		try {
			this.getFacade().isUsuarioNulo(usu);
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

	public void parametrosCons() throws DelegateException {
		try {
			this.getFacade().parametrosCons();
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public int getPagina() throws DelegateException {
		try {
			return this.getFacade().getPagina();
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

	public void setOrderby(String orderby) throws DelegateException {
		try {
			this.getFacade().setOrderby(orderby);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public void setOrderby2(String orderby) throws DelegateException {
		try {
			this.getFacade().setOrderby2(orderby);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public String getValorBD(String valor) throws DelegateException {
		try {
			return this.getFacade().getValorBD(valor);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public void setFiltro(String valor) throws DelegateException {
		try {
			this.getFacade().setFiltro(valor);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public String getWhere() throws DelegateException {
		try {
			return this.getFacade().getWhere();
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public void setWhere(String valor) throws DelegateException {
		try {
			this.getFacade().setWhere(valor);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public int getTampagina() throws DelegateException {
		try {
			return this.getFacade().getTampagina();
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public void setTampagina(int tampagina) throws DelegateException {
		try {
			this.getFacade().setTampagina(tampagina);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/* ========================================================= */
	/* ======================== REFERENCIA AL FACADE ========== */
	/* ========================================================= */

	private Handle facadeHandle;

	private UsuarioFacade getFacade() throws RemoteException {
		return (UsuarioFacade) this.facadeHandle.getEJBObject();
	}

	protected UsuarioDelegate() throws DelegateException {
		try {
			UsuarioFacadeHome home = UsuarioFacadeUtil.getHome();
			UsuarioFacade remote = home.create();
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
