package es.caib.gusite.front.view;

import java.util.Collection;
import java.util.List;

import es.caib.gusite.front.general.Version;
import es.caib.gusite.front.general.bean.MenuFront;
import es.caib.gusite.front.general.bean.Pardato;
import es.caib.gusite.front.general.bean.Tridato;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.plugins.organigrama.UnidadData;

/**
 * Plantilla base que monta la estructura general del resto de páginas web de microsite.
 * 
 * @author at4.net
 *
 */
@TemplateView(TemplateView.LAYOUT)
public class LayoutView {

	private Microsite microsite;
	private String idioma;

	private String idsite;

	private String micrositeTitulo;
	private String micrositeDescription;
	private String micrositeKeywords;
	private String micrositeAnalytics;

	private List<Pardato> listaIdiomas;
	private String css;

	private String direccion;

	private List<Pardato> listaPie;

	private List<Tridato> listaCabecera;

	private Idioma lang;

	private List<MenuFront> menu;

	private Collection<UnidadData> uos;

	private String servicio;

	private String homeTmpCampanya;
	
	private Version version;

	/**
	 *  
	 */
	private static final long serialVersionUID = 1L;

	public void setLang(Idioma lang) {
		this.lang = lang;
	}

	public Idioma getLang() {
		return this.lang;
	}

	public void setMicrosite(Microsite microsite) {
		this.microsite = microsite;
	}

	/**
	 * Datos completos del site 
	 * @return
	 */
	@Variable("MVS_microsite")
	public Microsite getMicrosite() {
		return microsite;
	}

	public void setIdioma(String idioma) {
		this.idioma = idioma;
	}

	/**
	 * String ISO del idioma actual (ca, en, es, ...)
	 * @return
	 */
	@Variable("MVS_idioma")
	public String getIdioma() {
		return idioma;
	}

	public void setIdsite(String txnewidsite) {
		this.idsite = txnewidsite;
	}

	/**
	 * Clave primaria del site
	 * @return
	 */
	@Variable("MVS_idsite")
	public String getIdsite() {
		return this.idsite;
	}

	/**
	 * Nombre del site en el idioma actual
	 * @return
	 */
	@Variable("MVS_micrositetitulo")
	public String getMicrositeTitulo() {
		return micrositeTitulo;
	}

	public void setMicrositeTitulo(String micrositeTitulo) {
		this.micrositeTitulo = micrositeTitulo;
	}

	/**
	 * Meta description del site
	 * @return
	 */
	@Variable("MVS_micrositedescription")
	public String getMicrositeDescription() {
		return micrositeDescription;
	}

	public void setMicrositeDescription(String micrositeDescription) {
		this.micrositeDescription = micrositeDescription;
	}

	/**
	 * Meta keywords del site
	 * @return
	 */
	@Variable("MVS_micrositekeywords")
	public String getMicrositeKeywords() {
		return micrositeKeywords;
	}

	public void setMicrositeKeywords(String micrositeKeywords) {
		this.micrositeKeywords = micrositeKeywords;
	}

	/**
	 * Identificador de sitio en google analytics
	 * @return
	 */
	@Variable("MVS_micrositeanalytics")
	public String getMicrositeAnalytics() {
		return micrositeAnalytics;
	}

	public void setMicrositeAnalytics(String micrositeAnalytics) {
		this.micrositeAnalytics = micrositeAnalytics;
	}

	
	public void setListaIdiomas(List<Pardato> listaIdiomas) {
		this.listaIdiomas = listaIdiomas;
	}

	/**
	 * Lista de idiomas en los que está disponible el microsite, donde cada elemento es un Pardato(iso, nombre)
	 * @return
	 */
	@Variable("MVS_listaidiomas")
	public List<Pardato> getListaIdiomas() {
		return listaIdiomas;
	}

	public void setCss(String css) {
		this.css = css;
	}

	/**
	 * Tags css generados para la vista actual
	 * @return
	 */
	@Variable("MVS_css")
	public String getCss() {
		return css;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	/**
	 * Dirección generada para los datos del pie a partir de los datos del organigrama
	 * TODO: ¿no debería ser MVS_direccion?
	 * 
	 */
	@Variable("direccion")
	public String getDireccion() {
		return direccion;
	}

	public void setListaPie(List<Pardato> listaPie) {
		this.listaPie = listaPie;
	}

	/**
	 * TODO: revisar el uso de esta variable, sólo aparece en mapa.html
	 * @deprecated
	 */
	@Variable("MVS_listapie")
	public List<Pardato> getListaPie() {
		return listaPie;
	}

	
	public void setListaCabecera(List<Tridato> listaCabecera) {
		this.listaCabecera = listaCabecera;
	}

	/**
	 * Lista de opciones de menú para el pie. 
	 * Nota: en la plantilla genérica v4/v5, se usa en realidad en el pie
	 * @return
	 */
	@Variable("MVS_listacabecera")
	public List<Tridato> getListaCabecera() {
		return listaCabecera;
	}

	public void setMenu(List<MenuFront> menu) {
		this.menu = menu;
	}

	/**
	 * Menú de navegación del sitio web
	 * @return
	 */
	@Variable("MVS_menu")
	public List<MenuFront> getMenu() {
		return menu;
	}

	public void setUos(Collection<UnidadData> uos) {

		this.uos = uos;

	}

	/**
	 * Lista de unidades orgánicas, proporcionadas por el organigrama
	 * @return
	 */
	@Variable("MVS2_uos")
	public Collection<UnidadData> getUos() {
		return this.uos;
	}

	public void setServicio(String servicio) {
		this.servicio = servicio;
	}

	/**
	 * Identificador del tipo de servicio para la vista actual
	 * @return
	 */
	@Variable("MVS_servicio")
	public String getServicio() {
		return this.servicio;
	}

	public void setHomeTmpCampanya(String tagHtmlTmpCampaya) {

		this.homeTmpCampanya = tagHtmlTmpCampaya;

	}

	/**
	 * String preparado para insertar en un html, contiene el trozo de html que muestra la imagen de la campaña
	 * @return
	 */
	@Variable("MVS_home_tmp_campanya")
	public String getHomeTmpCampanya() {

		return this.homeTmpCampanya;

	}

	public void setVersion(Version version) {
		this.version = version;
	}

	/**
	 * Datos de la versión de gusite en ejecución
	 * @return
	 */
	@Variable("MVS_version")
	public Version getVersion() {
		return version;
	}

}
