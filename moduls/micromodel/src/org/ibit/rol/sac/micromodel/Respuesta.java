package org.ibit.rol.sac.micromodel;

import java.util.HashSet;
import java.util.Set;

/**
 * Clase Respuesta. Bean que define una Respuesta de la respuesta de una encuesta.
 * Modela la tabla de BBDD GUS_RESPUS.
 * @author Indra
 */
public class Respuesta extends Traducible {

	private static final long serialVersionUID = 2967918550911471091L;
	private Long id;
    private Long idpregunta;
    private Integer orden;
    private Integer nrespuestas;
    private String tipo;
    
	
	private Set respuestadato = new HashSet();
	
	public Set getRespuestadato() {
		return respuestadato;
	}

	public void setRespuestadato(Set respuestadato) {
		this.respuestadato = respuestadato;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdpregunta() {
		return idpregunta;
	}

	public void setIdpregunta(Long idpregunta) {
		this.idpregunta = idpregunta;
	}

	public Integer getNrespuestas() {
		return nrespuestas;
	}

	public void setNrespuestas(Integer nrespuestas) {
		this.nrespuestas = nrespuestas;
	}

	public Integer getOrden() {
		return orden;
	}

	public void setOrden(Integer orden) {
		this.orden = orden;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public void addTraduccionMap(String lang, TraduccionRespuesta traduccion) {
        setTraduccion(lang, traduccion);
    }


}