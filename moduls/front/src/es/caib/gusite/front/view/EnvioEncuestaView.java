package es.caib.gusite.front.view;

import es.caib.gusite.micromodel.Encuesta;

@TemplateView(TemplateView.ENCUESTA_ENVIO_ENCUESTA)
public class EnvioEncuestaView extends PageView {

	private Encuesta encuesta;

	public void setEncuesta(Encuesta encuesta) {
		this.encuesta = encuesta;
	}

	/**
	 * Encuesta a la cuál pertenece el envío, o de la que se están mostrando resultados
	 * @return
	 */
	@Variable("MVS_encuesta")
	public Encuesta getEncuesta() {
		return encuesta;
	}

}
