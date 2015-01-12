package org.ibit.rol.sac.micromodel;

/**
 * Clase Lineadatocontacto. Bean que define una Línea  de Contacto. 
 * Modela la tabla de BBDD GUS_FRMLIN
 * @author Indra
 */
public class Lineadatocontacto extends Traducible{

	private static final long serialVersionUID = 8743296706196032258L;
	
	public static final int NUMERO_MAXIMO_SELECTOR = 30;
	private Long id;
    private String visible;
    private int tamano;
    private int lineas;
    private int obligatorio;
    private int orden;
    private String tipo;
    private Long idcontacto;

    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public int getLineas() {
		return lineas;
	}
	public void setLineas(int lineas) {
		this.lineas = lineas;
	}
	public int getObligatorio() {
		return obligatorio;
	}
	public void setObligatorio(int obligatorio) {
		this.obligatorio = obligatorio;
	}
	public int getOrden() {
		return orden;
	}
	public void setOrden(int orden) {
		this.orden = orden;
	}
	public int getTamano() {
		return tamano;
	}
	public void setTamano(int tamano) {
		this.tamano = tamano;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getVisible() {
		return visible;
	}
	public void setVisible(String visible) {
		this.visible = visible;
	}
	public Long getIdcontacto() {
		return idcontacto;
	}
	public void setIdcontacto(Long idcontacto) {
		this.idcontacto = idcontacto;
	}
	public void addTraduccionMap(String lang, TraduccionLineadatocontacto traduccion) {
        setTraduccion(lang, traduccion);
    }  
}