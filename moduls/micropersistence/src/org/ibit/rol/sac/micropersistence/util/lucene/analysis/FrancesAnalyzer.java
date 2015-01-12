package org.ibit.rol.sac.micropersistence.util.lucene.analysis;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;

import java.io.Reader;
import java.util.Set;

/**
 * Analizador de textos en frances
 * 08/2010 La clase ha sido creada en el módulo de microsites debido 
 * a que no se ha subido a producción el módulo 00librerias donde está esta clase
 * 
 * @author Indra
 */
public class FrancesAnalyzer extends Analyzer {

    protected static final String STEMMER = "French";

    protected static final String[] STOP_WORDS = {
    	
    	"de", "la", "que", "le", "en", "et", "à", "les", "du", "se", "pour", "par" , "un", "avec", "non", 
    	"une", "la sienne", "leur", "au", "il", "elle est", "le", "comme", "plus", "cependant",
    	"les siennes", "leurs", "lui", "déjà", "ou", "elle est allé", "celui ci", "a",
    	"oui", "parce que", "celle ci", "ils", "elles sont", "entre", "quand", 
    	"beaucoup", "sans", "sur", "être", "elle a", "aussi", "me", "jusqu'à", "il y a", "où", "ont", "qui", "état", 
    	"depuis", "tout", "organisme", "pendant", "états", "tous", "un", "même pas", "contre", "quelques autres", "elles sont allés",
    	"avait", "devant", "eux", "ceci", "moi", "avant", "quoi", "quelques uns", "un autre", "une autre", "tant", "pas du tout",
    	"peu", "elle", "avoir", "elle était", "quelques unes", "quelque chose",
		"le mien", "les miens", "toi", "te", "le tien", "les tiens", "elles", "nous", "vous", "à moi", "à toi", "siège", "à lui", "elle",
		"sièges", "elle","elles", "à nous", "à vous", "ceux ci", "celles ci", "tu es", 
		"nous sommes", "vous êtes", "soyez", "tu sois", "nous soyons", "elle sera", 
		"vous serez", "elles seront","elle serait", "elles seraient", "elles étaient",
		"j'ai été", "tu as été", "elle a été", "nous avons été", "lancez être", "elles ont été",
		"vous soyez","elles soient", "elle soit","séjour", "séjours","ai", "as", "a", "avons", "avez", "ont", "ait", "aies", 
        "aurai", "auras", "aura", "aurons", "aurez", "auront", "aurait", "aurais", "aurions", "auriez", "auraient", "salaire",
        "avais", "avions", "aviez", "avaient", "tu as eu", "a eu","ont eu",  "ayez", "ayons", "aient", "eu", "je suis", "tu es", "sommes",
        "je serai", "tu seras", "nous serons", "tu serais", "nous serions", "vous seriez","tu étais", "nous étions", "vous étiez",
        "je suis allé", "tu es allé", "nous sommes allés", "lancez aller","dehors", "nous aillions", "elles aillent","elle aille", "tu ailles",
		"vous ailliez", "sentant", "entendue", "sens", "entendues", "étant", "sentez", "j'ai", "tendu", "nous avons", "vous avez",
		"elles ont", "nous ayons", "j'aurai", "tu auras", "elle aura", "nous aurons", "vous aurez", 
		"elles auront", "elle aurait", "tu aurais", "nous aurions", "vous auriez", "elles auraient",
		"elle avait" , "tu avais", "nous avions", "vous aviez", "elles avaient","j'ai eu", "tu as eu","elle a eu", "nous avons eu", 
		"lancez avoir","elles ont eu", "elle ait","vous ayez", "elles aient","tu aies", "ayant", "eu", "contenance", "eus", "contenances"
    	
    };

    private Set<?> stopSet;

    public FrancesAnalyzer() {
        stopSet = StopFilter.makeStopSet(STOP_WORDS);
    }

    public TokenStream tokenStream(String fieldName, Reader reader) {
        TokenStream result = new StandardTokenizer(reader);
        result = new StandardFilter(result);
        result = new LowerCaseFilter(result);
        result = new StopFilter(result, stopSet);
        return result;
    }

}

