package de.neuefische.devquiz.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.sql.Date;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;

@Service
public class JWTUtilService {

    @Value("${de.neuefische.devquiz.jwt.secret}")
    private String JWT_SECRET;

    private long duration = 60 * 60 * 4 * 1000;

    public long getDuration() {
        return duration;
    }

    public String createToken(HashMap<String, Object> claims, String subject) {

        // Generate JWT
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plus(Duration.ofMillis(getDuration()))))
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET)
                .compact();
    }

    public String extractUsername(String token) {
        Claims claims = Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

}