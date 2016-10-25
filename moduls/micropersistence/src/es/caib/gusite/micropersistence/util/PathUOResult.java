package es.caib.gusite.micropersistence.util;

import java.util.ArrayList;
import java.util.List;

import es.caib.solr.api.model.PathUO;

public class PathUOResult {
	
	private List<PathUO> uosPath = new ArrayList<PathUO>();
	
	private String uosText;

	/**
	 * @return the uosPath
	 */
	public List<PathUO> getUosPath() {
		return uosPath;
	}

	/**
	 * @param uosPath the uosPath to set
	 */
	public void setUosPath(List<PathUO> uosPath) {
		this.uosPath = uosPath;
	}

	/**
	 * @return the uosText
	 */
	public String getUosText() {
		return uosText;
	}

	/**
	 * @param uosText the uosText to set
	 */
	public void setUosText(String uosText) {
		this.uosText = uosText;
	}
	
	

}
