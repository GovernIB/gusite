package org.ibit.rol.sac.micropersistence.intf;

import java.util.Map;

/**
 * Interface para definir un dominio
 * 
 * @author INDRA
 *
 */
public interface DominioInterface {

	public Map<?, ?> obtenerListado(String id, Map<?, ?> parametros);
}
