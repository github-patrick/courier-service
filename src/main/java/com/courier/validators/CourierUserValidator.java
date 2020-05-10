package com.courier.validators;

import com.courier.domain.dtos.CourierUserRequestDto;
import com.courier.domain.enums.UserType;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.Arrays;

@Component
public class CourierUserValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return CourierUserRequestDto.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CourierUserRequestDto courierUserRequestDto = (CourierUserRequestDto)target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "types", "User type cannot be empty or contain white spaces.");

        if (courierUserRequestDto.getPassword().length() < 6) {
            errors.rejectValue("password", "Password should be greater than 5 characters.");
        }
        if (!(courierUserRequestDto.getTypes() == null)) {
            courierUserRequestDto.getTypes().forEach(userType -> {
                if (!Arrays.asList(UserType.CUSTOMER, UserType.DRIVER).contains(userType)) {
                    errors.rejectValue("types", "A type can only be of CUSTOMER or DRIVER.");
                }
            });
        }
    }
}
