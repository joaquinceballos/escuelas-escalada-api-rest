package es.uniovi.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import es.uniovi.domain.Ubicable;

public class CoordenadasValidator implements ConstraintValidator<Coordenadas, Ubicable> {

	@Override
	public boolean isValid(Ubicable ubicable, ConstraintValidatorContext context) {
		if (ubicable.getLatitud() == null && ubicable.getLongitud() == null) {
			return true;
		} else if (ubicable.getLatitud() == null) {
			return ubicable.getLongitud() == null;
		} else if (ubicable.getLongitud() == null) {
			return false;
		}
		return ubicable.getLatitud() >= -90 && 
		       ubicable.getLatitud() <= 90 && 
		       ubicable.getLongitud() >= -180 &&
		       ubicable.getLongitud() <= 180;
	}

}
