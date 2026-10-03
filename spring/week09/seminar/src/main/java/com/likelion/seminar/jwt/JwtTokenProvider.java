package com.likelion.seminar.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtTokenProvider {

    private static final String ISSUER = "likelion-seminar";

    private final SecretKey key;
    private final long accessSeconds;
    private final long refreshSeconds;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-seconds}") long accessSeconds,
            @Value("${jwt.refresh-seconds}") long refreshSeconds
    ) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.accessSeconds = accessSeconds;
        this.refreshSeconds = refreshSeconds;
    }

    public String createAccessToken(String username) {
        return createToken(username, "access", accessSeconds);
    }

    public String createRefreshToken(String username) {
        return createToken(username, "refresh", refreshSeconds);
    }

    private String createToken(
            String username,
            String type,
            long seconds
    ) {
        Instant now = Instant.now();

        return Jwts.builder()
                .issuer(ISSUER)
                .subject(username)
                .id(UUID.randomUUID().toString())
                .claim("tokenType", type)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(seconds)))
                .signWith(key)
                .compact();
    }

    public String verifyAndGetUsername(
            String token,
            String expectedType
    ) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .requireIssuer(ISSUER)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        if (!expectedType.equals(claims.get("tokenType", String.class))) {
            throw new JwtException("토큰 종류가 올바르지 않습니다.");
        }

        if (claims.getSubject() == null
                || claims.getSubject().isBlank()
                || claims.getExpiration() == null) {
            throw new JwtException("필수 토큰 정보가 없습니다.");
        }

        return claims.getSubject();
    }
}