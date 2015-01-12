package org.ibit.rol.sac.micromodel;

import org.ibit.rol.sac.extractor.Extractor;
import org.ibit.rol.sac.extractor.ExtractorFactory;


import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.ibit.lucene.indra.model.ModelIndexObject;

/**
 * Clase que encapsula los datos a indexar.
 * @author Indra
 */
public class IndexObject extends ModelIndexObject {

	private static final long serialVersionUID = -2671707656052319079L;

	/**
	 * Definición: Añade un archivo para ser indexado. 
	 * @param archivo un objeto Archivo
	 */
    public void addArchivo(Archivo archivo) {
        if (archivo != null && archivo.getPeso() > 0) {
            Extractor extractor = ExtractorFactory.getExtractor(archivo.getMime());
            if (extractor != null) {
                try {
                    String aText = extractor.extractText(new ByteArrayInputStream(archivo.getDatos()));
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

