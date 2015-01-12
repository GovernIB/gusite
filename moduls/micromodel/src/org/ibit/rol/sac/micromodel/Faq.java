package org.ibit.rol.sac.micromodel;

import java.util.Date;

/**
 * Clase Faq. Bean que define una Faq. 
 * Modela la tabla de BBDD GUS_FAQ
 * @author Indra
 */
public class Faq extends Traducible  implements Indexable {

	private static final long serialVersionUID = 2723640089721073919L;
	
	private Long id;
    private Long idmicrosite;
    private Date fecha;
    private String visible;
    private Temafaq tema;
    
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getIdmicrosite() {
		return idmicrosite;
	}
	public void setIdmicrosite(Long id) {
		this.idmicrosite = id;
	}
	
	public Temafaq getTema() {
		return tema;
	}
	public void setTema(Temafaq tema) {
		this.tema = tema;
	}
	
	public String getVisible() {
		return visible;
	}
	public void setVisible(String visible) {
		this.visible = visible;
	}
	public void addTraduccionMap(String lang, TraduccionFaq traduccion) {
        setTraduccion(lang, traduccion);
    }  

}