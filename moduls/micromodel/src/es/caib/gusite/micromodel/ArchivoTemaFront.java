package es.caib.gusite.micromodel;

import static javax.persistence.GenerationType.SEQUENCE;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * Tabla que contiene los archivos (imágenes, js, etc.) utilizados en los
 * distintos elementos de un tema
 * 
 * @author at4.net
 */
@XmlAccessorType(XmlAccessType.NONE)
@Entity
@Table(name = "GUS_FR_ARCHIVO", uniqueConstraints = @UniqueConstraint(columnNames = {"ARC_FTECOD", "ARC_PATH" }))
public class ArchivoTemaFront extends AuditableModel implements Auditable, java.io.Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Identificador de documento
	 */
    @XmlAttribute
	@SequenceGenerator(name = "generator", sequenceName = "GUS_SEQARC")
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "ARC_CODI", unique = true, nullable = false, precision = 22, scale = 0)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ARC_FTECOD", nullable = false)
	private TemaFront tema;

    @XmlElement
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ARC_DOCCOD")
	private Archivo archivo;

	/**
	 * Ruta virtual del archivo
	 */
    @XmlAttribute
	@Column(name = "ARC_PATH", length = 256)
	private String path;

	public ArchivoTemaFront() {
	}

	public ArchivoTemaFront(TemaFront tema) {
		this.tema = tema;
	}

	public ArchivoTemaFront(TemaFront tema, Archivo archivo, String path) {
		this.tema = tema;
		this.archivo = archivo;
		this.path = path;
	}

	/**
	 * * Identificador de documento
	 */
	@Override
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TemaFront getTema() {
		return this.tema;
	}

	public void setTema(TemaFront tema) {
		this.tema = tema;
	}

	public Archivo getArchivo() {
		return this.archivo;
	}

	public void setArchivo(Archivo archivo) {
		this.archivo = archivo;
	}

	/**
	 * * Ruta virtual del archivo
	 */
	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
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
		if (!(other instanceof ArchivoTemaFront)) {
			return false;
		}
		ArchivoTemaFront castOther = (ArchivoTemaFront) other;

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
