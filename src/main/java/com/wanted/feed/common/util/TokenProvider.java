package com.wanted.feed.common.util;

import com.wanted.feed.common.response.JwtResponse;
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

    public static JwtResponse createJwt(Long userId, String secretKey, Long expiredMs) {

        Date issuedTime = new Date(System.currentTimeMillis());
        Date expiredTime = new Date(System.currentTimeMillis() + expiredMs);

        Claims claims = Jwts.claims();
        claims.put("userId", userId);

        return JwtResponse.builder()
                .token(Jwts.builder()
                        .setClaims(claims)
                        .setIssuedAt(issuedTime)
                        .setExpiration(expiredTime)
                        .signWith(getSigningKey(secretKey), SIGNATURE_ALGORITHM)
                        .compact())
                .issuedTime(String.valueOf(issuedTime))
                .expiredTime(String.valueOf(expiredTime))
                .build();
    }

    private static SecretKeySpec getSigningKey(String secretKey) {
        return new SecretKeySpec(DatatypeConverter.parseBase64Binary(secretKey),
                SIGNATURE_ALGORITHM.getJcaName());
    }
}
