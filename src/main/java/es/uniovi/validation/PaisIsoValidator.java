package es.uniovi.validation;

import java.util.Arrays;
import java.util.Locale;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PaisIsoValidator implements ConstraintValidator<PaisIso, String> {

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null) {
			return true;
		}
		return Arrays.asList(Locale.getISOCountries()).stream().anyMatch(p -> p.equals(value));
	}

}
