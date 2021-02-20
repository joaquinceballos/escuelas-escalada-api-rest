package es.uniovi.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import es.uniovi.domain.Periodo;

public class PeriodoFechasValidator implements ConstraintValidator<PeriodoFechas, Periodo> {

	@Override
	public boolean isValid(Periodo value, ConstraintValidatorContext context) {
		return value.getInicio() != null && value.getFin() != null && value.getFin().isAfter(value.getInicio());
	}

}
