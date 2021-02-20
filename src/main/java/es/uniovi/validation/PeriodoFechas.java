package es.uniovi.validation;

import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Target(TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PeriodoFechasValidator.class)
public @interface PeriodoFechas {
	String message() default "Periodo de fechas no válido";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
