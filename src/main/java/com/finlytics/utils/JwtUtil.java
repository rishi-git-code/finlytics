package com.finlytics.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.security.Key;

@Component
public class JwtUtil {

//    private static final Key key =Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final String SECRET = "your-256-bit-secret-key-should-be-shared-between-services"; // store securely
    private static final Key key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    private static final long EXPIRATION_TIME = 86400000; // 1 day
    private static final SignatureAlgorithm ALGORITHM = SignatureAlgorithm.HS384;


    public static String generateToken(String username){
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key,ALGORITHM)
                .compact();
    }

    public static String extractUserId(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
