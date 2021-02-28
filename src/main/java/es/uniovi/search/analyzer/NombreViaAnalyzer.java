package es.uniovi.search.analyzer;

import java.util.Arrays;
import java.util.Collection;

public class NombreViaAnalyzer extends ApiAnalyzer {

	@Override
	protected Collection<String> palabrasVaciasEspecificas() {
		return Arrays.asList("vía", "vías");
	}

}
