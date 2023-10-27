package com.wanted.feed.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.wanted.feed.user.domain.AuthCode;
import com.wanted.feed.user.domain.AuthCodeRepository;
import com.wanted.feed.user.domain.User;
import com.wanted.feed.user.domain.UserRepository;
import com.wanted.feed.user.dto.ApprovalRequestDto;
import com.wanted.feed.user.dto.JoinRequestDto;
import com.wanted.feed.user.dto.JoinResponseDto;
import com.wanted.feed.user.exception.DuplicateUserException;
import com.wanted.feed.user.exception.MismatchPasswordException;
import com.wanted.feed.user.exception.NotFoundUsernameException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;


@ExtendWith(MockitoExtension.class)
class JoinServiceTest {

    private static final String REQUEST_USERNAME = "wanted";
    private static final String REQUEST_EMAIL = "wanted@wanted.com";
    private static final String REQUEST_PASSWORD = "abcde1234^";
    private static final String REQUEST_AUTHCODE = "123456";

    @InjectMocks
    private JoinService joinService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthCodeRepository authCodeRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @DisplayName("회원가입 확인")
    @Test
    void save_join_success() {
        JoinRequestDto request = new JoinRequestDto(REQUEST_USERNAME, REQUEST_EMAIL, REQUEST_PASSWORD);
        String encryptedPassword = passwordEncoder.encode(request.getPassword());

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

    @DisplayName("가입승인 성공")
    @Test
    void approve_success() {
        ApprovalRequestDto request = new ApprovalRequestDto(REQUEST_USERNAME, REQUEST_PASSWORD, REQUEST_AUTHCODE);
        User user = new User(REQUEST_USERNAME, REQUEST_EMAIL, REQUEST_PASSWORD);
        AuthCode code = new AuthCode(REQUEST_AUTHCODE, REQUEST_USERNAME);

        when(userRepository.findByUsernameAndApproved(any(String.class), any(Boolean.class))).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(any(String.class), any(String.class))).thenReturn(true);
        when(authCodeRepository.findTopByUsernameOrderByCreatedAtDesc(any(String.class))).thenReturn(Optional.of(code));

        joinService.approve(request);

        verify(userRepository, times(1)).findByUsernameAndApproved(any(String.class), any(Boolean.class));
        verify(passwordEncoder, times(1)).matches(any(String.class), any(String.class));
        verify(authCodeRepository, times(1)).findTopByUsernameOrderByCreatedAtDesc(any(String.class));
    }

    @DisplayName("계정을 찾을 수 없을때 가입승인 실패")
    @Test
    void not_found_username_approve_fail() {
        ApprovalRequestDto request = new ApprovalRequestDto(REQUEST_USERNAME, REQUEST_PASSWORD, REQUEST_AUTHCODE);

        when(userRepository.findByUsernameAndApproved(any(String.class), any(Boolean.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> joinService.approve(request)).isInstanceOf(NotFoundUsernameException.class);
    }

    @DisplayName("계정에 대한 비밀번호가 맞지 않을때 가입승인 실패")
    @Test
    void mismatch_password_approve_fail() {
        ApprovalRequestDto request = new ApprovalRequestDto(REQUEST_USERNAME, REQUEST_PASSWORD, REQUEST_AUTHCODE);
        User user = new User(REQUEST_USERNAME, REQUEST_EMAIL, REQUEST_PASSWORD);

        when(userRepository.findByUsernameAndApproved(any(String.class), any(Boolean.class))).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(any(String.class), any(String.class))).thenReturn(false);

        assertThatThrownBy(() -> joinService.approve(request)).isInstanceOf(MismatchPasswordException.class);
    }

}