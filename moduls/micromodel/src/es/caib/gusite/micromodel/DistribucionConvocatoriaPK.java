package es.caib.gusite.micromodel;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created by tcerda on 26/11/2014.
 */
@Embeddable
public class DistribucionConvocatoriaPK implements Serializable {

    /** identifier field */
    @Column(name="CONVOCATORIA_ID")
    private Long idConvocatoria;

    /** identifier field */
    @Column(name="DISTRIB_ID")
    private Long idDistribucion;

    public DistribucionConvocatoriaPK() {
    }

    public Long getIdConvocatoria() {
        return idConvocatoria;
    }

    public void setIdConvocatoria(Long idConvocatoria) {
        this.idConvocatoria = idConvocatoria;
    }

    public Long getIdDistribucion() {
        return idDistribucion;
    }

    public void setIdDistribucion(Long idDistribucion) {
        this.idDistribucion = idDistribucion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DistribucionConvocatoriaPK that = (DistribucionConvocatoriaPK) o;

        if (idConvocatoria != null ? !idConvocatoria.equals(that.idConvocatoria) : that.idConvocatoria != null)
            return false;
        if (idDistribucion != null ? !idDistribucion.equals(that.idDistribucion) : that.idDistribucion != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = idConvocatoria != null ? idConvocatoria.hashCode() : 0;
        result = 31 * result + (idDistribucion != null ? idDistribucion.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DistribucionConvocatoriaPK{" +
                "idConvocatoria=" + idConvocatoria +
                ", idDistribucion=" + idDistribucion +
                '}';
    }
}
