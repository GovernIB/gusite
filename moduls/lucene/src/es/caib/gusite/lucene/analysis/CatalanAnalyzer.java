package es.caib.gusite.lucene.analysis;

import java.io.Reader;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardTokenizer;

import es.caib.gusite.lucene.hibernate.Constants;

public class CatalanAnalyzer extends Analyzer {

    protected static final String STEMMER = "Spanish";

    protected static final String STOP_WORDS[] = { "de", "la", "que", "el", "en", "i", "a", "els", "del", "es", "les", "per", "un", "per a", "amb", "no", "una", "la seva", "al", "\351s", "ho", "com",
            "m\351s", "per\362", "les seves", "li", "ja", "o", "va anar", "aquest", "ha", "s\355", "perqu\350", "aquesta", "s\363n", "entre", "est\340", "quan", "molt", "sense", "sobre", "\351sser",
            "t\351", "tamb\351", "em", "fins a", "hi ha", "on", "han", "qui", "estan", "estat", "des de", "tot", "ens", "durant", "estats", "tots", "un", "els", "ni", "contra", "uns altres",
            "van anar", "aquest", "aix\362", "havia", "davant", "ells", "i", "aix\362", "mi", "abans", "alguns", "qu\350", "uns", "jo", "un altre", "unes altres", "una altra", "ell", "tant",
            "aquesta", "aquests", "molt", "qui", "gens", "molts", "com", "sigui", "poc", "ella", "estar", "haver", "aquestes", "estava", "estem", "algunes", "alguna cosa", "nosaltres", "el meu",
            "els meus", "tu", "et", "tu", "el teu", "els teus", "elles", "nosaltres", "vosaltres", "vosaltres", "us", "meu", "meva", "meus", "meves", "teu", "teva", "teus", "teves", "seu", "seva",
            "seus", "seves", "nostre", "nostra", "nostres", "nostres", "vostre", "vostra", "vostres", "vostres", "aquests", "aquestes", "estic", "est\340s", "est\340", "estem", "esteu", "estan",
            "estigui", "estiguis", "estiguem", "estigueu", "estiguin", "estar\351", "estar\340s", "estar\340", "estarem", "estareu", "estaran", "estaria", "estaries", "estar\355em", "estar\355eu",
            "estarien", "estava", "estaves", "est\340vem", "est\340veu", "estaven", "vaig estar", "vas estar", "va estar", "vam estar", "vareu estar", "van estar", "estigu\351s", "estiguessis",
            "estigu\351ssim", "estigu\351ssiu", "estiguessin", "estigu\351s", "estiguessis", "estigu\351ssim", "estigu\351ssiu", "estiguessin", "estant", "estat", "estada", "estats", "estades",
            "estigueu", "he", "has", "ha", "hem", "heu", "han", "hagi", "hagis", "h\340gim", "h\340giu", "hagin", "haur\351", "haur\340s", "haur\340", "haurem", "haureu", "hauran", "hauria",
            "hauries", "haur\355em", "haur\355eu", "haurien", "havia", "havies", "hav\355em", "hav\355eu", "havien", "vaig haver", "vas haver", "va haver", "vam haver", "vareu haver", "van haver",
            "hagu\351s", "haguessis", "hagu\351ssim", "hagu\351ssiu", "haguessin", "hagu\351s", "haguessis", "hagu\351ssim", "hagu\351ssiu", "haguessin", "havent", "hagut", "haguda", "haguts",
            "hagudes", "s\363c", "ets", "\351s", "som", "sou", "s\363n", "sigui", "siguis", "siguem", "sigueu", "siguin", "ser\351", "ser\340s", "ser\340", "serem", "sereu", "seran", "seria",
            "series", "ser\355em", "ser\355eu", "serien", "era", "eres", "\351rem", "\351reu", "eren", "vaig anar", "vas anar", "va anar", "vam anar", "vareu anar", "van anar", "fora", "anessis",
            "an\351ssim", "an\351ssiu", "anessin", "an\351s", "anessis", "an\351ssim", "an\351ssiu", "anessin", "sentint", "sentit", "sentida", "sentits", "sentides", "sent", "sentiu", "tinc",
            "tens", "t\351", "tenim", "teniu", "tenen", "tingui", "tinguis", "tinguem", "tingueu", "tinguin", "tindr\351", "tindr\340s", "tindr\340", "tindrem", "tindreu", "tindran", "tindria",
            "tindries", "tindr\355em", "tindr\355eu", "tindrien", "tenia", "tenies", "ten\355em", "ten\355eu", "tenien", "vaig tenir", "vas tenir", "va tenir", "vam tenir", "vareu tenir",
            "van tenir", "tingu\351s", "tinguessis", "tingu\351ssim", "tingu\351ssiu", "tinguessin", "tingu\351s", "tinguessis", "tingu\351ssim", "tingu\351ssiu", "tinguessin", "tenint", "tingut",
            "tinguda", "tinguts", "tingudes", "tingueu" };

    private Set stopSet;


    public CatalanAnalyzer() {
        stopSet = StopFilter.makeStopSet(Constants.LUCENE_VERSION, STOP_WORDS);
    }


    public TokenStream tokenStream(String fieldName, Reader reader) {
        TokenStream result = new StandardTokenizer(Constants.LUCENE_VERSION, reader);
//        result = new StandardFilter(LUCENE_VERSION, result);
//        result = new ApostrofFilter(result);
//        result = new LowerCaseFilter(LUCENE_VERSION, result);
        result = new StopFilter(Constants.LUCENE_VERSION, result, stopSet);
        return result;
    }

}
