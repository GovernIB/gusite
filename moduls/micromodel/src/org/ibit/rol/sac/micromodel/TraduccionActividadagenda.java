package org.ibit.rol.sac.micromodel;

/**
 * Clase TraduccionActividadagenda. Encapsula los datos  que pueden tener valor en diferentes idiomas del objeto Actividadagenda.
 * @author Indra
 */
public class TraduccionActividadagenda  implements Traduccion{

	private static final long serialVersionUID = -7351780786615474210L;

	public TraduccionActividadagenda()   { }
	
	private String nombre;

    public String getNombre()
    {
        return nombre;
    }

    public void setNombre(String nombre)
    {
        this.nombre = nombre;
    }
    
}