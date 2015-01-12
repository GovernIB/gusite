package org.ibit.rol.sac.micromodel;

/**
 * Clase UsuarioPropietarioMicrosite. Bean que define un usuario propietario de un Microsite. 
 * Modela la tabla de BBDD GUS_MICUSU.
 * @author Indra
 */
public class UsuarioPropietarioMicrosite implements ValueObject {

	private static final long serialVersionUID = -9010452718936775987L;
	private Long id;
	private Long idmicrosite;
	private Long idusuario;
	
	public UsuarioPropietarioMicrosite() { }
	
	/**
	 * Constructor de la clase.
	 * @param idmicrosite
	 * @param idusuario
	 */
	public UsuarioPropietarioMicrosite(Long idmicrosite, Long idusuario) {
		super();
		this.idmicrosite = idmicrosite;
		this.idusuario = idusuario;
	}
	
	public Long getIdmicrosite() {
		return idmicrosite;
	}
	public void setIdmicrosite(Long idmicrosite) {
		this.idmicrosite = idmicrosite;
	}
	public Long getIdusuario() {
		return idusuario;
	}
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
		
}
