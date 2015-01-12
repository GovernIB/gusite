package org.ibit.rol.sac.micromodel;

/**
 * Clase UsuarioEncuesta. Bean que define usuario que ha contestado una encuesta.
 * Modela la tabla de BBDD GUS_USUARIENC.
 * @author pedro melia
 */
public class UsuarioEncuesta implements ValueObject {

	private static final long serialVersionUID = 1L;
    
	private Long id;
    private String nombre;
    private String observaciones;
	private String dni;
	/**
	 *  Descripci�n: Devuelve el Id del usuario
	 * @return Id del usuario
	 */
	public Long getId() {
        return id;
    }
	/**
	 *  Descripci�n: Establece el Id del usuario
	 * @param id del usario
	 */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Descripci�n: Devuelve Nombre del usuario
     * @return nombre del usuario 
     */
    public String getNombre() {
        return nombre;
    }

    /**
     *  Descripci�n: Establece el nombre del usuario 
     * @param nombre nombre del usario
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    /**
     * Descripci�n: Devuelve Observaciones del usuario
     * @return  observaciones
     */
    public String getObservaciones() {
        return observaciones;
    }
    
	/**
	 *  Descripci�n: Establece Observaciones del usuario
	 * @param observaciones
	 */
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
    /**
     * Descripcion: Establece el dni del que contesta la encuesta
     * @return dni
     */
	public String getDni() {
		return dni;
	}
	
    /**
     * Descripcion: Establece el dni del que contesta la encuesta
     * @param dni
     */
	public void setDni(String dni) {
		this.dni = dni;
	}
   
}
