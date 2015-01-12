package org.ibit.rol.sac.micromodel;

import java.util.Date;


/**
 * Clase Banner. Bean que define un Banner. 
 * Modela la tabla de BBDD GUS_BANNER
 * @author Indra
 */
public class Banner extends Traducible{
	
	private static final long serialVersionUID = 8298074195619244750L;
	
	private Long id;
	private Date fcaducidad;
	private Date fpublicacion;
	private String visible;
	private Long idmicrosite;
	
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
    public Long getIdmicrosite() {
        return idmicrosite;
    }
    public void setIdmicrosite(Long idmicrosite) {
        this.idmicrosite = idmicrosite;
    }
	public String getVisible() {
		return visible;
	}
	public void setVisible(String visible) {
		this.visible = visible;
	}
	public void addTraduccionMap(String lang, TraduccionBanner traduccion) {
        setTraduccion(lang, traduccion);
    }  

}