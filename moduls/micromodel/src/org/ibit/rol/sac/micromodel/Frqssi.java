package org.ibit.rol.sac.micromodel;


/**
 * Clase Frqssi. Bean que define un Formulario QSSI. 
 * Modela la tabla de BBDD GUS_FRQSSI
 * @author Indra
 */
public class Frqssi extends Traducible
{

	private static final long serialVersionUID = 6491465580006672873L;
	
	private Long id;
    private Long idmicrosite;
    private String centro;
    private String tipoescrito;
        
 	public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getIdmicrosite() {
		return idmicrosite;
	}

	public void setIdmicrosite(Long idmicrosite) {
		this.idmicrosite = idmicrosite;
	}

	public String getCentro() {
		return centro;
	}

	public void setCentro(String centro) {
		this.centro = centro;
	}	

	public String getTipoescrito() {
		return tipoescrito;
	}

	public void setTipoescrito(String tipoescrito) {
		this.tipoescrito = tipoescrito;
	}	
	
	public void addTraduccionMap(String lang, TraduccionFrqssi traduccion) {
        setTraduccion(lang, traduccion);
    }

}