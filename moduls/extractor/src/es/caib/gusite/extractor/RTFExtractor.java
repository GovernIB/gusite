package es.caib.gusite.extractor;

import java.io.IOException;
import java.io.InputStream;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.rtf.RTFEditorKit;

/**
 * Extrae texto de documentos RTF.
 */
public final class RTFExtractor implements Extractor {

    /**
     * Constructor.
     */
    public RTFExtractor() {
    }

    /**
     * Tipos mimes soportados por el Extractor.
     * @return devuelve los tipos de MS Excel.
     */
    public String[] getSupportedMimes() {
        return new String[]{"text/rtf"};
    }

    /**
     * Extra el texto de un documento RTF.
     * @param in InputStream del documento RTF.
     * @return Texto extraido del documento.
     * @throws IOException Si se produce cualquier error.
     */
    public String extractText(final InputStream in) throws IOException {
        final RTFEditorKit editor = new RTFEditorKit();
        final Document doc = editor.createDefaultDocument();
        final String s;
        try {
            editor.read(in, doc, 0);
            s = doc.getText(0, doc.getLength());
        } catch (BadLocationException e) {
            throw new IOException(e.getMessage());
        }

        return s;
    }
}
