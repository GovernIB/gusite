package es.caib.gusite.extractor;

import java.util.HashMap;
import java.util.Map;

/**
 * Classe factory de objetos Extractor.
 */
public final class ExtractorFactory {

    private static final Map extractors = new HashMap();

    static {
        addExtractor(new ExcelExtractor());
        addExtractor(new PDFExtractor());
        addExtractor(new RTFExtractor());
        addExtractor(new WordExtractor());
        addExtractor(new XMLExtractor());
        addExtractor(new PlainExtractor());
        addExtractor(new HTMLExtractor());
    }

    private static void addExtractor(final Extractor extractor) {
        final String[] mimes = extractor.getSupportedMimes();
        for (int i = 0; i < mimes.length; i++) {
            extractors.put(mimes[i], extractor);
        }
    }

    private static String getGeneralMime(final String mime) {
        final int slashPos = mime.indexOf("/");
        final String gMime = mime.substring(0, slashPos + 1) + "*";

        return gMime;
    }

    /**
     * Obtiene un {@link Extractor} para el tipo mime especificado.
     * @param mime Tipo de mime del fichero que se va a tratar.
     * @return clase adequada para extraer texto del tipo mime o <code>null</code> si no hay
     * ninguna disponible.
     */
    public static Extractor getExtractor(final String mime) {
        Extractor extractor = (Extractor) extractors.get(mime);
        if (extractor == null) {
            extractor = (Extractor) extractors.get(getGeneralMime(mime));
            if (extractor == null) {
                extractor = (Extractor) extractors.get("*/*");
            }
        }

        return extractor;
    }
}
