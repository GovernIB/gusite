package es.caib.gusite.micromodel;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Table(name="GUS_CORREO")
public class Correo implements Serializable {

	private static final long serialVersionUID = 1L;

	/** identifier field */
	@Id
	@Column(name="CORREO")
    private String correo;

    /** identifier field */
	@Column(name="NOMBRE")
    private String nombre;
    
    /** identifier field */
	@Column(name="APELLIDOS")
    private String apellidos;
	
    /** nullable persistent field */
	@Column(name="NOENVIAR")
    private Boolean noEnviar;

    /** nullable persistent field */
	@Column(name="ULTIMO_ENVIO")
    private Timestamp ultimoEnvio;
    
    /** nullable persistent field */
	@Column(name="ERROR_ENVIO")
    private String traceError;
    
    /** nullable persistent field */
	@Column(name="INTENTO_ENVIO")
    private Integer intentoEnvio;
    
    /** full constructor */
    public Correo(String correo, String nombre, String apellidos, Boolean noEnviar) {
        this.correo = correo;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.noEnviar = noEnviar;
    }
    
    public Correo(String correo, Boolean noEnviar) {
        this.correo = correo;
        this.noEnviar = noEnviar;
    }

    /** default constructor */
    public Correo() {
    	this.noEnviar = false;
    }

    /** minimal constructor */
    public Correo(String correo) {
        this.correo = correo;
    }

    public String getCorreo() {
        return this.correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Boolean getNoEnviar() {
        return this.noEnviar;
    }

    public void setNoEnviar(Boolean noEnviar) {
        this.noEnviar = noEnviar;
    }

	public Timestamp getUltimoEnvio() {
		return ultimoEnvio;
	}

	public void setUltimoEnvio(Timestamp ultimoEnvio) {
		this.ultimoEnvio = ultimoEnvio;
	}

	public String getTraceError() {
		return traceError;
	}

	public void setTraceError(String traceError) {
		this.traceError = traceError;
	}

	public Integer getIntentoEnvio() {
		return intentoEnvio;
	}

	public void setIntentoEnvio(Integer intentoEnvio) {
		this.intentoEnvio = intentoEnvio;
	}
	
    public String toString() {
        return new ToStringBuilder(this)
            .append("correo", getCorreo())
            .toString();
    }

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((correo == null) ? 0 : correo.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Correo other = (Correo) obj;
		if (correo == null) {
			if (other.correo != null)
				return false;
		} else if (!correo.equals(other.correo))
			return false;
		return true;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

}
