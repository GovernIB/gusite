package org.ibit.rol.sac.micromodel;

import java.util.HashSet;
import java.util.Set;

/**
 * Clase Menu. Bean que define un Menu. 
 * Modela la tabla de BBDD GUS_MENU
 * @author Indra
 */
public class Menu extends Traducible {

	private static final long serialVersionUID = 1505040618105464154L;
	private Long id;
    private int orden;
    private Long idmicrosite;
    private Long padre;
	private String visible;
	private String modo;
    private Archivo imagenmenu;    
    private Set<Contenido> contenidos = new HashSet<Contenido>();

    
    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }


    public int getOrden()
    {
        return orden;
    }

    public void setOrden(int orden)
    {
        this.orden = orden;
    }
	
	public Long getIdmicrosite() {
		return idmicrosite;
	}

	public void setIdmicrosite(Long idmicrosite) {
		this.idmicrosite = idmicrosite;
	}

	public Archivo getImagenmenu() {
		return imagenmenu;
	}

	public void setImagenmenu(Archivo imagenmenu) {
		this.imagenmenu = imagenmenu;
	}

	public Set<Contenido> getContenidos() {
		return contenidos;
	}

	public void setContenidos(Set<Contenido> contenidos) {
		this.contenidos = contenidos;
	}

	public Long getPadre() {
		return padre;
	}

	public void setPadre(Long padre) {
		this.padre = padre;
	}

	public String getVisible() {
		return visible;
	}

	public void setVisible(String visible) {
		this.visible = visible;
	}

	public String getModo() {
		return modo;
	}

	public void setModo(String modo) {
		this.modo = modo;
	}
	
	// Metodo para poder leer la coleccion del XML
	
	public void addContenidos (Contenido con) {
		contenidos.add(con);
	}

	public void addTraduccionMap(String lang, TraduccionMenu traduccion) {
        setTraduccion(lang, traduccion);
    }
}