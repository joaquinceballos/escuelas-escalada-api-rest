package es.uniovi.validation.validators;

import java.util.Arrays;
import java.util.Locale;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import es.uniovi.validation.PaisIso;

public class PaisIsoValidator implements ConstraintValidator<PaisIso, String> {

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null) {
			return true;
		}
		return Arrays
				.asList(Locale.getISOCountries())
				.stream()
				.filter(c -> c.length() == 2) /* sólo se validarán los códigos de 2 caracteres */
				.anyMatch(p -> p.equals(value));
	}

}
