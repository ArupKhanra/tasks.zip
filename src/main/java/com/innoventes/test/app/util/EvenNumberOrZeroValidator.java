package com.innoventes.test.app.util;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EvenNumberOrZeroValidator implements ConstraintValidator<EvenNumberOrZero, Number> {

    @Override
    public void initialize(EvenNumberOrZero constraintAnnotation) {
    }

    @Override
    public boolean isValid(Number value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Null values are considered valid (you can change this behavior if needed)
        }

        long number = value.longValue();
        return number % 2 == 0;
    }
}
