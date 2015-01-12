package es.caib.gusite.micromodel;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import es.caib.gusite.extractor.Extractor;
import es.caib.gusite.extractor.ExtractorFactory;
import es.caib.gusite.lucene.model.ModelIndexObject;

/**
 * Clase que encapsula los datos a indexar.
 * @author Indra
 */
public class IndexObject extends ModelIndexObject {

	private static final long serialVersionUID = -2671707656052319079L;

	/**
	 * Definición: Añade un archivo para ser indexado. 
	 * @param archi un objeto Archivo
	 */
    public void addArchivo(Archivo archi) {
        if (archi != null && archi.getPeso() > 0) {
            Extractor extractor = ExtractorFactory.getExtractor(archi.getMime());
            if (extractor != null) {
                try {
                    String aText = extractor.extractText(new ByteArrayInputStream(archi.getDatos()));
                    addTextLine(aText);
                } catch (IOException e) {
                    ;
                }
            }
        }
    }

    /**
     * Definición: Añade una descripción para ser indexada.
     * @param archivo un objeto Archivo
     */
    public void addDescripcion(Archivo archivo) {
        if (archivo != null && archivo.getPeso() > 0) {
            Extractor extractor = ExtractorFactory.getExtractor(archivo.getMime());
            if (extractor != null) {
                try {
                    String aText = extractor.extractText(new ByteArrayInputStream(archivo.getDatos()));
            		if (aText.length()>200) addDescripcionLine(aText.substring(0,199)+"...");
                	else addDescripcionLine(aText);
                } catch (IOException e) {
                    ;
                }
            }
        }
    }    


}

