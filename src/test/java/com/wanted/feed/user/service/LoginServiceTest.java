package com.wanted.feed.user.service;

import com.wanted.feed.common.response.JwtResponse;
import com.wanted.feed.common.util.TokenProvider;
import com.wanted.feed.user.domain.User;
import com.wanted.feed.user.domain.UserRepository;
import com.wanted.feed.user.dto.LoginRequestDto;
import com.wanted.feed.user.exception.NotFoundUserException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class LoginServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoginService loginService;

    @Value("${jwt.secret}")
    private String secretKey;

    private User user;

    @BeforeEach
    void init() {

        userRepository.deleteAll();

        User saveUser = User.builder()
                .username("user1")
                .password("1234")
                .email("user1@wanted.com")
                .build();
        this.user = userRepository.save(saveUser);
    }

    @DisplayName("JWT발급")
    @Test
    void getLoginAuthorization() {

        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                .username("user1")
                .password("1234")
                .build();
        JwtResponse jwtResponse = loginService.getLoginAuthorization(
                loginService.getAuthenticatedByLogin(loginRequestDto));
        Long userId = TokenProvider.getUserId(jwtResponse.getToken(), secretKey);
        Assertions.assertThat(userId).isEqualTo(user.getId());
    }

    @Test
    @DisplayName("JWT 발급 에러 - 잘못된 아이디")
    void invalidId() {
        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                .username("user2")
                .password("1234")
                .build();
        Assertions.assertThatThrownBy(() -> loginService.getAuthenticatedByLogin(loginRequestDto))
                .isInstanceOf(NotFoundUserException.class);
    }

    @Test
    @DisplayName("JWT 발급 에러 - 잘못된 패스워드")
    void invalidPassword() {
        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                .username("user1")
                .password("5678")
                .build();
        Assertions.assertThatThrownBy(() -> loginService.getAuthenticatedByLogin(loginRequestDto))
                .isInstanceOf(NotFoundUserException.class);
    }
}
