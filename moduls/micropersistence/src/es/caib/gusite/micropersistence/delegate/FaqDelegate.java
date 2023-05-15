package es.caib.gusite.micropersistence.delegate;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.Handle;
import javax.naming.NamingException;

import es.caib.gusite.micromodel.Faq;
import es.caib.gusite.micromodel.SolrPendienteResultado;
import es.caib.gusite.micropersistence.intf.FaqFacade;
import es.caib.gusite.micropersistence.intf.FaqFacadeHome;
import es.caib.gusite.micropersistence.util.FaqFacadeUtil;
import es.caib.gusite.micropersistence.util.PathUOResult;
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
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public void init(final Long id) throws DelegateException {
		try {
			this.getFacade().init(id);
		} catch (final RemoteException e) {
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
	public Long grabarFaq(final Faq faq) throws DelegateException {
		try {
			return this.getFacade().grabarFaq(faq);
		} catch (final RemoteException e) {
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
	public Faq obtenerFaq(final Long id) throws DelegateException {
		try {
			return this.getFacade().obtenerFaq(id);
		} catch (final RemoteException e) {
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
		} catch (final RemoteException e) {
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
	public ArrayList<?> listarFaqs(final String idioma) throws DelegateException {
		try {
			return this.getFacade().listarFaqs(idioma);
		} catch (final RemoteException e) {
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
	public void borrarFaq(final Long id) throws DelegateException {
		try {
			this.getFacade().borrarFaq(id);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * borra una faq
	 *
	 * @param id
	 *            Id de la faq
	 * @param indexar
	 *            Indica si se indexa
	 * @throws DelegateException
	 */
	public void borrarFaq(final Long id, final boolean indexar) throws DelegateException {
		try {
			this.getFacade().borrarFaq(id, indexar);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public Hashtable<?, ?> getParametros() throws DelegateException {
		try {
			return this.getFacade().getParametros();
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public void parametrosCons() throws DelegateException {
		try {
			this.getFacade().parametrosCons();
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public int getPagina() throws DelegateException {
		try {
			return this.getFacade().getPagina();
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public void setPagina(final int pagina) throws DelegateException {
		try {
			this.getFacade().setPagina(pagina);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public void setOrderby(final String orderby) throws DelegateException {
		try {
			this.getFacade().setOrderby(orderby);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public void setOrderby2(final String orderby) throws DelegateException {
		try {
			this.getFacade().setOrderby2(orderby);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public String getValorBD(final String valor) throws DelegateException {
		try {
			return this.getFacade().getValorBD(valor);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public void setFiltro(final String valor) throws DelegateException {
		try {
			this.getFacade().setFiltro(valor);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public String getWhere() throws DelegateException {
		try {
			return this.getFacade().getWhere();
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public void setWhere(final String valor) throws DelegateException {
		try {
			this.getFacade().setWhere(valor);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public int getTampagina() throws DelegateException {
		try {
			return this.getFacade().getTampagina();
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public void setTampagina(final int tampagina) throws DelegateException {
		try {
			this.getFacade().setTampagina(tampagina);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public boolean checkSite(final Long site, final Long id) throws DelegateException {
		try {
			return this.getFacade().checkSite(site, id);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public SolrPendienteResultado indexarSolr(final SolrIndexer solrIndexer, final Long idElemento,
			final EnumCategoria categoria, final PathUOResult iPathUO) throws DelegateException {
		try {
			return this.getFacade().indexarSolr(solrIndexer, idElemento, categoria, iPathUO);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public List<Faq> obtenerFaqsByMicrositeId(final Long idMicrosite) throws DelegateException {
		try {
			return this.getFacade().obtenerFaqsByMicrositeId(idMicrosite);
		} catch (final RemoteException e) {
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
			final FaqFacadeHome home = FaqFacadeUtil.getHome();
			final FaqFacade remote = home.create();
			this.facadeHandle = remote.getHandle();
		} catch (final NamingException e) {
			throw new DelegateException(e);
		} catch (final CreateException e) {
			throw new DelegateException(e);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}
}
