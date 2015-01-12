package org.ibit.rol.sac.micromodel;

/**
 * Clase Usuario. Bean que define un Usuario. 
 * Modela la tabla de BBDD GUS_MUSUAR.
 * @author Indra
 */
public class Usuario implements ValueObject {

	private static final long serialVersionUID = -6526246228389188142L;
	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    private Long id;
    private String username;
    private String password;
    private String nombre;
    private String observaciones;
    private String perfil;

}
