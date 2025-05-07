package com.rentease.rental_management.util.annotations.validators;

import com.rentease.rental_management.util.annotations.MinAge;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;

public class MinAgeValidator implements ConstraintValidator<MinAge, LocalDate>
{
    private Integer ageLimit;

    @Override
    public void initialize(MinAge minAge)
    {
        ageLimit = minAge.value();
    }

    @Override
    public boolean isValid(LocalDate dateOfBirth, ConstraintValidatorContext constraintValidatorContext)
    {
        if(dateOfBirth == null)
            return false;

        return Period.between(dateOfBirth, LocalDate.now()).getYears() >= ageLimit;

    }
}
