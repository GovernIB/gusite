package es.caib.gusite.plugins.organigrama;

import java.io.Serializable;

public class UnidadListData {

	private Serializable idUnidad;
	private Serializable idUnidadPadre;
	private String nombre;
	private String abreviatura;
	private String url;
	private String dominio;

	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public void setIdUnidadPadre(Serializable padre) {
		this.idUnidadPadre = padre;
		
	}
	
	public Serializable getId() {
		return getIdUnidad();
	}
	public Serializable getIdUnidad() {
		return idUnidad;
	}
	public void setIdUnidad(Serializable idUnidad) {
		this.idUnidad = idUnidad;
	}
	public Serializable getIdUnidadPadre() {
		return idUnidadPadre;
	}
	public String getAbreviatura() {
		return abreviatura;
	}
	public void setAbreviatura(String abreviatura) {
		this.abreviatura = abreviatura;
	}
	public String getDominio() {
		return dominio;
	}
	public void setDominio(String dominio) {
		this.dominio = dominio;
	}
}
