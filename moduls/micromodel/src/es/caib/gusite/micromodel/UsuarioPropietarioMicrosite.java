package es.caib.gusite.micromodel;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Clase UsuarioPropietarioMicrosite. Bean que define un usuario propietario de
 * un Microsite. Modela la tabla de BBDD GUS_MICUSU.
 * 
 * @author Indra
 */

@Entity
@Table(name = "GUS_MICUSU")
public class UsuarioPropietarioMicrosite implements ValueObject {

	private static final long serialVersionUID = -9010452718936775987L;

	@Transient
	private Long id;

	@EmbeddedId
	private UsuarioPropietarioMicrositePK pk;

	public UsuarioPropietarioMicrosite() {
	}

	/**
	 * Constructor de la clase.
	 * 
	 * @param idmicrosite
	 * @param idusuario
	 */
	public UsuarioPropietarioMicrosite(Long idmicrosite, Long idusuario) {
		super();
		this.pk = new UsuarioPropietarioMicrositePK();
		this.pk.setIdmicrosite(idmicrosite);
		this.pk.setIdusuario(idusuario);
	}

	public UsuarioPropietarioMicrositePK getPk() {
		return this.pk;
	}

	public void setPk(UsuarioPropietarioMicrositePK pk) {
		this.pk = pk;
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public String toString() {
		return super.toString();
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
