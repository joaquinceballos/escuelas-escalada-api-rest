package es.uniovi.validation.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import es.uniovi.domain.PeriodoHoras;
import es.uniovi.validation.RangoHorario;

public class RangoHorarioValidator implements ConstraintValidator<RangoHorario, PeriodoHoras> {

	@Override
	public boolean isValid(PeriodoHoras value, ConstraintValidatorContext context) {
		return value.getInicio() != null && value.getFin() != null && value.getFin().isAfter(value.getInicio());
	}

}
