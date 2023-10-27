package com.wanted.feed.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.wanted.feed.user.domain.AuthCodeRepository;
import com.wanted.feed.user.domain.User;
import com.wanted.feed.user.domain.UserRepository;
import com.wanted.feed.user.dto.JoinRequestDto;
import com.wanted.feed.user.dto.JoinResponseDto;
import com.wanted.feed.user.exception.DuplicateUserException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@ExtendWith(MockitoExtension.class)
class JoinServiceTest {

    private static final String REQUEST_USERNAME = "wanted";
    private static final String REQUEST_EMAIL = "wanted@wanted.com";
    private static final String REQUEST_PASSWORD = "abcde1234^";

    @InjectMocks
    private JoinService joinService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthCodeRepository authCodeRepository;

    @DisplayName("회원가입 확인")
    @Test
    void save_join_success() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        JoinRequestDto request = new JoinRequestDto(REQUEST_USERNAME, REQUEST_EMAIL, REQUEST_PASSWORD);
        String encryptedPassword = encoder.encode(request.getPassword());

        when(userRepository.save(any())).thenReturn(
                new User(request.getUsername(), request.getEmail(), encryptedPassword));
        JoinResponseDto response = joinService.join(request);

        assertThat(response.getUsername()).isEqualTo(request.getUsername());
        assertThat(response.getCode()).containsPattern("^\\d{6}$");

    }

    @DisplayName("중복된 계정이면 회원가입 실패")
    @Test
    void duplicate_username_join_fail() {
        JoinRequestDto request = new JoinRequestDto(REQUEST_USERNAME, REQUEST_EMAIL, REQUEST_PASSWORD);
        when(userRepository.existsByUsername(any())).thenReturn(true);

        assertThatThrownBy(() -> joinService.join(request)).isInstanceOf(DuplicateUserException.class);
    }

}