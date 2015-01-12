package org.ibit.rol.sac.micromodel;

import java.io.Serializable;
import java.util.Collections;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class ListaDistribucion implements Serializable {

    /** identifier field */
    private Long id;

    /** nullable persistent field */
    private String nombre;

    /** nullable persistent field */
    private String descripcion;

    /** nullable persistent field */
    private org.ibit.rol.sac.micromodel.Microsite microsite;

    /** nullable persistent field */
    private Boolean publico;
    
    /** persistent field */
    private Set destinatarios;

    /** full constructor */
    public ListaDistribucion(String nombre, String descripcion, org.ibit.rol.sac.micromodel.Microsite microsite, Set destinatarios) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.microsite = microsite;
        this.destinatarios = destinatarios;
        this.publico = false;
    }

    /** default constructor */
    public ListaDistribucion() {
    	this.destinatarios = Collections.EMPTY_SET;
    }

    /** minimal constructor */
    public ListaDistribucion(Set destinatarios) {
        this.destinatarios = destinatarios;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public org.ibit.rol.sac.micromodel.Microsite getMicrosite() {
        return this.microsite;
    }

    public void setMicrosite(org.ibit.rol.sac.micromodel.Microsite microsite) {
        this.microsite = microsite;
    }

    public Set getDestinatarios() {
        return this.destinatarios;
    }

    public void setDestinatarios(Set destinatarios) {
        this.destinatarios = destinatarios;
    }

	public Boolean getPublico() {
		return publico;
	}

	public void setPublico(Boolean publico) {
		this.publico = publico;
	}

    public String toString() {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ListaDistribucion))
			return false;
		ListaDistribucion other = (ListaDistribucion) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
