package org.ibit.rol.sac.micropersistence.delegate;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import net.sf.hibernate.HibernateException;

import org.ibit.lucene.indra.model.ModelFilterObject;
import org.ibit.rol.sac.micromodel.Noticia;

public interface NoticiaServiceItf {

	/* (non-Javadoc)
	 * @see org.ibit.rol.sac.micropersistence.delegate.NotificaServiceItf#init()
	 */
	public void init() throws Exception;

	/* (non-Javadoc)
	 * @see org.ibit.rol.sac.micropersistence.delegate.NotificaServiceItf#init(java.lang.Long)
	 */
	public void init(Long id) throws Exception;

	/* (non-Javadoc)
	 * @see org.ibit.rol.sac.micropersistence.delegate.NotificaServiceItf#grabarNoticia(org.ibit.rol.sac.micromodel.Noticia)
	 */
	public Long grabarNoticia(Noticia noticia) throws Exception;

	/* (non-Javadoc)
	 * @see org.ibit.rol.sac.micropersistence.delegate.NotificaServiceItf#obtenerNoticia(java.lang.Long)
	 */
	public Noticia obtenerNoticia(Long id) throws Exception;

	/* (non-Javadoc)
	 * @see org.ibit.rol.sac.micropersistence.delegate.NotificaServiceItf#clonarNoticia(java.lang.Long)
	 */
	public Long clonarNoticia(Long id) throws Exception;

	/* (non-Javadoc)
	 * @see org.ibit.rol.sac.micropersistence.delegate.NotificaServiceItf#obtenerNoticiaThin(java.lang.Long, java.lang.String)
	 */
	public Noticia obtenerNoticiaThin(Long id, String idioma)
			throws Exception;

	/* (non-Javadoc)
	 * @see org.ibit.rol.sac.micropersistence.delegate.NotificaServiceItf#listarNoticias()
	 */
	public List<?> listarNoticias() throws Exception;

	/* (non-Javadoc)
	 * @see org.ibit.rol.sac.micropersistence.delegate.NotificaServiceItf#listarNoticiasThin(java.lang.String)
	 */
	public List<?> listarNoticiasThin(String idioma) throws Exception;

	public List<?> buscarElementos(BuscarElementosParameter parameterObject) throws Exception;

	/* (non-Javadoc)
	 * @see org.ibit.rol.sac.micropersistence.delegate.NotificaServiceItf#buscarElementos(java.util.Map, java.util.Map, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Deprecated
	public List<?> buscarElementos(Map<?, ?> parametros, Map<?, ?> traduccion, String idmicrosite, String idtipo, String idioma) throws Exception;

	
	
	/* (non-Javadoc)
	 * @see org.ibit.rol.sac.micropersistence.delegate.NotificaServiceItf#buscarElementosLuc(java.lang.String, java.lang.String, java.lang.String, java.lang.String, boolean)
	 */
	public List<?> buscarElementosLuc(String micro, String idi, String idlista,
			String cadena, boolean sugerir) throws Exception;

	/* (non-Javadoc)
	 * @see org.ibit.rol.sac.micropersistence.delegate.NotificaServiceItf#borrarNoticia(java.lang.Long)
	 */
	public void borrarNoticia(Long id) throws Exception;

	/* (non-Javadoc)
	 * @see org.ibit.rol.sac.micropersistence.delegate.NotificaServiceItf#getParametros()
	 */
	public Hashtable<?, ?> getParametros() throws Exception;

	/* (non-Javadoc)
	 * @see org.ibit.rol.sac.micropersistence.delegate.NotificaServiceItf#parametrosCons()
	 */
	public void parametrosCons() throws Exception;

	/* (non-Javadoc)
	 * @see org.ibit.rol.sac.micropersistence.delegate.NotificaServiceItf#getPagina()
	 */
	public int getPagina() throws Exception;

	/* (non-Javadoc)
	 * @see org.ibit.rol.sac.micropersistence.delegate.NotificaServiceItf#setPagina(int)
	 */
	public void setPagina(int pagina) throws Exception;

	/* (non-Javadoc)
	 * @see org.ibit.rol.sac.micropersistence.delegate.NotificaServiceItf#setOrderby(java.lang.String)
	 */
	public void setOrderby(String orderby) throws Exception;

	/* (non-Javadoc)
	 * @see org.ibit.rol.sac.micropersistence.delegate.NotificaServiceItf#setOrderby2(java.lang.String)
	 */
	public void setOrderby2(String orderby) throws Exception;

	/* (non-Javadoc)
	 * @see org.ibit.rol.sac.micropersistence.delegate.NotificaServiceItf#getValorBD(java.lang.String)
	 */
	public String getValorBD(String valor) throws Exception;

	/* (non-Javadoc)
	 * @see org.ibit.rol.sac.micropersistence.delegate.NotificaServiceItf#setFiltro(java.lang.String)
	 */
	public void setFiltro(String valor) throws Exception;

	/* (non-Javadoc)
	 * @see org.ibit.rol.sac.micropersistence.delegate.NotificaServiceItf#getWhere()
	 */
	public String getWhere() throws Exception;

	/* (non-Javadoc)
	 * @see org.ibit.rol.sac.micropersistence.delegate.NotificaServiceItf#setWhere(java.lang.String)
	 */
	public void setWhere(String valor) throws Exception;

	/* (non-Javadoc)
	 * @see org.ibit.rol.sac.micropersistence.delegate.NotificaServiceItf#getTampagina()
	 */
	public int getTampagina() throws Exception;

	/* (non-Javadoc)
	 * @see org.ibit.rol.sac.micropersistence.delegate.NotificaServiceItf#setTampagina(int)
	 */
	public void setTampagina(int tampagina) throws Exception;

	/* (non-Javadoc)
	 * @see org.ibit.rol.sac.micropersistence.delegate.NotificaServiceItf#checkSite(java.lang.Long, java.lang.Long)
	 */
	public boolean checkSite(Long site, Long id) throws Exception;

	/* (non-Javadoc)
	 * @see org.ibit.rol.sac.micropersistence.delegate.NotificaServiceItf#indexInsertaNoticia(org.ibit.rol.sac.micromodel.Noticia, org.ibit.lucene.indra.model.ModelFilterObject)
	 */
	public void indexInsertaNoticia(Noticia noti, ModelFilterObject filter)
			throws Exception;

	/* (non-Javadoc)
	 * @see org.ibit.rol.sac.micropersistence.delegate.NotificaServiceItf#indexBorraNoticia(java.lang.Long)
	 */
	public void indexBorraNoticia(Long id) throws Exception;

	public List<String> listarAnyos() throws Exception;

}