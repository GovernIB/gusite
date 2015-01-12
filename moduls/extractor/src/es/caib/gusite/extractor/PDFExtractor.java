package es.caib.gusite.extractor;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

/**
 * This class extracts text from pdf files by using the PdfBox library.
 * @author Ryan Ackley
 * @author Antoni Reus
 */
public final class PDFExtractor implements Extractor {

    private static Log log = LogFactory.getLog(PDFExtractor.class);

    /**
     * Constructor.
     */
    public PDFExtractor() {
    }


    /**
     * Tipos mimes soportados por el Extractor.
     * @return devuelve los tipos de PDF.
     */
    public String[] getSupportedMimes() {

        return new String[] {"application/pdf"};
    }


    /**
     * Extracts text from a pdf es.caib.gusite.lucene.document.
     * @param in The InputStream representing the pdf file.
     * @return The text in the file
     * @throws IOException Si se produce algun error leyendo el documento.
     */
    public String extractText(final InputStream in) throws IOException {

        final PDFParser parser = new PDFParser(in);
        parser.parse();
        final PDDocument document = parser.getPDDocument();
        String s = null;
        try {
            final PDFTextStripper stripper = new PDFTextStripper();
            s = stripper.getText(document);

        } catch (Exception e) {
            log.error("El PDF tiene carácteres que no han podido ser codificados" + e.getMessage());
        } finally {
            if (document != null) {
                document.close();
            }
        }

        return s;
    }

}
