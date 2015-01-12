package org.ibit.rol.sac.micromodel;

/**
 * Clase Accesibilidad. Bean que contiene información de la accesibilidad de un microsite. 
 * Modela la tabla de BBDD GUS_W3C
 * @author Indra
 *
 */
public class Accesibilidad implements ValueObject {

	
	private static final long serialVersionUID = -3612093597829849842L;
	
	public static final String RES_OK = "1";
	public static final String RES_WARN = "2";
	public static final String RES_ERROR = "3";
	
	public static final String MES_SINMEDIA = "0";
	
	private Long id;
    private String servicio;
    private String resultado;
    private String mensaje;
    private String medida;
    private String idioma;
    private Long iditem;
    private Long codmicro;
    private String tawresultado;
    private String tawmensaje;
    
    /**
     * Constructor de la clase.
     */
    public Accesibilidad(){}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getServicio() {
		return servicio;
	}


	public void setServicio(String servicio) {
		this.servicio = servicio;
	}


	public String getResultado() {
		return resultado;
	}


	public void setResultado(String resultado) {
		this.resultado = resultado;
	}

	public String getMensaje() {
		return mensaje;
	}


	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public String getMedida() {
		return medida;
	}

	public void setMedida(String medida) {
		this.medida = medida;
	}

	public Long getIditem() {
		return iditem;
	}

	public void setIditem(Long iditem) {
		this.iditem = iditem;
	}

	public String getIdioma() {
		return idioma;
	}

	public void setIdioma(String idioma) {
		this.idioma = idioma;
	}

	public Long getCodmicro() {
		return codmicro;
	}

	public void setCodmicro(Long codmicro) {
		this.codmicro = codmicro;
	}

	public String getTawresultado() {
		return tawresultado;
	}

	public void setTawresultado(String tawresultado) {
		this.tawresultado = tawresultado;
	}

	public String getTawmensaje() {
		return tawmensaje;
	}

	public void setTawmensaje(String tawmensaje) {
		this.tawmensaje = tawmensaje;
	}
	
}
