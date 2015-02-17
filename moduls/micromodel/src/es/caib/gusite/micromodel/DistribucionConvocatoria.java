package es.caib.gusite.micromodel;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "GUS_DISTRIB_CONVOCATORIA")
public class DistribucionConvocatoria implements Serializable, Auditable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private DistribucionConvocatoriaPK id;

	/** nullable persistent field */
	@Column(name = "ULTIMO_ENVIO")
	private Date ultimoEnvio;

	/** default constructor */
	public DistribucionConvocatoria() {
		this.id = new DistribucionConvocatoriaPK();
	}

	public DistribucionConvocatoria(Long idConvocatoria, Long idDistribucion) {
		if (this.id == null) {
			this.id = new DistribucionConvocatoriaPK();
		}
		this.id.setIdConvocatoria(idConvocatoria);
		this.id.setIdDistribucion(idDistribucion);
	}

	public DistribucionConvocatoriaPK getId() {
		return this.id;
	}

	public void setId(DistribucionConvocatoriaPK id) {
		this.id = id;
	}

	public Date getUltimoEnvio() {
		return this.ultimoEnvio;
	}

	public void setUltimoEnvio(Date ultimoEnvio) {
		this.ultimoEnvio = ultimoEnvio;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || this.getClass() != o.getClass()) {
			return false;
		}

		DistribucionConvocatoria that = (DistribucionConvocatoria) o;

		if (this.id != null ? !this.id.equals(that.id) : that.id != null) {
			return false;
		}
		if (this.ultimoEnvio != null ? !this.ultimoEnvio
				.equals(that.ultimoEnvio) : that.ultimoEnvio != null) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = this.id != null ? this.id.hashCode() : 0;
		result = 31 * result
				+ (this.ultimoEnvio != null ? this.ultimoEnvio.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "DistribucionConvocatoria{" + "id=" + this.id + ", ultimoEnvio="
				+ this.ultimoEnvio + '}';
	}

	@Override
	public String getAuditKey() {
		return this.getId().toString();
	}

	/* No sabemos el microsite en este punto */
	@Override
	public Long getIdmicrosite() {
		return null;
	}
}
