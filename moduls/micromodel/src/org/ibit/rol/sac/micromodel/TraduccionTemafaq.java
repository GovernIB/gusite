package org.ibit.rol.sac.micromodel;

/**
 * Clase TraduccionTemafaq. Encapsula los datos  que pueden tener valor en diferentes idiomas del objeto Tema.
 * @author Indra
 */
public class TraduccionTemafaq  implements Traduccion{

	private static final long serialVersionUID = -1492821805120870638L;

	public TraduccionTemafaq()   { }
	
	private String nombre;

    public String getNombre(){
        return nombre;
    }

    public void setNombre(String nombre){
        this.nombre = nombre;
    }
    
}