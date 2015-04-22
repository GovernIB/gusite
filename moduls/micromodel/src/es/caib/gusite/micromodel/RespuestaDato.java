package es.caib.gusite.micromodel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * Clase RespuestaDato. Bean que define el dato de una respuesta. Hay respuestas
 * en las cuales el usuario puede introducir información, esta información es el
 * dato. Modela la tabla de BBDD GUS_ENCVOT.
 * 
 * @author Indra
 */
@XmlAccessorType(XmlAccessType.NONE)
@Entity
@Table(name = "GUS_ENCVOT")
public class RespuestaDato implements ValueObject {

	private static final long serialVersionUID = 2552194475264149738L;

	@XmlAttribute
	@Id
	@SequenceGenerator(name = "GUS_RESPUESTA_ID_GENERATOR", sequenceName = "GUS_SEQRDA", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GUS_RESPUESTA_ID_GENERATOR")
	@Column(name = "VOT_CODI")
	private Long id;

	@XmlAttribute
	@Column(name = "VOT_IDENCU")
	private Long idencuesta;

	@XmlAttribute
	@Column(name = "VOT_IDPREG")
	private Long idpregunta;

	@XmlAttribute
	@Column(name = "VOT_IDRESP")
	private Long idrespueta;

	@XmlAttribute
	@Column(name = "VOT_INPUT")
	private String dato;

	@XmlAttribute
	@Column(name = "VOT_CODUSU")
	private Long idusuari;

	public String getDato() {
		return this.dato;
	}

	public void setDato(String dato) {
		this.dato = dato;
	}

	public Long getIdencuesta() {
		return this.idencuesta;
	}

	public void setIdencuesta(Long idencuesta) {
		this.idencuesta = idencuesta;
	}

	public Long getIdpregunta() {
		return this.idpregunta;
	}

	public void setIdpregunta(Long idpregunta) {
		this.idpregunta = idpregunta;
	}

	public Long getIdrespueta() {
		return this.idrespueta;
	}

	public void setIdrespueta(Long idrespueta) {
		this.idrespueta = idrespueta;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setIdusuari(Long idusuari) {
		this.idusuari = idusuari;
	}

	public Long getIdusuari() {
		return this.idusuari;
	}

}
