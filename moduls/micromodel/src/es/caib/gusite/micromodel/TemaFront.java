package es.caib.gusite.micromodel;

import static javax.persistence.GenerationType.SEQUENCE;

import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

/**
 * Tema del front
 * 
 * @author at4.net
 */
@Entity
@Table(name = "GUS_FR_TEMA", uniqueConstraints = @UniqueConstraint(columnNames = "FTE_NOMBRE"))
public class TemaFront extends AuditableModel implements Auditable,
		java.io.Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	@SequenceGenerator(name = "generator", sequenceName = "GUS_SEQFTE")
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "FTE_CODI", unique = true, nullable = false, scale = 0)
	private Long id;

	/**
	 * CSS General del tema
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FTE_CSS")
	private Archivo css;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FTE_TEMA_PADRE", nullable = false)
	private TemaFront tempaPadre;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FTE_VERSION", nullable = false)
	private Version version;

	/**
	 * Nom del tema
	 */

	@Column(name = "FTE_NOMBRE", unique = true, nullable = false)
	private String nombre;

	/**
	 * Data d'actualització del tema
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "FTE_ACTUALIZACION", nullable = false, length = 7)
	private Date actualizacion;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tempaPadre")
	private Set<TemaFront> temasHijos = new HashSet<TemaFront>(0);

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "temaFront")
	private Set<PersonalizacionPlantilla> personalizacionesPlantilla = new HashSet<PersonalizacionPlantilla>(
			0);

	public TemaFront() {
	}

	public TemaFront(TemaFront tempaPadre, Version version, String nombre,
			Date actualizacion) {
		this.tempaPadre = tempaPadre;
		this.version = version;
		this.nombre = nombre;
		this.actualizacion = actualizacion;
	}

	public TemaFront(Archivo css, TemaFront tempaPadre, Version version,
			String nombre, Date actualizacion, Set<TemaFront> temasHijos,
			Set<PersonalizacionPlantilla> personalizacionesPlantilla) {
		this.css = css;
		this.tempaPadre = tempaPadre;
		this.version = version;
		this.nombre = nombre;
		this.actualizacion = actualizacion;
		this.temasHijos = temasHijos;
		this.personalizacionesPlantilla = personalizacionesPlantilla;
	}

	@Override
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * * CSS General del tema
	 */
	public Archivo getCss() {
		return this.css;
	}

	public void setCss(Archivo css) {
		this.css = css;
	}

	public TemaFront getTempaPadre() {
		return this.tempaPadre;
	}

	public void setTempaPadre(TemaFront tempaPadre) {
		this.tempaPadre = tempaPadre;
	}

	public Version getVersion() {
		return this.version;
	}

	public void setVersion(Version version) {
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

	/**
	 * * Data d'actualització del tema
	 */
	public Date getActualizacion() {
		return this.actualizacion;
	}

	public void setActualizacion(Date actualizacion) {
		this.actualizacion = actualizacion;
	}

	public Set<TemaFront> getTemasHijos() {
		return this.temasHijos;
	}

	public void setTemasHijos(Set<TemaFront> temasHijos) {
		this.temasHijos = temasHijos;
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
		if (!(other instanceof TemaFront)) {
			return false;
		}
		TemaFront castOther = (TemaFront) other;

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
