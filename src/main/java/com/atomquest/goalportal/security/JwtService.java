package com.atomquest.goalportal.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {
    private static final String SECRET = "mysecretkeymysecretkeymysecretkey12345";

    private final SecretKey key= Keys.hmacShaKeyFor(SECRET.getBytes());

    public String generateToken(String email){
        return Jwts.builder()
                .subject(email).
                issuedAt(new Date()).
                expiration(
                        new Date(System.currentTimeMillis()+86400000))
                .signWith(key).compact();
    }

    public String extractEmail(String token){

        Claims claims = Jwts.parser()
                .verifyWith(key).build().parseSignedClaims(token).getPayload();

        return claims.getSubject();
    }
}
