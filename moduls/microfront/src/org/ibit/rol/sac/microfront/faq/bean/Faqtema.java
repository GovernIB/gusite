package org.ibit.rol.sac.microfront.faq.bean;

import java.util.List;

/**
 * Bean que se mete en sesion o request para la página de las faqs.
 * contiene un tema y un listado de preguntas.
 * @author Indra
 *
 */
public class Faqtema {
	
	private String tema;
	private List<?> listadopreguntas;
	
	public List<?> getListadopreguntas() {
		return listadopreguntas;
	}
	public void setListadopreguntas(List<?> listadopreguntas) {
		this.listadopreguntas = listadopreguntas;
	}
	public String getTema() {
		return tema;
	}
	public void setTema(String tema) {
		this.tema = tema;
	}

}
