package es.caib.gusite.front.view;

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

	
}
