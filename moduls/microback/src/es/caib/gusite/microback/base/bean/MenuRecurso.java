package es.caib.gusite.microback.base.bean;

import java.util.ArrayList;

/**
 * Bean para definir un recurso menu
 * 
 *@author Indra
 * 
 */
public class MenuRecurso {
	private String urlnom ="";	
	private String tipo="";
	private String titulo="";
	private String url="";
	private String head="0";
	private ArrayList<Object> listacosas = new ArrayList<Object>();
	
	public String getHead() {
		return head;
	}
	public void setHead(String head) {
		this.head = head;
	}
	public ArrayList<Object> getListacosas() {
		return listacosas;
	}
	public void setListacosas(ArrayList<Object> listacosas) {
		this.listacosas = listacosas;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getUrlnom() {
		return urlnom;
	}
	public void setUrlnom(String urlnom) {
		this.urlnom = urlnom;
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
