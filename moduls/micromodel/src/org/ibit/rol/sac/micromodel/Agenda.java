
package org.ibit.rol.sac.micromodel;

import java.util.Date;

/**
 * Clase Agenda. Bean que define una Agenda.
 * Modela la tabla de BBDD GUS_AGENDA
 * @author Indra
 */
public class Agenda extends Traducible implements Indexable{


	private static final long serialVersionUID = 7222009737907543946L;
	
	private Long id;
	private String organizador;
	private Date finicio;
	private Date ffin;
	private String visible;
	private Long idmicrosite;
	private Actividadagenda actividad;
	
	public Actividadagenda getActividad() {
		return actividad;
	}
	public void setActividad(Actividadagenda actividad) {
		this.actividad = actividad;
	}
	public Date getFfin() {
		return ffin;
	}
	public void setFfin(Date fecha) {
		this.ffin=fecha;
	}
	public Date getFinicio() {
		return finicio;
	}
	public void setFinicio(Date finicio) {
		this.finicio = finicio;
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
	public String getOrganizador() {
		return organizador;
	}
	public void setOrganizador(String organizador) {
		this.organizador = organizador;
	}
	public String getVisible() {
		return visible;
	}
	public void setVisible(String visible) {
		this.visible = visible;
	}
    
	public void addTraduccionMap(String lang, TraduccionAgenda traduccion) {
        setTraduccion(lang, traduccion);
    }  

}