package io.github.dinolupo.doit.business.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Created by dinolupo.github.io on 12/07/16.
 */
@Documented
@Constraint(validatedBy = CrossCheckConstraintValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CrossCheck {

    String message() default "Cross check failed!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
