package com.rentease.rental_management.util.annotations;

import com.rentease.rental_management.util.annotations.validators.CarpetAreaValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CarpetAreaValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCarpetArea
{
    String message() default "Carpet area cannot be more than build up area.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
