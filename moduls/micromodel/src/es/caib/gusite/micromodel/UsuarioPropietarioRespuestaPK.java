package es.caib.gusite.micromodel;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Created by tcerda on 26/11/2014.
 */
@Embeddable
public class UsuarioPropietarioRespuestaPK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5044841446839856028L;

    @Column(name = "USR_CODRESP")
	private Long idrespuesta;

    @Column(name = "USR_CODUSU")
    private Long idusuario;

	public UsuarioPropietarioRespuestaPK() {
	}

	public Long getIdrespuesta() {
		return this.idrespuesta;
	}

	public void setIdrespuesta(Long idrespuesta) {
		this.idrespuesta = idrespuesta;
	}

	public Long getIdusuario() {
		return this.idusuario;
	}

	public void setIdusuario(Long idusuario) {
		this.idusuario = idusuario;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || this.getClass() != o.getClass()) {
			return false;
		}

		UsuarioPropietarioRespuestaPK that = (UsuarioPropietarioRespuestaPK) o;

		if (this.idrespuesta != null ? !this.idrespuesta
				.equals(that.idrespuesta) : that.idrespuesta != null) {
			return false;
		}
		if (this.idusuario != null ? !this.idusuario.equals(that.idusuario)
				: that.idusuario != null) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = this.idrespuesta != null ? this.idrespuesta.hashCode() : 0;
		result = 31 * result
				+ (this.idusuario != null ? this.idusuario.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "UsuarioPropietarioRespuestaPK{" + "idrespuesta="
				+ this.idrespuesta + ", idusuario=" + this.idusuario + '}';
	}
}
