package org.ibit.rol.sac.micromodel;

/**
 * Clase TraduccionFaq. Encapsula los datos  que pueden tener valor en diferentes idiomas del objeto Faq.
 * @author Indra
 */
public class TraduccionFaq  implements Traduccion{

	private static final long serialVersionUID = -5527976114771535502L;
	private String pregunta;
	private String respuesta;
	private String url;
	private String urlnom;
	
	public String getPregunta() {
		return pregunta;
	}
	public void setPregunta(String pregunta) {
		this.pregunta = pregunta;
	}
	public String getRespuesta() {
		return respuesta;
	}
	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUrlnom(){
        return urlnom;
    }
	public void setUrlnom(String urlnom){
        this.urlnom = urlnom;
    }    

}