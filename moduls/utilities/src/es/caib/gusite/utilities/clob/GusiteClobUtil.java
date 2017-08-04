package es.caib.gusite.utilities.clob;

import es.caib.gusite.utilities.property.GusitePropertiesUtil;
import org.hibernate.Hibernate;
import java.sql.Clob;

/**
 * Clase de utilidad para obtener clob (con un límite de máximo). 
 * @author slromero
 *
 */
public class GusiteClobUtil {
	/**
	 * Obtiene el clob.
	 * @param texto
	 * @return
	 */
	public static Clob getClob(String texto) {
		final Clob clob;
		if (GusitePropertiesUtil.getTamanyoMaximoClob() < texto.length()) {
			final String nuevaDescripcion = texto.substring(0, GusitePropertiesUtil.getTamanyoMaximoClob().intValue());
			clob = Hibernate.createClob(nuevaDescripcion);
		} else {
			clob = Hibernate.createClob(texto);
		}
		return clob;
	}
	
	
}
