package com.wanted.feed.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.xml.bind.DatatypeConverter;
import java.util.Date;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TokenProvider {

    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;

    public static Long getUserId(String token, String secretKey) {
        return verifyToken(token, secretKey).getBody().get("userId", Long.class);
    }

    public static Jws<Claims> verifyToken(String token, String secretKey) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey(secretKey)).build().parseClaimsJws(token);
    }

    public static String createJwt(Long userId, String secretKey, Long expiredMs) {

        Claims claims = Jwts.claims();
        claims.put("userId", userId);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(getSigningKey(secretKey), SIGNATURE_ALGORITHM)
                .compact();
    }

    private static SecretKeySpec getSigningKey(String secretKey) {
        return new SecretKeySpec(DatatypeConverter.parseBase64Binary(secretKey),
                SIGNATURE_ALGORITHM.getJcaName());
    }
}
