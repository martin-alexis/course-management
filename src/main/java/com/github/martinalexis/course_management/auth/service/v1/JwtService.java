package com.github.martinalexis.course_management.auth.service.v1;

import io.jsonwebtoken.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Service responsible for JWT (JSON Web Token) operations.
 * 
 * This service handles:
 * - JWT token generation with custom claims
 * - Token validation and verification
 * - Token expiration checking
 * - User information extraction from tokens
 * - Access and refresh token management
 * 
 * The service uses the JJWT library for token operations and supports
 * both access tokens (short-lived) and refresh tokens (long-lived).
 * 
 * @author Alexis Martin
 * @version 1.0
 */
@Service
@Slf4j
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Value("${jwt.refresh-token.expiration}")
    private long refreshExpiration;

    /**
     * Generates a JWT token with custom claims for a user.
     * 
     * This method creates a JWT token containing:
     * - Custom claims provided in the extraClaims parameter
     * - User information (username, authorities)
     * - Issued and expiration timestamps
     * - Digital signature using HMAC-SHA256
     * 
     * @param extraClaims additional claims to include in the token
     * @param userDetails user information for token generation
     * @return JWT token string
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    /**
     * Generates a standard JWT access token for a user.
     * 
     * Creates a token with:
     * - User's username as subject
     * - Standard claims (issued at, expiration)
     * - HMAC-SHA256 signature
     * - Default expiration time from configuration
     * 
     * @param userDetails user information for token generation
     * @return JWT access token string
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Generates a refresh token for a user.
     * 
     * Refresh tokens have:
     * - Longer expiration time than access tokens
     * - Same user information and signature
     * - Used for obtaining new access tokens
     * 
     * @param userDetails user information for token generation
     * @return JWT refresh token string
     */
    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, refreshExpiration);
    }

    /**
     * Builds a JWT token with specified parameters.
     * 
     * This private method handles the actual token construction:
     * - Sets claims and subject
     * - Adds timestamps
     * - Applies digital signature
     * - Returns compact token string
     * 
     * @param extraClaims additional claims to include
     * @param userDetails user information
     * @param expiration token expiration time in milliseconds
     * @return constructed JWT token
     */
    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), Jwts.SIG.HS256)
                .compact();
    }

    /**
     * Generates the signing key for JWT operations.
     * 
     * Creates a secret key from the configured JWT secret:
     * - Converts secret string to bytes
     * - Creates HMAC-SHA256 compatible key
     * - Used for signing and verifying tokens
     * 
     * @return SecretKey for JWT operations
     */
    private SecretKey getSignInKey() {
        byte[] keyBytes = secretKey.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Validates if a JWT token is valid for a specific user.
     * 
     * Performs comprehensive token validation:
     * - Verifies digital signature
     * - Checks token expiration
     * - Validates username matches user details
     * - Handles JWT parsing exceptions
     * 
     * @param token JWT token to validate
     * @param userDetails user details to validate against
     * @return true if token is valid, false otherwise
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String username = claims.getSubject();
            return username.equals(userDetails.getUsername());
        } catch (JwtException e) {
            log.error("Token inválido: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Extracts all claims from a JWT token.
     * 
     * Parses the token and returns all contained claims:
     * - Subject (username)
     * - Issued at timestamp
     * - Expiration timestamp
     * - Custom claims
     * 
     * @param token JWT token to parse
     * @return Claims object containing all token information
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Extracts the user ID from a JWT token.
     * 
     * The user ID is stored as the token subject and represents:
     * - The primary identifier for the authenticated user
     * - Used for user lookup and authentication
     * - Corresponds to the user's database ID
     * 
     * @param token JWT token to extract user ID from
     * @return user ID as string
     */
    public String extractIdUser(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts a specific claim from a JWT token.
     * 
     * Generic method for claim extraction that allows:
     * - Custom claim resolution logic
     * - Type-safe claim extraction
     * - Flexible token processing
     * 
     * @param token JWT token to extract claim from
     * @param claimsResolver function to resolve the specific claim
     * @param <T> type of the claim value
     * @return extracted claim value
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Checks if a JWT token has expired.
     * 
     * Compares the token's expiration timestamp with current time:
     * - Returns true if token is expired
     * - Returns false if token is still valid
     * - Used for token validation and refresh logic
     * 
     * @param token JWT token to check
     * @return true if expired, false if valid
     */
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extracts the expiration timestamp from a JWT token.
     * 
     * @param token JWT token to extract expiration from
     * @return Date object representing expiration time
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Gets the configured JWT expiration time.
     * 
     * Returns the expiration time configured in application properties:
     * - Used for token generation
     * - Configurable per environment
     * - Typically 24 hours for access tokens
     * 
     * @return JWT expiration time in milliseconds
     */
    public long getJwtExpiration() {
        return jwtExpiration;
    }
}