package org.ibit.rol.sac.micromodel;

/**
 * Clase TraduccionBanner. Encapsula los datos  que pueden tener valor en diferentes idiomas del objeto Banner.
 * @author Indra
 */
public class TraduccionBanner  implements Traduccion {

	private static final long serialVersionUID = -7510238320501258443L;
	private String titulo;
    private String url;
    private String alt;
    private Archivo imagen;
    
	public String getAlt() {
		return alt;
	}
	public void setAlt(String alt) {
		this.alt = alt;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Archivo getImagen() {
		return imagen;
	}
	public void setImagen(Archivo imagen) {
		this.imagen = imagen;
	}     
}