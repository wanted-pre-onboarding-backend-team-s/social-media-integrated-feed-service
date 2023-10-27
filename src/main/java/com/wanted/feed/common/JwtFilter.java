package com.wanted.feed.common;

import com.wanted.feed.common.util.TokenProvider;
import com.wanted.feed.user.domain.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private final String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.debug("authentication = {}", authorization);

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            log.error("[InvalidAuthenticationException] ex");
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization.split(" ")[1];

        TokenProvider.validationToken(token, secretKey);

        Long userId = TokenProvider.getUserId(token, secretKey);
        log.debug("userId = {}", userId);
        if (!userRepository.existsById(userId)) {
            log.error("[NotFoundUserException] ex");
            filterChain.doFilter(request, response);
            return;
        }

        request.setAttribute("userId", userId);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userId, null);

        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
