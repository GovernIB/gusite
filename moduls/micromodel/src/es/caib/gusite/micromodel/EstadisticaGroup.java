package es.caib.gusite.micromodel;

/**
 * Clase EstadísticaGroup.
 * 
 * @author Indra
 */
public class EstadisticaGroup implements ValueObject {

	private static final long serialVersionUID = -592414666202730199L;

	private Long item;
	private int mes;
	private String referencia;
	private String nombreservicio;
	private String tituloitem;
	private int accesos;
	private MicrositeCompleto microsite;
	private int publico;

	public int getAccesos() {
		return this.accesos;
	}

	public void setAccesos(int accesos) {
		this.accesos = accesos;
	}

	public MicrositeCompleto getMicrosite() {
		return this.microsite;
	}

	public void setMicrosite(MicrositeCompleto microsite) {
		this.microsite = microsite;
	}

	public Long getItem() {
		return this.item;
	}

	public void setItem(Long item) {
		this.item = item;
	}

	public int getMes() {
		return this.mes;
	}

	public void setMes(int mes) {
		this.mes = mes;
	}

	public String getReferencia() {
		return this.referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public String getNombreservicio() {
		return this.nombreservicio;
	}

	public void setNombreservicio(String nombreservicio) {
		this.nombreservicio = nombreservicio;
	}

	public String getTituloitem() {
		return this.tituloitem;
	}

	public void setTituloitem(String tituloitem) {
		this.tituloitem = tituloitem;
	}

	public int getPublico() {
		return this.publico;
	}

	public void setPublico(int publico) {
		this.publico = publico;
	}

}
