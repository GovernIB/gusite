package org.ibit.rol.sac.micromodel;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Clase Encuesta. Bean que define una Encuesta. 
 * Modela la tabla de BBDD GUS_ENCUST
 * @author Indra
 */
public class Encuesta extends Traducible  implements Indexable{

	private static final long serialVersionUID = 2356576663603622282L;
	
	private Long id;
    private Long idmicrosite;
    private Date fcaducidad;
    private Date fpublicacion;
    private String visible;
    private String indivisible;
    private Integer paginacion;
    private String mostrar;
    private String identificacion;
    private Set preguntas = new HashSet();
    private String votoDuplicado;
   
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

	public String getIndivisible() {
		return indivisible;
	}

	public void setIndivisible(String indivisible) {
		this.indivisible = indivisible;
	}

	public Integer getPaginacion() {
		return paginacion;
	}

	public void setPaginacion(Integer paginacion) {
		this.paginacion = paginacion;
	}

	public String getVisible() {
		return visible;
	}

	public void setVisible(String visible) {
		this.visible = visible;
	}

	public Set getPreguntas() {
		return preguntas;
	}

	public void setPreguntas(Set preguntas) {
		this.preguntas = preguntas;
	}

	// Metodos para poder leer las colecciones del XML	
	public void addPreguntas(Pregunta pre) {
		preguntas.add(pre);
	}
	
	public void addTraduccionMap(String lang, TraduccionEncuesta traduccion) {
        setTraduccion(lang, traduccion);
    }

	public String getMostrar() {
		return mostrar;
	}

	public void setMostrar(String mostrar) {
		this.mostrar = mostrar;
	}
	
	public String getVotoDuplicado() {
		return votoDuplicado;
	}

	public void setVotoDuplicado(String votoDuplicado) {
		this.votoDuplicado = votoDuplicado;
	}

	public String getIdentificacion() {
		return identificacion;
	}

	public void setIdentificacion(String identificacion) {
		this.identificacion = identificacion;
	}
}