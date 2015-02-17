package es.caib.gusite.micromodel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Clase Usuario. Bean que define un Usuario. Modela la tabla de BBDD
 * GUS_MUSUAR.
 * 
 * @author Indra
 */

@Entity
@Table(name = "GUS_MUSUAR")
public class Usuario extends AuditableModel implements ValueObject {

	private static final long serialVersionUID = -6526246228389188142L;

	@Override
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getObservaciones() {
		return this.observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getPerfil() {
		return this.perfil;
	}

	public void setPerfil(String perfil) {
		this.perfil = perfil;
	}

	@Id
	@SequenceGenerator(name = "GUS_USUARIO_ID_GENERATOR", sequenceName = "GUS_SQM_ALL", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GUS_USUARIO_ID_GENERATOR")
	@Column(name = "MSU_CODI")
	private Long id;

	@Column(name = "MSU_USERNA")
	private String username;

	@Column(name = "MSU_PASSWO")
	private String password;

	@Column(name = "MSU_NOMBRE")
	private String nombre;

	@Column(name = "MSU_OBSERV")
	private String observaciones;

	@Column(name = "MSU_PERFIL")
	private String perfil;

}
