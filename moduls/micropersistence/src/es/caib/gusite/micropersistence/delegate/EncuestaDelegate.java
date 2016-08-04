package es.caib.gusite.micropersistence.delegate;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.Handle;
import javax.naming.NamingException;

import es.caib.gusite.lucene.model.ModelFilterObject;
import es.caib.gusite.micromodel.Encuesta;
import es.caib.gusite.micromodel.Pregunta;
import es.caib.gusite.micromodel.Respuesta;
import es.caib.gusite.micromodel.RespuestaDato;
import es.caib.gusite.micromodel.UsuarioPropietarioRespuesta;
import es.caib.gusite.micropersistence.intf.EncuestaFacade;
import es.caib.gusite.micropersistence.intf.EncuestaFacadeHome;
import es.caib.gusite.micropersistence.util.EncuestaFacadeUtil;

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

	public void initra(Long id, String idiomapasado) throws DelegateException {
		try {
			this.getFacade().initra(id, idiomapasado);
		} catch (RemoteException e) {
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
	public Long grabarEncuesta(Encuesta encuesta) throws DelegateException {
		try {
			return this.getFacade().grabarEncuesta(encuesta);
		} catch (RemoteException e) {
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
	public Encuesta obtenerEncuesta(Long id) throws DelegateException {
		try {
			return this.getFacade().obtenerEncuesta(id);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Obtiene un encuesta a partir de la uri
	 * 
	 * @return Encuesta
	 * @throws DelegateException
	 */
	public Encuesta obtenerEncuestaDesdeUri(String idioma, String uri, String site)
			throws DelegateException {
		try {
			return this.getFacade().obtenerEncuestaDesdeUri(idioma, uri, site);
		} catch (RemoteException e) {
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
		} catch (RemoteException e) {
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
	public List<?> listarEncuestasrec(String idiomapasado)
			throws DelegateException {
		try {
			return this.getFacade().listarEncuestasrec(idiomapasado);
		} catch (RemoteException e) {
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
	public void borrarEncuesta(Long id) throws DelegateException {
		try {
			this.getFacade().borrarEncuesta(id);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Crea o actualiza una pregunta
	 * 
	 * @param pre
	 * @throws DelegateException
	 */
	public void grabarPregunta(Pregunta pre) throws DelegateException {
		try {
			this.getFacade().grabarPregunta(pre);
		} catch (RemoteException e) {
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
	public Pregunta obtenerPregunta(Long id) throws DelegateException {
		try {
			return this.getFacade().obtenerPregunta(id);
		} catch (RemoteException e) {
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
	public List<?> listarPreguntas(Long id) throws DelegateException {
		try {
			return this.getFacade().listarPreguntas(id);
		} catch (RemoteException e) {
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
	public List<?> listarPreguntasOrdAsc(Long id) throws DelegateException {
		try {
			return this.getFacade().listarPreguntasOrdAsc(id);
		} catch (RemoteException e) {
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
	public void eliminarPreguntas(String[] preguntas, Long encuesta_id)
			throws DelegateException {
		try {
			this.getFacade().eliminarPreguntas(preguntas, encuesta_id);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Crea o actualiza una respuesta
	 * 
	 * @param res
	 * @throws DelegateException
	 */
	public void grabarRespuesta(Respuesta res) throws DelegateException {
		try {
			this.getFacade().grabarRespuesta(res);
		} catch (RemoteException e) {
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
	public Respuesta obtenerRespuesta(Long id) throws DelegateException {
		try {
			return this.getFacade().obtenerRespuesta(id);
		} catch (RemoteException e) {
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
	public Hashtable<?, ?> listarRespuestasDato(Long idEncuesta, Long idUsuario)
			throws DelegateException {
		try {
			return this.getFacade().listarRespuestasDato(idEncuesta, idUsuario);
		} catch (RemoteException e) {
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
	public List<?> listarRespuestas(Long id) throws DelegateException {
		try {
			return this.getFacade().listarRespuestas(id);
		} catch (RemoteException e) {
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
	public void eliminarRespuestas(String[] respuestas, Long pregunta_id)
			throws DelegateException {
		try {
			this.getFacade().eliminarRespuestas(respuestas, pregunta_id);
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

	public void sumarRespuesta(Long id) throws DelegateException {
		try {
			this.getFacade().sumarRespuesta(id);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public void sumarPregunta(Long id) throws DelegateException {
		try {
			this.getFacade().sumarPregunta(id);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public void indexInsertaEncuesta(Encuesta enc, ModelFilterObject filter)
			throws DelegateException {
		try {
			this.getFacade().indexInsertaEncuesta(enc, filter);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public void indexBorraEncuesta(Long id) throws DelegateException {
		try {
			this.getFacade().indexBorraEncuesta(id);
		} catch (RemoteException e) {
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
	public long grabarUsuarioPropietarioRespuesta(
			UsuarioPropietarioRespuesta upm) throws DelegateException {
		try {
			return this.getFacade().grabarUsuarioPropietarioRespuesta(upm);

		} catch (RemoteException e) {
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
	public List<?> obtenerUsuariosRespuesta(Long idRespuesta)
			throws DelegateException {
		try {
			return this.getFacade().obtenerUsuariosRespuesta(idRespuesta);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	/**
	 * Obtener el número de votos por respuesta en función de un grupo de
	 * usuarios
	 * 
	 * @param condicioUsu
	 * @return hastable key: idRespuesta value:numero respuestas
	 * @throws DelegateException
	 */
	public Hashtable<?, ?> obtenerNumVotosByResp(Collection<?> condicioUsu)
			throws DelegateException {
		try {
			return this.getFacade().obtenerNumVotosByResp(condicioUsu);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public List<?> obtenerUsuariosEncuesta(Long idEncuesta)
			throws DelegateException {
		try {
			return this.getFacade().obtenerUsuariosEncuesta(idEncuesta);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public List<?> obtenerRespuestasDeUsuario(Long idUsuario)
			throws DelegateException {
		try {
			return this.getFacade().obtenerRespuestasDeUsuario(idUsuario);
		} catch (RemoteException e) {
			throw new DelegateException(e);
		}
	}

	public RespuestaDato obtenerRespuestaDato(Long idRespuesta, Long idUsuario)
			throws DelegateException {
		try {
			return this.getFacade()
					.obtenerRespuestaDato(idRespuesta, idUsuario);
		} catch (RemoteException e) {
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
			EncuestaFacadeHome home = EncuestaFacadeUtil.getHome();
			EncuestaFacade remote = home.create();
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