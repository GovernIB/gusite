package org.ibit.rol.sac.micromodel;

/**
 * Clase TraduccionContenido. Encapsula los datos  que pueden tener valor en diferentes idiomas del objeto Contenido.
 * @author Indra
 */
public class TraduccionContenido  implements Traduccion {

	private static final long serialVersionUID = -3199527315588330827L;
	private String titulo;
    private String url;
    private String texto;
    private String txbeta;
    
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
	public String getTxbeta() {
		return txbeta;
	}
	public void setTxbeta(String txbeta) {
		this.txbeta = txbeta;
	} 
}