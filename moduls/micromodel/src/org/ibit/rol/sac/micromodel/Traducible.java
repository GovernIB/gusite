package org.ibit.rol.sac.micromodel;

import java.util.Map;
import java.util.HashMap;

/**
 * Clase Traducible. 
 * @author Indra
 */
public class Traducible implements ValueObject {

	private static final long serialVersionUID = 7382393780090696827L;

	private String idi=Idioma.DEFAULT;
	
    private Map traducciones = new HashMap();

    protected Map getTraducciones() {
        return traducciones;
    }

    protected void setTraducciones(Map traducciones) {
        this.traducciones = traducciones;
    }

    /**
     * Obtiene la traduccion por defecto.
     * @return La traduccion en el idioma per defecto.
     */
    public Traduccion getTraduccion() {
        return (Traduccion) traducciones.get(Idioma.DEFAULT);
    }

    /**
     * Obtiene la traduccion en un idioma determinado o <code>null</code>.
     * @param idioma Idioma de la traducción.
     * @return Traduccion en el idioma indicado o <code>null</code> si no existe.
     */
    public Traduccion getTraduccion(String idioma) {
        return (Traduccion) traducciones.get(idioma);
    }
    
    /**
     * Fija una traducción en un idioma determinado, o la borra si es <code>null</code>.
     * @param idioma Idioma de la traducción,
     * @param traduccion La traducción a fijar.
     */
    public void setTraduccion(String idioma, Traduccion traduccion) {
        if (traduccion == null) {
            traducciones.remove(idioma);
        } else {
            traducciones.put(idioma, traduccion);
        }
    }
   
    /**
     * Obtiene la traduccion por defecto.
     * @return La traduccion en el idioma per defecto.
     */
    public Traduccion getTraduce() {
        return (Traduccion) traducciones.get(idi);
    }
 
    public Map getTraduccionMap() {
        return traducciones;
    }

    public void setTraduccionMap(Map traduccionMap) {
        this.traducciones = new HashMap(traduccionMap);
    }

    public void setIdi(String idi) {
    	this.idi=idi;
    }
 
}

