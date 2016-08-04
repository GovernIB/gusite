package es.caib.gusite.front.view;
import java.util.Map;

/**
 * Página de listado multiple con paginación
 * @author at4.net
 *
 */
public class ListPageView extends PageView {

	private String seuletSin;
	private Map<String, Integer> parametrosPagina;

	public void setSeuletSin(String seuletSin) {
		this.seuletSin = seuletSin;

	}

	@Variable("MVS_seulet_sin")
	public String getSeuletSin() {
		return this.seuletSin;
	}

	public void setParametrosPagina(Map<String, Integer> parametros) {
		this.parametrosPagina = parametros;
	}

	@Variable("MVS_parametros_pagina")
	public Map<String, Integer> getParametrosPagina() {
		return this.parametrosPagina;
	}

}
