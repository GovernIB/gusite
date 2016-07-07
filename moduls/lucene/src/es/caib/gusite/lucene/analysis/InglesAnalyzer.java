package es.caib.gusite.lucene.analysis;

import java.io.Reader;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardTokenizer;

import es.caib.gusite.lucene.hibernate.Constants;

public class InglesAnalyzer extends Analyzer {

    protected static final String STEMMER = "English";

    protected static final String STOP_WORDS[] = { "i", "me", "my", "myself", "we", "us", "our", "ours", "ourselves", "you", "your", "yours", "yourself", "yourselves", "he", "him", "his", "himself",
            "she", "her", "hers", "herself", "it", "its", "itself", "they", "them", "their", "theirs", "themselves", "what", "which", "who", "whom", "this", "that", "these", "those", "am", "is",
            "are", "was", "were", "be", "been", "being", "have", "has", "had", "having", "do", "does", "did", "doing", "will", "would", "shall", "should", "can", "could", "may", "might", "must",
            "ought", "i'm", "you're", "he's", "she's", "it's", "we're", "they're", "i've", "you've", "we've", "they've", "i'd", "you'd", "he'd", "she'd", "we'd", "they'd", "i'll", "you'll", "he'll",
            "she'll", "we'll", "they'll", "isn't", "aren't", "wasn't", "weren't", "hasn't", "haven't", "hadn't", "doesn't", "don't", "didn't", "won't", "wouldn't", "shan't", "shouldn't", "can't",
            "cannot", "couldn't", "mustn't", "let's", "that's", "who's", "what's", "here's", "there's", "when's", "where's", "why's", "how's", "a", "an", "the", "and", "but", "if", "or", "because",
            "as", "until", "while", "of", "at", "by", "for", "with", "about", "against", "between", "into", "through", "during", "before", "after", "above", "below", "to", "from", "up", "down", "in",
            "out", "on", "off", "over", "under", "again", "further", "then", "once", "here", "there", "when", "where", "why", "how", "all", "any", "both", "each", "few", "more", "most", "other",
            "some", "such", "no", "nor", "not", "only", "own", "same", "so", "than", "too", "very", "one", "every", "least", "less", "many", "now", "ever", "never", "say", "says", "said", "also",
            "get", "go", "goes", "just", "made", "make", "put", "see", "seen", "whether", "like", "well", "back", "even", "still", "way", "take", "since", "another", "however", "two", "three",
            "four", "five", "first", "second", "new", "old", "high", "long" };

    private Set stopSet;


    public InglesAnalyzer() {
        stopSet = StopFilter.makeStopSet(Constants.LUCENE_VERSION, STOP_WORDS);
    }


    public TokenStream tokenStream(String fieldName, Reader reader) {
        TokenStream result = new StandardTokenizer(Constants.LUCENE_VERSION, reader);
//        result = new StandardFilter(Constants.LUCENE_VERSION, result);
//        result = new LowerCaseFilter(Constants.LUCENE_VERSION, result);
//        result = new StopFilter(Constants.LUCENE_VERSION, result, stopSet);
        return result;
    }

}
