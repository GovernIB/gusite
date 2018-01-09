package es.caib.gusite.micropersistence.delegate;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.Handle;
import javax.naming.NamingException;

import es.caib.gusite.micromodel.Agenda;
import es.caib.gusite.micromodel.Noticia;
import es.caib.gusite.micromodel.SolrPendienteResultado;
import es.caib.gusite.micropersistence.intf.AgendaFacade;
import es.caib.gusite.micropersistence.intf.AgendaFacadeHome;
import es.caib.gusite.micropersistence.util.AgendaFacadeUtil;
import es.caib.gusite.micropersistence.util.PathUOResult;
import es.caib.solr.api.SolrIndexer;
import es.caib.solr.api.model.types.EnumCategoria;

/**
 * Business delegate para manipular Agenda.
 * 
 * @author Indra
 */
public class AgendaDelegate implements StatelessDelegate {

	private static final long serialVersionUID = 3550874014502707012L;

	/* ========================================================= */
	/* ======================== MÉTODOS DE NEGOCIO ============= */
	/* ========================================================= */

	public void init() throws DelegateException {
		try {
			this.getFacade().init();
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public void init(Long id) throws DelegateException {
		try {
			this.getFacade().init(id);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public void init(Long id, String idiomapasado) throws DelegateException {
		try {
			this.getFacade().init(id, idiomapasado);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Crea o actualiza una actividad
	 * 
	 * @param agenda
	 * @return Identificador de la agenda
	 * @throws DelegateException
	 */
	public Long grabarAgenda(Agenda agenda) throws DelegateException {
		try {
			return this.getFacade().grabarAgenda(agenda);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Obtiene una Agenda
	 * 
	 * @param id
	 *            Id de la agenda
	 * @return una Agenda
	 * @throws DelegateException
	 */
	public Agenda obtenerAgenda(Long id) throws DelegateException {
		try {
			return this.getFacade().obtenerAgenda(id);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Lista todas las Agendas
	 * 
	 * @return Lista de Agendas
	 * @throws DelegateException
	 */
	public List<?> listarAgendas() throws DelegateException {
		try {
			return this.getFacade().listarAgendas();
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Lista todas las Agendas
	 * 
	 * @param idiomapasado
	 * @return Lista de Agendas
	 * @throws DelegateException
	 */
	public List<?> listarAgendasrec(String idiomapasado)
			throws DelegateException {
		try {
			return this.getFacade().listarAgendasrec(idiomapasado);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Lista todas las Agendas
	 * 
	 * @param idioma
	 * @return un ArrayList
	 * @throws DelegateException
	 */
	public ArrayList<?> listarAgendas(String idioma) throws DelegateException {
		try {
			return this.getFacade().listarAgendas(idioma);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Lista todas las Agendas
	 * 
	 * @param fecha
	 * @param idioma
	 * @return ArrayList
	 * @throws DelegateException
	 */
	public ArrayList<?> listarAgendas(Date fecha, String idioma)
			throws DelegateException {
		try {
			return this.getFacade().listarAgendas(fecha, idioma);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Borra una Agenda
	 * 
	 * @param id
	 *            Id de la agenda
	 * @throws DelegateException
	 */
	public void borrarAgenda(Long id) throws DelegateException {
		try {
			this.getFacade().borrarAgenda(id);
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

	public boolean checkSite(Long site, Long id) throws DelegateException {
		try {
			return this.getFacade().checkSite(site, id);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public SolrPendienteResultado indexarSolr(final SolrIndexer solrIndexer, final Long idElemento, final EnumCategoria categoria, final PathUOResult iPathUO)
			throws DelegateException {
		try {
			return this.getFacade().indexarSolr(solrIndexer, idElemento, categoria, iPathUO);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}
	
	public SolrPendienteResultado indexarSolrArchivo(final SolrIndexer solrIndexer, final Long idElemento, final EnumCategoria categoria, final Long idArchivo, final PathUOResult iPathUO)
			throws DelegateException {
		try {
			return this.getFacade().indexarSolrArchivo(solrIndexer, idElemento, categoria,idArchivo, iPathUO);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}
	
	public List<Agenda> obtenerAgendasByMicrositeId(Long idMicrosite)
			  throws DelegateException {
		  try {
			   return this.getFacade().obtenerAgendasByMicrositeId(idMicrosite);
		  } catch (RemoteException e) {
			  throw new DelegateException(e);
		  }
			   
	}
	/* ========================================================= */
	/* ======================== REFERENCIA AL FACADE ========== */
	/* ========================================================= */

	private Handle facadeHandle;

	private AgendaFacade getFacade() throws RemoteException {
		return (AgendaFacade) this.facadeHandle.getEJBObject();
	}

	protected AgendaDelegate() throws DelegateException {
		try {
			AgendaFacadeHome home = AgendaFacadeUtil.getHome();
			AgendaFacade remote = home.create();
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