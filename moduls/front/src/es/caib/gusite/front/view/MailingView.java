package es.caib.gusite.front.view;

@TemplateView(TemplateView.MSG_GENERICO)
public class MailingView extends PageView {

	
	
	private String msg;

	/**
	 * Mensaje a mostrar en la plantilla del mailing
	 * @param msg
	 */
	@Variable("MVS_msg")
	public String getMsg(){
		return this.msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
