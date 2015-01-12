package org.ibit.rol.sac.micromodel;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class Convocatoria implements Serializable {

    /** identifier field */
    private Long id;

    /** nullable persistent field */
    private String nombre;

    /** nullable persistent field */
    private String descripcion;

    /** nullable persistent field */
    private Date ultimoEnvio;

    /** nullable persistent field */
    private Boolean envioSiError;

    /** nullable persistent field */
    private Boolean envioSiConfirmado;

    /** persistent field */
    private Set destinatarios;
    
    /** nullable persistent field */
    private String otrosDestinatarios;

    /** nullable persistent field */
    private String asuntoMsg;

    /** nullable persistent field */
    private String textoMsg;

    /** nullable persistent field */
    private org.ibit.rol.sac.micromodel.Microsite microsite;

    /** nullable persistent field */
    private org.ibit.rol.sac.micromodel.Encuesta encuesta;

    /** nullable persistent field */
    private org.ibit.rol.sac.micromodel.Respuesta respuestaCorreo;

    /** nullable persistent field */
    private org.ibit.rol.sac.micromodel.Pregunta preguntaConfirmacion;

    /** nullable persistent field */
    private org.ibit.rol.sac.micromodel.Respuesta respuestaConfirmacion;

    /** full constructor */
    public Convocatoria(String nombre, String descripcion, Date ultimoEnvio, Boolean envioSiError, Boolean envioSiConfirmado, String otrosDestinatarios, String asuntoMsg, String textoMsg, org.ibit.rol.sac.micromodel.Microsite microsite, org.ibit.rol.sac.micromodel.Encuesta encuesta, org.ibit.rol.sac.micromodel.Respuesta respuestaCorreo, org.ibit.rol.sac.micromodel.Pregunta preguntaConfirmacion, org.ibit.rol.sac.micromodel.Respuesta respuestaConfirmacion, Set destinatarios) {
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
    public Convocatoria(Set destinatarios) {
        this.destinatarios = destinatarios;
    }

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

    public org.ibit.rol.sac.micromodel.Microsite getMicrosite() {
        return this.microsite;
    }

    public void setMicrosite(org.ibit.rol.sac.micromodel.Microsite microsite) {
        this.microsite = microsite;
    }

    public org.ibit.rol.sac.micromodel.Encuesta getEncuesta() {
        return this.encuesta;
    }

    public void setEncuesta(org.ibit.rol.sac.micromodel.Encuesta encuesta) {
        this.encuesta = encuesta;
    }

    public org.ibit.rol.sac.micromodel.Respuesta getRespuestaCorreo() {
        return this.respuestaCorreo;
    }

    public void setRespuestaCorreo(org.ibit.rol.sac.micromodel.Respuesta respuestaCorreo) {
        this.respuestaCorreo = respuestaCorreo;
    }

    public org.ibit.rol.sac.micromodel.Pregunta getPreguntaConfirmacion() {
        return this.preguntaConfirmacion;
    }

    public void setPreguntaConfirmacion(org.ibit.rol.sac.micromodel.Pregunta preguntaConfirmacion) {
        this.preguntaConfirmacion = preguntaConfirmacion;
    }

    public org.ibit.rol.sac.micromodel.Respuesta getRespuestaConfirmacion() {
        return this.respuestaConfirmacion;
    }

    public void setRespuestaConfirmacion(org.ibit.rol.sac.micromodel.Respuesta respuestaConfirmacion) {
        this.respuestaConfirmacion = respuestaConfirmacion;
    }

    public Set getDestinatarios() {
        return this.destinatarios;
    }

    public void setDestinatarios(Set destinatarios) {
        this.destinatarios = destinatarios;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Convocatoria))
			return false;
		Convocatoria other = (Convocatoria) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
