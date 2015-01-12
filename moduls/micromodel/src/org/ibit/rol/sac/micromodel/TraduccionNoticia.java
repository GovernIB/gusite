package org.ibit.rol.sac.micromodel;

/**
 * Clase TraduccionNoticia. Encapsula los datos  que pueden tener valor en diferentes idiomas del objeto Noticia.
 * @author Indra
 */
public class TraduccionNoticia implements Traduccion {

	private static final long serialVersionUID = -6878989762827827892L;
	private String titulo;
    private String subtitulo;
    private String fuente;
    private String laurl;
    private String urlnom;
    private Archivo docu;
    private String texto;
	
    public TraduccionNoticia()   { }

    public Archivo getDocu(){
        return docu;
    }

    public void setDocu(Archivo docu){
        this.docu = docu;
    }

    public String getFuente(){
        return fuente;
    }

    public void setFuente(String fuente){
        this.fuente = fuente;
    }

    public String getLaurl(){
        return laurl;
    }

    public void setLaurl(String laurl){
        this.laurl = laurl;
    }
    
    public String getUrlnom(){
        return urlnom;
    }

    public void setUrlnom(String urlnom){
        this.urlnom = urlnom;
    }    

    public String getSubtitulo(){
        return subtitulo;
    }

    public void setSubtitulo(String subtitulo){
        this.subtitulo = subtitulo;
    }

    public String getTitulo(){
    	return titulo;
    }

    public void setTitulo(String titulo){
        this.titulo = titulo;
    }

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

}