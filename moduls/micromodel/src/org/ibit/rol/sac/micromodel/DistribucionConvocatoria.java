package org.ibit.rol.sac.micromodel;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class DistribucionConvocatoria implements Serializable {

    /** identifier field */
    private Long idConvocatoria;

    /** identifier field */
    private Long idDistribucion;

    /** nullable persistent field */
    private Date ultimoEnvio;

    /** default constructor */
    public DistribucionConvocatoria() {
    }

    /** minimal constructor */
    public DistribucionConvocatoria(Long idConvocatoria, Long idDistribucion) {
        this.idConvocatoria = idConvocatoria;
        this.idDistribucion = idDistribucion;
    }

    public Date getUltimoEnvio() {
        return this.ultimoEnvio;
    }

    public void setUltimoEnvio(Date ultimoEnvio) {
        this.ultimoEnvio = ultimoEnvio;
    }
 
	/**
	 * @return the convocatoria
	 */
	public Long getIdConvocatoria() {
		return idConvocatoria;
	}

	/**
	 * @param convocatoria the convocatoria to set
	 */
	public void setIdConvocatoria(Long idConvocatoria) {
		this.idConvocatoria = idConvocatoria;
	}

	/**
	 * @return the distribucion
	 */
	public Long getIdDistribucion() {
		return idDistribucion;
	}

	/**
	 * @param distribucion the distribucion to set
	 */
	public void setIdDistribucion(Long idDistribucion) {
		this.idDistribucion = idDistribucion;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((idConvocatoria == null) ? 0 : idConvocatoria.hashCode());
		result = prime * result
				+ ((idDistribucion == null) ? 0 : idDistribucion.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof DistribucionConvocatoria))
			return false;
		DistribucionConvocatoria other = (DistribucionConvocatoria) obj;
		if (idConvocatoria == null) {
			if (other.idConvocatoria != null)
				return false;
		} else if (!idConvocatoria.equals(other.idConvocatoria))
			return false;
		if (idDistribucion == null) {
			if (other.idDistribucion != null)
				return false;
		} else if (!idDistribucion.equals(other.idDistribucion))
			return false;
		return true;
	}

}
