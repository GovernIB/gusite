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
 * 08/2010 La clase ha sido creada en el m�dulo de microsites debido 
 * a que no se ha subido a producci�n el m�dulo 00librerias donde est� esta clase
 * 
 * @author Indra
 */
public class FrancesAnalyzer extends Analyzer {

    protected static final String STEMMER = "French";

    protected static final String[] STOP_WORDS = {
    	
    	"de", "la", "que", "le", "en", "et", "�", "les", "du", "se", "pour", "par" , "un", "avec", "non", 
    	"une", "la sienne", "leur", "au", "il", "elle est", "le", "comme", "plus", "cependant",
    	"les siennes", "leurs", "lui", "d�j�", "ou", "elle est all�", "celui ci", "a",
    	"oui", "parce que", "celle ci", "ils", "elles sont", "entre", "quand", 
    	"beaucoup", "sans", "sur", "�tre", "elle a", "aussi", "me", "jusqu'�", "il y a", "o�", "ont", "qui", "�tat", 
    	"depuis", "tout", "organisme", "pendant", "�tats", "tous", "un", "m�me pas", "contre", "quelques autres", "elles sont all�s",
    	"avait", "devant", "eux", "ceci", "moi", "avant", "quoi", "quelques uns", "un autre", "une autre", "tant", "pas du tout",
    	"peu", "elle", "avoir", "elle �tait", "quelques unes", "quelque chose",
		"le mien", "les miens", "toi", "te", "le tien", "les tiens", "elles", "nous", "vous", "� moi", "� toi", "si�ge", "� lui", "elle",
		"si�ges", "elle","elles", "� nous", "� vous", "ceux ci", "celles ci", "tu es", 
		"nous sommes", "vous �tes", "soyez", "tu sois", "nous soyons", "elle sera", 
		"vous serez", "elles seront","elle serait", "elles seraient", "elles �taient",
		"j'ai �t�", "tu as �t�", "elle a �t�", "nous avons �t�", "lancez �tre", "elles ont �t�",
		"vous soyez","elles soient", "elle soit","s�jour", "s�jours","ai", "as", "a", "avons", "avez", "ont", "ait", "aies", 
        "aurai", "auras", "aura", "aurons", "aurez", "auront", "aurait", "aurais", "aurions", "auriez", "auraient", "salaire",
        "avais", "avions", "aviez", "avaient", "tu as eu", "a eu","ont eu",  "ayez", "ayons", "aient", "eu", "je suis", "tu es", "sommes",
        "je serai", "tu seras", "nous serons", "tu serais", "nous serions", "vous seriez","tu �tais", "nous �tions", "vous �tiez",
        "je suis all�", "tu es all�", "nous sommes all�s", "lancez aller","dehors", "nous aillions", "elles aillent","elle aille", "tu ailles",
		"vous ailliez", "sentant", "entendue", "sens", "entendues", "�tant", "sentez", "j'ai", "tendu", "nous avons", "vous avez",
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

