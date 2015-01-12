package org.ibit.rol.sac.micromodel;

/**
 * Clase Actividadagenda. Bean que define la actividad de una agenda. En la GUI una actividad se considera un "Esdeveniment" 
 * Modela la tabla de BBDD GUS_ACTIVI
 * @author Indra
 */
public class Actividadagenda extends Traducible{
	
	
	private static final long serialVersionUID = -2270566256357933414L;
	
	private Long id;
    private Long idmicrosite;

    public Long getIdmicrosite() {
		return idmicrosite;
	}

	public void setIdmicrosite(Long idmicrosite) {
		this.idmicrosite = idmicrosite;
	}

	public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

	public void addTraduccionMap(String lang, TraduccionActividadagenda traduccion) {
        setTraduccion(lang, traduccion);
    }  


}