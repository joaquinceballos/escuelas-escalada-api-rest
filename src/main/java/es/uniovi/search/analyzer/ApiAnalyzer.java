package es.uniovi.search.analyzer;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

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

public class ApiAnalyzer extends Analyzer {

	@Override
	protected TokenStreamComponents createComponents(String fieldName) {
		StandardTokenizer src = new StandardTokenizer();
		TokenStream result = new StandardFilter(src);
		result = new ASCIIFoldingFilter(result);
		result = new LowerCaseFilter(result);
		result = new StopFilter(result, EnglishAnalyzer.getDefaultStopSet());
		result = new StopFilter(result, SpanishAnalyzer.getDefaultStopSet());
		CharArraySet charArraySet = new CharArraySet(Arrays.asList("escalada"), true);
		charArraySet.addAll(palabrasVaciasEspecificas());
		result = new StopFilter(result, charArraySet);		
		result = new PorterStemFilter(result);
		result = new CapitalizationFilter(result);
		return new TokenStreamComponents(src, result);
	}

	/**
	 * @return Set de palabras vacías específica para el ApiAnalyzer concreto
	 */
	protected Collection<String> palabrasVaciasEspecificas() {
		return Collections.emptyList();
	}

}
