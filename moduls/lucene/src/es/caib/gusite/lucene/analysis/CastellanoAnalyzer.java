package es.caib.gusite.lucene.analysis;

import java.io.Reader;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardTokenizer;

import es.caib.gusite.lucene.hibernate.Constants;

public class CastellanoAnalyzer extends Analyzer {

    protected static final String STEMMER = "Spanish";

    protected static final String STOP_WORDS[] = { "de", "la", "que", "el", "en", "y", "a", "los", "del", "se", "las", "por", "un", "para", "con", "no", "una", "su", "al", "es", "lo", "como",
            "m\341s", "pero", "sus", "le", "ya", "o", "fue", "este", "ha", "s\355", "porque", "esta", "son", "entre", "est\341", "cuando", "muy", "sin", "sobre", "ser", "tiene", "tambi\351n", "me",
            "hasta", "hay", "donde", "han", "quien", "est\341n", "estado", "desde", "todo", "nos", "durante", "estados", "todos", "uno", "les", "ni", "contra", "otros", "fueron", "ese", "eso",
            "hab\355a", "ante", "ellos", "e", "esto", "m\355", "antes", "algunos", "qu\351", "unos", "yo", "otro", "otras", "otra", "\351l", "tanto", "esa", "estos", "mucho", "quienes", "nada",
            "muchos", "cual", "sea", "poco", "ella", "estar", "haber", "estas", "estaba", "estamos", "algunas", "algo", "nosotros", "mi", "mis", "t\372", "te", "ti", "tu", "tus", "ellas", "nosotras",
            "vosostros", "vosostras", "os", "m\355o", "m\355a", "m\355os", "m\355as", "tuyo", "tuya", "tuyos", "tuyas", "suyo", "suya", "suyos", "suyas", "nuestro", "nuestra", "nuestros", "nuestras",
            "vuestro", "vuestra", "vuestros", "vuestras", "esos", "esas", "estoy", "est\341s", "est\341", "estamos", "est\341is", "est\341n", "est\351", "est\351s", "estemos", "est\351is",
            "est\351n", "estar\351", "estar\341s", "estar\341", "estaremos", "estar\351is", "estar\341n", "estar\355a", "estar\355as", "estar\355amos", "estar\355ais", "estar\355an", "estaba",
            "estabas", "est\341bamos", "estabais", "estaban", "estuve", "estuviste", "estuvo", "estuvimos", "estuvisteis", "estuvieron", "estuviera", "estuvieras", "estuvi\351ramos", "estuvierais",
            "estuvieran", "estuviese", "estuvieses", "estuvi\351semos", "estuvieseis", "estuviesen", "estando", "estado", "estada", "estados", "estadas", "estad", "he", "has", "ha", "hemos",
            "hab\351is", "han", "haya", "hayas", "hayamos", "hay\341is", "hayan", "habr\351", "habr\341s", "habr\341", "habremos", "habr\351is", "habr\341n", "habr\355a", "habr\355as",
            "habr\355amos", "habr\355ais", "habr\355an", "hab\355a", "hab\355as", "hab\355amos", "hab\355ais", "hab\355an", "hube", "hubiste", "hubo", "hubimos", "hubisteis", "hubieron", "hubiera",
            "hubieras", "hubi\351ramos", "hubierais", "hubieran", "hubiese", "hubieses", "hubi\351semos", "hubieseis", "hubiesen", "habiendo", "habido", "habida", "habidos", "habidas", "soy", "eres",
            "es", "somos", "sois", "son", "sea", "seas", "seamos", "se\341is", "sean", "ser\351", "ser\341s", "ser\341", "seremos", "ser\351is", "ser\341n", "ser\355a", "ser\355as", "ser\355amos",
            "ser\355ais", "ser\355an", "era", "eras", "\351ramos", "erais", "eran", "fui", "fuiste", "fue", "fuimos", "fuisteis", "fueron", "fuera", "fueras", "fu\351ramos", "fuerais", "fueran",
            "fuese", "fueses", "fu\351semos", "fueseis", "fuesen", "sintiendo", "sentido", "sentida", "sentidos", "sentidas", "siente", "sentid", "tengo", "tienes", "tiene", "tenemos", "ten\351is",
            "tienen", "tenga", "tengas", "tengamos", "teng\341is", "tengan", "tendr\351", "tendr\341s", "tendr\341", "tendremos", "tendr\351is", "tendr\341n", "tendr\355a", "tendr\355as",
            "tendr\355amos", "tendr\355ais", "tendr\355an", "ten\355a", "ten\355as", "ten\355amos", "ten\355ais", "ten\355an", "tuve", "tuviste", "tuvo", "tuvimos", "tuvisteis", "tuvieron",
            "tuviera", "tuvieras", "tuvi\351ramos", "tuvierais", "tuvieran", "tuviese", "tuvieses", "tuvi\351semos", "tuvieseis", "tuviesen", "teniendo", "tenido", "tenida", "tenidos", "tenidas",
            "tened" };

    private Set stopSet;


    public CastellanoAnalyzer() {
        stopSet = StopFilter.makeStopSet(Constants.LUCENE_VERSION, STOP_WORDS);
    }


    public TokenStream tokenStream(String fieldName, Reader reader) {
        TokenStream result = new StandardTokenizer(Constants.LUCENE_VERSION, reader);
//        result = new StandardFilter(Constants.LUCENE_VERSION, result);
//        result = new LowerCaseFilter(Constants.LUCENE_VERSION, result);
//        result = new ApostrofFilter(result);
//        result = new StopFilter(Constants.LUCENE_VERSION, result, stopSet);
        return result;
    }

}
