package es.caib.gusite.front.general.bean;

import java.util.ArrayList;

import es.caib.gusite.micromodel.Menu;

/**
 * Clase MenuFron. Esta clase extiende del Bean Menu.
 * 
 * @author Indra
 */
public class MenuFront extends Menu {

	private static final long serialVersionUID = -7988123581470642989L;
	private ArrayList<?> listacosas = new ArrayList<Object>();

	public MenuFront(Menu menu) {

		super.setId(menu.getId());
		super.setMicrosite(menu.getMicrosite());
		super.setImagenmenu(menu.getImagenmenu());
		super.setOrden(menu.getOrden());
		super.setPadre(menu.getPadre());
		super.setTraduccionMap(menu.getTraduccionMap());
		super.setVisible(menu.getVisible());
		super.setModo(menu.getModo());
		super.setIdi(menu.getIdi());
	}

	public ArrayList getListacosas() {
		return this.listacosas;
	}

	public void setListacosas(ArrayList listacosas) {
		this.listacosas = listacosas;
	}

}
