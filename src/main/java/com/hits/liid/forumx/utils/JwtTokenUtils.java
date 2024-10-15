package com.hits.liid.forumx.utils;

import com.hits.liid.forumx.entity.UserEntity;
import com.hits.liid.forumx.errors.GetUserIdFromAuthenticationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class JwtTokenUtils {

    @Value("${spring.jwt.secret}")
    private String secret;

    @Value("${spring.jwt.access_lifetime}")
    private Duration jwtLifetime;

    public String generateToken(UserEntity user){
        Map<String, Object> claims = new HashMap<>();

        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + jwtLifetime.toMillis());

        claims.put("userId", user.getId().toString());

        return Jwts.builder()
                .claims(claims)
//                .subject(user.getEmail())
                .issuedAt(issuedDate)
                .expiration(expiredDate)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    @SneakyThrows
    public UUID getUserIdFromAuthentication(Authentication authentication) {
        UUID userId;
        try {
            userId = UUID.fromString(authentication.getName());
        } catch (Exception e) {
            throw new GetUserIdFromAuthenticationException("Authentication not contain userId");
        }
        return userId;
    }

    public UUID getUserIdFromToken(String token){
        String userId = getAllClaimsFromToken(token).get("userId", String.class);
        return UUID.fromString(userId);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
// comment from main