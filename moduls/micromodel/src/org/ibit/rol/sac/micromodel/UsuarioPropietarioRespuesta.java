package org.ibit.rol.sac.micromodel;

public class UsuarioPropietarioRespuesta implements ValueObject {

	private static final long serialVersionUID = 1L;
	
	private Long idrespuesta;
	private Long idusuario;
	
	public UsuarioPropietarioRespuesta() { }
	
	/**
	 * Constructor de la clase.
	 * 
	 * @param idusuario id de un usuario que ha rellenado la encuesta
	 * @param idrespuesta id de la respuesta de la encuesta
	 */
	public UsuarioPropietarioRespuesta(Long idusuario, Long idrespuesta) {
		super();
		this.idrespuesta = idrespuesta;
		this.idusuario = idusuario;
	}
	
	/**
	 *  Descripción: Devuelve id de la respuesta.
	 * @return  id de la respuesta
	 */
	public Long getIdrespuesta() {
		return idrespuesta;
	}
	
	/**
	 *  Descripción: Establece id de la respuesta.
	 * @param idrespuesta
	 */
	public void setIdrespuesta(Long idrespuesta) {
		this.idrespuesta = idrespuesta;
	}
	
	/**
	 *  Descripción: Devuelve id del usuario.
	 * @return id del usuario
	 */
	public Long getIdusuario() {
		return idusuario;
	}
	
	/**
	 *  Descripción: Establece id del usuario.
	 * @param idusuario
	 */
	public void setIdusuario(Long idusuario) {
		this.idusuario = idusuario;
	}
	

	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	public int hashCode() {
		return super.hashCode();
	}

	public String toString() {
		return super.toString();
	}
	
	
}
