package com.wanted.feed.user.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Component;

@Component
public class PasswordSimilarValidator implements ConstraintValidator<PasswordSimilar, Object> {

    private String username;
    private String email;
    private String password;

    @Override
    public void initialize(PasswordSimilar constraintAnnotation) {
        username = constraintAnnotation.username();
        email = constraintAnnotation.email();
        password = constraintAnnotation.password();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        String usernameField = (String) new BeanWrapperImpl(value).getPropertyValue(username);
        String emailField = (String) new BeanWrapperImpl(value).getPropertyValue(email);
        String passwordField = (String) new BeanWrapperImpl(value).getPropertyValue(password);

        emailField = emailField.substring(0, emailField.indexOf("@"));

        return !passwordField.contains(usernameField) && !passwordField.contains(emailField);
    }
}
