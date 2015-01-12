package org.ibit.rol.sac.micromodel;

import java.util.Date;

/**
 * Clase Noticia. Bean que define una Noticia. En la GUI los elementos de un listado son noticias.
 * Modela la tabla de BBDD GUS_NOTICS.
 * @author Indra
 */
public class Noticia extends Traducible  implements Indexable {

	private static final long serialVersionUID = -3615339661028201007L;
	private Long id;
    private Long idmicrosite;
    private Archivo imagen;
    private Date fcaducidad;
    private Date fpublicacion;
    private String visible;
    private Integer orden;
    private String visibleweb;
    
    private Tipo tipo;
	
    public Date getFcaducidad()
    {
        return fcaducidad;
    }

    public void setFcaducidad(Date fcaducidad)
    {
        this.fcaducidad = fcaducidad;
    }

    public Date getFpublicacion()
    {
        return fpublicacion;
    }

    public void setFpublicacion(Date fpublicacion)
    {
        this.fpublicacion = fpublicacion;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getIdmicrosite()
    {
        return idmicrosite;
    }

    public void setIdmicrosite(Long idmicrosite)
    {
        this.idmicrosite = idmicrosite;
    }

    public String getVisible()
    {
    	return visible;
    }

    public void setVisible(String visible)
    {
   		this.visible=visible;
    }

    public String getVisibleweb()
    {
        return visibleweb;
    }

    public void setVisibleweb(String visibleweb)
    {
        this.visibleweb = visibleweb;
    }

    public Archivo getImagen()
    {
        return imagen;
    }

    public void setImagen(Archivo imagen)
    {
        this.imagen = imagen;
    }

	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}
   
	public void addTraduccionMap(String lang, TraduccionNoticia traduccion) {
        setTraduccion(lang, traduccion);
    }

	public Integer getOrden() {
		return orden;
	}

	public void setOrden(Integer orden) {
		this.orden = orden;
	}  
}