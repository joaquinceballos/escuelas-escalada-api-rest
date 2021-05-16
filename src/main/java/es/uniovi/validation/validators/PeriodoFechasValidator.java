package es.uniovi.validation.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import es.uniovi.domain.PeriodoDias;
import es.uniovi.validation.PeriodoFechas;

public class PeriodoFechasValidator implements ConstraintValidator<PeriodoFechas, PeriodoDias> {

	@Override
	public boolean isValid(PeriodoDias value, ConstraintValidatorContext context) {
		return value.getInicio() != null && value.getFin() != null && value.getFin().isAfter(value.getInicio());
	}

}
