package es.caib.gusite.front.view;

import es.caib.gusite.micromodel.Noticia;

/**
 * Ficha de noticia
 * @author at4.net
 *
 */
@TemplateView(TemplateView.NOTICIA_FICHA)
public class NoticiaView extends PageView {

	private String menuContenidoNoticia;
	private Noticia noticia;
	private String anchoImg;
	private String tipoNoticia;
	private boolean esForzarMapa = false;

	public void setMenuContenidoNoticia(String mcont) {
		this.menuContenidoNoticia = mcont;

	}

	/**
	 * Contenido de menú a resaltar
	 * @return
	 */
	@Variable("MVS_menu_cont_notic")
	public String getMenuContenidoNoticia() {
		return menuContenidoNoticia;
	}

	public void setNoticia(Noticia noticia) {
		this.noticia = noticia;
	}

	public void setAnchoImg(String ancho) {
		this.anchoImg = ancho;

	}

	public void setTipoNoticia(String nombre) {
		this.tipoNoticia = nombre;
	}

	/**
	 * Noticia a mostrar
	 * @return
	 */
	@Variable("MVS_noticia")
	public Noticia getNoticia() {
		return noticia;
	}

	/**
	 * Ancho de la foto
	 * @return
	 */
	@Variable("MVS_anchoImg")
	public String getAnchoImg() {
		return anchoImg;
	}

	/**
	 * Título del tipo de noticia 
	 * @return
	 */
	@Variable("MVS_tiponoticia")
	public String getTipoNoticia() {
		return tipoNoticia;
	}

	
	
	/**
	 * indica si se intenta forzar la visualización del mapa 
	 * @return
	 */
	@Variable("MVS_forzarmapa")	
	public boolean getForzarMapa() {
		return esForzarMapa;
	}

	public void setForzarMapa(boolean forzarMapa) {
		this.esForzarMapa = forzarMapa;
	}

}
