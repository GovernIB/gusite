package es.caib.gusite.lucene.analysis;

import java.io.Reader;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardTokenizer;

import es.caib.gusite.lucene.hibernate.Constants;

public class CastellaCatalaAnalyzer extends Analyzer {

    protected static final String STEMMER = "Spanish";

    protected static final String STOP_WORDS[] = { "de", "es", "la", "i", "que", "a", "el", "o", "en", "un", "y", "una", "unes", "los", "uns", "del", "se", "tot", "las", "tamb\351", "por", "altre",
            "algun", "para", "alguna", "con", "alguns", "no", "algunes", "ser", "su", "\351s", "al", "soc", "ets", "lo", "som", "como", "estic", "m\341s", "est\340", "pero", "estem", "sus", "esteu",
            "le", "estan", "ya", "com", "fue", "per", "este", "perqu\350", "ha", "estat", "s\355", "estava", "porque", "ans", "esta", "abans", "son", "\351ssent", "entre", "ambd\363s", "est\341",
            "per\362", "cuando", "muy", "poder", "sin", "potser", "sobre", "puc", "podem", "tiene", "podeu", "tambi\351n", "poden", "me", "vaig", "hasta", "va", "hay", "van", "donde", "fer", "han",
            "faig", "quien", "fa", "est\341n", "fem", "estado", "feu", "desde", "fan", "todo", "cada", "nos", "fi", "durante", "incl\362s", "estados", "primer", "todos", "conseguir", "uno",
            "consegueixo", "les", "consigueix", "ni", "consigueixes", "contra", "conseguim", "otros", "consigueixen", "fueron", "anar", "ese", "haver", "eso", "tenir", "hab\355a", "tinc", "ante",
            "te", "ellos", "tenim", "e", "teniu", "esto", "tene", "m\355", "antes", "algunos", "qu\351", "els", "unos", "seu", "yo", "aqu\355", "otro", "meu", "otras", "teu", "otra", "ells", "\351l",
            "elles", "tanto", "ens", "esa", "nosaltres", "estos", "vosaltres", "mucho", "si", "quienes", "dins", "nada", "sols", "muchos", "solament", "cual", "saber", "sea", "saps", "poco", "sap",
            "ella", "sabem", "estar", "sabeu", "haber", "saben", "estas", "\372ltim", "estaba", "llarg", "estamos", "bastant", "algunas", "fas", "algo", "molts", "nosotros", "aquells", "mi",
            "aquelles", "mis", "seus", "t\372", "llavors", "sota", "ti", "dalt", "tu", "\372s", "tus", "molt", "ellas", "era", "nosotras", "eres", "vosostros", "erem", "vosostras", "eren", "os",
            "mode", "m\355o", "b\351", "m\355a", "quant", "m\355os", "quan", "m\355as", "on", "tuyo", "mentre", "tuya", "qui", "tuyos", "amb", "tuyas", "suyo", "sense", "suya", "jo", "suyos",
            "aquell", "suyas", "nuestro", "nuestra", "nuestros", "nuestras", "vuestro", "vuestra", "vuestros", "vuestras", "esos", "esas", "estoy", "est\341s", "est\341is", "est\351", "est\351s",
            "estemos", "est\351is", "est\351n", "estar\351", "estar\341s", "estar\341", "estaremos", "estar\351is", "estar\341n", "estar\355a", "estar\355as", "estar\355amos", "estar\355ais",
            "estar\355an", "estabas", "est\341bamos", "estabais", "estaban", "estuve", "estuviste", "estuvo", "estuvimos", "estuvisteis", "estuvieron", "estuviera", "estuvieras", "estuvi\351ramos",
            "estuvierais", "estuvieran", "estuviese", "estuvieses", "estuvi\351semos", "estuvieseis", "estuviesen", "estando", "estada", "estadas", "estad", "he", "has", "hemos", "hab\351is", "haya",
            "hayas", "hayamos", "hay\341is", "hayan", "habr\351", "habr\341s", "habr\341", "habremos", "habr\351is", "habr\341n", "habr\355a", "habr\355as", "habr\355amos", "habr\355ais",
            "habr\355an", "hab\355as", "hab\355amos", "hab\355ais", "hab\355an", "hube", "hubiste", "hubo", "hubimos", "hubisteis", "hubieron", "hubiera", "hubieras", "hubi\351ramos", "hubierais",
            "hubieran", "hubiese", "hubieses", "hubi\351semos", "hubieseis", "hubiesen", "habiendo", "habido", "habida", "habidos", "habidas", "soy", "somos", "sois", "seas", "seamos", "se\341is",
            "sean", "ser\351", "ser\341s", "ser\341", "seremos", "ser\351is", "ser\341n", "ser\355a", "ser\355as", "ser\355amos", "ser\355ais", "ser\355an", "eras", "\351ramos", "erais", "eran",
            "fui", "fuiste", "fuimos", "fuisteis", "fuera", "fueras", "fu\351ramos", "fuerais", "fueran", "fuese", "fueses", "fu\351semos", "fueseis", "fuesen", "sintiendo", "sentido", "sentida",
            "sentidos", "sentidas", "siente", "sentid", "tengo", "tienes", "tenemos", "ten\351is", "tienen", "tenga", "tengas", "tengamos", "teng\341is", "tengan", "tendr\351", "tendr\341s",
            "tendr\341", "tendremos", "tendr\351is", "tendr\341n", "tendr\355a", "tendr\355as", "tendr\355amos", "tendr\355ais", "tendr\355an", "ten\355a", "ten\355as", "ten\355amos", "ten\355ais",
            "ten\355an", "tuve", "tuviste", "tuvo", "tuvimos", "tuvisteis", "tuvieron", "tuviera", "tuvieras", "tuvi\351ramos", "tuvierais", "tuvieran", "tuviese", "tuvieses", "tuvi\351semos",
            "tuvieseis", "tuviesen", "teniendo", "tenido", "tenida", "tenidos", "tenidas", "tened" };

    private Set stopSet;


    public CastellaCatalaAnalyzer() {
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
