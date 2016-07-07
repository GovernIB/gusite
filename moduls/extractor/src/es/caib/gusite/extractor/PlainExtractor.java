package es.caib.gusite.extractor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Extractor de texto plano.
 */
public final class PlainExtractor implements Extractor {

    /**
     * Constructor.
     */
    public PlainExtractor() {
    }

    /**
     * Tipos mimes soportados por el Extractor.
     * @return devuelve los tipos de texto.
     */
    public String[] getSupportedMimes() {
        //return new String[]{"text/plain", "text/*"};
    	return new String[]{"text/plain", "text\\plain"};
    }

    /**
     * Extra el texto de un documento en texto plano.
     * @param in InputStream del documento.
     * @return Texo el documento.
     * @throws IOException Si se produce algun error leyendo el documento.
     */
    public String extractText(final InputStream in) throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final byte[] buffer = new byte[1024];
        int n;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        out.close();

        return new String(out.toByteArray());
    }

}
