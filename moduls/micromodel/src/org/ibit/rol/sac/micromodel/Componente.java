package org.ibit.rol.sac.micromodel;


/**
 * Clase Componente. Bean que define un Componente. 
 * Modela la tabla de BBDD GUS_COMPOS
 * @author Indra
 */
public class Componente extends Traducible {


	private static final long serialVersionUID = 5072614956105180822L;
	
	private Long id;
    private Long idmicrosite;
    private Archivo imagenbul;
    private Tipo tipo;
    private String nombre;
	private String soloimagen;
	private String filas;
	private Integer numelementos;
	private Integer ordenacion;
	
	
	public String getFilas() {
		return filas;
	}
	public void setFilas(String filas) {
		this.filas = filas;
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
	public void setIdmicrosite(Long idmicrosite) {
		this.idmicrosite = idmicrosite;
	}
	public Archivo getImagenbul() {
		return imagenbul;
	}
	public void setImagenbul(Archivo imagenbul) {
		this.imagenbul = imagenbul;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Integer getNumelementos() {
		return numelementos;
	}
	public void setNumelementos(Integer numelementos) {
		this.numelementos = numelementos;
	}
	public Integer getOrdenacion() {
		return ordenacion;
	}
	public void setOrdenacion(Integer ordenacion) {
		this.ordenacion = ordenacion;
	}
	public String getSoloimagen() {
		return soloimagen;
	}
	public void setSoloimagen(String soloimagen) {
		this.soloimagen = soloimagen;
	}
	public Tipo getTipo() {
		return tipo;
	}
	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}
  
	public void addTraduccionMap(String lang, TraduccionComponente traduccion) {
        setTraduccion(lang, traduccion);
    }
	
}