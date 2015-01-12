package org.ibit.rol.sac.micromodel;

/**
 * Clase TraduccionEncuesta. Encapsula los datos  que pueden tener valor en diferentes idiomas del objeto Encuesta.
 * @author Indra
 */
public class TraduccionEncuesta implements Traduccion {

	private static final long serialVersionUID = 8202221844570423619L;
	private String titulo;
	
    public String getTitulo(){
    	return titulo;
    }

    public void setTitulo(String titulo){
        this.titulo = titulo;
    }
}