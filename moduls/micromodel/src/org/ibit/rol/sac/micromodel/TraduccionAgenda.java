package org.ibit.rol.sac.micromodel;


/**
 * Clase TraduccionAgenda. Encapsula los datos  que pueden tener valor en diferentes idiomas del objeto Agenda.
 * @author Indra
 */
public class TraduccionAgenda implements Traduccion{

	private static final long serialVersionUID = -8998196005824126269L;
	private String titulo;
	private String descripcion;
	private Archivo documento;
	private Archivo imagen;
	private String url;
	private String urlnom;
	
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Archivo getDocumento() {
		return documento;
	}
	public void setDocumento(Archivo documento) {
		this.documento = documento;
	}
	public Archivo getImagen() {
		return imagen;
	}
	public void setImagen(Archivo imagen) {
		this.imagen = imagen;
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
	public String getUrlnom()
	{
	    return urlnom;
	}	
	 public void setUrlnom(String urlnom)
	{
	    this.urlnom = urlnom;
	}    
    
}