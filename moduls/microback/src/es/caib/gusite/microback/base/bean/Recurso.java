package es.caib.gusite.microback.base.bean;

import java.io.Serializable;

/**
 * Bean para definir un recurso
 * 
 *@author Indra
 * 
 */
public class Recurso implements Serializable {

	private static final long serialVersionUID = 1911090456580602625L;
	private String tipo="";
	private String titulo="";
	private String url="";
	private String urlnom="";
	private String head="0";
	
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
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
	public String getUrlnom() {
		return urlnom;
	}	
	public void setUrlnom(String urlnom) {
		this.urlnom = urlnom;
	}	
	public String getHead() {
		return head;
	}
	public void setHead(String head) {
		this.head = head;
	}
	
}
