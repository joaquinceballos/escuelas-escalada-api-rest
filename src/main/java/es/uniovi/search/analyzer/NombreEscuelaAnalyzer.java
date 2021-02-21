package es.uniovi.search.analyzer;

import java.util.Arrays;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.es.SpanishAnalyzer;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;
import org.apache.lucene.analysis.miscellaneous.CapitalizationFilter;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.util.CharArraySet;

public class NombreEscuelaAnalyzer extends Analyzer {

	@Override
	protected TokenStreamComponents createComponents(String fieldName) {
		StandardTokenizer src = new StandardTokenizer();
		TokenStream result = new StandardFilter(src);
		result = new ASCIIFoldingFilter(result);
		result = new LowerCaseFilter(result);
		result = new StopFilter(result, EnglishAnalyzer.getDefaultStopSet());
		result = new StopFilter(result, SpanishAnalyzer.getDefaultStopSet());
		result = new StopFilter(result, new CharArraySet(Arrays.asList("escuela", "escuelas", "escalada"), true));
		result = new PorterStemFilter(result);
		result = new CapitalizationFilter(result);
		return new TokenStreamComponents(src, result);
	}

}
