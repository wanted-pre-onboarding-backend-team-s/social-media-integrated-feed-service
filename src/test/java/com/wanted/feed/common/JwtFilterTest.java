package com.wanted.feed.common;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.wanted.feed.common.response.JwtResponse;
import com.wanted.feed.common.util.TokenProvider;
import com.wanted.feed.exception.ErrorType;
import com.wanted.feed.user.domain.User;
import com.wanted.feed.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class JwtFilterTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private static final Long EXPIRED_MS = 3600000L;

    private static final String TOKEN_START_CHARACTERS = "Bearer ";

    private static final String AUTHORIZATION = "Authorization";

    @Value("${jwt.secret}")
    private String secretKey;

    private JwtResponse jwtResponse;

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

        this.jwtResponse = TokenProvider.createJwt(this.user.getId(), secretKey, EXPIRED_MS);
    }

    @DisplayName("정상 토큰 인증")
    @Test
    void test() throws Exception {
        mockMvc.perform(get("/test").header(AUTHORIZATION, TOKEN_START_CHARACTERS + jwtResponse.getToken()))
                .andExpect(status().isOk());
    }

    @DisplayName("토큰 미 입력 에러")
    @Test
    void nullToken() throws Exception {
        mockMvc.perform(get("/test")).andExpect(jsonPath("code").value(ErrorType.T001.getCode()));
    }

    @DisplayName("유효하지 않은 타입의 토큰 에러")
    @Test
    void invalidTypeOfToken() throws Exception {
        mockMvc.perform(get("/test").header(AUTHORIZATION, "Not " + TOKEN_START_CHARACTERS + jwtResponse.getToken()))
                .andExpect(jsonPath("code").value(ErrorType.T002.getCode()));
    }

    @DisplayName("만료된 토큰 에러")
    @Test
    void ExpiredToken() throws Exception {
        JwtResponse ExpiredJwtResponse = TokenProvider.createJwt(this.user.getId(), secretKey, EXPIRED_MS/EXPIRED_MS);
        mockMvc.perform(get("/test").header(AUTHORIZATION, TOKEN_START_CHARACTERS + ExpiredJwtResponse.getToken()))
                .andExpect(jsonPath("code").value(ErrorType.T003.getCode()));
    }

    @DisplayName("토큰 유효성 에러")
    @Test
    void invalidToken() throws Exception {
        mockMvc.perform(get("/test").header(AUTHORIZATION, TOKEN_START_CHARACTERS + TOKEN_START_CHARACTERS + jwtResponse.getToken()))
                .andExpect(jsonPath("code").value(ErrorType.T004.getCode()));
    }
}
