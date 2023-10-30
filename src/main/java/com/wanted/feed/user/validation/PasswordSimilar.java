package com.wanted.feed.user.validation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;


@Target(TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = PasswordSimilarValidator.class)
public @interface PasswordSimilar {

    String username();

    String email();

    String password();

    String message() default "다른 개인정보와 유사한 비밀번호는 사용할 수 없습니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
