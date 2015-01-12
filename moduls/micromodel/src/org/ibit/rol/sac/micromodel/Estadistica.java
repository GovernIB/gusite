package org.ibit.rol.sac.micromodel;

/**
 * Clase Estadística. Bean que define una Estadística. 
 * Modela la tabla de BBDD GUS_STATS
 * @author Indra
 */
public class Estadistica implements ValueObject {


	private static final long serialVersionUID = 7634727383680515426L;
	
	private Long id;
    private Long item;
    private int mes;
    private String referencia;
    private int accesos;
    private Long idmicrosite;
    private int publico;
    
    /**
     * Define: Constructor de la clase.
     */
    public Estadistica()
    {
    }
    
    /**
     * Define: Constructor de la clase
     * @param idmicrosite Identificador del Microsite
     * @param item	Item
     * @param referencia  Referencia
     * @param mes	mes del acceso.
     * @param publico Tipo de acceso
     */
    public Estadistica(Long idmicrosite, Long item, String referencia, int mes, int publico)
    {
    	this.idmicrosite=idmicrosite;
        this.item = item;
        this.referencia = referencia;
        this.mes = mes;
        this.accesos = 0;
        this.publico=publico;
    }    

    public int getAccesos()
    {
        return accesos;
    }

    public void setAccesos(int accesos)
    {
        this.accesos = accesos;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getItem()
    {
        return item;
    }

    public void setItem(Long item)
    {
        this.item = item;
    }

    public int getMes()
    {
        return mes;
    }

    public void setMes(int mes)
    {
        this.mes = mes;
    }

    public String getReferencia()
    {
        return referencia;
    }

    public void setReferencia(String referencia)
    {
        this.referencia = referencia;
    }

	public Long getIdmicrosite() {
		return idmicrosite;
	}

	public void setIdmicrosite(Long idmicrosite) {
		this.idmicrosite = idmicrosite;
	}

	public int getPublico() {
		return publico;
	}

	public void setPublico(int publico) {
		this.publico = publico;
	}
    
}