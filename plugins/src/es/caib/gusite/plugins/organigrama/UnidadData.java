package es.caib.gusite.plugins.organigrama;

import java.io.Serializable;

/**
 * Datos completos de unidades
 * @author agarcia
 *
 */
public class UnidadData extends UnidadListData {

	private String direccion;
	private String codigoPostal;
	private String poblacion;
	private String urlPlano;
	private String telefono;
	private String fax;
	
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public String getUrlPlano() {
		return urlPlano;
	}
	public void setUrlPlano(String urlPlano) {
		this.urlPlano = urlPlano;
	}
	public String getCodigoPostal() {
		return codigoPostal;
	}
	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}
	public String getPoblacion() {
		return poblacion;
	}
	public void setPoblacion(String poblacion) {
		this.poblacion = poblacion;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}

	
}
