package org.ibit.rol.sac.microfront.base.bean;

import java.io.Serializable;

/**
 * 
 * Bean de uso genérico que guarda el tres valores 'clave,valor1,valor2'.
 * @author vroca
 */

public class Tridato implements Serializable {

	private static final long serialVersionUID = -3402141231256381314L;
	private String key="";
	private String value1="";
	private String value2="";
	
	
	public Tridato() {	
	}
	
	public Tridato(String clave, String valor1) {
		this(clave,valor1,null);
	}	
	
	public Tridato(String clave, String valor1, String valor2) {
		this.key=clave;
		this.value1=valor1;
		this.value2=valor2;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue1() {
		return value1;
	}

	public void setValue1(String value1) {
		this.value1 = value1;
	}

	public String getValue2() {
		return value2;
	}

	public void setValue2(String value2) {
		this.value2 = value2;
	}
	
}
