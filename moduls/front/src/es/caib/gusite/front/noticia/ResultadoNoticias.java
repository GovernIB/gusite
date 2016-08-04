package es.caib.gusite.front.noticia;

import java.util.List;
import java.util.Map;

import es.caib.gusite.front.general.bean.ErrorMicrosite;
import es.caib.gusite.front.general.bean.ResultadoBusqueda;

public class ResultadoNoticias<T> extends ResultadoBusqueda<T> {

	private static final long serialVersionUID = 1L;

	private boolean busqueda;

	public ResultadoNoticias() {
		super();
	}

	public ResultadoNoticias(ErrorMicrosite errorMicrosite) {
		super(errorMicrosite);
	}

	public ResultadoNoticias(List<T> datos, Map<String, Integer> parametros) {
		super(datos, parametros);
	}

	public void setBusqueda(boolean busqueda) {
		this.busqueda = busqueda;
	}

	public boolean isBusqueda() {
		return this.busqueda;
	}

}
