package es.caib.gusite.micromodel;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "GUS_USURESP")
public class UsuarioPropietarioRespuesta implements ValueObject {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private UsuarioPropietarioRespuestaPK id;

	public UsuarioPropietarioRespuesta() {
		this.id = new UsuarioPropietarioRespuestaPK();
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

	public UsuarioPropietarioRespuestaPK getId() {
		return this.id;
	}

	public void setId(UsuarioPropietarioRespuestaPK id) {
		this.id = id;
	}
}
