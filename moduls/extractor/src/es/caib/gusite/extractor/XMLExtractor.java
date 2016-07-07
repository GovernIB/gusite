package es.caib.gusite.extractor;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * TODO documentar
 */
public class XMLExtractor implements Extractor {

    public String[] getSupportedMimes() {
        return new String[]{"text/xml"};
    }

    public String extractText(InputStream in) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int readed = in.read(buffer, 0, buffer.length);
        while (readed != -1) {
            baos.write(buffer, 0, readed);
            readed = in.read(buffer, 0, buffer.length);
        }

        File file = File.createTempFile("extractor", null);
        file.deleteOnExit();

        FileOutputStream fos = new FileOutputStream(file);
        fos.write(baos.toByteArray());

        SAXTextHandler handler = new SAXTextHandler(file);

        String content = handler.getDocumentText();

        fos.close();
        file.delete();
        baos.close();

        return content;
    }
}


class SAXTextHandler extends DefaultHandler {

    public SAXTextHandler(File file) throws IOException {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setValidating(false);
        spf.setNamespaceAware(true);

        try {
            SAXParser parser = spf.newSAXParser();
            parser.parse(file, this);
        } catch (ParserConfigurationException e) {
            throw new IOException(e.getMessage());
        } catch (SAXException e) {
            throw new IOException(e.getMessage());
        }
    }

    /** A buffer for each XML element */
    private StringBuffer documentBuffer;

    public String getDocumentText() {
        return documentBuffer.toString();
    }

    // Implementacion de Handler SAX

    public void startDocument() throws SAXException {
        documentBuffer = new StringBuffer();
    }

    public InputSource resolveEntity(String publicId, String systemId) throws SAXException {
        // Parche per fer funcionar sense DTDs.
        if (systemId.endsWith(".dtd")) {
            StringReader stringInput =
                    new StringReader(" ");
            return new InputSource(stringInput);
        } else {
            return null;    // default behavior
        }
    }

    public void characters(char[] text, int start, int length) throws SAXException {
        documentBuffer.append(text, start, length);
        documentBuffer.append("\n");
    }
}