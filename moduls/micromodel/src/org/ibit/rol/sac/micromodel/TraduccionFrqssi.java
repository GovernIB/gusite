package org.ibit.rol.sac.micromodel;

/**
 * Clase TraduccionFrqssi. Encapsula los datos  que pueden tener valor en diferentes idiomas del objeto Frqssi.
 * @author Indra
 */
public class TraduccionFrqssi  implements Traduccion{

	private static final long serialVersionUID = -633154459030254267L;

	public TraduccionFrqssi()   { }
	
	private String nombre;

    public String getNombre(){
        return nombre;
    }

    public void setNombre(String nombre){
        this.nombre = nombre;
    }
    
}