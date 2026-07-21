package com.codewithbavly.ecommercebackend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    // For just now
    private static final String SECRET_KEY =
            "mySuperSecretKeyForJwtAuthentication12345678901234567890";

    private static final long JWT_EXPIRATION =
            1000 * 60 * 60 * 24; // 24 hours

//    @Value("${jwt.secret}")
//    private String secretKey;
//
//    @Value("${jwt.expiration}")
//    private long jwtExpiration;


    private SecretKey getSigningKey() {

        return Keys.hmacShaKeyFor(
                SECRET_KEY.getBytes(StandardCharsets.UTF_8)
        );
    }

    public String generateToken(UserDetails userDetails) {

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(
                        System.currentTimeMillis() + JWT_EXPIRATION))
                .signWith(getSigningKey())
                .compact();

    }

    public String extractUsername(String token) {

        return extractClaims(token).getSubject();

    }

    private Claims extractClaims(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

    }

    private boolean isTokenExpired(String token) {

        return extractClaims(token)
                .getExpiration()
                .before(new Date());

    }

    public boolean isTokenValid(
            String token,
            UserDetails userDetails) {

        String username = extractUsername(token);

        return username.equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }
}