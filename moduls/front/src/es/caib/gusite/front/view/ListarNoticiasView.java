package es.caib.gusite.front.view;

import java.util.Collection;
import java.util.List;

import es.caib.gusite.micromodel.Noticia;
import es.caib.gusite.micromodel.Tipo;

/**
 * Listado paginado de noticias, documento y enlaces
 * @author at4.net
 *
 */
@TemplateView(	value=TemplateView.NOTICIA_LISTAR_NOTICIAS, 
				extraViews={TemplateView.NOTICIA_LISTAR_DOCUMENTOS, 
							TemplateView.NOTICIA_LISTAR_LINKS,
							TemplateView.NOTICIA_LISTAR_NOTICIAS_EXTERNAS} )
public class ListarNoticiasView extends ListPageView {

	private Long claseElementoId;
	private String mchtml;
	private Collection<Noticia> listado;
	private String tipoListado;
	private Tipo claseElemento;
	private String busqueda;
	private List<String> listadoAnyos;
	private int anyo;
	private String anchoFoto;
	private String altoFoto;
	private String menuContenidoNoticia;

	public void setClaseElementoId(Long id) {
		this.claseElementoId = id;
	}

	public void setMchtml(String mchtml) {
		this.mchtml = mchtml;

	}

	public void setListado(Collection<Noticia> resultados) {
		this.listado = resultados;

	}

	public void setTipoListado(String tipo) {
		this.tipoListado = tipo;

	}

	public void setClaseElemento(Tipo tipo) {
		this.claseElemento = tipo;

	}

	public void setBusqueda(String busqueda) {
		this.busqueda = busqueda;

	}

	public void setListadoAnyos(List<String> listaAnyos) {
		this.listadoAnyos = listaAnyos;

	}

	public void setAnyo(int anyo) {
		this.anyo = anyo;

	}

	/**
	 * Id de la clase de elemento a mostrar
	 * @return
	 */
	@Variable("MVS_claseelemento_id")
	public Long getClaseElementoId() {
		return claseElementoId;
	}

	/**
	 * HTML a incluir en el caso de noticias externas
	 * @return
	 */
	@Variable("MVS_mchtml")
	public String getMchtml() {
		return mchtml;
	}

	/**
	 * Listado de noticias a mostrar
	 * @return
	 */
	@Variable("MVS_listado")
	public Collection<Noticia> getListado() {
		return listado;
	}

	/**
	 * Nombre del tipo de listado que mostrará
	 * @return
	 */
	@Variable("MVS_tipolistado")
	public String getTipoListado() {
		return tipoListado;
	}

	/**
	 * Tipo de listado que mostrará
	 * @return
	 */
	@Variable("MVS_claseelemento")
	public Tipo getClaseElemento() {
		return claseElemento;
	}

	/**
	 * Búsqueda realizada en el filtro
	 * @return
	 */
	@Variable("MVS_busqueda")
	public String getBusqueda() {
		return busqueda;
	}

	/**
	 * Listado de años
	 * @return
	 */
	@Variable("MVS_listadoanyos")
	public List<String> getListadoAnyos() {
		return listadoAnyos;
	}

	/**
	 * Año actual
	 * @return
	 */
	@Variable("MVS_anyo")
	public int getAnyo() {
		return anyo;
	}

	public void setAnchoFoto(String string) {
		this.anchoFoto = string;
	}

	public void setAltoFoto(String string) {
		this.altoFoto = string;
	}

	/**
	 * Ancho de foto (sólo para galería de fotos)
	 * @param string
	 */
	@Variable("MVS_anchoFoto")
	public String getAnchoFoto() {
		return anchoFoto;
	}

	/**
	 * Alto de foto en el listado (sólo para galería de fotos)
	 * @param string
	 */
	@Variable("MVS_altoFoto")
	public String getAltoFoto() {
		return altoFoto;
	}

	public void setMenuContenidoNoticia(String mcont) {
		this.menuContenidoNoticia = mcont;

	}

	/**
	 * Opción de menú que hay que resaltar
	 * @return
	 */
	@Variable("MVS_menu_cont_notic")
	public String getMenuContenidoNoticia() {
		return menuContenidoNoticia;
	}

}
