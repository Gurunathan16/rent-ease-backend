package com.rentease.rental_management.util.annotations.validators;


import com.rentease.rental_management.util.annotations.ValidPropertyFloor;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ValidPropertyFloorValidator implements ConstraintValidator<ValidPropertyFloor, Object>
{

    Integer propertyFloor = null;
    Integer totalFloors = null;

    @Override
    public boolean isValid(Object obj,
                           ConstraintValidatorContext constraintValidatorContext) {
        try {
            if(obj == null)
                return false;

            Method getPropertyFloor = obj.getClass().getMethod("getPropertyFloor");
            Method getTotalFloor = obj.getClass().getMethod("getTotalFloor");

            propertyFloor = (Integer) getPropertyFloor.invoke(obj);
            totalFloors = (Integer) getTotalFloor.invoke(obj);


            return propertyFloor <= totalFloors;

        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
