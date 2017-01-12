package es.caib.gusite.front.view;

import es.caib.gusite.front.captcha.ImagenCaptcha;
import es.caib.gusite.micromodel.Contacto;

/**
 * Página que se muestra tras enviar un contacto satisfactoriamente
 * @author at4.net
 *
 */
@TemplateView(TemplateView.CONTACTO_ENVIO_CONTACTO)
public class ContactoDatosView extends PageView {

	private Contacto contacto;
	private String contactoTitulo;
	private ImagenCaptcha captcha;
	
	public void setContacto(Contacto contacto) {
		this.contacto = contacto;
	}

	/**
	 * datos de un formulario de contacto
	 */
	@Variable("MVS_contacto")
	public Contacto getContacto() {
		return contacto;
	}

	public void setContactoTitulo(String titulocontacto) {
		this.contactoTitulo = titulocontacto;
	}

	/**
	 * título del contacto en cuestión
	 */
	@Variable("MVS_contacto_titulo")
	public String getContactoTitulo() {
		return contactoTitulo;
	}

	/**
	 * @param captcha the captcha to set
	 */
	public void setCaptcha(ImagenCaptcha captcha) {
		this.captcha = captcha;
	}

	/**
	 * @return the captcha
	 */
	@Variable("MVS_captcha")
	public ImagenCaptcha getCaptcha() {
		return captcha;
	}
}
