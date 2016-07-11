package es.caib.gusite.front.view;

import es.caib.gusite.front.general.bean.ErrorMicrosite;

/**
 * Mensaje de error genérico.
 * @author at4.net
 *
 */
@TemplateView(TemplateView.ERROR_GENERICO)
public class ErrorGenericoView extends PageView {

	private PageView causeView;
	private ErrorMicrosite errParam;
	private String errEstado;

	public ErrorGenericoView(PageView causeView) {
		this.setMicrosite(causeView.getMicrosite());
		this.setIdioma(causeView.getIdioma());
		this.setLang(causeView.getLang());
		this.setCauseView(causeView);
		this.setErrEstado("");
		
	}

	public ErrorGenericoView() {
	}

	public void setCauseView(PageView causeView) {
		this.causeView = causeView;
	}

	/**
	 * Vista original que genera el error
	 */
	@Variable("MVS_causeView")
	public PageView getCauseView() {
		return this.causeView;
	}

	public void setErrParam(ErrorMicrosite errorMicrosite) {
		this.errParam = errorMicrosite;
	}

	/**
	 * Datos del error
	 */
	@Variable("MVS_errparam")
	public ErrorMicrosite getErrParam() {
		return this.errParam;
	}
	
	/**
	 * Datos del error
	 */
	@Variable("MVS_errestado")
	public String getErrEstado() {
		return this.errEstado;
	}

	public void setErrEstado(String errEstado) {
		this.errEstado = errEstado;
	}

}
