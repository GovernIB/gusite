package org.ibit.rol.sac.micromodel;

/**
 * Clase TraduccionPregunta. Encapsula los datos  que pueden tener valor en diferentes idiomas del objeto Pregunta.
 * @author Indra
 */
public class TraduccionPregunta implements Traduccion {

	private static final long serialVersionUID = 4201974686622631379L;
	private String titulo;
	
    public String getTitulo(){
    	return titulo;
    }

    public void setTitulo(String titulo){
        this.titulo = titulo;
    }

}