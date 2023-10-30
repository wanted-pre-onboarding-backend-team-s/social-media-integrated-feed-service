package com.wanted.feed.user.service;

import com.wanted.feed.common.response.JwtResponse;
import com.wanted.feed.common.util.TokenProvider;
import com.wanted.feed.user.domain.User;
import com.wanted.feed.user.domain.UserRepository;
import com.wanted.feed.user.dto.LoginRequestDto;
import com.wanted.feed.user.exception.MismatchPasswordException;
import com.wanted.feed.user.exception.NotFoundUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expired-ms}")
    public Long expiredMs;

    @Transactional(readOnly = true)
    public User getAuthenticatedByLogin(LoginRequestDto loginRequestDto) {
        User user = userRepository.findByUsername(loginRequestDto.getUsername())
            .orElseThrow(NotFoundUserException::new);
        String encryptedPassword = user.getPassword();

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), encryptedPassword)) {
            throw new MismatchPasswordException();
        }
        return user;
    }

    public JwtResponse getLoginAuthorization(User user) {
        return TokenProvider.createJwt(user.getId(), secretKey, expiredMs);
    }
}
