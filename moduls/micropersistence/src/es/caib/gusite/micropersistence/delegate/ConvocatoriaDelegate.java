package es.caib.gusite.micropersistence.delegate;

import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.Handle;
import javax.naming.NamingException;

import es.caib.gusite.micromodel.Convocatoria;
import es.caib.gusite.micropersistence.intf.ConvocatoriaFacade;
import es.caib.gusite.micropersistence.intf.ConvocatoriaFacadeHome;
import es.caib.gusite.micropersistence.util.ConvocatoriaFacadeUtil;

/**
 * Business delegate para manipular convocatoria.
 * 
 * @author Indra
 */
public class ConvocatoriaDelegate implements StatelessDelegate {

	private static final long serialVersionUID = -1652208405910819789L;

	/* ========================================================= */
	/* ======================== MÉTODOS DE NEGOCIO ============= */
	/* ========================================================= */

	/**
	 * Inicializo los parámetros de la consulta de Convocatoria.
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
	 * Inicializo los parámetros de la consulta de Convocatoria.
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
	 * Crea o actualiza un Convocatoria
	 * 
	 * @param convocatoria
	 * @return Id del convocatoria
	 * @throws DelegateException
	 */
	public Long grabarConvocatoria(Convocatoria convocatoria)
			throws DelegateException {
		try {
			return this.getFacade().grabarConvocatoria(convocatoria);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Obtiene un Convocatoria
	 * 
	 * @param id
	 *            Id del convocatoria
	 * @return Convocatoria
	 * @throws DelegateException
	 */
	public Convocatoria obtenerConvocatoria(Long id) throws DelegateException {
		try {
			return this.getFacade().obtenerConvocatoria(id);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Existe convocatoria
	 * 
	 * @param id
	 *            Id del convocatoria
	 * @return True si existe el convocatoria
	 * @throws DelegateException
	 */
	public boolean existeConvocatoria(Long id) throws DelegateException {
		try {
			return this.getFacade().existeConvocatoria(id);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Lista todos los Convocatorias
	 * 
	 * @return una lista de convocatorias
	 * @throws DelegateException
	 */
	public List<?> listarConvocatorias() throws DelegateException {
		try {
			return this.getFacade().listarConvocatorias();
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Lista todos los convocatorias de un microsite<br/>
	 * 
	 * @param micro
	 *            Identificador del microsite
	 * @return List de beans <em>Convocatoria</em>
	 * @throws DelegateException
	 */
	public List<?> listarAllConvocatorias() throws DelegateException {
		try {
			return this.getFacade().listarAllConvocatorias();
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * borra un Convocatoria
	 * 
	 * @param id
	 *            Id de un convocatoria
	 * @throws DelegateException
	 */
	public void borrarConvocatoria(Long id) throws DelegateException {
		try {
			this.getFacade().borrarConvocatoria(id);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Añade una lista de distribucion a una convocatoria
	 * 
	 * @param id
	 *            Id de un convocatoria, Id de una lista de distribucion
	 * @throws DelegateException
	 */
	public void ponerDestinatario(Long idConvocatoria, Long idLDistribucion)
			throws DelegateException {
		try {
			this.getFacade().ponerDestinatario(idConvocatoria, idLDistribucion);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Quita los destinatarios de una convocatoria
	 * 
	 * @param id
	 *            Id de un convocatoria
	 * @throws DelegateException
	 */
	public void quitarDestinatarios(Long idConvocatoria)
			throws DelegateException {
		try {
			this.getFacade().quitarDestinatarios(idConvocatoria);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public List<?> listarCorreosEnvio(Long idConvocatoria, Integer nIntentos)
			throws DelegateException {
		try {
			return this.getFacade().listarCorreosEnvio(idConvocatoria,
					nIntentos);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public List<?> listarCorreosEnvio(Long idConvocatoria, Integer nIntentos,
			String correo) throws DelegateException {
		try {
			return this.getFacade().listarCorreosEnvio(idConvocatoria,
					nIntentos, correo);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/*
	 * *
	 * Devuelve el titulo del listado de convocatorias, la miga de pan de donde
	 * se encuentra. Podrá ser: Menu padre > Menu hijo > Convocatoria o bien
	 * Menu padre > Convocatoria según exista idmenu y/o idconvocatoria
	 * 
	 * @param idmenu
	 * 
	 * @param idconvocatoria
	 * 
	 * @return string con el titulo del listado de convocatorias
	 * 
	 * @throws DelegateException
	 * 
	 * public String migapan (String idmenu, Long idconvocatoria) throws
	 * DelegateException { try { return getFacade().migapan(idmenu,
	 * idconvocatoria); } catch (RemoteException e) { throw new
	 * DelegateException(e); } }
	 */

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

	private ConvocatoriaFacade getFacade() throws RemoteException {
		return (ConvocatoriaFacade) this.facadeHandle.getEJBObject();
	}

	protected ConvocatoriaDelegate() throws DelegateException {
		try {
			ConvocatoriaFacadeHome home = ConvocatoriaFacadeUtil.getHome();
			ConvocatoriaFacade remote = home.create();
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
