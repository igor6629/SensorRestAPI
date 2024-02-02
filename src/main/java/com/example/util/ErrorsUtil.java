package com.example.util;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

public class ErrorsUtil {
    public static void notCreatedException(BindingResult bindingResult) {
        StringBuilder msg = new StringBuilder();
        List<FieldError> errors = bindingResult.getFieldErrors();

        for (FieldError error : errors)
            msg.append(error.getField()).append(" - ").append(error.getDefaultMessage()).append(";");

        throw new NotCreatedException(msg.toString());
    }
}
