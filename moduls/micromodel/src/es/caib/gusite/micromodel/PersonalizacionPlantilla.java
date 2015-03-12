package es.caib.gusite.micromodel;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.SEQUENCE;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Personalización de una plantilla
 * 
 * @author at4.net
 */
@Entity
@Table(name = "GUS_FR_PERPLA")
public class PersonalizacionPlantilla extends AuditableModel implements
		Auditable, java.io.Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	@SequenceGenerator(name = "generator", sequenceName = "GUS_SEQPPL")
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "PPL_CODI", unique = true, nullable = false, scale = 0)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "PPL_MICCOD")
	private Microsite microsite;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "PPL_FTECOD")
	private TemaFront tema;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "PPL_PLACOD", nullable = false)
	private Plantilla plantilla;

	/**
	 * Título de la plantilla
	 */

	@Column(name = "PPL_TITULO", nullable = false)
	private String titulo;

	/**
	 * Número de orden de la personalización de plantilla
	 */

	@Column(name = "PPL_ORDEN", precision = 22, scale = 0)
	private Long orden;

	/**
	 * Contenido de la plantilla
	 */

	@Lob
	@Column(name = "PPL_CONTENIDO")
	private String contenido;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "personalizacionPlantilla")
	private Set<Tipo> tipos = new HashSet<Tipo>(0);

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "personalizacionPlantilla")
	private Set<Contenido> contenidos = new HashSet<Contenido>(0);

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "personalizacionPlantilla")
	private Set<Componente> componentes = new HashSet<Componente>(0);

	public PersonalizacionPlantilla() {
	}

	public PersonalizacionPlantilla(Plantilla plantilla, String titulo) {
		this.plantilla = plantilla;
		this.titulo = titulo;
	}

	public PersonalizacionPlantilla(Microsite microsite, TemaFront tema,
			Plantilla plantilla, String titulo, Long orden, String contenido,
			Set<Tipo> tipos, Set<Contenido> contenidos,
			Set<Componente> componentes) {
		this.microsite = microsite;
		this.tema = tema;
		this.plantilla = plantilla;
		this.titulo = titulo;
		this.orden = orden;
		this.contenido = contenido;
		this.tipos = tipos;
		this.contenidos = contenidos;
		this.componentes = componentes;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Microsite getMicrosite() {
		return this.microsite;
	}

	public void setMicrosite(Microsite microsite) {
		this.microsite = microsite;
	}

	public TemaFront getTema() {
		return this.tema;
	}

	public void setTema(TemaFront tema) {
		this.tema = tema;
	}

	public Plantilla getPlantilla() {
		return this.plantilla;
	}

	public void setPlantilla(Plantilla plantilla) {
		this.plantilla = plantilla;
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

	/**
	 * * Número de orden de la personalización de plantilla
	 */
	public Long getOrden() {
		return this.orden;
	}

	public void setOrden(Long orden) {
		this.orden = orden;
	}

	/**
	 * * Contenido de la plantilla
	 */
	public String getContenido() {
		return this.contenido;
	}

	public void setContenido(String contenido) {
		this.contenido = contenido;
	}

	public Set<Tipo> getTipos() {
		return this.tipos;
	}

	public void setTipos(Set<Tipo> tipos) {
		this.tipos = tipos;
	}

	public Set<Contenido> getContenidos() {
		return this.contenidos;
	}

	public void setContenidos(Set<Contenido> contenidos) {
		this.contenidos = contenidos;
	}

	public Set<Componente> getComponentes() {
		return this.componentes;
	}

	public void setComponentes(Set<Componente> componentes) {
		this.componentes = componentes;
	}

	/**
	 * toString
	 * 
	 * @return String
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();

		buffer.append(getClass().getName()).append("@")
				.append(Integer.toHexString(hashCode())).append(" [");
		buffer.append("id").append("='").append(getId()).append("' ");
		buffer.append("]");

		return buffer.toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof PersonalizacionPlantilla))
			return false;
		PersonalizacionPlantilla castOther = (PersonalizacionPlantilla) other;

		return ((this.getId() == castOther.getId()) || (this.getId() != null
				&& castOther.getId() != null && this.getId().equals(
				castOther.getId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getId() == null ? 0 : this.getId().hashCode());

		return result;
	}

}
