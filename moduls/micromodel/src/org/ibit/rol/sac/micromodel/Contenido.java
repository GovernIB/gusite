package org.ibit.rol.sac.micromodel;

import java.util.Date;

/**
 * Clase Contenido. Bean que define un Contenido. 
 * Modela la tabla de BBDD GUS_CONTEN
 * @author Indra
 */
public class Contenido extends Traducible  implements Indexable {
	

	private static final long serialVersionUID = 4688044820582237768L;
	
	private Long id;
	private Long idmenu;
	private Date fcaducidad;
	private Date fpublicacion;
	private int orden;
	private String visible;
	private Archivo imagenmenu;
	
	private String urlExterna="";
	
	public Date getFcaducidad() {
		return fcaducidad;
	}

	public void setFcaducidad(Date fcaducidad) {
		this.fcaducidad = fcaducidad;
	}

	public Date getFpublicacion() {
		return fpublicacion;
	}

	public void setFpublicacion(Date fpublicacion) {
		this.fpublicacion = fpublicacion;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdmenu() {
		return idmenu;
	}

	public void setIdmenu(Long idmenu) {
		this.idmenu = idmenu;
	}

	public Archivo getImagenmenu() {
		return imagenmenu;
	}

	public void setImagenmenu(Archivo imagenmenu) {
		this.imagenmenu = imagenmenu;
	}

	public int getOrden() {
		return orden;
	}

	public void setOrden(int orden) {
		this.orden = orden;
	}

	public String getVisible() {
		return visible;
	}

	public void setVisible(String visible) {
		this.visible = visible;
	}  

	
	public void addTraduccionMap(String lang, TraduccionContenido traduccion) {
        setTraduccion(lang, traduccion);
    }

	public String getUrlExterna(){
		return urlExterna;
	}
	public void setUrlExterna(String urlExterna){
		this.urlExterna=urlExterna;
	}
	
}