package org.ibit.rol.sac.microfront.procedimiento.bean;

import java.util.Date;

/**
 * Clase Procedimiento 2
 * @author Indra
 *
 */
public class Procedimiento2 {

	private String nombre;
	private Long id;
	private Date fechaPublicacion;
	private String nombreua;
	
	public Date getFechaPublicacion() {
		return fechaPublicacion;
	}
	public void setFechaPublicacion(Date fechaPublicacion) {
		this.fechaPublicacion = fechaPublicacion;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getNombreua() {
		return nombreua;
	}
	public void setNombreua(String nombreua) {
		this.nombreua = nombreua;
	}
	
}
