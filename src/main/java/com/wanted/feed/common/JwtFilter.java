package com.wanted.feed.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanted.feed.common.util.TokenProvider;
import com.wanted.feed.exception.ErrorResponse;
import com.wanted.feed.exception.WantedException;
import com.wanted.feed.user.domain.UserRepository;
import com.wanted.feed.user.exception.InvalidTokenException;
import com.wanted.feed.user.exception.InvalidTypeOfTokenException;
import com.wanted.feed.user.exception.NullTokenException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private final String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(authorization == null) {
            log.error("[NullTokenException] ex");
            jwtExceptionHandler(response, new NullTokenException());
            return;
        } else if (!authorization.startsWith("Bearer ")) {
            log.error("[InvalidTypeOfTokenException] ex");
            jwtExceptionHandler(response, new InvalidTypeOfTokenException());
            return;
        }

        String token = authorization.split(" ")[1];

        try {
            TokenProvider.validationToken(token, secretKey);
        } catch (ExpiredJwtException e) {
            log.error("[ExpiredJwtException] ex", e);
            jwtExceptionHandler(response, e);
            return;
        } catch (Exception e) {
            log.error("[" + e.getClass().getSimpleName() + "] ex", e);
            jwtExceptionHandler(response, new InvalidTokenException());
            return;
        }

        Long userId = TokenProvider.getUserId(token, secretKey);
        log.debug("userId = {}", userId);
        if (!userRepository.existsById(userId)) {
            log.error("[NotFoundUserException] ex");
            jwtExceptionHandler(response, new InvalidTokenException());
            return;
        }

        request.setAttribute("userId", userId);
        filterChain.doFilter(request, response);
    }

    private void jwtExceptionHandler(HttpServletResponse response, Exception ex) {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            String json = "";
            if (ex instanceof WantedException e) {
                json = new ObjectMapper().writeValueAsString(ErrorResponse.of(e.getErrorType()));
            } else {
                json = new ObjectMapper().writeValueAsString(ErrorResponse.of("BAD_REQUEST", ex.getMessage()));
            }
            response.getOutputStream().write(json.getBytes());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
