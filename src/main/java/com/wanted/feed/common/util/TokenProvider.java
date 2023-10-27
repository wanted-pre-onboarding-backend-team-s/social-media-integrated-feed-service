package com.wanted.feed.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TokenProvider {

    public static Long getUserId(String token, String secretKey) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token)
                .getBody().get("userId", Long.class);
    }

    public static void validationToken(String token, String secretKey) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
        } catch (SecurityException | MalformedJwtException e) {
            log.error("[" + e.getClass().getName() + "] ex", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("[ExpiredJwtException] ex", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("[UnsupportedJwtException] ex", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("[IllegalArgumentException] ex", e.getMessage());
        }
    }

    public static String createJwt(Long userId, String secretKey, Long expiredMs) {

        Claims claims = Jwts.claims();
        claims.put("userId", userId);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}
