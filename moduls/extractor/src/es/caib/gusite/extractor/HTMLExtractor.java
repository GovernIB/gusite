package es.caib.gusite.extractor;

import java.io.IOException;
import java.io.InputStream;

import au.id.jericho.lib.html.Source;

/**
 * Extractor de texto HTML, admite un parametro include para indexar los archivos 
 * de microsites contenidos en el HTML.
 */
public final class HTMLExtractor implements Extractor {

    /**
     * Constructor.
     */
    public HTMLExtractor() {
    }

    /**
     * Tipos mimes soportados por el Extractor.
     * @return devuelve los tipos de texto.
     */
    public String[] getSupportedMimes() {
        return new String[]{"text/html"};
    }

    /**
     * Extra el texto de un documento HTML
     * @param in InputStream del documento.
     * @return Texto del documento HTML
     * @throws IOException Si se produce algun error leyendo el documento.
     */
    public String extractText(final InputStream in) throws IOException {
                
		Source fuente = new Source(in);
		fuente.fullSequentialParse();
		return fuente.getTextExtractor().toString();

    }

}
