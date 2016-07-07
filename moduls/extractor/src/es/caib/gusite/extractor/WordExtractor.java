package es.caib.gusite.extractor;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.DocumentInputStream;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.util.LittleEndian;

/**
 * This class extracts the text from a Word 97/2000/XP word doc.
 * @author Ryan Ackley
 * @author Antoni Reus
 */
public final class WordExtractor implements Extractor {

    /**
     * Constructor.
     */
    public WordExtractor() {
    }

    /**
     * Tipos mimes soportados por el Extractor.
     * @return devuelve los tipos de MS Word.
     */
    public String[] getSupportedMimes() {
        return new String[]{"application/msword"};
    }

    /**
     * Gets the text from a MS Word es.caib.gusite.lucene.document.
     * @param in The InputStream representing the MS Word file.
     * @return The text in the file.
     * @throws IOException Si se produce algun error leyendo el documento.
     */
    public String extractText(final InputStream in) throws IOException {
        final ArrayList text = new ArrayList();
        final POIFSFileSystem fsys = new POIFSFileSystem(in);

        final DocumentEntry headerProps = (DocumentEntry) fsys.getRoot().getEntry("WordDocument");
        DocumentInputStream din = fsys.createDocumentInputStream("WordDocument");
        final byte[] header = new byte[headerProps.getSize()];
        din.read(header);

        //Get the information we need from the header
        final int info = LittleEndian.getShort(header, 0xa);

        final boolean useTable1 = (info & 0x200) != 0;

        //get the location of the piece table
        final int complexOffset = LittleEndian.getInt(header, 0x1a2);

        final String tableName;
        if (useTable1) {
            tableName = "1Table";
        } else {
            tableName = "0Table";
        }

        final DocumentEntry table = (DocumentEntry) fsys.getRoot().getEntry(tableName);
        final byte[] tableStream = new byte[table.getSize()];

        din = fsys.createDocumentInputStream(tableName);

        din.read(tableStream);

        final int multiple = findText(tableStream, complexOffset, text);

        final StringBuffer sb = new StringBuffer();
        final int size = text.size();

        for (int x = 0; x < size; x++) {
            final WordTextPiece nextPiece = (WordTextPiece) text.get(x);
            final int start = nextPiece.getStart();
            final int length = nextPiece.getLength();

            final boolean unicode = nextPiece.usesUnicode();
            final String toStr;
            if (unicode) {
                toStr = new String(header, start, length * multiple, "UTF-16LE");
            } else {
                toStr = new String(header, start, length, "UTF-8");
            }
            sb.append(toStr).append(" ");

        }

        table.delete();
        din.close();
        headerProps.delete();

        return sb.toString();
    }

    /**
     * Goes through the piece table and parses out the info regarding the text
     * blocks. For Word 97 and greater all text is stored in the "complex" way
     * because of unicode.
     * @param tableStream buffer containing the main table stream.
     * @param complexOffset beginning of the complex data.
     * @param text
     * @return 2 o 1
     * @throws java.io.IOException
     */
    private static int findText(final byte[] tableStream, final int complexOffset, final ArrayList text) throws IOException {
        //actual text
        int pos = complexOffset;
        int multiple = 2;
        //skips through the prms before we reach the piece table. These contain data
        //for actual fast saved files
        while (tableStream[pos] == 1) {
            pos++;
            final int skip = LittleEndian.getShort(tableStream, pos);
            pos += 2 + skip;
        }
        if (tableStream[pos] != 2) {
            throw new IOException("corrupted Word file");
        } else {
            //parse out the text pieces
            final int pieceTableSize = LittleEndian.getInt(tableStream, ++pos);
            pos += 4;
            final int pieces = (pieceTableSize - 4) / 12;
            for (int x = 0; x < pieces; x++) {
                int filePos = LittleEndian.getInt(tableStream, pos + ((pieces + 1) * 4) + (x * 8) + 2);
                final boolean unicode;
                if ((filePos & 0x40000000) == 0) {
                    unicode = true;
                } else {
                    unicode = false;
                    multiple = 1;
                    filePos &= ~(0x40000000);//gives me FC in doc stream
                    filePos /= 2;
                }
                final int totLength = LittleEndian.getInt(tableStream, pos + (x + 1) * 4) -
                        LittleEndian.getInt(tableStream, pos + (x * 4));

                final WordTextPiece piece = new WordTextPiece(filePos, totLength, unicode);
                text.add(piece);
            }

        }
        return multiple;
    }

}

final class WordTextPiece {
    private final int _fcStart;
    private final boolean _usesUnicode;
    private final int _length;

    public WordTextPiece(final int start, final int length, final boolean unicode) {
        _usesUnicode = unicode;
        _length = length;
        _fcStart = start;
    }

    public boolean usesUnicode() {
        return _usesUnicode;
    }

    public int getStart() {
        return _fcStart;
    }

    public int getLength() {
        return _length;
    }
}
