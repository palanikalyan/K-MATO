package com.foodordering.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    private SecretKey getSigningKey() {
        // Support either a base64-encoded secret (prefix with "base64:") or a plain text secret.
        byte[] keyBytes;
        try {
            if (jwtSecret != null && jwtSecret.startsWith("base64:")) {
                String b64 = jwtSecret.substring("base64:".length());
                keyBytes = Decoders.BASE64.decode(b64);
                return Keys.hmacShaKeyFor(keyBytes);
            }

            keyBytes = jwtSecret == null ? new byte[0] : jwtSecret.getBytes(StandardCharsets.UTF_8);

            // HS512 requires a key size >= 512 bits (64 bytes). If the provided secret is shorter,
            // derive a 512-bit key deterministically using SHA-512 of the provided secret so tokens
            // remain valid across restarts. For production, prefer a securely-generated base64 key
            // produced by Keys.secretKeyFor(SignatureAlgorithm.HS512).
            if (keyBytes.length < 64) {
                try {
                    MessageDigest md = MessageDigest.getInstance("SHA-512");
                    byte[] digest = md.digest(keyBytes);
                    return Keys.hmacShaKeyFor(digest);
                } catch (NoSuchAlgorithmException e) {
                    // Fallback: use the raw bytes (this will likely fail later with an informative exception)
                    logger.warn("SHA-512 not available to expand JWT secret; using raw secret bytes", e);
                    return Keys.hmacShaKeyFor(keyBytes);
                }
            }

            return Keys.hmacShaKeyFor(keyBytes);
        } catch (IllegalArgumentException ex) {
            // Provide a clearer error message when key decoding/creation fails
            logger.error("Invalid JWT secret configuration - key creation failed", ex);
            throw ex;
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    public String generateToken(Authentication authentication) {
        String email = authentication.getName();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            logger.error("JWT validation failed: " + e.getMessage(), e);
            return false;
        }
    }
}
