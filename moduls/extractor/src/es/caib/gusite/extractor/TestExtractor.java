package es.caib.gusite.extractor;

import java.io.FileInputStream;

/**
 * Clase de pruebas.
 */
public final class TestExtractor {

    /**
     * Imprime el texto que contiene un fichero.
     * @param args primer argumento nombre del fichero, segundo, tipo mime.
     * @throws Exception Si se produce cualquier error.
     */
    public static void main(final String[] args) throws Exception {

        if (args.length < 2) {
            throw new Exception("es necessiten dos parametres");
        }

        final FileInputStream fin = new FileInputStream(args[0]);
        final String mime = args[1];

        final Extractor extr = ExtractorFactory.getExtractor(mime);
        if (extr == null) {
            return;
        }

        try {
            final String text = extr.extractText(fin);
            //System.out.println(text);
        } finally {
            fin.close();
        }
    }
}
