package com.wanted.feed.user.service;

import com.wanted.feed.common.util.TokenProvider;
import com.wanted.feed.user.exception.NotFoundUserException;
import com.wanted.feed.user.domain.User;
import com.wanted.feed.user.domain.UserRepository;
import com.wanted.feed.user.dto.LoginRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expired-ms}")
    public Long expiredMs;

    public User getAuthenticatedByLogin (LoginRequestDto loginRequestDto) {
        return userRepository.findByUsernameAndPassword(loginRequestDto.getUsername(),
                loginRequestDto.getPassword()).orElseThrow(NotFoundUserException::new);
    }

    @Transactional
    public String getLoginAuthorization (LoginRequestDto loginRequestDto) {
        return TokenProvider.createJwt(getAuthenticatedByLogin(loginRequestDto).getId(), secretKey, expiredMs);
    }
}
