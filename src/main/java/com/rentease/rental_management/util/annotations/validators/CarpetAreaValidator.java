package com.rentease.rental_management.util.annotations.validators;

import com.rentease.rental_management.util.annotations.ValidCarpetArea;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CarpetAreaValidator implements ConstraintValidator<ValidCarpetArea, Object>
{
    Integer buildUpArea = null;
    Integer carpetArea = null;

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext constraintValidatorContext)
    {
        try
        {
            if(obj == null)
                return false;

            Method getBuildUpArea = obj.getClass().getMethod("getBuildUpArea");
            Method getCarpetArea = obj.getClass().getMethod("getCarpetArea");

            buildUpArea = (Integer) getBuildUpArea.invoke(obj);
            carpetArea = (Integer) getCarpetArea.invoke(obj);

            return carpetArea <= buildUpArea;
        }
        catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }
    }
}
