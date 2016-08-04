package es.caib.gusite.micromodel;

import java.util.Map;

/**
 * Clase Traducible.
 * 
 * @author Indra
 */
public interface Traducible2 extends ValueObject {

	public Map getTraducciones();

	public void setTraducciones(Map traducciones);

	public Traduccion getTraduccion();

	public Traduccion getTraduccion(String idioma);

	public void setTraduccion(String idioma, Traduccion traduccion);

	public Traduccion getTraduce();

	public Map getTraduccionMap();

	public void setTraduccionMap(Map traduccionMap);

	public void setIdi(String idi);

}
