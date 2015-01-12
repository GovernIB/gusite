package es.caib.gusite.micromodel;

/**
 * Clase EstadísticaGroup. 
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
		return accesos;
	}
	public void setAccesos(int accesos) {
		this.accesos = accesos;
	}
	public MicrositeCompleto getMicrosite() {
		return microsite;
	}
	public void setMicrosite(MicrositeCompleto microsite) {
		this.microsite = microsite;
	}
	public Long getItem() {
		return item;
	}
	public void setItem(Long item) {
		this.item = item;
	}
	public int getMes() {
		return mes;
	}
	public void setMes(int mes) {
		this.mes = mes;
	}
	public String getReferencia() {
		return referencia;
	}
	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}
	public String getNombreservicio() {
		return nombreservicio;
	}
	public void setNombreservicio(String nombreservicio) {
		this.nombreservicio = nombreservicio;
	}
	public String getTituloitem() {
		return tituloitem;
	}
	public void setTituloitem(String tituloitem) {
		this.tituloitem = tituloitem;
	}
	public int getPublico() {
		return publico;
	}
	public void setPublico(int publico) {
		this.publico = publico;
	}
       
}
