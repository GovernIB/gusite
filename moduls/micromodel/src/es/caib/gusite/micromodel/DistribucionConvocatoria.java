package es.caib.gusite.micromodel;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name="GUS_DISTRIB_CONVOCATORIA")
public class DistribucionConvocatoria implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private DistribucionConvocatoriaPK id;

    /** nullable persistent field */
    @Column(name="ULTIMO_ENVIO")
    private Date ultimoEnvio;

    /** default constructor */
    public DistribucionConvocatoria() {
		id = new DistribucionConvocatoriaPK();
    }

	public DistribucionConvocatoria(Long idConvocatoria, Long idDistribucion) {
		if (id == null) {
			id = new DistribucionConvocatoriaPK();
		}
		id.setIdConvocatoria(idConvocatoria);
		id.setIdDistribucion(idDistribucion);
	}

	public DistribucionConvocatoriaPK getId() {
		return id;
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
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		DistribucionConvocatoria that = (DistribucionConvocatoria) o;

		if (id != null ? !id.equals(that.id) : that.id != null) return false;
		if (ultimoEnvio != null ? !ultimoEnvio.equals(that.ultimoEnvio) : that.ultimoEnvio != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (ultimoEnvio != null ? ultimoEnvio.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "DistribucionConvocatoria{" +
				"id=" + id +
				", ultimoEnvio=" + ultimoEnvio +
				'}';
	}
}
