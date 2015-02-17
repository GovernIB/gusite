package es.caib.gusite.front.noticia;

import es.caib.gusite.front.general.BaseCriteria;
import es.caib.gusite.micromodel.Tipo;

public class NoticiaCriteria extends BaseCriteria {

	public NoticiaCriteria(String filtro, int pagina, String ordenacion) {
		super(filtro, pagina, ordenacion);
	}

	public NoticiaCriteria(String filtro, int pagina, String ordenacion,
			int anyo) {
		super(filtro, pagina, ordenacion);
		this.anyo = anyo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

	public Tipo getTipo() {
		return this.tipo;
	}

	private Tipo tipo;
	private int anyo = 0;

	public void setAnyo(int anyo) {
		this.anyo = anyo;
	}

	public int getAnyo() {
		return this.anyo;
	}

}
