package org.ibit.rol.sac.micromodel;

/**
 * Clase TraduccionMenu. Encapsula los datos  que pueden tener valor en diferentes idiomas del objeto Menu.
 * @author Indra
 */
public class TraduccionMenu  implements Traduccion{

	private static final long serialVersionUID = 8119316534600598810L;

	public TraduccionMenu(){
    }

    public String getNombre(){
        return nombre;
    }

    public void setNombre(String nombre){
        this.nombre = nombre;
    }

    private String nombre;
}