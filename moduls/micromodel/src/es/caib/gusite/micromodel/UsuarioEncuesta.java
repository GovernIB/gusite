package es.caib.gusite.micromodel;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

/**
 * Clase UsuarioEncuesta. Bean que define usuario que ha contestado una
 * encuesta. Modela la tabla de BBDD GUS_USUARIENC.
 * 
 * @author pedro melia
 */
public class UsuarioEncuesta implements ValueObject {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "GUS_USUARIOENCUESTA_ID_GENERATOR", sequenceName = "GUS_SEQTIP", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GUS_USUARIOENCUESTA_ID_GENERATOR")
	@Column(name = "MSU_CODI")
	private Long id;

	@Column(name = "USE_NOMBRE")
	private String nombre;

	@Column(name = "USE_OBSERV")
	private String observaciones;

	@Column(name = "USE_DNI")
	private String dni;

	/**
	 * Descripción: Devuelve el Id del usuario
	 * 
	 * @return Id del usuario
	 */
	public Long getId() {
		return this.id;
	}

	/**
	 * Descripción: Establece el Id del usuario
	 * 
	 * @param id
	 *            del usario
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Descripción: Devuelve Nombre del usuario
	 * 
	 * @return nombre del usuario
	 */
	public String getNombre() {
		return this.nombre;
	}

	/**
	 * Descripción: Establece el nombre del usuario
	 * 
	 * @param nombre
	 *            nombre del usario
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Descripción: Devuelve Observaciones del usuario
	 * 
	 * @return observaciones
	 */
	public String getObservaciones() {
		return this.observaciones;
	}

	/**
	 * Descripción: Establece Observaciones del usuario
	 * 
	 * @param observaciones
	 */
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	/**
	 * Descripcion: Establece el dni del que contesta la encuesta
	 * 
	 * @return dni
	 */
	public String getDni() {
		return this.dni;
	}

	/**
	 * Descripcion: Establece el dni del que contesta la encuesta
	 * 
	 * @param dni
	 */
	public void setDni(String dni) {
		this.dni = dni;
	}

}
