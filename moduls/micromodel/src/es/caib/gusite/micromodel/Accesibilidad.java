package es.caib.gusite.micromodel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Clase Accesibilidad. Bean que contiene información de la accesibilidad de un
 * microsite. Modela la tabla de BBDD GUS_W3C
 * 
 * @author Indra
 * 
 */
@Entity
@Table(name = "GUS_W3C")
public class Accesibilidad extends AuditableModel implements ValueObject {

	private static final long serialVersionUID = -3612093597829849842L;

	public static final String RES_OK = "1";
	public static final String RES_WARN = "2";
	public static final String RES_ERROR = "3";

	public static final String MES_SINMEDIA = "0";
	@Id
	@SequenceGenerator(name = "GUS_ACCESIBILIDAD_ID_GENERATOR", sequenceName = "GUS_SEQCON", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GUS_ACCESIBILIDAD_ID_GENERATOR")
	@Column(name = "W3C_CODI")
	private Long id;
	@Column(name = "W3C_SERVIC")
	private String servicio;
	@Column(name = "W3C_RESULT")
	private String resultado;
	@Column(name = "W3C_MENSA")
	private String mensaje;
	@Column(name = "W3C_MESURA")
	private String medida;
	@Column(name = "W3C_IDIOMA")
	private String idioma;
	@Column(name = "W3C_IDITEM")
	private Long iditem;
	@Column(name = "W3C_MICCOD")
	private Long codmicro;
	@Column(name = "W3C_TAWRES")
	private String tawresultado;
	@Column(name = "W3C_TAWMEN")
	private String tawmensaje;

	/**
	 * Constructor de la clase.
	 */
	public Accesibilidad() {
	}

	@Override
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getServicio() {
		return this.servicio;
	}

	public void setServicio(String servicio) {
		this.servicio = servicio;
	}

	public String getResultado() {
		return this.resultado;
	}

	public void setResultado(String resultado) {
		this.resultado = resultado;
	}

	public String getMensaje() {
		return this.mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public String getMedida() {
		return this.medida;
	}

	public void setMedida(String medida) {
		this.medida = medida;
	}

	public Long getIditem() {
		return this.iditem;
	}

	public void setIditem(Long iditem) {
		this.iditem = iditem;
	}

	public String getIdioma() {
		return this.idioma;
	}

	public void setIdioma(String idioma) {
		this.idioma = idioma;
	}

	public Long getCodmicro() {
		return this.codmicro;
	}

	public void setCodmicro(Long codmicro) {
		this.codmicro = codmicro;
	}

	public String getTawresultado() {
		return this.tawresultado;
	}

	public void setTawresultado(String tawresultado) {
		this.tawresultado = tawresultado;
	}

	public String getTawmensaje() {
		return this.tawmensaje;
	}

	public void setTawmensaje(String tawmensaje) {
		this.tawmensaje = tawmensaje;
	}

}
