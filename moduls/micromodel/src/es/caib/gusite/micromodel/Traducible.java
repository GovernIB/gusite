package es.caib.gusite.micromodel;

import java.util.HashMap;
import java.util.Map;

/**
 * Clase Traducible.
 * 
 * @author Indra
 */
public class Traducible implements ValueObject {

	private static final long serialVersionUID = 7382393780090696827L;

	protected String idi = Idioma.getIdiomaPorDefecto();

	protected Map traducciones = new HashMap();

	protected Map getTraducciones() {
		return this.traducciones;
	}

	protected void setTraducciones(Map traducciones) {
		this.traducciones = traducciones;
	}

	/**
	 * Obtiene la traduccion por defecto.
	 * 
	 * @return La traduccion en el idioma per defecto.
	 */
	public Traduccion getTraduccion() {
		return (Traduccion) this.traducciones.get(Idioma.getIdiomaPorDefecto());
	}

	/**
	 * Obtiene la traduccion en un idioma determinado o <code>null</code>.
	 * 
	 * @param idioma
	 *            Idioma de la traducción.
	 * @return Traduccion en el idioma indicado o <code>null</code> si no
	 *         existe.
	 */
	public Traduccion getTraduccion(String idioma) {
		return (Traduccion) this.traducciones.get(idioma);
	}

	/**
	 * Fija una traducción en un idioma determinado, o la borra si es
	 * <code>null</code>.
	 * 
	 * @param idioma
	 *            Idioma de la traducción,
	 * @param traduccion
	 *            La traducción a fijar.
	 */
	public void setTraduccion(String idioma, Traduccion traduccion) {
		if (traduccion == null) {
			this.traducciones.remove(idioma);
		} else {
			this.traducciones.put(idioma, traduccion);
		}
	}

	/**
	 * Obtiene la traduccion por defecto.
	 * 
	 * @return La traduccion en el idioma per defecto.
	 */
	public Traduccion getTraduce() {
		return (Traduccion) this.traducciones.get(this.idi);
	}

	public Map getTraduccionMap() {
		return this.traducciones;
	}

	public void setTraduccionMap(Map traduccionMap) {
		this.traducciones = new HashMap(traduccionMap);
	}

	public void setIdi(String idi) {
		this.idi = idi;
	}

}
