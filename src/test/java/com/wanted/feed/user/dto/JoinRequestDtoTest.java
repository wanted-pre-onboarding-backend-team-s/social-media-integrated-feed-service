package com.wanted.feed.user.dto;

import com.wanted.feed.user.validation.ValidationGroups.NotEmptyGroup;
import com.wanted.feed.user.validation.ValidationGroups.PatternCheckGroup;
import com.wanted.feed.user.validation.ValidationGroups.SimilarCheckGroup;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JoinRequestDtoTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    public static void init() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    public static void close() {
        factory.close();
    }

    @Test
    @DisplayName("이메일 형식이 맞지 않으면 에러 발생")
    void email_pattern_validation_fail() {

        JoinRequestDto joinRequest = new JoinRequestDto(
                "wanted", "wantedwanted.com", "abcde12345");

        Set<ConstraintViolation<JoinRequestDto>> violations = validator.validate(joinRequest,
                PatternCheckGroup.class);

        Assertions.assertThat(violations).isNotEmpty();
    }


    @Test
    @DisplayName("비밀번호 빈 값이면 에러 발생")
    void password_notblank_validation_fail() {
        JoinRequestDto joinRequest = new JoinRequestDto("wanted", "wanted@wanted.com", "");

        Set<ConstraintViolation<JoinRequestDto>> violations = validator.validate(joinRequest,
                NotEmptyGroup.class);

        Assertions.assertThat(violations).isNotEmpty();
    }

    @Test
    @DisplayName("비밀번호 형식이 맞지 않으면 에러 발생")
    void password_pattern_validation_fail() {

        JoinRequestDto joinRequest = new JoinRequestDto(
                "wanted", "wanted@wanted.com", "1234567890");

        Set<ConstraintViolation<JoinRequestDto>> violations = validator.validate(joinRequest,
                PatternCheckGroup.class);

        Assertions.assertThat(violations).isNotEmpty();
    }

    @Test
    @DisplayName("비밀번호 형식이 맞으면 성공")
    void password_pattern_validation_success() {

        JoinRequestDto joinRequest = new JoinRequestDto(
                "wanted", "wanted@wanted.com", "abcde12345!");

        Set<ConstraintViolation<JoinRequestDto>> violations = validator.validate(joinRequest,
                PatternCheckGroup.class);

        Assertions.assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("비밀번호 다른 개인정보와 유사하면 에러 발생")
    void password_similar_validation_success() {

        JoinRequestDto joinRequest = new JoinRequestDto(
                "wanted", "wanted@wanted.com", "wanted12345");

        Set<ConstraintViolation<JoinRequestDto>> violations = validator.validate(joinRequest,
                SimilarCheckGroup.class);

        Assertions.assertThat(violations).isNotEmpty();
    }

}