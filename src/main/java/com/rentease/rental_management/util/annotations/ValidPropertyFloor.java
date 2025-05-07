package com.rentease.rental_management.util.annotations;

import com.rentease.rental_management.util.annotations.validators.ValidPropertyFloorValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidPropertyFloorValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPropertyFloor 
{
    String message() default "Property floor must be less than or equal to total floors.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
