package es.uniovi.validation;

import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import es.uniovi.validation.validators.CoordenadasValidator;

@Documented
@Target(TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CoordenadasValidator.class)
public @interface Coordenadas {
	String message() default "{error.coordenadas}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
