package es.caib.gusite.front.captcha;

/**
 * 
 * Sonido captcha.
 * 
 * @author Indra
 * 
 */
public final class SonidoCaptcha {

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
    public String getFichero() {
        return fichero;
    }

    /**
     * Método para establecer fichero.
     * 
     * @param pFichero
     *            fichero a establecer
     */
    public void setFichero(final String pFichero) {
        fichero = pFichero;
    }

    /**
     * Método de acceso a contenido.
     * 
     * @return contenido
     */
    public byte[] getContenido() {
        return contenido;
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

}
