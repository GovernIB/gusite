package org.ibit.rol.sac.micromodel;

/**
 * Clase TraduccionTipo. Encapsula los datos  que pueden tener valor en diferentes idiomas del objeto Tipo.
 * @author Indra
 */
public class TraduccionTipo  implements Traduccion{

	private static final long serialVersionUID = 3384380781846326766L;

	public TraduccionTipo()   { }
	
	private String nombre;

    public String getNombre(){
        return nombre;
    }

    public void setNombre(String nombre){
        this.nombre = nombre;
    }
    
}