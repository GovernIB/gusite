package org.ibit.rol.sac.micromodel;

import java.util.HashSet;
import java.util.Set;

/**
 * Clase Pregunta. Bean que define una Pregunta de una Encuesta
 * Modela la tabla de BBDD GUS_PREGUN.
 * @author Indra
 */
public class Pregunta extends Traducible {

	private static final long serialVersionUID = 8513598333939006319L;
	private Long id;
    private Long idencuesta;
    private Archivo imagen;
    private String multiresp;
    private String visiblecmp;
    private String obligatorio;
    private String visible;
    private Integer orden;
    private Integer nrespuestas;
    private Integer minContestadas;
    private Integer maxContestadas;
	
    private Set respuestas = new HashSet();
    
    public Long getId() {
		return id;
	}
    
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getIdencuesta() {
		return idencuesta;
	}
	
	public void setIdencuesta(Long idencuesta) {
		this.idencuesta = idencuesta;
	}
	
	public Archivo getImagen() {
		return imagen;
	}
	
	public void setImagen(Archivo imagen) {
		this.imagen = imagen;
	}
	
	public String getMultiresp() {
		return multiresp;
	}
	
	public void setMultiresp(String multiresp) {
		this.multiresp = multiresp;
	}
	
	public Integer getNrespuestas() {
		return nrespuestas;
	}
	
	public void setNrespuestas(Integer nrespuestas) {
		this.nrespuestas = nrespuestas;
	}
	
	public String getObligatorio() {
		return obligatorio;
	}
	
	public void setObligatorio(String obligatorio) {
		this.obligatorio = obligatorio;
	}
	
	public Integer getOrden() {
		return orden;
	}
	
	public void setOrden(Integer orden) {
		this.orden = orden;
	}
	
	public String getVisible() {
		return visible;
	}
	
	public void setVisible(String visible) {
		this.visible = visible;
	}
	
	public String getVisiblecmp() {
		return visiblecmp;
	}
	
	public void setVisiblecmp(String visiblecmp) {
		this.visiblecmp = visiblecmp;
	}

	public Set getRespuestas() {
		return respuestas;
	}

	public void setRespuestas(Set respuestas) {
		this.respuestas = respuestas;
	}

	// Metodos para poder leer las colecciones del XML
	
	public void addRespuestas(Respuesta resp) {
		respuestas.add(resp);
	}
	
	public void addTraduccionMap(String lang, TraduccionPregunta traduccion) {
        setTraduccion(lang, traduccion);
    }

	/**
	 * @return the minContestadas
	 */
	public Integer getMinContestadas() {
		return minContestadas;
	}

	/**
	 * @param minContestadas the minContestadas to set
	 */
	public void setMinContestadas(Integer minContestadas) {
		this.minContestadas = minContestadas;
	}

	/**
	 * @return the maxContestadas
	 */
	public Integer getMaxContestadas() {
		return maxContestadas;
	}

	/**
	 * @param maxContestadas the maxContestadas to set
	 */
	public void setMaxContestadas(Integer maxContestadas) {
		this.maxContestadas = maxContestadas;
	}

}