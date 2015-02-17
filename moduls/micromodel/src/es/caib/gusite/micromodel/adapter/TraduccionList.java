package es.caib.gusite.micromodel.adapter;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Created by tcerda on 04/11/2014.
 */
@XmlAccessorType(XmlAccessType.NONE)
public class TraduccionList {

	@XmlElement(name = "traduccion")
	private List<TraduccionNode> entries = new ArrayList<TraduccionNode>();

	public List<TraduccionNode> getEntries() {
		return this.entries;
	}

	public void setEntries(List<TraduccionNode> entries) {
		this.entries = entries;
	}
}
