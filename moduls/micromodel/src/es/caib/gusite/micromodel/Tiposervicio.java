package es.caib.gusite.micromodel;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Clase Tiposervicio. Bean que define un Tipo de servicio. Modela la tabla de
 * BBDD GUS_TIPSER.
 * 
 * @author Indra
 */

@Entity
@Table(name = "GUS_TIPSER")
public class Tiposervicio extends AuditableModel implements Serializable {

	private static final long serialVersionUID = 8927320761345779941L;

	@Id
	@SequenceGenerator(name = "GUS_TIPOSERVICIO_ID_GENERATOR", sequenceName = "GUS_SEQTIP", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GUS_TIPOSERVICIO_ID_GENERATOR")
	@Column(name = "TPS_CODI")
	private Long id;

	@Column(name = "TPS_NOMBRE")
	private String nombre;

	@Column(name = "TPS_VISIB")
	private String visible;

	@Column(name = "TPS_TIPO")
	private String tipo;

	@Column(name = "TPS_URL")
	private String url;

	@Column(name = "TPS_REF")
	private String referencia;

	public String getTipo() {
		return this.tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getVisible() {
		return this.visible;
	}

	public void setVisible(String visible) {
		this.visible = visible;
	}

	public String getReferencia() {
		return this.referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}
}