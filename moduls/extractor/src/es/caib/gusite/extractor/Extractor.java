package es.caib.gusite.extractor;

import java.io.IOException;
import java.io.InputStream;

/**
 * Representa classes que pueden extraer texto de un InputStream.
 * @author areus
 */
public interface Extractor {

    /**
     * Lista de tipos mime soportados por el extractor.
     * @return array de strings con los tipos mimes soportados.
     */
    public String[] getSupportedMimes();

    /**
     * Obtiene el texto de un docuemnto.
     * @param in InputStream que representa el fichero.
     * @return El texto del fichero.
     * @throws IOException Si se produce algun error leyendo el documento.
     */
    public String extractText(InputStream in) throws IOException;
}
