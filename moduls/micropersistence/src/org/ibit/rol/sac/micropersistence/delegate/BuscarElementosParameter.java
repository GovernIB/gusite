package org.ibit.rol.sac.micropersistence.delegate;

import java.util.HashMap;
import java.util.Map;

public class BuscarElementosParameter {
	public Map<?, ?> parametros;
	public Map<?, ?> traduccion;
	public String idmicrosite;
	public String idtipo;
	public String idioma;
	public String where;

	public BuscarElementosParameter(Map<?, ?> parametros,
			Map<?, ?> traduccion, String idmicrosite, String idtipo,
			String idioma) {
		this.parametros = parametros;
		this.traduccion = traduccion;
		this.idmicrosite = idmicrosite;
		this.idtipo = idtipo;
		this.idioma = idioma;
	}

	public BuscarElementosParameter() {
		parametros = new HashMap<String, String>();
		traduccion = new HashMap<String, String>();
	}
}