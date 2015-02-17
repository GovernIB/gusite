package es.caib.gusite.micromodel;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Created by tcerda on 26/11/2014.
 */
@Embeddable
public class DistribucionConvocatoriaPK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2534597992534148656L;

	/** identifier field */
	@Column(name = "CONVOCATORIA_ID")
	private Long idConvocatoria;

	/** identifier field */
	@Column(name = "DISTRIB_ID")
	private Long idDistribucion;

	public DistribucionConvocatoriaPK() {
	}

	public Long getIdConvocatoria() {
		return this.idConvocatoria;
	}

	public void setIdConvocatoria(Long idConvocatoria) {
		this.idConvocatoria = idConvocatoria;
	}

	public Long getIdDistribucion() {
		return this.idDistribucion;
	}

	public void setIdDistribucion(Long idDistribucion) {
		this.idDistribucion = idDistribucion;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || this.getClass() != o.getClass()) {
			return false;
		}

		DistribucionConvocatoriaPK that = (DistribucionConvocatoriaPK) o;

		if (this.idConvocatoria != null ? !this.idConvocatoria
				.equals(that.idConvocatoria) : that.idConvocatoria != null) {
			return false;
		}
		if (this.idDistribucion != null ? !this.idDistribucion
				.equals(that.idDistribucion) : that.idDistribucion != null) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = this.idConvocatoria != null ? this.idConvocatoria
				.hashCode() : 0;
		result = 31
				* result
				+ (this.idDistribucion != null ? this.idDistribucion.hashCode()
						: 0);
		return result;
	}

	@Override
	public String toString() {
		return "DistribucionConvocatoriaPK{" + "idConvocatoria="
				+ this.idConvocatoria + ", idDistribucion="
				+ this.idDistribucion + '}';
	}
}
