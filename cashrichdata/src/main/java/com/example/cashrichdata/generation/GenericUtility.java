package com.example.cashrichdata.generation;



import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import java.beans.FeatureDescriptor;


import java.util.stream.Stream;

public class GenericUtility {
    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper wrappedSource = new BeanWrapperImpl(source);
        return Stream.of(wrappedSource.getPropertyDescriptors()).map(FeatureDescriptor::getName)
                .filter(propertyName -> wrappedSource.getPropertyValue(propertyName) == null)
                .toArray(String[]::new);
    }


    public static boolean isValidUserName(String username) {
        // Validate username: 4 to 15 characters, alphanumeric only
        return username != null && username.matches("^[a-zA-Z0-9]{4,15}$");
    }

    public static boolean isValidPassword(String password) {
        // Validate password: 8 to 15 characters with at least one uppercase, one lowercase, one digit, and one special character
        return password != null && password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,15}$");
    }
}
