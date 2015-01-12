package org.ibit.rol.sac.micromodel;

/**
 * Clase Archivo. Bean que define una Archivo. 
 * Modela la tabla de BBDD GUS_DOCUS
 * @author Indra
 */
public class Archivo extends Traducible  implements Indexable {


	private static final long serialVersionUID = -3122017714028641802L;
	
	private Long id;
    private long idmicrosite;
    private String mime;
    private String nombre;
    private long peso;
    private byte[] datos;
    private Long pagina;

    public Archivo() {}
    
    public Long getPagina() {
		return pagina;
	}

	public void setPagina(Long pagina) {
		this.pagina = pagina;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public long getPeso() {
        return peso;
    }

    public void setPeso(long peso) {
        this.peso = peso;
    }

    public byte[] getDatos() {
        return datos;
    }

    public void setDatos(byte[] datos) {
        this.datos = datos;
    }

	public long getIdmicrosite() {
		return idmicrosite;
	}

	public void setIdmicrosite(long idmicrosite) {
		this.idmicrosite = idmicrosite;
	}

}
