package es.caib.gusite.micropersistence.util.lucene.analysis;

import java.io.Reader;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;

import es.caib.gusite.lucene.hibernate.Constants;

/**
 * Analizador de textos en frances 08/2010 La clase ha sido creada en el módulo
 * de microsites debido a que no se ha subido a producción el módulo 00librerias
 * donde está esta clase
 * 
 * @author Indra
 */
public class FrancesAnalyzer extends Analyzer {

	protected static final String STEMMER = "French";

	protected static final String[] STOP_WORDS = {

	"de", "la", "que", "le", "en", "et", "ï¿½", "les", "du", "se", "pour",
			"par", "un", "avec", "non", "une", "la sienne", "leur", "au", "il",
			"elle est", "le", "comme", "plus", "cependant", "les siennes",
			"leurs", "lui", "dï¿½jï¿½", "ou", "elle est allï¿½", "celui ci",
			"a", "oui", "parce que", "celle ci", "ils", "elles sont", "entre",
			"quand", "beaucoup", "sans", "sur", "ï¿½tre", "elle a", "aussi",
			"me", "jusqu'ï¿½", "il y a", "oï¿½", "ont", "qui", "ï¿½tat",
			"depuis", "tout", "organisme", "pendant", "ï¿½tats", "tous", "un",
			"mï¿½me pas", "contre", "quelques autres", "elles sont allï¿½s",
			"avait", "devant", "eux", "ceci", "moi", "avant", "quoi",
			"quelques uns", "un autre", "une autre", "tant", "pas du tout",
			"peu", "elle", "avoir", "elle ï¿½tait", "quelques unes",
			"quelque chose", "le mien", "les miens", "toi", "te", "le tien",
			"les tiens", "elles", "nous", "vous", "ï¿½ moi", "ï¿½ toi",
			"siï¿½ge", "ï¿½ lui", "elle", "siï¿½ges", "elle", "elles",
			"ï¿½ nous", "ï¿½ vous", "ceux ci", "celles ci", "tu es",
			"nous sommes", "vous ï¿½tes", "soyez", "tu sois", "nous soyons",
			"elle sera", "vous serez", "elles seront", "elle serait",
			"elles seraient", "elles ï¿½taient", "j'ai ï¿½tï¿½",
			"tu as ï¿½tï¿½", "elle a ï¿½tï¿½", "nous avons ï¿½tï¿½",
			"lancez ï¿½tre", "elles ont ï¿½tï¿½", "vous soyez", "elles soient",
			"elle soit", "sï¿½jour", "sï¿½jours", "ai", "as", "a", "avons",
			"avez", "ont", "ait", "aies", "aurai", "auras", "aura", "aurons",
			"aurez", "auront", "aurait", "aurais", "aurions", "auriez",
			"auraient", "salaire", "avais", "avions", "aviez", "avaient",
			"tu as eu", "a eu", "ont eu", "ayez", "ayons", "aient", "eu",
			"je suis", "tu es", "sommes", "je serai", "tu seras",
			"nous serons", "tu serais", "nous serions", "vous seriez",
			"tu ï¿½tais", "nous ï¿½tions", "vous ï¿½tiez", "je suis allï¿½",
			"tu es allï¿½", "nous sommes allï¿½s", "lancez aller", "dehors",
			"nous aillions", "elles aillent", "elle aille", "tu ailles",
			"vous ailliez", "sentant", "entendue", "sens", "entendues",
			"ï¿½tant", "sentez", "j'ai", "tendu", "nous avons", "vous avez",
			"elles ont", "nous ayons", "j'aurai", "tu auras", "elle aura",
			"nous aurons", "vous aurez", "elles auront", "elle aurait",
			"tu aurais", "nous aurions", "vous auriez", "elles auraient",
			"elle avait", "tu avais", "nous avions", "vous aviez",
			"elles avaient", "j'ai eu", "tu as eu", "elle a eu",
			"nous avons eu", "lancez avoir", "elles ont eu", "elle ait",
			"vous ayez", "elles aient", "tu aies", "ayant", "eu", "contenance",
			"eus", "contenances"

	};

	private Set<?> stopSet;

	public FrancesAnalyzer() {
		this.stopSet = StopFilter.makeStopSet(STOP_WORDS);
	}

	public TokenStream tokenStream(String fieldName, Reader reader) {
		TokenStream result = new StandardTokenizer(Constants.LUCENE_VERSION,
				reader);
		result = new StandardFilter(Constants.LUCENE_VERSION, result);
		result = new LowerCaseFilter(Constants.LUCENE_VERSION, result);
		result = new StopFilter(Constants.LUCENE_VERSION, result, this.stopSet);
		return result;
	}

}
