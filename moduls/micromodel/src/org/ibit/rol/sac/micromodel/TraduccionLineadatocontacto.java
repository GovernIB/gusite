package org.ibit.rol.sac.micromodel;

/**
 * Clase TraduccionLineadatocontacto. Encapsula los datos  que pueden tener valor en diferentes idiomas del objeto Lineadatocontacto.
 * @author Indra
 */
public class TraduccionLineadatocontacto  implements Traduccion{

	private static final long serialVersionUID = -2647818259513112558L;

	public String getTexto(){
        return texto;
    }

    public void setTexto(String texto){
        this.texto = texto;
    }

    private String texto;
}