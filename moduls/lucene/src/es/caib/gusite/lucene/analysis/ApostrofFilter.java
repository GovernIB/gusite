package es.caib.gusite.lucene.analysis;

import java.io.IOException;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardTokenizer;

public class ApostrofFilter extends TokenFilter {

    private static final String APOSTROPHE_TYPE;


    public ApostrofFilter(TokenStream in) {
        super(in);
    }


    public boolean incrementToken() throws IOException {

        return false;
//        Token t = input.next();
//        if (t == null) {
//            return null;
//            return false;
//        }
//        String text = t.term();
//        String type = t.type();
//        text = prepararAcentos(text);
//
//        if (type == APOSTROPHE_TYPE
//            && (text.startsWith("d'") || text.startsWith("D'") || text.startsWith("l'") || text.startsWith("L'") || text.startsWith("s'") || text.startsWith("S'") || text.startsWith("m'")
//                || text.startsWith("M'") || text.startsWith("t'") || text.startsWith("T'") || text.startsWith("n'") || text.startsWith("N'"))) {
//            return new Token(text.substring(2, text.length()), t.startOffset(), t.endOffset(), type);
//            return true;
//        } else {
//            return new Token(text, t.startOffset(), t.endOffset(), type);
//            return false;
//        }
    }


    private String prepararAcentos(String cadena) {

        String txcadena = reemplazarCaracter(cadena, '\341', 'a');
        txcadena = reemplazarCaracter(txcadena, '\340', 'a');
        txcadena = reemplazarCaracter(txcadena, '\350', 'e');
        txcadena = reemplazarCaracter(txcadena, '\351', 'e');
        txcadena = reemplazarCaracter(txcadena, '\355', 'i');
        txcadena = reemplazarCaracter(txcadena, '\354', 'i');
        txcadena = reemplazarCaracter(txcadena, '\363', 'o');
        txcadena = reemplazarCaracter(txcadena, '\362', 'o');
        txcadena = reemplazarCaracter(txcadena, '\372', 'u');
        txcadena = reemplazarCaracter(txcadena, '\371', 'u');
        return txcadena;
    }


    private String reemplazarCaracter(String cadenaorigen, char oldcar, char newcar) {
        return cadenaorigen.replace(oldcar, newcar);
    }


    static {
        APOSTROPHE_TYPE = StandardTokenizer.TOKEN_TYPES[2];
    }

}
