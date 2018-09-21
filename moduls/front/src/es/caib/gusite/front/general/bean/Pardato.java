package es.caib.gusite.front.general.bean;

import java.io.Serializable;

/**
 * Bean de uso genérico que guarda el par 'clave,valor'.
 * 
 * @author Indra
 * 
 */
public class Pardato implements Serializable {

	private static final long serialVersionUID = 7567193458667662802L;
	private String id = "";
	private String key = "";
	private String value = "";

	public Pardato() {
	}

	public Pardato(String clave, String valor) {
		this.key = clave;
		this.value = valor;
	}
	
	/**
	 * constructor con campo adicional para solventar problema con los formularios de contacto
	 * normalmente no se usa
	 * @param clave
	 * @param valor
	 * @param id
	 */
	public Pardato(String clave, String valor, String id) {
		this.id = id;
		this.key = clave;
		this.value = valor;
	}

	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Campo adicional para solventar problema con los formularios de contacto
	 * @return
	 */
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
