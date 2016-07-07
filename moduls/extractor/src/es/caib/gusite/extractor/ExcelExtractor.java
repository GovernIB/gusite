package es.caib.gusite.extractor;

import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * This class extracts text from MS Excel files by using the POI library.
 * @author James Shipley
 */
public final class ExcelExtractor implements Extractor {

    /**
     * Constructor.
     */
    public ExcelExtractor() {
    }

    /**
     * Tipos mimes soportados por el Extractor.
     * @return devuelve los tipos de MS Excel.
     */
    public String[] getSupportedMimes() {
        return new String[]{"application/msexcel"};
    }

    /**
     * Gets the text from a MS Excel es.caib.gusite.lucene.document.
     * @param in The InputStream representing the MS Excel file.
     * @return The text in the file
     * @throws IOException Si se produce algun error leyendo el documento.
     */
    public String extractText(final InputStream in) throws IOException {
        final StringBuffer sb = new StringBuffer();
        final HSSFWorkbook wb = new HSSFWorkbook(in);
        final int numSheets = wb.getNumberOfSheets();

        for (int i = 0; i < numSheets; ++i) {
            final HSSFSheet sheet = wb.getSheetAt(i);
            final int numRows = sheet.getLastRowNum();
            for (int j = 0; j < numRows; ++j) {
                final HSSFRow row = sheet.getRow(j);
                final int numCells = row.getLastCellNum();
                for (int k = 0; k < numCells; ++k) {
                    final HSSFCell cell = row.getCell((short) k);
                    if (cell != null) {
                        final int type = cell.getCellType();
                        if (type == HSSFCell.CELL_TYPE_STRING) {
                            String str = cell.getStringCellValue();
                            str = str.trim();
                            str = replace(str, "\n", "");
                            sb.append(str).append(" ");
                        }
                    }

                    // We will ignore all other types - numbers, forumlas, etc.
                    // as these don't hold alot of meaning outside of their tabular context.
                    // else if(type==, CELL_TYPE_NUMERIC, CELL_TYPE_FORMULA, CELL_TYPE_BOOLEAN, CELL_TYPE_ERROR

                } // cells
                //sb.append("\n"); // break on each row
            } // rows
            sb.append("\n"); // break on each sheet
        } // sheets

        return sb.toString();
    }


    private static final String replace(final String line, final String oldString, final String newString) {
        if (line == null) {
            return null;
        }

        int i = 0;
        if ((i = line.indexOf(oldString, i)) >= 0) {
            final char[] line2 = line.toCharArray();
            final char[] newString2 = newString.toCharArray();
            final int oLength = oldString.length();
            final StringBuffer buf = new StringBuffer(line2.length);
            buf.append(line2, 0, i).append(newString2);
            i += oLength;
            int j = i;
            while ((i = line.indexOf(oldString, i)) > 0) {
                buf.append(line2, j, i - j).append(newString2);
                i += oLength;
                j = i;
            }
            buf.append(line2, j, line2.length - j);
            return buf.toString();
        }
        return line;
    }
}

