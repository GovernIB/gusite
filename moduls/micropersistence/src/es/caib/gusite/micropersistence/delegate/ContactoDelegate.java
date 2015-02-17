package es.caib.gusite.micropersistence.delegate;

import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.Handle;
import javax.naming.NamingException;

import es.caib.gusite.micromodel.Contacto;
import es.caib.gusite.micromodel.Lineadatocontacto;
import es.caib.gusite.micropersistence.intf.ContactoFacade;
import es.caib.gusite.micropersistence.intf.ContactoFacadeHome;
import es.caib.gusite.micropersistence.util.ContactoFacadeUtil;

/**
 * Business delegate para manipular Accesibilidad.
 * 
 * @author Indra
 */
public class ContactoDelegate implements StatelessDelegate {

	/* ========================================================= */
	/* ======================== MÉTODOS DE NEGOCIO ============= */
	/* ========================================================= */

	private static final long serialVersionUID = -9212510036435820243L;

	public void init() throws DelegateException {
		try {
			this.getFacade().init();
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Inicializo los parámetros de la consulta de Contacto.
	 * 
	 * @param id
	 *            Id del contacto
	 * @throws DelegateException
	 */
	public void init(Long id) throws DelegateException {
		try {
			this.getFacade().init(id);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Crea o actualiza un Contacto
	 * 
	 * @param contacto
	 * @return Id del contacto
	 * @throws DelegateException
	 */
	public Long grabarContacto(Contacto contacto) throws DelegateException {
		try {
			return this.getFacade().grabarContacto(contacto);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Obtiene una linea del Formulario
	 * 
	 * @param id
	 *            Id de la linea de contacto
	 * @return Lineadatocontacto
	 * @throws DelegateException
	 */
	public Lineadatocontacto obtenerLinea(Long id) throws DelegateException {
		try {
			return this.getFacade().obtenerLinea(id);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Obtiene un contacto
	 * 
	 * @param id
	 *            Id del contato
	 * @return Contacto
	 * @throws DelegateException
	 */
	public Contacto obtenerContacto(Long id) throws DelegateException {
		try {
			return this.getFacade().obtenerContacto(id);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Lista todos los Formularios
	 * 
	 * @return una Lista de contactos
	 * @throws DelegateException
	 */
	public List<?> listarContactos() throws DelegateException {
		try {
			return this.getFacade().listarContactos();
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * borra un contacto
	 * 
	 * @param id
	 * @throws DelegateException
	 */
	public void borrarContacto(Long id) throws DelegateException {
		try {
			this.getFacade().borrarContacto(id);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Añade una nueva línea al formulario o modifica la que existe
	 * 
	 * @param lin
	 * @param idcontacto
	 *            Id del contacto
	 * @throws DelegateException
	 */
	public void creamodificaLinea(Lineadatocontacto lin, Long idcontacto)
			throws DelegateException {
		try {
			this.getFacade().creamodificaLinea(lin, idcontacto);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * elimina líneas del formulario
	 * 
	 * @param lineas
	 * @param contacto_id
	 *            Id del contacto
	 * @throws DelegateException
	 */
	public void eliminarLineas(String[] lineas, Long contacto_id)
			throws DelegateException {
		try {
			this.getFacade().eliminarLineas(lineas, contacto_id);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}

	}

	/**
	 * elimina una línea del formulario
	 * 
	 * @param idLinea
	 *            Id de la linea de contacto
	 * @param idContacto
	 *            Id del contacto
	 * @throws DelegateException
	 */
	public void eliminarLinea(Long idLinea, Long idContacto)
			throws DelegateException {
		try {
			this.getFacade().eliminarLinea(idLinea, idContacto);
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

	/**
	 * Comprueba que el elemento pertenece al Microsite
	 * 
	 * @param site
	 * @param id
	 * @return true si no pertenece
	 * @throws DelegateException
	 */
	public boolean checkSite(Long site, Long id) throws DelegateException {
		try {
			return this.getFacade().checkSite(site, id);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/* ========================================================= */
	/* ======================== REFERENCIA AL FACADE ========== */
	/* ========================================================= */

	private Handle facadeHandle;

	private ContactoFacade getFacade() throws RemoteException {
		return (ContactoFacade) this.facadeHandle.getEJBObject();
	}

	protected ContactoDelegate() throws DelegateException {
		try {
			ContactoFacadeHome home = ContactoFacadeUtil.getHome();
			ContactoFacade remote = home.create();
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