package es.caib.gusite.micropersistence.delegate;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.Handle;
import javax.naming.NamingException;

import es.caib.gusite.micromodel.Encuesta;
import es.caib.gusite.micromodel.Pregunta;
import es.caib.gusite.micromodel.Respuesta;
import es.caib.gusite.micromodel.RespuestaDato;
import es.caib.gusite.micromodel.SolrPendienteResultado;
import es.caib.gusite.micromodel.UsuarioPropietarioRespuesta;
import es.caib.gusite.micropersistence.intf.EncuestaFacade;
import es.caib.gusite.micropersistence.intf.EncuestaFacadeHome;
import es.caib.gusite.micropersistence.util.EncuestaFacadeUtil;
import es.caib.gusite.micropersistence.util.PathUOResult;
import es.caib.solr.api.SolrIndexer;
import es.caib.solr.api.model.types.EnumCategoria;

/**
 * Business delegate para manipular Encuestas.
 *
 * @author Indra
 */
public class EncuestaDelegate implements StatelessDelegate {

	/* ========================================================= */
	/* ======================== MÉTODOS DE NEGOCIO ============= */
	/* ========================================================= */

	private static final long serialVersionUID = 7844997800525315279L;

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

	public void initra(final Long id, final String idiomapasado) throws DelegateException {
		try {
			this.getFacade().initra(id, idiomapasado);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Crea o actualiza una encuesta
	 *
	 * @param encuesta
	 * @return Id de la encuesta
	 * @throws DelegateException
	 */
	public Long grabarEncuesta(final Encuesta encuesta) throws DelegateException {
		try {
			return this.getFacade().grabarEncuesta(encuesta);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Obtiene una encuesta
	 *
	 * @param id
	 *            Id de la encuesta
	 * @return Encuesta
	 * @throws DelegateException
	 */
	public Encuesta obtenerEncuesta(final Long id) throws DelegateException {
		try {
			return this.getFacade().obtenerEncuesta(id);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Obtiene un encuesta a partir de la uri
	 *
	 * @return Encuesta
	 * @throws DelegateException
	 */
	public Encuesta obtenerEncuestaDesdeUri(final String idioma, final String uri, final String site)
			throws DelegateException {
		try {
			return this.getFacade().obtenerEncuestaDesdeUri(idioma, uri, site);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Lista todas las encuestas
	 *
	 * @return una Lista de encuestas
	 * @throws DelegateException
	 */
	public List<?> listarEncuestas() throws DelegateException {
		try {
			return this.getFacade().listarEncuestas();
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Lista todas las encuestas de recursos
	 *
	 * @param idiomapasado
	 * @return una Lista de encuestas de recursos
	 * @throws DelegateException
	 */
	public List<?> listarEncuestasrec(final String idiomapasado) throws DelegateException {
		try {
			return this.getFacade().listarEncuestasrec(idiomapasado);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Borra una encuesta
	 *
	 * @param id
	 *            Id de la encuesta
	 * @throws DelegateException
	 */
	public void borrarEncuesta(final Long id) throws DelegateException {
		try {
			this.getFacade().borrarEncuesta(id);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Borra una encuesta
	 *
	 * @param id
	 *            Id de la encuesta
	 * @param indexar
	 *            Indica si se indexa
	 * @throws DelegateException
	 */
	public void borrarEncuesta(final Long id, final boolean indexar) throws DelegateException {
		try {
			this.getFacade().borrarEncuesta(id, indexar);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Crea o actualiza una pregunta
	 *
	 * @param pre
	 * @throws DelegateException
	 */
	public void grabarPregunta(final Pregunta pre) throws DelegateException {
		try {
			this.getFacade().grabarPregunta(pre);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Obtiene una pregunta
	 *
	 * @param id
	 *            Id de la pregunta
	 * @return Pregunta
	 * @throws DelegateException
	 */
	public Pregunta obtenerPregunta(final Long id) throws DelegateException {
		try {
			return this.getFacade().obtenerPregunta(id);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Lista todas las preguntas de una encuesta
	 *
	 * @param id
	 *            Id de la encuesta
	 * @return una lista de preguntas
	 * @throws DelegateException
	 */
	public List<?> listarPreguntas(final Long id) throws DelegateException {
		try {
			return this.getFacade().listarPreguntas(id);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Lista todas las preguntas de una encuesta ordenadas ascendentemente
	 *
	 * @param id
	 *            de la encuesta
	 * @return lista de preguntas
	 * @throws DelegateException
	 */
	public List<?> listarPreguntasOrdAsc(final Long id) throws DelegateException {
		try {
			return this.getFacade().listarPreguntasOrdAsc(id);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Elimina una o varias preguntas de la encuesta
	 *
	 * @param preguntas
	 * @param encuesta_id
	 *            Id de la encuesta
	 * @throws DelegateException
	 */
	public void eliminarPreguntas(final String[] preguntas, final Long encuesta_id) throws DelegateException {
		try {
			this.getFacade().eliminarPreguntas(preguntas, encuesta_id);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Crea o actualiza una respuesta
	 *
	 * @param res
	 * @throws DelegateException
	 */
	public void grabarRespuesta(final Respuesta res) throws DelegateException {
		try {
			this.getFacade().grabarRespuesta(res);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Obtiene una respuesta
	 *
	 * @param id
	 *            Id de una respuesta
	 * @return Respuesta
	 * @throws DelegateException
	 */
	public Respuesta obtenerRespuesta(final Long id) throws DelegateException {
		try {
			return this.getFacade().obtenerRespuesta(id);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Lista todas las respuestas libres de un usuario en una encuesta
	 *
	 * @param id
	 *            Id de encuesta
	 * @return una lista de respuestas
	 * @throws DelegateException
	 */
	public Hashtable<?, ?> listarRespuestasDato(final Long idEncuesta, final Long idUsuario) throws DelegateException {
		try {
			return this.getFacade().listarRespuestasDato(idEncuesta, idUsuario);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Lista todas las respuestas de una pregunta
	 *
	 * @param id
	 *            Id de una pregunta
	 * @return una lista de respuestas
	 * @throws DelegateException
	 */
	public List<?> listarRespuestas(final Long id) throws DelegateException {
		try {
			return this.getFacade().listarRespuestas(id);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Elimina una o varias respuestas de la pregunta
	 *
	 * @param respuestas
	 * @param pregunta_id
	 *            Id de una pregunta
	 * @throws DelegateException
	 */
	public void eliminarRespuestas(final String[] respuestas, final Long pregunta_id) throws DelegateException {
		try {
			this.getFacade().eliminarRespuestas(respuestas, pregunta_id);
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

	public void sumarRespuesta(final Long id) throws DelegateException {
		try {
			this.getFacade().sumarRespuesta(id);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public void sumarPregunta(final Long id) throws DelegateException {
		try {
			this.getFacade().sumarPregunta(id);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Crea o actualiza grabarUsuarioPropietarioRespuesta
	 *
	 * @param upm
	 * @return Identificador
	 * @throws DelegateException
	 */
	public long grabarUsuarioPropietarioRespuesta(final UsuarioPropietarioRespuesta upm) throws DelegateException {
		try {
			return this.getFacade().grabarUsuarioPropietarioRespuesta(upm);

		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Obtener todos los usuarios que han seleccionado una respuesta
	 *
	 * @param idRespuesta
	 * @return una Lista
	 * @throws DelegateException
	 */
	public List<?> obtenerUsuariosRespuesta(final Long idRespuesta) throws DelegateException {
		try {
			return this.getFacade().obtenerUsuariosRespuesta(idRespuesta);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Obtener el número de votos por respuesta en función de un grupo de usuarios
	 *
	 * @param condicioUsu
	 * @return hastable key: idRespuesta value:numero respuestas
	 * @throws DelegateException
	 */
	public Hashtable<?, ?> obtenerNumVotosByResp(final Collection<?> condicioUsu) throws DelegateException {
		try {
			return this.getFacade().obtenerNumVotosByResp(condicioUsu);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public List<?> obtenerUsuariosEncuesta(final Long idEncuesta) throws DelegateException {
		try {
			return this.getFacade().obtenerUsuariosEncuesta(idEncuesta);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public List<?> obtenerRespuestasDeUsuario(final Long idUsuario) throws DelegateException {
		try {
			return this.getFacade().obtenerRespuestasDeUsuario(idUsuario);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public RespuestaDato obtenerRespuestaDato(final Long idRespuesta, final Long idUsuario) throws DelegateException {
		try {
			return this.getFacade().obtenerRespuestaDato(idRespuesta, idUsuario);
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

	/**
	 * Obtiene las encuestas de un microsite
	 */
	public List<Encuesta> obtenerEncuestasByMicrositeId(final Long idMicrosite) throws DelegateException {
		try {
			return this.getFacade().obtenerEncuestasByMicrositeId(idMicrosite);
		} catch (final RemoteException e) {
			throw new DelegateException(e);
		}

	}

	/* ========================================================= */
	/* ======================== REFERENCIA AL FACADE ========== */
	/* ========================================================= */

	private Handle facadeHandle;

	private EncuestaFacade getFacade() throws RemoteException {
		return (EncuestaFacade) this.facadeHandle.getEJBObject();
	}

	protected EncuestaDelegate() throws DelegateException {
		try {
			final EncuestaFacadeHome home = EncuestaFacadeUtil.getHome();
			final EncuestaFacade remote = home.create();
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