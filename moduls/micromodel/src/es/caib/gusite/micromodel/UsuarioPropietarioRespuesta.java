package es.caib.gusite.micromodel;

import javax.persistence.*;

@Entity
@Table(name="GUS_MICUSU")
public class UsuarioPropietarioRespuesta implements ValueObject {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private UsuarioPropietarioRespuestaPK id;
	
	public UsuarioPropietarioRespuesta() {
			id = new UsuarioPropietarioRespuestaPK();
	}

	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	public int hashCode() {
		return super.hashCode();
	}

	public String toString() {
		return super.toString();
	}

	public UsuarioPropietarioRespuestaPK getId() {
		return id;
	}

	public void setId(UsuarioPropietarioRespuestaPK id) {
		this.id = id;
	}
}
