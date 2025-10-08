package co.jp.enon.tms.common.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;

import javax.crypto.SecretKey;

/**
 * Utility class for generating, validating, and parsing JSON Web Tokens (JWTs).
 */
@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${tms.app.jwtSecret}")
    private String jwtSecretString;

    @Value("${tms.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    private SecretKey jwtSecretKey;

    /**
     * Initialize the SecretKey for signing JWTs.
     */

    @PostConstruct
    public void init() {
        try {
            // Use plain text secret
            jwtSecretKey = Keys.hmacShaKeyFor(jwtSecretString.getBytes(StandardCharsets.UTF_8));
        } catch (IllegalArgumentException e) {
            logger.error("Invalid JWT secret string. Must be at least 32 bytes for HS256: {}", e.getMessage());
            throw new IllegalArgumentException("JWT secret must be at least 32 bytes for HS256.", e);
        }
    }
    /**
     * Generate JWT token for authenticated user.
     */
    public String generateJwtToken(Authentication authentication) {
        var userPrincipal = (ImplementsUserDetails) authentication.getPrincipal(); // Use your actual UserDetails implementation
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(jwtSecretKey)
                .compact();
    }

    /**
     * Extract username from JWT token.
     */
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtSecretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Validate JWT token.
     */
    public boolean validateJwtToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(jwtSecretKey)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}
