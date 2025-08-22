package com.NtgSummerTrainingApp.PersonalFinanceTracker.Services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import javax.crypto.SecretKey;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class JwtService {
    @Value("${jwt.expiration}")
    private long jwtExpiration;
    private final SecretKey  signingKey;
    public JwtService(@Value("${jwt.secret}") String secret) {
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes());
    }
    /**
     * Generate a JWT token with only username (subject)
     */

    public String generateToken(String username ){
        Map<String, Objects> claims = new HashMap<>();
        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+jwtExpiration))
                .signWith(signingKey)
                .compact();

    }


    /**
     * Extract username (subject) from token
     */
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }


    /**
     * Validate token against username and expiration
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }


    private boolean isTokenExpired(String token) {
        Date expiration = extractAllClaims(token).getExpiration();
        return expiration.before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()                      // start parsing
                .verifyWith(signingKey)           // verify using your key
                .build()                          // build the parser
                .parseSignedClaims(token)         // parse token
                .getPayload();                    // get claims payload
    }
}
