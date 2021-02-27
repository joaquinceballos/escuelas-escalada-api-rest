package es.uniovi.search.analyzer;

import java.util.Arrays;
import java.util.Collection;

public class NombreEscuelaAnalyzer extends ApiAnalyzer {

	@Override
	protected Collection<String> palabrasVaciasEspecificas() {
		return Arrays.asList("escuela", "escuelas", "escalada");
	}

}
