package es.caib.gusite.lucene.analysis;

import org.apache.lucene.analysis.Analyzer;

public class Analizador {

    public static Analyzer getAnalizador(String idioma) {

        Analyzer analyzer;

        if (idioma.toLowerCase().equals("de")) {
            analyzer = new AlemanAnalyzer();
        } else if (idioma.toLowerCase().equals("en")) {
            analyzer = new InglesAnalyzer();
        } else if (idioma.toLowerCase().equals("ca")) {
            analyzer = new CatalanAnalyzer();
        } else {
            analyzer = new CastellanoAnalyzer();
        }

        return analyzer;
    }
}
