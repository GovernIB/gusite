package es.caib.gusite.front.view;

import java.util.Map;

import es.caib.gusite.micromodel.Encuesta;

/**
 * Página de formulario de encuesta.
 * @author at4.net
 *
 */
@TemplateView(TemplateView.ENCUESTA_ENCUESTA)
public class EncuestaView extends PageView {

	private Boolean manteniment;
	private boolean fechasVigente;
	private Encuesta encuesta;
	private Map<?,?> respostesFixades;
	private String identificacion;

	public void setManteniment(Boolean mant) {
		this.manteniment = mant;
	}

	public void setFechasVigente(boolean vigente) {
		this.fechasVigente = vigente;
	}

	public void setEncuesta(Encuesta encuesta) {
		this.encuesta = encuesta;
	}

	public void setRespostesFixades(Map<?,?> param) {
		this.respostesFixades = param;
	}

	public void setIdentificacion(String identificacio) {
		this.identificacion = identificacio;

	}

	/**
	 * Indica que la encuesta está en modo de mantenimiento
	 * @return
	 */
	@Variable("MVS_manteniment")
	public Boolean getManteniment() {
		return manteniment;
	}

	/**
	 * Indica que la encuesta está vigente
	 * @return
	 */
	@Variable("MVS_Fechas_vigente")
	public boolean isFechasVigente() {
		return fechasVigente;
	}

	/**
	 * Encuesta a mostrar
	 * @return
	 */
	@Variable("MVS_encuesta")
	public Encuesta getEncuesta() {
		return encuesta;
	}

	/**
	 * Preguntas que vienen perfijadas en una encuesta 
	 * TODO: esto tiene que ver con el procesamiento que se hacía en UserRequestProcessor en microfront
	 */
	@Variable("MVS_mapRespostesFixades")
	public Map getRespostesFixades() {
		return respostesFixades;
	}

	/**
	 * Identificación del usuario en caso de encuestas autenticadas
	 * @return
	 */
	@Variable("MVS_identificacion")
	public String getIdentificacion() {
		return identificacion;
	}

}
