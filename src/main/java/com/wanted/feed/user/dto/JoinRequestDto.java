package com.wanted.feed.user.dto;

import com.wanted.feed.user.validation.PasswordSimilar;
import com.wanted.feed.user.validation.ValidationGroups;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@PasswordSimilar(
        username = "username", email = "email", password = "password",
        groups = ValidationGroups.SimilarCheckGroup.class)
public class JoinRequestDto {

    @NotBlank(message = "필수입니다.", groups = ValidationGroups.NotEmptyGroup.class)
    private String username;

    @Pattern(
            message = "형식에 맞게 입력해주세요.",
            regexp = "^[A-Za-z0-9\\-]+@[A-Za-z0-9\\-]+\\.[A-Za-z0-9\\-]+$",
            groups = ValidationGroups.PatternCheckGroup.class
    )
    @NotBlank(message = "필수입니다.", groups = ValidationGroups.NotEmptyGroup.class)
    private String email;

    @Pattern(
            message = "형식에 맞게 입력해주세요.",
            regexp = "^(?=(?:.*\\d.*)(?:.*[A-Za-z].*|.*[\\W_].*)|"
                    + ".*[A-Za-z].*(?:.*\\d.*|.*[\\W_].*)|.*[\\W_]"
                    + ".*(?:.*\\d.*|.*[A-Za-z].*))[A-Za-z\\d\\W_]{10,}$",
            groups = ValidationGroups.PatternCheckGroup.class
    )
    @NotBlank(message = "필수입니다.", groups = ValidationGroups.NotEmptyGroup.class)
    private String password;

    public JoinRequestDto(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}