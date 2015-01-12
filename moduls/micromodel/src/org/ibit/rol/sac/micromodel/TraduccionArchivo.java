package org.ibit.rol.sac.micromodel;

/**
 * Clase TraduccionArchivo. Encapsula los datos  que pueden tener valor en diferentes idiomas del objeto Archivo.
 * @author Indra
 */
public class TraduccionArchivo  implements Traduccion {

	private static final long serialVersionUID = 1965050279236645830L;
	private String titulo;
    private String url;
    private String texto;
    
	public String getTexto() {
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
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
    
}