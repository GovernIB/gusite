package es.caib.gusite.front.captcha;

import org.apache.commons.codec.binary.Base64;


/**
 * 
 * Imagen captcha.
 * 
 * @author Indra
 * 
 */
public final class ImagenCaptcha {

    /**
     * Fichero.
     */
    private String fichero;

    /**
     * Datos fichero.
     */
    private byte[] contenido;

    /**
     * Método de acceso a fichero.
     * 
     * @return fichero
     */
//    public String getFichero() {
//        return fichero;
//    }

    /**
     * Método para establecer fichero.
     * 
     * @param pFichero
     *            fichero a establecer
     */
//    public void setFichero(final String pFichero) {
//        fichero = pFichero;
//    }

    /**
     * Método de acceso a contenido.
     * 
     * @return contenido
     */
    public byte[] getContenido() {
    	return contenido;
    }

    /**
     * Método de acceso a contenido.
     * 
     * @return contenido
     */
    public String getContenidoBase64() {    	
    	Base64 base64 = new Base64();        
    	return new String(base64.encode(contenido));
    }
    
    /**
     * Método para establecer contenido.
     * 
     * @param pContenido
     *            contenido a establecer
     */
    public void setContenido(final byte[] pContenido) {
        contenido = pContenido;
    }

    private String error;

	/**
	 * @return the error
	 */
	public String getError() {
		return error;
	}

	/**
	 * @param error the error to set
	 */
	public void setError(String error) {
		this.error = error;
	}

	
}
