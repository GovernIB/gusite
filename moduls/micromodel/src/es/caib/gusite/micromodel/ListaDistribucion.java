package es.caib.gusite.micromodel;

import java.io.Serializable;
import java.util.Collections;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;


@Entity
@Table(name="GUS_LDISTRIBUCION")
public class ListaDistribucion implements Serializable {

	private static final long serialVersionUID = 1L;

	/** identifier field */
	@Id
	@SequenceGenerator(name="GUS_LDISTRIBUCION_ID_GENERATOR", sequenceName="GUS_LDISTRB_SEQ", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GUS_LDISTRIBUCION_ID_GENERATOR")
	@Column(name="CODI")
    private Long id;

    /** nullable persistent field */
	@Column(name="NOMBRE")
    private String nombre;

    /** nullable persistent field */
	@Column(name="DESCRIPCION")
    private String descripcion;

    /** nullable persistent field */
    @ManyToOne
	@JoinColumn(name="MICROSITE_ID")
    private Microsite microsite;

    /** nullable persistent field */
    @Column(name="PUBLICO")
    private Boolean publico;
    
    /** persistent field */
	@ManyToMany(fetch=FetchType.EAGER, cascade={CascadeType.ALL})
	@JoinTable(
		name="GUS_LDISTRIBUCION_CORREO"
		, joinColumns={
			@JoinColumn(name="LISTADISTRIB_ID")
			}
		, inverseJoinColumns={
			@JoinColumn(name="CORREO_ID")
			}
		)
    private Set<Correo> destinatarios;

    /** full constructor */
    public ListaDistribucion(String nombre, String descripcion, es.caib.gusite.micromodel.Microsite microsite, Set<Correo> destinatarios) {
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
    public ListaDistribucion(Set<Correo> destinatarios) {
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

    public es.caib.gusite.micromodel.Microsite getMicrosite() {
        return this.microsite;
    }

    public void setMicrosite(es.caib.gusite.micromodel.Microsite microsite) {
        this.microsite = microsite;
    }

    public Set<Correo> getDestinatarios() {
        return this.destinatarios;
    }

    public void setDestinatarios(Set<Correo> destinatarios) {
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
