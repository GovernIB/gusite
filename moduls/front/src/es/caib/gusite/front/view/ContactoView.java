package es.caib.gusite.front.view;

import java.util.List;

import es.caib.gusite.front.general.bean.Pardato;

/**
 * Muestra un formulario de contacto.
 * @author at4.net
 *
 */
@TemplateView(TemplateView.CONTACTO_CONTACTO)
public class ContactoView extends ContactoDatosView {

	private List<Pardato> contactoListaTags;

	public void setContactoListaTags(List<Pardato> lista) {
		this.contactoListaTags = lista;
	}

	@Variable("MVS_contacto_listatags")
	public List<Pardato> getContactoListaTags() {
		return contactoListaTags;
	}

}
