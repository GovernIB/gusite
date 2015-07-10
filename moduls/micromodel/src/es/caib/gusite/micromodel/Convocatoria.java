package es.caib.gusite.micromodel;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Table(name = "GUS_CONVOCATORIA")
public class Convocatoria extends AuditableModel implements Serializable {

	private static final long serialVersionUID = 1L;

	/** identifier field */
	@Id
	@SequenceGenerator(name = "GUS_CONVOCATORIA_ID_GENERATOR", sequenceName = "GUS_CONV_SEQ", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GUS_CONVOCATORIA_ID_GENERATOR")
	@Column(name = "CODI")
	private Long id;

	/** nullable persistent field */
	@Column(name = "NOMBRE")
	private String nombre;

	/** nullable persistent field */
	@Column(name = "DESCRIPCION")
	private String descripcion;

	/** nullable persistent field */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_ENVIO")
	private Date ultimoEnvio;

	/** nullable persistent field */
	@Column(name = "REENVIO_ERROR")
	private Boolean envioSiError;

	/** nullable persistent field */
	@Transient
	private Boolean envioSiConfirmado;

	/** persistent field */
	// bi-directional many-to-one association to GusDistribConvocatoria
	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@JoinColumn(name = "CONVOCATORIA_ID")
	private Set<DistribucionConvocatoria> destinatarios;
	// private Set destinatarios;

	/** nullable persistent field */
	@Column(name = "OTROS_DEST")
	private String otrosDestinatarios;

	/** nullable persistent field */
	@Column(name = "ASUNTO_MSG")
	private String asuntoMsg;

	/** nullable persistent field */
	@Lob()
	@Column(name = "TEXTO_MSG")
	private String textoMsg;

	/** nullable persistent field */
	@ManyToOne
	@JoinColumn(name = "MICROSITE_ID")
	private es.caib.gusite.micromodel.Microsite microsite;

	/** nullable persistent field */
	@ManyToOne
	@JoinColumn(name = "ENCUESTA_ID")
	private es.caib.gusite.micromodel.Encuesta encuesta;

	/** nullable persistent field */
	@ManyToOne
	@JoinColumn(name = "RESPUESTA_CORREO_ID")
	private es.caib.gusite.micromodel.Respuesta respuestaCorreo;

	/** nullable persistent field */
	@ManyToOne
	@JoinColumn(name = "PREGUNTA_CONFIRMACION_ID")
	private es.caib.gusite.micromodel.Pregunta preguntaConfirmacion;

	/** nullable persistent field */
	@ManyToOne
	@JoinColumn(name = "RESPUESTA_CONFIRMACION_ID")
	private es.caib.gusite.micromodel.Respuesta respuestaConfirmacion;

	/** full constructor */
	public Convocatoria(String nombre, String descripcion, Date ultimoEnvio,
			Boolean envioSiError, Boolean envioSiConfirmado,
			String otrosDestinatarios, String asuntoMsg, String textoMsg,
			es.caib.gusite.micromodel.Microsite microsite,
			es.caib.gusite.micromodel.Encuesta encuesta,
			es.caib.gusite.micromodel.Respuesta respuestaCorreo,
			es.caib.gusite.micromodel.Pregunta preguntaConfirmacion,
			es.caib.gusite.micromodel.Respuesta respuestaConfirmacion,
			Set<DistribucionConvocatoria> destinatarios) {
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.ultimoEnvio = ultimoEnvio;
		this.envioSiError = envioSiError;
		this.envioSiConfirmado = envioSiConfirmado;
		this.destinatarios = destinatarios;
		this.otrosDestinatarios = otrosDestinatarios;
		this.asuntoMsg = asuntoMsg;
		this.textoMsg = textoMsg;
		this.microsite = microsite;
		this.encuesta = encuesta;
		this.respuestaCorreo = respuestaCorreo;
		this.preguntaConfirmacion = preguntaConfirmacion;
		this.respuestaConfirmacion = respuestaConfirmacion;
	}

	/** default constructor */
	public Convocatoria() {
		this.destinatarios = Collections.EMPTY_SET;
	}

	/** minimal constructor */
	public Convocatoria(Set<DistribucionConvocatoria> destinatarios) {
		this.destinatarios = destinatarios;
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

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Date getUltimoEnvio() {
		return this.ultimoEnvio;
	}

	public void setUltimoEnvio(Date ultimoEnvio) {
		this.ultimoEnvio = ultimoEnvio;
	}

	public Boolean getEnvioSiError() {
		return this.envioSiError;
	}

	public void setEnvioSiError(Boolean envioSiError) {
		this.envioSiError = envioSiError;
	}

	public Boolean getEnvioSiConfirmado() {
		return this.envioSiConfirmado;
	}

	public void setEnvioSiConfirmado(Boolean envioSiConfirmado) {
		this.envioSiConfirmado = envioSiConfirmado;
	}

	public String getOtrosDestinatarios() {
		return this.otrosDestinatarios;
	}

	public void setOtrosDestinatarios(String otrosDestinatarios) {
		this.otrosDestinatarios = otrosDestinatarios;
	}

	public String getAsuntoMsg() {
		return this.asuntoMsg;
	}

	public void setAsuntoMsg(String asuntoMsg) {
		this.asuntoMsg = asuntoMsg;
	}

	public String getTextoMsg() {
		return this.textoMsg;
	}

	public void setTextoMsg(String textoMsg) {
		this.textoMsg = textoMsg;
	}

	public es.caib.gusite.micromodel.Microsite getMicrosite() {
		return this.microsite;
	}

	public void setMicrosite(es.caib.gusite.micromodel.Microsite microsite) {
		this.microsite = microsite;
	}

	public es.caib.gusite.micromodel.Encuesta getEncuesta() {
		return this.encuesta;
	}

	public void setEncuesta(es.caib.gusite.micromodel.Encuesta encuesta) {
		this.encuesta = encuesta;
	}

	public es.caib.gusite.micromodel.Respuesta getRespuestaCorreo() {
		return this.respuestaCorreo;
	}

	public void setRespuestaCorreo(
			es.caib.gusite.micromodel.Respuesta respuestaCorreo) {
		this.respuestaCorreo = respuestaCorreo;
	}

	public es.caib.gusite.micromodel.Pregunta getPreguntaConfirmacion() {
		return this.preguntaConfirmacion;
	}

	public void setPreguntaConfirmacion(
			es.caib.gusite.micromodel.Pregunta preguntaConfirmacion) {
		this.preguntaConfirmacion = preguntaConfirmacion;
	}

	public es.caib.gusite.micromodel.Respuesta getRespuestaConfirmacion() {
		return this.respuestaConfirmacion;
	}

	public void setRespuestaConfirmacion(
			es.caib.gusite.micromodel.Respuesta respuestaConfirmacion) {
		this.respuestaConfirmacion = respuestaConfirmacion;
	}

	public Set<DistribucionConvocatoria> getDestinatarios() {
		return this.destinatarios;
	}

	public void setDestinatarios(Set<DistribucionConvocatoria> destinatarios) {
		this.destinatarios = destinatarios;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("id", this.getId()).toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Convocatoria)) {
			return false;
		}
		Convocatoria other = (Convocatoria) obj;
		if (this.id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!this.id.equals(other.id)) {
			return false;
		}
		return true;
	}

}
