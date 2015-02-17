package es.caib.gusite.micromodel;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Versión de front / identifica las versiones para su implementación en temas
 * 
 * @author at4.net
 */
@Entity
@Table(name = "GUS_FR_VERSION", uniqueConstraints = @UniqueConstraint(columnNames = "FVE_NOMBRE"))
public class Version implements Auditable, java.io.Serializable {

	@Override
	public String getAuditKey() {
		return this.getVersion();
	}

	@Override
	public Long getIdmicrosite() {
		return null;
	}

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Versión de GUSITE para la que se ha desarrollado el tema
	 */
	@Id
	@Column(name = "FVE_VERSION", unique = true, nullable = false, length = 2)
	private String version;

	/**
	 * Nom del tema
	 */

	@Column(name = "FVE_NOMBRE", unique = true, nullable = false)
	private String nombre;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "version")
	private Set<Plantilla> plantillas = new HashSet<Plantilla>(0);

	public Version() {
	}

	public Version(String version, String nombre) {
		this.version = version;
		this.nombre = nombre;
	}

	public Version(String version, String nombre, Set<Plantilla> plantillas) {
		this.version = version;
		this.nombre = nombre;
		this.plantillas = plantillas;
	}

	/**
	 * * Versión de GUSITE para la que se ha desarrollado el tema
	 */
	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * * Nom del tema
	 */
	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Set<Plantilla> getPlantillas() {
		return this.plantillas;
	}

	public void setPlantillas(Set<Plantilla> plantillas) {
		this.plantillas = plantillas;
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
		buffer.append("version").append("='").append(this.getVersion())
				.append("' ");
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
		if (!(other instanceof Version)) {
			return false;
		}
		Version castOther = (Version) other;

		return ((this.getVersion() == castOther.getVersion()) || (this
				.getVersion() != null && castOther.getVersion() != null && this
				.getVersion().equals(castOther.getVersion())));
	}

	@Override
	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (this.getVersion() == null ? 0 : this.getVersion().hashCode());

		return result;
	}

}
