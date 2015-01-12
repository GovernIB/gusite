package org.ibit.rol.sac.micromodel;

/**
 * Clase Tiposervicio. Bean que define un Tipo de servicio. 
 * Modela la tabla de BBDD GUS_TIPSER.
 * @author Indra
 */
public class Tiposervicio extends Traducible{

	private static final long serialVersionUID = 8927320761345779941L;
	private Long id;
    private String nombre;
    private String visible;
    private String tipo;
    private String url;
    private String referencia;

    public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getVisible() {
		return visible;
	}

	public void setVisible(String visible) {
		this.visible = visible;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}
}