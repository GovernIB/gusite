package es.caib.gusite.micromodel.adapter;

import es.caib.gusite.micromodel.*;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * Created by tcerda on 04/11/2014.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlSeeAlso({TraduccionActividadagenda.class, TraduccionAgenda.class, TraduccionContenido.class, TraduccionEncuesta.class,
        TraduccionFaq.class, TraduccionFrqssi.class, TraduccionLineadatocontacto.class, TraduccionMenu.class,
        TraduccionMicrosite.class, TraduccionNoticia.class, TraduccionPregunta.class, TraduccionRespuesta.class,
        TraduccionTemafaq.class, TraduccionTipo.class, TraduccionComponente.class})
public class TraduccionNode {

    @XmlElement
    private String key;

    @XmlElement
    private Object value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
