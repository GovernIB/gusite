package es.caib.gusite.micromodel;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the GUS_TEMIDI database table.
 * 
 */
@Embeddable
public class UsuarioPropietarioMicrositePK implements Serializable {
	// default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name = "MIU_CODMIC")
	private Long idmicrosite;

	@Column(name = "MIU_CODUSU")
	private Long idusuario;

	public UsuarioPropietarioMicrositePK() {
	}

	public Long getIdmicrosite() {
		return this.idmicrosite;
	}

	public void setIdmicrosite(Long idmicrosite) {
		this.idmicrosite = idmicrosite;
	}

	public Long getIdusuario() {
		return this.idusuario;
	}

	public void setIdusuario(Long idusuario) {
		this.idusuario = idusuario;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof UsuarioPropietarioMicrositePK)) {
			return false;
		}
		UsuarioPropietarioMicrositePK castOther = (UsuarioPropietarioMicrositePK) other;
		return this.idmicrosite.equals(castOther.idmicrosite)
				&& (this.idusuario == castOther.idusuario);

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.idmicrosite.hashCode();
		hash = hash * prime
				+ ((int) (this.idusuario ^ (this.idusuario >>> 32)));

		return hash;
	}
}