package es.caib.gusite.micropersistence.delegate;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.Handle;
import javax.naming.NamingException;

import es.caib.gusite.micromodel.Encuesta;
import es.caib.gusite.micromodel.Faq;
import es.caib.gusite.micropersistence.intf.FaqFacade;
import es.caib.gusite.micropersistence.intf.FaqFacadeHome;
import es.caib.gusite.micropersistence.util.FaqFacadeUtil;
import es.caib.gusite.micropersistence.util.SolrPendienteResultado;
import es.caib.solr.api.SolrIndexer;
import es.caib.solr.api.model.types.EnumCategoria;

/**
 * Business delegate para manipular faq.
 * 
 * @author Indra
 */
public class FaqDelegate implements StatelessDelegate {

	private static final long serialVersionUID = 3261818919990875910L;

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

	/**
	 * Crea o actualiza una faq
	 * 
	 * @param faq
	 * @return Id de una faq
	 * @throws DelegateException
	 */
	public Long grabarFaq(Faq faq) throws DelegateException {
		try {
			return this.getFacade().grabarFaq(faq);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Obtiene una faq
	 * 
	 * @param id
	 *            Id de la faq
	 * @return Faq
	 * @throws DelegateException
	 */
	public Faq obtenerFaq(Long id) throws DelegateException {
		try {
			return this.getFacade().obtenerFaq(id);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Lista todas las faqs
	 * 
	 * @return una Lista
	 * @throws DelegateException
	 */
	public List<?> listarFaqs() throws DelegateException {
		try {
			return this.getFacade().listarFaqs();
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Lista todas las faqs poniendole un idioma por defecto
	 * 
	 * @param idioma
	 * @return ArrayList
	 * @throws DelegateException
	 */
	public ArrayList<?> listarFaqs(String idioma) throws DelegateException {
		try {
			return this.getFacade().listarFaqs(idioma);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * borra una faq
	 * 
	 * @param id
	 *            Id de la faq
	 * @throws DelegateException
	 */
	public void borrarFaq(Long id) throws DelegateException {
		try {
			this.getFacade().borrarFaq(id);
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
	
	public SolrPendienteResultado indexarSolr(final SolrIndexer solrIndexer, final Long idElemento, final EnumCategoria categoria)
			throws DelegateException {
		try {
			return this.getFacade().indexarSolr(solrIndexer, idElemento, categoria);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}
	
	
	public List<Faq> obtenerFaqsByMicrositeId(Long idMicrosite)
		  throws DelegateException {
	  try {
		   return this.getFacade().obtenerFaqsByMicrositeId(idMicrosite);
	  } catch (RemoteException e) {
		  throw new DelegateException(e);
	  }
		   
	}

	/* ========================================================= */
	/* ======================== REFERENCIA AL FACADE ========== */
	/* ========================================================= */

	private Handle facadeHandle;

	private FaqFacade getFacade() throws RemoteException {
		return (FaqFacade) this.facadeHandle.getEJBObject();
	}

	protected FaqDelegate() throws DelegateException {
		try {
			FaqFacadeHome home = FaqFacadeUtil.getHome();
			FaqFacade remote = home.create();
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
