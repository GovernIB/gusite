package org.ibit.rol.sac.micromodel;

/**
 * Clase Temafaq. Bean que define un Tema. 
 * Modela la tabla de BBDD GUS_TEMAS.
 * @author Indra
 */
public class Temafaq extends Traducible{

	private static final long serialVersionUID = -1590941371205365963L;
	private Long id;
    private Long idmicrosite;
    
    public Long getIdmicrosite() {
		return idmicrosite;
	}

	public void setIdmicrosite(Long idmicrosite) {
		this.idmicrosite = idmicrosite;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

	public void addTraduccionMap(String lang, TraduccionTemafaq traduccion) {
        setTraduccion(lang, traduccion);
    }  
}