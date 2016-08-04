package es.caib.gusite.front.general;

public class BaseCriteria {

	private String filtro;
	private String ordenacion;
	private int pagina = 1;

	public BaseCriteria(String filtro2, int pagina2, String ordenacion2) {
		this.filtro = filtro2;
		this.pagina = pagina2;
		this.ordenacion = ordenacion2;
	}
	public BaseCriteria() {
	}
	
	
	public void setFiltro(String filtro) {
		this.filtro = filtro;
	}

	public String getFiltro() {
		return this.filtro;
	}

	public void setOrdenacion(String ordenacion) {
		this.ordenacion = ordenacion;
	}

	public String getOrdenacion() {
		return this.ordenacion;
	}

	public void setPagina(int pagina) {
		this.pagina = pagina;
	}

	public int getPagina() {
		return this.pagina;
	}

}
