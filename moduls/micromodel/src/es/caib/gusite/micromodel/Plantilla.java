package es.caib.gusite.micromodel;

import static javax.persistence.GenerationType.SEQUENCE;

import java.sql.Clob;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Tabla que identifica las plantillas que se pueden implementar (sobreescribir)
 * para una versión determinada de front
 * 
 * @author at4.net
 */
@Entity
@Table(name = "GUS_FR_PLANTILLA", uniqueConstraints = @UniqueConstraint(columnNames = "PLA_NOMBRE"))
public class Plantilla extends AuditableModel implements Auditable,
		java.io.Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	@SequenceGenerator(name = "generator", sequenceName = "GUS_SEQPLA")
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "PLA_CODI", unique = true, nullable = false, scale = 0)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "PLA_VERSION", nullable = false)
	private Version version;

	/**
	 * Nombre (identificador) de la plantilla
	 */

	@Column(name = "PLA_NOMBRE", unique = true, nullable = false)
	private String nombre;

	/**
	 * Descripción y documentación de la plantilla
	 */

	@Column(name = "PLA_DESCRIPCION")
	private Clob descripcion;

	/**
	 * Título de la plantilla
	 */

	@Column(name = "PLA_TITULO", nullable = false)
	private String titulo;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "plantilla")
	private Set<PersonalizacionPlantilla> personalizacionesPlantilla = new HashSet<PersonalizacionPlantilla>(
			0);

	public Plantilla() {
	}

	public Plantilla(Version version, String nombre, String titulo) {
		this.version = version;
		this.nombre = nombre;
		this.titulo = titulo;
	}

	public Plantilla(Version version, String nombre, Clob descripcion,
			String titulo,
			Set<PersonalizacionPlantilla> personalizacionesPlantilla) {
		this.version = version;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.titulo = titulo;
		this.personalizacionesPlantilla = personalizacionesPlantilla;
	}

	@Override
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Version getVersion() {
		return this.version;
	}

	public void setVersion(Version version) {
		this.version = version;
	}

	/**
	 * * Nombre (identificador) de la plantilla
	 */
	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * * Descripción y documentación de la plantilla
	 */
	public Clob getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(Clob descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * * Título de la plantilla
	 */
	public String getTitulo() {
		return this.titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public Set<PersonalizacionPlantilla> getPersonalizacionesPlantilla() {
		return this.personalizacionesPlantilla;
	}

	public void setPersonalizacionesPlantilla(
			Set<PersonalizacionPlantilla> personalizacionesPlantilla) {
		this.personalizacionesPlantilla = personalizacionesPlantilla;
	}

	/**
	 * toString
	 * 
	 * @return String
	 */
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();

		buffer.append(this.getClass().getName()).append("@")
				.append(Integer.toHexString(this.hashCode())).append(" [");
		buffer.append("id").append("='").append(this.getId()).append("' ");
		buffer.append("]");

		return buffer.toString();
	}

	@Override
	public boolean equals(Object other) {
		if ((this == other)) {
			return true;
		}
		if ((other == null)) {
			return false;
		}
		if (!(other instanceof Plantilla)) {
			return false;
		}
		Plantilla castOther = (Plantilla) other;

		return ((this.getId() == castOther.getId()) || (this.getId() != null
				&& castOther.getId() != null && this.getId().equals(
				castOther.getId())));
	}

	@Override
	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (this.getId() == null ? 0 : this.getId().hashCode());

		return result;
	}

}
