package es.caib.gusite.front.general.bean;

/**
 * 
 * Clase ErrorMicrosite. Se definen Mensajes de error genéricos.
 * 
 * @author Indra
 * 
 */
public class ErrorMicrosite {

	private String aviso = "";
	private String mensaje = "";
	private String descripcion = "";
	private String accion = "";
	private String estado = "";

	// Ambit de Error

	static public final String ERROR_AMBIT_MICRO = "micro";
	static public final String ERROR_AMBIT_PAGINA = "pagina";
	static public final String ERROR_AMBIT_DOCUMENT = "document";
	static public final String ERROR_AMBIT_ACCES = "acces";
	static public final String ERROR_AMBIT_SESSIO = "sessio";

	// Mensajes de Error - Forwarding

	static public final String ERROR_MICRO_TIT = "Error de Microsite";
	static public final String ERROR_MICRO_MSG = "No s' ha trobat el microsite amb id ";
	static public final String ERROR_MICRO_MSG_NULL = "No s' ha sol·licitat un microsite";

	static public final String ERROR_PAGINA_TIT = "Error de Pàgina";
	static public final String ERROR_PAGINA_MSG = "No s' ha trobat la pàgina sol·licitada";

	static public final String ERROR_DOCU_TIT = "Error de Document";
	static public final String ERROR_DOCU_MSG = "No s' ha trobat el document sol·licitat";

	static public final String ERROR_ACCES_TIT = "Error d' Accés";
	static public final String ERROR_ACCES_MSG = "No disposa del ROL necessari per accedir al microsite.";

	static public final String ERROR_SESSIO_TIT = "Error de Sessió";
	static public final String ERROR_SESSIO_MSG = "Ha excedit el temps màxim de sessió.";
	
	static public final String ESTADO_NOT_FOUNT = "404";
	static public final String ESTADO_FORBIDDEN = "403";
	static public final String ESTADO_SESSION = "440";

	public ErrorMicrosite() {
	}

	/**
	 * Constructor de la clase.
	 * 
	 * @param aviso
	 */
	public ErrorMicrosite(String aviso) {
		this(aviso, "", "", "","");
	}

	/**
	 * Constructor de la clase.
	 * 
	 * @param aviso
	 *            un Aviso
	 * @param mensaje
	 *            un Mensaje de error
	 */
	public ErrorMicrosite(String aviso, String mensaje) {
		this(aviso, mensaje, "", "","");
	}

	/**
	 * Constructor de la clase.
	 * 
	 * @param aviso
	 *            un aviso
	 * @param mensaje
	 *            un mensaje
	 * @param descripcion
	 *            una descripción
	 */
	public ErrorMicrosite(String aviso, String mensaje, String descripcion) {
		this(aviso, mensaje, descripcion, "","");
	}

	/**
	 * Constructor de la clase.
	 * 
	 * @param aviso
	 * @param mensaje
	 * @param descripcion
	 * @param accion
	 */
	public ErrorMicrosite(String aviso, String mensaje, String descripcion, String accion, String estado) {
		this.aviso = aviso;
		this.mensaje = mensaje;
		this.descripcion = descripcion;
		this.accion = accion;
		this.estado = estado;
	}

	public String getAviso() {
		return this.aviso;
	}

	public void setAviso(String aviso) {
		this.aviso = aviso;
	}

	public String getAccion() {
		return this.accion;
	}

	public void setAccion(String accion) {
		this.accion = accion;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getMensaje() {
		return this.mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

}
