package es.caib.gusite.micropersistence.delegate;

import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.Handle;
import javax.naming.NamingException;

import es.caib.gusite.micromodel.Correo;
import es.caib.gusite.micromodel.ListaDistribucion;
import es.caib.gusite.micropersistence.intf.ListaDistribucionFacade;
import es.caib.gusite.micropersistence.intf.ListaDistribucionFacadeHome;
import es.caib.gusite.micropersistence.util.ListaDistribucionFacadeUtil;

/**
 * Business delegate para manipular listaDistribucion.
 * 
 * @author Salvador Antich
 */
public class LDistribucionDelegate implements StatelessDelegate {

	private static final long serialVersionUID = -1652208405910819789L;

	/* ========================================================= */
	/* ======================== MÉTODOS DE NEGOCIO ============= */
	/* ========================================================= */

	/**
	 * Inicializo los parámetros de la consulta de una listaDistribucion.
	 * 
	 * @throws DelegateException
	 */
	public void init(Long idConv, Long idMicro) throws DelegateException {
		try {
			this.getFacade().init(idConv, idMicro);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Inicializo los parámetros de la consulta de una listaDistribucion.
	 * 
	 * @param menu
	 * @throws DelegateException
	 */
	public void init(Long idMicro) throws DelegateException {
		try {
			this.getFacade().init(idMicro);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Crea o actualiza una listaDistribucion
	 * 
	 * @param convocatoria
	 * @return Id de la listaDistribucion
	 * @throws DelegateException
	 */
	public Long grabarListaDistribucion(ListaDistribucion listaDistribucion)
			throws DelegateException {
		try {
			return this.getFacade().grabarListaDistribucion(listaDistribucion);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Obtiene una listaDistribucion
	 * 
	 * @param id
	 *            Id de la listaDistribucion
	 * @return listaDistribucion
	 * @throws DelegateException
	 */
	public ListaDistribucion obtenerListaDistribucion(Long id)
			throws DelegateException {
		try {
			return this.getFacade().obtenerListaDistribucion(id);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Existe listaDistribucion
	 * 
	 * @param id
	 *            Id de la listaDistribucion
	 * @return True si existe la listaDistribucion
	 * @throws DelegateException
	 */
	public boolean existeListaDistribucion(Long id) throws DelegateException {
		try {
			return this.getFacade().existeListaDistribucion(id);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Añade un correo a una listaDistribucion
	 * 
	 * @return si el correo se ha andido a una lista de listaDistribucion
	 * @throws DelegateException
	 */
	public boolean anadeCorreo(Long id, String correo) throws DelegateException {
		try {
			return this.getFacade().anadeCorreo(id, correo);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Añade un correo en el sistema
	 * 
	 * @return si el correo se ha anadido al sistema
	 * @throws DelegateException
	 */
	public void anadeCorreo(Correo correo) throws DelegateException {
		try {
			this.getFacade().anadeCorreo(correo);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Añade un correo en el sistema
	 * 
	 * @return si el correo se ha anadido al sistema
	 * @throws DelegateException
	 */
	public void actualizaCorreo(Correo correo) throws DelegateException {
		try {
			this.getFacade().actualizaCorreo(correo);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Consulta un correo en el sistema
	 * 
	 * @return si el correo se ha anadido al sistema
	 * @throws DelegateException
	 */
	public Correo consultaCorreo(String correo) throws DelegateException {
		try {
			return this.getFacade().consultaCorreo(correo);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Borra un correo de una listaDistribucion
	 * 
	 * @return si el correo se ha borrado
	 * @throws DelegateException
	 */
	public boolean borrarCorreo(Long id, String correo)
			throws DelegateException {
		try {
			return this.getFacade().borrarCorreo(id, correo);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Lista todos los listaDistribucion
	 * 
	 * @return una lista de listaDistribucion
	 * @throws DelegateException
	 */
	public List<?> listarListaDistribucion() throws DelegateException {
		try {
			return this.getFacade().listarListaDistribucion();
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Lista todas las listaDistribucion de un microsite<br/>
	 * 
	 * @param micro
	 *            Identificador del microsite
	 * @return List de beans <em>listaDistribucion</em>
	 * @throws DelegateException
	 */
	public List<?> listarAllListaDistribucion() throws DelegateException {
		try {
			return this.getFacade().listarAllListaDistribucion();
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * borra una listaDistribucion
	 * 
	 * @throws DelegateException
	 */
	public void borrarListaDistribucion(Long id) throws DelegateException {
		try {
			this.getFacade().borrarListaDistribucion(id);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public StringBuffer exportarListaDistribucion(Long id)
			throws DelegateException {
		try {
			return this.getFacade().exportarListaDistribucion(id);
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

	private ListaDistribucionFacade getFacade() throws RemoteException {
		return (ListaDistribucionFacade) this.facadeHandle.getEJBObject();
	}

	protected LDistribucionDelegate() throws DelegateException {
		try {
			ListaDistribucionFacadeHome home = ListaDistribucionFacadeUtil
					.getHome();
			ListaDistribucionFacade remote = home.create();
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
