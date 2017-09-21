package es.caib.gusite.micromodel.adapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import es.caib.gusite.micromodel.TraduccionActividadagenda;
import es.caib.gusite.micromodel.TraduccionAgenda;
import es.caib.gusite.micromodel.TraduccionComponente;
import es.caib.gusite.micromodel.TraduccionContenido;
import es.caib.gusite.micromodel.TraduccionEncuesta;
import es.caib.gusite.micromodel.TraduccionFContacto;
import es.caib.gusite.micromodel.TraduccionFaq;
import es.caib.gusite.micromodel.TraduccionFrqssi;
import es.caib.gusite.micromodel.TraduccionLineadatocontacto;
import es.caib.gusite.micromodel.TraduccionMenu;
import es.caib.gusite.micromodel.TraduccionMicrosite;
import es.caib.gusite.micromodel.TraduccionNoticia;
import es.caib.gusite.micromodel.TraduccionPregunta;
import es.caib.gusite.micromodel.TraduccionRespuesta;
import es.caib.gusite.micromodel.TraduccionTemafaq;
import es.caib.gusite.micromodel.TraduccionTipo;

/**
 * Created by tcerda on 04/11/2014.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlSeeAlso({ TraduccionActividadagenda.class, TraduccionAgenda.class,
		TraduccionContenido.class, TraduccionEncuesta.class,
		TraduccionFaq.class, TraduccionFrqssi.class,
		TraduccionLineadatocontacto.class, TraduccionMenu.class,
		TraduccionMicrosite.class, TraduccionNoticia.class,
		TraduccionPregunta.class, TraduccionRespuesta.class,
		TraduccionTemafaq.class, TraduccionTipo.class,TraduccionFContacto.class,
		TraduccionComponente.class })
public class TraduccionNode {

	@XmlElement
	private String key;

	@XmlElement
	private Object value;

	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Object getValue() {
		return this.value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
}
