package org.ibit.rol.sac.micromodel;

/**
 * Clase TraduccionRespuesta. Encapsula los datos  que pueden tener valor en diferentes idiomas del objeto Respuesta.
 * @author Indra
 */
public class TraduccionRespuesta implements Traduccion {

	private static final long serialVersionUID = 6426636976073934777L;
	private String titulo;
	
    public String getTitulo(){
    	return titulo;
    }

    public void setTitulo(String titulo){
        this.titulo = titulo;
    }
}
