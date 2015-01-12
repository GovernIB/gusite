package es.caib.gusite.micromodel;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created by tcerda on 26/11/2014.
 */
@Embeddable
public class UsuarioPropietarioRespuestaPK implements Serializable {

    @Column(name="MIU_CODMIC")
    private Long idrespuesta;

    @Column(name="MIU_CODUSU")
    private Long idusuario;

    public UsuarioPropietarioRespuestaPK() {
    }

    public Long getIdrespuesta() {
        return idrespuesta;
    }

    public void setIdrespuesta(Long idrespuesta) {
        this.idrespuesta = idrespuesta;
    }

    public Long getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(Long idusuario) {
        this.idusuario = idusuario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UsuarioPropietarioRespuestaPK that = (UsuarioPropietarioRespuestaPK) o;

        if (idrespuesta != null ? !idrespuesta.equals(that.idrespuesta) : that.idrespuesta != null) return false;
        if (idusuario != null ? !idusuario.equals(that.idusuario) : that.idusuario != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = idrespuesta != null ? idrespuesta.hashCode() : 0;
        result = 31 * result + (idusuario != null ? idusuario.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UsuarioPropietarioRespuestaPK{" +
                "idrespuesta=" + idrespuesta +
                ", idusuario=" + idusuario +
                '}';
    }
}
